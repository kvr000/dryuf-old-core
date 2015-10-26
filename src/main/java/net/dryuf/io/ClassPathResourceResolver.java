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

package net.dryuf.io;

import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dryuf.core.AppContainer;
import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;


public class ClassPathResourceResolver extends AbstractResourceResolver
{
	Logger				logger = LogManager.getLogger(this.getClass());

	@Inject
	AppContainer			appContainer;

	@Override
	public int			checkFileType(String path)
	{
		return 1;
	}

	public FileData			getResource(String fullfilename)
	{
		try {
			//return applicationContext.getResource("classpath:"+fullfilename).getInputStream();
			URL url = getClass().getClassLoader().getResource(fullfilename);
			if (url == null)
				return null;
			return FileDataImpl.createFromUrl(url);
		}
		catch (Exception ex) {
			logger.error("Failed to open '"+fullfilename+"'", ex);
			//throw new RuntimeException("Failed to open '"+fullfilename+"'", ex);
			return null;
		}
	}

	@Override
	public Collection<String>	getResourcePaths(String path)
	{
		throw new UnsupportedOperationException();
	}
}
