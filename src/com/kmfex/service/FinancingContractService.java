package com.kmfex.service;

import java.util.List;

import com.kmfex.model.ContractKeyData;
import com.kmfex.model.FinancingContract;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface FinancingContractService extends BaseService<FinancingContract> {
	
	public List<ContractKeyData> getContractList(String id);
	
	public FinancingContract getByFinancingBase(String id);
	
	public List<FinancingContract> listByFinancinger(String rzrid);
}
