package com.kmfex.service.impl;

import org.springframework.stereotype.Service;

import com.kmfex.model.UserGroup;
import com.kmfex.service.UserGroupService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
/** 
 * @author   
 */
@Service
public class UserGroupImpl extends BaseServiceImpl<UserGroup> implements UserGroupService {
	public UserGroup findUserGroup(String groupId,String userId){
		String queryString = "from UserGroup c where c.groupId ='"+groupId+"' and c.user.id='"+userId+"'";
		UserGroup userGroup=this.selectByHql(queryString);
		return userGroup;
	}
	

}
