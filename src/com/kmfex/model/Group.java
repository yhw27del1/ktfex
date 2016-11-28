package com.kmfex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
 
/**
 * 分组信息 
 * @author   
 * 2013年07月31日15:45   新增停用启用标识字段state
 * 2013年8月23日11:4365   新增规则id字段groupRestrainId
 */ 
@Entity 
@Table(name="t_Group",schema="KT")
public class Group     implements java.io.Serializable{  
 
	private static final long serialVersionUID = -44008554898660275L;

	/**组ID**/
	private String id;
	
	/**组名称**/
	private String name;
	
	/**0停用1启用**/
	private int state=1;
	
	private Date createtime = new Date();
	
	
	/**开启的约束规则ID,com.kmfex.model.GroupRestrain**/ 
	private String groupRestrainId;
	
	//辅助页面显示
	private String groupRestrainName;
	
	/**备注**/
	private String note;
 	
	public Group() { 
	}
	public Group(String id) { 
		this.id = id;
	}
	
	public Group(String id, String name) { 
		this.id = id;
		this.name = name;
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
	
	@Column(name="name_",length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="note_",length=500)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Column(name="state_",columnDefinition="number(2) default 1")
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getGroupRestrainId() {
		return groupRestrainId;
	}
	public void setGroupRestrainId(String groupRestrainId) {
		this.groupRestrainId = groupRestrainId;
	}
	@Transient
	public String getGroupRestrainName() {
		return groupRestrainName;
	}
	public void setGroupRestrainName(String groupRestrainName) {
		this.groupRestrainName = groupRestrainName;
	}
 
 
}
