package com.kmfex.hxbank;

import java.util.Date;

public class HxbankParam_deal {
	private long user_id = 0;
	//清算,对账的循环域
	private String accountNo;//子账号，6位
	private String merAccountNo;//交易市场摊位号，40位之内
	private String accountName;//子账户名称
	private String flag = "1";//借贷标识:1为‘借’,2位‘贷’；type为02时必须输入，但不验证字段值
	private String type = "01";//交易类型:01为正常交易(仅对子账号进行借贷)，02为冻结资金
	private double amt_fasheng = 0.0;//用于清算时传递发生额
	//对账报文
	private double amt = 0.0;//余额；入金金额；出金金额；发生额；用于对账时传递发生额
	private double amtUse = 0.0;
	private double interests = 0.0;
	private String remark;
	private Date checkDate;
	private int bishu = 0;
	
	private double amtFrozen = 0.0;
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getMerAccountNo() {
		return merAccountNo;
	}
	public void setMerAccountNo(String merAccountNo) {
		this.merAccountNo = merAccountNo;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public double getAmtUse() {
		return amtUse;
	}
	public void setAmtUse(double amtUse) {
		this.amtUse = amtUse;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setInterests(double interests) {
		this.interests = interests;
	}
	public double getInterests() {
		return interests;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setAmt_fasheng(double amt_fasheng) {
		this.amt_fasheng = amt_fasheng;
	}
	public double getAmt_fasheng() {
		return amt_fasheng;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setAmtFrozen(double amtFrozen) {
		this.amtFrozen = amtFrozen;
	}
	public double getAmtFrozen() {
		return amtFrozen;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setBishu(int bishu) {
		this.bishu = bishu;
	}
	public int getBishu() {
		return bishu;
	}
}
