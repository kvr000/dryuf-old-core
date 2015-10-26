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

import org.apache.commons.lang3.RandomUtils;


public class TextUtil extends java.lang.Object
{
	public static String		transliterate(String text)
	{
		//Transliterator transliterator = Transliterator.createFromRules("Any-Latin", "Any-Latin; NFD; NFC", Transliterator.FORWARD);
		//return transliterator.transliterate(text);
		return org.apache.commons.lang3.StringUtils.stripAccents(text);
	}

	public static String		convertNameToDisplay(String name)
	{
		name = name.toLowerCase();
		name = transliterate(name);
		name = name.replaceAll("\\s", "-");
		name = name.replaceAll("--+", "-");
		name = name.replaceAll("(^-+|-+$)", "");
		return name;
	}

	public static String            generateCode(int length)
	{
		// avoid generating visually similar characters, such as 0, O, 1, l, 7
		byte[] out = new byte[length];
		for (int i = 0; i < out.length; i++)
			out[i] = CODE_CHARS[RandomUtils.nextInt(0, CODE_CHARS.length)];
		return new String(out);
	}

	public static final byte[]      CODE_CHARS = new byte[]{
		// perl -pe 's/(\w)/sprintf("%d", ord($1))/ge'
		'2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'D', 'E', 'F', 'G', 'H', 'J', 'M', 'N', 'Q', 'R', 'T',
		'a', 'b', 'd', 'e', 'f', 'h', 'i', 'j', 'm', 'n', 'q', 'r', 't'
	};
}
