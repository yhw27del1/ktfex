package com.kmfex.service.impl;

import org.springframework.stereotype.Service;

import com.kmfex.model.UserGroupRestrain1;
import com.kmfex.service.UserGroupRestrain1Service;
import com.wisdoor.core.service.impl.BaseServiceImpl;
/** 
 * @author  
 *
 */
@Service
public class UserGroupRestrain1Impl extends BaseServiceImpl<UserGroupRestrain1> implements UserGroupRestrain1Service {

	public UserGroupRestrain1 findUserGroup(String groupId,String userName){
		String queryString = "from UserGroupRestrain1 c where c.groupId ='"+groupId+"' and c.user.username='"+userName+"'";
		UserGroupRestrain1 userGroup=this.selectByHql(queryString);
		return userGroup;
	}

}
