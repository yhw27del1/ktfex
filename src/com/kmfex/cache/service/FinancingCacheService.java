package com.kmfex.cache.service;

import java.util.Set;

import com.kmfex.model.FinancingBase;
import com.kmfex.webservice.vo.FinancingBaseVo;
import com.wisdoor.core.service.BaseService;

/**
 * @author   
 * */
public interface FinancingCacheService extends BaseService<FinancingBase> {
	//缓存---待发布、投标中的每个融资项目
	public  void doingInvests() throws Exception;
	//缓存---更新某个融资项目缓存
	public void updateFinancingCache(FinancingBaseVo vo) throws Exception;
	//缓存---删除某个融资项目缓存
	public void deleteFinancingCache(String financingBaseCode) throws Exception;
	//查询---某个融资项目
	public  FinancingBaseVo swithInvestList(String financingBaseCode) throws Exception;
	//得到目前投标中的融资项目ids列表 
	public  Set<String> getFinancingBaseCodes() throws Exception;
	//更新投标中的融资项目ids中的某个id
	public void updateFinancingBaseCode(String  financingBaseCode) throws Exception;
	//删除的融资项目列表ids中的某个id
	public void deleteFinancingBaseCode(String  financingBaseCode) throws Exception;
	//缓存投标结束的包(4已满标   5融资确认已完成  6费用核算完成  7签约完成 8已撤单)
	public  void stopInvests() throws Exception;  
	//融资项目下的用户
	public  Set<String> getPreInvestUsers(String code) throws Exception;
	//组成员变动，更新投标中的融资项目缓存
	public  void updateDoingFinancingGroupByGroupId(String groupId,String userName);
	//查数据库更新缓存
	public void updateOneFinancingCache(String financingBaseId);
	
}
