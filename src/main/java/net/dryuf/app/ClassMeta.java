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

import java.util.List;
import java.util.Map;

import net.dryuf.core.CallerContext;
import net.dryuf.core.EntityHolder;
import net.dryuf.meta.ActionDef;
import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.FilterDef;
import net.dryuf.meta.RelationDef;
import net.dryuf.meta.ViewInfo;


public interface ClassMeta<ET>
{
	public Object			convertField(CallerContext callerContext, FieldDef<?> fdef, String value);

	public String			getDataClassName();

	public ET			instantiate();

	public boolean			canNew(CallerContext callerContext);

	public boolean			canDel(CallerContext callerContext);

	public boolean			hasCompos();

	public boolean			isPkEmbedded();

	/**
	 * @return
	 * 	list of additional PK fields within
	 */
	public String[]			getAdditionalPkFields();

	/**
	 * Gets list of field definitions.
	 */
	public FieldDef<?>[]		getFields();

	/**
	 * Gets the field name of the key.
	 */
	public String			getRefName();

	/**
	 * Gets object key from the existing object.
	 */
	public Object			getEntityPkValue(ET entity);

	/**
	 * Sets object key.
	 */
	public void			setEntityPkValue(ET entity, Object value);

	public void			setComposKey(ET entity, Object composKey);

	public Object			getComposKey(ET entity);

	public ActionDef[]		getActions();

	public FieldRoles		getFieldRoles(String name);

	public FieldDef<?>		getField(String name);

	public Object			getEntityFieldValue(ET entity, String fieldName);

	public void			setEntityFieldValue(ET entity, String fieldName, Object value);

	public FieldDef<?>		getPathField(String path);

	public Object			getEntityPathValue(ET entity, String path);

	public void			setEntityPathValue(ET entity, String path, Object value);

	public ActionDef		getAction(String name);

	public RelationDef		getRelation(String name);

	public String			urlDisplayKey(CallerContext callerContext, ET entity);

	public String			urlPkEntityKey(CallerContext callerContext, Object pk);

	public List<ActionDef>		getGlobalActionList(CallerContext callerContext);

	public List<ActionDef>		getObjectActionList(EntityHolder<ET> obj);

	public Class<ET>		getDataClass();

	public String			getDataView();

	public boolean			getEmbedded();

	public ViewInfo			getViewInfo();

	public Class<?>			getPkClass();

	public String			getPkName();

	public Class<?>			getComposClass();

	public Class<?>			getComposPkClass();

	public String			getComposPath();

	public FieldRoles		getEntityRoles();

	public String			getDbSource();

	public String			getDbTable();

	public String[]			getFieldOrder();

	public String[]			getSuggestFields();

	public String[]			getRefFields();

	public String[]			getDisplayKeys();

	public Map<String, RelationDef> getRelations();

	public FieldDef<?>		getPkFieldDef();

	public Map<String, FilterDef> 	getFilterDefsHash();

	public String			formatAssocType(int assocType);
}
