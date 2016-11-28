package com.kmfex.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kmfex.model.CreditRules;
import com.kmfex.service.CreditRulesService;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * 
 * @author eclipseeeeeeeeeeeeee
 * 
 */
@Service
public class CreditRulesServiceImpl extends BaseServiceImpl<CreditRules> implements CreditRulesService {
	@Resource AccountService accountService;
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

	}

}
