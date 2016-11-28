package com.kmfex.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.MoneyFormat;
import com.kmfex.model.BusinessType;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberBase;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
@Transactional
public class ContractKeyDataImpl extends BaseServiceImpl<ContractKeyData> implements ContractKeyDataService {
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	UserService userService;
	@Resource
	MemberBaseService memberBaseService;

	@Override
	@Transactional
	public void contractKeyDataFeeUpdate(String investRecordId,FinancingCost fc) throws EngineException {

		InvestRecord investRecord = investRecordService.selectById(investRecordId);
		FinancingBase financingBase = investRecord.getFinancingBase();
		ContractKeyData contractKeyData = this.selectById(investRecord.getContract().getId());
		
		int term = financingBase.getBusinessType().getTerm();//期限
		int returntimes = financingBase.getBusinessType().getReturnTimes();
		double ia = investRecord.getInvestAmount();//投标额
		double ia_bl = ia / financingBase.getCurrenyAmount();
		
		
		//风险管理费
		double fxglf = 0d;
		int fxglf_tariff = 0;
		double fxglf_bl = 0d;
	
		//融资服务费
		double rzfwf = 0d;
		int rzfwf_tariff = 0;
		double rzfwf_bl = 0d;
		
		//担保费
		double dbf = 0d;
		int dbf_tariff = 0;
		double dbf_bl = 0d;
		
		//保证金
		double bzj = 0d;
		int bzj_tariff =0;
		double bzj_bl = 0d;
		
		
		double realAmount = 0;
		if (financingBase.getCode().startsWith("X")) {
			//风险管理费
			fxglf = fc.getFee1();//风险管理费
			fxglf_tariff = fc.getFee1_tariff(); //风险管理费 收费方式
			fxglf_bl = fc.getFee1_bl();
			
			//融资服务费
			rzfwf = fc.getFee2();
			rzfwf_tariff = fc.getFee2_tariff();
			rzfwf_bl = fc.getFee2_bl();
			
			//担保费
			dbf = fc.getFee3();
			dbf_tariff = fc.getFee3_tariff();
			dbf_bl = fc.getFee3_bl();
			
			
			
		} else {// 0表示本金保障，1表示担保公司担保--费用标准
			//风险管理费
			fxglf = fc.getFxglf();//风险管理费
			fxglf_tariff = fc.getFxglf_tariff(); //风险管理费 收费方式
			fxglf_bl = fc.getFxglf_bl();
			
			//融资服务费
			rzfwf = fc.getRzfwf();
			rzfwf_tariff = fc.getRzfwf_tariff();
			rzfwf_bl = fc.getRzfwf_bl();
			
			//担保费
			dbf = fc.getDbf();
			dbf_tariff = fc.getDbf_tariff();
			dbf_bl = fc.getDbf_bl();

			
		}
		//保证金
		bzj = fc.getBzj();
		bzj_tariff = fc.getBzj_tariff();
		bzj_bl = fc.getBzj_bl();
		
		
		//风险管理费
		if(fxglf_tariff != 0 && fxglf != 0 ){
			double riskmanagement = fxglf/returntimes*ia_bl;
			contractKeyData.setRiskmanagement_cost_allah(Double.parseDouble(String.format("%.2f",riskmanagement)));
			contractKeyData.setRiskmanagement_cost_uppercase(MoneyFormat.format(String.format("%.2f", riskmanagement), true));
			contractKeyData.setFxglf_fs(1);
		}
		contractKeyData.setRiskmanagement_cost_bl(fxglf_bl);
		contractKeyData.setRiskmanagement_cost_all_allah(Double.parseDouble(String.format("%.2f",fxglf*ia_bl)));
		
		
		//融资服务费
		if(rzfwf_tariff != 0 && rzfwf != 0){
			double servicecost = rzfwf/returntimes*ia_bl;
			contractKeyData.setService_cost_allah(Double.parseDouble(String.format("%.2f",servicecost)));
			contractKeyData.setService_cost_uppercase(MoneyFormat.format(String.format("%.2f", servicecost), true));
			contractKeyData.setService_cost_fs(1);
		}
		contractKeyData.setService_cost_bl(rzfwf_bl);
		contractKeyData.setService_cost_all_allah(Double.parseDouble(String.format("%.2f",rzfwf*ia_bl)));
		
		//保证金
		if(bzj_tariff != 0 && bzj != 0){
			double bzj_ = bzj/returntimes*ia_bl;
			contractKeyData.setBzj(Double.parseDouble(String.format("%.2f",bzj_)));
			contractKeyData.setBzj_dx(MoneyFormat.format(String.format("%.2f", bzj_), true));
			contractKeyData.setBzj_fs(1);
		}
		contractKeyData.setBzj_bl(bzj_bl);
		contractKeyData.setBzj_all_allah(Double.parseDouble(String.format("%.2f",bzj*ia_bl)));
		
		
		//担保费
		if(dbf_tariff != 0 && dbf != 0){
			double dbf_ = dbf/returntimes*ia_bl;
			contractKeyData.setDbf(Double.parseDouble(String.format("%.2f",dbf_)));
			contractKeyData.setDbf_dx(MoneyFormat.format(String.format("%.2f", dbf_), true));
			contractKeyData.setDbf_fs(1);
		}
		contractKeyData.setDbf_bl(dbf_bl);
		contractKeyData.setDbf_all_allah(Double.parseDouble(String.format("%.2f",dbf*ia_bl)));
		
		contractKeyData.setOverallcost_allah(Double.parseDouble(String.format("%.2f",realAmount)));// 总费用
		contractKeyData.setOverallcost_uppercase(MoneyFormat.format(String.format("%.2f", realAmount), true));// 总费用

		this.update(contractKeyData);

	}

	@Override
	public String findContractKeyDatas(String first_party_code) {
		List<ContractKeyData> contractKeyDatas = this.getScrollDataCommon("from ContractKeyData a where a.first_party_code ='" + first_party_code + "'", new String[] {});
		String ids = "";
		for (ContractKeyData rm : contractKeyDatas) {
			ids += "'" + rm.getInverstrecord_id() + "',";
		}
		if (!"".equals(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		} else {
			ids = "''";
		}
		return ids;
	}
}
