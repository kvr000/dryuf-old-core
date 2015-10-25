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

public class GenericIntegerSetTextual extends  net.dryuf.textual.DirectKeyPreTrimTextual<java.lang.Integer>
{
	public				GenericIntegerSetTextual(String dataClass, LinkedHashMap<String, java.lang.Integer> optionsMap)
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
					return callerContext.getUiContext().localize(dataClass, "unknown option: ")+text.substring(newpos, pos);
			} while (pos > 0);
		}
		return null;
	}

	public java.lang.Integer	convert(String text, String style)
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
		return java.lang.Integer.valueOf(internal);
	}

	public String			validate(java.lang.Integer internal)
	{
		int max = 0;
		for (java.lang.Integer option: optionsMap.values())
			max += option;
		if ((internal&~max) != 0) {
			return getUiContext().localize(this.dataClass, "Invalid value passed: ")+(internal&~max);
		}
		return null;
	}

	public String			format(java.lang.Integer internal, String style)
	{
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

	public java.lang.Integer	convertKeyInternal(String text)
	{
		return java.lang.Integer.valueOf(text);
	}

	protected String		dataClass;

	protected java.util.LinkedHashMap<String, java.lang.Integer> optionsMap;
}
