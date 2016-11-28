package com.wisdoor.core.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
 
/** 
 *  
 * 发送请求 
 */
public class SendUrlRequestUtils {
	/**
	 * 发送请求  
	 * @param method  请求类型 POST|GET 目前只能使用POST
	 * @param params  参数集合 ( String | String )  
	 */
	public static void send_Url(String httpUrl,String method, HashMap<String, String> params) {
			StringBuffer param = new StringBuffer();
		try {
			URL url = new URL(httpUrl); 
			HttpURLConnection url_con = (HttpURLConnection) url
					.openConnection();
			url_con.setRequestMethod(method);
			url_con.setDoOutput(true);
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				param.append(key+"="+val+"&");
			}
			if(param.indexOf("&")>0){
				param.delete(param.length()-1, param.length());
			} 
			url_con.getOutputStream().write(param.toString().getBytes("utf-8"));
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			 InputStream in = url_con.getInputStream();
//			 BufferedReader rd = new BufferedReader(new
//			 InputStreamReader(in));
//			 StringBuilder tempStr = new StringBuilder();
//			 while (rd.read() != -1) {
//			 tempStr.append(rd.readLine());
//			 }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (url_con != null)
			// url_con.disconnect();
		}
	}
	
	
	public static String unescape(String src) {  
        StringBuffer tmp = new StringBuffer();  
        tmp.ensureCapacity(src.length());  
        int lastPos = 0, pos = 0;  
        char ch;  
        while (lastPos < src.length()) {  
            pos = src.indexOf("%", lastPos);  
            if (pos == lastPos) {  
                if (src.charAt(pos + 1) == 'u') {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 2, pos + 6), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 6;  
                } else {  
                    ch = (char) Integer.parseInt(src  
                            .substring(pos + 1, pos + 3), 16);  
                    tmp.append(ch);  
                    lastPos = pos + 3;  
                }  
            } else {  
                if (pos == -1) {  
                    tmp.append(src.substring(lastPos));  
                    lastPos = src.length();  
                } else {  
                    tmp.append(src.substring(lastPos, pos));  
                    lastPos = pos;  
                }  
            }  
        }  
        return tmp.toString();  
    }
}
