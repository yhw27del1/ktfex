/**
 * 
 */
package com.kmfex.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * @author lanwd
 *
 */
public class HttpclientManager {
	private static final int CONNECT_TIMEOUT=60000;
	private static final int SO_TIMEOUT=60000;
	private static final int MAX_TOTAL_CONNECT=100; //50
	private static final int defaultMaxConnectionsPerHost=80;  //40
	
	
	
	private static final MultiThreadedHttpConnectionManager MANAGER;
	
	static{
		HttpConnectionManagerParams p=new HttpConnectionManagerParams();
		p.setConnectionTimeout(CONNECT_TIMEOUT);
		p.setSoTimeout(SO_TIMEOUT);
		p.setMaxTotalConnections(MAX_TOTAL_CONNECT);
		p.setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
	
		MANAGER=new MultiThreadedHttpConnectionManager();
		MANAGER.setParams(p);
	}
	
	public static HttpClient getHttpClient(){
		return new HttpClient(MANAGER);
		
	}
}
