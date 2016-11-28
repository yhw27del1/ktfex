package com.wisdoor.core.trigger;
 
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kmfex.hxbank.HxbankVO;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.DayCutService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;

public class DayCutTrigger extends TransactionBase {
	private static DayCutService dayCutService;
	private static UserService userService;
	private static CoreAccountService coreAccountService;
	
	@Override
	public void excute() throws Exception {
		userService.updateOldBalance();
		System.out.println("日切开始"); 
		coreAccountService.clear();
		
		HxbankVO vo = new HxbankVO();
		int[] yyyymmdd = this.getDate(new Date());//日切日切为系统当前日期
		long start = System.currentTimeMillis();
		List<User> ls = userService.getUserForUse();
		dayCutService.createDayCut(ls, yyyymmdd[0], yyyymmdd[1], yyyymmdd[2]);
		long m = (System.currentTimeMillis()-start)/1000;
		vo.setTip(new SimpleDateFormat("yyyy年MM月dd日").format(new Date())+"，日切成功，共耗时："+m+"秒，共："+ls.size()+"个用户。");
		System.out.println(vo.getTip());
	}
	
	private int[] getDate(Date date){
		int year = DateUtils.getYear(date);
		int month = DateUtils.getMouth(date);
		int day = DateUtils.getDay(date);
		return new int[]{year,month,day};
	}

	@Override
	public void init() throws Exception {
		dayCutService = (DayCutService)wac.getBean("dayCutServiceImpl");
		userService = (UserService)wac.getBean("userImpl");
		coreAccountService = (CoreAccountService)wac.getBean("coreAccountImpl");
	}
}
