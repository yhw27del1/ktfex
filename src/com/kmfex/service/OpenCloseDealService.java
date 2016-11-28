package com.kmfex.service;

import java.util.Date;

import com.kmfex.model.OpenCloseDeal;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

public interface OpenCloseDealService  extends BaseService<OpenCloseDeal>{
	//检测系统状态
	public byte checkState();
	
	//执行开市与休市操作
	public OpenCloseDeal open_close(String name, User u);
	
	//取最后的开市日期
	public Date getLatestKaiShi();
	
	//执行开夜市与休夜市操作
	public OpenCloseDeal open_close_night(String name, User u);
	
	//OpenCloseDeal表中最后一条成功记录是否为“对账”，最后一条成功记录为“对账”才能开夜市
	public boolean isSuccessDuiZhang();
	
	//OpenCloseDeal表中最后一条成功记录是否为“开夜市”，最后一条成功记录为“开夜市”才能休夜市
	public boolean isSuccessOpenNight();
}
