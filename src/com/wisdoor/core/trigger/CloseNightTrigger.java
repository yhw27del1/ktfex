package com.wisdoor.core.trigger;
 
 
import com.kmfex.service.OpenCloseDealService;
import com.wisdoor.core.service.UserService;

public class CloseNightTrigger extends TransactionBase {
	private static UserService userService;
	private static OpenCloseDealService openCloseDealService;
	
	@Override
	public void excute() throws Exception {
		if(openCloseDealService.isSuccessOpenNight()){//最后一条成功记录为“开夜市”才能休夜市
			openCloseDealService.open_close_night("休夜市", userService.findUser("admin"));
		}else{
			System.out.println("未开夜市，不能休夜市。");
		}
	}
	
	@Override
	public void init() throws Exception {
		openCloseDealService = (OpenCloseDealService)wac.getBean("openCloseDealServiceImpl");
		userService = (UserService)wac.getBean("userImpl");
	}
}
