package com.kmfex.service;
 

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.hibernate.StaleObjectStateException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.kmfex.exceptions.PaymentRecordChangedException;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;
/**
 * 还款记录
 * @author eclipse
 * @author aora
 *<pre>
 * 2012-08-27 aora 修改此文件:增加selectByFanacingBase(String finacingBaseId)方法，以实现修改“电子合同汇总”页面中内容及样式
 *</pre>
 * 
 */
public interface PaymentRecordService extends BaseService<PaymentRecord>{ 
	
	/**
	 * 得到还款次数
	 * @param ir
	 * @return
	 * @throws Exception
	 */
	public Long getTotalMonth(InvestRecord ir) throws Exception;
	
	/**
	 * 返回融资项目对应的第一期还款记录
	 * @param finacingBaseId 融资项目id号
	 * @return 指定融资项目的第一期还款记录
	 * */
	public List<PaymentRecord> selectByFanacingBase(String finacingBaseId);
	
	/**
	 * 查取指定债权，指定期数的最大组别
	 * @param invest_id 投标记录ID
	 * @param succession 期数
	 * @return
	 */
	public int getMaxGroupNum(String invest_id,int succession);
	
	
	public List<PaymentRecord> queryListBySuccessionFromFinance(Serializable finance_id, int seccession);
	/**
	 * 某个投标的还款情况统计
	 * @param state 当前状态 0未还款 1正常还款 2延期还款(改为：提前还款) 3逾期还款
	 * @param invest_id 投标记录id
	 * @return
	 */
	public long totalPayMentRecord(String state,String invest_id);
	
	
	/**
	 * 取得指定融资项目的“每月应还本金”“每月应还利息”
	 * 
	 * @param financingbase_id
	 * @return
	 */
	public Map<String,Double> getBjLx(String financingbase_id);
	
	
	public int getPaymentedMaxSuccession(String financingbase_id);
	
	/**
	 * 删除指定融资项目下的所有{未还款}的还款记录
	 * @param fb
	 * @return
	 */
	public boolean terminal(FinancingBase fb);
	
	/**
	 * 还款批处理---预处理，将还款记录置为待审核
	 * @param fid
	 * @param qs
	 * @return
	 */
	public boolean changeToPreAudit(String fid,int qs,long date,String batch_no,User operator);
	/**
	 * 批量还款审核通过
	 * @param fid
	 * @param qs
	 * @param operator
	 * @return
	 */
	public int batchAudit(String fid,int qs,User operator)throws PaymentRecordChangedException,
	HibernateOptimisticLockingFailureException,
	StaleObjectStateException,
	EngineException,
	Exception;
	
	/**
	 * 批量还款
	 * @param fid
	 * @param records
	 * @param succession
	 * @param overdue_days
	 * @param dbdc
	 * @param operator
	 * @return result[0] 结果状态<br/>
	 *  result[1] 融资方帐户余额
	 */
	public int batchAudit(String fid, int [] succession, int overdue_days, boolean dbdc, JSONArray records, User operator) throws SQLException,HibernateOptimisticLockingFailureException, StaleObjectStateException, PaymentRecordChangedException, Exception;
	/**
	 * 批量还款审核驳回
	 * @param fid
	 * @param qs
	 * @return
	 */
	public boolean batchUnaudit(String fid,int qs);

	
	/**
	 * 发送还款短信
	 * @param bj 本金
	 * @param lx 利息
	 * @param fj 罚金
	 * @param fcode 项目编号
	 * @param balance 投资人帐户余额
	 * @param isdbdc 是否担保代偿
	 * @param telNo 投资人手机号
	 * @return
	 */
	public boolean sms(double bj,double lx, double fj, String fcode,double balance,boolean isdbdc,String telNo, double sxf);
	
	/**
	 * 取得指定融资项目 指定还款期次 正常还款所需资金总额
	 * @param fid
	 * @param qs
	 * @return 	1:成功<br/>
	 * 			0:其它异常<br/>
	 *			-1:余额不足
	 */
	public double getNeedMoneyWithPaymentRecord(String fid, int qs);
	
	

	/**
	 * 取得债权最后一次的还款日期
	 * @param ir
	 * @return
	 * @throws Exception
	 */
	public Date getLastRepayTime(InvestRecord ir) throws Exception;
}
