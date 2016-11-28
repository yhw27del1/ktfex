package com.wisdoor.struts;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
/**
 * 清除参数的前后空格
 * @author   
 */
public class TrimInterceptor implements Interceptor { 
 
	private static final long serialVersionUID = 1686702050924250287L;

	public void destroy() {
	}
 
	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {
		
		Map map=invocation.getInvocationContext().getParameters();
		Set keys = map.keySet();
                Iterator iters = keys.iterator();
        		while(iters.hasNext()){
        			Object key = iters.next();
        			Object value = map.get(key);
        			map.put(key, transfer((String[])value)); 
        		} 	
		return invocation.invoke();
	}
	
	/**
	 * 清除参数的前后空格
	 */
	private String[] transfer(String[] params){
		for(int i=0;i<params.length;i++){
			if(StringUtils.isEmpty(params[i]))continue;
			params[i]=params[i].trim();
		}
		return params;
	}
 
	public void init() {

	}
 
} 