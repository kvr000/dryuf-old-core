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

package net.dryuf.net.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class UrlUtil extends java.lang.Object
{
	public static String		encodeUrl(String path)
	{
		try {
			return URLEncoder.encode(path, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String		appendQuery(String url, String query)
	{
		if (url.indexOf('?') < 0)
			url += "?";
		else if (url.charAt(url.length()-1) != '?' && url.charAt(url.length()-1) != '&')
			url += "&";
		return url+query;
	}

	public static String		appendOptionalQuery(String url, String query)
	{
		return StringUtils.isEmpty(query) ? url : appendQuery(url, query);
	}

	public static String		appendParameter(String url, String parameter, String value)
	{
		return appendQuery(url, encodeUrl(parameter)+"="+encodeUrl(value));
	}

	public static String		buildQueryString(Map<String, String> params)
	{
		String query = "?";
		for (Map.Entry<String, String> entry: params.entrySet()) {
			query = appendParameter(query, entry.getKey(), entry.getValue());
		}
		return query.substring(1);
	}

	public static String		getReversePath(String url)
	{
		return url.replaceAll(".+?/", "../");
	}

	public static String		truncateToDir(String url)
	{
		int p;
		int end = url.indexOf('?');
		if (end < 0)
			end = url.length();
		if ((p = url.lastIndexOf('/', end-1)) < 0)
			p = -1;
		return url.substring(0, p+1);
	}

	public static String		truncateToParent(String url)
	{
		url = truncateToDir(url);
		int p;
		if ((p = url.lastIndexOf('/', url.length()-2)) < 0)
			p = p-1;
		return url.substring(0, p+1);
	}
}
