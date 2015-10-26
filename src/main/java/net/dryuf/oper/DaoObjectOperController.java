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

package net.dryuf.oper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.validation.constraints.NotNull;

import net.dryuf.app.ClassMetaManager;
import net.dryuf.core.ConversionUtil;
import net.dryuf.core.Textual;
import net.dryuf.core.NoSuchBeanException;
import org.junit.Assert;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.FieldDef;
import net.dryuf.core.AppContainer;
import net.dryuf.core.CallerContext;
import net.dryuf.core.EntityHolder;
import net.dryuf.dao.DynamicDao;
import net.dryuf.meta.RelationDef;
import net.dryuf.oper.ObjectOperContext.ListParams;


public class DaoObjectOperController<ET, PKT> extends AbstractObjectOperController<ET>
{
	@Override
	public void			afterAppContainer(@NotNull AppContainer appContainer)
	{
		super.afterAppContainer(appContainer);
		if (objectDao == null)
			throw new IllegalArgumentException("objectDao not specified");
		if (dataClass == null)
			setDataClass(objectDao.getEntityClass());
	}

	@SuppressWarnings("unchecked")
	public void			setDataClass(Class<ET> dataClass)
	{
		this.dataClass = dataClass;
		this.dataMeta = ClassMetaManager.openCached(null, dataClass, null);
		this.dataPkClass = (Class<PKT>) this.dataMeta.getPkClass();
		if (this.dataMeta.isPkEmbedded())
			this.pkMeta = (ClassMeta<PKT>) dataMeta.getPkFieldDef().getEmbedded();
		if (this.dataMeta.hasCompos())
			this.composMeta = (ClassMeta<Object>)ClassMetaManager.openCached(null, dataMeta.getComposClass(), null);
	}

	public Class<ET>		getDataClass()
	{
		return this.dataClass;
	}

	public void			setObjectDao(DynamicDao<ET, PKT> objectDao)
	{
		Assert.assertNotNull(objectDao);
		this.objectDao = objectDao;
	}

	public String[]			getObjectId(ObjectOperContext operContext)
	{
		if (!dataMeta.isPkEmbedded()) {
			return getObjectIdList(operContext, 1);
		}
		else {
			return getObjectIdList(operContext, dataMeta.getAdditionalPkFields().length);
		}
	}

	public Object			convertStringToKeyField(Class<?> keyClazz, String str)
	{
		return ConversionUtil.parseStringToClass(keyClazz, str);
	}

	@SuppressWarnings("unchecked")
	public PKT			getDataPk(EntityHolder<?> ownerHolder, String[] objectId)
	{
		if (!dataMeta.isPkEmbedded()) {
			return (PKT)convertStringToKeyField(dataMeta.getPkFieldDef().getType(), objectId[0]);
		}
		else {
			ET e = dataMeta.instantiate();
			if (composMeta != null)
				dataMeta.setComposKey(e, composMeta.getEntityPkValue(ownerHolder.getEntity()));
			PKT pk = (PKT) dataMeta.getEntityPkValue(e);
			for (int i = 0; i < dataMeta.getAdditionalPkFields().length; i++) {
				FieldDef<Object> fd = (FieldDef<Object>)pkMeta.getPathField(dataMeta.getAdditionalPkFields()[i]);
				pkMeta.setEntityPathValue(pk, dataMeta.getAdditionalPkFields()[i], convertStringToKeyField(fd.getType(), objectId[i]));
			}
			return pk;
		}
	}

	protected void			keepContextTransaction(CallerContext callerContext)
	{
		objectDao.keepContextTransaction(callerContext);
	}

	@Override
	protected EntityHolder<ET>	loadObject(EntityHolder<?> ownerHolder, String[] objectId)
	{
		return this.objectDao.retrieveDynamic(ownerHolder, getDataPk(ownerHolder, objectId));
	}

	@Override
	public String			operateStaticMeta(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		return MetaExport.buildMeta(operContext.getCallerContext(), this.dataClass, operContext.getRequest().getParamDefault("view", "Default"), operContext.getRequest().getContextPath()+"/_oper/"+this.dataClass.getName()+"/?");
	}

	@Override
	public Object			operateStaticList(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		keepContextTransaction(ownerHolder.getRole());
		ListContainer<ET> out = new ListContainer<ET>();
		out.total = executeStaticList(out.objects, operContext, ownerHolder, operContext.getListParams());
		return out;
	}

	public long			executeStaticList(List<EntityHolder<ET>> results, ObjectOperContext operContext, EntityHolder<?> ownerHolder, ListParams listParams)
	{
		return objectDao.listDynamic(results, ownerHolder, listParams.getFilters(), listParams.getSorts(), listParams.getOffset(), listParams.getLimit());
	}

	@Override
	public Object			operateStaticSuggest(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		ListContainer<ET> out = new ListContainer<ET>();
		out.total = executeStaticSuggest(out.objects, operContext, ownerHolder, operContext.getListParams());
		return out;
	}

	public long			executeStaticSuggest(List<EntityHolder<ET>> results, ObjectOperContext operContext, EntityHolder<?> ownerHolder, ListParams listParams)
	{
		return objectDao.listDynamic(results, ownerHolder, listParams.getFilters(), listParams.getSorts(), listParams.getOffset(), listParams.getLimit());
	}

	@Override
	public Object			operateStaticCreate(ObjectOperContext operContext, EntityHolder<?> ownerHolder, Map<String, Object> data)
	{
		if (!ownerHolder.checkAccess(this.dataMeta.getEntityRoles().roleNew())) {
			throw net.dryuf.validation.AccessValidationException.createObjectException("Denied to new");
		}
		else {
			Map<String, Object> prepared = this.prepareStaticCreate(operContext, ownerHolder, data);
			EntityHolder<ET> objectHolder = this.executeStaticCreate(operContext, ownerHolder, data);
			this.triggerStaticCreate(operContext, objectHolder, prepared);
			return objectHolder;
		}
	}

	public Map<String, Object>	prepareStaticCreate(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<?> ownerHolder, Map<String, Object> inputData)
	{
		return inputData;
	}

	public void			triggerStaticCreate(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<ET> objectHolder, Map<String, Object> prepared)
	{
	}

	@SuppressWarnings("unchecked")
	public EntityHolder<ET>		executeStaticCreate(final ObjectOperContext operContext, final EntityHolder<?> ownerHolder, final Map<String, Object> readValue)
	{
		try {
			return objectDao.runTransactionedNew(new Callable<EntityHolder<ET>>() {
				@Override
				public EntityHolder<ET> call() {
					ET object = objectDao.createDynamic(ownerHolder, readValue);
					return objectDao.retrieveDynamic(ownerHolder, (PKT)dataMeta.getEntityPkValue(object));
				}
			});
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Actioner			findActioner(final String actionName)
	{
		Actioner actioner;
		if ((actioner = super.findActioner(actionName)) != null)
			return actioner;

		RelationDef relationDef = this.dataMeta.getRelation(actionName);

		if (relationDef != null) {
			final ObjectOperController<?> actionController;
			try {
				actionController = (ObjectOperController<?>)appContainer.getBean(relationDef.targetClass()+"-oper");
			}
			catch (NoSuchBeanException ex) {
				return null;
			}

			return new Actioner() {
				@Override
				public String			getActionName()
				{
					return actionName;
				}
				@Override
				public ObjectOperRules		getOperRules() {
					return new ObjectOperRules() {
						@Override public Class<? extends Annotation> annotationType() { return null; }
						@Override public String value() { return actionName; }
						@Override public String reqRole() { return ""; }
						@Override public boolean isStatic() { return false; }
						@Override public boolean isFinal() { return false; }
						@SuppressWarnings({"unchecked", "rawtypes"})
						@Override public Class<? extends Textual<?>>[] parameters() { return new Class[0]; }
						@Override public Class<?> actionClass() { return void.class; }
					};
				}
				@Override
				public Object			runAction(AbstractObjectOperController<?> controller, ObjectOperContext operContext, EntityHolder<?> ownerHolder)
				{
					return actionController.operate(operContext, ownerHolder);
				}
			};
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object			operateObjectUpdate(final ObjectOperContext operContext, final EntityHolder<ET> objectHolder, final Map<String, Object> data)
	{
		if (!objectHolder.checkAccess(this.dataMeta.getEntityRoles().roleSet())) {
			throw net.dryuf.validation.AccessValidationException.createObjectException("Denied to set");
		}
		else {
			try {
				objectDao.runTransactionedNew(new Callable<Object>() {
					@Override
					public Object call() {
						Map<String, Object> prepared = prepareObjectUpdate(operContext, objectHolder, data);
						EntityHolder<ET> updatedObjectHolder = executeObjectUpdate(operContext, objectHolder, data);
						triggerObjectUpdate(operContext, updatedObjectHolder, prepared);
						return null;
					}
				});
				objectDao.keepContextTransaction(operContext.getCallerContext());
				return this.objectDao.retrieveDynamic(objectHolder, (PKT)dataMeta.getEntityPkValue(objectHolder.getEntity()));
			}
			catch (RuntimeException ex) {
				throw ex;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public EntityHolder<ET>		executeObjectUpdate(final ObjectOperContext operContext, final EntityHolder<ET> objectHolder, final Map<String, Object> data)
	{
		try {
			ET object = objectDao.updateDynamic(objectHolder, (PKT)dataMeta.getEntityPkValue(objectHolder.getEntity()), data);
			return objectDao.retrieveDynamic(objectHolder, (PKT)dataMeta.getEntityPkValue(object));
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, Object> 	prepareObjectUpdate(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<ET> objectHolder, Map<String, Object> inputData)
	{
		return inputData;
	}

	public void			triggerObjectUpdate(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<ET> objectHolder, Map<String, Object> prepared)
	{
	}

	@Override
	public Object			operateObjectRetrieve(ObjectOperContext operContext, EntityHolder<ET> objectHolder)
	{
		return objectHolder;
	}

	@Override
	public Object			operateObjectDelete(final ObjectOperContext operContext, final EntityHolder<ET> objectHolder)
	{
		if (!objectHolder.checkAccess(this.dataMeta.getEntityRoles().roleDel())) {
			throw net.dryuf.validation.AccessValidationException.createObjectException("Denied to del");
		}
		else {
			try {
				return objectDao.runTransactionedNew(new Callable<Object>() {
					@Override
					public Object call() {
						Map<String, Object> prepared = prepareObjectDelete(operContext, objectHolder);
						boolean success = executeObjectDelete(operContext, objectHolder);
						if (success) {
							triggerObjectDelete(operContext, objectHolder, prepared);
						}
						return new SuccessContainer(success);
					}
				});
			}
			catch (RuntimeException ex) {
				throw ex;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean			executeObjectDelete(ObjectOperContext operContext, EntityHolder<ET> objectHolder)
	{
		return this.objectDao.deleteDynamic(objectHolder, (PKT)dataMeta.getEntityPkValue(objectHolder.getEntity()));
	}

	public Map<String, Object>	prepareObjectDelete(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<ET> objectHolder)
	{
		return null;
	}

	public void			triggerObjectDelete(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<ET> objectHolder, Map<String, Object>prepared)
	{
	}

	protected Class<ET>		dataClass;

	protected ClassMeta<ET>		dataMeta;

	protected Class<PKT>		dataPkClass;

	protected ClassMeta<PKT>	pkMeta;

	protected ClassMeta<Object>	composMeta;

	protected DynamicDao<ET, PKT>	objectDao;
}
