package com.wisdoor.core.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
/**
 * BaseTool类创第一次创建于2012
 * 这个类提供了一些常用的数据操作方法, 
 * 其中包括数据校验、处理及加工的功能.
 * 类中的所有方法都是以静态方式提供的. 各个方法的使用请参看方法注释 
 * @author     
 * @version 1.0
 * @since JDK1.6
 */
public class BaseTool {
	/**
	 * <p>检测给定的对象是否不为 <code>null</code> 并且是一个 <code>{@link java.lang.Integer}</code> 
	 * 或是一个能被转换为 <code>{@link java.lang.Integer}</code> 对象.</p>
	 * @param checkObj 需要被检测的对象
	 * @return 被检测的对象是一个 <code>{@link java.lang.Integer}</code>
	 * 对象并且不为空则或是一个可以被转换为 {@link java.lang.Integer}
	 * 类型的字符串时返回 <code>true</code>, 否则返回 <code>false</code> . 
	 * */
	public static boolean isInteger(Object checkObj) {
		if (checkObj == null)
			return false;
		// 如果字符串则进行数据转换, 成功返回 true 发生错误或转换失败返回 false .
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			try {
				Integer.parseInt(checkObj.toString());
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		if (Integer.class.isAssignableFrom(checkObj.getClass()))
			return true;
		return false;
	}
	
	/**
	 * <p>检测给定的对象是否不为 <code>null</code> 并且是一个 <code>{@link java.lang.Long}</code> 
	 * 或是一个能被转换为 <code>{@link java.lang.Long}</code> 对象.</p>
	 * @param checkObj 需要被检测的对象
	 * @return 被检测的对象是一个 <code>{@link java.lang.Long}</code>
	 * 对象并且不为空则或是一个可以被转换为 {@link java.lang.Long}
	 * 类型的字符串时返回 <code>true</code>, 否则返回 <code>false</code> . 
	 * */
	public static boolean isLong(Object checkObj) {
		if (checkObj == null)
			return false;
		// 如果字符串则进行数据转换, 成功返回 true 发生错误或转换失败返回 false .
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			try {
				Long.parseLong(checkObj.toString());
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		if (Long.class.isAssignableFrom(checkObj.getClass()))
			return true;
		return false;
	}
	
	/**
	 * <p>检测给定的对象是否不为 <code>null</code> 并且是一个 <code>{@link java.lang.Float}</code> 
	 * 或是一个能被转换为 <code>{@link java.lang.Float}</code> 对象.</p>
	 * @param checkObj 需要被检测的对象
	 * @return 被检测的对象是一个 <code>{@link java.lang.Float}</code>
	 * 对象并且不为空则或是一个可以被转换为 {@link java.lang.Float}
	 * 类型的字符串时返回 <code>true</code>, 否则返回 <code>false</code> . 
	 * */
	public static boolean isFloat(Object checkObj) {
		if (checkObj == null)
			return false;
		// 如果字符串则进行数据转换, 成功返回 true 发生错误或转换失败返回 false .
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			try {
				Float.parseFloat(checkObj.toString());
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		if (Float.class.isAssignableFrom(checkObj.getClass()))
			return true;
		return false;
	}
	
	/**
	 * <p>检测给定的对象是否不为 <code>null</code> 并且是一个 <code>{@link java.lang.Double}</code> 
	 * 或是一个能被转换为 <code>{@link java.lang.Double}</code> 对象.</p>
	 * @param checkObj 需要被检测的对象
	 * @return 被检测的对象是一个 <code>{@link java.lang.Double}</code>
	 * 对象并且不为空则或是一个可以被转换为 {@link java.lang.Double}
	 * 类型的字符串时返回 <code>true</code>, 否则返回 <code>false</code> . 
	 * */
	public static boolean isDouble(Object checkObj) {
		if (checkObj == null)
			return false;
		// 如果字符串则进行数据转换, 成功返回 true 发生错误或转换失败返回 false .
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			try {
				Double.parseDouble(checkObj.toString());
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		if (Double.class.isAssignableFrom(checkObj.getClass()))
			return true;
		return false;
	}
	
	/**
	 * <p>检则一个对象是否不为空, 如果为字符串则会剔除空格来进行验证.</p>
	 * <p style="color:red;">字符串数据 <code>""</code> 同样为视为 <code>null</code> .</p>
	 * @param checkObj 需要被检测的对象
	 * @return 如查不为空,返回<code>true</code>,如果为空返回<code>false</code>.
	 * */
	public static boolean isNotNull(Object checkObj){
		if(checkObj == null)
			return false;
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			if (checkObj.toString().trim().equals(""))
				return false;
		}
		return true;
	}
	
	/**
	 * <p>检测一个对象是否为空, 如果为字符串则会剔除空格进行验证.</p>
	 * <p style="color:red;">字符串数据 <code>""</code> 同样被视为 <code>null</code> .</p>
	 * @param checkObj 需要被检测的对象.
	 * @return 如果对象为空返回 <code>true</code> 不为空返回 <code>false</code> .
	 * */
	public static boolean isNull(Object checkObj) {
		if (checkObj == null)
			return true;
		// 字符串验证
		if (String.class.isAssignableFrom(checkObj.getClass())) {
			if (checkObj.toString().trim().length() < 1 || 
					checkObj.toString().trim().equals(""))
				return true;
		}
		return false;
	}
	
	/**
	 * 检查字符串是否在minLength和maxLength之间,
	 * 如查str为<code>null</code>或minLength大于maxLength都
	 * 将直接返回<code>false</code>.此方法不会剔除字符串中的空格
	 * 来进行检查,如果字符串长度是在minLength和
	 * maxLength之间将返回<code>true</code>
	 * @param str 需要检查的字符串
	 * @param minLength 字符串最小长度
	 * @param maxLength 字符串最大长度
	 * @return 在minLength之间返回<code>true</code>,否则返回<code>false</code>.
	 * */
	public static boolean checkLength(String str, 
			Integer minLength, Integer maxLength){
		if(str == null)
			return false;
		if(minLength > maxLength)
			return false;
		if(str.length() >= minLength && str.length() <= maxLength)
			return true;
		return false;
	}
	
	/**
	 * 将一个<code>ISO-8859-1</code>的字符串转换为中文<code>gbk</code>编码的类型
	 * @param str 需要转换的字符串数据
	 * @return 转换完成后的字符串,如果转换失败则返回<code>null</code>对象.
	 * */
	public static String getChiese(String str){
		try {
			byte[] btStr = str.getBytes("ISO-8859-1");
	    	str = new String(btStr, "gbk");
	    	return str;
	    } catch(Exception ex){}
	    	// 2009-03-04重建时注释修改
	    	// return str;
	    	return null;
	}
	
	/**
	 * 取得一个按指定编号转换的字符
	 * @param sourceString 需要转换的源字符串
	 * @param getSourceEncoding 取得源字符串的编号
	 * @param changeEncoding 改变后的字符串编码
	 * @return 成功则返回转换成功的字符串,失败则返回<code>null</code>对象
	 * */
	public static String getChangeStringEncoding(
			String sourceString, String getSourceEncoding, String changeEncoding) {
		try{
			byte[] btStr = sourceString.getBytes(getSourceEncoding);
			sourceString = new String(btStr, changeEncoding);
			return sourceString;
	    }catch(Exception ex){}
	    return null;
	}
	/**
	 * 取得Web工程的物理工作空间地址, 值:{@value}
	 * */
	public static final String WEB_ROOT = loadWebRoot();
	/**
	 * 取得Web工程的物理工作空间地址
	 * @return 获取成功返回物理空间地址, 失败则返回一个 <code>null</code> 对象.
	 * */
	public static String getWebRoot() {
		return WEB_ROOT;
	}
	/**
	 * 读取Web项目的发工作空间地址
	 * @return 获取成功返回物理空间地址, 失败则返回一个 <code>null</code> 对象.
	 * */
	private static String loadWebRoot() {
		String classPath = BaseTool.class.getResource("/").getPath();
		try {
			classPath = java.net.URLDecoder.decode(classPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (BaseTool.isNull(classPath))
			return null;
		int bIndex = classPath.indexOf("WEB-INF/classes/");
		if (bIndex == -1)
			return null;
		return classPath.substring(0, bIndex).substring(1);
	}  
	
 
	public static String getProjectUrl(HttpServletRequest request) { 
		//System.out.println("proUrl="+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	}
}
