package com.kmfex.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ReadsStaticConstantPropertiesUtil { 
	private static Properties properties = new Properties();
	static{
		try {     
			properties.load(ReadsStaticConstantPropertiesUtil.class.getClassLoader().getResourceAsStream("static_constant.properties"));
		} catch (IOException e) {  
			e.printStackTrace();
		}
	}

	public static String readValue(String key){		
		return (String)properties.get(key);
	}
	
	public static void updateServiceCache(String  sid) {
		String url = readValue("SERVICE_CACHE_URL");  
		URL u = null;  
		HttpURLConnection con = null; 
		StringBuffer sb = new StringBuffer();
		sb.append("sid=").append(sid);  
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");  
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
			con.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}
	
}
