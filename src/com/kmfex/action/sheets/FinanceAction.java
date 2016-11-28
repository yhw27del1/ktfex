package com.kmfex.action.sheets;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.driver.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.AccountDeal;
import com.kmfex.model.FinancingBase;
import com.kmfex.service.FinancingBaseService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.struts.BaseAction;

@Controller
@Scope("prototype")
public class FinanceAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	FinancingBaseService financingBaseService;
	private ArrayList<LinkedHashMap<String, Object>> resultList;
	private String searchusername;

	private String selectby;
	
	
	private String financingcode;
	
	private String username;
	
	private String financeid;

	//导出功能
	private String excelFlag; 
	private List<FinancingBase> results ;
	
	private FinancingBase result;
	public String financeList() throws Exception {
		String disUrl="financeList";
		try {

			if (selectby == null || "".equals(this.selectby)) {
				this.selectby = "签约";
			}
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (u == null || u.getOrg() == null) return null;

			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));

			inParamList.put(3, u.getOrg().getId());

			if (this.financingcode == null || "".equals(this.financingcode)) {
				inParamList.put(4, null);
			} else {
				inParamList.put(4, this.financingcode);
			}
			if (this.selectby == null || "".equals(this.selectby)) {
				inParamList.put(5, null);
			} else {
				inParamList.put(5, this.selectby);
			}
	
			OutParameterModel outParameter = new OutParameterModel(6, OracleTypes.CURSOR); 
			
			if("1".equals(excelFlag)){//导出功能 
				disUrl="financeList_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} 
			this.resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.FINANCE", inParamList, outParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	public String financerList() throws Exception {
		try {
			if (selectby == null || "".equals(this.selectby)) {
				this.selectby = "签约";
			}
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (u == null || u.getOrg() == null) return null;

			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));

			inParamList.put(3, u.getOrg().getId());

			if (this.searchusername == null || "".equals(this.searchusername)) {
				inParamList.put(4, null);
			} else {
				inParamList.put(4, this.searchusername);
			}
			if (this.selectby == null || "".equals(this.selectby)) {
				inParamList.put(5, null);
			} else {
				inParamList.put(5, this.selectby);
			}

			OutParameterModel outParameter = new OutParameterModel(6, OracleTypes.CURSOR);

			this.resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.FINANCER", inParamList, outParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "financerList";
	}
	
	/**
	 * 查看融资项目详细列表
	 * @return
	 * @throws Exception
	 */
	public String financedetail() throws Exception{
		if(this.financeid ==null || "".equals(this.financeid)) return null;
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		this.result = this.financingBaseService.selectById(this.financeid);
		List<Map<String, Object>> logs = this.financingBaseService.queryLogs(this.financeid);
		ServletActionContext.getRequest().setAttribute("logs", logs);
		return "financedetail";
	}
	
	
	/**
	 * 查看融资项目详细列表
	 * @return
	 * @throws Exception
	 */
	public String financelistdetail() throws Exception{
		if(this.username ==null || "".equals(this.username)) return null;
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		
		HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
		inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		Calendar c = Calendar.getInstance();
		c.setTime(this.getEndDate());
		c.add(Calendar.DATE, 1);
		inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));

		inParamList.put(3, u.getOrg().getId());

		if (this.username == null || "".equals(this.username)) {
			inParamList.put(4, null);
		} else {
			inParamList.put(4, this.username);
		}
		if (this.selectby == null || "".equals(this.selectby)) {
			inParamList.put(5, null);
		} else {
			inParamList.put(5, this.selectby);
		}
		outParameter.put(6, Types.VARCHAR);
		
		HashMap<Integer, Object> result = this.financingBaseService.callProcedureForParameters("P_FINANCER_FINANCE.FINANCELIST", inParamList, outParameter);
		String ids = null ;
		if(result.get(6)!=null){
			ids = result.get(6).toString();
			this.results = this.financingBaseService.getScrollDataCommon("from FinancingBase where code in ("+ids+")", new String[]{});
		} 
		for(FinancingBase fb:this.results){
			List<Map<String, Object>> logs = this.financingBaseService.queryLogs(fb.getId());
			fb.setLogs(logs);
		}
		return "financelistdetail";
	}
	
	

	public ArrayList<LinkedHashMap<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<LinkedHashMap<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public String getSearchusername() {
		return searchusername;
	}

	public void setSearchusername(String searchusername) {
		this.searchusername = searchusername;
	}

	public String getSelectby() {
		return selectby;
	}

	public void setSelectby(String selectby) {
		this.selectby = selectby;
	}

	public String getFinancingcode() {
		return financingcode;
	}

	public void setFinancingcode(String financingcode) {
		if(financingcode!=null && !"".equals(financingcode))
		this.financingcode = financingcode.toUpperCase();
		
	}

	public List<FinancingBase> getResults() {
		return results;
	}

	public void setResults(List<FinancingBase> results) {
		this.results = results;
	}

	public FinancingBase getResult() {
		return result;
	}

	public void setResult(FinancingBase result) {
		this.result = result;
	}

	public String getFinanceid() {
		return financeid;
	}

	public void setFinanceid(String financeid) {
		this.financeid = financeid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getExcelFlag() {
		return excelFlag;
	}

	public void setExcelFlag(String excelFlag) {
		this.excelFlag = excelFlag;
	}
	

}
