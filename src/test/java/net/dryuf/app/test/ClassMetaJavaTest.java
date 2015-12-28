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

package net.dryuf.app.test;

import net.dryuf.app.ClassMetaJava;
import net.dryuf.app.FieldDef;
import net.dryuf.security.UserAccountDomainRole;
import net.dryuf.tenv.AppTenvObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dryuf.app.ClassMeta;
import net.dryuf.tenv.TestChild;
import net.dryuf.tenv.TestMain;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class ClassMetaJavaTest extends AppTenvObject
{
	@Test
	public void			testTestMain() throws Exception
	{
		ClassMeta<TestMain> meta = ClassMetaJava.openCached(createCallerContext().getAppContainer(), TestMain.class, null);

		Assert.assertNotNull("meta not created", meta);

		Assert.assertEquals(TestMain.class, meta.getDataClass());
		Assert.assertEquals(TestMain.class.getName(), meta.getDataClassName());

		Assert.assertEquals(false, meta.isPkEmbedded());
		Assert.assertEquals(Long.class, meta.getPkClass());
		Assert.assertEquals(false, meta.isPkEmbedded());
		Assert.assertEquals("testId", meta.getPkName());
		Assert.assertNull("", meta.getComposPath());
		Assert.assertNull(meta.getComposPkClass());
		Assert.assertEquals(0, meta.getAdditionalPkFields().length);

		Assert.assertEquals(4, meta.getFields().length);
		Assert.assertEquals("testId", meta.getFields()[0].getName());
		Assert.assertEquals("name", meta.getFields()[1].getName());
		Assert.assertEquals("svalue", meta.getFields()[2].getName());
		Assert.assertEquals("ivalue", meta.getFields()[3].getName());

		Assert.assertEquals("testId", meta.getRefName());

		//Assert.assertEquals(0, meta.getSuggestFields().length);
		//Assert.assertEquals("testId", meta.getSuggestFields()[0]);

		Assert.assertEquals(1, meta.getDisplayKeys().length);
		Assert.assertEquals("testId", meta.getDisplayKeys()[0]);

		//Assert.assertEquals(0, meta.getRefFields().length);
		//Assert.assertEquals("testId", meta.getRefFields()[0]);

		Assert.assertEquals("testId", meta.getField("testId").getName());
		Assert.assertEquals("ivalue", meta.getField("ivalue").getName());
	}

	@Test
	public void			testTestMainAccess() throws Exception
	{
		TestMain testMain = new TestMain();
		testMain.setPk(6L);
		testMain.setIvalue(10);
		testMain.setSvalue("str");

		ClassMeta<TestMain> meta = ClassMetaJava.openCached(createCallerContext().getAppContainer(), TestMain.class, null);

		Assert.assertEquals(6L, meta.getEntityPkValue(testMain));
		Assert.assertEquals(10, meta.getEntityFieldValue(testMain, "ivalue"));
		Assert.assertEquals("str", meta.getEntityFieldValue(testMain, "svalue"));

		Assert.assertEquals("6/", meta.urlDisplayKey(createCallerContext(), testMain));
	}

	@Test
	public void			testTestChild() throws Exception
	{
		ClassMeta<TestChild> meta = ClassMetaJava.openCached(createCallerContext().getAppContainer(), TestChild.class, null);

		Assert.assertNotNull("meta not created", meta);

		Assert.assertEquals(TestChild.class, meta.getDataClass());
		Assert.assertEquals(TestChild.class.getName(), meta.getDataClassName());

		Assert.assertEquals(true, meta.isPkEmbedded());
		Assert.assertEquals(TestChild.Pk.class, meta.getPkClass());
		Assert.assertEquals(true, meta.isPkEmbedded());
		Assert.assertEquals("pk", meta.getPkName());
		Assert.assertEquals("pk.testId", meta.getComposPath());
		Assert.assertEquals(FieldDef.AST_Compos, meta.getPathField("pk.testId").getAssocType());
		Assert.assertEquals(TestMain.class, meta.getPathField("pk.testId").getAssocClass());
		Assert.assertEquals(Long.class, meta.getComposPkClass());
		Assert.assertEquals(1, meta.getAdditionalPkFields().length);
		Assert.assertEquals(1, meta.getAdditionalPkFields().length);

		Assert.assertEquals(2, meta.getFields().length);
		Assert.assertEquals("pk", meta.getFields()[0].getName());
		Assert.assertEquals("svalue", meta.getFields()[1].getName());

		Assert.assertEquals("pk", meta.getRefName());

		//Assert.assertEquals(0, meta.getSuggestFields().length);
		//Assert.assertEquals("testId", meta.getSuggestFields()[0]);

		Assert.assertEquals(1, meta.getDisplayKeys().length);
		Assert.assertEquals("pk.childId", meta.getDisplayKeys()[0]);

		//Assert.assertEquals(0, meta.getRefFields().length);
		//Assert.assertEquals("testId", meta.getRefFields()[0]);

		Assert.assertEquals("pk", meta.getField("pk").getName());
		Assert.assertEquals("svalue", meta.getField("svalue").getName());
	}

	@Test
	public void			testTestChildAccess() throws Exception
	{
		TestChild testChild = new TestChild();
		testChild.setPk(new TestChild.Pk(6L, 16));
		testChild.setSvalue("str");

		ClassMeta<TestChild> meta = ClassMetaJava.openCached(getAppContainer(), TestChild.class, null);

		Assert.assertEquals(new TestChild.Pk(6L, 16), meta.getEntityPkValue(testChild));
		Assert.assertEquals("str", meta.getEntityFieldValue(testChild, "svalue"));

		Assert.assertEquals(6L, meta.getEntityPathValue(testChild, "pk.testId"));
		Assert.assertEquals(16, meta.getEntityPathValue(testChild, "pk.childId"));
		Assert.assertEquals("str", meta.getEntityPathValue(testChild, "svalue"));

		Assert.assertEquals("16/", meta.urlDisplayKey(createCallerContext(), testChild));
	}

	@Test
	public void			testReferenceDef() throws Exception
	{
		ClassMeta<TestChild> meta = ClassMetaJava.openCached(createCallerContext().getAppContainer(), TestChild.class, null);

		Assert.assertNotNull(meta.getPathField("svalue").getReferenceDef());
	}
}
