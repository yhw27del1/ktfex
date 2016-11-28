package com.kmfex.autoinvest.service;

import java.util.List;
import java.util.Map;

import com.kmfex.InvestVO;
import com.kmfex.autoinvest.model.UserPriority;
import com.kmfex.autoinvest.vo.Draw;
import com.kmfex.model.BusinessType;
import com.kmfex.model.FinancingBase;
import com.wisdoor.core.service.BaseService;

/**
 * @author   
 * */
public interface UserPriorityService extends BaseService<UserPriority> { 
	/**
	 * 是否已经开通过协议
	 * @param username
	 * @return
	 */
	public Boolean firstAutoInvest(String username);
	/**
	 * 求最小最大值
	 * @param in_userName
	 * @param in_financingBaseId
	 * @param in_usertype
	 * @return
	 */
	public InvestVO getMaxMin(String in_userName, String in_financingBaseId,String in_usertype);
	public String[] ConverterParam2(BusinessType bt);
	
	/**
	 * 自动投标的方法
	 * @param draws 抽签抽中的人
	 * @param financingBase 要下单的融资项目
	 */
	public void autoInvest(List<Draw> draws, FinancingBase financingBase);
    /***
     * 
     * @param map.get("draws") 满足委托的人
     * @param map.get("n")  委托人数
     * @param map.get("w")  委托金额  
     * @return
     */
	public List<Draw> draw1(Map<String, Object> map,Double finacingMoney);//抽签第一波规则
}
