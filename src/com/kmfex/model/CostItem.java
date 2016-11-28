package com.kmfex.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 收费标准明细
 * @author   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Cost_Item",schema="KT")
public class CostItem implements Serializable{

	private String id;
	private CostBase costBase;    //收费项目
	private MemberType memberTypel;  //会员类型
	private Double money;          //金额
	private Double percent;        //百分比
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	
	@ManyToOne
	public CostBase getCostBase() {
		return costBase;
	}
	@ManyToOne
	public MemberType getMemberTypel() {
		return memberTypel;
	}
	public Double getMoney() {
		return money;
	}
	public Double getPercent() {
		return percent;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCostBase(CostBase costBase) {
		this.costBase = costBase;
	}
	public void setMemberTypel(MemberType memberTypel) {
		this.memberTypel = memberTypel;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
}
