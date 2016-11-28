package com.kmfex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;
 
/**
 * 用户分组
 * @author   
 */ 
@Entity 
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="t_UserGroup",schema="KT")
public class UserGroup     implements java.io.Serializable{ 
 
	private static final long serialVersionUID = 2331208649141046197L;

	/**ID**/
	private String id;
	
	/**组ID**/
	private String groupId;
	
	/**成员**/
	private User user;


	public UserGroup() { 
	}
	public UserGroup(String groupId, User user) { 
		this.groupId = groupId;
		this.user = user;
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
	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
 
 
}
