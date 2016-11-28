package com.kmfex.service.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.Constant;
import com.kmfex.MoneyFormat;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.ContractTemplate;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.InvestRecordCost;
import com.kmfex.model.MemberBase;
import com.kmfex.model.UserGroupRestrain1;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.CostCategoryService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.InvestRecordCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.UserGroupRestrain1Service;
import com.kmfex.webservice.vo.MessageTip;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.IpAddrUtil;
import com.wisdoor.core.utils.StringUtils;

/**
 * 
 * @author linuxp 2013-07-11 15:09   按日计息 利息计算
 */
@Service
public class InvestRecordImpl extends BaseServiceImpl<InvestRecord> implements InvestRecordService {
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	AccountService accountService;
	@Resource
	InvestRecordCostService investRecordCostService;
	@Resource
	CostCategoryService costCategoryService;// 收费项目
	@Resource
	ChargingStandardService chargingStandardService;// 收费明细
	@Resource
	ContractKeyDataService contractKeyDataService;// 合同关键数据
	@Resource
	FinancingRestrainService financingRestrainService;
	@Resource
	UserGroupRestrain1Service userGroupRestrain1Service;
	@Resource
	LogsService logsService;
	@Resource
	UserService userService;
	
	private BigDecimal rcode; 
	//自动投标 
	@Transactional
	public String[] investAuto(MemberBase member, double money, FinancingBase f, int credit) {
		String[] id = new String[2];
		Date now = new Date();
		try {
			Account account = member.getUser().getUserAccount();
			FinancingBase financingBase = this.financingBaseService.selectById(f.getId());
			if (financingBase.getCurCanInvest() == money) {// 当前投标额等于融资的剩余融资额，说明当前投标为最后一次投标
				financingBase.setState("4");// 已满标
			}
			financingBase.setHaveInvestNum(financingBase.getHaveInvestNum() + 1);// 投标人数加1
			financingBase.setCurrenyAmount(financingBase.getCurrenyAmount() + money);// 已融资额增加
			financingBase.setCurCanInvest(financingBase.getCurCanInvest() - money);// 剩余融资额减少
			financingBase.setModifyDate(new Date());
			Logs log = logsService.log("投标" + money + "元");
			//this.financingBase.getLogs().add(log);// 日志
			log.setEntityId(financingBase.getId());
			log.setEntityFrom("FinancingBase");
			this.logsService.insert(log);
			
			try {
				this.financingBaseService.update(financingBase);
			} catch (StaleObjectStateException e) {
				e.printStackTrace();
				return null;
			}
			 
			
			ContractKeyData contract = createContract(member, money, financingBase);
			this.contractKeyDataService.insert(contract);
			
			
			
			// 投标费率
			InvestRecordCost cost = new InvestRecordCost();
			cost.setCreateDate(now);
			String tzfwf = "";
			double p = 0.0;
			if (financingBase.getCode().startsWith("X")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				}
				p = contract.getInterest_allah() * (getPercentByCostBaseCode(tzfwf)/ 100);
				cost.setFee1(p);
			} else {
				tzfwf = "tzfwf";
				//优化2 			
				p = money * (getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费 
				cost.setTzfwf(p);  
			}
			
			
			//委托投标增值服务费
			Double autoinvestFee = money * (getPercentByCostBaseCode("autoinvest")/ 100);// 委托投标增值服务费
			cost.setFee2(autoinvestFee); 
			
			
			cost.setRealAmount(money + p+autoinvestFee);
			
			
			// 投标记录
			InvestRecord record = new InvestRecord();
			record.setInvestor(member);
			record.setCreateDate(now);
			record.setFinancingBase(financingBase);
			record.setPreAmount(account.getBalance());// 本次投标前，会员的账户余额
			record.setInvestAmount(money);// 本次投标额
			record.setCredit(credit);// 抵扣积分
			record.setNextAmount(account.getBalance() - money - credit - p);// 本次投标后，会员的账户余额
			record.setBjye(money);
			record.setOrgcode(member.getOrgNo());
			record.setState("1");
			record.setZhaiQuanCode(this.buildZqZrCode(financingBase));// 生成债权转让编码
			record.setZhaiQuanCode(financingBase.getCode());
			record.setFromApp(financingBase.getFromApp());// 记录是否来自手机端
			record.setMonths(financingBase.getBusinessType().getTerm() + "");// 还款次数
			record.setContract(contract);
			record.setLxye( contract.getInterest_allah() );
			
			
				// 账户冻结
			this.accountService.frozenAccount(account, money - credit + p);
			// 减少积分
			account.setCredit(account.getCredit() - credit);
			this.accountService.update(account);
			
			this.insert(record);
			cost.setInvestRecord(record);// 费率关联投标记录
			this.investRecordCostService.insert(cost);
			contract.setInverstrecord_id(record.getId());// 合同关联投标记录
			this.contractKeyDataService.update(contract);
			
			
			id[0] = record.getId();
			id[1] = cost.getId();
			
			//优化3 
			List<Map<String,Object>> list=null;
			if(financingBase.getPreInvest()==1){// 投标次数加1
				list=financingRestrainService.usergrouprestrainCheck(member.getUser().getUsername(),financingBase.getCode());
				if(!list.isEmpty()){ 
					if(list.get(0).get("ugpid") != null){
						UserGroupRestrain1 userGroupRestrain1 = userGroupRestrain1Service.selectById(list.get(0).get("ugpid").toString());
						if (null != userGroupRestrain1) {
							userGroupRestrain1.setInvestCount(userGroupRestrain1.getInvestCount() + 1);
							userGroupRestrain1Service.update(userGroupRestrain1);   
						}
					}  
				}   		 
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;// 并发问题--最后一个回滚(投标提示成功问题)
		}
		return id;
	}
	
	
	// 要更新融资项目的信息：state(3部分投标
	// 4已满标),haveInvestNum(已投标人数),curCanInvest(剩余融资额),currenyAmount(已融资额)
	// 要更新会员的账户余额
	// 成功：返回投标明细id，失败：返回null
	@Transactional
	public String[] invest(MemberBase member, double money, FinancingBase financingBase, int credit) {
		String[] id = new String[2];
		Date now = new Date();
		try {
			Account account = member.getUser().getUserAccount();
			/*FinancingBase financingBase = this.financingBaseService.selectById(f.getId());
			if (financingBase.getCurCanInvest() == money) {// 当前投标额等于融资的剩余融资额，说明当前投标为最后一次投标
				financingBase.setState("4");// 已满标
			}
			financingBase.setHaveInvestNum(financingBase.getHaveInvestNum() + 1);// 投标人数加1
			financingBase.setCurrenyAmount(financingBase.getCurrenyAmount() + money);// 已融资额增加
			financingBase.setCurCanInvest(financingBase.getCurCanInvest() - money);// 剩余融资额减少
			financingBase.setModifyDate(new Date());
			Logs log = logsService.log("投标" + money + "元", member.getUser());
			financingBase.getLogs().add(log);
			try {
				this.financingBaseService.update(financingBase);
			} catch (StaleObjectStateException e) {
				e.printStackTrace();
				return null;
			}
			*/
			
			ContractKeyData contract = createContract(member, money, financingBase);
			this.contractKeyDataService.insert(contract);
			
			
			
			// 投标费率
			InvestRecordCost cost = new InvestRecordCost();
			cost.setCreateDate(now);
			String tzfwf = "";
			double p = 0.0;
			if (financingBase.getCode().startsWith("X")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				}
				p = contract.getInterest_allah() * (getPercentByCostBaseCode(tzfwf)/ 100);
				cost.setFee1(p);
			} else {
				tzfwf = "tzfwf";
				//优化2 			
				p = money * (getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费 
				cost.setTzfwf(p);  
			}
			cost.setRealAmount(money + p);
			
			
			// 投标记录
			InvestRecord record = new InvestRecord();
			record.setInvestor(member);
			record.setCreateDate(now);
			record.setFinancingBase(financingBase);
			record.setPreAmount(account.getBalance());// 本次投标前，会员的账户余额
			record.setInvestAmount(money);// 本次投标额
			record.setCredit(credit);// 抵扣积分
			record.setNextAmount(account.getBalance() - money - credit - p);// 本次投标后，会员的账户余额
			record.setBjye(money);
			record.setOrgcode(member.getOrgNo());
			record.setState("1");
			//record.setZhaiQuanCode(this.buildZqZrCode(financingBase));// 生成债权转让编码
			record.setZhaiQuanCode(financingBase.getCode());
			record.setFromApp(financingBase.getFromApp());// 记录是否来自手机端
			record.setMonths(financingBase.getBusinessType().getTerm() + "");// 还款次数
			record.setContract(contract);
			record.setLxye( contract.getInterest_allah() );
			
			
		/*	// 账户冻结
			this.accountService.frozenAccount(account, money - credit + p);
			// 减少积分
			account.setCredit(account.getCredit() - credit);
			this.accountService.update(account);*/
			
			this.insert(record);
			cost.setInvestRecord(record);// 费率关联投标记录
			this.investRecordCostService.insert(cost);
			contract.setInverstrecord_id(record.getId());// 合同关联投标记录
			this.contractKeyDataService.update(contract);
			
			
			id[0] = record.getId();
			id[1] = cost.getId();
			
			//优化3 
			List<Map<String,Object>> list=null;
			if(financingBase.getPreInvest()==1){// 投标次数加1
				list=financingRestrainService.usergrouprestrainCheck(member.getUser().getUsername(),financingBase.getCode());
				if(!list.isEmpty()){ 
					if(list.get(0).get("ugpid") != null){
						UserGroupRestrain1 userGroupRestrain1 = userGroupRestrain1Service.selectById(list.get(0).get("ugpid").toString());
						if (null != userGroupRestrain1) {
							userGroupRestrain1.setInvestCount(userGroupRestrain1.getInvestCount() + 1);
							userGroupRestrain1Service.update(userGroupRestrain1);   
						}
					}  
				}   		 
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;// 并发问题--最后一个回滚(投标提示成功问题)
		}
		return id;
	}
 
	// 针对p_investInsert2.sql
	public MessageTip investNew(MemberBase member, double money, FinancingBase f, int credit) { 
		MessageTip tip = new MessageTip(true, "下单成功");
 
		try { 
			//1、准备值
			String tzfwf = "";
			double p = 0.0;
			List<Map<String,Object>> list=null;
			if (f.getCode().startsWith("X")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				} 
				// 收取利息中的百分比
				double bj_all = money;// 总本金
				double rate = f.getRate();// 利率  
				int fzTerm = 12;// 默认按12个月算利息
				int day = 1;
				if ("day".equals(f.getBusinessType().getId())) {// 按日计息
					f.getBusinessType().setTerm(1);
					fzTerm = (int) Constant.YEAR_DAY;
					day = f.getInterestDay();
				}
				int term = f.getBusinessType().getTerm(); 
				double lx_all = bj_all * ((rate / 100.00) / fzTerm * term * day);// 总利息 
				p = lx_all * (getPercentByCostBaseCode(tzfwf)/ 100);
				 
			} else {
				tzfwf = "tzfwf"; 
				p = money * (getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费   
			} 
			int fzTerm = 12;// 默认按12个月算利息
			int day = 1;
			if ("day".equals(f.getBusinessType().getId())) {// 按日计息
				f.getBusinessType().setTerm(1);
				fzTerm = (int) Constant.YEAR_DAY;
				day = f.getInterestDay();
			}
			
			Double lx=((double) f.getRate() / (double) 100) * (double) money * f.getBusinessType().getTerm() / fzTerm * day;
 
			String in_userGroupRestrain1ID="";
			if(f.getPreInvest()==1){ 
				list=financingRestrainService.usergrouprestrainCheck(member.getUser().getUsername(),f.getCode());
				if(!list.isEmpty()){ 
					if(list.get(0).get("ugpid") != null){
						UserGroupRestrain1 userGroupRestrain1 = userGroupRestrain1Service.selectById(list.get(0).get("ugpid").toString());
						if (null != userGroupRestrain1) {
							in_userGroupRestrain1ID=userGroupRestrain1.getId();
						}
					}  
				}   		 
			}   
			String in_IpStr=IpAddrUtil.getIpAddr(ServletActionContext.getRequest());  
            if("0:0:0:0:0:0:0:1".equals(in_IpStr)){
            	in_IpStr="127.0.0.1";
            }
            if(null==f.getFromApp()){
            	f.setFromApp("");
            }
            
			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			inParamList.put(1, member.getUser().getUsername());
			inParamList.put(2, f.getId());
			inParamList.put(3, in_IpStr);
			inParamList.put(4, money);
			inParamList.put(5, credit);
			inParamList.put(6, f.getFromApp());
			inParamList.put(7, in_userGroupRestrain1ID);
			inParamList.put(8, p);
			inParamList.put(9, p);
			inParamList.put(10, lx); 
	 
			Map<Integer, Integer> outParameter = new HashMap<Integer, Integer>(); 
			outParameter.put(11, Types.VARCHAR);
			outParameter.put(12, Types.VARCHAR);
			outParameter.put(13, Types.NUMERIC); 
			outParameter.put(14, Types.VARCHAR);
			 
			HashMap<Integer, Object> st = this.callProcedureForParameters("p_investInsert", inParamList, outParameter); 
	 
			BigDecimal code = (BigDecimal) st.get(13); 
		    if (code.intValue()<=0){
			   tip.setSuccess(false);
			   tip.setMsg((String)st.get(14));
		    }
			tip.setParam1((String) st.get(11));
			tip.setParam2((String) st.get(12));
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("投标异常"); 
		    return tip;  
		}
		return tip;
	}
	// 针对p_investInsert3.sql
	public MessageTip investNew2(MemberBase member, double money, FinancingBase f, int credit) { 
		MessageTip tip = new MessageTip(true, "下单成功");
		try {  
			//1、准备值
			String tzfwf = "";
			double p = 0.0;
			List<Map<String,Object>> list=null;
			InvestRecordCost cost = new InvestRecordCost();
			if (f.getCode().startsWith("X")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				} 
				// 收取利息中的百分比
				double bj_all = money;// 总本金
				double rate = f.getRate();// 利率  
				int fzTerm = 12;// 默认按12个月算利息
				int day = 1;
				if ("day".equals(f.getBusinessType().getId())) {// 按日计息
					f.getBusinessType().setTerm(1);
					fzTerm = (int) Constant.YEAR_DAY;
					day = f.getInterestDay();
				}
				int term = f.getBusinessType().getTerm(); 
				double lx_all = bj_all * ((rate / 100.00) / fzTerm * term * day);// 总利息 
				p = lx_all * (getPercentByCostBaseCode(tzfwf)/ 100);
				cost.setFee1(p);
			} else {
				tzfwf = "tzfwf";  
				p = money * (getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费 
				cost.setTzfwf(p); 
			} 
			int fzTerm = 12;// 默认按12个月算利息
			int day = 1;
			if ("day".equals(f.getBusinessType().getId())) {// 按日计息
				f.getBusinessType().setTerm(1);
				fzTerm = (int) Constant.YEAR_DAY;
				day = f.getInterestDay();
				
			}
			cost.setRealAmount(money + p);
			Double lx=((double) f.getRate() / (double) 100) * (double) money * f.getBusinessType().getTerm() / fzTerm * day;	
			
 
			ContractKeyData contract = createContract(member, money, f); 
			
			String in_userGroupRestrain1ID="";
			if(f.getPreInvest()==1){ 
				list=financingRestrainService.usergrouprestrainCheck(member.getUser().getUsername(),f.getCode());
				if(!list.isEmpty()){ 
					if(list.get(0).get("ugpid") != null){
						UserGroupRestrain1 userGroupRestrain1 = userGroupRestrain1Service.selectById(list.get(0).get("ugpid").toString());
						if (null != userGroupRestrain1) {
							in_userGroupRestrain1ID=userGroupRestrain1.getId();
						}
					}  
				}   		 
			}   
			
			String in_IpStr=IpAddrUtil.getIpAddr(ServletActionContext.getRequest());  
			if("0:0:0:0:0:0:0:1".equals(in_IpStr)){
				in_IpStr="127.0.0.1";
			}
			if(null==f.getFromApp()){
				f.setFromApp("");
			} 
			
			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			inParamList.put(1, member.getUser().getUsername());
			inParamList.put(2, f.getId());
			inParamList.put(3, in_IpStr);
			inParamList.put(4, money);
			inParamList.put(5, credit);
			inParamList.put(6, f.getFromApp());
			inParamList.put(7, in_userGroupRestrain1ID);
			inParamList.put(8, p);
			inParamList.put(9, p);
			inParamList.put(10, lx); 
			
			//投标记录参数
			inParamList.put(11, f.getBusinessType().getTerm()); 
			inParamList.put(12, member.getOrgNo()); 
			inParamList.put(13, member.getId()); 
			
			//合同参数
			inParamList.put(14, contract.getFirst_id()); 
			inParamList.put(15, contract.getFirst_party()); 
			inParamList.put(16, contract.getFirst_party_code()); 
			inParamList.put(17, contract.getInterest_allah_monthly()); 
			inParamList.put(18, contract.getInterest_rate()); 
			
			inParamList.put(19, contract.getPayment_method()); 
			inParamList.put(20, contract.getPrincipal_allah_monthly()); 
			inParamList.put(21, contract.getRepayment_amount_monthly_allah()); 
			
			inParamList.put(22, contract.getSecond_party()); 
			inParamList.put(23, contract.getSecond_party_code()); 
			inParamList.put(24, contract.getSecond_party_yyzz()); 
			inParamList.put(25, contract.getSecond_party_zzjg()); 
			 
            
			Map<Integer, Integer> outParameter = new HashMap<Integer, Integer>(); 
			outParameter.put(26, Types.VARCHAR);
			outParameter.put(27, Types.VARCHAR);
			outParameter.put(28, Types.NUMERIC); 
			outParameter.put(29, Types.VARCHAR);
			
			HashMap<Integer, Object> st = this.callProcedureForParameters("p_investInsert", inParamList, outParameter); 
			
			BigDecimal code = (BigDecimal) st.get(28); 
			if (code.intValue()<=0){
				tip.setSuccess(false);
				tip.setMsg((String)st.get(29));
			}
			tip.setParam1((String) st.get(26));
			tip.setParam2((String) st.get(27));
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("投标异常"); 
			return tip;  
		}
		return tip;
	}
	
		
	//事务存过控制，简化存过的调用方式 
	@Transactional
	public strictfp MessageTip investNew3(MemberBase member, double money, FinancingBase f, int credit) { 
		MessageTip tip = new MessageTip(true, "下单成功");
		try {  
			
			ContractKeyData contract = createContract(member, money, f); 
			//1、准备值
			String tzfwf = "";
			double p = 0.0;
			List<Map<String,Object>> list=null;
			if (f.getCode().startsWith("X")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				} 
				double lx_all = contract.getInterest_allah();
				p = lx_all * (getPercentByCostBaseCode(tzfwf)/ 100);
			} else {
				tzfwf = "tzfwf";  
				p = money * (getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费 
			} 
			
			
			
			
			String in_userGroupRestrain1ID="";
			if(f.getPreInvest()==1){ 
				list=financingRestrainService.usergrouprestrainCheck(member.getUser().getUsername(),f.getCode());
				if(!list.isEmpty()){ 
					if(list.get(0).get("ugpid") != null){
						UserGroupRestrain1 userGroupRestrain1 = userGroupRestrain1Service.selectById(list.get(0).get("ugpid").toString());
						if (null != userGroupRestrain1) {
							in_userGroupRestrain1ID=userGroupRestrain1.getId();
						}
					}  
				}   		 
			}   
			
			String in_IpStr=IpAddrUtil.getIpAddr(ServletActionContext.getRequest());  
			if("0:0:0:0:0:0:0:1".equals(in_IpStr)){
				in_IpStr="127.0.0.1";
			}
			if(null==f.getFromApp()){
				f.setFromApp("");
			} 
			Map map=this.callProcedureForInvestInsert(member.getUser().getUsername(), f.getId(), in_IpStr, money, credit, f.getFromApp(), in_userGroupRestrain1ID, 
					p, p, contract.getInterest_allah(), f.getBusinessType().getTerm(), member.getOrgNo(), member.getId(), contract.getFirst_id(), contract.getFirst_party(),
					contract.getFirst_party_code(), contract.getInterest_allah_monthly(), contract.getInterest_rate(), contract.getPayment_method(), 
					contract.getPrincipal_allah_monthly(), contract.getRepayment_amount_monthly_allah(), contract.getSecond_party(), 
					contract.getSecond_party_code(), contract.getSecond_party_yyzz(), contract.getSecond_party_zzjg());
			
			BigDecimal code = (BigDecimal)map.get("out_code"); 
			if (code.intValue()<=0){
				tip.setSuccess(false);
				tip.setMsg((String)map.get("out_msg"));
				return tip;  
			}
			tip.setParam1((String) map.get("out_recordId"));
			tip.setParam2((String) map.get("out_costId"));
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("投标异常"); 
			return tip;  
		}
		return tip;
	}
	
	
	
	 /**
	  * JDBC
	  * @param fields
	  * @param tables
	  * @param whstr
	  * @return
	  */
	public Double getPercentByCostBaseCode(String costBaseCode) { 
		List<Map<String,Object>> list = null;
		try {  
		    StringBuilder sb = new StringBuilder(); 
		    sb.append(" t.costbase_id=tb.id  ");
		    sb.append(" and   tb.code='"+costBaseCode+"' ");
			list = this.queryForList(" t.percent "," T_COST_ITEM t,t_cost_base tb ", sb.toString()); 
			if(!list.isEmpty()){ 
				  if(list.get(0).get("percent") != null){
				    return  Double.valueOf(list.get(0).get("percent").toString());
				}  
		   }  
		} catch (Exception e) { 
			e.printStackTrace();
			return 0d;
		}
		return 0d;  
	}
	
	
	/**
	 * 
	 * 
	 * @param
	 */
	public ContractKeyData createContract(MemberBase member, double money, FinancingBase financingBase) {
		// 合同关键数据
		ContractKeyData contract = new ContractKeyData();
		try {

			
			//这里查出来，是否为按日记息
			boolean is_day_calc = "day".equals(financingBase.getBusinessType().getId());
			//填写到合同里的利率，因为增加了日结包，所以有百分比和万分比的区别
			double rate_input = 0d;
			
			double rate_day = 0d;
			
			double rate_month =0d;
			
			double rate_year =0d;
			
			int day = 0 , month = 0;
			
			rate_year = financingBase.getRate();
			
			rate_day = rate_year / (double) 100 / (double)Constant.YEAR_DAY ;
			rate_month = rate_year / (double) 100 / (double)12 ;
			
			
			
			
			day = financingBase.getInterestDay();
			
			month = financingBase.getBusinessType().getTerm();
			
			// 0:{到期一次还本付息,按月等额本息},1:{按月还息到期还本}
			int branch = financingBase.getBusinessType().getBranch();
			
			double interest = 0d;
			
			double principal_all = money;
			double parameter_2 = Math.pow(( 1 + rate_month ),month);
			double pmt_sum = Math.round((( principal_all * rate_month * parameter_2 ) / ( parameter_2 - 1 ))*100d)/100d;
			
			if( branch == 1 || branch == 2){
				if(is_day_calc){//按日计息  
					// 等息-利息计算公式
					// 利息 = 借款金额 * 日利率 * 借款期限 / 还款次数
					interest = Double.parseDouble(String.format("%.2f",money * rate_day * day));
				}else{
					interest = Double.parseDouble(String.format("%.2f",money * rate_month * month ));
				}
				
			}else if(branch == 3){//3:{按月还息到期还本}
				interest = Double.parseDouble(String.format("%.2f",money * rate_month * month ));
			}else if(branch == 4){
				interest =  pmt_sum * month - money;
			}
			
			
			if(is_day_calc){
				rate_input = rate_day * 10000;
			}else{
				rate_input = financingBase.getRate();
			}
			
			if(Double.toString(interest).contains(".")){
				String interest_str = Double.toString(interest);
				String decimal = interest_str.substring(interest_str.indexOf(".") + 1);
			    if(Integer.parseInt(decimal) == 34){
			    	interest -= 0.01;
			    }
			}
			
/*			
			// 设置合同编号
			long count = this.getScrollDataCount("from InvestRecord where financingBase.id = ? and investor.id = ? and contract != null", new String[] { financingBase.getId(), member.getId() });
			String count_result = ++count < 10 ? ("0" + count) : count + "";

			// 编号
			contract.setContract_numbers(financingBase.getCode() + "-" + member.getUser().getUsername() + "-" + count_result);*/
			
			// 甲方信息
			contract.setFirst_party("0".equals(member.getCategory()) ? member.geteName() : member.getpName());
			contract.setFirst_id(member.getIdCardNo());
			contract.setFirst_party_code(member.getUser().getUsername());
			contract.setInvestor_make_sure(new Date());
			// 乙方信息
			contract.setSecond_party("0".equals(financingBase.getFinancier().getCategory()) ? financingBase.getFinancier().geteName() : financingBase.getFinancier().getpName());
			contract.setSecond_party_yyzz(financingBase.getFinancier().geteBusCode());
			contract.setSecond_party_code(financingBase.getFinancier().getUser().getUsername());
			contract.setSecond_party_zzjg(financingBase.getFinancier().geteOrgCode());

			// 融资信息
			contract.setLoan_allah(money);
			contract.setLoan_uppercase(MoneyFormat.format(String.format("%.2f", money), true));
			contract.setLoan_term(financingBase.getBusinessType().getTerm());
			contract.setInterest_rate(rate_input);
			contract.setPayment_method(branch);
			
			
			
			/*
			 * 
			 * 
			 * 20140304修改
			 * 
			 * 1:到期一次还本付息
			 * 2:按月等本等息
			 * 3:按月还息到期还本
			 * 4:按月等额本息
			 * 
			 * 
			 * 
			 */
			double inte_moth = Double.parseDouble(String.format("%.2f",(double)(interest / month)));
			if(((double)Math.round((inte_moth%1)*100)/100) == 0.34 || ((double)Math.round((inte_moth%1)*100)/100) == 0.84){
				inte_moth -= 0.01;
			}
			
			if (branch == 3) {
				contract.setPrincipal_allah_monthly(0);
				contract.setPrincipal_uppercase_monthly("零元整");
				contract.setInterest_allah_monthly(inte_moth);
				contract.setInterest_uppercase_monthly(MoneyFormat.format(Double.toString( contract.getInterest_allah_monthly() ), true));
			} else if( branch == 2){
				contract.setPrincipal_allah_monthly(Double.parseDouble(String.format("%.2f",(double)(money / month))));
				contract.setPrincipal_uppercase_monthly(MoneyFormat.format(Double.toString(contract.getPrincipal_allah_monthly()), true));
				contract.setInterest_allah_monthly(inte_moth);
				contract.setInterest_uppercase_monthly(MoneyFormat.format( Double.toString(contract.getInterest_allah_monthly()) , true));
				contract.setRepayment_amount_monthly_allah(contract.getPrincipal_allah_monthly() + contract.getInterest_allah_monthly());
				contract.setRepayment_amount_monthly_uppercase(MoneyFormat.format(Double.toString(contract.getRepayment_amount_monthly_allah()), true));
			}else if(branch == 4){
				contract.setRepayment_amount_monthly_allah(pmt_sum);
				contract.setRepayment_amount_monthly_uppercase(MoneyFormat.format(Double.toString(pmt_sum), true));
			}
			
			
			
			contract.setInterest_allah(interest);
			contract.setInterest_uppercase(MoneyFormat.format(Double.toString(interest), true));
			
			contract.setPurpose(financingBase.getYongtu());// 用途
			contract.setSigndate(new Date());
			contract.setFbsj(new Date());

			contract.setInvestor_make_sure(new Date());

			/**
			 * 
			 * 
			 * 以下是为新增合同，新增费用预留
			 */
			contract.setContractTemplate(financingBase.getContractTemplate());
			ContractTemplate ct = financingBase.getContractTemplate();
			if (ct == null) return contract;

			if ("N".equals(ct.getCode()) && ct.getVersion() > 1) {
				contract.setFee_01("新增费用0101010101");
				contract.setFee_02("新增费用0202020202");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contract;
	}

	@Override
	public double investHistory(MemberBase member, FinancingBase financingBase) {
		double b = 0d;
		String hql = "from InvestRecord o where  o.investor.id='" + member.getId() + "'";
		List<InvestRecord> r = this.getCommonListData(hql);
		if (null != r && r.size() > 0) {
			for (InvestRecord c : r) {
				b += c.getInvestAmount();
			}
		}
		return b;
	}

	@Override
	public double investHistory2(MemberBase member, FinancingBase financingBase) {
		double b = 0d;
		String hql = "from InvestRecord o where o.financingBase.id='" + financingBase.getId() + "' and o.investor.id='" + member.getId() + "'";
		List<InvestRecord> r = this.getCommonListData(hql);
		if (null != r && r.size() > 0) {
			for (InvestRecord c : r) {
				b += c.getInvestAmount();
			}
		}
		return b;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public QueryResult<Object> groupBy(final String wherejpql, final String by, final List<String> queryParams) throws Exception {
		QueryResult qr = new QueryResult<Object>();
		List<Object> resultlist = this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String entityname = getGenericType(0).getSimpleName();
				String hql = "";
				if ("CreateDate".equals(by)) {
					hql = "select to_date(to_char(o.createDate,'yyyy-MM-dd'),'yyyy-MM-dd') as createDate,sum(o.investAmount) as money from " + entityname + " o " + ((wherejpql == null || "".equals(wherejpql.trim()) ? "" : "where " + wherejpql) + " order by to_date(to_char(o.createDate,'yyyy-MM-dd'),'yyyy-MM-dd') desc");
				} else if ("T".equals(by)) {
					hql = "select o.investor.pName,sum(o.investAmount),o.investor.id,o.investor.user.username from " + entityname + " o " + ((wherejpql == null || "".equals(wherejpql.trim()) ? "" : "where " + wherejpql) + " order by o.investor.user.username asc");
				} else if ("R".equals(by)) {
					hql = "select o.financingBase.financier.eName,sum(o.investAmount),o.financingBase.financier.id from " + entityname + " o " + ((wherejpql == null || "".equals(wherejpql.trim()) ? "" : "where " + wherejpql) + " order by o.financingBase.financier.user.username asc");
				}
				Query query = session.createQuery(hql);
				if (queryParams != null && queryParams.size() > 0) {
					for (int i = 0; i < queryParams.size(); i++) {
						query = query.setString(i, queryParams.get(i));
					}
				}
				return (List<Object>) query.list();
			}
		});
		qr.setResultlist(resultlist);
		return qr;
	}

	@Override
	@Transactional
	public List<InvestRecord> getInvestRecordListByFinancingId(String id) {
		String hql = "from InvestRecord o where o.financingBase.id='" + id + "'";
		return this.getCommonListData(hql);
	}

	public String buildZqZrCode(FinancingBase financingBase) throws Exception {
		StringBuffer code = new StringBuffer();
		code.append(financingBase.getCode() + "-");
		long count = this.getScrollDataCount(" from InvestRecord o where o.financingBase.id='" + financingBase.getId() + "'");
		code.append(StringUtils.fillZero(3, (count + 1) + ""));
		return code.toString();
	}

	@Override
	public boolean terminal(FinancingBase financingBase) {
		try {
			List<InvestRecord> irs = this.getInvestRecordListByFinancingId(financingBase.getId());
			for (InvestRecord ir : irs) {
				ir.setXyhkr(null);
				this.update(ir);
			}
		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	} 
	
	public MessageTip doInvest(String in_financingBaseId,double money,int credit,String username,String ip) {

		MessageTip tip = new MessageTip(true, "下单成功");
		Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
		inParamList.put(1, in_financingBaseId);
		inParamList.put(2, money);
		inParamList.put(3, credit);
		inParamList.put(4, username);
		inParamList.put(5, ip);

		Map<Integer, Integer> outParameter = new HashMap<Integer, Integer>();
		outParameter.put(6, Types.NUMERIC);
		outParameter.put(7, Types.VARCHAR);
		
		//HashMap<Integer, Object> st = this.investConditionService.callProcedureForParameters("p_investCheckMaxMin", inParamList, outParameter);
		HashMap<Integer, Object> st =this.callProcedureForParameters("p_test_updatefb", inParamList, outParameter);		
		   rcode= (BigDecimal)st.get(6);
		   if (rcode.intValue()<=0){
			   tip.setSuccess(false);
			   tip.setMsg((String)st.get(7));
		   }
		   return tip;
		 /*  if (rcode.intValue()<=0) {
			 throw new StaleObjectStateException(null, log);
			// return null;
		   }*/
	} 
	
	 
	/**
	 * 调用存储过程，返回值列表
	 * @param procedureName 存储过程名称
	 * @param inParamList   
	 * @param outParameter
	 * @return key为下标
	 */ 

	@SuppressWarnings("unchecked")
	public Map callProcedureForInvestInsert(
			final String  in_userName, 
			final String  in_financingBaseId,  
			final String  in_IpStr,  
			final double  in_money,  
			final int  in_credit,  
			final String  in_fromapp,  
			final String  in_userGroupRestrain1id,  
			final double  in_tzfwf,  
			final double  in_fee1,  
			final double  in_lx,  
			final int  in_BusinessTypeTerm,  
			final String  in_Orgcode,  
			final String  in_INVESTOR_ID,  
			final String  in_First_id,  
			final String  in_First_party,  
			final String  in_First_party_code,  
			final double  in_INTEREST_ALLAH_M, 
			final double  in_Interest_rate, 
			final double  in_Payment_method, 
			final double  in_PRINCIPAL_ALLAH_M, 
			final double  in_REPAYMENT_AMOUNT_A, 
			final String  in_Second_party, 
			final String  in_Second_party_code, 
			final String  in_Second_party_yyzz, 
			final String  in_Second_party_zzjg) 
	{ 
		try { 
			synchronized(this){
		   //System.out.println(in_userName+"-------in_money"+in_money+"-------in_credit"+in_credit+"-------in_tzfwf"+in_tzfwf);
		   return (Map)this.jdbcTemplate.execute("{call p_investInsert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}",new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs)throws SQLException,DataAccessException { 
					cs.setString(1, in_userName);   
					cs.setString(2, in_financingBaseId);   
					cs.setString(3, in_IpStr);   
					cs.setDouble(4, in_money);   
					cs.setInt(5,in_credit);   
					cs.setString(6, in_fromapp);   
					cs.setString(7, in_userGroupRestrain1id);   
					cs.setDouble(8, in_tzfwf);   
					cs.setDouble(9, in_fee1);   
					cs.setDouble(10, in_lx);       
					cs.setInt(11, in_BusinessTypeTerm);   
					cs.setString(12, in_Orgcode);   
					cs.setString(13, in_INVESTOR_ID);   
					cs.setString(14, in_First_id);   
					cs.setString(15, in_First_party);   
					cs.setString(16, in_First_party_code); 
					cs.setDouble(17, in_INTEREST_ALLAH_M); 
					cs.setDouble(18, in_Interest_rate); 
					cs.setDouble(19, in_Payment_method); 
					cs.setDouble(20, in_PRINCIPAL_ALLAH_M); 
					cs.setDouble(21, in_REPAYMENT_AMOUNT_A); 
					cs.setString(22, in_Second_party); 
					cs.setString(23, in_Second_party_code); 
					cs.setString(24, in_Second_party_yyzz); 
					cs.setString(25, in_Second_party_zzjg);    
					cs.setString(26, UUID.randomUUID().toString());    
                     
					cs.registerOutParameter(27,Types.VARCHAR); 
					cs.registerOutParameter(28,Types.VARCHAR); 
					cs.registerOutParameter(29,Types.NUMERIC);  
					cs.registerOutParameter(30,Types.VARCHAR);  
					cs.execute();
					Map map = new HashMap();
					map.put("out_recordId", cs.getString(27));       
					map.put("out_costId", cs.getString(28));    
					map.put("out_code", cs.getBigDecimal(29)); 
					map.put("out_msg", cs.getString(30));  
					return map;
				}
			});	
				
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	/**
	 * 调用存储过程，返回值列表
	 * @param procedureName 存储过程名称
	 * @param inParamList   
	 * @param outParameter
	 * @return key为下标
	 */ 
	
	@SuppressWarnings("unchecked")
	public Map callProcedureForInvestInsert2(
			final String  in_userName, 
			final String  in_financingBaseId,  
			final String  in_IpStr,  
			final double  in_money,  
			final int  in_credit,  
			final String  in_fromapp,  
			final String  in_userGroupRestrain1id,  
			final double  in_tzfwf,  
			final double  in_fee1,  
			final double  in_lx,  
			final int  in_BusinessTypeTerm,  
			final String  in_Orgcode,  
			final String  in_INVESTOR_ID,  
			final String  in_First_id,  
			final String  in_First_party,  
			final String  in_First_party_code,  
			final double  in_INTEREST_ALLAH_M, 
			final double  in_Interest_rate, 
			final double  in_Payment_method, 
			final double  in_PRINCIPAL_ALLAH_M, 
			final double  in_REPAYMENT_AMOUNT_A, 
			final String  in_Second_party, 
			final String  in_Second_party_code, 
			final String  in_Second_party_yyzz, 
			final String  in_Second_party_zzjg) 
	{ 
		try { 
			synchronized(this){
				//System.out.println(in_userName+"-------in_money"+in_money+"-------in_credit"+in_credit+"-------in_tzfwf"+in_tzfwf);
				return (Map)this.jdbcTemplate.execute("{call p_investInsert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}",new CallableStatementCallback() {
					public Object doInCallableStatement(CallableStatement cs)throws SQLException,DataAccessException { 
						cs.setString(1, in_userName);   
						cs.setString(2, in_financingBaseId);   
						cs.setString(3, in_IpStr);   
						cs.setDouble(4, in_money);   
						cs.setInt(5,in_credit);   
						cs.setString(6, in_fromapp);   
						cs.setString(7, in_userGroupRestrain1id);   
						cs.setDouble(8, in_tzfwf);   
						cs.setDouble(9, in_fee1);   
						cs.setDouble(10, in_lx);       
						cs.setInt(11, in_BusinessTypeTerm);   
						cs.setString(12, in_Orgcode);   
						cs.setString(13, in_INVESTOR_ID);   
						cs.setString(14, in_First_id);   
						cs.setString(15, in_First_party);   
						cs.setString(16, in_First_party_code); 
						cs.setDouble(17, in_INTEREST_ALLAH_M); 
						cs.setDouble(18, in_Interest_rate); 
						cs.setDouble(19, in_Payment_method); 
						cs.setDouble(20, in_PRINCIPAL_ALLAH_M); 
						cs.setDouble(21, in_REPAYMENT_AMOUNT_A); 
						cs.setString(22, in_Second_party); 
						cs.setString(23, in_Second_party_code); 
						cs.setString(24, in_Second_party_yyzz); 
						cs.setString(25, in_Second_party_zzjg);    
						cs.setString(26, UUID.randomUUID().toString());    
						
						cs.registerOutParameter(27,Types.VARCHAR); 
						cs.registerOutParameter(28,Types.VARCHAR); 
						cs.registerOutParameter(29,Types.NUMERIC);  
						cs.registerOutParameter(30,Types.VARCHAR);  
						cs.execute();
						Map map = new HashMap();
						map.put("out_recordId", cs.getString(27));       
						map.put("out_costId", cs.getString(28));    
						map.put("out_code", cs.getBigDecimal(29)); 
						map.put("out_msg", cs.getString(30));  
						return map;
					}
				});	
				
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map callProcedureForDocheck(final String  in_userName,	final String  in_financingBaseId,final String in_usertype)
	{ 
		try { 
			return (Map)this.jdbcTemplate.execute("{call p_investCheck_new(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}",new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs)throws SQLException,DataAccessException { 
					cs.setString(1, in_userName);   
					cs.setString(2, in_financingBaseId);     
					cs.setString(16, in_usertype);   
					cs.setString(19, Constant.MAX_INVEST);  
					
					cs.registerOutParameter(3, Types.NUMERIC);
					cs.registerOutParameter(4, Types.NUMERIC);
					cs.registerOutParameter(5, Types.NUMERIC);
					cs.registerOutParameter(6, Types.NUMERIC);
					cs.registerOutParameter(7, Types.NUMERIC);
					cs.registerOutParameter(8, Types.NUMERIC);
					cs.registerOutParameter(9, Types.NUMERIC);
					cs.registerOutParameter(10, Types.NUMERIC);
					cs.registerOutParameter(11, Types.NUMERIC);
					cs.registerOutParameter(12, Types.NUMERIC);
					cs.registerOutParameter(13, Types.DATE);
					cs.registerOutParameter(14, Types.DATE);
					cs.registerOutParameter(15, Types.VARCHAR);
					
					cs.registerOutParameter(17, Types.NUMERIC); 
					cs.registerOutParameter(18, Types.VARCHAR); 
					
					cs.registerOutParameter(20, Types.VARCHAR);
					cs.registerOutParameter(21, Types.NUMERIC);
					cs.registerOutParameter(22, Types.NUMERIC); 
					cs.registerOutParameter(23, Types.NUMERIC);
					
					cs.registerOutParameter(24, Types.VARCHAR);
					cs.registerOutParameter(25, Types.VARCHAR);
					cs.registerOutParameter(26, Types.VARCHAR);
					cs.registerOutParameter(27, Types.VARCHAR);
					cs.registerOutParameter(28, Types.VARCHAR);
					
					cs.registerOutParameter(29, Types.VARCHAR);
					cs.registerOutParameter(30, Types.NUMERIC);
					cs.registerOutParameter(31, Types.VARCHAR);
					cs.registerOutParameter(32, Types.NUMERIC);
					cs.registerOutParameter(33, Types.NUMERIC);
					cs.registerOutParameter(34, Types.NUMERIC);
					cs.registerOutParameter(35, Types.NUMERIC); 
					cs.registerOutParameter(36, Types.NUMERIC);  
					cs.execute();  
					Map map = new HashMap();
					map.put("3", cs.getBigDecimal(3)); 
					map.put("4", cs.getBigDecimal(4)); 
					map.put("5", cs.getBigDecimal(5)); 
					map.put("6", cs.getBigDecimal(6)); 
					map.put("7", cs.getBigDecimal(7)); 
					map.put("8", cs.getBigDecimal(8)); 
					map.put("9", cs.getBigDecimal(9)); 
					map.put("10", cs.getBigDecimal(10)); 
					map.put("11", cs.getBigDecimal(11)); 
					map.put("12", cs.getBigDecimal(12)); 
					map.put("13", cs.getDate(13)); 
					map.put("14", cs.getDate(14)); 
					map.put("15", cs.getString(15)); 
					map.put("17", cs.getBigDecimal(17)); 
					map.put("18", cs.getString(18));  
					map.put("20", cs.getString(20));  
					map.put("21", cs.getBigDecimal(21)); 
					map.put("22", cs.getBigDecimal(22));  
					map.put("23", cs.getBigDecimal(23));  
					map.put("24", cs.getString(24));  
					map.put("25", cs.getString(25));  
					map.put("26", cs.getString(26));  
					map.put("27", cs.getString(27));  
					map.put("28", cs.getString(28));  
					map.put("29", cs.getString(29));  
					map.put("30", cs.getBigDecimal(30)); 
					map.put("31", cs.getString(31));  
					map.put("32", cs.getBigDecimal(32)); 
					map.put("33", cs.getBigDecimal(33));  
					map.put("34", cs.getBigDecimal(34));  
					map.put("35", cs.getBigDecimal(35));
					map.put("36", cs.getBigDecimal(36)); 
					return map;
				}
			});		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
		
}
