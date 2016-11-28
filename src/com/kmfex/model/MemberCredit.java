package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;

/**
 * 会员信用
 * @author yhw   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Member_Credit",schema="KT")
public class MemberCredit implements Serializable{
	
	private String id;
	private MemberBase memberBase;
	private CreditLevel creditLevel;  //信用级别
	private Double creditAmount;      //信用总额度
	private Double currAmount;        //当前可用额度	
	private User createBy;
	private Date createDate;
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	
	@OneToOne
	public MemberBase getMemberBase() {
		return memberBase;
	}
	@ManyToOne
	public CreditLevel getCreditLevel() {
		return creditLevel;
	}
	public Double getCreditAmount() {
		return creditAmount;
	}
	public Double getCurrAmount() {
		return currAmount;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}
	public void setCreditLevel(CreditLevel creditLevel) {
		this.creditLevel = creditLevel;
	}
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}
	public void setCurrAmount(Double currAmount) {
		this.currAmount = currAmount;
	}

	public User getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
