package com.wisdoor.core.model;
 
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 角色表
 * @author by   
 */
@SuppressWarnings("serial")
@Entity
@Table(name="sys_Role",schema="KT")
public class Role  implements java.io.Serializable{
	private long id;
	/**角色名称**/
	private String name;
	/**角色描述**/
	private String desc; 
	/**某个角色下的用户**/
	private Set<User> users = new HashSet<User>();
	/**
	 * 开户许可字符串
	 */
	private String p_t_o_a_a;
	
	/**
	 * 角色类型：顶层角色0，组织机构下属角色1
	 */
	private String type;
	
	

	public Role(){}
 
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_ROLE_SE")  
    @SequenceGenerator(name="SYS_ROLE_SE",sequenceName="SYS_ROLE_SEQUENCE",allocationSize=1)
	public long getId() {
		return id;
	}

	public Role(long id) { 
		this.id = id;
	}

	public Role(String name, String desc) { 
		this.name = name;
		this.desc = desc;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="name_")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="desc_")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
 
	@ManyToMany(mappedBy="roles",cascade=CascadeType.REFRESH)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getP_t_o_a_a() {
		return p_t_o_a_a;
	}

	public void setP_t_o_a_a(String p_t_o_a_a) {
		this.p_t_o_a_a = p_t_o_a_a;
	}
 
	@Column(columnDefinition="varchar2(2) default '0'")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
