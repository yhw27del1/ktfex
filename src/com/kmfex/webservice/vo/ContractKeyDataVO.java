package com.kmfex.webservice.vo;

import java.io.Serializable;
import java.util.Date;
 

public class ContractKeyDataVO implements Serializable{
	private static final long serialVersionUID = 4077967141506893493L;
	private String id;
	/**
	 * 合同编号
	 */
	private String contract_numbers;
	/**
	 * 甲方名称
	 */
	private String first_party;
	/**
	 * 甲方身份证号码
	 */
	private String first_id;
	/**
	 * 甲方会员代码
	 */
	private String first_party_code;
	/**
	 * 甲方签署时间
	 */
	private Date investor_make_sure;
	/**
	 * 乙方名称
	 */
	private String second_party;
	/**
	 * 乙方营业执照
	 */
	private String second_party_yyzz;
	/**
	 * 乙方会员代码
	 */
	private String second_party_code;
	/**
	 * 乙方签署时间
	 */
	private Date financier_make_sure;
	/**
	 * 乙方组织机构代码
	 */
	private String second_party_zzjg;
	/**
	 * 合同发布时间
	 */
	
	private Date fbsj;
	/**
	 * 借款(阿拉伯数字)
	 */
	private double loan_allah = 0d;
	/**
	 * 借款（大写）
	 */
	private String loan_uppercase;
	
	/**
	 * 贷款期限
	 */
	private int loan_term = 0;
	/**
	 * 借款期限-起始日期
	 */
	private Date start_date;
	
	/**
	 * 借款期限-截止日期
	 */
	private Date end_date;
	/**
	 * 利率
	 */
	private double interest_rate = 0d;
	
	/**利息总额(阿拉伯数字)**/
	
	private double interest_allah = 0d;
	
	/**利息总额(汉字大写)**/
	
	private String interest_uppercase;
	
	/**还款方式*/
	private int payment_method = 0;
	
	/**
	 * 按月等额本息还款(本金_阿拉伯数字 )
	 */
	
	private double principal_allah_monthly = 0d;
	/**
	 * 按月等额本息还款(本金_大写汉字 )
	 */
	
	private String principal_uppercase_monthly;

	/**
	 * 按月等额本息还款(利息_阿拉伯数字)
	 */
	
	private double interest_allah_monthly = 0d;
	/**
	 * 按月等额本息还款(利息_大写汉字)
	 */
	
	private String interest_uppercase_monthly;
	
	/**
	 * 每月还款日期
	 */
	private int repayment_term = 0;
	/**
	 * 每月应还金额(阿拉伯数字)
	 */
	
	private double repayment_amount_monthly_allah = 0d;
	/**
	 * 每月应还金额(汉字)
	 */
	
	private String repayment_amount_monthly_uppercase;
	
	/**
	 * 乙方借款的具体用途为
	 */
	
	private String purpose;
	/**
	 * 风险管理费(阿拉伯数字)
	 */
	
	private double riskmanagement_cost_allah = 0d;
	/**
	 * 风险管理费(汉字)
	 */
	
	private String riskmanagement_cost_uppercase;
	
	/**
	 * 融资服务费(阿拉伯数字)
	 */
	
	private double service_cost_allah = 0d;
	/**
	 * 融资服务费(汉字)
	 */
	
	private String service_cost_uppercase;
	/**
	 * 担保费
	 */
	
	private double dbf = 0d;
	/**
	 * 担保费-大写 
	 */
	
	private String dbf_dx;
	/**
	 * 保证金
	 */
	private double bzj = 0d;
	/**
	 * 保证金-大写
	 */
	private String bzj_dx;
	
	/**
	 * 融资服务费+风险管理费(阿拉伯数字)
	 */
	private double overallcost_allah = 0d;
	/**
	 * 融资服务费+风险管理费(汉字)
	 */
	
	private String overallcost_uppercase;
	
	/**
	 * 签署日期
	 */
	private Date signdate;
	
	
	
	
	public String getDbf_dx() {
		return dbf_dx;
	}
	public void setDbf_dx(String dbfDx) {
		dbf_dx = dbfDx;
	}
	public String getBzj_dx() {
		return bzj_dx;
	}
	public void setBzj_dx(String bzjDx) {
		bzj_dx = bzjDx;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 合同编号
	 */
	public String getContract_numbers() {
		return contract_numbers;
	}
	/**
	 * 合同编号
	 */
	public void setContract_numbers(String contractNumbers) {
		contract_numbers = contractNumbers;
	}
	

	/**
	 * 借款(阿拉伯数字)
	 */
	public void setLoan_allah(double loan_allah) {
		this.loan_allah = loan_allah;
	}
	/**
	 * 借款(阿拉伯数字)
	 */
	public double getLoan_allah() {
		return loan_allah;
	}
	/**
	 * 借款（大写）
	 */
	public void setLoan_uppercase(String loan_uppercase) {
		this.loan_uppercase = loan_uppercase;
	}
	/**
	 * 借款（大写）
	 */
	public String getLoan_uppercase() {
		return loan_uppercase;
	}
	/**
	 * 贷款期限
	 */
	public void setLoan_term(int loan_term) {
		this.loan_term = loan_term;
	}
	/**
	 * 贷款期限
	 */
	public int getLoan_term() {
		return loan_term;
	}
	/**
	 * 借款期限-起始日期
	 */
	public Date getStart_date() {
		return start_date;
	}
	/**
	 * 借款期限-起始日期
	 */
	public void setStart_date(Date startDate) {
		start_date = startDate;
	}
	/**
	 * 借款期限-截止日期
	 */
	public Date getEnd_date() {
		return end_date;
	}
	/**
	 * 借款期限-截止日期
	 */
	public void setEnd_date(Date endDate) {
		end_date = endDate;
	}
	/**
	 * 利率
	 */
	public void setInterest_rate(double interest_rate) {
		this.interest_rate = interest_rate;
	}
	/**
	 * 利率
	 */
	public double getInterest_rate() {
		return interest_rate;
	}
	/**利息总额(阿拉伯数字)**/
	public double getInterest_allah() {
		return interest_allah;
	}
	/**利息总额(阿拉伯数字)**/
	public void setInterest_allah(double interestAllah) {
		interest_allah = interestAllah;
	}
	/**利息总额(汉字大写)**/
	public void setInterest_uppercase(String interest_uppercase) {
		this.interest_uppercase = interest_uppercase;
	}
	/**利息总额(汉字大写)**/
	public String getInterest_uppercase() {
		return interest_uppercase;
	}
	/**还款方式*/
	public void setPayment_method(int payment_method) {
		this.payment_method = payment_method;
	}
	/**还款方式*/
	public int getPayment_method() {
		return payment_method;
	}
	/**
	 * 按月等额本息还款(本金_阿拉伯数字)
	 */
	public double getPrincipal_allah_monthly() {
		return principal_allah_monthly;
	}
	/**
	 * 按月等额本息还款(本金_阿拉伯数字)
	 */
	public void setPrincipal_allah_monthly(double principalAllahMonthly) {
		principal_allah_monthly = principalAllahMonthly;
	}
	
	/**
	 * 按月等额本息还款(利息)
	 */
	public double getInterest_allah_monthly() {
		return interest_allah_monthly;
	}
	/**
	 * 按月等额本息还款(利息)
	 */
	public void setInterest_allah_monthly(double interestAllahMonthly) {
		interest_allah_monthly = interestAllahMonthly;
	}
	/**
	 * 按月等额本息还款(本金_大写汉字 )
	 */
	public void setPrincipal_uppercase_monthly(String principal_uppercase_monthly) {
		this.principal_uppercase_monthly = principal_uppercase_monthly;
	}
	/**
	 * 按月等额本息还款(本金_大写汉字 )
	 */
	public String getPrincipal_uppercase_monthly() {
		return principal_uppercase_monthly;
	}
	/**
	 * 按月等额本息还款(利息_大写汉字)
	 */
	public void setInterest_uppercase_monthly(String interest_uppercase_monthly) {
		this.interest_uppercase_monthly = interest_uppercase_monthly;
	}
	/**
	 * 按月等额本息还款(利息_大写汉字)
	 */
	public String getInterest_uppercase_monthly() {
		return interest_uppercase_monthly;
	}
	/**
	 * 每月还款日期
	 */
	public void setRepayment_term(int repayment_term) {
		this.repayment_term = repayment_term;
	}
	/**
	 * 每月还款日期
	 */
	public int getRepayment_term() {
		return repayment_term;
	}
	/**
	 * 每月应还金额(阿拉伯数字)
	 */
	public double getRepayment_amount_monthly_allah() {
		return repayment_amount_monthly_allah;
	}
	/**
	 * 每月应还金额(阿拉伯数字)
	 */
	public void setRepayment_amount_monthly_allah(double repaymentAmountMonthlyAllah) {
		repayment_amount_monthly_allah = repaymentAmountMonthlyAllah;
	}
	/**
	 * 每月应还金额(汉字)
	 */
	public void setRepayment_amount_monthly_uppercase(String repayment_amount_monthly_uppercase) {
		this.repayment_amount_monthly_uppercase = repayment_amount_monthly_uppercase;
	}
	/**
	 * 每月应还金额(汉字)
	 */
	public String getRepayment_amount_monthly_uppercase() {
		return repayment_amount_monthly_uppercase;
	}
	/**
	 * 乙方借款的具体用途为
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * 乙方借款的具体用途为
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * 风险管理费(阿拉伯数字)
	 */
	public void setRiskmanagement_cost_allah(double riskmanagement_cost_allah) {
		this.riskmanagement_cost_allah = riskmanagement_cost_allah;
	}
	/**
	 * 风险管理费(阿拉伯数字)
	 */
	public double getRiskmanagement_cost_allah() {
		return riskmanagement_cost_allah;
	}
	/**
	 * 风险管理费(汉字)
	 */
	public void setRiskmanagement_cost_uppercase(String riskmanagement_cost_uppercase) {
		this.riskmanagement_cost_uppercase = riskmanagement_cost_uppercase;
	}
	/**
	 * 风险管理费(汉字)
	 */
	public String getRiskmanagement_cost_uppercase() {
		return riskmanagement_cost_uppercase;
	}
	/**
	 * 融资服务费(阿拉伯数字)
	 */
	public void setService_cost_allah(double service_cost_allah) {
		this.service_cost_allah = service_cost_allah;
	}
	/**
	 * 融资服务费(阿拉伯数字)
	 */
	public double getService_cost_allah() {
		return service_cost_allah;
	}
	/**
	 * 融资服务费(汉字)
	 */
	public void setService_cost_uppercase(String service_cost_uppercase) {
		this.service_cost_uppercase = service_cost_uppercase;
	}
	/**
	 * 融资服务费(汉字)
	 */
	public String getService_cost_uppercase() {
		return service_cost_uppercase;
	}
	/**
	 * 融资服务费+风险管理费(阿拉伯数字)
	 */
	public double getOverallcost_allah() {
		return overallcost_allah;
	}
	/**
	 * 融资服务费+风险管理费(阿拉伯数字)
	 * @param overallcostAllah
	 */
	public void setOverallcost_allah(double overallcostAllah) {
		overallcost_allah = overallcostAllah;
	}
	/**
	 * 融资服务费+风险管理费(汉字)
	 * @return
	 */
	public String getOverallcost_uppercase() {
		return overallcost_uppercase;
	}
	/**
	 * 融资服务费+风险管理费(汉字)
	 * @param overallcostUppercase
	 */
	public void setOverallcost_uppercase(String overallcostUppercase) {
		overallcost_uppercase = overallcostUppercase;
	}
	/**
	 * 甲方会员代码
	 */
	public String getFirst_party_code() {
		return first_party_code;
	}
	/**
	 * 甲方会员代码
	 */
	public void setFirst_party_code(String firstPartyCode) {
		first_party_code = firstPartyCode;
	}
	/**
	 * 乙方会员代码
	 */
	public String getSecond_party_code() {
		return second_party_code;
	}
	/**乙方会员代码
	 */
	public void setSecond_party_code(String secondPartyCode) {
		second_party_code = secondPartyCode;
	}
	
	public String getSecond_party_zzjg() {
		return second_party_zzjg;
	}
	public void setSecond_party_zzjg(String secondPartyZzjg) {
		second_party_zzjg = secondPartyZzjg;
	}
	/**
	 * 签署日期
	 */
	public void setSigndate(Date signdate) {
		this.signdate = signdate;
	}
	/**
	 * 签署日期
	 */
	public Date getSigndate() {
		return signdate;
	}
	
	public Date getFbsj() {
		return fbsj;
	}
	public void setFbsj(Date fbsj) {
		this.fbsj = fbsj;
	}
	public void setInvestor_make_sure(Date investor_make_sure) {
		this.investor_make_sure = investor_make_sure;
	}
	public Date getInvestor_make_sure() {
		return investor_make_sure;
	}
	public void setFinancier_make_sure(Date financier_make_sure) {
		this.financier_make_sure = financier_make_sure;
	}
	public Date getFinancier_make_sure() {
		return financier_make_sure;
	}
	public void setDbf(double dbf) {
		this.dbf = dbf;
	}
	public double getDbf() {
		return dbf;
	}
	public void setBzj(double bzj) {
		this.bzj = bzj;
	}
	public double getBzj() {
		return bzj;
	}
	public void setFirst_id(String first_id) {
		this.first_id = first_id;
	}
	public String getFirst_id() {
		return first_id;
	}
	public void setSecond_party_yyzz(String second_party_yyzz) {
		this.second_party_yyzz = second_party_yyzz;
	}
	public String getSecond_party_yyzz() {
		return second_party_yyzz;
	}
	public String getFirst_party() {
		return first_party;
	}
	public void setFirst_party(String firstParty) {
		first_party = firstParty;
	}
	public String getSecond_party() {
		return second_party;
	}
	public void setSecond_party(String secondParty) {
		second_party = secondParty;
	}
	
	
	
}
