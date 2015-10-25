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

package net.dryuf.io;

import java.io.Reader;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class IoUtil extends java.lang.Object
{
	public static byte[]		readFullBytes(InputStream reader)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[16384];
		int readed;
		try {
			while ((readed = reader.read(buf, 0, buf.length)) > 0)
				out.write(buf, 0, readed);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return out.toByteArray();
	}

	public static String		readFullString(Reader reader)
	{
		StringBuffer out = new StringBuffer();
		char[] buf = new char[16384];
		int readed;
		try {
			while ((readed = reader.read(buf, 0, buf.length)) > 0)
				out.append(buf, 0, readed);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return out.toString();
	}
}
