package com.kmfex.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.kmfex.TradeTimeVO;
import com.kmfex.model.TradeTime;
import com.kmfex.service.TradeTimeService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
@Service
public class TradeTimeServiceImpl  extends BaseServiceImpl<TradeTime> implements TradeTimeService{

	@Override
	public TradeTimeVO checkTradeTime() {
		Date dt = new Date();
		TradeTimeVO vo = new TradeTimeVO();
		TradeTime t = this.selectByHql(" from TradeTime where enabled = true");
		if(null!=t){//有启用的交易时间参数，则判断交易时间
			vo.setHasTradeTime(true);
			String[] am = new String[]{t.getAm_start(),t.getAm_end()};
			String[] pm = new String[]{t.getPm_start(),t.getPm_end()};
			//验证周末，并且验证时间范围。如果当前时间为周末，则false，如果当前时间为非周末，则进行时间范围验证。
			if(DateUtils.isShift(dt, am, true)||DateUtils.isShift(dt, pm, true)){//非周末且在设定时间范围内，允许交易。
				vo.setInTradeTime(true);
			}else{
				vo.setInTradeTime(false);
				vo.setTip("交易时间为周一至周五。上午交易时间段:"+t.getAm_start()+"至"+t.getAm_end()+",下午交易时间段:"+t.getPm_start()+"至"+t.getPm_end());
			}
		}else{//没有启用的交易时间参数，则不判断交易时间
			vo.setHasTradeTime(false);
			vo.setInTradeTime(true);
		}
		return vo;
	}

}
