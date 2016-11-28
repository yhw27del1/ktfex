package com.kmfex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.springframework.stereotype.Service;

import com.kmfex.MemberTotalVO;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

/**
 * @author
 * @version 
 * */
@Service
public class MemberBaseImpl extends BaseServiceImpl<MemberBase> implements
		MemberBaseService {
    @Resource OrgService orgService;
	@Override
	public MemberBase getMemByUser(User u) {
		String hql = "from MemberBase m where m.user.id ='" + u.getId() + "'";
		MemberBase m = null;
		try {
			m = this.selectByHql(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	@Override
	public String getMemberAccountNo(String orgNo) throws Exception {
		Long userNameLong=0L;   
		Org org=orgService.findOrg(orgNo.trim()); 
		if(null==org.getMaxUsername()||"".equals(org.getMaxUsername()))
		{
			String temp=getMaxUserName(orgNo.trim());
			userNameLong=Long.parseLong(orgNo.trim()+temp);
			userNameLong=userNameLong+1;
			org.setMaxUsername(userNameLong+"");
			orgService.update(org);
		}else{ 
			userNameLong=Long.parseLong(org.getMaxUsername());
			userNameLong=userNameLong+1;
			org.setMaxUsername(userNameLong+"");
			orgService.update(org); 
		}
		return userNameLong+"";
 
	}
    public String getMaxUserName(String orgNo){ 
    	String accountNo="";
    	long memberCount = this
		.getScrollDataCount("from MemberBase m where m.orgNo = '"
				+ orgNo.trim() + "'");
		if (memberCount < 10) {
			accountNo += "00000" + memberCount;
		} else if (memberCount >= 10 && memberCount <= 99) {
			accountNo += "0000" + memberCount;
		} else if (memberCount >= 100 && memberCount <= 999) {
			accountNo += "000" + memberCount;
		} else if (memberCount >= 1000 && memberCount <= 9999) {
			accountNo += "00" + memberCount;
		} else if (memberCount >= 10000 && memberCount <= 99999) {
				accountNo += "0" + memberCount;
		} else { 
			//请注意！这里埋下了一个隐患：当同一个机构下的会员数超过一万时， 会员编号不再遵循“六位机构编码+六位顺序号”的规则
			accountNo = String.valueOf(memberCount);
		}
        return accountNo;
    };
	@Override
	public PageView<MemberBase> listMembersForChange(String name, String type,
			String orgNo, int pageSize, int pageNo) {
		PageView<MemberBase> pageView = new PageView<MemberBase>(pageSize,
				pageNo);
		StringBuilder sb = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		sb.append(" 1=1 and state != ?");
		params.add(MemberBase.STATE_NOT_AUDIT);
		// if (null != orgNo && !Org.TOP_ORG_SHOWCODE.equals(orgNo)) {
		// // 非商企业金融交易中心操作员，只能看到自己所在机构的会员
		// sb.append(" and orgno = ?");
		// params.add(orgNo);
		// }
		if (!isEmpty(orgNo)) {
			sb.append(" and user.org.coding like '" + orgNo + "%' ");
		} else {
			sb.append(" and user.org.coding like '@@@@@@@%' ");
		}
		if (!isEmpty(name)) {
			sb
					.append(" and (pName like ? or eName like ? or user.username like ?)");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
		}
		if (!isEmpty(type)) {
			sb.append(" and memberType.code = ?");
			params.add(type);
		}
		sb.append(" order by createdate desc");
		try {
			pageView.setQueryResult(getScrollData(pageView.getFirstResult(),
					pageView.getMaxresult(), sb.toString(), params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageView;
	}

	@Override
	public PageView<MemberBase> listMembersByCondition(String name,
			String typeId, String state, String orgCode, String orgName,
			String province, String city, int pageSize, int pageNo,
			Date startDate, Date endDate,String bank,String signState) {

		SimpleDateFormat startDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		SimpleDateFormat endDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");

		PageView<MemberBase> pageView = new PageView<MemberBase>(pageSize,
				pageNo);
		StringBuilder sb = new StringBuilder(" ");
		List<String> params = new ArrayList<String>();
		if (!isEmpty(orgCode)) {
			sb.append(" user.org.coding like '" + orgCode + "%' ");
		} else {
			sb.append(" user.org.coding like '@@@@@@@%' ");
		}

		if (!isEmpty(orgName)) {
			sb.append(" and user.org.name = ?");
			params.add(orgName);
		}

		if (!isEmpty(province) && !"0".equals(province)) {
			sb.append(" and province = ?");
			params.add(province);
		}

		if (!isEmpty(city) && !"0".equals(city)) {
			sb.append(" and city = ?");
			params.add(city);
		}

		if (!isEmpty(name)) {
			sb
					.append(" and (pName like ? or eName like ? or user.username like ?) ");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
		}

		if (!isEmpty(typeId) && !"0".equals(typeId)) {
			sb.append(" and memberType.id = ?");
			params.add(typeId);
		}
		if (!isEmpty(state) && !"0".equals(state)) {
			sb.append(" and state = ?");
			params.add(state);
		}
		
		if(null!=bank&&!"全部".equals(bank)){
			sb.append(" and banklib.caption= ?");
			params.add(bank);
		}
		
		if(null!=signState&&!"全部".equals(signState)){
			sb.append(" and user.flag= ?");
			params.add(signState);
		}

		if (null != startDate && null != endDate) {
			sb.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null != startDate && null == endDate) {
			sb.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(new Date())
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null == startDate && null != endDate) {
			sb.append(" and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss')");
		}

		sb.append(" order by createdate desc");
		try {
			pageView.setQueryResult(getScrollData(pageView.getFirstResult(),
					pageView.getMaxresult(), sb.toString(), params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageView;
	}
	
	public List<Map<String, Object>> listMembersByCondition2(String name,String jingbanren,
			String typeId, String state, String orgCode, String orgName,
			String province, String city, int pageSize, int pageNo,
			Date startDate, Date endDate,String bank,String signState,String channel) {
		List<Map<String, Object>> result = null;
		try {
			
		ArrayList<Object> args_list = new ArrayList<Object>();
		SimpleDateFormat startDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		SimpleDateFormat endDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");

		//PageView<MemberBase> pageView = new PageView<MemberBase>(pageSize,pageNo);
		StringBuilder sb = new StringBuilder("");
		if (!isEmpty(orgCode)) {
			sb.append(" o.coding like ? ");
			args_list.add(orgCode+"%");
		} else {
			sb.append(" o.coding like '@@@@@@@%' ");
		}

		if (!isEmpty(orgName)) {
			sb.append(" and o.name_ = ? ");
			args_list.add(orgName);
		}
		
		if (!isEmpty(jingbanren)) {
			sb.append(" and t.jingbanren = ? ");
			args_list.add(jingbanren);
		}

		if (!isEmpty(province) && !"0".equals(province)) {
			sb.append(" and t.province = ? ");
			args_list.add(province);
		}

		if (!isEmpty(city) && !"0".equals(city)) {
			sb.append(" and t.city = ? ");
			args_list.add(city);
		}

		if (!isEmpty(name)) {
			sb.append(" and (t.pName like ? or t.eName like ? or u.username like ?) ");
			args_list.add("%" + name + "%");
			args_list.add("%" + name + "%");
			args_list.add("%" + name + "%");
		}

		if (!isEmpty(typeId) && !"0".equals(typeId)) {
			sb.append(" and tmt.id = ? ");
			args_list.add(typeId);
		}
		
		if (!isEmpty(state) && !"0".equals(state)) {
			sb.append(" and t.state = ? ");
			args_list.add(state);
		}
		
		/*if(null!=channel&&!"全部".equals(channel)&&!"".equals(channel)){
			if("1".equals(channel)){
				sb.append(" and u.channel = 1 ");
			}else if("2".equals(channel)){
				sb.append(" and u.channel = 2 ");
			}
		}*/
		
		/*if(null!=bank&&!"全部".equals(bank)&&!"".equals(bank)){
			if("1".equals(bank)){
				sb.append(" and b.caption = '华夏银行' ");
			}else if("2".equals(bank)){
				sb.append(" and b.caption = '招商银行' ");
			}else{
				sb.append(" and b.caption = 'xx银行' ");
			}
		}*/
		
		if(null!=signState&&!"全部".equals(signState)&&!"".equals(signState)){
			sb.append(" and u.flag = ? ");
			args_list.add(signState);
		}

		if (null != startDate && null != endDate) {
			sb.append(" and (t.createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and t.createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null != startDate && null == endDate) {
			sb.append(" and (t.createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and t.createDate <= to_date('"
					+ endDateFormat.format(new Date())
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null == startDate && null != endDate) {
			sb.append(" and t.createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss')");
		}

		sb.append(" order by createdate desc");
		
		result = this.queryForList("u.channel,t.id,u.username,u.realname,a.balance_,t.createdate,t.pSex,t.idCardNo," +
				"tml.levelname,tmt.name as tyname,a.frozenAmount,t.provinceName,t.cityName," +
				"t.category,o.name_ as orgname,t.state,t.jingbanren,u.flag,u.accountNo,u.signBank,u.signType," +
				"t.pName,t.eName,t.pMobile,t.eMobile,t.eContactMobile,t.qq,t.email,t.firstauditdate,t.recentauditdate",
				
				" t_member_base t left join sys_user u on t.user_id = u.id" +
				" left join sys_org o on u.org_id = o.id" +
				" left join t_member_types tmt on t.membertype_id = tmt.id " +
				" left join t_member_level tml on t.memberlevel_id = tml.id " +
				" left join t_banklibrary b on t.bank_id = b.id " +
				" left join sys_account a on u.useraccount_accountid_ = a.accountid_",
				sb.toString(),args_list.toArray(),pageNo,pageSize,false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public PageView<MemberBase> listNotAuditedMembers(String name, String type,
			String orgNo, int pageSize, int pageNo) {
		PageView<MemberBase> pageView = new PageView<MemberBase>(pageSize,
				pageNo);
		   LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		   orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 and (state = ? or state = ?)");
			List<String> params = new ArrayList<String>();
			params.add(MemberBase.STATE_NOT_AUDIT);
			params.add(MemberBase.STATE_NOT_PASS_AUDIT);

			// if (null != orgNo && !Org.TOP_ORG_SHOWCODE.equals(orgNo)) {
			// // 非商企业金融交易中心操作员，只能看到自己所在机构的会员
			// sb.append(" and orgno = ?");
			// params.add(orgNo);
			// }

			if (!isEmpty(orgNo)) {
				sb.append(" and user.org.coding like '" + orgNo + "%' ");
			} else {
				sb.append(" and user.org.coding like '@@@@@@@%' ");
			}

			if (!isEmpty(name)) {
				sb.append("and (pName like ? or eName like ? or user.username like ?) ");
				params.add("%" + name + "%");
				params.add("%" + name + "%");
				params.add("%" + name + "%");
			}

			if (!isEmpty(type)) {
				sb.append(" and memberType.code = ?");
				params.add(type.toUpperCase());
			} else {
				// 默认显示非投资方和融资方
				sb.append(" and memberType.code != ? and memberType.code != ? ");
				params.add(MemberType.CODE_INVESTORS);
				params.add(MemberType.CODE_FINANCING);
			}
			try {
				pageView.setQueryResult(getScrollData(pageView.getFirstResult(),
						pageView.getMaxresult(), sb.toString(), params, orderby));
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return pageView;
	}

	@Override
	public MemberBase findByBusCode(String busCode) {
		String hql = "from MemberBase where category = '"
				+ MemberBase.CATEGORY_ORG + "' and eBusCode = '" + busCode
				+ "'";
		return this.selectByHql(hql);
	}

	@Override
	public MemberBase findByIdCard(String idCard) {
		String hql = "from MemberBase where category = '"
				+ MemberBase.CATEGORY_PERSON + "' and idCardNo = '" + idCard
				+ "'";
		return this.selectByHql(hql);
	}

	@Override
	public MemberBase restPasswordQuery(String name, String userName,
			String idCardNo) {
		String hql = "from MemberBase o where ( o.eName = '" + name
				+ "' or o.pName = '" + name + "') and o.idCardNo = '"
				+ idCardNo + "' and o.user.username = '" + userName + "'";
		return this.selectByHql(hql);
	}

	@Override
	public List<MemberBase> listAllMembersByCondition(String name,
			String typeId, String state, String orgCode, String orgName,
			String province, String city, Date startDate, Date endDate) {
		SimpleDateFormat startDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		SimpleDateFormat endDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		StringBuilder hql = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		hql.append("from MemberBase where 1=1 ");
		params.add("1");
		// 非商企业金融交易中心操作员，只能看到自己所在机构的会员
		if (!isEmpty(orgCode)) {
			hql.append(" and user.org.coding like '" + orgCode + "%' ");
		} else {
			hql.append(" and user.org.coding like '@@@@@@@%' ");
		}

		if (!isEmpty(orgName)) {
			hql.append(" and user.org.name = '" + orgName + "'");
			params.add(orgName);
		}

		if (!isEmpty(province) && !"0".equals(province)) {
			hql.append(" and province = '" + province + "'");
			params.add(province);
		}

		if (!isEmpty(city) && !"0".equals(city)) {
			hql.append(" and city = '" + city + "'");
			params.add(city);
		}

		if (!isEmpty(name)) {
			hql.append(" and (pName like '%" + name + "%' or eName like '%"
					+ name + "%' or user.username like '%" + name + "%') ");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
			params.add("%" + name + "%");
		}
		if (!isEmpty(typeId) && !"0".equals(typeId)) {
			hql.append(" and memberType.id = '" + typeId + "'");
			params.add(typeId);
		}
		if (!isEmpty(state) && !"0".equals(state)) {
			hql.append(" and state = '" + state + "'");
			params.add(state);
		}
		if (null != startDate && null != endDate) {

			hql.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null != startDate && null == endDate) {
			hql.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(new Date())
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null == startDate && null != endDate) {
			hql.append(" and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss')");
		}
		hql.append(" order by createdate desc");

		return this.getCommonListData(hql.toString());
	}

	@Override
	public List<MemberTotalVO> totalMembers(Date startDate, Date endDate,String openOrgCode) {
		Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		final OutParameterModel outParameter = new OutParameterModel(5, OracleTypes.CURSOR);
		inParamList.put(1, new java.sql.Date(startDate.getTime()));
		inParamList.put(2, new java.sql.Date(endDate.getTime()));
		if(null!=openOrgCode){
			List<MemberTotalVO> ms = new ArrayList<MemberTotalVO>();
			String[] orgs = openOrgCode.split(",");
			String[] type = new String[]{"all","T","R","Y","Q"};
			for(String org:orgs){//机构过滤
				String[] code = org.split("@");
				MemberTotalVO mo = new MemberTotalVO(code[0],code[1]);
				inParamList.put(3, code[0]);
				for(String t:type){//会员类型过滤
					inParamList.put(4, t);
					ArrayList<LinkedHashMap<String,Object>> reult = this.callProcedureForList("PKG_Account.totalMembers_total", inParamList, outParameter);
					if(null!=reult){
						LinkedHashMap<String,Object> a0 = reult.get(0);
						Object a  = a0.get("total");
						if(null!=a){
							if(t.equals(type[0])){
								mo.setTotal(Integer.parseInt(a.toString()));
							}else if(t.equals(type[1])){
								mo.setTotal_T(Integer.parseInt(a.toString()));
							}else if(t.equals(type[2])){
								mo.setTotal_R(Integer.parseInt(a.toString()));
							}else if(t.equals(type[3])){
								mo.setTotal_D(Integer.parseInt(a.toString()));
							}else if(t.equals(type[4])){
								mo.setTotal_Y(Integer.parseInt(a.toString()));
							}else if(t.equals(type[5])){
								mo.setTotal_Q(Integer.parseInt(a.toString()));
							}
						}
					}
				}
				ms.add(mo);
			}
			return ms;
		}else{
			return null;
		}
	}

}
