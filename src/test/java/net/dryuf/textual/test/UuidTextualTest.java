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

package net.dryuf.textual.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class UuidTextualTest extends TextualsTestBase<java.lang.String>
{
	public UuidTextualTest()
	{
		super(net.dryuf.textual.UuidTextual.class);
	}

	@Test
	public void			testCorrect() throws Exception
	{
		Assert.assertNull(textual.check("01234567-0123-0123-0123-456789abcdef", null));
		Assert.assertNotNull(textual.check("01234567-0123-012-0123-456789abcdef", null));
		Assert.assertNotNull(textual.check("01234567-012-0123-0123-456789abcdef", null));
		Assert.assertNotNull(textual.check("0123456-0123-0123-0123-456789abcdef", null));
		Assert.assertNotNull(textual.check("01234567-0123-0123-0123-456789abcde", null));
		Assert.assertEquals("01234567-0123-0123-0123-456789abcdef", textual.convert("01234567-0123-0123-0123-456789abcdef", null));
	}

	@Test(expected = java.lang.Exception.class)
	public void			testIncorrectException() throws Exception
	{
		textual.convert("01234567-0123-0123-0123-456789abcdef0", null);
	}
}
