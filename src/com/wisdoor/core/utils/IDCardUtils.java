package com.wisdoor.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类是身份证号码的工具类
 * 
 * @author 敖汝安
 * @version 2012-03-31
 * */
public class IDCardUtils {

	/**
	 * 校验码
	 * */
	private static final Map<Byte, String> crcs = new HashMap<Byte, String>();
	static {
		crcs.put((byte) 0, "1");
		crcs.put((byte) 1, "0");
		crcs.put((byte) 2, "X");
		crcs.put((byte) 3, "9");
		crcs.put((byte) 4, "8");
		crcs.put((byte) 5, "7");
		crcs.put((byte) 6, "6");
		crcs.put((byte) 7, "5");
		crcs.put((byte) 8, "4");
		crcs.put((byte) 9, "3");
		crcs.put((byte) 10, "2");
	}

	/**
	 * 判断指定身份证号码是否为合法的身份证号码
	 * @return true 如果指定的身份证号码合法;false 如果指定的身份证号码不合法
	 * <pre>
	 * 我国现行使用公民身份证号码有两种尊循两个国家标准，
	 * 〖GB11643-1989〗和 〖GB11643-1999〗。 〖GB11643-1989〗中规定的是15位
	 * 身份证号码：排列顺序从左至右依次为：六位数字地址码，六位数字出生日期码，
	 * 三位数字顺序码 ，其中出生日期码不包含世纪数。 〖GB11643-1999〗中规定的
	 * 是18位身份证号码：公民身份号码是特征组合码，由十七位数字本体码和一位数
	 * 字校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期
	 * 码，三位数字顺序码和一位数字校验码。 地址码表示编码对象常住户口所在县
	 * (市、旗、区)的行政区划代码。 生日期码表示编码对象出生的年、月、日，其中
	 * 年份用四位数字表示，年、月、日之间不用分隔符。顺序码表示同一地址码所标
	 * 识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给
	 * 男性，偶数分给女性。校验码是根据前面十七位数字码，按照
	 * ISO 7064:1983.MOD 11-2校验码计算出来的检验码。 公式如下： 
	 * ∑(a[i]*W[i])mod 11 ( i = 2, 3, ..., 18 ) (1)  
	 * "*"表示乘号i--------表示身份证号码每一位的序号，从右至左，最左侧为18，
	 * 最右侧为1。 
	 * a[i]-----表示身份证号码第 i 位上的号码 
	 * W[i]-----表示第 i 位上的权值 W[i] = 2^(i-1) mod 11 
	 * i: 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
	 * Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
	 * 令计算公式 (1)结果为 R，根据下表找出 R 对应的校验码即为要求身份证号码的
	 * 校验码C。
	 *  R 0 1 2 3 4 5 6 7 8 9 10 
	 *  C 1 0 X 9 8 7 6 5 4 3 2 由此看出 X 就是 10，罗马数字中的 10 就是X，
	 *  所以在新标准的身份证号码中可能含有非数字的字母X。
	 * </pre>
	 * */
	public static boolean isValid(String cardNo) {
		boolean ret = false;
		int sum = 0;
		if (null != cardNo && cardNo.length() == 18) {

			String crc = cardNo.substring(17).toUpperCase();

			System.out.println("crc= " + crc + ",cardNo=" + cardNo);

			int[] a = new int[17];
			int[] w = new int[17];

			w[0] = 7;
			w[1] = 9;
			w[2] = 10;
			w[3] = 5;
			w[4] = 8;
			w[5] = 4;
			w[6] = 2;
			w[7] = 1;
			w[8] = 6;
			w[9] = 3;
			w[10] = 7;
			w[11] = 9;
			w[12] = 10;
			w[13] = 5;
			w[14] = 8;
			w[15] = 4;
			w[16] = 2;

			for (int i = 0; i < 17; i++) {
				a[i] = Integer.parseInt(cardNo.substring(i, i + 1));
				System.out.println("a[" + i + "]= " + a[i]);
				sum += a[i] * w[i];
			}

			sum %= 11;
			System.out.println("sum= " + sum);
			if (null != crcs.get((byte) sum)
					&& crcs.get((byte) sum).equals(crc)) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(isValid("452123790418431"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
