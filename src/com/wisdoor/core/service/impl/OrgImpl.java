package com.wisdoor.core.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;

@Service
@Transactional
public class OrgImpl extends BaseServiceImpl<Org> implements OrgService {
	@Resource
	UserService userService;

	@Override
	public Org findOrg(String showCoding) {
		String queryString = "from Org c where c.showCoding =? ";
		Org org = selectById(queryString, new String[] { String
				.valueOf(showCoding) });
		return org;
	}

	@Override
	public boolean cancel(long id) throws Exception {
		boolean canceled = false;
		Org org = selectById(id);
		if (null == org) {
			throw new Exception("no such Org which id is " + id + ".");
		}
		if (null != org.getChildren()) {
			// 注销此授权服务中心的所有下级机构
			Iterator<Org> children = org.getChildren().iterator();
			while (children.hasNext()) {
				Org child = children.next();
				cancel(child.getId());
			}
		}
		if (null != org.getUsers()) {
			// 停用此授权服务中心的所有用户
			Iterator<User> users = org.getUsers().iterator();
			while (users.hasNext()) {
				User user = users.next();
				if (user.isEnabled()) {
					user.setEnabled(false);
					user.setUserState(User.STATE_DISABLED);//edit by sxs 2013.05.24 停用用户时更新用户状态为已停用
					userService.update(user);
				}
			}
		}
		org.setState(Org.STATE_CANCELED);
		org.setCanceledDate(new Date());
		this.update(org);
		canceled = true;
		return canceled;
	}

	@Override
	public List<Org> getOrgChildren(String showCoding) {
		Org o = this.findOrg(showCoding);
		if(null!=o){
			String queryString = " from Org c where c.coding like '%"+ o.getId()+"m%' order by c.showCoding";
			return this.getCommonListData(queryString);
		}else{
			return null;
		}
	}
}