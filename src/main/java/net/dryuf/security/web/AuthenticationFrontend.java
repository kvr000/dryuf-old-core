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

package net.dryuf.security.web;

import javax.validation.constraints.NotNull;

import net.dryuf.core.CallerContext;
import net.dryuf.srvui.PageContext;
import net.dryuf.srvui.Request;

import java.util.Set;


public interface AuthenticationFrontend
{
	/**
	 * Authenticates user and password pair and sets up user session context.
	 *
	 * @param pageContext
	 * 	frontend presenter
	 * @param username
	 * 	username provided by user
	 * @param password
	 * 	password provided by user
	 *
	 * @return net.dryuf.security.bo.UserAccountBo.ERR_Ok
	 * 	if authentication was successful
	 * @return error code from net.dryuf.security.bo.UserAccountBo
	 * 	if authentication failed
	 */
	public int			authenticateUserPassword(@NotNull PageContext pageContext, String username, String password);

	/**
	 * Creates caller context from the request.
	 *
	 * @param request
	 * 	Incoming request
	 *
	 * @return caller context
	 */
	public CallerContext		initCallerContext(@NotNull Request request);

	/**
	 * Logs out user.
	 *
	 * @param pageContext
	 * 	frontend pageContext
	 */
	public void			logout(@NotNull PageContext pageContext);

	/**
	 * Sets effective user id.
	 *
	 * @param pageContext
	 * 	frontend presenter
	 * @param userId
	 * 	the user id of effective user
	 */
	public void			setEffectiveUserId(@NotNull PageContext pageContext, Object userId);

	/**
	 * Resets the roles currently assigned to user.
	 *
	 * @param pageContext
	 * 	frontend presenter
	 * @param newRoles
	 * 	new roles for the current session
	 */
	public void			resetRoles(@NotNull PageContext pageContext, Set<String> newRoles);

	/**
	 * Sets translation level for current session.
	 *
	 * @param pageContext
	 * 	frontend presenter
	 * @param translationLevel
	 * 	new translation level
	 */
	public void			setTranslationLevel(@NotNull PageContext pageContext, int translationLevel);

	/**
	 * Sets translation level for current session.
	 *
	 * @param pageContext
	 * 	frontend presenter
	 * @param timing
	 * 	timing indicator
	 */
	public void			setTiming(@NotNull PageContext pageContext, boolean timing);
}
