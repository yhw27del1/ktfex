package com.kmfex.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.BusinessType;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberBase;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.StringUtils;

/**
 * 
 * @author  
 * 20130609   将action的hesuan方法移到service里
 * 2012-8-17 66行 152行 将状态由6改为 5 需要进行费用核算才能签约
 * 2013年5月29日11:35   修改insertFinancingCost()方法,给借款履约保证金赋初始值；
 * 2013年07月09日11:35   修改insertFinancingCost()方法,给按日计息的包赋初始值；
 * 2013年09月06日11:35   修改feiyongjisuan()方法,增加其他费用；
 */
@Service
@Transactional
public class FinancingCostImpl extends BaseServiceImpl<FinancingCost> implements FinancingCostService {

	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	AccountDealService accountDealService;

	@Resource
	LogsService logsService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	UserService userService;

	/*
	 * 计算融资项目各种费用 (融资确认)
	 */
	@Override
	@Transactional
	public FinancingCost insertFinancingCost(String id) throws EngineException {
		if (null == id || "".equals(id)) return null;// id为空不计算
		try {
			FinancingBase financingBase = financingBaseService.selectById(id);
			Double currenyAmount = financingBase.getCurrenyAmount(); // 得到当前融资额

			BusinessType businessType = financingBase.getBusinessType();// 业务类型（融资期限）
			// 风险管理费
			double fxglf = 0d;
			if (null != businessType) {
				fxglf = (businessType.getFxglf() / 100);
			}
			Double realAmount = currenyAmount; // 实际费用

			FinancingCost financingCost = new FinancingCost();
			CostItem bzj = chargingStandardService.findCostItem("bzj", "R");// 保证金标准

			if (financingBase.getCode().startsWith("X")) {
				// 兴易贷费用标准
				CostItem fee1Item = chargingStandardService.findCostItem("xxzxf-xyd", "R");
				CostItem fee2Item = chargingStandardService.findCostItem("zhglf-xyd", "R");
				CostItem fee3Item = chargingStandardService.findCostItem("dbf-xyd", "R");
				Double fee1 = (fee1Item.getPercent() / 100) * currenyAmount; // 兴易贷费用--信息咨询费：借款初始金额的2.5%，借款发放时一次性扣收
				Double fee2 = (fee2Item.getPercent() / 100) * currenyAmount * financingBase.getBusinessType().getTerm(); // 兴易贷费用--综合管理费(总计)：借款初始金额的1%/月，按月计收
				Double fee3 = (fee3Item.getPercent() / 100) * currenyAmount * financingBase.getBusinessType().getTerm(); // 兴易贷费用--担保费(总计)：借款初始金额的0.3%/月，按月计收
				financingCost.setFee1(fee1);
				financingCost.setFee2(fee2);
				financingCost.setFee3(fee3);
				
				financingCost.setFee1_bl(fee1Item.getPercent());
				if("day".equals(financingBase.getBusinessType().getId())){
					financingCost.setFee1_tariff(0);//一次收取
				}else{
					financingCost.setFee1_tariff(fee1Item.getCostBase().getTariff());
				}
				
                
				financingCost.setFee2_bl(fee2Item.getPercent());
				if("day".equals(financingBase.getBusinessType().getId())){
					financingCost.setFee2_tariff(0);//一次收取
				}else{
					financingCost.setFee2_tariff(fee2Item.getCostBase().getTariff());
				}
				financingCost.setFee3_bl(fee3Item.getPercent());
				if("day".equals(financingBase.getBusinessType().getId())){
					financingCost.setFee3_tariff(0);//一次收取
				}else{
					financingCost.setFee3_tariff(fee3Item.getCostBase().getTariff());
				}
                
				realAmount = realAmount - fee1;// -fee2-fee3;

				financingCost.setFee4(currenyAmount);// 兴易贷费用--还本金(总计)：借款初始金额/借款月数=每月偿还本金数额，由投资人收取
				financingCost.setFee5((financingBase.getRate() / 100) * currenyAmount);// 兴易贷费用--还利息(总计)：年利率22%，按照等额本金方式按月偿还，由投资人收取
				CostItem fee6Item = chargingStandardService.findCostItem("tqhksxf-xyd", "R");
				financingCost.setFee6((fee6Item.getPercent() / 100) * currenyAmount);// 兴易贷费用--提前还款手续费：借款初始金额的3%，还款时一次性收取

			} else {// 0表示本金保障，1表示担保公司担保--费用标准
				
				CostItem rzfwf = chargingStandardService.findCostItem("rzfwf", "R");// 融资服务费
				CostItem fxglfD = chargingStandardService.findCostItem("fxglf", "R");// 风险服务费
				CostItem dbf = chargingStandardService.findCostItem("dbf", "R");// 担保费

				 
				if ("10".equals(financingBase.getFxbzState())) { 
					realAmount = realAmount - fxglf * currenyAmount;
					financingCost.setFxglf(fxglf * currenyAmount);// 风险管理费
					financingCost.setFxglfOld(fxglf * currenyAmount);// 风险管理费 
					financingCost.setFxglf_bl(financingBase.getBusinessType().getFxglf());
					financingCost.setFxglf_tariff(0);
				 
				} else {//有担保公司
					if (null != dbf) {
						realAmount = realAmount - (dbf.getPercent() / 100) * currenyAmount;
						financingCost.setDbf((dbf.getPercent() / 100) * currenyAmount);// 担保费
						financingCost.setDbfOld((dbf.getPercent() / 100) * currenyAmount);
						financingCost.setDbf_bl(dbf.getPercent());
						
						if("day".equals(financingBase.getBusinessType().getId())){
							financingCost.setDbf_tariff(0);//一次收取
						}else{
							financingCost.setDbf_tariff(dbf.getCostBase().getTariff());	
						}
					}
					if (null != fxglfD) {
						realAmount = realAmount - (fxglfD.getPercent() / 100) * currenyAmount;
						financingCost.setFxglf((fxglfD.getPercent() / 100) * currenyAmount);// 风险管理费
						financingCost.setFxglfOld((fxglfD.getPercent() / 100) * currenyAmount);
						financingCost.setFxglf_bl(fxglfD.getPercent());
						
						if("day".equals(financingBase.getBusinessType().getId())){
							financingCost.setFxglf_tariff(0);//一次收取
						}else{
							financingCost.setFxglf_tariff(fxglfD.getCostBase().getTariff());
						}
					}
				}
				if (null != rzfwf) {
					realAmount = realAmount - ((rzfwf.getPercent() / 100) * currenyAmount) * businessType.getTerm();// .getReturnTimes();
					financingCost.setRzfwf(((rzfwf.getPercent() / 100) * currenyAmount) * businessType.getTerm());// 融资服务费
					financingCost.setRzfwfOld(((rzfwf.getPercent() / 100) * currenyAmount) * businessType.getTerm());
					financingCost.setRzfwf_bl(rzfwf.getPercent());
					
					if("day".equals(financingBase.getBusinessType().getId())){
						financingCost.setRzfwf_tariff(0);//一次收取
					}else{
						financingCost.setRzfwf_tariff(rzfwf.getCostBase().getTariff());
					}
				}
	

			}
			if (null != bzj) {
				realAmount = realAmount - (bzj.getPercent() / 100) * currenyAmount;
				financingCost.setBzj((bzj.getPercent() / 100) * currenyAmount);// 保证金
				financingCost.setBzjOld((bzj.getPercent() / 100) * currenyAmount);

			}
			financingCost.setBzj_bl(bzj.getPercent());
			
			if("day".equals(financingBase.getBusinessType().getId())){
				financingCost.setBzj_tariff(0);//一次收取
			}else{
				financingCost.setBzj_tariff(bzj.getCostBase().getTariff());
			}
			
			
			financingCost.setRealAmount(realAmount);// 扣除各种费用后剩下金额

			if (null != financingBase.getFinancier()) {
				financingCost.setFinancier(financingBase.getFinancier());// 记录融资方
			}
			financingCost.setFinancingBase(financingBase);
			financingCost.setState("0");
			
			
			
			// 该融资项目的投标记录金额已经投标记录的服务费汇总后，转到中心账户去。
//			String hql = "from InvestRecord o where o.financingBase.id='" + financingBase.getId() + "'";
//			List<InvestRecord> ls = investRecordService.getCommonListData(hql);
//			double b = 0d;
//			double fee = 0d;
//			if (null != ls && ls.size() > 0) {
//				for (InvestRecord o : ls) {
//
//					// 兴易贷费用标准
//					if (financingBase.getCode().startsWith("X")) {
//						b += o.getInvestAmount();
//						fee += o.getCost().getFee1();
//					} else {// 0表示本金保障，1表示担保公司担保--费用标准
//						b += o.getInvestAmount();
//						fee += o.getCost().getTzfwf();
//					}
//				}
//			}
//			if (b > 0) {
//				this.accountDealService.tzkhr(b, fee, financingBase);// 投资款划入中心账户
//			}
			
			
			
			
			this.insert(financingCost);
			return financingCost;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 费用确认  
	 * 20130609  
	 * 
	 */
	@Override
	@Transactional
	public void feiyongjisuan(FinancingCost financingCost,FinancingCost financingCostIN,MemberBase financier,User u) throws Exception{
		//financingCostIN.setState("1");
		
		
		financingCostIN.setFee1(financingCost.getFee1());
		financingCostIN.setFee2(financingCost.getFee2());
		financingCostIN.setFee3(financingCost.getFee3()); 
	
		financingCostIN.setDbf(financingCost.getDbf());
		financingCostIN.setRzfwf(financingCost.getRzfwf());
		financingCostIN.setFxglf(financingCost.getFxglf());
		
		financingCostIN.setNote(financingCost.getNote());
		financingCostIN.setFee7(financingCost.getFee7());// 席位费
		financingCostIN.setFee10(financingCost.getFee10());// 信用管理费
		financingCostIN.setBzj(financingCost.getBzj());

		financingCostIN.setDbf_bl(financingCost.getDbf_bl());
		financingCostIN.setDbf_tariff(financingCost.getDbf_tariff());
		financingCostIN.setFxglf_bl(financingCost.getFxglf_bl());
		financingCostIN.setFxglf_tariff(financingCost.getFxglf_tariff());
		financingCostIN.setRzfwf_bl(financingCost.getRzfwf_bl());
		financingCostIN.setRzfwf_tariff(financingCost.getRzfwf_tariff());
		financingCostIN.setBzj_bl(financingCost.getBzj_bl());
		financingCostIN.setBzj_tariff(financingCost.getBzj_tariff());

		financingCostIN.setFee1_bl(financingCost.getFee1_bl());
		financingCostIN.setFee1_tariff(financingCost.getFee1_tariff());
		financingCostIN.setFee2_bl(financingCost.getFee2_bl());
		financingCostIN.setFee2_tariff(financingCost.getFee2_tariff());
		financingCostIN.setFee3_bl(financingCost.getFee3_bl());
		financingCostIN.setFee3_tariff(financingCost.getFee3_tariff());
		
		//其他费用
		financingCostIN.setOther(financingCost.getOther());
		financingCostIN.setOther_bl(financingCost.getOther_bl());
		financingCostIN.setOther_tariff(financingCost.getOther_tariff());

		

		/** *年费开始** */
		if (financingCost.getFee7() > 0) {
			boolean flagFee = false;
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			if (null != financier.getYearFeeDate()) {
				if (format.format(new Date()).equals(format.format(financier.getYearFeeDate()))) {
					flagFee = false; // 今年已经交过了
				} else {
					flagFee = true;
				}
			} else {
				flagFee = true;
			}

			if (flagFee) {
				financier.setYearFeeDate(new Date());
				financier.setYearFeeStartDate(financier.getYearFeeEndDate());
				Calendar c = Calendar.getInstance();
				c.setTime(financier.getYearFeeStartDate());
				c.add(c.YEAR, 1);
				financier.setYearFeeEndDate(c.getTime());
			}

			if (null != financier.getYearFeeNote() && !"".equals(financier.getYearFeeNote())) {
				financier.setYearFeeNote(financier.getYearFeeNote() + ",{\"username\":\"" + u.getUsername() + "\",date:\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",finanCode:\"" + financingCostIN.getFinancingBase().getCode() + "\",fee:\"" + financingCost.getFee7() + "\"},");
			} else {
				financier.setYearFeeNote("{username:\"" + u.getUsername() + "\",date:\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",finanCode:\"" + financingCostIN.getFinancingBase().getCode() + "\",fee:\"" + financingCost.getFee7() + "\"},");
			}
			if (null != financier.getYearFeeNote() && !"".equals(financier.getYearFeeNote())) {
				financier.setYearFeeNote(financier.getYearFeeNote().substring(0, financier.getYearFeeNote().length() - 1));
			}
		}
		/** *年费结束** */

		/** *信用管理费开始** */
		if (financingCost.getFee10() > 0) {
			boolean flagFee = false;
			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			if (null != financier.getCreditFeeDate()) {
				if (format.format(new Date()).equals(format.format(financier.getCreditFeeDate()))) {
					flagFee = false; // 今年已经交过年费了
				} else {
					flagFee = true;
				}
			} else {
				flagFee = true;
			}

			if (flagFee) {
				financier.setCreditFeeDate(new Date());
				financier.setCreditFeeStartDate(financier.getCreditFeeEndDate());
				Calendar c = Calendar.getInstance();
				c.setTime(financier.getCreditFeeStartDate());
				c.add(c.YEAR, 1);
				financier.setCreditFeeEndDate(c.getTime());
			}

			if (null != financier.getCreditFeeNote() && !"".equals(financier.getCreditFeeNote())) {
				financier.setCreditFeeNote(financier.getCreditFeeNote() + ",{\"username\":\"" + u.getUsername() + "\",date:\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",finanCode:\"" + financingCostIN.getFinancingBase().getCode() + "\",fee:\"" + financingCost.getFee10() + "\"},");
			} else {
				financier.setCreditFeeNote("{username:\"" + u.getUsername() + "\",date:\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\",finanCode:\"" + financingCostIN.getFinancingBase().getCode() + "\",fee:\"" + financingCost.getFee10() + "\"},");
			}
			if (null != financier.getCreditFeeNote() && !"".equals(financier.getCreditFeeNote())) {
				financier.setCreditFeeNote(financier.getCreditFeeNote().substring(0, financier.getCreditFeeNote().length() - 1));
			}
		}
		memberBaseService.update(financier);
		/** *信用管理结束** */

		// 重新计算费用
		if (financingCostIN.getFinancingBase().getCode().startsWith("X")) {
			Double realAmount = financingCostIN.getFinancingBase().getCurrenyAmount();
			if (financingCostIN.getFee1_tariff() == 0) {
				realAmount = realAmount - financingCost.getFee1();
			}
			if (financingCostIN.getFee2_tariff() == 0) {
				realAmount = realAmount - financingCost.getFee2();
			}
			if (financingCostIN.getFee3_tariff() == 0) {
				realAmount = realAmount - financingCost.getFee3();
			}
			if (financingCostIN.getBzj_tariff() == 0) {
				realAmount = realAmount - financingCost.getBzj();
			}
			if (financingCostIN.getOther_tariff() == 0) {
				realAmount = realAmount - financingCost.getOther();
			}
			realAmount = realAmount - financingCost.getFee7() - financingCost.getFee10(); // 实际费用
			financingCostIN.setRealAmount(realAmount);
		} else {
			Double realAmount = financingCostIN.getFinancingBase().getCurrenyAmount();
			if (financingCostIN.getDbf_tariff() == 0) {
				realAmount = realAmount - financingCost.getDbf();
			}
			if (financingCostIN.getBzj_tariff() == 0) {
				realAmount = realAmount - financingCost.getBzj();
			}
			if (financingCostIN.getFxglf_tariff() == 0) {
				realAmount = realAmount - financingCost.getFxglf();
			}

			if (financingCostIN.getRzfwf_tariff() == 0) {
				realAmount = realAmount - financingCost.getRzfwf();
			}
			if (financingCostIN.getOther_tariff() == 0) {
				realAmount = realAmount - financingCost.getOther();
			}

			realAmount = realAmount - financingCost.getFee7() - financingCost.getFee10(); // 实际费用

			financingCostIN.setRealAmount(realAmount);
		}
		financingCostIN.setAuditBy(u);
		financingCostIN.setAuditDate(new Date());
		
		
		
		FinancingBase financingBase = financingCostIN.getFinancingBase();
		financingBase.setState("5");// 修改状态为 已确认
		financingBase.setFinishtime(new Date());// 记录完成时间
		financingBase.setModifyDate(new Date());
		
		

		financingBaseService.update(financingBase);
		this.update(financingCostIN);

		/**
		 * 记个日志
		 */
		Logs log = null;
		log = logsService.log("确认融资项目");
		log.setEntityId(financingBase.getId());
		log.setEntityFrom("FinancingBase");
		this.logsService.insert(log);
		//financingBase.getLogs().add(log);
	}
	

	@Override
	public FinancingCost getCostByFinancingBase(String fid) throws EngineException {
		String hql = "from FinancingCost o where o.financingBase.id='" + fid + "'";
		FinancingCost cost = null;
		cost = this.selectByHql(hql);
		return cost;
	}

	@Override
	public FinancingCost getCostByFinancer(String id) throws EngineException {
		//
		String hql = "from FinancingCost o where o.financier.id='" + id + "' and o.state='1' and o.hkBy is null";
		FinancingCost cost = null;
		cost = this.selectByHql(hql);
		return cost;
	}
	
	/**
	 * 20130614    费用通过
	 * @throws Exception 
	 * 
	 */
	@Override
	@Transactional
	public void cost_pass(Serializable id) throws Exception{
		FinancingCost fc = this.selectById(id);
		FinancingBase fb = fc.getFinancingBase();
		if(!"5".equals(fb.getState())){throw new Exception("融资项目状态不正确");}
		fc.setState("1");
		fb.setState("6");
		fb.setModifyDate(new Date());
		
		
		
		//更新交易时长
		//List<Logs> logs = fb.getLogs();
		List<Logs> logs =this.logsService.getCommonListData("from Logs where entityFrom='FinancingBase' and entityId='"+fb.getId()+"'");
		Date starttime = null, endtime = null;
		long jysc = 0L;
		for(Logs log : logs){
			String str = log.getOperate();
			if("挂单通过".equals(str)){
				starttime = log.getTime();
			}else if(str.startsWith("投标")){
				if(endtime==null){
					endtime = log.getTime();
				}else if(endtime.getTime() < log.getTime().getTime()){
					endtime = log.getTime();
				}
			}
		}
		if(starttime != null && endtime !=null ){
			jysc = (endtime.getTime() - starttime.getTime()) / 1000;
		}
		fb.setJysc(jysc);
		//更新交易时长
		
		
		
		/**
		 * 记个日志
		 */
		Logs log = null;
		log = logsService.log("费用通过");
		log.setEntityId(fb.getId());
		log.setEntityFrom("FinancingBase");
		this.logsService.insert(log);
		
		//fb.getLogs().add(log);
		
		this.update(fc);
		this.financingBaseService.update(fb);
		
		
		//生成债权编号和合同编号
		List<InvestRecord> irs=this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?  and contract != null", new String[] { fb.getId()});
		int i=0;
		for(InvestRecord ir:irs){
			String count=StringUtils.fillZero(3, (i + 1) + "");
			ir.setZhaiQuanCode(ir.getFinancingBase().getCode() + "-"+count);
			ir.getContract().setContract_numbers(ir.getFinancingBase().getCode() + "-"+ir.getInvestor().getUser().getUsername()+ "-"+count);
			this.investRecordService.update(ir);
			i++; 
		} 
	}

	@Override
	@Transactional
	public void cost_ignore(Serializable id) throws Exception {
		FinancingCost fc = this.selectById(id);
		FinancingBase fb = fc.getFinancingBase();
		MemberBase financier = fb.getFinancier();
		if(!"5".equals(fb.getState())){throw new Exception("融资项目状态不正确");}
		if(fc.getFee7() > 0){
			financier.setYearFeeDate(null);
			financier.setYearFeeEndDate(null);
			financier.setYearFeeStartDate(null);
			fc.setFee7(0d);
		}
		if(fc.getFee10() > 0){
			financier.setCreditFeeDate(null);
			financier.setCreditFeeEndDate(null);
			financier.setCreditFeeStartDate(null);
			fc.setFee10(0d);
		}
		fb.setState("4");
		/**
		 * 记个日志
		 */
		Logs log = null;
		log = logsService.log("费用驳回");
		log.setEntityId(fb.getId());
		log.setEntityFrom("FinancingBase");
		this.logsService.insert(log);
		//fb.getLogs().add(log);
		this.financingBaseService.update(fb);
		this.memberBaseService.update(financier);
		
	}

}
