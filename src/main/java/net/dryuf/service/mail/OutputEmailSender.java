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

package net.dryuf.service.mail;

import javax.inject.Inject;

import net.dryuf.io.FileData;
import net.dryuf.service.logger.LoggerService;


class OutputEmailSender extends java.lang.Object implements EmailSender
{
	public void			mailUtf8(String to, String subject, String content, String from)
	{
		loggerService.getLogger(loggerIdentifier).logMessage("EMAIL", "sending e-mail to "+to+", subject "+subject+":\n"+content);
	}

	@Override
	public void			mailAttachment(String to, String subject, String content, String from, FileData attachment)
	{
		loggerService.getLogger(loggerIdentifier).logMessage("EMAIL", "sending e-mail to "+to+", subject "+subject+":\n"+content+"\n\twith attachment "+attachment.getName());
	}

	protected String		loggerIdentifier = "email";

	public String			getLoggerIdentifier()
	{
		return this.loggerIdentifier;
	}

	public void			setLoggerIdentifier(String loggerIdentifier_)
	{
		this.loggerIdentifier = loggerIdentifier_;
	}

	@Inject
	LoggerService			loggerService;
}
