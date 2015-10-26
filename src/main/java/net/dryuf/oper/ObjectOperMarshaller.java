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

package net.dryuf.oper;

import java.util.Map;

import net.dryuf.validation.AccessValidationException;
import net.dryuf.validation.DataValidationException;
import net.dryuf.validation.UniqueValidationException;


public interface ObjectOperMarshaller
{
	/**
	 * Preprocesses input.
	 */
	void				preprocessInput(ObjectOperContext context);

	/**
	 * Gets static operation method.
	 */
	ObjectOperMethod		getStaticOperMethod(ObjectOperContext context);

	/**
	 * Gets object operation method.
	 */
	ObjectOperMethod		getObjectOperMethod(ObjectOperContext context);

	/**
	 * Gets generic input data.
	 */
	Map<String, Object>		getInputData(ObjectOperContext context);

	/**
	 * Gets action input data.
	 */
	<T> T				getActionData(ObjectOperContext context, Class<T> actionClass);

	/**
	 * Gets list parameters.
	 */
	ObjectOperContext.ListParams	getListParams(ObjectOperContext context);

	/**
	 * Gets view filter data.
	 */
	<T> T				getViewFilter(ObjectOperContext context, Class<T> filterClass);

	/**
	 * Renders output through operContext.
	 */
	void				outputUnauthorizedException(ObjectOperContext context, AccessValidationException ex);

	/**
	 * Renders output through operContext.
	 */
	void				outputAccessValidationException(ObjectOperContext context, AccessValidationException ex);

	/**
	 * Renders output through operContext.
	 */
	void				outputDataValidationException(ObjectOperContext context, DataValidationException ex);

	/**
	 * Renders output through operContext.
	 */
	void				outputUniqueValidationException(ObjectOperContext context, UniqueValidationException ex);

	/**
	 * Renders output through operContext.
	 */
	void				outputObject(ObjectOperContext context, Object output);
}
