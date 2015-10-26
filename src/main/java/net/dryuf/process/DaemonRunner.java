/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.process;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import net.dryuf.config.IniConfig;
import net.dryuf.config.ValueConfig;
import net.dryuf.core.AppContainer;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.textual.TextualManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;
import sun.misc.Signal;


public class DaemonRunner extends java.lang.Object
{
	public static final long	RESTART_RETRY = 5000;

	public DaemonRunner             setUseSignalHandlers(boolean useSignalHandlers)
	{
		this.useSignalHandlers = useSignalHandlers;
		return this;
	}

	public ListenableFuture<Void>   startDaemon(AppContainer appContainer, String[] args, String profileConfig, String sectionName)
	{
		ListenableFutureTask<Void> task = new ListenableFutureTask<Void>(() -> {
			runDaemon(appContainer, args, profileConfig, sectionName);
		}, null);
		mainThread = new Thread(task);
		mainThread.start();
		return task;
	}

	public int			runDaemon(AppContainer appContainer, String[] args, String profileConfig, String sectionName)
	{
		try {
			mainThread = Thread.currentThread();

			if (useSignalHandlers) {
				Signal.handle(new Signal("HUP"), (Signal signal) -> handleReconfigureSignal());
				Signal.handle(new Signal("TERM"), (Signal signal) -> handleTermSignal());
				Signal.handle(new Signal("INT"), (Signal signal) -> handleTermSignal());
			}

			this.appContainer = appContainer;
			this.callerContext = appContainer.createCallerContext();
			iniConfig = callerContext.getBeanTyped(profileConfig, IniConfig.class);

			this.processesSection = iniConfig.getSection(sectionName);

			do {
				long nextEvent;
				synchronized (this) {
					if (reconfigure) {
						reconfigure = false;
						doReconfigure();
					}
					Runnable runnable;
					while ((runnable = eventQueue.poll()) != null)
						runnable.run();
					nextEvent = startProcesses();
				}
				try {
					if ((nextEvent -= System.currentTimeMillis()) > 0) {
						logger.debug("Sleeping for "+nextEvent+" ms");
						Thread.sleep(nextEvent);
					}
				}
				catch (InterruptedException e) {
				}
			} while (!terminating);
			this.endProcesses();
			return 0;
		}
		catch (RuntimeException ex) {
			logger.fatal("Error running daemon", ex);
			throw ex;
		}
	}

	public void			handleTermSignal()
	{
		terminating = true;
		mainThread.interrupt();
	}

	public void                     handleReconfigureSignal()
	{
		reconfigure = true;
		mainThread.interrupt();
	}

	protected void			doReconfigure()
	{
		Set<String> newNames = new LinkedHashSet<>();
		Set<String> waitList = new LinkedHashSet<>();
		for (String processName: processesSection.getValueMandatory("processes").split(" +")) {
			if (processName.length() == 0)
				continue;
			ValueConfig processSection = iniConfig.getSection(processName);
			if (processSection.getTextualDefault("disabled", TextualManager.createTextual(net.dryuf.textual.BoolSwitchTextual.class, callerContext), false))
				continue;
			newNames.add(processName);
		}
		for (Map.Entry<String, SubProcessInfo> processInfoEntry: subProcesses.entrySet()) {
			SubProcessInfo subProcess = processInfoEntry.getValue();
			ValueConfig processSection = iniConfig.getSection(processInfoEntry.getKey());
			if (!newNames.contains(processInfoEntry.getKey()) || (subProcess.cachedConfig != null && !subProcess.cachedConfig.equals(processSection.asMap()))) {
				if (!newNames.contains(processInfoEntry.getKey())) {
					subProcess.toRemove = true;
				}
				else {
					subProcess.nextRestart = System.currentTimeMillis();
				}
				processInfoEntry.getValue().exitFuture.cancel(true);
				waitList.add(processInfoEntry.getKey());
			}
		}
		for (String processName: newNames) {
			SubProcessInfo subProcess = subProcesses.get(processName);
			if (subProcess == null) {
				subProcesses.put(processName, new SubProcessInfo()
					{{
						this.name = processName;
						this.config = iniConfig.getSection(processName);
						this.nextRestart = System.currentTimeMillis();
					}}
				);
			}
			else {
				subProcess.toRemove = false;
			}
		}
	}

	protected SubProcess		initSubProcess(String processName)
	{
		ValueConfig processSection = iniConfig.getSection(processName);
		if (processSection.getTextualDefault("disabled", TextualManager.createTextual(net.dryuf.textual.BoolSwitchTextual.class, callerContext), false))
			return null;
		@SuppressWarnings("unchecked")
		Class<? extends SubProcess> processClass = (Class<? extends SubProcess>) Dryuf.loadClass(processSection.getValueMandatory("class"));
		SubProcess subProcess = callerContext.createBeaned(processClass, null);
		subProcess.setConfig(processSection);
		subProcess.init();
		return subProcess;
	}

	protected long			startProcesses()
	{
		long nextEvent = Long.MAX_VALUE;
		for (Iterator<Map.Entry<String, SubProcessInfo>> subProcessIterator = subProcesses.entrySet().iterator(); subProcessIterator.hasNext(); ) {
			SubProcessInfo subProcess = subProcessIterator.next().getValue();
			if (subProcess.exitFuture != null) {
				continue;
			}
			if (subProcess.toRemove) {
				subProcessIterator.remove();
				continue;
			}
			if (subProcess.nextRestart > System.currentTimeMillis()) {
				if (subProcess.nextRestart < nextEvent)
					nextEvent = subProcess.nextRestart;
				continue;
			}
			startSubProcess(subProcess);
		}
		return nextEvent;
	}

	protected void			startSubProcess(SubProcessInfo subProcess)
	{
		subProcess.subProcess = initSubProcess(subProcess.name);
		if (subProcess.subProcess != null) {
			subProcess.cachedConfig = subProcess.config.asMap();
			subProcess.exitFuture = subProcess.subProcess.start();
			subProcess.exitFuture.addCallback(new ListenableFutureCallback<Object>() {
				@Override
				public void onFailure(Throwable throwable) {
					handleProcessExit(subProcess);
				}

				@Override
				public void onSuccess(Object o)
				{
					handleProcessExit(subProcess);
				}
			});
		}
		subProcess.nextRestart = System.currentTimeMillis()+getSubProcessRestartTimeout(subProcess);
	}

	protected synchronized void	handleProcessExit(SubProcessInfo subProcess)
	{
		eventQueue.add(() -> {
			waitProcess(subProcess);
		});
		mainThread.interrupt();
	}

	protected long			getSubProcessRestartTimeout(SubProcessInfo subProcess)
	{
		return Long.valueOf(subProcess.config.getValueDefault("restart", String.valueOf(RESTART_RETRY)));
	}

	protected void			waitProcess(SubProcessInfo subProcess)
	{
		while (subProcess.exitFuture != null) {
			try {
				subProcess.exitFuture.get();
				logger.error("Subprocess "+subProcess.name+" exited.");
			}
			catch (ExecutionException ex) {
				logger.error("Subprocess "+subProcess.name+" died with exception.", ex.getCause());
			}
			catch (CancellationException e) {
				logger.error("Subprocess "+subProcess.name+" exited due to request.");
			}
			catch (InterruptedException e) {
				continue;
			}
			subProcess.exitFuture = null;
			long dieRestart = System.currentTimeMillis()+Long.valueOf(subProcess.config.getValueDefault("dietime", "0"));
			if (subProcess.nextRestart < dieRestart)
				subProcess.nextRestart = dieRestart;
		}
	}

	protected void			endProcesses()
	{
		for (SubProcessInfo subProcess: subProcesses.values()) {
			if (subProcess.exitFuture != null)
				subProcess.exitFuture.cancel(true);
		}
		for (SubProcessInfo subProcess: subProcesses.values()) {
			if (subProcess.exitFuture != null)
				waitProcess(subProcess);
		}
	}

	protected class SubProcessInfo
	{
		public Map<String, String>	cachedConfig;

		public ValueConfig		config;

		public String			name;

		public SubProcess		subProcess;

		public ListenableFuture<?>      exitFuture;

		public long			nextRestart;

		public boolean                  toRemove;
	}

	protected AppContainer		appContainer;

	protected CallerContext		callerContext;

	protected IniConfig		iniConfig;

	protected ValueConfig		processesSection;

	protected boolean               useSignalHandlers = true;

	protected volatile boolean	terminating = false;

	protected volatile boolean	reconfigure = true;

	protected Thread		mainThread;

	protected BlockingQueue<Runnable> eventQueue = new LinkedBlockingQueue<>();

	protected Map<String, SubProcessInfo> subProcesses = new LinkedHashMap<>();

	protected Logger		logger = LogManager.getLogger(getClass());
}
