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

package net.dryuf.xml.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dryuf.core.Dryuf;


public class DomUtil extends java.lang.Object
{
	@SuppressWarnings("unchecked")
	public static <T> T		convertNative(String value, T defaultValue)
	{
		Class<?> valueClass = defaultValue.getClass();
		if (defaultValue instanceof String)
			return (T)value;
		if (defaultValue instanceof Boolean)
			return (T)Boolean.valueOf(value.equals("true") || value.equals("1"));
		return (T)Dryuf.invokeMethod(null, Dryuf.getClassMethod(valueClass, "valueOf", String.class), value);
	}

	public static Element		getOptionalElement(Element parentElement, String elementName)
	{
		NodeList nodes = getImmediateElementsByTagName(parentElement, elementName);
		if (nodes.getLength() != 1) {
			if (nodes.getLength() == 0)
				return null;
			throw new RuntimeException("expected single element on node "+parentElement.getNodeName());
		}
		return (Element)nodes.item(0);
	}

	public static Element		getSingleElement(Element parentElement, String elementName)
	{
		NodeList nodes = getImmediateElementsByTagName(parentElement, elementName);
		if (nodes.getLength() != 1)
			throw new RuntimeException("expected single element on node "+parentElement.getNodeName());
		return (Element)nodes.item(0);
	}

	public static Element		getPreviousSameSibling(Element currentElement)
	{
		String name = currentElement.getNodeName();
		Node previous;
		for (previous = currentElement.getPreviousSibling(); previous != null && (previous.getNodeType() != Node.ELEMENT_NODE || !name.equals(previous.getNodeName())); previous = previous.getPreviousSibling()) ;
		return (Element) previous;
	}

	public static Element		getNextSameSibling(Element currentElement)
	{
		String name = currentElement.getNodeName();
		Node next;
		for (next = currentElement.getNextSibling(); next != null && (next.getNodeType() != Node.ELEMENT_NODE || !name.equals(next.getNodeName())); next = next.getNextSibling()) ;
		return (Element)next;
	}

	public static NodeList		getImmediateElementsByTagName(Element parentElement, String elementName)
	{
		NodeListImpl found = new NodeListImpl();
		Node child;
		for (child = parentElement.getFirstChild(); child != null; child = child.getNextSibling()) {
			 if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(elementName)) {
				 found.add(child);
			 }
		}
		return found.finish();
	}

	public static Element		getFirstElementByName(Element parentElement, String elementName)
	{
		NodeList nodes = getImmediateElementsByTagName(parentElement, elementName);
		if (nodes.getLength() == 0)
			return null;
		return (Element)nodes.item(0);
	}

	public static Element		getLastElementByName(Element parentElement, String elementName)
	{
		NodeList nodes = getImmediateElementsByTagName(parentElement, elementName);
		if (nodes.getLength() == 0)
			return null;
		return (Element)nodes.item(nodes.getLength()-1);
	}

	@SuppressWarnings("unchecked")
	public static <T> T		getAttributeDefault(Element parentElement, String attributeName, T defaultValue)
	{
		String value = parentElement.getAttribute(attributeName);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		if (defaultValue == null)
			return (T)value;
		return convertNative(value, defaultValue);
	}

	public static String		getAttributeMandatory(Element parentElement, String attributeName)
	{
		String value = parentElement.getAttribute(attributeName);
		if (StringUtils.isEmpty(value)) {
			throw new RuntimeException("mandatory attribute "+attributeName+" not found on "+parentElement.getNodeName());
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T		getSubElementContentDefault(Element parentElement, String elementName, T defaultValue)
	{
		NodeList nodes = getImmediateElementsByTagName(parentElement, elementName);
		if (nodes.getLength() == 0)
			return defaultValue;
		if (nodes.getLength() > 1)
			throw new RuntimeException("expected at most one element "+elementName+" on node "+parentElement.getNodeName());
		String value = nodes.item(0).getTextContent();
		return defaultValue == null ? (T)value : convertNative(value, defaultValue);
	}

	public static class NodeListImpl extends java.lang.Object implements NodeList
	{
		@Override
		public Node			item(int index)
		{
			return items.get(index);
		}

		@Override
		public int			getLength()
		{
			return items.size();
		}

		public				NodeListImpl(List<Node> list)
		{
			items = list;
		}

		public				NodeListImpl()
		{
			items = new LinkedList<Node>();
		}

		public void			add(Node node)
		{
			items.add(node);
		}

		public NodeListImpl		finish()
		{
			return this;
		}

		List<Node>			items;
	}
}
