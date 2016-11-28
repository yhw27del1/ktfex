package com.kmfex.cache.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;

/*
 * 1、初始化投标中的融资项目到缓存。
 * 2、为已经投标的投资人生成投标索引
 * 3、生成投标人融资项目最小最大值
 */
public class InitFinancingCacheServlet extends HttpServlet { 
	 
	private static final long serialVersionUID = 299661938519046994L;
	
	FinancingCacheService financingCacheService;
	
	@Override
	public void init(ServletConfig config) throws ServletException { 
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		financingCacheService = (FinancingCacheService)wac.getBean("financingCacheImpl");
		try {
			
			String cacheServiceFlag=ReadsStaticConstantPropertiesUtil.readValue("SERVICE_CACHE_FLAG");
			if("1".equals(cacheServiceFlag)){//是缓存服务器才初始化缓存
			    //缓存投标结束的包(4已满标   5融资确认已完成  6费用核算完成  7签约完成 8已撤单)
				financingCacheService.stopInvests();
				financingCacheService.doingInvests();  				
			} 
			
 		 } catch (Exception e) {  
			e.printStackTrace();
		}  
	}

	 
}
