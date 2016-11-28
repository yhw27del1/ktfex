package com.wisdoor.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;

/**
 * 货币操作类
 * 
 * @author eclipse
 * @author aora
 * 
 * <pre>
 * 2012-09-34 aora 增加addComma(double amount)方法。此方法在货币金额的整数部分
 * 每三位数之前加一个逗号。
 * </pre>
 */
public class CurrencyOperator {
	public static void main(String[] args) {
		double d = 6665558955446.065069;
		System.out.println(addComma(d));
	}

	public static String get(String key) throws Exception {
		InputStream in = CurrencyOperator.class.getClassLoader()
				.getResourceAsStream("currency.properties");
		Properties currency = new Properties();
		String value = null;
		currency.load(in);
		Object obj = currency.get(key);
		if (obj != null) {
			value = obj.toString();
		}
		return value;
	}

	public static boolean put(String key, String value) throws Exception {
		InputStream in = CurrencyOperator.class.getClassLoader()
				.getResourceAsStream("currency.properties");
		Properties currency = new Properties();
		try {

			currency.load(in);
			String configPath = CurrencyOperator.class.getClassLoader()
					.getResource("")
					+ "currency.properties";
			configPath = configPath.substring(6);
			OutputStream fos = new FileOutputStream(configPath);
			currency.setProperty(key, value);
			currency.save(fos, "Update '" + key + "' value");
			fos.flush();
			fos.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 在货币金额整数部分每三位前加逗号
	 * 
	 * @param amount
	 *            要加逗号的金额
	 * @return 加了逗号后的金额表示
	 * */
	public final static String addComma(double amount) {
		// String[] strs = new BigDecimal(amount).toString().split("\\.");
		// System.out.println("strs.length= " + strs.length);
		// String front = "";
		// String back = "";
		// if (strs.length == 1) {
		// front = strs[0];
		// } else if (strs.length >= 2) {
		// front = strs[0];
		// back = strs[1];
		// }
		// StringBuilder sb = new StringBuilder(front);
		// int counts = sb.length() / 3;
		//
		// if (0 == sb.length() % 3) {
		// counts--;
		// }
		//
		// sb = sb.reverse();
		// for (int i = 1; i <= counts; i++) {
		// sb.insert(3 * i + i - 1, ',');
		// }
		// if (strs.length == 1) {
		// return sb.reverse().toString() + back;
		// } else {
		// return sb.reverse().toString() + "." + back;
		// }		
		NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();
		return numberFormat.format(amount);
	}

}
