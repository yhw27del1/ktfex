package com.wisdoor.core.service;
 
import java.util.HashMap;

import org.hibernate.StaleObjectStateException;

import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.AccountDeal;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;

/*** 
 * 用户帐号
 * @author   
 */
public interface AccountService  extends BaseService<Account>{
	public Account createAccount();
	public Account selectByAccountId(String accountId);
	public double queryBalance(Account account);
	public boolean enableAccount(Account account);//开通账户
	public boolean cancelAccount(Account account);//销户
	public Account selectByUserId(long userId);//取用户的账户信息，并计算债权
	public Account centerAccount();//取中心账户
	public Account thirdPartyAccount(String thirdParty);//取第三方账户
	
	//冻结某账户的金额,同时更新oldbalance
	public boolean frozenAccount_old(Account a,double money);
	
	//冻结某账户的金额
	public boolean frozenAccount(Account a,double money);
	
	//解冻某账户的金额
	//情形1:冻结金额减少，可用金额增加。
	public boolean thawAccount1(Account a,double money);
	
	//解冻某账户的金额
	//情形1:冻结金额减少，可用金额增加，可转金额增加。
	public boolean thawAccount_old(Account a,double money);
	
	//解冻某账户的金额
	//情形2:冻结金额减少，可用金额不变(相当于扣除费用了)，产生一条扣费记录AccountDeal。
	public boolean thawAccount2(Account a,double money,AccountDeal ad);
	
	//为某账户增加金额：如充值，还款，签约，融资确认等等，充值需要审核时调用此方法
	public boolean addMoney(Account a,double money) throws StaleObjectStateException,EngineException,Exception;
	
	//充值审核通过时调用此方法，账户加钱，oldbalance累加
	public boolean addMoney_oldbalance(Account a,double money);
	
	//为某账户增加金额及冻结金额；交割时用到了此方法
	public boolean addMoney(Account a,double money,double djmoney);
	
	//为某账户减少金额：如提现
	public boolean loseMoney(Account a,double money);
	
	//提现已划款操作时，账户减钱，oldbalance累减
	public boolean loseMoney_oldbalance(Account a,double money);
	
	public HashMap<String,Object> activityWithdrawAllow(User user,double money);
	
	//会员账户冻结、解冻,审核通过
	public HxbankVO frozen_thaw_pass(AccountDeal ad,User checkUser);
	
	//会员账户冻结、解冻,审核驳回
	public HxbankVO frozen_thaw_nopass(AccountDeal ad,User checkUser);
	
	//某用户的持有债券
	public double cyzq(String username);	
	
	
	//投转贷限制判定
	public HashMap<String,Object> investToLendAllow(User user,double money,double iphone);
}
