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

import java.io.InputStream;
import java.util.Collection;

import net.dryuf.io.FileData;


public interface ResourceResolver
{
	public static final int		COMPRESS_Unknown		= -1;
	public static final int		COMPRESS_Not			= 0;
	public static final int		COMPRESS_Static			= 1;
	public static final int		COMPRESS_Dynamic		= 2;
	public static final int		COMPRESS_All			= 3;

	/**
	 * Checks file type for the specified path.
	 *
	 * @return -1
	 * 	if the path does not exist or is special device
	 * @return 0
	 * 	if the path is directory
	 * @return 1
	 * 	if the path is regular file
	 */
	public int			checkFileType(String path);

	/**
	 * Gets a resource as file data.
	 *
	 * @return null
	 * 	if the resource cannot be found
	 */
	public FileData			getResource(String path);

	/**
	 * Gets a resource as stream.
	 *
	 * @return null
	 * 	if the resource cannot be found
	 */
	public InputStream		getResourceAsStream(String path);

	/**
	 * Gets a resource as byte sequence.
	 *
	 * @return null
	 * 	if the resource cannot be found
	 */
	public byte[]			getResourceContent(String path);

	/**
	 * Gets list of entries in resource directory.
	 *
	 * @return
	 * 	list of directory entries
	 */
	public Collection<String>	getResourcePaths(String path);

	/**
	 * Gets a resource.
	 *
	 * @return
	 * 	resource data
	 *
	 * @throws IllegalArgumentException
	 * 	if the path is not found
	 */
	public FileData			getMandatoryResource(String path);

	/**
	 * Gets a resource as stream.
	 *
	 * @return
	 * 	resource stream
	 *
	 * @throws IllegalArgumentException
	 * 	if the path is not found
	 */
	public InputStream		getMandatoryResourceAsStream(String path);

	/**
	 * Gets content of resource.
	 *
	 * @return
	 * 	resource content
	 *
	 * @throws IllegalArgumentException
	 * 	if the path is not found
	 */
	public byte[]			getMandatoryResourceContent(String path);

	/**
	 * Gets cache timeout for specific kind of resource.
	 *
	 * @param resourceExtension
	 * 	file extension of resource name
	 *
	 * @return cache timeout
	 * 	in case timeout applies
	 * @return -1
	 * 	in case caching not suitable
	 */
	public long			getCacheTimeout(String resourceExtension);

	/**
	 * Gets compress policy for specific kind of resource.
	 *
	 * @param resourceExtension
	 * 	file extension of resource name
	 *
	 * @return COMPRESS_Unknown
	 * 	no special policy
	 * @return COMPRESS_Not
	 * 	if not compressable
	 * @return COMPRESS_Static
	 * 	compress static resources
	 * @return COMPRESS_Dynamic
	 * 	compress dynamic resources
	 * @return COMPRESS_All
	 * 	compress all resources
	 */
	public int			getCompressPolicy(String resourceExtension);
}
