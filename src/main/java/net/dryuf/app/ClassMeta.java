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
	Object				convertField(CallerContext callerContext, FieldDef<?> fdef, String value);

	String				getDataClassName();

	ET				instantiate();

	boolean				canNew(CallerContext callerContext);

	boolean				canDel(CallerContext callerContext);

	boolean				hasCompos();

	boolean				isPkEmbedded();

	/**
	 * @return
	 * 	list of additional PK fields within
	 */
	String[]			getAdditionalPkFields();

	/**
	 * Gets list of field definitions.
	 */
	FieldDef<?>[]			getFields();

	/**
	 * Gets the field name of the key.
	 */
	String				getRefName();

	/**
	 * Gets object key from the existing object.
	 */
	Object				getEntityPkValue(ET entity);

	/**
	 * Sets object key.
	 */
	void				setEntityPkValue(ET entity, Object value);

	void				setComposKey(ET entity, Object composKey);

	Object				getComposKey(ET entity);

	ActionDef[]			getActions();

	FieldRoles			getFieldRoles(String name);

	FieldDef<?>			getField(String name);

	Object				getEntityFieldValue(ET entity, String fieldName);

	void				setEntityFieldValue(ET entity, String fieldName, Object value);

	FieldDef<?>			getPathField(String path);

	Object				getEntityPathValue(ET entity, String path);

	void				setEntityPathValue(ET entity, String path, Object value);

	ActionDef			getAction(String name);

	RelationDef			getRelation(String name);

	String				urlDisplayKey(CallerContext callerContext, ET entity);

	String				urlPkEntityKey(CallerContext callerContext, Object pk);

	List<ActionDef>			getGlobalActionList(CallerContext callerContext);

	List<ActionDef>			getObjectActionList(EntityHolder<ET> obj);

	Class<ET>			getDataClass();

	String				getDataView();

	boolean				getEmbedded();

	ViewInfo			getViewInfo();

	Class<?>			getPkClass();

	String				getPkName();

	Class<?>			getComposClass();

	Class<?>			getComposPkClass();

	String				getComposPath();

	FieldRoles			getEntityRoles();

	String[]			getFieldOrder();

	String[]			getSuggestFields();

	String[]			getRefFields();

	String[]			getDisplayKeys();

	Map<String, RelationDef>	getRelations();

	FieldDef<?>			getPkFieldDef();

	Map<String, FilterDef> 		getFilterDefsHash();

	String				formatAssocType(int assocType);
}
