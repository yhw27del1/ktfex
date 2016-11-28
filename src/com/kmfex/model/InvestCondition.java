package com.kmfex.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 投资投标约束
 * @author yhw   
 */
@Entity
@Table(name = "t_Invest_Condition",schema="KT")
public class InvestCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532433426136632162L;

	private String id;
	
	private Double lowestMoney;  //单笔  最低投标额
	
	private Double highPercent;   //最高投标额百分比
	
	private String memo;

	private MemberLevel memberLevel;//会员级别
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public Double getLowestMoney() {
		return lowestMoney;
	}

	public Double getHighPercent() {
		return highPercent;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLowestMoney(Double lowestMoney) {
		this.lowestMoney = lowestMoney;
	}

	public void setHighPercent(Double highPercent) {
		this.highPercent = highPercent;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@OneToOne
	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}
	
}
