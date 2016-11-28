package com.kmfex.service;

import java.util.List;
import java.util.Map;

import com.kmfex.model.FinancingRestrain;
import com.wisdoor.core.service.BaseService;
/**
 * 
 * @author  
 *
 */
public interface FinancingRestrainService extends BaseService<FinancingRestrain>{
	public String getFinancingCodes(String username);
	public String getFinancingCodesByGroup(String groupId);
	 /**
     * 验证分组的约束规则1--新用户体验
     * @param userName  
     * @param financingBaseCode
     * @return
     */
	public List<Map<String,Object>> usergrouprestrainCheck(String userName,String financingBaseCode);
	/**
	 * 是否在优先投标组里
	 * @param userName
	 * @param financingBaseCode
	 * @return
	 */
	public boolean inUsergroupCheck(String userName,String financingBaseCode);
}
