package com.wisdoor.core.service.impl;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberBase;
import com.kmfex.model.activity.InvestToLend;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.activity.InvestToLendService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.StringUtils;

@Service

public class AccountImpl extends BaseServiceImpl<Account> implements AccountService {
	@Resource UserService userService; 
	@Resource MemberBaseService memberBaseService;
	@Resource InvestRecordService investRecordService;
	@Resource AccountDealService accountDealService;
	@Resource InvestToLendService investToLendService;
	//会员注册时，为会员创建交易账户，此时账户状态为：未开通
	@Transactional
	public Account createAccount(){
		Account account = new Account();
		account.setState(0);
		account.setAccountId(StringUtils.generateAccountId());//随机生成19位账户编号
		//account.setBalance(100000d);//测试用的金额
		try {
			this.insert(account);
			return account;
		} catch (EngineException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//通过账户编号找账户
	@Transactional
	public Account selectByAccountId(String accountId){
		return this.selectByHql("from Account a where a.accountId = '"+accountId+"'");
		/*List<Account> as = this.getCommonListData("from Account a where a.accountId = "+accountId);
		if(null!=as && as.size()==1){
			return as.get(0);
		}else{
			return null;
		}*/
	}
	//通过用户编号找账户
	//取用户的账户信息，并计算债权
	@Transactional
	public Account selectByUserId(long userId){
		User user=this.userService.selectById(userId);
		MemberBase member = this.memberBaseService.getMemByUser(user);  
		
		String hql = "from InvestRecord o where o.state='2' and o.investor.id='"+member.getId()+"' ";
		double sumAmount=0d;//持有债权
		List<InvestRecord> ls = investRecordService.getCommonListData(hql); 
		if(null!=ls&&ls.size()>0){
			for(InvestRecord o : ls){  
				sumAmount+=o.getBjye();//还款后，投标记录的本金会减少
				//sumAmount+=o.getInvestAmount(); 
			}
		}
		user.getUserAccount().setCyzq(sumAmount);
		return user.getUserAccount();
 
	}
	
	//实时查询账户余额
	@Transactional
	public double queryBalance(Account account){
		//查余额时，余额保留三位小数返回
		return DoubleUtils.doubleCheck(this.selectById(account.getId()).getBalance(), 3);
	}
	
	//账户开通，交易中心终审时使用
	@Transactional
	public boolean enableAccount(Account account){
		account.setState(1);
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//销户
	@Transactional
	public boolean cancelAccount(Account account){
		account.setState(2);
		try {
			this.update(account);
			//销户时，可能要回退该账户的金额
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public Account centerAccount() {
		return this.selectByAccountId(Account.CENTER);
	}
	
	@Override
	@Transactional
	public Account thirdPartyAccount(String thirdParty) {
		return this.selectByAccountId(thirdParty);
	}
	
	@Override
	@Transactional
	public boolean frozenAccount_old(Account a, double money) {
		Account account = this.selectById(a.getId());
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()+money, 2));//冻结金额增加
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()-money,2));//可用金额减少
		account.setOld_balance(DoubleUtils.doubleCheck(account.getOld_balance()-money,2));//可转金额减少
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean frozenAccount(Account a, double money) {
		Account account = this.selectById(a.getId());
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()+money, 3));//冻结金额增加
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()-money,3));//可用金额减少
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

	@Override
	@Transactional
	public boolean thawAccount1(Account a, double money) {
		Account account = this.selectById(a.getId());
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()-money,3));//冻结金额减少
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()+money,3));//可用金额增加
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	@Transactional
	public boolean thawAccount_old(Account a, double money) {
		Account account = this.selectById(a.getId());
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()-money,2));//冻结金额减少
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()+money,2));//可用金额增加
		account.setOld_balance(DoubleUtils.doubleCheck(account.getOld_balance()+money,2));//可转金额增加
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean thawAccount2(Account a, double money, AccountDeal ad) {
		Account account = this.selectById(a.getId());
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()-money,3));//冻结金额减少，可用金额不变(相当于扣除费用了)
		try {
			this.update(account);
			this.accountDealService.insert(ad);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	//为某账户增加金额：如充值，还款，签约，融资确认等等，充值需要审核时调用此方法
	@Override
	@Transactional
	public boolean addMoney(Account a, double money) throws StaleObjectStateException,EngineException,Exception {
		Account account = this.selectById(a.getId());
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()+money,3));
		this.update(account);
		return true;
	}
	
	@Override
	@Transactional
	public boolean addMoney_oldbalance(Account a, double money) {
		Account account = this.selectById(a.getId());
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()+money,2));
		account.setOld_balance(DoubleUtils.doubleCheck(account.getOld_balance()+money,2));//oldbalance累加
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//为某账户增加金额及冻结金额；交割时用到了此方法
	@Override
	@Transactional
	public boolean addMoney(Account a, double money,double djmoney) {
		Account account = this.selectById(a.getId());
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()+money,3));
		account.setFrozenAmount(DoubleUtils.doubleCheck(account.getFrozenAmount()+djmoney,3));
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//为某账户减少金额：如提现
	@Override
	@Transactional
	public strictfp boolean loseMoney(Account a, double money) {
		Account account = this.selectById(a.getId());
		if(money>account.getBalance()){
			System.out.println("loseMoney:账户减钱出现负值了。--------------------");
			return false;
		}
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()-money,3));
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	@Transactional
	public boolean loseMoney_oldbalance(Account a, double money) {
		Account account = this.selectById(a.getId());
		if(money>account.getBalance()){
			System.out.println("loseMoney:账户减钱出现负值了。--------------------");
			return false;
		}
		account.setBalance(DoubleUtils.doubleCheck(account.getBalance()-money,3));
		account.setOld_balance(DoubleUtils.doubleCheck(account.getOld_balance()-money,3));//oldbalance累减
		try {
			this.update(account);
			return true;
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public HashMap<String,Object> activityWithdrawAllow(final User user,double money) { 
		HashMap<String, Object> result = new HashMap<String, Object>();
		float activiti_amount = 0f;
		boolean expire  = false;
		
		if(activiti_amount==0 || expire){
			result.put("boolean", true);
		}else{
			double invest = DoubleUtils.doubleCheck(this.cyzq(user.getUsername()), 2);
			double balance = user.getUserAccount().getBalance();
			double frozen = user.getUserAccount().getFrozenAmount();
			//待审核的提现及商转银
			double tx = this.accountDealService.noCheck_cash(user.getUserAccount());
			if(money > invest + balance + frozen - tx - activiti_amount){
				result.put("boolean", false);
				double amount = DoubleUtils.doubleCheck(invest + balance + frozen - tx - activiti_amount,2);
				result.put("surplus", amount<0?0:amount);//可提款额
				result.put("frozen", activiti_amount);
			}else{
				result.put("boolean", true);
			}
		}
		return result;
	}
	
	//资金冻结，资金解冻，审核通过调用
	//资金冻结:变更状态即可,申请冻结时已经冻结了
	//资金解冻:解冻金额再变更状态,申请解冻时没有解冻
	@Override
	@Transactional
	public HxbankVO frozen_thaw_pass(AccountDeal ad,User checkUser){
		HxbankVO vo = new HxbankVO();
		AccountDeal ad_new = this.accountDealService.selectById(ad.getId());
		boolean f = false;
		try {
			Account a = this.selectById(ad_new.getAccount().getId());
			if("资金冻结".equals(ad_new.getType())){
				if("30".equals(ad_new.getCheckFlag())){
					ad_new.setCheckFlag("31");//通过
					ad_new.setCheckDate(new Date());
					ad_new.setCheckUser(checkUser);
					ad_new.setSuccessFlag(true);
					ad_new.setSuccessDate(ad_new.getCheckDate());
					this.accountDealService.update(ad_new);
					vo.setFlag(true);
					vo.setTip("操作成功");
				}else{
					vo.setFlag(false);
					vo.setTip("审核驳回失败，非待审核状态，请勿重复审核。");
				}
			}
			if("资金解冻".equals(ad_new.getType())){
				if("33".equals(ad_new.getCheckFlag())){
					if(ad_new.getMoney()<=a.getFrozenAmount()){
						f = this.thawAccount1(a, ad_new.getMoney());
						if(f){
							ad_new.setCheckFlag("34");//通过
							ad_new.setCheckDate(new Date());
							ad_new.setCheckUser(checkUser);
							ad_new.setSuccessFlag(true);
							ad_new.setSuccessDate(ad_new.getCheckDate());
							this.accountDealService.update(ad_new);
						}
						vo.setFlag(true);
						vo.setTip("操作成功");
					}else{
						vo.setFlag(false);
						vo.setTip("操作失败，交易账号冻结金额不足。");
					}
				}else{
					vo.setFlag(false);
					vo.setTip("审核驳回失败，非待审核状态，请勿重复审核。");
				}
			}
		} catch (Exception e) {
			vo.setFlag(false);
			vo.setTip("操作异常，请稍后再试。");
		}
		return vo;
	}
	
	//资金冻结，资金解冻，审核驳回调用
	//资金冻结:解冻金额再变更状态,申请冻结时已经冻结了
	//资金解冻:变更状态即可,申请解冻时没有解冻
	@Override
	@Transactional
	public HxbankVO frozen_thaw_nopass(AccountDeal ad,User checkUser){
		HxbankVO vo = new HxbankVO();
		AccountDeal ad_new = this.accountDealService.selectById(ad.getId());
		boolean f = false;
		try {
			Account a = this.selectById(ad_new.getAccount().getId());
			if("资金冻结".equals(ad_new.getType())){
				if("30".equals(ad_new.getCheckFlag())){
					if(ad_new.getMoney()<=a.getFrozenAmount()){
						f = this.thawAccount1(a, ad_new.getMoney());
						if(f){
							ad_new.setCheckFlag("32");//驳回
							ad_new.setCheckDate(new Date());
							ad_new.setCheckUser(checkUser);
							this.accountDealService.update(ad_new);
						}
						vo.setFlag(true);
						vo.setTip("操作成功");
					}else{
						vo.setFlag(false);
						vo.setTip("操作失败，交易账号冻结金额不足。");
					}
				}else{
					vo.setFlag(false);
					vo.setTip("审核驳回失败，非待审核状态，请勿重复审核。");
				}
			}
			if("资金解冻".equals(ad_new.getType())){
				if("33".equals(ad_new.getCheckFlag())){
					ad_new.setCheckFlag("35");//驳回
					ad_new.setCheckDate(new Date());
					ad_new.setCheckUser(checkUser);
					this.accountDealService.update(ad_new);
					AccountDeal duiying = this.accountDealService.selectById(ad_new.getAccountDeal().getId());
					duiying.setAccountDeal(null);
					this.accountDealService.update(duiying);
					vo.setFlag(true);
					vo.setTip("操作成功");
				}else{
					vo.setFlag(false);
					vo.setTip("审核驳回失败，非待审核状态，请勿重复审核。");
				}
			}
		} catch (Exception e) {
			vo.setFlag(false);
			vo.setTip("操作异常，请稍后再试。");
		}
		return vo;
	}
	//某用户的持有债券
	public double cyzq(String username){
		HashMap<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		inParamList.put(1, username);
		HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
		outParameter.put(2, Types.DOUBLE);
		HashMap<Integer, Object> results = this.callProcedureForParameters("PKG_sys_qingsuan.cyzq", inParamList, outParameter);
		if(null!=results&&results.size()==1){
			return Double.parseDouble(results.get(2).toString());
		}else{
			return 0d;
		}
	}
	
	@Override
	public HashMap<String,Object> investToLendAllow(final User user,double money,double iphone) { 
		HashMap<String, Object> result = new HashMap<String, Object>();
		double activiti_amount = 0f;
		boolean expire  = false;
		InvestToLend lend = this.investToLendService.selectById("from InvestToLend where user.id = ?", new Object[]{user.getId()});
		if(lend!=null){
			activiti_amount = lend.getMoney();
			expire = lend.getEndDate().before(new Date());
			if(activiti_amount==0 || expire){
				result.put("boolean", true);
			}else{
				double invest = DoubleUtils.doubleCheck(this.cyzq(user.getUsername()), 2);
				double balance = user.getUserAccount().getBalance();
				double frozen = user.getUserAccount().getFrozenAmount();
				//待审核的提现及商转银
				double tx = this.accountDealService.noCheck_cash(user.getUserAccount());
				if(money > invest + balance + frozen - tx - activiti_amount - iphone){
					result.put("boolean", false);
					double amount = DoubleUtils.doubleCheck(invest + balance + frozen - tx - activiti_amount - iphone,2);
					result.put("surplus", amount<0?0:amount);//可提款额
					result.put("frozen", activiti_amount);
				}else{
					result.put("boolean", true);
				}
			}
		}else{//没有投转贷关联
			result.put("boolean", true);
		}
		return result;
	}
	
}