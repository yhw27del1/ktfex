package com.kmfex.webservice.vo;
 
import java.util.Date; 

/**
 * 资金明细
 */ 
@SuppressWarnings("serial")
public class AccountDealVo  implements java.io.Serializable{   
	
	private String id;
	private Date createDate = new Date();
 	private double preMoney;//交易前金额
	private double money;//交易金额
	private double nextMoney;//交易后金额 
	private double bj = 0d;//本金
	private double lx = 0d;//利息  
	public String bankAccount; 
	
	public void setId(String id) {
		this.id = id;
	} 
	
	public String getId() {
		return id;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	} 
	 
	public void setMoney(double money) {
		this.money = money;
	}
	public double getMoney() {
		return money;
	}
	public void setPreMoney(double preMoney) {
		this.preMoney = preMoney;
	}
	public double getPreMoney() {
		return preMoney;
	}
	public void setNextMoney(double nextMoney) {
		this.nextMoney = nextMoney;
	}
	public double getNextMoney() {
		return nextMoney;
	}
 
	 
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public void setBj(double bj) {
		this.bj = bj;
	}
	public double getBj() {
		return bj;
	}
	public void setLx(double lx) {
		this.lx = lx;
	}
	public double getLx() {
		return lx;
	}
 
}
