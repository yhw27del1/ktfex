package com.kmfex.cache.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kmfex.cache.service.FinancingCacheService;

public class OtherAppCacheServlet extends HttpServlet { 
 
	private static final long serialVersionUID = 3481642155904060344L;
	
	FinancingCacheService financingCacheService;  
	@Override
	public void init(ServletConfig config) throws ServletException { 
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		financingCacheService = (FinancingCacheService)wac.getBean("financingCacheImpl");
	}

	public OtherAppCacheServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		String financingBaseId = request.getParameter("sid"); 
		try {
			if(null!=financingBaseId&&!"".equals(financingBaseId)){
				financingCacheService.updateOneFinancingCache(financingBaseId);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
 
