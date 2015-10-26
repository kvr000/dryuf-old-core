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

package net.dryuf.srvui.test;

import net.dryuf.srvui.DefaultPageContext;
import net.dryuf.srvui.DummyResponse;
import net.dryuf.srvui.PageContext;
import net.dryuf.tenv.AppTenvObject;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dryuf.srvui.DummyRequest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class PathTest extends AppTenvObject
{
	public PageContext		createPageContext(String path)
	{
		DummyRequest testRequest = new DummyRequest();
		testRequest.setPath(path);
		DefaultPageContext pageContext = new DefaultPageContext(createCallerContext(), testRequest);
		return pageContext;
	}

	@Test
	public void			testRoot()
	{
		PageContext pageContext = createPageContext("/");
		Assert.assertNull(pageContext.getPathElement());
		Assert.assertEquals("",  pageContext.getCurrentPath());
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertTrue(pageContext.needPathFinal());
	}

	@Test
	public void			testFile()
	{
		PageContext pageContext = createPageContext("/file.html");
		Assert.assertEquals("file.html", pageContext.getPathElement());
		Assert.assertEquals("file.html",  pageContext.getCurrentPath());
		Assert.assertNotNull(pageContext.needPathSlash(false));
		Assert.assertNotNull(pageContext.needPathSlash(false));
		Assert.assertNull(pageContext.getPathElement());
		Assert.assertNull(pageContext.getRedirected());
		Assert.assertTrue(pageContext.needPathFinal());
	}

	@Test
	public void			testDir()
	{
		PageContext pageContext = createPageContext("/dir/");
		Assert.assertEquals("dir", pageContext.getPathElement());
		Assert.assertEquals("dir/", pageContext.getCurrentPath());
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertNull(pageContext.getPathElement());
	}

	@Test
	public void			testMultiPath()
	{
		PageContext pageContext = createPageContext("/dir/file.html");
		Assert.assertEquals("dir", pageContext.getPathElement());
		Assert.assertEquals("dir/",  pageContext.getCurrentPath());
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertNotNull(pageContext.needPathSlash(true));
		Assert.assertEquals("file.html", pageContext.getPathElement());
		Assert.assertEquals("dir/file.html",  pageContext.getCurrentPath());
		Assert.assertNotNull(pageContext.needPathSlash(false));
		Assert.assertNotNull(pageContext.needPathSlash(false));
		Assert.assertNull(((DummyResponse)pageContext.getResponse()).getRedirected());
	}

	@Test
	public void			testFileSlash()
	{
		PageContext pageContext = createPageContext("/file.html/");
		Assert.assertEquals("file.html", pageContext.getPathElement());
		Assert.assertEquals("file.html/",  pageContext.getCurrentPath());
		Assert.assertFalse(pageContext.needPathSlash(false));
		Assert.assertFalse(pageContext.needPathSlash(false));
	}

	@Test
	public void			testDirNoSlash()
	{
		PageContext pageContext = createPageContext("/dir");
		Assert.assertEquals("dir", pageContext.getPathElement());
		Assert.assertEquals("dir",  pageContext.getCurrentPath());
		Assert.assertFalse(pageContext.needPathSlash(true));
		Assert.assertFalse(pageContext.needPathSlash(true));
		Assert.assertNotNull(((DummyResponse)pageContext.getResponse()).getRedirected());
	}
}
