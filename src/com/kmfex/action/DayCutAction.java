package com.kmfex.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.hxbank.HxbankVO;
import com.kmfex.service.DayCutService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.vo.PageForProcedureVO;
import com.wisdoor.struts.BaseAction;

/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class DayCutAction extends BaseAction{
	private Date checkDate;
	private int day;
	@Resource
	private UserService userService;
	@Resource
	private DayCutService dayCutService;
	private String userType;
	private String orgInfo;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6605912122618291608L;
	public String daycut_operate(){
		if(null==this.checkDate)this.checkDate = new Date();//日切日期为当天日期
		this.checkDate();//初始化开始日期和结束日期
		int[] yyyymmdd = this.getDate(this.getStartDate());
		this.day = yyyymmdd[2];//根据所选日来定位显示数据
		Map<Integer, Object> in_detail = new LinkedHashMap<Integer, Object>();
		in_detail.put(1, this.userType);
		in_detail.put(2, this.orgInfo);
		in_detail.put(3, this.getKeyWord());
		in_detail.put(4, yyyymmdd[0]);
		in_detail.put(5, yyyymmdd[1]);
		in_detail.put(6, yyyymmdd[2]);
		in_detail.put(7, this.getPage());
		in_detail.put(8, this.getShowRecord());
		PageForProcedureVO pageView = this.dayCutService.callProcedureForPage("PKG_sys_qingsuan.sys_daycut_detail", in_detail);
		
		Map<Integer, Object> in_sum = new LinkedHashMap<Integer, Object>();
		final OutParameterModel out_sum = new OutParameterModel(7, OracleTypes.CURSOR);
		in_sum.put(1, this.userType);
		in_sum.put(2, this.orgInfo);
		in_sum.put(3, this.getKeyWord());
		in_sum.put(4, yyyymmdd[0]);
		in_sum.put(5, yyyymmdd[1]);
		in_sum.put(6, yyyymmdd[2]);
		ArrayList<LinkedHashMap<String, Object>> sum = this.dayCutService.callProcedureForList("PKG_sys_qingsuan.sys_daycut_sum", in_sum, out_sum);
		if (null != sum) {
			LinkedHashMap<String, Object> s = sum.get(0);
			//Object balance = s.get("balance_sum");
			//Object frozen = s.get("frozen_sum");
			//Object count = s.get("count");
			ServletActionContext.getRequest().setAttribute("balance_sum", s.get("balance_sum"));
			ServletActionContext.getRequest().setAttribute("frozen_sum", s.get("frozen_sum"));
			ServletActionContext.getRequest().setAttribute("count", s.get("count"));
		}
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "dayCutList";
	}
	
	private int[] getDate(Date date){
		int year = DateUtils.getYear(date);
		int month = DateUtils.getMouth(date);
		int day = DateUtils.getDay(date);
		return new int[]{year,month,day};
	}
	
	public String cut() throws Exception{
		System.out.println("系统日切开始了");
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		HxbankVO vo = new HxbankVO();
		int[] yyyymmdd = this.getDate(new Date());//日切日期为系统当前日期
		if(null!=this.checkDate){
			long start = System.currentTimeMillis();
			List<User> ls = this.userService.getUserForUse();
			this.dayCutService.createDayCut(ls, yyyymmdd[0], yyyymmdd[1], yyyymmdd[2]);
			long m = (System.currentTimeMillis()-start)/1000;
			vo.setTip(format.format(new Date())+"，日切成功，共耗时："+m+"秒，共："+ls.size()+"个用户。");
			System.out.println(vo.getTip());
		}else{
			vo.setTip("日切日期不能为空");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String daycut_query(){
		
		return null;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public void setOrgInfo(String orgInfo) {
		this.orgInfo = orgInfo;
	}

	public String getOrgInfo() {
		return orgInfo;
	}
}
