package com.kmfex.service.impl;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.kmfex.model.SignHistory;
import com.kmfex.service.SignHistoryService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.StringUtils;

@Service
public class SignHistoryImpl extends BaseServiceImpl<SignHistory> implements SignHistoryService {
	
	@Override
	public SignHistory getHistory(long owner,int signBank,int signType){
		return this.selectByHql("from SignHistory s where s.success=1 and s.owner="+owner+" and s.signBank="+signBank+" and s.signType="+signType+" order by s.signDate desc");
	}
	
	@Override
	public double[] icbc_balance_frozen(String subsql) {
		double[] result = new double[2];
		StringBuilder sql = new StringBuilder(" select sum(s.balance) as balance,sum(s.frozen) as frozen from t_signhistory s,sys_user u,sys_user op ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString());
			if(row.next()){
				result[0] = row.getDouble("balance");
				result[1] = row.getDouble("frozen");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
	
	@Override
	public double[] balance_frozen(String subsql) {
		double[] result = new double[2];
		StringBuilder sql = new StringBuilder(" select sum(s.balance) as balance,sum(s.frozen) as frozen from t_signhistory s,sys_user u ");
		if(StringUtils.isNotBlank(subsql)){
			sql.append(" where ").append(subsql);
		}
		try {
			SqlRowSet row = this.jdbcTemplate.queryForRowSet(sql.toString());
			if(row.next()){
				result[0] = row.getDouble("balance");
				result[1] = row.getDouble("frozen");
			}
		} catch (NullPointerException e) {
		}
		
		return result;
	}
}