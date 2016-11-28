package com.kmfex.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.velocity.VelocityContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kmfex.exceptions.PaymentRecordChangedException;
import com.kmfex.model.BusinessType;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.util.SMSNewUtil;
import com.kmfex.zhaiquan.service.SellingService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.VelocityUtils;

/**
 * 还款记录
 * 
 * @author eclipse
 * @author aora
 * 
 * <pre>
 * 2012-08-27 aora 修改此文件:增加selectByFanacingBase(String finacingBaseId)方法的实现，以实现修改“电子合同汇总”页面中内容及样式
 * 2013-07-10 15:09     按日计息 开始时间 结束时间
 * 2013-07-11 15:09     按日计息 利息计算
 * </pre>
 */
@Service
@Transactional
public class PaymentRecordImpl extends BaseServiceImpl<PaymentRecord> implements PaymentRecordService {
	

	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	AccountService accountService;
	@Resource
	AccountDealService accountDealService;
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource private SellingService sellingService;



	@Override
	public Long getTotalMonth(InvestRecord ir) throws Exception {
		return this.getScrollDataCount(" from PaymentRecord where investRecord.id = ? and state = '0' ", ir.getId());
	}

	@Override
	public List<PaymentRecord> selectByFanacingBase(String finacingBaseId) {
		FinancingBase financingBase = financingBaseService.selectById(finacingBaseId);
		if (null != financingBase) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(financingBase.getQianyueDate());
			BusinessType businessType = financingBase.getBusinessType();
			Date date = financingBase.getDaoqiDate();
			//
			if ("按月等额本息".equals(businessType.getReturnPattern())) {
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
				date = calendar.getTime();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String hql = "from PaymentRecord where investRecord.financingBase.id = ? and to_char(predict_repayment_date,'yyyy-mm-dd') = ? " + " order by predict_repayment_date,investRecord.investor.user.username";
			return this.getScrollDataCommon(hql, finacingBaseId, sdf.format(date));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getMaxGroupNum(final String invest_id, final int succession) {
		Object max = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session) throws HibernateException, SQLException {
				final String hql = "select max(o.group) from PaymentRecord o where o.investRecord.id = ? and o.succession = ?";
				final Query query = session.createQuery(hql);
				query.setString(0, invest_id);
				query.setInteger(1, succession);
				Iterator iter = query.iterate();
				Object result = iter.next();
				return result;
			}
		});
		return Integer.parseInt(max.toString());
	}
	/*
	 * 某个投标的还款情况统计
	 */
	public long totalPayMentRecord(String state,String invest_id){ 
		return getScrollDataCount("from PaymentRecord o where o.state='"+state+"' and o.investRecord.id='"+invest_id+"'");
	}
	
	@Override
	public List<PaymentRecord> queryListBySuccessionFromFinance(Serializable finance_id, int seccession) {
		return this.getScrollDataCommon("from PaymentRecord p where p.investRecord.financingBase.id = ? and p.succession = ? and p.group = 0 order by p.investRecord.investor.user.username", new Object[]{finance_id,seccession});
	}

	
	
	/**
	 * 
	 * 
	 */
	@Override
	public Map<String,Double> getBjLx(String financingbase_id) {
		Map<String,Double> result = new HashMap<String, Double>();
		double yuehuanbenjin = 0;
		double yuehuanlixi = 0;
		List<PaymentRecord> prs = this.getScrollDataCommon(" from PaymentRecord where investRecord.financingBase.id = ? and succession = 1 and group = 0", financingbase_id);
		for (PaymentRecord pr : prs) {
			yuehuanbenjin += pr.getXybj();
			yuehuanlixi += pr.getXylx();
		}
		result.put("yuehuanbenjin", yuehuanbenjin);
		result.put("yuehuanlixi", yuehuanlixi);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getPaymentedMaxSuccession(final String financingbase_id) {
		Object max = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session) throws HibernateException, SQLException {
				final String hql = "select max(o.succession) from PaymentRecord o where o.investRecord.financingBase.id = ? and o.state != 0";
				final Query query = session.createQuery(hql);
				query.setString(0, financingbase_id);
				Iterator iter = query.iterate();
				Object result = iter.next();
				if(result == null){
					return 1;
				}else{
					return result;
				}
			}
		});
		return Integer.parseInt(max.toString());
		
	}

	@Override
	public boolean terminal(FinancingBase fb) {
		final String sql = "from PaymentRecord p where p.investRecord.financingBase.id = ? and p.approve != 2";
		try {
			List<PaymentRecord> prs = this.getScrollDataCommon(sql, new Object[] { fb.getId() });
			for (PaymentRecord pr : prs) {
				this.delete(pr.getId());
			}
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean changeToPreAudit(String fid, int qs,long date,String batch_no,User operator) {
		final String hql = "from PaymentRecord p where p.investRecord.financingBase.id = ? and p.succession = ? ";
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		
		try{
			List<PaymentRecord> prs = this.getScrollDataCommon(hql, new Object[]{fid,qs});
			for(PaymentRecord pr : prs){
				String id = pr.getId();
				Object[] result = (Object[])(session.createSQLQuery("select v_p.rzfwf,v_p.dbf,v_p.fxglf from v_paymentrecord v_p where v_p.id = ?").setString(0, id).uniqueResult());
				double rzfwf = Double.parseDouble(result[0].toString());
				double dbf = Double.parseDouble(result[1].toString());
				double fxglf = Double.parseDouble(result[2].toString());
				pr.setFee_1(rzfwf);
				pr.setFee_2(dbf);
				pr.setFee_3(fxglf);
				pr.setApprove(1);
				pr.setState(1);
				pr.setOperator_id(operator.getId());
				pr.setBatch_no(batch_no);
				pr.setBatch_date(date);
				this.update(pr);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}
		return true;
	}

	@Override
	@Transactional
	public strictfp int batchAudit(String fid, int qs,User operator) throws PaymentRecordChangedException,
	HibernateOptimisticLockingFailureException,
	StaleObjectStateException,
	EngineException,
	Exception{
		final String hql = "from PaymentRecord p where p.investRecord.financingBase.id = ? and p.succession = ? and p.approve = 1";
		Date now = new Date();
		FinancingBase fb = this.financingBaseService.selectById(fid);
		User rzr = this.financingBaseService.getUserForFinancier(fid);
		Account r_account = this.accountService.selectByUserId(rzr.getId());
		
		double bj_all = 0d , lx_all = 0d , rzfwf_all = 0d, dbf_all = 0d , fj_all = 0d, rzfwf_fj_all = 0d, dbf_fj_all = 0d,fxglf_all = 0d, fxglf_fj_all = 0d;
		List<ArrayList<Object>> smslist = new ArrayList<ArrayList<Object>>();
		
		double ii_fee_bl = fb.getIi_fee_bl();
		double ss_fee_4_all = 0d;
		
		double nm = this.getNeedMoneyWithPaymentRecord(fid,qs);
		if(r_account.getBalance() < nm){
			return -1;//钱不够
		}
		try{
			List<PaymentRecord> prs = this.getScrollDataCommon(hql, new Object[] { fid, qs });
			for (PaymentRecord pr : prs) {
				
				
				if(pr.getActually_repayment_date() != null){
					throw new PaymentRecordChangedException("code:"+fb.getCode()+",qs:"+qs+",pr:"+pr.getInvestRecord());
				}
				
				InvestRecord ir = pr.getInvestRecord();
				User tzr = ir.getInvestor().getUser();
				Account t_account = tzr.getUserAccount();
				
				pr.setApprove(2);// 置为审核通过
				pr.setShbj(pr.getXybj());
				pr.setShlx(pr.getXylx());
				pr.setPaid_debt(pr.getShbj()+pr.getShlx());// 实际还款额
				pr.setActually_repayment_date(now);// 实际还款时间
				pr.setAuditor_id(operator.getId());//审核人
				pr.setBeneficiary_org_id(tzr.getOrg().getId());
				pr.setCash_pool(tzr.getChannel());
				
				
				if(ii_fee_bl > 0d){
					double ss_fee_4 = pr.getShlx() * ii_fee_bl;
					if( ss_fee_4 >= 0.01d ){
						ss_fee_4 = Math.round( ss_fee_4 * 100d ) / 100d;
						pr.setSs_fee_4( ss_fee_4 );
					}
				}
				
				
				// 以下代码更新投标记录中的，本金余额，利息余额，下次还款日期
				double bjye_temp = Math.round((ir.getBjye() - pr.getShbj()) * 100d) / 100d;
				double lxye_temp = Math.round((ir.getLxye() - pr.getShlx()) * 100d) / 100d;
				double bjye =  bjye_temp < 0d ? 0d : bjye_temp;
				double lxye =  lxye_temp < 0d ? 0d : lxye_temp;
				
				ir.setBjye(bjye);// 投标记录中的本金余额
				ir.setLxye(lxye);// 投标记录中的利息余额
				// 下次还款时间
				PaymentRecord pr_temp = this.selectById(" from PaymentRecord where investRecord.id = ? and state = 0 order by predict_repayment_date asc", new Object[] { ir.getId() });
				if (pr_temp != null) {
					ir.setXyhkr(pr_temp.getPredict_repayment_date());
				} else {
					ir.setXyhkr(null);
				}
				// 还款次数和最后还款时间
				ir.setMonths((this.getTotalMonth(ir)) + "");
				ir.setModifyDate(new Date());
				// 以上代码更新投标记录中的，本金余额，利息余额，下次还款日期
				
				// 更新债权出成本价
				CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
				CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
				double fee = (sxf.getPercent() / 100) * ir.getBjye();
				double taxes = (sf.getPercent() / 100) * ir.getBjye();
				ir.setCbj(ir.getBjye() + fee + taxes);// 计算成本价
				// 更新债权出成本价
				
				
				bj_all += pr.getShbj();
				lx_all += pr.getShlx();
				rzfwf_all += pr.getFee_1();
				dbf_all += pr.getFee_2();
				fxglf_all += pr.getFee_3();
				fj_all += pr.getPenal();
				rzfwf_fj_all += pr.getZhglf_fj();
				dbf_fj_all += pr.getDbf_fj();
				fxglf_fj_all += pr.getFee_3_fj();
				ss_fee_4_all += pr.getSs_fee_4();
				
				this.sellingService._cancel(ir.getId());
				int result = this.accountDealService.payToInvestor( fb, t_account, pr.getXybj(), pr.getXylx(), pr.getPenal(),pr.getSs_fee_4(), operator);
				if(result!=1) throw new Exception("还款失败");
				this.update(pr);
				this.investRecordService.update(ir);
				ArrayList<Object> smsobj = new ArrayList<Object>();
				smsobj.add( pr.getShbj());
				smsobj.add( pr.getShlx());
				smsobj.add( pr.getPenal());
				smsobj.add( fb.getCode());
				smsobj.add( t_account.getBalance());
				smsobj.add( false);
				smsobj.add( ir.getInvestor().getMobile());
				smsobj.add( pr.getSs_fee_4());
				smslist.add(smsobj);
			}
			/*
			 *批量扣款
			 */
			double [] tzr_kx = new double[]{bj_all, lx_all, fj_all};//投资人款项
			double [] fy_kx = new double[]{rzfwf_all + rzfwf_fj_all, dbf_all + dbf_fj_all,fxglf_all + fxglf_fj_all };//费用款项
			this.accountDealService.payFromFinancier( tzr_kx, fy_kx,ss_fee_4_all, r_account, fb, operator);
			
			for(ArrayList<Object> smsobj : smslist){
				try {
					this.sms(Double.parseDouble(smsobj.get(0).toString()), 
							Double.parseDouble(smsobj.get(1).toString()),
							Double.parseDouble(smsobj.get(2).toString()), 
							smsobj.get(3).toString(), 
							Double.parseDouble(smsobj.get(4).toString()), 
							Boolean.parseBoolean(smsobj.get(5).toString()), 
							smsobj.get(6).toString(),
							Double.parseDouble(smsobj.get(7).toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}catch(PaymentRecordChangedException e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return -3;
		}catch (EngineException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}catch (SQLException e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
			
		return 1;
	}

	
	
	@Override
	@Transactional
	public strictfp int batchAudit(String fid, int[] succession, int overdue_days, boolean dbdc,JSONArray records, User operator) throws SQLException,HibernateOptimisticLockingFailureException, StaleObjectStateException, PaymentRecordChangedException, Exception {
		int result = 0;
		Date now = new Date();
		StringBuffer successions = new StringBuffer();
		for (int x = 0; x < succession.length; x++) {
			if (x != 0) successions.append(",");
			successions.append(succession[x]);
		}
		String hql = "from PaymentRecord p where p.investRecord.id = ? and p.succession in (" + successions + ") and p.group = 0";
		double bj_all = 0d , lx_all = 0d , rzfwf_all = 0d, dbf_all = 0d , fj_all = 0d, rzfwf_fj_all = 0d, dbf_fj_all = 0d,fxglf_all = 0d, fxglf_fj_all = 0d;
		
		FinancingBase fb = this.financingBaseService.selectById(fid);
		User rzr = this.financingBaseService.getUserForFinancier(fid);
		Account r_account = this.accountService.selectByUserId(rzr.getId());
		List<ArrayList<Object>> smslist = new ArrayList<ArrayList<Object>>();
		double ii_fee_bl = fb.getIi_fee_bl();
		double ss_fee_4_all = 0d;
		try{
			for (int x = 0; x < records.size(); x++) {
				JSONObject object = records.getJSONObject(x);
				String ir_id = object.getString("investrecord_id");
				String remark = object.getString("remark");
				String remark2 = object.getString("remark2");
				double benjin = 0d, lixi = 0d, fajin = 0d, rongzifuwufei = 0d, rongzifuwufei_fajin = 0d, danbaofei = 0d, danbaofei_fajin = 0d, fengxianguanlifei = 0d, fengxianguanlifei_fajin = 0d;
				try {benjin = object.getDouble("benjin");} catch (JSONException e) {}
				try {lixi = object.getDouble("lixi");} catch (JSONException e) {}
				try {fajin = object.getDouble("fajin");} catch (JSONException e) {}
				try {rongzifuwufei = object.getDouble("rongzifuwufei");} catch (JSONException e) {}
				try {rongzifuwufei_fajin = object.getDouble("rongzifuwufei_fajin");} catch (JSONException e) {}
				try {danbaofei = object.getDouble("danbaofei");} catch (JSONException e) {}
				try {danbaofei_fajin = object.getDouble("danbaofei_fajin");} catch (JSONException e) {}
				try {fengxianguanlifei = object.getDouble("fengxianguanlifei");} catch (JSONException e) {}
				try {fengxianguanlifei_fajin = object.getDouble("fengxianguanlifei_fajin");} catch (JSONException e) {}
				
				
				double bj_temp = 0d , lx_temp = 0d , fj_temp = 0d;
				
				InvestRecord investrecord = this.investRecordService.selectById(ir_id);
				List<PaymentRecord> prs = this.getScrollDataCommon(hql, new Object[] { ir_id });
				User tzr = investrecord.getInvestor().getUser();
				Account t_account = tzr.getUserAccount();
				
				
				double ss_fee_4_temp = 0d;
				
				
				for (PaymentRecord pr : prs) {
					if (pr.getState() != 0) {
						pr = pr.getClone();
						pr.setBeneficiary_id(tzr.getId());
						int groupnum = this.getMaxGroupNum(pr.getInvestRecord().getId(), pr.getSuccession());
						pr.setGroup(++groupnum);
					}
					
					
					if(ii_fee_bl > 0d){
						double lx_fj = lixi + fajin;
						double ss_fee_4 = lx_fj * ii_fee_bl ;
						if(ss_fee_4 > 0.01d){
							ss_fee_4 = Math.round( ss_fee_4 * 100d )/100d;
							pr.setSs_fee_4(ss_fee_4);
						}
					}
					
					pr.setApprove(2);
					pr.setRemark(remark);
					pr.setRemark2(remark2);
					pr.setShbj(benjin);
					pr.setShlx(lixi);
					pr.setPenal(fajin);
					pr.setFee_1(rongzifuwufei);
					pr.setZhglf_fj(rongzifuwufei_fajin);
					pr.setFee_2(danbaofei);
					pr.setDbf_fj(danbaofei_fajin);
					pr.setFee_3(fengxianguanlifei);
					pr.setFee_3_fj(fengxianguanlifei_fajin);
					pr.setOverdue_days(overdue_days);
					pr.setActually_repayment_date(now);
					pr.setOperator_id(operator.getId());
					pr.setPaid_debt(pr.getShbj()+pr.getShlx()+pr.getPenal());// 实际还款额
					pr.setAuditor_id(operator.getId());//审核人
					pr.setCash_pool(tzr.getChannel());
					pr.setBeneficiary_org_id(tzr.getOrg().getId());
					
					
	
					if (dbdc == true) {
						pr.setState(4);
					} else {
						if (overdue_days < 0) {
							pr.setState(2);
						} else if (overdue_days > 0) {
							pr.setState(3);
						} else {
							pr.setState(1);
						}
					}
					
					
					
					
					// 以下代码更新投标记录中的，本金余额，利息余额，下次还款日期
					investrecord.setBjye(investrecord.getBjye() - pr.getShbj());// 投标记录中的本金余额
					investrecord.setLxye(investrecord.getLxye() - pr.getShlx());// 投标记录中的利息余额
					// 下次还款时间
					PaymentRecord pr_temp = this.selectById(" from PaymentRecord where investRecord.id = ? and state = 0 order by predict_repayment_date asc", new Object[] { investrecord.getId() });
					if (pr_temp != null) {
						investrecord.setXyhkr(pr_temp.getPredict_repayment_date());
					} else {
						investrecord.setXyhkr(null);
					}
					// 还款次数和最后还款时间
					investrecord.setMonths((this.getTotalMonth(investrecord)) + "");
					investrecord.setModifyDate(new Date());
					// 以上代码更新投标记录中的，本金余额，利息余额，下次还款日期
					
					// 更新债权出成本价
					CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
					CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
					double fee = (sxf.getPercent() / 100) * investrecord.getBjye();
					double taxes = (sf.getPercent() / 100) * investrecord.getBjye();
					investrecord.setCbj(investrecord.getBjye() + fee + taxes);// 计算成本价
					// 更新债权出成本价
					
					bj_all += pr.getShbj();
					lx_all += pr.getShlx();
					rzfwf_all += pr.getFee_1();
					dbf_all += pr.getFee_2();
					fj_all += pr.getPenal();
					rzfwf_fj_all += pr.getZhglf_fj();
					dbf_fj_all += pr.getDbf_fj();
					fxglf_all += pr.getFee_3();
					fxglf_fj_all += pr.getFee_3_fj();
					ss_fee_4_all += pr.getSs_fee_4();
					
					bj_temp += pr.getShbj();
					lx_temp += pr.getShlx();
					fj_temp += pr.getPenal();
					ss_fee_4_temp += pr.getSs_fee_4();
					
					//更新记录
					this.investRecordService.update(investrecord);
					this.update(pr);
				}
				
				this.sellingService._cancel(ir_id);
				if(bj_temp + lx_temp + fj_temp > 0){//如果实际还款额大于0，才发短信
					result = this.accountDealService.payToInvestor( fb, t_account, bj_temp, lx_temp, fj_temp,ss_fee_4_temp, operator);
					ArrayList<Object> smsobj = new ArrayList<Object>();
					smsobj.add( bj_temp);
					smsobj.add(lx_temp);
					smsobj.add( fj_temp);
					smsobj.add( fb.getCode());
					smsobj.add( t_account.getBalance());
					smsobj.add( false);
					smsobj.add( investrecord.getInvestor().getMobile());
					smsobj.add( ss_fee_4_temp);
					smslist.add(smsobj);
				}
	
			}
			
			/*
			 *批量扣款
			 */
			double [] tzr_kx = new double[]{bj_all, lx_all, fj_all};//投资人款项
			double [] fy_kx = new double[]{rzfwf_all + rzfwf_fj_all, dbf_all + dbf_fj_all,fxglf_all+fxglf_fj_all};//费用款项
			this.accountDealService.payFromFinancier( tzr_kx, fy_kx,ss_fee_4_all, r_account, fb, operator);
			
			
			for(ArrayList<Object> smsobj : smslist){
				try {
					this.sms(Double.parseDouble(smsobj.get(0).toString()), 
							Double.parseDouble(smsobj.get(1).toString()),
							Double.parseDouble(smsobj.get(2).toString()), 
							smsobj.get(3).toString(), 
							Double.parseDouble(smsobj.get(4).toString()), 
							Boolean.parseBoolean(smsobj.get(5).toString()), 
							smsobj.get(6).toString(),
							Double.parseDouble(smsobj.get(7).toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception("还款失败:"+e.getMessage());
		}
		return result;
	}


	@Override
	@Transactional
	public boolean batchUnaudit(String fid, int qs) {
		final String hql = "from PaymentRecord p where p.investRecord.financingBase.id = ? and p.succession = ? and p.approve = 1 ";
		try {
			List<PaymentRecord> prs = this.getScrollDataCommon(hql, new Object[] { fid, qs });
			for (PaymentRecord pr : prs) {
				pr.setApprove(0);
				pr.setState(0);
				pr.setBatch_no(null);
				pr.setBatch_date(0);
				this.update(pr);
			}
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean sms(double bj,double lx, double fj, String fcode,double balance,boolean isdbdc,String telNo, double sxf){
		try {
			if (bj + lx + fj > 0) {
				// 发送回款短信
				VelocityContext context = new VelocityContext();
				SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
				context.put("code", fcode);
				context.put("endDate", format.format(new Date()));
				context.put("amount", balance);
				if (isdbdc) {
					context.put("type", "(代偿)");
				} else {
					context.put("type", "");
				}
				context.put("money", DoubleUtils.doubleCheck(bj + lx + fj - sxf, 2));
				String content = "";
				content = VelocityUtils.getVelocityString(context, "tzr_huikuan.html");
				SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
				SMSNewUtil.sms(telNo, content, formats.format(new Date()), "","1");
				//老的短信接口不再使用2014-06-30
				//SMSUtil.sms(telNo, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


	@Override
	public double getNeedMoneyWithPaymentRecord(String fid, int qs) {
		final String sql = "select sum(v_p.yhbj + v_p.yhlx + v_p.rzfwf + v_p.dbf + v_p.fxglf) from v_paymentrecord v_p where v_p.financbaseid = ? and v_p.qs = ?";
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createSQLQuery(sql);
		query.setString(0, fid);
		query.setInteger(1, qs);
		Object result = query.uniqueResult();
		session.close();
		return Double.valueOf(result.toString());
	}
	
	
	
	@Override
	public Date getLastRepayTime(InvestRecord ir) throws Exception {
		Date result = null;
		PaymentRecord pr = this.selectByHql(" from PaymentRecord where investRecord.id = ? order by predict_repayment_date desc", ir.getId());
		if (pr != null) result = pr.getExtension_period() == null ? pr.getPredict_repayment_date() : pr.getExtension_period();
		return result;
	}
	
	
	
	
	
}

