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

package net.dryuf.process.command;

import net.dryuf.core.CallerContext;


public abstract class AbstractCommandRunner extends java.lang.Object implements CommandRunner
{
	public				AbstractCommandRunner(CallerContext callerContext)
	{
		this.callerContext = callerContext;
	}

	public int			run(Command command, String[] arguments)
	{
		command.setCommandRunner(this);
		String err;
		if ((err = command.parseArguments(arguments)) != null)
			return command.reportUsage(!err.isEmpty() ? err+"\n" : err);
		return command.process();
	}

	public int			runNew(Class<? extends Command> commandClass, String[] arguments)
	{
		Command command;
		try {
			command = getCallerContext().createBeaned(commandClass, null);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return run(command, arguments);
	}

	protected CallerContext		callerContext;

	public CallerContext		getCallerContext()
	{
		return this.callerContext;
	}

	public void			setCallerContext(CallerContext callerContext_)
	{
		this.callerContext = callerContext_;
	}
}
