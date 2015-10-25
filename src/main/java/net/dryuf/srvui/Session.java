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

import java.util.Map;

import net.dryuf.core.Textual;


public interface Session
{
	public abstract void		invalidate();
	public abstract String		getSessionId();

	public abstract void		removeAttribute(String name);
	public abstract void		setAttribute(String name, Object value);
	public abstract Object		getAttribute(String name);
	public abstract <T> T		getAttributeDefault(String name, T defaultValue);
	public abstract <T> T		getAttributeTextual(String name, Textual<T> textual);

	public Map<String, Object>	getAllAttributes();

	public void			setUserId(Object login);
	public Object			getUserId();
	public void			setUsername(String name);
	public String			getUsername();
}
