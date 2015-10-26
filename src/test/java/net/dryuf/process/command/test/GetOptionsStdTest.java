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

package net.dryuf.process.command.test;

import java.util.LinkedHashMap;
import java.util.Map;

import net.dryuf.tenv.AppTenvObject;
import net.dryuf.util.CollectionUtil;
import net.dryuf.util.MapUtil;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.dryuf.core.Dryuf;
import net.dryuf.core.Textual;
import net.dryuf.process.command.GetOptionsStd;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class GetOptionsStdTest extends AppTenvObject
{
	@org.junit.Test
	public void			testWrongIndicator()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("i", (Class<Textual<?>>)null));
		Assert.assertNotNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-v" }));
	}

	@org.junit.Test
	public void			testRequiredValue()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("f", net.dryuf.textual.LongTextual.class));
		Assert.assertNotNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-f" }));
	}

	@org.junit.Test
	public void			testUnexpectedParameter()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("i", (Class<Textual<?>>)null));
		Assert.assertNotNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-i", "parameter" }));
	}

	@org.junit.Test
	public void			testMandatories()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class))
			.setMandatories(CollectionUtil.createHashSet("n"));
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-n", "20" }));
	}

	@org.junit.Test
	public void			testUnsatisfiedMandatories()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class, "i", null))
			.setMandatories(CollectionUtil.createHashSet("n"));
		Assert.assertNotNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-i" }));
	}

	@org.junit.Test
	public void			testIndicator()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("i", (Class<Textual<?>>)null));
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-i" }));
		Assert.assertTrue((Boolean)options.get("i"));
	}

	@org.junit.Test
	public void			testTextual()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class));
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-n", "20" }));
		Assert.assertEquals(20, options.get("n"));
	}

	@org.junit.Test
	public void			testWrongTextual()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class));
		Assert.assertNotNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-n", "a" }));
	}

	@org.junit.Test
	public void			testParameter()
	{
		Map<String, Object> options;
		options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class))
			.setMaxParameters(1);
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "param1" }));
		Assert.assertEquals(1, ((String[])options.get("")).length);
	}

	@org.junit.Test
	public void			testMultiParameters()
	{
		Map<String, Object> options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class))
			.setMaxParameters(2);
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "-n", "1", "param1", "param2" }));
		Assert.assertEquals(2, ((String[])options.get("")).length);
	}

	@org.junit.Test
	public void			testOptionEnd()
	{
		Map<String, Object> options = new LinkedHashMap<String, Object>();
		GetOptionsStd getOpt = new GetOptionsStd()
			.setDefinition(MapUtil.createHashMap("n", net.dryuf.textual.IntegerTextual.class))
			.setMaxParameters(4);
		Assert.assertNull(getOpt.parseArguments(createCallerContext(), options, new String[]{ "--", "-n", "1" }));
		Assert.assertEquals(2, ((String[])options.get("")).length);
	}
}
