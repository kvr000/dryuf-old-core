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

public class LatitudeTextual extends LatitudeBaseTextual
{
	public String			check(String text, String style)
	{
		String groups[];
		if ((groups = StringUtil.matchText("^((N|S)\\s*(\\d{1,2})°\\s*(\\d{1,2})\\\'\\s*(\\d{1,2}(\\.\\d*)?)\\\"|[+-]?\\d+\\.\\d*)$", text)) != null) {
			double lat;
			if (groups[3] != null) {
				lat = java.lang.Integer.parseInt(groups[3])+java.lang.Float.parseFloat(groups[4])/60+java.lang.Float.parseFloat(groups[5])/3600;
				if (groups[2].equals("N"))
					lat = -lat;
			}
			else {
				throw new RuntimeException("todo");
			}
			return validateDouble(lat*10000000);
		}
		else {
			return callerContext.getUiContext().localize(LatitudeTextual.class, "format N|S dd°mm'ss[.sss]\" required");
		}
	}

	public java.lang.Integer	convertInternal(String text, String style)
	{
		String groups[];
		if ((groups = StringUtil.matchText("^((N|S)\\s*(\\d{1,2})°\\s*(\\d{1,2})\\\'\\s*(\\d{1,2}(\\.\\d*)?)\\\"|[+-]?\\d+\\.\\d*)$", text)) != null) {
			double lat;
			lat = java.lang.Integer.parseInt(groups[3])+java.lang.Float.parseFloat(groups[4])/60+java.lang.Float.parseFloat(groups[5])/3600;
			if (groups[2].equals("S"))
				lat = -lat;
			lat *= 10000000;
			String error;
			if ((error = validateDouble(lat)) != null)
				throw new RuntimeException(error);
			return java.lang.Integer.valueOf((int)lat);
		}
		else {
			throw new RuntimeException("Invalid format for longitude, no check performed?");
		}
	}

	public String			format(java.lang.Integer internal_, String style)
	{
		int internal = internal_;
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

	public java.lang.Integer	convertKeyInternal(String text)
	{
		return java.lang.Integer.valueOf(text);
	}

	public String			validate(java.lang.Integer internal)
	{
		if (internal < -900000000 || internal > 900000000)
			return getUiContext().localize(LatitudeTextual.class, "Latitude must be within interval -90 - 90");
		return null;
	}
}
