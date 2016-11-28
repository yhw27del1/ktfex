package com.kmfex.statistics.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import com.kmfex.model.FinancingRestrain;
import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PaymentRecordService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

@SuppressWarnings("serial")
@Controller("stcsMemBaseAction")
@Scope("prototype")
public class StatisticsMemberBaseAction extends BaseAction{
	private Date repayTrueDate;
	private String fbcode;
	private String state;
	private String uid;
	private String shdate;

	@Resource
	PaymentRecordService paymentRecordService;
	@Resource MemberBaseService memberBaseService;
	@Resource UserService userService;
	
    /**
     * 优化查询（用户账户现转为会员表）
     * @return
     */
	public String getMemberIdsByCon(String strWhere) {
		 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		 String fields = "mb.id";
		 String tables = "SYS_USER su,t_member_base mb,Sys_Account sa,T_MEMBER_LEVEL leveltemp";
		 StringBuilder sb = new StringBuilder(); 
		 sb.append(" su.id=mb.user_id and sa.accountid_=su.useraccount_accountid_  and mb.memberlevel_id=leveltemp.id ");
		 sb.append(strWhere);  
		 String ids=""; 
		 try {  
			    list = memberBaseService.queryForList(fields,tables, sb.toString());
			    if(!list.isEmpty()){
				   for(Map<String,Object> map:list) {      
					   if(map.get("ID") != null){
						   ids+= "'"+map.get("ID").toString()+"',";  
					   }       
			       } 
			    }
			
		  } catch (Exception e) { 
			e.printStackTrace(); 
			ids="";
		 }
		if (null != ids && !"".equals(ids.trim())) {
			ids=ids.substring(0, ids.length()-1);
		}else{
			ids="''";
		}
		return ids;    
	}
	
	/**
	 * 
	 */
	public void memberlevel_json(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			List<Map<String,Object>> list = this.memberBaseService.queryForList("t_member_level.levelname,t_invest_condition.highpercent", "t_invest_condition,t_member_level", "t_member_level.id = t_invest_condition.memberlevel_id"); 
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.element("id", "");
			obj.element("level","不限");
			obj.element("highpercent", "最高投标比例%");
			array.add(obj);
			for(Map<String,Object> item : list){
				JSONObject _obj = new JSONObject();
				_obj.element("id", item.get("levelname"));
				_obj.element("level", item.get("levelname"));
				_obj.element("highpercent", item.get("highpercent")+"%");
				array.add(_obj);
			}
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void m_detail() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String username = request.getParameter("username");
		String realname = request.getParameter("realname");
		String jingbanren = request.getParameter("jingbanren");
		String memberlevel = request.getParameter("memberlevel");
		String state = request.getParameter("state");
		String zzclevel = request.getParameter("zzclevel");
		String balancelevel = request.getParameter("balancelevel");
		
		
		
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
		JSONObject obj = new JSONObject();
		String orgcoding = user.getOrg().getCoding();
		int length = orgcoding.length();
		//StringBuilder sql = new StringBuilder(" substr(orgcoding,0,").append(length).append(") = '"+orgcoding+"' and membertype_code = 'T' ");
		StringBuilder sql = new StringBuilder("  instr(orgcoding,'"+orgcoding+"')> 0   and membertype_code = 'T' ");
		String memberIds="";
		
		JSONArray footerarr = new JSONArray();
		JSONObject footer = new JSONObject();
		
		if(StringUtils.isNotBlank(username)){  
			memberIds=getMemberIdsByCon(" and   su.username='"+username+"' ");
			sql.append(" and id in ("+memberIds+" )");
		}
		if(StringUtils.isNotBlank(realname)){ 
			memberIds=getMemberIdsByCon(" and   su.realname='"+realname+"' ");  
			sql.append(" and id in ("+memberIds+" )");  
		}
		if(StringUtils.isNotBlank(jingbanren)){ 
			sql.append(" and jingbanren = '"+jingbanren+"'");
		}
		if(StringUtils.isNotBlank(memberlevel)){
			//memberIds=getMemberIdsByCon(" and   leveltemp.levelname='"+memberlevel+"' ");  
			//sql.append(" and id in ("+memberIds+" )");  
			sql.append(" and id in (select mb.id from t_member_base mb,T_MEMBER_LEVEL leveltemp where mb.memberlevel_id=leveltemp.id and leveltemp.levelname = '"+memberlevel+"')");
			
		}
		if(StringUtils.isNotBlank(state)){
			sql.append(" and state = '"+state+"'");
		}
		if(StringUtils.isNotBlank(zzclevel)){  
			sql.append(" and id in ( ");
			sql.append("select mb.id from sys_user u,sys_account a,t_member_base mb  ");
			sql.append(",(");
			sql.append("  select id as mbid,bjye from ");
			sql.append("(select mb.id,nvl(sum(it.bjye),0) as bjye from t_invest_record it,sys_user u,t_member_base mb ");
		    sql.append("  where it.investor_id=mb.id and u.id=mb.user_id and it.state='2' group by mb.id ) ");
		    sql.append(")  investRecordBjye ");
		    sql.append("   where u.id=mb.user_id and u.useraccount_accountid_=a.accountid_ and investRecordBjye.mbid=mb.id"); 
			if(zzclevel.contains(",")){
				String[] args = zzclevel.split(",");
				sql.append("  and ((a.balance_+a.frozenamount+investRecordBjye.bjye) between ").append(args[0]).append(" and ").append(args[1]).append(" )");
			}else{
				sql.append("  and (a.balance_+a.frozenamount+investRecordBjye.bjye)"+zzclevel);
			}
		    
		    sql.append(") "); 
		      
 
		}
		if(StringUtils.isNotBlank(balancelevel)){
			sql.append(" and id in (select mb.id from t_member_base mb,SYS_USER su,Sys_Account sa where su.id=mb.user_id and sa.accountid_=su.useraccount_accountid_ ");  
			if(balancelevel.contains(",")){
				String[] args = balancelevel.split(","); 
				sql.append(" and sa.balance_ between "+args[0]+" and "+args[1]);  
			}else{
				sql.append(" and sa.balance_ "+balancelevel);   
			}
			sql.append(") ");
		}
		
		if(this.getStartDate() != null ){
			Calendar cal = Calendar.getInstance();
			
			if(this.getStartDate().getTime() > now.getTime()){
				this.setStartDate(now);
			}
			if( this.getEndDate() == null || this.getEndDate().getTime() > now.getTime()){
				this.setEndDate(now);
			}
			cal.setTime(this.getEndDate());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			this.setEndDate(cal.getTime());
			
			sql.append(" and createdate between to_date('")
				.append(sdf.format(this.getStartDate()))
				.append("','yyyy-mm-dd hh24:mi:ss') and to_date('")
				.append(sdf.format(this.getEndDate()))
				.append("','yyyy-mm-dd hh24:mi:ss'")
				.append(")");
		}
		
		sql.append(" order by user_username ");
		
		List<Map<String,Object>> list = memberBaseService.queryForList("user_username,name,sexstr,user_account_balance,invest_all_count,jingbanren,createdate,member_level,orgname,banklib,state,cityname,zzc", "v_member_user_account", sql.toString(), page, pagesize);
		for(Map<String,Object> rec : list){
			String user_name = rec.get("user_username").toString();
			if(StringUtils.isNotBlank(user_name)){
				List<Map<String,Object>> result = this.memberBaseService.queryForList("max(createdate) time", "V_INVESTRECORD", "investorname = '"+user_name+"'");
				if( result!=null && result.get(0) != null && result.get(0).get("time") !=null){
					rec.put("ZUIJINSHENGOU", result.get(0).get("time"));
				}
			}
		}
		
		int total = this.memberBaseService.queryForListTotal("user_username", "v_member_user_account", sql.toString());
		JSONArray object = JSONArray.fromObject(list);
		obj.element("total", total);
		obj.element("rows", object);
		
		List<Map<String,Object>> footerlist = memberBaseService.queryForList("sum(user_account_balance) balace_sum,sum(zzc) zzc_sum", "v_member_user_account", sql.toString());
		footer.element("USER_USERNAME", "合计");
		if(footerlist!=null && footerlist.size() > 0 && footerlist.get(0) != null){
			footer.element("USER_ACCOUNT_BALANCE", footerlist.get(0).get("balace_sum"));
			footer.element("ZZC", footerlist.get(0).get("zzc_sum"));
		}
		footerarr.element(footer);
		obj.element("footer", footerarr);
		
		response.getWriter().println(obj);
	}
	
	public void m_b_detail() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = request.getParameter("username");
		String state = request.getParameter("state");
		String fcode = request.getParameter("fcode");
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
		
		JSONArray footerarr = new JSONArray();
		JSONObject footer = new JSONObject();
		
		JSONObject obj = new JSONObject();
		StringBuilder sql = new StringBuilder();
		sql.append("holderusername = '").append(username).append("' and state = '2' and financbasestate = '7' ");
		
		if(StringUtils.isNotBlank(state)){
			sql.append(" and terminal = ").append(state);
		}
		
		if(StringUtils.isNotBlank(fcode)){
			sql.append(" and financbasecode like '").append(fcode.toUpperCase()).append("%' ");
		}
		
		if(this.getStartDate()!=null && this.getEndDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getEndDate());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			this.setEndDate(cal.getTime());
			sql.append(" and FINANCIER_MAKE_SURE_DATE between to_date('").append(sdf.format(this.getStartDate())).append("','YYYY-MM-DD')")
			.append(" and to_date('").append(sdf.format(this.getEndDate())).append("','YYYY-MM-DD')");
		}
		
		
		sql.append(" order by FINANCIER_MAKE_SURE_DATE desc,terminal desc");
		
		
		List<Map<String,Object>> list = memberBaseService.queryForList("*", "v_investrecord", sql.toString(),page,pagesize);
		List<Map<String,Object>> footerlist = memberBaseService.queryForList("sum(investamount) invest_amount", "v_investrecord", sql.toString());
		int total = this.memberBaseService.queryForListTotal("id", "v_investrecord", sql.toString());
		JSONArray object = JSONArray.fromObject(list);
		obj.element("total", total);
		obj.element("rows", object);
		
		footer.element("FINANCIER_MAKE_SURE_DATE", "合计");
		if(footerlist!=null && footerlist.size() > 0 && footerlist.get(0) != null){
			footer.element("INVESTAMOUNT", footerlist.get(0).get("invest_amount"));
		}
		footerarr.element(footer);
		obj.element("footer", footerarr);
		response.getWriter().println(obj);
		
	}
	
	/**
	 * 查询指定投资人，指定债权的还款情况 
	 * @throws Exception
	 */
	public void m_p_detail() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String investrecord_id = request.getParameter("investrecord_id");
		
		JSONObject obj = new JSONObject();
		JSONArray footerarr = new JSONArray();
		JSONObject footer = new JSONObject();
		StringBuilder sql = new StringBuilder();
		sql.append(" investrecord_id = '").append(investrecord_id).append("' group by to_date(to_char(p.predict_repayment_date,'yyyy-mm-dd'),'yyyy-mm-dd') ,to_date(to_char(p.actually_repayment_date,'yyyy-mm-dd'),'yyyy-mm-dd'),p.state,p.remark_,p.group_ order by p.group_ ");
		
		
		List<Map<String,Object>> list = memberBaseService.queryForList("to_date(to_char(p.predict_repayment_date,'yyyy-mm-dd'),'yyyy-mm-dd') yhdate,to_date(to_char(p.actually_repayment_date,'yyyy-mm-dd'),'yyyy-mm-dd') shdate,p.state,sum(p.xybj) xybj,sum(p.xylx) xylx,sum(p.shbj) shbj,sum(p.shlx) shlx,sum(p.penal) fj,p.remark_", " t_payment_record p", sql.toString());
		
		Map<String,Object> footer_map = memberBaseService.queryForObject("sum(p.xybj) xybj,sum(p.xylx) xylx,sum(p.shbj) shbj,sum(p.shlx) shlx,sum(p.penal) fj  , '合计' shdate", " t_payment_record p", " investrecord_id = '"+investrecord_id+"'");
		
		footer.putAll(footer_map);
		
		footerarr.add(footer);
		
		
		JSONArray object = JSONArray.fromObject(list);
		obj.element("rows", object);
		obj.element("footer", footerarr);
		response.getWriter().println(obj);
	
	}
	
	/**
	 * 投标情况
	 * @throws Exception 
	 */
	public void m_i_detail() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
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
		
		
		
		JSONArray footerarr = new JSONArray();
		JSONObject footer = new JSONObject();
		JSONObject obj = new JSONObject();
		StringBuilder sql = new StringBuilder();
		String sql_nogroup ;
		sql.append("investorname = '").append(username).append("' and financbasestate != '8'");
		if(this.getStartDate()!=null && this.getEndDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getEndDate());
			cal.add(Calendar.MONTH, 1);
			this.setEndDate(cal.getTime());
			sql.append(" and createdate between to_date('").append(sdf.format(this.getStartDate())).append("','YYYY-MM-DD')")
			.append(" and to_date('").append(sdf.format(this.getEndDate())).append("','YYYY-MM-DD')");
		}
		sql_nogroup = sql.toString();
		sql.append(" group by to_date(to_char(createdate,'yyyy-mm'),'yyyy-mm')  order by to_date(to_char(createdate,'yyyy-mm'),'yyyy-mm') desc");
		
		
		List<Map<String,Object>> list = memberBaseService.queryForList("to_date(to_char(createdate,'yyyy-mm'),'yyyy-mm') invest_date,count(*) invest_times,sum(investamount) invest_amount", "v_investrecord", sql.toString(),page,pagesize);
		List<Map<String,Object>> footerlist = memberBaseService.queryForList("sum(investamount) invest_amount", "v_investrecord", sql_nogroup);
		int total = this.memberBaseService.queryForListTotal("to_date(to_char(createdate,'yyyy-mm'),'yyyy-mm')", "v_investrecord", sql.toString());
		JSONArray object = JSONArray.fromObject(list); 
		obj.element("total", total);
		obj.element("rows", object);
		footer.element("INVEST_TIMES", "合计");
		if(footerlist!=null && footerlist.size() > 0 && footerlist.get(0) != null){
			footer.element("INVEST_AMOUNT", footerlist.get(0).get("invest_amount"));
		}
		footerarr.element(footer);
		obj.element("footer", footerarr);
		response.getWriter().println(obj);
	}
	/**
	 * 投标情况详细
	 * @throws Exception 
	 */
	public void m_i_m_detail() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String date = request.getParameter("date");
		String username = request.getParameter("username");
		
		
		JSONObject obj = new JSONObject();
		StringBuilder sql = new StringBuilder();
		sql.append("investorname = '").append(username).append("' and financbasestate != '8' ");
		if(StringUtils.isNotBlank(date)){
			sql.append(" and to_date(to_char(createdate,'yyyy-mm'),'yyyy-mm') = to_date('").append(date).append("','yyyy-mm')");
		}
		sql.append(" order by createdate desc");
		List<Map<String,Object>> list = memberBaseService.queryForList("createdate invest_date ,investamount invest_amount,financbasecode,FINANCIER_MAKE_SURE_DATE f_m_s_d", "v_investrecord", sql.toString());
		List<Map<String,Object>> footerlist = memberBaseService.queryForList("sum(investamount) invest_amount", "v_investrecord", sql.toString());
		JSONArray footerarr = new JSONArray();
		JSONObject footer = new JSONObject();
		JSONArray object = JSONArray.fromObject(list); 
		obj.element("rows", object);
		footer.element("INVEST_DATE", "合计");
		if(footerlist != null && footerlist.size() > 0 && footerlist.get(0) != null){
			footer.element("INVEST_AMOUNT", footerlist.get(0).get("invest_amount"));
		}
		footerarr.add(footer);
		obj.element("footer", footerarr);
		
		list.clear();
		
		response.getWriter().println(obj);
	}
	/**
	 * 用户余额
	 * @throws Exception
	 */
	public void member_ye_list() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		if(StringUtils.isBlank(username)) return;
		
		List<Map<String,Object>> userlist = this.memberBaseService.queryForList("id", "sys_user", "username = '"+username+"'");
		
		if(userlist == null || userlist.size() == 0 || userlist.get(0) == null || userlist.get(0).get("id") == null) return;
		
		int user_id = Integer.parseInt(userlist.get(0).get("id").toString());
		
		JSONObject obj = new JSONObject();
		JSONArray result_arr = new JSONArray();
		StringBuilder sql = new StringBuilder();
		
		
		sql.append("user_id = ").append(user_id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(this.getStartDate() != null && this.getEndDate() !=null){
			sql.append(" and to_date((year||'-'||month),'yyyy-mm') ");
			if(this.getStartDate().getMonth() == this.getEndDate().getMonth()){
				sql.append(" = to_date('").append(sdf.format(this.getStartDate())).append("','YYYY-MM')");
			}else{
				sql.append("between to_date('").append(sdf.format(this.getStartDate())).append("','YYYY-MM')")
				.append(" and to_date('").append(sdf.format(this.getEndDate())).append("','yyyy-MM') ");
			}
		}
		
		
		
		sql.append(" order by year desc,month desc");
		List<Map<String,Object>> list = memberBaseService.queryForList("*", "t_daycut", sql.toString());
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		for(Map<String,Object> item : list){
			int year = Integer.valueOf(item.get("year").toString());
			int month = Integer.valueOf(item.get("month").toString());
			for(int _day = 31; _day >= 1 ; _day--){
				double _balance = Double.valueOf(item.get("balance_"+_day).toString());
				double _frozen = Double.valueOf(item.get("frozen_"+_day).toString());
				
				if(this.getStartDate() != null && this.getEndDate() !=null){
					Date _date = sdf2.parse(year+"-"+month+"-"+_day);
					if(this.getStartDate().getTime() > _date.getTime() || this.getEndDate().getTime() < _date.getTime()){
						continue;
					}
				}
				JSONObject temp = new JSONObject();
				temp.element("year", year);
				temp.element("month", month);
				temp.element("date", _day);
				temp.element("balance", _balance);
				temp.element("frozen", _frozen);
				result_arr.add(temp);
			}
		}
		obj.element("rows", result_arr);
		
		list.clear();
		
		response.getWriter().println(obj);
	}
	
	
	/**
	 * 风险提示列表
	 * @throws Exception
	 */
	public void member_fx_list() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		
		if(StringUtils.isBlank(username)) return;
		
		List<Map<String,Object>> userlist = this.memberBaseService.queryForList("id", "sys_user", "username = '"+username+"'");
		
		if(userlist == null || userlist.size() == 0 || userlist.get(0) == null || userlist.get(0).get("id") == null) return;
		
		int user_id = Integer.parseInt(userlist.get(0).get("id").toString());
		
		JSONObject obj = new JSONObject();
		StringBuilder sql = new StringBuilder();
		
		
		sql.append("beneficiary_id = ").append(user_id).append(" group by state order by state");
		
		List<Map<String,Object>> list = memberBaseService.queryForList("state,count(id) counts", "t_payment_record", sql.toString());
		
		int total = 0;
		for(Map<String,Object> item : list){
			int counts = Integer.parseInt(item.get("counts").toString());
			item.put("scale", 0);
			total += counts;
		}
		if(total > 0){
			int exists = 0;
			for(Iterator<Map<String,Object>> iter = list.iterator(); iter.hasNext();){
				Map<String,Object> item = iter.next();
				if(iter.hasNext()){
					int counts = Integer.parseInt(item.get("counts").toString());
					int scale = (int)((float)counts / (float)total * (float)100); 
					exists += scale;
					item.put("scale", scale);
				}else{
					item.put("scale", 100-exists);
				}
				item.put("all", total);
			}
		}
		
		obj.element("rows", list);
		
		list.clear();
		
		response.getWriter().println(obj);
	}
	
	/**
	 * 回款情况汇总
	 * 输出JSON
	 */
	public void getPaymentRecordJson(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			StringBuilder subsql = new StringBuilder();
			String sqbsqlfortotal = null ;
			subsql.append(" state!=0  ");
			
			
			//实还日期区间
			if( null != this.getStartDate() && null!= this.getEndDate() ){
				subsql.append(" and shdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')  and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}
			
			if(null != uid){
				subsql.append(" and beneficiary_username = '" + uid + "'");
			}
			
			//机构控制由会员列表控制
			sqbsqlfortotal = subsql.toString();

			subsql.append(" group by to_char(shdate,'yyyy-MM-dd')  ");
			
			if(sort!=null && order !=null)
				subsql.append(" order by " + sort + " "+ order);
			
			List<Map<String, Object>> result = this.paymentRecordService.queryForList("count(*) as bs,sum(yhbj_from_contract+yhlx_from_contract) as yh,sum(yhbj+yhlx+fj) as sh,to_char(shdate,'yyyy-MM-dd') as shdate",
					"v_paymentrecord",subsql.toString(), getPage(), rows);
			int total = this.paymentRecordService.queryForListTotal("to_date(to_char(shdate,'yyyy-MM-dd'),'yyyy-MM-dd')","V_PAYMENTRECORD",subsql.toString());

			//添加操作
			for (Map<String, Object> obj : result) {
				if (null != obj.get("shdate")) {
					obj.put("showok", true);
				}
			}
			JSONObject _totalDate = new JSONObject();
			JSONArray footer = new JSONArray();
			List<Map<String, Object>> total_list = paymentRecordService.queryForList("sum(YHBJ+YHLX) as yh,sum(yhbj+yhlx+fj) as sh", "v_paymentrecord", sqbsqlfortotal.toString());
			if(total_list != null && total_list.size() > 0 && total_list.get(0) != null ){
				_totalDate.element("YH", total_list.get(0).get("yh"));
				_totalDate.element("SH", total_list.get(0).get("sh"));
			}
			
			
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			
			
			
//			_totalDate.element("MAXAMOUNT", totalData[0]);
//			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("SHDATE", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 回款情况详情
	 * 输出JSON
	 */
	public void getPaymentRecordJsonDetail(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			
			
			StringBuilder subsql = new StringBuilder();
			subsql.append(" state!=0  ");
			
			
			//融资项目
			if( null != this.fbcode && !"".equals(this.fbcode.trim())){
				subsql.append(" and financbasecode like '%" + fbcode + "%'");
			}
			
			//状态
			if( null != this.state && !"".equals(this.state.trim())){
				subsql.append(" and state =" + this.state + "");
			}
			
			//实还日期
			if( null != this.shdate ){
				subsql.append(" and to_date(to_char(shdate,'yyyy-MM-dd'),'yyyy-MM-dd') = to_date('" + shdate + "','yyyy-MM-dd')  ");
			}
			
			if(null != uid){
				subsql.append(" and beneficiary_username = '" + uid + "'");
			}
			
			//机构控制由会员列表控制
			
			if(sort!=null && order !=null)
				subsql.append(" order by " + sort + " "+ order);
			
			List<Map<String, Object>> result = this.paymentRecordService.queryForList("financbasecode as FBCODE, " +
					" qs||'/'|| returntimes as QS, " +
					" qianyuedate as QYDATE, " +
					" investorname as TZRCODE, " +
					" investorrealname as TZRNAME," +
					" yihuanbenjin as SHB, " +
					" INVESTAMOUNT,"+
					" yihuanlixi as SHX," +
					" yhbj_from_contract as yhbj," +
					" yhlx_from_contract as yhlx," +
					" FJ as FJ, shdate as SHDATE," +
					" (case     when ( state=1)     then         '正常还款'     when ( state=2)     then         '提前还款'     when ( state=3)     then         '逾期还款'   when ( state=4)     then         '担保代偿'     else        state || '' end) AS state",
					"v_paymentrecord",subsql.toString());
			int total = this.paymentRecordService.queryForListTotal("financbasecode as FBCODE ","V_PAYMENTRECORD",subsql.toString());

		
			
			
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total); 
			o.element("rows", object);
			//int[] totalData = paymentRecordService.queryForExpiredListCount(subsql.toString());
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
//			_totalDate.element("MAXAMOUNT", totalData[0]);
//			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("FBCODE", "合计");
			footer.add(_totalDate);
			//o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Date getRepayTrueDate() {
		return repayTrueDate;
	}

	public void setRepayTrueDate(Date repayTrueDate) {
		this.repayTrueDate = repayTrueDate;
	}

	public String getFbcode() {
		return fbcode;
	}

	public void setFbcode(String fbcode) {
		this.fbcode = fbcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getShdate() {
		return shdate;
	}

	public void setShdate(String shdate) {
		this.shdate = shdate;
	}
	
}
