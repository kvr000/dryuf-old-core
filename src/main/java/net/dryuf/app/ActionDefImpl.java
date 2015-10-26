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


import net.dryuf.meta.ActionDef;

import java.lang.annotation.Annotation;

public class ActionDefImpl extends java.lang.Object implements ActionDef
{
	String			name;

	public String			name()
	{
		return this.name;
	}

	public ActionDefImpl		setName(String name_)
	{
		this.name = name_;
		return this;
	}

	boolean			isStatic;

	public boolean			isStatic()
	{
		return this.isStatic;
	}

	public ActionDefImpl		setIsStatic(boolean isStatic_)
	{
		this.isStatic = isStatic_;
		return this;
	}

	String			guiDef;

	public String			guiDef()
	{
		return this.guiDef;
	}

	public ActionDefImpl		setGuiDef(String guiDef_)
	{
		this.guiDef = guiDef_;
		return this;
	}

	String			formName;

	public String			formName()
	{
		return this.formName;
	}

	public ActionDefImpl		setFormName(String formName_)
	{
		this.formName = formName_;
		return this;
	}

	String			formActioner;

	public String			formActioner()
	{
		return this.formActioner;
	}

	public ActionDefImpl		setFormActioner(String formActioner_)
	{
		this.formActioner = formActioner_;
		return this;
	}

	String			reqMode;

	public String			reqMode()
	{
		return this.reqMode;
	}

	public ActionDefImpl		setReqMode(String reqMode_)
	{
		this.reqMode = reqMode_;
		return this;
	}

	String			roleAction;

	public String			roleAction()
	{
		return this.roleAction;
	}

	public ActionDefImpl		setRoleAction(String roleAction_)
	{
		this.roleAction = roleAction_;
		return this;
	}

	public Class<? extends Annotation> annotationType()
	{
		return ActionDef.class;
	}
}
