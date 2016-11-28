package com.kmfex.action.sheets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import oracle.jdbc.driver.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.kmfex.service.InvestRecordService;
import com.kmfex.util.ImportExcelUtil;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.vo.PageForProcedureVO;
import com.wisdoor.struts.BaseAction;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class InvestorAction extends BaseAction {

	@Resource
	InvestRecordService investRecordService;
	@Resource
	OrgService orgService;

	private ArrayList<LinkedHashMap<String, Object>> resultList;

	private String username;
	private String financingcode;

	private String searchfinancingcode;
	private String searchusername;
	private String searchjinbanren;
	

	private String type = "1";
	private String excel = "0";
	
	

	/**
	 * 投标记录统计
	 * 
	 * @return
	 */
	public String investList() throws Exception {
		try {
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			if (this.getType()==null){
				this.setType("1");
			}
			if(this.getExcel()==null){
				this.setExcel("0");
			}
			
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (u == null || u.getOrg() == null) return null;
			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
			
			
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
			
			inParamList.put(3, this.getType());

			long orgId = u.getOrg().getId();
			if(orgId==1 || orgId == 1196){
				inParamList.put(4, null);
			}else
				inParamList.put(4, u.getOrg().getShowCoding());
			
			inParamList.put(5, this.searchusername);
			inParamList.put(6, this.searchjinbanren);

			OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
			this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_RECORD", inParamList, outParameter);
			if(excel.equalsIgnoreCase("1")){
				// 导出所有满足条件的会员信息为Excel
				String fileName = new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date());
				ServletActionContext.getResponse().setContentType(
						"application/x-xls");
				ServletActionContext.getResponse().setHeader(
						"Content-Disposition",
						"attachment;filename=" + fileName + "MembershipInvestmentStatistics.xls");
				ServletOutputStream os = ServletActionContext.getResponse()
						.getOutputStream();
				List<String> head = new ArrayList<String>();
		        head.add("会员帐号");
		        head.add("会员名称");
		        head.add("所属机构");
		        head.add("总投标额");
		        head.add("投资次数");
		        head.add("会员介绍人");
		        List<String> paramOrder = new ArrayList<String>();
		        paramOrder.add("investorname");
		        paramOrder.add("investorrealname");
		        paramOrder.add("investor_orgno");
		        paramOrder.add("money");
		        paramOrder.add("count");
		        paramOrder.add("jingbanren");
		        List<LinkedHashMap<String, Object>> theData = new ArrayList<LinkedHashMap<String, Object>>();
		        theData = resultList;
		        ImportExcelUtil.exportInvestors(os,head,paramOrder,theData,"HashMap");
		        //System.out.println("/sheets/investor!investDetailListImport?startDate=2013-06-18&endDate=2013-06-24&username=&searchusername=&type=1");
		        return null; 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "investList";
	}
	
	/**
	 * 投标记录统计
	 * 
	 * @return
	 */
	public String investListEx() throws Exception {
		try {
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			if (this.getType()==null){
				this.setType("1");
			}
			if(this.getExcel()==null){
				this.setExcel("0");
			}
			
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (u == null || u.getOrg() == null) return null;
			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
			
			
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
			inParamList.put(3, this.getType());
			long orgId = u.getOrg().getId();
			if(orgId==1 || orgId == 1196){
				inParamList.put(4, null);
			}else
				inParamList.put(4, u.getOrg().getShowCoding());
			inParamList.put(5, this.getSearchusername());
			inParamList.put(6, this.getSearchjinbanren());
			OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
			this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_RECORD", inParamList, outParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "investListEx";
	}

	/**
	 * 融资项目统计
	 * 
	 * @return
	 */
	public String financingList() throws Exception {
		try {
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			if (this.getType()==null){
				this.setType("1");
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (u == null || u.getOrg() == null) return null;
			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

			if (this.getStartDate() == null) {
				inParamList.put(1, null);
			} else {
				inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
			}
			if (this.getEndDate() == null) {
				inParamList.put(2, null);
			} else {
				Calendar c = Calendar.getInstance();
				c.setTime(this.getEndDate());
				c.add(Calendar.DATE, 1);
				inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
			}

			//
			inParamList.put(3, this.getType());
			
			long orgId = u.getOrg().getId();
			if(orgId==1 || orgId == 1196){
				inParamList.put(4, null);
			}else
				inParamList.put(4, u.getOrg().getShowCoding());

			if (this.searchfinancingcode == null || "".equals(this.searchfinancingcode)) {
				inParamList.put(5, null);
			} else {
				inParamList.put(5, this.searchfinancingcode.toUpperCase());
			}
			

			OutParameterModel outParameter = new OutParameterModel(6, OracleTypes.CURSOR);
			this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_FINANCING", inParamList, outParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "financingList";
	}

	/**
	 * 会员投资统计-详情
	 * @return
	 * @throws Exception
	 */
	public String investorDetailList() throws Exception {
		String returnString = "investorDetailList";
		if (this.getStartDate() == null) {
			this.setStartDate(new Date());
		}
		if (this.getEndDate() == null) {
			this.setEndDate(new Date());
		}
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		if (this.getStartDate() == null) {
			inParamList.put(1, null);
		} else {
			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		}
		if (this.getEndDate() == null) {
			inParamList.put(2, null);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
		}
		
		if (this.getType()==null){
			this.setType("1");
		}
		if (this.username == null || "".equals(this.username)) {
			//throw new RuntimeException("参数不正确，请刷新重试<br/>username is null");
			returnString = "investorDetailListNoReturn";
		}

		inParamList.put(3, this.getType());
		
		inParamList.put(4, this.username);

		if (this.financingcode == null || "".equals(this.financingcode)) {
			inParamList.put(5, null);
		} else {
			inParamList.put(5, this.financingcode.toUpperCase());
		}

		long orgId = u.getOrg().getId();
		if(orgId==1 || orgId == 1196){
			inParamList.put(6, null);
		}else
			inParamList.put(6, u.getOrg().getShowCoding());
		
		if(this.excel.equalsIgnoreCase("0")){
			inParamList.put(7, this.getPage());
			inParamList.put(8, this.getShowRecord());
			PageForProcedureVO pageView = this.investRecordService.callProcedureForPage("P_INVESTOR_INVEST.INVEST_RECORD_DETAIL_PAGE", inParamList);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		}
		// 下面是导出，使用不分页的存储过程
		if(this.excel.equalsIgnoreCase("1")){
			OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
			this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_RECORD_DETAIL", inParamList, outParameter);
			//System.out.println("query resultSet size is : "+this.resultList.size());
			String fileName = new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date());
			ServletActionContext.getResponse().setContentType(
					"application/x-xls");
			ServletActionContext.getResponse().setHeader(
					"Content-Disposition",
					"attachment;filename=" + fileName
							+ "MemberInvestorDetail.xls");
			ServletOutputStream os = ServletActionContext.getResponse()
					.getOutputStream();
			List<String> head = new ArrayList<String>();
			head.add("会员帐号");
			head.add("会员名称");
			head.add("投标额");
			head.add("投标日期");
			head.add("签约日期");
			head.add("项目简称");
			head.add("项目编号");
			head.add("投标合同");
			head.add("介绍人");
			head.add("期次");
			head.add("状态(1:待签约；2：已签约；3：已撤单)");
			List<String> paramOrder = new ArrayList<String>();
			paramOrder.add("investorname");
			paramOrder.add("investorrealname");
			paramOrder.add("investamount");
			paramOrder.add("createdate");
			paramOrder.add("qianyuedate");
			paramOrder.add("fshortname");
			paramOrder.add("financbasecode");
			paramOrder.add("contract_number");
			paramOrder.add("jingbanren");
			paramOrder.add("businesstype");
			paramOrder.add("state");
			List<LinkedHashMap<String, Object>> theData = new ArrayList<LinkedHashMap<String, Object>>();
			theData = resultList;
			ImportExcelUtil.exportInvestors(os, head, paramOrder, theData,"HashMap");
			return null;
		}
		return returnString;
	}
	
	/**
	 * 会员投资统计-详情-导出
	 * @return
	 * @throws Exception
	 */
	public String investorDetailListEx() throws Exception {
		String returnString = "investorlistdetaillist_ex";
		if (this.getStartDate() == null) {
			this.setStartDate(new Date());
		}
		if (this.getEndDate() == null) {
			this.setEndDate(new Date());
		}
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		if (this.getStartDate() == null) {
			inParamList.put(1, null);
		} else {
			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		}
		if (this.getEndDate() == null) {
			inParamList.put(2, null);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
		}
		
		if (this.getType()==null){
			this.setType("1");
		}
		if (this.username == null || "".equals(this.username)) {
			//throw new RuntimeException("参数不正确，请刷新重试<br/>username is null");
			returnString = "investorlistdetaillist_ex";
		}

		inParamList.put(3, Integer.parseInt(this.getType()));
		
		inParamList.put(4, this.username);		

		if (this.financingcode == null || "".equals(this.financingcode)) {
			inParamList.put(5, null);
		} else {
			inParamList.put(5, this.financingcode.toUpperCase());
		}

		long orgId = u.getOrg().getId();
		if(orgId==1 || orgId == 1196){
			inParamList.put(6, null);
		}else
			inParamList.put(6, u.getOrg().getShowCoding());
		OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
		this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_RECORD_DETAIL", inParamList, outParameter);
		return returnString;
	}
	
	public String investDetailListImport() throws Exception {
		//System.out.println("investDetailListImport");
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		if (this.getStartDate() == null) {
			inParamList.put(1, null);
		} else {
			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		}
		if (this.getEndDate() == null) {
			inParamList.put(2, null);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
		}
		if (this.getType()==null){
			this.setType("1");
		}
		if (this.username == null || "".equals(this.username)) {
			//throw new RuntimeException("参数不正确，请刷新重试<br/>username is null");
		}

		inParamList.put(3, this.getType());
		
		inParamList.put(4, this.username);

		if (this.financingcode == null || "".equals(this.financingcode)) {
			inParamList.put(5, null);
		} else {
			inParamList.put(5, this.financingcode.toUpperCase());
		}
		
		long orgId = u.getOrg().getId();
		if(orgId==1 || orgId == 1196){
			inParamList.put(6, null);
		}else
			inParamList.put(6, u.getOrg().getShowCoding());
		
		OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
		this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_RECORD_DETAIL", inParamList, outParameter);
		if(this.resultList==null){
			//System.out.println("query faild!you have to write a new Procedure");
		}else{
			//System.out.println("query resultSet size is : "+this.resultList.size());
			String fileName = new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date());
			ServletActionContext.getResponse().setContentType(
					"application/x-xls");
			ServletActionContext.getResponse().setHeader(
					"Content-Disposition",
					"attachment;filename=" + fileName
							+ "MemberInvestorDetail.xls");
			ServletOutputStream os = ServletActionContext.getResponse()
					.getOutputStream();
			List<String> head = new ArrayList<String>();
			head.add("会员帐号");
			head.add("会员名称");
			head.add("投标额");
			head.add("投标日期");
			head.add("签约日期");
			head.add("项目简称");
			head.add("项目编号");
			head.add("投标合同");
			head.add("状态(1:待签约；2：已签约；3：已撤单)");
			List<String> paramOrder = new ArrayList<String>();
			paramOrder.add("investorname");
			paramOrder.add("investorrealname");
			paramOrder.add("investamount");
			paramOrder.add("createdate");
			paramOrder.add("qianyuedate");
			paramOrder.add("fshortname");
			paramOrder.add("financbasecode");
			paramOrder.add("contract_number");
			paramOrder.add("state");
			List<LinkedHashMap<String, Object>> theData = new ArrayList<LinkedHashMap<String, Object>>();
			theData = resultList;
			ImportExcelUtil.exportInvestors(os, head, paramOrder, theData,
					"HashMap");
		}
		return null;
	}

	public String financingDetailList() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (u == null || u.getOrg() == null) return null;
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		if (this.getStartDate() == null) {
			inParamList.put(1, null);
		} else {
			inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		}
		if (this.getEndDate() == null) {
			inParamList.put(2, null);
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(this.getEndDate());
			c.add(Calendar.DATE, 1);
			inParamList.put(2, new java.sql.Date(c.getTimeInMillis()));
		}
		if (this.getType()==null){
			this.setType("1");
		}
		if (this.financingcode == null || "".equals(this.financingcode)) {
			throw new RuntimeException("参数不正确，请刷新重试<br/>financingcode is null");
		}

		inParamList.put(3, this.getType());
		
		inParamList.put(4, this.financingcode);

		if (this.username == null || "".equals(this.username)) {
			inParamList.put(5, null);
		} else {
			inParamList.put(5, this.username);
		}

		long orgId = u.getOrg().getId();
		if(orgId==1 || orgId == 1196){
			inParamList.put(6, null);
		}else
			inParamList.put(6, u.getOrg().getShowCoding());

		OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);
		this.resultList = this.investRecordService.callProcedureForList("P_INVESTOR_INVEST.INVEST_FINANCING_DETAIL", inParamList, outParameter);
		return "financingDetailList";
	}

	public ArrayList<LinkedHashMap<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<LinkedHashMap<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFinancingcode() {
		return financingcode;
	}

	public void setFinancingcode(String financingcode) {
		this.financingcode = financingcode;
	}

	public String getSearchfinancingcode() {
		return searchfinancingcode;
	}

	public void setSearchfinancingcode(String searchfinancingcode) {
		this.searchfinancingcode = searchfinancingcode;
	}

	public String getSearchusername() {
		return searchusername;
	}

	public void setSearchusername(String searchusername) {
		this.searchusername = searchusername;
	}
	
	public String getSearchjinbanren() {
		return searchjinbanren;
	}

	public void setSearchjinbanren(String searchjinbanren) {
		this.searchjinbanren = searchjinbanren;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}
}
