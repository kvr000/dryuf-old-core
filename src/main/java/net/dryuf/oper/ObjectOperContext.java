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

import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.dryuf.core.CallerContext;
import net.dryuf.srvui.PageContext;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Response;
import net.dryuf.validation.UniqueValidationException;

import net.dryuf.core.EntityHolder;
import net.dryuf.validation.AccessValidationException;
import net.dryuf.validation.DataValidationException;


public class ObjectOperContext implements AutoCloseable
{
	public				ObjectOperContext(PageContext pageContext, ObjectOperController<?> rootController, EntityHolder<?> ownerHolder)
	{
		Objects.requireNonNull(rootController, "rootController");
		this.pageContext = pageContext;
		this.rootController = rootController;
		this.ownerHolder = ownerHolder;
	}

	public void			close()
	{
		pageContext.close();
	}

	public PageContext		getPageContext()
	{
		return pageContext;
	}

	public CallerContext		getCallerContext()
	{
		return pageContext.getCallerContext();
	}

	public Request getRequest()
	{
		return pageContext.getRequest();
	}

	public Response			getResponse()
	{
		return pageContext.getResponse();
	}

	public ObjectOperContext	setupObjectOperMarshaller(String marshallerName)
	{
		if (marshallerName == null)
			marshallerName = "default";
		this.objectOperMarshaller = pageContext.getCallerContext().getBeanTyped(ObjectOperMarshaller.class.getName()+"-"+marshallerName, ObjectOperMarshaller.class);

		this.objectOperMarshaller.preprocessInput(this);

		return this;
	}

	public boolean			process()
	{
		setupObjectOperMarshaller(this.getRequest().getParamDefault("_servmarsh", "rest"));

		Object result;
		try {
			result = rootController.operate(this, ownerHolder);
			if (result == null)
				return true;
			outputObject(result);
		}
		catch (DataValidationException ex) {
			getObjectOperMarshaller().outputDataValidationException(this, ex);
		}
		catch (AccessValidationException ex) {
			if (getCallerContext().isLoggedIn()) {
				getObjectOperMarshaller().outputAccessValidationException(this, ex);
			}
			else {
				getObjectOperMarshaller().outputUnauthorizedException(this, ex);
			}
		}
		catch (UniqueValidationException ex) {
			getObjectOperMarshaller().outputUniqueValidationException(this, ex);
		}
		return false;
	}

	public Object			operateClass(Class<?> dataClass)
	{
		final ObjectOperController<?> actionController = (ObjectOperController<?>) this.getCallerContext().getBean(dataClass.getName()+"-oper");
		return actionController.operate(this, EntityHolder.createRoleOnly(getCallerContext()));
	}

	public void			outputObject(Object output)
	{
		if (output instanceof ObjectOperController.SkipContainer)
			return;
		getObjectOperMarshaller().outputObject(this, output);
	}

	public void			markError(int errorStatus)
	{
		this.errorStatus = errorStatus;
	}

	public void			markNotFound()
	{
		markError(404);
	}

	public void			markDenied()
	{
		markError(401);
	}

	public boolean			hasError()
	{
		return errorStatus != 0;
	}

	public String			getPathElement()
	{
		return pageContext.getPathElement();
	}

	public boolean			needPathSlash(boolean needSlash)
	{
		if (!pageContext.needPathSlash(needSlash)) {
			markError(302);
			return false;
		}
		return true;
	}

	public ObjectOperMethod		getStaticOperMethod()
	{
		return objectOperMarshaller.getStaticOperMethod(this);
	}

	public ObjectOperMethod		getObjectOperMethod()
	{
		return objectOperMarshaller.getObjectOperMethod(this);
	}

	public Map<String, Object>	getInputData()
	{
		if (this.inputData == null)
			this.inputData = this.objectOperMarshaller.getInputData(this);
		return inputData;
	}

	public int			getHaveData()
	{
		return this.haveData;
	}

	public void			setHaveData(int haveData)
	{
		this.haveData = haveData;
	}

	public <T> T			getActionData(Class<T> actionClass)
	{
		return objectOperMarshaller.getActionData(this, actionClass);
	}

	public ListParams		getListParams()
	{
		return objectOperMarshaller.getListParams(this);
	}

	public <T> T			getViewFilter(Class<T> filterClass)
	{
		return objectOperMarshaller.getViewFilter(this, filterClass);
	}

	public ObjectOperMarshaller	getObjectOperMarshaller()
	{
		return this.objectOperMarshaller;
	}

	protected PageContext		pageContext;

	protected ObjectOperController<?> rootController;

	protected EntityHolder<?>	ownerHolder;

	protected int			errorStatus = 0;

	protected ObjectOperMarshaller	objectOperMarshaller;

	protected Map<String, Object>	inputData;
	protected int			haveData = -1;


	public static class ListParams extends java.lang.Object
	{
		public Long			getOffset()
		{
			return offset;
		}

		public void			setOffset(Long offset)
		{
			this.offset = offset;
		}

		public Long			getLimit()
		{
			return limit;
		}

		public void			setLimit(Long limit)
		{
			this.limit = limit;
		}

		public Map<String, Object>	getFilters()
		{
			return filters;
		}

		public void			setFilters(Map<String, Object> filters)
		{
			this.filters = filters;
		}

		public List<String>		getSorts()
		{
			return sorts;
		}

		public void			setSorts(List<String> sorts)
		{
			this.sorts = sorts;
		}

		private Long			offset = null;
		private Long			limit = null;
		private Map<String, Object>	filters;
		private List<String>		sorts;
	}
}
