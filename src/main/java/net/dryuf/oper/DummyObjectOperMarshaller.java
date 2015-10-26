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
import net.dryuf.oper.ObjectOperContext.ListParams;


public class DummyObjectOperMarshaller extends java.lang.Object implements ObjectOperMarshaller
{
	public Object			prepareContent(Object content)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void			outputObject(ObjectOperContext operContext, Object output)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void			outputUnauthorizedException(ObjectOperContext operContext, AccessValidationException ex)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void			outputAccessValidationException(ObjectOperContext operContext, AccessValidationException ex)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void			outputDataValidationException(ObjectOperContext operContext, DataValidationException ex)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void			outputUniqueValidationException(ObjectOperContext operContext, UniqueValidationException ex)
	{
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void			preprocessInput(ObjectOperContext operContext)
	{
	}

	@Override
	public ObjectOperMethod		getStaticOperMethod(ObjectOperContext operContext)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ObjectOperMethod		getObjectOperMethod(ObjectOperContext operContext)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object>	getInputData(ObjectOperContext operContext)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T			getActionData(ObjectOperContext operContext, Class<T> actionClass)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ListParams		getListParams(ObjectOperContext operContext)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T			getViewFilter(ObjectOperContext operContext, Class<T> filterClass)
	{
		throw new UnsupportedOperationException();
	}
};
