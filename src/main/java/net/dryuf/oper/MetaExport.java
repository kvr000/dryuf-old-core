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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.ClassMetaManager;
import net.dryuf.app.FieldDef;
import net.dryuf.core.CallerContext;
import net.dryuf.meta.ActionDef;
import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.RelationDef;
import net.dryuf.meta.ViewInfo;
import net.dryuf.xml.util.XmlFormat;
import net.dryuf.core.StringUtil;


public class MetaExport extends java.lang.Object
{
	public static String		formatClassName(Class<?> clazz)
	{
		return clazz.getName().replaceAll("\\$", ".");
	}

	public static void		formatField(StringBuilder out, CallerContext callerContext, ClassMeta<?> classMeta, FieldDef<?> fieldDef)
	{
		try {
			FieldRoles fieldRoles = fieldDef.getRoles();
			Class<?> assocClass = fieldDef.getAssocClass();
			if (fieldRoles == null)
				fieldRoles = classMeta.getEntityRoles();
			XmlFormat.formatSb(out, callerContext, "<field name=%A",
					fieldDef.getName()
					);
			XmlFormat.formatSb(out, callerContext, " assocType=%A roleGet=%A roleSet=%A roleNew=%A",
					assocClass != null ? classMeta.formatAssocType(fieldDef.getAssocType()) : classMeta.formatAssocType(fieldDef.getAssocType()),
					fieldRoles.roleGet(),  fieldRoles.roleSet(), fieldRoles.roleNew());
			if (fieldDef.getAssocClass() != null)
				XmlFormat.appendAttributeSb(out, "ref", formatClassName(fieldDef.getAssocClass()));
			if (fieldDef.getAssocType() == FieldDef.AST_Compos) {
				XmlFormat.formatSb(out, callerContext, ">");
			}
			else if (fieldDef.getEmbedded() != null) {
				XmlFormat.appendAttributeSb(out, "embedded", formatClassName(fieldDef.getEmbedded().getDataClass()));
				XmlFormat.formatSb(out, callerContext, ">\n");
				formatFields(out, callerContext, fieldDef.getEmbedded());
			}
			else {
				XmlFormat.appendAttributeSb(out, "mandatory", fieldDef.getMandatory() ? "1" : "0");
				if (fieldDef.getDoMandatory() != null)
					XmlFormat.appendAttributeSb(out, "doMandatory", fieldDef.getDoMandatory().toString());
				if (fieldDef.getAssocClass() != null)
					XmlFormat.appendAttributeSb(out, "assoc", formatClassName(fieldDef.getAssocClass()));
				if (fieldDef.getDisplay() != null)
					XmlFormat.appendAttributeSb(out, "display", fieldDef.getDisplay());
				if (fieldDef.getTextual() != null)
					XmlFormat.appendAttributeSb(out, "textual", formatClassName(fieldDef.getTextual()));
				XmlFormat.formatSb(out, callerContext, ">");
			}
			XmlFormat.formatSb(out, callerContext, "</field>\n");
		}
		catch (Exception ex) {
			throw new RuntimeException("Failed to format "+fieldDef.getName()+": "+ex.toString(), ex);
		}
	}

	public static void		formatFields(StringBuilder out, CallerContext callerContext, ClassMeta<?> classMeta)
	{
		XmlFormat.formatSb(out, callerContext, "<fields name=%A>\n", formatClassName(classMeta.getDataClass()));

		for (String fieldName: classMeta.getFieldOrder()) {
			FieldDef<?> field = classMeta.getField(fieldName);
			formatField(out, callerContext, classMeta, field);
		}

		out.append("</fields>\n");
	}

	public static String		buildMeta(CallerContext callerContext, Class<?> clazz, String viewName, String rpcPath)
	{
		if (viewName == null)
			viewName = "Default";
		ClassMeta<?> classMeta = ClassMetaManager.openCached(callerContext.getAppContainer(), clazz, viewName);
		FieldRoles classRoles = classMeta.getEntityRoles();
		try {
			StringBuilder out = new StringBuilder("<?xml version='1.0' encoding='UTF-8' ?>\n");
			XmlFormat.formatSb(out, callerContext, "<meta xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://dryuf.org/schema/net/dryuf/app/meta/\" xsi:schemaLocation=\"http://dryuf.org/schema/net/dryuf/app/meta/ http://www.znj.cz/schema/net/dryuf/app/meta.xsd\" name=%A rpc=%A >\n", formatClassName(clazz), rpcPath);

			Map<String, RelationDef> relations = new HashMap<String, RelationDef>();

			{
				XmlFormat.formatSb(out, callerContext, "<req roleGet=%A roleSet=%A roleNew=%A roleDel=%A />\n",
						classRoles.roleGet(), classRoles.roleSet(), classRoles.roleNew(), classRoles.roleDel());
			}

			if (classMeta.getPkName() != null) {
				XmlFormat.formatSb(out, callerContext, "<pkeyDef pkEmbedded=%A pkClass=%A pkField=%A additionalPkFields=%A",
						classMeta.isPkEmbedded() ? "1" : "0",
						formatClassName(classMeta.getPkClass()),
						classMeta.getPkName(),
						StringUtil.joinArray(",", classMeta.getAdditionalPkFields()));
				if (classMeta.hasCompos())
					XmlFormat.formatSb(out, callerContext, " composClass=%A composPkClass=%A composPath=%A",
							formatClassName(classMeta.getComposClass()),
							formatClassName(classMeta.getComposPkClass()),
							classMeta.getComposPath());
				out.append(">\n</pkeyDef>\n");
			}

			String[] fieldOrder = classMeta.getFieldOrder();
			{
				String[] refFields = classMeta.getRefFields();
				if (refFields.length == 0)
					refFields = fieldOrder;
				XmlFormat.formatSb(out, callerContext, "<refFields fields=%A />\n", StringUtil.joinArray(",", refFields));
			}

			{
				out.append("<relations>\n");
				for (RelationDef relationDef: classMeta.getRelations().values()) {
					XmlFormat.formatSb(out, callerContext, "<relation name=%A targetClass=%A/>\n", relationDef.name(), relationDef.targetClass());
					relations.put(relationDef.name(), relationDef);
				}
				out.append("</relations>\n");
			}

			formatFields(out, callerContext, classMeta);

			ActionDef[] actionDefs = classMeta.getActions();
			{
				out.append("<actions>\n");
				for (ActionDef actionDef: actionDefs) {
					XmlFormat.formatSb(out, callerContext, "<action name=%A isStatic=%A guiDef=%A formName=%A formActioner=%A reqMode=%A roleAction=%A/>\n", actionDef.name(), (actionDef.isStatic() ? "1" : "0"), actionDef.guiDef(), actionDef.formName(), actionDef.formActioner(), actionDef.reqMode(), actionDef.roleAction());
				}
				out.append("</actions>\n");
			}

			{
				ViewInfo vi = classMeta.getViewInfo();
				out.append("<view");
				XmlFormat.formatSb(out, callerContext, " name=%A", vi.name());
				XmlFormat.formatSb(out, callerContext, " supplier=%A", vi.supplier());
				XmlFormat.formatSb(out, callerContext, " renderer=%A", vi.renderer());
				XmlFormat.formatSb(out, callerContext, " clientClass=%A", vi.clientClass());
				XmlFormat.formatSb(out, callerContext, " fields=\"");
				if (vi.fields().length == 1 && vi.fields()[0].equals("")) {
					boolean needComma = false;
					if (classMeta.isPkEmbedded() && classMeta.getAdditionalPkFields().length > 0) {
						XmlFormat.formatSb(out, callerContext, "%S", StringUtil.joinArray(",", classMeta.getAdditionalPkFields()));
						needComma = true;
					}
					if (fieldOrder.length > 0) {
						if (needComma)
							XmlFormat.formatSb(out, callerContext, ",");
					}
					XmlFormat.formatSb(out, callerContext, "%S", StringUtil.joinArray(",", fieldOrder));
				}
				else {
					XmlFormat.formatSb(out, callerContext, "%S", StringUtil.joinArray(",", vi.fields()));
				}
				XmlFormat.formatSb(out, callerContext, "\"");
				XmlFormat.formatSb(out, callerContext, " actions=\"");
				if (vi.actions().length == 1 && vi.actions()[0].equals("")) {
					List<String> list = new LinkedList<String>();
					for (ActionDef actionDef: actionDefs) {
						list.add(actionDef.name());
					}
					XmlFormat.formatSb(out, callerContext, "%S", StringUtil.joinArray(",", list.toArray(StringUtil.STRING_EMPTY_ARRAY)));
				}
				else {
					XmlFormat.formatSb(out, callerContext, "%S", StringUtil.joinArray(",", vi.actions()));
				}
				XmlFormat.formatSb(out, callerContext, "\"");
				out.append("/>\n");
			}

			out.append("</meta>\n");
			return out.toString();
		}
		catch (Exception ex) {
			throw new RuntimeException("build meta failed for "+clazz.getName()+": "+ex.getMessage(), ex);
		}
	}
}
