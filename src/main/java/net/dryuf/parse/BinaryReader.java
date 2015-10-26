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

package net.dryuf.parse;

import net.dryuf.core.ByteUtil;


public class BinaryReader extends java.lang.Object
{
	public				BinaryReader(byte[] content_)
	{
		this.content = content_;
		this.pos = 0;
		this.limit = content.length;
	}

	public static BinaryReader	wrapWithLength(byte[] content, int length)
	{
		BinaryReader self = new BinaryReader(content);
		self.limit = length;
		return self;
	}

	public static BinaryReader	wrapWithOffsetLength(byte[] content, int offset, int length)
	{
		BinaryReader self = new BinaryReader(content);
		self.pos = offset;
		self.limit = length;
		return self;
	}

	public int			readVarInt32(String name)
	{
		int number = 0;
		byte ch = this.content[pos++];
		while ((ch&0x80) != 0) {
			number |= ch&0x7f;
			number <<= 7;
			ch = this.content[pos++];
		}
		number |= ch;
		return number;
	}

	public long			readVarInt64(String name)
	{
		long number = 0;
		byte ch = this.content[pos++];
		while ((ch&0x80) != 0) {
			number |= ch&0x7f;
			number <<= 7;
			ch = this.content[pos++];
		}
		number |= ch;
		return number;
	}

	public byte			readLe8(String name)
	{
		return this.content[pos++];
	}

	public byte			readBe8(String name)
	{
		return this.content[pos++];
	}

	public short			readLe16(String name)
	{
		short v = (short)((this.content[pos]&0xff)+((this.content[pos+1]&0xff)<<8));
		pos += 2;
		return v;
	}

	public short			readBe16(String name)
	{
		short v = (short)(((this.content[pos]&0xff)<<8)+((this.content[pos+1]&0xff)<<0));
		pos += 2;
		return v;
	}

	public int			readLe32(String name)
	{
		int v = ((this.content[pos]&0xff)<<0)+((this.content[pos+1]&0xff)<<8)+((this.content[pos+2]&0xff)<<16)+((this.content[pos+3]&0xff)<<24);
		pos += 4;
		return v;
	}

	public int			readBe32(String name)
	{
		int v = ((this.content[pos]&0xff)<<24)+((this.content[pos+1]&0xff)<<16)+((this.content[pos+2]&0xff)<<8)+((this.content[pos+3]&0xff)<<0);
		pos += 4;
		return v;
	}

	public long			readLe64(String name)
	{
		long v = ((this.content[pos+0]&0xffL)<<0)+((this.content[pos+1]&0xffL)<<8)+((this.content[pos+2]&0xffL)<<16)+((this.content[pos+3]&0xffL)<<24)+((this.content[pos+4]&0xffL)<<32)+((this.content[pos+5]&0xffL)<<40)+((this.content[pos+6]&0xffL)<<48)+((this.content[pos+7]&0xffL)<<56);
		pos += 8;
		return v;
	}

	public long			readBe64(String name)
	{
		long v = ((this.content[pos+0]&0xffL)<<56)+((this.content[pos+1]&0xffL)<<48)+((this.content[pos+2]&0xffL)<<40)+((this.content[pos+3]&0xffL)<<32)+((this.content[pos+4]&0xffL)<<24)+((this.content[pos+5]&0xffL)<<16)+((this.content[pos+6]&0xffL)<<8)+((this.content[pos+7]&0xffL)<<0);
		pos += 8;
		return v;
	}

	public byte[]			readBytes(int length, String name)
	{
		byte[] v = ByteUtil.subBytes(content, pos, length);
		pos += length;
		return v;
	}

	public String			readString(int length, String name)
	{
		return new String(readBytes(length, name));
	}

	public static byte[]		convertBeHex(byte b)
	{
		byte f = (byte)((b>>4)&0xf);
		byte s = (byte)((b>>0)&0xf);
		return new byte[]{ f < 10 ? (byte)('0'+f) : (byte)('A'+f-10), s < 10 ? (byte)('0'+s) : (byte)('A'+s-10) };
	}

	public byte[]			readBeHex(int numHalfs, String name)
	{
		byte[] o = new byte[numHalfs];
		int p = 0;
		for (; numHalfs >= 2; numHalfs -= 2) {
			byte b = readLe8(name);
			byte f = (byte)((b>>4)&0xf);
			o[p++] = f < 10 ? (byte)('0'+f) : (byte)('A'+f-10);
			byte s = (byte)((b>>0)&0xf);
			o[p++] = s < 10 ? (byte)('0'+s) : (byte)('A'+s-10);
		}
		if (numHalfs > 0) {
			byte b = readLe8(name);
			byte f = (byte)((b>>4)&0xf);
			o[p++] = f < 10 ? (byte)('0'+f) : (byte)('A'+f-10);
		}
		return o;
	}

	public byte[]			readVarBytes(int max_size, String name)
	{
		int length = this.readVarInt32(name);
		if (length > max_size)
			throw new ArrayIndexOutOfBoundsException("length > max_size for "+name);
		return this.readBytes(length, name);
	}

	public String			readVarString(int max_size, String name)
	{
		return new String(readVarBytes(max_size, name));
	}

	public Object			readFrpcScalar(String name)
	{
		int type = this.readLe8(name);
		switch (type>>3) {
		case 1:
			{
				// int
				byte last = 0;
				long value = 0;
				type &= 7;
				int i;
				for (i = 0; type-- >= 0; i += 8) {
					value += ((last = this.readLe8(name))&0xff)<<i;
				}
				if (last < 0) {
					value += -1L<<i;
				}
				return value;
			}

		case 3:
			{
				// double
				return Double.longBitsToDouble(readLe64(name));
			}

		case 4:
			{
				// string
				long length = 0;
				type &= 7;
				for (int i = 0; type-- >= 0; i += 8) {
					length += this.readLe8(name)<<i;
				}
				return this.readString((int)length, name);
			}

		case 6:
			{
				// bytes
				long length = 0;
				type &= 7;
				for (int i = 0; type-- >= 0; i += 8) {
					length += this.readLe8(name)<<i;
				}
				return this.readBytes((int)length, name);
			}

		default:
			throw new UnsupportedOperationException("unknown FRPC scalar type: "+type);
		}
	}

	public byte[]			readRest(String name)
	{
		return readBytes(limit-pos, name);
	}

	public int			getPos()
	{
		return this.pos;
	}

	public boolean			isEnd()
	{
		return this.pos == this.limit;
	}

	public int			getLength()
	{
		return limit;
	}

	protected byte[]		content;

	protected int			pos;

	protected int			limit;
}
