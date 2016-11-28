package com.kmfex.service.hx.impl;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.kmfex.util.FileIOHelper;

/**
 * @author linuxp
 * */
@Service("hxbService")
@Scope("singleton")
public class HXBankService {
	static final int TIMEOUT = 60000;//连接超时时间
	static final int SO_TIMEOUT = 60000;//数据传输超时
	private static HttpClient httpclient;
	public HXBankService(){
		try {
			KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
			ClasspathResourceLoader m = new ClasspathResourceLoader();
            InputStream s = m.getResourceStream("server.jks");
			//InputStream s = new ClassPathResource("server.jks").getInputStream();
			try {
			    trustStore.load(s, "Abcd1234".toCharArray());
			} finally {
			    try { s.close(); } catch (Exception ignore) {}
			}
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
	        Scheme scheme = new Scheme("https", 443, socketFactory);
	        schemeRegistry.register(scheme);
			PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,TIMEOUT);
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient = new DefaultHttpClient(cm,params);
		} catch (Exception e) {
			System.out.println("初始化HXBankService错误");
		}
	}
	
	public String loadHXBank(String json,String trnxCode){
		String result = null;
        try {
        	//此地址为假地址，正式环境发布时换成正确地址即可
        	//https://116.55.243.30/kmfex/bank/hxbServlet
            HttpPost httppost = new HttpPost("https://localhost/kmfex/bank/hxbServlet");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
            formparams.add(new BasicNameValuePair("json", json)); 
            formparams.add(new BasicNameValuePair("trnxCode", trnxCode)); 
            UrlEncodedFormEntity url_param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(url_param);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            result = FileIOHelper.inputStreamTOString(resEntity.getContent());
        } catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
