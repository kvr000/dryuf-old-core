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

package net.dryuf.security;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import net.dryuf.security.bo.UserAccountBo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserAccountDetails extends java.lang.Object implements UserDetails
{
	private static final long serialVersionUID = 1L;

	public				UserAccountDetails(UserAccount userAccount, UserAccountBo userAccountBo)
	{
		this.userAccount = userAccount;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return Sets.newLinkedHashSet(Collections2.transform(userAccountBo.listUserDomainRoles(userAccount.getUserId()), (String roleName) -> new SimpleGrantedAuthority(roleName)));
	}

	@Override
	public String			getUsername()
	{
		return userAccount.getUsername();
	}

	@Override
	public boolean			isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean			isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean			isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean			isEnabled()
	{
		return true;
	}

	@Override
	public String			getPassword()
	{
		return userAccount.getPassword();
	}

	public UserAccount		getUserAccount()
	{
		return userAccount;
	}

	protected UserAccount		userAccount;

	protected UserAccountBo		userAccountBo;
}
