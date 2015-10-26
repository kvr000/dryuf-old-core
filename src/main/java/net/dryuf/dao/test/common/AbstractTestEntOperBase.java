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

package net.dryuf.dao.test.common;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;

import javax.inject.Inject;

import net.dryuf.dao.test.data.dao.GenericDryufDao;
import net.dryuf.dao.test.data.TestEnt;
import net.dryuf.dao.test.data.dao.TestEntDao;


@Ignore("base class only")
public class AbstractTestEntOperBase extends java.lang.Object
{
	public				AbstractTestEntOperBase()
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
//		runSqlSafe("DROP TABLE TestEnt");
//		runSql("CREATE TABLE TestEnt (testId bigint NOT NULL, name varchar(32), uniq varchar(32), nonull varchar(32) NOT NULL DEFAULT '', PRIMARY KEY (testId), UNIQUE INDEX udx_uniq (uniq))");
		runSql("DELETE FROM TestEnt");
	}

	@Test
	public void			testCorrect() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setTestId(1L);
		testEntDao.insert(te0);
	}

	@Test(expected = net.dryuf.dao.DaoPrimaryKeyConstraintException.class)
	public void			testDaoPrimaryKeyConstraint() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setTestId(2L);
		testEntDao.insert(te0);
		TestEnt te1 = new TestEnt();
		te1.setTestId(2L);
		testEntDao.insert(te1);
	}

	@Test(expected = net.dryuf.dao.DaoBadNullConstraintException.class)
	public void			testDaoBadNullConstraintException() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setTestId(3L);
		te0.setNonull(null);
		testEntDao.insert(te0);
	}

	@Test(expected = net.dryuf.dao.DaoUniqueConstraintException.class)
	public void			testDaoUniqueConstraintException() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setTestId(4L);
		te0.setUniq("four");
		testEntDao.insert(te0);
		TestEnt te1 = new TestEnt();
		te1.setTestId(5L);
		te1.setUniq("four");
		testEntDao.insert(te1);
	}

	@Inject
	protected GenericDryufDao	genericDryufDao;

	@Inject
	protected TestEntDao		testEntDao;
}
