package com.kmfex.service;

import com.kmfex.model.SignHistory;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface SignHistoryService extends BaseService<SignHistory> {
	public SignHistory getHistory(long owner,int signBank,int signType);
	
	//0:可用金额 1:冻结金额
	public double[] icbc_balance_frozen(String subsql);
	
	//0:可用金额 1:冻结金额
	public double[] balance_frozen(String subsql);
}