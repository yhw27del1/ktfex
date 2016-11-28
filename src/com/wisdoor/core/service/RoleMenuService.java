package com.wisdoor.core.service;
   
import java.util.List;
import java.util.Set;

import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.RoleMenu;

/*** 
 * 角色菜单管理
 * @author    
 */
public interface RoleMenuService  extends BaseService<RoleMenu>{
    /***
     * 得到某个资源下的所有角色
     * @param menuId
     * @return
     */
	public List<Role> findRoles(long menuId);  
    /***
     * 得到某个角色下的所有资源
     * @param menuId
     * @return
     */ 
	public List<Menu> findMenus(long roleId);
	
	/**
	 * 删除角色下所有资源  
	 */
	public void deleteByRoleId(long roleId)  throws EngineException;
	
	public void deleteByRoleId(Role role,long roleId,String menuIds) throws EngineException;
	/*
	 * 此角色是否有该资源 
	 */
	public boolean findRoleMenu(long roleId, long menuId);
	/*
	 * 检测当前节点是否有子节点
	 */
	public boolean findNoHasSubRoleMenu(long menuId);
	
	/*
	 * 判断某用户的所有角色有没有此权限
	 */
	public boolean findRoleMenu(String roleIds,long menuId);
	/*
	 * 判断某用户的在某个类别下的菜单
	 */
	public Set<Menu> findRoleMenu(String roleIds, String type, long parentId);
	/*
	 * 得到角色下所有资源
	 */
	public List<RoleMenu> findRoleMenuByRoleId(long roleId) throws EngineException;
}
