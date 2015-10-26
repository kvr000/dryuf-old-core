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

package net.dryuf.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import net.dryuf.core.EntityHolder;
import net.dryuf.transaction.TransactionManager;


@Transactional("dryuf")
public class DryufDaoContext<ET, PKT> extends net.dryuf.dao.DaoContextBase<ET, PKT>
{
	public				DryufDaoContext(Class<ET> entityClass)
	{
		super(entityClass);
	}

	@PersistenceContext(unitName = "dryuf")
	public void			setEntityManager(EntityManager em)
	{
		super.setEntityManagerInternal(em);
	}

	@Inject
	@Named("net.dryuf.transaction.TransactionManager-dryuf")
	public void			setTransactionManager(TransactionManager transactionManager)
	{
		super.setTransactionManagerInternal(transactionManager);
	}

	@Override
	@Transactional("dryuf")
	public ET			loadByPk(PKT pk)
	{
		return super.loadByPk(pk);
	}

	@Override
	@Transactional("dryuf")
	public List<ET>			listAll()
	{
		return super.listAll();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void			insert(ET obj)
	{
		super.insert(obj);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void			insertTxNew(ET obj)
	{
		super.insertTxNew(obj);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			update(ET obj)
	{
		return super.update(obj);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void			remove(ET obj)
	{
		super.remove(obj);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean			removeByPk(PKT pk)
	{
		return super.removeByPk(pk);
	}

	public PKT			openRelation(EntityHolder<ET> holder, String relation)
	{
		return super.openRelation(holder, relation);
	}

	public PKT			importDynamicKey(Map<String, Object> data)
	{
		return super.importDynamicKey(data);
	}

	public Map<String, Object>	exportDynamicData(EntityHolder<ET> holder)
	{
		return super.exportDynamicData(holder);
	}

	public Map<String, Object>	exportEntityData(EntityHolder<ET> holder)
	{
		return super.exportEntityData(holder);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			createDynamic(EntityHolder<?> composition, Map<String, Object> data)
	{
		return super.createDynamic(composition, data);
	}

	public EntityHolder<ET> 	retrieveDynamic(EntityHolder<?> composition, PKT pk)
	{
		return super.retrieveDynamic(composition, pk);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			updateDynamic(EntityHolder<ET> roleObject, PKT pk, Map<String, Object> updates)
	{
		return super.updateDynamic(roleObject, pk, updates);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean			deleteDynamic(EntityHolder<?> composition, PKT pk)
	{
		return super.deleteDynamic(composition, pk);
	}

	public long			listDynamic(List<EntityHolder<ET>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit)
	{
		return super.listDynamic(results, composition, filter, sorts, start, limit);
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception
	{
		return code.call();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public <R> R			runTransactionedSafe(java.util.concurrent.Callable<R> code)
	{
		try {
			return code.call();
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
