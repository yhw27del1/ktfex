package com.kmfex;
 
import java.util.Date; 

/**
 * 资金明细
 */ 
@SuppressWarnings("serial")
public class AccountDealVo  implements java.io.Serializable{   
	 
	private Date createDate = new Date();
	
 	private double preMoney;//交易前金额
	private double money;//交易金额
	private double nextMoney;//交易后金额 
 
	private String type;//类型
	private String note;//备注
	private String zf;
	
	public AccountDealVo(){}
	
	public AccountDealVo(Date createDate, double preMoney, double money,
			double nextMoney) { 
		this.createDate = createDate;
		this.preMoney = preMoney;
		this.money = money;
		this.nextMoney = nextMoney;
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
 


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getZf() {
		return zf;
	}

	public void setZf(String zf) {
		this.zf = zf;
	}

 
 
}
