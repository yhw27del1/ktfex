package com.kmfex.service.cmb.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import springannotationplugin.Properties;

import com.kmfex.action.cmb.CMBResult;
import com.kmfex.util.FileIOHelper;

/**
 * @author linuxp
 * */
@Service("cmbService")
@Scope("singleton")
public class CmbService {
	static final int TIMEOUT = 60000;//连接超时时间
	static final int SO_TIMEOUT = 60000;//数据传输超时
	private static HttpClient httpclient;
	public CmbService(){
		try {
			PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,TIMEOUT);
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient = new DefaultHttpClient(cm,params);
		} catch (Exception e) {
			System.out.println("初始化CmbService错误");
		}
	}
	
	@Properties(name="load_cmb_project_url")
	private String load_cmb_project_url;
	
	public CMBResult loadCmbServlet(String xmlData,String txCode,String txDate,String txTime){
		CMBResult r = new CMBResult();
		String result = null;
		String msg = null;
        try {
            HttpPost httppost = new HttpPost(this.load_cmb_project_url);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("xmlData", xmlData));
            formparams.add(new BasicNameValuePair("txCode", txCode));
            formparams.add(new BasicNameValuePair("txDate", txDate));
            formparams.add(new BasicNameValuePair("txTime", txTime));
            UrlEncodedFormEntity url_param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(url_param);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            result = FileIOHelper.inputStreamTOString(resEntity.getContent());
System.out.println("httpClientLoadResult:"+result);
            if(null!=result){
            	if("交易请求代码不能为空".equals(result)){
            		r.setSuccess(false);
            		msg = "交易请求代码不能为空";
            	}else if("交易请求实体不能为空".equals(result)){
            		r.setSuccess(false);
            		msg = "交易请求实体不能为空";
            	}else if("交易请求代码非法".equals(result)){
            		r.setSuccess(false);
            		msg = "交易请求代码非法";
            	}else if("交易日期不能为空".equals(result)){
            		r.setSuccess(false);
            		msg = "交易日期不能为空";
            	}else if("交易时间不能为空".equals(result)){
            		r.setSuccess(false);
            		msg = "交易时间不能为空";
            	}else if("未接收到招行响应报文".equals(result)){
            		r.setSuccess(false);
            		msg = "未接收到招行响应报文";
            	}else{//result为招行响应的报文数据
            		r.setSuccess(true);
            		msg = "调用获取数据成功";
            	}
            }else{
            	r.setSuccess(false);
            	msg = "未接收到招行响应报文";
            }
        } catch(Exception e) {
        	msg = "地址"+this.load_cmb_project_url+"找不到，请联系管理员";
        	r.setSuccess(false);
        }
        r.setResult(result);
        r.setMsg(msg);
		return r;
	}

	public String getLoad_cmb_project_url() {
		return load_cmb_project_url;
	}

	public void setLoad_cmb_project_url(String load_cmb_project_url) {
		this.load_cmb_project_url = load_cmb_project_url;
	}
}