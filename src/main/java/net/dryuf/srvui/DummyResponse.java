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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


public class DummyResponse extends java.lang.Object implements Response
{
	public				DummyResponse()
	{
		outputStream = new ByteArrayOutputStream();
	}

	@Override
	public OutputStream		getOutputStream()
	{
		return outputStream;
	}

	@Override
	public PrintWriter		getWriter()
	{
		if (writer == null)
			writer = new PrintWriter(outputStream);
		return writer;
	}

	@Override
	public int			getCurrentStatus()
	{
		return this.statusCode;
	}

	public byte[]			getOutputData()
	{
		return outputStream.toByteArray();
	}

	@Override
	public void			setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	@Override
	public void			sendHeader(String header, String value)
	{
	}

	@Override
	public void			redirect(String uri)
	{
		this.redirected = uri;
	}

	@Override
	public void			sendError(int code, String msg)
	{
		statusCode = code;
	}

	@Override
	public void			sendStatus(int code, String msg)
	{
		statusCode = code;
	}

	@Override
	public void			setHeader(String header, String content)
	{
	}

	@Override
	public void			setLongHeader(String header, long value)
	{
	}

	@Override
	public void			setDateHeader(String header, long value)
	{
	}

	@Override
	public void			setCookie(String name, String value, int maxAge)
	{
	}

	protected ByteArrayOutputStream	outputStream;

	protected java.io.PrintWriter	writer;

	protected int			statusCode = 0;

	public int			getStatusCode()
	{
		return this.statusCode;
	}

	protected String		redirected = null;

	public String			getRedirected()
	{
		return this.redirected;
	}

	protected String		contentType = null;

	public String			getContentType()
	{
		return this.contentType;
	}
}
