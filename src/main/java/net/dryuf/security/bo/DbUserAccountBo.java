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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;

import net.dryuf.core.UiContext;
import net.dryuf.security.AppDomainDef;
import net.dryuf.security.UserAccount;
import net.dryuf.security.UserAccountDomain;
import net.dryuf.security.UserAccountDomainRole;
import net.dryuf.security.UserLoginRecord;
import net.dryuf.security.dao.AppDomainDefDao;
import net.dryuf.security.dao.UserAccountDao;
import net.dryuf.security.dao.UserAccountDomainDao;
import net.dryuf.security.dao.UserAccountDomainGroupDao;
import net.dryuf.security.dao.UserAccountDomainRoleDao;
import net.dryuf.security.dao.UserLoginRecordDao;


public class DbUserAccountBo extends AbstractUserAccountBo
{
	public UserAccountDao		getDaoUserAccount()
	{
		return this.userAccountDao;
	}

	@Override
	public String			formatError(UiContext uiContext, int error)
	{
		String code;
		switch (error) {
		case ERR_Ok:
			return null;

		case ERR_UnknownAccount:
			code = "User unknown";
			break;

		case ERR_WrongPassword:
			code = "Wrong password";
			break;

		case ERR_AccountLocked:
			code = "Account locked";
			break;

		case ERR_AccountExpired:
			code = "Account expired";
			break;

		case ERR_AccountUnactivated:
			code = "Account unactivated";
			break;

		case ERR_UserExists:
			code = "User exists";
			break;

		case ERR_EmailExists:
			code = "Email exists";
			break;

		case ERR_OpenIdExists:
			code = "Open ID exists";
			break;

		case ERR_UniqueConstraint:
			code = "Unique constraint violation";
			break;

		case ERR_BadActivationCode:
			code = "Bad activation code";
			break;

		default:
			return uiContext.localizeArgs(UserAccountBo.class, "Unknown error: {0}", new Object[]{ error });
		}
		return uiContext.localize(UserAccountBo.class, code);
	}

	@Override
	public AppDomainDef		getAppDomainDef()
	{
		return appDomainDefDao.loadByPk(appDomainId);
	}

	@Override
	public void			storeLoginRecord(UserAccount userAccount, String sessionId, String sourceIp)
	{
		long time = timeBo.currentTimeMillis();
		UserLoginRecord userLoginRecord = new UserLoginRecord();
		userLoginRecord.setUserId(userAccount.getUserId());
		userLoginRecord.setLoginTime(time);
		userLoginRecord.setLoginAddress(sourceIp);
		userLoginRecord.setAccessTime(time);
		userLoginRecord.setSessionId(sessionId);
		userLoginRecord.setTargetApp(appName);
		userLoginRecordDao.insert(userLoginRecord);
	}

	public int			setUserPassword(final UserAccount userAccount, final String newPassword)
	{
		return userAccountDao.runTransactionedNewSafe(new Callable<Integer>() {
			@Override
			public Integer call() {
				if (!userAccountDao.setPassword(userAccount.getUserId(), encodePassword(userAccount.getUsername(), null, newPassword), System.currentTimeMillis()))
					return 1;
				return 0;
			}
		});
	}

	public String			getActivityCode(Long userId)
	{
		UserAccount userAccount = userAccountDao.loadByPk(userId);
		if (userAccount == null)
			return null;
		/*
		if (userAccount.getActivityStamp() <= (time()-1800)*1000)
			return 6;
		 */
		return digestString(userAccount.getUsername()+userAccount.getEmail()+userAccount.getActivityStamp());
	}

	public String			digestString(String input)
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
		return new String(org.apache.commons.codec.binary.Hex.encodeHex(md.digest(input.getBytes(StandardCharsets.UTF_8))));
	}

	@Override
	public String			updateActivity(Long userId)
	{
		userAccountDao.updateActivity(userId, System.currentTimeMillis());
		return this.getActivityCode(userId);
	}

	@Override
	public int			activateUser(final String username)
	{
		return userAccountDao.runTransactionedNewSafe(new Callable<Integer>() {
			@Override
			public Integer call() {
				UserAccount userAccount;
				if ((userAccount = userAccountDao.loadByUsername(username)) == null)
					return ERR_UnknownAccount;
				return userAccountDao.activateUser(userAccount.getUserId(), timeBo.currentTimeMillis()) ? ERR_Ok : ERR_UnknownAccount;
			}
		});
	}

	@Override
	public UserAccount		load(long userId)
	{
		return this.userAccountDao.loadByPk(userId);
	}

	@Override
	public String			loadUsername(long userId)
	{
		UserAccount userAccount = load(userId);
		return userAccount != null ? userAccount.getUsername() : null;
	}

	@Override
	public UserAccount		loadByUsername(String username)
	{
		return userAccountDao.loadByUsername(username);
	}

	@Override
	public Collection<String>	listUserDomainRoles(long userId)
	{
		LinkedHashSet<String> roles = new LinkedHashSet<>(userAccountDomainRoleDao.listRolesForUserDomain(userId, appDomainId));
		roles.addAll(userAccountDomainGroupDao.listGroupRolesForUserDomain(userId, appDomainId));
		return roles;
	}

	@Override
	public AppDomainDef		loadDomainByAlias(String alias)
	{
		return appDomainDefDao.loadByPk(appDomainId);
	}

	@Override
	public void			addUserDomainRoles(UserAccount userAccount, AppDomainDef domainDef, List<String> roles)
	{
		UserAccountDomain userAccountDomain = new UserAccountDomain();
		userAccountDomain.setUserId(userAccount.getUserId());
		userAccountDomain.setDomain(domainDef.getDomain());
		userAccountDomainDao.update(userAccountDomain);
		for (String roleName: roles) {
			UserAccountDomainRole userAccountDomainRole = new UserAccountDomainRole();
			userAccountDomainRole.setDomain(userAccountDomain.getPk());
			userAccountDomainRole.setRoleName(roleName);
			userAccountDomainRoleDao.update(userAccountDomainRole);
		}
	}

	@Override
	public int			createUser(final UserAccount userAccount, String plainPassword)
	{
		userAccount.setPassword(this.encodePassword(userAccount.getUsername(), this.genSalt(), plainPassword));
		userAccount.setActivityStamp(System.currentTimeMillis());
		userAccountDao.runTransactionedNewSafe(
				new Callable<Object>() {
					@Override public Object call() {
						userAccountDao.insert(userAccount);
						userAccountDomainRoleDao.initUserWithDefaultRoles(userAccount.getUserId(), appDomainId);
						userAccountDomainGroupDao.initUserWithDefaultGroups(userAccount.getUserId(), appDomainId);
						return userAccount;
					}
				}
		);
		return ERR_Ok;
	}

	@Inject
	protected UserAccountDao	userAccountDao;

	@Inject
	protected UserLoginRecordDao	userLoginRecordDao;

	@Inject
	protected AppDomainDefDao	appDomainDefDao;

	@Inject
	protected UserAccountDomainDao	userAccountDomainDao;

	@Inject
	protected UserAccountDomainRoleDao userAccountDomainRoleDao;

	@Inject
	protected UserAccountDomainGroupDao userAccountDomainGroupDao;
}
