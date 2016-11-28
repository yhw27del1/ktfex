package com.wisdoor.struts;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.wisdoor.core.utils.AntiXSS;
import com.wisdoor.core.utils.BaseTool;
/**
 * 防止XSS、sql注入
 * @author   
 */
public class IllegalCharacterInterceptor implements Interceptor {
	private static final long serialVersionUID = -2578561479301489061L;
	private String badStr = "";
	private String excludeParam = ""; 
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
			 if(value instanceof String[]){ 
				 String vStr=getStrValue((String[])value);
				 String keyStr=key.toString();
				 //System.out.println(key+"==="+vStr); 
				 if(BaseTool.isNotNull(excludeParam)){
					 if(excludeParam.contains(keyStr+"|")){
						 //System.out.println(keyStr+"="+vStr); 
						 continue;
					 }
				 } 
				 if(!isSQLValid(vStr,badStr)){
					 return "illegalPage";
				 }
				 
			 } 
				
			 map.put(key, clearXSS((String[])value)); 
		}
		//System.out.println("badStr==="+badStr); 		
		//System.out.println("excludeParam==="+excludeParam); 		
		return invocation.invoke();
	}
  
    
    /** 
     * 参数校验   
     */  
    protected boolean isSQLValid(String str,String badStr) {   
    	Pattern sqlPattern = Pattern.compile(badStr, Pattern.CASE_INSENSITIVE);  
        if (sqlPattern.matcher(str).find()) { 
            return false;  
        }  
        return true;  
    }   
    
	/**
	 * @Description: 清除所有XSS攻击的字符串  
	 */
	private String[] clearXSS(String[] params){
		for(int i=0;i<params.length;i++){
			if(StringUtils.isEmpty(params[i]))continue;
			params[i]=AntiXSS.antiXSS(params[i]);
		}
		return params;
	}
 
	public void init() {

	}

	public String getBadStr() {
		return badStr;
	}

	public void setBadStr(String badStr) {
		this.badStr = badStr;
	}

	public String getExcludeParam() {
		return excludeParam;
	}

	public void setExcludeParam(String excludeParam) {
		this.excludeParam = excludeParam;
	}
 
	private String getStrValue(String[] params){ 
		if(null!=params&&params.length>0){
			return params[0];
		}else{
			return "";
		} 
	}
} 