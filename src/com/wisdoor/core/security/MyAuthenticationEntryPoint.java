package com.wisdoor.core.security;

import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
/*
 * 未鉴权访问自定义
 * @author   
 */
public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
			org.springframework.security.core.AuthenticationException arg2)
			throws IOException, ServletException { 
		//增加自己的处理逻辑
		super.commence(arg0, arg1, arg2);
	}
 
}
