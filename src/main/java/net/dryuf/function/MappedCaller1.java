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

public class MappedCaller1<CHOOSER, CLAZZ, A0, R> extends java.lang.Object
{
	public R			call(CHOOSER ch, CLAZZ self, A0 a0) throws Exception
	{
		Callable2<CLAZZ, A0, R> method = methodMap.get(ch);
		if (method == null && (method = defaultMethod) == null)
			throw new IllegalArgumentException("Unknown object passed: "+ch.getClass().getName());
		return method.call(self, a0);
	}

	public static class Builder<CHOOSER, CLAZZ, A0, R>
	{
		public Builder<CHOOSER, CLAZZ, A0, R> put(CHOOSER ch, Callable2<CLAZZ, A0, R> method)
		{
			methodMapBuilder.put(ch, method);
			return this;
		}

		public Builder<CHOOSER, CLAZZ, A0, R> setDefault(Callable2<CLAZZ, A0, R> defaultMethod)
		{
			this.defaultMethod = defaultMethod;
			return this;
		}

		public MappedCaller1<CHOOSER, CLAZZ, A0, R> build()
		{
			MappedCaller1<CHOOSER, CLAZZ, A0, R> built = new MappedCaller1<>();
			built.methodMap = this.methodMapBuilder.build();
			built.defaultMethod = this.defaultMethod;
			return built;
		}

		protected ImmutableMap.Builder<CHOOSER, Callable2<CLAZZ, A0, R>> methodMapBuilder = new ImmutableMap.Builder<>();

		protected Callable2<CLAZZ, A0, R> defaultMethod;
	}

	protected Map<CHOOSER, Callable2<CLAZZ, A0, R>> methodMap;

	protected Callable2<CLAZZ, A0, R> defaultMethod;
}
