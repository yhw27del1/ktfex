package com.kmfex.zhaiquan.service;

import com.kmfex.model.InvestRecord;
import com.kmfex.zhaiquan.model.Buying;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

public interface BuyingService extends BaseService<Buying> {

	public Buying saveBuying(Buying buy) throws Exception;

	/*
	 * 查找是否有满足撮合的受让记录(价格优先，时间优先)
	 * 
	 * @param investRecordId 投标记录
	 * 
	 * @param money 报价
	 * 
	 * @return
	 */
	public Buying getBuyingByUnit(String investRecordId, double money);

	/**
	 * 受让撤销
	 * 
	 * @param buyingId
	 */
	public void cancel(String buyingId) throws EngineException;

	/**
	 * 修改债权挂牌主状态
	 * 
	 * @param zqzrState
	 * @param investRecordId
	 * @throws EngineException
	 */
	public void upZqzrState(String zqzrState, String investRecordId)
			throws EngineException;

	/**
	 * 判断指定会员(的账号)是否已对指定的债权下达受让指令
	 * */
	public boolean isAlreadyBought(User memberUser,InvestRecord investRecord);

}
