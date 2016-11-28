package com.kmfex.zhaiquan.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kmfex.model.InvestRecord;
import com.wisdoor.core.model.User;
/**
 * 债权转让规则
 * @author  hwy
 */
@Entity
@Table(name="zq_BuySellRule",schema="KT")
public class ZQBuySellRule  implements Serializable{  
	
	private static final long serialVersionUID = 1L;
	
	private String id;

	//启用、停用
	private boolean enable = true;
	
	//启用日期
	private Date createDate;
	//停用日期
	private Date endDate;
	
	//允许转让包类型
	private int term;  //包期限  月数
	
	private int days;   //转让间隔天数
	
	private int overdue;   //逾期天数  大于该天数不允许转让
	
	
	
	
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isEnable() {
		return enable;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public int getTerm() {
		return term;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	
	public int getDays() {
		return days;
	}

	public int getOverdue() {
		return overdue;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}
	
 
	
	
	
	
}
