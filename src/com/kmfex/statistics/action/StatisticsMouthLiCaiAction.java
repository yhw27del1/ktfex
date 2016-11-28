package com.kmfex.statistics.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.kmfex.service.MemberBaseService;
import com.kmfex.statistics.service.StatisticsMonthLiCaiService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 投资会员月度理财报告
 * 
 * @author  
 */
@SuppressWarnings("serial")
@Controller("stcsMouthLiCaiAction")
@Scope("prototype")
public class StatisticsMouthLiCaiAction extends BaseAction {
	@Resource UserService userService;
	@Resource StatisticsMonthLiCaiService statisticsMonthLiCaiService;
	/**
	 * 投资会员月度理财报告列表
	 * 
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void list_for_mouth_lc_report() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = request.getParameter("username");
			String state = request.getParameter("state");
			
			
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
			
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			
			
			JSONObject obj = new JSONObject();
			String orgcoding = user.getOrg().getCoding();
			//StringBuilder sql = new StringBuilder(" substr(orgcoding,0,").append(length).append(") = '"+orgcoding+"' and membertype_code = 'T' ");
			StringBuilder sql = new StringBuilder("  instr(orgcoding,'"+orgcoding+"')> 0   and membertype_code = 'T' ");
			
			if(StringUtils.isNotBlank(username)){
				sql.append(" and instr(user_username,'"+username+"') > 0");
			}
			
			if(StringUtils.isNotBlank(state)){
				sql.append(" and state = '"+state+"'");
			}
			if(StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)){
				sql.append(" order by ").append(sort).append(" ").append(order);
			}
			
			List<Map<String,Object>> list = statisticsMonthLiCaiService.queryForList("user_username,name,member_level,orgname,banklib,state,cityname,zzc", "v_member_user_account", sql.toString(), page, pagesize);
			
			int total = this.statisticsMonthLiCaiService.queryForListTotal("user_username", "v_member_user_account", sql.toString());
			JSONArray object = JSONArray.fromObject(list);
			obj.element("total", total);
			obj.element("rows", object);
			
			response.getWriter().println(obj);

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 本方法提供“会员理财月度报告”HTML页面<br/>
	 * @param username 用户交易号
	 * @param shortdate 查询月份
	 * 
	 * 
	 */
	public void html_for_mouth_lc_report(){
		try {
			User source_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(source_user == null) return;
			
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			
			
			
			String username = request.getParameter("username");
			String shortdate_str = request.getParameter("shortdate");
			
			
//外链限制，如需启用，取消注释即可
//			String referer =  request.getHeader("referer");
//			boolean outsidelink = false;
//			if(referer == null || referer.indexOf("/")==-1 ){
//				outsidelink = true;
//			}else{
//				referer = referer.substring(referer.lastIndexOf("/"));
//				if(!"/list_for_mouth_lc_report.jsp".equals(referer)){
//					outsidelink = true;
//				}
//			}
//			if(outsidelink){
//				response.getWriter().print("外链，系统已记录你的IP与交易帐户，请正常访问系统");
//				System.out.println("source_user_username:"+source_user.getUsername()+",time"+new Date()+",description:外链访问投资会员月报,target_user_username="+username);
//				return;
//			}	
//外链限制，如需启用，取消注释即可
			
			
			
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			
			if(StringUtils.isBlank(username)) return;
			User target_user = this.userService.findUser(username);
			if(target_user == null) return;
			
			
			SimpleDateFormat sdf_minidate = new SimpleDateFormat("yyyyMM");
			
			
			Date shortdate = null,now = new Date();
			if(StringUtils.isBlank(shortdate_str)){
				shortdate = now;
			}else{
				try {
					shortdate = sdf_minidate.parse(shortdate_str);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			
			
			String source_user_orgcoding = source_user.getOrg().getCoding();
			String target_user_orgcoding = target_user.getOrg().getCoding();
			if(!target_user_orgcoding.startsWith(source_user_orgcoding)) return;
			
			
			String htmlfilename = username+sdf_minidate.format(shortdate);
			String htmlfileurl = "/back/statistics/investor_month_rp/html/"+htmlfilename+".html";
			String htmlfilePath = ServletActionContext.getServletContext().getRealPath(htmlfileurl);
			
			
			String repayment_project_chart_url = "/back/statistics/investor_month_rp/html/"+htmlfilename+"_chart_.jpg";
			String repayment_project_chart_path = ServletActionContext.getServletContext().getRealPath(repayment_project_chart_url);
			
			
			File htmlfile = new File(htmlfilePath);
			
			
			//如果查询的不是当前月，那么检查是已经生成有文件,如果有，则输出到浏览器，没有则提示没有
			if(!sdf_minidate.format(shortdate).equals(sdf_minidate.format(now))){
				if(htmlfile.exists()){
					BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(htmlfile),"utf-8"));
					StringBuilder str = new StringBuilder();
					String temp;
					while ((temp = in.readLine()) != null){
						str.append(temp);
					}
					in.close();
					response.getWriter().print(str.toString());
				}else{
					response.getWriter().print("该月报表不存在！");
				}
				return;
			}
			
			
			String templatefilePath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/index.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(templatefilePath), "utf-8"));
			StringBuilder str = new StringBuilder();
			String temp;
			while ((temp = in.readLine()) != null) {
				str.append(temp);
			}
			in.close();
			
			
			String result = statisticsMonthLiCaiService.processHtmlCode(target_user.getId(),target_user.getUsername(),target_user.getRealname(),shortdate, htmlfilename, htmlfilePath, repayment_project_chart_url,repayment_project_chart_path, str.toString());
			
			
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(htmlfile),"UTF-8");
			//writer.write(result.replaceAll("<a.*?</a>", ""));
			writer.write(result);
			writer.flush();
			writer.close();
			//response.sendRedirect(htmlfileurl);
			
			
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void pdf_for_mouth_lc_report(){
			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				HttpServletRequest request = ServletActionContext.getRequest();
				
				User source_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if(source_user == null){
					response.sendRedirect("/login.jsp");
					return;
				};
				
				String filename = request.getParameter("file");
				
				
				String referer =  request.getHeader("referer");
				
				boolean outsidelink = false;
				if(referer == null || referer.indexOf("!")==-1 || referer.indexOf("?")==-1 ){
					outsidelink = true;
				}else{
					referer = referer.substring(referer.indexOf("!"));
					referer = referer.substring(0, referer.indexOf("?"));
					if(!"!html_for_mouth_lc_report".equals(referer)){
						outsidelink = true;
					}
				}
				if(outsidelink){
					response.getWriter().print("外链，系统已记录你的IP与交易帐户，请正常访问系统");
					System.out.println("username:"+source_user.getUsername()+",time"+new Date()+",description:外链下载PDF,filename="+filename);
					return;
				}				
				
				
				String htmlfilePath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/html/"+filename+".html");
				String cssfilePath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/style.css");
				String headimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/images/report_head.bmp");
				String chartimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/html/"+filename+"_chart_.jpg");
				String wechatimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/images/wechat.png");
				
				
				InputStream htmlfileis = new FileInputStream(htmlfilePath);
				InputStream cssfileis = new FileInputStream(cssfilePath);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				
				BufferedReader in = new BufferedReader(new InputStreamReader(htmlfileis,"utf-8"));
				StringBuilder str = new StringBuilder();
				String temp;
				while ((temp = in.readLine()) != null){
					str.append(temp);
				}
				in.close();
				
				String result = str.toString().replaceAll("<a.*?</a>", "");
				
				htmlfileis = new   ByteArrayInputStream(result.getBytes("UTF-8"));
				
				Document document = new Document(PageSize.A4,75,75,120,20);

				PdfWriter writer = PdfWriter.getInstance(document, baos);
				XMLWorkerHelper xmlhelper = XMLWorkerHelper.getInstance(); 
				document.open();
				
				Image img_head = Image.getInstance(headimgPath);    
			    //img.setAlignment(Image.LEFT | Image.TOP);  
				img_head.setAbsolutePosition(0, 740);  //位置
				img_head.scaleToFit(1000, 67);// 大小  
			    
				
				
				Image img_chart = Image.getInstance(chartimgPath); 
				//img_chart.setAbsolutePosition(0, 740);  //位置
				img_chart.setAlignment(Image.LEFT | Image.TOP);
				img_chart.scaleToFit(480, 300);// 大小  
				
				
				Image img_wechat = Image.getInstance(wechatimgPath); 
				img_wechat.setAbsolutePosition(426, 350);  //位置
				//img_wechat.setAlignment(Image.RIGHT | Image.BOTTOM);
				img_wechat.scaleToFit(110, 206);// 大小  
				
				
				
				
				document.add(img_head);
				xmlhelper.parseXHtml(writer, document, htmlfileis, cssfileis, Charset.forName("UTF-8"),new ChineseFontProvider());
				document.newPage();
				document.add(img_head);
				document.add(img_chart);
				document.add(img_wechat);
				document.close();
							
				
				
				
				response.setContentType("text/xml;charset=UTF-8");
				response.addHeader("Content-Disposition", "attachment;filename="+filename+".pdf");
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
	
	
	
	
	
	public void year_json_for_mouth_lc_report(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String username = request.getParameter("username");
			if(StringUtils.isBlank(username)) return;
			
			User user = this.userService.findUser(username);
			
			if(user == null) return;
			
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			Calendar cal = Calendar.getInstance();
			int thisyear = cal.get(Calendar.YEAR);
			cal.setTime(user.getRegTime());
			//int startyear = cal.get(Calendar.YEAR) <= 2014 ? 2014: cal.get(Calendar.YEAR);
			int startyear = cal.get(Calendar.YEAR) ;
			if(startyear < 2014) startyear = 2014;
			
			
			
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
			String username = request.getParameter("username");
			String year_str = request.getParameter("year");
			if(StringUtils.isBlank(username)) return;
			
			User user = this.userService.findUser(username);
			
			if(user == null) return;
			
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			Calendar cal = Calendar.getInstance();
			Calendar now = Calendar.getInstance();
			int year = 2012 ;
			int thismonth = cal.get(Calendar.MONTH) + 1;
			int startmonth = 1;
			if(StringUtils.isBlank(year_str)){
				return ;
				//year = cal.get(Calendar.YEAR);
			}else{
				year = Integer.parseInt(year_str);
			}
			cal.setTime(user.getRegTime());
			
			if(year == cal.get(Calendar.YEAR)){
				//如果年为开户年，则开始月为开户月，终止月为当前月
				startmonth = cal.get(Calendar.MONTH) + 1;
				thismonth = 12;
				if(year == now.get(Calendar.YEAR)){
					thismonth = now.get(Calendar.MONTH) + 1;
				}
			}else if(year == now.get(Calendar.YEAR)){
				//如果年为当年，且不是开户年，则开始月为1月，终止月为当前月
				startmonth = 1;
				thismonth = now.get(Calendar.MONTH) + 1;
			}else{
				//其它情况，不是开户年，也不是当前年，开始月为1，终止月为12
				startmonth = 1;
				thismonth = 12;
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
	
	

	public void test() throws IOException {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.getWriter().print("WTF");
	}
	
	class ChineseFontProvider extends XMLWorkerFontProvider  {

		@Override
		public com.itextpdf.text.Font getFont(String s, String s1, boolean flag, float f, int i, BaseColor basecolor) {
			BaseFont bfChinese = null;
			try {
				bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			com.itextpdf.text.Font descriptionfont = new com.itextpdf.text.Font(bfChinese,f,i,basecolor);
			return descriptionfont;
		}
	}
	
	
// public static void main( String[] args ) throws DocumentException,
// IOException{
//	 createChart("d:/test.jpg");
//    }
//
}
