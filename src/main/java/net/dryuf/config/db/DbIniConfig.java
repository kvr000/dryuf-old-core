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

package net.dryuf.config.db;

import javax.inject.Inject;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.dryuf.config.AbstractValueConfig;
import net.dryuf.config.DbConfigEntry;
import net.dryuf.config.DbConfigSection;
import net.dryuf.config.IniConfig;
import net.dryuf.config.ValueConfig;
import net.dryuf.config.dao.DbConfigEntryDao;
import net.dryuf.core.Textual;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


public class DbIniConfig extends java.lang.Object implements IniConfig
{
	public String			getValueMandatory(String section, String name)
	{
		String value = this.getValueDefault(section, name, null);
		if (value == null)
			throw new net.dryuf.core.ReportException("key "+section+"/"+name+" not found");
		return value;
	}

	public String			getValueDefault(String section, String name, String defaultValue)
	{
		DbConfigEntry value = dbConfigEntryDao.loadByPk(new DbConfigEntry.Pk(new DbConfigSection.Pk(profileName, section), name));
		return value == null ? defaultValue : value.getConfigValue();
	}

	@Override
	public <T> T			getTextualMandatory(String section, String name, Textual<T> textual)
	{
		String value = this.getValueMandatory(section, name);
		return textual.convert(value, null);
	}

	@Override
	public <T> T			getTextualDefault(String section, String name, Textual<T> textual, T defaultValue)
	{
		String value = this.getValueDefault(section, name, null);
		if (value == null)
			return defaultValue;
		return textual.convert(value, null);
	}

	@Override
	public ValueConfig		getSection(String section)
	{
		return new AbstractValueConfig(this, section);
	}

	public Set<String>              listSectionKeys(String sectionName)
	{
		return Sets.newHashSet(Iterables.transform(dbConfigEntryDao.listByCompos(new DbConfigSection.Pk(profileName, sectionName)), (DbConfigEntry entry) -> { return entry.getConfigKey(); }));
	}

	protected DbConfigEntryDao	dbConfigEntryDao;

	@Inject
	public void			setDbConfigEntryDao(DbConfigEntryDao dbConfigEntryDao_)
	{
		this.dbConfigEntryDao = dbConfigEntryDao_;
	}

	protected String		profileName;

	@Inject
	public void			setProfileName(String profileName_)
	{
		this.profileName = profileName_;
	}
}
