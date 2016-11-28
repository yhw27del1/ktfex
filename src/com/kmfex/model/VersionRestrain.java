package com.kmfex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
/**
 * 债权转让版本约束条件表
 * 在到期日期内存在记录的用户可以使用债权转让版本
 * @author   
 *  2013-09-25 14:39     新增字段useFlag使用状态
 */ 
@Entity 
@Table(name="t_version_restrain",schema="KT")
public class VersionRestrain     implements java.io.Serializable{ 
 
	private static final long serialVersionUID = -1110088479176251523L;
	
	/**主键(开发使用债权的用户名)**/
	private String username;
	
	/**
	 * 使用债权的到期日期
	 * 格式：yyyyMMdd
	 **/ 
	private long endDate=0;
	
	/**
	 * 使用状态
	 * 停用将无法登陆
	 */
	private int useFlag = 0;//1正常使用，0停用
	@Id 
	@Column(name="username_",length=60)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="endDate_",columnDefinition="number(16) default 0")
	public long getEndDate() { 
		return endDate;
	}
	
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	
	@Column(columnDefinition="number(2) default 1")
	public int getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(int useFlag) {
		this.useFlag = useFlag;
	}
}
