package com.kmfex.hxbank;

import java.util.Date;
import java.util.List;

import com.google.gson.GsonBuilder;

public class HxbankParam {
	private String id;
	private String code;
	private String merchantTrnxNo;//交易市场流水号
	private String bankTxSerNo;//银行流水号
	private String accountNo;//子账号，6位
	private String accountName;//子账名称
	private String merAccountNo;//交易市场摊位号，40位之内
	private String prop = "1";//交易商性质或子账号性质，1位:0企业，1个人
	private double amt = 0.0;//余额；入金金额；出金金额；发生额
	private double amtUse = 0.0;//可用余额
	private String linkMan;//联系人，20位
	private String phone;//联系电话，20位
	private String mobile;//移动电话，20位
	private String address;//移动电话，100位
	
	private String result = "1";//审核结果,1位:0拒绝,1通过；出金审核结果发送
	
	
	private String batchNo = "1";//批次号,交易市场批次号
	private String flag = "1";//借贷标识:1为‘借’,2位‘贷’；type为02时必须输入，但不验证字段值
	private String type = "01";//交易类型:01为正常交易(仅对子账号进行借贷)，02为冻结资金
	
	
	private String relaAcct;//关联帐号
	private String relaAcctName;//关联账户名
	private String isOtherBank = "1";//他行标识,1位:0本行,1跨行;只能填写1
	private String relaAcctBank;//关联账户开户行
	private String relaAcctBankAddr;//关联账户开户行地址
	private String relaAcctBankCode;//关联账户开户行支付系统行号
	private String zipCode;//邮政编码
	private String lawName;//法人名称
	private String lawType;//证件类型,1位:1个人身份证,2:军人证,警官证,3:临时证件,4:户口本,5:护照,6:其他
	private String lawNo;//证件号码
	private String noteFlag = "0";//是否需要短信通知,1位:0不需要,1需要
	private String notePhone;//短信通知手机号码,当需要短信通知时为必填
	private String checkFlag;//是否需要复核,子账号性质为企业时必输入即prop=0时checkFlag必须输入
	
	
	private String iostart = "1";//入金方式,1位:1为他行现金汇款，2为他行转账汇款
	private String personName;//汇款人姓名
	private String date;//汇款日期，8位:yyyyHHdd
	private Date checkDate;
	private String bankName;//汇款银行
	private String outAccount;//汇款帐号
	private String email;
	private List<HxbankParam_deal> deals;
	private double balance = 0.0;
	private double balanceUse = 0.0;
	public String getMerchantTrnxNo() {
		return merchantTrnxNo;
	}
	public void setMerchantTrnxNo(String merchantTrnxNo) {
		this.merchantTrnxNo = merchantTrnxNo;
	}
	public String getBankTxSerNo() {
		return bankTxSerNo;
	}
	public void setBankTxSerNo(String bankTxSerNo) {
		this.bankTxSerNo = bankTxSerNo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMerAccountNo() {
		return merAccountNo;
	}
	public void setMerAccountNo(String merAccountNo) {
		this.merAccountNo = merAccountNo;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
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
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	public String getRelaAcct() {
		return relaAcct;
	}
	public void setRelaAcct(String relaAcct) {
		this.relaAcct = relaAcct;
	}
	public String getRelaAcctName() {
		return relaAcctName;
	}
	public void setRelaAcctName(String relaAcctName) {
		this.relaAcctName = relaAcctName;
	}
	public String getIsOtherBank() {
		return isOtherBank;
	}
	public void setIsOtherBank(String isOtherBank) {
		this.isOtherBank = isOtherBank;
	}
	public String getRelaAcctBank() {
		return relaAcctBank;
	}
	public void setRelaAcctBank(String relaAcctBank) {
		this.relaAcctBank = relaAcctBank;
	}
	public String getRelaAcctBankAddr() {
		return relaAcctBankAddr;
	}
	public void setRelaAcctBankAddr(String relaAcctBankAddr) {
		this.relaAcctBankAddr = relaAcctBankAddr;
	}
	public String getRelaAcctBankCode() {
		return relaAcctBankCode;
	}
	public void setRelaAcctBankCode(String relaAcctBankCode) {
		this.relaAcctBankCode = relaAcctBankCode;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLawName() {
		return lawName;
	}
	public void setLawName(String lawName) {
		this.lawName = lawName;
	}
	public String getLawType() {
		return lawType;
	}
	public void setLawType(String lawType) {
		this.lawType = lawType;
	}
	public String getLawNo() {
		return lawNo;
	}
	public void setLawNo(String lawNo) {
		this.lawNo = lawNo;
	}
	public String getNoteFlag() {
		return noteFlag;
	}
	public void setNoteFlag(String noteFlag) {
		this.noteFlag = noteFlag;
	}
	public String getNotePhone() {
		return notePhone;
	}
	public void setNotePhone(String notePhone) {
		this.notePhone = notePhone;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getIostart() {
		return iostart;
	}
	public void setIostart(String iostart) {
		this.iostart = iostart;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getOutAccount() {
		return outAccount;
	}
	public void setOutAccount(String outAccount) {
		this.outAccount = outAccount;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	public String getJSON(HxbankParam p){
		return new GsonBuilder().create().toJson(p);
	}
	public HxbankParam toParam(String json){
		return new GsonBuilder().create().fromJson(json, HxbankParam.class);
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setDeals(List<HxbankParam_deal> deals) {
		this.deals = deals;
	}
	public List<HxbankParam_deal> getDeals() {
		return deals;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalanceUse(double balanceUse) {
		this.balanceUse = balanceUse;
	}
	public double getBalanceUse() {
		return balanceUse;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
}
