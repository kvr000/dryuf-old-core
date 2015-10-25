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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.codec.Charsets;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import net.dryuf.core.CallerContext;
import net.dryuf.security.UserAccount;
import net.dryuf.security.UserAccountDomainRole;
import net.dryuf.service.time.TimeBo;
import net.dryuf.text.util.TextUtil;


public abstract class AbstractUserAccountBo extends java.lang.Object implements UserAccountBo
{
	@Override
	public String			encodePassword(String username, String salt, String plain)
	{
		return digestString(plain+"{"+username+"}");
	}

	public String			genSalt()
	{
		return null;
	}

	public String			genPassword()
	{
		return TextUtil.generateCode(16);
	}

	public boolean			equalPassword(String username, String storedPassword, String plain)
	{
		return storedPassword.equals(this.encodePassword(username, storedPassword, plain));
	}

	@Override
	public int			login(UserAccount userInfo, Collection<String> roles, String sessionId, String sourceIp)
	{
		UserAccount loaded;
		if ((loaded = this.loadByUsername(userInfo.getUsername())) == null)
			return ERR_UnknownAccount;
		if (!this.equalPassword(loaded.getUsername(), loaded.getPassword(), userInfo.getPassword()))
			return ERR_WrongPassword;
		if (!loaded.getActivated())
			return ERR_AccountUnactivated;

		storeLoginRecord(loaded, sessionId, sourceIp);

		userInfo.setUserId(loaded.getUserId());
		userInfo.setFirstName(loaded.getFirstName());
		userInfo.setLastName(loaded.getLastName());
		//userInfo.setSysRoles(loaded.getSysRoles());

		for (String roleName: listUserDomainRoles(loaded.getUserId())) {
			roles.add(roleName);
		}

		return ERR_Ok;
	}

	public abstract void		storeLoginRecord(UserAccount userAccount, String sessionId, String sourceIp);

	@Override
	public int			checkUserPassword(Long userId, String password)
	{
		UserAccount userAccount;
		if ((userAccount = load(userId)) == null)
			return ERR_UnknownAccount;
		if (!this.equalPassword(userAccount.getUsername(), userAccount.getPassword(), password))
			return ERR_WrongPassword;
		return ERR_Ok;
	}

	@Override
	public String			getActivityCode(Long userId)
	{
		UserAccount userAccount = load(userId);
		if (userAccount == null)
			return null;
		/*
		if (userAccount.getActivityStamp() <= (time()-1800)*1000)
			return 6;
		 */
		return digestString(userAccount.getUsername()+userAccount.getEmail()+userAccount.getActivityStamp());
	}

	@Override
	public String			digestString(String input)
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
		return new String(org.apache.commons.codec.binary.Hex.encodeHex(md.digest(input.getBytes(Charsets.UTF_8))));
	}

	@Override
	public String			loadUsername(long userId)
	{
		UserAccount userAccount = load(userId);
		return userAccount != null ? userAccount.getUsername() : null;
	}

	@Override
	public String			checkRequiredRoleForRole(CallerContext callerContext, String roleName)
	{
		return callerContext.checkRole(addableRolesDependencies.get(roleName)) ? null : addableRolesDependencies.get(roleName);
	}

	@Override
	public String			checkRequiredRoleForGroup(CallerContext callerContext, String groupName)
	{
		return callerContext.checkRole(addableGroupsDependencies.get(groupName)) ? null : addableGroupsDependencies.get(groupName);
	}

	@Override
	public Set<String>		listAddableRoles(final CallerContext callerContext)
	{
		return Sets.newLinkedHashSet(
			Collections2.transform(
				Sets.filter(
					addableRolesDependencies.entrySet(),
					(Map.Entry<String, String> entry) -> callerContext.checkRole(entry.getValue())
				),
				(Map.Entry<String, String> entry) -> entry.getKey()
			)
		);
	}

	@Override
	public Set<String>		listAddableGroups(final CallerContext callerContext)
	{
		return new HashSet<String>();
	}

	protected String		appDomainId;

	public String			getAppDomainId()
	{
		return this.appDomainId;
	}

	public void			setAppDomainId(String appDomainId_)
	{
		this.appDomainId = appDomainId_;
	}

	protected String		appName;

	public void			setAppName(String appName_)
	{
		this.appName = appName_;
	}

	protected Map<String, String>	addableRolesDependencies;

	public Map<String, String>	getAddableRolesDependencies()
	{
		return this.addableRolesDependencies;
	}

	public void			setAddableRolesDependencies(Map<String, String> addableRolesDependencies_)
	{
		this.addableRolesDependencies = addableRolesDependencies_;
	}

	protected Map<String, String>	addableGroupsDependencies;

	public Map<String, String>	getAddableGroupsDependencies()
	{
		return this.addableGroupsDependencies;
	}

	public void			setAddableGroupsDependencies(Map<String, String> addableGroupsDependencies_)
	{
		this.addableGroupsDependencies = addableGroupsDependencies_;
	}

	@Inject
	protected TimeBo		timeBo;

	protected final String[]	globalRolesNames = {
	        	"guest",
	        	"free",
	        	"user",
	        	"admin",
	        	"sysmeta",
	        	"sysconf",
	        	"swapuser",
	        	"dataop",
	        	"devel",
	        	"extreme",
	        	"translation",
	        	"timing"
	};
}
