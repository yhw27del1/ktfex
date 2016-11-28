package com.kmfex.autoinvest.webservice;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.model.UserPriority;
import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberBaseService;
import com.kmfex.webservice.vo.MessageTip;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.CloseSoftUtils;
import com.wisdoor.core.utils.MD5;

/** 
 * @author   访问地址：http://localhost:8080/services/kmfex2Service?wsdl 
 */
@Component
@WebService(serviceName = "kmfex2Service")
public class KeFex2Service implements Serializable {
 
	private static final long serialVersionUID = 8996896899791945942L;

	@Resource
	transient UserService userService;	
	@Resource
	transient  UserParameterService userParameterService;
	@Resource
	transient  UserPriorityService userPriorityService; 
	@Resource
	transient MemberBaseService memberBaseService;
        /**
         * 自动投标参数设置(多选用半角逗号相隔) 
		 * @param username 用户名
		 * @param password MD5后的密码
		 * @param s1风险评级-->如：1,2,3,4,选择了四项
		 * @param s2还款方式 --> 如：1,选择了第一项按月等额本息 
		 * @param dayMin;//按天最小值 
		 * @param dayMax;//按天最大值 
		 * @param monthMin;//按月最小值 
		 * @param monthMax//按月最大值
		 * @param s5担保方式 
		 * @param s6按月年利率(不低于)-->如: 10.5表示按月的融资项目，年利率不低于10.5,double型数字
		 * @param s7按天年利率(不低于)-->如: double型数字
		 * @param param1=时间戳或随机数
		 * @param param2=MD5(MD5后的密码+param1+username) 
		 */
	public MessageTip2 setParams(String username, String password,String s1,String s2,long dayMin, long dayMax, long monthMin, long monthMax,String s5,double s6,double s7,String param1,String param2) throws Exception{ 
		MessageTip2 tip = new MessageTip2(true, "自动投标参数设置成功，下一交易日生效！"); 
		MessageTip tip0 = CloseSoftUtils.closeAutoInvest(); 
		if(tip0.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg(tip0.getMsg()); 
			return tip;  
		}
	    try { 
	    	MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			}   
			User user = userService.findUser(username, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			} else { 
				MemberBase member = this.memberBaseService.getMemByUser(user);
				UserParameter userParameter=new UserParameter();
				//参数赋值
				if (BaseTool.isNull(s1)||BaseTool.isNull(s2)||BaseTool.isNull(s5)) {
					tip.setSuccess(false);
					tip.setMsg("自动投标参数设置错误！"); 
					return tip;  
				}  
				userParameter.setParam1(s1);
				userParameter.setParam2(s2);
				userParameter.setDayMin(dayMin);
				userParameter.setDayMax(dayMax);
				userParameter.setMonthMin(monthMin);
				userParameter.setMonthMax(monthMax);
				userParameter.setParam5(s5); 
				userParameter.setParam6(s6); 
				userParameter.setParam7(s7);  
			    this.userParameterService.insertAutoParam(member, userParameter); 
			}	 
		      
		} catch (Exception e) {
			 e.printStackTrace();
			 tip.setMsg("自动投标参数设置异常！");
			 tip.setSuccess(false); 
		} 
		return tip;
	}
	/**
	 * 自动投标参数设置2(多选用半角逗号相隔) 
	 * @param username 用户名
	 * @param password MD5后的密码
	 * @param s1风险评级-->如：1,2,3,4,选择了四项
	 * @param s2还款方式 --> 如：1,选择了第一项按月等额本息 
	 * @param dayMin;//按天最小值 
	 * @param dayMax;//按天最大值 
	 * @param monthMin;//按月最小值 
	 * @param monthMax//按月最大值
	 * @param s5担保方式 
	 * @param s6按月年利率(不低于)-->如: 10.5表示按月的融资项目，年利率不低于10.5,double型数字
	 * @param s7按天年利率(不低于)-->如: double型数字
	 * @param s8账户可用余额不低于-->如: double型数字(为0表示不限制)
	 * @param s9单笔投标的最大金额-->如: double型数字(为0表示不限制)
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5后的密码+param1+username) 
	 */
	public MessageTip2 setParams(String username, String password,String s1,String s2,long dayMin, long dayMax, long monthMin, long monthMax,String s5,double s6,double s7,double s8,double s9,String param1,String param2) throws Exception{ 
		MessageTip2 tip = new MessageTip2(true, "自动投标参数设置成功，下一交易日生效！"); 
		MessageTip tip0 = CloseSoftUtils.closeAutoInvest(); 
		if(tip0.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg(tip0.getMsg()); 
			return tip;  
		}
		
		try { 
			MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			}   
			User user = userService.findUser(username, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			} else { 
				MemberBase member = this.memberBaseService.getMemByUser(user);
				UserParameter userParameter=new UserParameter();
				//参数赋值
				if (BaseTool.isNull(s1)||BaseTool.isNull(s2)||BaseTool.isNull(s5)) {
					tip.setSuccess(false);
					tip.setMsg("自动投标参数设置错误！"); 
					return tip;  
				}  
				userParameter.setParam1(s1);
				userParameter.setParam2(s2);
				userParameter.setDayMin(dayMin);
				userParameter.setDayMax(dayMax);
				userParameter.setMonthMin(monthMin);
				userParameter.setMonthMax(monthMax);
				userParameter.setParam5(s5); 
				userParameter.setParam6(s6); 
				userParameter.setParam7(s7);  
				userParameter.setParam8(s8);
				userParameter.setParam9(s9);
				this.userParameterService.insertAutoParam(member, userParameter); 
			}	 
			
		} catch (Exception e) {
			e.printStackTrace();
			tip.setMsg("自动投标参数设置异常！");
			tip.setSuccess(false); 
		} 
		return tip;
	}
	/**
	 * 开头协议
	 * @param username 用户名
	 * @param password MD5后的密码 
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5后的密码+param1+username) 
	 */
	public MessageTip2 firstAutoInvest(String username, String password,String param1,String param2) throws Exception{
		
		MessageTip2 tip = new MessageTip2(true, "自动投标开通成功！"); 
		MessageTip tip0 = CloseSoftUtils.closeAutoInvest(); 
		if(tip0.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg(tip0.getMsg()); 
			return tip;  
		}
		
		try { 
			MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("开通失败！"); 
				return tip;  
			}   
			User user = userService.findUser(username, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("开通失败！"); 
				return tip;  
			} else { 
				UserPriority userPriority=userPriorityService.selectById(username);
				SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss"); 
				if(null==userPriority){
					userPriority=new UserPriority(username,null,Long.valueOf(sf.format(new Date())), 0l); 
					userPriorityService.insert(userPriority);
				}
			}	 
			
		} catch (Exception e) {
			e.printStackTrace();
			tip.setMsg("开通失败！");
			tip.setSuccess(false); 
		} 
		return tip;
	}
	/**
	 * 主动退出自动自动投标
	 * @param username 用户名
	 * @param password MD5后的密码
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5后的密码+param1+username)
	 */
	public MessageTip2 stopAuto(String username, String password,String param1,String param2) throws Exception{ 
		MessageTip2 tip = new MessageTip2(true, "自动投标解约成功，下一交易日生效！"); 
		MessageTip tip0 = CloseSoftUtils.closeAutoInvest(); 
		if(tip0.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg(tip0.getMsg()); 
			return tip;  
		}
		try {   
			MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("解约失败！"); 
				return tip;  
			}     
			User user = userService.findUser(username, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("解约失败！"); 
				return tip;  
			} else {  
				this.userParameterService.stopAutoParam(username);
			}	
	
			
		} catch (Exception e) {
			e.printStackTrace();
			tip.setMsg("解约失败！");
			tip.setSuccess(false); 
		} 
		return tip;
	}
	/**
	 * 根据用户名得到服务端自动情况
	 * @param username 用户名
	 * @param password MD5后的密码
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5后的密码+param1+username)
	 */
	
	public ParamVo startParam(String username, String password,String param1,String param2) throws Exception{ 
		ParamVo tip = new ParamVo(true, "自动投标正常使用！"); 
		MessageTip tip0 = CloseSoftUtils.closeAutoInvest(); 
		if(tip0.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg(tip0.getMsg()); 
			return tip;  
		}
		try {   
			MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			}     
			User user = userService.findUser(username, password);
			if (null == user) {  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			} else { 
				UserParameter up=this.userParameterService.selectByHql("from UserParameter o where  o.nextFlag='1' and o.member.user.username='"+username+"'");
				if(null!=up){
					tip.setS1(up.getParam1());
					tip.setS2(up.getParam2());
					tip.setDayMin(up.getDayMin());
					tip.setDayMax(up.getDayMax());
					tip.setMonthMin(up.getMonthMin());
					tip.setMonthMax(up.getMonthMax());
					tip.setS5(up.getParam5());
					tip.setS6(up.getParam6());
					tip.setS7(up.getParam7());
					tip.setS8(up.getParam8());
					tip.setS9(up.getParam9());
					tip.setNextFlag(up.getNextFlag());
					tip.setSuccess(true);
					tip.setMsg("最新设置的参数！");
					UserPriority userPriority=this.userPriorityService.selectById(username);
				    if(null!=userPriority&&userPriority.getStopTime()>up.getCreateTime()){
						tip.setMsg("自动投标解约已经提交！");
						tip.setSuccess(false); 	
						return tip; 
					} 
                }else{ 
            		    tip.setMsg("自动投标申请已经提交！");
						tip.setSuccess(false); 	
						return tip; 
                } 
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			tip.setMsg("操作异常！");
			tip.setSuccess(false); 
		} 
		return tip;
	}
	/**
	 * 获得用户使用中的协议参数
	 * @param username 用户名
	 * @param password MD5后的密码
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5后的密码+param1+username)
	 */
	
	public ParamVo currentParam(String username, String password,String param1,String param2) throws Exception{ 
		ParamVo tip = new ParamVo(true, "当前生效的自动投标参数！");  
		try {   
			MessageTip2 checkTip=checkRequest2(username,password,param1,param2);
			if(!checkTip.isSuccess()){  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			}     
			User user = userService.findUser(username, password);
			if (null == user) {  
				tip.setSuccess(false);
				tip.setMsg("操作错误！"); 
				return tip;  
			} else { 
				UserPriority userPriority=this.userPriorityService.selectById(username);
				if(null!=userPriority){
					if(null==userPriority.getUserParameter()){
						tip.setSuccess(false);
						tip.setMsg("暂无生效的自动投标参数！"); 
						return tip;   
					}else{
						UserParameter up=userPriority.getUserParameter();
						tip.setS1(up.getParam1());
						tip.setS2(up.getParam2());
						tip.setDayMin(up.getDayMin());
						tip.setDayMax(up.getDayMax());
						tip.setMonthMin(up.getMonthMin());
						tip.setMonthMax(up.getMonthMax());
						tip.setS5(up.getParam5());
						tip.setS6(up.getParam6());
						tip.setS7(up.getParam7());
						tip.setS8(up.getParam8());
						tip.setS9(up.getParam9());
						tip.setNextFlag(up.getNextFlag());
						tip.setSuccess(true); 
						if(userPriority.getLastTime()>0){
							tip.setLastDate((userPriority.getLastTime()+"").substring(0,8));
						}
						
					}
					
				}else{
					tip.setMsg("未签署过自动投标！"); 
					tip.setSuccess(false); 
					return tip;
				}
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			tip.setMsg("操作异常！");
			tip.setSuccess(false); 
		} 
		return tip;
	}
	/** 
	 * 验证非法请求2(加密传值的约定) 
	 * @param username 用户名 
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5(密码)+param1+username)
	 *        32位MD5小写
	 * @return
	 */
	private MessageTip2 checkRequest2(String username,String password,String param1,String param2){
		MessageTip2 tip = new MessageTip2(true, "验证成功！"); 
		String keyStr=MD5.MD5Encode(password+param1+username);
		if(!keyStr.equals(param2)){
			tip.setSuccess(false);
			tip.setMsg("错误！");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(format.format(new Date())+"非法请求的KeFex2Service用户:"+username); 
			return tip;
		} 
		return tip;
		
	}  
	

	public KeFex2Service() {
		
	} 
}
