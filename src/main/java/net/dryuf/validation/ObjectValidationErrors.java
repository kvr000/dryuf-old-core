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

package net.dryuf.validation;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectValidationErrors extends java.lang.Object implements DataValidationErrors
{
	public 				ObjectValidationErrors(Object obj)
	{
		this.obj = obj;
	}

	public Map<String, String>	listFieldsErrors()
	{
		return fieldsErrors;
	}

	public String			getFieldError(String fieldName)
	{
		return fieldsErrors.get(fieldName);
	}

	public List<String>		listGlobalErrors()
	{
		return globalErrors;
	}

	public void			rejectField(String fieldName, String message)
	{
		fieldName = getFullFieldPath(fieldName);
		if (!fieldsErrors.containsKey(fieldName))
			fieldsErrors.put(fieldName, message);
	}

	public void			rejectGlobal(String message)
	{
		globalErrors.add(message);
	}

	public void			pushNestedPath(String path)
	{
		this.path = this.path != null ? this.path+"."+path : path;
	}

	public void			popNestedPath()
	{
		int p = this.path.lastIndexOf('.');
		if (p < 0) {
			this.path = null;
		}
		else {
			this.path = this.path.substring(0, p);
		}
	}

	public boolean			hasErrors()
	{
		return !fieldsErrors.isEmpty() || !globalErrors.isEmpty();
	}

	private String			getFullFieldPath(String fieldName)
	{
		return this.path != null ? this.path+"."+fieldName : fieldName;
	}

	private Map<String, String>	fieldsErrors = new LinkedHashMap<String, String>();

	private List<String>		globalErrors = new LinkedList<String>();

	private Object			obj;

	private String			path;
}
