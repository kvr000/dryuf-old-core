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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

public class BinaryReaderTest extends java.lang.Object
{
	@Test
	public void			testLeInts()
	{
		Assert.assertEquals((byte)12, new BinaryReader(new byte[]{ 12 }).readLe8("byte"));
		Assert.assertEquals((short)312, new BinaryReader(new byte[]{ 56, 1 }).readLe16("short"));
		Assert.assertEquals(19770312, new BinaryReader(new byte[]{ (byte)0xc8, (byte)0xab, 0x2d, 1 }).readLe32("int"));
		Assert.assertEquals(19770312022330L, new BinaryReader(new byte[]{ (byte)0x3a, (byte)0xd9, (byte)0x6c, 0x22, (byte)0xfb, 0x11, 0, 0 }).readLe64("long"));
	}

	@Test
	public void			testBeInts()
	{
		Assert.assertEquals((byte)12, new BinaryReader(new byte[]{ 12 }).readBe8("byte"));
		Assert.assertEquals((short)312, new BinaryReader(new byte[]{ 1, 56 }).readBe16("short"));
		Assert.assertEquals(19770312, new BinaryReader(new byte[]{ 1, 0x2d, (byte)0xab, (byte)0xc8 }).readBe32("int"));
		Assert.assertEquals(19770312022330L, new BinaryReader(new byte[]{ 0, 0, 0x11, (byte)0xfb, 0x22, (byte)0x6c, (byte)0xd9, (byte)0x3a }).readBe64("long"));
	}

	@Test
	public void			testVarInts()
	{
		Assert.assertEquals((byte)12, new BinaryReader(new byte[]{ 12 }).readVarInt32("byte"));
		Assert.assertEquals(19770312, new BinaryReader(new byte[]{ (byte)(0x80+9), (byte)(0x80+54), (byte)(0x80+87), (byte)72 }).readVarInt32("int"));
		Assert.assertEquals(19770312022330L, new BinaryReader(new byte[]{ (byte)(0x80+4), (byte)(0x80+63), (byte)(0x80+50), (byte)(0x80+19), (byte)(0x80+51), (byte)(0x80+50), 58 }).readVarInt64("long"));
	}

	@Test
	public void			testPbufInts()
	{
		Assert.assertEquals((byte)12, new BinaryReader(new byte[]{ 12 }).readPbufInt32("byte"));
		Assert.assertEquals(19770312, new BinaryReader(new byte[]{ (byte)(0x80+72), (byte)(0x80+87), (byte)(0x80+54), (byte)9 }).readPbufInt32("int"));
		Assert.assertEquals(19770312022330L, new BinaryReader(new byte[]{ (byte)(0x80+58), (byte)(0x80+50), (byte)(0x80+51), (byte)(0x80+19), (byte)(0x80+50), (byte)(0x80+63), 4 }).readPbufInt64("long"));
	}

	@Test
	public void			testZigZagInts()
	{
		Assert.assertEquals((byte)12, new BinaryReader(new byte[]{ 24 }).readZigZag32("byte"));
		Assert.assertEquals(19770312, new BinaryReader(new byte[]{ (byte)(0x80+16), (byte)(0x80+47), (byte)(0x80+109), (byte)18 }).readZigZag32("int"));
		Assert.assertEquals(19770312022330L, new BinaryReader(new byte[]{ (byte)(0x80+116), (byte)(0x80+100), (byte)(0x80+102), (byte)(0x80+38), (byte)(0x80+100), (byte)(0x80+126), 8 }).readZigZag64("long"));
		Assert.assertEquals(-1, new BinaryReader(new byte[]{ (byte)1 }).readZigZag32("int"));
		Assert.assertEquals(-128, new BinaryReader(new byte[]{ (byte)(0x80+127), (byte)(1) }).readZigZag64("long"));
	}

	@Test
	public void			testString()
	{
		Assert.assertEquals("hello", new BinaryReader(new byte[]{ 'h', 'e', 'l', 'l', 'o' }).readString(5, "string"));
	}

	@Test
	public void			testHex()
	{
		Assert.assertEquals("0123456789ABCDEF", new String(new BinaryReader(new byte[]{ 0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef }).readBeHex(16, "string")));
		Assert.assertEquals("FED", new String(new BinaryReader(new byte[]{ (byte)0xFE, (byte)0xD0 }).readBeHex(3, "string")));
	}

	@Test
	public void			testVarString()
	{
		Assert.assertEquals("hello", new BinaryReader(new byte[]{ 5, 'h', 'e', 'l', 'l', 'o' }).readVarString(1000000, "string"));
	}

	@Test
	public void			testVarBytes()
	{
		Assert.assertEquals("hello", new String(new BinaryReader(new byte[]{ 5, 'h', 'e', 'l', 'l', 'o' }).readVarBytes(1000000, "string")));
	}

	@Test
	public void			testFrpcs()
	{
		Assert.assertEquals(19770312L, new BinaryReader(new byte[]{ 0x0b, (byte)0xc8, (byte)0xab, 0x2d, 1 }).readFrpcScalar("int"));
		Assert.assertEquals(0L, new BinaryReader(new byte[]{ 0x08, 0 }).readFrpcScalar("int"));
		Assert.assertEquals(1.5, new BinaryReader(new byte[]{ 0x18, 0, 0, 0, 0, 0, 0, (byte)0xf8, 0x3f }).readFrpcScalar("int"));
		Assert.assertEquals("hello", new BinaryReader(new byte[]{ 0x20, 5, 'h', 'e', 'l', 'l', 'o' }).readFrpcScalar("string"));
		Assert.assertEquals("hello", new String((byte[])new BinaryReader(new byte[]{ 0x30, 5, 'h', 'e', 'l', 'l', 'o' }).readFrpcScalar("string")));
	}
}
