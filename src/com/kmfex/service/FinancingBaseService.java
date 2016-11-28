package com.kmfex.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.kmfex.model.FinancingBase;
import com.kmfex.model.PreFinancingBase;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

/**
 * @author  
 * @author aora
 * <pre>
 * 2012-08-14 aora 增加cancel(String financingBaseId)方法的定义，
 * 此方法用于取消指定的融资项目。
 * </pre>
 * */
public interface FinancingBaseService extends BaseService<FinancingBase> {
	public boolean getFinancingBase(String code);

	public void noCheck(String financingBaseId, String code, String opeNote)
			throws Exception;
	public void fabuBohui(String financingBaseId) throws Exception;
	public void xyFinish(User u, PreFinancingBase preFinancingBase,
			String financierId, String guaranteeId) throws Exception;

	/**
	 * 取消指定的融资项目。
	 * 
	 * <pre>
	 * 只有签约之前的融资项目才能取消。
	 * 若还没有投资人投标指定的融资项目，则直接修改此融资项目的状态为“取消”；
	 * 若已经有投资人投标了指定的融资项目，则先解冻所有投标了
	 * 此融资项目的投资人的账户，再删除此融资项目的所有投标记录、借款合同及其融资
	 * 人的融资费用等记录，最后修改此融资项目的状态为“取消”。
	 * 
	 * <pre>
	 * @param financingBaseId 融资项目id号
	 * @return true 取消成功；false 取消失败
	 * */
	public boolean cancel(String financingBaseId) throws Exception;
	
	public Map<String[],Double> groupByBank(FinancingBase fb);
	public boolean terminal(String fid);
	
	/**
	 * 取得融资项目融资方
	 * @param fid 融资项目ID
	 * @return
	 */
	public User getUserForFinancier(String fid);
	/**
	 * 
	 * @param subsql 
	 * @return 0:最大投标额 1:剩余投标额 2:已投标额
	 */
	public long[] queryForExpiredListCount(String subsql,Object [] args);
	
	
	public List<Map<String,Object>> queryLogs(String id);
	
	/**
	 * 融资项目签约
	 * @param id 融资项目ID
	 */
	public void sign(Serializable id);
	
}
