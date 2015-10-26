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

package net.dryuf.core;

import net.dryuf.io.FileData;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public abstract class AbstractRootCallerContext extends Object implements CallerContext
{
	public                          AbstractRootCallerContext(AppContainer appContainer)
	{
		this.appContainer = appContainer;
	}

	@Override
	public AppContainer		getAppContainer()
	{
		return appContainer;
	}

	@Override
	public String			getWorkRoot()
	{
		return appContainer.getWorkRoot();
	}

	@Override
	public String			getAppRoot()
	{
		return appContainer.getAppRoot();
	}

	@Override
	public CallerContext		getRootContext()
	{
		return this;
	}

	@Override
	public boolean                  isLoggedIn()
	{
		return getUserId() != null;
	}

	public <T> T	 		getConfigValue(String name, T defaultValue)
	{
		return appContainer.getConfigValue(name, defaultValue);
	}

	@Override
	public Object	 		getContextVar(String name)
	{
		return null;
	}

	@Override
	public CallerContext		createFullContext()
	{
		return this;
	}

	@Override
	public void			close()
	{
		for (AutoCloseable handler: handlers.values()) {
			try {
				handler.close();
			}
			catch (Exception ex) {
			}
		}
	}

	@Override
	public AutoCloseable		checkResource(String identifier)
	{
		return handlers.get(identifier);
	}

	@Override
	public void			saveResource(String identifier, AutoCloseable handler)
	{
		handlers.put(identifier, handler);
	}

	@Override
	public void			loggedOut()
	{
	}

	@Override
	public <T> T			createBeaned(Class<T> clazz, Map<String, Object> injects)
	{
		return appContainer.createBeaned(clazz, injects);
	}

	@Override
	public <T> T			createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects)
	{
		return appContainer.createBeanedArgs(constructor, args, injects);
	}

	@Override
	public Object			getBean(String name)
	{
		return appContainer.getBean(name);
	}

	@Override
	public <T> T			getBeanTyped(String name, Class<T> type)
	{
		return appContainer.getBeanTyped(name, type);
	}

	@Override
	public UiContext		getUiContext()
	{
		return null;
	}

	protected AppContainer          appContainer;

	protected Map<String, AutoCloseable> handlers = new HashMap<String, AutoCloseable>();
}
