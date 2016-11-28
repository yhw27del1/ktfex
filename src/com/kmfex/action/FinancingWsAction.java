package com.kmfex.action;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberGuarantee;
import com.kmfex.service.FinancingBaseService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
/**
 * 融资项目管理(WebService)
 * @author
 * 2013年07月26日11:14 新增方法wsdb() 针对Pc客户端显示融资担保情况
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class FinancingWsAction  extends ActionSupport  implements Preparable{
 
	@Resource FinancingBaseService financingBaseService;
 
	private String id;  
	
	private String appFlag;
	private  FinancingBase financingBase;
 
	public String xyfx() throws Exception{ 
		if(null!=appFlag&&"1".equals(appFlag)){
			return "appxyfx";
		}
 		return "xyfx";
	}
	
	
	public String wsdetail() throws Exception{ 
		if(null!=appFlag&&"1".equals(appFlag)){
			return "appwsdetail";
		}
 		return "wsdetail";
	}
	
	public String wsdb() throws Exception{ 
		MemberGuarantee memberGuarantee=null;
		if (null != financingBase) {
			if (null != financingBase.getGuarantee()) {
				if (null != financingBase.getMemberGuarantee()) { 
					memberGuarantee=financingBase.getMemberGuarantee();
				}
			}
		}
		ServletActionContext.getRequest().setAttribute("memberGuarantee", memberGuarantee); 		
		if(null!=appFlag&&"1".equals(appFlag)){
			return "appwsdb";
		}
		return "wsdb";
	}
	 
	
	@Override
	public void prepare() throws Exception {
       try {
		this.financingBase = this.financingBaseService.selectById(this.id);
	} catch (Exception e) { 
		e.printStackTrace();
	}
  	}

	public FinancingBaseService getFinancingBaseService() {
		return financingBaseService;
	}

	public void setFinancingBaseService(FinancingBaseService financingBaseService) {
		this.financingBaseService = financingBaseService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancingBase getFinancingBase() {
		return financingBase;
	}

	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}


	public String getAppFlag() {
		return appFlag;
	}


	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}
 
}
