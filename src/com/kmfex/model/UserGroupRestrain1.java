package com.kmfex.model;

import javax.persistence.Entity;
import javax.persistence.Table;
 
/**
 * 组成员约束表1（投标次数、最大金额控制）
 * @author   
 */ 
@Entity 
@Table(name="t_UserGroupRestrain1",schema="KT")
public class UserGroupRestrain1   extends UserGroup{ 
 
	private static final long serialVersionUID = -3616488997681539681L;


	/*
	 *允许最大投标次数
	 */
	private int investMaxCount = 2;
	
	
	/*
	 *统计某投资人投标次数
	 */
	private int investCount = 0;
	
	
	/*
	 *单笔允许最大金额 
	 */
	private double investMaxMoney = 2000;
	
 
 



	public UserGroupRestrain1(int investMaxCount, double investMaxMoney) { 
		this.investMaxCount = investMaxCount;
		this.investMaxMoney = investMaxMoney;
	}



	public UserGroupRestrain1() { 
	} 
 
	

	public int getInvestMaxCount() {
		return investMaxCount;
	}

	public void setInvestMaxCount(int investMaxCount) {
		this.investMaxCount = investMaxCount;
	}

	public int getInvestCount() {
		return investCount;
	}

	public void setInvestCount(int investCount) {
		this.investCount = investCount;
	}

	public double getInvestMaxMoney() {
		return investMaxMoney;
	}

	public void setInvestMaxMoney(double investMaxMoney) {
		this.investMaxMoney = investMaxMoney;
	}



 
}
