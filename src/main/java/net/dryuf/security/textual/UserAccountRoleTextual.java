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

package net.dryuf.security.textual;


import net.dryuf.core.Dryuf;
import net.dryuf.security.UserAccount;
import net.dryuf.util.MapUtil;

import java.util.LinkedHashMap;

public class UserAccountRoleTextual extends net.dryuf.textual.GenericIntegerSetTextual
{
	public				UserAccountRoleTextual()
	{
		super(Dryuf.dotClassname(UserAccountRoleTextual.class), roleMapping);
	}

	protected static final LinkedHashMap<String, Integer> roleMapping = MapUtil.createLinkedHashMap(
		"guest",		UserAccount.UserRole.UR_Guest,
		"free",			UserAccount.UserRole.UR_Free,
		"user",			UserAccount.UserRole.UR_User,
		"admin",		UserAccount.UserRole.UR_Admin,
		"sysmeta",		UserAccount.UserRole.UR_Sysmeta,
		"sysconf",		UserAccount.UserRole.UR_Sysconf,
		"swapuser",		UserAccount.UserRole.UR_Swapuser,
		"dataop",		UserAccount.UserRole.UR_Dataop,
		"devel",		UserAccount.UserRole.UR_Devel,
		"extreme",		UserAccount.UserRole.UR_Extreme,
		"translation",		UserAccount.UserRole.UR_Translation,
		"timing",		UserAccount.UserRole.UR_Timing
	);
}
