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

import net.dryuf.config.ValueConfig;
import net.dryuf.core.CallerContext;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;


public abstract class AbstractSyncSubProcess extends AbstractSubProcess
{
	public				AbstractSyncSubProcess()
	{
	}

	@Override
	public ListenableFuture<?> start()
	{
		ListenableFutureTask<?> future = new ListenableFutureTask<Object>(AbstractSyncSubProcess.this::run, null);
		new Thread(future).start();
		return future;
	}

	public void			run()
	{
		// We set runThread from both start() and run() whichever runs
		// first.
		// The reason for this is that we may be terminated by both
		// creator or internal child.
		runThread = Thread.currentThread();
		prepare();
		try {
			loop();
		}
		finally {
			finish();
		}
	}

	/**
	 * Runs the process loop.
	 */
	public abstract void		loop();

	/**
	 * Sleeps safely, checking for InterruptedException.
	 */
	public boolean			safeSleep(long timeout)
	{
		try {
			Thread.sleep(timeout);
			return true;
		}
		catch (InterruptedException ex) {
			return false;
		}
	}

	protected Thread 		runThread = null;
}
