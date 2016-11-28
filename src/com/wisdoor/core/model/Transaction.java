package com.wisdoor.core.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.trigger.TransactionBase;

/**
 * 事务实体
 * @author eclipse
 *
 */
@Entity
@Table(name="sys_Transaction",schema="KT")
public class Transaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -330901225014456621L;
	
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 执行实例
	 */
	private String entity;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 执行时间
	 */
	private String time;
	
	/**
	 * 执行体
	 */
	private TransactionBase transactionbase;
	
	private boolean enable;
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTransactionbase(TransactionBase transactionbase) {
		this.transactionbase = transactionbase;
	}
	@Transient
	public TransactionBase getTransactionbase() {
		return transactionbase;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	@Transient
	public boolean isEnable() {
		return enable;
	}
	
	
	
}
