package com.kmfex.webservice.client.hx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant_HX {
    /**  
     * 产生20位的交易流水
     * @return yyMMddHHmmssSSSxxxxx 
     */  
    public static String generateNo20(){
		//yyMMddHHmmssSSS：年月日时分秒毫秒，共15位
		//2位年，2位月，2位日，2位时，2位分，2位秒，3位毫秒
		SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date today = new Date();
		return (f.format(today)+(String.valueOf(Math.random()).substring(2))).substring(0, 20);
	}
    /**  
     * 产生2位的随机批次号
     * @return 
     */  
    public static String generateBatchNo2(){
    	return String.valueOf(Math.random()).substring(2).substring(0, 2);
    }
	public static final String[] TIME_SLOT_TRADE = new String[]{"9:00","18:00"};//会员进行入金、出金等操作的时间段
	
	public static final String[] TIME_SLOT_CHECK = new String[]{"9:00","18:00"};//内部员工进行清算、对账、出入金明细核对等操作的时间段
}
