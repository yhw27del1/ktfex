package com.kmfex.action;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.BankLibrary;
import com.kmfex.service.BankLibraryService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class BankLibraryAction extends BaseAction implements Preparable {
	@Override
	public void prepare() throws Exception {
		if(this.banklibrary_id==null || "".equals(this.banklibrary_id)){
			this.banklibrary = new BankLibrary();
		}else{
			this.banklibrary = this.bankLibraryService.selectById(this.banklibrary_id);
		}
	}

	@Resource
	BankLibraryService bankLibraryService;

	private List<BankLibrary> banklist;
	
	private BankLibrary banklibrary;
	
	private String banklibrary_id;

	public String list() throws Exception{
		PageView<BankLibrary> pageView = new PageView<BankLibrary>(getShowRecord(),getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("order", "asc");
		pageView.setQueryResult(bankLibraryService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),  orderby));
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "list";
	}

	public String ui() throws Exception{
		return "ui";
	}
	
	public String edit() throws Exception{
		if(this.banklibrary.getId()==null || "".equals(this.banklibrary.getId())){
			this.bankLibraryService.insert(this.banklibrary);
		}else{
			this.bankLibraryService.update(this.banklibrary);
		}
		return "edit";
	}
	
	public String del() throws Exception{
		if(this.banklibrary_id!=null && !"".equals(this.banklibrary_id)){
			this.bankLibraryService.delete(this.banklibrary_id);
		}
		return "del";
	}
	
	public String order_up() throws Exception{
		if(this.banklibrary_id!=null && !"".equals(this.banklibrary_id)){
			this.banklibrary.setOrder(this.banklibrary.getOrder()-1);
			this.bankLibraryService.update(this.banklibrary);
		}
		return "edit";
	}
	public String order_down() throws Exception{
		if(this.banklibrary_id!=null && !"".equals(this.banklibrary_id)){
			this.banklibrary.setOrder(this.banklibrary.getOrder()+1);
			this.bankLibraryService.update(this.banklibrary);
		}
		return "edit";
	}
	
	
	
	public List<BankLibrary> getBanklist() {
		return banklist;
	}

	public void setBanklist(List<BankLibrary> banklist) {
		this.banklist = banklist;
	}

	public BankLibrary getBanklibrary() {
		return banklibrary;
	}

	public void setBanklibrary(BankLibrary banklibrary) {
		this.banklibrary = banklibrary;
	}

	public String getBanklibrary_id() {
		return banklibrary_id;
	}

	public void setBanklibrary_id(String banklibrary_id) {
		this.banklibrary_id = banklibrary_id;
	}
	
	

}
