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


public class TextualManager extends java.lang.Object
{
	@SuppressWarnings("unchecked")
	public static <T extends Textual<?>> T createTextual(Class<T> clazz, CallerContext callerContext)
	{
		try {
			return (T)(clazz.newInstance()).setCallerContext(callerContext);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Textual<?>	createTextualUnsafe(Class<? extends Textual<?>> clazz, CallerContext callerContext)
	{
		try {
			return ((Textual<?>)clazz.newInstance()).setCallerContext(callerContext);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static <T> T		convertTextual(Class<? extends Textual<T>> clazz, CallerContext callerContext, String text)
	{
		Textual<T> textual = createTextual(clazz, callerContext);
		String err;
		text = textual.prepare(text, null);
		if ((err = textual.check(text, null)) != null)
			throw new RuntimeException(err);
		return textual.convert(text, null);
	}

	public static Object		convertTextualUnsafe(Class<? extends Textual<?>> clazz, CallerContext callerContext, String text)
	{
		@SuppressWarnings("unchecked")
		Textual<Object> textual = (Textual<Object>)createTextualUnsafe(clazz, callerContext);
		String err;
		text = textual.prepare(text, null);
		if ((err = textual.check(text, null)) != null)
			throw new RuntimeException(err);
		return textual.convert(text, null);
	}

	public static <T> String	formatTextual(Class<? extends Textual<T>> clazz, CallerContext callerContext, T internal)
	{
		Textual<T> textual = createTextual(clazz, callerContext);
		return textual.format(internal, null);
	}

	public static String		formatTextualUnsafe(Class<? extends Textual<?>> clazz, CallerContext callerContext, Object internal)
	{
		@SuppressWarnings("unchecked")
		Textual<Object> textual = (Textual<Object>)createTextualUnsafe(clazz, callerContext);
		return textual.format(internal, null);
	}
}
