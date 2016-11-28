package com.kmfex.statistics.service.impl;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.stereotype.Service;

import com.kmfex.statistics.service.StatisticsMonthLiCaiService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
 
@Service
public class StatisticsMonthLiCaiImpl extends BaseServiceImpl<Object> implements StatisticsMonthLiCaiService{
	
	@Override
	public String processHtmlCode(long target_user_id,
			String target_user_username, 
			String target_user_realname, 
			Date shortdate, 
			String htmlfilename, 
			String htmlfilePath, 
			String repayment_project_chart_url, 
			String repayment_project_chart_path,
			String templatestr){
		String result = null;
			
		
		SimpleDateFormat sdf_shortdate = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf_text_shortdate = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			// ---------------------------生成开始

			// 会员信息
			StringBuilder sql_memberList = new StringBuilder("user_id = ");
			sql_memberList.append(target_user_id);
			List<Map<String, Object>> memberList = this.queryForList("sex,zzc,user_account_balance,user_account_frozenamount,cyzq", "v_member_user_account", sql_memberList.toString());

			// 帐户信息
			StringBuilder sql_accountList = new StringBuilder("userid = ");
			sql_accountList.append(target_user_id);
			List<Map<String, Object>> accountList = this.queryForList("cash_in,cash_out,rate_all,total_all", "v_accountincome", sql_accountList.toString());
			
			
			// 本月情况
			// 本月借出
			StringBuilder sql_this_month_invest = new StringBuilder("holderusername = '").append(target_user_username).append("' ").append(" and to_date(to_char(financier_make_sure_date,'yyyy-mm'),'yyyy-mm') = to_date('").append(sdf_shortdate.format(shortdate)).append("','yyyy-mm') and financier_make_sure_date is not null group by to_date(to_char(financier_make_sure_date,'yyyy-mm'),'yyyy-mm') ");
			List<Map<String, Object>> this_month_invest_List = this.queryForList("sum(investamount) all_invest_amount", "v_investrecord", sql_this_month_invest.toString());
			
			// 本月回收
			StringBuilder sql_this_month_repayment = new StringBuilder("beneficiary_id = ").append(target_user_id).append(" and to_date(to_char(actually_repayment_date,'yyyy-mm'),'yyyy-mm') = to_date('").append(sdf_shortdate.format(shortdate)).append("','yyyy-mm') and state != 0 group by to_date(to_char(actually_repayment_date,'yyyy-mm'),'yyyy-mm') ");
			List<Map<String, Object>> this_month_repayment_List = this.queryForList("sum(paid_debt) all_payment_amount,sum(shlx) all_payment_lx,sum(penal) all_payment_fx", "t_payment_record", sql_this_month_repayment.toString());

			// 风险分析
			StringBuilder sql_risk = new StringBuilder();
			sql_risk.append("beneficiary_id = ").append(target_user_id).append(" group by state order by state desc");
			List<Map<String, Object>> riskList = this.queryForList("state,count(id) counts,sum(shbj) bj,sum(shlx) lx, sum(penal) fx", "t_payment_record", sql_risk.toString());

			// 下月还款计划
			Calendar cal = Calendar.getInstance();
			cal.setTime(shortdate);
			cal.add(Calendar.MONTH, 1);
			StringBuilder sql_payment_project = new StringBuilder();
			sql_payment_project.append(" to_date(to_char(predict_repayment_date,'yyyy-mm'),'yyyy-mm') =  to_date('").append(sdf_shortdate.format(cal.getTime())).append("','yyyy-mm')").append(" and beneficiary_id = ").append(target_user_id).append(" group by predict_repayment_date order by predict_repayment_date asc");
			List<Map<String, Object>> list_payment_project = this.queryForList("to_char(predict_repayment_date,'dd') day ,sum(xybj+xylx) amount", "t_payment_record", sql_payment_project.toString());
			boolean payment_project_charts_success = createChart(repayment_project_chart_path, list_payment_project);

			/*
			 * 前面都是条件准备，下面正式开始
			 */
		

			int sex = 0;
			double zzc = 0, kyye = 0, djje = 0, cyzq = 0, zsy = 0, ljrj = 0, ljcj = 0, zsyl = 0, byhs = 0, zqlx = 0, zqfx = 0, jcje = 0;

			if (memberList.size() != 0) {
				Map<String, Object> memberbase = memberList.get(0);
				sex = Integer.parseInt(memberbase.get("sex").toString());
				zzc = Double.parseDouble(memberbase.get("zzc").toString());
				kyye = Double.parseDouble(memberbase.get("user_account_balance").toString());
				djje = Double.parseDouble(memberbase.get("user_account_frozenamount").toString());
				cyzq = Double.parseDouble(memberbase.get("cyzq").toString());
			}

			if (accountList.size() != 0) {
				Map<String, Object> account = accountList.get(0);
				zsy = Double.parseDouble(account.get("total_all").toString());
				ljrj = Double.parseDouble(account.get("cash_in").toString());
				ljcj = Double.parseDouble(account.get("cash_out").toString());
				zsyl = Double.parseDouble(account.get("rate_all").toString()) * 100;
			}

			if (this_month_repayment_List.size() != 0) {
				Map<String, Object> payment = this_month_repayment_List.get(0);
				byhs = Double.parseDouble(payment.get("all_payment_amount").toString());
				zqlx = Double.parseDouble(payment.get("all_payment_lx").toString());
				zqfx = Double.parseDouble(payment.get("all_payment_fx").toString());
			}

			if (this_month_invest_List.size() != 0) {
				Map<String, Object> invest = this_month_invest_List.get(0);
				jcje = Double.parseDouble(invest.get("all_invest_amount").toString());
			}

			HashMap<String, Object> target = new HashMap<String, Object>();
			target.put("username", target_user_username);
			target.put("realname", target_user_realname);
			target.put("shortdate", sdf_text_shortdate.format(shortdate));
			target.put("download_url", "<a href=\"/back/statistics/stcsMouthLiCaiAction!pdf_for_mouth_lc_report?file=" + htmlfilename + "\" class=\"download\">下载PDF</a>");
			target.put("sex", sex == 0 ? "先生" : "女士");
			target.put("date", sdf_date.format(new Date()));
			target.put("zzc", String.format("%.2f", zzc));
			target.put("kyye", String.format("%.2f", kyye));
			target.put("djje", String.format("%.2f", djje));
			target.put("cyzq", String.format("%.2f", cyzq));
			target.put("zsy", String.format("%.2f", zsy));
			target.put("ljrj", String.format("%.2f", ljrj));
			target.put("ljcj", String.format("%.2f", ljcj));
			target.put("zsyl", String.format("%.2f", zsyl));
			target.put("byhs", String.format("%.2f", byhs));
			target.put("zqlx", String.format("%.2f", zqlx));
			target.put("zqfx", String.format("%.2f", zqfx));
			target.put("jcje", String.format("%.2f", jcje));
			target.put("repayment_project_chart_path", repayment_project_chart_url);

			StringBuilder sqlhtml = new StringBuilder();
			int count_sum = 0;
			double daichangamount = 0;
			double bj_all = 0d;
			double lx_all = 0d;
			double fx_all = 0d;
			for (Map<String, Object> item : riskList) {
				String state = "";
				int counts = Integer.parseInt(item.get("counts").toString());
				count_sum += counts;
				String bj = String.format("%.2f", item.get("bj"));
				String lx = String.format("%.2f", item.get("lx"));
				String fx = String.format("%.2f", item.get("fx"));
				bj_all += Double.parseDouble(bj);
				lx_all += Double.parseDouble(lx);
				fx_all += Double.parseDouble(fx);
				if ("0".equals(item.get("state").toString())) {
					state = "未还款";
				} else if ("1".equals(item.get("state").toString())) {
					state = "正常还款";
				} else if ("2".equals(item.get("state").toString())) {
					state = "提前还款";
				} else if ("3".equals(item.get("state").toString())) {
					state = "逾期还款";
				} else if ("4".equals(item.get("state").toString())) {
					state = "担保代偿";
					daichangamount = Double.parseDouble(bj);
				}
				sqlhtml.append("<tr>").append("<td align=\"center\" class=\"inner-table-td\">").append(state).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(counts).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(bj).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(lx).append("</td>").append(
						"<td align=\"center\" class=\"inner-table-td\">").append(fx).append("</td>").append("<td></td>").append("</tr>");

			}

			String daichanglv = String.format("%.2f", (((float) daichangamount / (float) bj_all) * 100)) + "%";
			if (bj_all == 0) {
				daichanglv = "0%";
			}

			sqlhtml.append("<tr>").append("<td></td>").append("<td align=\"center\" class=\"inner-table-td\">").append(count_sum).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(String.format("%.2f", bj_all)).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(String.format("%.2f", lx_all)).append("</td>").append(
					"<td align=\"center\" class=\"inner-table-td\">").append(String.format("%.2f", fx_all)).append("</td>").append("<td align=\"center\" class=\"inner-table-td\">").append(daichanglv).append("</td>").append("</tr>");
			target.put("inner-table-content", sqlhtml.toString());
			result = replaceHTML(templatestr, target);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;

		// ---------------------------生成结束
	}
	
	
	private String replaceHTML(String htmlcode,HashMap<String, Object> target){
		String result = htmlcode;
		Set<String> keysets = target.keySet();
		for(Iterator<String> keys = keysets.iterator(); keys.hasNext();){
			String key = keys.next();
			String value = target.get(key).toString();
			result = result.replaceAll("\\$\\{"+key+"}", value);
		}
		return result;
	}
	
	
	
	
	public static boolean createChart(String path,List<Map<String, Object>> datalist) {
		try {
			
			ChartColor backgroundcolor = new ChartColor(224,224,224);
			ChartColor titlefontcolor = new ChartColor(34,72,136);
			
			DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
			// 曲线名称
			String series1 = "还款额";
			
			for(Map<String,Object> item : datalist){
				linedataset.addValue(Double.parseDouble(String.format("%.2f", item.get("amount"))), series1, item.get("day").toString());
			}

			JFreeChart chart = ChartFactory.createLineChart("还款计划统计（下图中峰值部，是您下月资金回笼的高峰期）",
					"", // 横坐标名称
					"", // 纵坐标名称
					linedataset, // 数据
					PlotOrientation.VERTICAL, // 水平显示图像
					true, // include legend
					true, // tooltips
					false // urls
					);
			CategoryPlot plot = chart.getCategoryPlot();
			plot.setRangeGridlinesVisible(true); // 是否显示格子线
			plot.setDomainGridlinesVisible(true);
			plot.setBackgroundAlpha(0f); // 设置背景透明度
			plot.setBackgroundPaint(backgroundcolor);
			
			LineAndShapeRenderer renderer = new LineAndShapeRenderer();
	        //renderer.setSeriesPaint(0, Color.BLUE);// 改变折线的颜色
	        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值
	        renderer.setBaseItemLabelsVisible(true); 
	        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
	        renderer.setItemLabelAnchorOffset(2D);// 设置柱形图上的文字偏离值 
	        plot.setRenderer(renderer);// 给折线加点
			
			
			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setLabelFont(new Font("宋体",Font.BOLD,20));
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setAutoRangeIncludesZero(true);
			rangeAxis.setUpperMargin(0.3);//纵坐标上浮跨度
			rangeAxis.setLabelAngle(Math.PI / 2.0);
			
			
			CategoryAxis domainAxis = plot.getDomainAxis();
			domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,14));//横坐标字体
			domainAxis.setUpperMargin(0);
			domainAxis.setLabelFont(new Font("宋体",Font.BOLD,20));
			
			
			
			chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));
			chart.getTitle().setPaint(titlefontcolor);
			chart.getLegend().setVisible(false);//不显示图例
			
			chart.setBackgroundPaint(backgroundcolor);
			
			ChartUtilities.saveChartAsPNG(new File(path),chart,740, 300);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	
	

}
