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

import net.dryuf.core.Dryuf;
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

	@Test
	public void			testCorrect() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setName(Dryuf.dotClassname(getClass())+".testCorrect");
		testEntDao.insert(te0);
	}

	@Test(expected = net.dryuf.dao.DaoPrimaryKeyConstraintException.class)
	public void			testDaoPrimaryKeyConstraint() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setName(Dryuf.dotClassname(getClass())+".testDaoPrimaryKeyConstraint.0");
		testEntDao.insert(te0);
		TestEnt te1 = new TestEnt();
		genericDryufDao.runNativeUpdate("INSERT INTO TestEnt (testId, name, nonull) values ("+te0.getTestId()+", '"+Dryuf.dotClassname(getClass())+".testDaoPrimaryKeyConstraint.1"+"', 'a')");
	}

	@Test(expected = net.dryuf.dao.DaoBadNullConstraintException.class)
	public void			testDaoBadNullConstraintException() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setName(Dryuf.dotClassname(getClass())+".testDaoBadNullConstraintException");
		te0.setNonull(null);
		testEntDao.insert(te0);
	}

	@Test(expected = net.dryuf.dao.DaoUniqueConstraintException.class)
	public void			testDaoUniqueConstraintException() throws Exception
	{
		TestEnt te0 = new TestEnt();
		te0.setName(Dryuf.dotClassname(getClass())+".testDaoUniqueConstraintException.0");
		te0.setUniq("four");
		testEntDao.insert(te0);
		TestEnt te1 = new TestEnt();
		te1.setName(Dryuf.dotClassname(getClass())+".testDaoUniqueConstraintException.1");
		te1.setUniq("four");
		testEntDao.update(te1);
	}

	@Inject
	protected GenericDryufDao	genericDryufDao;

	@Inject
	protected TestEntDao		testEntDao;
}
