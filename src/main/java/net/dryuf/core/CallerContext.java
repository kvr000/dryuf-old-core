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
 * {@code CallerContext} represents context of the calling party.
 *
 * It provides reference to application container and also information about the user.
 */
public interface CallerContext extends AutoCloseable
{
	AppContainer			getAppContainer();

	String				getWorkRoot();

	String				getAppRoot();

	/**
	 * Gets root context from this caller context.
	 *
	 * @return
	 * 	root context
	 */
	CallerContext			getRootContext();

	Object				getUserId();

	Object				getRealUserId();

	/**
	 * Checks whether the user is logged in.
	 *
	 * @return
	 * 	the indicator whether the user is logged in
	 */
	boolean				isLoggedIn();

	/**
	 * checks if the context has appropriate role (specified as comma separated list of roles)
	 *
	 * @return false
	 * 	if the role is not available
	 * @return true
	 * 	if the role is available
	 */
	boolean				checkRole(String role);

	/**
	 * gets the list of roles
	 *
	 * @return array of available roles
	 */
	Set<String>			getRoles();

	<T> T				getConfigValue(String name, T defaultValue);

	Object	 			getContextVar(String name);

	/**
	 * Closes all associated resources.
	 */
	void				close();

	/**
	 * Checks whether handler of specified identifier is opened within this context.
	 *
	 * @return null
	 * 	if no handler is found
	 * @return handler
	 *	handler associated with the identifier
	 */
	AutoCloseable			checkResource(String identifier);

	/**
	 * Saves handler of specified name in this context.
	 *
	 * @param identifier
	 * 	handler identifier
	 * @param handler
	 * 	handler to be associated with identifier
	 */
	void				saveResource(String identifier, AutoCloseable handler);

	CallerContext			createFullContext();

	<T> T				createBeaned(Class<T> clazz, Map<String, Object> injects);

	<T> T				createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects);

	/**
	 * Gets bean of the specified name.
	 *
	 * @param name
	 * 	name of the bean
	 *
	 * @return bean
	 * 	in case of success
	 *
	 * @throw RuntimeException
	 * 	in case the bean was not found
	 */
	Object				getBean(String name);

	/**
	 * Gets bean of the specified name and type.
	 *
	 * @param name
	 * 	name of the bean
	 * @param clazz
	 * 	type of the bean
	 *
	 * @return bean
	 * 	in case of success
	 *
	 * @throw RuntimeException
	 * 	in case the bean was not found
	 */
	<T> T				getBeanTyped(String name, Class<T> clazz);

	/**
	 * Notifies context about being logged off.
	 */
	void				loggedOut();

	/**
	 * Gets UI Context for this context.
	 *
	 * @return ui context
	 */
	UiContext			getUiContext();
}
