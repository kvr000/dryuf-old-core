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

package net.dryuf.parse.test;


import net.dryuf.parse.BinaryReader;
import net.dryuf.parse.BinaryWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

public class BinaryWriterTest extends java.lang.Object
{
	@Test
	public void			testLeInts()
	{
		Assert.assertArrayEquals(new byte[]{ 12 }, new BinaryWriter().writeLe8((byte)12).getContent());
		Assert.assertArrayEquals(new byte[]{ 56, 1 }, new BinaryWriter().writeLe16((short)312).getContent());
		Assert.assertArrayEquals(new byte[]{ (byte)0xc8, (byte)0xab, 0x2d, 1 }, new BinaryWriter().writeLe32(19770312).getContent());
		Assert.assertArrayEquals(new byte[]{ (byte)0x3a, (byte)0xd9, (byte)0x6c, 0x22, (byte)0xfb, 0x11, 0, 0 }, new BinaryWriter().writeLe64(19770312022330L).getContent());
	}

	@Test
	public void			testBeInts()
	{
		Assert.assertArrayEquals(new byte[]{ 12 }, new BinaryWriter().writeBe8((byte)12).getContent());
		Assert.assertArrayEquals(new byte[]{ 1, 56 }, new BinaryWriter().writeBe16((short)312).getContent());
		Assert.assertArrayEquals(new byte[]{ 1, 0x2d, (byte)0xab, (byte)0xc8 }, new BinaryWriter().writeBe32(19770312).getContent());
		Assert.assertArrayEquals(new byte[]{ 0, 0, 0x11, (byte)0xfb, 0x22, (byte)0x6c, (byte)0xd9, (byte)0x3a }, new BinaryWriter().writeBe64(19770312022330L).getContent());
	}

	@Test
	public void			testVarInts()
	{
		Assert.assertArrayEquals(new byte[]{ 12 }, new BinaryWriter().writeVarInt((byte) 12).getContent());
		Assert.assertArrayEquals(new byte[]{ (byte)(0x80+9), (byte)(0x80+54), (byte)(0x80+87), (byte)72 }, new BinaryWriter().writeVarInt(19770312).getContent());
		Assert.assertArrayEquals(new byte[]{ (byte)(0x80+4), (byte)(0x80+63), (byte)(0x80+50), (byte)(0x80+19), (byte)(0x80+51), (byte)(0x80+50), 58 }, new BinaryWriter().writeVarInt(19770312022330L).getContent());
	}

	@Test
	public void			testString()
	{
		Assert.assertArrayEquals(new byte[]{ 'h', 'e', 'l', 'l', 'o' }, new BinaryWriter().writeString("hello").getContent());
	}

	@Test
	public void			testHex()
	{
		Assert.assertArrayEquals(new byte[]{ 0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef }, new BinaryWriter().writeBeHex("0123456789ABCDEF").getContent());
		Assert.assertArrayEquals(new byte[]{ (byte)0xFE, (byte)0xD0 }, new BinaryWriter().writeBeHex("FED").getContent());
	}

	@Test
	public void			testVarString()
	{
		Assert.assertArrayEquals(new byte[]{ 5, 'h', 'e', 'l', 'l', 'o' }, new BinaryWriter().writeVarString("hello").getContent());
	}

	@Test
	public void			testVarBytes()
	{
		Assert.assertArrayEquals(new byte[]{ 5, 'h', 'e', 'l', 'l', 'o' }, new BinaryWriter().writeVarBytes(new byte[]{ 'h', 'e', 'l', 'l', 'o' }).getContent());
	}

	@Test
	public void			testFrpcs()
	{
		Assert.assertArrayEquals(new byte[]{ 0x0b, (byte)0xc8, (byte)0xab, 0x2d, 1 }, new BinaryWriter().writeFrpcInt(19770312L).getContent());
		Assert.assertArrayEquals(new byte[]{ 0x08, 0 }, new BinaryWriter().writeFrpcInt(0L).getContent());
		Assert.assertArrayEquals(new byte[]{ 0x18, 0, 0, 0, 0, 0, 0, (byte)0xf8, 0x3f }, new BinaryWriter().writeFrpcDouble(1.5).getContent());
		Assert.assertArrayEquals(new byte[]{ 0x20, 5, 'h', 'e', 'l', 'l', 'o' }, new BinaryWriter().writeFrpcString("hello").getContent());
		Assert.assertArrayEquals(new byte[]{0x30, 5, 'h', 'e', 'l', 'l', 'o'}, new BinaryWriter().writeFrpcBytes(new byte[]{'h', 'e', 'l', 'l', 'o'}).getContent());
	}
}
