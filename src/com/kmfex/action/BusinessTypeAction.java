package com.kmfex.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.BusinessType;
import com.kmfex.service.BusinessTypeService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;


@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class BusinessTypeAction extends BaseAction implements Preparable{

	@Resource BusinessTypeService businessTypeService;
	
	private String id;
	private BusinessType businessType;
	private List<Object> list;
	private String keyWord = "";
	private String qkeyWord = "";
	
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		try {
			if(id!=null&&!"".equals(id)) {
				businessType=businessTypeService.selectById(id);
				 
			   }else{			  
				   businessType=new BusinessType();
			   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String for_list() throws Exception { 
		return "list_for_json";
	}
	public void list(){
	   try{
		HttpServletRequest request = ServletActionContext.getRequest();
		int rows = Integer.parseInt(request.getParameter("rows"));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		StringBuilder sb = new StringBuilder(" id is not null order by order_ asc ");
		

		/*if (StringUtils.isNotBlank(this.qkeyWord)) {
			qkeyWord = qkeyWord.trim();
			subsql.append(" and (");
			subsql.append("     financerusername like ");
			subsql.append("'%" + qkeyWord + "%'");
			subsql.append("     or ");
			subsql.append("     financerrealname like ");
			subsql.append("'%" + qkeyWord + "%'");
			subsql.append("     or ");
			subsql.append("     financecode like ");
			subsql.append("'%" + qkeyWord + "%'");
			subsql.append("     or ");
			subsql.append("     financename like ");
			subsql.append("'%" + qkeyWord + "%'");
			subsql.append(" )");
		}*/

		
		List<Map<String, Object>> result = this.businessTypeService.queryForList("*", "t_business_type", sb.toString(),getPage(), rows);
		//添加操作
		for (Map<String, Object> obj : result) { 
				obj.put("SHOWOK", true); 
		}
		//int total = this.businessTypeService.queryForListTotal("id","v_finance",subsql.toString(),args);

		/*for (Map<String, Object> obj : result) {
			if (null != obj.get("enddate")) {
				if (((Date) obj.get("enddate")).compareTo(new Date()) <= 0){// 融资到期
					if ((!"5".equals(obj.get("state"))) && (!"6".equals(obj.get("state"))) && (!"7".equals(obj.get("state")))){// 5融资确认已完成
					// 6费用核算完成 7签约完成
						obj.put("showok", true);
					}
				} else {
					if ("4".equals(obj.get("state"))){// 已满标或者融资到期可以融资确认
						obj.put("showok", true);
					}
				}
			}

		}*/
		
		
		JSONArray object = JSONArray.fromObject(result);
		JSONObject o = new JSONObject();
		o.element("rows", object);
		//long[] totalData = businessTypeService.qu(subsql.toString(),args);
		
		ServletActionContext.getResponse().getWriter().write(o.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	/*public String list() throws Exception {
		try {
			PageView<BusinessType> pageView = new PageView<BusinessType>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 "); 
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.name like ?");
				params.add("%"+keyWord+"%");
				sb.append(" )");
				pageView.setQueryResult(businessTypeService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			} 
			pageView.setQueryResult(businessTypeService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		}
        return "list";
	}*/
	
	public String ui() throws Exception{
		/*try {
			//list=this.memberTypeService.getList();
 		} catch (Exception e) { 
			e.printStackTrace();
		}*/
		return "ui"; 
	}
	
	/**新增修改**/
	public String edit() throws Exception {
		try {
			if(id!=null&&!id.equals("")) {
			    //更新会员级别
				
			    this.businessTypeService.update(businessType);			
			}else{				
				businessTypeService.insert(businessType);				
		    }
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 

	public BusinessTypeService getBusinessTypeService() {
		return businessTypeService;
	}

	public String getId() {
		return id;
	}

	public BusinessType getBusinessType() {
		return businessType;
	}

	public List<Object> getList() {
		return list;
	}

	public void setBusinessTypeService(BusinessTypeService businessTypeService) {
		this.businessTypeService = businessTypeService;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public String getQkeyWord() {
		return qkeyWord;
	}

	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
	}
	
}
