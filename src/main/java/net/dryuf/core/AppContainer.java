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
import java.util.Map;


/**
 * {@code AppContainer} manages the beans and set up of the application.
 */
public interface AppContainer
{
	String				getWorkRoot();

	String				getAppRoot();

	<T> T				getConfigValue(String name, T defaultValue);

	InputStream			getCpResource(String file);

	byte[]				getCpResourceContent(String file);

	<T> T				postProcessBean(T bean, String name, Map<String, Object> params);

	CallerContext			createCallerContext();

	Object				getBean(String name);

	<T> T				getBeanTyped(String name, Class<T> clazz);

	<T> T				createBeaned(Class<T> clazz, Map<String, Object> injects);

	<T> T				createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects);

	String[]			getGlobalRoles();

	String[]			checkRoleDependency(String roleName);
}
