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

package net.dryuf.webc;

import java.util.Map;
import java.io.IOException;
import java.lang.String;
import java.lang.RuntimeException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dryuf.net.util.UrlUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;


public class WebClient extends java.lang.Object
{
	public String			prepareQuery(String url, Map<String, Object> params)
	{
		if (params != null && params.size() != 0) {
			char separator = '\0';
			StringBuilder urlBuilder = new StringBuilder(url);
			if (url.indexOf('?') < 0) {
				separator = '?';
			}
			else if (!url.endsWith("&")) {
				separator = '&';
			}
			for (Map.Entry<String, Object> param: params.entrySet()) {
				if (separator != '\0')
					urlBuilder.append(separator);
				urlBuilder.append(UrlUtil.encodeUrl(param.getKey())).append("=").append(UrlUtil.encodeUrl(ObjectUtils.defaultIfNull(param.getValue(), "").toString()));
				separator = '&';
			}
			url = urlBuilder.toString();
		}
		return url;
	}

	public <T> T			runGet(String url, Map<String, Object> params, Class<T> clazz)
	{
		url = prepareQuery(url, params);
		HttpGet method = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(method);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("failed to run "+url+": "+response.getStatusLine());
			}
			return objectMapper.readValue(response.getEntity().getContent(), clazz);
		}
		catch (ClientProtocolException ex) {
			throw new RuntimeException(ex);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		finally {
			method.releaseConnection();
		}
	}

	public void			close()
	{
		//client.close();
	}

	protected HttpClient		httpclient = HttpClients.createDefault();

	protected static final ObjectMapper objectMapper = new ObjectMapper()
			.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, JsonParser.Feature.ALLOW_COMMENTS);
}
