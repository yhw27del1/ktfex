package com.wisdoor.core.model;
  
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 角色资源表
 * @author by   
 */ 
@Entity
@Table(name="sys_Role_menu",schema="KT")
public class RoleMenu  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1552380065464115556L;
	private long id; 
	/**角色ID**/
	private long roleId;
	/**资源ID**/
	private long menuId;
	
	public RoleMenu(){}

	public RoleMenu(long menuId, long roleId) {  
		this.menuId = menuId;
		this.roleId = roleId;
	}

	@Id 
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_ROLE_MENU_SE")  
    @SequenceGenerator(name="SYS_ROLE_MENU_SE",sequenceName="SYS_ROLE_MENU_SEQUENCE",allocationSize=1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	 
	@Column(name="roleId_")
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	@Column(name="menuId_")
	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
 

	
}
