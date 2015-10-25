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

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;


public class JeeWebSession extends net.dryuf.srvui.AbstractSession
{
	public				JeeWebSession(HttpSession httpSession_)
	{
		httpSession = httpSession_;
	}

	public void			invalidate()
	{
		httpSession.invalidate();
	}

	public String			getSessionId()
	{
		return httpSession.getId();
	}

	public void			removeAttribute(String name)
	{
		httpSession.removeAttribute(name);
	}

	public void		   	setAttribute(String name, Object value)
	{
		httpSession.setAttribute(name, value);
	}

	public Object			getAttribute(String name)
	{
		return httpSession.getAttribute(name);
	}

	@Override
	public Map<String, Object>	getAllAttributes()
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Enumeration<String> names = httpSession.getAttributeNames(); names.hasMoreElements(); ) {
			String name = names.nextElement();
			map.put(name, httpSession.getAttribute(name));
		}
		return map;
	}

	protected HttpSession		httpSession;

}
