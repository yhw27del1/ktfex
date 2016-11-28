package com.wisdoor.core.servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wisdoor.core.model.User;
import com.wisdoor.core.symbol.UserTags;
 

public class SessionFilter implements Filter{  
    private  List<String> list = new ArrayList<String>();  
    private  String noinclude="";
    public void destroy() {    
    }  

    @SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {   
        try {
			String path=((HttpServletRequest)request).getServletPath();  
			for(int i=0;i<list.size();i++){  
				if(path.indexOf((String)list.get(i))!=-1){
			    	HttpServletRequest httpRequest = (HttpServletRequest)request; 
			    	String url=httpRequest.getRequestURI();
			    	if(null!=noinclude&&!"".equals(noinclude)){
			    		if(noinclude.contains(url)){
			    			chain.doFilter(request, response);
			    			return; 
			    		}
			    	}
			    	
			    	User user =(User)httpRequest.getSession().getAttribute(UserTags.LOGININFO);  
			        if (null==user) {     
			            ((HttpServletResponse)response).sendRedirect("/common/login.jsp"); 
			            return; 
			        }else{   
			        	 Map<String,String> urlMap =(Map<String,String>)httpRequest.getSession().getAttribute(UserTags.URLMAP);  
			        	 if("none".equals(urlMap.get(httpRequest.getRequestURI()))){
			        		((HttpServletResponse)response).sendRedirect("/common/login.jsp");  
			        		return; 
			             }  
			            
			            chain.doFilter(request, response);  
			            return; 
			        }  
			    }else{  
			        chain.doFilter(request, response);  
			        return; 
			    }  
			}
		} catch (Exception e) { 
			//e.printStackTrace();
		}  
          
    }  

    public void init(FilterConfig filterConfig) throws ServletException { 
         String include = filterConfig.getInitParameter("include");  
         if (include != null) {  
                StringTokenizer st = new StringTokenizer(include, ",");  
                list.clear();  
                while (st.hasMoreTokens()) {  
                    list.add(st.nextToken());  
                }  
         }  
          
         noinclude = filterConfig.getInitParameter("noinclude");  
          
         
    }  

}  