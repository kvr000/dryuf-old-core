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

package net.dryuf.srvui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.dryuf.io.FileData;
import org.apache.commons.lang3.StringUtils;


public class DummyRequest extends AbstractRequest
{
	public				DummyRequest()
	{
		session = new DummySession();
		response = new DummyResponse();
		params = new HashMap<String, String>();
	}

	public String			getUri()
	{
		return path+(!StringUtils.isEmpty(queryString) ? "?"+queryString : "");
	}

	public String			getParam(String param)
	{
		return params.get(param);
	}

	public String			getParamDefault(String param, String defaultValue)
	{
		String value = getParam(param);
		return value == null ? defaultValue : value;
	}

	public String			getHeader(String header)
	{
		return null;
	}

	public long			getDateHeader(String header)
	{
		return -1;
	}

	public long			getLongHeader(String header)
	{
		return -1;
	}

	public Object			getServletContext()
	{
		throw new UnsupportedOperationException();
	}

	public FileData			getFile(String param)
	{
		return null;
	}

	public void			resetData()
	{
	}

	public Session			getSession()
	{
		return session;
	}

	public Session			forceSession()
	{
		return session;
	}

	@Override
	public void invalidateSession()
	{
		session.invalidate();
	}

	public Response			getResponse()
	{
		return response;
	}

	public void			addParam(String name, String value)
	{
		this.params.put(name, value);
	}

	public void                     addParams(Map<String, String> added)
	{
		for (Entry<String, String> x: added.entrySet()) {
			this.params.put(x.getKey(), x.getValue());
		}
	}

	@Override
	public String			getCookie(String name)
	{
		return null;
	}

	@Override
	public String			getRemoteHost()
	{
		return "NA";
	}

	@Override
	public String			getContextPath()
	{
		return "";
	}

	protected String		path = "";

	public String			getPath()
	{
		return this.path;
	}

	public void			setPath(String path_)
	{
		this.path = path_;
	}

	protected String		queryString = "";

	public String			getQueryString()
	{
		return this.queryString;
	}

	public void			setQueryString(String queryString_)
	{
		this.queryString = queryString_;
	}

	protected String		method = "GET";

	public String			getMethod()
	{
		return this.method;
	}

	public void			setMethod(String method_)
	{
		this.method = method_;
	}

	protected String		requestContentType = "application/json";

	public String			getRequestContentType()
	{
		return this.requestContentType;
	}

	public void			setRequestContentType(String requestContentType_)
	{
		this.requestContentType = requestContentType_;
	}

	protected java.io.InputStream	inputStream;

	public java.io.InputStream	getInputStream()
	{
		return this.inputStream;
	}

	public void			setInputStream(java.io.InputStream inputStream_)
	{
		this.inputStream = inputStream_;
	}

	protected Session		session;
	protected Response		response;

	protected Map<String, String>	params;
}
