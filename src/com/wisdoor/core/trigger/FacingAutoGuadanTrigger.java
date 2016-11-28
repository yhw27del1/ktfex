package com.wisdoor.core.trigger;
 
 
import java.util.Date;
import java.util.List;

import com.kmfex.model.FinancingBase;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.LogsService;

public class FacingAutoGuadanTrigger extends TransactionBase {
	private static  FinancingBaseService financingBaseService; 
 	private static   LogsService logsService;
 	private static   OpenCloseDealService openCloseDealService;
	@Override  
	public void excute() throws Exception {   
		byte state = openCloseDealService.checkState();
		if(1==state){//休市期间不自动挂单---20140429开启了委托投标的包,不自动挂单
			List<FinancingBase> ls=financingBaseService.getCommonListData("from FinancingBase f where f.state='1.5' and f.autoinvest='0' ");  
			User user=new User();
			user.setId(1151); 
			for(FinancingBase financing:ls){
				if(null!=financing&&"1.5".equals(financing.getState())){
					Date date=new Date();
					Logs l = new Logs();
					l.setOperate("挂单通过");
					l.setOperator(user);
					l.setIp("127.0.0.1");
					l.setTime(date);
					l.setEntityId(financing.getId());
					l.setEntityFrom("FinancingBase");
					logsService.insert(l);
					
					financing.setState("2");
					financing.setAuditDate(date); 
					//financing.getLogs().add(l);
					
					financing.setModifyDate(date);
					financingBaseService.update(financing); 
					//更新未结束的融资项目缓存  
					try { ReadsStaticConstantPropertiesUtil.updateServiceCache(financing.getId()); } catch (Exception e) {e.printStackTrace();} 
				} 
			}			
		}

		  
	}
	@Override
	public void init() throws Exception {
		financingBaseService = (FinancingBaseService)wac.getBean("financingBaseImpl"); 
		//financingCacheService = (FinancingCacheService)wac.getBean("financingCacheImpl"); 
		logsService = (LogsService)wac.getBean("logsServiceImpl"); 
		openCloseDealService = (OpenCloseDealService)wac.getBean("openCloseDealServiceImpl"); 
		
	}
 
	

}
