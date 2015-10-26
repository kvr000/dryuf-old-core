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

package net.dryuf.function;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MappedCaller2<CHOOSER, CLAZZ, A0, A1, R> extends java.lang.Object
{
	public R			call(CHOOSER ch, CLAZZ self, A0 a0, A1 a1) throws Exception
	{
		Callable3<CLAZZ, A0, A1, R> method = methodMap.get(ch);
		if (method == null && (method = defaultMethod) == null)
			throw new IllegalArgumentException("Unknown object passed: "+ch.getClass().getName());
		return method.call(self, a0, a1);
	}

	public static class Builder<CHOOSER, CL, A0, A1, R>
	{
		public Builder<CHOOSER, CL, A0, A1, R> put(CHOOSER ch, Callable3<CL, A0, A1, R> method)
		{
			methodMapBuilder.put(ch, method);
			return this;
		}

		public Builder<CHOOSER, CL, A0, A1, R> setDefault(Callable3<CL, A0, A1, R> defaultMethod)
		{
			this.defaultMethod = defaultMethod;
			return this;
		}

		public MappedCaller2<CHOOSER, CL, A0, A1, R> build()
		{
			MappedCaller2<CHOOSER, CL, A0, A1, R> built = new MappedCaller2<>();
			built.methodMap = this.methodMapBuilder.build();
			built.defaultMethod = this.defaultMethod;
			return built;
		}

		protected ImmutableMap.Builder<CHOOSER, Callable3<CL, A0, A1, R>> methodMapBuilder = new ImmutableMap.Builder<>();

		protected Callable3<CL, A0, A1, R> defaultMethod;
	}

	protected Map<CHOOSER, Callable3<CLAZZ, A0, A1, R>> methodMap;

	protected Callable3<CLAZZ, A0, A1, R> defaultMethod;
}
