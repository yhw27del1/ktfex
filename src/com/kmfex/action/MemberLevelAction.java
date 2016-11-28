package com.kmfex.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import com.kmfex.model.MemberLevel;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberLevelService;
import com.kmfex.service.MemberTypeService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;


@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class MemberLevelAction extends BaseAction implements Preparable{

	@Resource MemberLevelService memberLevelService; 
	@Resource MemberTypeService memberTypeService; 

	private String id;
	private String memberTypeid;
 
	private MemberLevel memberLevel;
	private List<MemberType> list;
	private String keyWord = "";
	
	public void prepare() throws Exception {
	      try {
			if(id!=null&&!"".equals(id)) {
				  memberLevel=memberLevelService.selectById(id);
				  memberTypeid=memberLevel.getMemberType().getId();
			   }else{			  
				   memberLevel=new MemberLevel();
			   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }   
	
	public void list() throws Exception {
		try {
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter outer = response.getWriter();
			ArrayList<Object> args_list = new ArrayList<Object>();
			StringBuilder wheresql = new StringBuilder();
			int page = 1,rows = 15;
			String page_str = request.getParameter("page");
			String rows_str = request.getParameter("rows");
			if(StringUtils.isNotBlank(page_str) && StringUtils.isNumeric(page_str)){
				page = Integer.parseInt(page_str);
			}
			if(StringUtils.isNotBlank(rows_str) && StringUtils.isNumeric(rows_str)){
				rows = Integer.parseInt(rows_str);
			}
			
			wheresql.append(" l.membertype_id = t.id ");
				
			if(StringUtils.isNotBlank(this.keyWord)){
				wheresql.append(" and l.levelname like ? ");
				args_list.add("%"+this.keyWord+"%");
			}
			
			JSONObject result = new JSONObject();
			Object [] args = args_list.toArray();
			List<Map<String,Object>> list = this.memberLevelService.queryForList(" l.id,l.levelname,t.code typecode,t.name typename,(select count(m.id) from t_member_base m where m.memberlevel_id = l.id) usercount", "t_member_level l,t_member_types t", wheresql.toString(), args,page,rows);
			int total = this.memberLevelService.queryForListTotal(" l.id", "t_member_level l,t_member_types t", wheresql.toString(), args);
			result.element("rows", list);
			result.element("total", total);
			outer.print(result);
			
			
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
	
	public void hasitem(){
		if(StringUtils.isNotBlank(this.id)){
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			try {
				JSONObject result = new JSONObject();
				PrintWriter outer = response.getWriter();
				ArrayList<Object> args_list = new ArrayList<Object>();
				args_list.add(this.id);
				int count = this.memberLevelService.queryForListTotal("m.id", "t_member_base m ", "m.memberlevel_id = ?",args_list.toArray());
				result.element("count", count);
				outer.print(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void exceptteam(){
		if(StringUtils.isNotBlank(this.id)){
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			try {
				PrintWriter outer = response.getWriter();
				ArrayList<Object> args_list = new ArrayList<Object>();
				args_list.add(this.id);
				List<Map<String,Object>> list = this.memberLevelService.queryForList("l.id,l.levelname text", "t_member_level l ", "l.id != ?",args_list.toArray());
				JSONArray result = JSONArray.fromObject(list);
				outer.print(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void del(){
		if(StringUtils.isNotBlank(this.id)){
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			response.setCharacterEncoding("utf-8");
			String newID  = null;
			if(StringUtils.isNotBlank(request.getParameter("newid"))){
				newID = request.getParameter("newid");
			}
			try {
				PrintWriter outer = response.getWriter();
				int code = this.memberLevelService.remove(this.id, newID);
				JSONObject result = new JSONObject();
				result.element("code", code);
				outer.print(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public String ui() throws Exception{
		try {
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			list=this.memberTypeService.getList(loginUser);
 		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "ui"; 
	}
	/**新增修改**/
	public void edit() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter outer = response.getWriter();
			if(id!=null&&!id.equals("")) {
			    //更新会员级别
			    this.memberLevelService.update(memberLevel);			
			}else{		   
				memberLevel.setMemberType(memberTypeService.selectById(memberTypeid));
				memberLevelService.insert(memberLevel);				
		    }
			outer.write("操作成功!");
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
	} 

	public MemberLevelService getMemberLevelService() {
		return memberLevelService;
	}

	public MemberTypeService getMemberTypeService() {
		return memberTypeService;
	}

	public String getId() {
		return id;
	}

	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setMemberLevelService(MemberLevelService memberLevelService) {
		this.memberLevelService = memberLevelService;
	}

	public void setMemberTypeService(MemberTypeService memberTypeService) {
		this.memberTypeService = memberTypeService;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<MemberType> getList() {
		return list;
	}

	public void setList(List<MemberType> list) {
		this.list = list;
	}

	public String getMemberTypeid() {
		return memberTypeid;
	}

	public void setMemberTypeid(String memberTypeid) {
		this.memberTypeid = memberTypeid;
	}
}
