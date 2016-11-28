package com.kmfex.webservice.vo;
 
/**
 * 用户帐户
 * @author      
 */  
public class AccountVo  implements java.io.Serializable{  
	private static final long serialVersionUID = 8980082726144578147L;

	private long id;    
  	
	private double balance = 0d; //帐号金额  

	public AccountVo() {  
	}
	
	public AccountVo(long id, double balance) { 
		this.id = id;
		this.balance = balance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
 
	 
	 
}
