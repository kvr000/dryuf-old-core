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

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.dryuf.io.FileData;


/**
 * {@link CallerContext} forwarding all its methods to its parent.
 */
public class SubCallerContext extends java.lang.Object implements net.dryuf.core.CallerContext
{
	public				SubCallerContext(CallerContext parentContext)
	{
		this.parentContext = parentContext;
	}

	@Override
	public AppContainer		getAppContainer()
	{
		return parentContext.getAppContainer();
	}

	@Override
	public Object			getUserId()
	{
		return parentContext.getUserId();
	}

	@Override
	public Object			getRealUserId()
	{
		return parentContext.getRealUserId();
	}

	@Override
	public String			getWorkRoot()
	{
		return parentContext.getWorkRoot();
	}

	@Override
	public String			getAppRoot()
	{
		return parentContext.getAppRoot();
	}

	@Override
	public CallerContext		getRootContext()
	{
		return parentContext;
	}

	@Override
	public boolean			isLoggedIn()
	{
		return parentContext.isLoggedIn();
	}

	@Override
	public boolean			checkRole(String role)
	{
		return parentContext.checkRole(role);
	}

	@Override
	public Set<String>		getRoles()
	{
		return parentContext.getRoles();
	}

	@Override
	public <T> T		 	getConfigValue(String name, T defaultValue)
	{
		return parentContext.getConfigValue(name, defaultValue);
	}

	@Override
	public Object		 	getContextVar(String name)
	{
		return parentContext.getContextVar(name);
	}

	@Override
	public CallerContext		createFullContext()
	{
		return parentContext.createFullContext();
	}

	@Override
	public void			close()
	{
	}

	@Override
	public AutoCloseable		checkResource(String identifier)
	{
		return parentContext.checkResource(identifier);
	}

	@Override
	public void			saveResource(String identifier, AutoCloseable handler)
	{
		parentContext.saveResource(identifier, handler);
	}

	@Override
	public <T> T			createBeaned(Class<T> clazz, Map<String, Object> injects)
	{
		return parentContext.createBeaned(clazz, injects);
	}

	@Override
	public <T> T			createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects)
	{
		return parentContext.createBeanedArgs(constructor, args, injects);
	}

	@Override
	public Object			getBean(String name)
	{
		return parentContext.getBean(name);
	}

	@Override
	public <T> T			getBeanTyped(String name, Class<T> clazz)
	{
		return parentContext.getBeanTyped(name, clazz);
	}

	@Override
	public void			loggedOut()
	{
		parentContext.loggedOut();
	}

	public UiContext		getUiContext()
	{
		if (uiContext == null)
			uiContext = parentContext.getUiContext();
		return uiContext;
	}

	protected CallerContext		parentContext;
	protected UiContext		uiContext;
}
