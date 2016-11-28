package com.wisdoor.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 授权中心通知
 * @author   
 */
@Entity
@Table(name="sys_Notice",schema="KT")
public class Notice implements Serializable{

	private static final long serialVersionUID = -8402387737561148921L;
	
	private String id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 添加时间
	 */
	private Date addtime = new Date();
	/**
	 * 审核状态 0为预备审核 1为待审核 2为通过审核 3为驳回
	 */
	
	private int audit_state = 0;
	/**
	 * 过期时间
	 */
	private Date endtime;
	/**
	 * 排序
	 */
	private int sort = 0;
 
    /**
     * 目前用户
     */  
	private String targetUser;
	
	/**
	 * 已经阅读的过的用户
	 */
	private String readEdUser="";
   
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Lob   
	@Type(type="text")         
	@Column(name="content",nullable=true)   
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	/**
	 * 审核状态 0为预备审核 1为待审核 2为通过审核 3为驳回
	 * @return
	 */
	@Column(name="state",nullable=false,columnDefinition="int default 0")
	public int getAudit_state() {
		return audit_state;
	}
	/**
	 * 审核状态 0为预备审核 1为待审核 2为通过审核 3为驳回
	 * @return
	 */
	public void setAudit_state(int auditState) {
		audit_state = auditState;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	@Column(length=4000)  
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	@Column(length=4000)  
	public String getReadEdUser() {
		return readEdUser;
	}
	public void setReadEdUser(String readEdUser) {
		this.readEdUser = readEdUser;
	}
 
}
