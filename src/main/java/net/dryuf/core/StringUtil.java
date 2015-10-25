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

package net.dryuf.core;

import java.util.Collection;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil extends java.lang.Object
{
	public static final String[]	STRING_EMPTY_ARRAY = new String[0];

	public static String		capitalize(String s)
	{
		if (s.isEmpty())
			throw new StringIndexOutOfBoundsException(0);
		return s.substring(0, 1).toUpperCase()+s.substring(1);
	}

	public static String		uncapitalize(String s)
	{
		return s.substring(0, 1).toLowerCase()+s.substring(1);
	}

	public static String		joinArgs(String sep, String... input)
	{
		return joinArray(sep, input);
	}

	public static String		joinArray(String sep, String[] input)
	{
		if (input.length == 0)
			return "";
		StringBuilder out = new StringBuilder(input[0]);
		for (int i = 1; i < input.length; i++)
			out.append(sep).append(input[i]);
		return out.toString();
	}

	public static String		join(String sep, Collection<String> input)
	{
		StringBuilder out = new StringBuilder();
		for (String s: input) {
			out.append(s).append(sep);
		}
		int l = out.length();
		if (l == 0)
			return "";
		out.replace(l-sep.length(), l, "");
		return out.toString();
	}

	public static String		joinEscaped(Function<String, String> escaper, String sep, Collection<String> input)
	{
		int counter = 0;
		StringBuilder out = new StringBuilder();
		for (String s: input) {
			if (counter++ > 0)
				out.append(sep);
			out.append(escaper.apply(s));
		}
		return out.toString();
	}

	/**
	 * Concatenates arguments of strings, ignoring nulls.
	 *
	 * @param sep
	 * 	string to separate entries
	 * @param input
	 * 	list of strings to concatenate
	 *
	 * @return
	 * 	concatenated strings
	 */
	public static String		joinValidArgs(String sep, String... input)
	{
		return joinValidArray(sep, input);
	}

	/**
	 * Concatenates array of strings, ignoring nulls.
	 *
	 * @param sep
	 * 	string to separate entries
	 * @param input
	 * 	list of strings to concatenate
	 *
	 * @return
	 * 	concatenated strings
	 */
	public static String		joinValidArray(String sep, String[] input)
	{
		int found = 0;
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			String s = input[i];
			if (s == null)
				continue;
			if (found++ > 0)
				out.append(sep);
			out.append(s);
		}
		return out.toString();
	}

	/**
	 * Concatenates collection of strings, ignoring nulls.
	 *
	 * @param sep
	 * 	string to separate entries
	 * @param input
	 * 	list of strings to concatenate
	 *
	 * @return
	 * 	concatenated strings
	 */
	public static String		joinValid(String sep, Collection<String> input)
	{
		int found = 0;
		StringBuilder out = new StringBuilder();
		for (String s: input) {
			if (s == null)
				continue;
			if (found++ > 0)
				out.append(sep);
			out.append(s);
		}
		return out.toString();
	}

	public static String		joinValidEscaped(Function<String, String> escaper, String sep, Collection<String> input)
	{
		int found = 0;
		StringBuilder out = new StringBuilder();
		for (String s: input) {
			if (s == null)
				continue;
			if (found++ > 0)
				out.append(sep);
			out.append(escaper.apply(s));
		}
		return out.toString();
	}

	public static String		defaultIfEmpty(String s, String defaultValue)
	{
		return s == null || s.isEmpty() ? defaultValue : s;
	}

	public static String[]		matchText(String regexp, String text)
	{
		Matcher matcher = Pattern.compile(regexp).matcher(text);
		if (!matcher.matches())
			return null;
		String[] groups = new String[(matcher.groupCount()+1)];
		for (int i = 0; i <= matcher.groupCount(); i++) {
			groups[i] = matcher.group(i);
		}
		return groups;
	}

	public static String		matchNeedGroup(String regexp, String text)
	{
		String groups[] = matchText(regexp, text);
		if (groups == null || groups.length < 2)
			throw new IllegalArgumentException("Failed to match '"+text+"' on '"+regexp+"'.");
		return groups[1];
	}

	public static String		matchNeedGroupSpecific(String regexp, String text, int group)
	{
		String groups[] = matchText(regexp, text);
		if (groups == null || groups.length <= group)
			throw new IllegalArgumentException("Failed to match '"+text+"' on '"+regexp+"'.");
		return groups[group];
	}
}
