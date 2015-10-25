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

import net.dryuf.trans.meta.NoDynamic;

import java.util.function.Function;


public abstract class AbstractUiContext extends java.lang.Object implements net.dryuf.core.UiContext
{
	public				AbstractUiContext()
	{
		super();
	}

	@NoDynamic
	@Override
	public String			localize(Class<?> class_name, String text)
	{
		return localize(class_name.getName(), text);
	}

	@NoDynamic
	@Override
	public String			localizeArgs(Class<?> class_name, String text, Object[] args)
	{
		return localizeArgs(class_name.getName(), text, args);
	}

	@NoDynamic
	@Override
	public String			localizeArgsEscape(Function<String, String> escaper, Class<?> clazz, String text, String[] args)
	{
		return localizeArgsEscape(escaper, clazz.getName(), text, args);
	}

	@Override
	public String			localizeArgs(String class_name, String text, Object[] args)
	{
		return this.localizeArgsEscape(new Function<String, String>() { public String apply(String v) { return v; } }, class_name, text, args);
	}

	@Override
	public String			localizeArgsEscape(Function<String, String> escaper, String class_name, String text, Object[] args)
	{
		StringBuilder out = new StringBuilder();
		String[] match;
		while ((match = StringUtil.matchText("^(.*)\\{(\\d+)\\}(.*)$", text)) != null) {
			out.append(match[1]).append(escaper.apply(args[Integer.valueOf(match[2])].toString()));
			text = match[3];
		}
		out.append(text);
		return out.toString();
	}

	@Override
	public String[]			listLanguages()
	{
		return new String[0];
	}

	@Override
	public void			setLocalizeContextPath(String path)
	{
		this.localizeContextPath = path;
	}

	protected String		language;

	public String			getLanguage()
	{
		return this.language;
	}

	public void			setLanguage(String language_)
	{
		this.language = language_;
	}

	protected int			localizeDebug = 0;

	public int			getLocalizeDebug()
	{
		return this.localizeDebug;
	}

	public void			setLocalizeDebug(int localizeDebug_)
	{
		this.localizeDebug = localizeDebug_;
	}

	protected boolean		timing;

	public boolean			getTiming()
	{
		return this.timing;
	}

	public void			setTiming(boolean timing_)
	{
		this.timing = timing_;
	}

	private String			localizeContextPath;

	public String			getLocalizeContextPath()
	{
		return this.localizeContextPath;
	}
}
