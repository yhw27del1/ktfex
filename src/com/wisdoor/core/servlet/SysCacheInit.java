package com.wisdoor.core.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wisdoor.core.cache.CacheAccessManager;
import com.wisdoor.core.cache.GlobalParameters;
 

/**
 * 初始化参数
 * 
 * @author zuguo wu
 * @since 12,8 2012
 * @version 1.0
 */
public class SysCacheInit  extends HttpServlet {

	private static final long serialVersionUID = 7059811633375294342L;

	public void init() throws ServletException {
		try {
			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			GlobalParameters.webApplicationContext = context;
 			GlobalParameters.context=this.getServletContext();
			CacheAccessManager cacheManager = (CacheAccessManager) context.getBean("cacheAccessManagerBean");
 			cacheManager.initCache(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

 
	public void destroy() {
	}

}

