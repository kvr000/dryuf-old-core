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

package net.dryuf.srvui;

import net.dryuf.core.Options;


public class PageUrl extends java.lang.Object
{
	public static final int		TYPE_FINAL		= 0;
	public static final int		TYPE_RELATIVE		= 1;
	public static final int		TYPE_ROOTED		= 2;
	public static final int		TYPE_LANGUAGED		= 3;
	public static final int		TYPE_PAGED		= 4;
	public static final int		TYPE_VIRTUAL		= 5;

	/**
	 * Creates PageUrl directly passing the input url and options.
	 */
	public static PageUrl		createVirtual(String url, Options options)
	{
		return new PageUrl(TYPE_VIRTUAL, url, options);
	}

	/**
	 * Creates direct URL.
	 */
	public static PageUrl		createResource(String url)
	{
		return url.startsWith("/") ? createRooted(url) : createRelative(url);
	}

	/**
	 * Creates direct URL.
	 */
	public static PageUrl		createFinal(String url)
	{
		return new PageUrl(TYPE_FINAL, url, Options.NONE);
	}

	/**
	 * Creates direct URL.
	 */
	public static PageUrl		createRelative(String url)
	{
		return new PageUrl(TYPE_RELATIVE, url, Options.NONE);
	}

	/**
	 * Creates root relative URL.
	 */
	public static PageUrl		createRooted(String url)
	{
		return new PageUrl(TYPE_ROOTED, url, Options.NONE);
	}

	/**
	 * Creates page url, i.e. pageCode identifies the name of the page.
	 */
	public static PageUrl		createPaged(String pageCode)
	{
		return new PageUrl(TYPE_PAGED, pageCode, Options.NONE);
	}

	/**
	 * Creates local url, within the current language path.
	 */
	public static PageUrl		createLanguaged(String url)
	{
		return new PageUrl(TYPE_LANGUAGED, url, Options.NONE);
	}

	public static PageUrl	getDummy()
	{
		return createFinal("");
	}

	protected			PageUrl(int type, String url, Options options)
	{
		this.type = type;
		this.url = url;
		this.options = options;
	}

	public String			getUrl()
	{
		return this.url;
	}

	protected String		url;

	public int			getType()
	{
		return type;
	}

	protected int			type;

	public Options			getOptions()
	{
		return this.options;
	}

	public boolean			equals(Object o)
	{
		if (!(o instanceof PageUrl))
			return false;
		PageUrl s = (PageUrl)o;
		if (s.type != this.type)
			return false;
		if (s.url != null ? !s.url.equals(this.url) : this.url != null)
			return false;
		if (s.options != null ? !s.options.equals(this.options) : this.options != null)
			return false;
		return true;
	}

	public int			hashCode()
	{
		return (this.type*37+(this.url == null ? 0 : this.url.hashCode()))*37+(this.options == null ? 0 : this.options.hashCode());
	}

	protected Options		options;
};
