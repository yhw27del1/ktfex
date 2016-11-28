package com.kmfex.autoinvest.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

import com.wisdoor.core.utils.DoubleUtils;
/**
 * 自动投标工具类
 * @author  
 */
public class AutoInvestUtils {
	
	//以divisionChar拆分字符串为数组   
    public static String[] stringAnalytical(String str, String divisionChar) {   
        String string[];   
        int i = 0;  
        if(null==str) return null;
        StringTokenizer tokenizer = new StringTokenizer(str, divisionChar);   
        string = new String[tokenizer.countTokens()]; 
         while (tokenizer.hasMoreTokens()) {   
            string[i] = new String();   
            string[i] = tokenizer.nextToken();   
            i++;   
        }   
        return string;
    } 
    
    //转换Param1为查询参数
    public static String ConverterParam1(String zhzs) { 
		if(Double.parseDouble(zhzs)>=8.75d){ 
			return "1,";
		}else if(Double.parseDouble(zhzs)<8.75d&&Double.parseDouble(zhzs)>=6.25d){
			return "2,";
		}else if(Double.parseDouble(zhzs)<6.25d&&Double.parseDouble(zhzs)>=3.75d){
			return "3,";
		}else if(Double.parseDouble(zhzs)<3.75d){  
			return "4,";
		}else{
	    	return "@,";
	    }
    }  
    
    //转换Param5为查询参数  
    public static String ConverterParam5(String fxbzState) { 
    	if("12".equals(fxbzState)){// 12本金担保
    		return "1,";
    	}else if("15".equals(fxbzState)){//15本息担保
    		return "2,";
    	}else if("10".equals(fxbzState)){//10无担保
    		return "3,";
    	}else{
    		return "@,";
    	}
    } 
    
    //Param3是否再按月的期间内
    public static Boolean ConverterParam3(String userParameterParam3,String Param3) { 
    	try { 
			String[] arr=stringAnalytical(userParameterParam3,","); 
			String[] arr2=stringAnalytical(Param3,","); 
			if(arr2.length==1){
				if(Long.parseLong(arr[0])<=Long.parseLong(arr2[0])&&Long.parseLong(arr[1])>=Long.parseLong(arr2[0])){
					return true;
				} 
			}
			if(arr2.length==2){
				long data1=Long.parseLong(arr[0]);
				long data2=Long.parseLong(arr[1]);
				long para1=Long.parseLong(arr2[0]);
				long para2=Long.parseLong(arr2[1]); 
				if(para1>=data1&&para2<=data2){
					return true;
				} 
			}
		 }catch (Exception e) { 
			e.printStackTrace();
		}

		return false;
    } 
    
    //Param4是否再按天的期间内  
    public static Boolean ConverterParam4(String userParameterParam4,String Param4) { 
    	try {
			String[] arr=stringAnalytical(userParameterParam4,","); 
			String[] arr2=stringAnalytical(Param4,","); 
			
			if(arr2.length==1){
				if(Long.parseLong(arr[0])<=Long.parseLong(arr2[0])&&Long.parseLong(arr[1])>=Long.parseLong(arr2[0])){
					return true;
				} 
			}
			if(arr2.length==2){
				long data1=Long.parseLong(arr[0]);
				long data2=Long.parseLong(arr[1]);
				long para1=Long.parseLong(arr2[0]);
				long para2=Long.parseLong(arr2[1]); 
				if(para1>=data1&&para2<=data2){
					return true;
				} 
			}
		 } catch (Exception e) { 
			e.printStackTrace();
		}

		return false;
    } 
	
	//千位
	public static double doubleToQian(double money){ 
		if(money==0)
			return 0d; 
		String moneyStr=DoubleUtils.doubleToString(money,0);
		if(moneyStr.length()<4)
		{
			return money; 
		}else{
			moneyStr=moneyStr.substring(0, moneyStr.length()-3)+"000";
		    return Double.valueOf(moneyStr);
		} 
	}
	
       /** 
        * 随机指定范围内N个不重复的数 
        * 在初始化的无重复待选数组中随机产生一个数放入结果中， 
        * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换 
        * 然后从len-2里随机产生下一个随机数，如此类推 
        * @param max  指定范围最大值 
        * @param min  指定范围最小值 
        * @param n  随机数个数 
        * @return int[] 随机数结果集 
        */  
       public static int[] randomArray(int min,int max,int n){  
           int len = max-min+1;  
             
           if(max < min || n > len){  
               return null;  
           }  
             
           //初始化给定范围的待选数组  
           int[] source = new int[len];  
              for (int i = min; i < min+len; i++){  
               source[i-min] = i;  
              }  
                
              int[] result = new int[n];  
              Random rd = new Random();  
              int index = 0;  
              for (int i = 0; i < result.length; i++) {  
               //待选数组0到(len-2)随机一个下标  
                  index = Math.abs(rd.nextInt() % len--);  
                  //将随机到的数放入结果集  
                  result[i] = source[index];  
                  //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换  
                  source[index] = source[len];  
              }  
              Arrays.sort(result);
              return result;  
       }  	
}
