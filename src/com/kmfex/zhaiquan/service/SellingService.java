package com.kmfex.zhaiquan.service;

import com.kmfex.model.InvestRecord;
import com.kmfex.zhaiquan.model.Selling;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

public interface SellingService extends BaseService<Selling> {
 
	public Selling saveSelling(Selling selling) throws Exception;

	/**
	 * 查找是否有满足撮合的出让记录(价格优先，时间优先) 
	 * @param investRecordId 投标记录
	 * @param money  报价
	 * @return  
	 */
	public Selling getSellingByUnit(String investRecordId, double money);
    /**
     * 出让撤销
     * @param buyingId
     * @throws EngineException
     */
	public void cancel(String buyingId) throws EngineException;
	
	/**
	 * 判断指定会员(的用户)是否已对指定债权下达过转让指令
	 * */
	public boolean isAlreadySold(User memberUser,InvestRecord investRecord);
	
	
	public void _cancel(String investrecord_id);
}
