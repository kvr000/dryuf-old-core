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
public class WebpageTextualTest extends TextualsTestBase<java.lang.String>
{
	public WebpageTextualTest()
	{
		super(net.dryuf.textual.WebpageTextual.class);
	}

	@Test
	public void			testCorrect() throws Exception
	{
		Assert.assertNull(textual.check("http://kvr.znj.cz/drt/", null));
		Assert.assertNotNull(textual.check("http://kvr .znj.cz/drt/", null));
		Assert.assertNotNull(textual.check("//kvr.znj.cz/drt/", null));
		Assert.assertNotNull(textual.check("kvr.znj.cz/drt/", null));
		Assert.assertNotNull(textual.check("", null));
		Assert.assertEquals("http://kvr.znj.cz/drt/", textual.convert("http://kvr.znj.cz/drt/", null));
	}

	@Test(expected = java.lang.Exception.class)
	public void			testIncorrectException() throws Exception
	{
		textual.convert("http: //kvr.znj.cz/drt/", null);
	}
}
