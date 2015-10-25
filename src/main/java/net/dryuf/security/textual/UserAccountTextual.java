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

import net.dryuf.core.CallerContext;
import net.dryuf.core.Textual;
import net.dryuf.security.bo.UserAccountBo;


public class UserAccountTextual extends net.dryuf.textual.NaturalLongTextual
{
	@Override
	public Textual<Long>		setCallerContext(CallerContext callerContext)
	{
		super.setCallerContext(callerContext);
		this.boUserAccount = (UserAccountBo) callerContext.getBean("userAccountBo");
		return this;
	}

	public String			format(java.lang.Long internal, String style)
	{
		return this.boUserAccount.loadUsername(internal);
	}

	public UserAccountBo		boUserAccount;
}
