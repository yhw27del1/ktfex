package com.wisdoor.core.trigger;

import com.kmfex.service.FinancingBaseService;

public class TimeoutTransaction extends TransactionBase {

	private static FinancingBaseService financingBaseService;
	
	
	public void excute() throws Exception { 
	}

	@Override
	public void init() throws Exception {
		financingBaseService = (FinancingBaseService)wac.getBean("financingBaseImpl"); 
		
	}



}
