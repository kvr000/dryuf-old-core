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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dryuf.config.ValueConfig;
import net.dryuf.core.CallerContext;


public abstract class ThreadedChildrenSubProcess extends AbstractSyncSubProcess
{
	public				ThreadedChildrenSubProcess()
	{
	}

	/**
	 * Finishes the process, terminates children.
	 */
	public void			finish()
	{
		for (Thread child: children.keySet()) {
			child.interrupt();
		}
		while (!children.isEmpty()) {
			Thread t = children.keySet().iterator().next();
			try {
				t.wait();
			}
			catch (InterruptedException e) {
				continue;
			}
			finishChild(t);
		}
	}

	/**
	 * Adds running child.
	 */
	protected void			addChild(Thread child, Object work)
	{
		children.put(child, work);
	}

	/**
	 * Finishes the terminated child.
	 */
	protected void			finishChild(Thread child)
	{
		finishWork(children.remove(child));
	}

	/**
	 * Finishes the work from terminated child.
	 */
	protected void			finishWork(Object o)
	{
	}

	/**
	 * Processes the terminated children, deregistering them.
	 */
	protected void			processChildren()
	{
		while (!finishedChildren.isEmpty()) {
			Thread t;
			synchronized (finishedChildren) {
				t = finishedChildren.remove(0);
			}
			finishChild(t);
		}
	}

	/**
	 * Notifies about finished child.
	 */
	public void			notifyChildFinished(Thread child)
	{
		synchronized (finishedChildren) {
			finishedChildren.add(child);
		}
		runThread.interrupt();
	}

	protected Map<Thread, Object>	children = new HashMap<Thread, Object>();
	protected List<Thread>		finishedChildren = new LinkedList<Thread>();
};
