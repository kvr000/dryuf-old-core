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

package net.dryuf.io;

import java.io.IOException;
import java.lang.String;
import java.lang.RuntimeException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;


public class HttpUtil extends java.lang.Object
{
	public static void		putRaw(String url, String contentType, byte[] content, String[] headerPairs)
	{
		HttpClient httpclient = HttpClients.createDefault();
		HttpPut method = new HttpPut(url);
		method.addHeader("content-type", contentType);
		for (int i = 0; i < headerPairs.length; i++)
			method.addHeader(headerPairs[0], headerPairs[1]);
		try {
			HttpResponse response = httpclient.execute(method);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("failed to run "+url+": "+response.getStatusLine());
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		finally {
			method.releaseConnection();
		}
	}
}
