package com.kmfex.zhaiquan.service.impl;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kmfex.zhaiquan.model.ZQBuySellRule;
import com.kmfex.zhaiquan.service.ZQBuySellRuleService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.vo.CommonVo;

/**
 * 
 * @author
 * 
 */
@Service
public class ZQBuySellRuleServiceImpl extends BaseServiceImpl<ZQBuySellRule> implements ZQBuySellRuleService {
	///
	/*@Resource AccountService accountService;
	@Override
	public void open_an_account(Account account) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			CreditRules cr = this.selectByHql("from CreditRules where enable = true and to_date('"+now+"','yyyy-MM-dd HH24:mi:ss') between effecttime and expiretime");
			if(cr!=null){
				int value = cr.getKhjf();
				account.setCredit(account.getCredit()+value);
				this.accountService.update(account);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}*/
	
	public  CommonVo  zhaiQuanCheck(String financingId,String investRecordid){
		 CommonVo vo=new CommonVo();
	     Map<Integer, Object> inParamList=new HashMap<Integer, Object>();
	     inParamList.put(1, financingId);
	     inParamList.put(2, investRecordid);
	     
	     Map<Integer, Integer> outParameter=new HashMap<Integer, Integer>();
	     outParameter.put(3, Types.NUMERIC);
	     outParameter.put(4, Types.VARCHAR);
	     HashMap<Integer, Object> st=this.callProcedureForParameters("p_zhaiquan_check", inParamList, outParameter) ;
	     BigDecimal code=(BigDecimal)st.get(3);
	     String message=(String)st.get(4);
	     vo.setString1(code.toString());
	     vo.setString2(message);
		 return vo; 
	}

}
