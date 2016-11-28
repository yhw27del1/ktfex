package com.wisdoor.core.trigger;
 
 
import com.kmfex.service.OpenCloseDealService;
import com.wisdoor.core.service.UserService;

public class OpenNightTrigger extends TransactionBase {
	private static UserService userService;
	private static OpenCloseDealService openCloseDealService;
	
	@Override
	public void excute() throws Exception {
		if(openCloseDealService.isSuccessDuiZhang()){//最后一条成功记录为“对账”才能开夜市
			openCloseDealService.open_close_night("开夜市", userService.findUser("admin"));
		}else{
			System.out.println("对帐未成功，不能开夜市。");
		}
	}
	
	@Override
	public void init() throws Exception {
		openCloseDealService = (OpenCloseDealService)wac.getBean("openCloseDealServiceImpl");
		userService = (UserService)wac.getBean("userImpl");
	}
}
