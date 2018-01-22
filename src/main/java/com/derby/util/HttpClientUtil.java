package com.derby.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpClientUtil {
	private static HttpClient httpClient;
	
	static {
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setConnectionTimeout(20000);
		params.setSoTimeout(180000);
		params.setDefaultMaxConnectionsPerHost(100);
		params.setMaxTotalConnections(256);
		
		httpClient = new HttpClient(connectionManager);
	}
	
	public static synchronized HttpClient getInstance() {
		return httpClient;
	}
}
