package com.kmfex.service;

import com.kmfex.model.CoreAccountLiveRecord;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.BaseService;
/**
 * 中心帐户操作service
 * @author eclipseeeeee
 *
 */
public interface CoreAccountService extends BaseService<CoreAccountLiveRecord>{
	//中心账户查询，按日期汇总,分页困难,需要写存储过程或视图
	public QueryResult<Object> groupByCreatDate(final String wherejpql) throws Exception ;
	
	public double clear();
}
