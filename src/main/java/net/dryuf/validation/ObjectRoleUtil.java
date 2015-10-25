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

package net.dryuf.validation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.ClassMetaManager;
import net.dryuf.app.FieldDef;
import net.dryuf.core.ConversionUtil;
import net.dryuf.textual.TextualManager;
import net.dryuf.util.MapUtil;
import net.dryuf.validation.DataValidationErrors;
import org.springframework.validation.BindingResult;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.Textual;
import net.dryuf.textual.TextualUse;
import net.dryuf.meta.FieldOrder;
import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.PKeyDef;
import net.dryuf.meta.Mandatory;
import net.dryuf.meta.ViewInfo;
import net.dryuf.meta.ViewsList;


public class ObjectRoleUtil extends java.lang.Object
{
	private				ObjectRoleUtil()
	{
	}

	public static final boolean	checkMandatory(DataValidationErrors errors, CallerContext role, FieldDef<?> fieldDef)
	{
		if (fieldDef.getMandatory()) {
			errors.rejectField(fieldDef.getName(), role.getUiContext().localize(ObjectRoleUtil.class, "Field is mandatory"));
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static final <T> boolean	setWithRole(DataValidationErrors errors, T obj, CallerContext role, Map<String, Object> values)
	{
		return setWithRoleInternal(errors, obj, role, values, ClassMetaManager.openCached(role.getAppContainer(), (Class<T>)obj.getClass(), null));
	}

	@SuppressWarnings("unchecked")
	public static final <T> boolean	newWithRole(DataValidationErrors errors, T obj, CallerContext role, Map<String, Object> values)
	{
		return newWithRoleInternal(errors, obj, role, values, ClassMetaManager.openCached(role.getAppContainer(), (Class<T>) obj.getClass(), null));
	}

	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getWithRole(Object obj, CallerContext role)
	{
		return getWithRoleInternal(obj, role, ClassMetaManager.<Object>openCached(role.getAppContainer(), (Class<Object>)obj.getClass(), null));
	}

	protected static <T> Object	getScalarInternal(T obj, CallerContext role, FieldDef<?> fieldDef)
	{
		return fieldDef.getValue(obj);
	}

	protected static <T> boolean	setScalarInternal(DataValidationErrors errors, T obj, CallerContext role, FieldDef<Object> fieldDef, Object value)
	{
		Class<? extends Textual<?>> textualClass = fieldDef.getTextual();
		if (textualClass != null) {
			Object newValue = ConversionUtil.convertToClass(fieldDef.getType(), value);
			if (newValue == null) {
				if (!checkMandatory(errors, role, fieldDef))
					return false;
			}
			else {
				@SuppressWarnings("unchecked")
				Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe(textualClass, role);
				try {
					String err = textual.validate(newValue);
					if (err != null) {
						errors.rejectField(fieldDef.getName(), err);
						return false;
					}
				}
				catch (Exception ex) {
					throw new RuntimeException("Failed to validate field "+fieldDef.getName()+": "+ex.toString(), ex);
				}
			}
			fieldDef.setValue(obj, newValue);
		}
		else if (ConversionUtil.isScalarClass(fieldDef.getType())) {
			fieldDef.setValue(obj, ConversionUtil.convertToClass(fieldDef.getType(), value));
		}
		return true;
	}

	protected static <T> Object	getOrCreateEmbeddedInternal(T obj, FieldDef<Object> fieldDef)
	{
		Object value = fieldDef.getValue(obj);
		if (value == null) {
			value = fieldDef.getEmbedded().instantiate();
			fieldDef.setValue(obj, value);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	protected static <T> Map<String, Object> getWithRoleInternal(T obj, CallerContext role, ClassMeta<T> classMeta)
	{
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();

		for (FieldDef<?> fieldDef: classMeta.getFields()) {
			FieldRoles roles = fieldDef.getRoles();
			if (!role.checkRole(roles.roleGet()))
				continue;
			try {
				Object value;
				switch (fieldDef.getAssocType()) {
				case FieldDef.AST_None:
				case FieldDef.AST_Reference:
				case FieldDef.AST_Compos:
					ClassMeta<Object> embeddedMeta;
					if ((embeddedMeta = (ClassMeta<Object>) fieldDef.getEmbedded()) != null) {
						value = fieldDef.getValue(obj);
						if (value != null)
							value = getWithRoleInternal(value, role, embeddedMeta);
					}
					else {
						value = getScalarInternal(obj, role, fieldDef);
					}
					break;

				case FieldDef.AST_Children:
					{
						Collection<?> orig = (Collection<?>)fieldDef.getValue(obj);
						Collection<Object> converted = new LinkedList<Object>();

						for (Object element: orig)
							converted.add(getWithRole(element, role));

						value = converted;
					}
					break;

				default:
					throw new RuntimeException(("Unknown association type: "+fieldDef.getAssocType()));
				}
				result.put(fieldDef.getName(), value);
			}
			catch (Exception ex) {
				throw new RuntimeException("Failed to get "+fieldDef.getName()+": "+ex.toString(), ex);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected static <T> boolean	setWithRoleInternal(DataValidationErrors errors, T obj, CallerContext role, Map<String, Object> values, ClassMeta<T> classMeta)
	{

		for (Entry<String, Object> entry: values.entrySet()) {
			FieldDef<Object> fieldDef = (FieldDef<Object>)classMeta.getField(entry.getKey());
			try {
				FieldRoles roles = fieldDef.getRoles();
				ClassMeta<Object> embeddedMeta;
				if ((embeddedMeta = fieldDef.getEmbedded()) != null) {
					Map<String, Object> inputMap;
					try {
						inputMap = (Map<String, Object>) entry.getValue();
					}
					catch (ClassCastException ex) {
						throw new RuntimeException("Failed to cast field "+fieldDef.getName()+" to Map: "+ex.toString());
					}
					errors.pushNestedPath(fieldDef.getName());
					setWithRoleInternal(errors, getOrCreateEmbeddedInternal(obj, fieldDef), role, inputMap, embeddedMeta);
					errors.popNestedPath();
				}
				else {
					if (!role.checkRole(roles.roleSet()))
						throw new SecurityException("Denied to set "+obj.getClass().getName()+"."+fieldDef.getName());
					setScalarInternal(errors, obj, role, fieldDef, entry.getValue());
				}
			}
			catch (Exception ex) {
				throw new RuntimeException("Failed to set "+fieldDef.getName()+": "+ex.toString(), ex);
			}
		}
		return !errors.hasErrors();
	}

	@SuppressWarnings("unchecked")
	protected static <T> boolean	newWithRoleInternal(DataValidationErrors errors, T obj, CallerContext role, Map<String, Object> values, ClassMeta<T> classMeta)
	{
		for (Entry<String, Object> entry: values.entrySet()) {
			FieldDef<Object> fieldDef = (FieldDef<Object>)classMeta.getField(entry.getKey());
			try {
				FieldRoles roles = fieldDef.getRoles();
				ClassMeta<Object> embeddedMeta;
				if ((embeddedMeta = fieldDef.getEmbedded()) != null) {
					Map<String, Object> inputMap;
					try {
						inputMap = (Map<String, Object>) entry.getValue();
					}
					catch (ClassCastException ex) {
						throw new RuntimeException("Failed to cast field "+fieldDef.getName()+" to Map: "+ex.toString());
					}
					errors.pushNestedPath(fieldDef.getName());
					newWithRoleInternal(errors, getOrCreateEmbeddedInternal(obj, fieldDef), role, inputMap, embeddedMeta);
					errors.popNestedPath();
				}
				else {
					if (!role.checkRole(roles.roleNew()))
						throw new SecurityException("Denied to set "+obj.getClass().getName()+"."+fieldDef.getName());
					setScalarInternal(errors, obj, role, fieldDef, entry.getValue());
				}
			}
			catch (Exception ex) {
				throw new RuntimeException("Failed to set "+fieldDef.getName()+": "+ex.toString(), ex);
			}
		}
		return !errors.hasErrors();
	}

	protected static final Map<Class<?>, Class<?>> primitiveMap = MapUtil.createHashMap(
			(Class<?>)boolean.class, (Class<?>)Boolean.class,
			byte.class, Byte.class,
			short.class, Short.class,
			int.class, Integer.class,
			long.class, Long.class
			);
}
