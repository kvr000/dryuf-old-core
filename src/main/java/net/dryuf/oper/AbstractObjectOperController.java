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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.reflections.ReflectionUtils;

import com.google.common.base.Predicate;

import net.dryuf.core.CallerContext;
import net.dryuf.core.Dryuf;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.InvalidValueException;
import net.dryuf.core.AppContainer;
import net.dryuf.core.AppContainerAware;
import net.dryuf.srvui.Response;

import javax.validation.constraints.NotNull;


public abstract class AbstractObjectOperController<T> extends java.lang.Object implements ObjectOperController<T>, AppContainerAware
{
	public abstract Class<T>	getDataClass();

	public Object			operate(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		String[] objectId = getObjectId(operContext);

		String actionName;
		if ((actionName = operContext.getPathElement()) == null) {
			return objectId == null ? operateStaticCommon(operContext, ownerHolder) : operateObjectCommon(operContext, ownerHolder, objectId);
		}

		if (operContext.hasError())
			return null;

		Actioner actioner = findActioner(actionName);
		if (actioner == null) {
			operContext.markNotFound();
			return null;
		}

		ObjectOperRules rules = actioner.getOperRules();
		if (rules.isFinal()) {
			// dont be so restrictive and allow final slash at the end of action request
			if (false && !operContext.needPathSlash(false)) {
				return null;
			}
		}
		else {
			if (!operContext.needPathSlash(true)) {
				return null;
			}
		}
		if (rules.isStatic()) {
			return this.operateStaticAction(actioner, rules, operContext, ownerHolder);
		}
		else {
			return this.operateObjectAction(actioner, rules, operContext, ownerHolder, objectId);
		}
	}

	public String[]			getObjectId(ObjectOperContext operContext)
	{
		return getObjectIdList(operContext, 1);
	}

	public String[]			getObjectIdList(ObjectOperContext operContext, int count)
	{
		String[] ids = new String[count];
		for (int i = 0; i < count; i++) {
			if ((ids[i] = operContext.getPathElement()) == null) {
				return null;
			}
			if (!operContext.needPathSlash(true)) {
				return null;
			}
		}
		return ids;
	}

	public Actioner			findActioner(final String actionName)
	{
		@SuppressWarnings("unchecked")
		Set<Method> found = ReflectionUtils.getAllMethods(this.getClass(), new Predicate<Method>() {
			@Override
			public boolean			apply(Method pmethod)
			{
				ObjectOperRules prules = pmethod.getAnnotation(ObjectOperRules.class);
				if (prules == null)
					return false;
				if (!prules.value().equals(actionName))
					return false;
				return true;
			}
		});
		if (found.isEmpty()) {
			return null;
		}
		if (found.size() > 1)
			throw new InvalidValueException(found, "more than one method found handling the action: "+actionName);
		final Method method = found.iterator().next();
		final ObjectOperRules rules = method.getAnnotation(ObjectOperRules.class);
		return new Actioner() {
			@Override
			public String			getActionName()
			{
				return actionName;
			}
			@Override
			public ObjectOperRules		getOperRules()
			{
				return rules;
			}
			@Override
			public Object			runAction(AbstractObjectOperController<?> controller, ObjectOperContext operContext, EntityHolder<?> ownerHolder)
			{
				if (rules.actionClass() != void.class) {
					return Dryuf.invokeMethod(AbstractObjectOperController.this, method, operContext, ownerHolder, operContext.getActionData(rules.actionClass()));
				}
				else {
					return Dryuf.invokeMethod(AbstractObjectOperController.this, method, operContext, ownerHolder);
				}
			}
		};
	}

	protected void			keepContextTransaction(CallerContext callerContext)
	{
	}

	protected EntityHolder<T>	loadObject(EntityHolder<?> ownerHolder, String[] objectId)
	{
		throw new UnsupportedOperationException(getClass().getName()+".loadObject");
	}

	public Map<String, Object>	getInputData(ObjectOperContext operContext)
	{
		return operContext.getInputData();
	}

	public Object			operateStaticCommon(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		switch (operContext.getStaticOperMethod()) {
		case META:
			Response response = operContext.getResponse();
			response.setContentType("text/xml; charset=UTF-8");
			try {
				IOUtils.write(operateStaticMeta(operContext, ownerHolder), response.getOutputStream(), "UTF-8");
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
			return new SkipContainer();

		case ROLE:
			return ownerHolder.getRole();

		case CREATE:
			return operateStaticCreate(operContext, ownerHolder, getInputData(operContext));

		case LIST:
			return operateStaticList(operContext, ownerHolder);

		case SUGGEST:
			return operateStaticSuggest(operContext, ownerHolder);

		default:
			throw new UnsupportedOperationException("unknown method used on object tree root: "+operContext.getStaticOperMethod());
		}
	}

	@Override
	public RoleContainer		operateStaticRole(net.dryuf.oper.ObjectOperContext operContext, net.dryuf.core.EntityHolder<?> ownerHolder)
	{
		return new RoleContainer(ownerHolder.getRole());
	}

	public String			operateStaticMeta(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		throw new UnsupportedOperationException("unknown method used on object tree root: meta");
	}

	public Object			operateStaticList(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		throw new UnsupportedOperationException("unknown method used on object tree root: list");
	}

	public Object			operateStaticSuggest(ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		throw new UnsupportedOperationException("unknown method used on object tree root: suggest");
	}

	public Object			operateStaticCreate(ObjectOperContext operContext, EntityHolder<?> ownerHolder, Map<String, Object> readValue)
	{
		throw new UnsupportedOperationException("unknown method used on object tree root: create");
	}

	public Object			operateObjectCommon(ObjectOperContext operContext, EntityHolder<?> ownerHolder, String objectId[])
	{
		if (ownerHolder == null)
			return null;
		switch (operContext.getObjectOperMethod()) {
		case CREATE:
		case UPDATE:
			EntityHolder<T> objectHolder = loadObject(ownerHolder, objectId);
			if (objectHolder == null)
				return null;
			return operateObjectUpdate(operContext, objectHolder, getInputData(operContext));

		case RETRIEVE:
			keepContextTransaction(ownerHolder.getRole());
			EntityHolder<T> retrievedHolder = loadObject(ownerHolder, objectId);
			if (retrievedHolder == null)
				return null;
			return operateObjectRetrieve(operContext, retrievedHolder);

		case DELETE:
			EntityHolder<T> deletingHolder = loadObject(ownerHolder, objectId);
			return operateObjectDelete(operContext, deletingHolder);

		default:
			throw new UnsupportedOperationException("unknown method used on object tree root: "+operContext.getObjectOperMethod());
		}
	}

	public Object			operateObjectRetrieve(ObjectOperContext operContext, EntityHolder<T> objectHolder)
	{
		throw new UnsupportedOperationException(getClass().getName()+".operateObjectRetrieve");
	}

	public Object			operateObjectUpdate(ObjectOperContext operContext, EntityHolder<T> objectHolder, Map<String, Object> data)
	{
		throw new UnsupportedOperationException(getClass().getName()+".operateObjectUpdate");
	}

	public Object			operateObjectDelete(ObjectOperContext operContext, EntityHolder<T> objectHolder)
	{
		throw new UnsupportedOperationException(getClass().getName()+".operateObjectDelete");
	}

	public Object			operateStaticAction(Actioner actioner, ObjectOperRules rules, ObjectOperContext operContext, EntityHolder<?> ownerHolder)
	{
		if (!ownerHolder.getRole().checkRole(rules.reqRole())) {
			operContext.markDenied();
			return null;
		}
		return actioner.runAction(this, operContext, ownerHolder);
	}

	public Object			operateObjectAction(Actioner actioner, ObjectOperRules rules, ObjectOperContext operContext, EntityHolder<?> ownerHolder, String[] objectId)
	{
		ownerHolder = loadObject(ownerHolder, objectId);
		if (ownerHolder == null) {
			operContext.markNotFound();
			return null;
		}
		if (!ownerHolder.getRole().checkRole(rules.reqRole())) {
			operContext.markDenied();
			return null;
		}
		return actioner.runAction(this, operContext, ownerHolder);
	}

	@Override
	public void			afterAppContainer(@NotNull AppContainer appContainer)
	{
		this.appContainer = appContainer;
	}

	public static interface Actioner
	{
		public String			getActionName();
		public ObjectOperRules		getOperRules();
		public Object			runAction(AbstractObjectOperController<?> controller, ObjectOperContext operContext, EntityHolder<?> ownerHolder);
	}

	protected AppContainer		appContainer;
}
