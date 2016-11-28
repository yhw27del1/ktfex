package com.kmfex.autoinvest.service;

import java.util.Map;

import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.vo.PreParams;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.service.BaseService;

/**
 * @author    
 */
public interface UserParameterService extends BaseService<UserParameter> { 
	
 /**
  * 根据融资项目查中签的人
  * @param financingId 融资项目ID
  * @return
  */
 public Map<String, Object> drawByFinancingId(String financingid) throws Exception;
 /**
  * 根据协议参数查中签的人
  * @param params 
  */
 public Map<String, Object> drawByPreParams(PreParams preParams,FinancingBase fb) throws Exception;
 /**
  * 设置参数
  * @param member
  * @param userParameter
  * @throws EngineException
  */
 public void insertAutoParam(MemberBase member,UserParameter userParameter) throws EngineException;
 /***
  * 解除协议
  * @param username
  * @throws EngineException
  */
 public void stopAutoParam(String username) throws EngineException; 
 /**
  * 让参数生效和解约
  * @throws EngineException
  */
 public void autoParamOpen();
 
}
