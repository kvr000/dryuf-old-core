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

package net.dryuf.srvui.spring;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.web.context.ServletContextAware;

import net.dryuf.io.AbstractResourceResolver;
import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;
import net.dryuf.text.mime.MimeTypeService;


public class WebAppResourceResolver extends AbstractResourceResolver implements ServletContextAware
{
	public				WebAppResourceResolver()
	{
	}

	@Inject
	public void			setServletContext(ServletContext servletContext)
	{
		this.servletContext = servletContext;
		appRoot = servletContext.getRealPath("/");
		if (appRoot == null)
			throw new RuntimeException("servletContext.getRealPath() returns null");
		if (!appRoot.endsWith("/"))
			appRoot += "/";
	}

	public int			checkFileType(String path)
	{
		File file = new File(appRoot+path);
		if (file.isFile())
			return 1;
		if (file.isDirectory())
			return 0;
		return -1;
	}

	public FileData			getResource(String path)
	{
		String fileName = appRoot+path;
		final File file = new File(fileName);
		if (!file.exists())
			return null;

		String contentType = mimeTypeService.guessContentTypeFromName(fileName);
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
		fileData.setName(fileName);
		fileData.setSize(FileUtils.sizeOf(file));
		fileData.setModifiedTime(file.lastModified());
		return fileData;
	}

	@Override
	public Collection<String>	getResourcePaths(String path)
	{
		if (!path.endsWith("/"))
			path += "/";
		LinkedHashSet<String> list = new LinkedHashSet<>();
		for (String name: servletContext.getResourcePaths("/"+path)) {
			list.add(name.substring(1+path.length()));
		}
		return list;
	}

	protected ServletContext	servletContext;

	protected String		appRoot;

	@Inject
	protected MimeTypeService	mimeTypeService;
}
