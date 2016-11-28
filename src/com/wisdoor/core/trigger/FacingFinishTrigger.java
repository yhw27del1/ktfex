package com.wisdoor.core.trigger;
 
 
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;

public class FacingFinishTrigger extends TransactionBase {
	private static  FinancingBaseService financingBaseService;
	private static  FinancingCostService financingCostService;
	@Override
	public void excute() throws Exception {
	  
/*	  List<FinancingBase> lists=this.financingBaseService.getCommonListData("");
	  for(FinancingBase fb:lists)    
	  {
		  this.financingCostService.insertFinancingCost(fb.getId());
	  }*/
	}
	@Override
	public void init() throws Exception {
		financingBaseService = (FinancingBaseService)wac.getBean("financingBaseImpl"); 
		financingCostService = (FinancingCostService)wac.getBean("financingCostImpl"); 
		
	}
 
	

}
