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

package net.dryuf.srvui;

import net.dryuf.core.Textual;


public abstract class AbstractRequest extends java.lang.Object implements Request
{
	@Override
	public String			getParamMandatory(String param)
	{
		String value;
		if ((value = getParam(param)) == null)
			throw new net.dryuf.core.ReportException("param "+param+" not found");
		return value;
	}

	@Override
	public String			getParamDefault(String param, String defaultValue)
	{
		String value = getParam(param);
		return value == null ? defaultValue : value;
	}

	@Override
	public <T> T			getTextual(String param, Textual<T> textual)
	{
		String value;
		if ((value = getParam(param)) == null)
			return null;
		try {
			return textual.convert(value, null);
		}
		catch (Exception ex) {
			throw new RuntimeException("failed to convert param "+param+": "+ex.getMessage(), ex);
		}
	}

	@Override
	public <T> T			getTextualDefault(String param, Textual<T> textual, T defaultValue)
	{
		String value;
		if ((value = getParam(param)) == null)
			return defaultValue;
		try {
			return textual.convert(value, null);
		}
		catch (Exception ex) {
			throw new RuntimeException("failed to convert param "+param+": "+ex.getMessage(), ex);
		}
	}

	@Override
	public <T> T			getTextualMandatory(String param, Textual<T> textual)
	{
		String value = this.getParamMandatory(param);
		try {
			return textual.convert(value, null);
		}
		catch (Exception ex) {
			throw new RuntimeException("failed to convert param "+param+": "+ex.getMessage(), ex);
		}
	}
}
