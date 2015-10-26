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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.dryuf.core.CallerContext;

import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;


public class SpringUiContext extends net.dryuf.core.AbstractUiContext
{
	public				SpringUiContext(CallerContext callerContext)
	{
		super();
		this.callerContext = callerContext;
		language = getDefaultLanguage();
		setLocalizeContextPath(callerContext.getConfigValue("app.contextDomain", ""));
		messageSource = callerContext.getBeanTyped("messageSource", MessageSource.class);
	}

	protected String		getDefaultMessage(String className, String text)
	{
		switch (getLocalizeDebug()) {
		case 0:
			return text;

		case 1:
			return className+"^"+text;

		default:
			return className+"^"+text;
		}
	}

	@Override
	public String			localize(String className, String text)
	{
		switch (getLocalizeDebug()) {
		case 0:
		case 1:
			return messageSource.getMessage(className+"^"+text, null, getDefaultMessage(className, text), locale);

		default:
			return className+"^"+text;
		}
	}

	@Override
	public String			localizeArgs(String className, String text, Object[] args)
	{
		return messageSource.getMessage(className+"^"+text, args, getDefaultMessage(className, text), locale);
	}

	@Override
	public String			getDefaultLanguage()
	{
		String defaultLanguage = callerContext.getConfigValue("localize.defaultLanguage", null);
		if (defaultLanguage == null)
			throw new RuntimeException("localize.defaultLanguage not set");
		return defaultLanguage;
	}

	@Override
	public String			getLocalizePath()
	{
		return null;
	}

	@Override
	public String			readLocalizedFile(String filename)
	{
		String fullfilename = "localize/"+language+"/"+getLocalizeContextPath()+filename;
		InputStream stream = getClass().getClassLoader().getResourceAsStream(fullfilename);
		try {
			if (stream == null)
				throw new FileNotFoundException(fullfilename);
			try {
				return IOUtils.toString(stream, "UTF-8");
			}
			finally {
				stream.close();
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String[]			listLanguages()
	{
		String languages = callerContext.getConfigValue("localize.languages", null);
		if (languages == null)
			throw new RuntimeException("localize.languages not set");
		return languages.split(",");
	}

	@Override
	public boolean			checkLanguage(String language)
	{
		if (!language.matches("^(\\w{1,10})$"))
			return false;
		String languages = callerContext.getConfigValue("localize.languages", null);
		if (languages == null)
			throw new RuntimeException("localize.languages not set");
		if ((","+languages+",").indexOf(","+language+",") < 0)
			return false;
		this.setLanguage(language);
		return true;
	}

	@Override
	public void			setLanguage(String language)
	{
		String languages = callerContext.getConfigValue("localize.languages", null);
		if (languages == null)
			throw new RuntimeException("localize.languages not set");
		if ((","+languages+",").indexOf(","+language+",") < 0)
			throw new RuntimeException("language not allowed: "+language);
		super.setLanguage(language);
		locale = Locale.forLanguageTag(language);
	}

	@Override
	public Map<String, String>	getClassLocalization(String className)
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		Properties lp = new Properties();
		InputStream stream;
		if ((stream = getClass().getResourceAsStream("localize/"+this.getLanguage()+"/_class/"+className+".localize.properties")) == null)
			return map;
		try {
			lp.load(new InputStreamReader(stream, "UTF-8"));
			for (String key: lp.stringPropertyNames()) {
				String value = lp.getProperty(key);
				key = key.substring(key.indexOf('^')+1);
				map.put(key, value);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	protected MessageSource		messageSource;

	protected CallerContext		callerContext;

	protected Locale		locale;
}
