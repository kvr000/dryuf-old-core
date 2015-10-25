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


public class JsResourceRenderer extends java.lang.Object
{
	public				JsResourceRenderer()
	{
	}

	public void			prepare(PageContext pageContext)
	{
		for (String cssUrl: cssUrls) {
			pageContext.addLinkedFile("css", PageUrl.createResource(cssUrl));
		}
		for (String jsUrl: jsUrls) {
			pageContext.addLinkedFile("js", PageUrl.createResource(jsUrl));
		}
	}

	public void			render(PageContext pageContext)
	{
	}

	protected String[]		cssUrls = new String[0];

	public String[]			getCssUrls()
	{
		return this.cssUrls;
	}

	public void			setCssUrls(String[] cssUrls_)
	{
		this.cssUrls = cssUrls_;
	}
	protected String[]		jsUrls = new String[0];

	public String[]			getJsUrls()
	{
		return this.jsUrls;
	}

	public void			setJsUrls(String[] jsUrls_)
	{
		this.jsUrls = jsUrls_;
	}
	protected String		jsImpl;

	public String			getJsImpl()
	{
		return this.jsImpl;
	}

	public void			setJsImpl(String jsImpl_)
	{
		this.jsImpl = jsImpl_;
	}
}
