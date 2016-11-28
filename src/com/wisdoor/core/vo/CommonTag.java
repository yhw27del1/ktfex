package com.wisdoor.core.vo;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;

/**
 * 
 * 通用标签
 * @author  
 *
 */
public class CommonTag { 
	//操作所有对象提示
	public static final String MESSAGE = "MESSAGE";  
	
	//存放资源与对应权限的，Map<资源(url),角色集合>
	private static final Map<String,Collection<ConfigAttribute>> resourceMap = null;
}
