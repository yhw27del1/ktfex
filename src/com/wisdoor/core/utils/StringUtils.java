package com.wisdoor.core.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 * 
 * @author  
 * 
 */
public class StringUtils {

	/**
	 * 得到某个网址的字符串显示
	 * 
	 * @param urlString
	 * @return
	 */
	public static String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp).append("\n");
			}
			br.close();
			isr.close();
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 生成账户编号，17位
	public static String generateAccountId() {
		SimpleDateFormat f = new SimpleDateFormat("yyMMdd");
		return (f.format(new Date()) + (String.valueOf(Math.random()).substring(2))).substring(0, 17);
	}

	/**
	 * 补零
	 * 
	 * @param length
	 *            补零后的长度
	 * @param source
	 *            需要补零的长符串
	 * @return
	 */
	public static String fillZero(int length, String source) {
		StringBuilder result = new StringBuilder(source);
		for (int i = result.length(); i < length; i++) {
			result.insert(0, '0');
		}
		return result.toString();
	}

	public static String genRandomNum(int len) {
		final int maxNum = 10;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 取指定长度
	 * 
	 * @param str
	 * @param toCount
	 *            取长度 2表示一个汉字，两个字母
	 * @param more
	 *            替换字符，如"..." more
	 * @return
	 */
	public static String substring(String str, int toCount, String more) {
		int reInt = 0;
		String reStr = "";
		if (str == null) return "";
		char[] tempChar = str.toCharArray();
		for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr += tempChar[kk];
		}
		if (toCount == reInt || (toCount == reInt - 1)) reStr += more;
		return reStr;
	}



	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i))) return false;

		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
