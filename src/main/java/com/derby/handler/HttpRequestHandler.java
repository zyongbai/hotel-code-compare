package com.derby.handler;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import com.derby.util.Dom4jXml;
import com.derby.entity.Message;
import com.derby.util.HttpClientUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Http«Î«Û
 * 
 * @author ZHANGYONG415
 *
 */
public class HttpRequestHandler {
	public Map<String, List<Message>> getRequest(Message message) {
		Map<String, List<Message>> result = null;

		HttpClient httpClient = HttpClientUtil.getInstance();
		GetMethod getMethod = new GetMethod(message.getUrl());
		try {
			httpClient.executeMethod(getMethod);
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			
			result = Dom4jXml.xml2Message(message, inputStream);
		} catch (HttpException e) {
			e.printStackTrace();
			result = new HashMap<String, List<Message>>();
			List<Message> list = new ArrayList<Message>();
			list.add(message);
			result.put(message.getHotelCode(), list);
		} catch (IOException e) {
			e.printStackTrace();
			result = new HashMap<String, List<Message>>();
			List<Message> list = new ArrayList<Message>();
			list.add(message);
			result.put(message.getHotelCode(), list);
		}
		
		return result;
	}
}
