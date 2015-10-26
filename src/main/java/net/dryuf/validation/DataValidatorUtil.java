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

import java.util.Map;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.meta.DataValidatorDef;
import net.dryuf.validation.ObjectRoleUtil;


public class DataValidatorUtil extends java.lang.Object
{
	public static void		validateObject(CallerContext role, Object obj)
	{
		DataValidatorDef validatorDef = obj.getClass().getAnnotation(DataValidatorDef.class);
		if (validatorDef == null)
			return;
		DataValidator validator;
		try {
			validator = validatorDef.validator().newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		DataValidationErrors errors = new ObjectValidationErrors(obj);
		if (!validator.validate(role, errors)) {
			throw new DataValidationException(errors);
		}
	}

	public static void		validateWithNew(CallerContext role, Object obj, Map<String, Object> data)
	{
		DataValidationErrors errors = new ObjectValidationErrors(obj);
		if (!ObjectRoleUtil.newWithRole(errors, obj, role, data)) {
			throw new DataValidationException(errors);
		}
		validateObject(role, obj);
	}

	public static void		validateWithSet(CallerContext role, Object obj, Map<String, Object> data)
	{
		DataValidationErrors errors = new ObjectValidationErrors(obj);
		if (!ObjectRoleUtil.setWithRole(errors, obj, role, data)) {
			throw new DataValidationException(errors);
		}
		validateObject(role, obj);
	}

	public static void		throwValidationError(Object obj, String fieldName, String err)
	{
		DataValidationErrors errors = new ObjectValidationErrors(obj);
		errors.rejectField(fieldName, err);
		throw new DataValidationException(errors);
	}

	public static void		throwGlobalError(Object obj, String err)
	{
		DataValidationErrors errors = new ObjectValidationErrors(obj);
		errors.rejectGlobal(err);
		throw new DataValidationException(errors);
	}
};
