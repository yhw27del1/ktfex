package com.kmfex;
/**
 * 投标VO
 * @author linuxp
 */
public class InvestVO {
	private double money;
	private String financingId;//融资项目id
	private boolean canInvest = false;//这次投标能进行吗？
	private double maxMoney = 0d;//投标能进行，最大投标金额
	private String maxMoney_daxie;
	private double minMoney= 0d;//投标能进行，最小投标金额
	private String minMoney_daxie;
	
	private double hasMoney = 0d;
	private String hasMoney_daxie;
	private boolean investFlag = false;
	private String msg;
	public void setCanInvest(boolean canInvest) {
		this.canInvest = canInvest;
	}
	public boolean isCanInvest() {
		return canInvest;
	}
	public void setMaxMoney(double maxMoney) {
		this.maxMoney = maxMoney;
	}
	public double getMaxMoney() {
		return maxMoney;
	}
	public void setMinMoney(double minMoney) {
		this.minMoney = minMoney;
	}
	public double getMinMoney() {
		return minMoney;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getMoney() {
		return money;
	}
	public void setFinancingId(String financingId) {
		this.financingId = financingId;
	}
	public String getFinancingId() {
		return financingId;
	}
	public void setInvestFlag(boolean investFlag) {
		this.investFlag = investFlag;
	}
	public boolean isInvestFlag() {
		return investFlag;
	}
	public void setMaxMoney_daxie(String maxMoney_daxie) {
		this.maxMoney_daxie = maxMoney_daxie;
	}
	public String getMaxMoney_daxie() {
		return maxMoney_daxie;
	}
	public void setMinMoney_daxie(String minMoney_daxie) {
		this.minMoney_daxie = minMoney_daxie;
	}
	public String getMinMoney_daxie() {
		return minMoney_daxie;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setHasMoney(double hasMoney) {
		this.hasMoney = hasMoney;
	}
	public double getHasMoney() {
		return hasMoney;
	}
	public void setHasMoney_daxie(String hasMoney_daxie) {
		this.hasMoney_daxie = hasMoney_daxie;
	}
	public String getHasMoney_daxie() {
		return hasMoney_daxie;
	}
}
