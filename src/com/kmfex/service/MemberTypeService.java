package com.kmfex.service;

import java.util.List;

import com.kmfex.model.MemberType;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;

/**
 *会员类型接口
 * 
 * @author aora
 **/
public interface MemberTypeService extends BaseService<MemberType> {
	public List<MemberType> getList(User loginuser);

	/**
	 * 根据指定的会员类型名称查找会员类型
	 * 
	 * @param name
	 *            指定的会员类型名称
	 * @return 会员类型对象
	 * */
	public MemberType selectByName(String name);
}
