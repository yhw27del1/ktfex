package com.kmfex.service;

import com.kmfex.model.CostItem;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.BaseService;

/**
 * 
 * @author eclipse,aora
 * 
 */
public interface ChargingStandardService extends BaseService<CostItem> {
	/*
	 * 根据收费项目编码、得到收费标准
	 */
	public CostItem findCostItem(String costBaseCode, String memberTypelCode);

	/**
	 * 分页按条件查询指定收费项目名称或编码的收费标准明细
	 * 
	 * @param keyword
	 *            查询关键字：收费项目名称或编码
	 * @param memberTypeId 会员类型id号
	 * @param pageSize
	 *            每页要显示的记录数
	 * @param pageNo
	 *            要查询的页码
	 * */
	public PageView<CostItem> queryForCondition(String keyword,String memberTypeId, int pageSize,
			int pageNo);
}
