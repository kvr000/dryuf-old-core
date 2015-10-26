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


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class InjectionTest extends java.lang.Object
{
	/**
	*/
	public void			InjectionTest()
	{
	}

	@Test
	public void			testAppContainerAware()
	{
		Assert.assertNotNull(this.testBean.getAppContainer());
	}

	@Test
	public void			testInjectMethod()
	{
		Assert.assertNotNull(this.testBean.getMimeTypeService());
	}

	@Test
	public void			testInjectField()
	{
		Assert.assertNotNull(this.testBean.getLoggerService());
	}

	@Test
	public void			testParamMethod()
	{
		Assert.assertTrue(this.testBean.getTimeBo());
	}

	@Test
	public void			testParamField()
	{
		Assert.assertNotNull(this.testBean.getCaptchaService());
	}

	@Inject
	protected TestBean		testBean;
}
