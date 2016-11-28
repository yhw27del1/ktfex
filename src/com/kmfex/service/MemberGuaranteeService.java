package com.kmfex.service;

import java.util.List;

import com.kmfex.model.MemberGuarantee;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.BaseService;

/**
 * 担保公司会员担保情况
 * @author  
 *
 */
public interface MemberGuaranteeService extends BaseService<MemberGuarantee>{
	/** 
	 * 得到最后一次新增的记录
	 */
	public MemberGuarantee getLastByMemberGuarantee(String memberBaseId); 
	
	/**
	 * 查询最新的记录
	 * */
	public PageView<MemberGuarantee> listByLatest(String keyword,int pageSize,int pageNo);
	
}
