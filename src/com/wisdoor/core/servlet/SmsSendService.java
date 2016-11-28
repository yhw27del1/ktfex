package com.wisdoor.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kmfex.util.FileIOHelper;

public class SmsSendService extends HttpServlet {
	Logger logger = Logger.getLogger(SmsSendService.class.getName());
	private static final long serialVersionUID = 1205876916282173922L;
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		jdbcTemplate = (JdbcTemplate)wac.getBean("jdbcTemplate");
	}

	public SmsSendService() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String phones = request.getParameter("phones");
		String content = request.getParameter("content");
		String time = request.getParameter("time");
		String decode = request.getParameter("decode");
		String action = request.getParameter("action");
		try {
			HttpPost httppost = new HttpPost("http://192.168.100.11:8001/sms");
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
	        formparams.add(new BasicNameValuePair("phones", phones)); 
	        formparams.add(new BasicNameValuePair("content", content)); 
	        formparams.add(new BasicNameValuePair("time", time)); 
	        formparams.add(new BasicNameValuePair("decode", decode)); 
	        formparams.add(new BasicNameValuePair("action", action)); 
	        UrlEncodedFormEntity url_param = new UrlEncodedFormEntity(formparams, "UTF-8");
	        httppost.setEntity(url_param);
	        
	        SmsThread st = new SmsThread();
	        st.setHttppost(httppost);
	        st.setTelnomber(phones);
	        st.setContext(content);
	        
	        Thread t = new Thread(st);
	        t.start();   
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void record(String telnumber, String context, int result, String message){
		String id = UUID.randomUUID().toString();
		jdbcTemplate.update("insert into nh_sms_logs (id,telnumber,smscontext,message,datetime,result,providers) values (?,?,?,?,?,?,?)", id,telnumber,context,message,new Date(),result,0);
	}
	class SmsThread implements Runnable {
		private HttpPost httppost;
		private String telnomber;
		private String context;
		
		
		public void setContext(String context) {
			this.context = context;
		}
		public void setTelnomber(String telnomber) {
			this.telnomber = telnomber;
		}
		public void setHttppost(HttpPost httppost) {
			this.httppost = httppost;
		}

		@Override
		public void run() {
			int _result = 0;
			String _message = null;
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				String str = FileIOHelper.inputStreamTOString(resEntity.getContent());
				JSONObject json = JSONObject.fromObject(str);
				if(json.get("result")!= null && isInteger(json.get("result").toString())){
					int _temp = Integer.parseInt(json.get("result").toString());
					_result = _temp == 0 ? 1 : -1;
				}else{
					_result = -1;
				}
				if(json.get("str")!=null){
					_message = json.get("str").toString();
				}
			} catch (Exception e) {
				_result = -1;
				_message = e.getMessage();
			}finally{
				record(telnomber, context, _result, _message);
			}
		}

	}

	
	 public static boolean isInteger(String str) {
         int begin = 0;
         if (str == null || str.trim().equals("")) {
             return false;
         }
         str = str.trim();
         if (str.startsWith("+") || str.startsWith("-")) {
             if (str.length() == 1) {
                 // "+" "-"
                 return false;
             }
             begin = 1;
         }
         for (int i = begin; i < str.length(); i++) {
             if (!Character.isDigit(str.charAt(i))) {
                 return false;
             }
         }
         return true;
     }
}
