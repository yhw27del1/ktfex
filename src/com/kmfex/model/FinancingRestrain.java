package com.kmfex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
 
/**
 * 融资投标列表过滤（针对组过滤）
 * @author   
 */ 
@Entity 
@Table(name="t_FinancingRestrain",schema="KT")
public class FinancingRestrain     implements java.io.Serializable{ 
 
	private static final long serialVersionUID = 7453698065426965808L;

	/**ID**/
	private String id;
	
	/**组ID**/
	private String groupId;
	
	/**项目编号**/
	private String financingCode;
 	

	public FinancingRestrain() { 
	}
	public FinancingRestrain(String groupId, String financingCode) { 
		this.groupId = groupId;
		this.financingCode = financingCode;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="groupId_",length=50)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Column(length=50)
	public String getFinancingCode() {
		return financingCode;
	}
	public void setFinancingCode(String financingCode) {
		this.financingCode = financingCode;
	}
 
}
