package com.kmfex.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.MemberGuarantee;
import com.kmfex.service.MemberGuaranteeService;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * 担保公司会员担保情况
 * 
 * @author  ,aora
 * 
 */
@Service
public class MemberGuaranteeImpl extends BaseServiceImpl<MemberGuarantee>
		implements MemberGuaranteeService {
	@Override
	@Transactional
	public MemberGuarantee getLastByMemberGuarantee(String memberBaseId) {
		String hql = "from MemberGuarantee f where f.memberBase.id='"
				+ memberBaseId + "' order by f.createDate desc ";
		List<MemberGuarantee> ls = this.getCommonListData(hql);
		if (null != ls && ls.size() > 0) {
			return ls.get(0);
		} else {
			return null;
		}
	}

	@Override
	public PageView<MemberGuarantee> listByLatest(String keyword, int pageSize,
			int pageNo) {
		PageView<MemberGuarantee> pageView = new PageView<MemberGuarantee>(
				pageSize, pageNo);
		try {
			List<String> params = new ArrayList<String>();
			StringBuilder hql = new StringBuilder("state = ? ");
			params.add(MemberGuarantee.STATE_LATEST);
			if (!isEmpty(keyword)) {
				hql
						.append(" and (memberBase.pName like ? or memberBase.eName like ? or memberBase.user.username like ? )");
				params.add("%" + keyword + "%");
				params.add("%" + keyword + "%");
				params.add("%" + keyword + "%");
			}
			hql.append(" order by createDate desc");

			pageView.setQueryResult((getScrollData(pageView.getFirstResult(),
					pageView.getMaxresult(), hql.toString(), params)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pageView;
	}

}
