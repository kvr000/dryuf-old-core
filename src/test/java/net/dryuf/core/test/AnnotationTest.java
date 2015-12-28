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

package net.dryuf.core.test;

import java.lang.annotation.Annotation;

import net.dryuf.core.Dryuf;
import net.dryuf.tenv.DAssert;
import org.junit.Test;


public class AnnotationTest extends java.lang.Object
{
	public <T extends Annotation> T	needMethodAnnotation(Class<?> clazz, String methodName, Class<T> annoType) throws Exception
	{
		T anno;
		if ((anno = clazz.getMethod(methodName).getAnnotation(annoType)) == null)
			throw new RuntimeException("annotation not found");
		return anno;
	}

	public <T extends Annotation> T	needFieldAnnotation(Class<?> clazz, String fieldName, Class<T> annoType) throws Exception
	{
		T anno;
		if ((anno = clazz.getField(fieldName).getAnnotation(annoType)) == null)
			throw new RuntimeException("annotation not found");
		return anno;
	}

	@Test
	public void			testElements() throws Exception
	{
		DAssert.assertEquals("parent", Dryuf.getMandatoryAnnotation(AnnoParent.class, TestAnnotationOne.class).value());
		DAssert.assertEquals("parent", Dryuf.getMandatoryAnnotation(AnnoParent.class, TestAnnotationTwo.class).value());
		DAssert.assertNull(AnnoParent.class.getAnnotation(TestAnnotationThree.class));
		DAssert.assertEquals("parent", needMethodAnnotation(AnnoParent.class, "method", TestAnnotationOne.class).value());
		DAssert.assertEquals("parent", needMethodAnnotation(AnnoParent.class, "method", TestAnnotationTwo.class).value());
		//DAssert.assertNull(needMethodAnnotation(AnnoParent.class, "method", TestAnnotationThree.class));
		DAssert.assertEquals("parent", needFieldAnnotation(AnnoParent.class, "field", TestAnnotationOne.class).value());
		DAssert.assertEquals("parent", needFieldAnnotation(AnnoParent.class, "field", TestAnnotationTwo.class).value());
		//DAssert.assertNull(needFieldAnnotation(AnnoParent.class, "field", TestAnnotationThree.class));
	}

	@Test
	public void			testInheritance() throws Exception
	{
		DAssert.assertEquals("parent", Dryuf.getMandatoryAnnotation(AnnoChild.class, TestAnnotationOne.class).value());
		DAssert.assertEquals("child", Dryuf.getMandatoryAnnotation(AnnoChild.class, TestAnnotationTwo.class).value());
		DAssert.assertEquals("child", Dryuf.getMandatoryAnnotation(AnnoChild.class, TestAnnotationThree.class).value());
		//DAssert.assertEquals("parent", needMethodAnnotation(AnnoChild.class, "method", TestAnnotationOne.class).value());
		DAssert.assertEquals("child", needMethodAnnotation(AnnoChild.class, "method", TestAnnotationTwo.class).value());
		DAssert.assertEquals("child", needMethodAnnotation(AnnoChild.class, "method", TestAnnotationThree.class).value());
		//DAssert.assertEquals("parent", needFieldAnnotation(AnnoChild.class, "field", TestAnnotationOne.class).value());
		DAssert.assertEquals("child", needFieldAnnotation(AnnoChild.class, "field", TestAnnotationTwo.class).value());
		DAssert.assertEquals("child", needFieldAnnotation(AnnoChild.class, "field", TestAnnotationThree.class).value());
	}
}
