package com.kmfex.service;

import com.kmfex.model.CreditRules;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.service.BaseService;

public interface CreditRulesService extends BaseService<CreditRules>{
	
	public void open_an_account(Account account);
}
