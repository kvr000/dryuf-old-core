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

import net.dryuf.trans.meta.NoDynamic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Various reflection methods managing special behaviour of specific target languages.
 */
public class Dryuf extends java.lang.Object
{
	public static String		convertClassname(String name)
	{
		return name;
	}

	public static String		dotClassname(String name)
	{
		return name;
	}

	@NoDynamic
	public static String		dotClassname(Class<?> clazz)
	{
		return dotClassname(clazz.getName());
	}

	public static String		pathClassname(String name)
	{
		return name.replace(".", "/");
	}

	@NoDynamic
	public static String		pathClassname(Class<?> clazz)
	{
		return pathClassname(clazz.getName());
	}

	public static String		dashClassname(String name)
	{
		return name.replace(".", "-");
	}

	@NoDynamic
	public static String		dashClassname(Class<?> clazz)
	{
		return dashClassname(clazz.getName());
	}

	public static <T> T		assertNotNull(T value, String message)
	{
		if (value == null)
			throw new NullPointerException(message);
		return value;
	}

	public static Class<?>		loadClass(String name)
	{
		try {
			return Class.forName(name);
		}
		catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... params)
	{
		try {
			return clazz.getConstructor(params);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Constructor<?>	loadConstructor(String className, Class<?>... params)
	{
		try {
			return Class.forName(className).getConstructor(params);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> findConstructorN(Class<T> clazz, int n)
	{
		Constructor<T> best = null;
		for (Constructor<?> c: clazz.getConstructors()) {
			if (c.getParameterTypes().length == n) {
				if (best != null)
					throw new UnsupportedOperationException("call createClassArg"+n+" ambigious");
				best = (Constructor<T>)c;
			}
		}
		if (best == null)
			throw new UnsupportedOperationException("No constructor found which accepts "+n+" arguments");
		return best;
	}

	public static <T> T		createClassArg0(Class<T> clazz)
	{
		try {
			return clazz.newInstance();
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T		createClassArg1(Class<T> clazz, Object arg0)
	{
		try {
			return findConstructorN(clazz, 1).newInstance(arg0);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg2(Class<T> clazz, Object arg0, Object arg1)
	{
		try {
			return findConstructorN(clazz, 2).newInstance(arg0, arg1);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg3(Class<T> clazz, Object arg0, Object arg1, Object arg2)
	{
		try {
			return findConstructorN(clazz, 3).newInstance(arg0, arg1, arg2);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg4(Class<T> clazz, Object arg0, Object arg1, Object arg2, Object arg3)
	{
		try {
			return findConstructorN(clazz, 4).newInstance(arg0, arg1, arg2, arg3);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg5(Class<T> clazz, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4)
	{
		try {
			return findConstructorN(clazz, 5).newInstance(arg0, arg1, arg2, arg3, arg4);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg6(Class<T> clazz, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5)
	{
		try {
			return findConstructorN(clazz, 6).newInstance(arg0, arg1, arg2, arg3, arg4, arg5);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createClassArg7(Class<T> clazz, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6)
	{
		try {
			return findConstructorN(clazz, 7).newInstance(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static <T> T		createObjectArgs(Constructor<T> constructor, Object... params)
	{
		try {
			return constructor.newInstance(params);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Method		getObjectMethod(Object object, String method, Class<?>... params)
	{
		try {
			return object.getClass().getMethod(method, params);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Method		getClassMethod(Class<?> clazz, String method, Class<?>... params)
	{
		try {
			return clazz.getMethod(method, params);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Object		invokeMethod(Object object, Method method, Object... args)
	{
		try {
			return method.invoke(object, args);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Object		invokeMethodString0(Object object, String methodName)
	{
		return invokeMethod(object, getObjectMethod(object, methodName));
	}

	public static Field		getClassField(Class<?> cls, String fieldName)
	{
		try {
			return cls.getDeclaredField(fieldName);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Field		getClassPublicField(Class<?> cls, String fieldName)
	{
		try {
			return cls.getField(fieldName);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Object		getFieldValueNamed(Object object, String fieldName)
	{
		try {
			Field field;
			field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Object		getFieldValue(Object object, Field field)
	{
		try {
			field.setAccessible(true);
			return field.get(object);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static void		setFieldValue(Object object, Field field, Object value)
	{
		try {
			field.setAccessible(true);
			field.set(object, value);
		}
		catch (Exception ex) {
			throw propagateException(ex);
		}
	}

	public static Object		callGetter(Object object, String fieldName)
	{
		return invokeMethod(object, getObjectMethod(object, "get"+StringUtil.capitalize(fieldName), new Class<?>[0]), new Object[0]);
	}

	public static Object		callSetter(Object object, String fieldName, Object value)
	{
		return invokeMethod(object, getObjectMethod(object, "set"+StringUtil.capitalize(fieldName), value.getClass()), value);
	}

	public static <T extends Annotation> T getMandatoryAnnotation(AnnotatedElement ae, Class<T> annotationClass)
	{
		T annotation = ae.getAnnotation(annotationClass);
		if (annotation == null)
			throw new net.dryuf.core.ReportException("Annotation "+annotationClass.getName()+" not found on "+ae.toString());
		return annotation;
	}

	public static String		getStackTrace(Throwable aThrowable)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public static String		formatExceptionFull(Throwable ex)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		printWriter.append(ex.toString()).append('\n');
		ex.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * Translates the passed exception into RuntimeException if necessary.
	 *
	 * @return
	 * 	wrapping exception
	 */
	public static RuntimeException	translateException(Throwable ex)
	{
		if (ex instanceof RuntimeException) {
			return (RuntimeException)ex;
		}
		else if (ex instanceof InvocationTargetException) {
			return translateCausingException(ex);
		}
		else {
			return new RuntimeException(ex);
		}
	}

	/**
	 * Translates the passed exception into RuntimeException if necessary.
	 *
	 * @return
	 * 	wrapping exception
	 */
	public static RuntimeException	translateCausingException(Throwable ex)
	{
		if (ex.getCause() != null)
			ex = ex.getCause();
		if (ex instanceof RuntimeException) {
			return (RuntimeException)ex;
		}
		else if (ex instanceof InvocationTargetException) {
			return translateCausingException(ex);
		}
		else {
			return new RuntimeException(ex);
		}
	}

	/**
	 * Propagates the passed exception, wrapping it in RuntimeException if necessary.
	 *
	 * @return
	 * 	the method actually never returns, it just defines the return type to easily write throw propagateException(...)
	 */
	public static RuntimeException	propagateException(Throwable ex)
	{
		if (ex instanceof RuntimeException)
			throw (RuntimeException)ex;
		if (ex instanceof Error)
			throw (Error)ex;
		throw new RuntimeException(ex);
	}

	/**
	 * Propagates the passed exception cause, wrapping it in RuntimeException if necessary.
	 *
	 * @return
	 * 	the method actually never returns, it just defines the return type to easily write throw propagateException(...)
	 */
	public static RuntimeException	propagateCausingException(Throwable ex)
	{
		if (ex.getCause() != null)
			throw propagateException(ex.getCause());
		throw propagateException(ex);
	}
}
