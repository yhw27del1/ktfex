package com.kmfex.action.sheets;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.service.PaymentRecordService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

@Controller
@Scope("prototype")
public class PaymentWithFinanceAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	
	@Resource PaymentRecordService paymentRecordService;
	@Resource UserService userService;
	@Resource OrgService orgService;
	private ArrayList<LinkedHashMap<String,Object>> resultList;
	
	
	
	private String fcode;
	
	private String fid;
	
	private String selectby;
	
	private int state;
	
	private int qs;
	
	private String queryByOrgCode;
	
	
	
	public String financeList() throws Exception{
		String disUrl = null;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("utf-8");
		
		if (user == null || user.getOrg() == null) return null;
		
		String page_str = request.getParameter("page");
		
		String rows_str = request.getParameter("rows");
		
		String selectby = request.getParameter("selectby");
		
		String state_str = request.getParameter("state");
		
		String orgCode = request.getParameter("orgCode");
		
		String fcode = request.getParameter("fcode");
		
		String action_str = request.getParameter("action");
		
		String order_by = null;
		
		int action = 0;
		
		ArrayList<Object> args_list = new ArrayList<Object>();
		
		String userOrgNo = user.getOrg().getShowCoding();
		
		int page = 1, pagesize = 15 , state = 0;
		
		if(StringUtils.isNotBlank(page_str) && StringUtils.isNumeric(page_str)){
			page = Integer.parseInt(page_str);
		}
		if(StringUtils.isNotBlank(rows_str) && StringUtils.isNumeric(rows_str)){
			pagesize = Integer.parseInt(rows_str);
		}
		
		if(StringUtils.isNotBlank(action_str) && StringUtils.isNumeric(action_str)){
			action = Integer.parseInt(action_str);
		}
		
		StringBuilder fields = new StringBuilder();
		StringBuilder wheresql = new StringBuilder();
		StringBuilder tablename = new StringBuilder();
		StringBuilder groupby = new StringBuilder();
		
		if (this.getStartDate() == null) {
			this.setStartDate(new Date());
		}
		if (this.getEndDate() == null) {
			this.setEndDate(new Date());
		}
		
		SimpleDateFormat short_formatter = new SimpleDateFormat("yyyyMMdd");
		
		
		fields.append(" p.financbaseid,p.yhdate ,to_char(p.shdate,'yyyymmddhh24') shdate,p.qs,p.financbasecode ,p.returntimes, p.qianyuedate,p.frealname,p.fbalance,p.frozenamount,p.forgname,p.remark2,p.overdue_days,p.state,sum(p.yhbj) yhbj,sum(p.yhlx) yhlx,sum(p.yihuanbenjin) shbj,sum(p.yihuanlixi) shlx,sum(p.fj) shfj,sum(p.yhfee1) yhfee1,sum(p.yhfee2) yhfee2,sum(p.yhfee3) yhfee3,sum(p.shfee1) shfee1,sum(p.shfee2) shfee2,sum(p.shfee3) shfee3,sum(p.fj1) fj1,sum(p.fj2) fj2,sum(p.fj3) fj3 , sum(p.ssfee4) ssfee4");
		
		groupby.append(" group by ").append(" p.financbaseid,p.yhdate,to_char(p.shdate,'yyyymmddhh24') ,p.qs,p.financbasecode ,p.returntimes, p.qianyuedate,p.frealname,p.fbalance,p.frozenamount,p.forgname,p.remark2,p.overdue_days,p.state ");
		
		tablename.append("v_paymentrecord p");
		
		
		if(StringUtils.isBlank(selectby)){
			selectby = "to_char(p.yhdate,'yyyymmdd') ";
			order_by = "to_char(p.yhdate,'yyyymmdd') ";
		}else{
			if("yhdate".equals(selectby)){
				selectby = "to_char(p.yhdate,'yyyymmdd')";
				order_by = "to_char(p.yhdate,'yyyymmdd') ";
			}else if("shdate".equals(selectby)){
				selectby = "to_char(p.shdate,'yyyymmdd') ";
				order_by = "to_char(p.shdate,'yyyymmddhh24')"; 
			}else{
				selectby = "to_char(p.yhdate,'yyyymmdd')";
				order_by = "to_char(p.yhdate,'yyyymmdd') ";
			}
		}
		
		
		
		wheresql.append(selectby).append(" between '")
				.append(short_formatter.format(this.getStartDate())).append("' and '")
				.append(short_formatter.format(this.getEndDate())).append("' ");
		
		
		if(StringUtils.isNotBlank(state_str) && StringUtils.isNumeric(state_str)){
			state = Integer.parseInt(state_str);
			if(state == 5){
				wheresql.append(" and p.state != 0 ");
			}else{
				wheresql.append(" and p.state =  ? ");
				args_list.add(state);
			}
		}else{
			wheresql.append(" and p.state = 0 ");
		}
		
		if(StringUtils.isNotBlank(fcode)){
			wheresql.append(" and p.financbasecode like ? ");
			args_list.add(fcode.toUpperCase()+"%");
		}
		
		
		Org selectOrg = this.orgService.findOrg(orgCode);
		
		
		if(StringUtils.isNotBlank(orgCode)){
			if("530100".equals(userOrgNo) || "5301".equals(userOrgNo)){
				wheresql.append(" and p.forgno = ? ");
				args_list.add(orgCode);
			}else{
				wheresql.append(" and p.forgno = ? ");
				args_list.add(userOrgNo);
			}
		}else{
			if(!"530100".equals(userOrgNo) && !"5301".equals(userOrgNo)){
				wheresql.append(" and p.forgno = ? ");
				args_list.add(userOrgNo);
			}
		}
		
		
		Object [] args = args_list.toArray();
		
		
		try {
			if (action == 1) {//打印功能
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fields.toString(), tablename.toString(), wheresql.toString() + groupby.toString()+ " order by "+order_by+" desc",args);
				ServletActionContext.getRequest().setAttribute("resultList", list);
				ServletActionContext.getRequest().setAttribute("state", state);
				if(selectOrg!=null){
					ServletActionContext.getRequest().setAttribute("org", selectOrg);
				}
				disUrl = "financinglist_print";
			}else if(action == 2){//导出功能
				disUrl = "financeList_ex";
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fields.toString(), tablename.toString(), wheresql.toString() + groupby.toString() + " order by "+order_by+" desc",args);
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
				ServletActionContext.getRequest().setAttribute("resultList", list);
			} else {
				PrintWriter out = response.getWriter();
				JSONObject result = new JSONObject();
				JSONArray footerarray = new JSONArray();
				JSONObject footer1 = new JSONObject();
				JSONObject footer2 = new JSONObject();
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fields.toString(), tablename.toString(), wheresql.toString() + groupby.toString() + " order by "+order_by+" desc",args, page, pagesize);
				
				Map<String,Object> sumMap = this.paymentRecordService.queryForObject("sum(p.yhbj_from_contract) yhbj,sum(p.yhlx_from_contract) yhlx,sum(p.yihuanbenjin) shbj,sum(p.yihuanlixi) shlx,sum(p.fj) shfj,sum(p.yhfee1) yhfee1,sum(p.yhfee2) yhfee2,sum(p.yhfee3) yhfee3,sum(p.shfee1) shfee1,sum(p.shfee2) shfee2,sum(p.shfee3) shfee3,sum(p.fj1) fj1,sum(p.fj2) fj2, sum(p.fj3) fj3 ,sum(p.yhbj_from_contract+p.yhlx_from_contract+p.yhfee1+p.yhfee2+p.yhfee3) yhje,sum(p.yihuanbenjin+p.yihuanlixi+p.fj+p.shfee1+p.fj1+p.shfee2+p.fj2+p.shfee3+p.fj3) shje , sum(p.ssfee4) ssfee4 ", tablename.toString(), wheresql.toString(),args);
				
				int total = this.paymentRecordService.queryForListTotal("p.financbaseid,p.qs,to_char(p.shdate,'yyyymmddhh24')", tablename.toString(), wheresql.toString() + " group by p.financbaseid,p.qs,to_char(p.shdate,'yyyymmddhh24')",args);
				footer1.element("YHBJ", sumMap.get("yhbj"));
				footer1.element("YHLX", sumMap.get("yhlx"));
				footer1.element("SHBJ", sumMap.get("shbj"));
				footer1.element("SHLX", sumMap.get("shlx"));
				footer1.element("SHFJ", sumMap.get("shfj"));
				footer1.element("YHFEE1", sumMap.get("yhfee1"));
				footer1.element("YHFEE2", sumMap.get("yhfee2"));
				footer1.element("YHFEE3", sumMap.get("yhfee3"));
				footer1.element("SHFEE1", sumMap.get("shfee1"));
				footer1.element("SHFEE2", sumMap.get("shfee2"));
				footer1.element("SHFEE3", sumMap.get("shfee3"));
				footer1.element("FJ1", sumMap.get("fj1"));
				footer1.element("FJ2", sumMap.get("fj2"));
				footer1.element("FJ3", sumMap.get("fj3"));
				footer1.element("SSFEE4", sumMap.get("ssfee4"));
				footer1.element("FORGNAME", "小计");
				
				
				footer2.element("YHBJ", sumMap.get("yhje"));
				footer2.element("SHBJ", sumMap.get("shje"));
				footer2.element("FORGNAME", "应还合计");
				footer2.element("YHFEE3", "实还合计");
				
				
				footerarray.add(footer1);
				footerarray.add(footer2);
				
				
				
				
				JSONArray array = JSONArray.fromObject(list);
				result.element("rows", array);
				result.element("total", total);
				result.element("footer", footerarray);
				
				out.print(result);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}
	
	
	
	
	
	
	
	
	public ArrayList<LinkedHashMap<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<LinkedHashMap<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getSelectby() {
		return selectby;
	}

	public void setSelectby(String selectby) {
		this.selectby = selectby;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public int getQs() {
		return qs;
	}

	public void setQs(int qs) {
		this.qs = qs;
	}




	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}

	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}


}
