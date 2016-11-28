package com.wisdoor.core.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 判断用户是否有角色访问对应的链接
 * @author  
 *
 */
public class MyAccessDecisionManager implements AccessDecisionManager {

	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException { 
		if(configAttributes == null||configAttributes.size() == 0){
			System.out.println("公共资源!");
			return;
		}
		for(Iterator<ConfigAttribute> iterator = configAttributes.iterator(); iterator.hasNext();){
			ConfigAttribute configAttribute = iterator.next();
			String needRole = configAttribute.getAttribute(); 
			for(GrantedAuthority ga : authentication.getAuthorities()){ 
				if(needRole.equals(ga.getAuthority())){
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限访问！");
	}

	public boolean supports(ConfigAttribute arg0) { 
		return true;
	}

	public boolean supports(Class<?> arg0) { 
		return true;
	}

}
