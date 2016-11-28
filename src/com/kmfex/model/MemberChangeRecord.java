package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 变更机构的会员记录  
 * @author  
 */
@Entity
@Table(name="t_memberChangeRecord",schema="KT")
public class MemberChangeRecord implements Serializable{ 
 
	private static final long serialVersionUID = 1020956170526911144L;
	
	private String id;
	
	private String userid;//被转移人、变更人账户ID	
	private String username;//转移人、变更人账户	
	private String userRealName;//转移人姓名	
	
	private String fromOrgName;//原开户机构	
	private String fromOrgRealName;//原开户机构名称	
	private String fromOrgId;//原开户机构ID	
	
	private String toOrgName;//新客户归属机构
	private String toOrgRealName;//新客户归属机构名称
	
	private String applyUserRealName;//申请人	
	private String fromOkUserRealName;//原开户机构确认人	
	
	private String changeRes;//转移原因	
	private String note;//备注	
	
	private Date applyTime=new Date();//申请时间
	private Date changetime=new Date();//变更时间,转移时间 
	private Date starttime=new Date();//启用时间--计算费用
	
	private Date endtime;//失效时间--重新计算费用
	
	
	private String isFinishFlag;//完成情况0未完成1已经变更成功 
	


	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getFromOrgName() {
		return fromOrgName;
	}

	public void setFromOrgName(String fromOrgName) {
		this.fromOrgName = fromOrgName;
	}

	public String getFromOrgRealName() {
		return fromOrgRealName;
	}

	public void setFromOrgRealName(String fromOrgRealName) {
		this.fromOrgRealName = fromOrgRealName;
	}

	public String getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(String fromOrgId) {
		this.fromOrgId = fromOrgId;
	}

	public String getToOrgName() {
		return toOrgName;
	}

	public void setToOrgName(String toOrgName) {
		this.toOrgName = toOrgName;
	}

	public String getToOrgRealName() {
		return toOrgRealName;
	}

	public void setToOrgRealName(String toOrgRealName) {
		this.toOrgRealName = toOrgRealName;
	}

	public String getApplyUserRealName() {
		return applyUserRealName;
	}

	public void setApplyUserRealName(String applyUserRealName) {
		this.applyUserRealName = applyUserRealName;
	}

	public String getFromOkUserRealName() {
		return fromOkUserRealName;
	}

	public void setFromOkUserRealName(String fromOkUserRealName) {
		this.fromOkUserRealName = fromOkUserRealName;
	}

	public String getChangeRes() {
		return changeRes;
	}

	public void setChangeRes(String changeRes) {
		this.changeRes = changeRes;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getChangetime() {
		return changetime;
	}

	public void setChangetime(Date changetime) {
		this.changetime = changetime;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getIsFinishFlag() {
		return isFinishFlag;
	}

	public void setIsFinishFlag(String isFinishFlag) {
		this.isFinishFlag = isFinishFlag;
	}

	
}
