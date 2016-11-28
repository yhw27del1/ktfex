package com.kmfex.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.Constant;
import com.kmfex.model.DaiLiFeePercent;
import com.kmfex.model.MemberBase;
import com.kmfex.model.Region;
import com.kmfex.service.DaiLiFeePercentService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.RegionService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Contact;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.RoleService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.MD5;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;

/***
 * 授权服务机构类
 * 
 * @author 
 * @author 修改sqList()方法，增加按机构代码(showCoding)查询功能。 2012-08-13
 *         修改buildShowCoding(String
 *         showCoding)方法，以实现机构代码的生成规则为：市（区）的代码+2位流水号。
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class SqOrgAction extends BaseAction implements Preparable {
	@Resource
	OrgService orgService;
	@Resource
	UserService userService;
	@Resource
	RoleService roleService;
	@Resource MemberBaseService memberBaseService;
	private long id;
	private long parentId = 1l;
	private long parent_id;
	private Org org;
	private String keyWord = "";
	private String showCoding = "";

	private String orgName;
	private String username;
	private String password;
	
	private String guarantee_id;
	
	
	//机构类型
	private List<CommonVo> orgtypeList;

	@Resource
	private RegionService regionService;
	/**
	 * 省份
	 * */
	private List<Region> regions_province = new ArrayList<Region>();

	/**
	 * 城市
	 * */
	private List<Region> regions_city = new ArrayList<Region>();

	public void prepare() throws Exception {
		if (0 != id) {
			org = orgService.selectById(id);
		} else {
			org = new Org();
		}

		regions_province = regionService.getScrollDataCommon(
				"from Region where areaparentcode = '0000' order by areacode",
				new String[] {});
		regions_city = regionService.getScrollDataCommon(
				"from Region where areaparentcode = ' ' order by areacode",
				new String[] {});
	}

	/** 得到机构编码 **/
	public String ajaxShowCoding() throws Exception {
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"message\":\"" + buildShowCoding(showCoding) + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"message\":\"0\"}");
		}
		return null;
	}

	public String sqList() {
		try {
			PageView<Org> pageView = new PageView<Org>(getShowRecord(),
					getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			// 加入授权服务的过滤
			StringBuilder sb = new StringBuilder(
					" 1=1 and coding != '1' and coding != '1m1196' ");
			User u;
			try {
				u = (User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
			} catch (Exception e) {
				e.printStackTrace();
				return "sqList";
			}
			if (null == u) {
				return "sqList";
			}
			// if (null != u.getOrg().getCoding() &&
			// !"".equals(u.getOrg().getCoding())) {
			// sb.append(" and o.createBy.org.coding like '" +
			// u.getOrg().getCoding()+ "%' ");
			// } else {
			// sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			// }
			if (null != u.getOrg().getCoding()
					&& !"".equals(u.getOrg().getCoding())) {
				sb.append(" and o.coding like '" + u.getOrg().getCoding()
						+ "%' ");
			} else {
				sb.append(" and o.coding like '@@@@@@@%' ");
			}
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.name like ?");
				params.add("%" + keyWord + "%");
				sb.append(" )");
			}
			if (null != showCoding && !"".equals(showCoding.trim())) {
				showCoding = showCoding.trim();
				sb.append(" and (");
				sb.append(" o.showCoding like ?");
				params.add("%" + showCoding + "%");
				sb.append(" )");
			}
			sb.append(" order by createDateBy desc");
			QueryResult<Org> qs = orgService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby);
			/*
			 * for(Org o:qs.getResultlist()) {
			 * if(Constant.FWZX_ORG_CODE.equals(o.getParent().getCoding())){
			 * o.setParent(null); } }
			 */
			pageView.setQueryResult(qs);
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sqList";
	}

	/** 新增 **/
	public String sqAdd() throws Exception {
		try {
			User u;
			User user = new User();
			try {
				u = (User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				org.setCreateBy(u);
				org.setCreateDateBy(new Date());
				user.setCreateBy(u);
				user.setCreateDateBy(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(this.guarantee_id) && "0".equals(this.org.getType())){
				MemberBase guarantee = this.memberBaseService.selectById(this.guarantee_id);
				this.org.setGuarantee(guarantee);
			}else{
				this.org.setGuarantee(null);
			}
			org.setParent(orgService.findOrg(Constant.FWZX_ORG_CODE));
			org.setParentCoding(org.getShowCoding());
			orgService.insert(org);
			if (null != org.getParent()) {
				org.setCoding(org.getParent().getCoding() + "m" + org.getId());// 编码
				orgService.update(org);
			}

			// 创建用户
			user.setTypeFlag("2");// 2后台用户
			user.setPassword(MD5.MD5Encode("123456"));

			this.orgName = org.getName();
			this.username = org.getShowCoding();
			this.password = "123456";

			user.setOrg(org);
			user.setRealname(org.getName());
			user.setUsername(org.getShowCoding());
			if("0".equals(org.getType())){//类型为0，保荐机构
				user.addRole(roleService.selectByHql("from Role c where c.name like '%担保机构%'"));
				user.addRole(roleService.selectByHql("from Role c where c.name like '%会员姓名%'"));
				user.addRole(roleService.selectByHql("from Role c where c.name like '%会员手机号%'"));
				
			}else if("1".equals(org.getType())){//类型为1，投资服务中心
				user.addRole(roleService.selectByHql("from Role c where c.name like '%投资服务中心%'"));
				user.addRole(roleService.selectByHql("from Role c where c.name like '%会员姓名%'"));
				user.addRole(roleService.selectByHql("from Role c where c.name like '%会员手机号%'"));
				
			}
			
			
			
			// 联系方式
			if (null != org.getOrgContact()) {
				Contact userContact = new Contact();
				userContact.setAddress(org.getOrgContact().getAddress());
				userContact.setMobile(org.getOrgContact().getMobile());
				userContact.setPhone(org.getOrgContact().getPhone());
				userContact.setPostalcode(org.getOrgContact().getPostalcode());
				user.setUserContact(userContact);
				userContact.setOrg(org);
				user.setUserContact(userContact);

			}
			userService.insert(user);
			// 更新编码
			if (null != org) {
				user.setCoding(org.getCoding() + "m" + user.getId());// 用户编码
				userService.update(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sqAdd";
	}

	/** 修改 **/
	public String sqEdit() throws Exception {
		try {
			if (0 != parent_id) {
				org.setParent(orgService.selectById(parent_id));// 设置父机构
			}

			if (null != org.getParent()) {
				org.setCoding(org.getParent().getCoding() + "m" + id);// 编码
			}
			if(StringUtils.isNotBlank(this.guarantee_id) && "0".equals(this.org.getType())){
				MemberBase guarantee = this.memberBaseService.selectById(this.guarantee_id);
				this.org.setGuarantee(guarantee);
			}else{
				this.org.setGuarantee(null);
			}
			
			orgService.update(org);

			// 更新用户
			User user = userService.findUser(org.getShowCoding());
			user.setRealname(org.getName());
			if("0".equals(org.getType())){//类型为0，担保机构
				user.addRole(roleService.selectByHql("from Role c where c.name like '%担保机构%'"));
			}else if("1".equals(org.getType())){//类型为1，授权服务中心
				user.addRole(roleService.selectByHql("from Role c where c.name like '%投资服务中心%'"));
			}else if("2".equals(org.getType())){//类型为2，担保+投资服务中心
				user.addRole(roleService.selectByHql("from Role c where c.name like '%投资服务中心%'"));
			}
			// 联系方式
			if (null != org.getOrgContact()) {
				Contact userContact = new Contact();
				userContact.setAddress(org.getOrgContact().getAddress());
				userContact.setMobile(org.getOrgContact().getMobile());
				userContact.setPhone(org.getOrgContact().getPhone());
				userContact.setPostalcode(org.getOrgContact().getPostalcode());
				user.setUserContact(userContact);
				userContact.setOrg(org);
				user.setUserContact(userContact);
			}
			userService.update(user);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sqEdit";
	}

	public String ui() throws Exception {
		return "ui";
	}

	/** 删除 **/
	public String del() throws Exception {
		try {
			List<Org> orgList = orgService
					.getCommonListData(" from Org o where o.coding like '"
							+ org.getCoding() + "%'");
			for (Org o : orgList) {
				orgService.delete(o.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "del";
	}

	/**
	 * 注销指定的授权服务中心
	 * */
	public String cancel() {
		try {
			orgService.cancel(id);
		} catch (Exception e) {
			return sqList();
		}
		return sqList();
	}

	private Set<Role> inUserRoleList;//已选角色
	private List<Role> notInUserRoleList;//未选角色
	private long orgId;
	
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public Set<Role> getInUserRoleList() {
		return inUserRoleList;
	}
	public void setInUserRoleList(Set<Role> inUserRoleList) {
		this.inUserRoleList = inUserRoleList;
	}
	public List<Role> getNotInUserRoleList() {
		return notInUserRoleList;
	}
	public void setNotInUserRoleList(List<Role> notInUserRoleList) {
		this.notInUserRoleList = notInUserRoleList;
	}
	
	
	/**跳转页面**/
	public String newOrgUserUI() throws Exception { 
		try {
			User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			this.inUserRoleList=new HashSet(this.roleService.findOrgInRole());
			//System.out.println("SqOrgAction："+inUserRoleList.size());
			this.notInUserRoleList=this.roleService.findNotInOrgRole(this.inUserRoleList);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		this.org = this.orgService.selectById(this.orgId);
		try {
			PageView<User> pageView = new PageView<User>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 and o.typeFlag='2' and o.org.id="+orgId+""); //后台用户
			List<String> params = new ArrayList<String>(); 
			pageView.setQueryResult(userService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return "newOrgUserUI"; 
	} 
	
	public String daiLiFeeContractList(){
		return "daiLiFeeContractList";
	}
	
	private String queryByOrgCode;
	
	/**
	 * 2013年12月26日新增  维护代理费  查看代理费列表
	 */
	public void getDaiLiFeeList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			String field = "*";
			String table = "T_DAILIFEI_PERCENT dlf ";
			StringBuilder sb = new StringBuilder(" 1=1 ");
			
			User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			String org = user.getOrg().getId()+"";
			
			//开户日期区间
			if( null != org){
				if(!"1".equals(org) && !"1196".equals(org)){
					sb.append(" and dlf.orgShowCoding = '"+ user.getOrg().getShowCoding() + "'");
				}
			}  
			
			if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
				sb.append(" and dlf.orgShowCoding = '" + queryByOrgCode + "'");   
			} 
			
			List<Map<String, Object>> result = this.orgService.queryForList(field,table,sb.toString(), getPage(), rows);
			//添加代理费
			int total = this.orgService.queryForListTotal("id","T_DAILIFEI_PERCENT dlf ",sb.toString());  
		 
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			 
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String _id;
	private DaiLiFeePercent dlf;
	@Resource
	DaiLiFeePercentService daiLiFeePercentService;
	
	public String modify(){
		if (this._id != null && !"".equals(this._id)) {
			dlf = this.daiLiFeePercentService.selectById(_id);
			//System.out.println("modify");
		} else {
			dlf = new DaiLiFeePercent();
			//System.out.println("add");
		}
		return "dlfmodify";
	}
	
	public String save() throws SQLException{
		User loginUser = (User) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
		try{
			if (this.dlf.getId() != null && !"".equals(this.dlf.getId())) {
				this.daiLiFeePercentService.update(dlf);
			} else {
				this.daiLiFeePercentService.insert(dlf);
			}
		}catch (EngineException e) {
			addActionError("保存失败，请重试");
			e.printStackTrace();
		}
		return "toDailifeiList";
	}
	
	public String delContract(){
		try {
			daiLiFeePercentService.delete(_id);
		} catch (EngineException e) {
			e.printStackTrace();
			addActionError("删除失败，请重试");
		}
		return "toDailifeiList";
	}
	
	
	
	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}

	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		_id = id;
	}

	public DaiLiFeePercent getDlf() {
		return dlf;
	}

	public void setDlf(DaiLiFeePercent dlf) {
		this.dlf = dlf;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parentId) {
		parent_id = parentId;
	}

	public String getShowCoding() {
		return showCoding;
	}

	public void setShowCoding(String showCoding) {
		this.showCoding = showCoding;
	}

	public List<Region> getRegions_province() {
		return regions_province;
	}

	public void setRegions_province(List<Region> regionsProvince) {
		regions_province = regionsProvince;
	}

	public List<Region> getRegions_city() {
		return regions_city;
	}

	public void setRegions_city(List<Region> regionsCity) {
		regions_city = regionsCity;
	}

	/**
	 * 生成机构代码。 机构代码的生成规则为：市（区）的代码+3位流水号
	 * */
	private String buildShowCoding(String showCoding) throws Exception {
		StringBuffer code = new StringBuffer();
		long count = 0;
		count = userService
				.getScrollDataCount(" from Org o where o.showCoding like '"
						+ showCoding + "%' and o.parent is not null");
		if(count>100&&"5301".equalsIgnoreCase(showCoding)){
			count = userService
			.getScrollDataCount(" from Org o where o.showCoding like '5300%'");
			count=count+1;
			code.append("5300");
		}else {
			code.append(showCoding);
		}
		code.append(StringUtils.fillZero(3, (count) + ""));

		return code.toString();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<CommonVo> getOrgtypeList() {
		if (orgtypeList != null)
			return orgtypeList;
		orgtypeList = new ArrayList<CommonVo>();
		orgtypeList.add(new CommonVo("1", "授权服务中心"));
		orgtypeList.add(new CommonVo("0", "保荐机构"));
		return orgtypeList;
	}

	public void setOrgtypeList(List<CommonVo> orgtypeList) {
		this.orgtypeList = orgtypeList;
	}

	public String getGuarantee_id() {
		return guarantee_id;
	}

	public void setGuarantee_id(String guarantee_id) {
		this.guarantee_id = guarantee_id;
	}
  

}
