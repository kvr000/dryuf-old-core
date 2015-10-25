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

package net.dryuf.dao.spring;

import org.springframework.transaction.PlatformTransactionManager;

import net.dryuf.core.AppContainer;
import net.dryuf.core.AppContainerAware;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public class SpringTransactionManager extends java.lang.Object implements net.dryuf.transaction.TransactionManager, AppContainerAware
{
	public				SpringTransactionManager()
	{
	}

	public void			setName(String name)
	{
		this.name = name;
	}

	public void			setSpringTransactionManager(PlatformTransactionManager platformTransactionManager)
	{
		this.platformTransactionManager = platformTransactionManager;
	}

	@Override
	public void			afterAppContainer(AppContainer appContainer)
	{
		if (name == null)
			throw new NullPointerException("name");
		if (platformTransactionManager == null)
			throw new NullPointerException("springTransactionManager");
	}

	@Override
	public TransactionHandler	openTransaction(boolean readOnly)
	{
		return new SpringPlatformTransactionHandler(platformTransactionManager, readOnly);
	}

	@Override
	public TransactionHandler	keepContextTransaction(CallerContext callerContext)
	{
		AutoCloseable handler;
		if ((handler = callerContext.checkResource(name)) == null) {
			handler = openTransaction(false);
			callerContext.saveResource(name, handler);
		}
		return (TransactionHandler) handler;
	}

	@Override
	public RuntimeException		tryTranslateException(Exception ex)
	{
		return null;
	}

	protected String		name;

	protected PlatformTransactionManager platformTransactionManager;
}
