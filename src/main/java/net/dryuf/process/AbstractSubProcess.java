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
import net.dryuf.core.AppContainer;
import net.dryuf.core.CallerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;


public abstract class AbstractSubProcess extends java.lang.Object implements SubProcess
{
	public				AbstractSubProcess()
	{
	}

	@Override
	public void                     setConfig(ValueConfig config)
	{
		this.config = config;
	}

	@Override
	public void			init()
	{
		callerContext = appContainer.createCallerContext();
	}

	/**
	 * Prepares process within the running thread.
	 */
	public void			prepare()
	{
	}

	/**
	 * Finishes the process.
	 */
	public void			finish()
	{
	}

	protected ValueConfig		config;

	@Inject
	protected AppContainer          appContainer;

	protected CallerContext         callerContext;

	protected Logger		logger = LogManager.getLogger(this.getClass());
}
