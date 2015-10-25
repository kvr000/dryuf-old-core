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

package net.dryuf.security.bo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dryuf.core.CallerContext;
import net.dryuf.core.UiContext;
import net.dryuf.security.AppDomainDef;
import net.dryuf.security.UserAccount;
import net.dryuf.security.UserAccountDomainRole;


/**
 * Common error codes:
 *
 * @return 0
 * 	if everything is successfull
 * @return 1
 * 	if the account does not exist
 * @return 2
 * 	if the password was wrong
 * @return 3
 * 	if the account is locked
 * @return 4
 * 	if the account expired
 * @return 5
 * 	if the account is not activated
 * @return 6
 * 	if the user already exists
 * @return 7
 * 	if the email already exists
 * @return 8
 * 	if the email already exists
 * @return 9
 * 	other unique constraint unsatisfied
 *
 * @return 10
 * 	if the activation code is wrong
 */
public interface UserAccountBo
{
	public static final int		ERR_Ok				= 0;
	public static final int		ERR_UnknownAccount		= 1;
	public static final int		ERR_WrongPassword		= 2;
	public static final int		ERR_AccountLocked		= 3;
	public static final int		ERR_AccountExpired		= 4;
	public static final int		ERR_AccountUnactivated		= 5;
	public static final int		ERR_UserExists			= 6;
	public static final int		ERR_EmailExists			= 7;
	public static final int		ERR_OpenIdExists		= 8;
	public static final int		ERR_UniqueConstraint		= 9;
	public static final int		ERR_BadActivationCode		= 10;

	public String			formatError(UiContext uiContext, int error);

	public String			encodePassword(String username, String salt, String plain);

	public AppDomainDef		getAppDomainDef();

	public String			getAppDomainId();

	public String			genPassword();

	public int			login(UserAccount userAccount, Collection<String> roles, String sid, String sourceIp);

	public int			checkUserPassword(Long userId, String password);

	public int			setUserPassword(UserAccount userAccount, String newPassword);

	public int			createUser(UserAccount userInfo, String plainPassword);

	public String			getActivityCode(Long userId);

	public String			digestString(String input);

	public String			updateActivity(Long userId);

	public int			activateUser(String username);

	public UserAccount		load(long userId);

	public String			loadUsername(long userId);

	public UserAccount		loadByUsername(String username);

	public Collection<String>	listUserDomainRoles(long userId);

	public AppDomainDef		loadDomainByAlias(String alias);

	public void			addUserDomainRoles(UserAccount userInfo, AppDomainDef domainDef, List<String> roles);

	/**
	 * Checks whether the caller is eligible for adding this domain role.
	 *
	 * @param callerContext
	 * 	caller details
	 *
	 * @return null
	 * 	if action is allowed
	 * @return required role name
	 * 	if the role is not allowed to be added by caller
	 */
	public String			checkRequiredRoleForRole(CallerContext callerContext, String roleName);

	/**
	 * Checks whether the caller is eligible for adding this domain group.
	 *
	 * @param callerContext
	 * 	caller details
	 *
	 * @return null
	 * 	if action is allowed
	 * @return required role name
	 * 	if the group is not allowed to be added by caller
	 */
	public String			checkRequiredRoleForGroup(CallerContext callerContext, String groupName);

	public Set<String>		listAddableRoles(CallerContext callerContext);

	public Set<String>		listAddableGroups(CallerContext callerContext);

	public Map<String, String>	getAddableRolesDependencies();

	public Map<String, String>	getAddableGroupsDependencies();
}
