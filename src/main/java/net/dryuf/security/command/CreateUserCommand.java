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

package net.dryuf.security.command;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import net.dryuf.process.command.AbstractCommand;
import net.dryuf.process.command.ExternalCommandRunner;
import net.dryuf.process.command.GetOptions;
import net.dryuf.process.command.GetOptionsStd;
import net.dryuf.security.UserAccount;
import net.dryuf.security.bo.UserAccountBo;


public class CreateUserCommand extends AbstractCommand
{
	public void			main(String[] arguments)
	{
		System.exit(ExternalCommandRunner.createFromClassPath().runNew(CreateUserCommand.class, arguments));
	}

	public String			parseArguments(String[] arguments)
	{
		this.options = new HashMap<String, Object>();
		return super.parseArguments(arguments);
	}

	@Override
	public int			reportUsage(String reason)
	{
		return this.commandRunner.reportUsage(reason,
			"Options: [-h] username password email\n"+
			"	-h		print this help\n"
		);
	}

	@Override
	public int			process()
	{
		String[] parameters = (String[]) options.get("");
		String username = parameters[0];
		String email = parameters[1];
		String password = parameters[2];

		UserAccount userAccount = new net.dryuf.security.UserAccount();
		userAccount.setUsername(username);
		userAccount.setActivated(true);
		userAccount.setEmail(email);
		int error;
		if ((error = userAccountBo.createUser(userAccount, password)) == 0) {
			System.err.print(getCallerContext().getUiContext().localizeArgs(CreateUserCommand.class, "User {0} created, userId={1}\n", new Object[]{ userAccount.getUsername(), userAccount.getUserId() }));
			return 1;
		}
		else {
			System.err.print("Error occurred: "+userAccountBo.formatError(getCallerContext().getUiContext(), error)+"\n");
			return 0;
		}
	}

	protected List<Integer>		actions = new LinkedList<Integer>();

	protected Map<String, Object>	options;

	public Map<String, Object>	getOptions()
	{
		return this.options;
	}

	@Inject
	protected UserAccountBo		userAccountBo;

	@Override
	public GetOptions		getOptionsDefinition()
	{
		return optionsDefinition;
	}

	protected static GetOptions	optionsDefinition = new GetOptionsStd()
			.setMinParameters(3)
			.setMaxParameters(3);
}
