package com.kmfex.statistics.action;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.statistics.service.StatisticsAuthorityService;
import com.opensymphony.xwork2.ActionSupport;
import com.wisdoor.core.service.OrgService;

/**
 * 授权中心交易量图表统计
 * @author   
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class JfreeChartJylAction extends ActionSupport {
	@Resource OrgService orgService; 
	@Resource StatisticsAuthorityService statisticsAuthorityService; 
	private int year=2013;
	private String queryByOrgCode;
    @Override
    public String execute() throws Exception {
       
        return SUCCESS;
    }	 
	private JFreeChart chart;

	public JFreeChart getChart() { 
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
		//构造数据集合
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
	    try {
			list = this.statisticsAuthorityService.getMapList_Jyl(year, queryByOrgCode);
		} catch (Exception e) { 
			e.printStackTrace();
			dataset.addValue(0, "1月", "1月");  	
			dataset.addValue(0, "2月", "2月");  	
			dataset.addValue(0, "3月", "3月");  	
			dataset.addValue(0, "4月", "4月");  	
			dataset.addValue(0, "5月", "5月");  	
			dataset.addValue(0, "6月", "6月");  	
			dataset.addValue(0, "7月", "7月");  	
			dataset.addValue(0, "8月", "8月");  	
			dataset.addValue(0, "9月", "9月");  	
			dataset.addValue(0, "10月", "10月");  	
			dataset.addValue(0, "11月", "11月");  	
			dataset.addValue(0, "12月", "12月");  	
		} 
		Double min=0d,max=0d;
		if(null!=list&&list.size()>0){
			Double M1=Double.parseDouble(list.get(0).get("M1").toString()); 
			Double M2=Double.parseDouble(list.get(0).get("M2").toString()); 
			Double M3=Double.parseDouble(list.get(0).get("M3").toString()); 
			Double M4=Double.parseDouble(list.get(0).get("M4").toString()); 
			Double M5=Double.parseDouble(list.get(0).get("M5").toString()); 
			Double M6=Double.parseDouble(list.get(0).get("M6").toString()); 
			Double M7=Double.parseDouble(list.get(0).get("M7").toString()); 
			Double M8=Double.parseDouble(list.get(0).get("M8").toString()); 
			Double M9=Double.parseDouble(list.get(0).get("M9").toString()); 
			Double M10=Double.parseDouble(list.get(0).get("M10").toString()); 
			Double M11=Double.parseDouble(list.get(0).get("M11").toString()); 
			Double M12=Double.parseDouble(list.get(0).get("M12").toString()); 
			
			if(max<M1) max=M1;
			if(max<M2) max=M2;
			if(max<M3) max=M3;
			if(max<M4) max=M4;
			if(max<M5) max=M5;
			if(max<M6) max=M6;
			if(max<M7) max=M7;
			if(max<M8) max=M8;
			if(max<M9) max=M9;
			if(max<M10) max=M10;
			if(max<M11) max=M11;
			if(max<M12) max=M12;
			
			if(min>M1) min=M1;
			if(min>M2) min=M2;  
			if(min>M3) min=M3;
			if(min>M4) min=M4;
			if(min>M5) min=M5;
			if(min>M6) min=M6;
			if(min>M7) min=M7;
			if(min>M8) min=M8;
			if(min>M9) min=M9;
			if(min>M10) min=M10;
			if(min>M11) min=M11;
			if(min>M12) min=M12; 
			
			dataset.addValue(M1, "1月", "1月");  	
			dataset.addValue(M2, "2月", "2月");  	
			dataset.addValue(M3, "3月", "3月");  	
			dataset.addValue(M4, "4月", "4月");  	
			dataset.addValue(M5, "5月", "5月");  	
			dataset.addValue(M6, "6月", "6月");  	
			dataset.addValue(M7, "7月", "7月");  	
			dataset.addValue(M8, "8月", "8月");  	
			dataset.addValue(M9, "9月", "9月");  	
			dataset.addValue(M10, "10月", "10月");  	
			dataset.addValue(M11, "11月", "11月");  	
			dataset.addValue(M12, "12月", "12月");  		
		}else{
			dataset.addValue(0, "1月", "1月");  	
			dataset.addValue(0, "2月", "2月");  	
			dataset.addValue(0, "3月", "3月");  	
			dataset.addValue(0, "4月", "4月");  	
			dataset.addValue(0, "5月", "5月");  	
			dataset.addValue(0, "6月", "6月");  	
			dataset.addValue(0, "7月", "7月");  	
			dataset.addValue(0, "8月", "8月");  	
			dataset.addValue(0, "9月", "9月");  	
			dataset.addValue(0, "10月", "10月");  	
			dataset.addValue(0, "11月", "11月");  	
			dataset.addValue(0, "12月", "12月");  	
		} 
        chart = ChartFactory.createBarChart3D(  
                            "授权中心交易量统计("+year+"年)", // 图表标题  
                            "月份", // 目录轴的显示标签  
                            "金额", // 数值轴的显示标签  
                            dataset, // 数据集  
                            PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
                            true,           // 是否显示图例(对于简单的柱状图必须是false)  
                            false,          // 是否生成工具  
                            false           // 是否生成URL链接  
                            );  
          
        //从这里开始  
        CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象  
        CategoryAxis domainAxis=plot.getDomainAxis();         //水平底部列表  
        domainAxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题  
        domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题  
        ValueAxis rangeAxis=plot.getRangeAxis();//获取柱状  
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));  
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
        chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体  
            
        
		

		return chart;

	}

	public void setChart(JFreeChart chart) { 
		this.chart = chart; 

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

	 

}