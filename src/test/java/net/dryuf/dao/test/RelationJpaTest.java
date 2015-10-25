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


import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import net.dryuf.dao.test.DryufJpaChild.Pk;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class RelationJpaTest extends net.dryuf.tenv.AppTenvObject
{
	@Test
	@Ignore
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void			testHandler()
	{
		entityManager.createQuery("DELETE FROM DryufJpaMain WHERE mainId = :mainId").setParameter("mainId", "one").executeUpdate();
		entityManager.createQuery("DELETE FROM DryufJpaChild WHERE pk.mainId = :mainId").setParameter("mainId", "one").executeUpdate();
		DryufJpaMain main = new DryufJpaMain();
		main.setMainId("one");
		DryufJpaChild child = new DryufJpaChild();
		Pk childPk = new DryufJpaChild.Pk();
		childPk.setMainId(main.getMainId());
		childPk.setChildId("1");
		child.setPk(childPk);
		entityManager.persist(main);
		entityManager.persist(child);
	}

	@PersistenceContext(unitName = "dryuf")
	protected EntityManager 	entityManager;
}
