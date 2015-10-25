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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import net.dryuf.util.MapUtil;


/**
 * Conversion utilities among basic scalar types.
 */
public abstract class ConversionUtil extends java.lang.Object
{
	public static final int		TC_Complex		= 0;
	public static final int		TC_Null			= 1;
	public static final int		TC_Boolean		= 2;
	public static final int		TC_Character		= 3;
	public static final int		TC_Byte			= 4;
	public static final int		TC_Short		= 5;
	public static final int		TC_Int			= 6;
	public static final int		TC_Long			= 7;
	public static final int		TC_String		= 8;
	public static final int		TC_Date			= 9;
	public static final int		TC_Set			= 10;
	public static final int		TC_List			= 11;
	public static final int		TC_Map			= 12;
	public static final int		TC_CallerContext	= 15;
	public static final int		TC_EntityHolder		= 16;
	public static final int		TC_EntityView		= 17;

	/**
	 * Translates primitive type to its wrapper class. Returns null if passed class is not primitive.
	 *
	 * @param orig
	 * 	original class
	 *
	 * @return null
	 * 	if the passed argument is not primitive
	 * @return
	 * 	wrapper class for its primitive
	 */
	public static Class<?>		translatePrimitiveToWrap(Class<?> orig)
	{
		return primitiveMap.get(orig);
	}

	/**
	 * Translates primitive type to its wrapper class. Returns the original if passed class is not primitive.
	 *
	 * @param orig
	 * 	original class
	 *
	 * @return
	 * 	wrapper class for its primitive
	 */
	public static Class<?>		translatePrimitiveToWrapOrOriginal(Class<?> orig)
	{
		return primitiveMap.getOrDefault(orig, orig);
	}

	public static int		getSerializableType(Object o)
	{
		Integer tc;
		if (o == null)
			return TC_Null;
		Class<?> c = o.getClass();
		if ((tc = serializableTypes.get(c)) == null) {
			Class<?>[] interfaces = c.getInterfaces();
			if (interfaces.length > 0 && (tc = serializableTypes.get(interfaces[0])) != null)
				return tc;
			if (o instanceof Set)
				return TC_Set;
			if (o instanceof List)
				return TC_List;
			if (o instanceof Map)
				return TC_Map;
			if (o instanceof CallerContext)
				return TC_CallerContext;
			if (o instanceof EntityHolder)
				return TC_EntityHolder;
			if (o instanceof EntityView)
				return TC_EntityView;
			return TC_Complex;
		}
		return tc;
	}

	public static boolean		isScalarClass(Class<?> clazz)
	{
		if (Number.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (clazz.isPrimitive()) {
			return true;
		}
		else if (Boolean.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (String.class.isAssignableFrom(clazz)) {
			return true;
		}
		else if (Character.class.isAssignableFrom(clazz)) {
			return true;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T		convertToClass(Class<T> target, Object source)
	{
		if (source == null) {
			return null;
		}
		else if (source.getClass() == target) {
			return (T)source;
		}
		else if ((Number.class.isAssignableFrom(target) || target.isPrimitive()) && source instanceof Number) {
			Number number = (Number)source;
			if (target == Integer.class || target == int.class)
				return (T)Integer.valueOf(number.intValue());
			if (target == Long.class || target == long.class)
				return (T)Long.valueOf(number.longValue());
			if (target == Float.class || target == float.class)
				return (T)Float.valueOf(number.floatValue());
			if (target == Double.class || target == double.class)
				return (T)Double.valueOf(number.doubleValue());
			if (target == Short.class || target == short.class)
				return (T)Short.valueOf(number.shortValue());
			if (target == Byte.class || target == byte.class)
				return (T)Byte.valueOf(number.byteValue());
			if (target == boolean.class)
				return (T)Boolean.valueOf(number.doubleValue() != 0);
			throw new RuntimeException("cannot convert "+number.getClass().getName()+" to "+target.getName());
		}
		else if (String.class.isAssignableFrom(target)) {
			return (T)source;
		}
		else if (Boolean.class.isAssignableFrom(target) || target == boolean.class) {
			if (source.toString().equals("1") || source.toString().equals("true"))
				return (T)Boolean.valueOf(true);
			if (source.toString().equals("0") || source.toString().equals("false"))
				return (T)Boolean.valueOf(false);
			throw new RuntimeException("cannot convert value "+source.toString()+" to boolean");
		}
		else {
			Object result = Dryuf.createObjectArgs(Dryuf.getConstructor(target));
			Map<String, Object> map = (Map<String, Object>)source;
			for (Map.Entry<String, Object> entry: map.entrySet()) {
				Field field = Dryuf.getClassField(result.getClass(), entry.getKey());
				Dryuf.setFieldValue(result, field, convertToClass(field.getType(), entry.getValue()));
			}
			return (T)result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T		parseStringToClass(Class<T> target, String source)
	{
		Class<?> wrapperClass;
		if (target == String.class) {
			return (T)source;
		}
		else if ((wrapperClass = primitiveMap.get(target)) != null) {
			return (T)Dryuf.invokeMethod(null, Dryuf.getClassMethod(wrapperClass, "valueOf", String.class), source);
		}
		else {
			return (T)Dryuf.invokeMethod(null, Dryuf.getClassMethod(target, "valueOf", String.class), source);
		}
	}

	protected static final Map<Class<?>, Class<?>> primitiveMap = MapUtil.createHashMap(
			(Class<?>)boolean.class,	(Class<?>)Boolean.class,
			char.class,			Character.class,
			byte.class,			Byte.class,
			short.class,			Short.class,
			int.class,			Integer.class,
			long.class,			Long.class,
			float.class,			Float.class,
			double.class,			Double.class
			);

	protected static final Map<Class<?>, Integer> serializableTypes = MapUtil.createHashMap(
			(Class<?>)boolean.class,	TC_Boolean,
			Boolean.class,			TC_Boolean,
			char.class,			TC_Character,
			Character.class,		TC_Character,
			byte.class,			TC_Byte,
			Byte.class,			TC_Byte,
			short.class,			TC_Short,
			Short.class,			TC_Short,
			int.class,			TC_Int,
			Integer.class,			TC_Int,
			long.class,			TC_Long,
			Long.class,			TC_Long,
			String.class,			TC_String,
			Date.class,			TC_Date,
			Set.class,			TC_Set,
			HashSet.class,			TC_Set,
			TreeSet.class,			TC_Set,
			List.class,			TC_List,
			LinkedList.class,		TC_List,
			ArrayList.class,		TC_List,
			Map.class,			TC_Map,
			HashMap.class,			TC_Map,
			TreeMap.class,			TC_Map
			);
}
