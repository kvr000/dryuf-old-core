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

package net.dryuf.app;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;

import net.dryuf.textual.TextualManager;
import net.dryuf.meta.ReferenceDef;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import net.dryuf.core.AppContainer;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.Textual;
import net.dryuf.textual.DisplayUse;
import net.dryuf.textual.TextualUse;
import net.dryuf.meta.ActionDef;
import net.dryuf.meta.ActionDefs;
import net.dryuf.meta.AssocDef;
import net.dryuf.meta.DisplayKeysDef;
import net.dryuf.meta.FieldOrder;
import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.FilterDef;
import net.dryuf.meta.FilterDefs;
import net.dryuf.meta.Mandatory;
import net.dryuf.meta.PKeyDef;
import net.dryuf.meta.RefFieldsDef;
import net.dryuf.meta.RelationDef;
import net.dryuf.meta.RelationDefs;
import net.dryuf.meta.SuggestDef;
import net.dryuf.meta.ViewInfo;
import net.dryuf.meta.ViewsList;
import net.dryuf.net.util.UrlUtil;
import net.dryuf.core.StringUtil;


public class ClassMetaJava<ET> extends java.lang.Object implements ClassMeta<ET>
{
	@SuppressWarnings("unchecked")
	public static <ET> ClassMeta<ET> openCached(AppContainer appContainer, Class<ET> dataClass, String dataView)
	{
		if (dataView == null)
			dataView = "Default";
		String full = dataClass.getName()+"-"+dataView;
		ClassMetaJava<ET> meta = (ClassMetaJava<ET>) cachedData.get(full);
		if (meta == null) {
			meta = new ClassMetaJava<ET>(dataClass, dataView);
			meta.parseDataDescription();
			cachedData.put(full, meta);
		}
		return meta;
	}

	public static <ET> ClassMetaJava<ET> openEmbedded(AppContainer appContainer, Class<ET> dataClass, String basePath, String composPath)
	{
		ClassMetaJava<ET> meta = new ClassMetaJava<ET>(dataClass, null);
		meta.basePath = basePath;
		meta.embedded = true;
		meta.composPath = composPath;
		meta.parseDataDescription();
		return meta;
	}

	public static void		refresh()
	{
		cachedData.clear();
	}

	protected			ClassMetaJava(Class<ET> dataClass, String dataView)
	{
		this.dataClass = dataClass;
		this.dataView = dataView;
	}

	public Object			convertField(CallerContext callerContext, FieldDef<?> fdef, String value)
	{
		Textual<?> textual = TextualManager.createTextualUnsafe(fdef.needTextual(), callerContext);
		return textual.convert(value, null);
	}

	public String			getDataClassName()
	{
		return getDataClass().getName();
	}

	public ET			instantiate()
	{
		try {
			return dataClass.newInstance();
		}
		catch (Exception ex) {
			throw Dryuf.translateException(ex);
		}
	}

	public boolean			canNew(CallerContext callerContext)
	{
		return callerContext.checkRole(entityRoles.roleNew());
	}

	public boolean			canDel(CallerContext callerContext)
	{
		return callerContext.checkRole(entityRoles.roleDel());
	}

	public boolean			hasCompos()
	{
		return composPath != null;
	}

	public FieldDef<?>		getPkFieldDef()
	{
		return pkFieldDef;
	}

	/**
	 * @return
	 * 	list of additional PK fields within
	 */
	public String[]			getAdditionalPkFields()
	{
		return this.pkeyDef.additionalPkFields();
	}

	/**
	 * Gets list of field definitions.
	 */
	public FieldDef<?>[]		getFields()
	{
		return this.fieldDefs;
	}

	/**
	 * Gets the field name of the key.
	 */
	public String			getRefName()
	{
		return this.pkeyDef.pkField();
	}

	/**
	 * Gets object key from the existing object.
	 */
	public Object			getEntityPkValue(ET entity)
	{
		return pkFieldDef.getValue(entity);
	}

	/**
	 * Sets object key.
	 */
	@SuppressWarnings("unchecked")
	public void			setEntityPkValue(ET entity, Object value)
	{
		((FieldDefImpl<Object>)pkFieldDef).setValue(entity, value);
	}

	@SuppressWarnings("unchecked")
	public void			setComposKey(ET entity, Object composKey)
	{
		if (composBaseField.getEmbedded() != null) {
			((ClassMetaJava<Object>)composBaseField.getEmbedded()).setComposKey(composBaseField.getValue(entity), composKey);
		}
		else {
			((FieldDefImpl<Object>)composBaseField).setValue(entity, composKey);
		}
	}

	@SuppressWarnings("unchecked")
	public Object			getComposKey(ET entity)
	{
		if (composBaseField.getEmbedded() != null) {
			return ((ClassMetaJava<Object>)((FieldDefImpl<Object>)composBaseField).getEmbedded()).getComposKey((Object)composBaseField.getValue(entity));
		}
		else {
			return composBaseField.getValue(entity);
		}
	}

	public ActionDef[]		getActions()
	{
		return this.actionDefs;
	}

	public FieldRoles		getFieldRoles(String name)
	{
		FieldRoles roles = getField(name).getRoles();
		if (roles == null)
			roles = getEntityRoles();
		return roles;
	}

	public FieldDef<?>		getField(String name)
	{
		FieldDef<?> fieldDef = this.fieldDefsHash.get(name);
		if (fieldDef == null)
			throw new RuntimeException("asking for unknown field named "+name);
		return fieldDef;
	}

	@SuppressWarnings("unused")
	public Object			getEntityFieldValue(ET entity, String fieldName)
	{
		FieldDefImpl<?> fieldDef = (FieldDefImpl<?>)this.getField(fieldName);
		if (true /*"".equals(fieldDef.getPrefield())*/) {
			return doGetObjectField(entity, fieldDef);
		}
		else {
			return null;
			//return doGetObjectField(Dryuf.getFieldValueNamed(entity, fieldDef.getPrefield()), fieldDef);
		}
	}

	@SuppressWarnings("unused")
	public void			setEntityFieldValue(ET entity, String fieldName, Object value)
	{
		FieldDefImpl<?> fieldDef = (FieldDefImpl<?>)this.getField(fieldName);
		if (true /*"".equals(fieldDef.getPrefield())*/) {
			doSetObjectField(entity, fieldDef, value);
		}
		else {
			//doSetObjectField(Dryuf.getFieldValueNamed(entity, fieldDef.getPrefield()), fieldDef, value);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public FieldDef<?>		getPathField(String path)
	{
		int p;
		if ((p = path.indexOf(".")) < 0) {
			return getField(path);
		}
		else {
			FieldDefImpl<Object> fieldDef = (FieldDefImpl<Object>) getField(path.substring(0, p));
			return ((ClassMetaJava<Object>)fieldDef.getEmbedded()).getPathField(path.substring(p+1));
		}
	}

	@SuppressWarnings({ "unchecked" })
	public Object			getEntityPathValue(ET entity, String path)
	{
		int p;
		if ((p = path.indexOf(".")) < 0) {
			return getEntityFieldValue(entity, path);
		}
		else {
			FieldDefImpl<Object> fieldDef = (FieldDefImpl<Object>) getField(path.substring(0, p));
			return (fieldDef.getEmbedded()).getEntityPathValue(fieldDef.getValue(entity), path.substring(p+1));
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void			setEntityPathValue(ET entity, String path, Object value)
	{
		int p;
		if ((p = path.indexOf(".")) < 0) {
			setEntityFieldValue(entity, path, value);
		}
		else {
			FieldDefImpl<Object> fieldDef = (FieldDefImpl<Object>) getField(path.substring(0, p));
			(fieldDef.getEmbedded()).setEntityPathValue(fieldDef.getValue(entity), path.substring(p+1), value);
		}
	}

	public RelationDef		getRelation(String name)
	{
		return relations.get(name);
	}

	public ActionDef		getAction(String name)
	{
		ActionDef actionDef = this.actionDefsHash.get(name);
		if (actionDef == null)
			throw new RuntimeException("asking for unknown action named "+name);
		return actionDef;
	}

	@SuppressWarnings("unchecked")
	public String			urlDisplayKey(CallerContext callerContext, ET entity)
	{
		StringBuilder sb = new StringBuilder();
		for (String fieldPath: this.getDisplayKeys()) {
			FieldDef<?> fieldDef = this.getPathField(fieldPath);
			sb.append(UrlUtil.encodeUrl(TextualManager.formatTextual((Class<Textual<Object>>)(Class<?>)fieldDef.needTextual(), callerContext, getEntityPathValue(entity, fieldPath)))).append("/");
		}
		return sb.toString();
	}

	public String			urlPkEntityKey(CallerContext callerContext, Object pk)
	{
		try {
			StringBuilder sb = new StringBuilder();
			if (!pkEmbedded) {
				FieldDef<?> fieldDef = getField(pkeyDef.pkField());
				sb.append(URLEncoder.encode(TextualManager.formatTextualUnsafe(fieldDef.needTextual(), callerContext, pk), "UTF-8")).append("/");
			}
			else {
				for (String fieldName: this.getAdditionalPkFields()) {
					FieldDef<?> fieldDef = getField(fieldName);
					sb.append(URLEncoder.encode(TextualManager.formatTextualUnsafe(fieldDef.needTextual(), callerContext, Dryuf.getFieldValueNamed(pk, fieldName)), "UTF-8")).append("/");
				}
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ActionDef>		getGlobalActionList(CallerContext callerContext)
	{
		List<ActionDef> actions = new LinkedList<ActionDef>();
		for (ActionDef action: this.actionDefs) {
			if (!action.isStatic())
				continue;
			if (!StringUtils.isEmpty(action.roleAction()) && !callerContext.checkRole(action.roleAction()))
				continue;
			actions.add(action);
		}
		return actions;
	}

	public List<ActionDef>		getObjectActionList(EntityHolder<ET> obj)
	{
		List<ActionDef> actions = new LinkedList<ActionDef>();
		for (ActionDef action: this.actionDefs) {
			if (action.isStatic())
				continue;
			if (!obj.getRole().checkRole(action.roleAction()))
				continue;
			actions.add(action);
		}
		return actions;
	}

	public String			formatAssocType(int stype)
	{
		return assocTypesStrings[stype];
	}

	protected static Object		doGetObjectField(Object o, FieldDefImpl<?> fieldDef)
	{
		if (fieldDef.getGetter() != null)
			return Dryuf.invokeMethod(o, fieldDef.getGetter());
		else
			return Dryuf.getFieldValue(o, fieldDef.getField());
	}

	protected static void		doSetObjectField(Object o, FieldDefImpl<?> fieldDef, Object value)
	{
		if (fieldDef.getSetter() != null)
			Dryuf.invokeMethod(o, fieldDef.getSetter(), value);
		else
			Dryuf.setFieldValue(o, fieldDef.getField(), value);
	}

	@SuppressWarnings("unchecked")
	protected void			parseDataDescription()
	{
		this.fieldDefsHash = new HashMap<String, FieldDefImpl<?>>();

		this.pkeyDef = dataClass.getAnnotation(PKeyDef.class);
		if (pkeyDef != null) {
			pkEmbedded = pkeyDef.pkEmbedded();
			this.pkClass = pkeyDef.pkClazz();
			if (StringUtils.isEmpty(this.pkName = pkeyDef.pkField()))
				this.pkName = null;
			if (StringUtils.isEmpty(this.composPath = pkeyDef.composPath()))
				this.composPath = null;
			if ((this.composClass = pkeyDef.composClazz()) == void.class)
				this.composClass = null;
			if ((this.composPkClass = pkeyDef.composPkClazz()) == void.class)
				this.composPkClass = null;
		}

		if (!this.embedded) {
			for (ViewInfo viewCheck: (Dryuf.getMandatoryAnnotation(dataClass, ViewsList.class)).views()) {
				if (viewCheck.name().equals(this.dataView)) {
					viewInfo = viewCheck;
					break;
				}
			}
			if (viewInfo == null)
				throw new RuntimeException("no view named '"+this.dataView+"' exists on "+this.dataClass.getName());
		}

		relations = new LinkedHashMap<String, RelationDef>();
		RelationDefs relationDefsAnno = this.dataClass.getAnnotation(RelationDefs.class);
		if (relationDefsAnno != null) {
			for (RelationDef relationDef: relationDefsAnno.relations())
				relations.put(relationDef.name(), relationDef);
		}

		List<FieldDef<?>> fieldDefsList = new LinkedList<FieldDef<?>>();
		fieldOrder = (viewInfo == null || viewInfo.fields().length == 1 && viewInfo.fields()[0].isEmpty()) ? (Dryuf.getMandatoryAnnotation(this.dataClass, FieldOrder.class)).fields() : viewInfo.fields();

		entityRoles = dataClass.getAnnotation(FieldRoles.class);
		for (String fieldName: fieldOrder) {
			FieldDefImpl<Object> fieldDef = new FieldDefImpl<Object>();
			fieldDef.setName(fieldName);
			fieldDef.setPath(basePath == null ? fieldName : (basePath+fieldName));
			try {
				fieldDef.setField(dataClass.getDeclaredField(fieldName));
			}
			catch (NoSuchFieldException e) {
			}
			try {
				fieldDef.setGetter(dataClass.getMethod("get"+StringUtil.capitalize(fieldName)));
			}
			catch (NoSuchMethodException e) {
			}
			if (fieldDef.getField() != null) {
				fieldDef.setType((Class<Object>)fieldDef.getField().getType());
			}
			else if (fieldDef.getGetter() != null) {
				fieldDef.setType((Class<Object>)fieldDef.getGetter().getReturnType());
			}
			else {
				throw new RuntimeException("no field nor getter defined for "+dataClass.getName()+"."+fieldName);
			}
			try {
				fieldDef.setSetter(dataClass.getMethod("set"+StringUtil.capitalize(fieldName), fieldDef.getType()));
			}
			catch (NoSuchMethodException e) {
			}
			AssocDef assocDef = getFieldDefAnnotation(fieldDef, AssocDef.class);
			if (assocDef != null) {
				fieldDef.setAssocClass(assocDef.target());
			}
			fieldDef.setReferenceDef(getFieldDefAnnotation(fieldDef, ReferenceDef.class));
			if (relations.containsKey(fieldName)) {
				fieldDef.setAssocType(assocDef != null ? assocDef.assocType() : FieldDef.AST_Children);
				fieldDef.setTextual(null);
				fieldDef.setDisplay(null);
				fieldDef.setMandatory(false);
			}
			else if (fieldName.equals(composPath)) {
				fieldDef.setAssocType(FieldDef.AST_Compos);
				fieldDef.setTextual(null);
				fieldDef.setDisplay(null);
				fieldDef.setMandatory(true);
				composBaseField = fieldDef;
			}
			else if (getFieldDefAnnotation(fieldDef, Embedded.class) != null || getFieldDefAnnotation(fieldDef, EmbeddedId.class) != null) {
				boolean isComposBase = composPath != null && composPath.startsWith(fieldName) && composPath.charAt(fieldName.length()) == '.';
				fieldDef.setEmbedded(openEmbedded(null, fieldDef.getType(), fieldDef.getPath()+".", isComposBase ? composPath.substring(fieldName.length()+1) : null));
				fieldDef.setDisplay(null);
				fieldDef.setMandatory(false);
				if (isComposBase)
					composBaseField = fieldDef;
			}
			else {
				fieldDef.setAssocType(assocDef != null ? assocDef.assocType() : FieldDef.AST_None);
				fieldDef.setTextual((Class<Textual<Object>>) (getFieldDefMandatoryAnnotation(fieldDef, TextualUse.class)).textual());
				fieldDef.setAlign((getFieldDefMandatoryAnnotation(fieldDef, DisplayUse.class)).align());
				fieldDef.setDisplay((getFieldDefMandatoryAnnotation(fieldDef, DisplayUse.class)).display());
				Mandatory mandatory = getFieldDefMandatoryAnnotation(fieldDef, Mandatory.class);
				fieldDef.setMandatory(mandatory.mandatory());
				fieldDef.setDoMandatory(mandatory.doMandatory().length() != 0 ? mandatory.doMandatory() : null);
				fieldDef.setReferenceDef(getFieldDefAnnotation(fieldDef, ReferenceDef.class));
			}
			fieldDef.setRoles(ObjectUtils.defaultIfNull(getFieldDefAnnotation(fieldDef, FieldRoles.class), entityRoles));
			fieldDefsHash.put(fieldName, fieldDef);
			fieldDefsList.add(fieldDef);
		}
		fieldDefs = fieldDefsList.toArray(fieldDefEmptyArray);

		// resolve primary key
		if (pkName != null) {
			if ((pkFieldDef = fieldDefsHash.get(pkName)) == null)
				throw new RuntimeException("cannot find pkField named "+pkName+" in class "+dataClass.getName());
		}

		// resolve compos reference, only for main class, not embedded one
		if (pkeyDef != null && composPath != null) {
			FieldDefImpl<?> composField = ((FieldDefImpl<?>)getPathField(composPath));
			composField.setAssocType(FieldDef.AST_Compos);
			composField.setAssocClass(composClass);
		}

		SuggestDef suggestDef;
		if ((suggestDef = dataClass.getAnnotation(SuggestDef.class)) != null)
			this.suggestFields = suggestDef.fields();
		else
			this.suggestFields = StringUtil.STRING_EMPTY_ARRAY;

		RefFieldsDef refFieldsDef;
		if ((refFieldsDef = dataClass.getAnnotation(RefFieldsDef.class)) != null)
			this.refFields = refFieldsDef.fields();
		else
			this.refFields = StringUtil.STRING_EMPTY_ARRAY;

		ActionDefs actionDefsAnno = this.dataClass.getAnnotation(ActionDefs.class);
		actionDefs = actionDefsAnno == null ? new ActionDef[0] : actionDefsAnno.actions();

		filterDefsHash = new LinkedHashMap<String, FilterDef>();
		FilterDefs filterDefs = dataClass.getAnnotation(FilterDefs.class);
		if (filterDefs != null) {
			for (FilterDef filterDef: filterDefs.filters())
				filterDefsHash.put(filterDef.name(), filterDef);
		}

		DisplayKeysDef displayKeysDef = dataClass.getAnnotation(DisplayKeysDef.class);
		if (displayKeysDef != null) {
			displayKeys = displayKeysDef.fields();
		}
		else if (pkeyDef != null) {
			if (!pkEmbedded) {
				displayKeys = new String[]{ pkeyDef.pkField() };
			}
			else {
				displayKeys = new String[pkeyDef.additionalPkFields().length];
				for (int i = 0; i < pkeyDef.additionalPkFields().length; i++) {
					displayKeys[i] = pkeyDef.pkField()+"."+pkeyDef.additionalPkFields()[i];
				}
			}
		}
		else {
			displayKeys = null;
		}
	}

	protected <A extends Annotation> A getFieldDefAnnotation(FieldDefImpl<?> fieldDef, Class<A> annotation)
	{
		A a;
		if (fieldDef.getField() != null && (a = fieldDef.getField().getAnnotation(annotation)) != null)
			return a;
		if (fieldDef.getGetter() != null && (a = fieldDef.getGetter().getAnnotation(annotation)) != null)
			return a;
		if (fieldDef.getSetter() != null && (a = fieldDef.getSetter().getAnnotation(annotation)) != null)
			return a;
		return null;
	}

	protected <A extends Annotation> A getFieldDefMandatoryAnnotation(FieldDefImpl<?> fieldDef, Class<A> annotation)
	{
		A a = getFieldDefAnnotation(fieldDef, annotation);
		if (a == null)
			throw new RuntimeException("mandatory annotation not found on field "+dataClass.getName()+"."+fieldDef.getName()+": "+annotation.getName());
		return a;
	}

	protected Class<ET>		dataClass;

	public Class<ET>		getDataClass()
	{
		return this.dataClass;
	}

	protected String		dataView;

	public String			getDataView()
	{
		return this.dataView;
	}

	protected boolean		embedded;

	public boolean			getEmbedded()
	{
		return this.embedded;
	}

	protected ViewInfo		viewInfo;

	public ViewInfo			getViewInfo()
	{
		return this.viewInfo;
	}

	protected Class<?>		pkClass;

	public Class<?>			getPkClass()
	{
		return this.pkClass;
	}

	protected String		pkName;

	public String			getPkName()
	{
		return this.pkName;
	}

	protected boolean		pkEmbedded;

	public boolean			isPkEmbedded()
	{
		return this.pkEmbedded;
	}

	protected Class<?>		composClass;

	public Class<?>			getComposClass()
	{
		return this.composClass;
	}

	protected Class<?>		composPkClass;

	public Class<?>			getComposPkClass()
	{
		return this.composPkClass;
	}

	protected String		composPath;

	public String			getComposPath()
	{
		return this.composPath;
	}

	protected FieldRoles		entityRoles;

	public FieldRoles		getEntityRoles()
	{
		return this.entityRoles;
	}

	protected FieldDef<?>[]		fieldDefs;

	protected Map<String, FieldDefImpl<?>> fieldDefsHash;

	protected PKeyDef		pkeyDef;

	protected ActionDef[]		actionDefs;

	protected Map<String, ActionDef> actionDefsHash;

	protected String[]		fieldOrder;

	public String[]			getFieldOrder()
	{
		return this.fieldOrder;
	}

	protected String[]		suggestFields;

	public String[]			getSuggestFields()
	{
		return this.suggestFields;
	}

	protected String[]		refFields;

	public String[]			getRefFields()
	{
		return this.refFields;
	}

	protected String[]		displayKeys;

	public String[]			getDisplayKeys()
	{
		return this.displayKeys;
	}

	protected Map<String, RelationDef> relations;

	public Map<String, RelationDef>	getRelations()
	{
		return this.relations;
	}

	protected FieldDefImpl<?>	pkFieldDef;

	protected FieldDefImpl<?>	composBaseField;

	protected String		basePath;

	protected Map<String, FilterDef> filterDefsHash;

	public Map<String, FilterDef>	getFilterDefsHash()
	{
		return this.filterDefsHash;
	}

	protected static final FieldDef<?>[] fieldDefEmptyArray = new FieldDef<?>[0];

	protected static Map<String, ClassMetaJava<?>> cachedData = new ConcurrentHashMap<String, ClassMetaJava<?>>();

	protected static final String[] assocTypesStrings = new String[]{ "none", "compos", "reference", "children" };
}
