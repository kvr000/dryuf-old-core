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

package net.dryuf.app;


import net.dryuf.meta.FieldRoles;

import java.lang.annotation.Annotation;


public class FieldRolesImpl extends java.lang.Object implements FieldRoles
{
	public				FieldRolesImpl(String roleNew, String roleSet, String roleGet, String roleDel)
	{
		this.roleNew = roleNew;
		this.roleSet = roleSet;
		this.roleGet = roleGet;
		this.roleDel = roleDel;
	}

	String				roleNew;

	public String			roleNew()
	{
		return this.roleNew;
	}

	String				roleSet;

	public String			roleSet()
	{
		return this.roleSet;
	}

	String				roleGet;

	public String			roleGet()
	{
		return this.roleGet;
	}

	String				roleDel;

	public String			roleDel()
	{
		return this.roleDel;
	}

	public Class<? extends Annotation> annotationType()
	{
		return FieldRoles.class;
	}
}
