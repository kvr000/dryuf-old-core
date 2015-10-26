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

package net.dryuf.srvui.mock;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.ClassMetaManager;
import net.dryuf.app.FieldDef;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Textual;
import net.dryuf.textual.TextualManager;
import net.dryuf.srvui.DummyRequest;


public class MockRequest extends DummyRequest
{
	public				MockRequest(CallerContext callerContext)
	{
		this.callerContext = callerContext;
	}

	public void			addFormObject(String prefix, Object form)
	{
		@SuppressWarnings("unchecked")
		ClassMeta<Object> meta = (ClassMeta<Object>) ClassMetaManager.openCached(null, form.getClass(), null);
		for (FieldDef<?> fieldDef: meta.getFields()) {
			Object value = meta.getEntityFieldValue(form, fieldDef.getName());
			@SuppressWarnings("unchecked")
			Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe(fieldDef.needTextual(), callerContext);
			addParam(prefix+fieldDef.getName(), value == null ? "" : textual.format(value, null));
		}
	}

	protected CallerContext		callerContext;
}
