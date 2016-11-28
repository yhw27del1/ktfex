package com.wisdoor.core.service.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.RoleMenu;
import com.wisdoor.core.service.MenuService;
import com.wisdoor.core.service.RoleMenuService;
import com.wisdoor.core.service.RoleService;
 

@Service
@Transactional
public class RoleMenuImpl extends BaseServiceImpl<RoleMenu> implements RoleMenuService {
	
	@Resource RoleService roleService;
	@Resource MenuService menuService; 
    @Override
	public List<Role> findRoles(long menuId){
	  List<RoleMenu> roleMenus=this.getScrollDataCommon("from RoleMenu a where a.menuId ='"+menuId+"'", new String[]{});
	  
	  StringBuffer buffer=new StringBuffer("from Role c where 1=1 and ");
	  String ids="";
	  if(null!=roleMenus)
	  {
		  for(RoleMenu rm:roleMenus)
		  {
			  ids+=rm.getRoleId()+",";
		  }
	  } 
	  if(!"".equals(ids)){
		  ids=ids.substring(0, ids.length()-1);
		  buffer.append(" c.id in("+ids+")");
	  }else
	  {
		  buffer.append(" c.id in('')");
	  }
	  return roleService.getCommonListData(buffer.toString());
  }
    @Override
	public List<Menu> findMenus(long roleId){
	  List<RoleMenu> roleMenus=this.getScrollDataCommon("from RoleMenu a where a.roleId ='"+roleId+"'", new String[]{});
	  
	  StringBuffer buffer=new StringBuffer("from Menu c where 1=1 and ");
	  String ids="";
	  if(null!=roleMenus)
	  {
		  for(RoleMenu rm:roleMenus)
		  {
			  ids+=rm.getMenuId()+",";
		  }
	  } 
	  if(!"".equals(ids)){
		  ids=ids.substring(0, ids.length()-1);
		  buffer.append(" c.id in("+ids+")");
	  }else
	  {
		  buffer.append(" c.id in('')");
	  }
	  return menuService.getCommonListData(buffer.toString());
  }
    
    @Override
	public void deleteByRoleId(long roleId) throws EngineException{
 	  StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  ");
	  buffer.append("   and  c.roleId='"+roleId+"'"); 
	  List<RoleMenu>  roleMenus=this.getCommonListData(buffer.toString());
	  for(RoleMenu rm:roleMenus)
	  {
	      this.delete(rm.getId());
	  }
    }
    @Override
	public List<RoleMenu> findRoleMenuByRoleId(long roleId) throws EngineException{
 	  StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  ");
	  buffer.append("   and  c.roleId='"+roleId+"'"); 
	  return this.getCommonListData(buffer.toString());
 
    }
    @Override 
    @Transactional
	public void deleteByRoleId(Role role,long roleId,String menuIds) throws EngineException{
 	  try {
		StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  ");
		  buffer.append("   and  c.roleId='"+roleId+"'"); 
		  List<RoleMenu>  roleMenus=this.getCommonListData(buffer.toString());
		  for(RoleMenu rm:roleMenus)
		  {
			  RoleMenu rmtemp=this.selectById(rm.getId());
		      this.delete(rmtemp.getId());
		  }
			if(menuIds != null && !menuIds.equals("")){ 
				  for (StringTokenizer tokenizer = new StringTokenizer(menuIds, ","); tokenizer.hasMoreTokens();) {
						String appID = tokenizer.nextToken();
						if (!appID.equals("0")) { 
							if (appID != null&& !appID.equals(""))
								{
								 appID=appID.trim();
								this.insert(new RoleMenu(new Long(appID),roleId));
								}
						} 
					  } 
			}
			roleService.update(role);
	} catch (NumberFormatException e) { 
		e.printStackTrace();
	}
    }
    @Override
	public boolean findRoleMenu(long roleId,long menuId) {
      boolean flag=false;
 	  StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  ");
	  buffer.append("   and  c.roleId='"+roleId+"' and c.menuId='"+menuId+"'"); 
	  RoleMenu rm=this.selectById(buffer.toString(),new String[]{});
	  if(null!=rm) flag=true;
	  return flag;
	  
    }
    
    @Override
	public boolean findRoleMenu(String roleIds,long menuId) {
      boolean flag=false;
 	  StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  "); 
  	 if(roleIds != null && !roleIds.equals("")){
  		buffer.append("   and  c.roleId in ("+roleIds+") and c.menuId='"+menuId+"'"); 
  	 }else{
  		buffer.append("   and  c.roleId in ('') and c.menuId='"+menuId+"'"); 
  	 }
	  
	  RoleMenu rm=this.selectById(buffer.toString(),new String[]{});
	  if(null!=rm) flag=true;
	  return flag;
	  
    }
    
    @Override
	public Set<Menu> findRoleMenu(String roleIds,String type,long parent_id) { 
 	  StringBuffer buffer=new StringBuffer("from RoleMenu c where 1=1  ");
   	 if(roleIds != null && !roleIds.equals("")){
   		 buffer.append("   and  c.roleId in ("+roleIds+") "); 
   	 }else{
   		 buffer.append("   and  c.roleId in ('') "); 
   	 }
	 
	  
	  List<RoleMenu> roleMenus=this.getCommonListData(buffer.toString());
	  Set<Menu> menus=new HashSet<Menu>();
	  for(RoleMenu rm:roleMenus)
	  {
		  Menu menu=this.menuService.selectById(rm.getMenuId());
		  if(null!=menu && null!=menu.getParent())
			  if(type.equals(menu.getType())&&parent_id==menu.getParent().getId())
			  {
				  menus.add(menu);
			  }
	  }
	  return menus;
    }
    
    @Override
	public boolean findNoHasSubRoleMenu(long menuId) {
      boolean flag=true;
      Menu menu=menuService.selectById(menuId); 
	  if(null!=menu) 
	  {
		  if(null!=menu.getChildMenus()&&menu.getChildMenus().size()>0)
		  {
			  flag=false; 
		  }
	  }
	  return flag;
	  
    }
}
