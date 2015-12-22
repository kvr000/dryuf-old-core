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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dryuf.util.MapUtil;
import java.nio.charset.StandardCharsets;

import net.dryuf.textual.TextualManager;
import net.dryuf.serialize.DataMarshaller;
import net.dryuf.srvui.Request;
import net.dryuf.validation.AccessValidationException;
import net.dryuf.validation.DataValidationException;
import net.dryuf.validation.UniqueValidationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.validation.ObjectRoleUtil;
import net.dryuf.validation.DataValidatorUtil;
import net.dryuf.oper.ObjectOperController.EntityContainer;
import net.dryuf.oper.ObjectOperController.ErrorContainer;
import net.dryuf.oper.ObjectOperController.ListContainer;
import net.dryuf.oper.ObjectOperController.SimpleListContainer;
import net.dryuf.oper.ObjectOperController.SuccessContainer;
import net.dryuf.oper.ObjectOperContext.ListParams;


public class RestObjectOperMarshaller extends java.lang.Object implements ObjectOperMarshaller
{
	public Object			prepareContent(ObjectOperContext operContext, Object content)
	{
		if (content == null) {
			return null;
		}
		else if (content instanceof EntityHolder) {
			EntityHolder<?> eh = (EntityHolder<?>)content;
			return ObjectRoleUtil.getWithRole(eh.getEntity(), eh.getRole());
		}
		else if (content instanceof CallerContext) {
			return null;
		}
		else if (content instanceof List) {
			List<?> list = (List<?>)content;
			List<Object> olist = new LinkedList<Object>();
			for (Object o: list) {
				olist.add(prepareContent(operContext, o));
			}
			return olist;
		}
		else if (content instanceof SimpleListContainer) {
			SimpleListContainer<?> lc = (SimpleListContainer<?>)content;
			return lc.entities;
		}
		else if (content instanceof ListContainer) {
			ListContainer<?> lc = (ListContainer<?>) content;
			return prepareContent(operContext, lc.objects);
		}
		else if (content instanceof EntityContainer) {
			EntityContainer<?> ec = (EntityContainer<?>)content;
			return prepareContent(operContext, ec.entityHolder);
		}
		else if (content instanceof SuccessContainer) {
			SuccessContainer sc = (SuccessContainer)content;
			return sc.result;
		}
		else if (content instanceof ErrorContainer) {
			ErrorContainer sc = (ErrorContainer)content;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			operContext.getResponse().sendError(412, "parameter errors");
			if (sc.errors.getFieldErrorCount() != 0) {
				map.put("_error", 412);
				for (FieldError fieldError: sc.errors.getFieldErrors()) {
					errors.put(fieldError.getField(), fieldError.getDefaultMessage());
				}
			}
			else {
				map.put("_error", 413);
				List<String> list = new LinkedList<String>();
				map.put("globals", list);
				for (ObjectError objectError: sc.errors.getGlobalErrors()) {
					list.add(objectError.getDefaultMessage());
				}

			}
			map.put("_errors", errors);
			return map;
		}
		else {
			return content;
			//throw new UnsupportedOperationException("Cannot marshal unsafe object "+content.getClass().getName());
		}
	}

	@Override
	public void			outputObject(ObjectOperContext operContext, Object output)
	{
		DataMarshaller marshaller = marshallers.get("json");
		try {
			operContext.getResponse().setContentType(marshaller.getMimeType());
			marshaller.marshal(operContext.getResponse().getOutputStream(), prepareContent(operContext, output));
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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
		switch (operContext.getRequest().getMethod()) {
		case "GET":
			return ObjectOperMethod.LIST;

		case "POST":
		case "PUT":
			return ObjectOperMethod.CREATE;

		default:
			throw new UnsupportedOperationException("unknown static operation used: "+operContext.getRequest().getMethod());
		}
	}

	@Override
	public ObjectOperMethod		getObjectOperMethod(ObjectOperContext operContext)
	{
		switch (operContext.getRequest().getMethod()) {
		case "GET":
			return ObjectOperMethod.RETRIEVE;

		case "POST":
		case "PUT":
		case "PATCH":
			return ObjectOperMethod.UPDATE;

		case "DELETE":
			return ObjectOperMethod.DELETE;
		default:
			throw new UnsupportedOperationException("unknown object operation used: "+operContext.getRequest().getMethod());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object>	getInputData(ObjectOperContext operContext)
	{
		Request request = operContext.getRequest();

		if (operContext.getHaveData() == 0)
			return new HashMap<String, Object>();
		InputStream inputStream;
		if (request.getRequestContentType().equals("multipart/form-data")) {
			inputStream = new ByteArrayInputStream(request.getParam("_arg").getBytes(StandardCharsets.UTF_8));
		}
		else {
			inputStream = request.getInputStream();
		}

		try {
			DataMarshaller marshaller = marshallers.get("json");
			return (Map<String, Object>)marshaller.unmarshal(inputStream, Map.class);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T			getActionData(ObjectOperContext operContext, Class<T> actionClass)
	{
		T actionData = Dryuf.createClassArg0(actionClass);
		DataValidatorUtil.validateWithNew(operContext.getCallerContext(), actionData, (Map<String, Object>) MapUtil.getMapMandatory(operContext.getInputData(), "actionData"));
		return actionData;
	}

	@Override
	public ListParams		getListParams(ObjectOperContext operContext)
	{
		ListParams listParams = new ListParams();
		listParams.setOffset(operContext.getRequest().getTextual("_offset", TextualManager.createTextual(net.dryuf.textual.LongTextual.class, operContext.getCallerContext())));
		listParams.setLimit(operContext.getRequest().getTextual("_limit", TextualManager.createTextual(net.dryuf.textual.LongTextual.class, operContext.getCallerContext())));
		//listParams.setFilters((Map<String, Object>) operContext.getInputData().get("_filters"));
		//listParams.setSorts((List<String>) operContext.getInputData().get("_sorts"));
		return listParams;
	}

	@Override
	public <T> T			getViewFilter(ObjectOperContext operContext, Class<T> filterClass)
	{
		return null;
	}

	public void			setMarshallers(Map<String, DataMarshaller> marshallers)
	{
		this.marshallers = marshallers;
	}

	protected Map<String, DataMarshaller> marshallers;
}
