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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Options;
import net.dryuf.core.ReportException;
import net.dryuf.core.UiContext;
import net.dryuf.trans.meta.NoDynamic;
import net.dryuf.web.jee.JeeWebRequest;
import java.nio.charset.StandardCharsets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;


public class DefaultPageContext extends java.lang.Object implements PageContext
{
	public				DefaultPageContext(CallerContext callerContext_, Request request_)
	{
		callerContext = callerContext_;
		uiContext = callerContext.getUiContext();
		request = request_;
		response = request.getResponse();
		started = System.currentTimeMillis();
		remainPath = request.getPath();
		while (remainPath.startsWith("/"))
			remainPath = remainPath.substring(1);
		if (remainPath.equals(""))
			remainPath = null;
	}

	@Override
	public void			setServerError(int code, String message)
	{
		this.serverError = code;
		this.serverMessage = message;
	}

	@Override
	public Session			getSession()
	{
		if (session == null)
			session = getRequest().getSession();
		return session;
	}

	@Override
	public Session			forceSession()
	{
		return getRequest().forceSession();
	}

	@Override
	public void			invalidateSession()
	{
		getRequest().invalidateSession();
		session = null;
	}

	@Override
	public void			output(byte[] content)
	{
		try {
			response.getOutputStream().write(content);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@NoDynamic
	public void			output(String text)
	{
		try {
			response.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String			getLanguage()
	{
		return uiContext.getLanguage();
	}

	@Override
	public String			getContextPath()
	{
		return "";
	}

	@Override
	public String			getProcessingPath()
	{
		return processingPath;
	}

	@Override
	public String			getPathElement()
	{
		if (remainPath == null)
			return null;
		Matcher pathMatch = pathMatcher.matcher(remainPath);
		if (pathMatch.matches()) {
			String parsed = pathMatch.group(1);
			try {
				processingPath = URLDecoder.decode(pathMatch.group(1), "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			if (parsed.equals(".") || parsed.equals(".."))
				throw new ReportException("wrong path element: "+parsed);
			this.currentPath += parsed;
			this.realPath += parsed;
			if ((remainPath = pathMatch.group(3)) == null) {
				// ok, we are at the end
			}
			else {
				currentPath += "/";
				realPath += "/";
				if (remainPath.equals(""))
					remainPath = null;
			}
		}
		else {
			throw new ReportException("unexpected state, remainPath not null and doesn't match: "+remainPath);
		}
		return processingPath;
	}

	/**
	 * Gets next path element, excluding the optional slash.
	 * Checks for safety of the element, i.e. it must not contain special filesystem characters, like /.
	 *
	 * @return next path element
	 */
	@Override
	public String			getPathElementSafe()
	{
		String element;
		if ((element = getPathElement()) == null)
			return null;
		if (element.indexOf('/') >= 0)
			throw new IllegalArgumentException("/ not allowed as part of path");
		if (element.equals(".."))
			throw new IllegalArgumentException(".. not allowed as part of path");
		return element;
	}

	/**
	 * Puts back the last element so it can be processed again.
	 */
	@Override
	public void			putBackLastElement()
	{
		Matcher lastMatch = lastMatcher.matcher(currentPath);
		if (!lastMatch.matches())
			throw new RuntimeException("failed to find last element in currentPath: "+this.currentPath);
		this.currentPath = lastMatch.group(1);
		this.realPath = this.realPath.substring(0, this.realPath.length()-lastMatch.group(2).length());
		this.remainPath = this.remainPath != null ? lastMatch.group(2)+this.remainPath : lastMatch.group(2);
	}

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	@Override
	public String			getLastElement()
	{
		Matcher lastMatch = lastMatcher.matcher(currentPath);
		if (!lastMatch.matches())
			throw new RuntimeException("failed to find last element in currentPath: "+this.currentPath);
		return lastMatch.group(2);
	}

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	@Override
	public String			getLastElementWithoutSlash()
	{
		Matcher lastMatchWithoutSlash = lastMatcherWithoutSlash.matcher(currentPath);
		if (!lastMatchWithoutSlash.matches())
			throw new RuntimeException("failed to find last element in currentPath: "+this.currentPath);
		return lastMatchWithoutSlash.group(2);
	}

	/**
	 * Get reverse path to current path part.
	 *
	 * @return
	 * 	reverse path to current path part.
	 */
	@Override
	public String			getReversePath()
	{
		return remainPath == null ? "" : remainPath.replaceAll("[^/]+/", "../").replaceAll("[^/]+$", "");
	}

	/**
	 * Checks that there is no more item in the path.
	 *
	 * @return true
	 * 	if there is no more item
	 * @return false
	 * 	if there is an item remaining
	 */
	@Override
	public boolean			needPathFinal()
	{
		return getPathElement() == null;
	}

	/**
	 * Checks if the current path ends with slash, depending on needSlash parameter. Either redirects to slashed
	 * path or creates not found presenter if the condition is not satisfied.
	 *
	 * @return current element
	 * 	if the condition was satisfied
	 * @return null
	 * 	otherwise, in that case process() function should return !needSlash
	 */
	@Override
	public boolean			needPathSlash(boolean needSlash)
	{
		if (needSlash) {
			if (!currentPath.endsWith("/") && !currentPath.equals("")) {
				if (!getRequest().getMethod().equals("GET")) {
					setServerError(405, "Method Not Allowed");
				}
				else {
					getResponse().redirect("/"+currentPath+"/");
				}
				return false;
			}
		}
		else {
			if (currentPath.endsWith("/") || currentPath.equals("")) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String			getFullUrl()
	{
		HttpServletRequest servletRequest = ((JeeWebRequest)getRequest()).getServletRequest();
		StringBuffer url = servletRequest.getRequestURL();
		String query;
		if ((query = servletRequest.getQueryString()) != null)
			url.append(query);
		return url.toString();
	}

	@Override
	public boolean			redirect(@NotNull String url)
	{
		if (redirected != null) {
			throw new RuntimeException("already redirected to "+this.redirected);
		}
		// process pendingMessages ??
		this.redirected = url;
		return false;
	}

	@Override
	public void			redirectImm(String url)
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	@Override
	public String			getRedirected()
	{
		return this.redirected;
	}

	@Override
	public String			localize(String className, String text)
	{
		return uiContext.localize(className, text);
	}

	@Override
	public String			localizeArgs(String className, String text, Object[] args)
	{
		return uiContext.localizeArgs(className, text, args);
	}

	@Override
	public void			addMeta(MetaTag metaTag)
	{
		Map<String, MetaTag> typedMetas = metas.get(metaTag.getType());
		if (typedMetas == null)
			metas.put(metaTag.getType(), typedMetas = new LinkedHashMap<String, MetaTag>());
		typedMetas.put(metaTag.getName(), metaTag);
	}

	@Override
	public void			addMetaName(String name, String content)
	{
		this.addMeta(new MetaTag("name", name, content));
	}

	@Override
	public void			addMetaHttp(String name, String content)
	{
		this.addMeta(new MetaTag("http-equiv", name, content));
	}

	@Override
	public Map<String, Map<String, MetaTag>> getMetas()
	{
		return metas;
	}

	@Override
	public void			addLinkedFile(String type, PageUrl url)
	{
		Collection<PageUrl> linked = linkedFiles.get(type);
		if (linked == null)
			linkedFiles.put(type, linked = new LinkedHashSet<PageUrl>());
		linked.add(url);
	}

	@Override
	public void			addLinkedContent(String type, String identity, String content)
	{
		Collection<PageUrl> linked = linkedFiles.get(type);
		if (linked == null)
			linkedFiles.put(type, linked = new LinkedHashSet<PageUrl>());
		linked.add(PageUrl.createVirtual("@"+(identity == null ? "" : identity), Options.buildListed("content", content)));
	}

	@Override
	public Collection<PageUrl>	getLinkedFiles(String type)
	{
		return linkedFiles.get(type);
	}

	@Override
	public void addMessage(int msgType, String msg)
	{
		pendingMessages.add(new PendingMessage(msgType, msg));
	}

	@Override
	public void addMessageLocalized(int msgType, String classname, String msg)
	{
		pendingMessages.add(new PendingMessage(msgType, localize(classname, msg)));
	}

	@Override
	public void			close()
	{
		callerContext.close();
	}

	@Override
	public void			setActiveField(int priority, String activeField_)
	{
		this.activeField = activeField_;
	}

	@Override
	public List<PendingMessage>	getPendingMessages()
	{
		return this.pendingMessages;
	}

	protected String		redirected;

	protected int			serverError;

	public int			getServerError()
	{
		return this.serverError;
	}

	protected String		serverMessage;

	protected UiContext		uiContext;

	public UiContext		getUiContext()
	{
		return this.uiContext;
	}

	protected CallerContext		callerContext;

	public CallerContext		getCallerContext()
	{
		return this.callerContext;
	}

	public void			setCallerContext(CallerContext callerContext_)
	{
		this.callerContext = callerContext_;
	}

	protected Request		request;

	public Request			getRequest()
	{
		return this.request;
	}

	protected Response		response;

	public Response			getResponse()
	{
		return this.response;
	}

	protected String		title = "";

	public String			getTitle()
	{
		return this.title;
	}

	public void			setTitle(String title_)
	{
		this.title = title_;
	}

	protected String		activeField;

	public String			getActiveField()
	{
		return this.activeField;
	}

	protected long			started;

	public long			getStarted()
	{
		return this.started;
	}

	public void			setStarted(long started_)
	{
		this.started = started_;
	}

	protected String		currentPath = "";

	public String			getCurrentPath()
	{
		return this.currentPath;
	}

	public void			setCurrentPath(String currentPath_)
	{
		this.currentPath = currentPath_;
	}

	protected String		realPath = "";

	public String			getRealPath()
	{
		return this.realPath;
	}

	public void			setRealPath(String realPath_)
	{
		this.realPath = realPath_;
	}

	protected String		remainPath;

	public String			getRemainPath()
	{
		return this.remainPath;
	}

	public void			setRemainPath(String remainPath_)
	{
		this.remainPath = remainPath_;
	}

	protected String		processingPath = "";

	protected Map<String, Collection<PageUrl>> linkedFiles = new LinkedHashMap<String, Collection<PageUrl>>();

	protected Map<String, Map<String, MetaTag>> metas = new LinkedHashMap<String, Map<String, MetaTag>>();

	protected List<PendingMessage>	pendingMessages = new LinkedList<PendingMessage>();

	protected Session		session;

	protected static Pattern	pathMatcher = Pattern.compile("^([^/]+)(/+(.*))?$");
	protected static Pattern	lastMatcher = Pattern.compile("^(|.*/)([^/]+/*)$");
	protected static Pattern	lastMatcherWithoutSlash = Pattern.compile("^(|.*/)([^/]+)/*$");
}
