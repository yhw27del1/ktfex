package com.kmfex.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.CostItem;
import com.kmfex.service.ChargingStandardService;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * 
 * @author eclipse,aora
 * 
 */
@Service
@Transactional
public class ChargingStandardImpl extends BaseServiceImpl<CostItem> implements
		ChargingStandardService {
	@Override
	public CostItem findCostItem(String costBaseCode, String memberTypelCode) {
		String queryString = "from CostItem c where c.costBase.code = ? and c.memberTypel.code=? ";
		CostItem costItem = selectById(queryString, new String[] {
				String.valueOf(costBaseCode), String.valueOf(memberTypelCode) });
		return costItem;
	}

	@Override
	public PageView<CostItem> queryForCondition(String keyword,String memberTypeId, int pageSize,
			int pageNo) {
		PageView<CostItem> pageView = new PageView<CostItem>(pageSize, pageNo);
		StringBuilder hql = new StringBuilder("1=1 ");
		ArrayList<String> params = new ArrayList<String>();
		if (!isEmpty(keyword)) {
			hql.append("and (costBase.name like ? or costBase.code like ?) ");
			params.add("%" + trim(keyword) + "%");
			params.add("%" + trim(keyword) + "%");
		}
		if(!isEmpty(memberTypeId)){
			hql.append("and memberTypel.id = ? ");
			params.add(trim(memberTypeId));
		}
		hql.append("order by costBase.addtime desc ");
		try {
			pageView.setQueryResult(this.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), hql.toString(),
					params));
		} catch (Exception e) {
			e.printStackTrace();
			return pageView;
		}
		return pageView;
	}
}
