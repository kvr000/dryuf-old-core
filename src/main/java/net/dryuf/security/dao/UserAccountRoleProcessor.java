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

package net.dryuf.security.dao;

import java.util.LinkedList;
import java.util.List;

import net.dryuf.core.CallerContext;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.RoleContext;
import net.dryuf.dao.RoleQuery;
import net.dryuf.security.UserAccount;


public class UserAccountRoleProcessor extends net.dryuf.dao.AbstractRoleProcessor<UserAccount>
{
	public				UserAccountRoleProcessor(CallerContext baseContext, Class<UserAccount> clazz)
	{
		super(baseContext, clazz);
		this.isUserview = baseContext.checkRole("userview");
		this.isAdmin = baseContext.checkRole("admin");
	}

	@Override
	public void			modifyQuery(RoleQuery query)
	{
		if (this.isUserview) {
			query.appendColumns(adminAppendedColumns);
		}
		else if (this.isAdmin) {
			query.appendColumns(adminAppendedColumns);
			query.appendWhere(" AND ent.userId = ?");
			query.appendWhereBind(callerContext.getContextVar("userId"));
		}
		else {
			query.appendColumns(appendedColumns);
			query.appendWhere(" AND ent.userId = ?");
			query.appendWhereBind(callerContext.getContextVar("userId"));
		}
	}

	@Override
	public EntityHolder<UserAccount> createEntityFromResult(Object result)
	{
		UserAccount entity = (UserAccount)result;
		RoleContext newContext = new RoleContext(this.callerContext);
		newContext.getRoles().add("UserAccount.read");
		if (entity.getUserId().equals(callerContext.getUserId()) && callerContext.checkRole("free")) {
			newContext.getRoles().add("UserAccount.update");
		}
		if (this.isAdmin) {
			newContext.getRoles().add("UserAccount.update");
			newContext.getRoles().add("UserAccount.admin");
		}
		return new EntityHolder<UserAccount>(entity, newContext);
	}

	protected boolean		isUserview;
	protected boolean		isAdmin;

	protected static List<String> appendedColumns = new LinkedList<String>();
	static {
	}
	protected static List<String> adminAppendedColumns = new LinkedList<String>();
	static {
	}
}
