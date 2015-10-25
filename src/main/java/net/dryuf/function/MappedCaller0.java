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
import java.util.concurrent.Callable;

public class MappedCaller0<CHOOSER, CLAZZ, R> extends java.lang.Object
{
	public R			call(CHOOSER ch, CLAZZ self) throws Exception
	{
		Callable1<CLAZZ, R> method = methodMap.get(ch);
		if (method == null && (method = defaultMethod) == null)
			throw new IllegalArgumentException("Unknown object passed: "+ch.getClass().getName());
		return method.call(self);
	}

	public static class Builder<CHOOSER, CLAZZ, R>
	{
		public Builder<CHOOSER, CLAZZ, R> put(CHOOSER ch, Callable1<CLAZZ, R> method)
		{
			methodMapBuilder.put(ch, method);
			return this;
		}

		public Builder<CHOOSER, CLAZZ, R> setDefault(Callable1<CLAZZ, R> defaultMethod)
		{
			this.defaultMethod = defaultMethod;
			return this;
		}

		public MappedCaller0<CHOOSER, CLAZZ, R> build()
		{
			MappedCaller0<CHOOSER, CLAZZ, R> built = new MappedCaller0<>();
			built.methodMap = this.methodMapBuilder.build();
			built.defaultMethod = this.defaultMethod;
			return built;
		}

		protected ImmutableMap.Builder<CHOOSER, Callable1<CLAZZ, R>> methodMapBuilder = new ImmutableMap.Builder<>();

		protected Callable1<CLAZZ, R> defaultMethod;
	}

	protected Map<CHOOSER, Callable1<CLAZZ, R>> methodMap;

	protected Callable1<CLAZZ, R> defaultMethod;
}
