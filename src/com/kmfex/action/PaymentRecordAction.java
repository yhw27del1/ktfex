package com.kmfex.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.exceptions.PaymentRecordChangedException;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberBase;
import com.kmfex.model.PayMGroup;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.vo.FinancingBaseJhVo;
import com.kmfex.zhaiquan.model.Buying;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.SellingService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.core.vo.PageForProcedureVO;
import com.wisdoor.struts.BaseAction;

/**
 * 还款记录
 * 
 * @author 
 * 
 * <pre>
 * 2012-08-27  修改此文件，以实现修改“电子合同汇总”页面中内容及样式
 * 2012-08-28  修改此文件，还款明细 按期汇总
 * 2013-07-16  增加4个新方法list_zdye(),list_rzhkqkcx(),list_jqhktx(),list_yqhktx()
 * 2013-08-07  逾期天数计算错误问题，逾期记录提醒里增加当月应还本金，修改方法list_yqhktx()
 * 2013-09-11  融资方还款计划list_rzhkjh()
 * </pre>
 */
@Controller
@Scope("prototype")
public class PaymentRecordAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7293182057404814926L;
	@Resource transient private PaymentRecordService paymentRecordService;
	@Resource transient private FinancingBaseService financingBaseService;
	@Resource transient private InvestRecordService investRecordService;
	@Resource transient private AccountDealService accountDealService;
	@Resource transient private MemberBaseService memberBaseService;
	@Resource transient private UserService userService;
	@Resource transient private OrgService orgService;


	private String id;
	private String pu;
	private String[] ids;
	private PaymentRecord paymentRecord;
	private int state = 1;// 状态 0未还款 1已还款 2提前还款 3逾期还款
	private int approve = 0;
	private String fbcode, ivcode;

	private String remark;
	// 返回给webservice的页面参数
	private String userName;
	private String userType;
	private String qkeyWord = "";
	private String org_code;

	private String bjStr = "=";

	private int day = 5;

	private String selectby = "yhdate";

	private String fxbzstate = "12";
	private String createrOrgStr;
	
	// 导出功能
	private String excelFlag;
	/**
	 * 实际还款金额
	 */
	private double paid_debt;

	private double zhglf_fj;
	private double dbf_fj;

	private double count1 = 0;
	private double count2 = 0;
	private double count3 = 0;
	private double count4 = 0;
	private int payOver = 9;// 还清状态：0未还清，1已还清，9全部
	
	private int fstate = 0;
	
	
	//批处理号，在批量还款打印时用来查询
	private String batch_no;
	/* 
	 * 融资方--还款计划 
	*/
	public String list_rzhkjh() throws Exception {
		
		this.checkDate();
		String disUrl = "list_rzhkjh";
		try {
			Object user_object = (Object) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = null;
			if (user_object != null) {
				user = (User) user_object;
			}else{
				return disUrl;
			} 
			
			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.financierid,o.financiername,o.frealname,o.financbasecode,o.fshortname,o.currenyamount,o.yhdate,o.emobile,dbhsname,o.qs,o.returntimes,o.maxamount,o.contracttemplateId,o.rate,o.qianyuedate,o.overdue_days,to_date(to_char(o.shdate,'YYYY-MM-DD'),'YYYY-MM-DD')  ";
			sb.append(sqlGroup).append(" shdate");
			sb.append("  ,sum(o.yhbj) as totalbj ,sum(o.yhlx) as totallx ,sum(o.rzfwf) as totalrzfwf ,sum(o.dbf) as totaldbf ,sum(o.fxglf) as totalfxglf ,sum(o.bzj) as totalbzj");
			sb.append("  from V_PAYMENTRECORD o ");
			
			sb.append(" where 1=1  ");  
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if (9 != this.fstate) {
				if(5 == this.fstate ){
					sb.append(" and o.state = '0' and  o.yhdate>to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
				}else{
					sb.append(" and o.state = '" + this.fstate + "' ");
				}
             } 

			
			sb.append("   and  o.yhdate " + bjStr + " to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sb.append("   and  o.financierid='" + this.memberBaseService.getMemByUser(user).getId()+ "' ");
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sb.append(" and ( o.financbasecode like '" + this.fbcode.toUpperCase().trim() + "%' or o.fshortname like '%" + this.fbcode.trim() + "%')");
			}
			if (this.qkeyWord != null && !"".equals(this.qkeyWord)) {
				sb.append(" and ( o.financiername like '" + this.qkeyWord.trim() + "%' or o.frealname like '%" + this.qkeyWord.trim() + "%')");
			}
			sb.append(" group by ").append(sqlGroup);
			sb.append(" order by  o.qs,o.yhdate asc");
			ArrayList<LinkedHashMap<String, Object>> dataList = investRecordService.selectListWithJDBC(sb.toString());
			List<FinancingBaseJhVo> jhVoS = new ArrayList<FinancingBaseJhVo>(); 
			for (int i = 0; i < dataList.size(); i++) {
				
				String mobile = null;
				if(dataList.get(i).get("emobile") != null && StringUtils.isNotBlank(dataList.get(i).get("emobile").toString())){
					mobile = dataList.get(i).get("emobile").toString();
				}
				
				FinancingBaseJhVo vo = new FinancingBaseJhVo();
				vo.setFinancierid(dataList.get(i).get("financierid").toString());
				vo.setFinanciername(dataList.get(i).get("financiername").toString());
				vo.setFrealname(dataList.get(i).get("frealname").toString());
				vo.setFinancbasecode(dataList.get(i).get("financbasecode").toString());
				vo.setFshortname(dataList.get(i).get("fshortname").toString());
				vo.setEmobile(mobile);
				vo.setDbhsname(dataList.get(i).get("dbhsname").toString());
				vo.setQs(dataList.get(i).get("qs").toString());
				vo.setReturntimes(dataList.get(i).get("returntimes").toString());
				vo.setContracttemplateId(dataList.get(i).get("contracttemplateid").toString());  

				vo.setYhdate(format.parse(dataList.get(i).get("yhdate").toString()));
				vo.setQianyuedate(format.parse(dataList.get(i).get("qianyuedate").toString())); 
				
				if(null!=dataList.get(i).get("shdate")){
					vo.setShdate(format.parse(dataList.get(i).get("shdate").toString()));
					
				}else{
					vo.setOverdue_days(DateUtils.getBetween(new Date(),vo.getYhdate())+"");
				}
				
				vo.setOverdue_days(dataList.get(i).get("overdue_days").toString());
				
				/*if("0".equals(dataList.get(i).get("overdue_days").toString())) {	
					System.out.println(format.format(vo.getYhdate())+"--->"+format.format(new Date())+"--->"+DateUtils.getBetween(new Date(),vo.getYhdate()));
					vo.setOverdue_days(DateUtils.getBetween(new Date(),vo.getYhdate())+"");
				} */ 
				if(Integer.parseInt(vo.getOverdue_days())<0){
					vo.setOverdue_days("0");
				}
				vo.setCurrenyamount(Double.valueOf(dataList.get(i).get("currenyamount").toString()));
				vo.setMaxamount(Double.valueOf(dataList.get(i).get("maxamount").toString()));
				vo.setRate(Double.valueOf(dataList.get(i).get("rate").toString()));
				vo.setTotalbj(Double.valueOf(dataList.get(i).get("totalbj").toString()));
				vo.setTotallx(Double.valueOf(dataList.get(i).get("totallx").toString()));
				vo.setTotalrzfwf(Double.valueOf(dataList.get(i).get("totalrzfwf").toString()));
				vo.setTotaldbf(Double.valueOf(dataList.get(i).get("totaldbf").toString()));
				vo.setTotalfxglf(Double.valueOf(dataList.get(i).get("totalfxglf").toString()));
				vo.setTotalbzj(Double.valueOf(dataList.get(i).get("totalbzj").toString())); 
                vo.setTotal(vo.getTotalbj()+vo.getTotallx()+vo.getTotalrzfwf()+vo.getTotaldbf()+vo.getTotalfxglf()+vo.getTotalbzj());
				jhVoS.add(vo); 
				
			}	
			
			if ("1".equals(excelFlag)) {// 导出功能
				disUrl = "list_rzhkjh_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				
			}
			ServletActionContext.getRequest().setAttribute("dataList", jhVoS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}
	
//在菜单中没有引用该方法的地址
//	/**
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public String list_0() throws Exception {
//		try {
//			StringBuffer sql = new StringBuffer();
//
//			// 时间过滤，只显示当前月内，未处理项
//			Calendar month_start = Calendar.getInstance();
//			Calendar month_end = Calendar.getInstance();
//			Calendar now = Calendar.getInstance();
//			month_start.set(Calendar.DATE, 1);
//			month_end.set(Calendar.DATE, 1);
//			month_end.roll(Calendar.DATE, -1);
//			now.set(Calendar.HOUR_OF_DAY, 0);
//			now.set(Calendar.MINUTE, 0);
//			now.set(Calendar.SECOND, 0);
//
//			// sql.append(" live = true and state = 0 ");
//			// sql.append("and predict_repayment_date >=
//			// to_date('"+formatter.format(month_start.getTime())+"','yyyy-MM-dd')
//			// and predict_repayment_date <=
//			// to_date('"+formatter.format(month_end.getTime())+"','yyyy-MM-dd')");
//
//			sql.append(" state = '7' and terminal = false ");
//			// 过滤项目编号
//			if (this.fbcode != null && !"".equals(this.fbcode)) {
//				sql.append(" and code like '" + this.fbcode.toUpperCase().trim() + "%'");
//			}
//			sql.append(" order by code desc ");
//			// 过滤投标方帐号
//			// if (this.ivcode != null && !"".equals(this.ivcode)) {
//			// sql.append(" and investRecord.investor.user.username like '"
//			// + this.ivcode.toUpperCase().trim() + "%'");
//			// }
//
//			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
//
//			QueryResult<FinancingBase> fbs = this.financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), new ArrayList<String>());
//
//			for (FinancingBase fb : fbs.getResultlist()) {
//				LinkedHashMap<PayMGroup, List<PaymentRecord>> result = new LinkedHashMap<PayMGroup, List<PaymentRecord>>();
//				List<PaymentRecord> prs = this.paymentRecordService.getScrollDataCommon(" from PaymentRecord where investRecord.financingBase.id = ? order by predict_repayment_date ", fb.getId());
//				/*
//				 * 将还款记录按还款时间进行分组 方面页面显示
//				 */
//				for (PaymentRecord pr : prs) {
//					Date time_group = pr.getPredict_repayment_date();
//					int num_group = pr.getGroup();
//					PayMGroup group = new PayMGroup(time_group, num_group);
//					Set<PayMGroup> sets = result.keySet();
//					boolean has = false;
//					for (PayMGroup k : sets) {
//						if (k.getDate().equals(time_group) && k.getGroup() == num_group) {
//							result.get(k).add(pr);
//							has = true;
//						}
//					}
//					if (!has) {
//						List<PaymentRecord> temp_list = new ArrayList<PaymentRecord>();
//						temp_list.add(pr);
//						result.put(group, temp_list);
//					}
//
//					/**
//					 * 计算扣除费用 现在有 兴易贷费用--综合管理费 兴易贷费用--担保费 两项费用
//					 * 
//					 * 公式为 总计费用 / 投标人数 / 货款期限
//					 */
//					double fee1 = 0;
//					double fee2 = 0;
//					if (fb.getCode().startsWith("X")) {
//						CostItem fee2Item = chargingStandardService.findCostItem("zhglf-xyd", "R");
//						CostItem fee3Item = chargingStandardService.findCostItem("dbf-xyd", "R");
//						fee1 = pr.getInvestRecord().getInvestAmount() * fee2Item.getPercent() / 100 * fb.getBusinessType().getTerm() / fb.getBusinessType().getReturnTimes();
//						fee2 = pr.getInvestRecord().getInvestAmount() * fee3Item.getPercent() / 100 * fb.getBusinessType().getTerm() / fb.getBusinessType().getReturnTimes();
//						pr.setFee_1(fee1);
//						pr.setFee_2(fee2);
//					}
//
//				}
//				/**
//				 * 有投标记录，才加入
//				 */
//				if (result.size() > 0) fb.setPaymentrecords(result);
//				FinancingCost fc = this.financingCostService.selectById(" from FinancingCost where financingBase.id = ? ", fb.getId());
//				if (fc != null) {
//					fb.setFc(fc);
//				}
//			}
//
//			pageView.setQueryResult(fbs);
//			ServletActionContext.getRequest().setAttribute("pageView", pageView);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "list";
//	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_die() throws Exception {
		state = 1;
		String disUrl = "list_die";
		PageView<PaymentRecord> pageView = null;
		try {
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state = 1 ");

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and investRecord.investor.user.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append("and o.actually_repayment_date between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");

			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("predict_repayment_date", "desc");

			QueryResult<PaymentRecord> qr = null;
			if ("1".equals(excelFlag)) {// 导出功能
				pageView = new PageView<PaymentRecord>(9999999, getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
				// 汇总
				count1 = 0;
				for (PaymentRecord o : qr.getResultlist()) {
					count1 += o.getInvestRecord().getInvestAmount();
				}
				disUrl = "list_die_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
			}

			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	private String bank = "华夏银行";// 默认为华夏银行

	public String list_qianyue() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);

			String header1 = "select to_char(p.actually_repayment_date,'yyyy-MM-dd HH24:mi:ss') as actdate,p.xybj,p.xylx,p.penal,p.succession,t.returntimes,t.term,u.username,u.realname,b.caption,u.accountno,to_char(u.signdate,'yyyy-MM-dd HH24:mi:ss') as signdate,f.code,f.interestday,(select fu.username from t_member_base fm,sys_user fu where fm.user_id=fu.id and f.financier_id=fm.id) as f_username,(select fu.realname from t_member_base fm,sys_user fu where fm.user_id=fu.id and f.financier_id=fm.id) as f_realname,(select fm.bankaccount from t_member_base fm,sys_user fu where fm.user_id=fu.id and f.financier_id=fm.id) as f_bankaccount ";
			String header2 = "select nvl(sum(p.shbj+p.shlx+p.penal),0) as sum,count(p.id) as count ";
			String where = " from t_payment_record p,sys_user u,t_member_base m,t_banklibrary b,t_invest_record r,t_financing_base f,t_business_type t where m.user_id=u.id and m.bank_id=b.id and p.beneficiary_id=u.id and p.investrecord_id=r.id and r.financingbase_id=f.id and f.businesstype_id=t.id and u.flag='2' and p.state!='0' ";

			where += " and ( u.signdate <= to_date('" + format.format(DateUtils.getAfter(this.getStartDate(), 1)) + "','yyyy-MM-dd') ";
			where += " or to_char(u.signdate,'yyyy-MM-dd HH24:mi:ss') <= to_char(p.actually_repayment_date,'yyyy-MM-dd HH24:mi:ss') ) ";

			if (null != this.bank && !"".equals(this.bank)) {
				where += " and b.caption='" + this.bank + "' ";
			}
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				where += " and f.code like '" + this.fbcode.toUpperCase().trim() + "%'";
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				where += " and u.username like '%" + this.ivcode.toUpperCase().trim() + "%'";
			}
			where += " and p.actually_repayment_date >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ";
			where += " and p.actually_repayment_date <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ";
			where += "order by p.actually_repayment_date desc";
			ArrayList<LinkedHashMap<String, Object>> sums = this.paymentRecordService.selectListWithJDBC(header2 + where);
			Double sum = 0.0;
			Integer count = 0;
			if (null != sums) {
				LinkedHashMap<String, Object> s = sums.get(0);
				sum = Double.parseDouble(s.get("sum").toString());
				count = Integer.parseInt(s.get("count").toString());
			}

			ArrayList<LinkedHashMap<String, Object>> list = this.paymentRecordService.selectListWithJDBC(header1 + where);
			ServletActionContext.getRequest().setAttribute("sum", sum);
			ServletActionContext.getRequest().setAttribute("count", count);
			ServletActionContext.getRequest().setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_qianyue";
	}

	public String list_qianyue_invest() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		try {
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state = 2 ");// state=2表示已签约
			sql.append(" and investor.user.flag='2' ");

			sql.append(" and ");
			sql.append(" ( investor.user.signDate <= to_date('" + format.format(DateUtils.getAfter(this.getStartDate(), 1)) + "','yyyy-MM-dd') ");

			sql.append(" or to_char(investor.user.signDate,'yyyy-MM-dd HH24:mi:ss') <= to_char(financingBase.qianyueDate,'yyyy-MM-dd HH24:mi:ss') )");

			if (null != this.bank && !"".equals(this.bank)) {
				sql.append(" and investor.banklib.caption='" + this.bank + "' ");
			}
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and investor.user.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append(" and ");
			sql.append(" financingBase.qianyueDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sql.append(" and ");
			sql.append(" financingBase.qianyueDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			sql.append(" order by financingBase.qianyueDate desc");
			String hql = "select sum(investAmount),count(id) from InvestRecord where " + sql.toString();
			List pd = investRecordService.getCommonListData(hql);
			Double sum = 0.0;
			Long count = 0L;
			if (null != pd) {
				Object[] s = (Object[]) pd.get(0);
				sum = (Double) s[0];
				count = (Long) s[1];
			}
			String detail = "from InvestRecord where "+sql.toString();
			ServletActionContext.getRequest().setAttribute("pageView", investRecordService.getCommonListData(detail));
			ServletActionContext.getRequest().setAttribute("sum", sum);
			ServletActionContext.getRequest().setAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_qianyue_invest";
	}

	@Resource
	private transient SellingService sellingService;
	@Resource
	private transient BuyingService buyingService;

	public String list_qianyue_buy() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		try {
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state='1' and buyer.flag='2' ");

			sql.append(" and ");
			sql.append(" ( buyer.signDate <= to_date('" + format.format(DateUtils.getAfter(this.getStartDate(), 1)) + "','yyyy-MM-dd') ");

			sql.append(" or to_char(buyer.signDate,'yyyy-MM-dd HH24:mi:ss') <= to_char(createDate,'yyyy-MM-dd HH24:mi:ss') )");

			// if (null != this.bank && !"".equals(this.bank)) {
			// sql.append(" and investRecord.investor.banklib.caption='" +
			// this.bank + "' ");
			// }

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and buyer.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append(" and ");
			sql.append(" createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sql.append(" and ");
			sql.append(" createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			PageView<Buying> pageView = new PageView<Buying>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			String hql = "select sum(realAmount),count(id) from Buying where " + sql.toString();
			List pd = buyingService.getCommonListData(hql);
			Double sum = 0.0;
			Long count = 0L;
			if (null != pd) {
				Object[] s = (Object[]) pd.get(0);
				if (null != s[0]) {
					sum = (Double) s[0];
				}
				count = (Long) s[1];
			}
			pageView.setQueryResult(buyingService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			ServletActionContext.getRequest().setAttribute("sum", sum);
			ServletActionContext.getRequest().setAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_qianyue_buy";
	}

	public String list_qianyue_sell() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		try {
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state='1' and seller.flag='2' ");

			sql.append(" and ");

			sql.append(" to_char(seller.signDate,'yyyy-MM-dd HH24:mi:ss') <= to_char(createDate,'yyyy-MM-dd HH24:mi:ss') ");

			// if (null != this.bank && !"".equals(this.bank)) {
			// sql.append(" and investRecord.investor.banklib.caption='" +
			// this.bank + "' ");
			// }

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and seller.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append(" and ");
			sql.append(" createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sql.append(" and ");
			sql.append(" createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			PageView<Selling> pageView = new PageView<Selling>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			String hql = "select sum(realAmount),count(id) from Selling where " + sql.toString();
			List pd = sellingService.getCommonListData(hql);
			Double sum = 0.0;
			Long count = 0L;
			if (null != pd) {
				Object[] s = (Object[]) pd.get(0);
				if (null != s[0]) {
					sum = (Double) s[0];
				}
				count = (Long) s[1];
			}
			pageView.setQueryResult(sellingService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby));
			for (Selling o : pageView.getRecords()) {
				MemberBase m = this.memberBaseService.getMemByUser(o.getSeller());
				o.setId(m.getBanklib().getCaption());
			}
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			ServletActionContext.getRequest().setAttribute("sum", sum);
			ServletActionContext.getRequest().setAttribute("count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_qianyue_sell";
	}

	/**
	 * 待还款的还款记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_wait_date() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		Object user_object = (Object) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = null;
		if (user_object != null) {
			user = (User) user_object;
		}else {
			return null;
		}
		String disUrl = "list_wait_date";
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" state = 0 ");
			sb.append(" and ");
			sb.append(" o.predict_repayment_date >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			// sb.append(" o.predict_repayment_date <= to_date('" +
			// format.format(endDateNext) + "','yyyy-MM-dd')");
			sb.append(" o.predict_repayment_date < to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sb.append(" and o.investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}

			// 根据用户的不同所属机构，控制显示记录的范围
			/*
			 * 1、取得当前用户的所属机构 2、取得当前用户的机构的子机构 3、取得在该机构范围内的融资方的融资项目
			 */
			HashMap<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
			inParamList.put(1, user.getOrg().getId());
			HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
			outParameter.put(2, Types.VARCHAR);
			HashMap<Integer, Object> results = this.paymentRecordService.callProcedureForParameters("P_FINANCE_PAYMENTRECORD.SELECTALLCHILDREN", inParamList, outParameter);
			String ids = results.get(2).toString();
			if (ids != null && !"".equals(ids)) {
				sb.append(" and o.investRecord.financingBase.financier.orgNo in (").append(ids).append(") ");
			}
			//

			sb.append(" order by o.predict_repayment_date asc,o.investRecord.financingBase.code desc ");
			PageView<PaymentRecord> pageView = null;
			QueryResult<PaymentRecord> ps = null;
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			if ("1".equals(excelFlag)) {// 导出功能
				pageView = new PageView<PaymentRecord>(9999999, getPage());
				ps = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), null, orderby);
				// 汇总
				count1 = 0;
				count2 = 0;
				count3 = 0;
				count4 = 0;
				for (PaymentRecord o : ps.getResultlist()) {
					count1 += o.getInvestRecord().getInvestAmount();
					count2 += o.getXybj() + o.getXylx();
					count3 += o.getXybj();
					count4 += o.getXylx();
				}
				disUrl = "list_wait_date_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
				ps = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), null, orderby);
			}

			// orderby.put("predict_repayment_date", "asc");

			pageView.setQueryResult(ps);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 在贷余额统计
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_zdye() throws Exception {

		this.checkDate();
		String disUrl = "list_zdye";
		try {

			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.financierid,o.financiername,o.frealname,o.financbasecode,o.fshortname,o.emobile,o.currenyamount,o.dbhsname,o.currenyamount,o.dbhsname,o.showcoding,o.createuseruame ";
			sb.append(sqlGroup);
			sb.append("  ,sum(o.yihuanbenjin) as totalbj,sum(o.yuqiweihuan) as yuqiweihuan,sum(o.weihuan) as weihuan ");
			sb.append("  from V_FINANCE_ZDYE o ");
			sb.append("  where ");

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sb.append(" o.qianyuedate < to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");

			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sb.append(" and ( o.financbasecode like '" + this.fbcode.toUpperCase().trim() + "%' or o.fshortname like '%" + this.fbcode.trim() + "%')");
			}
			if (this.qkeyWord != null && !"".equals(this.qkeyWord)) {
				sb.append(" and ( o.financiername like '" + this.qkeyWord.trim() + "%' or o.frealname like '%" + this.qkeyWord.trim() + "%')");
			}
			if (this.createrOrgStr != null && !"".equals(this.createrOrgStr)) {
				sb.append(" and ( o.showcoding like '" + this.createrOrgStr.trim() + "%' or o.createuseruame like '%" + this.createrOrgStr.trim() + "%')");
			}
			sb.append(" group by ").append(sqlGroup);
			//System.out.println(sb.toString());
			ArrayList<LinkedHashMap<String, Object>> dataList = investRecordService.selectListWithJDBC(sb.toString());

			if ("1".equals(excelFlag)) {// 导出功能
				disUrl = "list_zdye_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {

			}
			ServletActionContext.getRequest().setAttribute("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 融资还款情况查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_rzhkqkcx() throws Exception {

		this.checkDate();
		String disUrl = "list_rzhkqkcx";
		try {

			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.financierid,o.financierid,o.financiername,o.frealname,o.financbasecode,o.fshortname,o.emobile,o.yhdate,o.shdatestr,o.currenyamount,dbhsname,o.qs,o.returntimes";
			sb.append(sqlGroup);
			sb.append("  ,sum(o.yhbj) as totalbj ");
			sb.append("  from V_PAYMENTRECORD o ");
			sb.append("  where  1=1");

			if (99 != state) {
				sb.append("  and o.state='" + state + "'");
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);

			if ("yhdate".equals(selectby)) {
				sb.append(" and ");
				sb.append(" o.yhdate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
				sb.append(" and ");
				sb.append(" o.yhdate <to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			} else {
				sb.append(" and ");
				sb.append(" o.shdatestr >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
				sb.append(" and ");
				sb.append(" o.shdatestr <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			}

			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sb.append(" and ( o.financbasecode like '" + this.fbcode.toUpperCase().trim() + "%' or o.fshortname like '%" + this.fbcode.trim() + "%')");
			}
			if (this.qkeyWord != null && !"".equals(this.qkeyWord)) {
				sb.append(" and ( o.financiername like '" + this.qkeyWord.trim() + "%' or o.frealname like '%" + this.qkeyWord.trim() + "%')");
			}
			if (this.fxbzstate != null && !"".equals(this.fxbzstate)) {
				sb.append(" and  o.fxbzstate = '" + this.fxbzstate.trim() + "'  ");
			}

			sb.append(" group by ").append(sqlGroup);
			sb.append(" order by  o.yhdate desc");
			ArrayList<LinkedHashMap<String, Object>> dataList = investRecordService.selectListWithJDBC(sb.toString());

			if ("1".equals(excelFlag)) {// 导出功能
				disUrl = "list_rzhkqkcx_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {

			}
			ServletActionContext.getRequest().setAttribute("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 近期还款提醒
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_jqhktx() throws Exception {

		this.checkDate();
		String disUrl = "list_jqhktx";
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		ArrayList<Object> args_list = new ArrayList<Object>();
		
		int page = 1;
		int pagesize = 15;
		
		String page_str = request.getParameter("page");
		String pagesize_str = request.getParameter("rows");
		
		if(StringUtils.isNotBlank(page_str) && StringUtils.isNumeric(page_str)){
			page = Integer.parseInt(page_str);
			if(page <= 0) page = 1;
		}
		
		if(StringUtils.isNotBlank(pagesize_str) && StringUtils.isNumeric(pagesize_str)){
			pagesize = Integer.parseInt(pagesize_str);
		}
		
		
		response.setCharacterEncoding("utf-8");
		
		try {

			StringBuilder fileds = new StringBuilder(" financierid,financiername,frealname,financbasecode,fshortname,currenyamount,yhdate,qianyuedate,dbhsname,qs,returntimes,fbalance,fbankaccount");
			
			StringBuilder where = new StringBuilder(" state='0' and financbaseterminal = 0 ");
			

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			where.append("   and  yhdate " + bjStr + " to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");

			if (this.fbcode != null && !"".equals(this.fbcode)) {
				where.append(" and ( financbasecode like ? or fshortname like ? )");
				args_list.add(this.fbcode.toUpperCase().trim()+"%");
				args_list.add(this.fbcode.trim()+"%");
			}
			if (this.qkeyWord != null && !"".equals(this.qkeyWord)) {
				where.append(" and ( financiername like ? or frealname like ? )");
				args_list.add(this.qkeyWord.trim()+"%");
				args_list.add("%" + this.qkeyWord.trim() + "%");
			}
			
			StringBuilder group = new StringBuilder("group by").append(fileds).append(" order by yhdate desc ");
	
			
			fileds.append(",sum(yhbj+yhlx+yhfee1+yhfee2+yhfee3) as totalyh");
			
			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> dataList = null;

			if ("1".equals(excelFlag)) {// 导出功能
				dataList = investRecordService.queryForList(fileds.toString(), "v_paymentrecord", where.append(group).toString(),args);
				disUrl = "list_jqhktx_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
				ServletActionContext.getRequest().setAttribute("dataList", dataList);
			} else {
				PrintWriter out = response.getWriter();
				JSONArray array = null;
				JSONObject result = new JSONObject();
				JSONArray footerarr = new JSONArray();
				JSONObject footer = new JSONObject();
				disUrl = null;
				dataList = investRecordService.queryForList(fileds.toString(), "v_paymentrecord", where.toString()+group.toString(),args, page, pagesize,true);
				int total = investRecordService.queryForListTotal("financbasecode", "v_paymentrecord", where.toString()+group.toString(),args);
				Map<String,Object> summap = investRecordService.queryForObject("sum(totalyh) totalyh,sum(fbalance) balance,sum(currentamount) currentamount", "( select sum(yhbj+yhlx+yhfee1+yhfee2+yhfee3) totalyh,fbalance,currenyamount currentamount from v_paymentrecord where "+where.toString()+group.toString()+")",null, args);
				if(summap.get("currentamount") != null){
					footer.element("CURRENYAMOUNT", summap.get("currentamount"));
				}else{
					footer.element("CURRENYAMOUNT", 0);
				}
				if(summap.get("balance") != null){
					footer.element("FBALANCE", summap.get("balance"));
				}else{
					footer.element("FBALANCE", 0);
				}
				if(summap.get("totalyh") != null){
					footer.element("TOTALYH", summap.get("totalyh"));
				}else{
					footer.element("TOTALYH", 0);
				}
				footer.element("FINANCBASECODE", "合计");
				array = JSONArray.fromObject(dataList);
				result.element("rows",array);
				result.element("total",total);
				footerarr.add(footer);
				result.element("footer", footerarr);
				out.print(result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 逾期记录提醒
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_yqhktx() throws Exception {

		this.checkDate();
		String disUrl = "list_yqhktx";
		try {

			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.financierid,o.financierid,o.financiername,o.frealname,o.financbasecode,o.fshortname,o.yhdate,o.emobile,o.currenyamount,dbhsname,o.qs,o.returntimes,o.branch,o.qianyuedate ";
			sb.append(sqlGroup);
			sb.append("  ,sum(o.yhbj) as totalbj ");
			sb.append("  from V_PAYMENTRECORD o ");
			sb.append("  where o.state='0' ");

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Date preDate = DateUtils.getAfter(this.getEndDate(), -day);
			this.setStartDate(preDate);

			sb.append("   and  o.yhdate " + bjStr + " to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");

			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sb.append(" and ( o.financbasecode like '" + this.fbcode.toUpperCase().trim() + "%' or o.fshortname like '%" + this.fbcode.trim() + "%')");
			}
			if (this.qkeyWord != null && !"".equals(this.qkeyWord)) {
				sb.append(" and ( o.financiername like '" + this.qkeyWord.trim() + "%' or o.frealname like '%" + this.qkeyWord.trim() + "%')");
			}
			sb.append(" group by ").append(sqlGroup);
			sb.append(" order by  o.yhdate desc");
			ArrayList<LinkedHashMap<String, Object>> dataList = investRecordService.selectListWithJDBC(sb.toString());

			if ("1".equals(excelFlag)) {// 导出功能
				disUrl = "list_yqhktx_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {

			}
			ServletActionContext.getRequest().setAttribute("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public String dieWs() throws Exception {
		try {
			StringBuffer sql = new StringBuffer();

			User user = this.userService.findUser(this.userName);
			if (user == null) return null;
			// 时间过滤，只显示当前月内，未处理项
			Calendar month_start = Calendar.getInstance();
			Calendar month_end = Calendar.getInstance();
			month_start.set(Calendar.DATE, 1);
			month_end.set(Calendar.DATE, 1);
			month_end.roll(Calendar.DATE, -1);
			sql.append(" 1 = 1 ");
			if (9 != this.state) {
				sql.append(" and state = '" + this.state + "' ");
			}
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '%" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			sql.append(" and beneficiary_id = " + user.getId());
			boolean terminal = false;
			if (this.payOver != 9) {
				if (this.payOver == 1) {
					terminal = true;
				}
				sql.append(" and investRecord.financingBase.terminal = " + terminal);
			}
			PageView<PaymentRecord> pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("investRecord.financingBase.code", "asc");
			orderby.put("predict_repayment_date", "asc");
			pageView.setQueryResult(paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dieWs";
	}*/
	
	public String dieWs() throws Exception {
		User user = this.userService.findUser(this.userName);
		if (user == null) return null;
		
		//加入密码验证
		if(!user.getPassword().equals(pu)){
			return "illegalPage2";
		}
		
		ArrayList<Object> args_list = new ArrayList<Object>();
		try {
			String fields=" f.code,f.shortname,f.interestday,b.name,b.term,p.succession,m.ename,p.state,r.investamount,p.predict_repayment_date,p.actually_repayment_date,p.xybj,p.xylx,p.shbj,p.shlx,p.penal,p.remark_ ";
			String tablename=" t_financing_base f,t_business_type b,t_member_base m,sys_user u,t_payment_record p left join t_invest_record r on p.investrecord_id=r.id ";
			StringBuilder sb = new StringBuilder(" r.financingbase_id=f.id and f.businesstype_id=b.id and f.financier_id=m.id and p.beneficiary_id=u.id ");
			sb.append(" and u.username='"+user.getUsername()+"'");
			if (9 != this.state) {
				sb.append(" and p.state = ? ");
				args_list.add(this.state);
			}
			if (StringUtils.isNotBlank(this.fbcode)) {//融资项目过滤
				sb.append(" and instr(f.code,?)> 0 ");
				args_list.add(this.fbcode);
			}
			boolean terminal = false;
			int i = 0;
			if (this.payOver != 9) {
				if (this.payOver == 1) {
					terminal = true;
				}
				if(terminal){
					i = 1;
				}
				sb.append(" and f.terminal = " + i);
			}
			sb.append(" order by f.code asc,p.predict_repayment_date asc ");
			
			Object [] args = args_list.toArray();
			List<Map<String, Object>> results=accountDealService.queryForList(fields, tablename, sb.toString(),args, this.getPage(), this.getShowRecord());
			PageForProcedureVO pageView=new PageForProcedureVO();
			ArrayList<LinkedHashMap<String,Object>> newResult = new ArrayList<LinkedHashMap<String,Object>>();
			for(Map<String, Object> map:results){
				newResult.add((LinkedHashMap<String,Object>)map);  
			}
			pageView.setResult(newResult);
		    int totalrecord = this.paymentRecordService.queryForListTotal("r.id",tablename,sb.toString(),args);
		    int totalpage=totalrecord% this.getShowRecord()==0? totalrecord/ this.getShowRecord() : totalrecord/ this.getShowRecord()+1;//总页数
		    pageView.setTotalpage(totalpage);
		    pageView.setTotalrecord(totalrecord); 
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dieWs";
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String dieWeb() throws Exception {
		try {
			StringBuffer sql = new StringBuffer();
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// 时间过滤，只显示当前月内，未处理项
			Calendar month_start = Calendar.getInstance();
			Calendar month_end = Calendar.getInstance();
			month_start.set(Calendar.DATE, 1);
			month_end.set(Calendar.DATE, 1);
			month_end.roll(Calendar.DATE, -1);
			sql.append(" 1 = 1 ");
			if (9 != this.state) {
				sql.append(" and state = '" + this.state + "' ");
			}
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '%" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			sql.append(" and investRecord.investor.user.username = '" + u.getUsername() + "'");

			PageView<PaymentRecord> pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("investRecord.financingBase.code", "asc");
			orderby.put("predict_repayment_date", "asc");
			pageView.setQueryResult(paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dieWeb";
	}

	/**
	 * 提前还款记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_2() throws Exception {
		state = 2;
		String disUrl = "list_tq";
		try {
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state = 2 ");
			// sql.append("and predict_repayment_date >=
			// to_date('"+formatter.format(month_start.getTime())+"','yyyy-MM-dd')
			// and predict_repayment_date <=
			// to_date('"+formatter.format(month_end.getTime())+"','yyyy-MM-dd')");

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and investRecord.investor.user.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append(" and ");
			sql.append(" o.actually_repayment_date >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sql.append(" and ");
			sql.append(" o.actually_repayment_date <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			PageView<PaymentRecord> pageView = null;
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("investRecord.financingBase.code", "asc");
			orderby.put("actually_repayment_date", "asc");
			QueryResult<PaymentRecord> qr = null;
			if ("1".equals(excelFlag)) {// 导出功能
				pageView = new PageView<PaymentRecord>(9999999, getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
				// 汇总
				count1 = 0;
				for (PaymentRecord o : qr.getResultlist()) {
					count1 += o.getInvestRecord().getInvestAmount();
				}
				disUrl = "list_tq_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
			}

			pageView.setQueryResult(qr);

			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 逾期还款记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_3() throws Exception {
		String disUrl = "list_other";
		state = 3;
		try {
			StringBuffer sql = new StringBuffer();

			// 时间过滤，只显示当前月内，未处理项
			// Calendar month_start = Calendar.getInstance();
			// Calendar month_end = Calendar.getInstance();
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			// month_start.set(Calendar.DATE, 1);
			// month_end.set(Calendar.DATE, 1);
			// month_end.roll(Calendar.DATE, -1);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			sql.append(" state = 3 ");
			// sql.append("and predict_repayment_date >=
			// to_date('"+formatter.format(month_start.getTime())+"','yyyy-MM-dd')
			// and predict_repayment_date <=
			// to_date('"+formatter.format(month_end.getTime())+"','yyyy-MM-dd')");

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and investRecord.investor.user.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			sql.append(" and ");
			sql.append(" o.actually_repayment_date >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
			sql.append(" and ");
			sql.append(" o.actually_repayment_date <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
			PageView<PaymentRecord> pageView = null;
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("actually_repayment_date", "desc");

			QueryResult<PaymentRecord> qr = null;
			if ("1".equals(excelFlag)) {// 导出功能
				pageView = new PageView<PaymentRecord>(9999999, getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
				// 汇总
				count1 = 0;
				for (PaymentRecord o : qr.getResultlist()) {
					count1 += o.getInvestRecord().getInvestAmount();
				}
				disUrl = "list_other_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
				qr = paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby);
			}
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	/**
	 * 待审核还款记录(已停用，启用时，请将括号及其中的内容删除)
	 * 
	 * @return
	 * @throws Exception
	 */

	public String list_approve() throws Exception {
		try {
			StringBuffer sql = new StringBuffer();

			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and investRecord.financingBase.code like '" + this.fbcode.toUpperCase().trim() + "%'");
			}
			// 过滤投标方帐号
			if (this.ivcode != null && !"".equals(this.ivcode)) {
				sql.append(" and investRecord.investor.user.username like '" + this.ivcode.toUpperCase().trim() + "%'");
			}
			PageView<PaymentRecord> pageView = new PageView<PaymentRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("investRecord.financingBase.code", "asc");
			orderby.put("predict_repayment_date", "asc");
			pageView.setQueryResult(paymentRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), null, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_approve";
	}

	/**
	 * 查看详细
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ui";
	}

	/**
	 * 更改状态
	 * 
	 * @return
	 * @throws Exception
	 */
	/**
	 * public String state() throws Exception { try { if (this.id == null ||
	 * "".equals(this.id) || this.state == 0) { return null; } paymentRecord =
	 * this.paymentRecordService.selectById(this.id); if (this.paymentRecord ==
	 * null) return null; this.paymentRecord.setState(this.state); if
	 * (this.extension_period != null) {
	 * 
	 * this.paymentRecord.setExtension_period(this.extension_period);
	 * this.paymentRecord.getInvestRecord().setXyhkr(this.extension_period);
	 * InvestRecord ir=this.paymentRecord.getInvestRecord();
	 * ir.setLastDate(this.extension_period);
	 * this.investRecordService.update(ir); }
	 * this.paymentRecordService.update(this.paymentRecord);
	 * System.out.println("change payment_record state"); } catch (Exception e) {
	 * e.printStackTrace(); } return null; }
	 */

	/**
	 * 审核操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String approve_pro() throws Exception {
		try {
			if (this.id == null || "".equals(this.id) || this.state == 0) {
				return null;
			}
			paymentRecord = this.paymentRecordService.selectById(this.id);
			if (this.paymentRecord == null) return null;

			this.paymentRecord.setApprove(this.approve);
			this.paymentRecord.setLive(false);
			this.paymentRecordService.update(this.paymentRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 自动完成搜索
	 * 
	 * @return
	 * @throws Exception
	 */
	public String autocomplete_fbcode() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			List<FinancingBase> financingBases = this.financingBaseService.getScrollDataCommon("from FinancingBase where state = 6", new String[] {});
			out.write("[");
			int count = 0;
			for (FinancingBase fb : financingBases) {
				if (count != 0) out.write(",");
				out.write("\"" + fb.getCode() + "\"");
				count++;
			}
			out.write("]");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 替换换行符
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String replacespecialchar(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String result = m.replaceAll("");
		return result;
	}

	@Resource
	private transient OpenCloseDealService openCloseDealService;

	/**
	 * 付款
	 * 
	 * @return
	 * @throws Exception
	 */
	public strictfp void die() throws Exception {
		
		User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JSONObject result = new JSONObject();

		String request_parameters = request.getParameter("parameters");
		String string_parameters = replacespecialchar(request_parameters);
		JSONObject parameters = JSONObject.fromObject(string_parameters);

		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		
		
		
		try {
			
			if (state == 3 || state == 4 ||state == 5||state==6) {
				result.element("code", "error");
				result.element("tip", "现在是休市时间,请在开市时间进行操作");
				return;
			}
			
			String request_succession = parameters.get("succession").toString().replace("[", "").replace("]", "");
			String[] temp_succession = request_succession.split(",");
			int[] succession = new int[temp_succession.length];
			for (int x = 0; x < temp_succession.length; x++) {
				succession[x] = Integer.parseInt(temp_succession[x]);
			}
			
			int overdue_days = parameters.getInt("days");
			boolean dbdc = parameters.getBoolean("dbdc");
			String financingbaseid = parameters.getString("financingbaseid");
			
			
			JSONArray array = parameters.getJSONArray("records");
			if(array.size() == 0){
				result.element("code", "error");
				result.element("tip", "数据列为空");
				return;
			}
			
			
			double zongjine = 0d;
			for(int arr = 0; arr < array.size();arr++){
				JSONObject record = array.getJSONObject(arr);
				double benjin = 0d, lixi = 0d, fajin = 0d, rongzifuwufei = 0d, rongzifuwufei_fajin = 0d, danbaofei = 0d, danbaofei_fajin = 0d, fengxianguanlifei = 0d, fengxianguanlifei_fajin = 0d;
				try {benjin = record.getDouble("benjin");} catch (JSONException e) {}
				try {lixi = record.getDouble("lixi");} catch (JSONException e) {}
				try {fajin = record.getDouble("fajin");} catch (JSONException e) {}
				try {rongzifuwufei = record.getDouble("rongzifuwufei");} catch (JSONException e) {}
				try {rongzifuwufei_fajin = record.getDouble("rongzifuwufei_fajin");} catch (JSONException e) {}
				try {danbaofei = record.getDouble("danbaofei");} catch (JSONException e) {}
				try {danbaofei_fajin = record.getDouble("danbaofei_fajin");} catch (JSONException e) {}
				try {fengxianguanlifei = record.getDouble("fengxianguanlifei");} catch (JSONException e) {}
				try {fengxianguanlifei_fajin = record.getDouble("fengxianguanlifei_fajin");} catch (JSONException e) {}
				zongjine += benjin + lixi + fajin + rongzifuwufei + rongzifuwufei_fajin + danbaofei + danbaofei_fajin;
			}
			zongjine = Math.floor(zongjine*100)/100;
			if(zongjine <=0 && overdue_days >=0 ){
				result.element("code", "error");
				result.element("tip", "应还款为“0”");
				return;
			}
			
			FinancingBase fb = this.financingBaseService.selectById(financingbaseid);

			Account r_account = fb.getFinancier().getUser().getUserAccount();
			
			if (null == r_account || r_account.getBalance() < (double) Math.round(zongjine * 100) / 100) {
				result.element("code", "error");
				result.element("tip", "融资方帐户余额不足:before("+zongjine+");after("+((double) Math.round(zongjine * 100) / 100)+")");
				return;
			}

			
			this.paymentRecordService.batchAudit(financingbaseid, succession, overdue_days, dbdc, array,operator);
			result.element("code", "success");
			result.element("tip", "成功");
			result.element("balance", r_account.getBalance());
			
		} catch (SQLException sjone) {
			result.element("code", "error");
			result.element("tip", "数据格式错误："+sjone.getMessage()+";务必联系管理员!");
		} catch (JSONException sjone) {
			result.element("code", "error");
			result.element("tip", "数据格式错误："+sjone.getMessage()+";务必联系管理员!");
		} catch (HibernateOptimisticLockingFailureException sose) {
			sose.printStackTrace();
			result.element("code", "error");
			result.element("tip", "同步锁错误，稍后再试");
		} catch (StaleObjectStateException eose) {
			eose.printStackTrace();
			result.element("code", "error");
			result.element("tip", "同步锁错误，稍后再试");
		} catch (Exception e) {
			e.printStackTrace();
			result.element("code", "error");
			result.element("tip", "未知错误:"+e.getMessage().substring(0,20)+";务必联系管理员!");
		} finally {
			out.print(result.toString());
		}
	}


	/**
	 * 会员管理员查看还款情况列表 只能看，不能进行其它操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list_view() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String[] ids = request.getParameterValues("org_code2");

		//if (ids == null || ids.length == 0) {
		//	ids = new String[] { "530105" };
		//}

		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" state = '7' and o.qianyueDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			// 过滤项目编号
			if (this.fbcode != null && !"".equals(this.fbcode)) {
				sql.append(" and code like '" + this.fbcode.toUpperCase().trim() + "%' ");
			}
			List<String> params = new ArrayList<String>();

			if (StringUtils.isNotBlank(this.qkeyWord)) {
				qkeyWord = qkeyWord.trim();
				sql.append(" and (");
				sql.append("     o.financier.eName like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.financier.pName like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.guarantee.eName like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.guarantee.pName like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.financier.user.realname like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.financier.user.username like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.guarantee.user.realname like ?");
				params.add("%" + qkeyWord + "%");
				sql.append("     or ");
				sql.append("     o.guarantee.user.username like ?");
				params.add("%" + qkeyWord + "%");
				sql.append(" )");
			}
			if (StringUtils.isNotBlank(this.org_code)) {
				sql.append(" and createBy.org.showCoding = ? ");
				params.add(this.org_code);
			}

			sql.append(" order by o.qianyueDate desc");
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			QueryResult<FinancingBase> fbs = this.financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), params);
			pageView.setQueryResult(fbs);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);

			List<Org> orgs = this.orgService.getScrollDataCommon("from Org o where o.type = '1' or o.type = '2' order by o.showCoding ", new String[] {});
			ServletActionContext.getRequest().setAttribute("orgs", orgs);
			ServletActionContext.getRequest().setAttribute("org_code2", ids);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_view";
	}

	// 代还款记录2，新的方式展现还款操作
	public String list_wait() throws Exception {

		return "list_wait";
	}

	/**
	 * 还款预处理(批量) 接收 应还日期 从视图里取数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String paymentrecord_list_standby() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<LinkedHashMap<String, Object>> fbs = null;
		try {
			Date endDateNext = null;
			if (this.getStartDate() == null && StringUtils.isBlank(this.fbcode)) {
				return "paymentrecord_list_standby";
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			endDateNext = this.getEndDate();

			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			OutParameterModel outParameter = new OutParameterModel(9, OracleTypes.CURSOR);

			inParamList.put(1, "yhdate");
			inParamList.put(2, StringUtils.isBlank(this.fbcode) ? null : this.fbcode.toUpperCase().trim());
			inParamList.put(3, this.getStartDate() == null ? null : new java.sql.Date(this.getStartDate().getTime()));
			inParamList.put(4, endDateNext == null ? null : new java.sql.Date(endDateNext.getTime()));
			inParamList.put(5, 0);// approve 为 0 表示待处理。
			inParamList.put(6, null);
			inParamList.put(7, null);
			inParamList.put(8, null);

			fbs = this.paymentRecordService.callProcedureForList("P_FINANCE_PAYMENTRECORD.get_record_for_financingbase", inParamList, outParameter);

			request.setAttribute("records", fbs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "paymentrecord_list_standby";
	}

	/**
	 * 还款预处理
	 * 
	 */
	public void paymentrecord_do_standby() {
		User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject result = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		Date time = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String batch_no = sdf.format(time);
		try {
			out = response.getWriter();
			HttpServletRequest request = ServletActionContext.getRequest();
			String request_parameters = request.getParameter("parameters");
			if (null == request_parameters || "".equals(request_parameters)) return;
			String string_parameters = replacespecialchar(request_parameters);

			JSONObject parameters = JSONObject.fromObject(string_parameters);
			JSONArray array = parameters.getJSONArray("array");
			Map<String, Boolean> list = new HashMap<String, Boolean>();
			for (int x = 0; x < array.size(); x++) {
				String fid = array.getJSONObject(x).getString("fid");
				int qs = array.getJSONObject(x).getInt("qs");
				Date date = formatDate.parse(array.getJSONObject(x).getString("date"));
				boolean success = this.paymentRecordService.changeToPreAudit(fid, qs,date.getTime(), batch_no, operator);
				list.put(fid, success);
			}

			result.element("result", list);
			result.element("batch_no", batch_no);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(result);
	}

	/**
	 * 批量还款待审核列表
	 * 
	 */
	public String paymentrecord_list_preaudit() {
		HttpServletRequest request = ServletActionContext.getRequest();
		List<LinkedHashMap<String, Object>> fbs = null;
		LinkedHashMap<String,List<LinkedHashMap<String, Object>>> results = new LinkedHashMap<String, List<LinkedHashMap<String, Object>>>();
		try {

			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			OutParameterModel outParameter = new OutParameterModel(9, OracleTypes.CURSOR);

			inParamList.put(1, null);
			inParamList.put(2, null);
			inParamList.put(3, null);
			inParamList.put(4, null);
			inParamList.put(5, 1);// approve 为 0 表示待处理。
			inParamList.put(6, null);
			inParamList.put(7, null);
			inParamList.put(8, null);

			fbs = this.paymentRecordService.callProcedureForList("P_FINANCE_PAYMENTRECORD.get_record_for_financingbase", inParamList, outParameter);
			for(LinkedHashMap<String,Object> m : fbs){
				String batch_no = m.get("batch_no").toString();
				if(results.containsKey(batch_no)){
					results.get(batch_no).add(m);
				}else{
					List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
					list.add(m);
					results.put(batch_no, list);
				}
			}
			request.setAttribute("records", results);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "paymentrecord_list_preaudit";
	}

	/**
	 * 批量还款审核通过
	 * 
	 */
	public void paymentrecord_do_audit() {

		JSONObject result = new JSONObject();

		User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			result.element("code", "error");
			result.element("tip", "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			if (!result.isNullObject()) out.print(result.toString());
			return;
		}

		HttpServletRequest request = ServletActionContext.getRequest();
		String request_parameters = request.getParameter("parameters");
		if (null == request_parameters || "".equals(request_parameters)) return;
		String string_parameters = replacespecialchar(request_parameters);

		JSONObject parameters = JSONObject.fromObject(string_parameters);
		JSONArray array = parameters.getJSONArray("array");
		Map<String, Integer> list = new HashMap<String, Integer>();
		for (int x = 0; x < array.size(); x++) {
			
			String fid = array.getJSONObject(x).getString("fid");
			int qs = array.getJSONObject(x).getInt("qs");
			try {
				int code = this.paymentRecordService.batchAudit(fid, qs, operator);
				list.put(fid, code);
			} catch (PaymentRecordChangedException prce) {
				prce.printStackTrace();
				list.put(fid, -3);
			} catch (HibernateOptimisticLockingFailureException sose) {
				sose.printStackTrace();
				list.put(fid, -2);
			} catch (StaleObjectStateException sose) {
				sose.printStackTrace();
				list.put(fid, -2);
			} catch (EngineException e) {
				e.printStackTrace();
				list.put(fid, 0);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				list.put(fid, 0);
			}
		}
		result.element("result", list);
		out.print(result);
	}

	/**
	 * 批量还款审核驳回
	 * 
	 */
	public void paymentrecord_do_unaudit() {
		if(true){
			return;
			}
		JSONObject result = new JSONObject();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String request_parameters = request.getParameter("parameters");
			if (null == request_parameters || "".equals(request_parameters)) return;
			String string_parameters = replacespecialchar(request_parameters);

			JSONObject parameters = JSONObject.fromObject(string_parameters);
			JSONArray array = parameters.getJSONArray("array");
			Map<String, Boolean> list = new HashMap<String, Boolean>();
			for (int x = 0; x < array.size(); x++) {
				String fid = array.getJSONObject(x).getString("fid");
				int qs = array.getJSONObject(x).getInt("qs");
				boolean success = this.paymentRecordService.batchUnaudit(fid, qs);
				list.put(fid, success);
			}

			result.element("result", list);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(result);
	}
	
	
	public String paymentrecord_list_query_print(){
		HttpServletRequest request = ServletActionContext.getRequest();
		List<LinkedHashMap<String, Object>> fbs = null;
		LinkedHashMap<String,List<LinkedHashMap<String, Object>>> results = new LinkedHashMap<String, List<LinkedHashMap<String, Object>>>();
		try {
			Date endDateNext = null;
			if (this.getStartDate() == null && StringUtils.isBlank(this.batch_no)) {
				return "paymentrecord_list_query_print";
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			
			endDateNext = DateUtils.getAfter(this.getEndDate(), 1);

			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			OutParameterModel outParameter = new OutParameterModel(9, OracleTypes.CURSOR);

			inParamList.put(1, "shdate");
			inParamList.put(2, StringUtils.isBlank(this.fbcode) ? null : this.fbcode.toUpperCase().trim());
			inParamList.put(3, this.getStartDate() == null ? null : new java.sql.Date(this.getStartDate().getTime()));
			inParamList.put(4, endDateNext == null ? null : new java.sql.Date(endDateNext.getTime()));
			inParamList.put(5, 2);// approve 为 2 已处理。
			inParamList.put(6, StringUtils.isBlank(this.org_code) ? null : this.org_code.trim());
			inParamList.put(7, 1);
			inParamList.put(8, StringUtils.isBlank(this.batch_no) ? null : this.batch_no.trim());

			fbs = this.paymentRecordService.callProcedureForList("P_FINANCE_PAYMENTRECORD.get_record_for_financingbase", inParamList, outParameter);

			for(LinkedHashMap<String,Object> m : fbs){
				if(m.get("batch_no") == null) continue;
				String batch_no = m.get("batch_no").toString();
				if(results.containsKey(batch_no)){
					results.get(batch_no).add(m);
				}else{
					List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
					list.add(m);
					results.put(batch_no, list);
				}
			}
			request.setAttribute("records", results);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "paymentrecord_list_query_print";
	}
	
	/**
	 * 批量正常还款列表打印
	 * @return
	 */
	public String paymentrecord_list_do_print(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String array_str = request.getParameter("list");
		if (null == array_str || "".equals(array_str)) return null;
		
		
		StringBuffer sql = new StringBuffer();
		List<LinkedHashMap<String, Object>> fbs = null;
		
		
		try{
			JSONObject parameters = JSONObject.fromObject(array_str);
			JSONArray array = parameters.getJSONArray("array");
			for (int x = 0; x < array.size(); x++) {
				String fid = array.getJSONObject(x).getString("fid");
				int qs = array.getJSONObject(x).getInt("qs");
				if(x != 0){
					sql.append(" or ");
				}
				sql.append(" (v_p.financbaseid = '"+fid+"' and v_p.qs = "+qs+") ");
			}
			
			if(sql.length() > 0){
				Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
				OutParameterModel outParameter = new OutParameterModel(2, OracleTypes.CURSOR);
				inParamList.put(1, sql.toString());
				fbs = this.paymentRecordService.callProcedureForList("P_FINANCE_PAYMENTRECORD.paymentrecord_list_do_print", inParamList, outParameter);
				request.setAttribute("records", fbs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "paymentrecord_list_do_print";
	}
	
	
	/**
	 * 批量正常还款详细打印
	 * @return
	 */
	public String paymentrecord_details_do_print(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String array_str = request.getParameter("list");
		HashMap<String,Double[]> benjin_lixi = new LinkedHashMap<String, Double[]>();
		if (null == array_str || "".equals(array_str)) return null;
		List<FinancingBase> fbs = new ArrayList<FinancingBase>();
		
		try{
			JSONObject parameters = JSONObject.fromObject(array_str);
			JSONArray array = parameters.getJSONArray("array");
			for (int x = 0; x < array.size(); x++) {//迭代全部{包ID-还款期次}组合
				String fid = array.getJSONObject(x).getString("fid");
				int qs = array.getJSONObject(x).getInt("qs");
				FinancingBase fb = this.financingBaseService.selectById(fid);
				LinkedHashMap<PayMGroup, List<PaymentRecord>> paymentrecords = new LinkedHashMap<PayMGroup, List<PaymentRecord>>();
				List<PaymentRecord> prs = this.paymentRecordService.queryListBySuccessionFromFinance(fid, qs);
				for(PaymentRecord pr : prs){
					Date date = pr.getPredict_repayment_date();
					int group = pr.getGroup();
					PayMGroup _group = new PayMGroup(date, group);
					Set<PayMGroup> sets = paymentrecords.keySet();
					boolean has = false;
					for (PayMGroup k : sets) {
						if (k.getDate().equals(date) && k.getGroup() == group) {
							paymentrecords.get(k).add(pr);
							has = true;
							break;
						}
					}
					if (!has) {
						List<PaymentRecord> temp_list = new ArrayList<PaymentRecord>();
						temp_list.add(pr);
						paymentrecords.put(_group, temp_list);
					}
					if (pr.getBeneficiary_id() != 0) {
						User beneficiary = this.userService.selectById(pr.getBeneficiary_id());
						pr.setBeneficiary_name(beneficiary.getRealname());
					} else {
						pr.setBeneficiary_name(pr.getInvestRecord().getInvestor().getShowName());
					}
				}
				fb.setPaymentrecords(paymentrecords);
				Map<String, Double> benjinlixi = this.paymentRecordService.getBjLx(fb.getId());
				Double [] bj_lx = new Double[]{benjinlixi.get("yuehuanbenjin"),benjinlixi.get("yuehuanlixi")};
				benjin_lixi.put(fb.getId(), bj_lx);
				fbs.add(fb);
			}
			request.setAttribute("records", fbs);
			request.setAttribute("benjin_lixi", benjin_lixi);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "paymentrecord_details_do_print";
	}
	
	
	/**
	 * 单条还款处理（原还款）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String paymentrecord_list_single() throws Exception {
		SimpleDateFormat format = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		List<Map<String,Object>> list = null;
		StringBuffer wheresql = new StringBuffer();
		StringBuffer fields = new StringBuffer();

		
		ArrayList<Object> args_list = new ArrayList<Object>();
		Date endDateNext = null;
		try {

			wheresql.append(" fb.state = '7' and fb.terminal = 0 ");

			if (this.getStartDate() != null) {
				if (this.getEndDate() == null) {
					this.setEndDate(new Date());
				}
				endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
				format = new SimpleDateFormat("yyyy-MM-dd");
				wheresql.append(" and fb.qianyuedate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			}

			if (StringUtils.isNotBlank(this.fbcode)) {
				wheresql.append(" and fb.code like ? ");
				args_list.add("%"+this.fbcode.toUpperCase().trim()+"%");
			}
			
			wheresql.append(" and fb.financier_id = m.id and m.user_id = u.id and fb.businesstype_id = bt.id and fb.c_t_id = ct.id");
			
			
			wheresql.append(" order by fb.qianyuedate");
			
			
			fields.append("fb.id,")
			.append("fb.qianyuedate,")
			.append("fb.code,")
			.append("fb.currenyamount,")
			.append("fb.interestday,")
			.append("u.username,")
			.append("u.realname,")
			.append("m.bankaccount,")
			.append("bt.term,")
			.append("ct.hksd");
			

			Object [] args = args_list.toArray();
			
			list = this.financingBaseService.queryForList(fields.toString(), "t_financing_base fb,t_member_base m,sys_user u,t_business_type bt,t_contract_template ct ", wheresql.toString(),args);
			request.setAttribute("records", list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "paymentrecord_list_single";
	}
	

	private FinancingBase financingBase;
	private List<PaymentRecord> paymentRecordList;

	public String list_paymentRecord() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();

		try {

			if (StringUtils.isNotBlank(this.id)) {

				Map<String,Object> financingbase = this.financingBaseService.queryForObject("fb.code fb_code,fb.interestday fb_interestday," +
						"fb.rate fb_rate" +
						",fb.shortname fb_shortname" +
						",fb.currenyamount fb_currentamount"+
						",bt.term bt_term" +
						",bt.returnpattern bt_returnpattern" +
						",bt.returntimes bt_returntimes" +
						",ct.zhinajinbili ct_zhinajinbili" +
						",ct.weiyuejinbili ct_weiyuejinbili" +
						",create_org.name_ create_org_name" +
						",financier_member.pname fm_pname" +
						",financier_member.ename fm_ename" +
						",financier_member.bankaccount fm_bankaccount" +
						",financier_user.username fu_username ", 
						"t_financing_base fb,t_financing_cost fc,t_business_type bt,t_contract_template ct,sys_user create_user,sys_org create_org,t_member_base financier_member,sys_user financier_user ", 
						"fb.id = '"+this.id+"' and fc.financingbase_id = fb.id and bt.id = fb.businesstype_id and fb.c_t_id = ct.id and create_user.id = fb.createby_id and create_user.org_id = create_org.id and fb.financier_id = financier_member.id and financier_member.user_id = financier_user.id");
				
				int succession = -1;
				int max_group = 0;
				Date yhsj = null;
				String succession_str = request.getParameter("succession");
				if (StringUtils.isNotBlank(succession_str) && StringUtils.isNumeric(succession_str)) {
					succession = Integer.parseInt(succession_str);
				} else {
					Map<String,Object> succession_map = this.paymentRecordService.queryForObject("max(pr.succession) succession", "t_financing_base fb,t_invest_record ir,t_payment_record pr", "fb.id = '"+this.id+"' and fb.id = ir.financingbase_id and ir.id = pr.investrecord_id and pr.state != 0  ");
					if(succession_map!= null && succession_map.get("succession") != null){
						succession = Integer.parseInt(succession_map.get("succession").toString());
					}else{
						succession = 1;
					}
				}
				LinkedHashMap<Integer,List<Map<String,Object>>> result = new LinkedHashMap<Integer,List<Map<String,Object>>>();

				Map<String, Object> xybj_xylx = this.paymentRecordService.queryForObject("sum(pr.xybj) xybj,sum(pr.xylx) xylx", "t_financing_base fb,t_invest_record ir,t_payment_record pr", " fb.id = '" + this.id + "' and fb.id = ir.financingbase_id and ir.id = pr.investrecord_id and pr.succession = 1 and pr.group_ = 0");
				
				if( xybj_xylx != null ){
					financingbase.put("xybj", xybj_xylx.get("xybj"));
					financingbase.put("xylx", xybj_xylx.get("xylx"));
				}
				
				
				Map<String,Object> max_group_map = this.paymentRecordService.queryForObject("max(pr.group_) max_group,max(pr.predict_repayment_date) yhsj", " t_financing_base fb,t_invest_record ir,t_payment_record pr", " fb.id = '" + this.id + "' and fb.id = ir.financingbase_id and ir.id = pr.investrecord_id and pr.succession = " + succession);
				if(null !=  max_group_map){
					if(max_group_map.get("max_group") != null){
						max_group = Integer.parseInt(max_group_map.get("max_group").toString());
					}
					if(max_group_map.get("yhsj") != null){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						yhsj = sdf.parse(max_group_map.get("yhsj").toString());
					}
				}else{
					throw new Exception("参数不正确");
				}
				
				
				
				
				for(int group = max_group; group >= 0 ; group--){
					List<Map<String,Object>> list = this.paymentRecordService.queryForList("*", "t_financing_base fb,t_invest_record ir,t_payment_record pr,sys_user u", "fb.id = '"+this.id+"' and fb.id = ir.financingbase_id and ir.id = pr.investrecord_id and pr.succession = " + succession+" and pr.group_ = "+group+" and pr.beneficiary_id = u.id");
					result.put(group, list);
				}
				
				
				request.setAttribute("financingbase", financingbase);
				request.setAttribute("yhsj", yhsj);
				request.setAttribute("list", result);
				request.setAttribute("succession", succession);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_paymentRecord";
	}

	public String template() {
		try {
			//if(true)
			//return null;
			HttpServletRequest request = ServletActionContext.getRequest();
			if (StringUtils.isNotBlank(this.id)) {
				Map<String,Object> financingbase = this.financingBaseService.queryForObject("fb.*,fc.*,bt.returntimes,ct.zhinajinbili,a.balance_", "t_financing_base fb,t_financing_cost fc , t_business_type bt ,t_contract_template ct ,sys_user u,sys_account a,t_member_base m", "fb.id = '"+this.id+"' and fb.id = fc.financingbase_id and bt.id = fb.businesstype_id and ct.id = fb.c_t_id and fb.financier_id = m.id and m.user_id = u.id and a.accountid_ = u.useraccount_accountid_ ");
				int times = Integer.parseInt(financingbase.get("returntimes").toString());
				
				financingbase.put("times", times);
				
				//this.paymentRecordList = this.paymentRecordService.queryListBySuccessionFromFinance(this.id, times);
				
				StringBuilder fields = new StringBuilder();
				fields.append(" i.id investrecord_id,")
				.append("i.investamount,")
				.append("c.interest_allah,")
				.append("c.principal_allah_m,")
				.append("c.interest_allah_m,")
				.append("c.service_cost_a,")
				.append("c.service_cost_fs,")
				.append("c.dbf,")
				.append("c.dbf_fs,")
				.append("c.riskmanagement_a,")
				.append("c.fxglf_fs,")
				.append("u.realname");
				List<Map<String,Object>> investrecordlist = this.financingBaseService.queryForList(fields.toString(), "t_invest_record i,t_contract_key_data c,sys_user u", " i.financingbase_id = '"+this.id+"' and i.id = c.inverstrecord_id and u.id = (select m.user_id from t_member_base m where m.id = i.investor_id) order by u.username");
				List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> investrecorditem : investrecordlist){
					int dbf_fs = Integer.parseInt(investrecorditem.get("dbf_fs").toString());
					int service_cost_fs = Integer.parseInt(investrecorditem.get("service_cost_fs").toString());
					int fxglf_fs = Integer.parseInt(investrecorditem.get("fxglf_fs").toString());
					Map<String,Object> item = new HashMap<String, Object>();
					item.put("holderrealname", investrecorditem.get("realname"));
					item.put("holderid", investrecorditem.get("investor_id"));
					item.put("investrecord_id", investrecorditem.get("investrecord_id"));
					item.put("investamount", investrecorditem.get("investamount"));
					item.put("transferflag", investrecorditem.get("transferflag"));
					
					 
					//按月收取的担保费
					if(dbf_fs == 1 ){
						item.put("dbf", investrecorditem.get("dbf"));
					}else{
						item.put("dbf", 0);
					}
					//按月收取的担保费
					if(service_cost_fs == 1 ){
						item.put("fwf", investrecorditem.get("service_cost_a"));
					}else{
						item.put("fwf", 0);
					}
					//按月收取的风险管理费
					if(fxglf_fs == 1 ){
						item.put("fxglf", investrecorditem.get("riskmanagement_a"));
					}else{
						item.put("fxglf", 0);
					}
					
					resultlist.add(item);
				}
				
				if(Integer.parseInt(financingbase.get("dbf_tariff").toString())==1 || Integer.parseInt(financingbase.get("fee3_tariff").toString())==1){
					financingbase.put("_dbf", true);
				}else{
					financingbase.put("_dbf", false);
				}
				if(Integer.parseInt(financingbase.get("fxglf_tariff").toString())==1 || Integer.parseInt(financingbase.get("fee1_tariff").toString())==1){
					financingbase.put("_fxglf", true);
				}else{
					financingbase.put("_fxglf", false);
				}
				if(Integer.parseInt(financingbase.get("rzfwf_tariff").toString())==1 || Integer.parseInt(financingbase.get("fee2_tariff").toString())==1){
					financingbase.put("_rzfwf", true);
				}else{
					financingbase.put("_rzfwf", false);
				}
				if(Integer.parseInt(financingbase.get("bzj_tariff").toString())==1){
					financingbase.put("_bzj", true);
				}else{
					financingbase.put("_bzj", false);
				}
				
				
				
				request.setAttribute("paymentrecordlist", resultlist);
				request.setAttribute("financingbase", financingbase);
				return "paymenttemplate";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 取得融资项目已还信息
	 * @throws Exception 
	 */
	public void get_record_info() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String fid = request.getParameter("fid");
		String succession = request.getParameter("succession");
		JSONObject obj = new JSONObject();
		
		List<Map<String,Object>> group = this.paymentRecordService.queryForList(" max(group_) group_", " (select  group_ from v_paymentrecord where  qs = "+succession+" and FINANCBASEID = '"+fid+"' and state != 0 group by FINANCBASEID,group_)", null);
		if(group.get(0).get("group_") != null){
			obj.element("num", group.get(0).get("group_"));
		}else{
			obj.element("num", -1);
		}
		List<Map<String,Object>> yhdate = this.paymentRecordService.queryForList(" yhdate,rownum", " v_paymentrecord ", " qs = "+succession+" and FINANCBASEID = '"+fid+"' and rownum = 1");
		if(yhdate.get(0).get("yhdate") != null){
			obj.element("yhdate", yhdate.get(0).get("yhdate"));
		}else{
			obj.element("yhdate", -1);
		}
		
		List<Map<String,Object>> records = this.paymentRecordService.queryForList("ir.id,pr.xybj,pr.xylx", "t_financing_base fb, t_invest_record ir ,t_payment_record pr", " fb.id = '"+fid+"' and ir.financingbase_id = fb.id and pr.investrecord_id = ir.id and pr.succession = "+succession+" and pr.group_ = 0");
		
		obj.element("rows", records);
		response.getWriter().println(obj);
	}

	
	/**
	 * 融资还款凭证 列表 输出JSON
	 * @throws Exception 
	 */
	public void listForVoucherOfPayment() throws Exception{
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int page = 1,pagesize = 15;
			
			if(StringUtils.isNotBlank(request.getParameter("page"))){
				page = Integer.valueOf(request.getParameter("page"));
			}
			if(StringUtils.isNotBlank(request.getParameter("rows"))){
				pagesize = Integer.valueOf(request.getParameter("rows"));
			}
			
			String states = request.getParameter("states");
			String qkeyWord = request.getParameter("qkeyWord");
			
			StringBuilder wheresql = new StringBuilder();
			if(StringUtils.isNotBlank(states)){
				wheresql.append(" state in (").append(states).append(")");
			}else{
				wheresql.append(" state != 0 ");
			}
			if(StringUtils.isNotBlank(qkeyWord)){
				wheresql.append(" and  ( frealname like '%")
				.append(qkeyWord)
				.append("%' or frealname = '")
				.append(qkeyWord)
				.append("' or fshortname like '%")
				.append(qkeyWord)
				.append("%' or fshortname = '")
				.append(qkeyWord)
				.append("')");
			}
			
			if(StringUtils.isNotBlank(this.fbcode)){
				wheresql.append(" and ( ");
				if(this.fbcode.contains(",")){
					String [] codes = this.fbcode.split(",");
					for(int x = 0; x < codes.length ; x++){
						if( x > 0 ){
							wheresql.append(" or ");						
						}
						wheresql.append(" (financbasecode like '").append(codes[x].toUpperCase()).append("%' or financbasecode = '").append(codes[x].toUpperCase()).append("')");
					}
				}else{
					wheresql.append("  financbasecode like '").append(this.fbcode.toUpperCase()).append("%' or financbasecode = '").append(this.fbcode.toUpperCase()).append("' ");
				}
				wheresql.append(" ) ");
			}
			
			if(StringUtils.isNotBlank(this.org_code)){
				wheresql.append(" and forgno = '").append(this.org_code).append("'");
			}
			
			StringBuilder fields = new StringBuilder("financbaseid,qs,financbasecode,fshortname,FINANCIERID,FREALNAME,FORGNAME,FORGNO,CURRENYAMOUNT,STATE,RETURNTIMES,GROUP_,REMARK,to_date(to_char(SHDATE,'YYYY-MM-DD hh24:mi'),'yyyy-MM-DD hh24:mi')");
			
			String group = fields.toString();
			
			fields.append(" SHDATE").insert(0, "count(id) times,sum(YHBJ+YHLX+fj+SHFEE1+fj1+shfee2+fj2) shje,");
			
			if( null != this.getStartDate() && null != this.getEndDate()){
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getEndDate());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				wheresql.append(" and shdate between to_date('")
					.append(sdf.format(this.getStartDate()))
					.append("','yyyy-mm-dd hh24:mi:ss') and to_date('")
					.append(sdf.format(cal.getTime()))
					.append("','yyyy-mm-dd hh24:mi:ss')");
			}
			
			wheresql.append(" group by ").append(group).append(" order by SHDATE,financbasecode ");
			
			List<Map<String,Object>> result = this.paymentRecordService.queryForList(fields.toString() , "v_paymentrecord", wheresql.toString(), page, pagesize);
			int total = this.paymentRecordService.queryForListTotal("financbaseid","v_paymentrecord", wheresql.toString());
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String printForVoucherOfPayment() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String data = request.getParameter("data");
		if(StringUtils.isBlank(data)) return "printForVoucherOfPayment";
		
		JSONObject jsonobject = JSONObject.fromObject(data);
		JSONArray array = jsonobject.getJSONArray("rows");
		
		StringBuilder wheresql = new StringBuilder(" state != 0 and ( ");
		StringBuilder fields = new StringBuilder(" overdue_days,fbankaccount,financbaseid,FINANCIERNAME,qs,financbasecode,fshortname,FINANCIERID,FREALNAME,FORGNAME,FORGNO,CURRENYAMOUNT,STATE,RETURNTIMES,GROUP_,REMARK,to_date(to_char(SHDATE,'YYYY-MM-DD'),'yyyy-MM-DD')");
		
		String group = fields.toString();
		
		fields.append(" SHDATE").insert(0, "count(id) times,sum(yihuanbenjin) shbj, sum(yihuanlixi) shlx, sum(FJ) shfj, sum(SHFEE1) rongzifuwufei,sum(FJ1) fuwufeifajin,sum(SHFEE2) danbaofei,sum(FJ2) danbaofeifajin,");
		
		
		for(int x = 0; x < array.size(); x++){
			JSONObject obj = array.getJSONObject(x);
			if( x > 0){
				wheresql.append(" or ");
			}
			wheresql.append(" ( financbaseid = '").append(obj.getString("f_id"))
			.append("' and qs = ").append(obj.getInt("qs"))
			.append(" and group_ = ").append(obj.getInt("group_"))
			.append(" ) ");
		}
		wheresql.append(" ) ");
		
		wheresql.append(" group by ").append(group).append(" order by to_date(to_char(SHDATE,'YYYY-MM-DD'),'yyyy-MM-DD'),financbasecode,qs,group_ ");
		
		List<Map<String,Object>> list = this.paymentRecordService.queryForList(fields.toString(), "v_paymentrecord", wheresql.toString());
		ServletActionContext.getRequest().setAttribute("list", list);
		return "printForVoucherOfPayment";
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPaymentRecord(PaymentRecord paymentRecord) {
		this.paymentRecord = paymentRecord;
	}

	public PaymentRecord getPaymentRecord() {
		return paymentRecord;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFbcode() {
		return fbcode;
	}

	public void setFbcode(String fbcode) {
		this.fbcode = fbcode;
	}

	public String getIvcode() {
		return ivcode;
	}

	public void setIvcode(String ivcode) {
		this.ivcode = ivcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setApprove(int approve) {
		this.approve = approve;
	}

	public int getApprove() {
		return approve;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<CommonVo> getStateList() {
		List<CommonVo> stateList = new ArrayList<CommonVo>();
		stateList.add(new CommonVo("9", "全部"));
		stateList.add(new CommonVo("0", "未还款"));
		stateList.add(new CommonVo("1", "已还款"));
		stateList.add(new CommonVo("2", "延期"));
		stateList.add(new CommonVo("3", "逾期"));
		return stateList;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String ids) {
		try {
			if (ids.contains(",")) {
				this.ids = ids.split(",");
			} else {
				this.ids = new String[] { ids };
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public double getPaid_debt() {
		return paid_debt;
	}

	public void setPaid_debt(double paid_debt) {
		this.paid_debt = paid_debt;
	}

	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}

	public FinancingBase getFinancingBase() {
		return financingBase;
	}

	public void setPaymentRecordList(List<PaymentRecord> paymentRecordList) {
		this.paymentRecordList = paymentRecordList;
	}

	public List<PaymentRecord> getPaymentRecordList() {
		return paymentRecordList;
	}

	public void setZhglf_fj(double zhglf_fj) {
		this.zhglf_fj = zhglf_fj;
	}

	public double getZhglf_fj() {
		return zhglf_fj;
	}

	public void setDbf_fj(double dbf_fj) {
		this.dbf_fj = dbf_fj;
	}

	public double getDbf_fj() {
		return dbf_fj;
	}

	public String getQkeyWord() {
		return qkeyWord;
	}

	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank() {
		return bank;
	}

	public String getExcelFlag() {
		return excelFlag;
	}

	public void setExcelFlag(String excelFlag) {
		this.excelFlag = excelFlag;
	}

	public double getCount1() {
		return count1;
	}

	public void setCount1(double count1) {
		this.count1 = count1;
	}

	public double getCount2() {
		return count2;
	}

	public void setCount2(double count2) {
		this.count2 = count2;
	}

	public double getCount3() {
		return count3;
	}

	public void setCount3(double count3) {
		this.count3 = count3;
	}

	public double getCount4() {
		return count4;
	}

	public void setCount4(double count4) {
		this.count4 = count4;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getBjStr() {
		return bjStr;
	}

	public void setBjStr(String bjStr) {
		this.bjStr = bjStr;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getSelectby() {
		return selectby;
	}

	public void setSelectby(String selectby) {
		this.selectby = selectby;
	}

	public String getFxbzstate() {
		return fxbzstate;
	}

	public void setFxbzstate(String fxbzstate) {
		this.fxbzstate = fxbzstate;
	}

	public void setPayOver(int payOver) {
		this.payOver = payOver;
	}

	public int getPayOver() {
		return payOver;
	}
	public int getFstate() {
		return fstate;
	}
	public void setFstate(int fstate) {
		this.fstate = fstate;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getCreaterOrgStr() {
		return createrOrgStr;
	}
	public void setCreaterOrgStr(String createrOrgStr) {
		this.createrOrgStr = createrOrgStr;
	}

	public String getPu() {
		return pu;
	}

	public void setPu(String pu) {
		this.pu = pu;
	}
	
	
}
