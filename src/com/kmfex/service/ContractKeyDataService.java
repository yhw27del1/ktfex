package com.kmfex.service;

import com.kmfex.model.ContractKeyData;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.service.BaseService;

public interface ContractKeyDataService extends BaseService<ContractKeyData> {
	/**
	 * 更新合同各种费用
	 * @param 合同ID
	 * @throws EngineException
	 */
	
	public void contractKeyDataFeeUpdate(String id,FinancingCost fc) throws EngineException;
	
	
	
	/**
	 * 我的投标记录ID
	 * @param   first_party_code 投资人编码 
	 * @return  1，2，3
	 */
	public String findContractKeyDatas(String first_party_code);
}
