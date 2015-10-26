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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import net.dryuf.io.FileData;
import net.dryuf.srvui.Response;


public class JeeWebRequest extends net.dryuf.srvui.AbstractRequest
{
	public				JeeWebRequest(HttpServletRequest servletRequest_, HttpServletResponse servletResponse_)
	{
		servletRequest = servletRequest_;
		servletResponse = servletResponse_;
	}

	@Override
	public String			getContextPath()
	{
		return servletRequest.getServletContext().getContextPath();
	}

	@Override
	public String			getMethod()
	{
		return servletRequest.getMethod();
	}

	@Override
	public String			getRequestContentType()
	{
		String ct = servletRequest.getContentType();
		int p;
		if ((p = ct.indexOf(';')) >= 0)
			ct = ct.substring(0, p);
		return ct;
	}

	@Override
	public String			getParam(String param)
	{
		String value = servletRequest.getParameter(param);
		if (value == null)
			value = additionalParams.get(param);
		return value;
	}

	@Override
	public String			getCookie(String name)
	{
		Cookie cookie = WebUtils.getCookie(this.servletRequest, name);
		if (cookie != null)
			return cookie.getValue();
		return null;
	}

	@Override
	public String			getUri()
	{
		return servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
	}

	@Override
	public String			getPath()
	{
		return servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
	}

	@Override
	public String			getQueryString()
	{
		return servletRequest.getQueryString();
	}

	@Override
	public FileData			getFile(String param)
	{
		{
			final Part part;
			try {
				part = servletRequest.getPart(param);
			}
			catch (RuntimeException ex) {
				throw ex;
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			if (part != null) {
				return new FileData() {
					@Override
					public InputStream getInputStream()
					{
						try {
							return part.getInputStream();
						}
						catch (IOException e) {
							throw new RuntimeException(e);
						}
					}

					@Override
					public String getContentType()
					{
						return part.getContentType();
					}

					@Override
					public String getName()
					{
						return "filename.bin";
					}

					@Override
					public long getSize()
					{
						return part.getSize();
					}

					@Override
					public long getModifiedTime()
					{
						return -1;
					}

					@Override
					public void close()
					{
					}
				};
			}
		}

		if (servletRequest instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest msr = (MultipartHttpServletRequest)servletRequest;
			final MultipartFile springFile;
			if ((springFile = msr.getFile(param)) != null) {
				return new FileData() {
					@Override
					public InputStream getInputStream()
					{
						try {
							return springFile.getInputStream();
						}
						catch (IOException e) {
							throw new RuntimeException(e);
						}
					}

					@Override
					public String getContentType()
					{
						return springFile.getContentType();
					}

					@Override
					public String getName()
					{
						return springFile.getOriginalFilename();
						//return null;
					}

					@Override
					public long getSize()
					{
						return springFile.getSize();
					}

					@Override
					public long getModifiedTime()
					{
						return -1;
					}

					@Override
					public void close()
					{
					}

				};
			}
		}
		return null;
	}

	@Override
	public Object			getServletContext()
	{
		return servletRequest.getServletContext();
	}

	@Override
	public net.dryuf.srvui.Session		getSession()
	{
		if (session == null) {
			HttpSession httpSession = servletRequest.getSession(false);
			if (httpSession == null)
				return null;
			session = new JeeWebSession(httpSession);
		}
		return session;
	}

	@Override
	public net.dryuf.srvui.Session		forceSession()
	{
		if (session == null)
			session = new JeeWebSession(servletRequest.getSession(true));
		return session;
	}

	@Override
	public void			invalidateSession()
	{
		HttpSession servletSession = servletRequest.getSession(false);
		if (servletSession != null)
			servletSession.invalidate();
		session = null;
	}

	@Override
	public java.io.InputStream	getInputStream()
	{
		try {
			return servletRequest.getInputStream();
		}
		catch (java.io.IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String			getRemoteHost()
	{
		return servletRequest.getRemoteHost();
	}

	@Override
	public Response			getResponse()
	{
		if (response == null)
			response = new JeeWebResponse(this);
		return response;
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

	protected JeeWebSession		session;

	protected JeeWebResponse	response;

	Map<String, String>		additionalParams = new HashMap<String, String>();

	Map<String, FileItem>		files;

	@Override
	public String getHeader(String header)
	{
		return servletRequest.getHeader(header);
	}

	@Override
	public long getDateHeader(String header)
	{
		return servletRequest.getDateHeader(header);
	}

	@Override
	public long getLongHeader(String header)
	{
		String stringHeader = getHeader(header);
		if (stringHeader == null)
			return -1;
		return Long.valueOf(stringHeader);
	}
};
