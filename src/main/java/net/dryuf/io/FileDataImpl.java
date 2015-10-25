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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class FileDataImpl extends java.lang.Object implements FileData
{
	public static FileDataImpl	createFromFile(File file)
	{
		FileDataImpl fileData = new FileDataImpl();
		fileData.name = file.getName();
		fileData.size = file.length();
		fileData.modifiedTime = file.lastModified();
		try {
			fileData.inputStream = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return fileData;
	}

	public static FileDataImpl	createFromFilename(String filename)
	{
		return createFromFile(new File(filename));
	}

	public static FileDataImpl	createFromNameStream(String name, InputStream stream)
	{
		FileDataImpl fileData = new FileDataImpl();
		fileData.name = name;
		fileData.inputStream = stream;
		return fileData;
	}

	public static FileDataImpl	createFromStream(InputStream stream)
	{
		FileDataImpl fileData = new FileDataImpl();
		fileData.inputStream = stream;
		return fileData;
	}

	public static FileDataImpl	createFromNameBytes(String name, byte[] bytes)
	{
		FileDataImpl fileData = new FileDataImpl();
		fileData.name = name;
		fileData.inputStream = new ByteArrayInputStream(bytes);
		return fileData;
	}

	public static FileDataImpl	createFromBytes(byte[] bytes)
	{
		FileDataImpl fileData = new FileDataImpl();
		fileData.inputStream = new ByteArrayInputStream(bytes);
		return fileData;
	}

	public static FileDataImpl	createFromUrl(URL url)
	{
		FileDataImpl fileData = new FileDataImpl();
		try {
			URLConnection connection = url.openConnection();
			fileData.name = url.getFile();
			fileData.size = connection.getContentLength();
			fileData.modifiedTime = connection.getLastModified();
			fileData.inputStream = connection.getInputStream();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileData;
	}

	public void			close()
	{
		try {
			if (inputStream != null)
				inputStream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String		name;

	public String			getName()
	{
		return this.name;
	}

	public void			setName(String name_)
	{
		this.name = name_;
	}

	protected long			size = -1;

	public long			getSize()
	{
		return this.size;
	}

	public void			setSize(long size_)
	{
		this.size = size_;
	}

	protected long			modifiedTime = -1;

	public long			getModifiedTime()
	{
		return this.modifiedTime;
	}

	public void			setModifiedTime(long modifiedTime_)
	{
		this.modifiedTime = modifiedTime_;
	}

	protected String		contentType;

	public String			getContentType()
	{
		return this.contentType;
	}

	public void			setContentType(String contentType_)
	{
		this.contentType = contentType_;
	}

	protected InputStream		inputStream;

	public InputStream		getInputStream()
	{
		return this.inputStream;
	}

	public void			setInputStream(InputStream inputStream_)
	{
		this.inputStream = inputStream_;
	}
}
