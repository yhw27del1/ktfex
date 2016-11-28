package com.kmfex;
/**
 * 会员账户余额查询及充值VO
 * @author linuxp
 */
public class QueryVO {
	private String id;
	private String account;//交易账户对于的资金账户
	private double balance;
	private double frozenAmount;
	private double chargeAmount;
	private String bank;
	/**
	 * 会员名称
	 */
	private String name;
	private boolean flag;
	private String username;
	private String tip;
	
	private String sign;
	
	public void setId(String id) {
		this.id = id;
	} 
	public String getId() {
		return id;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getBalance() {
		return balance;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public double getChargeAmount() {
		return chargeAmount;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public void setFrozenAmount(double frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	public double getFrozenAmount() {
		return frozenAmount;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSign() {
		return sign;
	}
	
	
	
}
