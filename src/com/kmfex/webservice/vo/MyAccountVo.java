package com.kmfex.webservice.vo;
/*
 * 我的账户
 * @author  
 */
public class MyAccountVo {
	private boolean success = true;//操作是否正常
	private String   msg; //操作的提示信息 
	private String  str1;// 总资产
	private String  str2;  //可用余额
	private String  str3; //冻结金额
	private String  str4;  //可转金额
	private String  str5; //持有债权
	 
	public MyAccountVo(boolean success, String msg) { 
		this.success = success;
		this.msg = msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
	public String getStr3() {
		return str3;
	}
	public void setStr3(String str3) {
		this.str3 = str3;
	}
	public String getStr4() {
		return str4;
	}
	public void setStr4(String str4) {
		this.str4 = str4;
	}
	public String getStr5() {
		return str5;
	}
	public void setStr5(String str5) {
		this.str5 = str5;
	}
 
}
