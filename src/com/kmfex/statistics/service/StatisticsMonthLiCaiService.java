package com.kmfex.statistics.service;

import java.util.Date;

import com.wisdoor.core.service.BaseService;

public interface StatisticsMonthLiCaiService extends BaseService<Object> {
	/**
	 * 生成HTML文件<br/>
	 * 读入模板文件，生成用户理财月报文件
	 * 
	 * @param target_user_id 目标交易帐号ID
	 * @param target_user_username 目标交易帐号编号
	 * @param target_user_realname 目标交易帐号实名
	 * @param shortdate 生成目标日期
	 * @param htmlfilename 生成HTML文件名（PDF下载文件名）
	 * @param htmlfilePath 生成HTML文件完整路径 
	 * @param repayment_project_chart_url 下月还款情况图例文件URL（显示在HTML中）
	 * @param repayment_project_chart_path 下月还款情况图例完整路径（用于生成文件）
	 * @return html页面代码
	 */
	public String processHtmlCode(long target_user_id,
			String target_user_username, 
			String target_user_realname, 
			Date shortdate, 
			String htmlfilename, 
			String htmlfilePath, 
			String repayment_project_chart_url, 
			String repayment_project_chart_path,
			String templatestr);
}
