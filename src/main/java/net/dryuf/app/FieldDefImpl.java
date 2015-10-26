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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.Mandatory;
import net.dryuf.meta.AssocDef;
import net.dryuf.core.Textual;
import net.dryuf.textual.DisplayUse;
import net.dryuf.textual.TextualUse;
import net.dryuf.meta.ReferenceDef;

public class FieldDefImpl<FT> extends java.lang.Object implements FieldDef<FT>
{
	@SuppressWarnings("unchecked")
	@Override
	public FT			getValue(Object o)
	{
		try {
			if (getter != null)
				return (FT)getter.invoke(o);
			return (FT)field.get(o);
		}
		catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void			setValue(Object o, FT value)
	{
		try {
			if (setter != null) {
				setter.invoke(o, value);
				return;
			}
			field.set(o, value);
		}
		catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	protected String		name;

	public String			getName()
	{
		return this.name;
	}

	public FieldDefImpl<FT>		setName(String name_)
	{
		this.name = name_;
		return this;
	}

	protected String		path;

	public String			getPath()
	{
		return this.path;
	}

	public FieldDefImpl<FT>		setPath(String path_)
	{
		this.path = path_;
		return this;
	}

	protected Class<FT>		type;

	public Class<FT>		getType()
	{
		return this.type;
	}

	public FieldDefImpl<FT>		setType(Class<FT> type_)
	{
		this.type = type_;
		return this;
	}

	protected int			assocType;

	public int			getAssocType()
	{
		return this.assocType;
	}

	public FieldDefImpl<FT>		setAssocType(int assocType_)
	{
		this.assocType = assocType_;
		return this;
	}

	protected ClassMeta<FT>		embedded;

	public ClassMeta<FT>		getEmbedded()
	{
		return this.embedded;
	}

	public FieldDefImpl<FT>		setEmbedded(ClassMeta<FT> embedded_)
	{
		this.embedded = embedded_;
		return this;
	}

	protected Class<?>		childClass;

	public Class<?>			getChildClass()
	{
		return this.childClass;
	}

	public FieldDefImpl<FT>		setChildClass(Class<?> childClass_)
	{
		this.childClass = childClass_;
		return this;
	}

	protected Class<?>		assocClass;

	public Class<?>			getAssocClass()
	{
		return this.assocClass;
	}

	public FieldDefImpl<FT>		setAssocClass(Class<?> assocClass_)
	{
		this.assocClass = assocClass_;
		return this;
	}

	protected Class<Textual<FT>>	textual;

	public Class<Textual<FT>>	getTextual()
	{
		return this.textual;
	}

	@SuppressWarnings("unchecked")
	public FieldDefImpl<FT>		setTextual(Class<? extends Textual<FT>> textual_)
	{
		this.textual = (Class<Textual<FT>>)textual_;
		return this;
	}

	@SuppressWarnings("unchecked")
	public Class<Textual<FT>>	needTextual()
	{
		if (textual == null) {
			throw new RuntimeException("textual undefined for "+name);
		}
		return textual;
	}

	protected int			align;

	public int			getAlign()
	{
		return this.align;
	}

	public FieldDefImpl<FT>		setAlign(int align_)
	{
		this.align = align_;
		return this;
	}

	protected String		display;

	public String			getDisplay()
	{
		return this.display;
	}

	public FieldDefImpl<FT>		setDisplay(String display_)
	{
		this.display = display_;
		return this;
	}

	protected boolean		mandatory;

	public boolean			getMandatory()
	{
		return this.mandatory;
	}

	public FieldDefImpl<FT>		setMandatory(boolean mandatory_)
	{
		this.mandatory = mandatory_;
		return this;
	}

	protected FT			doMandatory;

	public FT			getDoMandatory()
	{
		return this.doMandatory;
	}

	public FieldDefImpl<FT>		setDoMandatory(FT doMandatory_)
	{
		this.doMandatory = doMandatory_;
		return this;
	}

	protected FieldRoles		roles;

	public FieldRoles		getRoles()
	{
		return this.roles;
	}

	public FieldDefImpl<FT>		setRoles(FieldRoles roles_)
	{
		this.roles = roles_;
		return this;
	}

	protected ReferenceDef		referenceDef;

	public ReferenceDef		getReferenceDef()
	{
		return this.referenceDef;
	}

	public FieldDefImpl<FT>		setReferenceDef(ReferenceDef referenceDef_)
	{
		this.referenceDef = referenceDef_;
		return this;
	}

	protected Field			field;

	public Field			getField()
	{
		return this.field;
	}

	public FieldDefImpl<FT>		setField(Field field_)
	{
		this.field = field_;
		return this;
	}

	protected Method		getter;

	public Method			getGetter()
	{
		return this.getter;
	}

	public FieldDefImpl<FT>		setGetter(Method getter_)
	{
		this.getter = getter_;
		return this;
	}

	protected Method		setter;

	public Method			getSetter()
	{
		return this.setter;
	}

	public FieldDefImpl<FT>		setSetter(Method setter_)
	{
		this.setter = setter_;
		return this;
	}
}
