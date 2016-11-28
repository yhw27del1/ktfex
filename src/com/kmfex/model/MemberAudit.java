package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;

/**
 * 会员审核记录
 * 
 * @author yhw
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Member_Audit", schema = "KT")
public class MemberAudit implements Serializable {

	private String id;
	private MemberBase memberBase;
	private String reasion;
	/**
	 * 审核人
	 * */
	private User auditor;
	private Date additDate;
	private String state; // 2通过 3未通过，含义与MemberBase的state一致

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	@Column(length = 2044)
	public String getReasion() {
		return reasion;
	}

	@ManyToOne
	public User getAuditor() {
		return auditor;
	}

	public Date getAdditDate() {
		return additDate;
	}

	public String getState() {
		return state;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReasion(String reasion) {
		this.reasion = reasion;
	}

	public void setAuditor(User auditor) {
		this.auditor = auditor;
	}

	public void setAdditDate(Date additDate) {
		this.additDate = additDate;
	}

	public void setState(String state) {
		this.state = state;
	}

	@ManyToOne
	public MemberBase getMemberBase() {
		return memberBase;
	}

	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}

}
