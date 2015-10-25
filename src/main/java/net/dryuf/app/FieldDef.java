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

import net.dryuf.app.ClassMeta;
import net.dryuf.core.Textual;
import net.dryuf.textual.DisplayUse;
import net.dryuf.textual.TextualUse;
import net.dryuf.meta.AssocDef;
import net.dryuf.meta.FieldRoles;
import net.dryuf.meta.Mandatory;
import net.dryuf.meta.ReferenceDef;


public interface FieldDef<FT>
{
	public static final int		AST_None			= 0;
	public static final int		AST_Compos			= 1;
	public static final int		AST_Reference			= 2;
	public static final int		AST_Children			= 3;

	String				getName();
	String				getPath();
	Class<FT>			getType();
	int				getAssocType();
	ClassMeta<FT>			getEmbedded();

	Class<?>			getAssocClass();

	boolean				getMandatory();
	FT				getDoMandatory();

	String				getDisplay();
	int				getAlign();
	FieldRoles			getRoles();

	ReferenceDef			getReferenceDef();

	Class<Textual<FT>>		getTextual();
	Class<Textual<FT>>		needTextual();

	FT				getValue(Object o);
	void				setValue(Object o, FT value);
}
