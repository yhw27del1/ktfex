package com.wisdoor.core.trigger;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 事务
 * @author eclipse    
 *
 */
public abstract class TransactionBase implements ServletContextAware{
	private ServletContext servletcontext;
	protected static WebApplicationContext wac;
	
	
	public abstract void excute() throws Exception;
	
	public abstract void init() throws Exception;
	
	@Override
	public void setServletContext(ServletContext servletcontext) {
		this.servletcontext = servletcontext;
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.servletcontext);
	}
	
	
	
}
