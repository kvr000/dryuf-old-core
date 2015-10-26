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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;
import net.dryuf.text.mime.MimeTypeService;


public class FileSystemResourceResolver extends AbstractResourceResolver
{
	public				FileSystemResourceResolver()
	{
	}

	public void			setPaths(String[] paths)
	{
		int i = 0;
		this.paths = new PathDef[paths.length];
		for (String pathDef: paths) {
			String[] pathSplit = pathDef.split("=", 2);
			if (pathSplit.length != 2 || (!pathSplit[0].endsWith("/") && pathSplit[0].length() != 0) || !pathSplit[1].endsWith("/"))
				throw new IllegalArgumentException("Invalid path definition, must contain 'prefix/=filesystem-path/': "+pathDef);
			this.paths[i++] = new PathDef(pathSplit[0], pathSplit[1]);
		}
	}

	@Override
	public int			checkFileType(String path)
	{
		for (PathDef pathDef: paths) {
			if (!path.startsWith(pathDef.prefix))
				continue;
			final File file = new File(pathDef.path, path.substring(pathDef.prefix.length()));
			if (!file.exists())
				continue;
			if (file.isFile())
				return 1;
			if (file.isDirectory())
				return 0;
			return -1;
		}
		return -1;
	}

	public FileData			getResource(String path)
	{
		for (PathDef pathDef: paths) {
			if (!path.startsWith(pathDef.prefix))
				continue;
			final File file = new File(pathDef.path, path.substring(pathDef.prefix.length()));
			if (file.exists()) {
				String contentType = StringUtils.defaultString(mimeTypeService.guessContentTypeFromName(path), "application/octet-stream");

				FileDataImpl fileData = new FileDataImpl() {
					@Override
					public InputStream		getInputStream()
					{
						try {
							return FileUtils.openInputStream(file);
						}
						catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				};

				fileData.setContentType(contentType);
				fileData.setName(path);
				fileData.setSize(FileUtils.sizeOf(file));
				fileData.setModifiedTime(file.lastModified());
				return fileData;
			}
		}
		return null;
	}

	@Override
	public Collection<String>	getResourcePaths(String path)
	{
		LinkedHashSet<String> result = new LinkedHashSet<String>();
		for (PathDef pathDef: paths) {
			if (!path.startsWith(pathDef.prefix))
				continue;
			final File file = new File(pathDef.path, path.substring(pathDef.prefix.length()));
			if (!file.isDirectory())
				continue;
			for (String name: file.list()) {
				if (new File(file, name).isDirectory())
					name += "/";
				result.add(name);
			}
		}
		return result;
	}

	protected static class PathDef
	{
		public				PathDef(String prefix, String path)
		{
			this.prefix = prefix;
			this.path = path;
		}

		public String			prefix;
		public String			path;
	}

	protected PathDef[]		paths;

	@Inject
	protected MimeTypeService	mimeTypeService;
}
