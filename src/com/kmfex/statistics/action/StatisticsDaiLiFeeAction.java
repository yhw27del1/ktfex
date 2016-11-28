package com.kmfex.statistics.action;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.VerticalAlignment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.jfree.ui.HorizontalAlignment;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmfex.service.DaiLiFeePercentService;
import com.kmfex.statistics.service.StatisticsAuthorityService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 信息服务费账单
 * @author SXS
 */
@SuppressWarnings("serial")
@Controller("stcsDaiLiFeeAction")
@Scope("prototype")
public class StatisticsDaiLiFeeAction  extends BaseAction {
	
	private String qkeyWord; 
	private String queryByOrgCode;
	private String year;
	private String month;
	private String jingbanren;
	private String investorname; 
	private String rztype;
	private String rztyper;
	private String tariff;
	@Resource OrgService orgService; 
	@Resource StatisticsAuthorityService statisticsAuthorityService; 
	@Resource 
	UserService userService; 
	@Resource
	DaiLiFeePercentService daiLiFeePercentService;

	/**
	 * 信息服务费账单列表
	 * @return  
	 */
	public String mouthfee_list(){ 
		return "mouthfee_list"; 
	}  
	 
	public String mouthfee_detail(){ 
		return "mouthfee_detail"; 
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

	public void getDaiLiFeeList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			
			Map<String,String> sqlMap = spellDetailSQL();
			
			String field = sqlMap.get("field");
			String tables = sqlMap.get("table");
			String wheresql = sqlMap.get("wheresql");
			
			List<Map<String, Object>> result = this.orgService.queryForList(field, tables,wheresql, getPage(), rows,true);
			List<Map<String, Object>> result2 = this.orgService.queryForList(spellSumField, tables,wheresql);
			//TODO 计算总计
			double totalData[] = {0.0,0.0};
			if(result2.size()>0){	 
				totalData[0] += Double.parseDouble(result2.get(0).get("INVESTAMOUNT")==null?"0":result2.get(0).get("INVESTAMOUNT").toString());
				totalData[1] += Double.parseDouble(result2.get(0).get("DAILIFEE")==null?"0":result2.get(0).get("DAILIFEE").toString());
			}
			DecimalFormat df = new DecimalFormat("#.00");
			totalData[0] =Double.parseDouble(df.format(totalData[0]));
			totalData[1] =Double.parseDouble(df.format(totalData[1]));
			
			//添加信息服务费
			int total = this.orgService.queryForListTotal("vi.id","v_dailifee vi",wheresql);  
		 
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("INVESTAMOUNT", totalData[0]);
			_totalDate.element("DAILIFEE", totalData[1]);
			_totalDate.element("CYR", "总计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private List<Map<String,Object>> resultList;
	private List<Map<String,Object>> resultList2;
	
	public String export_ex(){
		try {
			
			Map<String,String> sqlMap = spellDetailGroupSQL();
			
			String field = sqlMap.get("field");
			String tables = sqlMap.get("table");
			String wheresql = sqlMap.get("wheresql");

			this.resultList = this.orgService.queryForList(field, tables,wheresql,true);
			
			sqlMap = spellDetailSQL();
			
			 field = sqlMap.get("field");
			 tables = sqlMap.get("table");
			 wheresql = sqlMap.get("wheresql");
			this.resultList2 = this.orgService.queryForList(field, tables,wheresql,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dailifee_ex";
	}
	

	/**
	 * 导出PDF--代理费明细
	 */
	public void export_pdf(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			User source_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(source_user == null){
				response.sendRedirect("/login.jsp");
				return;
			};
			
			String referer =  request.getHeader("referer");
			
			boolean outsidelink = false;
			if(referer == null || referer.indexOf("!")==-1){
				outsidelink = true;
			}else{
				
				referer = referer.substring(referer.indexOf("!"));
				if(referer.indexOf("?")!=-1){
					referer = referer.substring(0, referer.indexOf("?"));
				}
				if(!"!mouthfee_list".equals(referer)){
					outsidelink = true;
				}
			}
			if(outsidelink){
				response.getWriter().print("外链，系统已记录你的IP与交易帐户，请正常访问系统");
				System.out.println("username:"+source_user.getUsername()+",time"+new Date()+",description:外链下载信息服务费核算报表PDF");
				return;
			}				
			
			Map<String,String> sqlMap = spellDetailSQL();
			
			String field = sqlMap.get("field");
			String tables = sqlMap.get("table");
			String wheresql = sqlMap.get("wheresql");
			
			List<Map<String, Object>> result = this.orgService.queryForList(field, tables,wheresql);
			List<Map<String, Object>> result2 = this.orgService.queryForList(spellSumField, tables,wheresql,true);
			//计算总计
			double totalData[] = {0.0,0.0};
			if(result2.size()>0){	 
				totalData[0] += Double.parseDouble(result2.get(0).get("INVESTAMOUNT")==null?"0":result2.get(0).get("INVESTAMOUNT").toString());
				totalData[1] += Double.parseDouble(result2.get(0).get("DAILIFEE")==null?"0":result2.get(0).get("DAILIFEE").toString());
			}
			DecimalFormat df = new DecimalFormat("#.00");
			totalData[0] =Double.parseDouble(df.format(totalData[0]));
			totalData[1] =Double.parseDouble(df.format(totalData[1]));
			
			String headimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/images/report_head.bmp");
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			Document document = new Document(PageSize.A4,20,20,20,20);

			PdfWriter.getInstance(document, baos);
//			XMLWorkerHelper xmlhelper = XMLWorkerHelper.getInstance(); 
			
			String endDate = null;
			
			
			if(StringUtils.isBlank(this.year) || StringUtils.isBlank(this.month)){
				endDate = "未选择";
			}else{
				endDate = this.year + "-" + this.month + "月";
			}
			
			
			Image img_head = Image.getInstance(headimgPath);    
			img_head.setAlignment(Image.LEFT | Image.TOP);  
			//img_head.setAbsolutePosition(0, 740);  //位置
			img_head.scaleToFit(1000, 67);// 大小
			
			
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
			Font titlefont = new Font(bfChinese,18,Font.BOLD,BaseColor.BLACK);
			Font tableBodyFont = new Font(bfChinese,12,Font.NORMAL,new BaseColor(77,77,77));
			Font descriptionfont = new Font(bfChinese,12,Font.BOLD,BaseColor.BLACK);
			Font tableHeaderFont = new Font(bfChinese,12,Font.NORMAL,BaseColor.BLACK);
			Font paginationFont = new Font(bfChinese,12,Font.NORMAL,BaseColor.BLACK);
			
			
			Paragraph title = new Paragraph("昆投互联网金融交易",titlefont);
			title.setAlignment(Element.ALIGN_CENTER);
			
			Paragraph description = new Paragraph("项目：信息服务费明细表                                结算月份:"+endDate,descriptionfont);
			description.setAlignment(Element.ALIGN_CENTER);
			
			
			
			document.open();
			
			document.add(img_head);
			document.add(title);
			document.add(description);
			document.add(new Paragraph("                  ",tableBodyFont));
			
			PdfPTable table = null;
			
			int firstpagesize = 25;
			int otherpagesize = 30;
			
			int pagecount = 1;
			int listsize = result.size();
			
			if( listsize > firstpagesize){
				listsize -= firstpagesize;
				int otherpagecount = listsize / otherpagesize;
				pagecount += otherpagecount + 1;//修改加了1
			}
			
			
			int page = 1;
			int pagesize = 0;
			int count = 1;//页内计数
			
			
			PdfPCell headercell = new PdfPCell();
			headercell.setBorder(0);
			headercell.setPadding(5);
			
			PdfPCell contentcell = new PdfPCell();
			contentcell.setBorder(0);
			contentcell.setPadding(5);
			
			int [] widths = new int[]{70,80,40,50,60,100};
			
			double dailifee_sum = 0d;
			double investamount_sum = 0d;

			 df = new DecimalFormat("#.##");
			int totalcount = 0;
			for(Iterator<Map<String,Object>> iter = result.iterator(); iter.hasNext();){
				Map<String,Object> item = iter.next();
				if(count == 1 ){
					if( page == 1 ){
						pagesize = firstpagesize;
					}else{
						pagesize = otherpagesize;
					}
					table = new PdfPTable(6);
					
					table.setWidthPercentage(90);
					table.setWidths(widths);
					
					//每页添加一个表头
					Phrase header_1 = new Phrase("投标方户名",tableHeaderFont);
					Phrase header_2 = new Phrase("介绍人",tableHeaderFont);
					Phrase header_3 = new Phrase("期限",tableHeaderFont);
					Phrase header_4 = new Phrase("信息服务费",tableHeaderFont);
					Phrase header_5 = new Phrase("交易额",tableHeaderFont);
					Phrase header_6 = new Phrase("签约日期",tableHeaderFont);
					
					headercell.setBackgroundColor(new BaseColor(240,240,240));
					headercell.setPhrase(header_1);
					table.addCell(headercell);
					headercell.setPhrase(header_2);
					table.addCell(headercell);
					headercell.setPhrase(header_3);
					table.addCell(headercell);
					headercell.setPhrase(header_4);
					table.addCell(headercell);
					headercell.setPhrase(header_5);
					table.addCell(headercell);
					headercell.setPhrase(header_6);
					table.addCell(headercell);
				}
				
				
				if( count % 2 == 1){
					contentcell.setBackgroundColor(new BaseColor(240,240,240));
				}else{
					contentcell.setBackgroundColor(BaseColor.WHITE);
				}
				
				Phrase cyr = new Phrase(item.get("cyr")==null?"":item.get("cyr").toString(),tableBodyFont);
				Phrase jingbanrenr = new Phrase(item.get("jingbanren")==null?"":item.get("jingbanren").toString().replaceAll("（已离职）", ""),tableBodyFont);
				Phrase businesstype = new Phrase( item.get("interestday").toString().equals("0")?
						item.get("businesstype").toString()+"个月":item.get("interestday").toString()+"天",tableBodyFont);
				
				String dailifee_str = item.get("dailifee") == null ? "" : df.format(Double.parseDouble(item.get("dailifee").toString())) ;
				String investamount_str = item.get("investamount")==null?"":item.get("investamount").toString();
				dailifee_sum += StringUtils.isBlank(dailifee_str)?0d:Double.parseDouble(dailifee_str);
				investamount_sum += StringUtils.isBlank(investamount_str)?0d:Double.parseDouble(investamount_str);
				Phrase dailifee = new Phrase(dailifee_str,tableBodyFont);
				Phrase investamount = new Phrase(investamount_str,tableBodyFont);
				Phrase qianyuedate = new Phrase(item.get("qianyuedate")==null?"":sdf.format(item.get("qianyuedate")),tableBodyFont);
				
				contentcell.setPhrase(cyr);
				table.addCell(contentcell);
				contentcell.setPhrase(jingbanrenr);
				table.addCell(contentcell);
				contentcell.setPhrase(businesstype);
				table.addCell(contentcell);
				contentcell.setPhrase(dailifee);
				table.addCell(contentcell);
				contentcell.setPhrase(investamount);
				table.addCell(contentcell);
				contentcell.setPhrase(qianyuedate);
				table.addCell(contentcell);
				
				if(count == pagesize || !iter.hasNext()){
					if(!iter.hasNext()){
						if( count % 2 == 0){
							contentcell.setBackgroundColor(new BaseColor(240,240,240));
						}else{
							contentcell.setBackgroundColor(BaseColor.WHITE);
						}
						Phrase sum_str = new Phrase("合计",tableBodyFont);
						Phrase dailifee_sum_phs = new Phrase(String.format("%.2f", totalData[1]==0?dailifee_sum:totalData[1]),tableBodyFont);
						Phrase investamount_sum_phs = new Phrase(String.format("%.2f", investamount_sum),tableBodyFont);
						Phrase space_phs = new Paragraph();
						contentcell.setPhrase(sum_str);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						table.addCell(contentcell);
						contentcell.setPhrase(dailifee_sum_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(investamount_sum_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						
						//
						contentcell.setBackgroundColor(BaseColor.WHITE);
						contentcell.setColspan(3);
						
						Phrase kong_str = new Phrase("  ",tableBodyFont);
						contentcell.setPhrase(kong_str);
						
						//留一行间距
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						Phrase zhibiao_str = new Phrase("服务中心：",tableBodyFont);
						contentcell.setPhrase(zhibiao_str);
						table.addCell(contentcell);
						
						//留一行间距
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						Phrase fuhe_str = new Phrase("签章：",tableBodyFont);
						contentcell.setPhrase(fuhe_str);
						table.addCell(contentcell);
						
						//留一行间距
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						Phrase shengchengriqi_str = new Phrase("导出数据日期："+sdf.format(new Date()),tableBodyFont);
						contentcell.setPhrase(shengchengriqi_str);
						table.addCell(contentcell);
						
					}
					Paragraph pagination = new Paragraph("共"+pagecount+"页，第"+page+"页",paginationFont);
					pagination.setAlignment(Element.ALIGN_RIGHT);
					pagination.setSpacingBefore(10);
					document.add(table);
					document.add(pagination);
					document.newPage();
					page++;
					count = 1;
				}else{
					count++;
				}
				
				totalcount += 1 ;
			}
			
			//System.out.println("pdf输出总计数量："+totalcount);
			
			document.close();
						
			String pdffilename = this.queryByOrgCode+(this.year==null?"":this.year)+(this.month==null?"":this.month)+"details";
			response.setContentType("text/xml;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="+pdffilename+".pdf");
			response.setContentType("application/pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
			response.setHeader("Pragma", "public");
			response.addHeader("Content-Length", Integer.toString(baos.size()));
						
			ServletOutputStream sos = response.getOutputStream();
						
			baos.writeTo(sos);
			sos.flush();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * pdf导出汇总表
	 */
	public void export_pdf_sum(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			
			User source_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(source_user == null){
				response.sendRedirect("/login.jsp");
				return;
			};
			
			String referer =  request.getHeader("referer");
			
			boolean outsidelink = false;
			if(referer == null || referer.indexOf("!")==-1){
				outsidelink = true;
			}else{
				referer = referer.substring(referer.indexOf("!"));
				if(referer.indexOf("?")!=-1){
					referer = referer.substring(0, referer.indexOf("?"));
				}
				if(!"!mouthfee_list".equals(referer)){
					outsidelink = true;
				}
			}
			if(outsidelink){
				response.getWriter().print("外链，系统已记录你的IP与交易帐户，请正常访问系统");
				System.out.println("username:"+source_user.getUsername()+",time"+new Date()+",description:外链下载信息服务费核算报表PDF");
				return;
			}				
			
			Map<String,String> sqlMap = spellDetailGroupSQLPdf();
			
			String field = sqlMap.get("field");
			String tables = sqlMap.get("table");
			String wheresql = sqlMap.get("wheresql");
			
			Map<String,String> sqlMap2 = spellDetailSQL();
			String wheresql2 = sqlMap2.get("wheresql");
			
			List<Map<String, Object>> result = this.orgService.queryForList(field, tables,wheresql);
			List<Map<String, Object>> result2 = this.orgService.queryForList(spellSumField, tables,wheresql2);
			//TODO 计算总计
			
			
			double totalData[] = {0.0,0.0};
			if(result2.size()>0){	 
				totalData[0] += Double.parseDouble(result2.get(0).get("INVESTAMOUNT")==null?"0":result2.get(0).get("INVESTAMOUNT").toString());
				totalData[1] += Double.parseDouble(result2.get(0).get("DAILIFEE")==null?"0":result2.get(0).get("DAILIFEE").toString());
			}
			DecimalFormat df = new DecimalFormat("0.00");
			DecimalFormat df_s = new DecimalFormat("#,###,###,##0.00");
			totalData[0] =Double.parseDouble(df.format(totalData[0]));
			totalData[1] =Double.parseDouble(df.format(totalData[1]));
			
			String headimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/images/report_head.bmp");
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			Document document = new Document(PageSize.A4,20,20,20,20);

			PdfWriter.getInstance(document, baos);
//			XMLWorkerHelper xmlhelper = XMLWorkerHelper.getInstance(); 
			
			String endDate = null;
			
			String orgName = null;
			
			if(StringUtils.isBlank(this.year) || StringUtils.isBlank(this.month)){
				endDate = "未选择";
			}else{
				endDate = this.year + "-" + this.month + "月";
			}
			
			if(StringUtils.isBlank(queryByOrgCode)){
				orgName = "未知";
			}else{
				Org _org = this.orgService.findOrg(queryByOrgCode);
				if(_org == null || StringUtils.isBlank(_org.getName())){
					orgName = "未知";
				}else{
					orgName = _org.getName();
				}
			}
			
			Image img_head = Image.getInstance(headimgPath);    
			img_head.setAlignment(Image.LEFT | Image.TOP);  
			img_head.scaleToFit(1000, 67);// 大小
			
			
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
			Font titlefont = new Font(bfChinese,17,Font.BOLD,BaseColor.BLACK);
			Font tableBodyFont = new Font(bfChinese,11,Font.NORMAL,new BaseColor(77,77,77));
			Font tableNoteFont = new Font(bfChinese,8,Font.NORMAL,new BaseColor(77,77,77));
			Font descriptionfont = new Font(bfChinese,11,Font.BOLD,BaseColor.BLACK);
			Font tableHeaderFont = new Font(bfChinese,11,Font.NORMAL,BaseColor.BLACK);
			Font paginationFont = new Font(bfChinese,8,Font.NORMAL,BaseColor.BLACK);
			
			Paragraph title = new Paragraph("昆投互联网金融交易",titlefont);
			title.setAlignment(Element.ALIGN_CENTER);
			
			Paragraph description = new Paragraph("项目：信息服务费                      会计期间:"+endDate,descriptionfont);
			description.setAlignment(Element.ALIGN_CENTER);
			
			document.open();
			
			document.add(img_head);
			document.add(title);
			document.add(description);
			document.add(new Paragraph("                  ",tableNoteFont));
			
			PdfPTable table = null;
			
			int firstpagesize = 28;
			int otherpagesize = 30;
			
			int pagecount = 1;
			int listsize = result.size();
			
			if( listsize > firstpagesize){
				listsize -= firstpagesize;
				int otherpagecount = listsize / otherpagesize;
				pagecount += otherpagecount + 1;//修改加了1
			}
			
			
			int page = 1;
			int pagesize = 0;
			int count = 1;//页内计数
			
			
			PdfPCell headercell = new PdfPCell();
			headercell.setBorder(Rectangle.BOX);
			headercell.setBorderColor(BaseColor.BLACK);
			headercell.setPadding(4);
			
			PdfPCell contentcell = new PdfPCell();
			contentcell.setBorder(Rectangle.BOX);
			contentcell.setPadding(3);
			
			int [] widths = new int[]{70,60,90,60,60,65};
			
			double dailifee_sum = 0d;
			double investamount_sum = 0d;
			
			int totalcount = 0;
			//
			 df = new DecimalFormat("0.00");
			String[] types = new String[]{"1天","2天","3天","4天","5天","6天","7天","8天","9天","14天","15天","21天","28天","45天","1","2","3","4","5","6","8","9","12"};
			//double[] investamounts = new 	double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			//double[] dailifees = new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			String[] investamounts = new String[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
			String[] dailifees = new String[]{"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
			for(Iterator<Map<String,Object>> iter = result.iterator(); iter.hasNext();){
				Map<String,Object> item = iter.next();
				String type = item.get("interestday").toString().equals("0")?
						item.get("businesstype").toString():item.get("interestday").toString()+"天";
						for (int i = 0; i <types.length ; i++ ) {
							if(types[i].equals(type)){
								//dailifees[i]= item.get("dailifee") == null ? 0d : Double.parseDouble(df.format(Double.parseDouble(item.get("dailifee").toString()))) ;
								//investamounts[i]= item.get("totalamount") == null ? 0d : Double.parseDouble(df.format(Double.parseDouble(item.get("totalamount").toString())));
								dailifees[i]= item.get("dailifee") == null ? "0" : df.format(Double.parseDouble(item.get("dailifee").toString())) ;
								investamounts[i]= item.get("totalamount") == null ? "0" : df.format(Double.parseDouble(item.get("totalamount").toString()));
							}
						}
			}
			for(int i = 0; i <types.length ; i++ ){
				if(count == 1 ){
					if( page == 1 ){
						pagesize = firstpagesize;
					}else{
						pagesize = otherpagesize;
					}
					table = new PdfPTable(6);
					
					table.setWidthPercentage(90);
					table.setWidths(widths);
					
					//每页添加一个表头
					Phrase header_1 = new Phrase("名称",tableHeaderFont);
					Phrase header_2 = new Phrase("机构编码",tableHeaderFont);
					Phrase header_3 = new Phrase("投资金额（元）",tableHeaderFont);
					Phrase header_4 = new Phrase("期限（月）",tableHeaderFont);
					Phrase header_5 = new Phrase("计算标准",tableHeaderFont);
					Phrase header_6 = new Phrase("信息服务费",tableHeaderFont);
					
					headercell.setBackgroundColor(new BaseColor(240,240,240));
					headercell.setPhrase(header_1);
					table.addCell(headercell);
					headercell.setPhrase(header_2);
					table.addCell(headercell);
					headercell.setPhrase(header_3);
					table.addCell(headercell);
					headercell.setPhrase(header_4);
					table.addCell(headercell);
					headercell.setPhrase(header_5);
					table.addCell(headercell);
					headercell.setPhrase(header_6);
					table.addCell(headercell);
				}
				
				
				
				Phrase orgname = new Phrase(orgName==null?"":orgName,tableBodyFont);
				Phrase orgcode = new Phrase(queryByOrgCode==null?"":queryByOrgCode,tableBodyFont);
                
				String investamount_str = investamounts[i];
				investamount_sum += StringUtils.isBlank(investamount_str)?0d:Double.parseDouble(investamount_str);
				Phrase investamount = new Phrase(df_s.format(Double.parseDouble(investamounts[i])),tableBodyFont);
				
				Phrase businesstype = new Phrase(types[i],tableBodyFont);
				
				Phrase jisuanbiaozhun = new Phrase(i<10?"按天计算":"按月计算",tableBodyFont);
				
				String dailifee_str =dailifees[i] ;
				dailifee_sum += StringUtils.isBlank(dailifee_str)?0d:Double.parseDouble(dailifee_str);
				Phrase dailifee = new Phrase(df_s.format(Double.parseDouble(dailifees[i])),tableBodyFont);
				
				//添加代理费机构和机构编码的列合并  19
				if(i == 0) {
					contentcell.setRowspan(23);
					contentcell.setPhrase(orgname);
					table.addCell(contentcell);
					contentcell.setPhrase(orgcode);
					table.addCell(contentcell);
				}
				contentcell.setRowspan(1);
				contentcell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				contentcell.setPhrase(investamount);
				table.addCell(contentcell);
				contentcell.setPhrase(businesstype);
				table.addCell(contentcell);
				//计算标准：按天、按月   按天14  按月9
				if(i == 0) {
					contentcell.setRowspan(14);
					contentcell.setPhrase(jisuanbiaozhun);
					table.addCell(contentcell);
				}else if(i == 14){
					contentcell.setRowspan(9);
					contentcell.setPhrase(jisuanbiaozhun);
					table.addCell(contentcell);
				}
				contentcell.setRowspan(1);
				contentcell.setPhrase(dailifee);
				table.addCell(contentcell);
				
				if(count == pagesize || i ==types.length-1){
					if(i ==types.length-1){//到最后一条记录
						
						Phrase sum_str = new Phrase("合计",tableBodyFont);
						Phrase dailifee_sum_phs = new Phrase(df.format(totalData[1]==0? dailifee_sum:totalData[1]),tableBodyFont);
						Phrase investamount_sum_phs = new Phrase(df_s.format(investamount_sum),tableBodyFont);
						Phrase space_phs = new Paragraph();
						contentcell.setPhrase(sum_str);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(investamount_sum_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						table.addCell(contentcell);
						contentcell.setPhrase(dailifee_sum_phs);
						table.addCell(contentcell);
						
						//
						contentcell.setBackgroundColor(BaseColor.WHITE);
						contentcell.setColspan(2);
						
						
						
						contentcell.setColspan(6);
						contentcell.setBorder(Rectangle.NO_BORDER);
						contentcell.setPhrase(new Phrase("             ",tableBodyFont));
						table.addCell(contentcell);
						
						contentcell.setBorder(Rectangle.BOX);
						contentcell.setColspan(3);
						Phrase antian_str = new Phrase("按天计算服务费",tableBodyFont);
						contentcell.setPhrase(antian_str);
						table.addCell(contentcell);
						Phrase anyue_str = new Phrase("按月计算服务费",tableBodyFont);
						contentcell.setPhrase(anyue_str);
						table.addCell(contentcell);
						
						contentcell.setColspan(1);
						
						PdfPCell contentcell2 = new PdfPCell();
						contentcell2.setBorder(Rectangle.BOX);
						contentcell2.setPadding(5);
						contentcell2.setColspan(2);
						
						PdfPCell contentcell3 = new PdfPCell();
						contentcell3.setBorder(Rectangle.BOX);
						contentcell3.setPadding(5);
						contentcell3.setColspan(2);
						
						PdfPCell contentcell_5 = new PdfPCell();
						contentcell_5.setBorder(Rectangle.BOX);
						contentcell_5.setPadding(5);
						contentcell_5.setRowspan(5);
						contentcell_5.setColspan(3);
						
						Phrase type_str = new Phrase("期限",tableNoteFont);
						contentcell.setPhrase(type_str);
						table.addCell(contentcell);
						Phrase biaozhun_str = new Phrase("服务费结算标准",tableNoteFont);
						contentcell2.setPhrase(biaozhun_str);
						table.addCell(contentcell2);
						Phrase anyuebiaozhun_str = new Phrase("每月    0.3%",tableNoteFont);
						contentcell_5.setPhrase(anyuebiaozhun_str);
						table.addCell(contentcell_5);
						
						Phrase type_str7 = new Phrase("7天",tableNoteFont);
						contentcell.setPhrase(type_str7);
						table.addCell(contentcell);
						Phrase gs_str7 = new Phrase("投资金额*0.3%/30*7*协议约定分配比例",tableNoteFont);
						contentcell2.setPhrase(gs_str7);
						table.addCell(contentcell2);
						
						 type_str7 = new Phrase("14天",tableNoteFont);
						contentcell.setPhrase(type_str7);
						table.addCell(contentcell);
						 gs_str7 = new Phrase("投资金额*0.3%/30*14*协议约定分配比例",tableNoteFont);
						contentcell2.setPhrase(gs_str7);
						table.addCell(contentcell2);
						
						 type_str7 = new Phrase("15天",tableNoteFont);
							contentcell.setPhrase(type_str7);
							table.addCell(contentcell);
							 gs_str7 = new Phrase("投资金额*0.3%/30*15*协议约定分配比例",tableNoteFont);
							contentcell2.setPhrase(gs_str7);
							table.addCell(contentcell2);
							
						 type_str7 = new Phrase("21天",tableNoteFont);
						contentcell.setPhrase(type_str7);
						table.addCell(contentcell);
						 gs_str7 = new Phrase("投资金额*0.3%/30*21*协议约定分配比例",tableNoteFont);
						contentcell2.setPhrase(gs_str7);
						table.addCell(contentcell2);
						
						 type_str7 = new Phrase("28天",tableNoteFont);
							contentcell.setPhrase(type_str7);
							table.addCell(contentcell);
							 gs_str7 = new Phrase("投资金额*0.3%/30*28*协议约定分配比例",tableNoteFont);
							contentcell2.setPhrase(gs_str7);
							table.addCell(contentcell2);
							
						contentcell_5.setRowspan(2);
						Phrase anyuegongshi_str = new Phrase("计算公式=投资金额*借款期限*服务费标准*协议约定分配比例",tableNoteFont);
						contentcell_5.setPhrase(anyuegongshi_str);
						table.addCell(contentcell_5);
							
						 type_str7 = new Phrase("45天",tableNoteFont);
						contentcell.setPhrase(type_str7);
						table.addCell(contentcell);
						 gs_str7 = new Phrase("投资金额*0.3%/30*45*协议约定分配比例",tableNoteFont);
						contentcell2.setPhrase(gs_str7);
						table.addCell(contentcell2);
						
						contentcell.setBorder(Rectangle.NO_BORDER);
						contentcell.setColspan(3);
						Phrase note_str = new Phrase("备注：服务费计算均按最新签订的授权协议标准执行",tableNoteFont);
						contentcell.setPhrase(note_str);
						table.addCell(contentcell);
						
						Phrase kong_str = new Phrase("                 ",paginationFont);
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						//添加一个换行
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						Phrase zhibiao_str = new Phrase("昆投互联网金融交易",tableNoteFont);
						contentcell.setPhrase(zhibiao_str);
						table.addCell(contentcell);
						
						Phrase fuhe_str = new Phrase("服务中心：",tableNoteFont);
						contentcell.setPhrase(fuhe_str);
						table.addCell(contentcell);
						
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						contentcell.setPhrase(kong_str);
						table.addCell(contentcell);
						
						
						Phrase shengchengriqi_str = new Phrase("签章：",tableNoteFont);
						contentcell.setPhrase(shengchengriqi_str);
						table.addCell(contentcell);
						//table.addCell(contentcell);
						

					}
					Paragraph pagination = new Paragraph("共"+pagecount+"页，第"+page+"页",paginationFont);
					pagination.setAlignment(Element.ALIGN_RIGHT);
					pagination.setSpacingBefore(10);
					document.add(table);
					
					document.add(pagination);
					document.newPage();
					page++;
					count = 1;
				}else{
					count++;
				}
				totalcount += 1 ;
			}
			
			//System.out.println("pdf输出总计数量："+totalcount);
			
			document.close();
						
			String pdffilename = this.queryByOrgCode+(this.year==null?"":this.year)+(this.month==null?"":this.month)+"sum";
//			System.out.println(pdffilename);
			response.setContentType("text/xml;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="+pdffilename+".pdf");
			response.setContentType("application/pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
			response.setHeader("Pragma", "public");
			response.addHeader("Content-Length", Integer.toString(baos.size()));
						
			ServletOutputStream sos = response.getOutputStream();
						
			baos.writeTo(sos);
			sos.flush();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据机构开户时间动态选择机构可查询的年月
	 */
	public void year_json_for_mouth_lc_report(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String showCoding = request.getParameter("orgcode");
			if(StringUtils.isBlank(showCoding)) return;
			
			Org org = this.orgService.findOrg(showCoding);
			
			if(org == null) return;
			
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			Calendar cal = Calendar.getInstance();
			int thisyear = cal.get(Calendar.YEAR);
			cal.setTime(org.getCreateDateBy());
			//int startyear = cal.get(Calendar.YEAR) <= 2014 ? 2014: cal.get(Calendar.YEAR);
			int startyear = cal.get(Calendar.YEAR);
			if(startyear < 2013){
				startyear = 2013;
			}
			
			JSONArray array = new JSONArray();
			
			for(;startyear <= thisyear;startyear++){
				JSONObject temp = new JSONObject();
				temp.element("value", startyear);
				temp.element("text", startyear+"年");
				if(startyear == thisyear) temp.element("selected", true);
				array.add(temp);
			}
			
			out.print(array);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void month_json_for_mouth_lc_report(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String showCoding = request.getParameter("orgcode");
			
			String year_str = request.getParameter("year");
			if(StringUtils.isBlank(showCoding)) return;
			
			Org org = this.orgService.findOrg(showCoding);
			
			if(org == null) return;
			
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			Calendar cal = Calendar.getInstance();
			Calendar curcal = Calendar.getInstance();
			int year = 2012 ;
			int thismonth = curcal.get(Calendar.MONTH)+1;//当前月
			int startmonth = 1;
			if(StringUtils.isBlank(year_str)){
				return ;
				//year = cal.get(Calendar.YEAR);
			}else{
				year = Integer.parseInt(year_str);
			}
			
			cal.setTime(org.getCreateDateBy());
			curcal.setTime(new Date());
			
			//year为查询时间，可选为今年及之前
			//如果查询开户那一年，设置开始查询月为开户月
			if(year == cal.get(Calendar.YEAR)){
				startmonth = cal.get(Calendar.MONTH)+1;
				 if(year != curcal.get(Calendar.YEAR)){
					//如果查询的开户年，不是当前年，设置其查询结尾为12月而不是当前月
					 thismonth = 12;
				 }//如果是当前年，月结尾为当前月，即默认值
			}else if(year == curcal.get(Calendar.YEAR)){
				startmonth = 1;
				//月结尾为当前月，即默认值
				//thismonth = curcal.get(Calendar.MONTH)+1;//当前月
			}else{
				startmonth = 1;
				thismonth = 12;
			}
			
			if(year == 2013){
				if(startmonth < 11){
					startmonth = 11;
				}
			}
			
			
			JSONArray array = new JSONArray();
			
			for(;startmonth <= thismonth; startmonth++){
				JSONObject temp = new JSONObject();
				temp.element("value", startmonth);
				temp.element("text", startmonth+"月");
				if(startmonth == thismonth) temp.element("selected", true);
				array.add(temp);
			}
			
			
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  final String spellSumField = "sum(vi.investamount) as INVESTAMOUNT,sum(vi.dailifee) as DAILIFEE";
	/**
	 * 代理费列表sql
	 * @return
	 */
	public Map<String,String> spellDetailSQL(){
		if(null == this.year || "".equals(this.year)){
			 this.year = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(0,4); 
		}
		if(null == this.month || "".equals(this.month)){
			this.month = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(4,6); 
		}
		String nextYear;
		String nextMonth;
		if(Integer.parseInt(this.month)==12){
			nextYear = (Integer.parseInt(this.year)+1)+"";
			nextMonth = "01";
		}else{
			nextYear = this.year;
			nextMonth = Integer.parseInt(this.month)+1>=10?
				(Integer.parseInt(this.month)+1+""):
					"0"+(Integer.parseInt(this.month)+1);
		}
		
		if(Integer.parseInt(this.month)<10){
			this.month = "0"+(Integer.parseInt(this.month));
		}
		//TODO 代理费sql  列表查询
		Map<String, String> sqlMap = new HashMap<String, String>();
		String field = "vi.*,to_char(vi.qianyuedate,'yyyy-MM')  as date2,Round(MONTHS_BETWEEN(to_date('"
			+nextYear+nextMonth+"', 'yyyymm'),vi.qianyuedate) + 0.5) mon," +
					"add_months(vi.qianyuedate, vi.businesstype - 1) lastDlf";
		String table = "v_dailifee vi";
		StringBuilder sb = new StringBuilder(" 1=1   ");
		
		if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
			sb.append(" and vi.orgcode = '" + queryByOrgCode + "'");   
		} 
		
		if(null != jingbanren&&!"".equals(jingbanren)){
			sb.append(" and vi.jingbanren like '%" + jingbanren + "%'");   
		} 
		
		if(null != tariff&&!"".equals(tariff)){
			sb.append(" and vi.rzfwf_tariff = " + tariff + " ");   
		} 
		
		if(null != investorname&&!"".equals(investorname)){
			sb.append(" and vi.investorname like '%" + investorname + "%'");   
		} 
		
		if(null != rztyper&&!"".equals(rztyper)){
			sb.append(" and vi.interestday = '" + rztyper + "'");   
		}else if(null != rztype&&!"".equals(rztype)){
			sb.append(" and vi.businesstype = '" + rztype + "'");   
		}
		
		//查询日期区间
		if( null != this.year && null!= this.month ){
			sb.append(" and ((vi.qianyuedate between to_date('"+this.year+this.month+"01"+"', 'yyyyMMdd') and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd')  and vi.rzfwf_tariff = 0) or " +
					"(vi.rzfwf_tariff = 1 and vi.financbasestate = '7' " +
					"and vi.qianyuedate > to_date('20140201','yyyymmdd') " +
					"and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd') between vi.qianyuedate and add_months(vi.qianyuedate,vi.businesstype))) ");
		}  
		
		sqlMap.put("field", field);
		sqlMap.put("table", table);
		sqlMap.put("wheresql", sb.toString());
		
		return sqlMap;
	}
	
	/**
	 * 导出到excel：按机构、介绍人、投资类型（按日、按月）
	 * @return
	 */
	public Map<String,String> spellDetailGroupSQL(){
		Map<String, String> sqlMap = new HashMap<String, String>();
		String field = "vi.orgcode,vi.jingbanren,sum(vi.investamount) as totalamount,vi.businesstype,vi.interestday," +
		"sum(vi.dailifee) " +
				"as DAILIFEE";
		String table = "v_dailifee vi";
		StringBuilder sb = new StringBuilder(" 1=1   ");
		 
		if(null == this.year || "".equals(this.year)){
			 this.year = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(0,4); 
		}
		if(null == this.month || "".equals(this.month)){
			this.month = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(4,6); 
		}
		String nextYear;
		String nextMonth;
		if(Integer.parseInt(this.month)==12){
			nextYear = (Integer.parseInt(this.year)+1)+"";
			nextMonth = "01";
		}else{
			nextYear = this.year;
			nextMonth = Integer.parseInt(this.month)+1>=10?
				(Integer.parseInt(this.month)+1+""):
					"0"+(Integer.parseInt(this.month)+1);
		}
		
		if(Integer.parseInt(this.month)<10){
			this.month = "0"+(Integer.parseInt(this.month));
		}
		
		if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
			sb.append(" and vi.orgcode = '" + queryByOrgCode + "'");   
		} 
		
		if(null != jingbanren&&!"".equals(jingbanren)){
			sb.append(" and vi.jingbanren like '%" + jingbanren + "%'");   
		} 
		
		if(null != investorname&&!"".equals(investorname)){
			sb.append(" and vi.investorname like '%" + investorname + "%'");   
		} 
		
		if(null != tariff&&!"".equals(tariff)){
			sb.append(" and vi.rzfwf_tariff = " + tariff + " ");   
		}
		
		if(null != rztyper&&!"".equals(rztyper)){
			sb.append(" and vi.interestday = '" + rztyper + "'");   
		}else if(null != rztype&&!"".equals(rztype)){
			sb.append(" and vi.businesstype = '" + rztype + "'");   
		}
		
		//开户日期区间
		if( null != this.year && null!= this.month ){
			sb.append(" and ((vi.qianyuedate between to_date('"+this.year+this.month+"01"+"', 'yyyyMMdd') and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd')  and vi.rzfwf_tariff = 0) or " +
					"(vi.rzfwf_tariff = 1 and vi.financbasestate = '7' " +
					"and vi.qianyuedate > to_date('20140201','yyyymmdd') " +
					" and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd') between vi.qianyuedate and add_months(vi.qianyuedate,vi.businesstype))) ");
		}  
		
		sb.append(" group by vi.orgcode,vi.jingbanren,vi.businesstype,vi.interestday");
		sb.append(" order by vi.orgcode,vi.jingbanren,vi.businesstype desc,vi.interestday desc");
		
		sqlMap.put("field", field);
		sqlMap.put("table", table);
		sqlMap.put("wheresql", sb.toString());
		
		return sqlMap;
	}
	
	/**
	 * 导出到pdf：按照投标类型分组
	 * @return
	 */
	public Map<String,String> spellDetailGroupSQLPdf(){
		Map<String, String> sqlMap = new HashMap<String, String>();
		String field = "vi.orgcode,sum(vi.investamount) as totalamount,vi.businesstype,vi.interestday," +
		"sum(dailifee) as DAILIFEE";
		String table = "v_dailifee vi";
		StringBuilder sb = new StringBuilder(" 1=1   ");
		 
		if(null == this.year || "".equals(this.year)){
			 this.year = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(0,4); 
		}
		if(null == this.month || "".equals(this.month)){
			this.month = new SimpleDateFormat("yyyyMMdd").format(new Date()).substring(4,6); 
		}
		String nextYear;
		String nextMonth;
		if(Integer.parseInt(this.month)==12){
			nextYear = (Integer.parseInt(this.year)+1)+"";
			nextMonth = "01";
		}else{
			nextYear = this.year;
			nextMonth = Integer.parseInt(this.month)+1>=10?
				(Integer.parseInt(this.month)+1+""):
					"0"+(Integer.parseInt(this.month)+1);
		}
		
		if(Integer.parseInt(this.month)<10){
			this.month = "0"+(Integer.parseInt(this.month));
		}
		
		if(null != queryByOrgCode&&!"".equals(queryByOrgCode)){
			sb.append(" and vi.orgcode = '" + queryByOrgCode + "'");   
		} 
		
		if(null != jingbanren&&!"".equals(jingbanren)){
			sb.append(" and vi.jingbanren like '%" + jingbanren + "%'");   
		} 
		
		if(null != investorname&&!"".equals(investorname)){
			sb.append(" and vi.investorname like '%" + investorname + "%'");   
		} 
		
		if(null != rztyper&&!"".equals(rztyper)){
			sb.append(" and vi.interestday = '" + rztyper + "'");   
		}else if(null != rztype&&!"".equals(rztype)){
			sb.append(" and vi.businesstype = '" + rztype + "'");   
		}
		
		if( null != this.year && null!= this.month ){
			sb.append(" and ((vi.qianyuedate between to_date('"+this.year+this.month+"01"+"', 'yyyyMMdd') and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd')  and vi.rzfwf_tariff = 0) or " +
					"(vi.rzfwf_tariff = 1 and vi.financbasestate = '7' " +
					"and vi.qianyuedate > to_date('20140201','yyyymmdd') " +
					" and to_date('"+nextYear+nextMonth+"01"+"', 'yyyyMMdd') between vi.qianyuedate and add_months(vi.qianyuedate,vi.businesstype))) ");
		}  
		
		sb.append(" group by vi.orgcode,vi.businesstype,vi.interestday");
		sb.append(" order by vi.orgcode,vi.interestday asc,vi.businesstype asc");
		
		sqlMap.put("field", field);
		sqlMap.put("table", table);
		sqlMap.put("wheresql", sb.toString());
		
		return sqlMap;
	}
	
	
	public String getQkeyWord() {
		return qkeyWord;
	}


	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
	}


	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}


	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getJingbanren() {
		return jingbanren;
	}

	public void setJingbanren(String jingbanren) {
		this.jingbanren = jingbanren;
	}

	public String getRztype() {
		return rztype;
	}

	public void setRztype(String rztype) {
		this.rztype = rztype;
	}

	public String getRztyper() {
		return rztyper;
	}

	public void setRztyper(String rztyper) {
		this.rztyper = rztyper;
	}

	public String getInvestorname() {
		return investorname;
	}

	public void setInvestorname(String investorname) {
		this.investorname = investorname;
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public List<Map<String, Object>> getResultList2() {
		return resultList2;
	}

	public void setResultList2(List<Map<String, Object>> resultList2) {
		this.resultList2 = resultList2;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}
	
	
}
