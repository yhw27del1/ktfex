package com.kmfex.util;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSUtil {
	static final int TIMEOUT = 60000;// 连接超时时间
	static final int SO_TIMEOUT = 60000;// 数据传输超时


	public static void sms(String phones, String content, String time, String decode, String action) {
		String url = "http://localhost:8080/sms/SmsService";
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		sb.append("phones=").append(phones).append("&content=").append(content).append("&time=").append(time).append("&decode=").append(decode).append("&action=").append(action);
		
		// 尝试发送请求
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
	public static void sms(String phones, String content) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date_str = sdf.format(new Date());
		SMSUtil.sms(phones, content, date_str, "", "1");
	}

	public static void main(String[] args) {
		sms("13759102440", "张三", "0", "", "1");
	}

}
