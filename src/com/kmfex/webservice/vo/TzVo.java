package com.kmfex.webservice.vo;
/*
 * 通知类
 * @author  
 */
public class TzVo {
	private String str1="";//标题
	private String str2="";//发布时间
	private String str3="";//过期日期 
	private String str4="";//id
	
	public TzVo() { 
	}
	public TzVo(String str1, String str2, String str3, String str4) { 
		this.str1 = str1;
		this.str2 = str2;
		this.str3 = str3;
		this.str4 = str4;
	}
	
	public TzVo(String str1, String str2, String str3) { 
		this.str1 = str1;
		this.str2 = str2;
		this.str3 = str3;
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
	
}
