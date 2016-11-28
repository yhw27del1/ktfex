package com.wisdoor.core.cache;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

 

public class GlobalParameters {
	
	// 系统缓存信息,在SysParametersInit中初始化
	public static BusinessCache business;

	// spring中WebApplicationContext对象
	public static ServletContext context;
	
	// spring中WebApplicationContext对象
	public static WebApplicationContext webApplicationContext;

}
