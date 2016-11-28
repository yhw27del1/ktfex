package com.kmfex.service.hx;


import java.util.Date;
import java.util.List;

import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankParam_deal;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.hx.HxbankDeal;
import com.kmfex.model.hx.HxbankDealSub;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface HxbankDealService extends BaseService<HxbankDeal>{
	public HxbankVO sign_in(HxbankParam p,User operator);//签到
	public HxbankVO sign_off(HxbankParam p,User operator);//签退
	public HxbankVO subAccountSigned(HxbankParam p,User operator,User owner);//子账户签约
	public HxbankVO subAccountSync(HxbankParam p,User operator,User owner);//子账户同步
	
	public HxbankVO inGoldRequest(HxbankParam p,User operator,User owner);//入金申请(入金方式1,直接入金)
	
	public HxbankVO inGoldRegistrationRequest(HxbankParam p,User operator,User owner);//入金登记申请(入金方式2,交易商先转账,再入金登记申请,银行最后发起入金通知)
	public HxbankVO outGold(HxbankParam p,User operator,User owner);//出金(出金方式1,直接出金)
	
	public HxbankVO outGoldAuditResultSend(HxbankParam p,User checkUser,User owner);//出金审核结果发送(出金方式2,银行先发起出金申请,交易市场再审核)
	
	public HxbankVO tradingMarketBankBalance(HxbankParam p,User operator,User owner);//银行子帐号余额查询
	public HxbankVO surrender(HxbankParam p,User operator,User owner);//交易商解约
	
	//以下3个交易要判断时间，TIME_SLOT_CHECK = new String[]{"16:10","17:30"}
	public HxbankVO inOutGoldDetailedCheck(HxbankParam p,User operator);//出入金明细核对
	public HxbankVO liquidation(HxbankParam p,User operator);//清算
	public HxbankVO reconciliation(HxbankParam p,User operator);//对账
	
	//统计某交易日的清算报文
	public List<HxbankParam_deal> liquidation_report(String start,String end);
	
	public List<HxbankParam_deal> common_report();
	
	public List<HxbankDealSub> getSubs(HxbankDeal deal);
	
	//return 1；情形1：未签到，未签退：提示未签到，请签到。
	//return 2；情形2：已签到，未签退：提示已签到。
	//return 3；情形3：已签到，已签退：提示已签退，请清算、对账。
	//判断华夏银行的签到状态
	public byte signState();
}
