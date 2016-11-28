package com.kmfex.zhaiquan.service;


import com.kmfex.zhaiquan.model.ZQBuySellRule;
import com.wisdoor.core.service.BaseService;
import com.wisdoor.core.vo.CommonVo;

public interface ZQBuySellRuleService extends BaseService<ZQBuySellRule> {
////////////
  public  CommonVo  zhaiQuanCheck(String financingId,String investRecordid);
}
