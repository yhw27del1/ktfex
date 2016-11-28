package com.kmfex.zhaiquan.action;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.kmfex.MoneyFormat;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.CostItem;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
import com.opensymphony.xwork2.Action;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 
 * @author eclipse
 * @author aora
 * 修改记录   
 * 2013年5月30日16:32   修改execute()方法,根据关键字模糊查询修正--去除关键字前后空格；

 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class ContractAction extends BaseAction {

	@Resource
	ContractService contractService;

	@Resource
	ContractKeyDataService contractKeyDataService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	PaymentRecordService paymentRecordService;
	@Resource SellingService sellingService;

	/**
	 * 出让方关键字
	 */
	private String keyword;
	/**
	 * 债权代码
	 */
	private String searchtype;
	// 导出功能
	private String excelFlag;
	private String id;

	private Contract this_;
	/**
	 * 主合同编号
	 */
	private String code;
	/**
	 * 主合同对应投标记录ID
	 */
	private String ivr_id;

	/**
	 * 投标记录的id号
	 * */
	private String investRecordId;

	/**
	 * 要预览协议user的username(编号)
	 * */
	private String username;
	/**
	 * 出让价格
	 */
	private double price = 0d;  
	private double zqfwf_b = 0d;  
	private double zqsf_b= 0d;  
	private double zqfwf_s = 0d;  
	private double zqsf_s= 0d;  
	
	@Resource
	private UserService userService;
	@Resource
	ChargingStandardService chargingStandardService;// 收费明细
	
	@Override
	public String execute() throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Contract> pageView = new PageView<Contract>(
					getShowRecord(), getPage());

			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 ");
			List<String> params = new ArrayList<String>();
			if (null != this.keyword && !"".equals(this.keyword)) {
				keyword = this.keyword.trim();
				if ("a".equals(searchtype)) {
					sb.append(" and ( o.buyer.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.buyer.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" )");
				} else if ("b".equals(searchtype)) {
					sb.append(" and  o.zhaiQuanCode like ? ");
					params.add("%" + this.keyword + "%");
				} else if ("c".equals(searchtype)) {
					sb.append(" and  o.contract_numbers like ? ");
					params.add("%" + this.keyword + "%");
				}
			}

			sb.append(" and o.fbrq between to_date('"
					+ format.format(this.getStartDate())
					+ "','yyyy-MM-dd') and  to_date('"
					+ format.format(endDateNext) + "','yyyy-MM-dd') ");

			pageView.setQueryResult(contractService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby));
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	public String fee() throws Exception {
		try { 
			CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
			ServletActionContext.getRequest().setAttribute("bl", sxf.getPercent());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Contract> pageView = new PageView<Contract>(
					getShowRecord(), getPage());
			
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 ");
			List<String> params = new ArrayList<String>();
			if (null != this.keyword && !"".equals(this.keyword)) {
				if ("a".equals(searchtype)) {
					sb.append(" and ( o.buyer.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.buyer.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" )");
				} else if ("b".equals(searchtype)) {
					sb.append(" and  o.zhaiQuanCode like ? ");
					params.add("%" + this.keyword + "%");
				} else if ("c".equals(searchtype)) {
					sb.append(" and  o.contract_numbers like ? ");
					params.add("%" + this.keyword + "%");
				} else if ("d".equals(searchtype)) {
					sb.append(" and ( o.seller.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" )");
				} else if ("e".equals(searchtype)) {
					sb.append(" and ( o.buyer.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.buyer.username like ? ");
					params.add("%" + this.keyword + "%"); 
					sb.append(" )");
				}
			}
			
			sb.append(" and o.fbrq between to_date('"
					+ format.format(this.getStartDate())
					+ "','yyyy-MM-dd') and  to_date('"
					+ format.format(endDateNext) + "','yyyy-MM-dd') ");
			
			pageView.setQueryResult(contractService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby));
			ServletActionContext.getRequest()
			.setAttribute("pageView", pageView);
			//汇总计算
			price = 0d;zqfwf_b = 0d;zqsf_b= 0d;zqfwf_s = 0d;zqsf_s= 0d;  
			List<Contract> contracts=contractService.getScrollData(sb.toString(),params).getResultlist();
			for(Contract o:contracts){
				price+=o.getPrice();
				zqfwf_b+=o.getBuying().getZqfwf();
				zqsf_b+=o.getBuying().getZqsf();
				zqfwf_s+=o.getSelling().getZqfwf();
				zqsf_s+=o.getSelling().getZqsf();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fee";
	}
	public String feePrint() throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Contract> pageView = new PageView<Contract>(9999999, getPage());
			CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
			ServletActionContext.getRequest().setAttribute("bl", sxf.getPercent());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("fbrq", "asc");     
			StringBuilder sb = new StringBuilder(" 1=1 ");
			List<String> params = new ArrayList<String>();
			if (null != this.keyword && !"".equals(this.keyword)) {
				if ("a".equals(searchtype)) {
					sb.append(" and ( o.buyer.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.buyer.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" )");
				} else if ("b".equals(searchtype)) {
					sb.append(" and  o.zhaiQuanCode like ? ");
					params.add("%" + this.keyword + "%");
				} else if ("c".equals(searchtype)) {
					sb.append(" and  o.contract_numbers like ? ");
					params.add("%" + this.keyword + "%");
				} else if ("d".equals(searchtype)) {
					sb.append(" and ( o.seller.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.seller.username like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" )");
				} else if ("e".equals(searchtype)) {
					sb.append(" and ( o.buyer.realname like ? ");
					params.add("%" + this.keyword + "%");
					sb.append(" or o.buyer.username like ? ");
					params.add("%" + this.keyword + "%"); 
					sb.append(" )");
				}
			}
			
			
			sb.append(" and o.fbrq between to_date('"
					+ format.format(this.getStartDate())
					+ "','yyyy-MM-dd') and  to_date('"
					+ format.format(endDateNext) + "','yyyy-MM-dd') ");
			
			pageView.setQueryResult(contractService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby));
			ServletActionContext.getRequest()
			.setAttribute("pageView", pageView);
			//汇总计算
			price = 0d;zqfwf_b = 0d;zqsf_b= 0d;zqfwf_s = 0d;zqsf_s= 0d;  
			List<Contract> contracts=contractService.getScrollData(sb.toString(),params).getResultlist();
			for(Contract o:contracts){
				price+=o.getPrice();
				zqfwf_b+=o.getBuying().getZqfwf();
				zqsf_b+=o.getBuying().getZqsf();
				zqfwf_s+=o.getSelling().getZqfwf();
				zqsf_s+=o.getSelling().getZqsf();
			}
			
			
			if ("1".equals(excelFlag)) {// 导出功能  
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
				return "feeEx";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "feePrint";
	}
	/**
	 * 预览协议
	 * 
	 * @return
	 * @throws Exception
	 */
	public String preview_for_buy() throws Exception {
		try {
			if (this.ivr_id != null && !this.ivr_id.isEmpty()) {
				HttpServletRequest request = ServletActionContext.getRequest();
				InvestRecord ir = this.investRecordService
						.selectById(this.ivr_id);
				PaymentRecord pr = this.paymentRecordService
						.selectById(
								"from PaymentRecord where investRecord.id = ? order by predict_repayment_date desc",
								ir.getId());
				
				Selling sell = this.sellingService.selectById(" from Selling where investRecord.id = ? order by createDate desc", new Object[]{ir.getId()});
				
				User user = null;
				Object obj = SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();

				if ("anonymousUser".equals(obj)) {
					// 兼顾客户端预览债权转让协议的功能，客户端必须要传一个用户的id号
					if (null != username && !"".equals(username)) {
						user = userService.findUser(username);
					}
				} else {
					user = (User) obj;
				}
				if (null == user) {
					throw new Exception("所需的用户数据不够，请先登录！");
				}

				double price = Double.valueOf(request.getParameter("price"));

				if (ir != null && pr != null) {
					this_ = new Contract();
					if (user != null
							&& !ir.getInvestor().getUser().getUsername()
									.equals(user.getUsername())) {
						this_.setBuyer(user);
					}
					this_.setSeller(ir.getInvestor().getUser());
					this_.setContract_numbers(ir.getContract()
							.getContract_numbers());
					this_.setEndDate(pr.getExtension_period() == null ? pr
							.getPredict_repayment_date() : pr
							.getExtension_period());
					// this_.setFbrq(fbrq);
					this_.setInvestRecord(ir);
					this_.setPrice(price);
					this_.setPrice_dx(MoneyFormat.format(
							Double.toString(price), true));
					this_.setSyje(ir.getBjye() + ir.getLxye());
					this_.setSyje_dx(MoneyFormat.format(Double.toString(ir
							.getBjye()
							+ ir.getLxye()), true));
					this_.setXieyiCode(this.contractService
							.getNextContractCode(ir, user));
					this_.setZhaiQuanCode(ir.getZhaiQuanCode());
					
					/*
					 * 
					 * 出让方出让时间做为 签约时间
					 */
					if( sell!=null ){
						this_.setSellerDate(sell.getCreateDate());
					}

					WebApplicationContext wac = WebApplicationContextUtils
							.getRequiredWebApplicationContext(ServletActionContext
									.getServletContext());
					ZhaiQuanInvestAction zqia = (ZhaiQuanInvestAction) wac
							.getBean("zhaiQuanInvestAction");
					zqia.setMoney(price);
					zqia.setZhaiQuanId(this.ivr_id);
					HashMap<String, Double> fy = zqia.js();

					request.setAttribute("price", price);// 成交价
					request.setAttribute("zqfwf", fy.get("fee"));// 债权转让手续费
					request.setAttribute("zqsf", fy.get("taxes"));// 债权税费
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "preview_for_buy";
	}

	/**
	 * 预览协议
	 * 
	 * @return
	 * @throws Exception
	 */
	public String preview_for_sell() {
		try {
			if (this.ivr_id != null && !this.ivr_id.isEmpty()) {
				HttpServletRequest request = ServletActionContext.getRequest();
				InvestRecord ir = this.investRecordService
						.selectById(this.ivr_id);
				PaymentRecord pr = this.paymentRecordService
						.selectById(
								"from PaymentRecord where investRecord.id = ? order by predict_repayment_date desc",
								ir.getId());

				double price = Double.valueOf(request.getParameter("price"));

				// User user = pr.getInvestRecord().getInvestor().getUser();

				if (ir != null && pr != null) {
					this_ = new Contract();

					this_.setSeller(ir.getInvestor().getUser());
					this_.setContract_numbers(ir.getContract()
							.getContract_numbers());
					this_.setEndDate(pr.getExtension_period() == null ? pr
							.getPredict_repayment_date() : pr
							.getExtension_period());
					// this_.setFbrq(fbrq);
					this_.setInvestRecord(ir);
					this_.setPrice(price);
					this_.setPrice_dx(MoneyFormat.format(
							Double.toString(price), true));
					this_.setSyje(ir.getBjye() + ir.getLxye());
					this_.setSyje_dx(MoneyFormat.format(Double.toString(ir
							.getBjye()
							+ ir.getLxye()), true));

					this_.setZhaiQuanCode(ir.getZhaiQuanCode());
					
					this_.setSellerDate(new Date());
					
					WebApplicationContext wac = WebApplicationContextUtils
							.getRequiredWebApplicationContext(ServletActionContext
									.getServletContext());
					ZhaiQuanInvestAction zqia = (ZhaiQuanInvestAction) wac
							.getBean("zhaiQuanInvestAction");
					zqia.setMoney(price);
					zqia.setZhaiQuanId(this.ivr_id);
					HashMap<String, Double> fy = zqia.js();

					request.setAttribute("price", price);// 成交价
					request.setAttribute("zqfwf", fy.get("fee"));// 债权转让手续费
					request.setAttribute("zqsf", fy.get("taxes"));// 债权税费
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "preview_for_sell";
	}

	/**
	 * 查看已存在协议
	 * 
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		this.this_ = this.contractService.selectById(this.id);
		this.this_.setPrice_dx(MoneyFormat.format(
				Double.toString(this.this_.getPrice()), true)); 
		this.this_.setSyje_dx(MoneyFormat.format(Double.toString(this.this_.getSyje()), true));
		return "view";
	}

	/**
	 * 查看主合同 跳转到原action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String agreement() throws Exception {
		ContractKeyData contract = this.contractKeyDataService.selectById(
				" from ContractKeyData where contract_numbers = ?", this.code);
		if (contract != null) {
			this.ivr_id = contract.getInverstrecord_id();
		}
		return "agreement";
	}

	public String pdf() {
		try {
			// System.out.println("pdf start");
			String filePath = ServletActionContext.getServletContext()
					.getRealPath("/download/assignment.pdf");
			HttpServletResponse resp = ServletActionContext.getResponse();
			resp.setCharacterEncoding("utf-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfReader pdf = new PdfReader(new FileInputStream(filePath));

			PdfStamper stamp = new PdfStamper(pdf, baos);

			this_ = this.contractService.selectById(this.id);
			if (this_ == null) {
				resp.getWriter().write("file not found");
				return null;
			}
			ServletOutputStream sos = resp.getOutputStream();

			/* 取出报表模板中的所有字段 */
			AcroFields form = stamp.getAcroFields();
			/* 为字段赋值,注意字段名称是区分大小写的 */
			form.setField("xybh", this_.getXieyiCode());
			form.setField("jfmc", this_.getSeller().getRealname());
			form.setField("yfmc", this_.getBuyer().getRealname());
			form.setField("zqbh", this_.getInvestRecord().getZhaiQuanCode());
			form.setField("sghtbh", this_.getInvestRecord().getContract()
					.getContract_numbers());
			Calendar end_time = Calendar.getInstance();
			end_time.setTime(this_.getEndDate());
			form.setField("dqrq_n", Integer.toString(end_time
					.get(Calendar.YEAR)));
			form.setField("dqrq_y", Integer.toString(end_time
					.get(Calendar.MONDAY) + 1));
			form.setField("dqrq_r", Integer.toString(end_time
					.get(Calendar.DATE))); 
			form.setField("syje", DoubleUtils.doubleToString(this_.getSyje(), 2));
			form.setField("syje_dx", this_.getSyje_dx());
			form.setField("zrjg", DoubleUtils.doubleToString(this_.getPrice(), 2));
			form.setField("zrjg_dx", this_.getPrice_dx());
			form.setField("jfhybh", this_.getSeller().getUsername());
			form.setField("jfqssj", new SimpleDateFormat("yyyy-MM-dd")
					.format(this_.getSellerDate()));
			form.setField("yfhybh", this_.getBuyer().getUsername());
			form.setField("yfqssj", new SimpleDateFormat("yyyy-MM-dd")
					.format(this_.getBuyerDate()));
			form.setField("fbsj", new SimpleDateFormat("yyyy-MM-dd")
					.format(this_.getFbrq()));

			form.setField("bffddbr", "XXX");

			stamp.setFormFlattening(true);

			int total = pdf.getNumberOfPages() + 1;
			// 增加内容

			Image image = Image.getInstance(ServletActionContext
					.getServletContext().getRealPath(
							"/Static/images/tag.png"));
			image.setAbsolutePosition(0, 200);
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.5f);
			PdfContentByte over;
			for (int i = 1; i < total; i++) {
				over = stamp.getUnderContent(i);
				// over.beginText();
				over.addImage(image);
				over.setGState(gs);
				// over.setFontAndSize(base, 18);
				// over.setColorFill(BaseColor.RED);
				// over.showText("page1");
				// over.showTextAligned(Element.ALIGN_LEFT, waterMarkName, 230,
				// 430, 45);
				// over.setTextMatrix(30, 30);
				// over.endText();

				// 画一个圆
				// under.ellipse(250, 450, 350, 550);
				// under.setLineWidth(1f);
				// under.stroke();
			}

			/* 必须要调用这个，否则文档不会生成的 */
			stamp.close();

			resp.setContentType("text/xml;charset=UTF-8");
			resp.addHeader("Content-Disposition", "attachment; filename="
					+ this_.getId() + ".pdf");
			resp.setContentType("application/pdf");
			resp.setHeader("Content-Transfer-Encoding", "binary");
			resp.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			resp.setHeader("Pragma", "public");
			resp.addHeader("Content-Length", Integer.toString(baos.size()));

			baos.writeTo(sos);

			sos.flush();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 以json格式返回指定债权的买入价
	 * */
	public String getBuyPrice() {

		String hql = "from Contract where investRecord.id = '" + investRecordId
				+ "' order by fbrq desc";

		Contract contract = contractService.selectByHql(hql);

		double buyPrice = investRecordService.selectById(investRecordId)
				.getInvestAmount();
		try {
			if (null != contract) {
				buyPrice = contract.getPrice();
			}
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(),
					buyPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	
	public String listByInvestRecordId(){
		List<Contract> cs = this.contractService.getScrollDataCommon("from Contract c where c.investRecord.id = ?", this.investRecordId);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("list", cs);
		return "listByInvestRecordId";
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSearchtype() {
		return searchtype;
	}

	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Contract getThis_() {
		return this_;
	}

	public void setThis_(Contract this_) {
		this.this_ = this_;
	}

	public String getIvr_id() {
		return ivr_id;
	}

	public void setIvr_id(String ivr_id) {
		this.ivr_id = ivr_id;
	}

	public String getInvestRecordId() {
		return investRecordId;
	}

	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getZqfwf_b() {
		return zqfwf_b;
	}
	public void setZqfwf_b(double zqfwf_b) {
		this.zqfwf_b = zqfwf_b;
	}
	public double getZqsf_b() {
		return zqsf_b;
	}
	public void setZqsf_b(double zqsf_b) {
		this.zqsf_b = zqsf_b;
	}
	public double getZqfwf_s() {
		return zqfwf_s;
	}
	public void setZqfwf_s(double zqfwf_s) {
		this.zqfwf_s = zqfwf_s;
	}
	public double getZqsf_s() {
		return zqsf_s;
	}
	public void setZqsf_s(double zqsf_s) {
		this.zqsf_s = zqsf_s;
	}
	public String getExcelFlag() {
		return excelFlag;
	}
	public void setExcelFlag(String excelFlag) {
		this.excelFlag = excelFlag;
	}

}
