package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/*
 * 交易时间 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_kmfextrademarket",schema="KT")
public class KmfexTradeMarket implements Serializable{
	
	private String id;
	private String title = "交易市场开市与休市规则";
	
	/**
	 * 启用状态
	 */
	private boolean enabled;//规则是否启用
	
	private String state;//辅助显示字段，规则是否启用
	private Date createDate = new Date();
	private Date startDate;//启用日期
	private Date endDate;//停用日期
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Transient
	public String getState() {
		if(enabled){
			state = "已启用";
		}else{
			state = "已停用";
		}
		return state;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
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
}
