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

import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import net.dryuf.core.StringUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTextual extends DateTimeBaseTextual
{
	public String			check(String text, String style)
	{
		String[] groups;
		if ((groups = StringUtil.matchText("^(\\d{1,2})\\s*\\.\\s*(\\d{1,2})\\s*\\.\\s*(\\d{4})$", text)) == null)
			return getErrorMessage();
		if (checkDateValid(java.lang.Integer.parseInt(groups[3]), java.lang.Integer.parseInt(groups[2])-1, java.lang.Integer.parseInt(groups[1])-1) == null)
			return getErrorMessage();
		return null;
	}

	public java.lang.Long		convertInternal(String text, String style)
	{
		String[] groups;
		java.lang.Long internal;
		if ((groups = StringUtil.matchText("^(\\d{1,2})\\s*\\.\\s*(\\d{1,2})\\s*\\.\\s*(\\d{4})$", text)) == null) {
			throw new RuntimeException(getErrorMessage());
		}
		if ((internal = checkDateValid(java.lang.Integer.parseInt(groups[3]), java.lang.Integer.parseInt(groups[2])-1, java.lang.Integer.parseInt(groups[1])-1)) == null)
			throw new RuntimeException(getErrorMessage());
		return internal;
	}

	@SuppressWarnings("cast")
	public String			format(java.lang.Long internal, String style)
	{
		return (FastDateFormat.getInstance("dd.MM.yyyy", TimeZone.getTimeZone("UTC"))).format(new java.util.Date(((java.lang.Long)internal)));
	}

	@SuppressWarnings("deprecation")
	protected java.lang.Long	checkDateValid(int year, int month, int day)
	{
		try {
			return java.util.Date.UTC(year-1900, month, day+1, 0, 0, 0);
		}
		catch (Exception ex) {
			return null;
		}
	}

	protected String		getErrorMessage()
	{
		return callerContext.getUiContext().localize(DateTextual.class, "format dd.mm.yyyy required");
	}
}
