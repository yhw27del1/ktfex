package com.kmfex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
/**
 * 约束分类表
 * @author   
 */ 
@Entity 
@Table(name="t_GroupRestrain",schema="KT")
public class GroupRestrain     implements java.io.Serializable{ 
  
	private static final long serialVersionUID = 7096047557073289141L; 
	/*
	 * 手动指定约束对象实体model
	 * 第一个约束表1 entityId='UserGroupRestrain1' （投标次数、最大金额控制)--com.kmfex.model.UserGroupRestrain1 
	 * 第二个约束表2 待增加 
	 */
	private String entityId; 
	
	private String name;  //分类名称
	
	private String restrainJson; //约束条件   
	
	
	@Id
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	 
	
	@Column(name="name_",length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length=600)
	public String getRestrainJson() {
		return restrainJson;
	}
	public void setRestrainJson(String restrainJson) {
		this.restrainJson = restrainJson;
	}

 

 
}
