package com.kmfex.service;

import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.service.BaseService;

/**
 * 定义会员审核相关接口
 * @author 敖汝安
 * @version 2012-03-21
 * */
public interface MemberAuditService extends BaseService<MemberAudit> {

	
	public MemberAudit findByMemberBase(MemberBase memberBase);
	
	public MemberAudit findByMemberBaseId(String memberBaseId);
}
