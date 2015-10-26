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

package net.dryuf.service.image.kaptcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

import net.dryuf.io.FileData;
import net.dryuf.io.FileDataImpl;
import net.dryuf.service.image.CaptchaService;


public class KaptchaCaptchaService extends java.lang.Object implements CaptchaService
{
	public FileData			generateCaptcha()
	{
		FileDataImpl fileData = new FileDataImpl();

		Config config = new Config(new Properties());
		Producer kaptchaProducer = config.getProducerImpl();

		// return a jpeg
		fileData.setContentType("image/jpeg");

		// create the text for the image
		String capText = kaptchaProducer.createText();
		fileData.setName(capText);

		// create the image with the text
		BufferedImage bi = kaptchaProducer.createImage(capText);

		// write the data out
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", out);
			byte[] bytes = out.toByteArray();
			fileData.setInputStream(new ByteArrayInputStream(bytes));
			fileData.setSize(bytes.length);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		return fileData;
	}
}
