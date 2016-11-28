package com.kmfex.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.kmfex.model.CreditRules;
import com.kmfex.service.CreditRulesService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.struts.BaseAction;
/**
 * 积分管理 
 * @author eclipseeeeeeeeeeeeee
 *
 */
@Controller
@SuppressWarnings("serial")
public class CreditRulesAction extends BaseAction implements Preparable{
	@Resource CreditRulesService creditRulesService;
	@Resource AccountService accountService;
	private List<CreditRules> rules;
	
	private CreditRules creditrule;
	
	private File excel;
	private String excelFileName;
	private String excelContentType;
	
	private HashMap<String, Double> list = new HashMap<String,Double>();
	
	/**
	 * 查询新建用户时的规则
	 * @return
	 */
	public String rules_list(){
		try {
			PageView<CreditRules> pageview = new PageView<CreditRules>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put( "createtime","desc");
			QueryResult<CreditRules> rules = this.creditRulesService.getScrollData(pageview.getFirstResult(), pageview.getMaxresult(), orderby);
			pageview.setQueryResult(rules);
			ServletActionContext.getRequest().setAttribute("pageView", pageview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "rules_list";
	}

	/**
	 * 
	 * @return
	 */
	public String setEnable() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			boolean value = Boolean.parseBoolean(request.getParameter("value"));
			CreditRules cr = this.creditRulesService.selectById(id);
			if(cr!=null){
				if(value){
					CreditRules cr_ = this.creditRulesService.selectByHql("from CreditRules where enable = true");
					if(cr_!=null){
						cr_.setEnable(false);
						this.creditRulesService.update(cr_);
					}
				}
				cr.setEnable(value);
				this.creditRulesService.update(cr);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String save(){
		try {
			this.creditRulesService.insert(this.creditrule);
			return "save";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String imp_json(){
		try {
			this.list.clear();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			DecimalFormat df = new DecimalFormat("#");
			out.print("{\"list\":[");
			if(this.excel!=null){
				InputStream input = new FileInputStream(this.excel);
				POIFSFileSystem fs  = new POIFSFileSystem(input);  
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				int firstrownum = sheet.getFirstRowNum();
				for(int y = firstrownum; y<sheet.getPhysicalNumberOfRows(); y++){
					HSSFRow row = sheet.getRow(y);
					String username = df.format(row.getCell((short)0).getNumericCellValue());
					double credit = row.getCell((short)1).getNumericCellValue();
					out.print("{\"username\":\"");
					out.print(username);
					out.print("\",\"credit\":");
					out.print(credit);
					out.print("}");
					list.put(username, credit);
					if(y!=sheet.getPhysicalNumberOfRows()-1){
						out.print(",");
					}
				}
				input.close();
			}
			out.print("]}");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String imp_do(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out =response.getWriter();
			out.print("{\"resultlist\":[");
			Iterator<Entry<String,Double>> it = list.entrySet().iterator();  
			while(it.hasNext()){
				Map.Entry<String,Double> e = (Map.Entry<String,Double>) it.next();
				String username = e.getKey().toString();
				int value = (int)(Math.round(e.getValue()*100)/100);
				Account account = accountService.selectById("from Account where user.username = ?", username);
				out.print("{\"username\":\""+username+"\",");
				if(account != null){
					account.setCredit(value);
					out.print("\"result\":\"成功\"}");
					this.accountService.update(account);
				}else{
					out.print("\"result\":\"未找到\"}");
				}
				if(it.hasNext()){
					out.print(",");
				}
			}
			
			out.print("]}");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String clear(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			this.accountService.executeHql("update Account set credit = 0 where credit != 0");
			out = response.getWriter();
			out.print("{\"result\":\"操作成功\"");
		} catch (Exception e) {
			out.print("{\"result\":\""+e+"\"");
		}
		return null;
	}
	
	
	
	public String rule(){
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "rule";
	}
	
	
	public List<CreditRules> getRules() {
		return rules;
	}

	public void setRules(List<CreditRules> rules) {
		this.rules = rules;
	}
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		this.creditrule = new CreditRules();
	}
	public CreditRules getCreditrule() {
		return creditrule;
	}
	public void setCreditrules(CreditRules creditrule) {
		this.creditrule = creditrule;
	}

	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public String getExcelContentType() {
		return excelContentType;
	}

	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}
	
	
	
	
	
	
}
