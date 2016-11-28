package com.kmfex.service.impl;

import org.springframework.stereotype.Service;

import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberAuditService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
public class MemberAuditImpl extends BaseServiceImpl<MemberAudit> implements
		MemberAuditService {

	@Override
	public MemberAudit findByMemberBase(MemberBase memberBase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberAudit findByMemberBaseId(String memberBaseId) {
		MemberAudit ma = null;
		if (null != memberBaseId) {
			String hql = "from MemberAudit where memberBase.id = '"
					+ memberBaseId
					+ "' order by to_date(to_char(additDate,'yyyy-mm-dd'),'yyyy-mm-dd') desc";
			ma=this.selectByHql(hql);
		}
		return ma;
	}

}
