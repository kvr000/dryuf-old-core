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

package net.dryuf.service.mail.spring;

import javax.inject.Inject;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import net.dryuf.io.FileData;
import net.dryuf.service.mail.EmailSender;


class SpringEmailSender extends java.lang.Object implements EmailSender
{
	@Inject
	private MailSender		mailSender;
	@Inject
	private SimpleMailMessage	mailTemplate;

	public void			setMailSender(MailSender mailSender)
	{
		this.mailSender = mailSender;
	}

	public void			setMailTemplate(SimpleMailMessage templateMessage)
	{
		this.mailTemplate = templateMessage;
	}

	@Override
	public void			mailUtf8(String to, String subject, String content, String from)
	{
		SimpleMailMessage msg = new SimpleMailMessage(this.mailTemplate);
		if (from != null)
			msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(content);
		try{
			this.mailSender.send(msg);
		}
		catch (MailException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void			mailAttachment(String to, String subject, String content, String from, FileData attachment)
	{
		throw new RuntimeException("TODO");
	}
}
