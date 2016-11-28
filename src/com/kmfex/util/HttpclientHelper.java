/**
 * 
 */
package com.kmfex.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * @author lanwd
 *
 */
public class HttpclientHelper {
	
	private static Logger logger = Logger.getLogger(HttpclientHelper.class);
	
	private final HostConfiguration hostConfig;
	
	private Cookie[] cookies;
	
	private final HttpClient client;
	
	public HttpclientHelper(String host,int port){
		hostConfig=new HostConfiguration();
		hostConfig.setHost(host,port);
		client=HttpclientManager.getHttpClient();
	}
	
	private String inputStreamTOString(InputStream is){
	       return FileIOHelper.inputStreamTOString(is);
	    }
	
	
	public String executeMethod(String url,NameValuePair[] params,Header[] headers){
		
		String result=null;
		String redirect=null;
		PostMethod post=new PostMethod(url);
		 post.getParams().setContentCharset("UTF-8");
		post.setRequestBody(params);
		if(headers!=null&&headers.length!=0)for(Header h:headers){
			post.addRequestHeader(h);
		}
		
		int state=0;
		try {
			state=client.executeMethod(hostConfig, post);
			if(state==HttpStatus.SC_OK){
				result=inputStreamTOString(post.getResponseBodyAsStream());
			}else if(state==HttpStatus.SC_MOVED_PERMANENTLY||state==HttpStatus.SC_MOVED_TEMPORARILY){
				Header locationHeader = post.getResponseHeader("location");
				if(locationHeader!=null){
					redirect=locationHeader.getValue();
					
				}
			}else{
				HttpClientStatus.verifyStatus(state);
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
		
		if(redirect!=null){
//			logger.info(redirect);
			result=executeMethod(redirect);
//			logger.info(executeMethod("/egj/egjjfxt/teczUserInfo!unicomQuery.do?entity.acceptTel=15687132800"));
		}
		return result;
		
	}
	
	public String executeMethod(String url,NameValuePair[] params){
		return executeMethod(url,params,null);
	}
	
	public String executeMethod(String url){
		String result=null;
		GetMethod post=new GetMethod(url);
		post.getParams().setContentCharset("UTF-8");
		int state=0;
		try {
			state=client.executeMethod(hostConfig,post);
			if(state==HttpStatus.SC_OK){
				result=inputStreamTOString(post.getResponseBodyAsStream());
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
		return result;
	}
	
	public BufferedImage executeImage(String url){
		String redirect=null;
		BufferedImage result=null;
		GetMethod post=new GetMethod(url);
		 post.getParams().setContentCharset("UTF-8");
		int state=0;
		try {
			state=client.executeMethod(hostConfig,post);
			if(state==HttpStatus.SC_OK){
				result=ImageIO.read(post.getResponseBodyAsStream());  
			}else if(state==HttpStatus.SC_MOVED_PERMANENTLY||state==HttpStatus.SC_MOVED_TEMPORARILY){
				Header locationHeader = post.getResponseHeader("location");
				if(locationHeader!=null){
					redirect=locationHeader.getValue();
					
				}
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}

		return result;
	}

	/**
	 * @param client
	 */
	private void getCookies(HttpClient client) {
		cookies=client.getState().getCookies();
		
	}

	/**
	 * @param client
	 */
	private void setCookies(HttpClient client) {
		if(cookies!=null)client.getState().addCookies(cookies);
		
	}
	
}
