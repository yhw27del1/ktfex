package com.kmfex.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.model.Group;
import com.kmfex.model.GroupRestrain;
import com.kmfex.model.MemberType;
import com.kmfex.model.UserGroup;
import com.kmfex.model.UserGroupRestrain1;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.GroupRestrainService;
import com.kmfex.service.GroupService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberTypeService;
import com.kmfex.service.UserGroupRestrain1Service;
import com.kmfex.service.UserGroupService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.struts.BaseAction;
/**
 * 用户分组管理
 * @author  
 * 2013年07月31日15:45   新增方法stop(),start()停用启用组
 * 2013年11月13日14:39   缓存修改了addUser()、addUsers()方法   
 */
@Controller
@SuppressWarnings("serial")
@Scope("prototype")
public class UserGroupAction extends BaseAction  implements Preparable{
	@Resource GroupService groupService; 
	@Resource UserGroupService userGroupService; 
	@Resource UserGroupRestrain1Service userGroupRestrain1Service; 
	@Resource GroupRestrainService groupRestrainService; 
	@Resource UserService userService;
	@Resource
	FinancingCacheService financingCacheService;
	@Resource
	private MemberBaseService memberBaseService;
	@Resource
	private MemberTypeService memberTypeService;
	private String id;
	private String groupId;
	private String groupUserId;
	private String username;
	private String keyWord;
	private Group group; 
	/*
	 *统计某投资人投标次数
	 */
	private int investCount = 0; 
	/*
	 *单笔允许最大金额 
	 */
	private double investMaxMoney = 2000;
	
	@Resource FinancingRestrainService financingRestrainService;
	public String list(){
		try {
			PageView<Group> pageview = new PageView<Group>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put( "createtime","desc");
			StringBuilder sb = new StringBuilder(" 1=1 "); 
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.name like ?");
				params.add("%" + keyWord + "%");
			}
			QueryResult<Group> lists = groupService.getScrollData(pageview.getFirstResult(), pageview.getMaxresult(), sb.toString(), params, orderby);
			for(Group g:lists.getResultlist()){
				if (null != g.getGroupRestrainId() && !"".equals(g.getGroupRestrainId())) { 
				  g.setGroupRestrainName(groupRestrainService.selectById(g.getGroupRestrainId()).getName());
				} 
			}
			pageview.setQueryResult(lists);
			ServletActionContext.getRequest().setAttribute("pageView", pageview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	public String groupSelectUserList(){
		try {
			PageView<UserGroup> pageview = new PageView<UserGroup>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put( "id","desc");
			StringBuilder sb = new StringBuilder(" 1=1 "); 
			List<String> params = new ArrayList<String>();
			sb.append(" and  o.groupId='"+id+"'");    
			
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  (o.user.realname like "+"'%" + keyWord + "%'");
				sb.append(" or  o.user.username like "+"'%" + keyWord + "%') ");
			}
			
			
			QueryResult<UserGroup> lists = userGroupService.getScrollData(pageview.getFirstResult(), pageview.getMaxresult(), sb.toString(), params, orderby);
			pageview.setQueryResult(lists);
			ServletActionContext.getRequest().setAttribute("pageView", pageview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "groupSelectUserList";
	}
	
	
	public String groupSelectUser1List(){
		try {
			PageView<UserGroupRestrain1> pageview = new PageView<UserGroupRestrain1>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put( "id","desc");
			StringBuilder sb = new StringBuilder(" 1=1 "); 
			List<String> params = new ArrayList<String>();
			sb.append(" and  o.groupId='"+id+"'");    
			Group group=groupService.selectById(id);
			if(null!=group){
				GroupRestrain groupRestrain=groupRestrainService.selectById(group.getGroupRestrainId());
				if(null!=groupRestrain){
					JSONObject jsons= JSONObject.fromObject(groupRestrain.getRestrainJson());  
					investCount=jsons.getInt("investMaxCount");
					investMaxMoney=jsons.getDouble("investMaxMoney"); 
				}
			}
			
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  (o.user.realname like "+"'%" + keyWord + "%'");
				sb.append(" or  o.user.username like "+"'%" + keyWord + "%') ");
			}
			
			QueryResult<UserGroupRestrain1> lists = userGroupRestrain1Service.getScrollData(pageview.getFirstResult(), pageview.getMaxresult(), sb.toString(), params, orderby);
			pageview.setQueryResult(lists);
			ServletActionContext.getRequest().setAttribute("pageView", pageview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "groupSelectUser1List";
	}
	public void prepare() throws Exception {
		if (null==id|| "".equals(id)) { 
	    	   group=new Group();
	       }else{
	    	   group=groupService.selectById(id);
	       }
	}    
	public String edit() throws Exception {
		try { 
			if (null==id|| "".equals(id)) { 
				groupService.insert(group); 
			}else{ 
				groupService.update(group); 
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 	 
	public String stop() throws Exception {
		try {  
			group.setState(0);
			groupService.update(group);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "分组停用成功!");
		} catch (Exception e) { 
			e.printStackTrace(); 
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "分组停用失败!");
		}   
		return "stop"; 
	} 	 
	
	public String start() throws Exception {
		try {   
			group.setState(1);
			groupService.update(group);  
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "分组启用成功!");
		} catch (Exception e) { 
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "分组启用失败!");
		}   
		return "start"; 
	} 	 
 
	public String del() throws Exception {
		try {  
			groupService.delete(id); 
	 	} catch (Exception e) { 
			e.printStackTrace(); 
		}   
		return "del"; 
	} 	 
	
	public String delUser() throws Exception {
		try { 
			userGroupService.delete(groupUserId);
		} catch (Exception e) { 
			e.printStackTrace(); 
		}   
		return "delUser"; 
	} 	 
	public String delUser1() throws Exception {
		try { 
			userGroupService.delete(groupUserId);
		} catch (Exception e) { 
			e.printStackTrace(); 
		}   
		return "delUser1"; 
	} 	 
	public String addUser() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：加入用户成功");
        //用户是否登陆 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
	        result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
	    } 
		
		try {
			User user=userService.findUser(username);
			if(null!=user){
				
				if(!"T".equals(user.getUserType())){
			        result.element("code", "0"); 
			        result.element("tip", "操作提示：只有投资人才能加入分组!");
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString()); 
					return null;
				}
				
				UserGroup userGroup=userGroupService.findUserGroup(groupId, user.getId()+"");
				if(null!=userGroup){
			        result.element("code", "0"); 
			        result.element("tip", "操作提示：用户'"+username+"'已经加入的该分组!");	
			        DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			        return null;
				}else{
					userGroupService.insert(new UserGroup(groupId,user)); 
					
					//更新关联融资项目的缓存
					this.financingCacheService.updateDoingFinancingGroupByGroupId(groupId, user.getUsername()); 
					
				}  
			}else{
		        result.element("code", "0"); 
		        result.element("tip", "操作提示：用户交易帐号不存在!");	
		        DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		        return null;
			}
		} catch (Exception e) { 
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：保存数据异常，稍后重试!");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}	

	private User logUser;
	
	public String addUsersPage() throws Exception {
		initListData();
		try {
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgName = null;// loginUser.getOrg().getName();
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding())) {
				topOrg = true;
				orgName = this.orgName;
			}
			
			memberTypeId = memberTypeService.selectByName("投资方").getId();
				// 页面上分页查询满足条件的会员信息
			ServletActionContext.getRequest().setAttribute(
						"pageView",
						memberBaseService.listMembersByCondition(keyword,
								memberTypeId, memberState, logUser.getOrg()
										.getCoding(), orgName, provinceCode,
								cityCode, getShowRecord(), getPage(),
								startDate, endDate, bank,signState));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "select";
	}
	
	private void initListData() {
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		this.setMemberTypes(memberTypeService.getList(loginUser));
	}
	
	/**
	 * 会员类型列表
	 * */
	private List<MemberType> memberTypes;
	private boolean topOrg;
	private String keyword;
	private String memberTypeId;
	private String memberState;
	private String provinceCode;
	private String cityCode;
	private Date startDate;
	private Date endDate;
	private String bank;
	private String signState;
	private String usernames;
	private String orgName;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUsernames() {
		return usernames;
	}
	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}
	public String addUsers() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：加入全部用户成功");
        String tip = "操作提示：";
        int errorNum = 0;//统计错误数据数
        int repNum = 0;//统计已存在会员数
        String repId="";//保存错误会员ID
        String errorId="";//保存重复会员ID
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
	        result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
	    } 
		String[] users = usernames.split(",");
		for(String usernametemp : users){
			try {
				User user=userService.findUser(usernametemp);
				if(null!=user){
					if(!"T".equals(user.getUserType())){
						errorNum+=1;
						errorId+=" "+usernametemp;
				        continue;
					}else{
						UserGroup userGroup=userGroupService.findUserGroup(id, user.getId()+"");
						if(null!=userGroup){
							repNum+=1;
							repId+=" "+usernametemp;
							continue;
						}else{
							userGroupService.insert(new UserGroup(id,user));
							//更新关联融资项目的缓存
							this.financingCacheService.updateDoingFinancingGroupByGroupId(id, user.getUsername()); 
						}  
					}
				}else{
					//无该user
					errorNum+=1;
					errorId+=" "+usernametemp;
				}
			} catch (Exception e) { 
		        result.element("code", "0"); 
		        result.element("tip", "操作提示：保存数据异常，稍后重试!");
			}
		}
		if(errorNum>0 || repNum>0){
			if(errorNum>0){
				tip += "保存出错数："+errorNum+"个，用户名分别为"+errorId+"；";
			}
			if(repNum>0){
				tip += "已存在数据数："+repNum+"个，用户名分别为"+repId+"；";
			}
			result.element("code", "0"); 
	        result.element("tip", tip+"其他数据已保存成功！");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return "addUsers";
	}
	
	public String delUsers() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：删除全部选中用户成功");
        String tip = "操作提示：";
        int errorNum = 0;//统计错误数据数
        String errorId="";//保存重复会员ID
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
	        result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
	    } 
		String[] users = usernames.split(",");
		for(String usernametemp : users){
			try{
				userGroupService.delete(usernametemp);
			}catch(Exception e){
				//无该groupUserId
				errorNum+=1;
				errorId+=" "+usernametemp;
			}
		}
		if(errorNum>0){
			if(errorNum>0){
				tip += "删除出错数："+errorNum+"个，用户名分别为"+errorId+"；";
			}
			
			result.element("code", "0"); 
	        result.element("tip", tip+"删除其他数据已保存成功！");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return "addUsers";
	}
	
	public String addSelectedUser() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：加入用户成功");
        //用户是否登陆 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
	        result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
	    } 
		
		try {
			User user=userService.findUser(username);
			if(null!=user){
				
				if(!"T".equals(user.getUserType())){
			        result.element("code", "0"); 
			        result.element("tip", "操作提示：只有投资人才能加入分组!");
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString()); 
					return null;
				}
				
				UserGroup userGroup=userGroupService.findUserGroup(id, user.getId()+"");
				if(null!=userGroup){
			        result.element("code", "0"); 
			        result.element("tip", "操作提示：用户'"+username+"'已经加入的该分组!");	
			        DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			        return null;
				}else{
					userGroupService.insert(new UserGroup(id,user));
				}  
			}else{
		        result.element("code", "0"); 
		        result.element("tip", "操作提示：用户交易帐号不存在!");	
		        DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		        return null;
			}
		} catch (Exception e) { 
	        result.element("code", "0"); 
	        result.element("tip", "操作提示：保存数据异常，稍后重试!");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}
	
	public String clearInvestCount() throws Exception {
		JSONObject result = new JSONObject(); 
		result.element("code", "1"); 
		result.element("tip", "操作提示：清除所有体验完成的成员操作成功"); 
		//用户是否登陆 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
			result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		
		try { 
			
			Group group=groupService.selectById(id);
			if(null!=group){
				GroupRestrain groupRestrain=groupRestrainService.selectById(group.getGroupRestrainId());
				if(null!=groupRestrain){  
					JSONObject jsons= JSONObject.fromObject(groupRestrain.getRestrainJson());   
					List<UserGroupRestrain1> lists = userGroupRestrain1Service.getCommonListData("from UserGroupRestrain1 o where o.groupId='"+id+"' and o.investCount>="+jsons.getInt("investMaxCount"));
					for(UserGroupRestrain1 ug:lists){ 
						userGroupRestrain1Service.delete(ug.getId());
					}
				}
			}
			

		} catch (Exception e) { 
			result.element("code", "0"); 
			result.element("tip", "操作提示：保存数据异常，稍后重试!");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}	
	
	
	public String delGroupUsers() throws Exception {
		JSONObject result = new JSONObject(); 
		result.element("code", "1"); 
		result.element("tip", "操作提示：删除成功");
		//用户是否登陆 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
			result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		
		try { 
			List<UserGroupRestrain1> lists = userGroupRestrain1Service.getCommonListData("from UserGroupRestrain1 o where o.groupId='"+id+"'");
			for(UserGroupRestrain1 ug:lists){
				userGroupService.delete(ug.getId());
			}
		} catch (Exception e) { 
			result.element("code", "0"); 
			result.element("tip", "操作提示：保存数据异常，稍后重试!");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}	
	
	public String addUser1() throws Exception {
		JSONObject result = new JSONObject(); 
		result.element("code", "1"); 
		result.element("tip", "操作提示：加入用户成功");
		//用户是否登陆 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
			result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		
		try {
			User user=userService.findUser(username);
			if(null!=user){
				
				if(!"T".equals(user.getUserType())){
					result.element("code", "0"); 
					result.element("tip", "操作提示：只有投资人才能加入分组!");
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString()); 
					return null;
				} 
				UserGroup userGroup=userGroupService.findUserGroup(id, user.getId()+"");
				if(null!=userGroup){//再就删除
					//userGroupService.delete(userGroup.getId());
					result.element("code", "0"); 
					result.element("tip", "操作提示：用户交易已经存在此分组中！");	
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString()); 
					return null;
				}
				 
				UserGroupRestrain1 ugr=new UserGroupRestrain1(investCount,investMaxMoney); 
				ugr.setUser(user);
				ugr.setGroupId(id);
				this.userGroupRestrain1Service.insert(ugr); 
			}else{
				result.element("code", "0"); 
				result.element("tip", "操作提示：用户交易帐号不存在!");	
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
				return null;
			}
		} catch (Exception e) { 
			result.element("code", "0"); 
			result.element("tip", "操作提示：保存数据异常，稍后重试!");
		}
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}	
	
	public String ui() throws Exception { 
		List<GroupRestrain> grs=groupRestrainService.getScrollData().getResultlist();
		ServletActionContext.getRequest().setAttribute("restrainList",grs);
		return "ui"; 
	}  
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public String getGroupUserId() {
		return groupUserId;
	}
	public void setGroupUserId(String groupUserId) {
		this.groupUserId = groupUserId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getInvestCount() {
		return investCount;
	}
	public void setInvestCount(int investCount) {
		this.investCount = investCount;
	}
	public double getInvestMaxMoney() {
		return investMaxMoney;
	}
	public void setInvestMaxMoney(double investMaxMoney) {
		this.investMaxMoney = investMaxMoney;
	}
	public MemberBaseService getMemberBaseService() {
		return memberBaseService;
	}
	public void setMemberBaseService(MemberBaseService memberBaseService) {
		this.memberBaseService = memberBaseService;
	}
	public MemberTypeService getMemberTypeService() {
		return memberTypeService;
	}
	public void setMemberTypeService(MemberTypeService memberTypeService) {
		this.memberTypeService = memberTypeService;
	}
	public User getLogUser() {
		return logUser;
	}
	public void setLogUser(User logUser) {
		this.logUser = logUser;
	}
	public List<MemberType> getMemberTypes() {
		return memberTypes;
	}
	public void setMemberTypes(List<MemberType> memberTypes) {
		this.memberTypes = memberTypes;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMemberTypeId() {
		return memberTypeId;
	}
	public void setMemberTypeId(String memberTypeId) {
		this.memberTypeId = memberTypeId;
	}
	public String getMemberState() {
		return memberState;
	}
	public void setMemberState(String memberState) {
		this.memberState = memberState;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getSignState() {
		return signState;
	}
	public void setSignState(String signState) {
		this.signState = signState;
	}
	public boolean isTopOrg() {
		return topOrg;
	}
	public void setTopOrg(boolean topOrg) {
		this.topOrg = topOrg;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	} 
	
	
}
