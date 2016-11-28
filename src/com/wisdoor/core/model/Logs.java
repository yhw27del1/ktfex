package com.wisdoor.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 日志
 * @author ms
 *
 */
@Entity
@Table(name="sys_logs",schema="KT")
public class Logs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5526096251899664537L;

	private String id;
	private Date time = new Date();
	private User operator;
	private String operate;
	private String ip;
	
	// 来自的实体model对象(比如entityFrom="FinancingBase" 融资项目基本信息表的日志)
	private String entityFrom;  
	// 来自的实体那个model对象记录ID(比如entityId="融资项目基本信息表ID")
	private String entityId;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@ManyToOne
	public User getOperator() {
		return operator;
	}
	public void setOperator(User operator) {
		this.operator = operator;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getEntityFrom() {
		return entityFrom;
	}
	public void setEntityFrom(String entityFrom) {
		this.entityFrom = entityFrom;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	
	
}
