package com.kmfex.statistics.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.statistics.service.StatisticsAuthorityService;
import com.opensymphony.xwork2.ActionSupport;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;

/**
 * 授权中心活跃度图表统计
 * @author   
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class JfreeChartHyAction extends ActionSupport {
	@Resource 
	OrgService orgService; 
	@Resource 
	StatisticsAuthorityService statisticsAuthorityService; 
	@Resource 
	UserService userService; 
	private int year=2013;
	private String queryByOrgCode;
	private Date startDate;
	private Date endDate; 
	

	private JFreeChart chart;
	
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }	 
	

	public JFreeChart getChart() { 
		//构造数据集合
		PieDataset piedataset = this.createDataset();
		
		chart = ChartFactory.createPieChart3D("会员投资活跃度统计",  //图形标题名称  
                piedataset,   // dataset数据集  
                true,      // legend?  
                true,     // tooltips?  
                false);  //URLs?  
		PiePlot pieplot = (PiePlot)chart.getPlot();  //通过JFreeChart 对象获得 plot：PiePlot！！  
		//设置背景透明度.  
		pieplot.setBackgroundAlpha(0.5f);  
		//图形边框颜色  
		pieplot.setBaseSectionOutlinePaint(Color.BLACK);  
		//图形边框粗细  
		pieplot.setBaseSectionOutlineStroke(new BasicStroke(1.6f));  
		
		pieplot.setBaseSectionPaint(Color.red);  
		//设置图形是否为圆形,true为圆形,false为椭圆形.  
		pieplot.setCircular(true);  
		//设置饼状图的绘制方向，可以按顺时针方向绘制，也可以按逆时针方向绘制  
		pieplot.setDirection(Rotation.ANTICLOCKWISE);  
		//设置绘制角度(图形旋转角度)  
		pieplot.setStartAngle(30);  
		//设置突出显示的数据块  
		pieplot.setExplodePercent("One", 0.1D);  
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		    "{0}={1}({2})", NumberFormat.getNumberInstance(),
		    new DecimalFormat("0.00%")));
		//设置前景透明度  
		pieplot.setForegroundAlpha(1f);  
		//设置标签背景色  
		pieplot.setLabelBackgroundPaint(Color.LIGHT_GRAY);  
		//设置标签的字体  
		chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体  
        PiePlot piePlot= (PiePlot) chart.getPlot();//获取图表区域对象  
        piePlot.setLabelFont(new Font("宋体",Font.BOLD,15));//解决乱码  
        chart.getLegend().setItemFont(new Font("黑体",Font.BOLD,10));  
		pieplot.setNoDataMessage("找不到可用数据...");    // 没有数据的时候显示的内容1  
		pieplot.setCircular(true);

		return chart;

	}
	
	
	/** 
     * 构建饼图数据集 
     * @return 
     */  
    PieDataset createDataset()  
    {  
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
		
    	Double nobuy = 0.0;
    	Double buy1 = 0.0;
    	Double buy2 = 0.0;
    	Double buy3_10 = 0.0;
    	Double buy10_ = 0.0;
//    	System.out.println(startDate);
//    	System.out.println(endDate);
    	try {
  			list = this.statisticsAuthorityService.getMapList_forPie(startDate,endDate,queryByOrgCode);
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
//				System.out.println("totalinvest from jfreeChart:"+totalinvest);
//				System.out.println("list.size() from jfreeChart:"+ list.size());
//				System.out.println("nobuy from jfreeChart:"+nobuy.toString());
  		} catch (Exception e) { 
  			e.printStackTrace();
  		}
        DefaultPieDataset defaultpiedataset = new DefaultPieDataset();   
        defaultpiedataset.setValue("未投标", nobuy);  
        defaultpiedataset.setValue("投标1次", buy1);  
        defaultpiedataset.setValue("投标2次", buy2);  
        defaultpiedataset.setValue("投标3~10次", buy3_10);  
        defaultpiedataset.setValue("投标10次以上", buy10_);  
        return defaultpiedataset;  
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


	 

}