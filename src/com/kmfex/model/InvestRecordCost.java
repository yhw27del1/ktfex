package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


/**
 * 投资投标费用明细
 * @author yhw   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Invest_Record_Cost",schema="KT")
public class InvestRecordCost implements Serializable{
	
	private String id;
	private InvestRecord  investRecord;   //投标记录
	
	private double tzfwf =0d;//投资服务费
	private String tzfwf_daxie;
	
    private double   sxf =0d;    //手续费
    private double   yhs =0d;    //印花税
    
    /**
     * fee1针对兴易贷--投资人管理费总计
     * 普通会员投资人每笔利息收入的10%
     * 高级会员投资人每笔利息收入的9%
     * VIP会员投资人每笔利息收入的8% 
     */
    private double   fee1 =0d;   
    private double   fee2 =0d;   //其它费2
    private double   realAmount =0d;   //实际费用             投资记录的投标额+手续费+印花税等费用
    
    private Date  createDate;

    
    @Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
    
    @OneToOne
	public InvestRecord getInvestRecord() {
		return investRecord;
	}

	public double getSxf() {
		return sxf;
	}

	public double getYhs() {
		return yhs;
	}

	public double getFee1() {
		return fee1;
	}

	public double getFee2() {
		return fee2;
	}

	public double getRealAmount() {
		return realAmount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInvestRecord(InvestRecord investRecord) {
		this.investRecord = investRecord;
	}

	public void setSxf(double sxf) {
		this.sxf = sxf;
	}

	public void setYhs(double yhs) {
		this.yhs = yhs;
	}

	public void setFee1(double fee1) {
		this.fee1 = fee1;
	}

	public void setFee2(double fee2) {
		this.fee2 = fee2;
	}

	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setTzfwf(double tzfwf) {
		this.tzfwf = tzfwf;
	}

	public double getTzfwf() {
		return tzfwf;
	}

	public void setTzfwf_daxie(String tzfwf_daxie) {
		this.tzfwf_daxie = tzfwf_daxie;
	}

	@Transient
	public String getTzfwf_daxie() {
		return tzfwf_daxie;
	}
	

}
