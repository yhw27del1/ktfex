package com.kmfex.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.AccountDealVo;
import com.kmfex.ChargeImport;
import com.kmfex.ExecelImportUtil;
import com.kmfex.JYRBVO;
import com.kmfex.MoneyFormat;
import com.kmfex.QueryVO;
import com.kmfex.action.cmb.CMBVO;
import com.kmfex.cmb.request.merchant.MerChantRequest6201;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.BankLibrary;
import com.kmfex.model.CoreAccountLiveRecord;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.model.SignHistory;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.SignHistoryService;
import com.kmfex.service.cmb.CmbDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.kmfex.util.ImportExcelUtil;
import com.kmfex.util.SMSNewUtil;
import com.kmfex.util.SMSUtil;
import com.kmfex.zhaiquan.service.ContractService;
import com.opensymphony.xwork2.ActionContext;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.CurrencyOperator;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.VelocityUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.core.vo.PageForProcedureVO;
import com.wisdoor.struts.BaseAction;

/**
 * @author
 * 
 *  会员资产查询优化，修改方法totalAccount(),totalAccountEx()
 *  方法toCashRz(),bzjCashList(),bzjCashCheckToPass(),bzjCashCheckToNoPass()保证金提现申请审核
 */
@Controller
@Scope("prototype")
public class AccountDealAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1152544676274765917L;
	private String id;
	private String[] ids;
	private AccountDeal accountDeal;
	private Account account;
	private User user;
	private MemberBase member;
	private FinancingCost cost;
	private double chargeAmount;
	@Resource transient private MemberBaseService memberBaseService;
	@Resource transient private AccountService accountService;
	@Resource transient private UserService userService;
	@Resource transient private OpenCloseDealService openCloseDealService;
	@Resource transient private FinancingBaseService financingBaseService;
	private List<AccountDeal> deals = new ArrayList<AccountDeal>();

	@Resource transient private AccountDealService accountDealService;
	@Resource transient private HxbankDealService hxbankDealService;
	@Resource transient private FinancingCostService financingCostService;
	@Resource transient private InvestRecordService investRecordService;
	@Resource transient private ContractService contractService;

	@Resource transient private CoreAccountService coreAccountService;

	@Resource transient private OrgService orgService;
	
	@Resource transient private CmbDealService cmbDealService;
	
	@Resource transient private SignHistoryService signHistoryService;

	// 返回给webservice的页面参数
	private String userName;
	private String pu;
	private String userType;
	private String jyType;
	private List<AccountDealVo> dealVos = new ArrayList<AccountDealVo>();
	private double sumAmount;

	/**
	 * 查询时使用，查询内容
	 */
	private String value;
	/**
	 * 查询时使用，查询依据
	 */
	private String type;
	private String codes;
	private double bzj;
	private String qudao;
	private long userId;

	/** 会员统计查询* */
	private String queryCode;
	private String memberType = "T";
	private String keyWord;
	
	private String note;//提现备注
	private String queryBalance = "0";
	private String queryFrozenAmount;
	private double totalAmountSum = 0d;
	private double balanceSum = 0d;
	private double frozenAmountSum = 0d;
	private double cyzqSum = 0d;
	private double count1 = 0d;
	private double count2 = 0d;
	private double count3 = 0d;
	private double count4 = 0d;
	private List<Account> acs;
	// 导出功能
	private String excelFlag;
	private boolean load = false;
	private int action = 0;

	// 统计会员的总资产、账户余额、冻结金额、持有债权
	@SuppressWarnings("unchecked")
	public String totalAccount() {
		if (null == id || "".equals(id)) {
			return "totalAccount";
		}
		try {
			String sql = "select o.user_account_balance,o.user_username,o.name,o.user_id,o.cyzq,o.user_account_balance,o.user_account_frozenamount,o.zzc"+
		       " ,o.invest_all_count,o.invest_all,o.come_countt,o.come_amount " +
		       " from V_MEMBER_USER_ACCOUNT o where 1=1  ";
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sql += " and (";
				sql +=" o.user_username like '%" + keyWord + "%'";
				sql +=" or ";
				sql +=" o.name like '%" + keyWord + "%'";
				sql +=" )";
			}

			if (null != queryCode && !"".equals(queryCode.trim())) {
				User u = this.userService.findUser(queryCode);
				MemberBase m = this.memberBaseService.getMemByUser(u);
				queryCode = queryCode.trim();
				sql +=" and  o.id = '" + m.getId() + "'";
			}
			if (null != queryBalance && !"".equals(queryBalance.trim()) && !"0".equals(queryBalance.trim())) {
				queryBalance = queryBalance.trim();
				sql +=" and o.user_account_balance>=" + queryBalance;
			}
			if (null != queryFrozenAmount && !"".equals(queryFrozenAmount.trim()) && !"0".equals(queryFrozenAmount.trim())) {
				queryFrozenAmount = queryFrozenAmount.trim();
				sql +=" and o.user_account_frozenamount>=" + queryFrozenAmount;
			}
			if (!"4".contains(memberType)) {
				sql +=" and o.membertype_code='" + memberType + "'  ";
			}
			//sql += "order by o.user_account_balance desc "; 
			
			//System.out.println(sql);
			
			ArrayList<LinkedHashMap<String, Object>> dataList = this.accountService.selectListWithJDBC(sql);  
			
			//System.out.println("sql查询过程使用"+(System.currentTimeMillis()-time) +"毫秒");
			
			
			List<Account> acs =swithAccountList(dataList);
			 
			totalAmountSum = 0d;
			balanceSum = 0d;
			frozenAmountSum = 0d;
			cyzqSum = 0d;
			count1 = 0d;
			count2 = 0d;
			count3 = 0d;
			count4 = 0d;
			for (Account b : acs) {  		
				count1 += Double.parseDouble(b.getString1());
				count2 += Double.parseDouble(b.getString2());
				count3 += Double.parseDouble(b.getString3());
				count4 += Double.parseDouble(b.getString4());
				cyzqSum += b.getCyzq();
				balanceSum += b.getBalance();
				frozenAmountSum += b.getFrozenAmount();
				totalAmountSum += b.getTotalAmount();
			} 
			ServletActionContext.getRequest().setAttribute("acs", acs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "totalAccount";
	}

	// 统计会员的总资产、账户余额、冻结金额、持有债权
	@SuppressWarnings("unchecked")
	public String totalAccountEx() {
		try {

			String sql = "select * from V_MEMBER_USER_ACCOUNT o where o.membertype_code in('D','R','T')  ";
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sql += " and (";
				sql +=" o.user_username like '%" + keyWord + "%'";
				sql +=" or ";
				sql +=" o.name like '%" + keyWord + "%'";
				sql +=" )";
			}

			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sql +=" and  o.user_username like '%" + queryCode + "%'";
			}
			if (null != queryBalance && !"".equals(queryBalance.trim())) {
				queryBalance = queryBalance.trim();
				sql +="and o.user_account_balance>=" + queryBalance;
			}
			if (null != queryFrozenAmount && !"".equals(queryFrozenAmount.trim())) {
				queryFrozenAmount = queryFrozenAmount.trim();
				sql +="and o.user_account_frozenamount>=" + queryFrozenAmount;
			}
			if (!"4".contains(memberType)) {
				sql +="and o.membertype_code='" + memberType + "'  ";
			}
			sql += "order by o.user_account_balance desc "; 
			
			ArrayList<LinkedHashMap<String, Object>> dataList = this.accountService.selectListWithJDBC(sql);  
			
			List<Account> acs =swithAccountList(dataList);
			 
			 
			totalAmountSum = 0d;
			balanceSum = 0d;
			frozenAmountSum = 0d;
			cyzqSum = 0d;
			count1 = 0d;
			count2 = 0d;
			count3 = 0d;
			count4 = 0d;
			for (Account b : acs) {  
	 
				count1 += Double.parseDouble(b.getString1());
				count2 += Double.parseDouble(b.getString2());
				count3 += Double.parseDouble(b.getString3());
				count4 += Double.parseDouble(b.getString4());
				cyzqSum += b.getCyzq();
				balanceSum += b.getBalance();
				frozenAmountSum += b.getFrozenAmount();
				totalAmountSum += b.getTotalAmount();

			} 
			ServletActionContext.getRequest().setAttribute("acs", acs);
			
			 
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			ServletActionContext.getRequest().setAttribute("msg", format.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "totalAccountEx";
	}

	public CommonVo getincome(String userName) {
		Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		OutParameterModel outParameter = new OutParameterModel(2, OracleTypes.CURSOR);
		inParamList.put(1, userName);
		CommonVo vo = new CommonVo();
		ArrayList<LinkedHashMap<String, Object>> resultList = this.investRecordService.callProcedureForList("PKG_income.getRate", inParamList, outParameter);
		if (null == resultList || resultList.size() > 1 || resultList.size() == 0) {
			vo.setString1("0");
			vo.setString2("0.00");
			vo.setString3("0");
			vo.setString4("0.00");
		}else{
			for (int i = 0; i < resultList.size(); i++) {
				LinkedHashMap<String, Object> a = resultList.get(i);

				double invest_all = Double.parseDouble(a.get("invest_all").toString());
				double invest_all_count = Double.parseDouble(a.get("invest_all_count").toString());

				double come_amount = Double.parseDouble(a.get("come_amount").toString());
				double come_count = Double.parseDouble(a.get("come_count").toString());

				vo.setString1("0");
				vo.setString2("0.00");
				vo.setString3("0");
				vo.setString4("0.00");

				vo.setString1(DoubleUtils.doubleCheck2(invest_all_count, 0));// 借出笔数
				vo.setString2(DoubleUtils.doubleCheck2(invest_all, 2));// 借出金额
				vo.setString3(DoubleUtils.doubleCheck2(come_count, 0));// 回收笔数
				vo.setString4(DoubleUtils.doubleCheck2(come_amount, 2));// 回收金额

			}
		}
		return vo;

	}

	public Map<String, Double> getCyzqByUser(String memberIds) {
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.investor.user.id  ";
			sb.append(sqlGroup);
			sb.append("  ,sum(o.bjye) as cyzq");
			sb.append("  from InvestRecord o");
			sb.append("  where   o.state='2'  ");
			sb.append("  and  o.investor.user.id in(" + memberIds + ")");
			sb.append(" group by ").append(sqlGroup);
			QueryResult<Object> qr = investRecordService.groupHqlQuery(sb.toString());
			if (null != qr.getResultlist() && qr.getResultlist().size() > 0) {
				for (Object arr : qr.getResultlist()) {
					Object[] o = (Object[]) arr;
					if (null != o[1]) {
						map.put(o[0].toString(), (Double) o[1]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("0", 0d);
		}
		return map;
	}

	// 会员账户查询
	public String list() {
		String disUrl = "list";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<AccountDeal> pageView = null;
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("time", "desc");
		//orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.realname like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMCF + "','" + AccountDeal.ZQMRF + "')");
		sb.append(" and ");
		sb.append(" o.checkFlag != '16' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			QueryResult<AccountDeal> qr = null;
			if ("1".equals(excelFlag)) {// 导出功能
				pageView = new PageView<AccountDeal>(9999999, getPage());
				qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);

				disUrl = "list_ex";
				SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
				ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
			} else {
				pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
				qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			}
			pageView.setQueryResult(qr);
			List<AccountDeal> accountDeals = qr.getResultlist();

			List<AccountDeal> tempfbs = new ArrayList<AccountDeal>();

			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
				tempfbs.add(deal);
			}

			qr.setResultlist(tempfbs);

			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return disUrl;
	}

	public String acWs() {// 现金流水
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		try {
			User u = null;
			if (null != this.userName && !"".equals(this.userName)) {
				u = userService.findUser(userName);  
				if (null == u) {
					return "acWs";
				} else {
					if (!u.getUserType().equals(userType)) {
						return "acWs";
					}
				}
				
				//加入密码验证
				if(!u.getPassword().equals(pu)){
					return "illegalPage2";
				}
				
			} else {
				u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
			this.account = this.accountService.selectById(u.getUserAccount().getId());
			this.member = this.memberBaseService.getMemByUser(u);
			String checkFlag = "";
			if ("all".equals(type) || null == type) {// 全部
				if (null ==type) {
					type = "all";
				}
				checkFlag = "('1','3','4','5','9','11','12','13','14','20','21','22','23','24','25','26','27','31','34','37','51')";
			} else if ("charge".equals(type)) {// 现金充值
				checkFlag = "('1','37')";
			} else if ("cash".equals(type)) {// 提现
				checkFlag = "('3','4','5')";
			} else if ("invest_out".equals(type)) {// 投标划出
				checkFlag = "('11')";
			} else if ("payment_in".equals(type)) {// 划款划入
				checkFlag = "('9')";
			} else if ("zqzr".equals(type)) {// 债权转让:债权买入，债权卖出，手续费。
				checkFlag = "('12','13','14')";
			} else if ("daidian".equals(type)) {// 代垫操作:代垫划出，代垫划入。
				checkFlag = "('20','21')";
			} else if ("neibu".equals(type)) {// 内部转账:内部划出，内部划入。
				checkFlag = "('22','23')";
			} else if ("bank_zhengquan".equals(type)) {// 入金，出金
				checkFlag = "('24','25','26','27')";
			} else if ("jyfwf".equals(type)) {// 入金，出金
				checkFlag = "('51')";
			} else {
				checkFlag = "('1','3','4','5','9','11','12','13','14','20','21','22','23','24','25','26','27','51')";
			}
			StringBuilder sb = new StringBuilder(" sa.accountid_=o.account_accountid_ ");
			sb.append(" and ");
			sb.append(" o.CHECKFLAG in " + checkFlag);// 投资人只能看：充值审核通过；提现等待审核；提现审核通过；提现审核驳回；还款划入
			sb.append(" and ");
			sb.append(" o.TYPE not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMCF + "','" + AccountDeal.ZQMRF + "')");
			sb.append(" and ");
			sb.append(" o.ACCOUNT_ACCOUNTID_='" + account.getId() + "' ");
			sb.append(" and ");
			sb.append(" o.CREATEDATE >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.CREATEDATE <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			///System.out.println("myAcList----satartDate="+format.format(startDate)+"------endDateNext="+format.format(endDateNext));
			sb.append(" order by o.time_ desc,o.CREATEDATE desc");
			
			String fields="to_char(o.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate,o.businessflag,o.type,o.checkflag,o.checkflag2,o.zhaiquancode,o.memo,o.bj,o.lx,o.fj,o.money,o.nextmoney,fb.code,sa.accountid_ as id,o.time_ ";
			String tablename="sys_account sa,t_accountdeal o left join t_Financing_Base fb on fb.id=o.financing_id ";
			// 来自资金明细(充值和提现)
		    List<Map<String, Object>> result=accountDealService.queryForList(fields, tablename, sb.toString(),null, getPage(), getShowRecord());
		    List<AccountDeal> vos=new ArrayList<AccountDeal>(); 
		    FinancingBase financing=null;
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    for (Map<String, Object> obj : result) {
				if (null != obj.get("id")) { 
					Date createDate=format1.parse(obj.get("createdate").toString()); 
					String businessFlag=obj.get("businessFlag").toString();
					String typeTemp=obj.get("type").toString();    
					String checkflag=obj.get("checkflag").toString();    
					String checkflag2=obj.get("checkflag2").toString();    
				   
				     
				   String financingCode="";
				   if(null!=obj.get("code")){
					   financingCode=obj.get("code").toString(); 
				   }
				   
				   String zhaiQuanCode="";
				   if(null!=obj.get("zhaiquancode")){
					   zhaiQuanCode=obj.get("zhaiquancode").toString(); 
				   }
				   String memo="";
				   if(null!=obj.get("memo")){
					   memo=obj.get("memo").toString(); 
				   }
				    AccountDeal vo=new  AccountDeal(); 
				    vo.setCreateDate(createDate);
				    vo.setBusinessFlag(Integer.parseInt(businessFlag));
				    vo.setType(typeTemp);
				    vo.setCheckFlag(checkflag);
				    vo.setCheckFlag2(checkflag2);
				    vo.setBj(Double.parseDouble(obj.get("bj").toString()));
				    vo.setLx(Double.parseDouble(obj.get("lx").toString()));
				    vo.setFj(Double.parseDouble(obj.get("fj").toString()));
				    vo.setMoney(Double.parseDouble(obj.get("money").toString()));
				    vo.setNextMoney(Double.parseDouble(obj.get("nextmoney").toString()));
				    vo.setMemo(memo);
				    vo.setTime(Long.parseLong(obj.get("time_").toString()));
				    vo.setZhaiQuanCode(zhaiQuanCode);
				    financing=new FinancingBase();
				    financing.setCode(financingCode);
				    vo.setFinancing(financing); 
				    vos.add(vo);
				} 
			}	
		    QueryResult<AccountDeal>  qr=new QueryResult<AccountDeal>();
		    qr.setResultlist(vos);
		    int totalrecord = this.financingBaseService.queryForListTotal("sa.accountid_",tablename,sb.toString(),null);
		    qr.setTotalrecord(totalrecord);
		    pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "acWs";
	}

	public String sgWs() { // 投标明细
		User u = null;
		try {
			if (null != this.userName && !"".equals(this.userName)) {
				u = userService.findUser(userName);
				if (null == u) {
					return "sgWs";
				} else {
					if (!u.getUserType().equals(userType)) {
						return "sgWs";
					}
				}
				//加入密码验证
				if(!u.getPassword().equals(pu)){
					return "illegalPage2";
				}
			} else {
				u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			this.user = u;
			double cyzq = this.accountService.cyzq(this.user.getUsername());
			this.account = this.user.getUserAccount();
			this.account.setCyzq(cyzq);
			
			String fields=" ir.zhaiQuanCode,fb.shortname as fshortname,fb.rate,fb.interestday,bt.term as BUSINESSTYPE,bt.returnpattern,ir.investamount,ckd.interest_allah,ir.bjye,ir.lxye,ir.state,ir.xyhkr,zb.buyingprice as cbj,zb.zqfwf as fwf,zb.zqsf as sf,ir.transferflag ,ir.id,fb.terminal,ir.fromapp";
			String tablename="t_financing_base fb,t_business_type bt,t_contract_key_data ckd, sys_user su,t_member_base mb, t_invest_record ir  left join zq_Buying zb  on  zb.investrecord_id=ir.id";
			StringBuilder sb = new StringBuilder(" ir.financingbase_id=fb.id and bt.id=fb.businesstype_id and ckd.inverstrecord_id=ir.ID and su.id=mb.user_id and mb.id=ir.investor_id");
			sb.append(" and su.username='"+this.user.getUsername()+"'");
			sb.append(" order by ir.createdate desc");  
			List<Map<String, Object>> results=accountDealService.queryForList(fields, tablename, sb.toString(),null, this.getPage(), this.getShowRecord());
			PageForProcedureVO pageView=new PageForProcedureVO();
			ArrayList<LinkedHashMap<String,Object>> newResult = new ArrayList<LinkedHashMap<String,Object>>();
			for(Map<String, Object> map:results){
				newResult.add((LinkedHashMap<String,Object>)map);  
			}
			pageView.setResult(newResult);
		    int totalrecord = this.investRecordService.queryForListTotal("ir.id",tablename,sb.toString(),null);
		    int totalpage=totalrecord% this.getShowRecord()==0? totalrecord/ this.getShowRecord() : totalrecord/ this.getShowRecord()+1;//总页数
		    pageView.setTotalpage(totalpage);
		    pageView.setTotalrecord(totalrecord); 
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			
			// 以下积分没有使用
			// Map<Integer, Object> inParamList = new LinkedHashMap<Integer,
			// Object>();
			// Map<Integer, Integer> outParameter = new LinkedHashMap<Integer,
			// Integer>();
			// inParamList.put(1, member.getId());
			// outParameter.put(2, Types.INTEGER);
			// HashMap<Integer, Object> result =
			// this.investRecordService.callProcedureForParameters("P_INVESTOR_INVEST.GET_FORZENCREDIT",
			// inParamList, outParameter);

			// ServletActionContext.getRequest().setAttribute("forzencredit",
			// result.get(2));
			// ServletActionContext.getRequest().setAttribute("listInVos",ir);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "sgWs";
	}

	public String listForPersonT() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.account =
		// this.accountService.selectById(u.getUserAccount().getId());
		this.account = this.accountService.selectByUserId(u.getId());
		this.member = this.memberBaseService.getMemByUser(u);
		this.userName = u.getUsername();
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag in ('1','3','4','5','9','11','12','13','14','20','21','24','25')");// 投资人只能看：充值审核通过；提现等待审核；提现审核通过；提现审核驳回；还款划入；投标划出；代垫划出/入
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "')");
		sb.append(" and ");
		sb.append(" o.account.id='" + this.account.getId() + "'");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listForPerson";
	}

	public String listForPersonR() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.account =
		// this.accountService.selectById(u.getUserAccount().getId());
		this.account = this.accountService.selectByUserId(u.getId());
		this.member = this.memberBaseService.getMemByUser(u);
		this.userName = u.getUsername();
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag in ('1','2.4','2.5','2.9','3','4','5','6','10','15','19','20','21','24','25','30','31','32','33','34','35')");// 融资方只能看：充值审核通过；提现等待审核；提现审核通过；提现审核驳回；还款划出；交割划入；代垫划出/入
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "')");
		sb.append(" and ");
		sb.append(" o.account.id='" + this.account.getId() + "'");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listForPerson";
	}
	
	private int channel = 0;

	public String query() {
		try {
			if (id != null) {
				if (Account.CENTER.equals(id)) {
					QueryVO vo = new QueryVO();
					vo.setFlag(false);
					DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
				}
			} else if (value != null && !value.isEmpty()) {
				ArrayList<QueryVO> vos = new ArrayList<QueryVO>();
				if ("name".equals(type)) {
					List<MemberBase> members = this.memberBaseService.getScrollDataCommon("from MemberBase where pName like '%" + value + "%' or eName like '%" + value + "%'", new String[] {});
					for (MemberBase mb : members) {
						String ttt = "";
						QueryVO vo = new QueryVO();
						User u = mb.getUser();
						if(this.channel!=0){
							if(u.getChannel()!=this.channel){
								continue;
							}
						}
						if ("T".equals(u.getUserType())) {
							ttt = "投资人";
						} else if ("R".equals(u.getUserType())) {
							ttt = "融资方";
						} else if ("D".equals(u.getUserType())) {
							ttt = "担保方";
						}
						Account account = u.getUserAccount();
						if (null != account) {
							double b = accountService.queryBalance(account);
							vo.setId(mb.getId());
							vo.setAccount(mb.getBankAccount());
							vo.setBalance(b);
							vo.setBank(mb.getBank());
							vo.setName("0".equals(mb.getCategory()) ? mb.geteName() : mb.getpName());
							vo.setFlag(true);
							vo.setUsername(u.getUsername());
							vo.setTip(ttt);
							if(mb.getState().equals(MemberBase.STATE_STOPPED)){
								continue;
							}else if("2".equals(mb.getUser().getFlag())){
								vo.setSign(mb.getBanklib().getCaption()+"已签约用户,不能充值");
							}else if("1".equals(mb.getUser().getFlag())){
								vo.setSign(mb.getBanklib().getCaption()+"签约中用户,不能充值");
							}else{
								vo.setSign(mb.getBanklib().getCaption());
							}
							vos.add(vo);
						}

					}
				} else if ("username".equals(type)) {
					User u = this.userService.findUser(value);
					QueryVO vo = new QueryVO();
					if (u != null) {
						boolean f = false;
						if(this.channel==0){
							f = true;
						}else{
							if(u.getChannel()==this.channel){
								f = true;
							}
						}
						if(f){
							String ttt = "";
							Account account = u.getUserAccount();
							if (null != account) {
								double b = accountService.queryBalance(account);
								MemberBase mb = memberBaseService.getMemByUser(u);
								if ("T".equals(u.getUserType())) {
									ttt = "投资人";
								} else if ("R".equals(u.getUserType())) {
									ttt = "融资方";
								} else if ("D".equals(u.getUserType())) {
									ttt = "担保方";
								}
								vo.setId(mb.getId());
								vo.setAccount(mb.getBankAccount());
								vo.setBalance(b);
								vo.setBank(mb.getBank());
								vo.setName("0".equals(mb.getCategory()) ? mb.geteName() : mb.getpName());
								vo.setFlag(true);
								vo.setUsername(u.getUsername());
								vo.setTip(ttt);
								if(mb.getState().equals(MemberBase.STATE_STOPPED)){
									return null;
								}else if("2".equals(mb.getUser().getFlag())){
									vo.setSign(mb.getBanklib().getCaption()+"已签约用户,不能充值");
								}else if("1".equals(mb.getUser().getFlag())){
									vo.setSign(mb.getBanklib().getCaption()+"签约中用户,不能充值");
								}else{
									vo.setSign(mb.getBanklib().getCaption());
								}
								vos.add(vo);
							}
						}
					}

				} else if ("bankaccount".equals(type)) {
					List<MemberBase> members = this.memberBaseService.getScrollDataCommon("from MemberBase where bankAccount like '%" + value + "%'", new String[] {});

					for (MemberBase mb : members) {
						String ttt = "";
						QueryVO vo = new QueryVO();
						User u = mb.getUser();
						if(this.channel!=0){
							if(u.getChannel()!=this.channel){
								continue;
							}
						}
						if ("T".equals(u.getUserType())) {
							ttt = "投资人";
						} else if ("R".equals(u.getUserType())) {
							ttt = "融资方";
						} else if ("D".equals(u.getUserType())) {
							ttt = "担保方";
						}
						Account account = u.getUserAccount();
						if (null != account) {
							double b = accountService.queryBalance(account);
							vo.setId(mb.getId());
							vo.setAccount(mb.getBankAccount());
							vo.setBalance(b);
							vo.setBank(mb.getBank());
							vo.setName("0".equals(mb.getCategory()) ? mb.geteName() : mb.getpName());
							vo.setFlag(true);
							vo.setUsername(u.getUsername());
							vo.setTip(ttt);
							if(MemberBase.STATE_STOPPED.equals(mb.getState())){
								continue;
							}else if("2".equals(mb.getUser().getFlag())){
								vo.setSign(mb.getBanklib().getCaption()+"已签约用户,不能充值");
							}else if("1".equals(mb.getUser().getFlag())){
								vo.setSign(mb.getBanklib().getCaption()+"签约中用户,不能充值");
							}else{
								vo.setSign(mb.getBanklib().getCaption());
							}
							vos.add(vo);
						}
					}
				}
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 中心账户查询(按日期汇总)
	public String centerAccount() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		this.account = this.accountService.centerAccount();
		PageView<Object> pageView = new PageView<Object>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.createtime", "desc");

		StringBuilder sb = new StringBuilder();
		sb.append(" o.createtime between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and " + "to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		sb.append(" and o.action <> '35' ");
		sb.append(" group by to_date(to_char(o.createtime,'yyyy-MM-dd'),'yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.coreAccountService.groupByCreatDate(sb.toString()));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount";
	}

	// 中心账户查询(某天的明细)
	public String centerAccount2() {
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(request.getParameter("date"));
			request.setAttribute("date", date);
			this.account = this.accountService.centerAccount();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("fbase.code", "desc");

			PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
			StringBuilder sb = new StringBuilder();
			List<String> params = new ArrayList<String>();
			sb.append(" to_date(to_char(o.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(date) + "','yyyy-MM-dd') ");

			pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount2";
	}
	
	/**
	 * 使用sql查询 导出t_coreaccount_liverecord中的内容
	 * @return
	 */ 
	public String centerAccount2_toExcel() {
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(request.getParameter("date"));
					
			List<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
			StringBuilder sb = new StringBuilder();
			sb.append("select tcl.createtime,decode(tcl.action, 1, '融资款交割-划出', 2, '投资款划入中心账户-划入',3, '三方结算-划入', 4, '债权买入-划入', 5, '债权卖出-划入',");
			sb.append("6, '融资服务费与罚金-划入', 7, '担保费-划入', " +
					"8, '风险管理费',9,'交易手续费', 21, '兴易贷费用--风险管理费', 22, '兴易贷费用--融资服务费', 23, '兴易贷费用--担保费', ");
			sb.append("31, '非兴易贷费用-担保费', 32, '非兴易贷费用-风险管理费（按月）', 33, '非兴易贷费用-融资服务费', 34, '非兴易贷费用-保证金', 35, '中心账户出账-划出',");
			sb.append("36, '席位费', 37, '信用管理费', 41, '内部转帐') actionName,");
			sb.append("su.realname,su.username,tfb.code,");
			sb.append("(case when tcl.value > 0 then tcl.abs_value  else 0 end) receiveValue,");
			sb.append("(case when tcl.value < 0 then tcl.abs_value else  0 end) payValue,");
			sb.append("tcl.operater_id,suoper.realname operator");
			sb.append(" from t_coreaccount_liverecord tcl left join t_Financing_Base tfb on tcl.fbase_id = tfb.id");
			sb.append(" left join sys_Account sa on tcl.object__accountid_ = sa.accountid_");
			sb.append(" left join sys_user su on su.useraccount_accountid_ = sa.accountid_");
			sb.append(" left join sys_user suoper on tcl.operater_id = suoper.id");
			sb.append(" where 1=1 and tcl.action <> '35' ");
			sb.append(" and to_date(to_char(tcl.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(date) + "','yyyy-MM-dd') ");
			//System.out.println(sb.toString());
			results = this.coreAccountService.selectListWithJDBC(sb.toString());
			//System.out.println(results.get(0).get("actionName"));
			String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			ServletActionContext.getResponse().setContentType(
					"application/x-xls");
			ServletActionContext.getResponse().setHeader(
					"Content-Disposition",
					"attachment;filename=CoreAccountLiveRecord" + fileName + ".xls");
			ServletOutputStream os = ServletActionContext.getResponse()
					.getOutputStream();
			List<String> head = new ArrayList<String>();
		    head.add("发生时间");
		    head.add("类型");
		    head.add("融资方");
		    head.add("融资方帐号");
		    head.add("融资项目ID");
		    head.add("(收)发生额");
		    head.add("(付)发生额");
		    head.add("操作者");
		    List<String> paramOrder = new ArrayList<String>();
		    paramOrder.add("createtime");
		    paramOrder.add("actionname");
		    paramOrder.add("realname");
		    paramOrder.add("username");
		    paramOrder.add("code");
		    paramOrder.add("receivevalue");
		    paramOrder.add("payvalue");
		    paramOrder.add("operator");
		    List<LinkedHashMap<String, Object>> theData = new ArrayList<LinkedHashMap<String, Object>>();
		    theData = results;
		    ImportExcelUtil.exportInvestors("中心账户收入汇总导出",os,head,paramOrder,theData,"HashMap");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public String centerAccount2_a() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();

		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createtime", "desc");

		PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
		StringBuilder sb = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sb.append(" to_date(to_char(o.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
		sb.append(" and tariff = 0 ");//1 融资款交割-划出\2投资款划入中心账户-划入\6融资服务费与罚金-划入\7担保费-划入\41内部转帐
		try {
			pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount2_classify";
	}

	/**
	 * 
	 * @return
	 */
	public String centerAccount2_b() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();

		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createtime", "desc");

		PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
		StringBuilder sb = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sb.append(" to_date(to_char(o.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
		sb.append(" and tariff = 1 ");//6 融资服务费与罚金-划入  7 担保费-划入
		try {
			pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount2_classify";
	}

	public String centerAccount2_c() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();

		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createtime", "desc");

		PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
		StringBuilder sb = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sb.append(" to_date(to_char(o.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') ");
		sb.append(" and action in (1,2,35,41) ");
		//System.out.println(1);
		try {
			//pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount2_classify";
	}
	
	//中心账户查询参数之项目编号
	private String fcode;
	
	//中心账户查询分类排序功能之列表缓存
	
	// 中心账户查询(按融资项目分别列出)
		public String centerAccount2_groupByFbase() {
			HttpServletRequest request = ServletActionContext.getRequest();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(this.getFcode()==null || "".equals(this.getFcode()) && (this.getUserName()==null || "".equals(this.getUserName()))){
					return "centerAccount2_groupByFbase";
				}
				String str = request.getParameter("date");
				Date date = null;
				if(str!=null && !str.equalsIgnoreCase(""))
					date = format.parse(str);
				request.setAttribute("date", date);
				this.account = this.accountService.centerAccount();
				
				StringBuilder sb = new StringBuilder();
				sb.append("select a.action,a.fbase_id,a.total_value,a.total_abs_value,a.operater_id,a.object__accountid_,a.message,a.count,su.realname,su.username,tfb.code,suoper.realname operater,a.calculated,tfb.shortName,creater.org_id,org.name_ orgname,org.showcoding ");
				sb.append(" from (select tcl.action,tcl.fbase_id,tcl.operater_id,tcl.object__accountid_,sum(tcl.abs_value) total_abs_value,count(tcl.abs_value) count,sum(tcl.value) total_value,tcl.message,tcl.calculated");
				sb.append(" from t_coreaccount_liverecord tcl  where tcl.action > 2 ");
				
				//筛选条件之  时间（具体到日）
				if(date != null){
					sb.append(" and to_date(to_char(tcl.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') = to_date('" + format.format(date) + "','yyyy-MM-dd') ");
				}else if(this.getStartDate()!=null && this.getEndDate()!=null){
					sb.append(" and to_date(to_char(tcl.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') between  to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(DateUtils.getAfter(this.getEndDate(), 1)) + "','yyyy-MM-dd') ");
				}
				sb.append(" group by tcl.fbase_id,tcl.action,tcl.calculated,operater_id,object__accountid_,tcl.message)a ");
				sb.append(" left join t_Financing_Base tfb on a.fbase_id = tfb.id");
				sb.append(" left join sys_Account sa on a.object__accountid_ = sa.accountid_");
				sb.append(" left join sys_user su on su.useraccount_accountid_ = sa.accountid_");
				sb.append(" left join sys_user suoper on a.operater_id = suoper.id");
				sb.append(" left join sys_user creater on tfb.createby_id = creater.id");
				sb.append(" left join sys_org org on creater.org_id = org.id");
				sb.append(" where 1 = 1 ");
				if(this.getKeyWord()!=null && !this.getKeyWord().equalsIgnoreCase("")){
					sb.append(" and (su.realname like '%"+this.getKeyWord()+"%' or su.username like '%"+this.getKeyWord()+"%' or tfb.code like '%"+this.getKeyWord()+"%' or tfb.shortname like '%"+this.getKeyWord()+"%')");
				}
				if(this.getFcode()!=null && !"".equals(this.getFcode())){
					sb.append(" and (tfb.code like '%"+this.getFcode()+"%') ");
				}
				if(this.getUserName()!=null && !"".equals(this.getUserName())){
					sb.append(" and (su.username like '%"+this.getUserName()+"%') ");
				}
				sb.append(" order by tfb.id desc,a.action");
				
//				System.out.println(sb.toString());
				
				ArrayList<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
				results = this.coreAccountService.selectListWithJDBC(sb.toString());
				
				PageForProcedureVO pageView = new PageForProcedureVO();
				pageView.setResult(results);
				pageView.setTotalrecord(results.size());
				
				ServletActionContext.getRequest().setAttribute("pageView", pageView);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "centerAccount2_groupByFbase";
		}

	public String getFcode() {
			return fcode;
		}

	public void setFcode(String fcode) {
			this.fcode = fcode;
		}
	
	public String centerAccount2_groupByWholeFbase() {
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (this.getStartDate() == null) {
				this.setStartDate(new Date());
			}
			if (this.getEndDate() == null) {
				this.setEndDate(new Date());
			}
			
			this.account = this.accountService.centerAccount();

			StringBuilder sb = new StringBuilder();
			sb.append(" select vcalr.fbase_id,vcalr.object__accountid_,vcalr.fcode,vcalr.shortname,vcalr.username,vcalr.realname,vcalr.org_id,vcalr.orgname,  sum(vcalr.total_income) total_income,sum(vcalr.one_off_income) one_off_income,sum(vcalr.sustainable_income) sustainable_income" +
					" from V_CoreAccountLiveRecord vcalr where vcalr.action > 3 and (vcalr.action< 35 or vcalr.action >35)");
			if(this.getKeyWord()!=null && !this.getKeyWord().equalsIgnoreCase("")){
				sb.append(" and (realname like '%"+this.getKeyWord()+"%' or username like '%"+this.getKeyWord()+"%' or fcode like '%"+this.getKeyWord()+"%' or shortname like '%"+this.getKeyWord()+"%')");
			}
			if(this.getFcode()!=null && !"".equals(this.getFcode())){
				sb.append(" and (fcode like '%"+this.getFcode()+"%') ");
			}
			
			sb.append(" and to_date(to_char(vcalr.createtime,'YYYY-MM-DD'),'YYYY-MM-DD') between  to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd') ");
			
			sb.append(" group by vcalr.fbase_id,vcalr.object__accountid_,vcalr.fcode,vcalr.shortname,vcalr.username,vcalr.realname,vcalr.org_id,vcalr.orgname" +
			" order by vcalr.fbase_id desc");
			
//			System.out.println(sb.toString());
			
			ArrayList<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
			results = this.coreAccountService.selectListWithJDBC(sb.toString());
			
			
			PageForProcedureVO pageView = new PageForProcedureVO();
			pageView.setResult(results);
			pageView.setTotalrecord(results.size());
			
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "centerAccount2_groupByFbase2";
	}

	public String center_out() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println(this.fcode);
		//System.out.println(this.);
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		this.account = this.accountService.centerAccount();
		PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createtime", "desc");
		StringBuilder sb = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sb.append(" action=35 and createtime between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and " + "to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "center_out";
	}

	public String to_center_out_page(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		this.account = this.accountService.centerAccount();
		PageView<CoreAccountLiveRecord> pageView = new PageView<CoreAccountLiveRecord>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createtime", "desc");
		StringBuilder sb = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sb.append(" action=35 and createtime between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and " + "to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.coreAccountService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "center_out";
	}
	
	//出账的目标机构
	private String tarOrg;
	
	public String getTarOrg() {
		return tarOrg;
	}

	public void setTarOrg(String tarOrg) {
		this.tarOrg = tarOrg;
	}

	public String center_out_do() {
		PrintWriter out = null;
		try {
			out = getOut();
//			System.out.println(this.chargeAmount);
//			System.out.println(this.memo);
//			System.out.println(this.tarOrg);
//			System.out.println(this.fcode);
			if (this.chargeAmount != 0) {
				this.account = this.accountService.centerAccount();
				if (this.chargeAmount > this.account.getBalance()) {
					out.write("{\"tip\":\"出账失败，出账金额不能大于中心账户余额。\"}");
				} else {
					CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
					calr.setAction(35);
					calr.setCalculated(true);
					calr.setCalculat_time(new Date());
					calr.setValue(this.chargeAmount);
					calr.setAbs_value(Math.abs(calr.getValue()));
					//calr.setObject_(this.account);//这里原为对应当前的用户
					Account ac1 = null;
					Account ac2 = null;
					Org org = null;
					if(this.tarOrg!=null){
						org = this.orgService.findOrg(tarOrg);//通过收款记录中的参数（发包人creatBy）寻找机构
						ac1 = org.getOrgAccount();
						calr.setObject_(ac1);
					}
					if(this.fcode!=null){
						FinancingBase fb = this.financingBaseService.selectById(this.fcode);//通过融资方寻找机构
						if(fb!=null && org!=null){
							ac2 = fb.getFinancier().getUser().getOrg().getOrgAccount();
							if(ac1.getAccountId() != ac2.getAccountId()){
								this.setMemo(this.memo+"(融资方机构和担保机构不同)");
							}
						}
					}
					if(ac1 == null){
						if(ac2!=null){
							calr.setObject_(ac2);//如果参数中机构不存在，保存到融资方机构并记录
							this.setMemo(this.memo+"(担保机构不存在，保存融资方机构)");
						}else{
							calr.setObject_(this.account);
							this.setMemo(this.memo+"(无相关的机构)");
						}
					}
					calr.setOperater(this.getLoginUser());
					calr.setCreatetime(new Date());
					calr.setMessage(this.memo);
					this.coreAccountService.insert(calr);
					this.accountService.loseMoney(this.account, this.chargeAmount);
					out.write("{\"tip\":\"出账成功\"}");
				}
			} else {
				out.write("{\"tip\":\"出账失败，出账金额不能为0\"}");
			}
		} catch (Exception e) {

		}
		return null;
	}

	// 三方账户查询(按日期汇总)
	public String thirdAccount() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		this.account = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
		PageView<Object> pageView = new PageView<Object>(getShowRecord(), getPage());
		StringBuilder sb = new StringBuilder(" 1=1 ");
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.createDate", "desc");
		sb.append(" and ");
		sb.append(" o.checkFlag != '15'");
		sb.append(" and ");
		sb.append(" o.account.id='" + this.account.getId() + "'");
		sb.append(" and ");
		sb.append(" o.type in ('" + AccountDeal.ZHGLF + "','" + AccountDeal.DBF + "','" + AccountDeal.JSHC + "')");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		sb.append(" group by to_date(to_char(o.createDate,'yyyy-MM-dd'),'yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.coreAccountService.groupByCreatDate(sb.toString()));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "thirdAccount";
	}

	// 三方账户查询(某天的明细)
	public String thirdAccount2() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		this.account = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag != '15'");
		sb.append(" and ");
		sb.append(" o.account.id='" + this.account.getId() + "'");
		sb.append(" and ");
		sb.append(" o.type in ('" + AccountDeal.ZHGLF + "','" + AccountDeal.DBF + "','" + AccountDeal.JSHC + "')");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "thirdAccount2";
	}

	// 三方账户结算
	public String thirdAccount_js() {
		this.account = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
		return "thirdAccount_js";
	}

	private double jsbl = 0.0d;

	// 三方账户结算
	public String thirdAccount_js_do() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Account third = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
		if (this.accountDealService.sfjs(third, jsbl)) {// 结算成功
			out.write("{\"message\":\"success\"}");
		} else {// 结算失败
		  out.write("{\"message\":\"结算失败，请重试！\"}");
		}
		return null;
	}

	private double inAccount_530101 = 0.0;// 入金汇总
	private double outAccount_530101 = 0.0;// 出金汇总

	private double inAccount_530105 = 0.0;// 入金汇总
	private double outAccount_530105 = 0.0;// 出金汇总

	private double start_530101_balance = 0.0;// 账户余额汇总

	private double start_530105_balance = 0.0;// 账户余额汇总

	private Date today = new Date();

	// 交易日报
	public String jyrb() throws Exception {
		this.openCloseDealService.getLatestKaiShi();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getStartDate(), 1);

		//已挂单未满标,state=2,3
		String hql_2_3 = "select m.ename,f.enddate,f.state,to_char(l.time,'yyyy-MM-dd HH24:mi:ss') as guadan,f.id,f.code,f.shortname,f.maxamount,f.currenyamount,f.curcaninvest,f.haveinvestnum,(f.currenyamount/f.maxamount)*100 as jd from t_financing_base f,sys_logs l,t_member_base m where f.state in ('2','3') and l.operate='挂单通过' and entityid=f.id and f.financier_id=m.id ";
		ArrayList<LinkedHashMap<String,Object>> list23 = this.financingBaseService.selectListWithJDBC(hql_2_3);
		ServletActionContext.getRequest().setAttribute("list23", list23);
		
		//已满标未交易确认,state=4
		String hql_4 = "select to_char(vbs.mane,'yyyy-MM-dd HH24:mi:ss') as mane,mb.ename,fb.enddate,fb.state,fb.id,fb.code,fb.shortname,fb.maxamount,fb.currenyamount,fb.curcaninvest,fb.haveinvestnum,(fb.currenyamount/fb.maxamount)*100 as jd from t_financing_base fb,t_member_base mb,(select max(l.time) as mane,f.code as code from t_financing_base f,sys_logs l where f.state='4' and l.operate like '投标%' and l.entityid=f.id group by f.code) vbs where fb.financier_id=mb.id and fb.code=vbs.code and fb.state='4'";
		ArrayList<LinkedHashMap<String,Object>> list4 = this.financingBaseService.selectListWithJDBC(hql_4);
		ServletActionContext.getRequest().setAttribute("list4", list4);

		//已交易确认未费用确认,state=5
		String hql_5 = "select m.ename,f.enddate,f.state,to_char(l.time,'yyyy-MM-dd HH24:mi:ss') as jyqr,f.id,f.code,f.shortname,f.maxamount,f.currenyamount,f.curcaninvest,f.haveinvestnum,(f.currenyamount/f.maxamount)*100 as jd from t_financing_base f,sys_logs l,t_member_base m where f.state='5' and l.operate='确认融资项目' and l.entityid=f.id and f.financier_id=m.id ";
		ArrayList<LinkedHashMap<String,Object>> list5 = this.financingBaseService.selectListWithJDBC(hql_5);
		ServletActionContext.getRequest().setAttribute("list5", list5);
		
		//已费用确认未签约,state=6
		String hql_6 = "select m.ename,f.enddate,f.state,to_char(l.time,'yyyy-MM-dd HH24:mi:ss') as fyqr,f.id,f.code,f.shortname,f.maxamount,f.currenyamount,f.curcaninvest,f.haveinvestnum,(f.currenyamount/f.maxamount)*100 as jd from t_financing_base f,sys_logs l,t_member_base m where f.state='6' and l.operate='费用通过' and l.entityid=f.id and f.financier_id=m.id ";
		ArrayList<LinkedHashMap<String,Object>> list6 = this.financingBaseService.selectListWithJDBC(hql_6);
		ServletActionContext.getRequest().setAttribute("list6", list6);
		
		//已签约
		String hql_7 = "select m.ename,to_char(f.qianyuedate,'yyyy-MM-dd HH24:mm:ss') as qianyuedate,f.state,f.id,f.code,f.shortname,f.maxamount,f.currenyamount,f.curcaninvest,f.haveinvestnum,(f.currenyamount/f.maxamount)*100 as jd from t_financing_base f,t_member_base m where f.financier_id=m.id and f.state='7' and f.qianyuedate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and f.qianyuedate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ";// state=7表示已签约
		ServletActionContext.getRequest().setAttribute("list7", this.financingBaseService.selectListWithJDBC(hql_7));
		
		//已撤单
		String hql_8 = "select m.ename,to_char(f.modifydate,'yyyy-MM-dd HH24:mm:ss') as modifydate,f.state,f.id,f.code,f.shortname,f.maxamount,f.currenyamount,f.curcaninvest,f.haveinvestnum,(f.currenyamount/f.maxamount)*100 as jd from t_financing_base f,t_member_base m where f.financier_id=m.id and f.state='8' and f.modifydate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and f.modifydate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ";// state=7表示已签约
		ServletActionContext.getRequest().setAttribute("list8", this.financingBaseService.selectListWithJDBC(hql_8));
		return "jyrb";
	}

	private List<JYRBVO> jyrb = new ArrayList<JYRBVO>();

	private boolean show = false;
	public String jyrb_extend() throws Exception {
		long start = System.currentTimeMillis();
		this.checkDate();
		if(DateUtils.getBetween(this.getStartDate(), new Date())==0){
			this.show = true;//选择当天日期，则日切还没有数据，故显示实时余额
		}
		Date startDate = this.getStartDate();
		Date endDate = DateUtils.getAfter(startDate, 1);
		User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HashMap<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		inParamList.put(1, loginUser.getOrg().getId());
		HashMap<Integer, Integer> outParameter = new LinkedHashMap<Integer, Integer>();
		outParameter.put(2, Types.VARCHAR);
		HashMap<Integer, Object> results = this.accountDealService.callProcedureForParameters("PKG_Account.getOrgChildren", inParamList, outParameter);
		JYRBVO vo = new JYRBVO();
		if(null!=results&&results.size()==1){
//			System.out.println(results.get(2).toString());
			this.jyrb = this.accountDealService.jyrb_extend(startDate, endDate, results.get(2).toString());
			for (JYRBVO o : this.jyrb) {
				vo.setSum_balance(vo.getSum_balance() + o.getSum_balance());
				vo.setSum_frozen(vo.getSum_frozen() + o.getSum_frozen());
				vo.setSum_in(vo.getSum_in() + o.getSum_in());
				vo.setCount_in(vo.getCount_in() + o.getCount_in());
				vo.setSum_out(vo.getSum_out() + o.getSum_out());
				vo.setCount_out(vo.getCount_out() + o.getCount_out());
				vo.setSum_balance_s(vo.getSum_balance_s()+o.getSum_balance_s());
				vo.setSum_frozen_s(vo.getSum_frozen_s()+o.getSum_frozen_s());
			}
		}
		ServletActionContext.getRequest().setAttribute("hz", vo);
		long m = (System.currentTimeMillis()-start)/1000;
//		System.out.println("共耗时："+m+"秒");
		return "jyrb_extend";
	}

	// 专用账户查询
	public String zyzh_query() throws Exception {
		QueryVO vo = new QueryVO();
		if (value != null && !value.isEmpty()) {
			User u = this.userService.findUser(value.trim());
			if (null != u) {
				vo.setFlag(true);
				vo.setName(u.getRealname());
				vo.setUsername(u.getUsername());
				vo.setBalance(DoubleUtils.doubleRound(u.getUserAccount().getBalance(), 2));
			} else {
				vo.setTip("请选择专用账户");
			}
		} else {
			vo.setTip("请选择专用账户");
		}
		DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
		return null;
	}

	public String daidian_payment_detail() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		// 专用账户用keyword传入
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			User u_zyzh = this.userService.findUser(this.getKeyWord().trim());
			if (null != u_zyzh) {
				// 取专用账户的代垫划出记录
				String hql = " from AccountDeal o where o.checkFlag='20' and o.account.id=" + u_zyzh.getUserAccount().getId() + " and o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMCF + "','" + AccountDeal.ZQMRF + "')";
				hql += " and o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
				hql += " and o.createDate < to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
				hql += " order by o.createDate desc ";
				this.deals = this.accountDealService.getCommonListData(hql);
				for (AccountDeal d : this.deals) {
					d.setBankAccount(this.memberBaseService.getMemByUser(d.getAccount().getUser()).getBankAccount());
					if (d.getType().equals(AccountDeal.CASH) || d.getType().equals(AccountDeal.DDHC)||d.getType().equals(AccountDeal.NBZC)) {// 支出：提现，代垫划出，内部转出 
						this.sum_cash += d.getMoney();
					} else if (d.getType().equals(AccountDeal.CASHCHARGE)||d.getType().equals(AccountDeal.DDHR)||d.getType().equals(AccountDeal.NBZR)) {// 收入：现金充值，代垫划入，内部转入 
						this.sum_charge += d.getMoney();
					}
				}
			}
		}
		return "daidian_payment_detail";
	}

	public String daidian_payment_ui() {
		// 兴易贷:530105;专用账户:孔祥超,53010100269
		return "daidian_payment";
	}

	// 代垫还款，与会员充值类似，从专用账户中扣除金额到指定的融资方账户中。
	public String daidian_payment() {
		QueryVO vo = new QueryVO();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			vo.setTip("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
		}else {
			if (null != id && chargeAmount > 0) {// 金额不能为负数
				if (value != null && !value.isEmpty()) {
					User uuu = this.userService.findUser(id);
					User u_zyzh = this.userService.findUser(value.trim());
					if (uuu == null) {
						vo.setFlag(false);
						vo.setTip("没有这个帐号");
					} else if (u_zyzh == null) {
						vo.setFlag(false);
						vo.setTip("专用账户不存在");
					} else if (u_zyzh.getUserAccount().getBalance() < chargeAmount) {
						vo.setFlag(false);
						vo.setTip("专用账户余额不足");
					} else {
						MemberBase mb = this.memberBaseService.selectById(" from MemberBase where user.id = ?", new Object[] { uuu.getId() });
						Account zyzh = u_zyzh.getUserAccount();
						Account mbzh = uuu.getUserAccount();
						if(null!=this.memo&&!"".equals(this.memo)){
							mbzh.setDaxie(this.memo);// 传递备注信息
						}
						if (mb.getState().equals(MemberBase.STATE_PASSED_AUDIT) && null != mbzh && null != zyzh) {
							try {
								User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
								accountDealService.chargeRecord_zyzh(zyzh, mbzh, chargeAmount, u);
								vo.setId(id);
								vo.setChargeAmount(chargeAmount);
								vo.setBalance(DoubleUtils.doubleRound(this.userService.findUser(id).getUserAccount().getBalance(), 2));
								vo.setFlag(true);
							} catch (Exception e) {
								e.printStackTrace();
								vo.setFlag(false);
							}
						} else {
							vo.setFlag(false);
							vo.setTip("该帐号未启用");
						}
					}
				} else {
					vo.setTip("请选择专用账户");
				}
			} else {
				vo.setTip("用户名或金额错误");
			}
		}
		try {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String in_member_username;// 转入会员
	private String out_member_username;// 转出会员

	public String internalTransfer_ui() {
		if (null != in_member_username && !"".equals(in_member_username) && null != out_member_username && !"".equals(out_member_username)) {
			User in_user = this.userService.findUser(in_member_username);
			User out_user = this.userService.findUser(out_member_username);
			if (null != in_user && null != out_user) {
				List<QueryVO> vos = new ArrayList<QueryVO>();
				QueryVO vo1 = new QueryVO();// 转入会员
				vo1.setBank(this.memberBaseService.getMemByUser(in_user).getBankAccount());
				vo1.setBalance(in_user.getUserAccount().getBalance());
				vo1.setFrozenAmount(in_user.getUserAccount().getFrozenAmount());
				vo1.setUsername(in_user.getUsername());
				vo1.setName(in_user.getRealname());
				vo1.setTip("转入会员");
				QueryVO vo2 = new QueryVO();// 转出会员
				vo2.setBank(this.memberBaseService.getMemByUser(out_user).getBankAccount());
				vo2.setBalance(out_user.getUserAccount().getBalance());
				vo2.setFrozenAmount(out_user.getUserAccount().getFrozenAmount());
				vo2.setUsername(out_user.getUsername());
				vo2.setName(out_user.getRealname());
				vo2.setTip("转出会员");
				vos.add(vo2);
				vos.add(vo1);
				ServletActionContext.getRequest().setAttribute("in_out", vos);
			}
		}
		return "internalTransfer";
	}

	public String internalTransfer_do() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			out.write("{\"message\":\"交易市场未开市\"}");
		} else if (state == 2) {
			out.write("{\"message\":\"现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)\"}");
		} else {
			if (null != in_member_username && !"".equals(in_member_username) && null != out_member_username && !"".equals(out_member_username)) {
				if (this.chargeAmount < 0) {
					out.write("{\"message\":\"转账失败，转账金额不能小于0元。\"}");
				} else {
					User in_user = this.userService.findUser(in_member_username);
					User out_user = this.userService.findUser(out_member_username);
					if (null != in_user && null != out_user) {
						out_user.getUserAccount().setDaxie(memo);
						in_user.getUserAccount().setDaxie(memo);
						this.accountDealService.internalTransfer(out_user.getUserAccount(), in_user.getUserAccount(), chargeAmount, u);
						out.write("{\"message\":\"success\"}");
					} else {
						out.write("{\"message\":\"转账失败，转出会员账户或转入会员账户错误。\"}");
					}
				}
			} else {
				out.write("{\"message\":\"转账失败，转出会员账户或转入会员账户错误。\"}");
			}
		}
		return null;
	}

	// 待审核转账，列出“内部转出”的记录
	public String internalTransfer_check() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		// 取内部转出的记录，转出账户对应转出记录
		String hql = " from AccountDeal o where o.checkFlag2='0' and o.checkFlag = '22' and o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMRF + "','" + AccountDeal.ZQMCF + "')";
		hql += " and o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
		hql += " and o.createDate < to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		hql += " order by o.createDate desc ";
		this.deals = this.accountDealService.getCommonListData(hql);
		for (AccountDeal d : this.deals) {
			d.setBankAccount(this.memberBaseService.getMemByUser(d.getAccount().getUser()).getBankAccount());
			d.getAccountDeal().setBankAccount(this.memberBaseService.getMemByUser(d.getAccountDeal().getAccount().getUser()).getBankAccount());
			this.sum_cash += d.getMoney();
		}
		return "internalTransfer_check";
	}

	// 内部转帐 审核通过
	public String nbzzCheckToPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad1 = this.accountDealService.selectById(this.id);// 转出账户
		AccountDeal ad2 = this.accountDealService.selectById(ad1.getAccountDeal().getId());// 转入账户
		if (!"0".equals(ad1.getCheckFlag2())) {// checkFlag2=0表示未审核,checkFlag2=3表示已审核
			System.out.println("此转账已经审核无需再审核");
		} else {
			ad1.setCheckFlag2("3");
			ad1.setCheckUser(u);
			ad1.setCheckDate(new Date());
			ad1.setSuccessFlag(true);
			ad1.setSuccessDate(ad1.getCheckDate());
			ad1.setSignBank(ad1.getAccount().getUser().getSignBank());//签约行
			ad1.setSignType(ad1.getAccount().getUser().getSignType());//签约类型
			ad1.setTxDir(2);//交易转账方向
			ad1.setChannel(ad1.getAccount().getUser().getChannel());//手工专户
			ad1.setPreMoney(ad1.getAccount().getBalance()+ad1.getAccount().getFrozenAmount());
			ad1.setNextMoney(ad1.getPreMoney()-ad1.getMoney());
			ad1.setTime(ad1.getCheckDate().getTime());

			ad2.setCheckFlag2("3");
			ad2.setCheckUser(u);
			ad2.setCheckDate(new Date());
			ad2.setSuccessFlag(true);
			ad2.setSuccessDate(ad2.getCheckDate());
			ad2.setSignBank(ad2.getAccount().getUser().getSignBank());//签约行
			ad2.setSignType(ad2.getAccount().getUser().getSignType());//签约类型
			ad2.setTxDir(1);//交易转账方向
			ad2.setChannel(ad2.getAccount().getUser().getChannel());//手工专户
			ad2.setPreMoney(ad2.getAccount().getBalance()+ad2.getAccount().getFrozenAmount());
			ad2.setNextMoney(ad2.getPreMoney()+ad2.getMoney());
			try {
				this.accountService.addMoney(ad2.getAccount(), ad2.getMoney());// 转入账户加钱
				this.accountService.loseMoney(ad1.getAccount(), ad1.getMoney());// 转出账户减钱
				this.accountDealService.update(ad1);
				this.accountDealService.update(ad2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return internalTransfer_check();
	}

	// 内部转帐 审核驳回
	public String nbzzCheckToNoPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad1 = this.accountDealService.selectById(this.id);// 转出账户
		AccountDeal ad2 = this.accountDealService.selectById(ad1.getAccountDeal().getId());// 转入账户
		if (!"0".equals(ad1.getCheckFlag2())) {// checkFlag2=0表示未审核,checkFlag2=3表示已审核
			System.out.println("此转账已经审核无需再审核");
		} else {
			ad1.setCheckFlag2("4");
			ad1.setCheckUser(u);
			ad1.setCheckDate(new Date());
			ad2.setCheckFlag2("4");
			ad2.setCheckUser(u);
			ad2.setCheckDate(new Date());
		}
		try {
			this.accountDealService.update(ad1);
			this.accountDealService.update(ad2);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return internalTransfer_check();
	}

	public String internalTransfer_detail() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		// 取内部转出的记录，转出账户对应转出记录
		String hql = " from AccountDeal o where o.checkFlag2='3' and o.checkFlag = '22' and o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMRF + "','" + AccountDeal.ZQMCF + "')";
		hql += " and o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
		hql += " and o.createDate < to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		hql += " order by o.createDate desc ";
		this.deals = this.accountDealService.getCommonListData(hql);
		for (AccountDeal d : this.deals) {
			d.setBankAccount(this.memberBaseService.getMemByUser(d.getAccount().getUser()).getBankAccount());
			d.getAccountDeal().setBankAccount(this.memberBaseService.getMemByUser(d.getAccountDeal().getAccount().getUser()).getBankAccount());
			this.sum_cash += d.getMoney();
		}
		return "internalTransfer_detail";
	}
	
	/**
	 * 打印凭证:代垫与内转的
	 * @return
	 */
	public String print_voucher_dn() {
		try {
			this.deals.clear();
			HttpServletRequest request = ServletActionContext.getRequest();
			if (this.ids != null && this.ids.length > 0) {
				for (String id : ids) {
					AccountDeal out = null;//转出流水
					AccountDeal in = null;//转入流水
					out = this.accountDealService.selectById(id);
					if (out != null) {
						in = out.getAccountDeal();
						User user_out = out.getAccount().getUser();
						MemberBase mb_out = this.memberBaseService.getMemByUser(user_out);
						out.setBankAccount(mb_out.getBankAccount());
						BankLibrary bl = mb_out.getBanklib();
						out.setBank(bl == null ? null : bl.getCaption());
						out.setMoney_upcase(MoneyFormat.format(String.format("%.2f", out.getMoney()), true));
						
						User user_in = in.getAccount().getUser();
						MemberBase mb_in = this.memberBaseService.getMemByUser(user_in);
						in.setBankAccount(mb_in.getBankAccount());
						
						out.setAccountDeal(in);
						request.setAttribute("date", new Date());
					}
					this.deals.add(out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "print_voucher_dn";
	}

	// 融资款交割列表
	public String delivery() {
		try {
			PageView<FinancingCost> pageView = new PageView<FinancingCost>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			sb.append(" and ");
			sb.append(" o.state ='1'");// 只能交割已核算的
			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {

				sb.append(" and o.financingBase.createBy.org.coding like '" + orgCode + "%' and o.financingBase.dilivery='0' ");
			} else {
				sb.append(" and o.financingBase.createBy.org.coding like '@@@@@@@%' and o.financingBase.dilivery='0' ");
			}
			pageView.setQueryResult(financingCostService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "delivery";
	}

	public String doDelivery() {
		// 交割：中心账户扣钱，融资方账户加钱，融资项目变成已交割状态
		String r = "0";
		if (null != id) {
			this.cost = financingCostService.selectById(id);
			if (null != this.cost) {
				String fid = cost.getFinancingBase().getId();
				long rid = cost.getFinancier().getUser().getUserAccount().getId();
				double money = cost.getRealAmount();
				double bzj = cost.getBzj();
				if (money > 0) {
					this.accountDealService.rzkjg(money, bzj, fid, rid);
					r = "1";
				}
			}
		}
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(), r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 充值，需要审核
	public String charge() {
		QueryVO vo = new QueryVO();
		//首先：金额为负数，提示必须输入备注信息
		if ((null == this.memo || "".equals(this.memo)) && this.chargeAmount < 0) {
			vo.setTip("金额为负，请务必填写备注信息。");
		} else {
			//其次：用户id必须不为空，金额不能为0
			if (null != id &&!"".equals(id) && chargeAmount != 0) {
				User uuu = this.userService.findUser(id);
				//再次：用户id必须找到user
				if (uuu == null) {
					vo.setFlag(false);
					vo.setTip("没有这个帐号");
				} else {
					//验证开市与休市
					boolean r = uuu.getUserType().equals("R");//是融资方吗
					//是融资方则休市后清算前也可以现金充值
					byte state = this.openCloseDealService.checkState();
					//state=1;开市
					//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
					//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
					//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
					//state=5;开夜市：只能投标
					//state=6;休夜市：一切业务停止
					if (state == 2 && !r) {//已休市且不是融资方
						vo.setTip("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
					}else if(state == 3 || state == 4 ||state == 5||state==6){//已清算,对账
						vo.setTip("交易市场未开市");
					}else {
						int s = 0;
						if(null != this.huidan && !"".equals(this.huidan)){//界面输入了回单号，则验证回单号
							this.setHuidan(this.huidan.trim());
							s = this.accountDealService.getHuiDan(uuu.getUserAccount(), this.huidan);
						}
						if(s>0){
							vo.setTip("回单号:"+this.huidan+"已经充值了，请仔细核对回单号是否重复");
						}else{
							if("1".equals(uuu.getFlag())){
								vo.setFlag(false);
								vo.setTip("三方存管签约中，不能充值");
							}else if("2".equals(uuu.getFlag())){
								vo.setFlag(false);
								vo.setTip("三方存管已签约，不能充值");
							}else{
								MemberBase mb = this.memberBaseService.selectById(" from MemberBase where user.id = ?", new Object[] { uuu.getId() });
								account = uuu.getUserAccount();
								if (mb.getState().equals(MemberBase.STATE_PASSED_AUDIT) && null != account) {
									try {
										if (null == this.type || "".equals(this.type)) {
											this.type = "1";
										}
										if (null == this.qudao || "".equals(this.qudao)) {
											this.qudao = "0";
										}
										int t = Integer.parseInt(this.type);// 传递类型，1，21，22等
										int q = Integer.parseInt(this.qudao);// 传递类型，0，1，2等
										User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
										account.setDaxie(this.memo);// 传递备注信息
										account.setPassword(this.huidan);//传递回单信息
										accountDealService.chargeRecord(account, chargeAmount, u,t);
										vo.setId(id);
										vo.setChargeAmount(chargeAmount);
										vo.setBalance(this.userService.findUser(id).getUserAccount().getBalance());
										vo.setFlag(true);
									} catch (Exception e) {
										e.printStackTrace();
										vo.setFlag(false);
									}
								} else {
									vo.setFlag(false);
									vo.setTip("该帐号未启用");
								}
							}
						}
					}
				}
			} else {
				vo.setTip("用户名或金额错误");
			}
		}
		try {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private File upload;

	// 会员现金充值批量导入
	public String charge_import() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			out.write("{\"message\":\"交易市场未开市\"}");
			return null;
		} else if (state == 2) {
			out.write("{\"message\":\"现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)\"}");
			return null;
		}
		List<ChargeImport> bs = new ExecelImportUtil(upload).readChargeImportFromSheet(1);
		if (null != bs) {
			int count = 0;
			double sum = 0.0;
			for (ChargeImport c : bs) {
				User uuu = this.userService.findUser(c.getUsername().trim());
				if(null!=uuu){
					Account a = uuu.getUserAccount();
					if(null!=a){
						a.setDaxie(c.getMemo());
						if (null != uuu && !"1".equals(uuu.getFlag())) {//三方存管签约中不能导入
							count += 1;
							sum +=c.getMoney();
							this.accountDealService.chargeRecord_import(a, c.getMoney(), u);
						}
					}
				}
			}
			out.write("{\"message\":\"success\",\"tip\":\"共:"+count+"笔，共计:"+DoubleUtils.doubleCheck2(sum, 2)+"元\"}");
		} else {
			out.write("{\"message\":\"导入错误，请核实excel文件格式后重新导入\"}");
		}
		return null;
	}
	
	// 会员利息批量导入
	public String lx_import() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			out.write("{\"message\":\"交易市场未开市\"}");
			return null;
		} else if (state == 2) {
			out.write("{\"message\":\"现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)\"}");
			return null;
		}
		List<ChargeImport> bs = new ExecelImportUtil(upload).readLXImportFromSheet(1);
		if (null != bs) {
			int count = 0;
			double sum = 0.0;
			for (ChargeImport c : bs) {
				User uuu = this.userService.findUser(c.getUsername().trim());
				Account a = uuu.getUserAccount();
				a.setDaxie(c.getMemo());
				count += 1;
				sum +=c.getMoney();
				this.accountDealService.lx_import(a, c.getMoney(), u);
			}
			out.write("{\"message\":\"success\",\"tip\":\"共:"+count+"笔，共计:"+DoubleUtils.doubleCheck2(sum, 3)+"元\"}");
		} else {
			out.write("{\"message\":\"导入错误，请核实excel文件格式后重新导入\"}");
		}
		return null;
	}
	
	//兴易贷批量划拨
	public String xyd_batch() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		List<ChargeImport> bs = new ExecelImportUtil(upload).readXYDImportFromSheet(1);
		if (null != bs) {
			int count = 0;
			double sum = 0.0;
			for (ChargeImport c : bs) {
				User uuu = this.userService.findUser(c.getUsername().trim());
				Account a = uuu.getUserAccount();
				a.setDaxie(c.getMemo());
				count += 1;
				sum +=c.getMoney();
				this.accountDealService.xyd_import(a, c.getMoney(), u);
			}
			out.write("{\"message\":\"success\",\"tip\":\"共:"+count+"笔，共计:"+DoubleUtils.doubleCheck2(sum, 2)+"元\"}");
		} else {
			out.write("{\"message\":\"导入错误，请核实excel文件格式后重新导入\"}");
		}
		return null;
	}
	
	public String checkXYD(){
		return "checkXYD";
	}
	
	public String checkXYD_data() {
		this.load = true;
		String disUrl = null;
		this.checkDate();
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		DecimalFormat df = new DecimalFormat("#0.00");
		if(this.load){
			try {
				Account xyd = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
				if(null==xyd){
					List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("tip", "兴易贷归集账户出错，请联系管理员");
					o.element("total", 0);
					o.element("rows", object);
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("TYPE", "合计");
					System.out.println(df.format(0));
					_totalDate.element("MONEY", df.format(0));
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else{
					StringBuilder subsql = new StringBuilder(" 1 = 1 ");
					subsql.append(" and checkflag = '40' and businessflag = '24' ");
					ArrayList<Object> args_list = new ArrayList<Object>();
					Object [] args = args_list.toArray();
					if(this.action==0){//查询
						double balance = xyd.getBalance();
						ServletActionContext.getRequest().setAttribute("bb", balance);
						HttpServletRequest request = ServletActionContext.getRequest();
						int rows = Integer.parseInt(request.getParameter("rows"));
						List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by time_ desc,checkdate desc",args, getPage(), rows);
						int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
						
						JSONArray object = JSONArray.fromObject(result);
						JSONObject o = new JSONObject();
						o.element("tip", "兴易贷归集账户:"+xyd.getUser().getUsername()+",可用余额:"+df.format(xyd.getBalance()));
						o.element("total", total);
						o.element("rows", object);
						double totalData = this.accountDealService.XYD(subsql.toString(),args);
						JSONArray footer = new JSONArray();
						JSONObject _totalDate = new JSONObject();
						_totalDate.element("TYPE", "合计");
						_totalDate.element("MONEY", df.format(totalData));
						footer.add(_totalDate);
						o.element("footer", footer);
						ServletActionContext.getResponse().getWriter().write(o.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	public String checkXYD_pass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String msg = "";
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			msg = "交易市场未开市";
		} else if (state == 2) {
			msg = "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)";
		}else{
			Account xyd = this.accountService.thirdPartyAccount(Account.THIRDPARTY_XYD);
			if(null!=xyd){
				double balance = xyd.getBalance();
				double zjbr = this.accountDealService.XYD(" 1 = 1  and checkflag = '40' and businessflag = '24' ");
				if(balance>=zjbr){//归集账户余额足够
					AccountDeal adc = new AccountDeal();
					adc.setAccount(xyd);
					adc.setMoney(DoubleUtils.doubleCheck(zjbr, 2));// 发生额
					adc.setPreMoney(xyd.getBalance() + xyd.getFrozenAmount());
					adc.setNextMoney(adc.getPreMoney()-adc.getMoney());
					adc.setType(AccountDeal.ZJBC);//资金拨出
					adc.setUser(u);//操作者
					adc.setCheckUser(u);//审核者
					adc.setCheckFlag("43");
					adc.setBusinessFlag(25);
					adc.setSignBank(adc.getAccount().getUser().getSignBank());//签约行
					adc.setSignType(adc.getAccount().getUser().getSignType());//签约类型
					adc.setTxDir(2);//交易转账方向
					adc.setChannel(adc.getAccount().getUser().getChannel());//手工专户
					adc.setBatchFlag("1");
					adc.setCheckDate(new Date());
					adc.setSuccessDate(adc.getCheckDate());
					adc.setSuccessFlag(true);
					boolean b = this.accountService.loseMoney(xyd, zjbr);
					if(b){
						try {
							this.accountDealService.insert(adc);
							String hql = " from AccountDeal o where o.checkFlag='40' and o.type = '" + AccountDeal.ZJBR + "' ";
							this.deals = this.accountDealService.getCommonListData(hql);
							for (AccountDeal a : deals) {
								AccountDeal ad = this.accountDealService.selectById(a.getId());
								if (!"40".equals(ad.getCheckFlag())) {// 待审核标志不为40，可能已经被审核掉了。
									System.out.println("此资金拨入已经审核无需再审核");
								} else {
									ad.setCheckFlag("41");
									ad.setCheckUser(u);
									ad.setCheckDate(new Date());
									ad.setSuccessFlag(true);
									ad.setSuccessDate(ad.getCheckDate());
									ad.setSignBank(ad.getAccount().getUser().getSignBank());//签约行
									ad.setSignType(ad.getAccount().getUser().getSignType());//签约类型
									ad.setTxDir(1);//交易转账方向
									ad.setChannel(ad.getAccount().getUser().getChannel());//手工专户
									ad.setPreMoney(ad.getAccount().getBalance()+ad.getAccount().getFrozenAmount());
									ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
									ad.setTime(ad.getCheckDate().getTime());
									try {
										this.accountService.addMoney(ad.getAccount(), ad.getMoney());
										this.accountDealService.update(ad);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							msg = "操作成功";
						} catch (EngineException e) {//出现异常，则把资金还给归集账户
							try {
								this.accountService.addMoney(xyd, zjbr);
							} catch (StaleObjectStateException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (EngineException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							msg = "操作失败，请重试";
						}
					}else{
						msg = "操作失败，请重试";
					}
				}else{
					msg = "兴易贷归集账户资金不足，请先给归集账户充值";
				}
			}else{
				msg = "兴易贷归集账户出错，请联系管理员";
			}
		}
		ActionContext.getContext().getSession().put(Constant.MESSAGETIP, msg);
		return "checkXYD_do";
	}
	
	public String checkXYD_nopass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String hql = " from AccountDeal o where o.checkFlag='40' and o.type = '" + AccountDeal.ZJBR + "' ";
		this.deals = this.accountDealService.getCommonListData(hql);
		for (AccountDeal a : deals) {
			AccountDeal ad = this.accountDealService.selectById(a.getId());
			if (!"40".equals(ad.getCheckFlag())) {// 待审核标志不为40，可能已经被审核掉了。
				System.out.println("此资金拨入已经审核无需再审核");
			} else {
				ad.setCheckFlag("42");
				ad.setCheckUser(u);
				ad.setCheckDate(new Date());
				try {
					//审核驳回，无需为会员账户增加金额
					this.accountDealService.update(ad);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "操作成功");
		return "checkXYD_do";
	}

	// 保证金解冻
	public String bzjThaw() {
		QueryVO vo = new QueryVO();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			vo.setFlag(false);
			vo.setTip("交易市场未开市");
		} else {
			if (null != id && chargeAmount > 0) {
				// Account account = this.accountService.selectByAccountId(id);
				User uuu = this.userService.findUser(id);
				if (uuu == null) {
					vo.setFlag(false);
					vo.setTip("没有这个帐号");
				} else {
					MemberBase mb = this.memberBaseService.selectById(" from MemberBase where user.id = ?", new Object[] { uuu.getId() });
					account = uuu.getUserAccount();
					if (mb.getState().equals(MemberBase.STATE_PASSED_AUDIT) && null != account) {
						try {
							User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
							if (account.getFrozenAmount() >= chargeAmount) {
								accountDealService.bzjThaw(account, chargeAmount, u);
								vo.setId(id);
								vo.setChargeAmount(chargeAmount);
								vo.setBalance(this.userService.findUser(id).getUserAccount().getBalance());
								vo.setFlag(true);
							} else {
								vo.setFlag(false);
								vo.setTip("该帐号冻结金额为:" + account.getFrozenAmount() + "元，不足于解冻:" + chargeAmount + "元。");
							}
						} catch (Exception e) {
							e.printStackTrace();
							vo.setFlag(false);
						}
					} else {
						vo.setFlag(false);
						vo.setTip("该帐号未启用");
					}
				}
			}
		}
		try {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 融资方保证金解冻查询
	public String query_bzj() {
		try {
			if (id != null) {
				if (Account.CENTER.equals(id)) {
					QueryVO vo = new QueryVO();
					vo.setFlag(false);
					DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vo);
				}
			} else if (value != null && !value.isEmpty()) {
				ArrayList<QueryVO> vos = new ArrayList<QueryVO>();
				if ("name".equals(type)) {
					List<MemberBase> members = this.memberBaseService.getScrollDataCommon("from MemberBase where user.userType='R' and ( pName like '%" + value + "%' or eName like '%" + value + "%' )", new String[] {});
					for (MemberBase mb : members) {
						QueryVO vo = new QueryVO();
						User u = mb.getUser();
						Account account = u.getUserAccount();
						if (null != account) {
							double b = accountService.queryBalance(account);
							Account a = accountService.selectById(account.getId());
							vo.setId(mb.getId());
							vo.setAccount(mb.getBankAccount());
							vo.setBalance(b);
							vo.setFrozenAmount(a.getFrozenAmount());
							vo.setBank(mb.getBank());
							vo.setName("0".equals(mb.getCategory()) ? mb.geteName() : mb.getpName());
							vo.setFlag(true);
							vo.setUsername(u.getUsername());
							vos.add(vo);
						}

					}
				} else if ("username".equals(type)) {
					User u = this.userService.findUser(value, true, "R");
					QueryVO vo = new QueryVO();
					if (u != null) {
						Account account = u.getUserAccount();
						if (null != account) {
							double b = accountService.queryBalance(account);
							Account a = accountService.selectById(account.getId());
							MemberBase m = memberBaseService.getMemByUser(u);
							vo.setId(m.getId());
							vo.setAccount(m.getBankAccount());
							vo.setBalance(b);
							vo.setFrozenAmount(a.getFrozenAmount());
							vo.setBank(m.getBank());
							vo.setName("0".equals(m.getCategory()) ? m.geteName() : m.getpName());
							vo.setFlag(true);
							vo.setUsername(u.getUsername());
							vos.add(vo);
						}
					}

				} else if ("bankaccount".equals(type)) {
					List<MemberBase> members = this.memberBaseService.getScrollDataCommon("from MemberBase where user.userType='R' and bankAccount like '%" + value + "%'", new String[] {});

					for (MemberBase mb : members) {
						QueryVO vo = new QueryVO();
						User u = mb.getUser();
						Account account = u.getUserAccount();
						if (null != account) {
							double b = accountService.queryBalance(account);
							Account a = accountService.selectById(account.getId());
							vo.setId(mb.getId());
							vo.setAccount(mb.getBankAccount());
							vo.setBalance(b);
							vo.setFrozenAmount(a.getFrozenAmount());
							vo.setBank(mb.getBank());
							vo.setName("0".equals(mb.getCategory()) ? mb.geteName() : mb.getpName());
							vo.setFlag(true);
							vo.setUsername(u.getUsername());
							vos.add(vo);
						}

					}

				}
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(), vos);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String chargeStyle;

	// 待审核充值列表，不包含"提现"，2012-8-13去掉时间过滤，2012-10-12新增充值方式过滤
	public String chargeCheckList() {
		String rest = "chargeCheckList";
		if(this.action==2){
			this.channel = 2;
			rest = "chargeCheckList2";
		}else{
			this.channel = 1;
		}
		if(null==this.getChargeStyle()||"".equals(this.getChargeStyle())){
			this.chargeStyle = "single";
		}
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.realname like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		if (null != this.getChargeStyle() && !"".equals(this.getChargeStyle().trim())) {
			if ("batch".equals(this.getChargeStyle())) {
				sb.append(" and o.batchFlag='1'");
			} else if ("single".equals(this.getChargeStyle())) {
				sb.append(" and (o.batchFlag='0' or o.batchFlag is null )");
			} else {
				//
			}
		}
		sb.append(" and ");
		sb.append(" o.checkFlag ='0' and o.channel="+this.channel);
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "'," + "'" + AccountDeal.CASH + "'," + "'" + AccountDeal.ZQMRF + "'" + ",'" + AccountDeal.ZQMCF + "')");
		// sb.append(" and ");
		// sb.append(" o.createDate >= to_date('"+
		// format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		// sb.append(" and ");
		// sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+
		// "','yyyy-MM-dd')");
		try {
			if ("batch".equals(this.getChargeStyle())){
				long c = this.accountDealService.getScrollCount(sb.toString(), params, orderby);
				double s = this.accountDealService.getScrollSum(sb.toString(), params, orderby);
				ServletActionContext.getRequest().setAttribute("tip", "共:"+c+"笔，共计:"+s+"元");
			}
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			List<AccountDeal> accountDeals = pageView.getRecords();
			for (AccountDeal deal : accountDeals) {
				MemberBase mb = this.memberBaseService.getMemByUser(deal.getAccount().getUser());
				if(null!=mb){
					deal.setBankAccount(mb.getBankAccount());
					if(null!=mb.getBanklib()){
						deal.setBank(mb.getBanklib().getCaption());
					}
				}
			}
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}

	// 批量审核通过:根据条件查询出要批量审核的所有记录，然后逐个审核通过。
	public String chargeCheckList_batch_check() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (null != this.getChargeStyle() && "batch".equals(this.getChargeStyle().trim())) {
			String hql = " from AccountDeal o where o.checkFlag='0' and o.batchFlag='1' and o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "'," + "'" + AccountDeal.CASH + "'," + "'" + AccountDeal.ZQMRF + "'" + ",'" + AccountDeal.ZQMCF + "')";
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				hql += " and ( o.account.accountId like '%" + this.getKeyWord() + "%' or o.account.user.username like '%" + this.getKeyWord() + "%' or o.account.user.realname like '%" + this.getKeyWord() + "%' or o.type like '%" + this.getKeyWord() + "%' )";
			}
			this.deals = this.accountDealService.getCommonListData(hql);
			for (AccountDeal a : deals) {
				AccountDeal ad = this.accountDealService.selectById(a.getId());
				if (!"0".equals(ad.getCheckFlag())) {// 待审核标志不为0，可能已经被审核掉了。
					System.out.println("此充值已经审核无需再审核");
				} else {
					ad.setCheckFlag("1");
					ad.setCheckFlag2("1");// 对充值的无影响，解决 已审核提现/充值明细 中的查询问题
					ad.setCheckUser(u);
					ad.setCheckDate(new Date());
					ad.setSuccessFlag(true);
					ad.setSuccessDate(ad.getCheckDate());
					ad.setPreMoney(ad.getAccount().getBalance()+ad.getAccount().getFrozenAmount());
					ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
					ad.setTime(ad.getCheckDate().getTime());
					try {
						this.accountService.addMoney_oldbalance(ad.getAccount(), ad.getMoney());
						this.accountDealService.update(ad);
						// 审核通过发短信
						if (ad.getMoney() > 1) {
							VelocityContext context = new VelocityContext();
							MemberBase memberBase = memberBaseService.getMemByUser(ad.getAccount().getUser());
							SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
							context.put("createDate", format.format(new Date()));
							context.put("money", String.format("%.2f", ad.getMoney()) + "元");
							String content = "";
							if (MemberType.CODE_INVESTORS.equals(memberBase.getMemberType().getCode())){
								content = VelocityUtils.getVelocityString(context, "tzr_rujin.html");
								SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
								SMSNewUtil.sms(memberBase.getMobile(), content.trim(), formats.format(new Date()), "","1");
								//老的短信接口不再使用2014-06-30
								//SMSUtil.sms(memberBase.getMobile(), content.trim());
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "chargeCheckToPass";
	}
	
	// 批量审核驳回:根据条件查询出要批量审核的所有记录，然后逐个审核驳回。
	public String chargeCheckList_batch_check_no() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (null != this.getChargeStyle() && "batch".equals(this.getChargeStyle().trim())) {
			String hql = " from AccountDeal o where o.checkFlag='0' and o.batchFlag='1' and o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "'," + "'" + AccountDeal.CASH + "'," + "'" + AccountDeal.ZQMRF + "'" + ",'" + AccountDeal.ZQMCF + "')";
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				hql += " and ( o.account.accountId like '%" + this.getKeyWord() + "%' or o.account.user.username like '%" + this.getKeyWord() + "%' or o.account.user.realname like '%" + this.getKeyWord() + "%' or o.type like '%" + this.getKeyWord() + "%' )";
			}
			this.deals = this.accountDealService.getCommonListData(hql);
			for (AccountDeal a : deals) {
				AccountDeal ad = this.accountDealService.selectById(a.getId());
				if (!"0".equals(ad.getCheckFlag())) {// 待审核标志不为0，可能已经被审核掉了。
					System.out.println("此充值已经审核无需再审核");
				} else {
					ad.setCheckFlag("2");
					ad.setCheckFlag2("1");// 对充值的无影响，解决 已审核提现/充值明细 中的查询问题
					ad.setCheckUser(u);
					ad.setCheckDate(new Date());
					try {
						//审核驳回，无需为会员账户增加金额
						this.accountDealService.update(ad);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "chargeCheckToPass";
	}

	// 今日充值成功列表
	public String todayChargeList() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.checkFlag ='1'");
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "'," + "'" + AccountDeal.CASH + "'," + "'" + AccountDeal.ZQMRF + "'" + ",'" + AccountDeal.ZQMCF + "')");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "todayChargeList";
	}

	// 充值审核通过
	public String chargeCheckToPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if (!"0".equals(ad.getCheckFlag())) {// 待审核标志不为0，可能已经被审核掉了。
			System.out.println("此充值已经审核无需再审核");
		} else {
			ad.setCheckFlag("1");
			ad.setCheckFlag2("1");// 对充值的无影响，解决 已审核提现/充值明细 中的查询问题
			ad.setCheckUser(u);
			ad.setCheckDate(new Date());
			ad.setSuccessFlag(true);
			ad.setSuccessDate(ad.getCheckDate());
			ad.setPreMoney(ad.getAccount().getBalance()+ad.getAccount().getFrozenAmount());
			ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
			ad.setTime(ad.getCheckDate().getTime());
			try {
				boolean result = this.accountService.addMoney_oldbalance(ad.getAccount(), ad.getMoney());
				if(result){//账户增加金额成功继续执行
					this.accountDealService.update(ad);
					// 审核通过发短信
					if (ad.getMoney() > 1) {
						VelocityContext context = new VelocityContext();
						MemberBase memberBase = memberBaseService.getMemByUser(ad.getAccount().getUser());
						SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
						context.put("createDate", format.format(new Date()));
						context.put("money", String.format("%.2f", ad.getMoney()) + "元");
						String content = "";
						if (MemberType.CODE_INVESTORS.equals(memberBase.getMemberType().getCode())){
							content = VelocityUtils.getVelocityString(context, "tzr_rujin.html");
							SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
							SMSNewUtil.sms(memberBase.getMobile(), content.trim(), formats.format(new Date()), "","1");
							//老的短信接口不再使用2014-06-30
							//SMSUtil.sms(memberBase.getMobile(), content.trim());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "chargeCheckToPass";
	}

	private String memo;
	
	private String huidan;

	// 充值审核驳回
	public String chargeCheckToNoPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if (!"0".equals(ad.getCheckFlag())) {// 待审核标志不为0，可能已经被审核掉了。
			System.out.println("此充值已经审核无需再审核");
		}else{
			ad.setCheckFlag("2");
			ad.setMemo(memo);
			ad.setCheckDate(new Date());
			ad.setCheckUser(u);
			try {
				this.accountDealService.update(ad);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		}
		return "chargeCheckToNoPass";
	}

	public String toCashForPersonT() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.account =
		// this.accountService.selectById(u.getUserAccount().getId());
		this.account = this.accountService.selectByUserId(u.getId());
		this.member = this.memberBaseService.getMemByUser(u);
		this.userName = u.getUsername();
		return "toCash";
	}

	public String toCashForPersonR() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// this.account =
		// this.accountService.selectById(u.getUserAccount().getId());
		this.account = this.accountService.selectByUserId(u.getId());
		this.member = this.memberBaseService.getMemByUser(u);
		this.userName = u.getUsername();
		// 加载历史提现
		this.account = this.accountService.selectByUserId(u.getId());
		this.member = this.memberBaseService.getMemByUser(u);
		this.userName = u.getUsername();
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag in ('2.4','2.5','3','4','5')");// 提现等待审核；提现审核通过；提现审核驳回
		sb.append(" and ");
		sb.append(" o.type not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "')");
		sb.append(" and ");
		sb.append(" o.account.id='" + this.account.getId() + "'");
		try {
			pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "toCash";
	}
	 
	/**
	 * 融资方自主提现申请
	 * @return
	 * @throws Exception
	 */
	public String toCash() throws Exception {
		Object msg = "";
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.account = this.accountService.selectById(u.getUserAccount().getId());
		this.member = this.memberBaseService.getMemByUser(u);
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {//清算，融资方不允许提现
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), "现在是休市时间,请在开市时间进行操作");
			return null;
		} else if (state == 2) {
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),"现在是休市时间,请在开市时间进行操作");
			 return null;
		}
		// 融资方提现控制--有还款不允许提现
		/*List<FinancingBase> fbs = financingBaseService.getCommonListData("from  FinancingBase o where o.financier.id='" + this.member.getId() + "' and o.state = '7' and o.terminal = 0 ");
		if (null != fbs && fbs.size() > 0) {
			String temp = "";
			for (FinancingBase o : fbs) {
				temp += o.getCode() + "、";
			}
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), "融资项目为:" + temp.substring(0, temp.length() - 1) + "还在还款中，不能提现。");
			return null;
		}*/

		if (this.chargeAmount <= 0d) {
			msg = "提现金额不能为0元或低于0元";
		} else {
			if (this.chargeAmount > this.account.getBalance()) {
				msg = "您的余额不足，请检查您的交易账户余额，再进行申请操作。";
			} else {
				boolean b = this.accountDealService.cash(this.account, this.chargeAmount);
				this.account = this.accountService.selectById(u.getUserAccount().getId());
				if(b){
					msg = "提现申请成功。";
				}else{
					msg = "提现申请异常";
				}
			}
		}
		try {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), msg);
		} catch (Exception e) {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), "申请失败");
			e.printStackTrace();

		}
		return null;
	}
	
	
	/**
	 * 交易部为融资方发起提现申请
	 * @return
	 * @throws Exception
	 */
	public String toCashRz() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：操作成功");
		if (null == this.userName||"".equals(this.userName.trim())) {
			result.element("code", "0"); 
		     result.element("tip", "用户名不能为空!");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			 return null;	 
		}
		
		User u = this.userService.findUser(this.userName);
		if (null == u) {
			 result.element("code", "0"); 
		     result.element("tip", "用户不存在!");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			 return null;	 
		}
		
		if (!"R".equals(u.getUserType())) {
			 result.element("code", "0"); 
		     result.element("tip", "此类用户不允许使用!");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;	 
		}
		
		this.account = this.accountService.selectById(u.getUserAccount().getId());
		this.member = this.memberBaseService.getMemByUser(u);
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			 result.element("code", "0"); 
		     result.element("tip", "交易系统未开市");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			 return null;
		} else if (state == 2) {
			 result.element("code", "0"); 
		     result.element("tip", "现在是休市时间,请在开市时间进行操作");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			 return null;
		}
		try {
			
	    User uo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (this.chargeAmount <= 0d) {
			 result.element("code", "0"); 
		     result.element("tip", "提现金额不能为0元或低于0元");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		} else {
			if (this.chargeAmount > this.account.getBalance()) {
				 result.element("code", "0"); 
			     result.element("tip", "用户的余额不足，请检查用户的交易账户余额，再进行申请操作。");
			     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			} else {
				 this.account.setString1(note);//备注
				 
				 this.account.setString2("2.5_toCachRz");//2.5_toCachRz表示来自交易部的提现申请
				 boolean b = this.accountDealService.cash(this.account, this.chargeAmount,uo);//uo操作人
				 this.account = this.accountService.selectById(u.getUserAccount().getId());
				 if(b){
					 result.element("code", "1"); 
				     result.element("tip", "申请成功，我们会在一个工作日里转账到融资方的银行账户中。");
				 }else{
					 result.element("code", "0"); 
				     result.element("tip", "提现申请异常");
				 }
			     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			}
	    }
		 result.element("code", "1");
		 DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		 return null;
		} catch (Exception e) {
			 result.element("code", "0"); 
		     result.element("tip", "申请失败");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		     return null;
		    
		}
		
	}
	
	public String getCashRzR() throws Exception {
		JSONObject result = new JSONObject(); 
        result.element("code", "1"); 
        result.element("tip", "操作提示：加入用户成功");
		if (null == this.userName||"".equals(this.userName.trim())) {
			 result.element("code", "0"); 
		     result.element("tip", "用户名不能为空!");
		     DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			 return null;	 
		}
		
		User u = this.userService.findUser(this.userName);
		if (null == u) {
			result.element("code", "0"); 
		    result.element("tip", "用户不存在!");
		    DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;	 
		}
		
		if (!"R".equals(u.getUserType())) {
			result.element("code", "0"); 
		    result.element("tip", "此类用户不允许此窗口申请!");
		    DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;	 
		}
		
		this.account = this.accountService.selectById(u.getUserAccount().getId());
		
		result.element("code", "1");  
		result.element("balance", ""+this.account.getBalance()); 
	    result.element("realname", u.getRealname());
	    DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
	    return null;	
		 
	}
	/**
	 * 融资方提现，风管放款待确认列表
	 * @return
	 */
	public String cash_risk_list(){
		try {
			PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
			String sql = " o.checkFlag='2.9' and o.businessFlag = 20 ";
			
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql,null);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cash_risk_list";
	}
	/**
	 * 融资方提现，风管放款确认通过
	 * @return
	 */
	public void cash_risk_makesure(){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		String str = null;
		int code = 0;
		try {
			out = response.getWriter();
			this.accountDealService.qrfk(user,this.id);
			code = 1;
		} catch (IOException e) {
			str = e.getMessage();
		}catch (InvalidDataAccessApiUsageException e) {
			str = e.getMessage();
		}catch (EngineException e) {
			str = e.getMessage();
		}catch (Exception e) {
			str = e.getMessage();
		}
		
		
		JSONObject json = new JSONObject();
		json.element("code", code);
		json.element("str", str);
		out.print(json);
		
	}
	/**
	 * 融资方提现，风管放款确认驳回
	 * @return
	 */
	public void cash_risk_reject(){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter out = null;
		String str = null;
		int code = 0;
		try {
			response.setCharacterEncoding("utf-8");
			request.setCharacterEncoding("utf-8");
			String remark = request.getParameter("remark");
			out = response.getWriter();
			this.accountDealService.bhfk(user,this.id,remark);
			code = 1;
		} catch (IOException e) {
			str = e.getMessage();
		}catch (InvalidDataAccessApiUsageException e) {
			str = e.getMessage();
		}catch (EngineException e) {
			str = e.getMessage();
		}catch (Exception e) {
			str = e.getMessage();
		}
		
		
		JSONObject json = new JSONObject();
		json.element("code", code);
		json.element("str", str);
		out.print(json);
	}
	
	
	// 提现审核列表，2012-8-13去掉时间过滤
	public String cashList() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(null==this.getStartDate()){
			this.setStartDate(new Date());
		}
		Date endDateNext = DateUtils.getAfter(this.getStartDate(), 1);
		String rest = "cashList";
		if(this.action==2){
			this.channel = 2;
			rest = "cashList2";
		}else{
			this.channel = 1;
		}
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if(this.channel==2){
			sb.append(" and o.createDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
		}
		sb.append(" and ");
		sb.append(" o.checkFlag='3' and o.channel="+this.channel);
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.CASH + "'");
		if(this.channel == 2){
			String sums = "select nvl(sum(o.money),0) as amounts from AccountDeal o where";
			String sums_where = sb.toString();
			sums += sums_where;
			List ss = this.accountDealService.getCommonListData(sums);
			Object o = ss.get(0);
			ServletActionContext.getRequest().setAttribute("zje", o.toString());
		}
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}
	
	//工行待审核提现，导出Excel
	public String icbc_tx_excel() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(null==this.getStartDate()){
			this.setStartDate(new Date());
		}
		Date endDateNext = DateUtils.getAfter(this.getStartDate(), 1);
		String rest = "icbc_tx_excel";
		this.channel = 2;
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 2) {//已休市
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder("from AccountDeal o where 1=1 and o.createDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.checkFlag='3' and o.channel="+this.channel);
			sb.append(" and ");
			sb.append(" o.type ='" + AccountDeal.CASH + "'");
			try {
				List<AccountDeal> lists = accountDealService.getCommonListData(sb.toString());
				for (AccountDeal deal : lists) {
					deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
				}
				ServletActionContext.getRequest().setAttribute("list", lists);
				ServletActionContext.getRequest().setAttribute("msg", new Date().getTime()+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			ServletActionContext.getRequest().getSession().setAttribute("MESSAGE", "休市后才能导出Excel");
			rest = "cashList2";
			PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		}
		return rest;
	}
	
	//工行待审核提现，导出Excel
	public String icbc_tx_pass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		if(null!=this.getStartDate()){
			//提现审核日与审核日为同一天，则i=0，就不能执行审核操作
			int i = DateUtils.getBetween(this.getStartDate(), today);
			if(i!=0){
				Date endDateNext = DateUtils.getAfter(this.getStartDate(), 1);
				this.channel = 2;
				
				LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
				orderby.put("id", "desc");
				StringBuilder sb = new StringBuilder("from AccountDeal o where 1=1 and o.createDate between to_date('" + format1.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format1.format(endDateNext) + "','yyyy-MM-dd')");
				sb.append(" and ");
				sb.append(" o.checkFlag='3' and o.channel="+this.channel);
				sb.append(" and ");
				sb.append(" o.type ='" + AccountDeal.CASH + "'");
				try {
					List<AccountDeal> lists = accountDealService.getCommonListData(sb.toString());
					for (AccountDeal deal : lists) {
						if("3".equals(deal.getCheckFlag())){//提现待审核
							Account a = this.accountService.selectById(deal.getAccount().getId());
							double dongjie = DoubleUtils.doubleCheck(a.getFrozenAmount(),2);
							if(dongjie>=deal.getMoney()){
								a.setFrozenAmount(dongjie - deal.getMoney());// 冻结金额减少
								deal.setCheckFlag("4");
								deal.setCheckDate(new Date());
								deal.setCheckUser(u);
								deal.setCheckFlag2("1");
								deal.setHkDate(deal.getCheckDate());
								deal.setHkUser(u);
								deal.setTime(deal.getCheckDate().getTime());
								//审核通过则该记录置为成功状态
								deal.setSuccessFlag(true);
								deal.setSuccessDate(deal.getCheckDate());
								try {
									this.accountService.update(a);
									deal.setNextMoney(a.getBalance()+a.getFrozenAmount());
									this.accountDealService.update(deal);
									// 审核通过发短信
									VelocityContext context = new VelocityContext();
									MemberBase memberBase = memberBaseService.getMemByUser(deal.getAccount().getUser());
									SimpleDateFormat format2 = new SimpleDateFormat("yy年MM月dd日");
									context.put("createDate", format2.format(new Date()));
									context.put("money", String.format("%.2f", deal.getMoney()) + "元");
									String content = "";
									content = VelocityUtils.getVelocityString(context, "tzr_tixian.html");
									SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
									SMSNewUtil.sms(memberBase.getMobile(), content.trim(), formats.format(new Date()), "","1");
									//老的短信接口不再使用2014-06-30
									//SMSUtil.sms(memberBase.getMobile(), content.trim());
								} catch (EngineException e) {
									e.printStackTrace();
								}
							}
						}
					}
					ServletActionContext.getRequest().getSession().setAttribute("MESSAGE", "批量审核通过成功");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				ServletActionContext.getRequest().getSession().setAttribute("MESSAGE", "批量审核失败，请下一交易日审核");
			}
		}else{
			ServletActionContext.getRequest().getSession().setAttribute("MESSAGE", "批量审核失败，必须选择日期");
		}
		return "cashList2";
	}
	
	// 交易部发起的保证金提现
	public String bzjCashList() { 
		this.checkDate(); 
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.checkFlag in('2.4','2.5')");
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.CASH + "'");
		
		if (this.userType != null && !"".equals(this.userType)) {
			sb.append(" and o.account.user.userType = '" + this.userType + "' ");
		} 
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bzjCashList";
	}
	// 保证金提现申请，标记为交易部审核通过
	public String bzjCashCheckToPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if("2.5".equals(ad.getCheckFlag())){
			ad.setCheckFlag("3");
			ad.setCheckDate(new Date());
			ad.setCheckUser(u);
			try {
				this.accountDealService.update(ad);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		}
		return "bzjCashCheckToPass";
	}

	// 交易部保证金提现申请，标记为已驳回，提现申请额返还给会员
	public String bzjCashCheckToNoPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if("2.5".equals(ad.getCheckFlag())){
			Account a = this.accountService.selectById(ad.getAccount().getId());
			a.setBalance(a.getBalance() + ad.getMoney());// 会员可用加钱
			a.setFrozenAmount(a.getFrozenAmount()-ad.getMoney());// 会员冻结减钱
			a.setOld_balance(a.getOld_balance()+ad.getMoney());//可转金额加钱
			ad.setCheckFlag("2.4"); 
			ad.setMemo(memo);
			ad.setCheckDate(new Date());
			ad.setCheckUser(u);
			try {
				this.accountService.update(a);
				ad.setNextMoney(a.getBalance()+a.getFrozenAmount());
				this.accountDealService.update(ad);
			} catch (EngineException e) {
				e.printStackTrace();
			} 
		}
		return "bzjCashCheckToNoPass";
	}
	
	// 提现申请，标记为审核通过
	//2014-1-8修改:审核通过时，将冻结金额扣除
	//2014-3-10修改:审核通过时，资金流水标志为成功
	public String cashCheckToPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if (!"3".equals(ad.getCheckFlag())) {// 待审核标志不为3，可能已经被审核掉了。
			System.out.println("此提现已经审核无需再审核");
		}else{
			Account a = this.accountService.selectById(ad.getAccount().getId());
			double dongjie = DoubleUtils.doubleCheck(a.getFrozenAmount(),2);
			double old = DoubleUtils.doubleCheck(a.getOld_balance(),2);
			if(dongjie>=ad.getMoney()){
				a.setFrozenAmount(dongjie - ad.getMoney());// 冻结金额减少
				ad.setCheckFlag("4");
				ad.setCheckDate(new Date());
				ad.setCheckUser(u);
				ad.setTime(ad.getCheckDate().getTime());
				//审核通过则该记录置为成功状态
				ad.setSuccessFlag(true);
				ad.setSuccessDate(ad.getCheckDate());
				try {
					this.accountService.update(a);
					ad.setNextMoney(a.getBalance()+a.getFrozenAmount());
					this.accountDealService.update(ad);
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}else{//冻结金额不足
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "审核失败,用户冻结余额不足,请联系管理员");
			}
		}
		return "cashCheckToPass";
	}

	// 提现申请，标记为已驳回，提现申请额返还给会员
	//2014-1-8修改:审核驳回时，冻结金额减少，可用金额增加
	public String cashCheckToNoPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		if (!"3".equals(ad.getCheckFlag())) {// 待审核标志不为3，可能已经被审核掉了。
			System.out.println("此提现已经审核无需再审核");
		}else{
			int i = 9999;
			if(2==ad.getChannel()){//工行专户提现，下一交易日审核
				//提现审核日与审核日为同一天，则i=0，就不能执行审核操作
				i = DateUtils.getBetween(ad.getCreateDate(), today); 
			}
			if(i!=0){
				Account a = this.accountService.selectById(ad.getAccount().getId());
				double dongjie = DoubleUtils.doubleCheck(a.getFrozenAmount(),2);
				if(dongjie>=ad.getMoney()){
					ad.setCheckFlag("5");
					ad.setCheckFlag2("2");
					ad.setMemo(memo);
					ad.setCheckDate(new Date());
					ad.setCheckUser(u);
					try {
						this.accountService.thawAccount_old(a, ad.getMoney());
						this.accountDealService.update(ad);
					} catch (EngineException e) {
						e.printStackTrace();
					}
				}else{//冻结金额不足
					ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败,用户冻结余额不足,请联系管理员");
				}
			}else{
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败,请下一交易日审核");
			}
		}
		return "cashCheckToNoPass";
	}

	/** 根据用户id号查询其账户余额 * */
	public String queryAccountBalance() {
		Account account = accountService.selectByUserId(userId);
		double balance = account.getBalance();
		try {
			// 返回账户余额
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), balance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private double sum_charge = 0.0;// 充值汇总，入金汇总
	private double sum_cash = 0.0;// 提现汇总，出金汇总

	private int sum_charge_count = 0;// 充值汇总，入金汇总
	private int sum_cash_count = 0;// 提现汇总，出金汇总

	// 已审核充值明细
	public String chargeList_checked() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String hql = "from AccountDeal o where o.checkFlag = '1' and o.type = '" + AccountDeal.CASHCHARGE + "' ";
		hql += " and o.checkDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
		hql += " and o.checkDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		if (null != this.userType && !"".equals(this.userType) && !"all".equals(this.userType)) {
			hql += " and o.account.user.userType = '" + this.userType + "' ";
		}
		hql += " order by o.checkDate desc ";
		try {
			this.deals = accountDealService.getCommonListData(hql);
			for (AccountDeal d : this.deals) {
				this.sum_charge += d.getMoney();
				MemberBase m = this.memberBaseService.getMemByUser(d.getAccount().getUser());
				d.setBankAccount(m.getBankAccount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "chargeList_checked";
	}

	// 已审核提现明细
	public String cashList_checked() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String hql = "from AccountDeal o where o.checkFlag = '4' and o.type = '" + AccountDeal.CASH + "' ";
		hql += " and o.checkDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		if (null != this.userType && !"".equals(this.userType) && !"all".equals(this.userType)) {
			hql += " and o.account.user.userType = '" + this.userType + "' ";
		}
		hql += " order by o.checkDate desc ";
		try {
			this.deals = accountDealService.getCommonListData(hql);
			for (AccountDeal d : this.deals) {
				this.sum_cash += d.getMoney();
				MemberBase m = this.memberBaseService.getMemByUser(d.getAccount().getUser());
				d.setBankAccount(m.getBankAccount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cashList_checked";
	}

	/*// 已审核提现/充值明细
	public String checkAndCashList_checked() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		if(this.load){
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			String hql_all = "from AccountDeal o where o.checkFlag in ('1','4','37') and o.type in ('" + AccountDeal.CASH + "','" + AccountDeal.CASHCHARGE + "','"+AccountDeal.HQLX+"') ";
			String hql_charge = "from AccountDeal o where checkFlag2='1' and o.checkFlag = '1' and o.type = '" + AccountDeal.CASHCHARGE + "' ";
			String hql_cash = "from AccountDeal o where o.checkFlag = '4' and o.type = '" + AccountDeal.CASH + "' ";
			String hql_lx = "from AccountDeal o where o.checkFlag = '37' and o.type = '" + AccountDeal.HQLX + "' ";
			String hql = "";
			if (null != this.jyType && !"".equals(this.jyType)) {
				if ("charge".equals(this.jyType)) {
					hql = hql_charge;
				} else if ("cash".equals(this.jyType)) {
					hql = hql_cash;
				} else if ("lx".equals(this.jyType)) {
					hql = hql_lx;
				} else {
					hql = hql_all;
				}
			} else {
				hql = hql_all;
			}
			if (null != this.getChargeStyle() && !"".equals(this.getChargeStyle().trim())) {
				if (!"cash".equals(this.jyType)) {// 提现不区分单笔和批量
					if ("batch".equals(this.getChargeStyle())) {
						hql += " and o.batchFlag='1' ";
					} else if ("single".equals(this.getChargeStyle())) {
						hql += " and (o.batchFlag='0' or o.batchFlag is null ) ";
					} else {
						//
					}
				}
			}
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				hql += " and ( o.account.accountId like '%" + this.getKeyWord() + "%' ";
				hql += " or o.account.user.username like '%" + this.getKeyWord() + "%' ";
				hql += " or o.account.user.realname like '%" + this.getKeyWord() + "%' ";
				hql += " or o.type like '%" + this.getKeyWord() + "')";
			}
			if (null != this.getOrgName() && !"".equals(this.getOrgName().trim())) {
				this.setOrgName(this.getOrgName().trim());
				hql += " and o.account.user.org.showCoding = '" + this.getOrgName() + "' ";
			}
			hql += " and o.checkDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
			hql += " and o.checkDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
			if (null != this.userType && !"".equals(this.userType) && !"all".equals(this.userType)) {
				hql += " and o.account.user.userType = '" + this.userType + "' ";
			}
			hql += " order by o.checkDate desc ";
			try {
				this.deals = accountDealService.getCommonListData(hql);
				for (AccountDeal d : this.deals) {
					if (d.getType().equals(AccountDeal.CASH) && "1".equals(d.getCheckFlag2())) {
						this.sum_cash += d.getMoney();
						this.sum_cash_count += 1;
					} else if (d.getType().equals(AccountDeal.CASHCHARGE)||d.getType().equals(AccountDeal.HQLX)) {
						this.sum_charge += d.getMoney();
						this.sum_charge_count += 1;
					}
					MemberBase m = this.memberBaseService.getMemByUser(d.getAccount().getUser());
					d.setBankAccount(m.getBankAccount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "chargeAndCashList_checked";
	}*/
	
	public String checkAndCashList_checked(){
		return "chargeAndCashList_checked";
	}
	
	// 已审核提现/充值明细
	public String checkAndCashList_data() {
		String disUrl = null;
		this.checkDate();
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){ 
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" 1 = 1 and checkdate is not null ");
				ArrayList<Object> args_list = new ArrayList<Object>();
				if (StringUtils.isNotBlank(this.userType) && !"all".equals(this.userType.trim())) {
					this.userType = this.userType.trim();
					subsql.append(" and target_account_user_usertype = ? ");
					args_list.add(this.userType);
				}
				if (null == this.qudao || "".equals(this.qudao)) {
					this.qudao = "0";
				}
				int q = Integer.parseInt(this.qudao);// 传递类型，0，1，2等
				if(q!=0){
					subsql.append(" and channel = "+q+" ");
				}
				if (StringUtils.isNotBlank(this.chargeStyle)) {
					if (!"cash".equals(this.jyType)) {// 提现不区分单笔和批量
						if ("batch".equals(this.chargeStyle)) {
							subsql.append(" and batchflag='1' ");
						} else if ("single".equals(this.chargeStyle)) {
							subsql.append(" and (batchflag='0' or batchflag is null )");
						}
					}
				}
				if (StringUtils.isNotBlank(this.jyType)) {
					if("charge".equals(this.jyType)){//现金充值
						subsql.append(" and checkflag = '1' and businessflag in (1,21,22) ");
					}else if("cash".equals(this.jyType)){//提现
						subsql.append(" and checkflag = '4' and businessflag in (2,20) ");
					}else if("lx".equals(this.jyType)){//活期利息
						subsql.append(" and checkflag = '37' and businessflag = 23 ");
					}else if("hb".equals(this.jyType)){//资金划拨
						subsql.append(" and checkflag in ('41','43') and businessflag in (24,25) ");
					}else{
						subsql.append(" and checkflag in ('1','4','37','41','43') and businessflag in (1,21,22,23,2,20,24,25) ");
					}
				}else{
					subsql.append(" and checkflag in ('1','4','37','41','43') and businessflag in (1,21,22,23,2,20,24,25) ");
				}
				if (StringUtils.isNotBlank(this.getKeyWord())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (");
					subsql.append(" instr(type,? ) > 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_username, ? ) > 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_realname, ? ) > 0 ");
					subsql.append(" )");
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
				}
				subsql.append(" and checkdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				
				
				Object [] args = args_list.toArray();
				
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by time_ desc,checkdate desc",args, getPage(), rows);
					int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.accountDealService.charge_cash(subsql.toString(),args);
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("TYPE", "合计");
					_totalDate.element("MONEY_ADD", totalData[0]);
					_totalDate.element("MONEY_SUBTRACT", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by time_ desc,checkdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "chargeAndCashList_checked_print";
				}else if(this.action==2){//导出excel
					HttpServletRequest request = ServletActionContext.getRequest();
					request.setAttribute("msg", new Date().getTime());
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by time_ desc,checkdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "chargeAndCashList_checked_excel";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	private String orgName;
	
	//导出上月财务入金出金情况
	public String importCashAndChargeExcel() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT sum(rzcz) rzcz, sum(tzcz) tzcz, sum(tztx) tztx, sum(rztx) rztx,sum(cwzj) cwzj,to_char(cfdate,'yyyy-MM-dd') date_ " +
				" FROM KMFEXTEST.v_cashFlowDetail where 1=1 ");
		if(this.getStartDate()!=null && this.getEndDate()!=null){
			sb.append(" and  cfdate between  to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(DateUtils.getAfter(this.getEndDate(), 1)) + "','yyyy-MM-dd') ");
		}
		sb.append(" group by to_char(cfdate, 'yyyy-MM-dd')");
		sb.append(" order by to_char(cfdate, 'yyyy-MM-dd')");
		ArrayList<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
		results = this.memberBaseService.selectListWithJDBC(sb.toString());
		//System.out.println(results.size() + results.get(0).get("rzcz").toString());
		String fileName = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		ServletActionContext.getResponse().setContentType(
				"application/x-xls");
		ServletActionContext.getResponse().setHeader(
				"Content-Disposition",
				"attachment;filename=" + fileName + "CashHistory.xls");
		ServletOutputStream os = ServletActionContext.getResponse()
				.getOutputStream();
		List<String> head = new ArrayList<String>();
		head.add("日期");
		head.add("投资充值");
		head.add("融资充值");
		head.add("投资提现");
		head.add("融资提现");
		head.add("财务转结（需补录）");
		head.add("交易账户余额(默认公式已有，需将前面内容格式修改为数字，目前格式为文本)");
		List<String> paramOrder = new ArrayList<String>();
		paramOrder.add("date_");
		paramOrder.add("tzcz");
		paramOrder.add("rzcz");
		paramOrder.add("tztx");
		paramOrder.add("rztx");
		paramOrder.add("cwzj");
		Map<String,String> expressions = new HashMap<String, String>();
		expressions.put("0206", "0");
		expressions.put("0306", "=G3+B4+C4-D4-E4-F4");
		List theData = results;
		ImportExcelUtil.exportExcelWithDecode("昆投互联网金融交易-招商银行（投、融资充值、提现）日报表 "+format.format(this.getStartDate())+"到"+format.format(this.getEndDate()),os,head,paramOrder,theData,"HashMap",expressions);
		return null;
	}

	//查看上月财务入金出金情况
	public String cashFlow() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT sum(rzcz) rzcz, sum(tzcz) tzcz, sum(tztx) tztx, sum(rztx) rztx,sum(cwzj) cwzj,to_char(cfdate,'yyyy-MM-dd') date_,sum(rzcz)+sum(tzcz)-sum(tztx)-sum(rztx) yu_e  " +
				" FROM KMFEXTEST.v_cashFlowDetail where 1=1 ");
		if(this.getStartDate()!=null && this.getEndDate()!=null){
			sb.append(" and  cfdate between  to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(DateUtils.getAfter(this.getEndDate(), 1)) + "','yyyy-MM-dd') ");
		}
		sb.append(" group by to_char(cfdate, 'yyyy-MM-dd')");
		sb.append(" order by to_char(cfdate, 'yyyy-MM-dd')");
		ArrayList<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
		results = this.memberBaseService.selectListWithJDBC(sb.toString());
		PageForProcedureVO vo = new PageForProcedureVO();
		vo.setResult(results);
		vo.setTotalrecord(results.size());
		ServletActionContext.getRequest().setAttribute("pageView", vo);
		return "cashFlow";
	}
	//查看上月财务入金出金情况
	public String cashFlowEx() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT sum(rzcz) rzcz, sum(tzcz) tzcz, sum(tztx) tztx, sum(rztx) rztx,sum(cwzj) cwzj,to_char(cfdate,'yyyy-MM-dd') date_,sum(rzcz)+sum(tzcz)-sum(tztx)-sum(rztx) yu_e  " +
				" FROM KMFEXTEST.v_cashFlowDetail where 1=1 ");
		if(this.getStartDate()!=null && this.getEndDate()!=null){
			sb.append(" and  cfdate between  to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(DateUtils.getAfter(this.getEndDate(), 1)) + "','yyyy-MM-dd') ");
		}
		sb.append(" group by to_char(cfdate, 'yyyy-MM-dd')");
		sb.append(" order by to_char(cfdate, 'yyyy-MM-dd')");
		ArrayList<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
		results = this.memberBaseService.selectListWithJDBC(sb.toString());
		PageForProcedureVO vo = new PageForProcedureVO();
		vo.setResult(results);
		vo.setTotalrecord(results.size());
		ServletActionContext.getRequest().setAttribute("pageView", vo);
		return "cashFlowEx";
	}
	private double lmsumtzcz;
	private double lmsumrzcz;
	private double lmsumtztx;
	private double lmsumrztx;
	private double lmcwzj;
	private double lmjyzhye;

	public double getLmsumtzcz() {
		return lmsumtzcz;
	}

	public void setLmsumtzcz(String lmsumtzcz) {
		this.lmsumtzcz = Double.parseDouble(lmsumtzcz);
	}

	public double getLmsumrzcz() {
		return lmsumrzcz;
	}

	public void setLmsumrzcz(String lmsumrzcz) {
		this.lmsumrzcz = Double.parseDouble(lmsumrzcz);
	}

	public double getLmsumtztx() {
		return lmsumtztx;
	}

	public void setLmsumtztx(String lmsumtztx) {
		this.lmsumtztx = Double.parseDouble(lmsumtztx);
	}

	public double getLmsumrztx() {
		return lmsumrztx;
	}

	public void setLmsumrztx(String lmsumrztx) {
		this.lmsumrztx =Double.parseDouble(lmsumrztx); 
	}

	public double getLmcwzj() {
		return lmcwzj;
	}

	public void setLmcwzj(String lmcwzj) {
		this.lmcwzj = Double.parseDouble(lmcwzj);
	}

	public double getLmjyzhye() {
		return lmjyzhye;
	}

	public void setLmjyzhye(String lmjyzhye) {
		this.lmjyzhye =Double.parseDouble(lmjyzhye);
	}

	// 已审核提现/充值明细,调用视图数据,会员充值提现
	public String checkAndCashList_checked2(){
		return "chargeAndCashList_checked2";
	}
	
	public void charge_cash_data(){
		this.checkDate();
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgCode = u.getOrg().getShowCoding();
		//530100，5301可以查询所有数据
		if (null != orgCode && ("530100".equals(orgCode)||"5301".equals(orgCode))) {
			orgCode = null;
		}
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				int rows = Integer.parseInt(request.getParameter("rows"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" 1 = 1 ");
				
				ArrayList<Object> args_list = new ArrayList<Object>();
				
				if (StringUtils.isNotBlank(orgCode)) {
					subsql.append(" and instr(target_account_user_orgcode,?)> 0 ");
					args_list.add(orgCode);
				}
				if (null != this.userType && !"".equals(this.userType.trim())&&!"all".equals(this.userType.trim())) {
					this.userType = this.userType.trim();
					subsql.append(" and target_account_user_usertype = ? ");
					args_list.add(this.userType);
				}
				if (null != this.jyType && !"".equals(this.jyType.trim())) {
					if("charge".equals(this.jyType)){//现金充值
						subsql.append(" and businessflag in (1,21,22) ");
					}else if("cash".equals(this.jyType)){//提现
						subsql.append(" and businessflag in (2,20) ");
					}else if("lx".equals(this.jyType)){//活期利息
						subsql.append(" and businessflag = 23 ");
					}else if("charge_hx".equals(this.jyType)){//银转商(入金)
						subsql.append(" and businessflag = 18 ");
					}else if("cash_hx".equals(this.jyType)){//商转银(出金)
						subsql.append(" and businessflag = 19 ");
					}else{
						subsql.append(" and businessflag in (1,21,22,23,18,2,20,19) ");
					}
				}else{
					subsql.append(" and businessflag in (1,21,22,23,18,2,20,19) ");
				}
				if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (");
					subsql.append(" instr(type,? )> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_username,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_realname,?)> 0 ");
					subsql.append(" )");
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
				}
				subsql.append(" and createdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

				Object [] args = args_list.toArray();
				
				List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by createdate desc", args,getPage(), rows);
				int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
				
				
				JSONArray object = JSONArray.fromObject(result);
				JSONObject o = new JSONObject();
				o.element("total", total);
				o.element("rows", object);
				double[] totalData = this.accountDealService.charge_cash(subsql.toString(),args);
				JSONArray footer = new JSONArray();
				JSONObject _totalDate = new JSONObject();
				_totalDate.element("TYPE", "合计");
				_totalDate.element("MONEY_ADD", totalData[0]);
				_totalDate.element("MONEY_SUBTRACT", totalData[1]);
				footer.add(_totalDate);
				o.element("footer", footer);
				ServletActionContext.getResponse().getWriter().write(o.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String charge_cash_excel(){
		this.checkDate();
		SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
		ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String orgCode = u.getOrg().getShowCoding();
		//530100，5301可以查询所有数据
		if (null != orgCode && ("530100".equals(orgCode)||"5301".equals(orgCode))) {
			orgCode = null;
		}
		
		ArrayList<Object> args_list = new ArrayList<Object>();
		
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				HttpServletRequest request = ServletActionContext.getRequest();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" 1 = 1 ");
				if (null != orgCode) {
					subsql.append(" and instr(target_account_user_orgcode,? ) > 0 ");
					args_list.add(orgCode);
				}
				if (null != this.userType && !"".equals(this.userType.trim())&&!"all".equals(this.userType.trim())) {
					this.userType = this.userType.trim();
					subsql.append(" and target_account_user_usertype = ? ");
					args_list.add(this.userType);
				}
				if (null != this.jyType && !"".equals(this.jyType.trim())) {
					if("charge".equals(this.jyType)){//现金充值
						subsql.append(" and businessflag in (1,21,22) ");
					}else if("cash".equals(this.jyType)){//提现
						subsql.append(" and businessflag in (2,20) ");
					}else if("lx".equals(this.jyType)){//活期利息
						subsql.append(" and businessflag = 23 ");
					}else if("charge_hx".equals(this.jyType)){//银转商(入金)
						subsql.append(" and businessflag = 18 ");
					}else if("cash_hx".equals(this.jyType)){//商转银(出金)
						subsql.append(" and businessflag = 19 ");
					}else{
						subsql.append(" and businessflag in (1,21,22,23,18,2,20,19) ");
					}
				}else{
					subsql.append(" and businessflag in (1,21,22,23,18,2,20,19) ");
				}
				if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (");
					subsql.append(" instr(type,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_username,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_realname,?)> 0 ");
					subsql.append(" )");
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
				}
				subsql.append(" and createdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				
				Object [] agrs = args_list.toArray();
				
				List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by createdate desc",agrs);
				double[] totalData = this.accountDealService.charge_cash(subsql.toString(),agrs);
				request.setAttribute("list", result);
				request.setAttribute("totalData", totalData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "charge_cash_excel";
	}
	
	// 结算审核通过后，由出纳来处理：已划款，转账异常

	// 出纳操作，待处理提现申请。取 checkFlag=4 checkFlag=0，2012-8-13去掉时间过滤
	public String waitrequestforcash() {
		String rest = "waitrequestforcash";
		if(this.action==2){
			this.channel = 2;
			rest = "waitrequestforcash2";
		}else{
			this.channel = 1;
		}
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag2='0'");
		sb.append(" and ");
		sb.append(" o.checkFlag='4' and o.channel="+this.channel);
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.CASH + "' ");
		if (this.userType != null && !"".equals(this.userType)&&!"all".equals(this.userType)) {
			sb.append(" and o.account.user.userType = '" + this.userType + "' ");
		}
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				try {
					MemberBase mb=this.memberBaseService.getMemByUser(deal.getAccount().getUser());
					deal.setBankAccount(mb.getBankAccount()); 
					deal.setBank("<br/>开户行:"+mb.getBanklib().getCaption()+"(全称："+mb.getBank()+")");
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			long c = this.accountDealService.getScrollCount(sb.toString(), params, orderby);
			double s = this.accountDealService.getScrollSum(sb.toString(), params, orderby);
			ServletActionContext.getRequest().setAttribute("bishu", c);
			ServletActionContext.getRequest().setAttribute("jine", s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}

	// 出纳操作，已处理提现申请。取 checkFlag=4 checkFlag=1
	public String handlerrequestforcash() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.checkFlag2='1'");
		sb.append(" and ");
		sb.append(" o.checkFlag='4'");
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.CASH + "'");
		sb.append(" and ");
		sb.append(" o.hkDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "handlerrequestforcash";
	}
	/**
	 * 打印凭证
	 * 20130724  
	 * 解决融资方拥有多个融资项目，导致融资凭证打印错误的问题
	 * @return
	 */
	public String print_voucher() {
		try {
			this.deals.clear();
			HttpServletRequest request = ServletActionContext.getRequest();
			if (this.ids != null && this.ids.length > 0) {
				for (String id : ids) {
					this.accountDeal = this.accountDealService.selectById(id);
					if (this.accountDeal != null) {
						User user = this.accountDeal.getAccount().getUser();
						MemberBase mb = this.memberBaseService.getMemByUser(user);
						this.accountDeal.setBankAccount(mb.getBankAccount());
						BankLibrary bl = mb.getBanklib();
						this.accountDeal.setBank(bl == null ? null : bl.getCaption());
						this.accountDeal.setMoney_upcase(MoneyFormat.format(String.format("%.2f", this.accountDeal.getMoney()), true));
						if (this.accountDeal.getBusinessFlag()==20) {//融资提现
							FinancingCost fc = this.financingCostService.getCostByFinancingBase(this.accountDeal.getFinancing().getId());
							request.setAttribute("cost", fc);
						} else if (this.accountDeal.getBusinessFlag()==21) {//融资方还款
							FinancingBase fb = this.financingBaseService.selectById("from FinancingBase f where f.financier.user.id = ? and f.terminal = false and f.state = '7' ", new Object[] { user.getId() });
							if (fb != null) {
								request.setAttribute("fcode", fb.getCode());
							}
						}
						request.setAttribute("date", new Date());
						request.setAttribute("usertype", user.getUserType());
					}
					this.deals.add(this.accountDeal);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "print_voucher";
	}

	private String state;

	// 出纳操作，异常提现及审核驳回。取 checkFlag=4 checkFlag=2,3，2012-8-13去掉时间过滤
	public String exceptionforcash() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		if(null==this.getStartDate()){
			this.setStartDate(DateUtils.getAfter(today, -10));
		}
		if(null==this.getEndDate()){
			this.setEndDate(today);
		}
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("checkDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getState() && !"".equals(this.getState().trim())) {
			if ("exception".equals(this.getState())) {// 异常
				sb.append(" and ");
				sb.append(" ( o.checkFlag2='2' or o.checkFlag2='3' ) ");
				sb.append(" and ");
				sb.append(" o.checkFlag='4' ");
				sb.append(" and ");
			} else if ("reject".equals(this.getState())) {// 驳回
				sb.append(" and ");
				sb.append(" o.checkFlag2='2' ");
				sb.append(" and ");
				sb.append(" o.checkFlag='5' ");
				sb.append(" and ");
			} else {// 全部
				sb.append(" and ");
				sb.append(" ( o.checkFlag2='2' or o.checkFlag2='3' ) ");
				sb.append(" and ");
				sb.append(" ( o.checkFlag='4' or o.checkFlag='5' ) ");
				sb.append(" and ");
			}
		} else {// 全部
			sb.append(" and ");
			sb.append(" ( o.checkFlag2='2' or o.checkFlag2='3' ) ");
			sb.append(" and ");
			sb.append(" ( o.checkFlag='4' or o.checkFlag='5' ) ");
			sb.append(" and ");
		}
		sb.append(" o.type ='" + AccountDeal.CASH + "' ");
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.realname like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		if(this.channel !=0){
			sb.append(" and o.account.user.channel = "+this.channel);
		}
		sb.append(" and ");
		sb.append(" o.checkDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and  to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "exceptionforcash";
	}

	// 出纳，提现处理，已划款
	public String yhk() {
		User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = null;
		FinancingCost fc = null;
		try {
			ad = this.accountDealService.selectById(this.id);
			if("1".equals(ad.getCheckFlag2())){
				System.out.println("此提现已划款，不用重复操作。");
			}else{
				if(null!=ad.getFinancing()){
					fc = this.financingCostService.getCostByFinancingBase(ad.getFinancing().getId());
				}
				this.accountDealService.qrhk(fc, operator, ad);
			}
		} catch (EngineException e) {
			e.printStackTrace();
		}

		try {
			// 审核通过发短信
			VelocityContext context = new VelocityContext();
			MemberBase memberBase = memberBaseService.getMemByUser(ad.getAccount().getUser());
			SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
			context.put("createDate", format.format(new Date()));
			context.put("money", String.format("%.2f", ad.getMoney()) + "元");
			String content = "";
			content = VelocityUtils.getVelocityString(context, "tzr_tixian.html");
			SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
			SMSNewUtil.sms(memberBase.getMobile(), content.trim(), formats.format(new Date()), "","1");
			//老的短信接口不再使用2014-06-30
			//SMSUtil.sms(memberBase.getMobile(), content.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "yhk";
	}

	// 出纳，提现处理，转账异常
	public String zzyc() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		ad.setCheckFlag2("3");// 转账异常;2014-3-10：转账异常后直接进入提现错误状态，结算部无需再次操作。
		if(null!=ad.getMemo()){
			ad.setMemo(ad.getMemo()+memo);//追加备注
		}else{
			ad.setMemo(memo);//追加备注 
		}
		ad.setHkDate(new Date());// 划款日期，转账异常日期
		ad.setHkUser(u);
		
		//冲正提现将资金返还给会员
		AccountDeal newAD = new AccountDeal();
		newAD.setMoney(-ad.getMoney());
		newAD.setType(ad.getType());
		newAD.setAccount(ad.getAccount());
		newAD.setCheckFlag(ad.getCheckFlag());
		newAD.setCheckFlag2("4");//冲正提现
		newAD.setBusinessFlag(ad.getBusinessFlag());
		newAD.setSuccessFlag(true);
		newAD.setSuccessDate(new Date());
		
		newAD.setCreateDate(new Date());
		newAD.setCheckDate(new Date());
		newAD.setHkDate(new Date());
		newAD.setUser(ad.getUser());
		newAD.setCheckUser(ad.getCheckUser());
		newAD.setHkUser(ad.getHkUser());
		newAD.setMemo(DateUtils.formatDate(ad.getCheckDate(), "yyyy年MM月dd日")+ad.getMoney()+"元提现，"+memo);
		
		newAD.setSignBank(ad.getSignBank());
		newAD.setSignType(ad.getSignType());
		newAD.setTxDir(ad.getTxDir());
		newAD.setTxOpt(ad.getTxOpt());
		newAD.setChannel(ad.getChannel());//手工专户
		
		Account a = this.accountService.selectById(ad.getAccount().getId());
		newAD.setPreMoney(a.getBalance()+a.getFrozenAmount());
		newAD.setNextMoney(newAD.getPreMoney()-newAD.getMoney());
		newAD.setTime(new Date().getTime());
		a.setBalance(a.getBalance() + ad.getMoney());// 会员账户加钱
		a.setOld_balance(a.getOld_balance()+ad.getMoney());//可转金额增加
		try {
			this.accountDealService.update(ad);
			this.accountService.update(a);
			this.accountDealService.insert(newAD);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		// return cashList();
		return "zzyc";
	}
	
	private String realName;
	
	//已划款冲正
	public String yhk_cz() {
		this.checkDate();
		return "yhk_cz";
	}
	public String yhk_cz_data() {
		Date endDateNext = DateUtils.getAfter(this.getStartDate(), 1);
		if (null != userName && !"".equals(userName) && null != realName && !"".equals(realName)) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" 1 = 1 and hkdate is not null ");
				subsql.append(" and checkflag = '4' and checkflag2 = '1' and businessflag in (2,20) ");
				subsql.append(" and target_account_user_username = '" + userName + "' and trim(target_account_user_realname) = '" + realName + "' and money = "+ chargeAmount);
				subsql.append(" and hkdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by time_ desc,hkdate desc");
				JSONArray object = JSONArray.fromObject(result);
				JSONObject o = new JSONObject();
				o.element("rows", object);
				ServletActionContext.getResponse().getWriter().write(o.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return null;
	}
	
	public String yhk_cz_do() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (null != id && !"".equals(id)) {
			AccountDeal ad = this.accountDealService.selectById(id);
			if(null!=ad){
				if("4".equals(ad.getCheckFlag())&&"1".equals(ad.getCheckFlag2())&&null!=ad.getHkDate()){
					ad.setCheckFlag2("3");//已还款变更为提现错误
					if(null!=ad.getMemo()){
						ad.setMemo(ad.getMemo()+memo);//追加备注
					}else{
						ad.setMemo(memo);//追加备注
					}
					
					//冲正提现将资金返还给会员
					AccountDeal newAD = new AccountDeal();
					newAD.setMoney(-ad.getMoney());
					newAD.setType(ad.getType());
					newAD.setAccount(ad.getAccount());
					newAD.setCheckFlag(ad.getCheckFlag());
					newAD.setCheckFlag2("4");//冲正提现
					newAD.setBusinessFlag(ad.getBusinessFlag());
					newAD.setSuccessFlag(true);
					newAD.setSuccessDate(new Date());
					
					newAD.setCreateDate(new Date());
					newAD.setCheckDate(new Date());
					newAD.setHkDate(new Date());
					newAD.setUser(ad.getUser());
					newAD.setCheckUser(ad.getCheckUser());
					newAD.setHkUser(ad.getHkUser());
					newAD.setMemo(DateUtils.formatDate(ad.getCheckDate(), "yyyy年MM月dd日")+ad.getMoney()+"元提现，"+memo);
					
					newAD.setSignBank(ad.getSignBank());
					newAD.setSignType(ad.getSignType());
					newAD.setTxDir(ad.getTxDir());
					newAD.setTxOpt(ad.getTxOpt());
					newAD.setChannel(ad.getChannel());//手工专户
					
					Account a = this.accountService.selectById(ad.getAccount().getId());
					newAD.setPreMoney(a.getBalance()+a.getFrozenAmount());
					newAD.setNextMoney(newAD.getPreMoney()-newAD.getMoney());
					newAD.setTime(new Date().getTime());
					a.setBalance(a.getBalance() + ad.getMoney());// 会员账户加钱
					a.setOld_balance(a.getOld_balance() + ad.getMoney());//可转金额增加
					try {
						this.accountDealService.update(ad);
						this.accountService.update(a);
						this.accountDealService.insert(newAD);
						//融资项目提现的冲正，将txed由1变更为0
						if(null!=ad.getFinancing()&&null!=ad.getFinancing().getId()&&!"".equals(ad.getFinancing().getId())){
							FinancingBase f = this.financingBaseService.selectById(ad.getFinancing().getId());
							if(f.isTxed()){
								f.setTxed(false);
								this.financingBaseService.update(f);
							}
						}
					} catch (EngineException e) {
						e.printStackTrace();
					}
				}else{
					out.write("{\"success\":\"false\",\"message\":\"冲正失败，参数错误，请重新查询后再试。\"}");
				}
			}else{
				out.write("{\"success\":\"false\",\"message\":\"冲正失败，参数错误，请重新查询后再试。\"}");
			}
			out.write("{\"success\":\"true\",\"message\":\"冲正成功\"}");
		} else {
			out.write("{\"success\":\"false\",\"message\":\"冲正失败，参数错误，请重新查询后再试。\"}");
		}
		return null;
	}

	//2014-3-10取消了此项操作，结算部做转账异常操作后，就置为提现错误状态
	// 结算针对转账异常的提现，处理为提现错误，提现额返还给会员
	public String cashError() {
		AccountDeal ad = this.accountDealService.selectById(this.id);
		Account a = this.accountService.selectById(ad.getAccount().getId());
		a.setBalance(a.getBalance() + ad.getMoney());// 会员账户加钱
		a.setOld_balance(a.getOld_balance() + ad.getMoney());
		ad.setNextMoney(ad.getNextMoney() + ad.getMoney());
		ad.setCheckFlag2("3");// 由转账异常变成提现错误
		try {
			this.accountDealService.update(ad);
			this.accountService.update(a);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return exceptionforcash();
	}

	// 交易部查询会员提现进展
	public String cashProcess() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.CASH + "'");
		sb.append(" and ");
		sb.append(" o.createDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cashProcess";
	}

	private int bank = -1;//三方存管签约行，全部签约:-1；未签约三方存管=0,签约华夏三方存管=1,签约招商三方存管=2

	// 签约用户出金待审核列表
	public String check_out_list() {
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" and ( o.account.accountId like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.realname like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.type like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.checkFlag='25'");
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.ZQ2BANK + "'");
		if(this.bank!=-1){
			sb.append(" and o.account.user.signBank=" + this.bank + " ");
		}
		if (this.userType != null && !"".equals(this.userType)) {
			sb.append(" and o.account.user.userType = '" + this.userType + "' ");
		}
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "check_out_list";
	}
	
	// 出金，调用三方接口，再更新为审核通过。华夏接口2013年12月17日正式停用
	//招行在交易所发起出金时，后台审核通过，调用此方法，调用6201接口
	public String outGoldenToPass() {
		User u = getLoginUser();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		double pre = ad.getPreMoney();
		double next = ad.getNextMoney();
		//25为待审核状态
		if(!"25".equals(ad.getCheckFlag())){//不是25，则不允许审核
			CMBVO vo = new CMBVO();
			vo.setMsg("该笔出金已经审核通过，请勿重复审核。");
			vo.setSuccess(false);
			try {
				PrintWriter out = getOut();
				out.write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{//是25，则进行审核
			//HxbankVO vo = new HxbankVO();
			CMBVO vo = new CMBVO();
			String txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
			String txTime = DateUtils.formatDate(new Date(), "HHmmss");
			MemberBase mb = this.memberBaseService.getMemByUser(ad.getAccount().getUser());
			String bankaccount = mb.getBankAccount();//银行账号
			String idtype = "";//证件类型
			String idcard = "";//证件号
			boolean qiye = "0".equals(mb.getCategory());
			if(qiye){
				idtype = "C09";//组织机构证
				idcard = mb.geteOrgCode();//组织机构编码
			}else{
				idtype = "P01";//身份证
				idcard = mb.getIdCardNo();
			}
			MerChantRequest6201 request6201 = new MerChantRequest6201();
			request6201.setCoSerial(DateUtils.generateNo20());
			request6201.setCurCode("CNY");
			request6201.setCurFlag("1");
			request6201.setCountry("CHN");
			String formatAmount = DoubleUtils.formatDouble(ad.getMoney());
			request6201.setAmount(formatAmount);
			request6201.setBankAcc(bankaccount);
			request6201.setCustName(ad.getAccount().getUser().getRealname());
			request6201.setFundAcc(ad.getAccount().getUser().getUsername());
			request6201.setIDNo(idcard);
			request6201.setIDType(idtype);
			//首先去变更ad为审核通过
			boolean b = false;
			try {
				b = this.accountDealService.checkOutGolden(ad,u);
			} catch (Exception e) {
				vo.setName("出金");
				vo.setTxCode("6201");
				vo.setMsg("出金失败");
			}
			if(b){//变更为审核通过成功，再去调用招行接口出金
				try {
					vo = this.cmbDealService.request6201(request6201, this.getLoginUser().getId(),txDate,txTime);
					if (vo.isSuccess()) {
						//招行出金审核通过，冻结金额减少
						Account a = this.accountService.selectById(ad.getAccount().getId());
						a.setFrozenAmount(DoubleUtils.doubleCheck(a.getFrozenAmount() - ad.getMoney(),2));// 冻结金额减少
						this.accountService.update(a);
					}else{//调用接口出金失败，ad再变更为待审核
						AccountDeal lastad = this.accountDealService.selectById(ad.getId());
						lastad.setCheckFlag("25");
						lastad.setCheckDate(null);
						lastad.setCheckUser(null);
						lastad.setSuccessFlag(false);
						lastad.setSuccessDate(null);
						lastad.setPreMoney(pre);
						lastad.setNextMoney(next);
						this.accountDealService.update(lastad);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				vo.setName("出金");
				vo.setTxCode("6201");
				vo.setMsg("出金失败");
			}
			try {
				PrintWriter out = getOut();
				out.write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// 出金，标记为已驳回，出金额返还给会员
	//可用金额增加，冻结金额减少
	public String outGoldenToNoPass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AccountDeal ad = this.accountDealService.selectById(this.id);
		Account a = this.accountService.selectById(ad.getAccount().getId());
		if(!"25".equals(ad.getCheckFlag())){
			CMBVO vo = new CMBVO();
			vo.setMsg("该笔出金已经审核驳回，请勿重复审核。");
			vo.setSuccess(false);
			try {
				PrintWriter out = getOut();
				out.write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
			ad.setCheckFlag("27");// 驳回
			ad.setMemo(memo);
			ad.setCheckDate(new Date());
			ad.setCheckUser(u);
			PrintWriter out = null;
			try {
				out = getOut();
				this.accountService.thawAccount_old(a, ad.getMoney());
				ad.setNextMoney(a.getBalance()+a.getFrozenAmount());
				this.accountDealService.update(ad);
				out.write("{\"success\":\"true\",\"msg\":\"驳回成功\"}");
			} catch (Exception e) {
				e.printStackTrace();
				out.write("{\"success\":\"false\",\"msg\":\"审核驳回失败，请重试！\"}");
			}
			return null;
		}
	}
	
	public String in_out_deal(){
		return "in_out_deal";
	}

	// 签约用户出入金明细列表
	public String in_out_deal_data() {
		String disUrl = null;
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date latest = this.openCloseDealService.getLatestKaiShi();
		
		
		ArrayList<Object> args_list = new ArrayList<Object>();
		
		if(null==latest){
			this.checkDate();
		}else{
			if(null==this.getStartDate()){
				this.setStartDate(latest);
			}
			if(null==this.getEndDate()){
				this.setEndDate(new Date());
			}
		}
		if(this.isLoad()){
			try {
				Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
				StringBuilder subsql = new StringBuilder(" successflag = 1 and businessflag in (18,19) and signbank != 0 and signtype !=0 and successdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and  to_date('" + format.format(endDateNext) + "','yyyy-MM-dd') ");
				if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (target_account_user_username like ? ");
					subsql.append(" or target_account_user_realname like ? ");
					subsql.append(" or type like ?)");
					
					args_list.add("%" + this.getKeyWord() + "%");
					args_list.add("%" + this.getKeyWord() + "%");
					args_list.add("%" + this.getKeyWord() + "%");
				}

				if(this.bank!=-1){
					subsql.append(" and signbank = ?");
					
					args_list.add(this.bank);
				}
				subsql.append(" order by successdate desc ");
				
				Object [] args = args_list.toArray();
				
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString(),args, getPage(), rows);
					int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.accountDealService.charge_cash(subsql.toString(),args);
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("TYPE", "合计");
					_totalDate.element("MONEY_ADD", totalData[0]);
					_totalDate.element("MONEY_SUBTRACT", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString(),args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "in_out_deal_print";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	public String business_deal(){
		return "business_deal";
	}
	
	public String business_data(){
		String disUrl = null;
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				ArrayList<Object> args_list = new ArrayList<Object>();
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" successflag = 1 and signbank !=0 and signtype !=0 and businessflag not in (1,2,20,21,22,7,16,17,18,19) ");
				if(this.bank!=-1&&this.bank!=0){
					subsql.append(" and signbank=" + this.bank);
				}
				
				if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (");
					subsql.append(" instr(type,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_username,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_realname,?)> 0 ");
					subsql.append(" )");
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
				}
				subsql.append(" and successdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				Object [] args = args_list.toArray();
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by successdate desc",args, getPage(), rows);
					int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.accountDealService.dai_jie(subsql.toString(),args);
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("FCODE", "合计");
					_totalDate.element("MONEY_DAI", totalData[0]);
					_totalDate.element("MONEY_JIE", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by successdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "business_deal_print";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	public String balance_deal(){
		return "balance_deal";
	}
	
	public String balance_data(){
		String disUrl = null;
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				ArrayList<Object> args_list = new ArrayList<Object>();
				Object [] args = args_list.toArray();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" s.success=1 and s.name in ('一步式签约','激活银商转账服务') and s.signed=1 and (s.balance+s.frozen) !=0 ");
				subsql.append(" and s.signdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				subsql.append(" and s.owner=u.id ");
				if (this.bank!=0&&this.bank!=-1) {
					subsql.append(" and s.signBank= "+this.bank);
				}
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.signHistoryService.queryForList("s.id as id,s.name as name,u.username as username,u.realname as realname,u.userType_ as type,s.signbank as signbank,s.signtype as signtype,s.balance as balance,s.frozen as frozen,s.signdate as signdate,s.syndate_market as syndate,s.surrenderdate as surrenderdate","t_signhistory s,sys_user u",subsql.toString()+" order by s.signdate desc",args, getPage(), rows);
					int total = this.signHistoryService.queryForListTotal("s.id","t_signhistory s,sys_user u",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.signHistoryService.balance_frozen(subsql.toString());
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("NAME", "合计");
					_totalDate.element("BALANCE", totalData[0]);
					_totalDate.element("FROZEN", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.signHistoryService.queryForList("s.id as id,s.name as name,u.username as username,u.realname as realname,u.userType_ as type,s.signbank as signbank,s.signtype as signtype,s.balance as balance,s.frozen as frozen,s.signdate as signdate,s.syndate_market as syndate,s.surrenderdate as surrenderdate","t_signhistory s,sys_user u",subsql.toString()+" order by s.signdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "balance_deal_print";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}

	public String checkState() {
		String s = "";
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 0) {
			s = "规则未启用";
		} else if (state == 1) {
			s = "状态:已开市";
		} else if (state == 2) {
			s = "状态:已休市";
		} else if (state == 3) {
			s = "状态:已清算";
		} else if (state == 4){
			s = "状态:已对账";
		} else if (state == 5) {
			s = "已开夜市";
		} else if (state == 6) {
			s = "已休夜市";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.getWriter().print("{\"state\":\"" + s + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 充值、提现日结单
	 * 
	 * @return
	 */
	public String print_recharge_kiting_report() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_short = new SimpleDateFormat("yyyyMMdd");
		String date_str = request.getParameter("date");
		if (StringUtils.isNotBlank(date_str)) {
			try {
				date = sdf.parse(date_str);
			} catch (ParseException e) {
				date = new Date();
			}
		} else {
			date = new Date();
		}
		
		LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>> list = new LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>>();
		try{
	
			LinkedHashMap<String, HashMap<String, Object>> list_530101 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530101.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo "," ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530101.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530101.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530101.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530101", list_530101);
	
			LinkedHashMap<String, HashMap<String, Object>> list_530105 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530105.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530105.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530105.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530105.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530105", list_530105);
			
			LinkedHashMap<String, HashMap<String, Object>> list_db = new LinkedHashMap<String, HashMap<String, Object>>();
			HashMap<String,Object> db_in = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and (ad.businessflag = 21 or ad.businessflag = 22 ) and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'",null);
			HashMap<String,Object> db_out = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) sum,sum(case when ad.channel = 1 then ad.money else 0 end) cmb,sum(case when ad.channel = 2 then ad.money else 0 end) icbc,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and ad.businessflag = 2 and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'" ,null);
			list_db.put("充值", db_in);
			list_db.put("提现", db_out);
			list.put("担保公司", list_db);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("date", date);
		request.setAttribute("list", list);

		return "print_recharge_kiting_report";
	}

	/**
	 * 充值、提现日结单(招行)
	 * 
	 * @return
	 */
	public String print_recharge_kiting_report_cmb() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_short = new SimpleDateFormat("yyyyMMdd");
		String date_str = request.getParameter("date");
		if (StringUtils.isNotBlank(date_str)) {
			try {
				date = sdf.parse(date_str);
			} catch (ParseException e) {
				date = new Date();
			}
		} else {
			date = new Date();
		}
		
		LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>> list = new LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>>();
		try{
	
			LinkedHashMap<String, HashMap<String, Object>> list_530101 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530101.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo "," ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530101.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530101.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530101.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530101", list_530101);
	
			LinkedHashMap<String, HashMap<String, Object>> list_530105 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530105.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530105.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530105.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530105.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=1 and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530105", list_530105);
			
			LinkedHashMap<String, HashMap<String, Object>> list_db = new LinkedHashMap<String, HashMap<String, Object>>();
			HashMap<String,Object> db_in = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and ad.channel=1 and (ad.businessflag = 21 or ad.businessflag = 22 ) and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'",null);
			HashMap<String,Object> db_out = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) cmb,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and ad.channel=1 and ad.businessflag = 2 and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'" ,null);
			list_db.put("充值", db_in);
			list_db.put("提现", db_out);
			list.put("担保公司", list_db);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("date", date);
		request.setAttribute("list", list);

		return "print_recharge_kiting_report_cmb";
	}
	
	/**
	 * 充值、提现日结单(工行)
	 * 
	 * @return
	 */
	public String print_recharge_kiting_report_icbc() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_short = new SimpleDateFormat("yyyyMMdd");
		String date_str = request.getParameter("date");
		if (StringUtils.isNotBlank(date_str)) {
			try {
				date = sdf.parse(date_str);
			} catch (ParseException e) {
				date = new Date();
			}
		} else {
			date = new Date();
		}
		
		LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>> list = new LinkedHashMap<String, LinkedHashMap<String, HashMap<String, Object>>>();
		try{
	
			/*LinkedHashMap<String, HashMap<String, Object>> list_530101 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530101.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo "," ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530101.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530101.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530101.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding != '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530101", list_530101);
	
			LinkedHashMap<String, HashMap<String, Object>> list_530105 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530105.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530105.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530105.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530105.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and uo.parentcoding = '530105' and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530105", list_530105);*/
			
			LinkedHashMap<String, HashMap<String, Object>> list_530101 = new LinkedHashMap<String, HashMap<String, Object>>();
			list_530101.put("投资充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo "," ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id  and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 1 and u.usertype_ = 'T' ",null));
			list_530101.put("融资还款充值", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id  and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ( ad.businessFlag = 21 or ad.businessFlag = 22) and u.usertype_ = 'R' ",null));
			list_530101.put("投资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and  and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and ad.businessFlag = 2 and u.usertype_ = 'T' and ad.checkflag2 = '1' ",null));
			list_530101.put("融资提现", (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count","t_accountdeal ad,sys_user u,sys_org uo","ad.account_accountid_ = u.useraccount_accountid_ and u.org_id = uo.id and  and ad.successFlag = 1 and to_char(ad.successDate,'yyyymmdd') = '"+sdf_short.format(date)+"' and ad.channel=2 and (ad.businessFlag = 20 or ad.businessFlag = 2) and u.usertype_ = 'R' and ad.checkflag2 = '1' ",null));
			list.put("530101", list_530101);
			
			LinkedHashMap<String, HashMap<String, Object>> list_db = new LinkedHashMap<String, HashMap<String, Object>>();
			HashMap<String,Object> db_in = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and ad.channel=2 and (ad.businessflag = 21 or ad.businessflag = 22 ) and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'",null);
			HashMap<String,Object> db_out = (HashMap<String,Object>)this.accountDealService.queryForObject("sum(ad.money) icbc,count(ad.id) count", "t_accountdeal ad,sys_user u ", " ad.account_accountid_ = u.useraccount_accountid_ and ad.channel=2 and ad.businessflag = 2 and ad.successflag = 1 and u.usertype_ = 'D' and to_char(ad.successdate,'yyyymmdd') = '"+sdf_short.format(date)+"'" ,null);
			list_db.put("充值", db_in);
			list_db.put("提现", db_out);
			list.put("担保公司", list_db);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("date", date);
		request.setAttribute("list", list);

		return "print_recharge_kiting_report_icbc";
	}
	
	public String account_frozen_thaw() {
		HttpServletRequest request = ServletActionContext.getRequest();
		if (StringUtils.isNotBlank(this.type) && StringUtils.isNotBlank(this.userName)) {
			User u = this.userService.findUser(this.userName);
			if (null != u) {
				if ("冻结".equals(this.type)) {
					request.setAttribute("user", u);
					request.setAttribute("list", null);
				}
				if ("解冻".equals(this.type)) {
					String qhl = " from AccountDeal o where o.checkFlag='31' and o.accountDeal.id is null and o.account.user.id=" + u.getId();
					List<AccountDeal> ad = this.accountDealService.getCommonListData(qhl);
					request.setAttribute("list", ad);
					request.setAttribute("user", null);
				}
			} else {
				request.setAttribute("user", null);
				request.setAttribute("list", null);
			}
		}
		return "account_frozen_thaw";
	}

	public String account_frozen_thaw_detail() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
		if(null!=this.memo&&!"".equals(this.memo)){
			sb.append(" and ");
			sb.append(" o.memo like '%"+this.memo+"%' ");
		}
		if(null!=this.keyWord&&!"".equals(this.keyWord)){
			sb.append(" and ");
			sb.append(" o.account.user.org.showCoding = '"+this.keyWord+"' ");
		}
		String flag = " and o.checkFlag in ('30','31','32','33','34','35') ";
		String hql = "from AccountDeal o where "+sb.toString();
		
		String hql_dj_sum = "select nvl(sum(o.money),0) as sum "+hql+" and o.type='资金冻结' and o.checkFlag='31' ";
		String hql_dj_count = "select count(*) as count "+hql+" and o.type='资金冻结' and o.checkFlag='31'";
		
		String hql_jd_sum = "select nvl(sum(o.money),0) as sum "+hql+" and o.type='资金解冻' and o.checkFlag='34' ";
		String hql_jd_count = "select count(*) as count "+hql+" and o.type='资金解冻' and o.checkFlag='34'";
		
		hql += flag;
		
		double dj_sum = 0d;
		double jd_sum = 0d;
		
		int dj_count = 0;
		int jd_count = 0;
		hql += " order by o.createDate desc ";
		try {
			List<AccountDeal> one_sum = accountDealService.getCommonListData(hql_dj_sum);
			List<AccountDeal> one_count = accountDealService.getCommonListData(hql_dj_count);
			List<AccountDeal> two_sum = accountDealService.getCommonListData(hql_jd_sum);
			List<AccountDeal> two_count = accountDealService.getCommonListData(hql_jd_count);
			if(null!=one_sum&&one_sum.size()==1){
				Object o = (Object)one_sum.get(0);
				dj_sum = Double.parseDouble(o.toString());
			}
			if(null!=one_count&&one_count.size()==1){
				Object o = (Object)one_count.get(0);
				dj_count = Integer.parseInt(o.toString());
			}
			if(null!=two_sum&&two_sum.size()==1){
				Object o = (Object)two_sum.get(0);
				jd_sum = Double.parseDouble(o.toString());
			}
			if(null!=two_count&&two_count.size()==1){
				Object o = (Object)two_count.get(0);
				jd_count = Integer.parseInt(o.toString());
			}
			ServletActionContext.getRequest().setAttribute("dj_sum", dj_sum);
			ServletActionContext.getRequest().setAttribute("dj_count", dj_count);
			ServletActionContext.getRequest().setAttribute("jd_sum", jd_sum);
			ServletActionContext.getRequest().setAttribute("jd_count", jd_count);
			ServletActionContext.getRequest().setAttribute("list", accountDealService.getCommonListData(hql));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "account_frozen_thaw_detail";
	}

	public String account_frozen_thaw_do() throws Exception {
		User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			out.write("{\"message\":\"交易市场未开市\"}");
		}else {
			if (StringUtils.isNotBlank(this.type) && StringUtils.isNotBlank(this.userName)) {
				if (this.chargeAmount <= 0) {
					out.write("{\"message\":\"申请" + this.type + "失败，金额不能小于0元。\"}");
				} else {
					User u = this.userService.findUser(this.userName);
					if (null != u) {
						AccountDeal ad = this.accountDealService.accountDealRecord("资金" + this.type, "-1", this.chargeAmount);
						Account a = this.accountService.selectById(u.getUserAccount().getId());
						ad.setPreMoney(a.getBalance() + a.getFrozenAmount());
						ad.setNextMoney(ad.getPreMoney());
						ad.setUser(operator);
						ad.setMemo(this.memo);
						ad.setAccount(a);
						ad.setBzj(this.bzj);
						ad.setCodes(this.codes);
						if ("冻结".equals(this.type)) {
							if (ad.getMoney() > a.getBalance()) {
								out.write("{\"message\":\"申请冻结失败，交易账号可用余额不足。\"}");
							} else {
								ad.setCheckFlag("30");
								ad.setBusinessFlag(16);
								ad.setSignBank(a.getUser().getSignBank());//签约行
								ad.setSignType(a.getUser().getSignType());//签约类型
								ad.setTxDir(2);//交易转账方向
								ad.setChannel(a.getUser().getChannel());//手工专户
								boolean f = this.accountService.frozenAccount(a, ad.getMoney());
								if (f) {
									this.accountDealService.insert(ad);
									out.write("{\"message\":\"success\"}");
								} else {
									out.write("{\"message\":\"申请冻结失败，请重试！\"}");
								}
							}
						} else if ("解冻".equals(this.type) && StringUtils.isNotBlank(this.id)) {
							AccountDeal od = this.accountDealService.selectById(this.id);
							if (null != od) {
								if (ad.getMoney() > a.getFrozenAmount()) {
									out.write("{\"message\":\"申请解冻失败，交易账号冻结金额不足。\"}");
								} else {
									ad.setCheckFlag("33");
									ad.setBusinessFlag(17);
									ad.setAccountDeal(od);
									ad.setSignBank(a.getUser().getSignBank());//签约行
									ad.setSignType(a.getUser().getSignType());//签约类型
									ad.setTxDir(1);//交易转账方向
									ad.setChannel(a.getUser().getChannel());//手工专户
									if(null!=ad.getMemo()){
										if(null!=od.getMemo()){
											ad.setMemo(ad.getMemo()+"("+od.getMemo()+")");
										}
									}else{
										if(null!=od.getMemo()){
											ad.setMemo(od.getMemo());
										}
									}
									this.accountDealService.insert(ad);
									od.setAccountDeal(ad);
									this.accountDealService.update(od);
									out.write("{\"message\":\"success\"}");
								}
							} else {
								out.write("{\"message\":\"申请解冻失败，参数错误。\"}");
							}
						} else {
							out.write("{\"message\":\"申请失败，参数错误。\"}");
						}
					} else {
						out.write("{\"message\":\"申请" + this.type + "失败，交易账号错误。\"}");
					}
				}
			} else {
				out.write("{\"message\":\"申请" + this.type + "失败，参数错误。\"}");
			}
		}
		return null;
	}

	// 资金冻结,资金解冻,审核通过
	public String account_frozen_thaw_pass() {
		User checkUser = this.getLoginUser();
		PrintWriter out = null;
		try {
			out = getOut();
			if (StringUtils.isNotBlank(this.id)) {
				AccountDeal ad = this.accountDealService.selectById(this.id);
				if (null != ad) {
					HxbankVO vo = this.accountService.frozen_thaw_pass(ad, checkUser);
					if (vo.isFlag()) {
						out.write("{\"message\":\"success\"}");
					} else {
						out.write("{\"message\":\"" + vo.getTip() + "\"}");
					}
				} else {
					out.write("{\"message\":\"审核通过失败，参数错误。\"}");
				}
			} else {
				out.write("{\"message\":\"审核通过失败，参数错误。\"}");
			}
		} catch (Exception e) {
			out.write("{\"message\":\"审核通过失败，请稍后再试。\"}");
		}
		return null;
	}

	// 资金冻结,资金解冻,审核驳回
	public String account_frozen_thaw_nopass() {
		User checkUser = this.getLoginUser();
		PrintWriter out = null;
		try {
			out = getOut();
			if (StringUtils.isNotBlank(this.id)) {
				AccountDeal ad = this.accountDealService.selectById(this.id);
				if (null != ad) {
					HxbankVO vo = this.accountService.frozen_thaw_nopass(ad, checkUser);
					if (vo.isFlag()) {
						out.write("{\"message\":\"success\"}");
					} else {
						out.write("{\"message\":\"" + vo.getTip() + "\"}");
					}
				} else {
					out.write("{\"message\":\"审核驳回失败，参数错误。\"}");
				}
			} else {
				out.write("{\"message\":\"审核驳回失败，参数错误。\"}");
			}
		} catch (Exception e) {
			out.write("{\"message\":\"审核驳回失败，请稍后再试。\"}");
		}
		return null;
	}
	private List<Account> swithAccountList(ArrayList<LinkedHashMap<String, Object>> dataList) throws Exception { 
		List<Account> acs = new ArrayList<Account>();
		for (int i = 0; i < dataList.size(); i++) {
			Account vo = new Account(); 
			vo.setString5(dataList.get(i).get("user_username").toString());
			vo.setString6(dataList.get(i).get("name")==null?"":dataList.get(i).get("name").toString());
			vo.setString7(dataList.get(i).get("user_id").toString()); 
			vo.setCyzq(Double.valueOf(dataList.get(i).get("cyzq").toString()));
			vo.setBalance(Double.valueOf(dataList.get(i).get("user_account_balance").toString()));
			vo.setFrozenAmount(Double.valueOf(dataList.get(i).get("user_account_frozenamount").toString()));
			vo.setTotalAmount(Double.valueOf(dataList.get(i).get("zzc").toString()));  
			

			if (null == dataList.get(i).get("invest_all_count") || "null".equals(dataList.get(i).get("invest_all_count")) || "".equals(dataList.get(i).get("invest_all_count"))) {
				vo.setString1("0");
			} else {
				vo.setString1(dataList.get(i).get("invest_all_count").toString());
			}
			
			if (null == dataList.get(i).get("invest_all") || "null".equals(dataList.get(i).get("invest_all")) || "".equals(dataList.get(i).get("invest_all"))) {
				vo.setString2("0.00");
			} else {
				vo.setString2(dataList.get(i).get("invest_all").toString());
			}
			
			
			if (null == dataList.get(i).get("come_countt") || "null".equals(dataList.get(i).get("come_countt")) || "".equals(dataList.get(i).get("come_countt"))) {
				vo.setString3("0");
			} else {
				vo.setString3(dataList.get(i).get("come_countt").toString());
			} 
			
			if (null == dataList.get(i).get("come_amount") || "null".equals(dataList.get(i).get("come_amount")) || "".equals(dataList.get(i).get("come_amount"))) {
				vo.setString4("0.00");
			} else {
				vo.setString4(dataList.get(i).get("come_amount").toString());
			}
			acs.add(vo); 
		}
		return acs;

	}
	
	// 已审核利息明细
	public String checkLX_checked() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ServletActionContext.getRequest().setAttribute("user", u);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String hql = "from AccountDeal o where o.checkFlag='37' and o.type='" + AccountDeal.HQLX + "' ";
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			hql += " and ( o.account.accountId like '%" + this.getKeyWord() + "%' ";
			hql += " or o.account.user.username like '%" + this.getKeyWord() + "%' ";
			hql += " or o.account.user.realname like '%" + this.getKeyWord() + "%' ";
			hql += " or o.type like '%" + this.getKeyWord() + "')";
		}

		hql += " and o.checkDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')";
		hql += " and o.checkDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')";
		if (null != this.userType && !"".equals(this.userType) && !"all".equals(this.userType)) {
			hql += " and o.account.user.userType = '" + this.userType + "' ";
		}
		hql += " order by o.checkDate desc ";
		try {
			this.deals = accountDealService.getCommonListData(hql);
			for (AccountDeal d : this.deals) {
				this.sum_charge += d.getMoney();
				this.sum_charge_count += 1;
				MemberBase m = this.memberBaseService.getMemByUser(d.getAccount().getUser());
				d.setBankAccount(m.getBankAccount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "checkLX_checked";
	}
	
	public String checkLX() {
		PageView<AccountDeal> pageView = new PageView<AccountDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "asc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sb.append(" or o.account.user.username like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" or o.account.user.realname like ?");
			params.add("%" + this.getKeyWord() + "%");
			sb.append(" ) ");
		}
		sb.append(" and ");
		sb.append(" o.checkFlag='36'");
		sb.append(" and ");
		sb.append(" o.type ='" + AccountDeal.HQLX + "'");
		try {
			QueryResult<AccountDeal> qr = accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<AccountDeal> accountDeals = qr.getResultlist();
			for (AccountDeal deal : accountDeals) {
				deal.setBankAccount(this.memberBaseService.getMemByUser(deal.getAccount().getUser()).getBankAccount());
			}
			qr.setResultlist(accountDeals);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			long c = this.accountDealService.getScrollCount(sb.toString(), params, orderby);
			double s = this.accountDealService.getScrollSum(sb.toString(), params, orderby);
			ServletActionContext.getRequest().setAttribute("tip", "共:"+c+"笔，利息合计:"+s+"元");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "checkLX";
	}
	
	// 利息批量审核通过:根据条件查询出要批量审核的所有记录，然后逐个审核通过。
	public String checkLX_pass() {
		String msg = "";
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			msg = "交易市场未开市";
		} else if (state == 2) {
			msg = "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)";
		}else{
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String hql = " from AccountDeal o where o.checkFlag='36' and o.type = '" + AccountDeal.HQLX + "' ";
			if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
				this.setKeyWord(this.getKeyWord().trim());
				hql += " and ( o.account.user.username like '%" + this.getKeyWord() + "%' or o.account.user.realname like '%" + this.getKeyWord() + "%' )";
			}
			this.deals = this.accountDealService.getCommonListData(hql);
			for (AccountDeal a : deals) {
				AccountDeal ad = this.accountDealService.selectById(a.getId());
				if (!"36".equals(ad.getCheckFlag())) {// 待审核标志不为36，可能已经被审核掉了。
					System.out.println("此利息发放已经审核无需再审核");
				} else {
					ad.setCheckFlag("37");
					ad.setCheckUser(u);
					ad.setCheckDate(new Date());
					ad.setSuccessFlag(true);
					ad.setSuccessDate(ad.getCheckDate());
					ad.setSignBank(ad.getAccount().getUser().getSignBank());//签约行
					ad.setSignType(ad.getAccount().getUser().getSignType());//签约类型
					ad.setTxDir(1);//交易转账方向
					ad.setChannel(ad.getAccount().getUser().getChannel());//手工专户
					ad.setPreMoney(ad.getAccount().getBalance()+ad.getAccount().getFrozenAmount());
					ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
					ad.setTime(ad.getCheckDate().getTime());
					try {
						this.accountService.addMoney(ad.getAccount(), ad.getMoney());
						this.accountDealService.update(ad);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			msg = "操作成功";
		}
		ActionContext.getContext().getSession().put(Constant.MESSAGETIP, msg);
		return "checkLX_do";
	}
	
	// 利息批量审核驳回:根据条件查询出要批量审核的所有记录，然后逐个审核驳回。
	public String checkLX_nopass() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String hql = " from AccountDeal o where o.checkFlag='36' and o.type = '" + AccountDeal.HQLX + "' ";
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			hql += " and ( o.account.user.username like '%" + this.getKeyWord() + "%' or o.account.user.realname like '%" + this.getKeyWord() + "%' )";
		}
		this.deals = this.accountDealService.getCommonListData(hql);
		for (AccountDeal a : deals) {
			AccountDeal ad = this.accountDealService.selectById(a.getId());
			if (!"36".equals(ad.getCheckFlag())) {// 待审核标志不为36，可能已经被审核掉了。
				System.out.println("此利息发放已经审核无需再审核");
			} else {
				ad.setCheckFlag("38");
				ad.setCheckUser(u);
				ad.setCheckDate(new Date());
				try {
					//审核驳回，无需为会员账户增加金额
					this.accountDealService.update(ad);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "操作成功");
		return "checkLX_do";
	}
	
	//工行专户变更
	public String icbc_biangeng(){
		return "icbc_biangeng";
	}
	
	public String icbc_biangeng_query(){
		String disUrl = null;
		if(this.load){
			try {
				ArrayList<Object> args_list = new ArrayList<Object>();
				StringBuilder subsql = new StringBuilder(" u.usertype_='T' and u.channel =1 and u.flag not in ('1','2') and u.useraccount_accountid_=a.accountid_ and mb.user_id=u.id and mb.bank_id=bk.id and bk.caption='工商银行'");
				if (null!=this.userName&&!"".equals(this.userName)&&null!=this.keyWord&&!"".equals(this.keyWord)) {
					subsql.append(" and u.username = ? ");
					args_list.add(this.userName);
					subsql.append(" and u.realname = ? ");
					args_list.add(this.keyWord);
				}else{//不输入交易帐号和会员名称，则不让查询
					this.action = -1;
				}
				Object [] args = args_list.toArray();
				if(this.action==0){//查询
					int rows = 1;
					List<Map<String, Object>> result = this.userService.queryForList("u.id as id,u.username as username,u.realname as realname,u.userType_ as type,a.balance_ as balance,a.frozenamount as frozen,u.channel,bk.caption","sys_user u,sys_account a,t_member_base mb,t_banklibrary bk",subsql.toString(),args, getPage(), rows);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("rows", object);
					
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	public void icbc_biangeng_do(){
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		String msg = "";
		if (state == 2) {//休市状态不让做变更
			msg = "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)";
			try {
				PrintWriter out = null;
				out = getOut();
				out.write("{\"success\":\"false\",\"msg\":\""+msg+"\"}");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			PrintWriter out = null;
			User aim = this.userService.selectById(this.userId);
			if(null!=aim && null!=aim.getUserAccount() && !"1".equals(aim.getFlag()) && !"2".equals(aim.getFlag()) && aim.getChannel()!=2){
				Account a = this.accountService.selectById(aim.getUserAccount().getId());
				if(null!=a){
					try {
						out = getOut();
						//user表channel=2
						aim.setChannel(2);
						this.userService.update(aim);
						
						//account表oldbalance=0
						a.setOld_balance(0);
						this.accountService.update(a);
						
						//记录变更成功时的存量余额
						SignHistory history = new SignHistory();
						history.setName("变更专户为工行");
						history.setSignDate(new Date());
						history.setChannel(2);//手工专户为2
						history.setMemo("原专户为:招行专户");
						history.setSuccess(true);
						history.setSigned(true);
						history.setBalance(a.getBalance());
						history.setFrozen(a.getFrozenAmount());
						history.setOwner(aim.getId());
						history.setOperater(u.getId());
						this.signHistoryService.insert(history);
						out.write("{\"success\":\"true\",\"msg\":\"变更成功\"}");
					} catch (Exception e) {
						
					}
				}
			}
		}
	}
	
	//工行专户余额明细
	public String icbc_balance_deal(){
		return "icbc_balance_deal";
	}
	
	public String icbc_balance_deal_data(){
		String disUrl = null;
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				ArrayList<Object> args_list = new ArrayList<Object>();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" s.success=1 and s.name = '变更专户为工行' and s.signed=1 and (s.balance+s.frozen) !=0 ");
				subsql.append(" and s.signdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				subsql.append(" and s.owner=u.id and s.operater=op.id ");
				if (this.channel!=0) {
					subsql.append(" and s.channel= "+this.channel);
				}
				Object [] args = args_list.toArray();
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.signHistoryService.queryForList("s.id as id,s.name as name,u.username as username,u.realname as realname,op.realname as operater,u.userType_ as type,s.channel as channel,s.balance as balance,s.frozen as frozen,s.signdate as signdate","t_signhistory s,sys_user u,sys_user op",subsql.toString()+" order by s.signdate desc",args, getPage(), rows);
					int total = this.signHistoryService.queryForListTotal("s.id","t_signhistory s,sys_user u,sys_user op",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.signHistoryService.icbc_balance_frozen(subsql.toString());
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("NAME", "合计");
					_totalDate.element("BALANCE", totalData[0]);
					_totalDate.element("FROZEN", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.signHistoryService.queryForList("s.id as id,s.name as name,u.username as username,u.realname as realname,op.realname as operater,u.userType_ as type,s.channel as channel,s.balance as balance,s.frozen as frozen,s.signdate as signdate","t_signhistory s,sys_user u,sys_user op",subsql.toString()+" order by s.signdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "icbc_balance_deal_print";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	//工行专户业务明细
	public String icbc_business_deal(){
		return "icbc_business_deal";
	}
	
	public String icbc_business_deal_data(){
		String disUrl = null;
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(this.load){
			try {
				ArrayList<Object> args_list = new ArrayList<Object>();
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				StringBuilder subsql = new StringBuilder(" successflag = 1 and businessflag not in (1,2,20,21,22,7,16,17,18,19) ");
				if(this.channel!=0){
					subsql.append(" and channel=" + this.channel);
				}
				
				if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
					this.setKeyWord(this.getKeyWord().trim());
					subsql.append(" and (");
					subsql.append(" instr(type,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_username,?)> 0 ");
					subsql.append(" or ");
					subsql.append(" instr(target_account_user_realname,?)> 0 ");
					subsql.append(" )");
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
					args_list.add(this.getKeyWord());
				}
				subsql.append(" and successdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
				Object [] args = args_list.toArray();
				if(this.action==0){//查询
					HttpServletRequest request = ServletActionContext.getRequest();
					int rows = Integer.parseInt(request.getParameter("rows"));
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by successdate desc",args, getPage(), rows);
					int total = this.accountDealService.queryForListTotal("id","v_accountdeal_new",subsql.toString(),args);
					
					JSONArray object = JSONArray.fromObject(result);
					JSONObject o = new JSONObject();
					o.element("total", total);
					o.element("rows", object);
					double[] totalData = this.accountDealService.dai_jie(subsql.toString(),args);
					JSONArray footer = new JSONArray();
					JSONObject _totalDate = new JSONObject();
					_totalDate.element("FCODE", "合计");
					_totalDate.element("MONEY_DAI", totalData[0]);
					_totalDate.element("MONEY_JIE", totalData[1]);
					footer.add(_totalDate);
					o.element("footer", footer);
					ServletActionContext.getResponse().getWriter().write(o.toString());
				}else if(this.action==1){//打印
					List<Map<String, Object>> result = this.accountDealService.queryForList("*","v_accountdeal_new",subsql.toString()+" order by successdate desc",args);
					ServletActionContext.getRequest().setAttribute("resultList", result);
					disUrl = "icbc_business_deal_print";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return disUrl;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAccountDeal(AccountDeal accountDeal) {
		this.accountDeal = accountDeal;
	}

	public AccountDeal getAccountDeal() {
		return accountDeal;
	}

	public void setDeals(List<AccountDeal> deals) {
		this.deals = deals;
	}

	public List<AccountDeal> getDeals() {
		return deals;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setChargeAmount(String chargeAmount) {
		try {
			this.chargeAmount = Double.parseDouble(chargeAmount);
		} catch (Exception e) {
			this.chargeAmount = 0d;
		}
		
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setCost(FinancingCost cost) {
		this.cost = cost;
	}

	public FinancingCost getCost() {
		return cost;
	}

	public List<AccountDealVo> getDealVos() {
		return dealVos;
	}

	public void setDealVos(List<AccountDealVo> dealVos) {
		this.dealVos = dealVos;
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

	public double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getSum_charge() {
		return sum_charge;
	}

	public void setSum_charge(double sumCharge) {
		sum_charge = sumCharge;
	}

	public double getSum_cash() {
		return sum_cash;
	}

	public void setSum_cash(double sumCash) {
		sum_cash = sumCash;
	}

	public void setJyType(String jyType) {
		this.jyType = jyType;
	}

	public String getJyType() {
		return jyType;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public void setJsbl(double jsbl) {
		this.jsbl = jsbl;
	}

	public double getJsbl() {
		return jsbl;
	}

	public void setChargeStyle(String chargeStyle) {
		this.chargeStyle = chargeStyle;
	}

	public String getChargeStyle() {
		return chargeStyle;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	public void setStart_530101_balance(double start_530101_balance) {
		this.start_530101_balance = start_530101_balance;
	}

	public double getStart_530101_balance() {
		return start_530101_balance;
	}

	public void setStart_530105_balance(double start_530105_balance) {
		this.start_530105_balance = start_530105_balance;
	}

	public double getStart_530105_balance() {
		return start_530105_balance;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public Date getToday() {
		return today;
	}

	public void setInAccount_530101(double inAccount_530101) {
		this.inAccount_530101 = inAccount_530101;
	}

	public double getInAccount_530101() {
		return inAccount_530101;
	}

	public void setOutAccount_530101(double outAccount_530101) {
		this.outAccount_530101 = outAccount_530101;
	}

	public double getOutAccount_530101() {
		return outAccount_530101;
	}

	public double getInAccount_530105() {
		return inAccount_530105;
	}

	public void setInAccount_530105(double inAccount_530105) {
		this.inAccount_530105 = inAccount_530105;
	}

	public double getOutAccount_530105() {
		return outAccount_530105;
	}

	public void setOutAccount_530105(double outAccount_530105) {
		this.outAccount_530105 = outAccount_530105;
	}

	public void setIn_member_username(String in_member_username) {
		this.in_member_username = in_member_username;
	}

	public String getIn_member_username() {
		return in_member_username;
	}

	public void setOut_member_username(String out_member_username) {
		this.out_member_username = out_member_username;
	}

	public String getOut_member_username() {
		return out_member_username;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getQueryBalance() {
		return queryBalance;
	}

	public void setQueryBalance(String queryBalance) {
		this.queryBalance = queryBalance;
	}

	public String getQueryFrozenAmount() {
		return queryFrozenAmount;
	}

	public void setQueryFrozenAmount(String queryFrozenAmount) {
		this.queryFrozenAmount = queryFrozenAmount;
	}

	public double getTotalAmountSum() {
		return totalAmountSum;
	}

	public void setTotalAmountSum(double totalAmountSum) {
		this.totalAmountSum = totalAmountSum;
	}

	public double getBalanceSum() {
		return balanceSum;
	}

	public void setBalanceSum(double balanceSum) {
		this.balanceSum = balanceSum;
	}

	public double getFrozenAmountSum() {
		return frozenAmountSum;
	}

	public void setFrozenAmountSum(double frozenAmountSum) {
		this.frozenAmountSum = frozenAmountSum;
	}

	public double getCyzqSum() {
		return cyzqSum;
	}

	public void setCyzqSum(double cyzqSum) {
		this.cyzqSum = cyzqSum;
	}

	public List<Account> getAcs() {
		return acs;
	}

	public void setAcs(List<Account> acs) {
		this.acs = acs;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public void setBank(int bank) {
		this.bank = bank;
	}

	public int getBank() {
		return bank;
	}

	public void setSum_charge_count(int sum_charge_count) {
		this.sum_charge_count = sum_charge_count;
	}

	public int getSum_charge_count() {
		return sum_charge_count;
	}

	public void setSum_cash_count(int sum_cash_count) {
		this.sum_cash_count = sum_cash_count;
	}

	public int getSum_cash_count() {
		return sum_cash_count;
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

	public void setJyrb(List<JYRBVO> jyrb) {
		this.jyrb = jyrb;
	}

	public List<JYRBVO> getJyrb() {
		return jyrb;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setHuidan(String huidan) {
		this.huidan = huidan;
	}

	public String getHuidan() {
		return huidan;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getPu() {
		return pu;
	}

	public void setPu(String pu) {
		this.pu = pu;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getCodes() {
		return codes;
	}

	public void setBzj(double bzj) {
		this.bzj = bzj;
	}

	public double getBzj() {
		return bzj;
	}

 
	
}
