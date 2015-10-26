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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.dryuf.app.ClassMetaManager;
import net.dryuf.core.ReportException;
import net.dryuf.util.MapUtil;
import net.dryuf.validation.DataValidationErrors;
import net.dryuf.validation.ObjectValidationErrors;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.FieldDef;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.core.InvalidValueException;
import net.dryuf.meta.FilterDef;
import net.dryuf.meta.PKeyDef;
import net.dryuf.meta.ListOrder;
import net.dryuf.meta.SuggestDef;
import net.dryuf.meta.ViewInfo;
import net.dryuf.validation.ObjectRoleUtil;
import net.dryuf.validation.DataValidatorUtil;


public class RoleDaoAccessJpa<ET, PKT> extends java.lang.Object
{
	public				RoleDaoAccessJpa(Class<ET> clazz, EntityManager entityManager)
	{
		this.dataClass = clazz;
		this.entityManager = entityManager;
		initInfo();
	}

	@SuppressWarnings("unchecked")
	protected void			initInfo()
	{
		classMeta = ClassMetaManager.openCached(null, this.dataClass, null);
		if (classMeta.getComposClass() != null) {
			composMeta = ClassMetaManager.openCached(null, (Class<Object>) classMeta.getComposClass(), null);
			composPath = classMeta.getComposPath();
		}
		pkClass = classMeta.getPkClass();
		pkName = classMeta.getPkName();
	}

	@SuppressWarnings("unchecked")
	public RoleProcessor<ET>	createRoleProcessor(CallerContext baseContext)
	{
		RoleProcessor<ET> roleProc;
		RoleProcessorUse roleProcessorUse = this.dataClass.getAnnotation(RoleProcessorUse.class);
		if (roleProcessorUse != null) {
			roleProc = (RoleProcessor<ET>)Dryuf.createObjectArgs(Dryuf.getConstructor(roleProcessorUse.roleProcessor(), CallerContext.class, Class.class), baseContext, this.dataClass);
		}
		else {
			roleProc = new AbstractRoleProcessor<ET>(baseContext, this.dataClass);
		}

		return roleProc;
	}

	public Query			createQuery(StringBuilder squery, Object[] binds)
	{
		int bindargs = 0;
		for (int p = 0, length = squery.length(); p < length; ) {
			int quotePos = squery.indexOf("\'", p);
			int questionPos = squery.indexOf("?", p);

			if (quotePos >= 0 && (questionPos < 0 || quotePos < questionPos)) {
				for (p = quotePos+1; squery.charAt(p) != '\''; p++) {
					if (squery.charAt(p) == '\\')
						++p;
				}
			}
			else if (questionPos >= 0) {
				p = questionPos+1;
				squery.insert(p, ++bindargs);
				length = squery.length();
			}
			else {
				break;
			}
		}
		Assert.isTrue(bindargs == binds.length, "binds count do not match");
		String ssquery = squery.toString();
		Query query = entityManager.createQuery(ssquery);
		for (int i = 0; i < binds.length; i++) {
			try {
				query.setParameter(i+1, binds[i]);
			}
			catch (Exception ex) {
				throw new RuntimeException("failed to bind "+i+" parameter for query: "+ssquery, ex);
			}
		}
		return query;
	}

	public Object			getPkValue(ET object)
	{
		return classMeta.getEntityPkValue(object);
	}

	public Object			importClassDynamic(Class<?> clazz, Object data)
	{
		if (Boolean.class.isAssignableFrom(clazz)) {
			return Dryuf.invokeMethod(Dryuf.getClassMethod(clazz, "valueOf", String.class), null, data.toString());
		}
		else if (Number.class.isAssignableFrom(clazz)) {
			return Dryuf.invokeMethod(Dryuf.getClassMethod(clazz, "valueOf", String.class), null, data.toString());
		}
		else if (String.class.isAssignableFrom(clazz)) {
			return (String)data;
		}
		else {
			@SuppressWarnings("unchecked")
			ClassMeta<Object> meta = (ClassMeta<Object>)ClassMetaManager.openCached(null, clazz, null);
			Object result = meta.instantiate();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)data;
			for (Map.Entry<String, Object> entry: map.entrySet()) {
				String key = entry.getKey();
				FieldDef<?> fieldDef = meta.getField(key);
				meta.setEntityFieldValue(result, entry.getKey(), importClassDynamic(fieldDef.getType(), entry.getValue()));
			}
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public PKT			importDynamicKey(Object key)
	{
		return (PKT)importClassDynamic(pkClass, key);
	}

	public Map<String, Object>	exportDynamicData(EntityHolder<ET> holder)
	{
		return ObjectRoleUtil.getWithRole(holder.getEntity(), holder.getRole());
	}

	public Map<String, Object>	exportEntityData(EntityHolder<ET> holder)
	{
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("role", holder.getRole().getRoles());
		result.put("view", holder.getView());
		result.put("entity", ObjectRoleUtil.getWithRole(holder.getEntity(), holder.getRole()));
		return result;
	}

	public ViewSupplier<ET>		createViewSupplier(ViewInfo viewInfo, CallerContext callerContext)
	{
		return null;
	}

	public String			getFieldJql(String fieldName)
	{
		//FieldDef fieldDef = classMeta.getField(fieldName);
		//if (!StringUtils.isEmpty(fieldDef.getPrefield()))
		//	return fieldDef.getPrefield()+"."+fieldName;
		return fieldName;
	}

	@SuppressWarnings("unchecked")
	public long			listObjects(List<EntityHolder<ET>> results, EntityHolder<?> composition, Map<String, Object> filters, List<String> sorts, Long start, Long limit)
	{
		PKeyDef keyDef = this.dataClass.getAnnotation(PKeyDef.class);

		if (filters == null)
			filters = new HashMap<String, Object>();

		RoleQuery roleQuery = new RoleQuery();

		roleQuery.setColumns("ent");
		roleQuery.setTable(" FROM "+this.getJqlName()+" ent");
		roleQuery.setWhere(" WHERE 0=0");

		{
			StringBuilder ssort = new StringBuilder();
			if (sorts == null || sorts.size() == 0) {
				ListOrder listOrder = this.dataClass.getAnnotation(ListOrder.class);
				if (listOrder != null && listOrder.order().length != 0) {
					for (String fieldName: listOrder.order()) {
						ssort.append(", ent.").append(fieldName);
					}
				}
				else {
					ssort.append(", ent."+getPkName());
				}
			}
			else {
				for (String s: sorts) {
					ssort.append(", ent.").append(s);
				}
			}
			roleQuery.appendSort("ORDER BY "+ssort.substring(1));
		}

		{
			if (!keyDef.composPath().equals("")) {
				roleQuery.appendWhere(" AND ent."+keyDef.composPath()+" = ?");
				roleQuery.appendWhereBind(composMeta.getEntityPkValue((Object)composition.getEntity()));
			}
		}

		for (Entry<String, Object> filter: filters.entrySet()) {
			String fkey = filter.getKey();
			Object fvalue = filter.getValue();
			String jqlName;
			if (fkey.startsWith("-")) {
				if (fkey.equals("-composKey")) {
					throw new ReportException("-composKey unsupported yet");
				}
				else if (fkey.equals("-cond")) {
					for (Map<String, Object> cond: (List<Map<String, Object>>)fvalue) {
						String ckey = (String) MapUtil.getMapMandatory(cond, "field");
						Object cvalue = MapUtil.getMapMandatory(cond, "value");
						String op = (String) MapUtil.getMapMandatory(cond, "op");
						if ((jqlName = getFieldJql(ckey)) == null)
							throw new ReportException("field not found: "+ckey);
						String jqlOperation;
						if ((jqlOperation = jqlOperatorMap.get(op)) == null)
							throw new ReportException("operator not found: "+op);
						roleQuery.appendWhere(" AND ent."+jqlName+" "+jqlOperation+" ?");
						roleQuery.appendWhereBind(cvalue);
					}
				}
				else if (fkey.equals("-suggest")) {
					fvalue = fvalue+"%";
					SuggestDef suggestDef = Dryuf.getMandatoryAnnotation(this.dataClass, SuggestDef.class);
					roleQuery.appendWhere(" AND (0=1");
					for (String field: suggestDef.fields()) {
						roleQuery.appendWhere(" OR ent."+getFieldJql(field)+" LIKE ?");
						roleQuery.appendWhereBind(fvalue);
					}
					roleQuery.appendWhere(")");
				}
				else {
					throw new ReportException("unknown high-level filter: "+fkey);
				}
			}
			else if (fkey.startsWith(":")) {
				FilterDef filterDef;
				if ((filterDef = classMeta.getFilterDefsHash().get(fkey.substring(1))) == null)
					throw new RuntimeException("unsupported filterDef: "+fkey);
				String condition = filterDef.condition().replace(fkey, "?");
				roleQuery.appendWhere(" AND "); roleQuery.appendWhere(condition);
				roleQuery.appendWhereBind(fvalue);
			}
			else if ((jqlName = getFieldJql(fkey)) != null) {
				roleQuery.appendWhere(" AND ent."+jqlName+" = ?");
				roleQuery.appendWhereBind(fvalue);
			}
			else {
				throw new ReportException(fkey+" not defined as a field");
			}
		}
		RoleProcessor<ET> roleProc = createRoleProcessor(composition.getRole());
		roleProc.modifyQuery(roleQuery);
		String sstatement = "SELECT "+roleQuery.getColumns()+" "+roleQuery.getTable()+" "+roleQuery.getWhere()+" "+roleQuery.getSort();
		Query query = createQuery(new StringBuilder(sstatement), roleQuery.getBinds().toArray());
		if (limit != null) {
			query.setFirstResult(start.intValue());
			query.setMaxResults(limit.intValue());
		}
		List<Object> objects = query.getResultList();

		ViewSupplier<ET> viewSupplier = this.createViewSupplier(null, composition.getRole());
		for (Object row: objects) {
			EntityHolder<ET> result = roleProc.createEntityFromResult(row);
			if (viewSupplier != null)
				viewSupplier.processResult(result);
			results.add(result);
		}
		if (limit == null) {
			return objects.size();
		}
		else {
			Query countQuery = createQuery(new StringBuilder("SELECT COUNT(*) ").append(roleQuery.getTable()).append(" ").append(roleQuery.getWhere()), roleQuery.getWhereBinds().toArray());
			return ((Number)countQuery.getSingleResult()).longValue();
		}
	}

	public EntityHolder<ET>		retrieveObject(EntityHolder<?> composition, PKT pk)
	{
		if (pk == null)
			throw new InvalidValueException(pk, "missing pk");
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setColumns("ent");
		roleQuery.setWhere("WHERE ent."+getPkName()+" = ?");
		roleQuery.setTable(" FROM "+this.getJqlName()+" ent");
		roleQuery.appendWhereBind(pk);

		RoleProcessor<ET> roleProc = createRoleProcessor(composition.getRole());
		roleProc.modifyQuery(roleQuery);

		String sstatement = "SELECT "+roleQuery.getColumns()+" "+roleQuery.getTable()+" "+roleQuery.getWhere()+" "+roleQuery.getSort();
		Query query = createQuery(new StringBuilder(sstatement), roleQuery.getBinds().toArray());

		@SuppressWarnings("unchecked")
		List<Object> objects = query.getResultList();

		if (objects.size() == 0)
			return null;

		return roleProc.createEntityFromResult(objects.get(0));
	}

	public ET			createObject(EntityHolder<?> composition, Map<String, Object> values)
	{
		if (!classMeta.canNew(composition.getRole()))
			throw new SecurityException("Denied to new "+dataClass.getName());
		ET object = null;
		try {
			object = this.dataClass.newInstance();
		}
		catch (Exception ex) {
			throw Dryuf.translateException(ex);
		}
		ObjectRoleUtil.newWithRole(new ObjectValidationErrors(object), object, composition.getRole(), values);
		if (classMeta.hasCompos()) {
			classMeta.setComposKey(object, composMeta.getEntityPkValue(composition.getEntity()));
		}
		entityManager.persist(object);
		entityManager.flush();
		return object;
	}

	public ET			updateObject(EntityHolder<ET> holder, Object pk, Map<String, Object> values)
	{
		ET object = holder.getEntity();
		DataValidatorUtil.validateWithSet(holder.getRole(), object, values);
		entityManager.merge(object);
		entityManager.flush();
		return object;
	}

	public boolean			deleteObject(EntityHolder<?> composition, PKT pk)
	{
		EntityHolder<ET> holder = this.retrieveObject(composition, pk);
		if (holder == null)
			return false;
		if (!classMeta.canDel(holder.getRole()))
			throw new SecurityException("Denied to new "+dataClass.getName());
		String sstatement = "DELETE FROM "+getJqlName()+" WHERE "+getPkName()+" = ?";
		return createQuery(new StringBuilder(sstatement), new Object[]{ pk }).executeUpdate() != 0;
	}

	public String			getComposPath()
	{
		return composPath;
	}

	protected String		getJqlName()
	{
		return this.dataClass.getName();
	}

	protected Class<ET>		dataClass;

	public Class<ET>		getDataClass()
	{
		return this.dataClass;
	}

	protected EntityManager		entityManager;

	public EntityManager		getEntityManager()
	{
		return this.entityManager;
	}

	Class<?>			pkClass;

	public Class<?>			getPkClass()
	{
		return this.pkClass;
	}

	String				pkName;

	public String			getPkName()
	{
		return this.pkName;
	}

	protected ClassMeta<ET>		classMeta;

	protected Field			pkField;

	protected String		composPath;

	protected ClassMeta<Object>	composMeta;

	protected final static Map<String, String> jqlOperatorMap = MapUtil.createHashMap(
			"=",		"==",
			"==",		"==",
			"!=",		"!=",
			">",		">",
			"<",		"<",
			">=",		">=",
			"<=",		"<=",
			"LIKE",		"LIKE"
			);
};
