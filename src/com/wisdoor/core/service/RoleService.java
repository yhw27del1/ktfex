package com.wisdoor.core.service;
  
import java.util.List;
import java.util.Set;
 

import com.wisdoor.core.model.Role;

/*** 
 * 角色表
 * @author    
 */
public interface RoleService  extends BaseService<Role>{

	List<Role> findNotInRole(Set<Role> roleMenus);

	List<Role> findOrgInRole();

	List<Role> findNotInOrgRole(Set<Role> inUserRoleList); 
}
