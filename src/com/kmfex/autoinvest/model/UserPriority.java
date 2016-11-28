package com.kmfex.autoinvest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 委托投标----用户优先级
 * @author    
 * 时间格式：yyyyMMddHHmmss 
 */ 
@Entity
@Table(name = "Auto_UserPriority",schema="KT")
public class UserPriority implements Serializable{ 
 
	private static final long serialVersionUID = -2771441758423314805L;


	private String username;
	
	/**
	 * 解约时，置null
	 * 开市时，检查设置情况，赋值
	 */ 
	private UserParameter userParameter;//当前正在使用的参数标准,为空表示已经解约 
	
	
	private long createTime=0;//设置时间
	
	
	private long lastTime=0;//最后自动成交时间
	
	
	private Double levelScore=0.00;//优先级别   
	
	private long stopTime=0;//废除协议时间(开市时更新)
	
	@Column(name="stopFlag_",length=20)
	private String stopFlag="0";//1提交了停用协议申请， 其他值是正在使用中
	
	
	public UserPriority() { 
	}
	
	
	public UserPriority(String username, UserParameter userParameter,
			long createTime, long lastTime) { 
		this.username = username;
		this.userParameter = userParameter;
		this.createTime = createTime;
		this.lastTime = lastTime;
	}
	@Id
	@Column(name="username_")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="createTime_")
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	@Column(name="lastTime_")
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	@Column(name="levelScore_", nullable = true,precision=12, scale=2)
	public Double getLevelScore() {
		return levelScore;
	}
	public void setLevelScore(Double levelScore) {
		this.levelScore = levelScore;
	}  
	
	@ManyToOne 
	public UserParameter getUserParameter() {
		return userParameter;
	}
	public void setUserParameter(UserParameter userParameter) {
		this.userParameter = userParameter;
	}

	@Column(name="stopTime_")
	public long getStopTime() {
		return stopTime;
	}


	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}


	public String getStopFlag() {
		return stopFlag;
	}


	public void setStopFlag(String stopFlag) {
		this.stopFlag = stopFlag;
	}
 
	
}
