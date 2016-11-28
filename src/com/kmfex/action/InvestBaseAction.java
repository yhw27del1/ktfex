package com.kmfex.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.kmfex.Constant;
import com.kmfex.InvestVO;
import com.kmfex.MoneyFormat;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.ContractTemplate;
import com.kmfex.model.CostBase;
import com.kmfex.model.CostItem;
import com.kmfex.model.CreditRules;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestCondition;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.InvestRecordCost;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberGuarantee;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.CostCategoryService;
import com.kmfex.service.CreditRulesService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.InvestConditionService;
import com.kmfex.service.InvestRecordCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 投标业务
 * 
 * @author linuxp
 * @author aora
 * 2013-07-09 15:09     合同相关按日按月动态显示 
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class InvestBaseAction extends BaseAction implements Preparable {
	private String id;
	private User user = new User();
	private MemberBase member = new MemberBase();
	private Account account = new Account();
	private FinancingBase financingBase;
	private InvestCondition condition;
	private InvestVO invest = new InvestVO();
	private InvestRecord record;
	private ContractKeyData contract;
	private InvestRecordCost cost;
	private String invest_record_id;
	private String second_party_idcard;
	@Resource
	InvestConditionService investConditionService;
	@Resource
	ContractKeyDataService contractKeyDataService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	InvestRecordCostService investRecordCostService;
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	UserService userService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	AccountService accountService;
	@Resource
	CostCategoryService costCategoryService;// 收费项目
	@Resource
	ChargingStandardService chargingStandardService;// 收费明细
	@Resource
	CreditRulesService creditRulesService;
	@Resource
	MemberGuaranteeService memberGuaranteeService;
	private MemberGuarantee memberGuarantee = new MemberGuarantee();
	private String groupBy = "CreateDate";// T表示按投资人统计，R表示按融资方统计
	private String memberId;
	private double investMoney;// 投标金额
	private String financingBaseId;// 融资项目ID
	// 导出功能
	private String excelFlag;

	// 投标查询统计，按日期统计，按投资人统计，按融资方统计
	public String recordListTotal() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		try {
			PageView<Object> pageView = new PageView<Object>(getShowRecord(), getPage());
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + this.getKeyWord() + "%");

				sb.append(" or o.investor.pName like ?");
				params.add("%" + this.getKeyWord() + "%");

				sb.append(" or o.investor.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");

				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + this.getKeyWord() + "%");

				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");

				sb.append(" ) ");
			}
			sb.append(" and ");
			sb.append(" o.state<>'3'");
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			if ("CreateDate".equals(this.groupBy)) {
				sb.append(" group by to_date(to_char(o.createDate,'yyyy-MM-dd'),'yyyy-MM-dd')");
			} else if ("T".equals(this.groupBy)) {
				sb.append(" group by o.investor.id,o.investor.pName,o.investor.user.username ");
			} else if ("R".equals(this.groupBy)) {
				sb.append(" group by o.financingBase.financier.id,o.financingBase.financier.eName, o.financingBase.financier.user.username ");
			}
			QueryResult<Object> qr = investRecordService.groupBy(sb.toString(), this.groupBy, params);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recordList";
	}

	// 投标查询明细
	public String recordListDetail() {
		if (null != memberId && !"".equals(memberId)) {
			this.member = this.memberBaseService.selectById(memberId);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1  ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.financingBase.shortName like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.investor.pName like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.investor.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.financingBase.financier.eName like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.financingBase.financier.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.state<>'3'");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		if ("T".equals(this.groupBy)) {// 投资人id
			sb.append(" and ");
			// sb.append(" o.investor.id='"+memberId+"'");
			sb.append(" o.id in (" + contractKeyDataService.findContractKeyDatas(this.member.getUser().getUsername()) + ")"); // 根据合同的投资人找投标记录
		} else if ("R".equals(this.groupBy)) {// 融资方id
			sb.append(" and ");
			sb.append(" o.financingBase.financier.id='" + memberId + "'");
		}
		try {
			pageView.setQueryResult(investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recordListDetail";
	}

	// 投资投标的明细（所有）
	public String recordList() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		try {
			PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" ) ");
			}
			// sb.append(" and ");
			// sb.append(" o.state='1'");
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			QueryResult<InvestRecord> qr = investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<InvestRecord> lis = qr.getResultlist();
			List<InvestRecord> tempfbs = new ArrayList<InvestRecord>();
			for (InvestRecord f : lis) {
				f.setPreAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getPreAmount(), 3), false));
				f.setNextAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getNextAmount(), 3), false));
				f.setInvestAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getInvestAmount(), 3), false));
				f.getCost().setTzfwf_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getCost().getTzfwf(), 3), false));
				tempfbs.add(f);
			}
			qr.setResultlist(tempfbs);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recordList";
	}

	// 查看某个人的投标明细
	public String recordListForPerson() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		try {
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// MemberBase m = this.memberBaseService.getMemByUser(u);
			PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + this.getKeyWord() + "%");
				sb.append(" ) ");
			}
			// sb.append(" and ");
			// sb.append(" o.state='1'");
			sb.append(" and ");
			// sb.append(" o.investor.id='"+m.getId()+"'");
			sb.append(" o.id in (" + contractKeyDataService.findContractKeyDatas(u.getUsername()) + ")"); // 根据合同的投资人找投标记录
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			QueryResult<InvestRecord> qr = investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<InvestRecord> lis = qr.getResultlist();
			List<InvestRecord> tempfbs = new ArrayList<InvestRecord>();
			for (InvestRecord f : lis) {
				f.setPreAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getPreAmount(), 3), false));
				f.setNextAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getNextAmount(), 3), false));
				f.setInvestAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getInvestAmount(), 3), false));
				f.getCost().setTzfwf_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getCost().getTzfwf(), 3), false));
				tempfbs.add(f);
			}
			qr.setResultlist(tempfbs);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "recordListForPerson";
	}

	private void doCheck(MemberBase m) {
		this.condition = this.investConditionService.getInvestCondition(m.getMemberLevel());
		double can = this.financingBase.getCurCanInvest();
		// double min = this.condition.getLowestMoney();//约束的最小融资额
		double minFinancing = financingBase.getMaxAmount() / 200;

		double minCondition = condition.getLowestMoney();// 约束的最小融资额

		// (融资额%200)与约束的最小融资额比较取大者；2012-6-11改成取小者 2012-6-21改成取大者
		double min = minFinancing < minCondition ? minCondition : minFinancing;
		if (min < 1000) {
			min = 1000.00;
		}

		// double max =
		// this.financingBase.getMaxAmount()*(this.condition.getHighPercent()/100);//约束的最大融资额
		double accountBlance = m.getUser().getUserAccount().getBalance();// 得到此会员的帐号余额
		// double b1 = this.investRecordService.investHistory(m,
		// financingBase);//当前用户已经投标的金额
		Account account = this.accountService.selectByUserId(m.getUser().getId());
		double b1 = account.getTotalAmount();
		double b2 = this.investRecordService.investHistory2(m, financingBase);// 当前用户对当前融资项目已经投标的金额
		double max = 0d;

		if (Constant.MAX_INVEST.equals("F")) {// 根据融资项目可融资额*会员级别的比例
			max = (this.financingBase.getMaxAmount()) * (condition.getHighPercent() / 100);
			max = max - b2;
			this.invest.setHasMoney(b2);
		} else {
			// 约束的最大融资额（当前用户已经投标的金额+帐号余额）*会员级别的比例
			max = b1 * (condition.getHighPercent() / 100);
			this.invest.setHasMoney(b1);
		}

		min = DoubleUtils.doubleToQian(min);
		max = DoubleUtils.doubleToQian(max);


		if (max > can) {
			max = can;
		}
		if (min > can) {
			min = can;
		}
		if (max <= min) {
			min = max;
		}
		if (max <= 0) {
			min = 0;
			max = 0;
		}
		this.invest.setMinMoney(min);
		this.invest.setMinMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(min, 3), false));
		this.invest.setMaxMoney(max);
		this.invest.setMaxMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(max, 3), false));
		this.account = this.accountService.selectById(this.user.getUserAccount().getId());
		this.account.setDaxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.account.getBalance(), 3), true));
		/*
		 * double b2 = this.investRecordService.investHistory2(member,
		 * financingBase);//当前用户已经投标的金额 if(b2>0){ double chajia =
		 * this.invest.getMaxMoney()-b2; if(chajia<=0){ chajia = 0d; }
		 * this.invest.setMaxMoney(chajia);
		 * this.invest.setMaxMoney_daxie(MoneyFormat
		 * .format(DoubleUtils.doubleCheck2(chajia,3),false));
		 * 
		 * this.invest.setHasMoney(b);
		 * this.invest.setHasMoney_daxie(MoneyFormat.
		 * format(DoubleUtils.doubleCheck2(this.invest.getHasMoney(),3),false)); }
		 * if(this.invest.getMinMoney()>this.invest.getMaxMoney()){
		 * this.invest.setMinMoney(0d); this.invest.setMinMoney_daxie("零元整");
		 * this.invest.setMaxMoney(0d); this.invest.setMaxMoney_daxie("零元整"); }
		 */
	}

	// 投标页面
	public String investUI() {
		try {
			this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (null != this.user) {
				this.member = this.memberBaseService.getMemByUser(this.user);
				if (null != this.member && this.member.getState().equals(MemberBase.STATE_PASSED_AUDIT)) {// 会员审核通过
					doCheck(this.member);
					double b = this.invest.getHasMoney();
					// 会员账户开通,且账户余额大于或等于最小融资金额
					if (null != this.account && this.account.getState() == Account.STATE_OPEN && this.account.getBalance() >= this.invest.getMinMoney()) {
						if (this.invest.getMaxMoney() <= 0) {// 已经投标了融资项目的最大限额,不能再投标了
							this.invest.setCanInvest(false);
							this.invest.setMsg("您已经投标了" + b + "元,已经达到此融资项目的投标上限.");
						} else {
							this.invest.setCanInvest(true);// 当前会员的投标是可行的
							this.invest.setFinancingId(this.financingBase.getId());
						}
					} else {
						this.invest.setMsg("您的账户余额不足,不能进行本次投标操作.");
					}
				} else {
					this.invest.setMsg("您还未通过审核,请联系系统客服.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "investUI";
	}

	// 投标:融资项目id,会员id,融资金额
	public String invest() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (null != this.user) {
				this.member = this.memberBaseService.getMemByUser(this.user);
				if (null != this.member && this.member.getState().equals(MemberBase.STATE_PASSED_AUDIT)) {// 会员审核通过
					doCheck(this.member);
					double b = this.invest.getHasMoney();
					// 会员账户开通,且账户余额大于或等于最小融资金额+投资服务费
					String tzfwf = "";
					double p = 0.0;

					CostBase cb = null;
					CostItem ci = null;
					if (this.financingBase.getCode().startsWith("X")) {
						if (this.member.getMemberLevel().getLevelname().equals("VIP")) {
							tzfwf = "viptzrglf-xyd";
						} else if (this.member.getMemberLevel().getLevelname().equals("高级")) {
							tzfwf = "gjtzrglf-xyd";
						} else if (this.member.getMemberLevel().getLevelname().equals("普通")) {
							tzfwf = "pttzrglf-xyd";
						} else {
							tzfwf = "viptzrglf-xyd";
						}
						cb = this.costCategoryService.selectByHql("from CostBase cb where cb.code='" + tzfwf + "'");
						ci = this.chargingStandardService.selectByHql("from CostItem ci where ci.costBase.id='" + cb.getId() + "'");
						// 收取利息中的百分比
						double bj_all = this.invest.getMoney();// 总本金
						double rate = this.financingBase.getRate();// 利率
						// 还款期限
						int term = financingBase.getBusinessType().getTerm();
						double lx_all = bj_all * ((rate / 100.00) / 12.00 * term);// 总利息
						p = lx_all * (ci.getPercent().doubleValue() / 100);
					} else {
						tzfwf = "tzfwf";
						cb = this.costCategoryService.selectByHql("from CostBase cb where cb.code='" + tzfwf + "'");
						ci = this.chargingStandardService.selectByHql("from CostItem ci where ci.costBase.id='" + cb.getId() + "'");
						p = this.invest.getMoney() * (ci.getPercent().doubleValue() / 100);// 投资服务费
					}

					double jiandiao = this.invest.getMoney() + p;// 要扣除会员的金额
					/*
					 * 可抵扣积分
					 */
					CreditRules creditrules = this.creditRulesService.selectByHql("from CreditRules where enable = true and  to_date('" + now + "','yyyy-MM-dd HH24:mi:ss') between effecttime and expiretime");
					int credit = 0;
					int availablecredit = this.account.getCredit();
					if (creditrules != null && availablecredit > 0) {
						if (this.invest.getMoney() >= creditrules.getRelation_value()) {
							credit = (int) (Math.ceil((this.invest.getMoney() * creditrules.getValue()) * 100) / 100);
							if (credit > availablecredit) {
								credit = availablecredit;
							}
						}
					}
					/*
					 * 可抵扣积分
					 */
					if (null != this.account && this.account.getState() == Account.STATE_OPEN && this.account.getBalance() + credit >= jiandiao) {
						if (this.invest.getMaxMoney() <= 0) {// 已经投标了融资项目的最大限额,不能再投标了
							this.invest.setCanInvest(false);
							this.invest.setMsg("您已经投标了" + b + "元,已经达到此融资项目的投标上限.");
						} else {
							String[] ids = this.investRecordService.invest(member, this.invest.getMoney(), financingBase, credit);
							if (null != ids) {
								this.record = this.investRecordService.selectById(ids[0]);
								this.cost = this.investRecordCostService.selectById(ids[1]);
								invest.setInvestFlag(true);// 投标成功
								invest.setMsg("投标成功");
							}
						}
					} else {
						this.invest.setMsg("您的账户余额不足,不能进行本次投标操作.");
					}
				} else {
					this.invest.setMsg("您还未通过审核,请联系服务中心.");
				}
			}
			this.account = this.accountService.selectById(this.user.getUserAccount().getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 投标记录合同
	 */
	public String agreement_ui() throws Exception {
		try {
			this.user = null;

			Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if ("anonymousUser".equals(o)) {
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				if (username != null && !"".equals(username)) {
					this.user = userService.findUser(username);
				}
			} else {
				this.user = (User) o;
			}

			this.member = this.memberBaseService.getMemByUser(this.user);
			if (user != null) {
				if(this.invest_record_id!=null &&!"".equals(this.invest_record_id)){
					this.record =this.investRecordService.selectById(this.invest_record_id); 
					this.financingBase = record.getFinancingBase();
				}
				if(this.financingBaseId!=null &&!"".equals(this.financingBaseId)){ 
					this.financingBase = this.financingBaseService.selectById(financingBaseId);
				}
				
				this.contract = this.investRecordService.createContract(this.member, investMoney, financingBase);
				if(null!=this.financingBase){
					ServletActionContext.getRequest().setAttribute("interestDay",this.financingBase.getInterestDay());
					if(this.financingBase.getInterestDay()>0){
						ServletActionContext.getRequest().setAttribute("loan_lx_",financingBase.getRate()/Constant.YEAR_DAY); 
						ServletActionContext.getRequest().setAttribute("loan_term_str",this.financingBase.getInterestDay()+"天");
					}else{
						ServletActionContext.getRequest().setAttribute("loan_term_str",this.financingBase.getBusinessType().getTerm()+"个月");
					}
				}
			}

			/*
			 * if(user!=null && this.invest_record_id!=null &&
			 * !"".equals(this.invest_record_id)){ this.record =
			 * this.investRecordService.selectById(this.invest_record_id);
			 * this.member = this.memberBaseService.selectById("from MemberBase
			 * where user.id = ?" , new Object[]{this.user.getId()}); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		ContractTemplate ct = this.contract.getContractTemplate();
		if (ct != null) {
			return ct.getResult_name();
		} else {
			if (this.financingBase.getCode().startsWith("X")) {
				return "agreement_xyd_ui";
			}
			return "agreement_ui";
		}
	}
	
	private String reQuestFromApp;//99表示在桌面应用中显示合同模板
	
	/**
	 * 投标记录合同(桌面应用 调用)
	 */
	public String agreement_ui_ws() throws Exception {
		reQuestFromApp = "99";
		contract = new ContractKeyData();

		contract.setPayment_method(financingBase.getBusinessType().getBranch());
		ContractKeyData tempContract = this.contractKeyDataService.selectByHql("from ContractKeyData o where o.contract_numbers like '" + financingBase.getCode() + "-%'");
		if (null != tempContract) {
			contract.setStart_date(tempContract.getStart_date());
			contract.setEnd_date(tempContract.getEnd_date());
		}
		ServletActionContext.getRequest().setAttribute("interestDay", this.financingBase.getInterestDay());
		// ///////////////////////////////////////////////////////
		// 这里查出来，是否为按日记息
		boolean is_day_calc = "day".equals(financingBase.getBusinessType().getId());
		// 填写到合同里的利率，因为增加了日结包，所以有百分比和万分比的区别
		double rate_input = 0d;

		double rate_day = 0d;

		double rate_year = 0d;

		rate_year = financingBase.getRate() / 100;

		rate_day = rate_year / (double) Constant.YEAR_DAY;

		if (is_day_calc) {
			rate_input = rate_day * 10000;
			rate_input = (double) Math.round(rate_input * (double) 100) / (double) 100;
		} else {
			rate_input = financingBase.getRate();
		}
		// ////////////////////////////////////////////////////////////

		ServletActionContext.getRequest().setAttribute("loan_lx_", rate_input);
		if (this.financingBase.getInterestDay() > 0) {
			ServletActionContext.getRequest().setAttribute("loan_term_str", this.financingBase.getInterestDay() + "天");
			contract.setInterest_rate(financingBase.getRate() / Constant.YEAR_DAY * 100);
		} else {
			ServletActionContext.getRequest().setAttribute("loan_term_str", this.financingBase.getBusinessType().getTerm() + "个月");
		}
		
		Map<String,Object> financingbase_map = this.contractKeyDataService.queryForObject("fc.bzj_bl", "t_financing_cost fc", " fc.financingbase_id = '"+this.financingBase.getId()+"'");
		ServletActionContext.getRequest().setAttribute("interestDay",this.financingBase.getInterestDay());
		if(financingbase_map != null){
			ServletActionContext.getRequest().setAttribute("bzj_bl",financingbase_map.get("bzj_bl"));
		}
		ContractTemplate ct = this.financingBase.getContractTemplate();
		contract.setContractTemplate(ct);
		if (ct != null) {
			return ct.getResult_name() + "_ws";
		} else {
			if (this.financingBase.getCode().startsWith("X")) {
				return "agreement_xyd_ui_ws";
			}
			return "agreement_ui_ws";
		}
	}
	

	/**
	 * 生成合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String c() throws Exception {
		try {

			List<InvestRecord> irs = this.investRecordService.getScrollData().getResultlist();
			for (InvestRecord ir : irs) {
				ContractKeyData ckd = this.investRecordService.createContract(ir.getInvestor(), ir.getInvestAmount(), ir.getFinancingBase());
				ir.setContract(ckd);
				ckd.setInvestor_make_sure(ir.getCreateDate());
				ckd.setFinancier_make_sure(ir.getFinancingBase().getEndDate());
				this.contractKeyDataService.insert(ckd);
				this.investRecordService.update(ir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}


	/**
	 * 投标记录合同查看
	 */
	public String agreement_ui2() throws Exception {
		try {
			this.user = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			String username = request.getParameter("username");
			Object login_user_object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if ("anonymousUser".equals(login_user_object)) {
				if (StringUtils.isNotBlank(username)) {
					this.user = userService.findUser(username);
				}else{
					return null;
				}
			} else if(login_user_object instanceof User){
				this.user = (User) login_user_object;
			}else{
				return null;
			}

			this.member = this.memberBaseService.getMemByUser(this.user);

			if (user != null && this.invest_record_id != null && !"".equals(this.invest_record_id)) {
				this.record = this.investRecordService.selectById(this.invest_record_id);
				MemberBase second_party = this.record.getFinancingBase().getFinancier();
				this.second_party_idcard = second_party.getIdCardNo();
				this.contract = this.record.getContract();
				this.member = this.memberBaseService.selectById("from MemberBase where user.id = ?", new Object[] { this.user.getId() });
				if(null!=this.record){
					Map<String,Object> financingbase_map = this.contractKeyDataService.queryForObject("fc.bzj_bl", "t_financing_cost fc", " fc.financingbase_id = '"+this.record.getFinancingBase().getId()+"'");
					ServletActionContext.getRequest().setAttribute("interestDay",this.record.getFinancingBase().getInterestDay());
					if(financingbase_map != null){
						ServletActionContext.getRequest().setAttribute("bzj_bl",financingbase_map.get("bzj_bl"));
					}
					if(this.record.getFinancingBase().getInterestDay()>0){
						ServletActionContext.getRequest().setAttribute("loan_lx_",this.record.getFinancingBase().getRate()/Constant.YEAR_DAY);
						ServletActionContext.getRequest().setAttribute("loan_term_str",this.record.getFinancingBase().getInterestDay()+"天");
 					}else{
						ServletActionContext.getRequest().setAttribute("loan_term_str",this.record.getFinancingBase().getBusinessType().getTerm()+"个月");
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		ContractTemplate ct = this.contract.getContractTemplate();
		if (ct != null) {
			return ct.getResult_name();
		} else {
			if (this.record.getFinancingBase().getCode().startsWith("X")) {
				return "agreement_xyd_ui";
			}
			return "agreement_ui";
		}
	}

	/**
	 * 合同查看
	 */
	public String agreement_ui3() throws Exception {
		try {
			this.user = null;
			Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if ("anonymousUser".equals(o)) {
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				if (username != null && !"".equals(username)) {
					this.user = userService.findUser(username);
				}
			} else {
				this.user = (User) o;
			}

			this.member = this.memberBaseService.getMemByUser(this.user);
			if (user != null && this.contract.getId() != null && !"".equals(this.contract.getId())) {
				this.contract = this.contractKeyDataService.selectById(this.contract.getId());
				this.record = this.investRecordService.selectById(this.contract.getInverstrecord_id());
				MemberBase second_party = this.record.getFinancingBase().getFinancier();
				this.second_party_idcard = second_party.getIdCardNo();
				this.member = this.memberBaseService.selectById("from MemberBase where user.id = ?", new Object[] { this.user.getId() });
			}
			
			
			Map<String,Object> financingbase_map = this.contractKeyDataService.queryForObject("fc.bzj_bl", "t_financing_cost fc", " fc.financingbase_id = '"+this.record.getFinancingBase().getId()+"'");
			
			if(null!=this.record){
				ServletActionContext.getRequest().setAttribute("interestDay",this.record.getFinancingBase().getInterestDay());
				if(financingbase_map != null){
					ServletActionContext.getRequest().setAttribute("bzj_bl",financingbase_map.get("bzj_bl"));
				}
				if(this.record.getFinancingBase().getInterestDay()>0){
					ServletActionContext.getRequest().setAttribute("loan_lx_",this.record.getFinancingBase().getRate()/Constant.YEAR_DAY); 
					ServletActionContext.getRequest().setAttribute("loan_term_str",this.record.getFinancingBase().getInterestDay()+"天");
				}else{
					ServletActionContext.getRequest().setAttribute("loan_term_str",this.record.getFinancingBase().getBusinessType().getTerm()+"个月");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ContractTemplate ct = this.contract.getContractTemplate();
		if (ct != null) {
			return ct.getResult_name();
		} else {
			if (this.record.getFinancingBase().getCode().startsWith("X")) {
				return "agreement_xyd_ui";
			}
			return "agreement_ui";
		}
	}

	/**
	 * 确认合同
	 */
	public String agreement() throws Exception {
		try {
			if (this.invest_record_id != null && !"".equals(this.invest_record_id)) {
				this.record = this.investRecordService.selectById(this.invest_record_id);
			}
			if (this.record != null) {
				this.record.getContract().setInvestor_make_sure(new Date());
				this.investRecordService.update(this.record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 发布时间
	 */
	public String print() throws Exception {
		try {
			if (this.id != null && !"".equals(this.id)) {
				this.contract = this.contractKeyDataService.selectById(this.id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * pdf
	 */
	public String pdf() {
		try {
			
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			
			
			request.setCharacterEncoding("utf-8");
			String html = request.getParameter("html");
			String num = request.getParameter("num");
			
			html = URLDecoder.decode(html,"utf-8");
			html = html.toLowerCase().replaceAll("\n", "").replaceAll("\t", "").replaceAll("<a.*?</a>", "").replaceAll("<script.*?</script>", "").replaceAll("<input.*?>", "").replaceAll("<form.*?</form>", "").replaceAll("<br>", "");
			html = html.replaceAll("=(?!['\"])(.*?)(\\s|>)","=\"$1\"$2");
			
			html = html.replaceAll("l2\">", "l2\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			html = html.replaceAll("l4\">", "l4\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			
			html = html.replaceAll(num.toLowerCase(), num);
			
			InputStream htmlfileis = new ByteArrayInputStream(html.getBytes("UTF-8"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			Document document = new Document(PageSize.A4,80,80,50,50);

			PdfWriter writer = PdfWriter.getInstance(document, baos);
			XMLWorkerHelper xmlhelper = XMLWorkerHelper.getInstance(); 
			document.open();
			
			xmlhelper.parseXHtml(writer, document, htmlfileis, null, Charset.forName("UTF-8"),new ChineseFontProvider());
			
			document.close();
			
			PdfReader pdf = new PdfReader(baos.toByteArray());
			PdfStamper stamp = new PdfStamper(pdf, baos);

			stamp.setFormFlattening(true);

			int total = pdf.getNumberOfPages() + 1;
			// 增加内容

			Image image = Image.getInstance(ServletActionContext.getServletContext().getRealPath("/Static/images/tag.png"));
			image.setAbsolutePosition(0, 200);
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.2f);
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
			
			
			
			response.setContentType("text/xml;charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=" + num + ".pdf");
			response.setContentType("application/pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.addHeader("Content-Length", Integer.toString(baos.size()));
			
			ServletOutputStream sos = response.getOutputStream();
			
			baos.writeTo(sos);
			sos.flush();
			sos.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}

	public FinancingBase getFinancingBase() {
		return financingBase;
	}

	@Override
	public void prepare() throws Exception {
		if (this.id == null || "".equals(this.id)) {// 融资项目id
			this.financingBase = new FinancingBase();
		} else {
			this.financingBase = this.financingBaseService.selectById(this.id);
			this.financingBase.setMaxAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getMaxAmount(), 3), false));
			this.financingBase.setCurCanInvest_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getCurCanInvest(), 3), false));
			this.financingBase.setCurrenyAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getCurrenyAmount(), 3), false));

			if (null != this.financingBase.getMemberGuarantee()) {
				memberGuarantee = this.financingBase.getMemberGuarantee();
			}

			if (null == memberGuarantee) {
				memberGuarantee = new MemberGuarantee();
			}

		}

	}

	/**
	 * 投标合同明细列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String contractDetail() throws Exception {
		String disUrl = "contractDetail";
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			int pagesize = 10;// 单页大小
			int recordcount = 0;
			int page = super.getPage();// 当前页数
			int selectby = 0;// 按投标日期、签约日期查询
			float amount = 0f;
			if (req.getParameter("selectby") != null) {
				selectby = Integer.valueOf(req.getParameter("selectby"));
			}

			if (this.getStartDate() == null) {// 开始日期
				this.setStartDate(now);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				calendar.add(Calendar.MONTH, 1);
				this.setEndDate(calendar.getTime());// 开始日期为空，设置为当前，把结束日期设置为一个月后
			} else if (this.getEndDate() == null) {// 结束日期
				this.setEndDate(now);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				calendar.add(Calendar.MONTH, -1);
				this.setStartDate(calendar.getTime());
			}
			String fcode = req.getParameter("fcode");// 项目编号
			String iorusername = req.getParameter("iorusername");// 投标人帐号

			if (page == 0) page = 1;
			StringBuilder sql_sub = new StringBuilder();
			StringBuilder sql_list = new StringBuilder(" select t.* from V_INVESTRECORD t ");
			StringBuilder sql_count = new StringBuilder(" select count(*) from V_INVESTRECORD t ");
			StringBuilder sql_sum = new StringBuilder(" select sum(t.investamount) from V_INVESTRECORD t ");

			//
			switch (selectby) {
			case 0:
				sql_sub.append(" where t.investdate between to_date('").append(sdf.format(this.getStartDate())).append("','yyyy-MM-dd') and to_date('").append(sdf.format(this.getEndDate())).append("','yyyy-MM-dd') ");
				break;
			case 1:
				sql_sub.append(" where t.financier_make_sure_date between to_date('").append(sdf.format(this.getStartDate())).append("','yyyy-MM-dd') and to_date('").append(sdf.format(this.getEndDate())).append("','yyyy-MM-dd') ");
			}

			if (fcode != null && !"".equals(fcode)) {
				sql_sub.append(" and t.financbasecode like '").append(fcode.toUpperCase()).append("%' ");
			}
			if (iorusername != null && !"".equals(iorusername)) {
				sql_sub.append(" and t.investorname like '").append(iorusername).append("%' ");
			}
			amount = this.investRecordService.selectsum(sql_sum.append(sql_sub).toString());
			recordcount = this.investRecordService.selectcount(sql_count.append(sql_sub).toString());

			sql_list.append(sql_sub);

			ArrayList<LinkedHashMap<String, Object>> result = this.investRecordService.selectListWithJDBC(sql_list.toString());

			req.setAttribute("result", result);
			req.setAttribute("fcode", fcode);
			req.setAttribute("iorusername", iorusername);
			req.setAttribute("selectby", selectby);
			req.setAttribute("recordcount", recordcount);
			req.setAttribute("amount", amount);

			if ("1".equals(excelFlag)) {// 导出功能
				disUrl = "contractDetail_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	public void setCondition(InvestCondition condition) {
		this.condition = condition;
	}

	public InvestCondition getCondition() {
		return condition;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setInvest(InvestVO invest) {
		this.invest = invest;
	}

	public InvestVO getInvest() {
		return invest;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setRecord(InvestRecord record) {
		this.record = record;
	}

	public InvestRecord getRecord() {
		return record;
	}

	public void setCost(InvestRecordCost cost) {
		this.cost = cost;
	}

	public InvestRecordCost getCost() {
		return cost;
	}

	public void setInvest_record_id(String invest_record_id) {
		this.invest_record_id = invest_record_id;
	}

	public String getInvest_record_id() {
		return invest_record_id;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}

	public double getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(double investMoney) {
		this.investMoney = investMoney;
	}

	public ContractKeyData getContract() {
		return contract;
	}

	public void setContract(ContractKeyData contract) {
		this.contract = contract;
	}

	public String getFinancingBaseId() {
		return financingBaseId;
	}

	public void setFinancingBaseId(String financingBaseId) {
		this.financingBaseId = financingBaseId;
	}

	public MemberGuarantee getMemberGuarantee() {
		return memberGuarantee;
	}

	public void setMemberGuarantee(MemberGuarantee memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}

	public void setPage(String page) {
		if (page != null && !"".equals(page)) {
			int page_ = Integer.valueOf(page);
			this.setPage(page_);
		}
	}

	public String getSecond_party_idcard() {
		return second_party_idcard;
	}

	public void setSecond_party_idcard(String second_party_idcard) {
		this.second_party_idcard = second_party_idcard;
	}

	public String getExcelFlag() {
		return excelFlag;
	}

	public void setExcelFlag(String excelFlag) {
		this.excelFlag = excelFlag;
	}

	public String getReQuestFromApp() {
		return reQuestFromApp;
	}

	public void setReQuestFromApp(String reQuestFromApp) {
		this.reQuestFromApp = reQuestFromApp;
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

}
