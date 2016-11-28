package com.kmfex.service.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.velocity.VelocityContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.Constant;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.BusinessType;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.ContractTemplate;
import com.kmfex.model.CoreAccountLiveRecord;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingContract;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.InvestRecordCost;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.model.PaymentRecord;
import com.kmfex.model.PreFinancingBase;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingContractService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.service.PreFinancingBaseService;
import com.kmfex.util.SMSNewUtil;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.VelocityUtils;

/**
 * 
 * @author  
 * @author aora
 *<pre>
 * 2012-08-15 aora 增加cancel(String financingBaseId)方法的实现，
 * 此方法用于取消指定的融资项目。
 * 2013-07-05 15:09     修改xyFinish方法   利息方式赋值
 * </pre>
 */
@Service
@Transactional
public class FinancingBaseImpl extends BaseServiceImpl<FinancingBase> implements FinancingBaseService {
	@Resource PreFinancingBaseService preFinancingBaseService;
	@Resource MemberBaseService memberBaseService;
	@Resource MemberGuaranteeService memberGuaranteeService;
	@Resource InvestRecordService investRecordService;
	@Resource ContractKeyDataService contractKeyDataService;
	@Resource FinancingCostService financingCostService;
	@Resource AccountService accountService;
	@Resource LogsService logsService;
	@Resource PaymentRecordService paymentRecordService;
	@Resource FinancingContractService financingContractService;
	@Resource AccountDealService accountDealService;
	@Resource ChargingStandardService chargingStandardService;
	@Resource CoreAccountService coreAccountService;
	

	@Override
	@Transactional
	public boolean getFinancingBase(String code) {
		String hql = "from FinancingBase f where f.code='" + code + "'";
		List<FinancingBase> ls = this.getCommonListData(hql);
		if (null != ls && ls.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public void noCheck(String financingBaseId, String code, String opeNote)
			throws Exception {
		// 历史记录修改状态为已经驳回
		PreFinancingBase tempPreFinancingBase = this.preFinancingBaseService
				.selectByHql(" from PreFinancingBase o where o.state=2 and o.code='"
						+ code + "'");
		tempPreFinancingBase.setState(4);
		tempPreFinancingBase
				.setOpeNote(tempPreFinancingBase.getOpeNote() != null ? tempPreFinancingBase
						.getOpeNote()
						+ opeNote
						: opeNote);
		this.preFinancingBaseService.update(tempPreFinancingBase);
		// 删除待审核的记录
		this.delete(financingBaseId);
	}
	@Transactional
	public void fabuBohui(String financingBaseId) throws Exception {
		// 历史记录修改状态为已经驳回
		FinancingBase tempFinancingBase = this.selectById(financingBaseId);
		tempFinancingBase.setState("0");  
		Logs log = logsService.log("发布时驳回");// 日志
		log.setEntityId(tempFinancingBase.getId());
		log.setEntityFrom("FinancingBase");
		this.logsService.insert(log);
		//tempFinancingBase.getLogs().add(log);// 日志
		tempFinancingBase.setModifyDate(new Date());
		this.update(tempFinancingBase); 
	}
	
	@Transactional
	public void xyFinish(User u, PreFinancingBase preFinancingBase,
			String financierId, String guaranteeId) throws Exception {

		try {
			MemberBase financier = null;
			MemberBase guarantee = null;
			if (BaseTool.isNotNull(financierId)) {
				financier = this.memberBaseService.selectById(financierId);
			}
			if (BaseTool.isNotNull(guaranteeId)) {
				guarantee = this.memberBaseService.selectById(guaranteeId);
			}

			if (null != preFinancingBase.getGuarantee()) {
				guarantee = preFinancingBase.getGuarantee();
			}

			if (!this.getFinancingBase(preFinancingBase.getCode())) {
				// 新增确认记录
				FinancingBase newFinancingBase = new FinancingBase();
				newFinancingBase
						.setBusinessType(preFinancingBase.getBusinessType());
				newFinancingBase.setFxbzState(preFinancingBase.getFxbzState());

				if (null != financier)
					newFinancingBase.setFinancier(financier);
				if (null != guarantee) {
					newFinancingBase.setGuarantee(guarantee);
					newFinancingBase.setMemberGuarantee(memberGuaranteeService
							.getLastByMemberGuarantee(guarantee.getId()));
				}

				newFinancingBase.setYongtu(preFinancingBase.getYongtu());
				newFinancingBase.setDkType(preFinancingBase.getDkType());//贷款分类
				newFinancingBase.setHyType(preFinancingBase.getHyType());//行业
				newFinancingBase.setQyType(preFinancingBase.getQyType());//企业类型(中型企业、小型企业、微型企业)

				newFinancingBase.setFinancier(preFinancingBase.getFinancier());
				newFinancingBase.setShortName(preFinancingBase.getShortName());
				newFinancingBase.setStartDate(preFinancingBase.getStartDate());
				newFinancingBase.setEndDate(preFinancingBase.getEndDate());
				newFinancingBase.setCreateDate(new Date());

				newFinancingBase.setPurpose(preFinancingBase.getPurpose());// 信用分析
				newFinancingBase.setPreMaxAmount(preFinancingBase.getMaxAmount());
				newFinancingBase.setMaxAmount(preFinancingBase.getPreMaxAmount());// 资额的金额
				newFinancingBase
						.setCurCanInvest(preFinancingBase.getPreMaxAmount());// 可融资额
				newFinancingBase.setFxbzState(preFinancingBase.getFxbzState());
				newFinancingBase.setNote(preFinancingBase.getNote());
				newFinancingBase.setCode(preFinancingBase.getCode());
				newFinancingBase.setRate(preFinancingBase.getRate());
				newFinancingBase.setGuaranteeNote(preFinancingBase
						.getGuaranteeNote());

				newFinancingBase.setQyzs(preFinancingBase.getQyzs());
				newFinancingBase.setFddbzs(preFinancingBase.getFddbzs());
				newFinancingBase.setCzzs(preFinancingBase.getCzzs());
				newFinancingBase.setDbzs(preFinancingBase.getDbzs());
				newFinancingBase.setZhzs(preFinancingBase.getZhzs());

				newFinancingBase.setQyzsNote(preFinancingBase.getQyzsNote());
				newFinancingBase.setFddbzsNote(preFinancingBase.getFddbzsNote());
				newFinancingBase.setCzzsNote(preFinancingBase.getCzzsNote());
				newFinancingBase.setDbzsNote(preFinancingBase.getDbzsNote());
				newFinancingBase.setZhzsNote(preFinancingBase.getZhzsNote());
				newFinancingBase.setContractTemplate(preFinancingBase.getContractTemplate());
				newFinancingBase.setCreateBy(preFinancingBase.getCreateBy());
				newFinancingBase.setCreateDate(new Date());
				newFinancingBase.setHyType(preFinancingBase.getHyType());
				if("day".equals(preFinancingBase.getBusinessType().getId())){ 
				 	newFinancingBase.setInterestDay(preFinancingBase.getInterestDay());//利息天数 
				}else{
					newFinancingBase.setInterestDay(0);//利息天数
					preFinancingBase.setInterestDay(0);
				} 


				this.insert(newFinancingBase);
				//newFinancingBase.getLogs().addAll(preFinancingBase.getLogs());// 日志转移
				
				List<Logs> logs =this.logsService.getCommonListData("from Logs o where o.entityFrom='PreFinancingBase' and o.entityId='"+preFinancingBase.getId()+"'" );
				Logs nl=null;
				for(Logs l:logs){ 
                	nl=new Logs();
                	nl.setIp(l.getIp());
                	nl.setTime(l.getTime());
                	nl.setOperator(l.getOperator());
                	nl.setOperate(l.getOperate());
                	nl.setEntityId(newFinancingBase.getId());
                	nl.setEntityFrom("FinancingBase");
                	this.logsService.insert(nl);
                }
                
				Logs log = this.logsService.log("信用确认");// 日志
				//newFinancingBase.getLogs().add(log);// 日志
				log.setEntityId(newFinancingBase.getId());
				log.setEntityFrom("FinancingBase");
				this.logsService.insert(log);	
				// 修改状态为已经确认
				preFinancingBase.setState(2);   
				preFinancingBaseService.update(preFinancingBase);		 
			}

		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public boolean cancel(String financingBaseId) throws Exception {
		/**
		 * 此操作涉及到的表有： FinancingBase, MemberBase, User, Account, ContractKeyData,
		 * FinancingCose, MemberBase, InvestRecord, InvestRecordCost。
		 * 通过FinancingBase的id号找到对应的InvestRecord，然后根据InvestRecord的id号找到
		 * 投资人(MemberBase)和InvestRecordCost，再根据投资人找到其对应的User，最后根据
		 * 此User找到其对应的Account。
		 * */
		boolean canceled = false;
		FinancingBase financingBase = this.selectById(financingBaseId);
		if (null != financingBase) {
			double state = Double.parseDouble(financingBase.getState());
			if ( 7 > state) {
				// 此为签约之前的融资项目
				if ( 6 == state) {
					// 融资项目确认之后才有融资费用
					try {
						// 删除融资方的融资费用记录
						FinancingCost financingCost = financingCostService
								.getCostByFinancingBase(financingBaseId);
						financingCostService.delete(financingCost.getId());
					} catch (EngineException e) {
						throw e;
					}
				}

				List<InvestRecord> investRecords = investRecordService
						.getInvestRecordListByFinancingId(financingBaseId);
				for (int i = 0; i < investRecords.size(); i++) {
					// 此融资项目已有投标记录
					InvestRecord investRecord = investRecords.get(i);
					// 投标人(投资人)
					MemberBase investor = investRecord.getInvestor();

					User memberUser = investor.getUser();
					Account userAccount = memberUser.getUserAccount();

					// 投标费用明细
					InvestRecordCost investRecordCost = investRecord.getCost();
					double amount = investRecordCost.getRealAmount();
					// 解冻投标人的账户冻结资金
					if (userAccount.getFrozenAmount() >= amount) {
						//把积分还原
						userAccount.setCredit(userAccount.getCredit()+investRecord.getCredit());
						this.accountService.update(userAccount);
						// 解冻投标人的账户冻结资金
						boolean thawed = accountService.thawAccount1(userAccount, amount);
						
					} else {
						System.out.println("无法为"
								+ userAccount.getUser().getUsername() + "的账户"
								+ userAccount.getAccountId() + "解冻" + amount
								+ "元。原因是需要解冻的金额大于冻结金额"
								+ userAccount.getFrozenAmount() + "元");
					}

					// 删除借款合同数据?
					// StringBuilder sqlHql = new StringBuilder(
					// "from ContractKeyData o where o.inverstrecord_id = '"
					// + investRecord.getId() + "'");
					// ContractKeyData contractKeyData =
					// contractKeyDataService
					// .selectByHql(sqlHql.toString());
					// contractKeyDataService.delete(contractKeyData.getId());

					// 删除投标记录?
					// investRecordService.delete(investRecord);

					// 修改投标记录的状态为“取消”
					investRecord.setState("3");
					investRecordService.update(investRecord);
					// 试图发送融资项目取消的短信通知该投资人，
					// 如果发送短信不成功则继续发短信通知下一个投资人
					// try {
					// noticeInvestor(investor, investRecord);
					// } catch (Exception e) {
					// System.err.println("不能发送融资项目取消的短信到投标人"
					// + memberUser.getUsername() + "的手机："
					// + investor.getMobile() + "！");
					// e.printStackTrace();
					// continue;
					// }
				}
				// 修改融资项目的状态为“取消”
				financingBase.setState("8");
				financingBase.setModifyDate(new Date());
				Logs logs = this.logsService.log("撤单");
				//financingBase.getLogs().add(logs);
				logs.setEntityId(financingBase.getId());
				logs.setEntityFrom("FinancingBase");
				this.logsService.insert(logs);
				this.update(financingBase);
				canceled = true;
			}
		}
		return canceled;
	}

	/**
	 * 发送融资项目撤单的短信通知投资人
	 * 
	 * @param investor
	 *            投资人会员
	 * @param investRecord
	 *            投资会员的投资记录
	 * */
	private void noticeInvestor(MemberBase investor, InvestRecord investRecord)
			throws Exception {
		FinancingBase financingBase = investRecord.getFinancingBase();
		VelocityContext context = new VelocityContext();
		SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
		context.put("boughtDate", format.format(investRecord.getCreateDate()));
		context.put("finacingCode", financingBase.getCode());
		context.put("canceledDate", format.format(new Date()));
		context.put("boughtAmount", String.format("%.2f", investRecord
				.getInvestAmount()));
		String content = "";
		if (MemberType.CODE_INVESTORS
				.equals(investor.getMemberType().getCode()))
			content = VelocityUtils.getVelocityString(context,
					"financing_canceled.html");

		System.out.println("content= " + content);
		SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
		SMSNewUtil.sms(investor.getMobile(), content.trim(), formats.format(new Date()), "","1");
		//老的短信接口不再使用2014-06-30
		//SMSUtil.sms(investor.getMobile(), content.trim());
	}

	@Override
	public Map<String[], Double> groupByBank(FinancingBase fb) {
		SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.openSession();
		Map<String[], Double> map = new HashMap<String[], Double>();
		String hql = "select sum(o.investAmount),o.investor.banklib.caption,o.investor.user.flag from InvestRecord o where o.state='2' and o.financingBase.id='"+fb.getId()+"' group by o.investor.user.flag,o.investor.banklib.caption";
		Query query = session.createQuery(hql);
		for (Iterator it = query.iterate(); it.hasNext();) {
			Object[] row = (Object[]) it.next();
			String[] m = {(String)row[1],(String)row[2]};
			//System.out.println((String)row[1]+(String)row[2]);
			map.put(m, (Double)row[0]);
		}
		session.close();
		return map;
	}
	
	@Transactional
	public boolean terminal(String fid){
		Logs log = null;
		FinancingBase fb = null;
		try {
			fb = this.selectById(fid);
			log = logsService.log("融资项目还款完成");
			//fb.getLogs().add(log);
			log.setEntityId(fb.getId());
			log.setEntityFrom("FinancingBase");
			this.logsService.insert(log);
			
			fb.setTerminal(true);
			fb.setTerminal_date(new Date());
			fb.setModifyDate(new Date());
			
			boolean investrecord_result = this.investRecordService.terminal(fb);
			//boolean paymentrecord_result = this.paymentRecordService.terminal(fb);
			
			this.update(fb);
			
		}catch (EngineException ee){
			ee.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}

	@Override
	public User getUserForFinancier(String fid) {
		FinancingBase fb = this.selectById(fid);
		MemberBase mb = fb.getFinancier();
		User user = mb.getUser();
		return user;
	}

	

	@Override
	public long[] queryForExpiredListCount(String subsql,Object[] args) {
		long[] result = new long[3];
		StringBuilder sql = new StringBuilder(" select sum(maxamount) maxamount,sum(curcaninvest) curcaninvest,sum(currenyamount) currenyamount from v_finance ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString(), args);
			if(row.next()){
				result[0] = row.getLong("maxamount");
				result[1] = row.getLong("curcaninvest");
				result[2] = row.getLong("currenyamount");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
		
	}

	@Override
	public List<Map<String, Object>> queryLogs(String id) {
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select sys_logs.*,sys_user.realname,sys_user.username from sys_logs,sys_user where   sys_logs.entityfrom='FinancingBase' and sys_logs.entityid='"+id+"' and sys_logs.operator_id = sys_user.id order by time");
		return list; 
	}

	@Override
	@Transactional
	public void sign(Serializable id) {
		try {
			Date now = new Date();
			Date day_model_value = null;
			
			
			
			
			// 状态改为已经签约
			FinancingBase financingBase = this.selectById(id);
			FinancingCost financingcost = this.financingCostService.getCostByFinancingBase(financingBase.getId());
			ContractTemplate ctemplate = financingBase.getContractTemplate();
			if ("7".equals(financingBase.getState())) return;
			
			BusinessType businesstype = financingBase.getBusinessType();
			
			if("day".equals(businesstype.getId())){ 
				financingBase.setDaoqiDate(DateUtils.getAfter(now, financingBase.getInterestDay())); 
			}else{
				financingBase.setDaoqiDate(DateUtils.getAfterMonth(now, businesstype.getTerm()));
			} 
			
			
			//2014年5月4日后，可删除下面这段代码
			//判断是否可以启用“收取交易手续费”
			Date audiodate = financingBase.getAuditDate();
			SimpleDateFormat ii_fee_4_date_sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			Date ii_fee_4_date = ii_fee_4_date_sdf.parse("20140504000000");
			boolean ii_fee_4_date_available = audiodate.after(ii_fee_4_date);
			if(!ii_fee_4_date_available) financingBase.setIi_fee_bl(0d);
			//2014年5月4日后，可删除上面这段代码
			
			// 得到融资项目下所有投标记录
			List<InvestRecord> investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord i where i.financingBase.id='" + financingBase.getId() + "' order by i.createDate asc", new String[] {});

			// 还款次数
			int returntimes = businesstype.getReturnTimes();
			
			// 利率
			double rate = financingBase.getRate();// 利率
			//交易手续费比例
			double ii_fee_bl = financingBase.getIi_fee_bl();

			Calendar time = Calendar.getInstance();;
			time.set(Calendar.HOUR_OF_DAY, 0);
			time.set(Calendar.MINUTE, 0);
			time.set(Calendar.SECOND, 0);
			
			double rate_of_day = rate / (double) 100 / (double)Constant.YEAR_DAY ;
			double rate_of_month = rate / (double) 100 / (double)12 ;
			

			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 生成还还款记录
			
			
			for (InvestRecord record : investRecords) {
				
				
				
				double principal_all = record.getInvestAmount();
				double principal = 0,interest = 0;
				long beneficiary_id = record.getInvestor().getUser().getId();
				
				double pmt_sum = 0;
				
				//此处要注意啦 20130304修改
				//按银行的贷款公式进行计算，区别之前的本息计算方式
				//pmt表示excel中的pmt公式
				//下面两个变量只有在 还款方式为PMT的时候才有效
				if(businesstype.getBranch() == 4){
					double parameter_2 = Math.pow(( 1 + rate_of_month ),returntimes);
					pmt_sum = Math.round( (( principal_all * rate_of_month * parameter_2 ) / ( parameter_2 - 1 )) * 100d )/100d;
				}
				
				
				
				for (int x = 1; x <= returntimes; x++) {
					// 小于等于三个月的要区分开
					if (returntimes == 1) {
						if("day".equals(businesstype.getId())){
							time.add(Calendar.DATE, financingBase.getInterestDay());
							//按日还款 还款日 跨周末  20140714 注释
//							if("G".equals(ctemplate.getCode())){
//								if(time.get(Calendar.DAY_OF_WEEK)==7){
//									time.add(Calendar.DATE, 2);
//								}else if(time.get(Calendar.DAY_OF_WEEK)==1){
//									time.add(Calendar.DATE, 1);
//								}
//							}
							day_model_value = time.getTime();
							
						}else{
							time.add(Calendar.MONTH, businesstype.getTerm());
						}
					} else {
						time.add(Calendar.MONTH, x);
					}
					
					
					
					PaymentRecord pr = new PaymentRecord();
					pr.setInvestRecord(record);
					pr.setLive(true);
					pr.setState(0);
					pr.setPredict_repayment_date(time.getTime());// 还款时间
					pr.setBeneficiary_id(beneficiary_id);
					if (x == 1) {
						record.setXyhkr(time.getTime());
					}
					
					// 计算还款额
					/*
					 * 还款模式
					 * 1:{到期一次还本付息}
					 * 2:{按月等本等息} 
					 * 3:{按月还息到期还本}
					 * 4:{按月等额本息}
					 */
					if( businesstype.getBranch() == 1 || businesstype.getBranch() == 2){
						principal = Math.round((principal_all / returntimes) * 100) / 100.0;// 应还本金
						if("day".equals(businesstype.getId())){//按日计息  
							businesstype.setTerm(1);
							int day=financingBase.getInterestDay();
							
							// 等息-利息计算公式
							// 利息 = 借款金额 * 日利率 * 借款期限 / 还款次数
							interest = Math.round((principal_all * rate_of_day * day ) * 100) / 100.0;
						}else{
							interest = Math.round((principal_all * rate_of_month) * 100) / 100.0;
						}
						
					}else if(businesstype.getBranch() == 3){//3:{按月还息到期还本}
						interest = Math.round((principal_all * rate_of_month) * 100) / 100.0;
						if(x == returntimes){
							principal = record.getInvestAmount();
						}else{
							principal = 0;
						}
					}else if(businesstype.getBranch() == 4){
						if(x == returntimes){
							interest = pmt_sum - principal_all;
							principal = principal_all;
						}else{
							interest = Math.round((principal_all * rate_of_month) * 100) / 100.0;
							principal = Math.round((pmt_sum - interest) * 100) / 100.0;
							principal_all -= principal;
						}
					}
					
					
					
					
					pr.setXybj(principal);// 本金
					pr.setXylx(interest);// 利息
					
					//20140423 新增交易手续费 在签约时写到 还款记录的 协议收费字段上。
					if(ii_fee_bl > 0d){
						double xy_fee_4 = interest * ii_fee_bl;
						if( xy_fee_4 >= 0.01d ){
							xy_fee_4 = Math.round( xy_fee_4 * 100d ) / 100d;
							pr.setXy_fee_4( xy_fee_4 );
						}
					}
					
					pr.setSuccession(x);
					this.paymentRecordService.insert(pr);
					time.setTime(now);
					time.set(Calendar.HOUR_OF_DAY, 0);
					time.set(Calendar.MINUTE, 0);
					time.set(Calendar.SECOND, 0);
				}
				
				record.setZqSuccessDate(now);
				record.setOrgcode(record.getInvestor().getOrgNo());
				this.investRecordService.update(record);
			}
			
			

			// 合同处理
			List<ContractKeyData> con = this.financingContractService.getContractList(id.toString());
			double interest_allah = 0d;
			double principal_allah = 0d;
			double principal_allah_monthly = 0d;
			double interest_allah_monthly = 0d;
			double repayment_amount_monthly_allah = 0d;
			int loan_term = 0;
			Date today = new Date();
			Date startDate = today;
			Date endDate = null;
			if (null != con && con.size() > 0) {
				for (ContractKeyData c : con) {
					loan_term = c.getLoan_term();
					interest_allah += c.getInterest_allah();
					principal_allah += c.getLoan_allah();
					principal_allah_monthly += c.getPrincipal_allah_monthly();
					interest_allah_monthly += c.getInterest_allah_monthly();
					repayment_amount_monthly_allah += c.getRepayment_amount_monthly_allah();
					c.setFinancier_make_sure(today);
					c.setStart_date(startDate);
					
					if("day".equals(businesstype.getId())){
						endDate = day_model_value;
					}else{
						endDate = DateUtils.getAfterMonth(startDate, loan_term);
					}
					SimpleDateFormat sdf = new SimpleDateFormat("dd");
					c.setRepayment_term(Integer.parseInt(sdf.format(endDate)));
					c.setEnd_date(endDate);
					
					this.contractKeyDataService.update(c);
				}
			}
			FinancingContract fc = new FinancingContract();
			fc.setFinancingBase(financingBase);
			fc.setBj(principal_allah);
			fc.setLx(interest_allah);
			fc.setBj_monthly(principal_allah_monthly);
			fc.setLx_monthly(interest_allah_monthly);
			fc.setRepayment_monthly(repayment_amount_monthly_allah);
			fc.setLoan_term(loan_term);
			fc.setCreateDate(today);
			fc.setStartDate(startDate);
			
			fc.setEndDate(endDate);
			
			this.financingContractService.insert(fc);

			// 计算债权，更新冻结金额
			int seccond = 1;
			for (InvestRecord r : investRecords) {
				seccond += 1;
				r.setState("2");
				Account ac = this.accountService.selectById(r.getInvestor().getUser().getUserAccount().getId());
				// 投资人账户解冻
				double sss = 0.0;
				 
				if(financingBase.getCode().startsWith("X")){
					sss = r.getCost().getFee1();
				} else {
					sss = r.getCost().getTzfwf();
				}
				double money = r.getInvestAmount() + sss-r.getCredit();
				AccountDeal ad = this.accountDealService.accountDealRecord(AccountDeal.SGHC, "11", money);
				ad.setBj(r.getInvestAmount());//投标额本金
				ad.setLx(sss);//投资服务费
				ad.setAccount(ac);
				ad.setUser(ac.getUser());
				ad.setPreMoney(ac.getBalance() + ac.getFrozenAmount());
				ad.setNextMoney(ad.getPreMoney() - ad.getMoney());
				ad.setCreateDate(DateUtils.getAfterSeccond(now, seccond));
				ad.setFinancing(financingBase);
				ad.setBusinessFlag(8);
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCreateDate());
				ad.setSignBank(ac.getUser().getSignBank());//签约行
				ad.setSignType(ac.getUser().getSignType());//签约类型
				ad.setTxDir(2);//交易转账方向
				ad.setChannel(ac.getUser().getChannel());//手工专户
				this.accountService.thawAccount2(ac, money, ad);

				// 更新债权出成本价
				r.setZqzrState(InvestRecord.STATE_SUCCESS);// 签约 债权状态改为挂牌中
				CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
				CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
				double fee = (sxf.getPercent() / 100) * r.getInvestAmount();
				double taxes = (sf.getPercent() / 100) * r.getInvestAmount();
				r.setCbj(r.getInvestAmount() + fee + taxes);// 计算成本价
				r.setLastDate(time.getTime());
				r.setZqSuccessDate(new Date());
				this.investRecordService.update(r);
				// 计算合同各种费用
				contractKeyDataService.contractKeyDataFeeUpdate(r.getId(),financingcost);
			}

			// 签约时就交割
			FinancingCost cost = financingCostService.getCostByFinancingBase(financingBase.getId());
			User rzr_user = cost.getFinancier().getUser();
			Account rzr_account = cost.getFinancier().getUser().getUserAccount();
			long rid = rzr_account.getId();
			double money = cost.getRealAmount();// 此金额转到融资方可用余额上
			double bzj = cost.getBzj();// 保证金，此金额转到融资方的冻结金额上
			this.accountDealService.rzkjg(money, bzj, id.toString(), rid);
			
			//对中心账户进行记录补加，内容为 融资项目一次性收取的几项费用
			//中心帐户在“融资交割”行为中，记录的划出费用为融资项目的全部融资额，没有扣除费用，流程需要，在这里进行补录
			//s
			
			List<CoreAccountLiveRecord> calrs = new ArrayList<CoreAccountLiveRecord>();
			if(cost.getFinancingBase().getCode().startsWith("X")){//兴易贷的包，费用为cost里的fee1
				
				//风险管理费--一次性收取
				if(cost.getFee1() != 0 && cost.getFee1_tariff() == 0){
					CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
					calr.setAction(21);
					calr.setFbase(cost.getFinancingBase());
					calr.setObject_(rzr_account);
					calr.setOperater(rzr_user);
					calr.setValue(cost.getFee1());
					calr.setAbs_value(Math.abs(calr.getValue()));
					calr.setTariff(0);
					calrs.add(calr);
				}
				//融资服务费 --一次性收取
				if(cost.getFee2() != 0 && cost.getFee2_tariff() == 0){
					CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
					calr.setAction(22);
					calr.setFbase(cost.getFinancingBase());
					calr.setObject_(rzr_account);
					calr.setOperater(rzr_user);
					calr.setValue(cost.getFee2());
					calr.setAbs_value(Math.abs(calr.getValue()));
					calr.setTariff(0);
					calrs.add(calr);
				}
				
				//担保费  --一次性收取
				if(cost.getFee3() != 0 && cost.getFee3_tariff() == 0){
					CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
					calr.setAction(23);
					calr.setFbase(cost.getFinancingBase());
					calr.setObject_(rzr_account);
					calr.setOperater(rzr_user);
					calr.setValue(cost.getFee3());
					calr.setAbs_value(Math.abs(calr.getValue()));
					calr.setTariff(0);
					calrs.add(calr);
				}
			}else{//其它包，补上除fee头的其它费用
				//一次性费用--担保费
				if(cost.getDbf()!= 0 && cost.getDbf_tariff() == 0){
					CoreAccountLiveRecord calr_dbf = new CoreAccountLiveRecord();//担保费
					calr_dbf.setAction(31);//非兴易贷费用-担保费
					calr_dbf.setFbase(cost.getFinancingBase());
					calr_dbf.setObject_(rzr_account);
					calr_dbf.setOperater(rzr_user);
					calr_dbf.setValue(cost.getDbf());
					calr_dbf.setAbs_value(Math.abs(calr_dbf.getValue()));
					calr_dbf.setTariff(0);
					calrs.add(calr_dbf);
				}
				
				//一次性费用--风险管理费
				if(cost.getFxglf()!= 0 && cost.getFxglf_tariff() == 0){
					CoreAccountLiveRecord calr_fxglf = new CoreAccountLiveRecord();//风险管理费
					calr_fxglf.setAction(32);//非兴易贷费用-风险管理费
					calr_fxglf.setFbase(cost.getFinancingBase());
					calr_fxglf.setObject_(rzr_account);
					calr_fxglf.setOperater(rzr_user);
					calr_fxglf.setValue(cost.getFxglf());
					calr_fxglf.setAbs_value(Math.abs(calr_fxglf.getValue()));
					calr_fxglf.setTariff(0);
					calrs.add(calr_fxglf);
				}
				
				//一次性费用-- 融资服务费
				if(cost.getRzfwf()!= 0 && cost.getRzfwf_tariff() == 0){
					CoreAccountLiveRecord calr_rzfwf = new CoreAccountLiveRecord();//融资服务费
					calr_rzfwf.setAction(33);//非兴易贷费用-融资服务费
					calr_rzfwf.setFbase(cost.getFinancingBase());
					calr_rzfwf.setObject_(rzr_account);
					calr_rzfwf.setOperater(rzr_user);
					calr_rzfwf.setValue(cost.getRzfwf());
					calr_rzfwf.setAbs_value(Math.abs(calr_rzfwf.getValue()));
					calr_rzfwf.setTariff(0);
					calrs.add(calr_rzfwf);
				}
			}
			//e
			
			
			//其他费用
			if(cost.getOther()!= 0 && cost.getOther_tariff() == 0){
				CoreAccountLiveRecord calr_Other = new CoreAccountLiveRecord(); 
				calr_Other.setAction(38);   
				calr_Other.setFbase(cost.getFinancingBase());
				calr_Other.setObject_(rzr_account);
				calr_Other.setOperater(rzr_user);
				calr_Other.setValue(cost.getOther());
				calr_Other.setAbs_value(Math.abs(calr_Other.getValue()));
				calr_Other.setTariff(0);
				calrs.add(calr_Other);
			}
			
			if(cost.getFee7()!= 0){
				CoreAccountLiveRecord calr_xwf = new CoreAccountLiveRecord();//席位费
				calr_xwf.setAction(36);//席位费
				calr_xwf.setFbase(cost.getFinancingBase());
				calr_xwf.setObject_(rzr_account);
				calr_xwf.setOperater(rzr_user);
				calr_xwf.setValue(cost.getFee7());
				calr_xwf.setAbs_value(Math.abs(calr_xwf.getValue()));
				calr_xwf.setTariff(0);
				calrs.add(calr_xwf);
			}
			if(cost.getFee10()!= 0){
				CoreAccountLiveRecord calr_xygl = new CoreAccountLiveRecord();//信用管理费
				calr_xygl.setAction(37);//信用管理费
				calr_xygl.setFbase(cost.getFinancingBase());
				calr_xygl.setObject_(rzr_account);
				calr_xygl.setOperater(rzr_user);
				calr_xygl.setValue(cost.getFee10());
				calr_xygl.setAbs_value(Math.abs(calr_xygl.getValue()));
				calr_xygl.setTariff(0);
				calrs.add(calr_xygl);
			}
			
			
			this.coreAccountService.insert(calrs);
			
			Logs log = logsService.log("签约提现");// 日志
			financingBase.setState("7");
			financingBase.setQianyueDate(now);
			financingBase.setModifyDate(now);//
			//financingBase.getLogs().add(log);// 日志
			log.setEntityId(financingBase.getId());
			log.setEntityFrom("FinancingBase");
			this.logsService.insert(log);
			
			this.update(financingBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void test(){
		Date now = null;
		
		//2014年5月4日后，可删除下面这段代码
		SimpleDateFormat ii_fee_4_date_sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date ii_fee_4_date = null;
		try {
			ii_fee_4_date = ii_fee_4_date_sdf.parse("20140504000000");
			now = ii_fee_4_date_sdf.parse("20140504000001");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean ii_fee_4_date_available = now.after(ii_fee_4_date);
		System.out.println(ii_fee_4_date_available);
		
	}
	public static void main(String[] args){
		FinancingBaseImpl.test();
	}
	

	
}
