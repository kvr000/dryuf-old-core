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

package net.dryuf.srvui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class DummyUiContext extends net.dryuf.core.AbstractUiContext
{
	public				DummyUiContext()
	{
		super();
	}

	@Override
	public String			localize(String class_name, String text)
	{
		return text;
	}

	@Override
	public String			getDefaultLanguage()
	{
		return language;
	}

	@Override
	public String			getLocalizePath()
	{
		return null;
	}

	@Override
	public String			readLocalizedFile(String filename)
	{
		throw new UnsupportedOperationException();
	}

	public String			getLanguage()
	{
		return this.language;
	}

	public void			setLanguage(String language_)
	{
		this.language = language_;
	}

	public boolean			checkLanguage(String language_)
	{
		if (language_.equals(this.language))
			return true;
		return false;
	}

	protected String		language;

	@Override
	public Map<String, String> getClassLocalization(String className)
	{
		return new HashMap<String, String>();
	}
}
