package com.kmfex.service.activity;

import com.kmfex.model.activity.InvestToLend;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

public interface InvestToLendService extends BaseService<InvestToLend>{
	public boolean getInvestToLend(User user);
}
