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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Map utilities.
 */
public class CollectionUtil extends java.lang.Object
{
	@SuppressWarnings("unchecked")
	public static <K> Set<K>	createHashSet(K... params)
	{
		Set<K> set = new HashSet<K>();
		for (int i = 0; i < params.length; ) {
			set.add(params[i++]);
		}
		return set;
	}

	@SuppressWarnings("unchecked")
	public static <K> Set<K>	createLinkedHashSet(K... params)
	{
		Set<K> set = new LinkedHashSet<K>();
		for (int i = 0; i < params.length; ) {
			set.add(params[i++]);
		}
		return set;
	}

	/**
	 * Sorts the passed and return the original list.
	 *
	 * @param list
	 * 	items to be sorted
	 * @param comparator
	 * 	comparator to perform the sort
	 * @param <L>
	 *      list type
	 * @param <K>
	 *      element type
	 * @return
	 * 	the original sorted list
	 */
	public static <K> List<K>	sortList(List<K> list, Comparator<K> comparator)
	{
		Collections.sort(list, comparator);
		return list;
	}
}
