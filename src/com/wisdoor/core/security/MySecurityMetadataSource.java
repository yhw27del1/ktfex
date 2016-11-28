package com.wisdoor.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.service.MenuService;
import com.wisdoor.core.service.RoleMenuService;

 

/**
 * 1.加载权限与资源的对应关系
 * 2.根据url取得对应的权限
 * @author  
 *
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
 
	private MenuService menuService;
	
	private RoleMenuService roleMenuService;
	
	private static Map<String,Collection<ConfigAttribute>> resourceMap = null;
	
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public RoleMenuService getRoleMenuService() {
		return roleMenuService;
	}

	public void setRoleMenuService(RoleMenuService roleMenuService) {
		this.roleMenuService = roleMenuService;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public MySecurityMetadataSource(MenuService menuService,RoleMenuService roleMenuService){
		try {
			this.menuService=menuService;
			this.roleMenuService=roleMenuService;
			loadResource();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	} 

 
	public void loadResource() throws Exception{ 
		if(resourceMap == null){
			resourceMap = new HashMap<String,Collection<ConfigAttribute>>();
			List<Menu> menuList = menuService.getScrollData().getResultlist(); 
			for(Menu menu : menuList){ 
				List<Role> roleList = roleMenuService.findRoles(menu.getId()); 
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				if(null!=roleList)
				{
					for(Role role : roleList){
						ConfigAttribute configAttribute = new SecurityConfig("ROLE_"+role.getId());
						configAttributes.add(configAttribute);
					}
				}
				resourceMap.put(menu.getHref(), configAttributes); 
			}
			
			
		}
	}
	 
	public Collection<ConfigAttribute> getAttributes(Object obj)
			throws IllegalArgumentException { 
		String requestUrl = ((FilterInvocation)obj).getRequestUrl(); 
		if(resourceMap == null){
			try {
				loadResource();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		
		return resourceMap.get(requestUrl);
	}

	public boolean supports(Class<?> arg0) { 
		return true;
	}

}
