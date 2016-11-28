package com.kmfex.service.cmb;

import com.kmfex.action.cmb.CMBVO;
import com.kmfex.cmb.request.merchant.MerChantRequest6200;
import com.kmfex.cmb.request.merchant.MerChantRequest6201;
import com.kmfex.cmb.request.merchant.MerChantRequest6203;
import com.kmfex.cmb.request.merchant.MerChantRequest6204;
import com.kmfex.cmb.request.merchant.MerchantRequest6100;
import com.kmfex.cmb.request.merchant.MerchantRequest6400;
import com.kmfex.cmb.request.merchant.MerchantRequest6410;
import com.kmfex.cmb.request.merchant.MerchantRequest6500;
import com.kmfex.cmb.request.merchant.MerchantRequest6600;
import com.kmfex.model.cmb.CmbDeal;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface CmbDealService extends BaseService<CmbDeal>{
	/*交易所发起的请求*/
	//系统类交易：申请密钥请求(交易所发起)；向银行申请会话密钥（双方不校验MAC）
	public CMBVO request6600(MerchantRequest6600 p,long operator,String txDate,String txTime);
	
	//管理类交易：管理类交易：交易日初签到(交易所发起)；交易所向银行签到（日初）
	public CMBVO request6500(MerchantRequest6500 p,long operator,String txDate,String txTime);
	
	//查询类交易：查询银行账户及余额(交易所发起)；查询客户的银行账户及余额
	public CMBVO request6400(MerchantRequest6400 p,long operator,String txDate,String txTime);
	
	//查询类交易：查询交易所结算帐户余额(交易所发起)；查询交易所结算帐户的余额
	public CMBVO request6410(MerchantRequest6410 p,long operator,String txDate,String txTime);
	
	//转账类交易：银行转交易所(交易所发起)；交易所发起银行结算账户转资金帐户，入金
	public CMBVO request6200(MerChantRequest6200 p,long operator,String txDate,String txTime);
	
	//转账类交易：交易所转银行(交易所发起)；交易所发起资金帐户转银行结算账户，出金
	public CMBVO request6201(MerChantRequest6201 p,long operator,String txDate,String txTime);
	
	//转账类交易：重发交易所转银行(交易所发起)；交易所发起查询重做资金帐户转银行结算账户
	public CMBVO request6203(MerChantRequest6203 p,long operator,String txDate,String txTime);
	
	//转账类交易：冲正银行转交易所(交易所发起)；交易所发起冲正原银行结算账户转资金帐户交易
	public CMBVO request6204(MerChantRequest6204 p,long operator,String txDate,String txTime);
	
	//协议类交易：指定银商转账银行(交易所发起)，预指定、预开通银商转账协议；与5104为一套交易
	public CMBVO request6100(MerchantRequest6100 p,long operator,String txDate,String txTime);
	
	/*银行发起的请求*/
	public String request5600(String json);
	public String request5500(String json);
	public String request5400(String json);
	public String request5200(String json);
	public String request5201(String json);
	public String request5202(String json);
	public String request5205(String json);
	public String request5100(String json);
	public String request5101(String json);
	public String request5104(String json);
}
