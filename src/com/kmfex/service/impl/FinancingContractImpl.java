package com.kmfex.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.ContractKeyData;
import com.kmfex.model.FinancingContract;
import com.kmfex.model.InvestRecord;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingContractService;
import com.kmfex.service.InvestRecordService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * @author linuxp
 * */
@Service
public class FinancingContractImpl extends BaseServiceImpl<FinancingContract> implements FinancingContractService {
	@Resource FinancingBaseService financingBaseService;
	@Resource InvestRecordService investRecordService;
	@Resource ContractKeyDataService contractKeyDataService;
	@Override
	@Transactional
	public FinancingContract getByFinancingBase(String id) {
		String hql = "from FinancingContract f where f.financingBase.id='"+id+"'";
		List<FinancingContract> ls = this.getCommonListData(hql);
		if(null!=ls&&ls.size()>0){
			return ls.get(0);
		}else{
			return null;
		}
	}
	@Override
	public List<ContractKeyData> getContractList(String id) {
		String hql = "from InvestRecord o where o.financingBase.id='"+id+"'";
		List<InvestRecord> r = this.investRecordService.getCommonListData(hql);
		List<ContractKeyData> con = new ArrayList<ContractKeyData>();
		if(null!=r&&r.size()>0){
			for(InvestRecord c :r){
				con.add(this.contractKeyDataService.selectById(c.getContract().getId()));
			}
		}
		return con;
	}
	@Override
	public List<FinancingContract> listByFinancinger(String rzrid) {
		String hql = "from FinancingContract o where o.financingBase.financier.id='"+rzrid+"'";
		List<FinancingContract> r = this.getCommonListData(hql);
		if(null!=r&&r.size()>0){
			return r;
		}else{
			return null;
		}
	}
	
}
