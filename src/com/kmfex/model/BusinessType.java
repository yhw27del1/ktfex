package com.kmfex.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


/**
 * 业务类型
 * @author yhw   
 * 2013-07-09 15:09    当id='day'时表示是按日计息的业务类型
 * 2013-08-02 13:04    增加排序字段order
 */
@Entity
@Table(name = "t_Business_Type",schema="KT")
public class BusinessType implements Serializable{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4131188614454946939L;
	private String id;
	private String name;  //类型名称
	private String code;   //类型标识
	private int term;  //期限(月)
	private String returnPattern;  //还款方式
	private int returnTimes; //还款次数
	private double fxglf=0d;//风险管理费
	/**
	 * 还款模式<br/>
	 * 1:{到期一次还本付息}<br/> 
	 * 2:{按月等本等息}<br/> 
	 * 3:{按月还息到期还本}<br/> 
	 * 4:{按月等额本息}
	 */
	private int branch;  
	
	private int order=1; //排序字段  
	
	
	public BusinessType(){}
	
	public BusinessType(String id) { 
		this.id = id;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	
	public int getReturnTimes() {
		return returnTimes;
	}
	
	public void setReturnTimes(int returnTimes) {
		this.returnTimes = returnTimes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    @Transient
	public String getReturnPatternTerm() {
    	if(!"day".equals(id)){  
    		return this.term+"个月("+this.returnPattern+")";
    	}else{
    		return "按日计息("+this.returnPattern+")";
    	}
		
	}

	public String getReturnPattern() {
		return returnPattern;
	}
	
	public void setReturnPattern(String returnPattern) {
		this.returnPattern = returnPattern;
	}

	public double getFxglf() {
		return fxglf;
	}

	public void setFxglf(double fxglf) {
		this.fxglf = fxglf;
	}
	@Column(columnDefinition="int default 0")
	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}
	
	@Column(name="order_",columnDefinition="int default 1")
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	

}
