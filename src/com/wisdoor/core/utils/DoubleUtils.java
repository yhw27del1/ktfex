package com.wisdoor.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * double类型数据的操作
 * @author linuxp
 *
 */ 

public class DoubleUtils {
	//上千万的数据会用科学计数法
	//double类型数据的四舍五入
	public static double doubleCheck(double money,int scale){
		BigDecimal b = new BigDecimal(money+"");
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	//上千万的数据不会用科学计数法
	//double类型数据的四舍五入
	public static String doubleCheck2(double money,int scale){
		BigDecimal b = new BigDecimal(Double.toString(money));
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
		//return "123";
	}
	
	//如果字符串的长度小于size:liLeftAdd=true则在字符串的左边填充0;否则在右边填充0
	//如果字符串的长度大于size:liLeftCut=true则在字符串的左边剪切字符;否则在右边剪切字符
	public static String formatString(String input,int size,boolean isLeftAdd,boolean isLeftCut){
		if(null!=input&&input.length()!=size){
			if(input.length()>size){
				return isLeftCut?input.substring(input.length()-size):input.substring(0, size);
			}else{
				return isLeftAdd?(generateZoreStr(size-input.length())+input):(input+generateZoreStr(size-input.length()));
			}
		}else{
			return input;
		}
	}
	
	//填充size个0字符
	private static String generateZoreStr(int size){
		StringBuffer b=new StringBuffer();
	   	for(int i=0;i<size;i++){
	   		b.append("0");
	   	}
	   	return b.toString();
    }
	
	//上千万的数据不会用科学计数法
	//double类型数据的四舍五入
	//cmb金额格式化为15位字符串,无小数点
	public static String formatDouble(double money){
		BigDecimal b = new BigDecimal(Double.toString(money));
		String fm = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		String[] f = fm.split("\\.");
		return formatString(f[0]+f[1], 15, true, true);
	}
	
	//15未字符串转换成double,后2位为小数点,cmb字符串金额格式化为double
	public static double toDouble(String input){
		String set = input.substring(0,13);
		String dot = input.substring(13);
		BigDecimal b = new BigDecimal(set+"."+dot);
		return b.doubleValue();
	}
	
	public static double doubleRound(double money,int scale){
		BigDecimal b = new BigDecimal(money);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	//千 位上四舍五入 (个位)s
	public static double doubleToQian(double money){ 
		if(money==0)
			return 0d; 
		String moneyStr=doubleToString(money,0);
		if(moneyStr.length()<2)
		{
			return money;
			//return 1000d; 
		}
		char ge=moneyStr.charAt(moneyStr.length()-1);//得到个位的值
	//	char shi=moneyStr.charAt(moneyStr.length()-2);//得到十位的值 
	//	char bai=moneyStr.charAt(moneyStr.length()-3);//得到百位的值 
		int  numberGe =ge - 48; //char转int
	//	int  numberShi =shi - 48; //char转int
	//	int  numberBai =bai - 48; //char转int 
		if(numberGe>0)//只要个位有数字 
		{
		    moneyStr=moneyStr.substring(0, moneyStr.length()-1)+"0";
		    return Double.valueOf(moneyStr)+10;
		}else{
			return Double.valueOf(moneyStr);
		}
	}
	
	//double转字符串带四舍五入	
	public static String doubleToString(double d, int fNumber) {
	      if (fNumber < 0)
	            fNumber = 0;
	      String pattern = null;
	       switch (fNumber) {
	       case 0:
	           pattern = "#0"; 
	           break;
	       default:
	          pattern = "#0."; 
	          StringBuffer b = new StringBuffer(pattern);
	          for (int i = 0; i < fNumber; i++) {
	               b.append('#');
	           }
	            pattern = b.toString();
	          break;
	
	      }
	      DecimalFormat formatter = new DecimalFormat();
	      formatter.applyPattern(pattern);
	      String value = formatter.format(d);
	      return value;
	}
	
	public static void main(String[] args) {
		String input = formatDouble(1898.98);
		System.out.println(input);
		System.out.println(toDouble(input));
	}
}