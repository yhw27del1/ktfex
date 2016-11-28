package com.wisdoor.core.security;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
/*
 *  运行身份管理类
 *   
 */
public class MyUserDetailsServiceImpl implements UserDetailsService{
	@Resource UserService userService; 
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException { 
		User u =null;
		try {
			u =userService.findUser(userName);
			if(null!=u){ 
				u.setRoles(u.getRoles());
			}else{
				throw new UsernameNotFoundException("用户" + userName + "没有找到");
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return u;
	}

}
