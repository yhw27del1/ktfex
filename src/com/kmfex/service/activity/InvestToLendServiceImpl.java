package com.kmfex.service.activity;

import org.springframework.stereotype.Service;

import com.kmfex.model.activity.InvestToLend;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
public class InvestToLendServiceImpl extends BaseServiceImpl<InvestToLend> implements InvestToLendService{

	@Override
	public boolean getInvestToLend(User user) {
		String hql = "from InvestToLend o where o.user.id="+user.getId();
		int m = this.getCommonListData(hql).size();
		if(m>0){//有记录维护，返回true
			return true;
		}else{
			return false;
		}
	}
	
}
