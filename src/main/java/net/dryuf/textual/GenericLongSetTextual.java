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


import net.dryuf.core.StringUtil;

import java.util.LinkedHashMap;

public class GenericLongSetTextual extends  net.dryuf.textual.DirectKeyPreTrimTextual<java.lang.Long>
{
	public				GenericLongSetTextual(String dataClass, LinkedHashMap<String, java.lang.Long> optionsMap)
	{
		this.dataClass = dataClass;
		this.optionsMap = optionsMap;
	}

	public String			check(String text, String style)
	{
		if (!text.equals("")) {
			int pos = text.length();
			do {
				int newpos = text.lastIndexOf(',', pos-1);
				if (newpos < 0)
					newpos = 0;
				if (optionsMap.get(text.substring(newpos, pos)) == null)
					return getUiContext().localize(dataClass, "unknown option: ")+text.substring(newpos, pos);
			} while (pos > 0);
		}
		return null;
	}

	public java.lang.Long		convert(String text, String style)
	{
		int internal = 0;
		if (!text.equals("")) {
			int pos = text.length();
			do {
				int newpos = text.lastIndexOf(',', pos-1);
				if (newpos < 0)
					newpos = 0;
				internal |= 1<<optionsMap.get(text.substring(newpos, pos));
			} while (pos > 0);
		}
		return java.lang.Long.valueOf(internal);
	}

	public String			validate(java.lang.Long internal)
	{
		long max = 0;
		for (java.lang.Long option: optionsMap.values())
			max += option;
		if ((internal&~max) != 0) {
			return getUiContext().localize(this.dataClass, "Invalid value passed: ")+(internal&~max);
		}
		return null;
	}

	public String			format(java.lang.Long internal_, String style)
	{
		long internal = internal_.longValue();
		String text = "";
		for (int i = 0; (internal>>i) != 0; i++) {
			if ((internal&(1<<i)) != 0) {
				if (!text.equals(""))
					text = text+",";
				text = text+callerContext.getUiContext().localize(dataClass, optionsMap.keySet().toArray(StringUtil.STRING_EMPTY_ARRAY)[i]);
			}
		}
		return text;
	}

	public java.lang.Long		convertKeyInternal(String text)
	{
		return java.lang.Long.valueOf(text);
	}

	protected String		dataClass;

	protected java.util.LinkedHashMap<String, java.lang.Long> optionsMap;
}
