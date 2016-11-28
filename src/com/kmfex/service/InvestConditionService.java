package com.kmfex.service;

import com.kmfex.model.InvestCondition;
import com.kmfex.model.MemberLevel;
import com.wisdoor.core.service.BaseService;

/**
 * 
 * 
 */
public interface InvestConditionService extends BaseService<InvestCondition>{
	//投资人的投标约束
	public InvestCondition getInvestCondition(MemberLevel level);
}
