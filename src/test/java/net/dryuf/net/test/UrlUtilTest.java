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

package net.dryuf.net.test;

import net.dryuf.util.MapUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dryuf.core.Dryuf;
import net.dryuf.net.util.UrlUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class UrlUtilTest extends java.lang.Object
{
	@Test
	public void			encodeUrlTest() throws Exception
	{
		Assert.assertEquals("path+%3F%26%2Fend", UrlUtil.encodeUrl("path ?&/end"));
	}

	@Test
	public void			appendQueryTest() throws Exception
	{
		Assert.assertEquals("http://localhost/?par0=value0", UrlUtil.appendQuery("http://localhost/", "par0=value0"));
		Assert.assertEquals("http://localhost/?p=v&par1=value1", UrlUtil.appendQuery("http://localhost/?p=v", "par1=value1"));
	}

	@Test
	public void			appendParameterTest() throws Exception
	{
		Assert.assertEquals("http://localhost/?par0=value0", UrlUtil.appendParameter("http://localhost/", "par0", "value0"));
		Assert.assertEquals("http://localhost/?par1=%3F%26%3D", UrlUtil.appendParameter("http://localhost/", "par1", "?&="));
	}

	@Test
	public void			buildQueryStringTest() throws Exception
	{
		Assert.assertEquals("par0=value0&par1=%3F%26%3D", UrlUtil.buildQueryString(MapUtil.createLinkedHashMap("par0", "value0", "par1", "?&=")));
	}

	@Test
	public void			getReversePathTest() throws Exception
	{
		Assert.assertEquals("../", UrlUtil.getReversePath("abcd/"));
		Assert.assertEquals("../../", UrlUtil.getReversePath("abcd/xyz/"));
	}

	@Test
	public void			truncateToDirTest() throws Exception
	{
		Assert.assertEquals("abcd/", UrlUtil.truncateToDir("abcd/"));
		Assert.assertEquals("abcd/", UrlUtil.truncateToDir("abcd/ab"));
	}

	@Test
	public void			truncateToParentTest() throws Exception
	{
		Assert.assertEquals("/", UrlUtil.truncateToParent("/abcd/"));
		Assert.assertEquals("/", UrlUtil.truncateToParent("/abcd/ab"));
		Assert.assertEquals("abcd/", UrlUtil.truncateToParent("abcd/ab/"));
		Assert.assertEquals("abcd/", UrlUtil.truncateToParent("abcd/ab/xyz"));
	}
}
