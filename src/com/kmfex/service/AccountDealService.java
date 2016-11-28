package com.kmfex.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.StaleObjectStateException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.kmfex.JYRBVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.BankLibrary;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface AccountDealService extends BaseService<AccountDeal> {
	
	public AccountDeal accountDealRecord(String type,String checkFlag,double money);
	
	//产生充值记录
	public void chargeRecord(Account account,double money,User u,int businessflag);
	
	//产生充值记录,专用账户,目标账户
	public void chargeRecord_zyzh(Account zyzh,Account mbzh,double money,User u);
	
	//产生充值记录 导入时使用
	public void chargeRecord_import(Account account,double money,User u);
	
	public boolean cash(Account account,double money);//手工处理：提现
	/**
	 * 
	 * @param account 提现账户
	 * @param money   提现金额
	 * @param user    提现发起人
	 */
	public boolean cash(Account account, double money,User user);
	
	public boolean outGolden(Account account,double money,String type,String flag,BankLibrary bank);//三方接口：出金
	
	//投资款划入（中心账户加钱）
	//2013-01-24更改，金额分成:投资款，投资服务费/兴易贷标准的费用。
	//public void tzkhr(double money,double fee,FinancingBase fin);
	
	//融资款交割（中心账户减钱）
	public void rzkjg(double money,double bzj,String fid,long rid);
	
	
	
	//三方结算,third为三方账户，jsbl为结算比例
	public boolean sfjs(Account third,double jsbl);
	
	//产生保证金解冻记录
	public void bzjThaw(Account account,double money,User u);
	
	public void internalTransfer(Account out,Account in,double money,User u);
	
	//华夏银行出入金
	public void hxbank_in_out(Account account,double money,String type,String checkFlag);
	
	public void financeCash(long financer_id, double money,FinancingBase financingBase);
	
	
	
	/**
	 * 充值提现日结单-实现
	 * 
	 * @param org_code 授权中心编码
	 * @param type</br>可传参数:</br> 1投资充值</br>  2 融资还款充值</br>   3 投资提现</br>   4 融资提现
	 * @param count_key 笔数key 取出时用指定的key取出
	 * @param sum_key 金额key
	 * @return
	 */
	public HashMap<String,Object> recharge_kiting_today(String org_code,int type,String count_key, String sum_key,Date date);
	public List<JYRBVO> jyrb_extend(Date startDate,Date endDate,String orgs);
	
	/**
	 * 确认放款	
	 * 2013年10月23日，通知单编号JY20131018003
	 * 
	 * @param operator
	 * @param ad
	 * @throws EngineException 
	 */
	public void qrfk (User operator,String ad_id) throws EngineException,InvalidDataAccessApiUsageException,Exception;
	/**
	 * 驳回放款	
	 * 2013年10月23日，通知单编号JY20131018003
	 * 
	 * @param operator
	 * @param ad
	 * @param remark
	 * @throws EngineException 
	 */
	public void bhfk (User operator,String ad_id,String remark) throws EngineException,InvalidDataAccessApiUsageException,Exception;
	/**
	 * 确认划款
	 * @param fc
	 * @param operator
	 * @param ad
	 * @throws EngineException
	 */
	public void qrhk (FinancingCost fc,User operator,AccountDeal ad) throws EngineException;
	
	//根据回单号，找现金充值的业务流水
	public int getHuiDan(Account account,String huidan);
	
	
	
	
	/**
	 * 对投资人进行还款
	 * @param fb 融资项目
	 * @param r_Account 融资方帐户 
	 * @param t_Account 投资人帐户
	 * @param bj 本金
	 * @param lx 利息
	 * @param fj 罚金
	 * @param operator 操作人
	 * @return
	 */
	public int payToInvestor(FinancingBase fb, Account t_Account, double bj, double lx, double fj,double ss_fee_4,User operator) throws HibernateOptimisticLockingFailureException,StaleObjectStateException,EngineException,Exception;
	
	
	
	
	/**
	 * 从融资方帐户扣款进行还款
	 * 
	 * @param val 划到投资人帐户的钱
	 * @param val2 划到中心帐户的钱
	 * @param r_account
	 * @param fb
	 * @param operator 操作人
	 * @return -2 同步锁
	 * 
	 */
	public int payFromFinancier( double [] val, double [] val2,double ss_fee_4_all, Account r_account, FinancingBase fb,User operator) throws HibernateOptimisticLockingFailureException,StaleObjectStateException,EngineException,Exception;
	
	//批量导入利息
	public void lx_import(Account account,double money,User u);
	
	//兴易贷批量划拨
	public void xyd_import(Account account,double money,User u);
	
	
	//还款，融资方账户金额减少，投资人账户金额增加
	public boolean pay(String fid,long RID,long TID,double bj,double lx,double fj);
	
	
	//还款时扣除融资方的综合管理费，担保费至中心帐户
	public void zhglfAndDbf(double zhglf,double dbf,long RID,String fid,double zhglf_fj,double dbf_fj);
	
	//三方存管出入金:招行调用，银行发起出入金时调用此方法，出金和入金都不用审核
	public boolean in_out_bank(Account account,double money,String type,String checkFlag,String bkSerial,String coSerial);
	
	//三方存管出入金:招行调用，市场发起出入金时调用此方法，出金需要审核
	public boolean in_out_merchant(Account account,double money,String type,String checkFlag,String bkSerial,String coSerial);
	
	//判断某个账户当日是否有"银转商","商转银"的资金流水记录
	public boolean hasAccountDeal(Account account);
	
	//判断某个账户当日是否有待审核的资金流水记录
	public boolean hasNoCheck(Account account);
	
	//判断某个账户当日是否有流水号为bkSerial的资金流水
	public AccountDeal getBkSerial(Account account,String bkSerial);
	
	//0:贷金额 1:借金额
	public double[] dai_jie(String subsql,Object [] args);
	
	//0:充值金额 1:提现金额
	public double[] charge_cash(String subsql,Object[] args);
	
	//判断某个账户当日是否有待审核的提现及银转商流水的总额 
	public double noCheck_cash(Account account);
	public double XYD(String subsql);
	public double XYD(String subsql,Object[] args);
	
	public boolean checkOutGolden(AccountDeal ad,User u);
}
