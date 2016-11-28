package com.wisdoor.core.service.impl;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.utils.IpAddrUtil;

@Service
public class LogsServiceImpl extends BaseServiceImpl<Logs> implements LogsService {

	@Override
	@Transactional
	public Logs log(String operate) throws Exception {
		User operator = null;
		Object user_obj = null;
		user_obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user_obj != null && user_obj instanceof User) {
			operator = (User) user_obj;
		}
		return log(operate, operator);
	}
	
	
	
	
	
	@Override
	@Transactional
	public Logs log(String operate, User user) throws Exception {
		Logs l = null;
		try {
			if (user != null) {
				l = new Logs();
				l.setOperate(operate);
				l.setOperator(user);
				l.setIp(IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
				this.insert(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return l;
	}
	
	
}
