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
public class LatitudeTextualTest extends TextualsTestBase<Integer>
{
	public LatitudeTextualTest()
	{
		super(net.dryuf.textual.LatitudeTextual.class);
	}

	@Test
	public void			testCorrect() throws Exception
	{
		Assert.assertNull(textual.check("N 1°2'34.567\"", null));
		Assert.assertNotNull(textual.check("N °2'34.567\"", null));
		Assert.assertNotNull(textual.check("N 1°'34.567\"", null));
		Assert.assertNotNull(textual.check("N 1°24.567\"", null));
		Assert.assertNotNull(textual.check("N 1°2'3.567", null));
		Assert.assertEquals(10429352, (int) textual.convert("N 1°2'34.567\"", null));
	}

	@Test(expected = java.lang.Exception.class)
	public void			testIncorrectException() throws Exception
	{
		textual.convert("N 1°'34.567\"", null);
	}
}
