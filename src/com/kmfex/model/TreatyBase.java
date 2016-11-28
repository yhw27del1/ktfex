package com.kmfex.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
/**
 * 协议实体
 * @author eclipse
 *
 */
@Entity
@Table(name = "t_TreatyBase",schema="KT")
public class TreatyBase implements Serializable{
	private static final long serialVersionUID = -6383041129032945617L;
	
	private String id;
	/**
	 * 对应融资项目
	 */
	private FinancingBase financingBase;
	/**
	 * 签定时间
	 */
	private Date time;
	/**
	 * 投资记录
	 */
	private List<InvestRecord> investRecords;
	/**
	 * 所属用户
	 */
	private MemberBase memberBase;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@ManyToOne
	public FinancingBase getFinancingBase() {
		return financingBase;
	}
	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Transient
	public List<InvestRecord> getInvestRecords() {
		return investRecords;
	}
	public void setInvestRecords(List<InvestRecord> investRecords) {
		this.investRecords = investRecords;
	}
	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}
	@ManyToOne
	public MemberBase getMemberBase() {
		return memberBase;
	}

	
	
}
