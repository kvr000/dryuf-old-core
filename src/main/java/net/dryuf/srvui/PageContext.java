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

import net.dryuf.core.CallerContext;
import net.dryuf.core.StringUtil;
import net.dryuf.trans.meta.NoDynamic;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;


public interface PageContext extends AutoCloseable
{
	public void			setServerError(int code, String message);

	public CallerContext		getCallerContext();

	public Request			getRequest();

	public Response			getResponse();

	public Session			getSession();

	public Session			forceSession();

	public void			invalidateSession();

	public void			output(byte[] content);

	@NoDynamic
	public void			output(String text);

	public String			getLanguage();

	public String			getContextPath();

	/**
	 * Gets current path after partial parsing.
	 *
	 * @return
	 * 	current path
	 */
	public String			getCurrentPath();

	/**
	 * Gets currently processing path.
	 *
	 * @return
	 * 	processing path
	 */
	public String			getProcessingPath();

	/**
	 * Gets remaining path from parsed URL.
	 *
	 * @return
	 * 	remaining path
	 */
	public String			getRemainPath();

	/**
	 * Gets next path element, excluding the optional slash.
	 *
	 * @return next path element
	 */
	public String			getPathElement();

	/**
	 * Gets next path element, excluding the optional slash.
	 * Checks for safety of the element, i.e. it must not contain special filesystem characters, like /.
	 *
	 * @return next path element
	 */
	public String			getPathElementSafe();

	/**
	 * Puts back the last element so it can be processed again.
	 */
	public void			putBackLastElement();

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	public String			getLastElement();

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	public String			getLastElementWithoutSlash();

	/**
	 * Get reverse path to current path part.
	 *
	 * @return
	 * 	reverse path to current path part.
	 */
	public String			getReversePath();

	/**
	 * Checks that there is no more item in the path.
	 *
	 * @return true
	 * 	if there is no more item
	 * @return false
	 * 	if there is an item remaining
	 */
	public boolean			needPathFinal();

	/**
	 * Checks if the current path ends with slash, depending on needSlash parameter. Either redirects to slashed
	 * path or creates not found presenter if the condition is not satisfied.
	 *
	 * @return current element
	 * 	if the condition was satisfied
	 * @return null
	 * 	otherwise, in that case process() function should return !needSlash
	 */
	public boolean			needPathSlash(boolean needSlash);

	public String			getRealPath();

	public void			setRealPath(String realPath);

	public String			getFullUrl();

	public boolean			redirect(@NotNull String url);

	public void			redirectImm(String url);

	/**
	 * Gets redirected URL.
	 *
	 * @return null
	 * 	if no redirect was requested
	 * @return
	 * 	redirect string if redirect was requested
	 */
	public String			getRedirected();

	public String			localize(String className, String text);

	public String			localizeArgs(String className, String text, Object[] args);

	public void			addMeta(MetaTag metaTag);

	public void			addMetaName(String name, String content);

	public void			addMetaHttp(String name, String content);

	public Map<String, Map<String, MetaTag>> getMetas();

	public void			addLinkedFile(String type, PageUrl url);

	public void			addLinkedContent(String type, String identity, String content);

	public Collection<PageUrl>	getLinkedFiles(String type);

	public void			addMessage(int msgType, String msg);

	public void			addMessageLocalized(int msgType, String classname, String msg);

	public void			close();

	public void			setActiveField(int priority, String activeField_);

	public List<PendingMessage>	getPendingMessages();
}
