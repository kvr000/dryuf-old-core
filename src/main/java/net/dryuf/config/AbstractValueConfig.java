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

package net.dryuf.config;

import net.dryuf.core.Textual;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AbstractValueConfig extends Object implements ValueConfig
{
	public AbstractValueConfig(IniConfig iniConfig, String section)
	{
		this.iniConfig = iniConfig;
		this.section = section;
	}

	public String			getValueMandatory(String name)
	{
		return iniConfig.getValueMandatory(section, name);
	}

	public String			getValueDefault(String name, String defaultValue)
	{
		return iniConfig.getValueDefault(section, name, defaultValue);
	}

	public <T> T			getTextualMandatory(String name, Textual<T> textual)
	{
		return iniConfig.getTextualMandatory(section, name, textual);
	}

	public <T> T			getTextualDefault(String name, Textual<T> textual, T defaultValue)
	{
		return iniConfig.getTextualDefault(section, name, textual, defaultValue);
	}

	@Override
	public Set<String>              keySet()
	{
		return iniConfig.listSectionKeys(section);
	}

	@Override
	public Map<String, String>	asMap()
	{
		Map<String, String> map = new HashMap<>();
		for (String key: keySet())
			map.put(key, getValueMandatory(key));
		return map;
	}

	@Override
	public boolean			equals(Object o)
	{
		if (!(o instanceof ValueConfig))
			return false;
		return keySet().equals(((ValueConfig)o).asMap());
	}

	@Override
	public int			hashCode()
	{
		return asMap().hashCode();
	}

	protected IniConfig		iniConfig;

	protected String		section;
}
