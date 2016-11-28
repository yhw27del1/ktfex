package com.wisdoor.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

/**
 * 已鉴权但访问了受保护权限自定义
 * @author   
 */

public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

	@Override
	public void handle(HttpServletRequest arg0, HttpServletResponse arg1,
			AccessDeniedException arg2) throws IOException, ServletException {
		 //增加自己的处理逻辑
		super.handle(arg0, arg1, arg2);
	}

}
