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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import net.dryuf.io.FileData;


public abstract class AbstractResourceResolver extends java.lang.Object implements ResourceResolver
{
	@Override
	public abstract FileData	getResource(String path);

	@Override
	public InputStream		getResourceAsStream(String path)
	{
		FileData fileData = getResource(path);
		if (fileData == null)
			return null;
		return fileData.getInputStream();
	}

	@Override
	public byte[]			getResourceContent(String file)
	{
		InputStream stream = getResourceAsStream(file);
		if (stream == null)
			return null;
		try {
			try {
				return IOUtils.toByteArray(stream);
			}
			finally {
				stream.close();
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public FileData		getMandatoryResource(String path)
	{
		FileData fileData = getResource(path);
		if (fileData == null)
			throw new RuntimeException("Failed to find file within resources: "+path);
		return fileData;
	}

	@Override
	public InputStream		getMandatoryResourceAsStream(String path)
	{
		InputStream stream = getResourceAsStream(path);
		if (stream == null)
			throw new RuntimeException("Failed to find file within resources: "+path);
		return stream;
	}

	@Override
	public byte[]			getMandatoryResourceContent(String path)
	{
		byte[] data = getResourceContent(path);
		if (data == null)
			throw new RuntimeException("Failed to find file within resources: "+path);
		return data;
	}

	@Override
	public long			getCacheTimeout(String extension)
	{
		return cacheTimeout;
	}

	@Override
	public int			getCompressPolicy(String extension)
	{
		return COMPRESS_Unknown;
	}

	// one day timeout by default
	protected long			cacheTimeout = 86400000;
}
