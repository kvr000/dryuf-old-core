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

package net.dryuf.dao.test;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.dryuf.tenv.AppTenvObject;
import net.dryuf.util.MapUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.dao.RoleDaoAccessJpa;
import net.dryuf.tenv.TestChild;
import net.dryuf.tenv.TestMain;
import net.dryuf.tenv.dao.TestChildDao;
import net.dryuf.tenv.dao.TestMainDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class DaoAccessJpaTest extends AppTenvObject
{
	@Inject
	protected TestMainDao		testMainDao;
	@Inject
	protected TestChildDao		testChildDao;

	@PersistenceContext(unitName="dryuf")
	protected EntityManager		em;

	public long			allocateTestMain(String name)
	{
		TestMain testMain = new TestMain();
		testMain.setName(Dryuf.dotClassname(DaoAccessJpaTest.class)+"-"+name);
		testMain.setSvalue(testMain.getName());
		try {
			testMainDao.runTransactioned(() -> {
				em.createNativeQuery("DELETE FROM TestMain t WHERE t.name = ?").setParameter(1, name).executeUpdate();
				return null;
			});
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		testMainDao.insert(testMain);
		testChildDao.removeByCompos(testMain.getTestId());
		return testMain.getTestId();
	}

	public void			cleanTests()
	{
		em.createQuery("DELETE FROM TestMain").executeUpdate();
	}

	@Test
	@Transactional("dryuf")
	public void			testPkChange()
	{
		TestMain to0 = new TestMain();
		to0.setName(Dryuf.dotClassname(DaoAccessJpaTest.class)+"-testPkChange");
		to0.setSvalue("pkChange");
		testMainDao.insert(to0);
		TestChild tc0 = new TestChild();
		tc0.setTestId(to0.getPk());
		tc0.setChildId(0);
		testChildDao.insert(tc0);
		tc0.setSvalue("xx");
		testChildDao.update(tc0);
	}

	@Test
	@Transactional(value = "dryuf")
	public void			testRoot()
	{
		cleanTests();
		RoleDaoAccessJpa<TestMain, Long> rdaTestMain = new RoleDaoAccessJpa<TestMain, Long>(TestMain.class, em);
		RoleDaoAccessJpa<TestChild, TestChild.Pk> rdaTestChild = new RoleDaoAccessJpa<TestChild, TestChild.Pk>(TestChild.class, em);

		EntityHolder<Object> composition = new EntityHolder<Object>(null, this.createCallerContext());

		TestMain to0 = rdaTestMain.createObject(composition, MapUtil.createHashMap("name", Dryuf.dotClassname(DaoAccessJpaTest.class), "svalue", (Object)"xyz"));
		List<EntityHolder<TestMain>> results = new LinkedList<EntityHolder<TestMain>>();
		Assert.assertEquals(1, rdaTestMain.listObjects(results, composition, null, null, 0L, 100L));
		Assert.assertEquals(1, results.size());
		rdaTestMain.updateObject(results.get(0), to0.getPk(), MapUtil.createHashMap("ivalue", (Object)63L));
		TestMain to1 = results.get(0).getEntity();
		Assert.assertEquals("xyz", to1.getSvalue());
		Assert.assertEquals((Integer)63, to1.getIvalue());
		TestChild tc0 = rdaTestChild.createObject(results.get(0), MapUtil.createHashMap("pk", (Object)MapUtil.createHashMap("childId", (Object)0)));
		@SuppressWarnings("unused")
		TestChild tc1 = rdaTestChild.updateObject(new EntityHolder<TestChild>(tc0, composition.getRole()), tc0.getPk(), MapUtil.createHashMap("svalue", (Object)"data"));
		Assert.assertTrue(rdaTestMain.deleteObject(composition, to0.getPk()));
	}

	@Test
	@Transactional(value = "dryuf")
	public void			testSuggest()
	{
		EntityHolder<Object> composition = new EntityHolder<Object>(null, this.createCallerContext());
		List<EntityHolder<TestMain>> results = new LinkedList<EntityHolder<TestMain>>();

		testMainDao.listDynamic(results, composition, MapUtil.createHashMap("-suggest", (Object)"xyz"), null, null, null);
	}

	@Test
	@Transactional("dryuf")
	public void			testPlacement()
	{
		allocateTestMain("testPlacement");
	}
}
