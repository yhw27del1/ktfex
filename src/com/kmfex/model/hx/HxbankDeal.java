package com.kmfex.model.hx;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;
/**
 * @author linuxp
 * */
@Entity
@Table(name = "hx_bank_deal",schema="KT")
public class HxbankDeal {
	private String id;
	private Date createDate = new Date();
	private String name;//交易名称，比如:签到
	private String trnxCode;//交易码，比如:DZ015
	private String merchantTrnxNo;//交易市场流水号
	private String bankTxSerNo;//银行流水号
	private User operator;//操作者
	private User user;//所属者，如：入金者，出金者
	private User checkUser;//审核者，主要是出金审核结果发送的审核者
	private String message;
	private boolean success;
	private String accountNo;//子账号，6位
	private String accountName;//子账名称
	private String prop = "1";//交易商性质或子账号性质，1位:0企业，1个人
	private String merAccountNo;//交易市场摊位号，40位之内
	//出入金明细核对，其循环域对象inoutDetails，存在子表中
	private double amtOut = 0.0;//出金总金额
	private int countOut = 0;//出金总笔数
	private double amtIn = 0.0;//入金总金额
	private int countIn = 0;//入金总笔数
	//清算，其循环域对象liquidations，存在子表中
	//对账，其循环域对象accountChecks，存在子表中
	private String workDay;//工作日，汇款日期等yyyymmdd
	private Date checkDate;//清算，核对，出金审核结果发送的日期(工作日的日期格式)
	private String batchNo;//批次号,交易市场批次号
	//子账户签约
	private double amt = 0.0;//余额；入金金额；出金金额；发生额
	private double amtUse = 0.0;//可用余额
	private String linkMan;//联系人，20位
	private String phone;//联系电话，20位
	private String mobile;//移动电话，20位
	private String address;//移动电话，100位
	private String dealerOperNo;//登录银行系统操作员代码
	//出金审核结果发送
	private String result = "2";//审核结果,1位:0拒绝,1通过；2待审核
	
	private String iostart = "1";//入金方式,1位:1为他行现金汇款，2为他行转账汇款
	private String personName;//汇款人姓名
	private String bankName;//汇款银行
	private String outAccount;//汇款帐号
	
	private double balance = 0.0;//银行子账号当前余额
	private double balanceUse = 0.0;//银行子账号当前可用余额
	
	private double bankAmt = 0.0;//银行总余额
	private double bankAmtUse = 0.0;//银行可用余额
	
	private List<HxbankDealSub> subs;
	
	public HxbankDeal(){
		
	}
	
	public HxbankDeal(String name,String trnxCode){
		this.name = name;
		this.trnxCode = trnxCode;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTrnxCode() {
		return trnxCode;
	}
	public void setTrnxCode(String trnxCode) {
		this.trnxCode = trnxCode;
	}
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
	@ManyToOne
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
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
	public double getAmtOut() {
		return amtOut;
	}
	public void setAmtOut(double amtOut) {
		this.amtOut = amtOut;
	}
	public int getCountOut() {
		return countOut;
	}
	public void setCountOut(int countOut) {
		this.countOut = countOut;
	}
	public double getAmtIn() {
		return amtIn;
	}
	public void setAmtIn(double amtIn) {
		this.amtIn = amtIn;
	}
	public int getCountIn() {
		return countIn;
	}
	public void setCountIn(int countIn) {
		this.countIn = countIn;
	}
	public String getWorkDay() {
		return workDay;
	}
	public void setWorkDay(String workDay) {
		this.workDay = workDay;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getBalanceUse() {
		return balanceUse;
	}
	public void setBalanceUse(double balanceUse) {
		this.balanceUse = balanceUse;
	}

	public void setDealerOperNo(String dealerOperNo) {
		this.dealerOperNo = dealerOperNo;
	}

	public String getDealerOperNo() {
		return dealerOperNo;
	}

	public void setBankAmt(double bankAmt) {
		this.bankAmt = bankAmt;
	}

	public double getBankAmt() {
		return bankAmt;
	}

	public void setBankAmtUse(double bankAmtUse) {
		this.bankAmtUse = bankAmtUse;
	}

	public double getBankAmtUse() {
		return bankAmtUse;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public String getProp() {
		return prop;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setIostart(String iostart) {
		this.iostart = iostart;
	}

	public String getIostart() {
		return iostart;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonName() {
		return personName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setOutAccount(String outAccount) {
		this.outAccount = outAccount;
	}

	public String getOutAccount() {
		return outAccount;
	}

	public void setCheckUser(User checkUser) {
		this.checkUser = checkUser;
	}

	@ManyToOne
	public User getCheckUser() {
		return checkUser;
	}

	public void setSubs(List<HxbankDealSub> subs) {
		this.subs = subs;
	}

	@Transient
	public List<HxbankDealSub> getSubs() {
		return subs;
	}

}
