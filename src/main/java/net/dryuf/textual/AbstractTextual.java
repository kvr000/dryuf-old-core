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

package net.dryuf.textual;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Textual;
import net.dryuf.core.UiContext;

import java.lang.String;


public class AbstractTextual<T> extends java.lang.Object implements net.dryuf.core.Textual<T>
{
	public Textual<T>		setCallerContext(CallerContext callerContext)
	{
		if ((this.callerContext = callerContext) == null)
			throw new NullPointerException();
		return this;
	}

	public String			prepare(String text, String style)
	{
		return text;
	}

	public String			check(String text, String style)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	public T			convertInternal(String text, String style)
	{
		return (T) text;
	}

	public T			convert(String text, String style)
	{
		String err;
		T result = convertInternal(text, style);
		if ((err = validate(result)) != null)
			throw new RuntimeException(err);
		return result;
	}

	public String			validate(T internal)
	{
		return null;
	}

	public String			format(T internal, String style)
	{
		return internal == null ? "" : internal.toString();
	}

	public T			convertKey(String text)
	{
		String error;
		T result = convertKeyInternal(text);
		if ((error = validate(result)) != null)
			throw new RuntimeException(error);
		return result;
	}

	public T			convertKeyInternal(String text)
	{
		return convertInternal(text, null);
	}

	public String			formatKey(T internal)
	{
		return format(internal, null);
	}

	protected UiContext		getUiContext()
	{
		return this.callerContext.getUiContext();
	}

	protected CallerContext		getCallerContext()
	{
		return this.callerContext;
	}

	protected CallerContext		callerContext;
}
