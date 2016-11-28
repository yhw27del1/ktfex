package com.kmfex.statistics.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kmfex.service.PaymentRecordService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 交易手续费
 * @author ycc
 */
@SuppressWarnings("serial")
@Controller("jysxFeeAction")
@Scope("prototype")
public class StatisticsJYSXFeeAction  extends BaseAction {
	@Resource private transient PaymentRecordService paymentRecordService;
	
	
	public void orgList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter outer = response.getWriter();
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			SimpleDateFormat _short = new SimpleDateFormat("yyyyMMdd");
			
			StringBuilder wheresql = new StringBuilder();
			StringBuilder fieldsql = new StringBuilder();
			
			ArrayList<Object> args_list = new ArrayList<Object>();
			String orgcode = null,orgcoding = null;
			String datafilter = "";
			orgcoding = user.getOrg().getCoding();
			
			
			int page = 1,rows = 15;
			if(StringUtils.isNotBlank(request.getParameter("page")) && StringUtils.isNumeric(request.getParameter("page"))){
				page = Integer.parseInt(request.getParameter("page"));
				if(page <= 0) page = 1;
			}
			
			if(StringUtils.isNotBlank(request.getParameter("rows")) && StringUtils.isNumeric(request.getParameter("rows"))){
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			
			if(StringUtils.isNotBlank(request.getParameter("orgcode"))){
				orgcode = request.getParameter("orgcode");
			}
			
			
			if(StringUtils.isNotBlank(orgcoding)){
				wheresql.append(" ol.coding like ? ");
				args_list.add(orgcoding+"%");
			}  
			if(StringUtils.isNotBlank(orgcode)){
				wheresql.append(" and ol.showcoding like ? ");  
				args_list.add("%"+orgcode+"%");
			} 
			
			if(this.getStartDate() != null && this.getEndDate() != null){
				datafilter = " and to_char(vpo.shdate,'yyyymmdd') between '"+_short.format(this.getStartDate())+"' and '"+_short.format(this.getEndDate())+"' ";  
			}
			fieldsql.append("ol.name_ name,ol.showcoding,ol.typename type,ol.mobile_ ,ol.address_,ol.creatdate,ol.statename,(case when po.ii_fee is null then 0 else po.ii_fee end) ii_fee ");
			
			JSONObject json = new JSONObject();
			List<Map<String,Object>> list = this.paymentRecordService.queryForList(fieldsql.toString(), " v_org_list ol left join (select  sum(vpo.ii_fee) ii_fee,vpo.org_id from v_paymentrecord_org vpo where vpo.ii_fee is not null and to_char(vpo.shdate,'yyyymmdd')<'20140801' and vpo.ii_fee != 0 "+datafilter+" group by vpo.org_id) po on po.org_id = ol.id ", wheresql.toString()+"order by ol.showcoding", args_list.toArray(),page, rows,true);
			int total = this.paymentRecordService.queryForListTotal("ol.id", " v_org_list ol", wheresql.toString(), args_list.toArray());
			json.element("rows", list);
			json.element("total", total);
			outer.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String detail_list(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			SimpleDateFormat _short = new SimpleDateFormat("yyyyMMdd");
			
			StringBuilder wheresql = new StringBuilder();
			StringBuilder fieldsql = new StringBuilder();
			
			ArrayList<Object> args_list = new ArrayList<Object>();
			String orgcode = null,orgcoding = null;
			orgcoding = user.getOrg().getCoding();
			
			int page = 1,rows = 15,action = 0;
			if(StringUtils.isNotBlank(request.getParameter("page")) && StringUtils.isNumeric(request.getParameter("page"))){
				page = Integer.parseInt(request.getParameter("page"));
				if(page <= 0) page = 1;
			}
			
			if(StringUtils.isNotBlank(request.getParameter("rows")) && StringUtils.isNumeric(request.getParameter("rows"))){
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			if(StringUtils.isNotBlank(request.getParameter("action")) && StringUtils.isNumeric(request.getParameter("action"))){
				action = Integer.parseInt(request.getParameter("action"));
			}
			
			if ( action < 0 || action > 2){
				return null;
			}
			
			
			
			if(StringUtils.isNotBlank(request.getParameter("orgcode"))){
				orgcode = request.getParameter("orgcode");
			}
			
			wheresql.append(" o.id = p.beneficiary_org_id and p.state != 0 and trunc(p.ss_fee_4*0.2,2) > 0 and p.investrecord_id = i.id and i.financingbase_id = fb.id and u.id = p.beneficiary_id");
			
			if(StringUtils.isNotBlank(orgcoding)){
				wheresql.append(" and o.coding like ? ");
				args_list.add(orgcoding+"%");
			}  
			if(StringUtils.isNotBlank(orgcode)){
				wheresql.append(" and o.showcoding like ? ");  
				args_list.add("%"+orgcode+"%");
			} 
			
			
			if(this.getStartDate() != null && this.getEndDate() != null){
				wheresql.append(" and to_char(p.actually_repayment_date,'yyyymmdd') between ? and ? ");
				args_list.add(_short.format(this.getStartDate()));
				args_list.add(_short.format(this.getEndDate()));
			}
			
			wheresql.append(" and to_char(p.actually_repayment_date,'yyyymmdd') <'20140801'  ");
			
			fieldsql.append("fb.code,p.actually_repayment_date shdate,(p.shlx+p.penal) sy ,trunc(p.ss_fee_4*0.2,2) ii_fee,u.username");
			
			if(action == 0){
				JSONObject json = new JSONObject();
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fieldsql.toString(), "t_payment_record p , sys_org o,t_invest_record i,t_financing_base fb,sys_user u", wheresql.toString(), args_list.toArray(),page,rows);
				Map<String,Object> footer = this.paymentRecordService.queryForObject("sum(p.shlx+p.penal) SY,sum(trunc(p.ss_fee_4*0.2,2)) II_FEE", "t_payment_record p , sys_org o,t_invest_record i,t_financing_base fb,sys_user u", wheresql.toString(), args_list.toArray());
				int total = this.paymentRecordService.queryForListTotal("p.id", "t_payment_record p , sys_org o,t_invest_record i,t_financing_base fb,sys_user u", wheresql.toString(), args_list.toArray());
				
				JSONArray footer_arr = new JSONArray();
				footer_arr.add(footer);
				
				
				json.element("rows", list);
				json.element("footer", footer_arr);
				json.element("total", total);
				response.getWriter().print(json);
			}else if(action == 1){
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fieldsql.toString(), "t_payment_record p , sys_org o,t_invest_record i,t_financing_base fb,sys_user u", wheresql.toString(), args_list.toArray());
				request.setAttribute("list", list);
				return "detail_list_excel";
			}else if(action == 2){
				List<Map<String,Object>> list = this.paymentRecordService.queryForList(fieldsql.toString(), "t_payment_record p , sys_org o,t_invest_record i,t_financing_base fb,sys_user u", wheresql.toString(), args_list.toArray());
				detail_pdf(response,list,this.getStartDate(),this.getEndDate(),orgcode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void detail_pdf(HttpServletResponse response,List<Map<String,Object>> list, Date startDate, Date endDate, String orgcode){
		DecimalFormat df = new DecimalFormat("#.00");
		
		SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");
		String headimgPath = ServletActionContext.getServletContext().getRealPath("/back/statistics/investor_month_rp/images/report_head.bmp");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Document document = new Document(PageSize.A4,20,20,20,20);
		try {
			PdfWriter.getInstance(document, baos);
			
			Image img_head = Image.getInstance(headimgPath);    
			img_head.setAlignment(Image.LEFT | Image.TOP);  
			img_head.scaleToFit(1000, 67);// 大小
			
			
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED); 
			Font titlefont = new Font(bfChinese,18,Font.BOLD,BaseColor.BLACK);
			Font tableBodyFont = new Font(bfChinese,12,Font.NORMAL,new BaseColor(77,77,77));
			Font tableHeaderFont = new Font(bfChinese,12,Font.NORMAL,BaseColor.BLACK);
			Font paginationFont = new Font(bfChinese,12,Font.NORMAL,BaseColor.BLACK);
			
			
			Paragraph title = new Paragraph("昆投互联网金融交易",titlefont);
			title.setAlignment(Element.ALIGN_CENTER);
			
			String datapart = null;
			if(startDate != null){
				if(endDate == null){
					datapart = sdf.format(startDate)+"-至今";
				}else{
					datapart = sdf.format(startDate)+"-"+sdf.format(endDate);
				}
			}else{
				if(endDate == null){
					datapart = "未设定";
				}else{
					datapart = sdf.format(startDate)+"之前";
				}
			}
			
			Map<String,Object> org_name = this.paymentRecordService.queryForObject("o.name_ name", "sys_org o", " o.showcoding = ?", new Object[]{orgcode});
			
			Paragraph description = new Paragraph("项目：交易手续费明细表      统计区间:"+datapart,paginationFont);
			Paragraph description2 = new Paragraph(" 统计机构:"+"("+orgcode+")"+org_name.get("name")+"       生成时间："+sdf_.format(new Date()),paginationFont);
			description.setAlignment(Element.ALIGN_CENTER);
			description2.setAlignment(Element.ALIGN_CENTER);
			
			
			
			document.open();
			
			document.add(img_head);
			document.add(title);
			document.add(description);
			document.add(description2);
			document.add(new Paragraph("                  ",tableBodyFont));
			
			PdfPTable table = null;
			
			int firstpagesize = 25;
			int otherpagesize = 30;
			
			int pagecount = 1;
			int listsize = list.size();
			
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
			
			int [] widths = new int[]{30,60,60,60,60,100};
			double sy_sum = 0d , ii_fee_sum = 0d;
			
			
			df = new DecimalFormat("#,##0.00");
			int totalcount = 0;
			for (Iterator<Map<String, Object>> iter = list.iterator(); iter.hasNext();) {
				Map<String, Object> item = iter.next();
				if (count == 1) {
					if (page == 1) {
						pagesize = firstpagesize;
					} else {
						pagesize = otherpagesize;
					}
					table = new PdfPTable(6);
					table.setWidths(widths);
					table.setWidthPercentage(90);

					// 每页添加一个表头
					Phrase header_0 = new Phrase("#", tableHeaderFont);
					Phrase header_1 = new Phrase("投资人帐号", tableHeaderFont);
					Phrase header_2 = new Phrase("融资项目号", tableHeaderFont);
					Phrase header_3 = new Phrase("还款日期", tableHeaderFont);
					Phrase header_4 = new Phrase("收益金额", tableHeaderFont);
					Phrase header_5 = new Phrase("产生交易手续费", tableHeaderFont);

					headercell.setBackgroundColor(new BaseColor(240, 240, 240));
					headercell.setPhrase(header_0);
					table.addCell(headercell);
					headercell.setPhrase(header_1);
					table.addCell(headercell);
					headercell.setPhrase(header_2);
					table.addCell(headercell);
					headercell.setPhrase(header_3);
					table.addCell(headercell);
					headercell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					headercell.setPhrase(header_4);
					table.addCell(headercell);
					headercell.setPhrase(header_5);
					table.addCell(headercell);
					headercell.setHorizontalAlignment(Element.ALIGN_LEFT);
				}
				
				double sy_d = Double.parseDouble(item.get("sy").toString());
				double ii_fee_d = Double.parseDouble(item.get("ii_fee").toString());
				
				sy_sum += sy_d;
				ii_fee_sum += ii_fee_d;
				
				Phrase countphrase = new Phrase(Integer.toString(totalcount+1), tableBodyFont);
				Phrase investuser = new Phrase(item.get("username").toString(), tableBodyFont);
				Phrase financingcode = new Phrase(item.get("code").toString(), tableBodyFont);
				Phrase shdate = new Phrase(_sdf.format(item.get("shdate")), tableBodyFont);
				Phrase sy = new Phrase(df.format(sy_d), tableBodyFont);
				Phrase ii_fee = new Phrase(df.format(ii_fee_d), tableBodyFont);

				contentcell.setPhrase(countphrase);
				table.addCell(contentcell);
				contentcell.setPhrase(investuser);
				table.addCell(contentcell);
				contentcell.setPhrase(financingcode);
				table.addCell(contentcell);
				contentcell.setPhrase(shdate);
				table.addCell(contentcell);
				contentcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				contentcell.setPhrase(sy);
				table.addCell(contentcell);
				contentcell.setPhrase(ii_fee);
				table.addCell(contentcell);
				contentcell.setHorizontalAlignment(Element.ALIGN_LEFT);

				if (count == pagesize || !iter.hasNext()) {
					if (!iter.hasNext()) {
						if (count % 2 == 0) {
							contentcell.setBackgroundColor(new BaseColor(240, 240, 240));
						} else {
							contentcell.setBackgroundColor(BaseColor.WHITE);
						}
						Phrase sy_sum_phs = new Phrase(df.format(sy_sum), tableBodyFont);
						Phrase iifee_sum_phs = new Phrase(df.format(ii_fee_sum), tableBodyFont);
						Phrase space_phs = new Paragraph();
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(space_phs);
						table.addCell(contentcell);
						table.addCell(contentcell);
						contentcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						contentcell.setPhrase(sy_sum_phs);
						table.addCell(contentcell);
						contentcell.setPhrase(iifee_sum_phs);
						table.addCell(contentcell);
					}
					Paragraph pagination = new Paragraph("共" + pagecount + "页，第" + page + "页", paginationFont);
					pagination.setAlignment(Element.ALIGN_RIGHT);
					pagination.setSpacingBefore(10);
					document.add(table);
					document.add(pagination);
					document.newPage();
					page++;
					count = 1;
				} else {
					count++;
				}

				totalcount += 1;
			}

			document.close();
			
			response.setContentType("text/xml;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=transactionfees.pdf");
			response.setContentType("application/pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
			response.setHeader("Pragma", "public");
			response.addHeader("Content-Length", Integer.toString(baos.size()));
						
			ServletOutputStream sos = response.getOutputStream();
						
			baos.writeTo(sos);
			sos.flush();
			sos.close();
		
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
