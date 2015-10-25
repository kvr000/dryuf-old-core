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

package net.dryuf.security.web.mock;

import net.dryuf.core.CallerContext;
import net.dryuf.security.web.AuthenticationFrontend;
import net.dryuf.srvui.PageContext;
import net.dryuf.srvui.Request;

import javax.validation.constraints.NotNull;
import java.util.Set;


public class DummyAuthenticationFrontend extends java.lang.Object implements AuthenticationFrontend
{
	@Override
	public int			authenticateUserPassword(PageContext pageContext, String username, String password)
	{
		return 0;
	}

	@Override
	public CallerContext		initCallerContext(Request request)
	{
		throw new UnsupportedOperationException("initCallerContext");
	}

	@Override
	public void			logout(PageContext pageContext)
	{
	}

	@Override
	public void			setEffectiveUserId(@NotNull PageContext pageContext, Object userId)
	{
	}

	@Override
	public void			resetRoles(@NotNull PageContext pageContext, Set<String> newRoles)
	{
	}

	@Override
	public void			setTranslationLevel(@NotNull PageContext pageContext, int translationLevel)
	{
	}

	@Override
	public void			setTiming(@NotNull PageContext pageContext, boolean timing)
	{
	}
}
