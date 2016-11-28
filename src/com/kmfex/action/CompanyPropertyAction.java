package com.kmfex.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.CompanyProperty; 
import com.kmfex.service.CompanyPropertyService; 
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;
/*** 
 * 公司性质类
 * @author    
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class CompanyPropertyAction extends BaseAction implements Preparable {
	@Resource CompanyPropertyService companyPropertyService;  
	private long id; 
	private CompanyProperty companyProperty; 
	private String keyWord = "";  
	
	public void prepare() throws Exception {
      if(0!=id) {
    	   companyProperty=companyPropertyService.selectById(id);
       }else{
    	   companyProperty=new CompanyProperty();
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
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			StringBuilder sb = new StringBuilder(" 1=1 ");
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
			List<CompanyProperty> list=companyPropertyService.getScrollData(sb.toString(), params,orderby).getResultlist();
			ServletActionContext.getRequest().setAttribute("list", list);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "list";
	}
	public String listTree() throws Exception {
		try {
			
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			StringBuilder sb = new StringBuilder(" 1=1 ");
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
			List<CompanyProperty> listTree=companyPropertyService.getScrollData(sb.toString(), params,orderby).getResultlist();
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
				companyPropertyService.update(companyProperty); 
			}else{
				companyPropertyService.insert(companyProperty); 
			} 
			
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 
	
	 
	/**删除**/
	public String del() throws Exception {
		try { 
			companyPropertyService.delete(id); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "del";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public CompanyProperty getCompanyProperty() {
		return companyProperty;
	}

	public void setCompanyProperty(CompanyProperty companyProperty) {
		this.companyProperty = companyProperty;
	} 
	 
 
	public List<CompanyProperty> companyPropertyListFilter(
			List<CompanyProperty> companyPropertyList) {
		for (CompanyProperty pc : companyPropertyList) {
			if (pc.getParent() != null) { 
				boolean bln = false;
 				for (CompanyProperty subPc : companyPropertyList) {
					if (subPc.getId() == pc.getParent().getId()) {
						bln = true;
					}
				}
				if (bln == false) {
					pc.setParent(pc.getParent());
				}
			}
		}
		return companyPropertyList;
	}
 
	
}
