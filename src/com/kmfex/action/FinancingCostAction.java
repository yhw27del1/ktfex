package com.kmfex.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.MemberBase;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 融资费用 
 * @author  
 * 
 * 修改历史
 * 20130609   将“费用核算”移到融资项目action;
 * 2013年5月29日12:01   修改hesuan()方法,更新所有页面输入的值；
 * 2013年5月29日16:20   修改list()方法,融资项目确认列表按费用表的创建时间倒序；
 * 2013年5月29日16:32   修改hesuanUI()方法,年费和信用管理费是否可以缴费的判断修正,融资项目； 
 * 2013年6月27日16:32   修改list()方法实现列表打印，新增cost_print()方法实现单条费用打印； 
 * 2013年7月02日16:32   修改list()方法,签约前融资费用确认 列表日期查询由根据‘签约日期’改为‘费用创建日期’； 
 * 2013年12月03日14:18   修改fei_yong_que_ren()、feiyongqueren()方法,也刷新缓存； 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class FinancingCostAction extends BaseAction {

	@Resource
	FinancingCostService financingCostService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	FinancingBaseService financingBaseService;
	
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource
	UserService userService;
	@Resource
	ContractKeyDataService contractKeyDataService;
	private String keyWord = "";
	private String queryEname = "";

	private String id;
	private FinancingCost financingCost;
	private MemberBase financier;
	private List<FinancingCost> costs = new ArrayList<FinancingCost>();

	private String userType = "all";

	private String fxbzState = "3";

	private double dbfSum = 0d;
	private double fxglfSum = 0d;
	private double rzfwfSum = 0d;
	private double bzjSum = 0d;
	private double fee1Sum = 0d;

	

	public void fei_yong_que_ren() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		try {
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			FinancingCost financingCostIN = this.financingCostService.getCostByFinancingBase(id);
			financier = financingCostIN.getFinancier();
			this.financingCostService.feiyongjisuan(financingCost,financingCostIN,financier,u);
			
			//更新的融资项目缓存  
			try { ReadsStaticConstantPropertiesUtil.updateServiceCache(id); } catch (Exception e) {}
			
			out.println("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("操作失败，错误信息:"+e.getMessage());
		}
		
	}

	/**
	 * 融资费用列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) return null;
		
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {

			StringBuilder fields = new StringBuilder();
			StringBuilder tables = new StringBuilder();
			StringBuilder wheresql = new StringBuilder();
			ArrayList<Object> args_list = new ArrayList<Object>();
			
			int page = 1 , rows = 15 ; 
			
			if(StringUtils.isNotBlank(request.getParameter("page")) && StringUtils.isNumeric(request.getParameter("page"))){
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			if(StringUtils.isNotBlank(request.getParameter("rows")) && StringUtils.isNumeric(request.getParameter("rows"))){
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			
			
			long time = System.currentTimeMillis();
			
			fields.append("fb.code fb_code,fb.qianyuedate fb_qianyuedate, cbo.showcoding cbo_coding, fm.ename fm_name,fb.currenyamount fb_currenyamount ,fbt.term fbt_term,fb.interestday fb_interestday, fb.guarantee_id fb_guarantee,fc.id fc_id,fc.state fc_state,fc.note fc_note,fc.realamount fc_realamount,fc.createdate FC_CREATEDATE ");
			
			fields.append(",(case when substr(fb.code,1,1)='X' then fc.fee1 || '|' || fc.fee1_tariff || '|' || fc.fee1_bl else fc.fxglf || '|' || fc.fxglf_tariff || '|' || fc.fxglf_bl end ) fc_fxglf ");
			fields.append(",(case when substr(fb.code,1,1)='X' then fc.fee2 || '|' || fc.fee2_tariff || '|' || fc.fee2_bl else fc.rzfwf || '|' || fc.rzfwf_tariff || '|' || fc.rzfwf_bl end ) fc_rzfwf ");
			fields.append(",(case when substr(fb.code,1,1)='X' then fc.fee3 || '|' || fc.fee3_tariff || '|' || fc.fee3_bl else fc.dbf || '|' || fc.dbf_tariff || '|' || fc.dbf_bl end ) fc_dbf ");
			fields.append(",fc.bzj || '|' || fc.bzj_tariff || '|' || fc.bzj_bl fc_bzj ");
			fields.append(",fc.other || '|' || fc.other_tariff || '|' || fc.other_bl fc_other ");
			fields.append(",fc.fee7  fc_xwf ");
			fields.append(",fc.fee10  fc_xyglf ");
			
			
			
			tables.append("t_financing_base fb,t_financing_cost fc,sys_user cbu,sys_org cbo,t_member_base fm,sys_user fu,t_business_type fbt");
			
			
			
			wheresql.append(" fb.createby_id = cbu.id and cbu.org_id = cbo.id and fb.financier_id = fm.id and fm.user_id = fu.id and fb.businesstype_id = fbt.id and fb.id = fc.financingbase_id ");
			
			wheresql.append(" and fb.state in ('5','6','7') ");

			if(StringUtils.isNotBlank(this.keyWord)){
				wheresql.append(" and (")
					.append(" fb.code like ? ")
					.append(" or fb.shortname like ? ")
					.append(" or cbo.showcoding like ? ")
					.append(" or cbo.name_ like ? ")
					.append(") ");
				args_list.add("%"+this.keyWord+"%");
				args_list.add("%"+this.keyWord+"%");
				args_list.add("%"+this.keyWord+"%");
				args_list.add("%"+this.keyWord+"%");
			}
			if(StringUtils.isNotBlank(this.queryEname)){
				wheresql.append(" and (")
				.append(" fm.ename like ? ")
				.append(" or fu.username like ? ")
				.append(" or fu.realname like ? ")
				.append(") ");
				args_list.add("%"+this.queryEname+"%");
				args_list.add("%"+this.queryEname+"%");
				args_list.add("%"+this.queryEname+"%");
			}
			
			
			
			if (this.getStartDate() != null) {
				if (this.getEndDate() == null) {
					Date now = new Date();
					this.setEndDate(now);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				wheresql.append(" and to_char(fc.createdate,'yyyymmdd') between '").append(sdf.format(this.getStartDate())).append("' and '").append(sdf.format(this.getEndDate())).append("' ");
			}
			
			
			
			
			
			
			wheresql.append(" order by fb.state asc,fb.createdate desc,fb.code ");

			
			
			
			
			
			Object [] args = args_list.toArray();
			
			if ("print".equals(userType)) {
				List<Map<String,Object>> list = this.financingBaseService.queryForList(fields.toString(),tables.toString(),wheresql.toString(),args);
				request.setAttribute("list", list);
				return "listCost_print";
			} else {
				List<Map<String,Object>> list = this.financingBaseService.queryForList(fields.toString(),tables.toString(),wheresql.toString(),args, page,rows);
				JSONObject result = new JSONObject();
				JSONArray footarr = new JSONArray();
				result.put("rows", list);
				
				Map<String,Object> summap = this.financingBaseService.queryForObject("sum(fb.currenyamount) fb_currenyamount,sum(fc.realamount) fc_realamount," +
						"sum(case when substr(fb.code,1,1)='X' then fc.fee1  else fc.fxglf end ) fc_fxglf," +
						"sum(case when substr(fb.code,1,1)='X' then fc.fee2 else fc.rzfwf end ) fc_rzfwf," +
						"sum(case when substr(fb.code,1,1)='X' then fc.fee3 else fc.dbf end ) fc_dbf," +
						"sum(fc.bzj) fc_bzj," +
						"sum(fc.other) fc_other," +
						"sum(fc.fee7) fc_xwf," +
						"sum(fc.fee10) fc_xyglf", tables.toString(), wheresql.toString(),args);
				
				
				int total = this.financingBaseService.queryForListTotal("fb.id", tables.toString(),wheresql.toString(),args);
				
				
				result.element("total", total);
				
				footarr.add(summap);
				result.element("footer", footarr);
				
				
				PrintWriter out = response.getWriter();
				out.print(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 结算交易明细列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cost_print() throws Exception {
		 try {
			   ServletActionContext.getRequest().setAttribute("cost", financingCostService.selectById(id));	
			} catch (Exception e) { 
			e.printStackTrace();
		  }
		return "cost_print";
   }
	/**
	 * 结算交易明细列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String finishList() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgCode = u.getOrg().getShowCoding();
		//530100，5301可以查询所有数据
		if (null != orgCode && ("530100".equals(orgCode)||"5301".equals(orgCode))) {
			orgCode = null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			HttpServletRequest request = ServletActionContext.getRequest();
			Date now = new Date();
			if (this.getStartDate() == null) {
				this.setStartDate(now);
			}
			if (this.getEndDate() == null) {
				this.setEndDate(now);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getEndDate());
			cal.add(Calendar.DATE, 1);
			this.setEndDate(cal.getTime());
			
			
			
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("financingBase.qianyueDate", "desc");
			orderby.put("hkDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim().toUpperCase();
				sb.append(" and (");
				sb.append(" o.financingBase.code like ?");
				params.add(keyWord + "%");
				sb.append(" or ");
				sb.append(" o.financingBase.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" )");
			}
			if (null != queryEname && !"".equals(queryEname.trim())) {
				queryEname = queryEname.trim();
				sb.append(" and o.financingBase.financier.eName like ?");
				params.add("%" + queryEname + "%");
			}
			if(null!=orgCode&&!"".equals(orgCode)){
				sb.append(" and o.financingBase.createBy.org.showCoding like ?");
				params.add("%" + orgCode + "%");
			}
			sb.append(" and o.financingBase.qianyueDate between to_date('" + sdf.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + sdf.format(this.getEndDate()) + "','yyyy-MM-dd')");
			QueryResult<FinancingCost> list = financingCostService.getScrollData(sb.toString(), params, orderby);
			for (FinancingCost cost : list.getResultlist()) {
				FinancingBase fb = cost.getFinancingBase();
				cost.setBankmap(this.financingBaseService.groupByBank(fb));
			}
			request.setAttribute("list", list.getResultlist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "finishList";
	}

	// 已签约融资项目各项费用的查询
	public String financingBaseList_qianyued() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String hql = "from FinancingCost o where o.financingBase.state='7' ";
		hql += " and o.hkDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
		hql += " and o.hkDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		/*
		 * if (!"3".equals(fxbzState)) { if ("4".equals(fxbzState)) { hql += "
		 * and o.financingBase.fxbzState in ('0','1') "; } else { hql += " and
		 * o.financingBase.fxbzState='" + fxbzState + "' "; } }
		 */
		if (null != fxbzState && !"".equals(fxbzState.trim())) {

			hql += " and  o.financingBase.fxbzState='" + fxbzState + "' ";
		}
		hql += " order by o.hkDate desc ";
		this.costs = this.financingCostService.getCommonListData(hql);
		dbfSum = 0d;
		fxglfSum = 0d;
		rzfwfSum = 0d;
		bzjSum = 0d;
		fee1Sum = 0d;
		for (FinancingCost cost : this.costs) {
			dbfSum += cost.getDbf();
			fxglfSum += cost.getFxglf() + cost.getFee1();
			rzfwfSum += cost.getRzfwf();
			bzjSum += cost.getBzj();
			// fee1Sum += cost.getFee1();
		}
		return "financingBaseList_qianyued";
	}

	/**
	 * 我的融资费用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listFeeForPerson() throws Exception {
		try {
			PageView<FinancingCost> pageView = new PageView<FinancingCost>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MemberBase mb = memberBaseService.getMemByUser(u);
			sb.append(" and o.financier.id='" + mb.getId() + "' ");

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.financingBase.code like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.financingBase.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" ) and to_number(o.financingBase.state) > 5");
			}

			if (null != queryEname && !"".equals(queryEname.trim())) {
				queryEname = queryEname.trim();
				sb.append(" and o.financingBase.financier.eName like ?");
				params.add("%" + queryEname + "%");
			}
			pageView.setQueryResult(financingCostService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listFeeForPerson";
	}
	
	public void feiyongqueren() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JSONObject result = new JSONObject();
		String action_str = null;
		try {
			String action = request.getParameter("action") == null ? "" : request.getParameter("action").toString();
			if ("pass".equals(action)) {
				action_str = "确认";
				this.financingCostService.cost_pass(id);
			} else if ("ignore".equals(action)) {
				action_str = "驳回";
				this.financingCostService.cost_ignore(id);
			}
			result.element("info", "<"+action_str+"融资项目>操作成功!");
		} catch (Exception e) {
			result.element("info", "<"+action_str+"融资项目>操作失败!exception:"+e.getMessage());
		}finally{
			out.print(result.toString());
		}
		try {
			// 更新未结束的融资项目缓存
			ReadsStaticConstantPropertiesUtil.updateServiceCache(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getQueryEname() {
		return queryEname;
	}

	public void setQueryEname(String queryEname) {
		this.queryEname = queryEname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancingCost getFinancingCost() {
		return financingCost;
	}

	public void setFinancingCost(FinancingCost financingCost) {
		this.financingCost = financingCost;
	}

	public void setCosts(List<FinancingCost> costs) {
		this.costs = costs;
	}

	public List<FinancingCost> getCosts() {
		return costs;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFxbzState() {
		return fxbzState;
	}

	public void setFxbzState(String fxbzState) {
		this.fxbzState = fxbzState;
	}

	public double getDbfSum() {
		return dbfSum;
	}

	public void setDbfSum(double dbfSum) {
		this.dbfSum = dbfSum;
	}

	public double getFxglfSum() {
		return fxglfSum;
	}

	public void setFxglfSum(double fxglfSum) {
		this.fxglfSum = fxglfSum;
	}

	public double getRzfwfSum() {
		return rzfwfSum;
	}

	public void setRzfwfSum(double rzfwfSum) {
		this.rzfwfSum = rzfwfSum;
	}

	public double getBzjSum() {
		return bzjSum;
	}

	public void setBzjSum(double bzjSum) {
		this.bzjSum = bzjSum;
	}  

	public double getFee1Sum() {
		return fee1Sum;
	}

	public void setFee1Sum(double fee1Sum) {
		this.fee1Sum = fee1Sum;
	}

	public MemberBase getFinancier() {
		return financier;
	}

	public void setFinancier(MemberBase financier) {
		this.financier = financier;
	}


}
