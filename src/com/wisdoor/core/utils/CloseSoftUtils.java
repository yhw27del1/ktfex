package com.wisdoor.core.utils;

import com.kmfex.webservice.vo.MessageTip;


/**
 * 新系统上线，关闭新系统提示
 */  
public class CloseSoftUtils {
 	/** 
 	 *关闭发包的方法
 	 */
 	public static MessageTip closeFinaning() {
 		String tipSt="交易系统无法进行此项操作。"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	
 	/** 
 	 *关闭会员开户的方法
 	 */
 	public static MessageTip closeMember() {
 		String tipSt="交易系统无法进行此项操作。"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	
 	/** 
 	 * 关闭pc投标方法
 	 */
 	public static MessageTip closePcInvest() {
 		String tipSt="客户端已停止使用！请各位会员登陆官网进行下载全新版本，" +
 				"安装后再进行业务操作！"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	/** 
 	 * 关闭自动投标方法
 	 */
 	public static MessageTip closeAutoInvest() {
 		String tipSt="客户端已停止使用！请各位会员登陆官网进行下载全新版本，" +
 		"安装后再进行业务操作！"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	
 	/** 
 	 * 关闭IOS投标方法
 	 */
 	public static MessageTip closeIosInvest() {
 		String tipSt="客户端已停止使用！我们对客户端进行了全新的设计，IOS系统手机客户端将会在不久之后发布使用，"+
			"当前情况下，仅能通过新的安卓手机客户端和新的电脑客户端进行业务操作，因此给您带来的不便，向您表示深深的歉意！"+
			"感谢您对微交所的支持！"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	
 	/** 
 	 * 关闭android投标方法
 	 */
 	public static MessageTip closeAndroidInvest() {
 		String tipSt="客户端已停止使用！我们对客户端进行了全新的设计，您可登陆官网下载新的客户端，安装后登陆，您还可登陆官网下载新的电脑客户得到全新体验。"; 
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	

 
 	public static MessageTip closeOutInMoney() {
 		String tipSt="客户端已停止使用！请各位会员登陆官网进行下载全新版本，安装后再进行出入金操作！";  
 		MessageTip tip = new MessageTip(true, tipSt);
 		return tip;
 	}
 	

 	
	/**
	安卓手机客户端提示：
	尊敬的用户您好！您当前使用的客户端已停止使用！对客户端进行了全新的设计，您可登陆微交所官网下载新的客户端，安装后登陆，您还可登陆官网下载新的电脑客户得到全新体验。
	因此给您带来的不便，微交所向您表示深深的歉意！
	
	苹果手机客户端提示：
	尊敬的用户您好！您当前使用的微交所客户端已停止使用！微交所对客户端进行了全新的设计，IOS系统手机客户端将会在不久之后发布使用，
	当前情况下，仅能通过新的安卓手机客户端和新的电脑客户端进行业务操作，因此给您带来的不便，向您表示深深的歉意！  
	 */
}