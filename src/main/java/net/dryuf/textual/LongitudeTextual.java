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

public class LongitudeTextual extends LongitudeBaseTextual
{
	public String			check(String text, String style)
	{
		String groups[];
		if ((groups = StringUtil.matchText("^((W|E)\\s*(\\d{1,2})°\\s*(\\d{1,2})\\\'\\s*(\\d{1,2}(\\.\\d*)?)\\\"|[+-]?\\d+\\.\\d*)$", text)) != null) {
			double lng;
			if (groups[3] != null) {
				lng = java.lang.Integer.parseInt(groups[3])+java.lang.Float.parseFloat(groups[4])/60+java.lang.Float.parseFloat(groups[5])/3600;
				if (groups[2].equals("W"))
					lng = -lng;
			}
			else {
				throw new RuntimeException("todo");
			}
			return validateDouble(lng*10000000);
		}
		else {
			return callerContext.getUiContext().localize("net.dryuf.textual.Latitude", "format N|S dd°mm'ss[.sss]\" required");
		}
	}

	public java.lang.Integer	convertInternal(String text, String style)
	{
		String groups[];
		if ((groups = StringUtil.matchText("^((W|E)\\s*(\\d{1,2})°\\s*(\\d{1,2})\\\'\\s*(\\d{1,2}(\\.\\d*)?)\\\"|[+-]?\\d+\\.\\d*)$", text)) != null) {
			double lng;
			lng = java.lang.Integer.parseInt(groups[3])+java.lang.Float.parseFloat(groups[4])/60+java.lang.Float.parseFloat(groups[5])/3600;
			if (groups[2].equals("W"))
				lng = -lng;
			lng *= 10000000;
			String error;
			if ((error = validateDouble(lng)) != null)
				throw new RuntimeException(error);
			return java.lang.Integer.valueOf((int)lng);
		}
		else {
			throw new RuntimeException("invalid format for longitude, no check performed?");
		}
	}

	@SuppressWarnings({"cast", "rawtypes"})
	public String			format(java.lang.Integer internal_, String style)
	{
		@SuppressWarnings("UnnecessaryUnboxing")
		int internal = internal_.intValue();
		char orient = internal < 0 ? 'S' : 'N';
		if (internal < 0)
			internal = -internal;
		int degree = internal/10000000;
		internal -= degree*10000000;
		int min = internal*60/10000000;
		internal -= min*10000000/60;
		double sec = internal*3600/10000000.0;
		return String.format("%c%d°%d'%.3f\"", orient, degree, min, sec);
	}
}
