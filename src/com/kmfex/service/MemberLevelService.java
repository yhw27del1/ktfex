package com.kmfex.service;

import java.util.List;

import com.kmfex.model.MemberLevel;
import com.wisdoor.core.service.BaseService;

public interface MemberLevelService extends BaseService<MemberLevel> {
	/**
	 * 列出所有会员级别
	 * */
	public List<MemberLevel> listAll();
	/**
	 * 列出所有投资方会员级别
	 * */
	public List<MemberLevel> listTAll();
	
	/**
	 * 返回普通会员级别
	 * */
	public MemberLevel getCommonLevel();
	
	public int remove(String remove_id,String target_id);
}
