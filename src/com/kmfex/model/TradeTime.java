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
@Table(name = "t_Trade_Time",schema="KT")
public class TradeTime implements Serializable{
	
	private String id;
	private String title;
	
	private String am_start;
	private String am_end;
	private String pm_start;
	private String pm_end;
	
	private Date am_start_time;
	private Date am_end_time;
	private Date pm_start_time;
	private Date pm_end_time;
	
	/**
	 * 开始时间
	 */
	private Date startTime;  //只存   小时 分 秒
	/**
	 * 截止时间
	 */
	private Date endTime;    //只存 小时 分 秒
	/**
	 * 启用状态
	 */
	private boolean enabled;
	
	private String state;//辅助显示字段
	private String memo;//描述
	private Date createDate = new Date();
	
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
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return memo;
	}
	public void setAm_start(String am_start) {
		this.am_start = am_start;
	}
	public String getAm_start() {
		return am_start;
	}
	public void setAm_end(String am_end) {
		this.am_end = am_end;
	}
	public String getAm_end() {
		return am_end;
	}
	public void setPm_start(String pm_start) {
		this.pm_start = pm_start;
	}
	public String getPm_start() {
		return pm_start;
	}
	public void setPm_end(String pm_end) {
		this.pm_end = pm_end;
	}
	public String getPm_end() {
		return pm_end;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setAm_start_time(Date am_start_time) {
		this.am_start_time = am_start_time;
	}
	public Date getAm_start_time() {
		return am_start_time;
	}
	public void setAm_end_time(Date am_end_time) {
		this.am_end_time = am_end_time;
	}
	public Date getAm_end_time() {
		return am_end_time;
	}
	public void setPm_start_time(Date pm_start_time) {
		this.pm_start_time = pm_start_time;
	}
	public Date getPm_start_time() {
		return pm_start_time;
	}
	public void setPm_end_time(Date pm_end_time) {
		this.pm_end_time = pm_end_time;
	}
	public Date getPm_end_time() {
		return pm_end_time;
	}
}
