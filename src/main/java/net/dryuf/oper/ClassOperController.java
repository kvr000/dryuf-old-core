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

import java.lang.annotation.Annotation;

import net.dryuf.core.Textual;
import net.dryuf.core.NoSuchBeanException;
import net.dryuf.core.EntityHolder;
import net.dryuf.meta.PKeyDef;


public class ClassOperController extends DummyObjectOperController
{
	@Override
	public String[]			getObjectId(ObjectOperContext operContext)
	{
		return new String[0];
	}

	@Override
	public Actioner			findActioner(final String actionName)
	{
		Actioner actioner;
		if ((actioner = super.findActioner(actionName)) != null)
			return actioner;

		final ObjectOperController<?> actionController;
		try {
			actionController = (ObjectOperController<?>) appContainer.getBean(actionName+"-oper");
		}
		catch (NoSuchBeanException ex) {
			return null;
		}

		return new Actioner() {
			@Override
			public String			getActionName()
			{
				return actionName;
			}

			@Override
			public ObjectOperRules		getOperRules() {
				return new ObjectOperRules() {
					@Override public Class<? extends Annotation> annotationType() { return null; }
					@Override public String value() { return null; }
					@Override public String reqRole() { return ""; }
					@Override public boolean isStatic() { return true; }
					@Override public boolean isFinal() { return false; }
					@SuppressWarnings({"unchecked", "rawtypes"})
					@Override public Class<? extends Textual<?>>[] parameters() { return new Class[0]; }
					@Override public Class<?> actionClass() { return void.class; }
				};
			}

			@Override
			public Object			runAction(AbstractObjectOperController<?> controller, ObjectOperContext operContext, EntityHolder<?> ownerHolder)
			{
				return actionController.operate(operContext, ownerHolder);
			}
		};
	}
}
