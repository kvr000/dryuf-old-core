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

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.reflections.ReflectionUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * Dummy implementation of {@link AppContainer}.
 */
public class DummyAppContainer extends java.lang.Object implements AppContainer
{
	public                          DummyAppContainer()
	{
		addBean("appContainer", this);
	}

	public String			getWorkRoot()
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public String			getAppRoot()
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public <T> T                    getConfigValue(String name, T defaultValue)
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public String			getConfigValue(String name, String defaultValue)
	{
		return configMap.getOrDefault(name, defaultValue);
	}

	@Override
	public InputStream getCpResource(String file)
	{
		InputStream stream;
		if ((stream = getClass().getResourceAsStream(file)) == null)
			throw new IllegalArgumentException("File does not exist: "+file);
		return stream;
	}

	@Override
	public byte[]			getCpResourceContent(String file)
	{
		try {
			return IOUtils.toByteArray(getCpResource(file));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T			postProcessBean(T bean, String name, Map<String, Object> params)
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public CallerContext		createCallerContext()
	{
		return new DummyCallerContext(this);
	}

	public Object			getBean(String name)
	{
		return beans.computeIfAbsent(name, beanExceptionerFunction);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T			getBeanTyped(String name, Class<T> clazz)
	{
		return (T)beans.computeIfAbsent(name, beanExceptionerFunction);

	}

	@Override
	public <T> T			createBeaned(Class<T> clazz, Map<String, Object> injects)
	{
		try {
			return injectBean(clazz.newInstance(), injects);
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T			createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects)
	{
		try {
			return injectBean(constructor.newInstance(args), injects);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	public String[]			getGlobalRoles()
	{
		return StringUtil.STRING_EMPTY_ARRAY;
	}

	public String[]			checkRoleDependency(String roleName)
	{
		return StringUtil.STRING_EMPTY_ARRAY;
	}

	@SuppressWarnings("unchecked")
	protected <T> T                 injectBean(T bean, Map<String, Object> injects) throws InvocationTargetException, IllegalAccessException
	{
		if (injects == null)
			injects = Collections.EMPTY_MAP;
		Class<?> clazz = bean.getClass();
		for (Field field: ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Inject.class))) {
			String name = field.getName();
			ReflectionUtil.setFieldValue(field, bean, injects.containsKey(name) ? injects.get(name) : getBean(name));
		}
		for (Method method: ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Inject.class))) {
			String name = StringUtil.uncapitalize(method.getName().substring(3));
			method.invoke(bean, injects.containsKey(name) ? injects.get(name) : getBean(name));
		}
		for (Map.Entry<String, Object> inject : injects.entrySet()) {
			Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withName("set"+StringUtil.capitalize(inject.getKey())));
			if (methods.size() != 1)
				throw new IllegalArgumentException("Expected single method for setting "+inject.getKey()+" found "+methods.size());
			methods.iterator().next().invoke(bean, inject.getKey(), inject.getValue());
		}
		return bean;

	}

	public DummyAppContainer        addBean(String name, Object value)
	{
		beans.put(name, value);
		return this;
	}

	public DummyAppContainer        addConfig(String name, String value)
	{
		configMap.put(name, value);
		return this;
	}

	protected ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();

	protected ConcurrentHashMap<String, Object> beans = new ConcurrentHashMap<>();

	protected static final Function<String, Object> beanExceptionerFunction = (String name) -> { throw new IllegalArgumentException("Unknown bean "+name); };
}
