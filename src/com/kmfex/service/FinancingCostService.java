package com.kmfex.service;
 
import java.io.Serializable;

import com.kmfex.model.FinancingCost;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;
/**
 * @author  
 * */
public interface FinancingCostService extends BaseService<FinancingCost> {

	public FinancingCost insertFinancingCost(String id) throws EngineException;
	
	public FinancingCost getCostByFinancingBase(String fid) throws EngineException;
	
	public FinancingCost getCostByFinancer(String id) throws EngineException;
	public void feiyongjisuan(FinancingCost financingCost,FinancingCost financingCostIN,MemberBase financier,User u) throws Exception;
	
	public void cost_pass(Serializable id)  throws Exception;
	public void cost_ignore(Serializable id)  throws Exception;
}
