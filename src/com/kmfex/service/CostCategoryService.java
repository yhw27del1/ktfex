package com.kmfex.service;

import com.kmfex.model.CostBase;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.BaseService;

/**
 * 收费项目
 * 
 * @author eclipse,aora
 * 
 */
public interface CostCategoryService extends BaseService<CostBase> {

	/**
	 * 按指定关键字返回收费项目列表
	 * 
	 * @param keyword
	 *            指定关键字：收费项目名称或编码
	 * @param pageSize
	 *            每页要显示的记录数
	 * @param pageNo
	 *            要插询的页码
	 * */
	public PageView<CostBase> queryForConditions(String keyword, int pageSize,
			int pageNo);
}
