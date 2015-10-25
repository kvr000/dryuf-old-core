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

package net.dryuf.service.image.awt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.dryuf.service.image.ImageResizeService;


public class AwtResizeService extends java.lang.Object implements ImageResizeService
{
	@Override
	public byte[]			resizeToMaxWh(byte[] content, int maxWidth, int maxHeight, boolean rerotate, String suffix)
	{
		BufferedImage origImage;
		try {
			origImage = ImageIO.read(new ByteArrayInputStream(content));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		int origWidth = origImage.getWidth(), origHeight = origImage.getHeight();
		int type = origImage.getType() == 0? BufferedImage.TYPE_INT_RGB : origImage.getType();

		float ratio;
		ratio = maxWidth/origWidth;
		if (maxHeight/origHeight < ratio)
			ratio = maxHeight/origHeight;

		BufferedImage resizedImage = new BufferedImage((int)(origWidth*ratio), (int)(origHeight*ratio), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(origImage, 0, 0, (int)(origWidth*ratio), (int)(origHeight*ratio), null);
		g.dispose();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(resizedImage, suffix, out);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out.toByteArray();
	}

	@Override
	public byte[]			resizeScale(byte[] content, double scale, boolean rerotate, String suffix)
	{
		BufferedImage origImage;
		try {
			origImage = ImageIO.read(new ByteArrayInputStream(content));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		int origWidth = origImage.getWidth(), origHeight = origImage.getHeight();
		int type = origImage.getType() == 0? BufferedImage.TYPE_INT_RGB : origImage.getType();

		BufferedImage resizedImage = new BufferedImage((int)(origWidth*scale), (int)(origHeight*scale), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(origImage, 0, 0, (int)(origWidth*scale), (int)(origHeight*scale), null);
		g.dispose();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(resizedImage, suffix, out);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out.toByteArray();
	}
}
