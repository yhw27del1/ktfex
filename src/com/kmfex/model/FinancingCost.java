package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;



/**
 * 融资费用
 *   
 * 增加其他费用字段 other，other_bl,other_tariff
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Financing_Cost",schema="KT")
public class FinancingCost implements Serializable{

	private String id;
	private FinancingBase  financingBase;
	
	private MemberBase financier;  //融资项目信息中的 融资方	
	
	private Double   dbf=0d;      //担保费（最后一次修改的）
	private Double   fxglf=0d;    //风险管理费（按月）（最后一次修改的）
    private Double   rzfwf=0d;    //融资服务费 （最后一次修改的）
    private Double   bzj=0d;      //保证金（最后一次修改的）
    
    private Double   dbf_bl=0d;    //担保费计算比例      
    private Double   fxglf_bl=0d;  //风险管理费计算比例     
    private Double   rzfwf_bl=0d;  //融资服务费计算比例   
    private Double   bzj_bl=0d;    //保证金计算比例     
 
    private int   dbf_tariff=0;      //担保费收费方式(0:一次收费；1：按月收费)  
    private int   fxglf_tariff=0;    //风险管理费收费方式(0:一次收费；1：按月收费)
    private int   rzfwf_tariff=0;    //融资服务费收费方式(0:一次收费；1：按月收费) 
    private int   bzj_tariff=0;      //保证金收费方式(0:一次收费；1：按月收费)
    
    
    
	private Double   dbfOld=0d;      //担保费（系统自动计算 不可修改）
	private Double   fxglfOld=0d;    //风险管理费（按月）（系统自动计算 不可修改）
    private Double   rzfwfOld=0d;    //融资服务费 （系统自动计算 不可修改）
    private Double   bzjOld=0d;      //保证金（系统自动计算 不可修改）
    
	private String   state="0";   //0待核算  1已核算
	
	private String   history;   //修改历史
	
	private String   note;      //核算备注
    
	//融资方费用--兴易贷
    private Double   fee1=0d;   //兴易贷费用--风险管理费：借款初始金额的2.5%，借款发放时一次性扣收
    private Double   fee2=0d;   //兴易贷费用--融资服务费(总计)：借款初始金额的1%/月，按月计收
    private Double   fee3=0d;   //兴易贷费用--担保费(总计)：借款初始金额的0.3%/月，按月计收
    
    private Double   fee1_bl=0d; //兴易贷费用--风险管理费计算比例   
    private Double   fee2_bl=0d;  //兴易贷费用--融资服务费计算比例
    private Double   fee3_bl=0d;  //兴易贷费用--担保费计算比例  
    
    private int   fee1_tariff=0;  //兴易贷费用--风险管理费收费方式(0:一次收费；1：按月收费)
    private int   fee2_tariff=0;  //兴易贷费用--融资服务费收费方式(0:一次收费；1：按月收费)
    private int   fee3_tariff=0; //兴易贷费用--担保费收费方式(0:一次收费；1：按月收费)   
    
    
    private Double   fee4=0d;   //兴易贷费用--还本金(总计)：借款初始金额/借款月数=每月偿还本金数额，由投资人收取
    
    private Double   fee5=0d;  //还利息(总计)：年利率22%，按照等额本金方式按月偿还，由投资人收取
    
    private Double   other=0d;  //其他费用
    
    private Double   other_bl=0d;  
    private int   other_tariff=0;
    
    private Double   fee6=0d;   //兴易贷费用--提前还款手续费：借款初始金额的3%，还款时一次性收取 
    
    
    private Double   fee7=0d;   //席位费、年费
    private Double   fee10=0d;   //信用管理费(按年)
    
    
    
    
    
    private Double   realAmount=0d;   //实际费用             融资项目的 当前融资额-担保费-评估费等费用
   
    private Date    createDate=new Date();
    
	private User auditBy;   //核算人
	
	private Date auditDate;  //核算日期
	
	private User hkBy;   //划款人
	private Date hkDate;//划款日期
	private Map<String[],Double> bankmap;
    
    @Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
    
    @OneToOne
	public FinancingBase getFinancingBase() {
		return financingBase;
	}
    @ManyToOne
	public MemberBase getFinancier() {
		return financier;
	}
	public Double getDbf() {
		return dbf;
	}
 
	public void setId(String id) {
		this.id = id;
	}
	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}
	public void setFinancier(MemberBase financier) {
		this.financier = financier;
	}
	public void setDbf(Double dbf) {
		this.dbf = dbf;
	}
 
	public Double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getFee1() {
		return fee1;
	}

	public Double getFee2() {
		return fee2;
	}

	public void setFee1(Double fee1) {
		this.fee1 = fee1;
	}

	public void setFee2(Double fee2) {
		this.fee2 = fee2;
	}
 

	public Double getFxglf() {
		return fxglf;
	}

	public void setFxglf(Double fxglf) {
		this.fxglf = fxglf;
	}

	public Double getBzj() {
		return bzj;
	}

	public void setBzj(Double bzj) {
		this.bzj = bzj;
	}

	public Double getRzfwf() {
		return rzfwf;
	}

	public void setRzfwf(Double rzfwf) {
		this.rzfwf = rzfwf;
	}

	public Double getDbfOld() {
		return dbfOld;
	}

	public void setDbfOld(Double dbfOld) {
		this.dbfOld = dbfOld;
	} 

	public Double getFxglfOld() {
		return fxglfOld;
	}

	public void setFxglfOld(Double fxglfOld) {
		this.fxglfOld = fxglfOld;
	}

	public Double getRzfwfOld() {
		return rzfwfOld;
	}

	public void setRzfwfOld(Double rzfwfOld) {
		this.rzfwfOld = rzfwfOld;
	}

	public Double getBzjOld() {
		return bzjOld;
	}

	public void setBzjOld(Double bzjOld) {
		this.bzjOld = bzjOld;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
	@ManyToOne
	public User getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(User auditBy) {
		this.auditBy = auditBy;
	}
	
	

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	
	public Date getHkDate() {
		return hkDate;
	}

	public void setHkDate(Date hkDate) {
		this.hkDate = hkDate;
	}

	@ManyToOne
	public User getHkBy() {
		return hkBy;
	}

	public void setHkBy(User hkBy) {
		this.hkBy = hkBy;
	}
	
	public Double getFee3() {
		return fee3;
	}

	public void setFee3(Double fee3) {
		this.fee3 = fee3;
	}

	public Double getFee4() {
		return fee4;
	}

	public void setFee4(Double fee4) {
		this.fee4 = fee4;
	}

	public Double getFee5() {
		return fee5;
	}

	public void setFee5(Double fee5) {
		this.fee5 = fee5;
	}

	public Double getFee6() {
		return fee6;
	}

	public void setFee6(Double fee6) {
		this.fee6 = fee6;
	}

	public Double getFee7() {
		return fee7;
	}

	public void setFee7(Double fee7) {
		this.fee7 = fee7;
	}

	public void setBankmap(Map<String[],Double> bankmap) {
		this.bankmap = bankmap;
	}

	@Transient
	public Map<String[],Double> getBankmap() {
		return bankmap;
	}
	@Column(columnDefinition="float default 0")
	public Double getFee10() {
		return fee10;
	}

	public void setFee10(Double fee10) {
		this.fee10 = fee10;
	}
	@Column(columnDefinition="float default 0")
	public Double getDbf_bl() {
		return dbf_bl;
	}

	public void setDbf_bl(Double dbf_bl) {
		this.dbf_bl = dbf_bl;
	}
	@Column(columnDefinition="float default 0")
	public Double getFxglf_bl() {
		return fxglf_bl;
	}

	public void setFxglf_bl(Double fxglf_bl) {
		this.fxglf_bl = fxglf_bl;
	}
	@Column(columnDefinition="float default 0")
	public Double getRzfwf_bl() {
		return rzfwf_bl;
	}

	public void setRzfwf_bl(Double rzfwf_bl) {
		this.rzfwf_bl = rzfwf_bl;
	}
	@Column(columnDefinition="float default 0")
	public Double getBzj_bl() {
		return bzj_bl;
	}

	public void setBzj_bl(Double bzj_bl) {
		this.bzj_bl = bzj_bl;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getDbf_tariff() {
		return dbf_tariff;
	}

	public void setDbf_tariff(int dbf_tariff) {
		this.dbf_tariff = dbf_tariff;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getFxglf_tariff() {
		return fxglf_tariff;
	}

	public void setFxglf_tariff(int fxglf_tariff) {
		this.fxglf_tariff = fxglf_tariff;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getRzfwf_tariff() {
		return rzfwf_tariff;
	}

	public void setRzfwf_tariff(int rzfwf_tariff) {
		this.rzfwf_tariff = rzfwf_tariff;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getBzj_tariff() {
		return bzj_tariff;
	}

	public void setBzj_tariff(int bzj_tariff) {
		this.bzj_tariff = bzj_tariff;
	}
	@Column(columnDefinition="float default 0")
	public Double getFee1_bl() {
		return fee1_bl;
	}

	public void setFee1_bl(Double fee1_bl) {
		this.fee1_bl = fee1_bl;
	}
	@Column(columnDefinition="float default 0")
	public Double getFee2_bl() {
		return fee2_bl;
	}

	public void setFee2_bl(Double fee2_bl) {
		this.fee2_bl = fee2_bl;
	}
	@Column(columnDefinition="float default 0")
	public Double getFee3_bl() {
		return fee3_bl;
	}

	public void setFee3_bl(Double fee3_bl) {
		this.fee3_bl = fee3_bl;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getFee1_tariff() {
		return fee1_tariff;
	}

	public void setFee1_tariff(int fee1_tariff) {
		this.fee1_tariff = fee1_tariff;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getFee2_tariff() {
		return fee2_tariff;
	}

	public void setFee2_tariff(int fee2_tariff) {
		this.fee2_tariff = fee2_tariff;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getFee3_tariff() {
		return fee3_tariff;
	}

	public void setFee3_tariff(int fee3_tariff) {
		this.fee3_tariff = fee3_tariff;
	}
	
	@Column(columnDefinition="float default 0")
	public Double getOther() {
		return other;
	}

	public void setOther(Double other) {
		this.other = other;
	}
	@Column(columnDefinition="float default 0")
	public Double getOther_bl() {
		return other_bl;
	}

	public void setOther_bl(Double other_bl) {
		this.other_bl = other_bl;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getOther_tariff() {
		return other_tariff;
	}

	public void setOther_tariff(int other_tariff) {
		this.other_tariff = other_tariff;
	}

	 

	
	
}
