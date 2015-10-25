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

package net.dryuf.srvui.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import net.dryuf.core.AppContainer;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.ReportException;
import net.dryuf.core.UiContext;
import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;
import net.dryuf.srvui.spring.SpringUiContext;


public class SpringCallerContext extends java.lang.Object implements net.dryuf.core.CallerContext
{
	public				SpringCallerContext(AppContainer appContainer, ServletContext servletContext, ApplicationContext beanContext_)
	{
		this.appContainer = appContainer;
		this.beanContext = beanContext_;
		if (this.appContainer == null)
			this.appContainer = getBeanTyped("appContainer", AppContainer.class);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		roles.add("");
		roles.add("guest");
		if (auth != null) {
			for (GrantedAuthority granted: auth.getAuthorities())
				roles.add(granted.getAuthority());
			if (auth.getPrincipal() instanceof net.dryuf.security.UserAccountDetails)
				realUserId = userId = ((net.dryuf.security.UserAccountDetails)auth.getPrincipal()).getUserAccount().getUserId();
		}
		this.servletContext = servletContext;
		this.applicationProperties = (Properties) beanContext.getBean("applicationProperties");
	}

	public static SpringCallerContext createFromHttpRequest(ApplicationContext beanContext, HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		SpringCallerContext context = new SpringCallerContext(null, null, beanContext);
		context.uiContext = new SpringUiContext(context);
		if (session != null) {
			context.uiContext.setLocalizeDebug(ObjectUtils.defaultIfNull((Integer)session.getAttribute(UiContext.class.getName()+".translationLevel"), 0));
			context.uiContext.setTiming(ObjectUtils.defaultIfNull((Boolean)session.getAttribute(UiContext.class.getName()+".timing"), false));
			context.userId = ObjectUtils.defaultIfNull((Integer)session.getAttribute(CallerContext.class.getName()+".effectiveUserId"), context.userId);
		}
		return context;
	}

	public static SpringCallerContext createFromContext(ApplicationContext beanContext)
	{
		SpringCallerContext context = new SpringCallerContext(null, null, beanContext);
		context.uiContext = new SpringUiContext(context);
		return context;
	}

	public static SpringCallerContext createFromServletContext(ServletContext servletContext)
	{
		SpringCallerContext context = new SpringCallerContext(null, servletContext, WebApplicationContextUtils.getWebApplicationContext(servletContext));
		context.uiContext = new SpringUiContext(context);
		return context;
	}

	@Override
	public AppContainer		getAppContainer()
	{
		return getBeanTyped("appContainer", AppContainer.class);
	}

	public String			getWorkRoot()
	{
		throw new RuntimeException("getWorkRoot unsupported");
	}

	public String			getAppRoot()
	{
		try {
			return WebUtils.getRealPath(servletContext, "/");
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CallerContext		getRootContext()
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

	public AutoCloseable		checkResource(String identifier)
	{
		return handlers.get(identifier);
	}

	public void			saveResource(String identifier, AutoCloseable handler)
	{
		handlers.put(identifier, handler);
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

	public boolean			isLoggedIn()
	{
		return this.getUserId() != null;
	}

	public boolean			checkRole(String role)
	{
		return roles.contains(role);
	}

	public Set<String>		getRoles()
	{
		return roles;
	}

	@Override
	public void			loggedOut()
	{
		userId = null;
		realUserId = null;
		roles.clear();
		roles.add("");
		roles.add("guest");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T			getConfigValue(String name, @NotNull T defaultValue)
	{
		String value = applicationProperties.getProperty(name);
		if (value == null)
			return defaultValue;
		if (defaultValue == null || defaultValue instanceof String)
			return (T)value;
		return (T) Dryuf.invokeMethod(null, Dryuf.getObjectMethod(defaultValue, "valueOf", String.class), value);
	}

	public Object			getContextVar(String name)
	{
		if (name.equals("userId")) {
			return getUserId();
		}
		else {
			throw new ReportException("variable "+name+" undefined");
		}
	}

	@Override
	public CallerContext		createFullContext()
	{
		return this;
	}

	public InputStream		getClasspathResourceAsStream(String name)
	{
		try {
			return beanContext.getResource("classpath:"+name).getInputStream();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object			getBean(String name)
	{
		return ((BeanFactory)getBeanContext()).getBean(name);
	}

	public <T> T			getBeanTyped(String name, Class<T> clazz)
	{
		return ((BeanFactory)getBeanContext()).getBean(name, clazz);
	}

	protected AppContainer          appContainer;

	protected Set<String>		roles = new HashSet<String>();

	protected Object		userId = null;

	public Object			getUserId()
	{
		return this.userId;
	}

	protected Object		realUserId = null;

	public Object			getRealUserId()
	{
		return this.realUserId;
	}

	protected ApplicationContext	beanContext;

	public ApplicationContext	getBeanContext()
	{
		return this.beanContext;
	}

	protected ServletContext	servletContext;

	protected UiContext		uiContext;

	public UiContext		getUiContext()
	{
		return this.uiContext;
	}

	protected Properties		applicationProperties;

	protected Map<String, AutoCloseable> handlers = new HashMap<String, AutoCloseable>();
}
