package com.kmfex.service.cmb.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springannotationplugin.Properties;

import com.kmfex.action.cmb.CMBResult;
import com.kmfex.action.cmb.CMBVO;
import com.kmfex.cmb.request.bank.BankRequest5100;
import com.kmfex.cmb.request.bank.BankRequest5101;
import com.kmfex.cmb.request.bank.BankRequest5104;
import com.kmfex.cmb.request.bank.BankRequest5200;
import com.kmfex.cmb.request.bank.BankRequest5201;
import com.kmfex.cmb.request.bank.BankRequest5202;
import com.kmfex.cmb.request.bank.BankRequest5205;
import com.kmfex.cmb.request.bank.BankRequest5400;
import com.kmfex.cmb.request.bank.BankRequest5500;
import com.kmfex.cmb.request.bank.BankRequest5600;
import com.kmfex.cmb.request.merchant.MerChantRequest6200;
import com.kmfex.cmb.request.merchant.MerChantRequest6201;
import com.kmfex.cmb.request.merchant.MerChantRequest6203;
import com.kmfex.cmb.request.merchant.MerChantRequest6204;
import com.kmfex.cmb.request.merchant.MerchantRequest6100;
import com.kmfex.cmb.request.merchant.MerchantRequest6400;
import com.kmfex.cmb.request.merchant.MerchantRequest6410;
import com.kmfex.cmb.request.merchant.MerchantRequest6500;
import com.kmfex.cmb.request.merchant.MerchantRequest6600;
import com.kmfex.cmb.response.bank.BankResponse5100;
import com.kmfex.cmb.response.bank.BankResponse5101;
import com.kmfex.cmb.response.bank.BankResponse5104;
import com.kmfex.cmb.response.bank.BankResponse5200;
import com.kmfex.cmb.response.bank.BankResponse5201;
import com.kmfex.cmb.response.bank.BankResponse5202;
import com.kmfex.cmb.response.bank.BankResponse5205;
import com.kmfex.cmb.response.bank.BankResponse5400;
import com.kmfex.cmb.response.bank.BankResponse5500;
import com.kmfex.cmb.response.bank.BankResponse5600;
import com.kmfex.cmb.response.merchant.MerchantResponse6100;
import com.kmfex.cmb.response.merchant.MerchantResponse6200;
import com.kmfex.cmb.response.merchant.MerchantResponse6201;
import com.kmfex.cmb.response.merchant.MerchantResponse6203;
import com.kmfex.cmb.response.merchant.MerchantResponse6204;
import com.kmfex.cmb.response.merchant.MerchantResponse6400;
import com.kmfex.cmb.response.merchant.MerchantResponse6410;
import com.kmfex.cmb.response.merchant.MerchantResponse6500;
import com.kmfex.cmb.response.merchant.MerchantResponse6600;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.MemberBase;
import com.kmfex.model.SignHistory;
import com.kmfex.model.cmb.CmbDeal;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.SignHistoryService;
import com.kmfex.service.cmb.CmbDealService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;

/**
 * @author linuxp
 * */
@Service
public class CmbDealImpl extends BaseServiceImpl<CmbDeal> implements CmbDealService {
	
	@Resource
	UserService userService;
	
	@Resource
	AccountService accountService;
	
	@Resource
	AccountDealService accountDealService;
	
	@Resource
	CmbService cmbService;
	
	@Resource OpenCloseDealService openCloseDealService;
	
	@Resource SignHistoryService signHistoryService;
	
	@Resource MemberBaseService memberBaseService;

	@Properties(name="cmb_rzr")
	private String cmb_rzr;
	
	@Properties(name="sign")
	private String sign;
	
	@Properties(name="ingolden")
	private String ingolden;
	
	@Properties(name="outgolden")
	private String outgolden;
	
	@Override
	@Transactional
	public CMBVO request6100(MerchantRequest6100 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("预签","6100");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("预签","6100");
		cd.setCoSerial(p.getCoSerial());
		
		cd.setBusiType(p.getBusiType());
		cd.setAccType(p.getAccType());
		
		cd.setCurCode(p.getCurCode());
		cd.setBankAcc(p.getBankAcc());
		cd.setFundAcc(p.getFundAcc());
		
		cd.setIDType(p.getIDType());
		cd.setIDNo(p.getIDNo());
		cd.setCustName(p.getCustName());
		cd.setCountry(p.getCountry());
		
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		User u = userService.findUser(cd.getFundAcc());
		SignHistory history = new SignHistory();
		history.setName(cd.getName());
		if(null!=u){
			history.setOwner(u.getId());
			try {
				this.insert(cd);
				r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
				if(r.isSuccess()){//msg = 调用获取数据成功
					String result = r.getResult();//招行的报文数据
					MerchantResponse6100 r6100 = new MerchantResponse6100();
					r6100 = r6100.toObject(result);
					cd.setBkSerial(r6100.getBkSerial());
					cd.setBatchNo(r6100.getBatchNo());
					cd.setBankAcc(r6100.getBankAcc());
					cd.setFundAcc(r6100.getFundAcc());
					cd.setCurCode(r6100.getCurCode());
					cd.setCoBrn(r6100.getCoBrn());
					cd.setBkBrn(r6100.getBkBrn());
					cd.setRespCode(r6100.getRespCode());
					cd.setErrMsg(r6100.getErrMsg());
					cd.setUpdateDate(new Date());
					if("0000".equals(r6100.getRespCode())){//响应报文成功
						cd.setSuccess(true);
						vo.setSuccess(true);
						history.setSuccess(true);
						history.setSynDate_market(new Date());//预签时间
						history.setSignDate(history.getSynDate_market());
						history.setSignBank(2);
						history.setSignType(1);
						history.setMemo(vo.getMsg());
						this.signHistoryService.insert(history);
						u.setFlag("1");
						u.setChannel(0);//手工专户无
						u.setSynDate_market(history.getSynDate_market());
						this.userService.update(u);
					}else{//响应报文失败
						cd.setSuccess(false);
						cd.setMemo(r6100.getErrMsg());
						vo.setSuccess(false);
					}
					vo.setMsg(r6100.getErrMsg());
				}else{
					cd.setMemo(r.getMsg());
					vo.setSuccess(false);
					vo.setMsg(r.getMsg());
				}
				this.update(cd);
			} catch (EngineException e) {
				vo.setMsg("数据库操作异常，请联系管理员。");
			}
		}else{
			history.setOwner(-1);
			vo.setMsg("交易账号不存在");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6200(MerChantRequest6200 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("入金","6200");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("入金","6200");
		cd.setCoSerial(p.getCoSerial());
		
		cd.setCurCode(p.getCurCode());
		cd.setCurFlag(p.getCurFlag());
		cd.setBankAcc(p.getBankAcc());
		cd.setFundAcc(p.getFundAcc());
		cd.setAmount(p.getAmount());
		cd.setAmount_(DoubleUtils.toDouble(cd.getAmount()));
		
		cd.setIDType(p.getIDType());
		cd.setIDNo(p.getIDNo());
		cd.setCustName(p.getCustName());
		cd.setCountry(p.getCountry());
		
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6200 r6200 = new MerchantResponse6200();
				r6200 = r6200.toObject(result);
				cd.setBkSerial(r6200.getBkSerial());
				cd.setBatchNo(r6200.getBatchNo());
				cd.setBankAcc(r6200.getBankAcc());
				cd.setFundAcc(r6200.getFundAcc());
				cd.setCurCode(r6200.getCurCode());
				cd.setCurFlag(r6200.getCurFlag());
				cd.setAmount(r6200.getAmount());
				cd.setCoBrn(r6200.getCoBrn());
				cd.setRespCode(r6200.getRespCode());
				cd.setErrMsg(r6200.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6200.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6200.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6200.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6201(MerChantRequest6201 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("出金","6201");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("出金","6201");
		cd.setCoSerial(p.getCoSerial());
		
		cd.setCurCode(p.getCurCode());
		cd.setCurFlag(p.getCurFlag());
		cd.setBankAcc(p.getBankAcc());
		cd.setFundAcc(p.getFundAcc());
		cd.setAmount(p.getAmount());
		cd.setAmount_(DoubleUtils.toDouble(cd.getAmount()));
		
		cd.setIDType(p.getIDType());
		cd.setIDNo(p.getIDNo());
		cd.setCustName(p.getCustName());
		cd.setCountry(p.getCountry());
		
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6201 r6201 = new MerchantResponse6201();
				r6201 = r6201.toObject(result);
				cd.setBkSerial(r6201.getBkSerial());
				cd.setBatchNo(r6201.getBatchNo());
				cd.setBankAcc(r6201.getBankAcc());
				cd.setFundAcc(r6201.getFundAcc());
				cd.setCurCode(r6201.getCurCode());
				cd.setCurFlag(r6201.getCurFlag());
				cd.setAmount(r6201.getAmount());
				cd.setCoBrn(r6201.getCoBrn());
				cd.setRespCode(r6201.getRespCode());
				cd.setErrMsg(r6201.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6201.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6201.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6201.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6203(MerChantRequest6203 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("重发出金","6203");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("重发出金","6203");
		cd.setCoSerial(p.getCoSerial());
		
		cd.setCurCode(p.getCurCode());
		cd.setCurFlag(p.getCurFlag());
		cd.setBankAcc(p.getBankAcc());
		cd.setFundAcc(p.getFundAcc());
		cd.setAmount(p.getAmount());
		cd.setAmount_(DoubleUtils.toDouble(cd.getAmount()));
		
		cd.setIDType(p.getIDType());
		cd.setIDNo(p.getIDNo());
		cd.setCustName(p.getCustName());
		cd.setCountry(p.getCountry());
		
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6203 r6203 = new MerchantResponse6203();
				r6203 = r6203.toObject(result);
				cd.setBkSerial(r6203.getBkSerial());
				cd.setBatchNo(r6203.getBatchNo());
				cd.setBankAcc(r6203.getBankAcc());
				cd.setFundAcc(r6203.getFundAcc());
				cd.setCurCode(r6203.getCurCode());
				cd.setCurFlag(r6203.getCurFlag());
				cd.setAmount(r6203.getAmount());
				cd.setCoBrn(r6203.getCoBrn());
				cd.setRespCode(r6203.getRespCode());
				cd.setErrMsg(r6203.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6203.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6203.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6203.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6204(MerChantRequest6204 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("冲正入金","6204");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("冲正入金","6204");
		cd.setCoSerial(p.getCoSerial());
		
		cd.setCurCode(p.getCurCode());
		cd.setCurFlag(p.getCurFlag());
		cd.setBankAcc(p.getBankAcc());
		cd.setFundAcc(p.getFundAcc());
		cd.setAmount(p.getAmount());
		cd.setAmount_(DoubleUtils.toDouble(cd.getAmount()));
		
		cd.setIDType(p.getIDType());
		cd.setIDNo(p.getIDNo());
		cd.setCustName(p.getCustName());
		cd.setCountry(p.getCountry());
		
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6204 r6204 = new MerchantResponse6204();
				r6204 = r6204.toObject(result);
				cd.setBkSerial(r6204.getBkSerial());
				cd.setBatchNo(r6204.getBatchNo());
				cd.setBankAcc(r6204.getBankAcc());
				cd.setFundAcc(r6204.getFundAcc());
				cd.setCurCode(r6204.getCurCode());
				cd.setCurFlag(r6204.getCurFlag());
				cd.setAmount(r6204.getAmount());
				cd.setCoBrn(r6204.getCoBrn());
				cd.setRespCode(r6204.getRespCode());
				cd.setErrMsg(r6204.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6204.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6204.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6204.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6400(MerchantRequest6400 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("会员余额查询","6400");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("会员余额查询","6400");
		cd.setCoSerial(p.getCoSerial());
		cd.setCurCode(p.getCurCode());
		cd.setFundAcc(p.getFundAcc());
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6400 r6400 = new MerchantResponse6400();
				r6400 = r6400.toObject(result);
				cd.setBkSerial(r6400.getBkSerial());
				cd.setFundBal(r6400.getFundBal());
				cd.setFundBal_(DoubleUtils.toDouble(cd.getFundBal()));
				cd.setFundUse(r6400.getFundUse());
				cd.setFundUse_(DoubleUtils.toDouble(cd.getFundUse()));
				cd.setRespCode(r6400.getRespCode());
				cd.setErrMsg(r6400.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6400.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6400.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6400.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6410(MerchantRequest6410 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("交易所余额查询","6410");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("交易所余额查询","6410");
		cd.setCoSerial(p.getCoSerial());
		cd.setCurCode(p.getCurCode());
		cd.setBankAcc(p.getBankAcc());
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6410 r6410 = new MerchantResponse6410();
				r6410 = r6410.toObject(result);
				cd.setBkSerial(r6410.getBkSerial());
				cd.setFundBal(r6410.getFundBal());
				cd.setFundBal_(DoubleUtils.toDouble(cd.getFundBal()));
				cd.setFundUse(r6410.getFundUse());
				cd.setFundUse_(DoubleUtils.toDouble(cd.getFundUse()));
				cd.setRespCode(r6410.getRespCode());
				cd.setErrMsg(r6410.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6410.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6410.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6410.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6500(MerchantRequest6500 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("日初签到","6500");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("日初签到","6500");
		cd.setCoSerial(p.getCoSerial());
		cd.setCoTime(p.getCoTime());
		cd.setTransDate(p.getTransDate());
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6500 r6500 = new MerchantResponse6500();
				r6500 = r6500.toObject(result);
				cd.setBkSerial(r6500.getBkSerial());
				cd.setBatchNo(r6500.getBatchNo());
				cd.setTransDate(r6500.getTransDate());
				cd.setRespCode(r6500.getRespCode());
				cd.setErrMsg(r6500.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6500.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6500.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6500.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}

	@Override
	@Transactional
	public CMBVO request6600(MerchantRequest6600 p,long operator,String txDate,String txTime) {
		CMBVO vo = new CMBVO("申请密钥","6600");
		CMBResult r = null;
		CmbDeal cd = new CmbDeal("申请密钥","6600");
		cd.setCoSerial(p.getCoSerial());
		cd.setCreateDate(new Date());
		cd.setOperator(operator);
		try {
			this.insert(cd);
			r = this.cmbService.loadCmbServlet(p.toXML(), cd.getTxCode(),txDate,txTime);
			if(r.isSuccess()){//msg = 调用获取数据成功
				String result = r.getResult();//招行的报文数据
				MerchantResponse6600 r6600 = new MerchantResponse6600();
				r6600 = r6600.toObject(result);
				cd.setBkSerial(r6600.getBkSerial());
				cd.setKeyInfo(r6600.getKeyInfo());
				cd.setKMCode(r6600.getKMCode());
				cd.setKPCode(r6600.getKPCode());
				cd.setRespCode(r6600.getRespCode());
				cd.setErrMsg(r6600.getErrMsg());
				cd.setUpdateDate(new Date());
				if("0000".equals(r6600.getRespCode())){//响应报文成功
					cd.setSuccess(true);
					vo.setSuccess(true);
				}else{//响应报文失败
					cd.setSuccess(false);
					cd.setMemo(r6600.getErrMsg());
					vo.setSuccess(false);
				}
				vo.setMsg(r6600.getErrMsg());
			}else{
				cd.setMemo(r.getMsg());
				vo.setSuccess(false);
				vo.setMsg(r.getMsg());
			}
			this.update(cd);
		} catch (EngineException e) {
			vo.setMsg("数据库操作异常，请联系管理员。");
		}
		return vo;
	}
	
	/*银行发起的请求*/
	@Override
	@Transactional
	public String request5100(String json) {
		String msg = "";
		BankRequest5100 r5100 = new BankRequest5100();
		r5100 = r5100.toObject(json);
		CmbDeal cmb = new CmbDeal("一步式签约","5100");
		cmb.setBkSerial(r5100.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5100.getBatchNo());
		cmb.setBusiType(r5100.getBusiType());
		cmb.setBankAcc(r5100.getBankAcc());
		cmb.setFundAcc(r5100.getFundAcc());
		cmb.setCurCode(r5100.getCurCode());
cmb.setPinBlock(r5100.getPinBlock());
		cmb.setAccType(r5100.getAccType());
		cmb.setIDType(r5100.getIDType());
		cmb.setIDNo(r5100.getIDNo());
		cmb.setCustName(r5100.getCustName());
		cmb.setCountry(r5100.getCountry());
		cmb.setSex(r5100.getSex());
		cmb.setSalerId(r5100.getSalerId());
		cmb.setBkBrn(r5100.getBkBrn());
		cmb.setCoBrn(r5100.getCoBrn());
		cmb.setPhone(r5100.getPhone());
		cmb.setMobile(r5100.getMobile());
		cmb.setFax(r5100.getFax());
		cmb.setEmail(r5100.getEmail());
		cmb.setPostCode(r5100.getPostCode());
		cmb.setAddress(r5100.getAddress());
		
		User u = userService.findUser(cmb.getFundAcc());
		SignHistory history = new SignHistory();
		history.setName(cmb.getName());
		BankResponse5100 re5100 = new BankResponse5100();
		re5100.setBkSerial(cmb.getBkSerial());
		re5100.setBkSerial(cmb.getBankAcc());
		re5100.setFundAcc(cmb.getFundAcc());
		re5100.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能开通协议
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5100.setRespCode("2115");
			re5100.setErrMsg(cmb.getMemo());
		}else{
			if(null!=u){
				history.setOwner(u.getId());
				if("2".equals(u.getFlag())){//已开通三方存管
					cmb.setSuccess(false);
					cmb.setMemo("银商转账服务协议已存在");
					re5100.setRespCode("2037");
					re5100.setErrMsg(cmb.getMemo());
				}else{
					MemberBase mb = this.memberBaseService.getMemByUser(u);
					String pass = u.getPassword();//密码
					String bankaccount = mb.getBankAccount();//银行账号
					String realname = u.getRealname();//客户名称
					String idcard = "";//证件号
					boolean qiye = "0".equals(mb.getCategory());
					if(qiye){
						idcard = mb.geteOrgCode();//组织机构编码
					}else{
						idcard = mb.getIdCardNo();
					}
					if(pass.equals(cmb.getPinBlock())){//密码相符
						if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
							if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
								if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
									//招商接口融资方开通协议接口关闭，则融资方不能开通三方存管
									if("off".equals(this.cmb_rzr)&&"R".equals(u.getUserType())){
										cmb.setSuccess(false);
										cmb.setMemo("交易所交易账户开户失败-被禁止开户");
										re5100.setRespCode("2021");
										re5100.setErrMsg(cmb.getMemo());
									}else{
										if(accountDealService.hasNoCheck(u.getUserAccount())){
											cmb.setSuccess(false);
											cmb.setMemo("交易所交易账户开户失败-有待审核业务数据");
											re5100.setRespCode("2021");
											re5100.setErrMsg(cmb.getMemo());
										}else{
											cmb.setSuccess(true);
											cmb.setOperator(u.getId());
											re5100.setRespCode("0000");
											history.setSuccess(true);
										}
									}
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("证件不符");
									re5100.setRespCode("2093");
									re5100.setErrMsg(cmb.getMemo());
								}
							}else{
								cmb.setSuccess(false);
								cmb.setMemo("户名不符");
								re5100.setRespCode("2095");
								re5100.setErrMsg(cmb.getMemo());
							}
						}else{
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账号与银行账号不一致");
							re5100.setRespCode("2039");
							re5100.setErrMsg(cmb.getMemo());
						}
					}else{//密码不相符
						cmb.setSuccess(false);
						cmb.setMemo("交易所交易账户密码错误");
						re5100.setRespCode("2085");
						re5100.setErrMsg(cmb.getMemo());
					}
				}
			}else{
				history.setOwner(-1);
				cmb.setSuccess(false);
				cmb.setMemo("交易所交易账号不存在");
				re5100.setRespCode("2009");
				re5100.setErrMsg(cmb.getMemo());
			}
		}
		history.setMemo(cmb.getMemo());
		try {
			this.insert(cmb);
			if(cmb.isSuccess()){
				u.setFlag("2");
				u.setSignDate(new Date());//签约日期
				u.setSurrenderDate(null);
				u.setSignBank(2);//签约行:招行
				u.setSignType(1);//签约类型:本行
				u.setChannel(0);//手工专户无
				userService.update(u);
				//签约成功，更新old_balance为0
				Account a = accountService.selectById(u.getUserAccount().getId());
				a.setOld_balance(0d);
				accountService.update(a);
				history.setSignDate(u.getSignDate());
				history.setSignBank(u.getSignBank());
				history.setSignType(u.getSignType());
				history.setSigned(true);
				history.setBalance(this.accountService.selectById(u.getUserAccount().getId()).getBalance());
				history.setFrozen(this.accountService.selectById(u.getUserAccount().getId()).getFrozenAmount());
				signHistoryService.insert(history);
			}
			msg = re5100.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5101(String json) {
		String msg = "";
		BankRequest5101 r5101 = new BankRequest5101();
		r5101 = r5101.toObject(json);
		CmbDeal cmb = new CmbDeal("关闭银商转账服务","5101");
		cmb.setBkSerial(r5101.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5101.getBatchNo());
		cmb.setBusiType(r5101.getBusiType());
		cmb.setBankAcc(r5101.getBankAcc());
		cmb.setFundAcc(r5101.getFundAcc());
		cmb.setCurCode(r5101.getCurCode());
cmb.setPinBlock(r5101.getPinBlock());
		cmb.setAccType(r5101.getAccType());
		cmb.setIDType(r5101.getIDType());
		cmb.setIDNo(r5101.getIDNo());
		cmb.setCustName(r5101.getCustName());
		cmb.setCountry(r5101.getCountry());
		cmb.setSex(r5101.getSex());
		cmb.setSalerId(r5101.getSalerId());
		cmb.setBkBrn(r5101.getBkBrn());
		cmb.setCoBrn(r5101.getCoBrn());
		cmb.setPhone(r5101.getPhone());
		cmb.setMobile(r5101.getMobile());
		cmb.setFax(r5101.getFax());
		cmb.setEmail(r5101.getEmail());
		cmb.setPostCode(r5101.getPostCode());
		cmb.setAddress(r5101.getAddress());
		
		User u = userService.findUser(cmb.getFundAcc());
		SignHistory history = null;
		BankResponse5101 re5101 = new BankResponse5101();
		re5101.setBkSerial(cmb.getBkSerial());
		re5101.setFundAcc(cmb.getFundAcc());
		re5101.setCurCode(cmb.getCurCode());
		if(null!=u){
			if(!"2".equals(u.getFlag())){//未开通三方存管
				cmb.setSuccess(false);
				cmb.setMemo("银商转账服务协议已关闭");
				re5101.setRespCode("2038");
				re5101.setErrMsg(cmb.getMemo());
			}else{
				MemberBase mb = this.memberBaseService.getMemByUser(u);
				String pass = u.getPassword();//密码
				String bankaccount = mb.getBankAccount();//银行账号
				String realname = u.getRealname();//客户名称
				String idcard = "";//证件号
				boolean qiye = "0".equals(mb.getCategory());
				if(qiye){
					idcard = mb.geteOrgCode();//组织机构编码
				}else{
					idcard = mb.getIdCardNo();
				}
				history = signHistoryService.getHistory(u.getId(), u.getSignBank(), u.getSignType());
				if(pass.equals(cmb.getPinBlock())){//密码相符
					if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
						if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
							if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
								if(u.getUserAccount().getBalance()+u.getUserAccount().getFrozenAmount()==0){//可用与冻结金额都为0才能关闭协议
									if(!accountDealService.hasAccountDeal(u.getUserAccount())){
										cmb.setSuccess(true);
										cmb.setOperator(u.getId());
										re5101.setRespCode("0000");
										if(null!=history){
											history.setSurrenderDate(new Date());
										}
									}else{
										cmb.setSuccess(false);
										cmb.setMemo("当日有转账不能取消银商转账协议");
										re5101.setRespCode("2073");
										re5101.setErrMsg(cmb.getMemo());
									}
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("客户持有份额不能关闭转账协议");
									re5101.setRespCode("2098");
									re5101.setErrMsg(cmb.getMemo());
								}
							}else{
								cmb.setSuccess(false);
								cmb.setMemo("证件不符");
								re5101.setRespCode("2093");
								re5101.setErrMsg(cmb.getMemo());
							}
						}else{
							cmb.setSuccess(false);
							cmb.setMemo("户名不符");
							re5101.setRespCode("2095");
							re5101.setErrMsg(cmb.getMemo());
						}
					}else{//银行卡号与交易账号不相符
						cmb.setSuccess(false);
						cmb.setMemo("交易所交易账号与银行账号不一致");
						re5101.setRespCode("2039");
						re5101.setErrMsg(cmb.getMemo());
					}
				}else{//密码不相符
					cmb.setSuccess(false);
					cmb.setMemo("交易所交易账户密码错误");
					re5101.setRespCode("2085");
					re5101.setErrMsg(cmb.getMemo());
				}
			}
		}else{
			cmb.setSuccess(false);
			cmb.setMemo("交易所交易账号不存在");
			re5101.setRespCode("2009");
			re5101.setErrMsg(cmb.getMemo());
		}
		try {
			this.insert(cmb);
			if(cmb.isSuccess()){
				u.setFlag("3");
				u.setSignBank(0);
				u.setSignType(0);
				u.setChannel(1);//手工专户：招行
				u.setSurrenderDate(new Date());//解约日期
				userService.update(u);
				if(null!=history){
					signHistoryService.update(history);
				}
			}
			msg = re5101.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5104(String json) {
		String msg = "";
		BankRequest5104 r5104 = new BankRequest5104();
		r5104 = r5104.toObject(json);
		CmbDeal cmb = new CmbDeal("激活银商转账服务","5104");
		cmb.setBkSerial(r5104.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5104.getBatchNo());
		cmb.setBusiType(r5104.getBusiType());
		cmb.setBankAcc(r5104.getBankAcc());
		cmb.setFundAcc(r5104.getFundAcc());
		cmb.setCurCode(r5104.getCurCode());
cmb.setPinBlock(r5104.getPinBlock());
		cmb.setAccType(r5104.getAccType());
		cmb.setIDType(r5104.getIDType());
		cmb.setIDNo(r5104.getIDNo());
		cmb.setCustName(r5104.getCustName());
		cmb.setCountry(r5104.getCountry());
		cmb.setSex(r5104.getSex());
		cmb.setSalerId(r5104.getSalerId());
		cmb.setBkBrn(r5104.getBkBrn());
		cmb.setCoBrn(r5104.getCoBrn());
		cmb.setPhone(r5104.getPhone());
		cmb.setMobile(r5104.getMobile());
		cmb.setFax(r5104.getFax());
		cmb.setEmail(r5104.getEmail());
		cmb.setPostCode(r5104.getPostCode());
		cmb.setAddress(r5104.getAddress());
		
		User u = userService.findUser(cmb.getFundAcc());
		SignHistory history = null;
		BankResponse5104 re5104 = new BankResponse5104();
		re5104.setBkSerial(cmb.getBkSerial());
		re5104.setBkSerial(cmb.getBankAcc());
		re5104.setFundAcc(cmb.getFundAcc());
		re5104.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能开通协议
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5104.setRespCode("2115");
			re5104.setErrMsg(cmb.getMemo());
		}else{
			if(null!=u){
				if("2".equals(u.getFlag())){//已开通三方存管
					cmb.setSuccess(false);
					cmb.setMemo("银商转账服务协议已存在");
					re5104.setRespCode("2037");
					re5104.setErrMsg(cmb.getMemo());
				}else{
					MemberBase mb = this.memberBaseService.getMemByUser(u);
					String pass = u.getPassword();//密码
					String bankaccount = mb.getBankAccount();//银行账号
					history = signHistoryService.getHistory(u.getId(), u.getSignBank(), u.getSignType());
					if(pass.equals(cmb.getPinBlock())){//密码相符
						if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
							//招商接口融资方开通协议接口关闭，则融资方不能开通三方存管
							if("off".equals(this.cmb_rzr)&&"R".equals(u.getUserType())){
								cmb.setSuccess(false);
								cmb.setMemo("交易所交易账户开户失败-被禁止开户");
								re5104.setRespCode("2021");
								re5104.setErrMsg(cmb.getMemo());
							}else{
								if(accountDealService.hasNoCheck(u.getUserAccount())){
									cmb.setSuccess(false);
									cmb.setMemo("交易所交易账户开户失败-有待审核业务数据");
									re5104.setRespCode("2021");
									re5104.setErrMsg(cmb.getMemo());
								}else{
									cmb.setSuccess(true);
									cmb.setOperator(u.getId());
									re5104.setRespCode("0000");
									if(null!=history){
										history.setSignDate(new Date());//签约时间
									}
								}
							}
						}else{//银行卡号与交易账号不相符
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账号与银行账号不一致");
							re5104.setRespCode("2039");
							re5104.setErrMsg(cmb.getMemo());
						}
					}else{//密码不相符
						cmb.setSuccess(false);
						cmb.setMemo("交易所交易账户密码错误");
						re5104.setRespCode("2085");
						re5104.setErrMsg(cmb.getMemo());
					}
				}
			}else{
				cmb.setSuccess(false);
				cmb.setMemo("交易所交易账号不存在");
				re5104.setRespCode("2009");
				re5104.setErrMsg(cmb.getMemo());
			}
		}
		try {
			this.insert(cmb);
			if(cmb.isSuccess()){
				u.setFlag("2");
				u.setSignDate(new Date());//签约日期
				u.setSurrenderDate(null);
				u.setSignBank(2);//签约行:招行
				u.setSignType(1);//签约类型:本行
				u.setChannel(0);//手工专户无
				userService.update(u);
				//签约成功，更新old_balance为0
				Account a = accountService.selectById(u.getUserAccount().getId());
				a.setOld_balance(0d);
				accountService.update(a);
				if(null!=history){
					history.setSigned(true);
					history.setBalance(this.accountService.selectById(u.getUserAccount().getId()).getBalance());
					history.setFrozen(this.accountService.selectById(u.getUserAccount().getId()).getFrozenAmount());
					signHistoryService.update(history);
				}
			}
			msg = re5104.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5200(String json) {
		String msg = "";
		BankRequest5200 r5200 = new BankRequest5200();
		r5200 = r5200.toObject(json);
		//签约检查；通过才能入金
		CmbDeal cmb = new CmbDeal("银行转交易所","5200");
		cmb.setBkSerial(r5200.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5200.getBatchNo());
		cmb.setBankAcc(r5200.getBankAcc());
		cmb.setFundAcc(r5200.getFundAcc());
		cmb.setCurCode(r5200.getCurCode());
		cmb.setIDType(r5200.getIDType());
		cmb.setIDNo(r5200.getIDNo());
		cmb.setCustName(r5200.getCustName());
		cmb.setCountry(r5200.getCountry());
		cmb.setAmount(r5200.getAmount());
		cmb.setAmount_(DoubleUtils.toDouble(cmb.getAmount()));
		User u = userService.findUser(cmb.getFundAcc());
		double amount = cmb.getAmount_();//入金金额
		BankResponse5200 re5200 = new BankResponse5200();
		re5200.setBkSerial(cmb.getBkSerial());
		re5200.setFundAcc(cmb.getFundAcc());
		re5200.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能入金
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5200.setRespCode("2115");
			re5200.setErrMsg(cmb.getMemo());
		}else{
			//招商接口入金开关为off，则不能在银行方做入金
			if("off".equals(this.ingolden)){
				cmb.setSuccess(false);
				cmb.setMemo("非银行发起方交易");
				re5200.setRespCode("2091");
				re5200.setErrMsg(cmb.getMemo());
			}else{
				if(null!=u){
					if(!"2".equals(u.getFlag())){//未开通三方存管
						cmb.setSuccess(false);
						cmb.setMemo("银商转账服务协议已关闭");
						re5200.setRespCode("2038");
						re5200.setErrMsg(cmb.getMemo());
					}else{
						MemberBase mb = this.memberBaseService.getMemByUser(u);
						String bankaccount = mb.getBankAccount();//银行账号
						String realname = u.getRealname();//客户名称
						String idcard = "";//证件号
						boolean qiye = "0".equals(mb.getCategory());
						if(qiye){
							idcard = mb.geteOrgCode();//组织机构编码
						}else{
							idcard = mb.getIdCardNo();
						}
						if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
							if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
								if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
									cmb.setSuccess(true);
									cmb.setOperator(u.getId());
									re5200.setRespCode("0000");
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("证件不符");
									re5200.setRespCode("2093");
									re5200.setErrMsg(cmb.getMemo());
								}
							}else{
								cmb.setSuccess(false);
								cmb.setMemo("户名不符");
								re5200.setRespCode("2095");
								re5200.setErrMsg(cmb.getMemo());
							}
						}else{//银行卡号与交易账号不相符
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账号与银行账号不一致");
							re5200.setRespCode("2039");
							re5200.setErrMsg(cmb.getMemo());
						}
					}
				}else{
					cmb.setSuccess(false);
					cmb.setMemo("交易所交易账号不存在");
					re5200.setRespCode("2009");
					re5200.setErrMsg(cmb.getMemo());
				}
			}
		}
		try {
			if(cmb.isSuccess()){
				boolean s = accountDealService.in_out_bank(u.getUserAccount(), amount, AccountDeal.BANK2ZQ, "24",cmb.getBkSerial(),cmb.getCoSerial());
				if(!s){//入金错误
					cmb.setSuccess(false);
					msg = "error";
				}else{
					msg = re5200.toXML();
				}
			}else{
				msg = re5200.toXML();
			}
			this.insert(cmb);
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5201(String json) {
		String msg = "";
		//签约检查；可用余额检查；都通过才能出金
		BankRequest5201 r5201 = new BankRequest5201();
		r5201 = r5201.toObject(json);
		CmbDeal cmb = new CmbDeal("交易所转银行","5201");
		cmb.setBkSerial(r5201.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5201.getBatchNo());
		cmb.setBankAcc(r5201.getBankAcc());
		cmb.setFundAcc(r5201.getFundAcc());
		cmb.setCurCode(r5201.getCurCode());
cmb.setPinBlock(r5201.getPinBlock());
		cmb.setIDType(r5201.getIDType());
		cmb.setIDNo(r5201.getIDNo());
		cmb.setCustName(r5201.getCustName());
		cmb.setCountry(r5201.getCountry());
		cmb.setAmount(r5201.getAmount());
		cmb.setAmount_(DoubleUtils.toDouble(cmb.getAmount()));
		User u = userService.findUser(cmb.getFundAcc());
		double amount = cmb.getAmount_();//出金金额
		BankResponse5201 re5201 = new BankResponse5201();
		re5201.setBkSerial(cmb.getBkSerial());
		re5201.setFundAcc(cmb.getFundAcc());
		re5201.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能出金
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5201.setRespCode("2115");
			re5201.setErrMsg(cmb.getMemo());
		}else{
			//招商接口出金开关为off，则不能在银行方做出金
			if("off".equals(this.outgolden)){
				cmb.setSuccess(false);
				cmb.setMemo("非银行发起方交易");
				re5201.setRespCode("2091");
				re5201.setErrMsg(cmb.getMemo());
			}else{
				if(null!=u){
					if(!"2".equals(u.getFlag())){//未开通三方存管
						cmb.setSuccess(false);
						cmb.setMemo("银商转账服务协议已关闭");
						re5201.setRespCode("2038");
						re5201.setErrMsg(cmb.getMemo());
					}else{
						MemberBase mb = this.memberBaseService.getMemByUser(u);
						String pass = u.getPassword();//密码
						String bankaccount = mb.getBankAccount();//银行账号
						String realname = u.getRealname();//客户名称
						String idcard = "";//证件号
						boolean qiye = "0".equals(mb.getCategory());
						if(qiye){
							idcard = mb.geteOrgCode();//组织机构编码
						}else{
							idcard = mb.getIdCardNo();
						}
						if(pass.equals(cmb.getPinBlock())){//密码相符
							if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
								if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
									if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
										double old = DoubleUtils.doubleCheck(u.getUserAccount().getOld_balance(),2);
										if(old>0&&(amount<=old)){
											cmb.setSuccess(true);
											cmb.setOperator(u.getId());
											re5201.setRespCode("0000");
										}else{//金额不够
											cmb.setSuccess(false);
											cmb.setMemo("交易所交易账号可取资金不足");
											re5201.setRespCode("2003");
											re5201.setErrMsg(cmb.getMemo());
										}
									}else{
										cmb.setSuccess(false);
										cmb.setMemo("证件不符");
										re5201.setRespCode("2093");
										re5201.setErrMsg(cmb.getMemo());
									}
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("户名不符");
									re5201.setRespCode("2095");
									re5201.setErrMsg(cmb.getMemo());
								}
							}else{//银行卡号与交易账号不相符
								cmb.setSuccess(false);
								cmb.setMemo("交易所交易账号与银行账号不一致");
								re5201.setRespCode("2039");
								re5201.setErrMsg(cmb.getMemo());
							}
						}else{//密码不相符
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账户密码错误");
							re5201.setRespCode("2085");
							re5201.setErrMsg(cmb.getMemo());
						}
					}
				}else{
					cmb.setSuccess(false);
					cmb.setMemo("交易所交易账号不存在");
					re5201.setRespCode("2009");
					re5201.setErrMsg(cmb.getMemo());
				}
			}
		}
		try {
			if(cmb.isSuccess()){
				boolean s = accountDealService.in_out_bank(u.getUserAccount(), amount, AccountDeal.ZQ2BANK, "26",cmb.getBkSerial(),cmb.getCoSerial());
				if(!s){//出金错误
					cmb.setSuccess(false);
					msg = "error";
				}else{
					msg = re5201.toXML();
				}
			}else{
				msg = re5201.toXML();
			}
			this.insert(cmb);
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5202(String json) {
		String msg = "";
		//验证交易账号与银行账号
		BankRequest5202 r5202 = new BankRequest5202();
		r5202 = r5202.toObject(json);
		CmbDeal cmb = new CmbDeal("重发银行转交易所","5202");
		cmb.setBkSerial(r5202.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5202.getBatchNo());
		cmb.setBankAcc(r5202.getBankAcc());
		cmb.setFundAcc(r5202.getFundAcc());
		cmb.setCurCode(r5202.getCurCode());
		cmb.setIDType(r5202.getIDType());
		cmb.setIDNo(r5202.getIDNo());
		cmb.setCustName(r5202.getCustName());
		cmb.setCountry(r5202.getCountry());
		cmb.setAmount(r5202.getAmount());
		cmb.setAmount_(DoubleUtils.toDouble(cmb.getAmount()));
		User u = userService.findUser(cmb.getFundAcc());
		double amount = cmb.getAmount_();//重发入金金额
		BankResponse5202 re5202 = new BankResponse5202();
		re5202.setBkSerial(cmb.getBkSerial());
		re5202.setFundAcc(cmb.getFundAcc());
		re5202.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能重发入金
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5202.setRespCode("2115");
			re5202.setErrMsg(cmb.getMemo());
		}else{
			//招商接口入金开关为off，则不能在银行方做重发入金
			if("off".equals(this.ingolden)){
				cmb.setSuccess(false);
				cmb.setMemo("非银行发起方交易");
				re5202.setRespCode("2091");
				re5202.setErrMsg(cmb.getMemo());
			}else{
				if(null!=u){
					if(!"2".equals(u.getFlag())){//未开通三方存管
						cmb.setSuccess(false);
						cmb.setMemo("银商转账服务协议已关闭");
						re5202.setRespCode("2038");
						re5202.setErrMsg(cmb.getMemo());
					}else{
						MemberBase mb = this.memberBaseService.getMemByUser(u);
						String bankaccount = mb.getBankAccount();//银行账号
						String realname = u.getRealname();//客户名称
						String idcard = "";//证件号
						boolean qiye = "0".equals(mb.getCategory());
						if(qiye){
							idcard = mb.geteOrgCode();//组织机构编码
						}else{
							idcard = mb.getIdCardNo();
						}
						if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
							if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
								if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
									AccountDeal ad = accountDealService.getBkSerial(u.getUserAccount(), cmb.getBkSerial());
									if(null!=ad){//有记录
										cmb.setSuccess(false);
										re5202.setRespCode("0000");
									}else{//无记录，执行重发入金
										cmb.setSuccess(true);
										re5202.setRespCode("0000");
									}
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("证件不符");
									re5202.setRespCode("2093");
									re5202.setErrMsg(cmb.getMemo());
								}
							}else{
								cmb.setSuccess(false);
								cmb.setMemo("户名不符");
								re5202.setRespCode("2095");
								re5202.setErrMsg(cmb.getMemo());
							}
						}else{//银行卡号与交易账号不相符
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账号与银行账号不一致");
							re5202.setRespCode("2039");
							re5202.setErrMsg(cmb.getMemo());
						}
					}
				}else{
					cmb.setSuccess(false);
					cmb.setMemo("交易所交易账号不存在");
					re5202.setRespCode("2009");
					re5202.setErrMsg(cmb.getMemo());
				}
			}
		}
		try {
			if(cmb.isSuccess()){//执行重发入金操作，银转商
				boolean s = accountDealService.in_out_bank(u.getUserAccount(), amount, AccountDeal.BANK2ZQ, "24",cmb.getBkSerial(),cmb.getCoSerial());
				if(!s){//重发入金错误
					cmb.setSuccess(false);
					msg = "error";
				}else{
					msg = re5202.toXML();
				}
			}else{
				msg = re5202.toXML();
			}
			this.insert(cmb);
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5205(String json) {
		String msg = "";
		//验证交易账号与银行账号
		BankRequest5205 r5205 = new BankRequest5205();
		r5205 = r5205.toObject(json);
		CmbDeal cmb = new CmbDeal("冲正交易所转银行","5205");
		cmb.setBkSerial(r5205.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5205.getBatchNo());
		cmb.setBankAcc(r5205.getBankAcc());
		cmb.setFundAcc(r5205.getFundAcc());
		cmb.setCurCode(r5205.getCurCode());
cmb.setPinBlock(r5205.getPinBlock());
		cmb.setIDType(r5205.getIDType());
		cmb.setIDNo(r5205.getIDNo());
		cmb.setCustName(r5205.getCustName());
		cmb.setCountry(r5205.getCountry());
		cmb.setAmount(r5205.getAmount());
		cmb.setAmount_(DoubleUtils.toDouble(cmb.getAmount()));
		User u = userService.findUser(cmb.getFundAcc());
		double amount = cmb.getAmount_();//冲正出金金额
		BankResponse5205 re5205 = new BankResponse5205();
		re5205.setBkSerial(cmb.getBkSerial());
		re5205.setFundAcc(cmb.getFundAcc());
		re5205.setCurCode(cmb.getCurCode());
		//招商接口签约开关为off，则不能冲正出金
		if("off".equals(this.sign)){
			cmb.setSuccess(false);
			cmb.setMemo("未开通此功能");
			re5205.setRespCode("2115");
			re5205.setErrMsg(cmb.getMemo());
		}else{
			//招商接口出金开关为off，则不能在银行方做冲正出金
			if("off".equals(this.outgolden)){
				cmb.setSuccess(false);
				cmb.setMemo("非银行发起方交易");
				re5205.setRespCode("2091");
				re5205.setErrMsg(cmb.getMemo());
			}else{
				if(null!=u){
					if(!"2".equals(u.getFlag())){//未开通三方存管
						cmb.setSuccess(false);
						cmb.setMemo("银商转账服务协议已关闭");
						re5205.setRespCode("2038");
						re5205.setErrMsg(cmb.getMemo());
					}else{
						MemberBase mb = this.memberBaseService.getMemByUser(u);
						String pass = u.getPassword();//密码
						String bankaccount = mb.getBankAccount();//银行账号
						String realname = u.getRealname();//客户名称
						String idcard = "";//证件号
						boolean qiye = "0".equals(mb.getCategory());
						if(qiye){
							idcard = mb.geteOrgCode();//组织机构编码
						}else{
							idcard = mb.getIdCardNo();
						}
						if(pass.equals(cmb.getPinBlock())){//密码相符
							if(bankaccount.equals(cmb.getBankAcc())){//银行卡号与交易账号相符
								if(realname.equals(cmb.getCustName())){//客户名称与交易账号相符
									if(idcard.equals(cmb.getIDNo())){//证件号码与交易账号相符
										AccountDeal ad = accountDealService.getBkSerial(u.getUserAccount(), cmb.getBkSerial());
										if(null!=ad){//有记录,执行冲正操作
											cmb.setSuccess(true);
											re5205.setRespCode("0000");
										}else{//无记录
											cmb.setSuccess(false);
											cmb.setMemo("没有对应的转账记录");
											re5205.setRespCode("2089");
											re5205.setErrMsg(cmb.getMemo());
										}
									}else{
										cmb.setSuccess(false);
										cmb.setMemo("证件不符");
										re5205.setRespCode("2093");
										re5205.setErrMsg(cmb.getMemo());
									}
								}else{
									cmb.setSuccess(false);
									cmb.setMemo("户名不符");
									re5205.setRespCode("2095");
									re5205.setErrMsg(cmb.getMemo());
								}
							}else{//银行卡号与交易账号不相符
								cmb.setSuccess(false);
								cmb.setMemo("交易所交易账号与银行账号不一致");
								re5205.setRespCode("2039");
								re5205.setErrMsg(cmb.getMemo());
							}
						}else{//密码不相符
							cmb.setSuccess(false);
							cmb.setMemo("交易所交易账户密码错误");
							re5205.setRespCode("2085");
							re5205.setErrMsg(cmb.getMemo());
						}
					}
				}else{
					cmb.setSuccess(false);
					cmb.setMemo("交易所交易账号不存在");
					re5205.setRespCode("2009");
					re5205.setErrMsg(cmb.getMemo());
				}
			}
		}
		try {
			if(cmb.isSuccess()){//执行冲正出金操作，商转银，负值金额
				boolean s = accountDealService.in_out_bank(u.getUserAccount(), -amount, AccountDeal.ZQ2BANK, "26",cmb.getBkSerial(),cmb.getCoSerial());
				if(!s){//冲正出金错误
					cmb.setSuccess(false);
					msg = "error";
				}else{
					msg = re5205.toXML();
				}
			}else{
				msg = re5205.toXML();
			}
			this.insert(cmb);
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5400(String json) {
		String msg = "";
		BankRequest5400 r5400 = new BankRequest5400();
		r5400 = r5400.toObject(json);
		CmbDeal cmb = new CmbDeal("查询资金账户余额","5400");
		cmb.setBkSerial(r5400.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBankAcc(r5400.getBankAcc());
		cmb.setFundAcc(r5400.getFundAcc());
		cmb.setCurCode(r5400.getCurCode());
		cmb.setCoBrn(r5400.getCoBrn());
		cmb.setSuccess(true);
		
		User u = userService.findUser(cmb.getFundAcc());
		
		BankResponse5400 re5400 = new BankResponse5400();
		re5400.setBkSerial(cmb.getBkSerial());
		re5400.setFundAcc(cmb.getFundAcc());
		re5400.setCurCode(cmb.getCurCode());
		if(null!=u){
			cmb.setSuccess(true);
			//可取余额：old_balance
			re5400.setFundBal(DoubleUtils.formatDouble(DoubleUtils.doubleCheck(u.getUserAccount().getOld_balance(), 2)));
			//可用余额：balance+frozen
			re5400.setFundUse(DoubleUtils.formatDouble(DoubleUtils.doubleCheck(u.getUserAccount().getBalance()+u.getUserAccount().getFrozenAmount(),2)));
			re5400.setRespCode("0000");
		}else{
			cmb.setSuccess(false);
			cmb.setMemo("交易所交易账号不存在");
			re5400.setRespCode("2009");
			re5400.setErrMsg(cmb.getMemo());
		}
		try {
			this.insert(cmb);
			msg = re5400.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5500(String json) {
		String msg = "";
		BankRequest5500 r5500 = new BankRequest5500();
		r5500 = r5500.toObject(json);
		CmbDeal cmb = new CmbDeal("对账就绪通知","5500");
		cmb.setBkSerial(r5500.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setBatchNo(r5500.getBatchNo());
		cmb.setCoTime(r5500.getCoTime());
		cmb.setTransDate(r5500.getTransDate());
		cmb.setSettRet(r5500.getSettRet());
		cmb.setRemark(r5500.getRemark());
		cmb.setSuccess(true);
		
		BankResponse5500 re5500 = new BankResponse5500();
		re5500.setBkSerial(cmb.getBkSerial());
		re5500.setTransDate(cmb.getTransDate());
		try {
			this.insert(cmb);
			re5500.setRespCode("0000");
			msg = re5500.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	@Transactional
	public String request5600(String json) {
		String msg = "";
		BankRequest5600 r5600 = new BankRequest5600();
		r5600 = r5600.toObject(json);
		CmbDeal cmb = new CmbDeal("密钥更换通知","5600");
		cmb.setBkSerial(r5600.getBkSerial());
		cmb.setCoSerial(DateUtils.generateNo20());//交易所流水
		cmb.setKeyInfo(r5600.getKeyInfo());
		cmb.setKMCode(r5600.getKMCode());
		cmb.setKPCode(r5600.getKPCode());
		cmb.setSuccess(true);
		
		BankResponse5600 re5600 = new BankResponse5600();
		re5600.setBkSerial(cmb.getBkSerial());
		try {
			this.insert(cmb);
			re5600.setRespCode("0000");
			msg = re5600.toXML();
		} catch (EngineException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	public String getCmb_rzr() {
		return cmb_rzr;
	}

	public void setCmb_rzr(String cmb_rzr) {
		this.cmb_rzr = cmb_rzr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getIngolden() {
		return ingolden;
	}

	public void setIngolden(String ingolden) {
		this.ingolden = ingolden;
	}

	public String getOutgolden() {
		return outgolden;
	}

	public void setOutgolden(String outgolden) {
		this.outgolden = outgolden;
	}
}