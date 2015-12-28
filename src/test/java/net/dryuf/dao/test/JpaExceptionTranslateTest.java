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

import net.dryuf.core.Dryuf;
import net.dryuf.dao.test.data.TestEnt;
import net.dryuf.dao.test.data.dao.GenericDryufDao;
import net.dryuf.dao.test.data.dao.TestEntDao;
import net.dryuf.tenv.TestMain;
import net.dryuf.tenv.dao.TestChildDao;
import net.dryuf.tenv.dao.TestMainDao;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration("classpath:testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaExceptionTranslateTest extends java.lang.Object
{
	@Inject
	protected GenericDryufDao	genericDryufDao;

	@Inject
	protected TestMainDao		testMainDao;

	@Inject
	protected TestChildDao		testChildDao;

	public				JpaExceptionTranslateTest()
	{
	}

	public long			allocateTestMain(String name)
	{
		TestMain testMain = new TestMain();
		testMain.setName(Dryuf.dotClassname(JpaExceptionTranslateTest.class)+"-"+name);
		testMain.setSvalue(testMain.getName());
		if (name.indexOf('\'') >= 0 || name.indexOf('\\') >= 0)
			throw new IllegalArgumentException("unexpected value from test, identifier cannot contain ' or \\");
		runSql("DELETE FROM TestMain t WHERE t.name = '"+name+"'");
		testMainDao.insert(testMain);
		testChildDao.removeByCompos(testMain.getTestId());
		return testMain.getTestId();
	}

	public void			runSqlSafe(String sql)
	{
		try {
			genericDryufDao.runNativeUpdate(sql);
		}
		catch (Exception ex) {
		}
	}

	public void			runSql(String sql)
	{
		genericDryufDao.runNativeUpdate(sql);
	}

	@Test
	public void			testCorrect() throws Exception
	{
		long id = allocateTestMain("testCorrect");
		runSql("INSERT INTO TestChild (testId, childId, svalue) VALUES ("+id+", 1, '1')");
	}

	@Test(expected = net.dryuf.dao.DaoPrimaryKeyConstraintException.class)
	public void			testDaoPrimaryKeyConstraint() throws Exception
	{
		long id = allocateTestMain("testDaoPrimaryKeyConstraint");
		runSql("INSERT INTO TestChild (testId, childId, svalue) VALUES ("+id+", 2, '1')");
		runSql("INSERT INTO TestChild (testId, childId, svalue) VALUES ("+id+", 2, '2')");
	}

	@Test(expected = net.dryuf.dao.DaoBadNullConstraintException.class)
	public void			testDaoBadNullConstraintException() throws Exception
	{
		long id = allocateTestMain("testDaoBadNullConstraintException");
		runSql("INSERT INTO TestChild (testId, childId) VALUES ("+id+", null)");
	}

	@Test(expected = net.dryuf.dao.DaoUniqueConstraintException.class)
	public void			testDaoUniqueConstraintException() throws Exception
	{
		long id = allocateTestMain("testDaoUniqueConstraintException");
		runSql("INSERT INTO TestChild (testId, childId, svalue) VALUES ("+id+", 4, '1')");
		runSql("INSERT INTO TestChild (testId, childId, svalue) VALUES ("+id+", 5, '1')");
	}
}
