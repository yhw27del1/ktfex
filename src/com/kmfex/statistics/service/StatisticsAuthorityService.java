package com.kmfex.statistics.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wisdoor.core.model.Org;
import com.wisdoor.core.service.BaseService;

public interface StatisticsAuthorityService extends BaseService<Org> {
	/**
	 * 得到某个授权中心交易量数据
	 * @param year
	 * @param queryByOrgCode
	 * @return
	 */
	public List<Map<String,Object>> getMapList_Jyl(int year,String queryByOrgCode);


	public List<Map<String, Object>> getMapList_forPie(Date startDate,
			Date endDate, String queryByOrgCode);
 
}
