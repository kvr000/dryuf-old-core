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

package net.dryuf.core;

import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation of {@link EntityView}.
 */
public class AbstractEntityView extends java.lang.Object implements net.dryuf.core.EntityView
{
	private static final long serialVersionUID = 1L;

	public void			addDynamic(String key, Object value)
	{
		if (dynamic == null)
			dynamic = new HashMap<String, Object>();
		this.dynamic.put(key, value);
	}

	public Object			getDynamic(String key)
	{
		return dynamic == null ? null : this.dynamic.get(key);
	}

	public Object			getDynamicDefault(String key, Object defaultValue)
	{
		return dynamic != null && this.dynamic.containsKey(key) ? this.dynamic.get(key) : defaultValue;
	}

	protected Map<String, Object>	dynamic;
};


