package com.kmfex.model;

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
 * 公告实体
 * @author eclipse
 *
 */
@Entity
@Table(name="t_Announcement",schema="KT")
public class Announcement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1321362882333707983L;
	
	
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
	 * 针对用户类型
	 */
	private String target;
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
	/**
	 * 针对用户类型
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * 针对用户类型
	 * @return
	 */
	public String getTarget() {
		return target;
	}
	
	
	
	
}
