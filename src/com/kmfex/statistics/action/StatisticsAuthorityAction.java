package com.kmfex.statistics.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.kmfex.statistics.service.StatisticsAuthorityService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 授权中心交易跟踪
 * @author  
 */
@SuppressWarnings("serial")
@Controller("stcsAuthorityAction")
@Scope("prototype")
public class StatisticsAuthorityAction  extends BaseAction {
 
	@Resource OrgService orgService; 
	@Resource StatisticsAuthorityService statisticsAuthorityService; 
	@Resource 
	UserService userService; 
	private String queryByOrgCode;
	private String qkeyWord;
	private Date startDate;
	private Date endDate; 
	private int year=2013;
	private String repayState;
	private String dateState;
	/**
	 * 授权中心交易跟踪 
	 * @return
	 */
	public String m_a_Authoritys(){ 
		return "m_a_Authoritys"; 
	}
	
	public void getAuthoritysList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 
			  
			
			 StringBuilder sb = new StringBuilder(" id is not null ");
			 
			//开户日期区间
			if( null != this.getStartDate() && null!= this.getEndDate() ){
				sb.append(" and creatdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')  and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}  
			if(null != qkeyWord&&!"".equals(qkeyWord)){
				sb.append(" and name_ like '%" + qkeyWord + "%'");
			}
			User user=null;
			String orgcoding ="";
			try {
				user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				 orgcoding = user.getOrg().getCoding();
			} catch (Exception e) { 
				e.printStackTrace();
			} 
			if(null != orgcoding&&!"".equals(orgcoding)){
				sb.append(" and coding like '%" + orgcoding+ "%'");      
			}  
			if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
				sb.append(" and showcoding like '%" + queryByOrgCode + "%'");   
			} 
			List<Map<String, Object>> result = this.orgService.queryForList("*","v_org_list",sb.toString(), getPage(), rows);
			//添加操作
			for (Map<String, Object> obj : result) { 
					obj.put("SHOWOK", true); 
			}
			int total = this.orgService.queryForListTotal("id","v_org_list",sb.toString());  

		 
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			 
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 交易量统计
	 * @return
	 */
	public String m_a_jyl(){ 
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();   
//		 try {
//			list = this.statisticsAuthorityService.getMapList_Jyl(year, queryByOrgCode);
//			JSONObject obj = new JSONObject(); 
//			//ServletActionContext.getRequest().setAttribute("list",list); 
//			JSONArray object = JSONArray.fromObject(list);
//			obj.element("total", 1);
//			obj.element("rows", object); 
//			ServletActionContext.getResponse().getWriter().write(obj.toString());
//		} catch (Exception e) { 
//			e.printStackTrace();
//		} 
		
		return "m_a_jyl"; 
	}
	
	/**
	 * 交易量统计
	 * @return
	 */
	public void m_a_jyl_list(){ 
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();   
		 try {
			list = this.statisticsAuthorityService.getMapList_Jyl(year, queryByOrgCode);
			JSONObject obj = new JSONObject(); 
			JSONArray object = JSONArray.fromObject(list);
			obj.element("total", 1);
			obj.element("rows", object); 
			ServletActionContext.getResponse().getWriter().write(obj.toString());
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
	
	/**
	 * 还款统计
	 * @return
	 */
	public String m_a_hk(){ 
		return "m_a_hk"; 
	}
	
	public void m_a_hk_list() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest(); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String page_ = request.getParameter("page");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		int page = 1;
		boolean flag = true;//实还日期查询的时候，flag为true 否则为false
		if(StringUtils.isNotBlank(page_)){
			page = Integer.parseInt(page_);
		}
		String pagesize_ = request.getParameter("rows");
		int pagesize= 15;
		if(StringUtils.isNotBlank(pagesize_)){
			pagesize = Integer.parseInt(pagesize_);
		}
		if(startDate ==null){
			startDate = new Date();
		}
		if(endDate ==null){
			endDate = new Date();
		}
		String fields = "sum(v_p.yhbj) yhbj,sum(v_p.yhlx) yhlx,sum(v_p.fj) fj,to_char(v_p.SHDATE,'yyyy-MM-dd') shdate ";
		
		String tables = "v_paymentrecord v_p ";
		StringBuilder sql = new StringBuilder(); 
		sql.append("  1=1 ");
		StringBuilder group = new StringBuilder(); 
		
		
		if(this.repayState != null && !"".equals(this.repayState)){
			if("5".equals(this.repayState )){
				sql.append("  and v_p.state !=0  ");
			}else
				sql.append("  and v_p.state = " + this.repayState);
		}
		if("yhdate".equals(this.dateState)){
			flag = false;
			fields = "sum(v_p.yhbj_from_contract) yhbj,sum(v_p.yhlx_from_contract) yhlx,sum(v_p.yhbj_from_contract+v_p.yhlx_from_contract) xj,to_char(v_p.YHDATE,'yyyy-MM-dd') yhdate ";
			sql.append(" and  v_p.YHDATE between to_date('"+format.format(startDate)+"','YYYY-MM-DD') and to_date('"+format.format(endDate)+"','YYYY-MM-DD') ");
			group.append(" group by to_char(v_p.YHDATE,'yyyy-MM-dd')  "); 
		}else if("shdate".equals(this.dateState)){
			fields = "sum(v_p.yhbj) shbj,sum(v_p.yhlx) shlx,sum(v_p.fj) fj,sum(v_p.yhbj+v_p.yhlx+v_p.fj) xj,to_char(v_p.SHDATE,'yyyy-MM-dd') shdate ";
			sql.append(" and  v_p.SHDATE between to_date('"+format.format(startDate)+"','YYYY-MM-DD') and to_date('"+format.format(endDate)+"','YYYY-MM-DD') ");
			group.append(" group by to_char(v_p.SHDATE,'yyyy-MM-dd')  "); 
		}else{//意外处理
			fields = "sum(v_p.yhbj) shbj,sum(v_p.yhlx) shlx,sum(v_p.fj) fj,sum(v_p.yhbj+v_p.yhlx+v_p.fj) xj,to_char(v_p.SHDATE,'yyyy-MM-dd') shdate ";
			sql.append(" and  v_p.SHDATE between to_date('"+format.format(startDate)+"','YYYY-MM-DD') and to_date('"+format.format(endDate)+"','YYYY-MM-DD') ");
			group.append(" group by to_char(v_p.SHDATE,'yyyy-MM-dd')  "); 
		}
		sql.append("  and v_p.holder_orgcode='"+queryByOrgCode+"' "); 
		
		StringBuilder orderby = new StringBuilder(); 
		if(sort!=null && order !=null){
			if(flag){
				orderby.append(" order by " + sort + " "+ order);
			}else{
				sort = "YHDATE";
				orderby.append(" order by " + sort + " "+ order);
			}
		}
		JSONObject obj = new JSONObject(); 
		List<Map<String,Object>> list = statisticsAuthorityService.queryForList(fields, tables, sql.toString()+group.toString()+orderby.toString(),page,pagesize);
		double  totalData[] ={0.0,0.0,0.0,0.0,0.0,0.0};  
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			if(flag == false){
				double yhbj = Double.parseDouble( map.get("YHBJ").toString());
				double yhlx = Double.parseDouble( map.get("YHLX").toString());
				totalData[3] += yhbj;
				totalData[4] += yhlx;
				if( yhbj ==0 && yhlx ==0){
					list.remove(i);
				}
			}else{
				double shbj = Double.parseDouble( map.get("SHBJ").toString());
				double shlx = Double.parseDouble( map.get("SHLX").toString());
				double fj = Double.parseDouble( map.get("FJ").toString());
				totalData[0] += shbj;
				totalData[1] += shlx;
				totalData[2] += fj;
				if(0 == shlx && shbj ==0 && fj == 0){
					list.remove(i);
				}
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.00");
		totalData[0] =Double.parseDouble(df.format(totalData[0]));
		totalData[1] =Double.parseDouble(df.format(totalData[1]));
		totalData[2] =Double.parseDouble(df.format(totalData[2]));
		totalData[3] =Double.parseDouble(df.format(totalData[3]));
		totalData[4] =Double.parseDouble(df.format(totalData[4]));
		totalData[5] = totalData[0] + totalData[1] + totalData[2] + totalData[3] + totalData[4];
		totalData[5] =Double.parseDouble(df.format(totalData[5]));
		String totalString = "v_p.SHDATE";
		if(flag == false){
			totalString = "v_p.YHDATE";
		}
		int total = this.statisticsAuthorityService.queryForListTotal("to_char("+totalString+",'yyyy-MM-dd') shdate", tables, sql.toString()+group.toString());
		JSONArray object = JSONArray.fromObject(list);
		obj.element("total", total);
		obj.element("rows", object); 
		JSONArray footer = new JSONArray();
		JSONObject _totalDate = new JSONObject();
		_totalDate.element("SHBJ", totalData[0]);
		_totalDate.element("SHLX", totalData[1]);
		_totalDate.element("FJ", totalData[2]);
		_totalDate.element("YHBJ", totalData[3]);
		_totalDate.element("YHLX", totalData[4]);
		_totalDate.element("XJ", totalData[5]);
		_totalDate.element("SHDATE", "合计");
		_totalDate.element("YHDATE", "合计");
		footer.add(_totalDate);
		obj.element("footer", footer);
		ServletActionContext.getResponse().getWriter().write(obj.toString());
		
	}
	

	/**
	 * 投资会员活跃度统计
	 * @return
	 */
	public String m_a_hy(){ 
		return "m_a_hy"; 
	}
	
	public void m_a_hy_list() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest(); 
		Double nobuy = 0.0;
    	Double buy1 = 0.0;
    	Double buy2 = 0.0;
    	Double buy3_10 = 0.0;
    	Double buy10_ = 0.0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String page_ = request.getParameter("page");
		int page = 1;
		if(StringUtils.isNotBlank(page_)){
			page = Integer.parseInt(page_);
		}
		String pagesize_ = request.getParameter("rows");
		int pagesize= 15;
		if(StringUtils.isNotBlank(pagesize_)){
			pagesize = Integer.parseInt(pagesize_);
		}
		if("1".equals(queryByOrgCode)){
			queryByOrgCode = "5301";
		}
		if(startDate == null ){
			this.startDate = new Date();
		}
		if(endDate == null ){
			this.endDate = new Date();
		}
		String fields = " cyr,count(*) as cyr_by_times ";
		String tables = " v_investrecord ";
		StringBuilder sql = new StringBuilder(); 
		sql.append("  1=1  ");
		sql.append(" and  investdate between to_date('"+format.format(startDate)+"','YYYY-MM-DD') " +
				"and to_date('"+format.format(DateUtils.getAfter(endDate,1))+"','YYYY-MM-DD') ");
		if(queryByOrgCode!=null){
			sql.append("  and investor_orgno='"+queryByOrgCode+"' "); 
		}else{
			sql.append("  and investor_orgno='@@@' "); 
		}
		
		
		sql.append(" group by cyr "); 
		
		JSONObject obj = new JSONObject(); 
		List<Map<String,Object>> list = this.statisticsAuthorityService.queryForList(fields, tables, sql.toString());
		
		for(int i=0;i<list.size();i++){
				Map<String, Object> date2 = list.get(i);
				double times =Double.parseDouble( date2.get("cyr_by_times").toString());
				if(times == 1){
					buy1 +=1;
				}else if(times == 2){
					buy2 +=1;
				}else if(times<11){
					buy3_10 +=1;
				}else{
					buy10_ +=1;
				}
			}
		String countsql = " select count(*) from sys_user,sys_org where sys_user.org_id = sys_org.id and sys_user.usertype_='T' and sys_org.showcoding like '"+queryByOrgCode+"%'";
		int totalinvest = this.userService.selectcount(countsql);
		nobuy += (totalinvest - list.size());
//		System.out.println("totalinvest from statisticsAuthorityAction:"+totalinvest);
//		System.out.println("list.size() from statisticsAuthorityAction:"+list.size());
//		System.out.println("nobuy from statisticsAuthorityAction:"+nobuy.toString());
//		System.out.println(totalinvest);
//		System.out.println(nobuy.toString());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("buyer", totalinvest);
		map.put("nobuy", nobuy);
		map.put("buy1", buy1);
		map.put("buy2", buy2);
		map.put("buy3_10", buy3_10);
		map.put("buy10_", buy10_);
		
		list = new ArrayList<Map<String,Object>>();
		list.add(map);
		int total = 1;
		JSONArray object = JSONArray.fromObject(list);
		obj.element("total", total);
		obj.element("rows", object); 
		ServletActionContext.getResponse().getWriter().write(obj.toString());
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}

	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}

	public String getQkeyWord() {
		return qkeyWord;
	}

	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
	}

	public String getRepayState() {
		return repayState;
	}

	public void setRepayState(String repayState) {
		this.repayState = repayState;
	}

	public String getDateState() {
		return dateState;
	}

	public void setDateState(String dateState) {
		this.dateState = dateState;
	}
	
 
}
