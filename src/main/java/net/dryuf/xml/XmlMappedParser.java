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

package net.dryuf.xml;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.dryuf.core.Dryuf;


public class XmlMappedParser extends java.lang.Object
{
	public static class XmlMappedTree
	{
		public				XmlMappedTree(Method startHandler, Method endHandler, Map<String, XmlMappedTree> subtree)
		{
			this.startHandler = startHandler;
			this.endHandler = endHandler;
			this.subtree = subtree;
		}

		public				XmlMappedTree(Method startHandler, Method endHandler, String tag, XmlMappedTree tagsub, Object... moresub)
		{
			this.startHandler = startHandler;
			this.endHandler = endHandler;
			this.subtree = new HashMap<String, XmlMappedTree>();
			subtree.put(tag, tagsub);
			for (int i = 0; i < moresub.length; i += 2) {
				subtree.put((String)moresub[i], (XmlMappedTree)moresub[i+1]);
			}
		}

		public				XmlMappedTree(Method startHandler, Method endHandler)
		{
			this.startHandler = startHandler;
			this.endHandler = endHandler;
			this.subtree = EMPTY_TREE;
		}

		public static XmlMappedTree	create(Class<?> clazz, String startHandler, String endHandler)
		{
			return new XmlMappedTree(startHandler != null ? Dryuf.getClassMethod(clazz, startHandler, String.class, Attributes.class) : null, endHandler != null ? Dryuf.getClassMethod(clazz, endHandler, String.class, String.class) : null);
		}

		public static XmlMappedTree	create(Class<?> clazz, String startHandler, String endHandler, Map<String, XmlMappedTree> subtree)
		{
			return new XmlMappedTree(startHandler != null ? Dryuf.getClassMethod(clazz, startHandler, String.class, Attributes.class) : null, endHandler != null ? Dryuf.getClassMethod(clazz, endHandler, String.class, String.class) : null, subtree);
		}

		public static XmlMappedTree	create(Class<?> clazz, String startHandler, String endHandler, String tag, XmlMappedTree tagsub, Object... moresubs)
		{
			return new XmlMappedTree(startHandler != null ? Dryuf.getClassMethod(clazz, startHandler, String.class, Attributes.class) : null, endHandler != null ? Dryuf.getClassMethod(clazz, endHandler, String.class, String.class) : null, tag, tagsub, moresubs);
		}

		public Method			getStartHandler()
		{
			return this.startHandler;
		}

		Method				startHandler;
		public Method			getEndHandler()
		{
			return this.endHandler;
		}

		Method				endHandler;

		public Map<String, XmlMappedTree> getSubtree()
		{
			return subtree;
		}
		Map<String, XmlMappedTree>	subtree = null;
	}

	public static class XmlDynamicHandler
	{
		public				XmlDynamicHandler(Object handlerObject, Method startHandler, Method endHandler, Method childHandler)
		{
			this.handlerObject = handlerObject;
			this.startHandler = startHandler;
			this.endHandler = endHandler;
			this.childHandler = childHandler;
		}

		public static XmlDynamicHandler	create(Class<?> clazz, String startHandler, String endHandler, String childHandler)
		{
			return new XmlDynamicHandler(null, Dryuf.getClassMethod(clazz, startHandler, String.class, Attributes.class), Dryuf.getClassMethod(clazz, endHandler, String.class, Attributes.class), Dryuf.getClassMethod(clazz, childHandler, String.class, Attributes.class));
		}

		public Object			getHandlerObject()
		{
			return this.handlerObject;
		}

		Object				handlerObject;
		public Method			getStartHandler()
		{
			return this.startHandler;
		}

		Method				startHandler;
		public Method			getEndHandler()
		{
			return this.endHandler;
		}

		Method				endHandler;
		public Method			getChildHandler()
		{
			return this.childHandler;
		}

		Method				childHandler;
	}

	public				XmlMappedParser()
	{
		try {
			saxParser = SAXParserFactory.newInstance().newSAXParser();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void			setupHandlers(Object handlerObject, XmlDynamicHandler mainHandlers)
	{
		this.dynamicObject = handlerObject;
		this.dynamicStack = new LinkedList<XmlDynamicHandler>();
		this.dynamicStack.push(mainHandlers);

                saxHandler = new DefaultHandler()
		{
			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
				startDynamicElement(qName, attributes);
			}

			public void endElement(String uri, String localName, String qName) throws SAXException
			{
				endDynamicElement(qName);
			}

			public void characters(char ch[], int start, int length) throws SAXException
			{
				characterData(new String(ch, start, length));
			}
		};
	}

	public void			setupMapped(Object mappedObject, XmlMappedTree handlingMap)
	{
		this.mappedObject = mappedObject;
		this.mappedStack = new LinkedList<XmlMappedTree>();
		this.mappedStack.push(handlingMap);
		this.textStack = new LinkedList<String>();

                saxHandler = new DefaultHandler()
		{
			public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
				startMappedElement(qName, attributes);
			}

			public void endElement(String uri, String localName, String qName) throws SAXException
			{
				endMappedElement(qName);
			}

			public void characters(char ch[], int start, int length) throws SAXException
			{
				characterData(new String(ch, start, length));
			}
		};
	}

	public void			processStream(InputStream inputStream)
	{
		try {
			saxParser.parse(inputStream, saxHandler);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void			processContent(byte[] content)
	{
		processStream(new java.io.ByteArrayInputStream(content));
	}

	public void			processPartial(byte[] content)
	{
		processStream(new java.io.ByteArrayInputStream(content));
	}

	public void			startDynamicElement(String tag, Attributes attrList)
	{
		XmlDynamicHandler handler = dynamicStack.peek();
		if (handler.getChildHandler() != null) {
			if ((handler = (XmlDynamicHandler)Dryuf.invokeMethod(handler.getHandlerObject() != null ? handler.getHandlerObject() : dynamicObject, handler.getChildHandler(), tag, attrList)) == null)
				handler = handlerDummy;
			if (handler.getStartHandler() != null)
				Dryuf.invokeMethod(handler.getHandlerObject() != null ? handler.getHandlerObject() : dynamicObject, handler.getStartHandler(), tag, attrList);
		}
		this.textStack.push("");
		dynamicStack.push(handler);
	}

	public void			endDynamicElement(String tag)
	{
		XmlDynamicHandler handler = dynamicStack.pop();
		String content = this.textStack.pop();
		if (handler.getEndHandler() != null) {
			Dryuf.invokeMethod(handler.getHandlerObject() != null ? handler.getHandlerObject() : dynamicObject, handler.getEndHandler(), tag, content);
		}
	}

	public static void		startDummyElement(String tag, Attributes attrList)
	{
	}

	public static void		endDummyElement(String tag, String content)
	{
	}

	public static XmlDynamicHandler	childDummyElement(String tag, Attributes attrList)
	{
		return null;
	}

	public void			startMappedElement(String tag, Attributes attrList)
	{
		XmlMappedTree current = this.mappedStack.peek();
		if (current != null) {
			if (current.getSubtree().containsKey(tag)) {
				current = current.getSubtree().get(tag);
			}
			else if (current.getSubtree().containsKey("*")) {
				current = current.getSubtree().get("*");
			}
			else {
				current = null;
			}
		}
		this.mappedStack.push(current);
		this.textStack.push("");
		if (current != null && current.getStartHandler() != null) {
			Dryuf.invokeMethod(mappedObject, current.getStartHandler(), tag, attrList);
		}
	}

	public void			endMappedElement(String tag)
	{
		XmlMappedTree current = this.mappedStack.pop();
		String content = this.textStack.pop();
		if (current != null && current.getEndHandler() != null) {
			Dryuf.invokeMethod(mappedObject, current.getEndHandler(), tag, content);
		}
	}

	public void			characterData(String data)
	{
		this.textStack.push(textStack.pop()+data);
	}

	protected SAXParser		saxParser;
	protected DefaultHandler	saxHandler;

	protected LinkedList<String>	textStack;

	protected Object		dynamicObject;
	protected LinkedList<XmlDynamicHandler> dynamicStack;

	protected Object		mappedObject;
	protected LinkedList<XmlMappedTree> mappedStack;

	protected final static Map<String, XmlMappedTree> EMPTY_TREE = new HashMap<String, XmlMappedTree>();
	protected final static XmlDynamicHandler handlerDummy = new XmlDynamicHandler(null, Dryuf.getClassMethod(XmlMappedParser.class, "startDummyElement", String.class, Attributes.class), Dryuf.getClassMethod(XmlMappedParser.class, "endDummyElement", String.class, String.class), Dryuf.getClassMethod(XmlMappedParser.class, "childDummyElement", String.class, Attributes.class));
};
