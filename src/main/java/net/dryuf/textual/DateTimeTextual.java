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

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeTextual extends DateTimeBaseTextual
{
	public String			check(String text, String style)
	{
		String[] groups;
		if ((groups = StringUtil.matchText("^(\\d{1,2})\\s*\\.\\s*(\\d{1,2})\\s*\\.\\s*(\\d{4})\\s+(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$", text)) != null) {
			if (checkDatetimeValid(java.lang.Integer.parseInt(groups[3]), java.lang.Integer.parseInt(groups[2])-1, java.lang.Integer.parseInt(groups[1])-1, java.lang.Integer.parseInt(groups[4]), java.lang.Integer.parseInt(groups[5]), java.lang.Integer.parseInt(groups[6])) != null)
				return null;
		}
		else {
			if (StringUtil.matchText("^epoch:\\s*(\\d+)$", text) != null)
				return null;
		}
		return getErrorMessage();
	}

	public java.lang.Long		convertInternal(String text, String style)
	{
		java.lang.Long internal;
		String[] groups;
		if ((groups = StringUtil.matchText("^(\\d{1,2})\\s*\\.\\s*(\\d{1,2})\\s*\\.\\s*(\\d{4})\\s+(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$", text)) != null) {
			if ((internal = checkDatetimeValid(java.lang.Integer.parseInt(groups[3]), java.lang.Integer.parseInt(groups[2])-1, java.lang.Integer.parseInt(groups[1])-1, java.lang.Integer.parseInt(groups[4]), java.lang.Integer.parseInt(groups[5]), java.lang.Integer.parseInt(groups[6]))) != null)
				return internal;
		}
		else if ((groups = StringUtil.matchText("^epoch:\\s*(\\d+)$", text)) != null) {
			return java.lang.Long.parseLong(groups[1]);
		}
		throw new RuntimeException(getErrorMessage());
	}

	public String			format(java.lang.Long internal, String style)
	{
		return (new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(new java.util.Date(internal.longValue()));
	}

	protected java.lang.Long	checkDatetimeValid(int year, int month, int day, int hour, int minute, int second)
	{
		try {
			return new GregorianCalendar(year-1900, month, day+1, hour, minute, second).getTimeInMillis();
		}
		catch (Exception ex) {
			return null;
		}
	}

	protected String		getErrorMessage()
	{
		return callerContext.getUiContext().localize("net.dryuf.textual.DateTime", "format dd.mm.yyyy hh:mm:ss required");
	}
}
