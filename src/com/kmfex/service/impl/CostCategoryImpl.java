package com.kmfex.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.kmfex.model.CostBase;
import com.kmfex.service.CostCategoryService;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * 收费项目
 * 
 * @author eclipse
 * 
 */
@Service
public class CostCategoryImpl extends BaseServiceImpl<CostBase> implements
		CostCategoryService {

	@Override
	public PageView<CostBase> queryForConditions(String keyword, int pageSize,
			int pageNo) {
		PageView<CostBase> pageView = new PageView<CostBase>(pageSize, pageNo);
		StringBuilder hql = new StringBuilder("1=1 ");
		ArrayList<String> params = new ArrayList<String>();
		if (!isEmpty(keyword)) {
			hql.append("and (name like ? or code like ?)");
			params.add("%" + trim(keyword) + "%");
			params.add("%" + trim(keyword) + "%");
		}
		hql.append("order by addtime asc ");
		try {
			pageView.setQueryResult(this.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), hql.toString(),
					params));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return pageView;
		}
		return pageView;
	}

}
