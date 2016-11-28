package com.kmfex.service;

import com.kmfex.model.UserGroup;
import com.wisdoor.core.service.BaseService;
/**
 * 
 * @author  
 *
 */
public interface UserGroupService extends BaseService<UserGroup>{
	public UserGroup findUserGroup(String groupId,String userId);

}
