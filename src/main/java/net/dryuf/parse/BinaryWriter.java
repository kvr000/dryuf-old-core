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

import java.nio.charset.StandardCharsets;

import net.dryuf.core.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;


public class BinaryWriter extends java.lang.Object
{
	public				BinaryWriter()
	{
	}

	static public byte[]		createVarInt(long number)
	{
		byte[] buff = new byte[16];
		int i = buff.length;
		buff[--i] = (byte)(number&0x7f);
		for (number = (number>>7)&0x7fffffffffffffffL; number != 0; number >>= 7) {
			buff[--i] = (byte)(number|0x80);
		}
		return ByteUtil.subBytes(buff, i);
	}

	static public byte[]		createPbufInt32(int number)
	{
		byte[] buff = new byte[16];
		int i = 0;
		buff[i] = (byte)(number|0x80);
		number >>>= 7;
		for (int base = 7; number != 0; base += 7, number >>>= 7) {
			buff[++i] =(byte)(number|0x80);
		}
		buff[i] &= 0x7f;
		return ByteUtil.subBytes(buff, 0, i+1);
	}

	static public byte[]		createPbufInt64(long number)
	{
		byte[] buff = new byte[16];
		int i = 0;
		buff[i] = (byte)(number|0x80);
		number >>>= 7;
		for (int base = 7; number != 0; base += 7, number >>>= 7) {
			buff[++i] =(byte)(number|0x80);
		}
		buff[i] &= 0x7f;
		return ByteUtil.subBytes(buff, 0, i+1);
	}

	static public byte[]		createZigZagInt(long number)
	{
		return createPbufInt64((number<<1)^(number>>63));
	}

	public BinaryWriter		writeLe8(byte value)
	{
		this.writeDirect(new byte[]{ value });
		return this;
	}

	public BinaryWriter		writeLe16(short value)
	{
		this.writeDirect(new byte[]{ (byte)(value&0xff), (byte)(value>>8) });
		return this;
	}

	public BinaryWriter		writeLe32(int value)
	{
		this.writeDirect(new byte[]{ (byte)(value>>0), (byte)(value>>8), (byte)(value>>16), (byte)(value>>24) });
		return this;
	}

	public BinaryWriter		writeLe64(long value)
	{
		this.writeDirect(new byte[]{ (byte)(value>>0), (byte)(value>>8), (byte)(value>>16), (byte)(value>>24), (byte)(value>>32), (byte)(value>>40), (byte)(value>>48), (byte)(value>>56) });
		return this;
	}

	public BinaryWriter		writeBe8(byte value)
	{
		this.writeDirect(new byte[]{ value });
		return this;
	}

	public BinaryWriter		writeBe16(short value)
	{
		this.writeDirect(new byte[]{ (byte)(value>>8), (byte)(value&0xff) });
		return this;
	}

	public BinaryWriter		writeBe32(int value)
	{
		this.writeDirect(new byte[]{ (byte)(value>>24), (byte)(value>>16), (byte)(value>>8), (byte)(value>>0) });
		return this;
	}

	public BinaryWriter		writeBe64(long value)
	{
		this.writeDirect(new byte[]{ (byte)(value>>56), (byte)(value>>48), (byte)(value>>40), (byte)(value>>32), (byte)(value>>24), (byte)(value>>16), (byte)(value>>8), (byte)(value>>0) });
		return this;
	}

	public BinaryWriter		writeVarInt(long number)
	{
		writeDirect(createVarInt(number));
		return this;
	}

	public BinaryWriter		writePbufInt32(int number)
	{
		writeDirect(createPbufInt32(number));
		return this;
	}

	public BinaryWriter		writePbufInt64(long number)
	{
		writeDirect(createPbufInt64(number));
		return this;
	}

	public BinaryWriter		writeZigZagInt(long number)
	{
		writeDirect(createZigZagInt(number));
		return this;
	}

	public BinaryWriter		writeBytes(byte[] data)
	{
		this.writeDirect(data);
		return this;
	}

	public BinaryWriter		writeString(String str)
	{
		this.writeDirect(str.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public static byte		convertHexCode(char code)
	{
		switch (code) {
		case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
			return (byte)(code-'0');
		case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
			return (byte)(code-('A'-10));
		case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
			return (byte)(code-('a'-10));
		default:
			throw new ParseException("invalid hex code: "+code);
		}
	}

	public BinaryWriter		writeBeHex(String hex)
	{
		int i;
		for (i = 0; i < hex.length()-1; i += 2) {
			this.writeDirect(new byte[] { (byte)((convertHexCode(hex.charAt(i))<<4)|(convertHexCode(hex.charAt(i+1)))) });
		}
		if (i < hex.length()) {
			this.writeDirect(new byte[] { (byte)((convertHexCode(hex.charAt(i))<<4)) });
		}
		return this;
	}

	public BinaryWriter		writeVarBytes(byte[] data)
	{
		this.writeVarInt(data.length);
		this.writeDirect(data);
		return this;
	}

	public BinaryWriter		writeVarString(String data)
	{
		this.writeVarBytes(data.getBytes(StandardCharsets.UTF_8));
		return this;
	}

	public static byte[]		createFrpcInt(byte type, long value)
	{
		int len;
		for (len = 7; len < 63; len += 8) {
			if ((value>>len)+1 <= 1)
				break;
		}
		byte[] data = new byte[2+len/8];
		data[0] = (byte)(type|(len/8));
		for (++len; (len -= 8) >= 0; ) {
			data[1+len/8] = (byte)(value>>len);
		}
		return data;
	}

	public BinaryWriter		writeFrpcInt(long value)
	{
		byte[] data = createFrpcInt((byte)0x08, value);
		writeDirect(data);
		return this;
	}

	public BinaryWriter		writeFrpcDouble(double value)
	{
		this.writeDirect(new byte[]{ 0x18 });
		this.writeLe64(Double.doubleToLongBits(value));
		return this;
	}

	public BinaryWriter		writeFrpcString(String data)
	{
		byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
		this.writeDirect(createFrpcInt((byte)0x20, bytes.length));
		this.writeDirect(bytes);
		return this;
	}

	public BinaryWriter		writeFrpcBytes(byte[] data)
	{
		this.writeDirect(createFrpcInt((byte)0x30, data.length));
		this.writeDirect(data);
		return this;
	}

	public BinaryWriter		writeDirect(byte[] data)
	{
		content = ByteUtil.concatBytes(this.content, data);
		return this;
	}

	public byte[]			getContent()
	{
		return this.content;
	}

	public byte[]			content = ArrayUtils.EMPTY_BYTE_ARRAY;
}
