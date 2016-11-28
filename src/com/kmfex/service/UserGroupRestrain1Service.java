package com.kmfex.service;

import com.kmfex.model.UserGroupRestrain1;
import com.wisdoor.core.service.BaseService;
/**
 * 
 * @author  
 *
 */
public interface UserGroupRestrain1Service extends BaseService<UserGroupRestrain1>{
	public UserGroupRestrain1 findUserGroup(String groupId,String userName);
}
