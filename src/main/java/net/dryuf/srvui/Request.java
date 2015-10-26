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

import java.io.InputStream;

import net.dryuf.core.Textual;
import net.dryuf.io.FileData;


public interface Request
{
	public Response			getResponse();

	public String			getMethod();

	public String			getContextPath();

	public String			getRequestContentType();

	public String			getParam(String param);

	public String			getParamDefault(String param, String defaultValue);

	public String			getParamMandatory(String param);

	public <T> T			getTextual(String param, Textual<T> textual);

	public <T> T			getTextualDefault(String param, Textual<T> textual, T defaultValue);

	public <T> T			getTextualMandatory(String param, Textual<T> textual);

	public String			getHeader(String header);

	public long			getDateHeader(String header);

	public long			getLongHeader(String header);

	public String			getCookie(String name);

	public abstract InputStream	getInputStream();

	public String			getUri();

	public abstract String		getPath();

	public abstract String		getQueryString();

	public abstract Object		getServletContext();

	public abstract FileData	getFile(String param);

	public abstract Session		getSession();

	public abstract Session		forceSession();

	public abstract void		invalidateSession();

	public String			getRemoteHost();
}
