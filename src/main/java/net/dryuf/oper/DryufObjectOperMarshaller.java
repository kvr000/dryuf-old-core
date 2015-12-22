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

import net.dryuf.textual.TextualManager;
import net.dryuf.serialize.DataMarshaller;
import net.dryuf.srvui.Response;
import net.dryuf.util.MapUtil;
import net.dryuf.validation.AccessValidationException;
import net.dryuf.validation.DataValidationException;
import net.dryuf.validation.UniqueValidationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import java.nio.charset.StandardCharsets;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.validation.ObjectRoleUtil;
import net.dryuf.srvui.Request;
import net.dryuf.validation.DataValidatorUtil;
import net.dryuf.oper.ObjectOperController.EntityContainer;
import net.dryuf.oper.ObjectOperController.ErrorContainer;
import net.dryuf.oper.ObjectOperController.ListContainer;
import net.dryuf.oper.ObjectOperController.SimpleListContainer;
import net.dryuf.oper.ObjectOperController.StringContainer;
import net.dryuf.oper.ObjectOperController.SuccessContainer;
import net.dryuf.oper.ObjectOperContext.ListParams;


public class DryufObjectOperMarshaller extends java.lang.Object implements ObjectOperMarshaller
{
	public Object			prepareContent(Object content)
	{
		if (content == null) {
			return null;
		}
		else if (content instanceof EntityHolder) {
			EntityHolder<?> eh = (EntityHolder<?>)content;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("entity", ObjectRoleUtil.getWithRole(eh.getEntity(), eh.getRole()));
			map.put("role", eh.getRole().getRoles());
			map.put("view", eh.getView());
			return map;
		}
		else if (content instanceof CallerContext) {
			CallerContext role = (CallerContext)content;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("role", role.getRoles());
			return map;
		}
		else if (content instanceof List) {
			List<?> list = (List<?>)content;
			List<Object> olist = new LinkedList<Object>();
			for (Object o: list) {
				olist.add(prepareContent(o));
			}
			return olist;
		}
		else if (content instanceof SimpleListContainer) {
			SimpleListContainer<?> lc = (SimpleListContainer<?>)content;
			return lc.entities;
		}
		else if (content instanceof ListContainer) {
			ListContainer<?> lc = (ListContainer<?>)content;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("total", lc.total);
			map.put("objects", prepareContent(lc.objects));
			return map;
		}
		else if (content instanceof EntityContainer) {
			EntityContainer<?> ec = (EntityContainer<?>)content;
			return prepareContent(ec.entityHolder);
		}
		else if (content instanceof SuccessContainer) {
			SuccessContainer sc = (SuccessContainer)content;
			return sc.result;
		}
		else if (content instanceof StringContainer) {
			StringContainer sc = (StringContainer)content;
			return sc.content;
		}
		else if (content instanceof ErrorContainer) {
			ErrorContainer sc = (ErrorContainer)content;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			Map<String, Object> errors = new LinkedHashMap<String, Object>();
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
			throw new UnsupportedOperationException("cannot marshal unsafe object "+content.getClass().getName());
		}
	}

	@Override
	public void			outputObject(ObjectOperContext operContext, Object output)
	{
		DataMarshaller marshaller = marshallers.get("json");
		try {
			operContext.getResponse().setContentType(marshaller.getMimeType());
			marshaller.marshal(operContext.getResponse().getOutputStream(), prepareContent(output));
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
		Response response = operContext.getResponse();
		response.sendError(401, null);
		response.sendHeader("x-dryuf-conflict-cause", "UnauthorizedException");
	}

	@Override
	public void			outputAccessValidationException(ObjectOperContext operContext, AccessValidationException ex)
	{
		Response response = operContext.getResponse();
		response.sendError(409, "Access Denied");
		response.sendHeader("x-dryuf-conflict-cause", "AccessValidationException");
		response.sendHeader("x-dryuf-conflict-description", ex.toString());
	}

	@Override
	public void			outputDataValidationException(ObjectOperContext operContext, DataValidationException ex)
	{
		Response response = operContext.getResponse();
		response.sendError(409, "Data Rejected");
		response.sendHeader("x-dryuf-conflict-cause", "DataValidationException");
	}

	@Override
	public void			outputUniqueValidationException(ObjectOperContext operContext, UniqueValidationException ex)
	{
		Response response = operContext.getResponse();
		response.sendError(409, "Unique Violation");
		response.sendHeader("x-dryuf-conflict-cause", "UniqueValidationException");
		response.sendHeader("x-dryuf-conflict-description", ex.toString());
		response.sendHeader("x-dryuf-conflict-unique-key", ex.getDataClass()+"-"+ex.getConstraintName());
	}

	@Override
	public void			preprocessInput(ObjectOperContext operContext)
	{
		/*
		Request request = operContext.getRequest();
		String smethod = request.getParam("method");
		inputMethod = operMap.get(smethod);
		if (inputMethod == null) {
			if (smethod.equals("formaction")) {
				inputMethod = ObjectOperMethod.ACTION;
				inputStream = new ByteArrayInputStream(request.getParam("_arg").getBytes(StandardCharsets.UTF_8));
			}
			else {
				throw new RuntimeException("unknown method: "+smethod);
			}
		}
		else {
			inputStream = request.getInputStream();
		}
		*/
	}

	@SuppressWarnings("fallthrough")
	@Override
	public ObjectOperMethod		getStaticOperMethod(ObjectOperContext operContext)
	{
		Request request = operContext.getRequest();

		String method = request.getMethod();
		if (method.equals("POST") && request.getParam("_method") != null) {
			method = request.getParam("_method");
		}

		ObjectOperMethod operation = null;
		String specmethodString = request.getParam("_operation");
		if (specmethodString != null) {
			if ((operation = operMap.get(specmethodString)) == null)
				throw new RuntimeException("operation unsupported: "+specmethodString);
		}

		switch (method) {
		case "GET":
			if (operation != null) {
				switch (operation) {
				case LIST:
					operContext.setHaveData(1);
					// fall through
				case META:
				case ROLE:
					return operation;
				default:
					throw new RuntimeException("unexpected operation for static oper: "+operation);
				}
			}
			return ObjectOperMethod.LIST;

		case "POST":
		case "PUT":
			if (operation != null) {
				switch (operation) {
				case LIST:
				case SUGGEST:
				case CREATE:
					operContext.setHaveData(1);
					// fall through
				case META:
				case ROLE:
					return operation;
				default:
					throw new RuntimeException("unknown operation: "+operation);
				}
			}
			operContext.setHaveData(1);
			return ObjectOperMethod.CREATE;

		default:
			throw new UnsupportedOperationException("unknown static operation used: "+method);
		}
	}

	@Override
	public ObjectOperMethod		getObjectOperMethod(ObjectOperContext operContext)
	{
		Request request = operContext.getRequest();

		String method = request.getMethod();
		if (method.equals("POST") && request.getParam("_method") != null) {
			method = request.getParam("_method");
		}

		ObjectOperMethod operation = null;
		String specmethodString = request.getParam("_operation");
		if (specmethodString != null) {
			if ((operation = operMap.get(specmethodString)) == null)
				throw new RuntimeException("operation unsupported: "+specmethodString);
		}

		switch (method) {
		case "GET":
			return ObjectOperMethod.RETRIEVE;

		case "PUT":
		case "PATCH":
			operContext.setHaveData(1);
			return ObjectOperMethod.UPDATE;

		case "POST":
			if (operation != null) {
				switch (operation) {
				case RETRIEVE:
					return operation;
				default:
					throw new RuntimeException("unexpected operation for static oper: "+operation);
				}
			}
			throw new RuntimeException("unexpected object oper method: "+method+", operation empty");

		case "DELETE":
			return ObjectOperMethod.DELETE;

		default:
			throw new UnsupportedOperationException("unexpected object oper method: "+method);
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

	@SuppressWarnings("unchecked")
	@Override
	public ListParams		getListParams(ObjectOperContext operContext)
	{
		ListParams listParams = new ListParams();
		listParams.setOffset(operContext.getRequest().getTextual("_offset", TextualManager.createTextual(net.dryuf.textual.LongTextual.class, operContext.getCallerContext())));
		listParams.setLimit(operContext.getRequest().getTextual("_limit", TextualManager.createTextual(net.dryuf.textual.LongTextual.class, operContext.getCallerContext())));
		listParams.setFilters((Map<String, Object>) operContext.getInputData().get("_filters"));
		listParams.setSorts((List<String>) operContext.getInputData().get("_sorts"));
		return listParams;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T			getViewFilter(ObjectOperContext operContext, Class<T> filterClass)
	{
		T filter = Dryuf.createClassArg0(filterClass);
		DataValidatorUtil.validateWithNew(operContext.getCallerContext(), filter, (Map<String, Object>) MapUtil.getMapMandatory(operContext.getInputData(), "viewFilter"));
		return filter;
	}

	public void			setMarshallers(Map<String, DataMarshaller> marshallers)
	{
		this.marshallers = marshallers;
	}

	protected Map<String, DataMarshaller> marshallers;

	protected static final Map<String, ObjectOperMethod> operMap = MapUtil.createHashMap(
			"meta",				ObjectOperMethod.META,
			"create",			ObjectOperMethod.CREATE,
			"retrieve",			ObjectOperMethod.RETRIEVE,
			"update",			ObjectOperMethod.UPDATE,
			"delete",			ObjectOperMethod.DELETE,
			"list",				ObjectOperMethod.LIST,
			"role",				ObjectOperMethod.ROLE,
			"action",			ObjectOperMethod.ACTION,
			"suggest",			ObjectOperMethod.SUGGEST
			);
}
