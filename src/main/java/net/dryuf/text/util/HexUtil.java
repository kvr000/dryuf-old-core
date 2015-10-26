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

package net.dryuf.text.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Hexadecimal conversion utilities.
 */
public class HexUtil extends java.lang.Object
{
	/**
	 * Converts hexadecimal string into byte array.
	 *
	 * Whitespaces are silently ignored.
	 *
	 * @param input
	 * 	hexadecimal string
	 * @return
	 * 	bytes representing the input
	 */
	public static byte[]		decodeHex(String input)
	{
		try {
			return Hex.decodeHex(input.replaceAll("\\s", "").toCharArray());
		}
		catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts byte array into string.
	 *
	 * @param input
	 * 	byte array
	 * @return
	 * 	character array representing the original input
	 */
	public static char[]		encodeHex(byte[] input)
	{
		return Hex.encodeHex(input);
	}
}
