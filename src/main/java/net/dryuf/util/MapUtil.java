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

package net.dryuf.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Map utilities.
 */
public class MapUtil extends java.lang.Object
{
	public static <T, V> V		getMapMandatory(Map<T, V> map, T key)
	{
		V v;
		if ((v = map.get(key)) == null) {
			if (!map.containsKey(key))
				throw new IllegalArgumentException(key.toString()+" not found");
		}
		return v;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V>	createHashMap(K k0, V v0, Object... params)
	{
		Map<K, V> map = new HashMap<K, V>();
		map.put(k0, v0);
		for (int i = 0; i < params.length; ) {
			k0 = (K)params[i++];
			v0 = (V)params[i++];
			map.put(k0, v0);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(K k0, V v0, Object... params)
	{
		LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
		map.put(k0, v0);
		for (int i = 0; i < params.length; ) {
			k0 = (K)params[i++];
			v0 = (V)params[i++];
			map.put(k0, v0);
		}
		return map;
	}

	public static final Function<?, ?> notFoundFunction = (Object key) -> { throw new IllegalArgumentException(key.toString()+" not found"); };
}
