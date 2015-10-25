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

package net.dryuf.app.spring;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import net.dryuf.core.NoSuchBeanException;
import net.dryuf.core.StringUtil;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import net.dryuf.core.AppContainerAware;
import net.dryuf.core.CallerContext;
import net.dryuf.srvui.spring.SpringCallerContext;


public class SpringAppContainer extends java.lang.Object implements net.dryuf.core.AppContainer, BeanPostProcessor
{
	public String			getWorkRoot()
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public String			getAppRoot()
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public <T> T			getConfigValue(String name, T defaultValue)
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public InputStream		getCpResource(String file)
	{
		InputStream stream;
		if ((stream = getClass().getResourceAsStream("/"+file)) == null)
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

	public <T> T			postProcessBean(T bean, String name, Map<String, Object> properties)
	{
		if (properties != null && !properties.isEmpty())
			throw new UnsupportedOperationException("setting properties not yet implemented");
		springContext.getAutowireCapableBeanFactory().autowireBean(bean);
		return bean;
	}

	@Override
	public Object			postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object			postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
		if (bean instanceof AppContainerAware)
			((AppContainerAware)bean).afterAppContainer(this);
		return bean;
	}

	@Override
	public CallerContext		createCallerContext()
	{
		return SpringCallerContext.createFromContext(springContext);
	}

	@Override
	public Object			getBean(String name)
	{
		try {
			return springContext.getBean(name);
		}
		catch (NoSuchBeanDefinitionException ex) {
			throw new NoSuchBeanException(ex.toString(), ex);
		}
	}

	@Override
	public <T> T			getBeanTyped(String name, Class<T> clazz)
	{
		try {
			return springContext.getBean(name, clazz);
		}
		catch (NoSuchBeanDefinitionException ex) {
			throw new NoSuchBeanException(ex.toString(), ex);
		}
	}

	@Override
	public <T> T			createBeaned(Class<T> clazz, Map<String, Object> injects)
	{
		try {
			T obj = clazz.newInstance();
			springContext.getAutowireCapableBeanFactory().autowireBean(obj);
			if (injects != null) {
				applyBeanInjects(obj, injects);
			}
			return obj;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public <T> T			createBeanedArgs(Constructor<T> constructor, Object[] args, Map<String, Object> injects)
	{
		try {
			T obj = constructor.newInstance(args);
			springContext.getAutowireCapableBeanFactory().autowireBean(obj);
			if (injects != null)
				applyBeanInjects(obj, injects);
			return obj;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String[]			getGlobalRoles()
	{
		return rolesDependencies.keySet().toArray(StringUtil.STRING_EMPTY_ARRAY);
	}

	@Override
	public String[]			checkRoleDependency(String roleName)
	{
		return rolesDependencies.get(roleName);
	}

	public void			setRolesDependencies(Map<String, String[]> rolesDependencies)
	{
		this.rolesDependencies = rolesDependencies;
	}

	protected <T> void		applyBeanInjects(T obj, Map<String, Object> injects)
	{
		BeanWrapperImpl wrapper = new BeanWrapperImpl(obj);
		for (Map.Entry<String, Object> inject: injects.entrySet()) {
			wrapper.setPropertyValue(inject.getKey(), inject.getValue());
		}
	}

	protected Map<String, String[]>	rolesDependencies;

	@Inject
	protected ApplicationContext	springContext;
}
