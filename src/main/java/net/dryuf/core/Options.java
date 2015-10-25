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


/**
 * Container holding the dynamic construction options.
 */
public class Options extends java.lang.Object
{
	public static final Options	NONE = new Options();

	protected			Options()
	{
		options = new HashMap<String, Object>();
	}

	public static Options		buildListed(Object... pairs)
	{
		Options this_ = new Options();
		this_.addListed(pairs);
		return this_;
	}

	public Object			getOptionMandatory(String name)
	{
		Object value;
		if ((value = options.get(name)) != null)
			return value;
		throw new NullPointerException("option "+name+" is undefined");
	}

	public <T> T			getOptionDefault(String name, T defaultValue)
	{
		@SuppressWarnings("unchecked")
		T value = (T) options.get(name);
		if (value != null)
			return value;
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public Options			cloneAddingListed(Object... pairs)
	{
		Options copy = new Options();
		copy.options = (HashMap<String, Object>) this.options.clone();
		copy.addListed(pairs);
		return copy;
	}

	protected void			addListed(Object... pairs)
	{
		for (int i = 0; i < pairs.length; ) {
			String name = (String)pairs[i++];
			Object value = pairs[i++];
			this.options.put(name, value);
		}
	}

	public int			hashCode()
	{
		return options.hashCode();
	}

	public boolean			equals(Object o)
	{
		if (!(o instanceof Options))
			return false;
		Options s = (Options)o;
		return s.options.size() == 0 && this.options.size() == 0 || s.options.equals(this.options);
	}

	protected HashMap<String, Object> options;
}
