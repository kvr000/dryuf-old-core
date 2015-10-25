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

package net.dryuf.core.test;

import net.dryuf.core.ConversionUtil;
import net.dryuf.tenv.DAssert;
import org.junit.Assert;
import org.junit.Test;


public class ConversionUtilTest extends java.lang.Object
{
	@Test
	public void			testConversions()
	{
		Assert.assertEquals(false, ConversionUtil.convertToClass(boolean.class, 0));
		Assert.assertEquals(true, ConversionUtil.convertToClass(boolean.class, 1));
		Assert.assertEquals(false, ConversionUtil.convertToClass(boolean.class, false));
		Assert.assertEquals(true, ConversionUtil.convertToClass(boolean.class, true));
		Assert.assertEquals(Integer.valueOf(10), ConversionUtil.convertToClass(int.class, 10L));
		Assert.assertEquals(Integer.valueOf(10), ConversionUtil.convertToClass(int.class, 10.0f));
		Assert.assertEquals(Long.valueOf(10L), ConversionUtil.convertToClass(long.class, 10));
		Assert.assertEquals(Long.valueOf(10L), ConversionUtil.convertToClass(long.class, 10.0f));
		Assert.assertEquals(Double.valueOf(10), ConversionUtil.convertToClass(double.class, 10));
		Assert.assertEquals(Double.valueOf(10), ConversionUtil.convertToClass(double.class, 10.0f));
	}
}
