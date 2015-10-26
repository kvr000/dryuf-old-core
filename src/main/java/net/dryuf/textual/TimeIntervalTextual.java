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

public class TimeIntervalTextual extends ConvertRegexpTextual<java.lang.Long>
{
	public String			check(String text, String style)
	{
		if (StringUtil.matchText("^(((\\d+):)?((\\d+):))?(\\d+)\\s*s?$", text) != null) {
			return null;
		}
		return callerContext.getUiContext().localize(TimeIntervalTextual.class, "hour:min:sec required");
	}

	public java.lang.Long		convertInternal(String text, String style)
	{
		String[] groups;
		if ((groups = StringUtil.matchText("^(((\\d+):)?((\\d+):))?(\\d+)\\s*s?$", text)) != null) {
			return (1000*((groups[2] == null ? 0 : java.lang.Long.parseLong(groups[3])*3600)+(groups[4] == null ? 0 : java.lang.Long.parseLong(groups[5])*60)+java.lang.Long.parseLong(groups[6])*1));
		}
		throw new RuntimeException("invalid format for time interval, no check performed?");
	}

	public String			format(java.lang.Long internal_, String style)
	{
		long internal = internal_/1000;
		long sec = internal%60;
		internal = internal/60;
		long min = internal%60;
		internal = internal/60;
		long hour = internal;
		return String.format("%d:%02d:%02d s", hour, min, sec);
	}

	public String			validate(java.lang.Long internal)
	{
		return internal instanceof java.lang.Long ? null : callerContext.getUiContext().localize(TimeIntervalTextual.class, "Number required.");
	}
}
