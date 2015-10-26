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

package net.dryuf.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Special implementation of CallerContext providing handy methods to amend the roles.
 */
public class RoleContext extends net.dryuf.core.SubCallerContext
{
	public static RoleContext	createAdding(CallerContext parentContext, List<String> roles)
	{
		RoleContext rc = new RoleContext(parentContext);
		for (String role: roles) {
			rc.localRoles.add(role);
		}
		return rc;
	}

	public static RoleContext	createReplace(CallerContext parentContext, List<String> roles)
	{
		RoleContext rc = new RoleContext(parentContext);
		for (String role: roles) {
			rc.localRoles.add(role);
		}
		return rc;
	}

	/**
	 * Creates new CallerContext with roles mapped according to mapping specification.
	 *
	 * @param callerContext
	 * 	parent context
	 * @param mapping
	 * 	array of pairs, first of them defining new role and second specifying the dependent role
	 *
	 * @return
	 * 	new role context
	 */
	static public RoleContext	createMapped(CallerContext parentContext, String[] mapping)
	{
		RoleContext newContext = new RoleContext(parentContext);
		for (int i = 0; i < mapping.length; i += 2) {
			if (parentContext.checkRole(mapping[i+1]))
				newContext.localRoles.add(mapping[i]);
		}
		return newContext;
	}

	public				RoleContext(CallerContext parentContext)
	{
		super(parentContext);
	}

	public boolean			checkRole(String role)
	{
		return localRoles.contains(role) || this.parentContext.checkRole(role);
	}

	public Set<String>		getRoles()
	{
		localRoles.addAll(parentContext.getRoles());
		return localRoles;
	}

	protected Set<String>		localRoles = new HashSet<String>();
}
