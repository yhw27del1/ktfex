package com.wisdoor.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wisdoor.core.vo.CommonVo;
/**
 * 字典处理类
 * @author   
 */ 
public class DictionaryUtils {
    /**
	 * 得到字典模板的数据 
	 */
	public static JSONObject getJsonByUrl(String url) {
	   try {
	      String res=getStaticPage(url,"UTF-8"); 
	      return JSONObject.fromObject(removeSpecilChar(res));
		}catch (RuntimeException e) { return null;}  		    
	}
	
    /**
	 * 某个网址中的得到的某个键的值列表 
	 */
	public  static  JSONArray getJSONObjectByUrlKey(String url,String key)
	 {
		try {    
			String res=getStaticPage(url,"UTF-8");   
		    JSONObject json = JSONObject.fromObject(removeSpecilChar(res));  
		    return json.getJSONArray(key);
	     }catch (RuntimeException e) { return null;}  
	}
	
	/**
	 * 得到下拉列表 
	 */
	public static List<CommonVo> getListByUrlKey(String url,String key)
	{
		List<CommonVo> vos=new ArrayList<CommonVo>();
		try {    
			JSONArray jas=getJSONObjectByUrlKey(url,key);
			for(int i = 0; i < jas.size(); i++){  
                JSONObject oj = jas.getJSONObject(i);  
                vos.add(new CommonVo(oj.getString("id"), oj.getString("name")));
            }  
			return vos;
		}catch (RuntimeException e) { return null;}  
	}
	/**
	 * 得到下拉列表 
	 */
	public static List<CommonVo> getListByUrlKey2(String url,String key)
	{
		List<CommonVo> vos=new ArrayList<CommonVo>();
		try {    
			JSONArray jas=getJSONObjectByUrlKey(url,key);
			for(int i = 0; i < jas.size(); i++){  
				JSONObject oj = jas.getJSONObject(i);  
				vos.add(new CommonVo(oj.getString("id"), oj.getString("name"), oj.getString("yysr"), oj.getString("cyry"), oj.getString("zczh")));
			}     
			return vos;
		}catch (RuntimeException e) { return null;}  
	}
	

    /**
	 * 字符串中得到某个键的值列表  
	 */
	public  static  JSONArray getJSONObjectByStrKey(String content,String key)
	 {
		try {     
		    JSONObject json = JSONObject.fromObject(content);  
		    return json.getJSONArray(key);
	     }catch (RuntimeException e) { return null;}  
	}	
	
    /**
	 * 从JSONObject中得到某个键的值 
	 */
	public  static  Object getValueByJSONObject(JSONObject json,String key)
	 {
		try {     
		    return json.get(key); 
	     }catch (RuntimeException e) { return null;}  
	}	
	
    /**
	 * 从json字符串中得到某个键的值 
	 */
	public  static  Object getValueByJSONStr(String content,String key)
	 {
		try {     
			JSONObject json = JSONObject.fromObject(content);
		    return json.get(key); 
	     }catch (RuntimeException e) { return null;}  
	}	
	
	private static String getStaticPage(String surl,String charSet) { 
		String htmlContent = ""; 
		java.io.InputStream inputStream=null;
		java.net.URL url=null;
		java.net.HttpURLConnection connection=null;
		try{			
		    url = new java.net.URL(surl);
			connection = (java.net.HttpURLConnection) url.openConnection();
			connection.connect();
			inputStream = connection.getInputStream();
			byte bytes[] = new byte[1024 * 2000];
			int index = 0;
			int count = inputStream.read(bytes, index, 1024 * 2000);
			while (count != -1) {
				index += count;
				count = inputStream.read(bytes, index, 1);
			}
			htmlContent = new String(bytes, charSet);
		}catch (Exception ex) { 
			return "";
		}finally {  
            try {  
            	inputStream.close();  
            	connection.disconnect();
            } catch (Exception e) {  
            	return ""; 
            }  
        }  
		return htmlContent.trim();
	}
	/**
	 * 去掉换行符等特殊字符
	 * @param str
	 * @return
	 */
	private static String removeSpecilChar(String str){
		String result = "";
		if(null != str){
			 Pattern pat = Pattern.compile("\\s*|\n|\r|\t");
			 Matcher mat = pat.matcher(str);
			 result = mat.replaceAll("");
		}
		return result;
   }  
}
