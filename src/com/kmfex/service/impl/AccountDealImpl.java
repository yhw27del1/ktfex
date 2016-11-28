package com.kmfex.service.impl;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.StaleObjectStateException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.JYRBVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.BankLibrary;
import com.kmfex.model.CoreAccountLiveRecord;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.StringUtils;

/**
 * @author linuxp
 * modify sxs 2013.07.17 为插入中心账户记录增加相应的状态：一次性还是按月收费 
 * modify sxs 2013.09.05 修改cash()方法,交易部bzj融资方提现，审核 
 */
@Service
public class AccountDealImpl extends BaseServiceImpl<AccountDeal> implements AccountDealService {
	@Resource
	AccountService accountService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	CoreAccountService coreAccountService;
	@Resource private FinancingCostService financingCostService;

	@Override
	public AccountDeal accountDealRecord(String type, String checkFlag, double money) {
		AccountDeal ad = new AccountDeal();
		ad.setType(type);
		ad.setCheckFlag(checkFlag);
		ad.setMoney(money);
		return ad;
	}

	// 产生充值记录
	@Transactional
	public void chargeRecord(Account account, double money, User u,int businessflag) {
		//Account a = accountService.selectById(account.getId());
		Account a = account;
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 充值前金额
		ad.setNextMoney(ad.getPreMoney());// 充值后金额=充值前金额
		ad.setType(AccountDeal.CASHCHARGE);// 现金充值
		ad.setUser(u);// 充值操作者
		ad.setCheckFlag("0");// 充值待审核
		ad.setBusinessFlag(businessflag);// 1现金充值,21还款充值,22履约充值
		ad.setMemo(account.getDaxie());// 充值负数金额时，存入备注信息
		ad.setHuidan(account.getPassword());//回单信息使用password字段传递过来
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setTxDir(1);//交易转账方向
		ad.setChannel(a.getUser().getChannel());//手工专户
		try {
			this.insert(ad);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	// 产生充值记录,专用账户扣除金额,目标账户增加金额
	@Transactional
	public void chargeRecord_zyzh(Account zyzh, Account mbzh, double money, User u) {
		Account a1 = accountService.selectById(mbzh.getId());// 目标账户：代垫划入
		AccountDeal ad1 = new AccountDeal();
		ad1.setAccount(a1);
		ad1.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad1.setPreMoney(a1.getBalance() + a1.getFrozenAmount());// 操作前金额
		ad1.setNextMoney(ad1.getPreMoney() + ad1.getMoney());// 操作后金额=操作前金额+发生额
		ad1.setType(AccountDeal.DDHR);// 代垫划入
		ad1.setUser(u);// 操作者
		ad1.setCheckFlag("21");// 代垫划入
		ad1.setMemo(mbzh.getDaxie());// 充值负数金额时，存入备注信息
		ad1.setBusinessFlag(13);
		ad1.setSuccessFlag(true);
		ad1.setSuccessDate(ad1.getCreateDate());
		ad1.setSignBank(a1.getUser().getSignBank());//签约行
		ad1.setSignType(a1.getUser().getSignType());//签约类型
		ad1.setTxDir(1);//交易转账方向
		ad1.setChannel(a1.getUser().getChannel());//手工专户

		Account a2 = accountService.selectById(zyzh.getId());// 专用账户：代垫划出
		AccountDeal ad2 = new AccountDeal();
		ad2.setAccount(a2);
		ad2.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad2.setPreMoney(a2.getBalance() + a2.getFrozenAmount());// 操作前金额
		ad2.setNextMoney(ad2.getPreMoney() - ad2.getMoney());// 操作后金额=操作前金额-发生额
		ad2.setType(AccountDeal.DDHC);// 代垫划出
		ad2.setUser(u);// 操作者
		ad2.setCheckFlag("20");// 代垫划出
		ad2.setMemo(mbzh.getDaxie());// 负数金额时，存入备注信息
		ad2.setBusinessFlag(12);
		ad2.setSuccessFlag(true);
		ad2.setSuccessDate(ad2.getCreateDate());
		ad2.setSignBank(a2.getUser().getSignBank());//签约行
		ad2.setSignType(a2.getUser().getSignType());//签约类型
		ad2.setTxDir(2);//交易转账方向
		ad2.setChannel(a2.getUser().getChannel());//手工专户
		try {
			// 目标账户加钱
			a1 = accountService.selectById(mbzh.getId());
			a1.setBalance(a1.getBalance() + money);
			this.accountService.update(a1);
			// 专用账户减钱
			a2 = accountService.selectById(zyzh.getId());
			a2.setBalance(a2.getBalance() - money);
			this.accountService.update(a2);
			ad1.setAccountDeal(ad2);
			ad2.setAccountDeal(ad1);
			this.insert(ad1);
			this.insert(ad2);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	// 产生充值记录，导入时使用，batchFlag设为1，表示批量导入的。
	@Transactional
	public void chargeRecord_import(Account account, double money, User u) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 充值前金额
		ad.setNextMoney(ad.getPreMoney());// 充值后金额=充值前金额
		ad.setType(AccountDeal.CASHCHARGE);// 现金充值
		ad.setUser(u);// 充值操作者
		ad.setCheckFlag("0");// 充值待审核
		ad.setBatchFlag("1");
		ad.setBusinessFlag(1);
		ad.setMemo(account.getDaxie());
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setTxDir(1);//交易转账方向
		ad.setChannel(a.getUser().getChannel());//手工专户
		try {
			this.insert(ad);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	// 投资款划入中心账户
//	@Transactional
//	public void tzkhr(double money, double fee, FinancingBase fin) {
//		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
//		calr.setAction(2);
//		calr.setFbase(fin);
//		calr.setOperater(u);
//		calr.setValue(DoubleUtils.doubleCheck(money, 3));
//		calr.setAbs_value(Math.abs(calr.getValue()));
//		try {
//			this.coreAccountService.insert(calr);
//		} catch (EngineException e) {
//			e.printStackTrace();
//		}
//	}

	 
	/**
	 * 融资款交割给融资方
	 */
	@Transactional
	public void rzkjg(double money, double bzj, String fid, long rid) {
		FinancingBase f = financingBaseService.selectById(fid);// 融资项目
		f.setDilivery("1");// 融资项目变成已交割
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//2013年6月6日改：融资方的交割划入记一条记录；融资方的保证金记一条记录(资金冻结，备注为‘还款保证金’)
		Account rzr = accountService.selectById(rid);// 融资方账户
		AccountDeal rzrAD = new AccountDeal();
		rzrAD.setAccount(rzr);
		rzrAD.setMoney(money + bzj);// 发生额:可用+保证金冻结
		rzrAD.setPreMoney(rzr.getBalance() + rzr.getFrozenAmount());// 前金额
		rzrAD.setNextMoney(rzrAD.getPreMoney() + rzrAD.getMoney());// 后金额=前金额+发生额
		rzrAD.setType(AccountDeal.JGHR);// 交割划入
		rzrAD.setCheckFlag("10");
		rzrAD.setUser(u);
		rzrAD.setFinancing(f);
		rzrAD.setCreateDate(new Date());
		rzrAD.setBusinessFlag(3);
		rzrAD.setSuccessFlag(true);
		rzrAD.setSuccessDate(rzrAD.getCreateDate());
		rzrAD.setSignBank(rzr.getUser().getSignBank());//签约行
		rzrAD.setSignType(rzr.getUser().getSignType());//签约类型
		rzrAD.setTxDir(1);//交易转账方向
		rzrAD.setChannel(rzr.getUser().getChannel());//手工专户
		AccountDeal rzrAD_bzj = null;
		if(bzj>0){
			rzrAD_bzj = this.accountDealRecord("资金冻结", "31", bzj);
			rzrAD_bzj.setAccount(rzr);
			rzrAD_bzj.setPreMoney(rzrAD.getNextMoney());
			rzrAD_bzj.setNextMoney(rzrAD.getNextMoney());
			rzrAD_bzj.setUser(u);
			rzrAD_bzj.setFinancing(f);
			rzrAD_bzj.setTime(rzrAD.getTime()+1);
			rzrAD_bzj.setMemo("还款保证金");
			rzrAD_bzj.setBusinessFlag(16);
			rzrAD_bzj.setSignBank(rzr.getUser().getSignBank());//签约行
			rzrAD_bzj.setSignType(rzr.getUser().getSignType());//签约类型
			rzrAD_bzj.setTxDir(2);//交易转账方向
			rzrAD_bzj.setChannel(rzr.getUser().getChannel());//手工专户
			rzrAD_bzj.setCheckDate(new Date());
			rzrAD_bzj.setCheckUser(u);//审核人为融资方自己
			rzrAD_bzj.setSuccessFlag(true);
			rzrAD_bzj.setSuccessDate(rzrAD_bzj.getCheckDate());
		}
		try {
			// 融资方账户金额增加
			this.insert(rzrAD);
			this.accountService.addMoney(rzr, money, bzj);
			if(null!=rzrAD_bzj){
				this.insert(rzrAD_bzj);//资金冻结记录
			}
			this.financingBaseService.update(f);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	//投资人客户端调用，融资方web页面自主提现
	//2014-1-8修改:提现申请时,可用减少,冻结增加
	@Override
	@Transactional
	public boolean cash(Account account, double money) {
		boolean flag = false;
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 提现前金额
		ad.setNextMoney(ad.getPreMoney());// 提现后金额=提现前金额
		ad.setType(AccountDeal.CASH);// 提现
		ad.setCheckFlag("3");// 提现等待审核
		ad.setBusinessFlag(2);// 普通提现
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setTxDir(2);//交易转账方向
		ad.setChannel(a.getUser().getChannel());//手工专户
		User user = null;
		try {
			user = a.getUser();
			ad.setUser(user);
			try {
				this.insert(ad);
				// 提现者账户可用金额减少,冻结金额增加
				return this.accountService.frozenAccount_old(a, money);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	//交易部为融资方发起提现
	//2014-1-8修改:提现申请时,可用减少,冻结增加
	@Override
	@Transactional
	public boolean cash(Account account, double money,User user) {
		boolean flag = false;
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 提现前金额
		ad.setNextMoney(ad.getPreMoney());// 提现后金额=提现前金额
		ad.setType(AccountDeal.CASH);// 提现
		
		if("2.5_toCachRz".equals(account.getString2())){//辅助变量，解决交易部帮融资方提现A岗申请，B岗审核传参问题
			ad.setCheckFlag("2.5");//2.5表示交易部A岗提交状态，B岗审核变成3进入等待结算部审核状态-->2013-08-30新增
		}else{
			ad.setCheckFlag("3");// 提现等待审核
		}
		
		
		ad.setBusinessFlag(2);// 普通提现 
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setTxDir(2);//交易转账方向
		ad.setMemo(account.getString1());//申请备注
		ad.setChannel(a.getUser().getChannel());//手工专户
		try { 
			ad.setUser(user);
			try {
				this.insert(ad);
				// 提现者账户可用金额减少,冻结金额增加
				return this.accountService.frozenAccount_old(a, money);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	//投资人客户端华夏接口出金
	//2014-1-8修改:华夏接口出金申请时,可用减少,冻结增加
	@Override
	@Transactional
	public boolean outGolden(Account account, double money, String type, String flag, BankLibrary bank) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setCheckDate(ad.getCreateDate());
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 提现前金额
		ad.setNextMoney(ad.getPreMoney());// 提现后金额=提现前金额
		ad.setType(type);
		ad.setCheckFlag(flag);
		ad.setBusinessFlag(19);
		User user = null;
		try {
			user = a.getUser();
			ad.setUser(user);
			try {
				this.insert(ad);
				// 提现者账户可用金额减少,冻结金额增加
				return this.accountService.frozenAccount(a, money);
			} catch (EngineException e) {
				e.printStackTrace();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 融资提现
	 * 
	 * @param account
	 *            提现帐户
	 * @param money
	 *            提现金额
	 * @param fb
	 *            对应融资项目
	 */
	//2014-1-8修改:提现申请时,可用减少,冻结增加
	@Override
	@Transactional
	public void financeCash(long financer_id, double money, FinancingBase financingBase) {
		Account a = accountService.selectById(financer_id);
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 提现前金额
		ad.setNextMoney(ad.getPreMoney());// 提现后金额=提现前金额
		ad.setType(AccountDeal.CASH);// 提现
		ad.setCheckFlag("2.9");// 提现等待审核
		ad.setBusinessFlag(20);// 融资提现
		ad.setFinancing(financingBase);
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setTxDir(2);//交易转账方向
		ad.setChannel(a.getUser().getChannel());//手工专户
		User user = null;
		try {
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ad.setUser(user);
			try {
				this.insert(ad);
				// 提现者账户可用金额减少,冻结金额增加
				this.accountService.frozenAccount_old(a, money);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			ad.setUser(a.getUser());
			try {
				this.insert(ad);
				// 提现者账户可用金额减少,冻结金额增加
				this.accountService.frozenAccount_old(a, money);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public strictfp int payToInvestor(FinancingBase fb,  Account t_Account, double bj, double lx, double fj,double ss_fee_4 ,User operator) throws HibernateOptimisticLockingFailureException,StaleObjectStateException,EngineException,Exception {
		try {
			double money = bj + lx + fj;
			
			AccountDeal TAD = new AccountDeal();
			TAD.setAccount(t_Account);
			TAD.setMoney(DoubleUtils.doubleCheck(money, 3));
			TAD.setPreMoney(t_Account.getBalance() + t_Account.getFrozenAmount());
			TAD.setNextMoney(TAD.getPreMoney() + TAD.getMoney());
			TAD.setType(AccountDeal.HKHR);// 还款划入
			TAD.setCheckFlag("9");// 还款划入(还款划出待审核时，还款划入为9)
			TAD.setFinancing(fb);
			TAD.setBj(DoubleUtils.doubleCheck(bj, 3));
			TAD.setLx(DoubleUtils.doubleCheck(lx, 3));
			TAD.setFj(DoubleUtils.doubleCheck(fj, 3));
			TAD.setUser(operator);
			TAD.setBusinessFlag(9);
			TAD.setSuccessFlag(true);
			TAD.setSuccessDate(TAD.getCreateDate());
			TAD.setSignBank(t_Account.getUser().getSignBank());//签约行
			TAD.setSignType(t_Account.getUser().getSignType());//签约类型
			TAD.setTxDir(1);//交易转账方向
			TAD.setChannel(t_Account.getUser().getChannel());//手工专户
			
			// 投资人账户金额增加
			this.accountService.addMoney(t_Account, money - ss_fee_4);
			
			
			if(ss_fee_4 >0d){
				AccountDeal TAD2 = new AccountDeal();
				TAD2.setAccount(t_Account);
				TAD2.setMoney(DoubleUtils.doubleCheck(ss_fee_4, 3));
				TAD2.setPreMoney(TAD.getNextMoney());
				TAD2.setNextMoney(TAD2.getPreMoney() - TAD2.getMoney());
				TAD2.setType(AccountDeal.JYFWF);// 交易手续费
				TAD2.setCheckFlag("51");// 交易手续费
				TAD2.setFinancing(fb);
				TAD2.setUser(operator);
				TAD2.setBusinessFlag(51);
				TAD2.setSuccessFlag(true);
				TAD2.setSuccessDate(TAD2.getCreateDate());
				TAD2.setSignBank(t_Account.getUser().getSignBank());//签约行
				TAD2.setSignType(t_Account.getUser().getSignType());//签约类型
				TAD2.setTxDir(2);//交易转账方向
				TAD2.setChannel(t_Account.getUser().getChannel());//手工专户
				this.insert(TAD2);
			}
			
			
			this.insert(TAD);
			
			return 1;
		}catch(StaleObjectStateException sose){
			return -1;
		} catch (EngineException e) {
			e.printStackTrace();
			return 0;
		}
	}

	// (2012-9-14修改：third改为中心账户，将费用划给中心账户)
	@Override
	@Transactional
	public strictfp int payFromFinancier( double [] val, double [] val2,double ss_fee_4_all, Account r_account, FinancingBase fb, User operator) throws HibernateOptimisticLockingFailureException,StaleObjectStateException,EngineException,Exception {
		
		double premoney = 0d, nextmoney = 0d;
		double tzr_kx = 0d, fy_kx = 0d;
		for(double d : val){
			tzr_kx += d;
		}
		
		for(double d : val2){
			fy_kx += d;
		}
		
		if (tzr_kx+fy_kx !=0 ) {
			AccountDeal ad = new AccountDeal();
			ad.setAccount(r_account);
			ad.setBj(val[0]);
			ad.setLx(val[1]);
			ad.setFj(val[2]);
			ad.setMoney((double) Math.round((tzr_kx + fy_kx)* 100) / 100);// 发生额
			premoney = (double) Math.round((r_account.getBalance() + r_account.getFrozenAmount()) * 100) / 100;
			ad.setPreMoney(premoney);// 前金额
			nextmoney = (double) Math.round((ad.getPreMoney() - ad.getMoney()) * 100) / 100;
			ad.setNextMoney(nextmoney);// 后金额=前金额-发生额
			ad.setType(AccountDeal.HKHC);// 还款划出
			ad.setCreateDate(DateUtils.getAfterSeccond(new Date(), 4));
			ad.setCheckFlag("6");
			ad.setFinancing(fb);
			ad.setUser(operator);
			ad.setCreateDate(DateUtils.getAfterSeccond(new Date(), 5));
			ad.setBusinessFlag(4);
			ad.setSuccessFlag(true);
			ad.setSuccessDate(ad.getCreateDate());
			ad.setSignBank(r_account.getUser().getSignBank());//签约行
			ad.setSignType(r_account.getUser().getSignType());//签约类型
			ad.setTxDir(2);//交易转账方向
			ad.setChannel(r_account.getUser().getChannel());//手工专户
			boolean report = this.accountService.loseMoney(r_account, ad.getMoney());
			if(!report ) throw new Exception("融资方扣款失败");
			this.insert(ad);
		}
		//融资服务费
		if (val2[0] != 0) {
			// 第三方账户处理,加钱。
			CoreAccountLiveRecord calr_zhglf = new CoreAccountLiveRecord();
			calr_zhglf.setAction(6);
			calr_zhglf.setValue(val2[0]);// 发生额:融资服务费+担保费+罚金
			calr_zhglf.setAbs_value(Math.abs(calr_zhglf.getValue()));
			calr_zhglf.setOperater(operator);
			calr_zhglf.setFbase(fb);
			calr_zhglf.setObject_(r_account);
			calr_zhglf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 1));
			calr_zhglf.setTariff(1);
			this.coreAccountService.insert(calr_zhglf);
		}
		//担保费
		if (val2[1] != 0) {
			// 第三方账户处理,加钱。
			CoreAccountLiveRecord calr_dbf = new CoreAccountLiveRecord();
			calr_dbf.setAction(7);
			calr_dbf.setValue(val2[1]);// 发生额:融资服务费+担保费+罚金
			calr_dbf.setAbs_value(Math.abs(calr_dbf.getValue()));
			calr_dbf.setOperater(operator);
			calr_dbf.setFbase(fb);
			calr_dbf.setObject_(r_account);
			calr_dbf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 1));
			calr_dbf.setTariff(1);
			this.coreAccountService.insert(calr_dbf);
		}
		//风险管理费
		if (val2[2] != 0) {
			// 第三方账户处理,加钱。
			CoreAccountLiveRecord calr_dbf = new CoreAccountLiveRecord();
			calr_dbf.setAction(8);
			calr_dbf.setValue(val2[2]);// 发生额:融资服务费+担保费+罚金
			calr_dbf.setAbs_value(Math.abs(calr_dbf.getValue()));
			calr_dbf.setOperater(operator);
			calr_dbf.setFbase(fb);
			calr_dbf.setObject_(r_account);
			calr_dbf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 1));
			calr_dbf.setTariff(1);
			this.coreAccountService.insert(calr_dbf);
		}
		//20140424
		//交易手续费
		if (ss_fee_4_all != 0) {
			// 第三方账户处理,加钱。
			CoreAccountLiveRecord calr_jyfwf = new CoreAccountLiveRecord();
			calr_jyfwf.setAction(9);
			calr_jyfwf.setValue(ss_fee_4_all);// 发生额:(利息+罚金) * 比例
			calr_jyfwf.setAbs_value(Math.abs(calr_jyfwf.getValue()));
			calr_jyfwf.setOperater(operator);
			calr_jyfwf.setFbase(fb);
			calr_jyfwf.setObject_(r_account);
			calr_jyfwf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 1));
			calr_jyfwf.setTariff(1);
			this.coreAccountService.insert(calr_jyfwf);
		}
			
		
		return 1;
	}

	/**
	 * 三方结算
	 * 
	 * @param Account
	 *            th 第三方帐户
	 * @param double
	 *            jsbl
	 * @return
	 */
	@Override
	@Transactional
	public boolean sfjs(Account th, double jsbl) {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account third = this.accountService.selectById(th.getId());
		double b = third.getBalance() * (jsbl / 100);
		if (third.getBalance() >= b) {
			try {
				// 三方账户余额*结算比例，即为中心账户所得金额
				// 三方账户金额减少,结算划出 17
				AccountDeal ad_third = this.accountDealRecord(AccountDeal.JSHC, "17", b);
				ad_third.setPreMoney(third.getBalance() + third.getFrozenAmount());// 前金额
				ad_third.setNextMoney(ad_third.getPreMoney() - ad_third.getMoney());// 后金额=前金额-发生额
				ad_third.setAccount(third);
				ad_third.setUser(u);
				this.insert(ad_third);
				this.accountService.loseMoney(third, ad_third.getMoney());

				// 中心账户金额增加,结算划入
				CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
				calr.setAction(3);
				calr.setObject_(third);
				calr.setOperater(u);
				calr.setValue(b);
				calr.setAbs_value(Math.abs(calr.getValue()));
				this.coreAccountService.insert(calr);

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public void bzjThaw(Account account, double money, User u) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 解冻前金额
		ad.setNextMoney(ad.getPreMoney());// 解冻后金额=解冻前金额
		ad.setType(AccountDeal.BZJTHAW);// 保证金解冻
		ad.setCheckFlag("19");
		ad.setUser(u);// 解冻操作者
		ad.setBusinessFlag(7);
		ad.setSuccessFlag(true);
		ad.setSuccessDate(ad.getCreateDate());
		try {
			this.insert(ad);
			a.setFrozenAmount(a.getFrozenAmount() - money);// 冻结金额减少
			a.setBalance(a.getBalance() + money);// 可用余额增加
			this.insert(ad);
			this.accountService.update(a);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	//内部转账记录生成,此时并不扣除金额
	@Override
	@Transactional
	public void internalTransfer(Account out, Account in, double money, User u) {
		Account a1 = accountService.selectById(in.getId());// 转入账户：内部转入
		AccountDeal ad1 = new AccountDeal();
		ad1.setAccount(a1);
		ad1.setMoney(DoubleUtils.doubleCheck(money, 2));// 发生额
		ad1.setPreMoney(a1.getBalance() + a1.getFrozenAmount());// 操作前金额
		ad1.setNextMoney(ad1.getPreMoney());// 操作后金额=操作前金额
		ad1.setType(AccountDeal.NBZR);
		ad1.setUser(u);// 操作者
		ad1.setCheckFlag("23");// 内部转入
		ad1.setBusinessFlag(15);
		ad1.setMemo(in.getDaxie());// 存入备注信息

		Account a2 = accountService.selectById(out.getId());// 转出账户：内部转出
		AccountDeal ad2 = new AccountDeal();
		ad2.setAccount(a2);
		ad2.setMoney(DoubleUtils.doubleCheck(money, 2));// 发生额
		ad2.setPreMoney(a2.getBalance() + a2.getFrozenAmount());// 操作前金额
		ad2.setNextMoney(ad2.getPreMoney());// 操作后金额=操作前金额
		ad2.setType(AccountDeal.NBZC);
		ad2.setUser(u);// 操作者
		ad2.setCheckFlag("22");// 内部转出
		ad2.setBusinessFlag(14);
		ad2.setMemo(out.getDaxie());// 负数金额时，存入备注信息
		try {
			ad1.setAccountDeal(ad2);
			ad2.setAccountDeal(ad1);
			this.insert(ad1);
			this.insert(ad2);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	//后台“银行管理”下的功能进行出入金
	// 华夏银行出入金
	public void hxbank_in_out(Account account, double money, String type, String checkFlag) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setCheckDate(ad.getCreateDate());// checkDate与createDate同步。需要审核的业务，在审核时再更新checkDate字段
		ad.setAccount(a);
		ad.setType(type);
		ad.setCheckFlag(checkFlag);
		ad.setUser(a.getUser());// 操作者
		ad.setMemo(account.getDaxie());
		ad.setMoney(money);// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 充值前金额
		try {
			if (AccountDeal.BANK2ZQ.equals(type)) {// 银行转交易市场，入金，目标账户增加金额，银转商，入金（不用审核）
				ad.setNextMoney(ad.getPreMoney() + ad.getMoney());// 充值后金额=充值前金额+发生额
				ad.setCheckUser(ad.getUser());// 审核者为操作者
				ad.setBusinessFlag(18);
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCreateDate());
				Account my = accountService.selectById(account.getId());
				my.setOld_balance(my.getOld_balance() + ad.getMoney());
				my.setBalance(my.getBalance() + ad.getMoney());
				accountService.update(account);
			} else if (AccountDeal.ZQ2BANK.equals(type)) {// 交易市场转银行，出金，目标账户减少金额，商转银，出金（需要审核）
				ad.setNextMoney(ad.getPreMoney() - ad.getMoney());// 充值后金额=充值前金额-发生额
				ad.setBusinessFlag(19);
				Account my = accountService.selectById(account.getId());
				my.setOld_balance(my.getOld_balance() - ad.getMoney());
				my.setBalance(my.getBalance() - ad.getMoney());
				accountService.update(account);
			}
			this.insert(ad);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	@Override
	public HashMap<String, Object> recharge_kiting_today(String org_code, int type, String count_key, String sum_key, Date date) {
		HashMap<String, Object> result = new LinkedHashMap<String, Object>();
		HashMap<Integer, Object> list = null;
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
		inParamList.put(1, new java.sql.Date(date.getTime()));
		inParamList.put(2, org_code);
		inParamList.put(3, type);
		outParameter.put(4, Types.INTEGER);
		outParameter.put(5, Types.FLOAT);
		list = this.callProcedureForParameters("PKG_AccountDeal.print_recharge_kiting_report", inParamList, outParameter);
		result.put(count_key, list.get(4));
		result.put(sum_key, list.get(5));
		list.clear();
		inParamList.clear();
		outParameter.clear();
		list = null;
		inParamList = null;
		outParameter = null;
		return result;
	}

	public List<JYRBVO> jyrb_extend(Date startDate, Date endDate, String openOrgCode) {
		HashMap<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		inParamList.put(1, new java.sql.Date(startDate.getTime()));
		inParamList.put(2, new java.sql.Date(endDate.getTime()));
		HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
		outParameter.put(4, Types.DOUBLE);
		outParameter.put(5, Types.DOUBLE);
		outParameter.put(6, Types.DOUBLE);
		outParameter.put(7, Types.INTEGER);
		outParameter.put(8, Types.DOUBLE);
		outParameter.put(9, Types.INTEGER);
		outParameter.put(10, Types.DOUBLE);
		outParameter.put(11, Types.DOUBLE);
		if (null != openOrgCode) {
			List<JYRBVO> ms = new ArrayList<JYRBVO>();
			String[] orgs = openOrgCode.split(",");
			for (String org : orgs) {// 机构过滤
				String[] code = org.split("@");
				JYRBVO mo = new JYRBVO(code[0], code[1]);
				inParamList.put(3, code[0]);
				HashMap<Integer, Object> results = this.callProcedureForParameters("PKG_Account.jyrb_extend", inParamList, outParameter);
				if (null != results) {
					mo.setSum_balance(Double.parseDouble(results.get(4).toString()));
					mo.setSum_frozen(Double.parseDouble(results.get(5).toString()));
					mo.setSum_in(Double.parseDouble(results.get(6).toString()));
					mo.setCount_in(Integer.parseInt(results.get(7).toString()));
					mo.setSum_out(Double.parseDouble(results.get(8).toString()));
					mo.setCount_out(Integer.parseInt(results.get(9).toString()));
					mo.setSum_balance_s(Double.parseDouble(results.get(10).toString()));
					mo.setSum_frozen_s(Double.parseDouble(results.get(11).toString()));
				}
				ms.add(mo);
			}
			return ms;
		} else {
			return null;
		}
	}
	
	@Override
	@Transactional
	/**
	 * 
	 * 2013-05-27 增加费用
	 */
	public void qrhk(FinancingCost fc, User operator, AccountDeal ad) {
		try {
			ad.setCheckFlag2("1");// 已划款
			ad.setHkDate(new Date());// 划款日期
			ad.setHkUser(operator);
			FinancingBase fb = null;
			
			if (fc != null) {
				fb = fc.getFinancingBase();
				fc.setHkDate(new Date());
				fc.setHkBy(operator);
				
				
				
				fb.setTxed(true);
				this.financingBaseService.update(fb);
				
				this.financingCostService.update(fc);
			}
			
			
			this.update(ad);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public int getHuiDan(Account account,String huidan){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date today_next = DateUtils.getAfter(today, 1);
		String hql = "from AccountDeal o where o.successFlag = true and o.type ='" + AccountDeal.CASHCHARGE + "' and o.huidan='"+huidan+"' ";
		hql += " and o.account.id =" + account.getId();
		hql += " and o.createDate between to_date('" + format.format(today) + "','yyyy-MM-dd') and to_date('" + format.format(today_next) + "','yyyy-MM-dd')";
		List<AccountDeal>  s = this.getCommonListData(hql);
		if(null==s){
			return 0;
		}else{
			return s.size();
		}
	}
	
	//批量导入利息，batchFlag设为1，表示批量导入的。并未增加账户金额
	@Transactional
	public void lx_import(Account account, double money, User u) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 3));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 导入利息前金额
		ad.setNextMoney(ad.getPreMoney());// 导入利息后金额=导入利息前金额
		ad.setType(AccountDeal.HQLX);//活期利息
		ad.setUser(u);//操作者
		ad.setCheckFlag("36");//活期利息待审核
		ad.setBatchFlag("1");
		ad.setBusinessFlag(23);
		ad.setMemo(account.getDaxie());
		try {
			this.insert(ad);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	//兴易贷批量划拨，batchFlag设为1，表示批量导入的。并未增加账户金额
	@Transactional
	public void xyd_import(Account account, double money, User u) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setAccount(a);
		ad.setMoney(DoubleUtils.doubleCheck(money, 2));// 发生额
		ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 导入利息前金额
		ad.setNextMoney(ad.getPreMoney());// 导入利息后金额=导入利息前金额
		ad.setType(AccountDeal.ZJBR);//资金拨入
		ad.setUser(u);//操作者
		ad.setCheckFlag("40");//资金拨入待审核
		ad.setBatchFlag("1");
		ad.setBusinessFlag(24);
		ad.setMemo(account.getDaxie());
		try {
			this.insert(ad);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	
	//改方法废弃
	@Override
	@Transactional
	public boolean pay(String fid, long RID, long TID, double bj, double lx, double fj) {
		FinancingBase fb = this.financingBaseService.selectById(fid);
		double money = bj + lx + fj;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account R = this.accountService.selectById(RID);
		AccountDeal RAD = new AccountDeal();
		RAD.setAccount(R);
		RAD.setMoney(DoubleUtils.doubleCheck(money, 3));
		RAD.setPreMoney(R.getBalance() + R.getFrozenAmount());
		RAD.setNextMoney(RAD.getPreMoney() - RAD.getMoney());
		RAD.setType(AccountDeal.HKHC);// 还款划出
		RAD.setCheckFlag("6");// 还款划出待审核
		RAD.setFinancing(fb);
		RAD.setBj(DoubleUtils.doubleCheck(bj, 3));
		RAD.setLx(DoubleUtils.doubleCheck(lx, 3));
		RAD.setFj(DoubleUtils.doubleCheck(fj, 3));
		RAD.setUser(user);
		RAD.setBusinessFlag(4);
		RAD.setSuccessFlag(true);
		RAD.setSuccessDate(RAD.getCreateDate());

		Account T = this.accountService.selectById(TID);
		AccountDeal TAD = new AccountDeal();
		TAD.setAccount(T);
		TAD.setMoney(DoubleUtils.doubleCheck(money, 3));
		TAD.setPreMoney(T.getBalance() + T.getFrozenAmount());
		TAD.setNextMoney(TAD.getPreMoney() + TAD.getMoney());
		TAD.setType(AccountDeal.HKHR);// 还款划入
		TAD.setCheckFlag("9");// 还款划入(还款划出待审核时，还款划入为9)
		TAD.setFinancing(fb);
		TAD.setBj(DoubleUtils.doubleCheck(bj, 3));
		TAD.setLx(DoubleUtils.doubleCheck(lx, 3));
		TAD.setFj(DoubleUtils.doubleCheck(fj, 3));
		TAD.setUser(user);
		TAD.setBusinessFlag(9);
		TAD.setSuccessFlag(true);
		TAD.setSuccessDate(TAD.getCreateDate());
		try {
			// 投资人账户金额增加
			this.accountService.addMoney(T, money);
			// 融资方账户金额减少
			this.accountService.loseMoney(R, money);

			this.insert(RAD);
			TAD.setAccountDeal(RAD);
			this.insert(TAD);

			return true;
		} catch (StaleObjectStateException sose) {
			return false;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	
	// (2012-9-14修改：third改为中心账户，将费用划给中心账户)
	@Override
	@Transactional
	public void zhglfAndDbf(double zhglf, double dbf, long RID, String fid, double zhglf_fj, double dbf_fj) {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FinancingBase fb = this.financingBaseService.selectById(fid);
		double premoney = 0d, nextmoney = 0d;
		// 融资方账户处理,减钱。
		// 综合管理费
		try {
			Account rzrAccount = accountService.selectById(RID);// 融资方账户
			if (zhglf + zhglf_fj != 0) {
				AccountDeal rzrAD1 = new AccountDeal();
				rzrAD1.setAccount(rzrAccount);
				rzrAD1.setMoney(zhglf + zhglf_fj);// 发生额
				rzrAD1.setFj(zhglf_fj);// 综合管理费罚金
				premoney = (double) Math.round((rzrAccount.getBalance() + rzrAccount.getFrozenAmount()) * 100) / 100;
				rzrAD1.setPreMoney(premoney);// 前金额
				nextmoney = (double) Math.round((rzrAD1.getPreMoney() - rzrAD1.getMoney()) * 100) / 100;
				rzrAD1.setNextMoney(nextmoney);// 后金额=前金额-发生额
				rzrAD1.setType(AccountDeal.ZHGLF);// 综合管理费
				rzrAD1.setCheckFlag("15");
				rzrAD1.setFinancing(fb);
				rzrAD1.setUser(u);
				rzrAD1.setCreateDate(DateUtils.getAfterSeccond(new Date(), 3));
				rzrAD1.setBusinessFlag(5);
				rzrAD1.setSuccessFlag(true);
				rzrAD1.setSuccessDate(rzrAD1.getCreateDate());
				this.accountService.loseMoney(rzrAccount, rzrAD1.getMoney());
				this.insert(rzrAD1);
			}

			if (dbf + dbf_fj != 0) {
				// 担保费
				AccountDeal rzrAD2 = new AccountDeal();
				rzrAccount = accountService.selectById(RID);
				rzrAD2.setAccount(rzrAccount);
				rzrAD2.setMoney(dbf + dbf_fj);// 发生额
				rzrAD2.setFj(dbf_fj);// 担保费罚金
				premoney = (double) Math.round((rzrAccount.getBalance() + rzrAccount.getFrozenAmount()) * 100) / 100;
				rzrAD2.setPreMoney(premoney);// 前金额
				nextmoney = (double) Math.round((rzrAD2.getPreMoney() - rzrAD2.getMoney()) * 100) / 100;
				rzrAD2.setNextMoney(nextmoney);// 后金额=前金额-发生额
				rzrAD2.setType(AccountDeal.DBF);// 担保费
				rzrAD2.setCreateDate(DateUtils.getAfterSeccond(new Date(), 4));
				rzrAD2.setCheckFlag("15");
				rzrAD2.setFinancing(fb);
				rzrAD2.setUser(u);
				rzrAD2.setCreateDate(DateUtils.getAfterSeccond(new Date(), 5));
				rzrAD2.setBusinessFlag(6);
				rzrAD2.setSuccessFlag(true);
				rzrAD2.setSuccessDate(rzrAD2.getCreateDate());
				this.accountService.loseMoney(rzrAccount, rzrAD2.getMoney());
				this.insert(rzrAD2);
			}

			if (zhglf + zhglf_fj != 0) {
				// 第三方账户处理,加钱。
				CoreAccountLiveRecord calr_zhglf = new CoreAccountLiveRecord();
				calr_zhglf.setAction(6);
				calr_zhglf.setValue(zhglf + zhglf_fj);// 发生额:综合管理费+综合管理费罚金??融资服务费？
				calr_zhglf.setAbs_value(Math.abs(calr_zhglf.getValue()));
				calr_zhglf.setOperater(u);
				calr_zhglf.setFbase(fb);
				calr_zhglf.setObject_(rzrAccount);
				calr_zhglf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 1));
				calr_zhglf.setTariff(1);
				this.coreAccountService.insert(calr_zhglf);
			}
			if (dbf + dbf_fj != 0) {
				CoreAccountLiveRecord calr_dbf = new CoreAccountLiveRecord();
				calr_dbf.setAction(7);
				calr_dbf.setTariff(1);
				calr_dbf.setValue(dbf + dbf_fj);// 担保费
				calr_dbf.setAbs_value(Math.abs(calr_dbf.getValue()));
				calr_dbf.setOperater(u);
				calr_dbf.setFbase(fb);
				calr_dbf.setObject_(rzrAccount);
				calr_dbf.setCreatetime(DateUtils.getAfterSeccond(new Date(), 2));
				this.coreAccountService.insert(calr_dbf);
			}

		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void qrfk(User operator,String ad_id) throws EngineException,InvalidDataAccessApiUsageException,Exception {
		AccountDeal ad = this.selectById(ad_id);
		if("2.9".equals(ad.getCheckFlag())){
			ad.setCheckFlag("3");
			ad.setFkqruser(operator);
			ad.setFkqrdate(new Date());
			this.update(ad);
		}
	}

	@Override
	@Transactional
	public void bhfk(User operator, String ad_id,String remark) {
		AccountDeal ad = this.selectById(ad_id);
		if("2.9".equals(ad.getCheckFlag())){
			try {
				Account a = this.accountService.selectById(ad.getAccount().getId());
				a.setBalance(a.getBalance() + ad.getMoney());// 会员账户加钱
				a.setFrozenAmount(a.getFrozenAmount()-ad.getMoney());//会员冻结减钱
				a.setOld_balance(a.getOld_balance() + ad.getMoney());
				ad.setCheckFlag("5");
				ad.setCheckFlag2("2");
				ad.setMemo(remark);
				ad.setFkqrdate(new Date());
				ad.setFkqruser(operator);
				this.update(ad);
				this.accountService.update(a);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	//三方存管出入金:招行调用，银行发起出入金时调用此方法，出金和入金都不用审核
	@Override
	@Transactional
	public boolean in_out_bank(Account account, double money, String type, String checkFlag,String bkSerial,String coSerial) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setCoSerial(coSerial);
		ad.setBkSerial(bkSerial);
		ad.setTxOpt(1);//银行发起
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setChannel(a.getUser().getChannel());//手工专户
		ad.setCheckDate(ad.getCreateDate());// checkDate与createDate同步。需要审核的业务，在审核时再更新checkDate字段
		ad.setAccount(a);
		ad.setType(type);
		ad.setCheckFlag(checkFlag);
		ad.setUser(a.getUser());// 操作者
		ad.setMemo(account.getDaxie());
		ad.setMoney(money);// 发生额
		ad.setPreMoney(DoubleUtils.doubleCheck(a.getBalance() + a.getFrozenAmount(), 2));// 充值前金额
		try {
			if (AccountDeal.BANK2ZQ.equals(type)) {// 银行转交易市场，入金，目标账户增加金额，银转商，入金（不用审核）
				ad.setNextMoney(ad.getPreMoney() + ad.getMoney());// 充值后金额=充值前金额+发生额
				ad.setCheckUser(ad.getUser());// 审核者为操作者
				ad.setBusinessFlag(18);
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCreateDate());
				ad.setTxDir(1);
				Account my = accountService.selectById(a.getId());
				my.setBalance(DoubleUtils.doubleCheck(my.getBalance()+ ad.getMoney(), 2) );
				my.setOld_balance(DoubleUtils.doubleCheck(my.getOld_balance()+ ad.getMoney(), 2) );
				accountService.update(my);
			} else if (AccountDeal.ZQ2BANK.equals(type)) {// 交易市场转银行，出金，目标账户减少金额，商转银，出金（不用审核）
				ad.setNextMoney(ad.getPreMoney() - ad.getMoney());// 充值后金额=充值前金额-发生额
				ad.setCheckUser(ad.getUser());// 审核者为操作者
				ad.setBusinessFlag(19);
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCreateDate());
				ad.setTxDir(2);
				Account my = accountService.selectById(a.getId());
				double balance = DoubleUtils.doubleCheck(my.getBalance(),2);
				double old = DoubleUtils.doubleCheck(my.getOld_balance(),2);
				if(balance<ad.getMoney()){//可用金额小于出金金额，则出金失败
					return false;
				}else if(old<ad.getMoney()){//可转金额小于出金金额，则出金失败
					return false;
				}else{
					my.setBalance(DoubleUtils.doubleCheck(balance - ad.getMoney(),2));
					my.setOld_balance(DoubleUtils.doubleCheck(old - ad.getMoney(),2));
					accountService.update(my);
				}
			}
			this.insert(ad);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//三方存管出入金:招行调用，市场发起出入金时调用此方法，出金需要审核
	//2014-1-8修改:招行接口出金申请时,可用减少,冻结增加
	@Override
	@Transactional
	public boolean in_out_merchant(Account account, double money, String type, String checkFlag,String bkSerial,String coSerial) {
		Account a = accountService.selectById(account.getId());
		AccountDeal ad = new AccountDeal();
		ad.setCoSerial(coSerial);
		ad.setBkSerial(bkSerial);
		ad.setTxOpt(0);//交易所发起
		ad.setSignBank(a.getUser().getSignBank());
		ad.setSignType(a.getUser().getSignType());
		ad.setChannel(a.getUser().getChannel());//手工专户
		ad.setCheckDate(ad.getCreateDate());// checkDate与createDate同步。需要审核的业务，在审核时再更新checkDate字段
		ad.setAccount(a);
		ad.setType(type);
		ad.setCheckFlag(checkFlag);
		ad.setUser(a.getUser());// 操作者
		ad.setMemo(account.getDaxie());
		ad.setMoney(money);// 发生额
		ad.setPreMoney(DoubleUtils.doubleCheck(a.getBalance() + a.getFrozenAmount(),2));// 充值前金额
		try {
			if (AccountDeal.BANK2ZQ.equals(type)) {// 银行转交易市场，入金，目标账户增加金额，银转商，入金（不用审核）
				ad.setNextMoney(ad.getPreMoney() + ad.getMoney());// 入金后金额=入金前金额+发生额
				ad.setCheckUser(ad.getUser());// 审核者为操作者
				ad.setBusinessFlag(18);
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCreateDate());
				ad.setTxDir(1);
				Account my = accountService.selectById(a.getId());
				my.setBalance(DoubleUtils.doubleCheck(my.getBalance() + ad.getMoney(), 2));
				my.setOld_balance(DoubleUtils.doubleCheck(my.getOld_balance() + ad.getMoney(), 2));//入金申请时，可转金额增加
				accountService.update(my);
			} else if (AccountDeal.ZQ2BANK.equals(type)) {// 交易市场转银行，出金，目标账户减少金额，商转银，出金（需要审核）
				ad.setNextMoney(ad.getPreMoney());// 出金后金额=出金前金额
				ad.setBusinessFlag(19);
				ad.setSuccessFlag(false);//出金需要审核，审核通过才设置为true
				ad.setTxDir(2);
				Account my = accountService.selectById(a.getId());
				double kequ = DoubleUtils.doubleCheck(my.getBalance(),2);
				double old = DoubleUtils.doubleCheck(my.getOld_balance(),2);//可转金额
				double dongjie = DoubleUtils.doubleCheck(my.getFrozenAmount(),2);
				if(kequ<ad.getMoney()){//可用金额小于出金金额，则出金失败
					return false;
				}else if(old<ad.getMoney()){//可转金额小于出金金额，则出金失败
					return false;
				}else{
					my.setBalance(DoubleUtils.doubleCheck(kequ - ad.getMoney(),2));
					my.setOld_balance(DoubleUtils.doubleCheck(old - ad.getMoney(),2));//出金申请时，可转金额减少
					my.setFrozenAmount(DoubleUtils.doubleCheck(dongjie+ad.getMoney(),2));
					accountService.update(my);
				}
			}
			this.insert(ad);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//判断某个账户当日是否有"银转商","商转银"的资金流水记录
	public boolean hasAccountDeal(Account account){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date next = DateUtils.getAfter(today, 1);
		String s = format.format(today);
		String e = format.format(next);
		String sql = "select d.type from t_accountdeal d where d.successflag=1 and d.type in ('银转商','商转银') and d.account_accountid_="+account.getId()+" and (d.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by d.createDate desc ";
		System.out.println("hasAccountDeal:"+sql);
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(sql);
			if(null!=first&&first.size()>0){
				return true;//有资金流水
			}else{
				return false;
			}
		} catch (Exception e1) {
			return false;
		}
	}
	
	//判断某个账户当日是否有待审核的资金流水记录
	public boolean hasNoCheck(Account account){
		String sql = "select d.type from t_accountdeal d where d.successflag=0 and d.checkflag in ('0','3','36') and d.account_accountid_="+account.getId();
		System.out.println("hasNoCheck:"+sql);
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(sql);
			if(null!=first&&first.size()>0){
				return true;//有资金流水
			}else{
				return false;
			}
		} catch (Exception e1) {
			return false;
		}
	}
	
	//判断某个账户当日是否有流水号为bkSerial的资金流水
	public AccountDeal getBkSerial(Account account,String bkSerial){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date next = DateUtils.getAfter(today, 1);
		String s = format.format(today);
		String e = format.format(next);
		String hql = "from AccountDeal d where d.successFlag=1 and d.bkSerial='"+bkSerial+"' and d.type in ('银转商','商转银') and d.account.id="+account.getId()+" and (d.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by d.createDate desc ";
		System.out.println("getBkSerial:"+hql);
		return this.selectByHql(hql);
	}
	
	//0:贷金额 1:借金额
	public double[] dai_jie(String subsql,Object [] args){
		double[] result = new double[2];
		StringBuilder sql = new StringBuilder(" select sum(money_dai) dai,sum(money_jie) jie from v_accountdeal_new ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString(),args);
			if(row.next()){
				result[0] = row.getDouble("dai");
				result[1] = row.getDouble("jie");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
	
	//0:充值金额 1:提现金额
	public double[] charge_cash(String subsql,Object [] agrs){
		double[] result = new double[2];
		StringBuilder sql = new StringBuilder(" select sum(money_add) adds,sum(money_subtract) subtract from v_accountdeal_new ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString(), agrs);
			if(row.next()){
				result[0] = row.getDouble("adds");
				result[1] = row.getDouble("subtract");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
	
	//判断某个账户当日是否有待审核的提现及银转商流水的总额 
	public double noCheck_cash(Account account){
		double b = 0d;
		String sql = "select sum(d.money) money from t_accountdeal d where d.successflag=0 and d.checkflag in ('3','25') and d.account_accountid_="+account.getId();
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(sql);
			if(null!=first&&first.size()>0){
				b = Double.parseDouble(first.get(0).get("money").toString());
			}
		} catch (Exception e1) {
			//
		}
		return b;
	}
	
	public double XYD(String subsql){
		double result = 0d;
		StringBuilder sql = new StringBuilder(" select sum(money) money from v_accountdeal_new ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString());
			if(row.next()){
				result = row.getDouble("money");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
	
	public double XYD(String subsql,Object[] args){
		double result = 0d;
		StringBuilder sql = new StringBuilder(" select sum(money) money from v_accountdeal_new ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString(),args);
			if(row.next()){
				result = row.getDouble("money");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
	
	//仅仅将待审核变更为：已审核通过
	@Transactional
	public boolean checkOutGolden(AccountDeal ad,User u){
		boolean re = false;
		try {
			re = false;
			if(null!=ad){
				if("25".equals(ad.getCheckFlag())){
					ad.setCheckFlag("26");// 审核通过
					ad.setCheckDate(new Date());
					ad.setCheckUser(u);
					ad.setSuccessFlag(true);
					ad.setSuccessDate(ad.getCheckDate());
					ad.setNextMoney(ad.getPreMoney()-ad.getMoney());
					ad.setTime(ad.getCheckDate().getTime());
					this.update(ad);
					re = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}
}
