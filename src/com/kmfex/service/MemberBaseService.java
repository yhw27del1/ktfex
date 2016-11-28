package com.kmfex.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kmfex.MemberTotalVO;
import com.kmfex.model.MemberBase;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.BaseService;

/**
 * 会员基本信息接口定义
 * 
 * @author 敖汝安
 * @version 2012-03-20
 * */
public interface MemberBaseService extends BaseService<MemberBase> {
	public MemberBase getMemByUser(User u);  

	/**
	 * 返回11位指定编号的机构的下一会员的会员编号(会员对应的用户名)。
	 * 会员编号的编码规则为：6位开户机构编号+ 5位流水号(该机构下的当前会员总数)
	 * 
	 * @param orgNo
	 *            机构(服务中心)编号
	 * @return 11位下一会员的会员编号(会员对应的用户名)
	 * */
	public String getMemberAccountNo(String orgNo) throws Exception;

	/**
	 * 按条件列出需要变更资料的会员
	 * 
	 * @param name
	 *            会员名
	 * @param type
	 *            会员类别
	 * @param orgNo
	 *            页码
	 * @param pageSize
	 *            页大小
	 * @param pageNo
	 *            页码
	 * */
	public PageView<MemberBase> listMembersForChange(String name, String type,
			String orgNo, int pageSize, int pageNo);

	/**
	 * 分页列出满足条件的会员
	 * 
	 * @param name
	 *            会员(用户)名
	 * @param typeId
	 *            会员类别代码
	 * @param state
	 *            状态
	 * @param orgCode
	 *            查询机构代码
	 * @param orgName
	 *            开户机构编码
	 * @param province
	 *            省份编码
	 * @param city
	 *            城市代码
	 * @param pageSize
	 *            页大小
	 * @param pageNo
	 *            页码
	 * */
	public PageView<MemberBase> listMembersByCondition(String name,
			String typeId, String state, String orgCode, String orgName,
			String province, String city, int pageSize, int pageNo,
			Date startDate, Date endDate,String bank,String signState);

	public PageView<MemberBase> listNotAuditedMembers(String name, String type,
			String orgNo, int pageSize, int pageNo);

	public MemberBase findByIdCard(String idCard);

	public MemberBase findByBusCode(String busCode);

	public MemberBase restPasswordQuery(String name, String userName,
			String idCardNo);

	/**
	 * 列出所有满足条件的会员
	 * 
	 * @param name
	 *            会员(用户)名
	 * @param typeId
	 *            会员类别代码
	 * @param state
	 *            状态
	 * @param orgCode
	 *            查询机构代码
	 * @param orgName
	 *            开户机构编码
	 * @param province
	 *            省份编码
	 * @param city
	 *            城市代码
	 * */
	public List<MemberBase> listAllMembersByCondition(String name,
			String typeId, String state, String orgCode, String orgName,
			String province, String city, Date startDate, Date endDate);
	
	//会员统计，1 会员总数；2 融资方总数；3 投资人总数；4 担保公司总数
	public List<MemberTotalVO> totalMembers(Date startDate,Date endDate,String openOrgCode);

	public List<Map<String, Object>> listMembersByCondition2(String keyword, String jingbanren,String memberTypeId,
			String memberState, String coding, String orgName,
			String provinceCode, String cityCode, int i, int j, Date startDate,
			Date endDate, String bank, String signState,String channel);
}
