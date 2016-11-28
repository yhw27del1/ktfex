package com.kmfex.autoinvest.vo;



/**
 * 参与抽签的参数
 * @author  
 */
public class PreParams { 
	private String fid;//融资项目id
	private String param1;//风险评级 
	private String param2;//还款方式 
	private String dayMin;//按天最小值 
	private String dayMax;//按天最大值 
	private String monthMin;//按月最小值 
	private String monthMax;//按月最大值
	private String param5;//担保方式
	private double param6;//按月年利率(不低于)
	private double param7;//按天年利率(不低于)
	private double balance = 0d;//计划委托的融资项目金额
	private double param8 = 0d;//账户可用余额不低于 
	private double param9 = 0d;//单笔投标的最大金额    
	private String qxtype;//month,day
	private String keyword;//关键字
	
	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}
 

	public String getDayMin() {
		return dayMin;
	}

	public void setDayMin(String dayMin) {
		this.dayMin = dayMin;
	}

	public String getDayMax() {
		return dayMax;
	}

	public void setDayMax(String dayMax) {
		this.dayMax = dayMax;
	}

	public String getMonthMin() {
		return monthMin;
	}

	public void setMonthMin(String monthMin) {
		this.monthMin = monthMin;
	}

	public String getMonthMax() {
		return monthMax;
	}

	public void setMonthMax(String monthMax) {
		this.monthMax = monthMax;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public double getParam6() {
		return param6;
	}

	public void setParam6(double param6) {
		this.param6 = param6;
	}

	public double getParam7() {
		return param7;
	}

	public void setParam7(double param7) {
		this.param7 = param7;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getQxtype() {
		return qxtype;
	}

	public void setQxtype(String qxtype) {
		this.qxtype = qxtype;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public double getParam8() {
		return param8;
	}

	public void setParam8(double param8) {
		this.param8 = param8;
	}

	public double getParam9() {
		return param9;
	}

	public void setParam9(double param9) {
		this.param9 = param9;
	}

	 
 
}
