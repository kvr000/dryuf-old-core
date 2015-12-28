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

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.inject.Inject;

import net.dryuf.core.Dryuf;
import net.dryuf.oper.ObjectOperContext;
import net.dryuf.srvui.tenv.PageContextTestUtil;
import net.dryuf.tenv.AppTenvObject;
import net.dryuf.util.MapUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.nio.charset.StandardCharsets;

import net.dryuf.core.EntityHolder;
import net.dryuf.srvui.DummyRequest;
import net.dryuf.tenv.TestMain;
import net.dryuf.oper.ObjectOperController;
import net.dryuf.oper.ObjectOperController.ListContainer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class ObjectOperControllerTest extends AppTenvObject
{
	@Inject
	protected ObjectOperController<?> testMainOper;

	public ObjectOperContext	createObjectOperContext(String path, Map<String, String> params)
	{
		return new ObjectOperContext(PageContextTestUtil.createPageContext(createCallerContext(), path, params), testMainOper, EntityHolder.createRoleOnly(createCallerContext()))
			.setupObjectOperMarshaller("rest");
	}

	@Test
	public void			testList()
	{
		ObjectOperContext operContext = createObjectOperContext("/", MapUtil.createHashMap("method", "list"));
		try {
			((DummyRequest)operContext.getRequest()).setInputStream(new ByteArrayInputStream(("{ \"name\": \""+Dryuf.dotClassname(ObjectOperControllerTest.class)+".testList\"}").getBytes(StandardCharsets.UTF_8)));
			operContext.setupObjectOperMarshaller("dryuf");
			@SuppressWarnings({ "unchecked", "unused" })
			ListContainer<TestMain> list = (ListContainer<TestMain>) testMainOper.operate(operContext, new EntityHolder<Object>(null, operContext.getCallerContext()));
			//Assert.assertEquals(0L, list.total);
		}
		finally {
			operContext.close();
		}
	}

	@Test
	public void			testRetrieveNull()
	{
		ObjectOperContext operContext = createObjectOperContext("/20000000/", null);
		try {
			@SuppressWarnings("unchecked")
			EntityHolder<TestMain> obj = (EntityHolder<TestMain>) testMainOper.operate(operContext, new EntityHolder<Object>(null, operContext.getCallerContext()));
			Assert.assertNull(obj);
		}
		finally {
			operContext.close();
		}
	}

	@Test
	public void			testCreate()
	{
		ObjectOperContext operContext = createObjectOperContext("/", MapUtil.createHashMap("method", "create"));
		try {
			((DummyRequest)operContext.getRequest()).setMethod("POST");
			((DummyRequest)operContext.getRequest()).setInputStream(new ByteArrayInputStream(("{ \"name\": \""+Dryuf.dotClassname(ObjectOperControllerTest.class)+".testCreate\", \"svalue\": \"xyz\", \"ivalue\": 6 }").getBytes(StandardCharsets.UTF_8)));
			operContext.setupObjectOperMarshaller("dryuf");
			@SuppressWarnings("unchecked")
			EntityHolder<TestMain> obj = (EntityHolder<TestMain>) testMainOper.operate(operContext, EntityHolder.createRoleOnly(operContext.getCallerContext()));
			Assert.assertNotNull(obj);
			Assert.assertEquals("xyz", obj.getEntity().getSvalue());
		}
		finally {
			operContext.close();
		}
	}

	@Test
	public void			testUpdate()
	{
		TestMain tc = createTestMain("testUpdate");
		ObjectOperContext operContext = createObjectOperContext("/"+tc.getPk()+"/", MapUtil.createHashMap("method", "update"));
		try {
			((DummyRequest)operContext.getRequest()).setMethod("PUT");
			((DummyRequest)operContext.getRequest()).setInputStream(new ByteArrayInputStream(("{ \"name\": \""+Dryuf.dotClassname(ObjectOperControllerTest.class)+".testUpdate\", \"svalue\": \"abc\", \"ivalue\": 3 }").getBytes(StandardCharsets.UTF_8)));
			@SuppressWarnings("unchecked")
			EntityHolder<TestMain> holder = (EntityHolder<TestMain>) testMainOper.operate(operContext, new EntityHolder<Object>(null, operContext.getCallerContext()));
			Assert.assertNotNull(holder);
			Assert.assertEquals("abc", holder.getEntity().getSvalue());
		}
		finally {
			operContext.close();
		}
	}

	protected TestMain		createTestMain(String name)
	{
		ObjectOperContext presenter = createObjectOperContext("/", MapUtil.createHashMap("method", "create"));
		try {
			((DummyRequest)presenter.getRequest()).setMethod("POST");
			((DummyRequest)presenter.getRequest()).setInputStream(new ByteArrayInputStream(("{ \"name\": \""+Dryuf.dotClassname(ObjectOperControllerTest.class)+"."+name+"\", \"svalue\": \"xyz\", \"ivalue\": 6 }").getBytes(StandardCharsets.UTF_8)));
			@SuppressWarnings("unchecked")
			EntityHolder<TestMain> obj = (EntityHolder<TestMain>) testMainOper.operate(presenter, new EntityHolder<Object>(null, presenter.getCallerContext()));
			Assert.assertNotNull(obj);
			Assert.assertEquals("xyz", obj.getEntity().getSvalue());
			return obj.getEntity();
		}
		finally {
			presenter.close();
		}
	}
}
