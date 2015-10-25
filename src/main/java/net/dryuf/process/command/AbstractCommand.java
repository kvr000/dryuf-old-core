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

import java.util.Map;

import net.dryuf.core.CallerContext;
import net.dryuf.core.UiContext;


public abstract class AbstractCommand extends java.lang.Object implements Command
{
	public void			setCommandRunner(CommandRunner commandRunner)
	{
		this.commandRunner = commandRunner;
		this.callerContext = commandRunner.getCallerContext();
	}

	protected abstract GetOptions	getOptionsDefinition();

	protected abstract Map<String, Object> getOptions();

	protected String		validateArguments()
	{
		return null;
	}

	public boolean			checkOptionHelp()
	{
		return getOptions().containsKey("h");
	}

	public String			parseArguments(String[] args)
	{
		String error;
		if ((error = getOptionsDefinition().parseArguments(getCallerContext(), getOptions(), args)) != null)
			return error;
		if ((error = validateArguments()) != null)
			return error;
		return checkOptionHelp() ? "" : null;
	}

	public int			reportUsage(String reason)
	{
		return reason.equals("") ? 0 : 127;
	}

	protected UiContext		getUiContext()
	{
		return getCallerContext().getUiContext();
	}

	protected CommandRunner		commandRunner;

	public CommandRunner		getCommandRunner()
	{
		return this.commandRunner;
	}

	protected CallerContext		callerContext;

	public CallerContext		getCallerContext()
	{
		return this.callerContext;
	}
}
