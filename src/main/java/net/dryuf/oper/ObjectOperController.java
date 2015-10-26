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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.validation.Errors;
import net.dryuf.core.CallerContext;
import net.dryuf.core.EntityHolder;


public interface ObjectOperController<T>
{
	public Class<T>			getDataClass();

	public Object			operate(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public String[]			getObjectId(ObjectOperContext context);

	public Object			operateStaticCommon(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public RoleContainer		operateStaticRole(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public String			operateStaticMeta(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public Object			operateStaticList(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public Object			operateStaticSuggest(ObjectOperContext context, EntityHolder<?> ownerHolder);

	public Object			operateStaticCreate(ObjectOperContext context, EntityHolder<?> ownerHolder, Map<String, Object> readValue);

	public Object			operateObjectCommon(ObjectOperContext context, EntityHolder<?> ownerHolder, String objectId[]);

	public Object			operateObjectRetrieve(ObjectOperContext context, EntityHolder<T> objectHolder);

	public Object			operateObjectUpdate(ObjectOperContext context, EntityHolder<T> objectHolder, Map<String, Object> data);

	public Object			operateObjectDelete(ObjectOperContext context, EntityHolder<T> objectHolder);

	public static class RoleContainer extends java.lang.Object
	{
		public CallerContext		roleContext;

		public				RoleContainer(CallerContext roleContext)
		{
			this.roleContext = roleContext;
		}
	}

	public static class ListContainer<T> extends java.lang.Object
	{
		public long			total;
		public List<EntityHolder<T>>	objects = new LinkedList<EntityHolder<T>>();

		public static <T> ListContainer<T> createFromList(CallerContext callerContext, List<T> entities)
		{
			ListContainer<T> me = new ListContainer<T>();
			for (T e: entities)
				me.objects.add(new EntityHolder<T>(e, callerContext));
			return me;
		}
	}

	public static class SimpleListContainer<T> extends java.lang.Object
	{
		public List<T>			entities;

		public				SimpleListContainer(List<T> entities)
		{
			this.entities = entities;
		}
	}

	public static class EntityContainer<T> extends java.lang.Object
	{
		public EntityHolder<T>		entityHolder;

		public				EntityContainer(EntityHolder<T> entityHolder)
		{
			this.entityHolder = entityHolder;
		}
	}

	public static class SuccessContainer extends java.lang.Object
	{
		public boolean			result;

		public				SuccessContainer(boolean result)
		{
			this.result = result;
		}

		public static SuccessContainer	getOk()
		{
			return new SuccessContainer(true);
		}

		public static SuccessContainer	getFailed()
		{
			return new SuccessContainer(false);
		}
	}

	public static class ErrorContainer extends java.lang.Object
	{
		public Errors			errors;
		public String			message;

		public				ErrorContainer(String message, Errors errors)
		{
			this.errors = errors;
		}
	}

	public static class StringContainer extends java.lang.Object
	{
		public String			content;

		public				StringContainer(String content)
		{
			this.content = content;
		}
	}

	public static class SkipContainer extends java.lang.Object
	{
	}
}
