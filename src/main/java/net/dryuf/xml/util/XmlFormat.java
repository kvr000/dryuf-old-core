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

package net.dryuf.xml.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Function;

import java.nio.charset.StandardCharsets;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Textual;
import net.dryuf.core.StringUtil;


public class XmlFormat extends java.lang.Object
{
	public				XmlFormat()
	{
	}

	public static Function<String, String> escaper()
	{
		return new Function<String, String>() { @Override public String apply(String s) { return escapeXml(s); } };
	}

	public static String		escapeXml(String s)
	{
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
	}

	public static String		formatJsString(String s)
	{
		return "\""+s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t")+"\"";
	}

	public static String		joinEscaped(String separator, Collection<String> input)
	{
		return StringUtil.joinEscaped(escaper(), separator, input);
	}

	public static void		appendAttributeSb(StringBuilder out, String name, String value)
	{
		out.append(" ");
		out.append(name);
		out.append("=\"");
		out.append(escapeXml(value));
		out.append("\"");
	}

	public static void		formatStream(OutputStream outputStream, CallerContext callerContext, String fmt, Object... args)
	{
		StringBuilder out = new StringBuilder();
		formatSb(out, callerContext, fmt, args);
		try {
			outputStream.write(out.toString().getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void		formatSb(StringBuilder out, CallerContext callerContext, String fmt, Object... args)
	{
		int ai = 0;
		for (int i = 0; i < fmt.length(); i++) {
			if (fmt.charAt(i) == '%') {
				switch (fmt.charAt(++i)) {
				case '%':
					out.append('%');
					break;

				case 's':
					out.append((String)args[ai++]);
					break;

				case 'S':
					out.append(escapeXml((String)args[ai++]));
					break;

				case 'A':
					out.append("\""); out.append(escapeXml((String)args[ai++])); out.append("\"");
					break;

				case 'K':
					@SuppressWarnings("unchecked")
					net.dryuf.core.Textual<Object> textual = (Textual<Object>)args[ai++];
					out.append(escapeXml(textual.format(args[ai++], null)));
					break;

				case 'O':
					out.append(escapeXml(args[ai++].toString()));
					break;

				case 'R':
					throw new RuntimeException("unsupported yet");

				case 'U':
					throw new RuntimeException("unsupported yet");

				case 'W':
					{
						String msg = (String)args[ai++];
						Object cls = args[ai++];
						out.append(escapeXml(callerContext.getUiContext().localize((String)cls, msg)));
					}
					break;

				default:
					throw new RuntimeException("invalid format character: "+fmt.charAt(i-1));
				}
			}
			else {
				out.append(fmt.charAt(i));
			}
		}
	}
};
