package com.kmfex.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
 
import com.kmfex.model.Industry;
import com.kmfex.service.IndustryService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;
/*** 
 * 行业类
 * @author    
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class IndustryAction extends BaseAction implements Preparable {
	@Resource IndustryService industryService;  
	private long id; 
	private Industry industry; 
	private String keyWord = "";  
	
	public void prepare() throws Exception {
      if(0!=id) {
    	   industry=industryService.selectById(id);
       }else{
    	   industry=new Industry();
       }
	}   
	
	/**跳转页面**/
	public String ui() throws Exception { 
		try {
 		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "ui"; 
	} 
	public String list() throws Exception {
		try {
			PageView<Industry> pageView = new PageView<Industry>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.note like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.name like ?"); 
				params.add("%"+keyWord+"%");  
				sb.append(" )");
	 		} 
			pageView.setQueryResult(industryService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "list";
	}
	
	public String listTree() throws Exception {
		try {
			
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.note like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.name like ?"); 
				params.add("%"+keyWord+"%");  
				sb.append(" )");
	 		} 
			List<Industry> listTree=industryService.getScrollData(sb.toString(), params,orderby).getResultlist();
			ServletActionContext.getRequest().setAttribute("listTree", listTree);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "listTree";
	}
	
	/**新增修改**/
	public String edit() throws Exception {
		try { 
			if(0!=id) { 
				industryService.update(industry); 
			}else{
				industryService.insert(industry);
			   
			}
			
			
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 
	
	 
	/**删除**/
	public String del() throws Exception {
		try { 
			industryService.delete(id); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "del";
	} 
	 
	
	  
	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
 
	
}
