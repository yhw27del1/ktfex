package com.kmfex.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 融资项目总合同，记录：总本，总息
 * @author   
 */
@Entity
@Table(name = "t_Financing_Contract",schema="KT")
public class FinancingContract {
	private String id;
	private FinancingBase financingBase;
	private double bj;//本金总额
	private double lx;//利息总额
	private Date createDate = new Date();
	private Date startDate;
	private Date endDate;
	
	private int loan_term = 0;//贷款期限
	
	//融资项目选择3个月时，这三项金额为0(一次性还款，非按月还款)
	private double bj_monthly = 0d;//按月等额本息还款(本金_阿拉伯数字 )
	private double lx_monthly = 0d;//按月等额本息还款(利息_阿拉伯数字)
	private double repayment_monthly = 0d;//每月应还金额(阿拉伯数字)
	
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
	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}
	@OneToOne
	public FinancingBase getFinancingBase() {
		return financingBase;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setBj_monthly(double bj_monthly) {
		this.bj_monthly = bj_monthly;
	}
	public double getBj_monthly() {
		return bj_monthly;
	}
	public void setLx_monthly(double lx_monthly) {
		this.lx_monthly = lx_monthly;
	}
	public double getLx_monthly() {
		return lx_monthly;
	}
	public void setRepayment_monthly(double repayment_monthly) {
		this.repayment_monthly = repayment_monthly;
	}
	public double getRepayment_monthly() {
		return repayment_monthly;
	}
	public void setLoan_term(int loan_term) {
		this.loan_term = loan_term;
	}
	public int getLoan_term() {
		return loan_term;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getEndDate() {
		return endDate;
	}
}
