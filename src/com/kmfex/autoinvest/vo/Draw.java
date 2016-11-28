package com.kmfex.autoinvest.vo;

import java.io.Serializable;
import java.util.Date;

import com.wisdoor.core.utils.DoubleUtils;


/**
 * 抽签池中中签的对象
 * @author  
 */
public class Draw  implements Serializable{
 
	private static final long serialVersionUID = -5838060308981820173L;
	private String fid;//融资项目id
	private long userid;// 用户id
	private String username;// 用户名
	private String memberid;// 会员id
	private long accountid;// 账户
	private double balance = 0d;// 可用余额
	private double balance_zq = 0d;// 可用余额(整千)
	private double min = 0d;// 最小值
	private double max = 0d;// 最大值  
	private double prePrice = 0d;// 抽签预成交金额
	private double levelScore = 0d;// 优先级
	private long lastTime = 0;//最后一次中签时间,0表示从来未中签 
	private double fee1 = 0d;// 收取费用
	
	private String param1;
	private String param2;
	private long dayMin=0;//按天最小值 
	private long dayMax=0;//按天最大值 
	private long monthMin=0;//按月最小值 
	private long monthMax=0;//按月最大值
	private String param5;
	private String param6;
	private String param7;
	
	private double param8;//账户可用余额不低于
	private double param9;//单笔投标的最大金额  
	
	private Date last;
	
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

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public String getParam6() {
		return param6;
	}

	public void setParam6(String param6) {
		this.param6 = param6;
	}

	public String getParam7() {
		return param7;
	}

	public void setParam7(String param7) {
		this.param7 = param7;
	}

	public Draw() { 
	}

	public Draw(long userid, String username, String memberid, long accountid,
			double balance, double min, double max,double prePrice) { 
		this.userid = userid;
		this.username = username;
		this.memberid = memberid;
		this.accountid = accountid;
		this.balance = balance;
		this.min = min;
		this.max = max;
		this.prePrice = prePrice;
	}
	public Draw(long userid, String username, String memberid, long accountid,
			double balance, double min, double max,double prePrice,long lastTime,double levelScore) { 
		this.userid = userid;
		this.username = username;
		this.memberid = memberid;
		this.accountid = accountid;
		this.balance = balance;
		this.min = min;
		this.max = max;
		this.prePrice = prePrice;
		this.lastTime=lastTime;
		this.levelScore=levelScore;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public long getAccountid() {
		return accountid;
	}

	public void setAccountid(long accountid) {
		this.accountid = accountid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public double getPrePrice() {
		return prePrice;
	}

	public void setPrePrice(double prePrice) {
		this.prePrice = prePrice;
	}

	public double getLevelScore() {
		return DoubleUtils.doubleCheck(levelScore, 2);
	}

	public void setLevelScore(double levelScore) {
		this.levelScore = levelScore;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public double getBalance_zq() {
		return balance_zq;
	}

	public void setBalance_zq(double balance_zq) {
		this.balance_zq = balance_zq;
	}

	public double getFee1() {
		return fee1;
	}

	public void setFee1(double fee1) {
		this.fee1 = fee1;
	}

	public Date getLast() {
		return last;
	}

	public void setLast(Date last) {
		this.last = last;
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
