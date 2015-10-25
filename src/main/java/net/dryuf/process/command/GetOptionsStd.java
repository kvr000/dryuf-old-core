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

package net.dryuf.process.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Textual;
import net.dryuf.textual.TextualManager;


public class GetOptionsStd extends java.lang.Object implements GetOptions
{
	public				GetOptionsStd()
	{
	}

	public GetOptionsStd		setDefinition(Map<String, ?> map)
	{
		this.optionsDefinition = map;
		return this;
	}

	public GetOptionsStd		setMandatories(Set<String> mandatories)
	{
		this.mandatories = mandatories;
		return this;
	}

	public GetOptionsStd		setMinParameters(int minParameters)
	{
		this.minParameters = minParameters;
		return this;
	}

	public GetOptionsStd		setMaxParameters(int maxParameters)
	{
		this.maxParameters = maxParameters;
		return this;
	}

	public String			parseArguments(CallerContext callerContext, Map<String, Object> options, String[] args)
	{
		if (optionsDefinition == null)
			optionsDefinition = new HashMap<String, Object>();
		if (mandatories == null)
			mandatories = new HashSet<String>();

		int i;
		Set<String> appeared = new HashSet<String>();
		for (i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (args[i].equals("--")) {
					i++;
					break;
				}
				if (args[i].equals("-"))
					return "missing option string: "+args[i]+"\n";

				String option = args[i].substring(1);
				if (!optionsDefinition.containsKey(option))
					return "unknown argument: "+option+"\n";
				if (appeared.contains(option))
					return "option "+option+" already specified\n";
				Object textualClassName = optionsDefinition.get(option);
				if (textualClassName == null) {
					options.put(option, true);
				}
				else {
					Textual<?> textual;
					try {
						@SuppressWarnings("unchecked")
						Class<? extends Textual<?>> textualClass = (Class<? extends Textual<?>>)(textualClassName instanceof Class ? (Class<?>)textualClassName : Class.forName((String)optionsDefinition.get(option)));
						textual = TextualManager.createTextualUnsafe(textualClass, callerContext);
					}
					catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					if (++i >= args.length)
						return "option "+args[i-1]+" expects a value\n";
					String value = textual.prepare(args[i], null);
					String error;
					if ((error = textual.check(value, null)) != null)
						return "invalid option value for "+args[i]+": "+error+"\n";
					options.put(option, textual.convert(value, null));
				}
				appeared.add(option);
			}
			else {
				break;
			}
		}

		for (String name: mandatories) {
			if (!appeared.contains(name))
				return "Option "+name+" is mandatory\n";
		}

		if (args.length-i < minParameters)
			return "Expected at least "+maxParameters+" arguments\n";
		if (args.length-i > maxParameters)
			return "Expected at most "+maxParameters+" arguments\n";
		options.put("", Arrays.copyOfRange(args, i, args.length));
		return null;
	}

	protected int			minParameters = 0;

	protected int			maxParameters = 0;

	protected Map<String, ?>	optionsDefinition;

	protected Set<String>		mandatories;
}
