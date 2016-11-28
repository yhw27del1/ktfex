package com.kmfex.service;

import com.kmfex.TradeTimeVO;
import com.kmfex.model.TradeTime;
import com.wisdoor.core.service.BaseService;

public interface TradeTimeService  extends BaseService<TradeTime>{
	public TradeTimeVO checkTradeTime();
}
