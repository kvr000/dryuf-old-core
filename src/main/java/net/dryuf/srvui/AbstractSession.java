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

import net.dryuf.core.Textual;


public abstract class AbstractSession extends java.lang.Object implements net.dryuf.srvui.Session
{
	public				AbstractSession()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T			getAttributeDefault(String name, T defaultValue)
	{
		T value;
		if ((value = (T)getAttribute(name)) == null)
			value = defaultValue;
		return value;
	}

	@Override
	public <T> T			getAttributeTextual(String name, Textual<T> textual)
	{
		String value;
		if ((value = (String)getAttribute(name)) == null)
			return null;
		return textual.convert(value, null);
	}

	@Override
	public void			setUserId(Object userId)
	{
		this.userId = userId;
		setAttribute(Session.class.getName()+"._userId", userId);
	}

	@Override
	public Object			getUserId()
	{
		if (this.userId == null)
			this.userId = getAttribute(Session.class.getName()+"._userId");
		return this.userId;
	}

	@Override
	public void			setUsername(String userName)
	{
		this.userName = userName;
		setAttribute(Session.class.getName()+"._userName", userName);
	}

	@Override
	public String			getUsername()
	{
		if (this.userName == null)
			this.userName = (String)getAttribute(Session.class.getName()+"._userName");
		return this.userName;
	}

	protected Object		userId;
	protected String		userName;
};
