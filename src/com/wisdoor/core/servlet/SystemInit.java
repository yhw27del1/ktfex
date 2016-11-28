package com.wisdoor.core.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.app.Velocity;

public class SystemInit implements Filter {
	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterchain) throws IOException, ServletException {
	   HttpServletRequest req = (HttpServletRequest) request;
		req.setCharacterEncoding("UTF-8");      
		response.setContentType("text/html;charset=UTF-8"); 
		filterchain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
	try{
			Properties prop = new Properties();
			System.out.println(config.getServletContext().getRealPath("/"));
			prop.put("runtime.log", config.getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"template"+File.separator+"log"+File.separator+"velocity.log");
			prop.put("file.resource.loader.path", config.getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"template");
			prop.put(Velocity.ENCODING_DEFAULT, "UTF-8");
			prop.put(Velocity.INPUT_ENCODING, "UTF-8"); 
			prop.put(Velocity.OUTPUT_ENCODING, "UTF-8");  
			prop.put("default.contenttype", "text/html;charset=UTF-8");
			Velocity.init(prop); 
		}catch( Exception e ){
			e.printStackTrace();  
		}
	}

}
