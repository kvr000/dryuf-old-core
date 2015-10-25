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

import java.util.Set;


public class LangRefStringifier extends java.lang.Object implements net.dryuf.srvui.RefStringifier
{
	public				LangRefStringifier(PageContext pageContext, Set<String> ignoreList)
	{
		this.pageContext = pageContext;
		//this.ignoreList = ignoreList;
	}

	public String			stringifyRef(PageUrl ref)
	{
		String url = ref.getUrl();
		switch (ref.getType()) {
		case PageUrl.TYPE_FINAL:
			return url;

		case PageUrl.TYPE_RELATIVE:
			return url;

		case PageUrl.TYPE_ROOTED:
			return this.pageContext.getContextPath()+url;

		case PageUrl.TYPE_LANGUAGED:
			return this.pageContext.getContextPath()+"/"+this.pageContext.getLanguage()+url;

		case PageUrl.TYPE_PAGED:
			return this.pageContext.getContextPath()+"/"+this.pageContext.getLanguage()+"/"+(url.length() != 0 ? url+"/" : "");

		default:
			throw new RuntimeException("cannot stringify ref of type "+ref.getType());
		}
	}

	protected PageContext		pageContext;
}
