package com.kmfex.autoinvest.webservice;
 

public class ParamVo {
	private boolean success = true;//是否使用
	private String msg; //操作的提示信息
	private String s1;//s1风险评级-->如：1,2,3,4,选择了四项
	private String s2;//s2还款方式 --> 如：1,选择了第一项按月等额本息 
	private long dayMin=0;//按天最小值 
	private long dayMax=0;//按天最大值 
	private long monthMin=0;//按月最小值 
	private long monthMax=0;//按月最大值
	private String s5;//s5担保方式 
	private double s6;//按月年利率(不低于)-->如: 10.5表示按月的融资项目，年利率不低于10.5,double型数字
	private double s7;//按天年利率(不低于)-->如: double型数字
	private double s8;//账户可用余额不低于,double型数字
	private double s9;//单笔投标的最大金额-->如: double型数字
	private String  lastDate="";//生效时间
	private String  nextFlag="1";//1新增的;0使用中
	public ParamVo(long dayMin, long dayMax, long monthMin, long monthMax) { 
		this.dayMin = dayMin;
		this.dayMax = dayMax;
		this.monthMin = monthMin;
		this.monthMax = monthMax;
	}
	public ParamVo() { 
	}
	public ParamVo(boolean success, String msg) { 
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
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
 
	public long getDayMin() {
		return dayMin;
	}
	public void setDayMin(long dayMin) {
		this.dayMin = dayMin;
	}
	public long getDayMax() {
		return dayMax;
	}
	public void setDayMax(long dayMax) {
		this.dayMax = dayMax;
	}
	public long getMonthMin() {
		return monthMin;
	}
	public void setMonthMin(long monthMin) {
		this.monthMin = monthMin;
	}
	public long getMonthMax() {
		return monthMax;
	}
	public void setMonthMax(long monthMax) {
		this.monthMax = monthMax;
	}
	public String getS5() {
		return s5;
	}
	public void setS5(String s5) {
		this.s5 = s5;
	}
	public double getS6() {
		return s6;
	}
	public void setS6(double s6) {
		this.s6 = s6;
	}
	public double getS7() {
		return s7;
	}
	public void setS7(double s7) {
		this.s7 = s7;
	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getNextFlag() {
		return nextFlag;
	}
	public void setNextFlag(String nextFlag) {
		this.nextFlag = nextFlag;
	}
	public double getS8() {
		return s8;
	}
	public void setS8(double s8) {
		this.s8 = s8;
	}
	public double getS9() {
		return s9;
	}
	public void setS9(double s9) {
		this.s9 = s9;
	} 
 
}
