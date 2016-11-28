package com.kmfex.model.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;


/**
 *投转贷
 * @author    
 */

@Entity
@Table(name = "t_invest_lend",schema="KT")
public class InvestToLend implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1943730299234124100L;

	private String id;
	
	private User user;
	
	/**
	 * 限制金额
	 */
	private double money = 10000.0;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date createDate = new Date();
	
	private String memo;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	public User getUser() {
		return user;
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

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getMoney() {
		return money;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}
	

	
	
	
}
