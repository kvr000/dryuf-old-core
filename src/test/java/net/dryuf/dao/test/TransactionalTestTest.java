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

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.dryuf.core.Dryuf;
import org.junit.Assert;
import org.junit.Ignore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import net.dryuf.config.DbConfigSection;
import net.dryuf.config.dao.DbConfigEntryDao;
import net.dryuf.config.dao.DbConfigProfileDao;
import net.dryuf.config.dao.DbConfigSectionDao;
import net.dryuf.core.CallerContext;
import net.dryuf.tenv.AppTenvObject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class TransactionalTestTest extends AppTenvObject
{
	@Inject
	protected DbConfigProfileDao	dbConfigProfileDao;
	@Inject
	protected DbConfigSectionDao	dbConfigSectionDao;
	@Inject
	protected DbConfigEntryDao	dbConfigEntryDao;

	@PersistenceContext(unitName = "dryuf")
	protected EntityManager		em;

	@Ignore
	@Test
	public void			testTransactionalTarget()
	{
		DbConfigSection section = dbConfigSectionDao.loadByPk(new DbConfigSection.Pk(Dryuf.dotClassname(TransactionalTestTest.class), "section1"));
		Assert.assertNotNull("section exists", section);
		Assert.assertNotNull(section.getEntries());
		Assert.assertEquals("entries count is 2", 2, section.getEntries().size());
	}

	@Test
	@Transactional(value = "dryuf", readOnly = true)
	public void			testTransactionalSelf()
	{
		DbConfigSection section = dbConfigSectionDao.loadByPk(new DbConfigSection.Pk(Dryuf.dotClassname(TransactionalTestTest.class), "section1"));
		Assert.assertNotNull("section exists", section);
		Assert.assertNotNull(section.getEntries());
		Assert.assertEquals("entries count is 2", 2, section.getEntries().size());
	}

	@Ignore
	@Test
	public void			testNoneTransactional()
	{
		DbConfigSection section = em.find(DbConfigSection.class, new DbConfigSection.Pk(Dryuf.dotClassname(TransactionalTestTest.class), "section1"));
		Assert.assertNotNull("section exists", section);
		Assert.assertNotNull(section.getEntries());
		Assert.assertEquals("entries count is 2", 2, section.getEntries().size());
	}

	@Test
	public void			testCallTransactional() throws Exception
	{
		dbConfigSectionDao.runTransactioned(new Callable<DbConfigSection>() {
			@Override
			public DbConfigSection call() throws Exception
			{
				DbConfigSection section = dbConfigSectionDao.loadByPk(new DbConfigSection.Pk(Dryuf.dotClassname(TransactionalTestTest.class), "section1"));
				Assert.assertNotNull("section exists", section);
				Assert.assertNotNull(section.getEntries());
				Assert.assertEquals("entries count is 2", 2, section.getEntries().size());
				return section;
			}
		});
	}

	@Test
	public void			testContextTransactional()
	{
		CallerContext callerContext = createCallerContext();
		try {
			dbConfigSectionDao.keepContextTransaction(callerContext);
			DbConfigSection section = dbConfigSectionDao.loadByPk(new DbConfigSection.Pk(Dryuf.dotClassname(TransactionalTestTest.class), "section1"));
			Assert.assertNotNull("section exists", section);
			Assert.assertNotNull(section.getEntries());
			Assert.assertEquals("entries count is 2", 2, section.getEntries().size());
		}
		finally {
			callerContext.close();
		}
	}
}
