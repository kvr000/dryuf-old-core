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

import java.io.Serializable;

import net.dryuf.core.CallerContext;


/**
 * Wrapping class for managing the entities data.
 *
 * This wrapper contains reference to entity, related caller context and potentially dynamic view data.
 */
public class EntityHolder<TYPE> extends java.lang.Object implements Serializable
{
	private static final long	serialVersionUID = 1L;

	public				EntityHolder(TYPE entity_, CallerContext role_)
	{
		entity = entity_;
		role = role_;
	}

	public static EntityHolder<?>	createRoleOnly(CallerContext role)
	{
		return new EntityHolder<Object>(null, role);
	}

	public static <ET> EntityHolder<?> createFull(ET entity, CallerContext role)
	{
		return new EntityHolder<Object>(entity, role);
	}

	public boolean			checkAccess(String rolename)
	{
		return role.checkRole(rolename);
	}

	public static <GTYPE> GTYPE	getFrom(EntityHolder<GTYPE> ent)
	{
		return ent != null ? ent.getEntity() : null;
	}

	public EntityView		needView()
	{
		if (this.view == null)
			this.view = new net.dryuf.core.AbstractEntityView();
		return this.view;
	}

	protected TYPE			entity;

	public TYPE			getEntity()
	{
		return this.entity;
	}

	public void			setEntity(TYPE entity_)
	{
		this.entity = entity_;
	}

	protected CallerContext		role;

	public CallerContext		getRole()
	{
		return this.role;
	}

	protected EntityView		view;

	public EntityView		getView()
	{
		return this.view;
	}

	public void			setView(EntityView view_)
	{
		this.view = view_;
	}
}
