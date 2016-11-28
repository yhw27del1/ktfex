package com.wisdoor.core.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Notice;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.NoticeService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 授权中心通知
 * @author  
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class NoticeAction  extends BaseAction  implements Preparable {
 
	@Resource NoticeService noticeService;  
	@Resource OrgService orgService;  
	private String queryByOrgCode;
	private String qkeyWord;
	private Date startDate;
	private Date endDate;  
	private String id;
	private Notice notice;
 
	/**
	 * 编辑/新增 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception{
		return "ui";
	}
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		if(id==null || "".equals(id)){
			this.noticeService.insert(this.notice);
		}else{
			this.noticeService.update(this.notice);
		} 
		return "edit";
	}
	
	/**
	 * 更改状态_审核
	 * @return
	 * @throws Exception
	 */
	public String state_assessor() throws Exception{
		try {
			if(this.notice.getId()!=null&&!"".equals(this.notice.getId())){
				this.noticeService.update(this.notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "state_assessor";
	}
	/**
	 * 更改状态_发布
	 * @return
	 * @throws Exception
	 */
	public String state_normal() throws Exception{
		try {
			if(this.notice.getId()!=null&&!"".equals(this.notice.getId())){
				this.noticeService.update(this.notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "state_normal";
	}	
	public String detail() throws Exception{ 
		
		User user = null; 
		try {
			user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if(!this.notice.getTargetUser().contains(user.getRealname()+"("+user.getUsername()+")")){
				this.notice.setTitle("******");
				this.notice.setContent("******");
				return "detail";  
			}   
			
			if(this.notice.getReadEdUser()==null || "".equals(this.notice.getReadEdUser())){
				this.notice.setReadEdUser(user.getUsername()+",");   
				this.noticeService.update(this.notice);
				return "detail";
			}  
			
			if(!this.notice.getReadEdUser().contains(user.getUsername()+",")){
				this.notice.setReadEdUser(this.notice.getReadEdUser()+user.getUsername()+",");
				this.noticeService.update(this.notice);
			} 
		} catch (RuntimeException e) { 
			e.printStackTrace();
		} 
		return "detail";
	}
	
	/**
	 * 此用户是否存在未看的新通知
	 * @return
	 * @throws Exception
	 */
	public String isExist() throws Exception{ 
		String res="{\"flag\":\"0\",\"msg\":\"\",\"id\":\"\"}";
		try {
			User  user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//指定用户的通知 
			 StringBuilder sb = new StringBuilder(" 1=1 ");  
			 sb.append("  and  v.targetUser like '%"+user.getRealname()+"("+user.getUsername()+")%'");
			 sb.append("  and ( v.readEdUser is null or v.readEdUser not like '%"+user.getUsername()+"%')"); 
				
			 sb.append("  order by v.addtime desc"); 
			 List<Map<String,Object>> list = noticeService.queryForList("v.id", "sys_Notice v", sb.toString()); 
			 if(list.get(0).get("ID") != null){ 
				res="{\"flag\":\"1\",\"msg\":\"\",\"id\":\""+list.get(0).get("ID").toString()+"\"}"; 
			 }  
		} catch (Exception e) { 
			//e.printStackTrace();  
			res="{\"flag\":\"0\",\"msg\":\"\",\"id\":\"\"}";
		} 
		 DoResultUtil.doStringResult(ServletActionContext.getResponse(),res); 
		 return null;
	}
	
	
	/**
	 * 授权中心
	 */
	public void member_o_json(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();  
			String page_ = request.getParameter("page");
			int page = 1;
			if(StringUtils.isNotBlank(page_)){
				page = Integer.parseInt(page_);
			}
			String pagesize_ = request.getParameter("rows");
			
			int pagesize= 15;
			if(StringUtils.isNotBlank(pagesize_)){
				pagesize = Integer.parseInt(pagesize_);
			} 
			
			String first;
			try {
				first = request.getParameter("first");
			} catch (Exception e) {
				return ;
			}
			if(null!=first&&!"".equals(first)){
				return ;
			}    
			
			String fields = " t.id,t.username,t.realname,t.org_id ,org.name_,org.showcoding,t.regtime ";
			String tables = " SYS_USER t,V_ORG_LIST org";
			 StringBuilder sb = new StringBuilder(); 
			 sb.append(" t.org_id=org.id  ");  
			 sb.append(" and t.usertype_ is null");  
		 
			if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
				queryByOrgCode=queryByOrgCode.trim();
				sb.append(" and (org.showcoding like '%" + queryByOrgCode + "%' or org.name_ like '%" + queryByOrgCode + "%' ) ");   
			}    
		 
			if(null != qkeyWord&&!"".equals(qkeyWord)){
				qkeyWord=qkeyWord.trim();
				sb.append(" and (t.username like '%" + qkeyWord + "%' or t.realname like '%" + qkeyWord + "%' ) ");   
			}  
			
			
			List<Map<String,Object>> list = noticeService.queryForList(fields, 
					tables, sb.toString(), page, pagesize); 
			JSONObject obj = new JSONObject();
			int total = this.noticeService.queryForListTotal("*", tables, sb.toString());
			JSONArray object = JSONArray.fromObject(list);
			obj.element("total", total);
			obj.element("rows", object);
			
			response.getWriter().println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
 
	/**
	 * 融资方、投资人、担保方
	 * @return
	 * @throws Exception
	 */
	public void member_json() throws Exception{ 
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();  
		String page_ = request.getParameter("page");
		int page = 1;
		if(StringUtils.isNotBlank(page_)){
			page = Integer.parseInt(page_);
		}
		String pagesize_ = request.getParameter("rows");
		
		int pagesize= 15;
		if(StringUtils.isNotBlank(pagesize_)){
			pagesize = Integer.parseInt(pagesize_);
		} 
		
		String first;
		try {
			first = request.getParameter("first");
		} catch (Exception e) {
			return ;
		}
		if(null!=first&&!"".equals(first)){
			return ;
		}    
		String fields = " t.id,t.username,t.realname,t.org_id ,org.name_,org.showcoding,t.regtime ";
		String tables = " SYS_USER t,Sys_Org org";
		 StringBuilder sb = new StringBuilder(); 
		 sb.append(" t.org_id=org.id  "); 
		 String userType = request.getParameter("userType");
		 
		 sb.append(" and t.usertype_= '"+userType+"'"); 
	 
		if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
			queryByOrgCode=queryByOrgCode.trim();
			sb.append(" and (org.showcoding like '%" + queryByOrgCode + "%' or org.name_ like '%" + queryByOrgCode + "%' ) ");   
		}  
	 
		if(null != qkeyWord&&!"".equals(qkeyWord)){
			qkeyWord=qkeyWord.trim();
			sb.append(" and (t.username like '%" + qkeyWord + "%' or t.realname like '%" + qkeyWord + "%' ) ");   
		}  
		
		
		List<Map<String,Object>> list = noticeService.queryForList(fields, 
				tables, sb.toString(), page, pagesize); 
		JSONObject obj = new JSONObject();
		int total = this.noticeService.queryForListTotal("*", tables, sb.toString());
		JSONArray object = JSONArray.fromObject(list);
		obj.element("total", total);
		obj.element("rows", object);
		
		response.getWriter().println(obj);
	} 
	
	 
	
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception{
		this.noticeService.delete(this.id); 
		return "del";
	}
	

 
	public void list_data(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 
			  
			
			 StringBuilder sb = new StringBuilder(" ID is not null ");
			 
			//日期区间
			if( null != this.getStartDate() && null!= this.getEndDate() ){
				sb.append(" and ADDTIME between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')  and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}  
			if(null != qkeyWord&&!"".equals(qkeyWord)){
				qkeyWord=qkeyWord.trim();
				sb.append(" and TITLE like '%" + qkeyWord + "%'");
			} 
			 
			sb.append(" order by ADDTIME desc");
			List<Map<String, Object>> result = this.noticeService.queryForList("*"," sys_Notice ",sb.toString(), getPage(), rows);
			//添加操作
			for (Map<String, Object> obj : result) { 
					obj.put("SHOWOK", true); 
			}
			int total = this.noticeService.queryForListTotal("id"," sys_Notice ",sb.toString());  

		 
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			 
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void list_assessor(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 
			  
			
			 StringBuilder sb = new StringBuilder(" ID is not null ");
			 
			 sb.append(" and STATE = 1 ");
			 
			//日期区间
			if( null != this.getStartDate() && null!= this.getEndDate() ){
				sb.append(" and ADDTIME between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')  and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}  
			if(null != qkeyWord&&!"".equals(qkeyWord)){
				qkeyWord=qkeyWord.trim();
				sb.append(" and TITLE like '%" + qkeyWord + "%'");
			}
			
			
			sb.append(" order by ADDTIME desc");
			List<Map<String, Object>> result = this.noticeService.queryForList("*"," sys_Notice ",sb.toString(), getPage(), rows);
			//添加操作
			for (Map<String, Object> obj : result) { 
			   obj.put("SHOWOK", true); 
			}
			int total = this.noticeService.queryForListTotal("id"," sys_Notice ",sb.toString());  

		 
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			 
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void prepare() throws Exception {
		if (this.id == null || "".equals(this.id)) {
			notice= new Notice();
		} else {
			notice=this.noticeService.selectById(id);
		}
	}

	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}

	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}

	public String getQkeyWord() {
		return qkeyWord;
	}

	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	
	 
 
}
