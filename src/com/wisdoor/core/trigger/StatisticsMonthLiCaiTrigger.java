package com.wisdoor.core.trigger;
 
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.kmfex.statistics.service.StatisticsMonthLiCaiService;
import com.wisdoor.core.utils.StringUtils;

public class StatisticsMonthLiCaiTrigger extends TransactionBase {
	private StatisticsMonthLiCaiService statisticsMonthLiCaiService;
	
	private ServletContext servletContext;
	
	@Override
	public void excute() throws Exception {
		List<Map<String,Object>> userlist = this.statisticsMonthLiCaiService.queryForList("u.id,u.username,u.realname", "sys_user u,t_member_base m", " m.user_id = u.id and m.state = '2' and u.usertype_ = 'T' and u.typeflag = '1' ");
		if(userlist == null) return;
		
		SimpleDateFormat sdf_minidate = new SimpleDateFormat("yyyyMM");
		Date shortdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(shortdate);
		cal.add(Calendar.MONTH, -1);
		shortdate.setTime(cal.getTimeInMillis());
		
		
		String templatefilePath = servletContext.getRealPath("/back/statistics/investor_month_rp/index.html");
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(templatefilePath), "utf-8"));
		StringBuilder str = new StringBuilder();
		String temp;
		while ((temp = in.readLine()) != null) {
			str.append(temp);
		}
		in.close();
		
		
		System.out.println("月度理财报告任务开始");
		long start = System.currentTimeMillis();
		for(Map<String,Object> item : userlist){
			if(null == item.get("id") || StringUtils.isBlank(item.get("id").toString()) || !StringUtils.isNumeric(item.get("id").toString())) return;
			if(null == item.get("username") || StringUtils.isBlank(item.get("username").toString())) return;
			if(null == item.get("realname") || StringUtils.isBlank(item.get("realname").toString())) return;
			
			int target_user_id = Integer.parseInt(item.get("id").toString());
			String target_user_username = item.get("username").toString();
			String target_user_realname = item.get("realname").toString();
			
			
			
			String htmlfilename = target_user_username+sdf_minidate.format(shortdate);
			String htmlfileurl = "/back/statistics/investor_month_rp/html/"+htmlfilename+".html";
			String htmlfilePath = servletContext.getRealPath(htmlfileurl);
			
			String repayment_project_chart_url = "/back/statistics/investor_month_rp/html/"+htmlfilename+"_chart_.jpg";
			String repayment_project_chart_path = servletContext.getRealPath(repayment_project_chart_url);
			
			File htmlfile = new File(htmlfilePath);
			
			String result = this.statisticsMonthLiCaiService.processHtmlCode(target_user_id, 
					target_user_username, 
					target_user_realname, 
					shortdate, 
					htmlfilename, 
					htmlfilePath, 
					repayment_project_chart_url, 
					repayment_project_chart_path,
					str.toString());
			
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(htmlfile),"UTF-8");
			writer.write(result);
			writer.flush();
			writer.close();
		}
		long m = (System.currentTimeMillis()-start)/1000;
		System.out.println("月度理财报告任务结束;"+m+"秒;共："+userlist.size()+"个用户。");
		
	}
	

	@Override
	public void init() throws Exception {
		statisticsMonthLiCaiService = (StatisticsMonthLiCaiService)wac.getBean("statisticsMonthLiCaiImpl");
		servletContext = wac.getServletContext();
	}
}
