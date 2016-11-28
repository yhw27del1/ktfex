package com.wisdoor.core.utils;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.vo.Response;
/**
 * ajax调用JSON格式输出
 * @author  
 *
 */
@SuppressWarnings({ "unused"})
public class DoResultUtil {
	/**
	 * AJAX结果JSON输出(针对提示类)
	 * @param res
	 */ 
	public static void doResult(HttpServletResponse response,Response res) throws Exception{
		doStringResult(response,JSONObject.fromObject(res).toString());
	}
	/**
	 * AJAX结果JSON输出(针对字符串)
	 * @param res
	 */ 
	public static void doStringResult(HttpServletResponse response,String res) throws Exception{
		response.setContentType("text/json;charset=UTF-8"); 
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");    
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		response.setHeader("Pragma", "no-cache"); 
		response.getWriter().write(res);
		response.getWriter().flush();
		response.getWriter().close(); 
	} 
	 
	 
	/**
	 * AJAX结果JSON输出(针对某个对象)
	 * @param res
	 */ 
	public static void doObjectResult(HttpServletResponse response,Object object) throws Exception{
		response.setContentType("text/json;charset=UTF-8"); 
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");    
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		response.setHeader("Pragma", "no-cache"); 
		Gson gson = new Gson(); 
		response.getWriter().write(gson.toJson(object));
		response.getWriter().flush();
		response.getWriter().close(); 
	}
	/**
	 * AJAX结果XML输出(针对某个对象)
	 * @param res
	 */ 
	public static void doXMLResult(HttpServletResponse response,String object) throws Exception{ 
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");    
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
		response.setHeader("Pragma", "no-cache"); 
		response.getWriter().write(object);
		response.getWriter().flush();
		response.getWriter().close(); 
	}
}	


