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

public class FileTextual extends net.dryuf.textual.LineTrimTextual
{
	public static String		checkFileName(CallerContext callerContext, String filename)
	{
		// avoid slashes as path separator
		if (filename.indexOf("/") >= 0)
			return callerContext.getUiContext().localize(FileTextual.class, "File name cannot contain '/'");
		// avoid colons as protocol separator
		if (filename.indexOf(":") >= 0)
			return callerContext.getUiContext().localize(FileTextual.class, "File name cannot contain ':'");
		// avoid current and upper directories
		if (filename.equals(".") || filename.equals(".."))
			return callerContext.getUiContext().localize(FileTextual.class, "File name cannot be . or ..");
		return null;
	}

	public String			validate(String input)
	{
		return checkFileName(getCallerContext(), input);
	}
}
