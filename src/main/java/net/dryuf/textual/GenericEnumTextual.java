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

import java.util.LinkedHashMap;

import net.dryuf.core.StringUtil;


public class GenericEnumTextual extends net.dryuf.textual.DirectKeyPreTrimTextual<java.lang.Integer>
{
	public				GenericEnumTextual(String dataClass, LinkedHashMap<String, java.lang.Integer> optionsMap)
	{
		super();
		this.dataClass = dataClass;
		this.optionsMap = optionsMap;
	}

	public String			check(String text, String style)
	{
		if (this.optionsMap.get(text) == null)
			return getUiContext().localize(this.dataClass, "Invalid option, allowed only defined values: ")+StringUtil.join(", ", optionsMap.keySet());
		return null;
	}

	public java.lang.Integer	convertInternal(String text, String style)
	{
		if (this.optionsMap.get(text) == null)
			throw new RuntimeException("Invalid option, allowed only defined values for "+this.dataClass+": "+StringUtil.join(", ", optionsMap.keySet()));
		java.lang.Integer internal = this.optionsMap.get(text);
		return internal;
	}

	public String			validate(java.lang.Integer internal)
	{
		if (internal < 0 || internal >= this.optionsMap.size())
			return getUiContext().localize(this.dataClass, "Invalid option, allowed only defined values ");
		return null;
	}

	public String			format(java.lang.Integer internal, String style)
	{
		return this.callerContext.getUiContext().localize(this.dataClass, this.optionsMap.keySet().toArray(StringUtil.STRING_EMPTY_ARRAY)[internal]);
	}

	public java.lang.Integer	convertKeyInternal(String text)
	{
		return java.lang.Integer.valueOf(text);
	}

	protected String		dataClass;

	protected LinkedHashMap<String, java.lang.Integer> optionsMap;
}
