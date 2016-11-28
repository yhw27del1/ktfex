package com.kmfex.service.hx.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.GsonBuilder;
import com.hitrust.B2B.response.ResponseDZ007;
import com.hitrust.B2B.response.ResponseDZ008;
import com.hitrust.B2B.response.ResponseDZ009;
import com.hitrust.B2B.response.ResponseDZ010;
import com.hitrust.B2B.response.ResponseDZ012;
import com.hitrust.B2B.response.ResponseDZ015;
import com.hitrust.B2B.response.ResponseDZ016;
import com.hitrust.B2B.response.ResponseDZ017;
import com.hitrust.B2B.response.ResponseDZ018;
import com.hitrust.B2B.response.ResponseDZ020;
import com.hitrust.B2B.response.ResponseDZ021;
import com.hitrust.B2B.response.ResponseDZ022;
import com.hitrust.B2B.response.ResponseDZ032;
import com.hitrust.B2B.response.entry.FailedAccountCheckEntry;
import com.hitrust.B2B.response.entry.FailedLiquidationEntry;
import com.hitrust.B2B.response.entry.InOutDetailCheckEntry;
import com.hitrust.B2B.response.entry.ReAccountRegEntry;
import com.kmfex.hxbank.HXBankUtil;
import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankParam_deal;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.hx.HxbankDeal;
import com.kmfex.model.hx.HxbankDealSub;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.kmfex.service.hx.HxbankDealSubService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
/**
 * @author linuxp
 * */
@Service
public class HxbankDealImpl extends BaseServiceImpl<HxbankDeal> implements HxbankDealService {
	private static Logger logger = Logger.getLogger(HxbankDealImpl.class);
	@Resource
	private HxbankDealSubService hxbankDealSubService;
	@Resource
	private UserService userService;
	@Resource
	private AccountService accountService;
	
	@Resource
	private AccountDealService accountDealService;
	//先入库再调用银行接口最后更新库
	
	@Resource
	private HXBankService hxbService;
	
	@Resource OpenCloseDealService openCloseDealService;
	
	//签到
	@Override
	@Transactional
	public HxbankVO sign_in(HxbankParam p,User operator){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ015 rs = null;
		HxbankDeal d = new HxbankDeal("签到","DZ015");
		d.setOperator(operator);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ015.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("签到成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
			}else{
				vo.setFlag(false);
				vo.setTip("签到出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("签到出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//签退
	@Override
	@Transactional
	public HxbankVO sign_off(HxbankParam p,User operator){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ016 rs = null;
		HxbankDeal d = new HxbankDeal("签退","DZ016");
		d.setOperator(operator);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ016.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("签退成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
			}else{
				vo.setFlag(false);
				vo.setTip("签退出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("签退出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//子账户签约(跨行使用),会员开户后(使用非华夏银行卡)审核通过时系统自动调用此方法.
	@Override
	@Transactional
	public HxbankVO subAccountSigned(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ020 rs = null;
		HxbankDeal d = new HxbankDeal("子账户签约","DZ020");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setMerAccountNo(p.getMerAccountNo());
		d.setAccountName(p.getAccountName());
		d.setAmt(p.getAmt());
		d.setAmtUse(p.getAmtUse());
		d.setProp(p.getProp());
		d.setLinkMan(p.getLinkMan());
		d.setPhone(p.getPhone());
		d.setMobile(p.getMobile());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ020.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				ReAccountRegEntry[] m = rs.getAccountRegs();
				String twh = m[0].getMerAccountNo();
				String zzh = m[0].getAccountNo();
				String dealerOperNo = m[0].getDealerOperNo();
				String s = "摊位号:"+twh+",子账号:"+zzh+",登录银行系统操作员代码:"+dealerOperNo;
				vo.setFlag(true);
				vo.setTip("子账户签约成功,"+s);
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				d.setMerAccountNo(twh);
				d.setAccountNo(zzh);
				d.setDealerOperNo(dealerOperNo);
				owner = this.userService.selectById(owner.getId());
				owner.setSignDate(new Date());
				owner.setFlag("2");
				owner.setAccountNo(zzh);
				owner.setDealerOperNo(dealerOperNo);
				owner.setSignBank(1);//三方存管签约行:华夏=1
				owner.setSignType(2);//三方存管签约类型:他行=2
				Account a = this.accountService.selectById(owner.getUserAccount().getId());
				a.setOld_balance(0d);
				try {
					this.userService.update(owner);
					this.accountService.update(a);
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}else{
				vo.setFlag(false);
				vo.setTip("子账户签约出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("子账户签约出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip());
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//子账户同步,会员开户后(使用华夏银行卡)审核通过时系统自动调用此方法.
	@Override
	@Transactional
	public HxbankVO subAccountSync(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ010 rs = null;
		HxbankDeal d = new HxbankDeal("子账户同步","DZ010");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setMerAccountNo(p.getMerAccountNo());
		d.setAccountName(p.getAccountName());
		d.setAmt(p.getAmt());
		d.setAmtUse(p.getAmtUse());
		d.setProp(p.getProp());
		d.setLinkMan(p.getLinkMan());
		d.setPhone(p.getPhone());
		d.setMobile(p.getMobile());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ010.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("子账户同步成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				owner = this.userService.selectById(owner.getId());
				owner.setSynDate_market(new Date());
				owner.setFlag("1");
				owner.setSignBank(1);//三方存管签约行:华夏=1
				owner.setSignType(1);//三方存管签约类型:本行=1
				try {
					this.userService.update(owner);
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}else{
				vo.setFlag(false);
				vo.setTip("子账户同步出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("子账户同步出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//入金申请(入金方式1(本行使用),直接入金)，成功后，实时扣除会员金额
	@Override
	@Transactional
	public HxbankVO inGoldRequest(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ021 rs = null;
		HxbankDeal d = new HxbankDeal("入金","DZ021");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setAmt(p.getAmt());
		d.setAccountNo(p.getAccountNo());
		d.setMerAccountNo(p.getMerAccountNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ021.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("入金成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
			}else{
				vo.setFlag(false);
				vo.setTip("入金出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("入金出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			Account account = owner.getUserAccount();
			account.setDaxie(d.getMessage());
			if(d.isSuccess()){
				this.accountDealService.hxbank_in_out(account, p.getAmt(), AccountDeal.BANK2ZQ, "24");
			}
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//入金登记申请(入金方式2(跨行使用),交易商先转账,再入金登记申请,银行最后发起入金通知)，不扣除会员金额，等等银行的入金通知
	@Override
	@Transactional
	public HxbankVO inGoldRegistrationRequest(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		ResponseDZ022 rs = null;
		HxbankDeal d = new HxbankDeal("入金登记申请","DZ022");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setAmt(p.getAmt());
		d.setAccountNo(p.getAccountNo());
		d.setMerAccountNo(p.getMerAccountNo());
		d.setIostart(p.getIostart());// 入金方式
		d.setPersonName(p.getPersonName());// 汇款人姓名
		d.setWorkDay(p.getDate());// 汇款日期
		d.setCheckDate(p.getCheckDate());
		d.setBankName(p.getBankName());// 汇款银行
		d.setOutAccount(p.getOutAccount());// 汇款帐号
		p.setCheckDate(null);//此日期json转换会报错
		String json = p.getJSON(p);
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ022.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("入金登记申请成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
			}else{
				vo.setFlag(false);
				vo.setTip("入金登记申请出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("入金登记申请出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			//入金登记申请时，交易系统什么都不做。只等待银行的入金通知。
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//出金(出金方式1(本行及他行使用),直接出金)，出金成功后，不执行扣除会员金额。在审核时扣除
	@Override
	@Transactional
	public HxbankVO outGold(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ017 rs = null;
		HxbankDeal d = new HxbankDeal("出金","DZ017");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setAmt(p.getAmt());
		d.setAccountNo(p.getAccountNo());
		d.setMerAccountNo(p.getMerAccountNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ017.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("出金成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
			}else{
				vo.setFlag(false);
				vo.setTip("出金出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("出金出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			Account account = owner.getUserAccount();
			account.setDaxie(d.getMessage());
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//出金审核结果发送(出金方式2(跨行使用),银行先发起出金申请,交易市场再审核)，审核通过则扣除会员金额
	@Override
	@Transactional
	public HxbankVO outGoldAuditResultSend(HxbankParam p,User checkUser,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ007 rs = null;
		HxbankDeal d = this.selectById(p.getId());
		d.setCheckUser(checkUser);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ007.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("出金审核结果发送成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				d.setResult(p.getResult());
			}else{
				vo.setFlag(false);
				vo.setTip("出金审核结果发送出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("出金审核结果发送出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			Account account = owner.getUserAccount();
			account.setDaxie(d.getMessage());
			if("1".equals(p.getResult())){//审核通过
				if(d.isSuccess()){
					this.accountDealService.hxbank_in_out(account, p.getAmt(), AccountDeal.ZQ2BANK, "25");
				}
			}
			this.update(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//银行子帐号余额查询
	@Override
	@Transactional
	public HxbankVO tradingMarketBankBalance(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ032 rs = null;
		HxbankDeal d = new HxbankDeal("银行子帐号余额查询","DZ032");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setAccountNo(p.getAccountNo());
		d.setMerAccountNo(p.getMerAccountNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ032.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				String zzh = rs.getAccountNo();
				String twh = p.getMerAccountNo();
				double amt = rs.getBankAmt();
				double amt_use = rs.getBankAmtUse();
				String s = "摊位号:"+twh+"子账号:"+zzh+",银行总余额:"+amt+"银行可用余额:"+amt_use;
				vo.setFlag(true);
				vo.setTip("银行子帐号余额查询成功,"+s);
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage("银行子帐号余额查询成功");
				d.setAccountNo(rs.getAccountNo());
				d.setMerAccountNo(rs.getBoothNo());//boothNO
				d.setBankAmt(rs.getBankAmt());
				d.setBankAmtUse(rs.getBankAmtUse());
			}else{
				vo.setFlag(false);
				vo.setTip("银行子帐号余额查询出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("银行子帐号余额查询出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//交易商解约
	@Override
	@Transactional
	public HxbankVO surrender(HxbankParam p,User operator,User owner){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		String json = p.getJSON(p);
		ResponseDZ012 rs = null;
		HxbankDeal d = new HxbankDeal("交易商解约","DZ012");
		d.setOperator(operator);
		d.setUser(owner);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setMerAccountNo(p.getMerAccountNo());
		d.setAccountNo(p.getAccountNo());
		
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ012.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("交易商解约成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				owner = this.userService.selectById(owner.getId());
				owner.setSurrenderDate(new Date());
				owner.setFlag("3");
				try {
					this.userService.update(owner);
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}else{
				vo.setFlag(false);
				vo.setTip("交易商解约出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("交易商解约出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//出入金明细核对
	@Override
	@Transactional
	public HxbankVO inOutGoldDetailedCheck(HxbankParam p,User operator){
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		HxbankVO vo = new HxbankVO();
		ResponseDZ018 rs = null;
		HxbankDeal d = new HxbankDeal("出入金明细核对","DZ018");
		d.setOperator(operator);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		List<HxbankDealSub> subs = new ArrayList<HxbankDealSub>();
		d.setWorkDay(p.getDate());
		d.setCheckDate(p.getCheckDate());
		p.setCheckDate(null);//此日期json转换会报错
		String json = p.getJSON(p);
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ018.class);
		}else{
			rs = null;
		}
		
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("出入金明细核对成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setBankTxSerNo(rs.getBankTxSerNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				d.setAmtOut(rs.getAmtOut());
				d.setCountOut(rs.getCountOut());
				d.setAmtIn(rs.getAmtIn());
				d.setCountIn(rs.getCountIn());
				InOutDetailCheckEntry[] s = rs.getInoutDetails();
				int n = s.length;
				for(int i=0;i<n;i++){
					HxbankDealSub sub = new HxbankDealSub();
					sub.setBankTxSerNoHis(s[i].getBankTxSerNoHis());
					sub.setMerTxSerNoHis(s[i].getMerTxSerNoHis());
					sub.setTrnxCodeHis(s[i].getTrnxCodeHis());
					sub.setTrnxType(s[i].getTrnxType());
					sub.setAccountNo(s[i].getAccountNo());
					sub.setMerAccountNo(s[i].getMerAccountNo());
					sub.setAccountName(s[i].getAccountName());
					sub.setAmt(s[i].getAmt());
					sub.setDirection("response");
					sub.setCheckDate(d.getCheckDate());
					subs.add(sub);
				}
			}else{
				vo.setFlag(false);
				vo.setTip("出入金明细核对出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
			}
		}else{
			vo.setFlag(false);
			vo.setTip("出入金明细核对出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			this.insert(d);
			if(null!=subs&&subs.size()>0){
				for(HxbankDealSub sub:subs){
					sub.setParent(d);
					this.hxbankDealSubService.insert(sub);
				}
			}
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//清算,先清算,清算成功后,发送对账报文给银行,清算时清算日期里发生的业务明细发给银行。
	@Override
	public HxbankVO liquidation(HxbankParam p,User operator){
		//清算的业务数据，介于start-end之间
		String parten = "yyyy-MM-dd HH:mm:ss";
		Date start = this.openCloseDealService.getLatestKaiShi();
		Date end = new Date();
		List<HxbankParam_deal> common = this.common_report();
		List<HxbankParam_deal> jie_dai = this.liquidation_report(DateUtils.formatDate(start, parten),DateUtils.formatDate(end, parten));
		jie_dai.addAll(common);
		p.setDeals(jie_dai);
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		p.setBatchNo(HXBankUtil.generateBatchNo2());
		System.out.println("清算报文-"+p.getDate());
		System.out.println("时间跨度-"+DateUtils.formatDate(start, parten)+"至"+DateUtils.formatDate(end, parten));
		logger.info("清算报文-"+p.getDate());
		logger.info("时间跨度-"+DateUtils.formatDate(start, parten)+"至"+DateUtils.formatDate(end, parten));
		for(HxbankParam_deal r:jie_dai){
			String s = null;
			if("01".equals(r.getType())){
				s = "(正常交易)";
			}
			if("02".equals(r.getType())){
				s = "(冻结资金)";
			}
			System.out.println("摊位号:"+r.getMerAccountNo()+"，子账号:"+r.getAccountNo()+"，名称:"+r.getAccountName()+"，交易类型:"+r.getType()+s+"，借贷标识:"+r.getFlag()+"，发生额:"+r.getAmt_fasheng()+"，笔数:"+r.getBishu());
			logger.info("摊位号:"+r.getMerAccountNo()+"，子账号:"+r.getAccountNo()+"，名称:"+r.getAccountName()+"，交易类型:"+r.getType()+s+"，借贷标识:"+r.getFlag()+"，发生额:"+r.getAmt_fasheng());
		}
		HxbankVO vo = new HxbankVO();
		ResponseDZ008 rs = null;
		HxbankDeal d = new HxbankDeal("清算","DZ008");
		d.setOperator(operator);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setWorkDay(p.getDate());
		d.setCheckDate(p.getCheckDate());
		d.setBatchNo(p.getBatchNo());
		p.setCheckDate(null);//此日期json转换会报错
		String json = p.getJSON(p);
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ008.class);
		}else{
			rs = null;
		}
		
		List<HxbankDealSub> subs = new ArrayList<HxbankDealSub>();
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("清算成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				d.setWorkDay(rs.getWorkday());
				d.setBatchNo(rs.getBatchNo());
				//清算成功后，清算报文入库
				for(HxbankParam_deal hd:jie_dai){
					HxbankDealSub sub = new HxbankDealSub();
					sub.setUser_id(hd.getUser_id());
					sub.setAccountNo(hd.getAccountNo());
					sub.setMerAccountNo(hd.getMerAccountNo());
					sub.setAmt(hd.getAmt_fasheng());
					sub.setType(hd.getType());
					sub.setFlag(hd.getFlag());
					sub.setRemark(hd.getRemark());
					sub.setDirection("request");
					sub.setCheckDate(d.getCheckDate());
					sub.setAccountName(hd.getAccountName());
					sub.setBishu(hd.getBishu());
					subs.add(sub);
				}
			}else{
				vo.setFlag(false);
				vo.setTip("清算出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
				FailedLiquidationEntry[] s;
				if(null!=rs.getFailedLiquidations()){
					s = rs.getFailedLiquidations();
					int n = s.length;
					for(int i=0;i<n;i++){
						HxbankDealSub sub = new HxbankDealSub();
						sub.setAccountNo(s[i].getAccountNo());
						sub.setMerAccountNo(s[i].getMerAccountNo());
						sub.setAmt(s[i].getAmt());
						sub.setType(s[i].getType());
						sub.setFlag(s[i].getFlag());
						sub.setRemark(s[i].getRemark());
						sub.setDirection("response");
						sub.setCheckDate(d.getCheckDate());
						subs.add(sub);
					}
				}
			}
		}else{
			vo.setFlag(false);
			vo.setTip("清算出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			Session s = this.getHibernateTemplate().getSessionFactory().openSession();
			Transaction t = s.beginTransaction();
			s.save(d);
			//this.insert(d);
			if(null!=subs&&subs.size()>0){
				for(HxbankDealSub sub:subs){
					sub.setParent(d);
					//this.hxbankDealSubService.insert(sub);
					s.save(sub);
				}
			}
			t.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//对账,先清算,清算成功后,发送对账报文给银行
	@Override
	public HxbankVO reconciliation(HxbankParam p,User operator){
		boolean updateUser = false;//是否更新交易商的old_balance
		List<HxbankParam_deal> common = this.common_report();
		p.setDeals(common);
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		p.setBatchNo(HXBankUtil.generateBatchNo2());
		System.out.println("对账报文-"+p.getDate());
		logger.info("对账报文-"+p.getDate());
		for(HxbankParam_deal r:common){
			System.out.println("摊位号："+r.getMerAccountNo()+"，子账号："+r.getAccountNo()+"，名称:"+r.getAccountName()+"，总余额："+r.getAmt()+"，可用余额："+r.getAmtUse()+"，权益："+r.getInterests());
			logger.info("摊位号："+r.getMerAccountNo()+"，子账号："+r.getAccountNo()+"，名称:"+r.getAccountName()+"，总余额："+r.getAmt()+"，可用余额："+r.getAmtUse()+"，权益："+r.getInterests());
		}
		HxbankVO vo = new HxbankVO();
		
		ResponseDZ009 rs = null;
		HxbankDeal d = new HxbankDeal("对账","DZ009");
		d.setOperator(operator);
		d.setMerchantTrnxNo(p.getMerchantTrnxNo());
		d.setWorkDay(p.getDate());
		d.setCheckDate(p.getCheckDate());
		d.setBatchNo(p.getBatchNo());
		p.setCheckDate(null);//此日期json转换会报错
		String json = p.getJSON(p);
		String loadreturn = this.hxbService.loadHXBank(json,d.getTrnxCode());
		if(null!=loadreturn&&!"".equals(loadreturn)){
			rs = new GsonBuilder().create().fromJson(loadreturn, ResponseDZ009.class);
		}else{
			rs = null;
		}
		
		List<HxbankDealSub> subs = new ArrayList<HxbankDealSub>();
		if(null!=rs){
			if(rs.isSuccessed()){
				vo.setFlag(true);
				vo.setTip("对账成功");
				d.setMerchantTrnxNo(rs.getMerchantTrnxNo());
				d.setSuccess(true);
				//d.setMessage(vo.getTip());
				d.setWorkDay(rs.getWorkday());
				d.setBatchNo(rs.getBatchNo());
				//对账成功后，对账报文入库
				updateUser = true;
				for(HxbankParam_deal hd:common){
					HxbankDealSub sub = new HxbankDealSub();
					sub.setUser_id(hd.getUser_id());
					sub.setAccountNo(hd.getAccountNo());
					sub.setMerAccountNo(hd.getMerAccountNo());
					sub.setAmt(hd.getAmt());
					sub.setAmtUse(hd.getAmtUse());
					sub.setType(hd.getType());
					sub.setFlag(hd.getFlag());
					sub.setRemark(hd.getRemark());
					sub.setDirection("request");
					sub.setCheckDate(d.getCheckDate());
					sub.setAccountName(hd.getAccountName());
					subs.add(sub);
				}
			}else{
				vo.setFlag(false);
				vo.setTip("对账出错:"+rs.getMessage());
				d.setSuccess(false);
				d.setMessage(vo.getTip().split("出错:")[1]);
				FailedAccountCheckEntry[] s;
				if(null!=rs.getFailedAccountChecks()){
					s = rs.getFailedAccountChecks();
					int n = s.length;
					for(int i=0;i<n;i++){
						HxbankDealSub sub = new HxbankDealSub();
						sub.setAccountNo(s[i].getAccountNo());
						sub.setMerAccountNo(s[i].getMerAccountNo());
						sub.setAmt(s[i].getAmt());
						sub.setAmtUse(s[i].getAmtUse());
						sub.setBankAmt(s[i].getBankAmt());
						sub.setBankAmtUse(s[i].getBankAmtUse());
						sub.setDirection("response");
						sub.setCheckDate(d.getCheckDate());
						subs.add(sub);
					}
				}
			}
		}else{
			vo.setFlag(false);
			vo.setTip("对账出错:调用华夏银行接口未响应,请联系管理员.");
			d.setSuccess(false);
			d.setMessage(vo.getTip().split("出错:")[1]);
		}
		try {
			Session s = this.getHibernateTemplate().getSessionFactory().openSession();
			Transaction t = s.beginTransaction();
			s.save(d);
			//this.insert(d);
			if(null!=subs&&subs.size()>0){
				for(HxbankDealSub sub:subs){
					sub.setParent(d);
					//this.hxbankDealSubService.insert(sub);
					s.save(sub);
					if(updateUser){
						//更新交易商的old_balance为balance
						User u = (User) s.load(User.class, sub.getUser_id());
						if(null!=u){
							Account a = u.getUserAccount();
							a.setOld_balance(a.getBalance());
							s.update(a);
						}else{
							System.out.println("更新old_balance时出错，未找到交易商");
						}
					}
				}
			}
			t.commit();
			s.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//统计某交易日的清算报文
	 /**  
    * @param date 某个交易日;
    * @return 清算报文列表
    * 每个成功签约的交易商(有摊位号，有子账号)都有3条清算数据
    * 类别(01正常交易:从accountdeal表中取数据汇总金额；02冻结资金:从交易商的account表中取)
    * 借贷标志(1借:市场余额减少；2贷:市场余额增加)
    * 此方法返回，借列表和贷列表，不含02冻结资金列表
    */  
	@Override
	public List<HxbankParam_deal> liquidation_report(String start,String end){
		List<HxbankParam_deal> deals = new ArrayList<HxbankParam_deal>();
		Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		final OutParameterModel outParameter = new OutParameterModel(3, OracleTypes.CURSOR);
		inParamList.put(1, start);
		inParamList.put(2, end);
		//System.out.println("贷:账户金额增加，银行子账户金额增加");
		ArrayList<LinkedHashMap<String,Object>> list_dai = this.callProcedureForList("PKG_AccountDeal.get_dai", inParamList, outParameter);
		for(int i=0;i<list_dai.size();i++){
			LinkedHashMap<String,Object> a0 = list_dai.get(i);
			//System.out.println(a0.get("name"));
			//System.out.println(a0.get("money"));
			//System.out.println(a0.get("accountno"));
			HxbankParam_deal d = new HxbankParam_deal();
			d.setAccountName(a0.get("realname").toString());
			d.setAccountNo(a0.get("accountno").toString());
			d.setMerAccountNo(a0.get("name").toString());
			d.setAmt_fasheng(Double.parseDouble(a0.get("money").toString()));
			d.setType("01");//交易类型:01正常交易
			d.setFlag("2");//借贷标识:1为‘借’,2位‘贷’；type为02时必须输入，但不验证字段值
			d.setBishu(Integer.parseInt(a0.get("bishu").toString()));
			deals.add(d);
		}
		//System.out.println("============================");
		//System.out.println("借:账户金额减少，银行子账户金额减少");
		ArrayList<LinkedHashMap<String,Object>> list_jie = this.callProcedureForList("PKG_AccountDeal.get_jie", inParamList, outParameter);
		for(int i=0;i<list_jie.size();i++){
			LinkedHashMap<String,Object> a0 = list_jie.get(i);
			//System.out.println(a0.get("name"));
			//System.out.println(a0.get("money"));
			//System.out.println(a0.get("accountno"));
			HxbankParam_deal d = new HxbankParam_deal();
			d.setAccountName(a0.get("realname").toString());
			d.setAccountNo(a0.get("accountno").toString());
			d.setMerAccountNo(a0.get("name").toString());
			d.setAmt_fasheng(Double.parseDouble(a0.get("money").toString()));
			d.setType("01");//交易类型:01正常交易
			d.setFlag("1");//借贷标识:1为‘借’,2位‘贷’；type为02时(冻结资金)必须输入，但不验证字段值
			d.setBishu(Integer.parseInt(a0.get("bishu").toString()));
			//d.setCheckDate(date);//此日期json转换会报错
			deals.add(d);
		}
		return deals;
	}
	
	//共同报文，清算用到，对账也用到，取签约用户的账户金额信息。
	@Override
	public List<HxbankParam_deal> common_report(){
		List<HxbankParam_deal> deals = new ArrayList<HxbankParam_deal>();
		List<User> users = this.userService.getUserForHxbank();//已经成功签约的交易商
		for(User us:users){
			HxbankParam_deal d = new HxbankParam_deal();
			d.setUser_id(us.getId());
			d.setAccountNo(us.getAccountNo());
			d.setMerAccountNo(us.getUsername());
			d.setAccountName(us.getRealname());
			//清算专用数据
			d.setAmt_fasheng(us.getUserAccount().getFrozenAmount());//交易市场冻结金额
			d.setType("02");//交易类型:02冻结资金
			d.setFlag("1");
			
			//对账专用数据
			d.setAmt(us.getUserAccount().getBalance()+us.getUserAccount().getFrozenAmount());//交易市场总余额(可用+冻结)
			d.setAmtUse(us.getUserAccount().getBalance());//交易市场可用余额
			d.setInterests(0.0);//权益，暂时用0补充。
			d.setAmtFrozen(us.getUserAccount().getFrozenAmount());//日切使用
			d.setBishu(1);
			deals.add(d);
		}
		return deals;
	}
	
	@Override
	@Transactional
	public List<HxbankDealSub> getSubs(HxbankDeal deal) {
		String hql = "from HxbankDealSub o where o.parent.id='"+deal.getId()+"'";
		return this.hxbankDealSubService.getCommonListData(hql);
	}

	//return 1；情形1：未签到，未签退：提示未签到，请签到。
	//return 2；情形2：已签到，未签退：提示已签到。
	//return 3；情形3：已签到，已签退：提示已签退，请清算、对账。
	@Transactional
	public byte signState() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date endDateNext = DateUtils.getAfter(today, 1);
		byte state = 0;
		String hql_in = "from HxbankDeal o where o.success=true and o.name='签到' and o.trnxCode='DZ015' ";
		String hql_off = "from HxbankDeal o where o.success=true and o.name='签退' and o.trnxCode='DZ016' ";
		String d1 = " and o.createDate >= to_date('"+ format.format(today) + "','yyyy-MM-dd')";
		String d2 = " and o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')";
		hql_in += d1+d2;
		hql_off += d1+d2;
		List<HxbankDeal> in = this.getCommonListData(hql_in);
		List<HxbankDeal> off = this.getCommonListData(hql_off);
		if((null==in||in.size()<=0)&&(null==off||off.size()<=0)){//情形1：未签到，未签退：提示未签到，请签到。
			state = 1;
		}
		if(null!=in&&in.size()==1&&(null==off||off.size()<=0)){//情形2：已签到，未签退：提示已签到。
			state = 2;
		}
		if(null!=in&&in.size()==1&&null!=off&&off.size()==1){//情形3：已签到，已签退：提示已签退，请清算、对账。
			state = 3;
		}
		return state;
	}
}
