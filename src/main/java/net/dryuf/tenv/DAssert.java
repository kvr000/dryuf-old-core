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

package net.dryuf.tenv;


public abstract class DAssert extends java.lang.Object
{
	public static void		assertTrue(boolean cond)
	{
		assertTrue(cond, null);
	}

	public static void		assertFalse(boolean cond)
	{
		assertFalse(cond, null);
	}

	public static void		assertNull(Object value)
	{
		assertNull(value, null);
	}

	public static void		assertNotNull(Object value)
	{
		assertNotNull(value, null);
	}

	public static void		assertEquals(Object expected, Object actual)
	{
		assertEquals(expected, actual, null);
	}

	public static void		assertNotEquals(Object expected, Object actual)
	{
		assertNotEquals(expected, actual, null);
	}

	public static void		assertEqualsPercent1(double expected, double actual)
	{
		assertEqualsPercent1(expected, actual, null);
	}

	public static void		assertNotEqualsPercent1(double expected, double actual)
	{
		assertNotEqualsPercent1(expected, actual, null);
	}

	public static void		assertClass(Object value, Class<?> clazz)
	{
		assertClass(clazz, value, null);
	}

	public static void		assertInstance(Object value, Class<?> clazz)
	{
		assertInstance(clazz, value, null);
	}

	public static void		assertTrue(boolean cond, String message)
	{
		if (!cond)
			throw new net.dryuf.tenv.TestException("Test true failed"+(message != null ? ": "+message : ""));
	}

	public static void		assertFalse(boolean cond, String message)
	{
		if (cond)
			throw new net.dryuf.tenv.TestException("Test false failed"+(message != null ? ": "+message : ""));
	}

	public static void		assertNull(Object value, String message)
	{
		if (value != null)
			throw new net.dryuf.tenv.TestException("Test null failed"+(message != null ? ": "+message : ""));
	}

	public static void		assertNotNull(Object value, String message)
	{
		if (value == null)
			throw new net.dryuf.tenv.TestException("Test notnull failed"+(message != null ? ": "+message : ""));
	}

	public static void		assertEquals(Object expected, Object actual, String message)
	{
		if (!expected.equals(actual))
			throw new net.dryuf.tenv.TestException((message != null ? message : "Test equals failed")+": expected="+expected+", got="+actual);
	}

	public static void		assertNotEquals(Object expected, Object actual, String message)
	{
		if (expected.equals(actual))
			throw new net.dryuf.tenv.TestException((message != null ? message : "Test not equals failed")+": expected="+expected+", got="+actual);
	}

	public static void		assertEqualsPercent1(double expected, double actual, String message)
	{
		double d0 = Math.abs(expected);
		double d1 = Math.abs(actual);
		if (d0 < d1)
			d0 = d1;
		double diff = d0/100;
		if (Math.abs(expected-actual) > diff)
			throw new net.dryuf.tenv.TestException((message != null ? message : "Test equals failed")+": expected="+expected+", got="+actual);
	}

	public static void		assertNotEqualsPercent1(double expected, double actual, String message)
	{
		double d0 = Math.abs(expected);
		double d1 = Math.abs(actual);
		if (d0 < d1)
			d0 = d1;
		double diff = d0/100;
		if (Math.abs(expected-actual) <= diff)
			throw new net.dryuf.tenv.TestException((message != null ? message : "Test not equals failed")+": expected="+expected+", got="+actual);
	}

	public static void		assertClass(Class<?> clazz, Object value, String message)
	{
		if (value.getClass() != clazz)
			throw new net.dryuf.tenv.TestException("Test of class failed: real="+value.getClass()+", expected="+clazz.getName()+(message != null ? ": "+message : ""));
	}

	public static void		assertInstance(Class<?> clazz, Object value, String message)
	{
		if (!clazz.isAssignableFrom(value.getClass()))
			throw new net.dryuf.tenv.TestException("Test of class failed: real="+value.getClass().getName()+", expected="+clazz.getName()+(message != null ? ": "+message : ""));
	}
}

