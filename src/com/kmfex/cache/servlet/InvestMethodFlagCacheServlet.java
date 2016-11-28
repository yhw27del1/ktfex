package com.kmfex.cache.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmfex.cache.utils.CacheManagerUtils;
import com.kmfex.cache.vo.Cache;

/**
 * 投标方法优化（自由切换）
 * toMethod=jdbc  --->数据库端
 *         =hibernate --->老方式
 * @author   
 */
public class InvestMethodFlagCacheServlet extends HttpServlet { 
 
 
	private static final long serialVersionUID = -1629662561260596787L;

	@Override
	public void init(ServletConfig config) throws ServletException {  
	}

	public InvestMethodFlagCacheServlet() {
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
		String goMethod = request.getParameter("toMethod"); 
		try { 
	       	CacheManagerUtils.clearOnly("invest_toMethod"); 
	   		CacheManagerUtils.putCache("invest_toMethod", new Cache("invest_toMethod",goMethod,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
	   		PrintWriter  out = response.getWriter();
	   		out.print("success");  
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter  out = response.getWriter();
	   		out.print("fails");
		}  
	}
}
 
