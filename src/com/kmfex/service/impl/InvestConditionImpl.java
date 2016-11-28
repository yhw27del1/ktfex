package com.kmfex.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.kmfex.model.InvestCondition;
import com.kmfex.model.MemberLevel;
import com.kmfex.service.InvestConditionService;
import com.kmfex.service.MemberLevelService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
/**
 * 
 * @author eclipse
 *
 */
@Service
public class InvestConditionImpl extends BaseServiceImpl<InvestCondition> implements InvestConditionService{
	@Resource MemberLevelService memberLevelService;
	
	//投资人的投标约束
	@Override
	public InvestCondition getInvestCondition(MemberLevel level) {
		String hql = "";
		if(null!=level){
			hql = "from InvestCondition co where co.memberLevel.id='"+level.getId()+"'";
		}else{
			hql = "from InvestCondition co where co.memberLevel.id is null";
		}
		return this.getCommonListData(hql).get(0);
	}

}
