package com.kmfex;

public class MoneyFormat {
	private static final String[] pattern = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static final String[] cPattern = { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿" };
	private static final String[] cfPattern = { "", "角", "分" };
	private static final String ZEOR = "零";

	//精确到分
	public static String format(String moneyString,boolean includeCent) {
		String fushu = "";
		if(moneyString.startsWith("-")){
			moneyString = moneyString.substring(1);
			fushu = "负";
		}
		int dotPoint = moneyString.indexOf("."); // 判断是否为小数
		String moneyStr;
		if (dotPoint != -1) {
			moneyStr = moneyString.substring(0, moneyString.indexOf("."));
		} else {
			moneyStr = moneyString;
		}
		if("0".equals(moneyStr)&&!includeCent){
			return "零元整";
		}else{
			StringBuffer ms = new StringBuffer();//整数部分处理
			if("0".equals(moneyStr)){
				ms.insert(0, "零");
			}else{
				for (int i = 0; i < moneyStr.length(); i++) {
					ms.append(pattern[moneyStr.charAt(i) - 48]); // 按数组的编号加入对应大写汉字
				}
				int cpCursor = 1;
				for (int j = moneyStr.length() - 1; j > 0; j--) {
					ms.insert(j, cPattern[cpCursor]); // 在j之后加字符,不影响j对原字符串的相对位置
					// 只是moneyStr.length()不断增加
					// insert(j,"string")就在j位置处插入,j=0时为第一位
					cpCursor = cpCursor == 8 ? 1 : cpCursor + 1; // 亿位之后重新循环
				}
				while (ms.indexOf("零拾") != -1) { // 当十位为零时用一个"零"代替"零拾"
					// replace的起始于终止位置
					ms.replace(ms.indexOf("零拾"), ms.indexOf("零拾") + 2, ZEOR);
				}
				while (ms.indexOf("零佰") != -1) { // 当百位为零时,同理
					ms.replace(ms.indexOf("零佰"), ms.indexOf("零佰") + 2, ZEOR);
				}
				while (ms.indexOf("零仟") != -1) { // 同理
					ms.replace(ms.indexOf("零仟"), ms.indexOf("零仟") + 2, ZEOR);
				}
				while (ms.indexOf("零万") != -1) { // 万需保留，中文习惯
					ms.replace(ms.indexOf("零万"), ms.indexOf("零万") + 2, "万");
				}
				while (ms.indexOf("零亿") != -1) { // 同上
					ms.replace(ms.indexOf("零亿"), ms.indexOf("零亿") + 2, "亿");
				}
				while (ms.indexOf("零零") != -1) {// 有连续数位出现零，即有以下情况，此时根据习惯保留一个零即可
					ms.replace(ms.indexOf("零零"), ms.indexOf("零零") + 2, ZEOR);
				}
				while (ms.indexOf("亿万") != -1) { // 特殊情况，如:100000000,根据习惯保留高位
					ms.replace(ms.indexOf("亿万"), ms.indexOf("亿万") + 2, "亿");
				}
				while (ms.lastIndexOf("零") == ms.length() - 1) { // 当结尾为零j，不必显示,经过处理也只可能出现一个零
					ms.delete(ms.lastIndexOf("零"), ms.lastIndexOf("零") + 1);
				}
			}

			int end;
			StringBuffer fraction = null;//小数部分处理
			if (includeCent&&(dotPoint = moneyString.indexOf(".")) != -1) { // 是小数的进入
				String fs = moneyString.substring(dotPoint + 1, moneyString.length());
				if ((fs.indexOf("00") == -1 || fs.indexOf("00") >= 2) && !fs.equals("0")) {// 若前两位小数全为零，则跳过操作
					end = fs.length() > 2 ? 2 : fs.length(); // 仅保留两位小数
					fraction = new StringBuffer(fs.substring(0, end));
					for (int j = 0; j < fraction.length(); j++) {
						fraction.replace(j, j + 1,pattern[fraction.charAt(j) - 48]); // 替换大写汉字
					}
					for (int i = fraction.length(); i > 0; i--) { // 插入中文标识
						if(i==1&&(fraction.charAt(0)=='零')){//仅保留2位小数，则“零捌”表示为：零捌分
							break;
						}else{
							fraction.insert(i, cfPattern[i]);
						}
					}
					fraction.insert(0, "元"); // 为整数部分添加标识
				} else {
					fraction = new StringBuffer("元整");
				}

			} else {
				fraction = new StringBuffer("元整");
			}

			ms.append(fraction); // 加入小数部分
			return fushu+ms.toString();
		}
	}
	public static void main(String[] args) {
		//System.out.println(format("950.086", true));
		//System.out.println(new Double(10000.00).toString());
		//System.out.println("10000.0");
		System.out.println(format(new Double(10000.00).toString(), true));
		//System.out.println(format("10000.0", true));
		System.out.println("00859".indexOf("00"));
	}
}
