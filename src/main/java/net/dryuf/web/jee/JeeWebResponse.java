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

package net.dryuf.web.jee;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JeeWebResponse extends java.lang.Object implements net.dryuf.srvui.Response
{
	public				JeeWebResponse(JeeWebRequest request)
	{
		servletRequest = request.getServletRequest();
		servletResponse = request.getServletResponse();
	}

	@Override
	public int			getCurrentStatus()
	{
		return statusCode;
	}

	@Override
	public void			setContentType(String mime)
	{
		servletResponse.setContentType(mime);
	}

	@Override
	public void			sendHeader(String header, String value)
	{
		getServletResponse().setHeader(header,  value);
	}

	@Override
	public void			redirect(String uri)
	{
		if (uri.startsWith("/"))
			uri = servletRequest.getContextPath()+uri;
		sendStatus(HttpServletResponse.SC_FOUND, null);
		getServletResponse().addHeader("Location", uri);
	}

	@Override
	public void			sendStatus(int code, String msg)
	{
		statusCode = code;
		try {
			servletResponse.setStatus(code);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void			sendError(int code, String msg)
	{
		statusCode = code;
		sendStatus(code, msg);
	}

	@Override
	public java.io.OutputStream	getOutputStream()
	{
		try {
			return servletResponse.getOutputStream();
		}
		catch (java.io.IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public java.io.PrintWriter	getWriter()
	{
		try {
			return servletResponse.getWriter();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void			setHeader(String header, String content)
	{
		servletResponse.setHeader(header, content);
	}

	@Override
	public void			setLongHeader(String header, long value)
	{
		servletResponse.setHeader(header, String.valueOf(value));
	}

	@Override
	public void			setDateHeader(String header, long value)
	{
		servletResponse.setDateHeader(header, value);
	}

	@Override
	public void			setCookie(String name, String value, int maxAge)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(servletRequest.getContextPath());
		cookie.setMaxAge(maxAge);
		this.servletResponse.addCookie(cookie);
	}

	protected HttpServletRequest	servletRequest;

	public HttpServletRequest	getServletRequest()
	{
		return this.servletRequest;
	}

	protected HttpServletResponse	servletResponse;

	public HttpServletResponse	getServletResponse()
	{
		return this.servletResponse;
	}

	protected int			statusCode;
}
