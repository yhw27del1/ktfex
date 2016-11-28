package com.wisdoor.core.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberBase;
import com.kmfex.model.ModifyLog;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.ModifyLogService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.MenuService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.RoleMenuService;
import com.wisdoor.core.service.RoleService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.symbol.UserTags;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.MD5;
import com.wisdoor.core.utils.MenuUtils;
import com.wisdoor.core.utils.ObjectSwitch;
import com.wisdoor.core.utils.SortList;
import com.wisdoor.struts.BaseAction;
/*** 
 * 管理员用户类
 * @author  
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction implements Preparable {
	@Resource UserService userService; 
	@Resource MenuService menuService; 
	@Resource RoleMenuService roleMenuService;
	@Resource RoleService roleService; 
	@Resource MemberBaseService memberBaseService;
	private long id;
	@Resource OrgService orgService;   
	private long parent_id;
	private User user;
	private String roleIds;//接受用户的角色id
	private Set<Role> inUserRoleList;
	private List<Role> notInUserRoleList;
	private String keyWord = ""; 
	private long leftParent_id=3;
	private HashMap<String,List<Menu>> leftMenuMap;
	private String userName;
	private String password;
	private String validCode;// 验证码
	private String directUrl="/back/index.jsp";
	private List<Menu> rootmenulist;
	public void prepare() throws Exception {
      if(0!=id) {
    	   user=userService.selectById(id);
       }else{
    	   user=new User();
       }
	}   
	
	/**跳转页面**/
	public String ui() throws Exception { 
		try {
			this.inUserRoleList=user.getRoles();//用户拥有的角色列表 
			this.notInUserRoleList=this.roleService.findNotInRole(this.inUserRoleList);//用户未拥有的角色列表 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "ui"; 
	} 
	public String list() throws Exception {
		try {
			PageView<User> pageView = new PageView<User>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 and o.typeFlag='2' "); //后台用户
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.username like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.realname like ?"); 
				params.add("%"+keyWord+"%"); 
				//会员管理下授权服务中心菜单可以为服务中心新增子账号，但是未关联userContact表
				//sb.append(" or ");
				//sb.append(" o.userContact.address like ?"); 
				//params.add("%"+keyWord+"%");
				sb.append(" )");
	 		} 
			pageView.setQueryResult(userService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "list";
	}
	
	/**新增修改**/
	public String edit() throws Exception {
		try {
			if(0!=parent_id) { 
				user.setOrg(orgService.selectById(parent_id));//设置父机构
			}
			if(0!=id) {
			    user.getRoles().clear(); 
				if(null!=roleIds&&!"".equals(roleIds)) 
				{ 
				  for (StringTokenizer tokenizer = new StringTokenizer(roleIds, ","); tokenizer.hasMoreTokens();) {
					String appID = tokenizer.nextToken();
					if (!appID.equals("0")) { 
						if (appID != null&& !appID.equals(""))
							appID=appID.trim();
						  user.getRoles().add(roleService.selectById(new Long(appID)));  
					} 
				  }
				}
		      if(null!=user.getOrg())
		      {
		    	  user.setCoding(user.getOrg().getCoding()+"m"+id);//用户编码
		      }
			  userService.update(user); 
			}else{
				
				if(null!=roleIds&&!"".equals(roleIds)) 
				{ 
				  for (StringTokenizer tokenizer = new StringTokenizer(roleIds, ","); tokenizer.hasMoreTokens();) {
					String appID = tokenizer.nextToken();
					if (!appID.equals("0")) { 
						if (appID != null&& !appID.equals(""))
							appID=appID.trim();
						  user.getRoles().add(roleService.selectById(new Long(appID)));  
					} 
				  }
				}
				 user.setTypeFlag("2");//2后台用户 
				 
				 user.setPassword(MD5.MD5Encode(user.getPassword())); 
				 
				 userService.insert(user);
				 //更新编码
			      if(null!=user.getOrg())
			      {
			    	  user.setCoding(user.getOrg().getCoding()+"m"+user.getId());//用户编码
			    	  userService.update(user);
			      }else{
			    	  user.setOrg(new Org(Constant.ORGID));
			    	  user.setCoding(Constant.ORGID+"m"+user.getId());//用户编码
			    	  userService.update(user);
			      } 
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 
	
	/**新增修改**/
	public String edit2() throws Exception {
		try {
			if(0!=parent_id) { 
				user.setOrg(orgService.selectById(parent_id));//设置父机构
			}
			if(0!=id) {
			    user.getRoles().clear(); 
				if(null!=roleIds&&!"".equals(roleIds)) 
				{ 
				  for (StringTokenizer tokenizer = new StringTokenizer(roleIds, ","); tokenizer.hasMoreTokens();) {
					String appID = tokenizer.nextToken();
					if (!appID.equals("0")) { 
						if (appID != null&& !appID.equals(""))
							appID=appID.trim();
						  user.getRoles().add(roleService.selectById(new Long(appID)));  
					} 
				  }
				}
		      if(null!=user.getOrg())
		      {
		    	  user.setCoding(user.getOrg().getCoding()+"m"+id);//用户编码
		      }
			  userService.update(user); 
			}else{
				
				if(null!=roleIds&&!"".equals(roleIds)) 
				{ 
				  for (StringTokenizer tokenizer = new StringTokenizer(roleIds, ","); tokenizer.hasMoreTokens();) {
					String appID = tokenizer.nextToken();
					if (!appID.equals("0")) { 
						if (appID != null&& !appID.equals(""))
							appID=appID.trim();
						  user.getRoles().add(roleService.selectById(new Long(appID)));  
					} 
				  }
				}
				 user.setTypeFlag("2");//2后台用户 
				 user.setCreateDateBy(new Date());
				 user.setPassword(MD5.MD5Encode(user.getPassword())); 
				 
				 userService.insert(user);
				 //更新编码
			      if(null!=user.getOrg())
			      {
			    	  user.setCoding(user.getOrg().getCoding()+"m"+user.getId());//用户编码
			    	  userService.update(user);
			      }else{
			    	  user.setOrg(orgService.selectById(parent_id));
			    	  user.setCoding(Constant.ORGID+"m"+user.getId());//用户编码
			    	  userService.update(user);
			      } 
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit2"; 
	} 
	
	/**重置密码**/
	public String reSetPassWord() throws Exception {
		try { 
		   user.setPassword(MD5.MD5Encode("123456"));
		   userService.update(user);
		  DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); //重置成功  
		} catch (Exception e) { 
			e.printStackTrace(); 
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0");  
 		}   
		return null; 
	}  
	
	public String userinfo() throws Exception{
		User u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
		this.user = u;
		return "userinfo";
	}
	
	/**
	 * 初始化密码库使用
	 */
	/*public String pilPassWords() throws Exception {
		try { 
			List<User> users=userService.getScrollData().getResultlist();
			for(User u:users){
				u.setPassword(MD5.MD5Encode(u.getPassword()));
				userService.update(u);
			}
			  
		  DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); //重置成功  
		} catch (Exception e) { 
			e.printStackTrace(); 
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0");  
 		}   
		return null; 
	} */
	
	/**
	 * 加载后台首页左侧用户有权限访问的链接 
	 */
	public String loadLeftMenu(){
		try {
			User u  = null;
			try {
			     u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			     this.user = u;
			} catch (Exception e) { 
				e.printStackTrace();
                    directUrl="/common/login.jsp";
                return "index";
			}  
			Set<Menu> rootMenuSet = this.roleMenuService.findRoleMenu(u.getRoleString(),"class",leftParent_id);
			leftMenuMap = new LinkedHashMap<String,List<Menu>>(); 
			//调用排序类     
			List<Menu> rootMenuList=ObjectSwitch.Set2List(rootMenuSet); 
			SortList<Menu> sortList = new SortList<Menu>(); 
		    sortList.Sort(rootMenuList, "getOrder", "asc");    
		    
			for(Menu menu : rootMenuList){ 
				Set<Menu> subMenuSet = this.roleMenuService.findRoleMenu(u.getRoleString(),"link",menu.getId());
				//调用排序类  
				List<Menu> subMenuList=ObjectSwitch.Set2List(subMenuSet); 
				SortList<Menu> sortSubList = new SortList<Menu>(); 
				sortSubList.Sort(subMenuList, "getOrder", "asc");  
				for(Menu menuSub : subMenuList){
					if(null!=menuSub.getHref()) 
					{
						//if(menuSub.getHref().indexOf("/") == 0){
							String v = menuSub.getHref();
							menuSub.setHref(v);
						//}
					}
				}
				 
 				leftMenuMap.put(menu.getCoding(), subMenuList);
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "loadLeftMenu";
	}
	/**
	 * 加载上面权限
	 */
	public String menuroot(){
		try {
			User u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
			Set<Menu> rootMenuSet = this.roleMenuService.findRoleMenu(u.getRoleString(),"class",leftParent_id);
			leftMenuMap = new LinkedHashMap<String,List<Menu>>(); 
			//调用排序类     
			List<Menu> rootMenuList=ObjectSwitch.Set2List(rootMenuSet); 
			SortList<Menu> sortList = new SortList<Menu>(); 
			sortList.Sort(rootMenuList, "getOrder", "asc");    
			rootmenulist = rootMenuList;
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "menuroot";
	}
	
	/**
	 * 加载上面权限，修改到topframe.jsp
	 * 并加入mainFrame的内容 信息，userinfo的信息
	 */
	public String menuroot_top(){
		try {
			User u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
			this.user = u;
			Set<Menu> rootMenuSet = this.roleMenuService.findRoleMenu(u.getRoleString(),"class",leftParent_id);
			leftMenuMap = new LinkedHashMap<String,List<Menu>>(); 
			//调用排序类     
			List<Menu> rootMenuList=ObjectSwitch.Set2List(rootMenuSet); 
			SortList<Menu> sortList = new SortList<Menu>(); 
			sortList.Sort(rootMenuList, "getOrder", "asc");    
			rootmenulist = rootMenuList;
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "menuroot_top";
	}
	
	
	/**删除**/
	public String del() throws Exception {
		try { 
			userService.delete(id); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "del";
	} 
	
	
	/**
	 * 暂时未知
	 * @return
	 * @throws Exception
	 */
	public String start() throws Exception {
		try { 
			user.setEnabled(true);
			user.setUserState(User.STATE_PASSED_AUDIT);
			user.setLoginCount(0); 
			userService.update(user); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "start";
	}
	
	
	public String stop() throws Exception {
		try { 
			user.setLoginCount(0); 
			user.setEnabled(false);
			user.setUserState(User.STATE_DISABLED);
			userService.update(user); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "stop";
	}
	 
	public String validateUser() throws Exception {
		try {  
			User userTemp=userService.findUser(userName); 
			if(userTemp == null){ 
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); 
			}else{ 
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); 
			} 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}
	
	@Resource
	ModifyLogService modifyLogService;
	
	public String validateUser2() throws Exception {
//		System.out.println("有用户登录");
		try {  
			//password=MD5.MD5Encode(password);
			//User userTemp=userService.findUser(userName, password);
			User userByUserName=userService.findUser(userName);
			if(userByUserName == null){//用户名不存在
				LOG.warn("用户不存在");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"9"); 
				return null;
			}else if(userByUserName.getTypeFlag().equalsIgnoreCase("1") && userByUserName.getUserType()!=null && userByUserName.getUserType().equalsIgnoreCase("T")){ 
				//System.out.println("投资方请下载客户端登陆!");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"5");
				return null;
			}else{
				
				User loginIngUser=userService.findUser(userName, password);
				if(loginIngUser == null){//密码错误
						
					if(userByUserName.getLoginCount()<5){
						try{
						  int t=4-userByUserName.getLoginCount();
						  userByUserName.setLoginCount(userByUserName.getLoginCount()+1);
						  if(t==0){
							  userByUserName.setEnabled(false);
							  userByUserName.setUserState(User.STATE_LOCKED);//如果次数为0则锁定用户
						      MemberBase member=memberBaseService.getMemByUser(userByUserName);
						      if (member!=null){
							     member.setState(MemberBase.STATE_DISABLED);
							     memberBaseService.update(member);
						      }
						      //记录日志
						      HttpServletRequest request = ServletActionContext. getRequest(); 
								String ip = getIpAddr(request);
						      ModifyLog ml = new ModifyLog();
								ml.setChanger(null);
								ml.setDatatype("User");
								ml.setDataname("login");
								ml.setModifyDate(new Date());
								ml.setModifyIP(ip);
								ml.setModifyModel(userName);
								ml.setContent("用户登录连续5次输入错误的密码");
								ml.setFirstData(null);
								ml.setEndData(null);
								modifyLogService.insert(ml);
						  }
						  userService.update(userByUserName);
						  DoResultUtil.doStringResult(ServletActionContext.getResponse(),""+t+"");
						}catch(Exception e) {
							   e.printStackTrace();							
						}
					}else{//如果数据库里登录次数为5或者以上
					  if(userByUserName.isEnabled())
					  {
						try{
						 userByUserName.setEnabled(false);
						 userByUserName.setUserState(User.STATE_LOCKED);
						 userService.update(userByUserName);
						}catch(Exception e){
							 e.printStackTrace();	
						}
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); //锁定
					  }else{
						  if(userByUserName.getUserState().equals("2"))
							  userByUserName.setUserState(User.STATE_LOCKED);
						  userService.update(userByUserName);
						  DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); //锁定
					  }
					  return null;
					}
				}else{//查找到用户
					if(!loginIngUser.isEnabled()){
						if(userByUserName.getUserState()==null){
							MemberBase member2=memberBaseService.getMemByUser(userByUserName);
							if (member2!=null){
								//如果此账户关联了会员 且 用户状态不存在，则添加用户状态
								String memberState = member2.getState();
								
								userByUserName.setUserState(memberState);//设置为会员的状态 只有1-4 
							}else{
								//如果未关联用户，检查是否为后台用户，如果为后台用户则添加用户状态为停用（4）
								if(userByUserName.getTypeFlag().equals("2"))
									userByUserName.setUserState(User.STATE_DISABLED);
								else//如果不是后台用户，则设置为错误用户
									userByUserName.setUserState(User.STATE_ERROR_USER);
							}
							userService.update(userByUserName);
							LOG.info("用户状态enable=0处更新User状态");
						}
						if(loginIngUser.getUserState().equals(User.STATE_DISABLED)){
							DoResultUtil.doStringResult(ServletActionContext.getResponse(),"8"); //停用
						}else if(loginIngUser.getUserState().equals(User.STATE_SLEEPED)){
							DoResultUtil.doStringResult(ServletActionContext.getResponse(),"7"); //休眠
						}else if(loginIngUser.getUserState().equals(User.STATE_LOCKED)){
							DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); //锁定
						}else if(loginIngUser.getUserState().equals(User.STATE_NOT_AUDIT) || loginIngUser.getUserState().equals(User.STATE_NOT_PASS_AUDIT)){
							DoResultUtil.doStringResult(ServletActionContext.getResponse(),"10"); //审核未通过
						}else{
							LOG.error("数据异常，enable=0，但是userState为正常");
							DoResultUtil.doStringResult(ServletActionContext.getResponse(),"8"); //停用
						}
					    return null;
					}else{//正确验证完毕
						try { 
							  // userTemp.setEnabled(false);
							if(loginIngUser.getTypeFlag().equals("1")){
								MemberBase member2=memberBaseService.getMemByUser(userByUserName);
								if(member2.getState().equals(MemberBase.STATE_STOPPED)){
									DoResultUtil.doStringResult(ServletActionContext.getResponse(),"8"); //停用
									return null;
								}
							}
							   userByUserName.setLoginCount(0);
							   if(userByUserName.getUserState()==null)
								   userByUserName.setUserState(User.STATE_PASSED_AUDIT);
							   userService.update(userByUserName); 	
							   LOG.info("用户状态enable=1处,正常登录补充更新User状态");
							   DoResultUtil.doStringResult(ServletActionContext.getResponse(),"6");
							   return null;
						} catch (Exception e) {
							   e.printStackTrace(); 
						}
					}
				}
				
			} 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}  
	
	public String getIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	} 
	
	public String validateUser3() throws Exception {
		try {  
			//password=MD5.MD5Encode(password);
			User userTemp=userService.findUser(userName, password); 
			if(userTemp == null){ 
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); 
				return null;
			}else{ 
				if(!userTemp.isEnabled())
				{
				    DoResultUtil.doStringResult(ServletActionContext.getResponse(),"4"); //停用
				    return null;
				}
				if(!"R".equals(userTemp.getUserType()))
				{
					//System.out.println("--3242342wwwzffffgggg---");
				    DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); 
				    return null;
				}
				else
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),"2"); 
			} 
			return null;
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}  
	public String validateCode() throws Exception {
		try {  
			//取验证码
			Object random = ActionContext.getContext().getSession().get("_TXPT_AUTHKEY");  
			if (random==null) {             
				this.addActionError("验证码不能为空"); 
				    DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); 
			}
			else
			{
				if(!random.toString().equals(validCode)) {
				 	this.addActionError("验证码不正确"); 
				 	DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); 
				 } else{
					 DoResultUtil.doStringResult(ServletActionContext.getResponse(),"2"); 
				 }  
			} 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}
	
	
	public String index() throws Exception {
	    Map<String,String> menuMap = null; 
	    Map<String,String> urlMap = null; 
		try {   
			 User user = null;
			try {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				 
				 if(null!=principal) user=(User)principal;
			} catch (Exception e) { 
				e.printStackTrace();
                 directUrl="/common/login.jsp"; 
                return "loginPage";
			}
	         if(null!=user)
	         { 
				if(menuMap == null){
					menuMap = new HashMap<String,String>();
					urlMap = new HashMap<String,String>();
					List<Menu> menuList =(new MenuUtils()).getAll(); 
					for(Menu menu: menuList)
					{
						if(this.roleMenuService.findRoleMenu(user.getRoleString(), menu.getId())&&null!=menu.getPrivilegeValue()&&!"".equals(menu.getPrivilegeValue()))
						{
							menuMap.put(menu.getPrivilegeValue(), "inline");
							urlMap.put(menu.getHref(), "inline");
						}else
						{
							menuMap.put(menu.getPrivilegeValue(), "none");
							urlMap.put(menu.getHref(),  "none");
						}
					}
					ServletActionContext.getRequest().getSession().setAttribute(UserTags.MENUMAP, menuMap);
					ServletActionContext.getRequest().getSession().setAttribute(UserTags.URLMAP, urlMap);
					ServletActionContext.getRequest().getSession().setAttribute(UserTags.LOGININFO, user);
				}
				   
	         } 
			
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "index";
	}	
	
	
	
	public String loginPage() throws Exception { 
		try {    
	           
			     directUrl="/common/login.jsp"; 
		    }    
		 catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "loginPage";
	}	
	
	

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parentId) {
		parent_id = parentId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public long getLeftParent_id() {
		return leftParent_id;
	}

	public void setLeftParent_id(long leftParentId) {
		leftParent_id = leftParentId;
	}
 

	public HashMap<String, List<Menu>> getLeftMenuMap() {
		return leftMenuMap;
	}

	public void setLeftMenuMap(HashMap<String, List<Menu>> leftMenuMap) {
		this.leftMenuMap = leftMenuMap;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
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

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public void setRootmenulist(List<Menu> rootmenulist) {
		this.rootmenulist = rootmenulist;
	}

	public List<Menu> getRootmenulist() {
		return rootmenulist;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}
  
	
}
