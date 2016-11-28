package com.kmfex.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.FinancingCost;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;

/**
 * 担保公司、担保费总的统计
 * @author 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class GuaranteeAction extends BaseAction {

	@Resource
	FinancingCostService financingCostService;
	@Resource
	MemberBaseService memberBaseService; 
	private String keyWord = "";  
	private double dbfSum = 0d;
	private double dbfSumXyd = 0d;
	private double dbfPageSum = 0d;
	private double dbfPageSumXyd = 0d;
	
	private String id="0";
 
	
	/**
	 * 担保费用列表 
	 */
	public String list() throws Exception {
		checkDate();
		if("0".equals(id)){
			return "list";
		} 
		try {
			PageView<FinancingCost> pageView = new PageView<FinancingCost>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1   and o.financingBase.guarantee is not null ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			  MemberBase member =null;
			if (null != u)
				org = u.getOrg();
			    member = this.memberBaseService.getMemByUser(u);
			if (null != org) {
				orgCode = org.getCoding();
			}
			if(null!=member){
			   if(MemberType.CODE_SECURED.equals(member.getMemberType().getCode()))//担保公司登陆
			   { 
				    sb.append(" and o.financingBase.guarantee.id='"+ member.getId() + "' ");
 			   }else {
					sb.append(" and o.financingBase.guarantee.id='@@@@@@@' ");
				} 
			}else{//管理人员 
				if (null != orgCode && !"".equals(orgCode)) { 
					sb.append(" and o.financingBase.createBy.org.coding like '"+ orgCode + "%' ");
				} else {
					sb.append(" and o.financingBase.createBy.org.coding like '@@@@@@@%' ");
				} 
			}
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.financingBase.code like '%" + keyWord + "%'");
				sb.append(" or ");
				sb.append(" o.financingBase.shortName like '%" + keyWord + "%'");
				sb.append(" or ");
				sb.append(" o.financingBase.guarantee.eName like '%" + keyWord + "%'   ");
				sb.append(" )");
			} 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			if(null != this.getStartDate() ){
				sb.append(" and o.financingBase.qianyueDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			}
			if( null != this.getEndDate()){
				sb.append(" and o.financingBase.qianyueDate <= to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')+1");
			}	
			
			pageView.setQueryResult(financingCostService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby)); 
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		 
			dbfPageSum = 0d; 
			dbfPageSumXyd= 0d; 
			for (FinancingCost cost : pageView.getRecords()) {
				dbfPageSumXyd += cost.getFee3(); 
				dbfPageSum += cost.getDbf();
			}
			
			//统计总金额
			dbfSum = 0d; 
			dbfSumXyd= 0d; 
			List<FinancingCost> fcs=financingCostService.getCommonListData("from FinancingCost o where "+sb.toString());
			for (FinancingCost c :fcs) {
				dbfSumXyd += c.getFee3();    
				dbfSum += c.getDbf();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	public double getDbfSum() {
		return dbfSum;
	}

	public void setDbfSum(double dbfSum) {
		this.dbfSum = dbfSum;
	}

	public double getDbfSumXyd() {
		return dbfSumXyd;
	}

	public void setDbfSumXyd(double dbfSumXyd) {
		this.dbfSumXyd = dbfSumXyd;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public double getDbfPageSum() {
		return dbfPageSum;
	}

	public void setDbfPageSum(double dbfPageSum) {
		this.dbfPageSum = dbfPageSum;
	}

	public double getDbfPageSumXyd() {
		return dbfPageSumXyd;
	}

	public void setDbfPageSumXyd(double dbfPageSumXyd) {
		this.dbfPageSumXyd = dbfPageSumXyd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 
}
