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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.transaction.TransactionHandler;
import net.dryuf.transaction.TransactionManager;


public abstract class DaoContextBase<ET, PKT> extends java.lang.Object implements DynamicDao<ET, PKT>
{
	public				DaoContextBase(Class<ET> entityClass)
	{
		this.entityClass = entityClass;
		if (this.entityClass == null)
			return;
		this.pkName = resolvePkField();
	}

	@SuppressWarnings("unchecked")
	protected String		resolvePkField()
	{
		for (Field field: entityClass.getFields()) {
			if (field.getAnnotation(Id.class) != null || field.getAnnotation(EmbeddedId.class) != null) {
				this.pkName = field.getName();
				this.pkClass = (Class<PKT>) field.getType();
			}
		}
		for (Method method: entityClass.getMethods()) {
			if (method.getName().startsWith("get") && (method.getAnnotation(Id.class) != null || method.getAnnotation(EmbeddedId.class) != null)) {
				this.pkName = method.getName();
				this.pkName = this.pkName.substring(0, 1).toLowerCase()+this.pkName.substring(1);
				this.pkClass = (Class<PKT>) method.getReturnType();
			}
		}
		for (Class<?> clazz = entityClass; this.pkName == null && clazz != null; clazz = clazz.getSuperclass()) {
			for (Field field: entityClass.getDeclaredFields()) {
				if (field.getAnnotation(Id.class) != null || field.getAnnotation(EmbeddedId.class) != null) {
					this.pkName = field.getName();
					this.pkClass = (Class<PKT>) field.getType();
				}
			}
		}
		if (pkName == null)
			throw new RuntimeException("cannot find primary key in class "+this.entityClass.getName());
		return pkName;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ET			refresh(ET obj)
	{
		entityManager.refresh(obj);
		return obj;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ET			loadByPk(PKT pk)
	{
		return entityManager.find(entityClass, pk);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ET>			listAll()
	{
		return (List<ET>)entityManager.createQuery("SELECT ent FROM "+Dryuf.dotClassname(entityClass)+" ent ORDER BY "+this.pkName).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void			insert(ET obj)
	{
		entityManager.persist(obj);
		entityManager.flush();
	}

	@Override
	public void			insertTxNew(final ET obj)
	{
		runTransactionedNewSafe(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				entityManager.persist(obj);
				entityManager.flush();
				return false;
			}
		});
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			update(ET obj)
	{
		obj = entityManager.merge(obj);
		entityManager.flush();
		return obj;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void			remove(ET obj)
	{
		entityManager.remove(obj);
		entityManager.flush();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean			removeByPk(PKT pk)
	{
		return entityManager.createQuery("DELETE FROM "+Dryuf.dotClassname(entityClass)+" obj WHERE obj."+pkName+" = ?1").setParameter(1, pk).executeUpdate() != 0;
	}

	public PKT			openRelation(EntityHolder<ET> holder, String relation)
	{
		throw new UnsupportedOperationException("openRelation not supported yet");
	}

	public PKT			importDynamicKey(Map<String, Object> data)
	{
		return getRoleDaoAccess().importDynamicKey(data);
	}

	public Map<String, Object>	exportDynamicData(EntityHolder<ET> holder)
	{
		return getRoleDaoAccess().exportDynamicData(holder);
	}

	public Map<String, Object>	exportEntityData(EntityHolder<ET> holder)
	{
		return getRoleDaoAccess().exportEntityData(holder);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			createDynamic(EntityHolder<?> composition, Map<String, Object> data)
	{
		return getRoleDaoAccess().createObject(composition, data);
	}

	@Override
	public EntityHolder<ET>		retrieveDynamic(EntityHolder<?> composition, PKT pk)
	{
		return getRoleDaoAccess().retrieveObject(composition, pk);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ET			updateDynamic(EntityHolder<ET> roleObject, PKT pk, Map<String, Object> updates)
	{
		return getRoleDaoAccess().updateObject(roleObject, pk, updates);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean			deleteDynamic(EntityHolder<?> composition, PKT pk)
	{
		return getRoleDaoAccess().deleteObject(composition, pk);
	}

	@Override
	public long			listDynamic(List<EntityHolder<ET>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit)
	{
		return getRoleDaoAccess().listObjects(results, composition, filter, sorts, start, limit);
	}

	@Override
	public TransactionHandler	keepContextTransaction(CallerContext callerContext)
	{
		return transactionManager.keepContextTransaction(callerContext);
	}

	@Override
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception
	{
		TransactionHandler transaction = transactionManager.openTransaction(false);
		try {
			R ret = code.call();
			transaction.commit();
			return ret;
		}
		catch (Exception ex) {
			transaction.close();
			throw ex;
		}
	}

	@Override
	public <R> R			runTransactionedNewSafe(Callable<R> code)
	{
		TransactionHandler transaction = transactionManager.openTransaction(false);
		try {
			R ret = code.call();
			transaction.commit();
			return ret;
		}
		catch (RuntimeException ex) {
			transaction.close();
			throw ex;
		}
		catch (Exception ex) {
			transaction.close();
			throw new RuntimeException(ex);
		}
	}

	protected RoleDaoAccessJpa<ET, PKT> getRoleDaoAccess()
	{
		return roleDaoAccess;
	}

	protected void			setEntityManagerInternal(EntityManager entityManager)
	{
		this.entityManager = entityManager;
		roleDaoAccess = new RoleDaoAccessJpa<ET, PKT>(entityClass, entityManager);
	}

	protected void			setTransactionManagerInternal(TransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	protected TransactionManager	transactionManager;

	protected EntityManager		entityManager;

	protected Class<ET>		entityClass;

	public Class<ET>		getEntityClass()
	{
		return this.entityClass;
	}
	protected Class<PKT>		pkClass;
	protected String		pkName;

	protected RoleDaoAccessJpa<ET, PKT> roleDaoAccess;
}
