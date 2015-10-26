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

package net.dryuf.service.file.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;

import net.dryuf.core.AppContainer;
import net.dryuf.core.AppContainerAware;
import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;
import net.dryuf.service.file.FileStoreService;
import net.dryuf.text.mime.MimeTypeService;


public class FsFileStoreService extends java.lang.Object implements FileStoreService, AppContainerAware
{
	@Override
	public void			afterAppContainer(@NotNull AppContainer appContainer)
	{
		if (root == null)
			throw new IllegalArgumentException("root not specified");
		if (!root.endsWith("/"))
			throw new IllegalArgumentException("root must end with '/'");
	}

	@Override
	public void			putFile(String path, String filename, FileData content)
	{
		try {
			FileUtils.copyInputStreamToFile(content.getInputStream(), new File(root+path+filename));
		}
		catch (Exception ex) {
			new File(root+path).mkdirs();
			try {
				FileUtils.copyInputStreamToFile(content.getInputStream(), new File(root+path+filename));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public FileData			getFile(String path, String filename)
	{
		final File file = new File(root+path+filename);
		if (!file.exists())
			return null;
		String contentType = mimeTypeService.guessContentTypeFromName(filename);
		if (contentType == null)
			contentType = "application/octet-stream";
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
		fileData.setName(filename);
		fileData.setSize(FileUtils.sizeOf(file));
		fileData.setModifiedTime(file.lastModified());
		return fileData;
	}

	@Override
	public void			removeFile(String path, String filename)
	{
		new File(root+path+filename).delete();
	}

	@Override
	public void			removePath(String path)
	{
		try {
			FileUtils.deleteDirectory(new File(root+path));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String		root;

	public void			setRoot(String root_)
	{
		this.root = root_;
	}

	@Inject
	MimeTypeService			mimeTypeService;
}
