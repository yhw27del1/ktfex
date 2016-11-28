package junit4.datainit;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.FinancingCostService;
 
public class FinanCostTest { 
	private static FinancingCostService financingCostService;
	private static ChargingStandardService chargingStandardService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml"); 
			financingCostService = (FinancingCostService) cxt.getBean("financingCostImpl");
			chargingStandardService = (ChargingStandardService) cxt.getBean("chargingStandardImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
	     try {
  
	    		List<FinancingCost> costs=financingCostService.getScrollData().getResultlist(); 
	    		for(FinancingCost c:costs){
	    			FinancingBase financingBase=c.getFinancingBase(); 
	    			CostItem bzj = chargingStandardService.findCostItem("bzj", "R");// 保证金标准
		    	    if (financingBase.getCode().startsWith("X")) { 
		    			//兴易贷费用--风险管理费/信息咨询费
		    			CostItem fee1Item = chargingStandardService.findCostItem("xxzxf-xyd", "R");
						CostItem fee2Item = chargingStandardService.findCostItem("zhglf-xyd", "R");
						CostItem fee3Item = chargingStandardService.findCostItem("dbf-xyd", "R");
	                    c.setFee1_bl(fee1Item.getPercent());
	                    c.setFee1_tariff(fee1Item.getCostBase().getTariff());
	                    
	                    c.setFee2_bl(fee2Item.getPercent());
	                    c.setFee2_tariff(fee2Item.getCostBase().getTariff());
	                    
	                    c.setFee3_bl(fee3Item.getPercent());
	                    c.setFee3_tariff(fee3Item.getCostBase().getTariff());
	
		    		} else {// 0表示本金保障，1表示担保公司担保--费用标准 
		    			CostItem rzfwf = chargingStandardService.findCostItem("rzfwf", "R");// 融资服务费
		    			CostItem fxglfD = chargingStandardService.findCostItem("fxglf", "R");// 风险管理费
		    			CostItem dbf = chargingStandardService.findCostItem("dbf", "R");// 担保费  
		    			if ("10".equals(financingBase.getFxbzState())) {
		    				// 风险管理费--特殊来自业务类型
		    				 c.setFxglf_bl(financingBase.getBusinessType().getFxglf());
		    				 c.setFxglf_tariff(0);
		    				 
		    			} else {// 其他担保公司有担保费
		    				if (null != dbf) {// 担保费 
			    				 c.setDbf_bl(dbf.getPercent());
			    				 c.setDbf_tariff(dbf.getCostBase().getTariff());	
		    				}
		    				if (null != fxglfD) {// 风险管理费
			    				 c.setFxglf_bl(fxglfD.getPercent());
			    				 c.setFxglf_tariff(fxglfD.getCostBase().getTariff());
		 	    			}
		    			}
		    			if (null != rzfwf) {
		    				 c.setRzfwf_bl(rzfwf.getPercent());
		    				 c.setRzfwf_tariff(rzfwf.getCostBase().getTariff());
		    			}
	
		    		}
		 
	    			if (null != bzj) {
	   				 c.setBzj_bl(bzj.getPercent());
	   				 c.setBzj_tariff(bzj.getCostBase().getTariff());
	
	   			    }
		    		financingCostService.update(c);
	    		
	    		}
	    		
		    } catch (Exception e) { 
			e.printStackTrace();
		} 
	}
 
    
}
