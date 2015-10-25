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

package net.dryuf.dao.mysql.test;

import net.dryuf.dao.test.data.dao.GenericDryufDao;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.context.ApplicationContext;


@ContextConfiguration("classpath:testContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaExceptionTranslateTest extends java.lang.Object
{
	@Inject
	protected GenericDryufDao	genericDryufDao;

	public JpaExceptionTranslateTest()
	{
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

	@Before
	public void			createStructure()
	{
		runSql("DELETE FROM TestEnt");
	}

	@Test
	public void			testCorrect() throws Exception
	{
		runSql("INSERT INTO TestEnt (testId) VALUES (1)");
	}

	@Test(expected = net.dryuf.dao.DaoPrimaryKeyConstraintException.class)
	public void			testDaoPrimaryKeyConstraint() throws Exception
	{
		runSql("INSERT INTO TestEnt (testId) VALUES (2)");
		runSql("INSERT INTO TestEnt (testId) VALUES (2)");
	}

	@Test(expected = net.dryuf.dao.DaoBadNullConstraintException.class)
	public void			testDaoBadNullConstraintException() throws Exception
	{
		runSql("INSERT INTO TestEnt (testId, nonull) VALUES (3, null)");
	}

	@Test(expected = net.dryuf.dao.DaoUniqueConstraintException.class)
	public void			testDaoUniqueConstraintException() throws Exception
	{
		runSql("INSERT INTO TestEnt (testId, uniq) VALUES (4, 1)");
		runSql("INSERT INTO TestEnt (testId, uniq) VALUES (5, 1)");
	}
};
