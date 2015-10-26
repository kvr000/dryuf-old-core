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


public class BoolSwitchTextual extends net.dryuf.textual.PreTrimTextual<java.lang.Boolean>
{
	public String			validate(java.lang.Boolean internal)
	{
		if (internal instanceof java.lang.Boolean)
			return null;
		return callerContext.getUiContext().localize(BoolSwitchTextual.class, "Bool value required.");
	}

	public String			check(String text, String style)
	{
		if (!text.matches("^([01]|true|false)$"))
			return callerContext.getUiContext().localize(BoolSwitchTextual.class, "Bool value required.");
		return null;
	}

	@Override
	public String			format(Boolean internal, String style)
	{
		return internal ? "true" : "false";
	}

	public java.lang.Boolean	convertInternal(String text, String style)
	{
		if (text.equals("true"))
			return true;
		else if (text.equals("false"))
			return false;
		else
			return java.lang.Integer.valueOf(text) != 0;
	}

	public java.lang.Boolean	convertKeyInternal(String text)
	{
		return java.lang.Integer.valueOf(text) != 0;
	}
}
