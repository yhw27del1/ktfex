package com.kmfex.webservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.jws.WebService;

import oracle.jdbc.driver.OracleTypes;

import org.springframework.stereotype.Component;

import com.kmfex.Constant;
import com.kmfex.InvestVO;
import com.kmfex.MoneyFormat;
import com.kmfex.action.cmb.CMBVO;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.cache.utils.CacheManagerUtils;
import com.kmfex.cache.vo.Cache;
import com.kmfex.cmb.request.merchant.MerChantRequest6200;
import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.Announcement;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.CostItem;
import com.kmfex.model.CreditRules;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestCheckKey;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberBase;
import com.kmfex.model.VersionRestrain;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.AnnouncementService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.CostCategoryService;
import com.kmfex.service.CreditRulesService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.InvestCheckKeyService;
import com.kmfex.service.InvestConditionService;
import com.kmfex.service.InvestRecordCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.service.UserGroupRestrain1Service;
import com.kmfex.service.UserGroupService;
import com.kmfex.service.VersionRestrainService;
import com.kmfex.service.cmb.CmbDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.kmfex.webservice.vo.AcVo;
import com.kmfex.webservice.vo.AccountResult;
import com.kmfex.webservice.vo.AccountVo;
import com.kmfex.webservice.vo.BuyingResult;
import com.kmfex.webservice.vo.BuyingVo;
import com.kmfex.webservice.vo.FinancingBaseResult;
import com.kmfex.webservice.vo.FinancingBaseVo;
import com.kmfex.webservice.vo.InvestRecordCostResult;
import com.kmfex.webservice.vo.InvestRecordCostVo;
import com.kmfex.webservice.vo.InvestRecordResult;
import com.kmfex.webservice.vo.InvestRecordVo;
import com.kmfex.webservice.vo.MemberGuaranteeReSult;
import com.kmfex.webservice.vo.MemberGuaranteeVo;
import com.kmfex.webservice.vo.MessageTip;
import com.kmfex.webservice.vo.MyAcResult;
import com.kmfex.webservice.vo.MyAccountVo;
import com.kmfex.webservice.vo.MySgResult;
import com.kmfex.webservice.vo.SellingResult;
import com.kmfex.webservice.vo.SellingVo;
import com.kmfex.webservice.vo.SgVo;
import com.kmfex.webservice.vo.TzResult;
import com.kmfex.webservice.vo.TzVo;
import com.kmfex.webservice.vo.ZhaiQuanRecordResult;
import com.kmfex.webservice.vo.ZhaiQuanRecordVo;
import com.kmfex.webservice.vo.ZqOrderReSult;
import com.kmfex.webservice.vo.ZqOrderVo;
import com.kmfex.zhaiquan.model.Buying;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.model.ZQBuySellRule;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
import com.kmfex.zhaiquan.service.ZQBuySellRuleService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.CloseSoftUtils;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.MD5;
import com.wisdoor.core.vo.CommonVo;

/**
 * 投融资业务、债权业务 
 * @author 访问地址：http://localhost:8080/services/kmfexService?wsdl
 * * 修改记录  
 * 2013年5月29日  修改currentZqs()方法,符合转让天数、逾期天数过滤加入特殊情况(没维护记录，只有一条记录)查询
 * 2013年06月05日14:00  增加债权功能登陆方法clogin()方法,约束是否能使用债权功能
 * 2013年06月07日15:00  增加融资投标列表方法cInvestList2()方法,控制优先投标
 * 2013年06月20日13:14  增加签署过补充协议方法agreementUpdate()方法
 * 2013年06月20日14:04  修改登陆方法login()和clogin(),多返回一个参数MessageTip.param8,'true'已经签署过补充协议 ,'false'未签署过补充协议
 * 2013年06月28日11:24  修改投标优先方法cInvestList2(),融资结束的根据组过滤显示
 * 2013年07月09日10:34  修改方法swithInvestList(),给termStr interestDay赋值
 * 2013年07月17日13:15  修改方法guaranteeCondition(),给担保公司详细信息
 * 2013年07月26日13:14  修改方法cInvestList2(),investList2(),invest(),investNewUI(),investUI()发布信息状态的控制
 * 2013年09月25日14:39  修改clogin（）方法,新增字段useFlag等于停用，无法使用债权版本 
 * 2013年11月13日14:39  缓存修改investList()、investList2()、cInvestList()  
 * 2013年11月22日13:20  手机app新增方法myAccount()、mySgList()、myAcList()我的账号情况,我的投标情况,我的资金流水 
 * 2013年11月22日16:17  手机app新增方法tzs()通知公告 
 * 2013年12月04日16:17  修改方法invest(),解决定向投标问题
 * 2014年01月15日12:57  修改方法mySgList(),显示‘已还清’文字
 * 2014年01月27日14:10  债权myBuys()、mySells()方法的优化 
 */
@Component
@WebService(serviceName = "kmfexService")
public class KeFexService implements Serializable {
	private static final long serialVersionUID = 1883441789007065264L;
	public static final String  fields="select  id, code, shortname, maxamount, currenyamount, curcaninvest, rate, term, returnpattern, haveinvestnum, fxbzstate, fxbzstatename, startdate, enddate, qyzs, fddbzs, czzs, dbzs, zhzs, zhzsstar, qyzsnote, fddbzsnote, czzsnote, dbzsnote, zhzsnote, yongtu, address, industry, companyproperty, state_, statename, financierid, financiercode, financiername, guaranteeid, guaranteecode, guaranteename, logoname, guaranteenote, terminal, to_char(modifydate,'yyyy-MM-dd HH24:mi:ss') as modifydate, createdate, preinvest, interestday, businesstypeid ";

	public KeFexService() {
	}

	@Resource
	transient UserService userService;
	@Resource 
	transient UserPriorityService userPriorityService;
	@Resource
	transient FinancingBaseService financingBaseService;
	@Resource
	transient FinancingCacheService financingCacheService;
	@Resource transient UserGroupService userGroupService;
	@Resource
	transient InvestRecordService investRecordService;
	@Resource
	transient InvestRecordCostService investRecordCostService;
	@Resource
	transient MemberBaseService memberBaseService;
	@Resource
	transient AccountService accountService;
	@Resource
	transient CostCategoryService costCategoryService;// 收费项目
	@Resource
	transient ChargingStandardService chargingStandardService;// 收费明细
	@Resource
	transient InvestConditionService investConditionService;
	@Resource
	transient ContractKeyDataService contractKeyDataService;
	@Resource
	transient AccountDealService accountDealService;
	@Resource
	transient OpenCloseDealService openCloseDealService;
	@Resource
	transient HxbankDealService hxbankDealService;
	@Resource
	transient MemberGuaranteeService memberGuaranteeService;
	@Resource
	transient OrgService orgService;
	@Resource
	transient BuyingService buyingService;
	@Resource
	transient SellingService sellingService;
	@Resource
	transient CreditRulesService creditRulesService;
	@Resource
	transient ZQBuySellRuleService zhaiquanRuleService;
	@Resource
	transient PaymentRecordService paymentRecordService;

	@Resource
	transient ContractService contractService;
	@Resource
	transient VersionRestrainService versionRestrainService;
	@Resource
	transient FinancingRestrainService financingRestrainService;
	@Resource transient UserGroupRestrain1Service userGroupRestrain1Service;  
	@Resource transient InvestCheckKeyService investCheckKeyService; 
	@Resource transient AnnouncementService announcementService;  
	
	private BigDecimal  out_CURCANINVEST;
	private BigDecimal out_CURRENYAMOUNT;
	private BigDecimal out_balance;
	private Date out_startdate;
	private Date out_enddate;
	private String out_state; 
	private BigDecimal code;
	private String message; 
	
	private String  out_fbCode; 
	private BigDecimal out_fbPreInvest;
    private BigDecimal out_fbRate; 
    private BigDecimal out_fbInterestDay;
    
    private String  out_memberId;
    private String  out_memberState;
    private String  out_memberCategory;
    private String  out_memberLevelname;
    private String  out_memberLevelId;
    
    
    private String  out_userPassword;
    private BigDecimal out_userIsEnabled;
    private String  out_userUserType;
    private BigDecimal out_userAccountState;
    private BigDecimal out_userAccountId;
    private BigDecimal out_userId;
    private BigDecimal out_userAccountCredit;
    private BigDecimal out_minStart;
 
	

	@Resource transient private CmbDealService cmbDealService;
	@Resource transient private AccountDealService acccountDealService;
 
	
    /**
     * 通知公告
     * @param str1 用户类型(T=投资,R=融资,D=担保,Y=银行,Q=其他)
     * @param str2 第几页
     * @param str3 每页显示几条数据  
     */
	public TzResult tzs(String str1,int str2,int str3) throws Exception{ 
	   TzResult res=new TzResult();
	   try { 
		     PageView<Announcement> pageView = new PageView<Announcement>(str3, str2);
			 if(str1 !=null && !"".equals(str1)){ 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
				String now = sdf.format(new Date());
				List<String> paras=new ArrayList<String>();
				paras.add(str1);
				paras.add(now);
				StringBuilder sb = new StringBuilder(" 1=1 ");
				sb.append(" and (o.target = ? or o.target = 'A')");
				sb.append(" and o.audit_state = 2 ");
				sb.append(" and o.endtime >= to_date(substr(?,1,10),'yyyy-MM-dd') "); 
				pageView.setQueryResult(announcementService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), paras));
				List<TzVo> vos=new ArrayList<TzVo>(); 
				for(Announcement nt:pageView.getRecords()){
					vos.add(new TzVo(nt.getTitle(), sdf.format(nt.getAddtime()), sdf.format(nt.getEndtime()),nt.getId()));
			    }
				res.setMsg("成功");
				res.setSuccess(true);
				res.setTotalpage(Integer.parseInt(pageView.getTotalpage()+""));
				res.setTotalrecord(Integer.parseInt(pageView.getTotalrecord()+""));
				res.setTzs(vos);
			}else{  
				res.setMsg("无记录");
				res.setTotalpage(0);
				res.setTotalrecord(0); 
			}	 
		} catch (Exception e) {
			 res.setMsg("异常");
			 res.setSuccess(false);
			 res.setTotalpage(0);
			 res.setTotalrecord(0); 
		} 
		return res;
	}
 
	
	 /**
     * 我的账号情况
     * @param userName 用户名
     * @return
     */
	public MyAccountVo myAccount(String userName) {  
		MyAccountVo vo=new MyAccountVo(true, "查询成功");
		User user = null;
		  try {
			if (null != userName && !"".equals(userName)) {
				user = userService.findUser(userName);
				if (null == user) {
					vo.setSuccess(false);
					vo.setMsg("用户不存在");
					return vo;
				}  
			}   
			 
			double cyzq = this.accountService.cyzq(user.getUsername());
			Account account = user.getUserAccount();
			account.setCyzq(cyzq); 
			vo.setStr5(DoubleUtils.doubleCheck2(account.getCyzq(),2));
			vo.setStr2(DoubleUtils.doubleCheck2(account.getBalance(),2));
			vo.setStr3(DoubleUtils.doubleCheck2(account.getFrozenAmount(),2));
			vo.setStr4(DoubleUtils.doubleCheck2(account.getOld_balance(),2));
			vo.setStr1(DoubleUtils.doubleCheck2(account.getTotalAmount(),2)); 
		   } catch (Exception e) {
			    vo.setSuccess(false);
				vo.setMsg("查询异常");
				return vo; 
		  } 
		return vo;
	}

	
	/**
	 * 我的投标情况
	 * @param userName 用户名
	 * @param page 第几页
	 * @param showRecord 每页显示几条
	 * @return
	 */
	public MySgResult mySgList(String userName,int page,int showRecord) { 
		MySgResult res=new MySgResult();
		//String res=""; 
		try {			 
			
			String fields=" ir.zhaiQuanCode,fb.shortname as fshortname,fb.rate,fb.interestday,bt.term as BUSINESSTYPE,bt.returnpattern,ir.investamount,ckd.interest_allah,ir.bjye,ir.lxye,ir.state,ir.xyhkr,zb.buyingprice as cbj,zb.zqfwf as fwf,zb.zqsf as sf,ir.transferflag ,ir.id,fb.terminal";
			String tablename="t_financing_base fb,t_business_type bt,t_contract_key_data ckd, sys_user su,t_member_base mb, t_invest_record ir  left join zq_Buying zb  on  zb.investrecord_id=ir.id";
			StringBuilder sb = new StringBuilder(" ir.financingbase_id=fb.id and bt.id=fb.businesstype_id and ckd.inverstrecord_id=ir.ID and su.id=mb.user_id and mb.id=ir.investor_id");
			sb.append(" and su.username='"+userName+"'");
			sb.append(" order by ir.createdate desc "); 
			List<Map<String, Object>> result=accountDealService.queryForList(fields, tablename, sb.toString(), page, showRecord);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			List<SgVo> vos=new ArrayList<SgVo>();  
			for(int i=0;i<result.size();i++){ 
				String zhaiquancode="";//债权编号 
				String fshortname="";//简称
				String rate="";//年利率 
				String term="";//期限 
				String returnpattern="";//还款方式 
				double investamount=0;//协议本金
				double interest_allah=0;//协议利息	
				double bjye=0;//本金余额
				double lxye=0;//利息余额 
				String state="";//状态
				String xyhkr="";//下一还款日  
				String demo="";//说明
				
				if (null != result.get(i).get("zhaiquancode")){
					zhaiquancode=result.get(i).get("zhaiquancode").toString();
				} 
				if (null != result.get(i).get("fshortname")){
					fshortname=result.get(i).get("fshortname").toString();
				} 
				if (null != result.get(i).get("rate")){
					rate=result.get(i).get("rate").toString();
				} 
				if (null != result.get(i).get("interestday")){ 
					if("0".equals(result.get(i).get("interestday").toString())){
						term=result.get(i).get("businesstype").toString()+"月";
					}else{
						term=result.get(i).get("interestday").toString()+"天";
					}
				}   
				if (null != result.get(i).get("returnpattern")){
					returnpattern=result.get(i).get("returnpattern").toString();
				} 
				if (null != result.get(i).get("investamount")){
					investamount=Double.parseDouble(result.get(i).get("investamount").toString());
				} 
				if (null != result.get(i).get("interest_allah")){
					interest_allah=Double.parseDouble(result.get(i).get("interest_allah").toString());
				} 
				if (null != result.get(i).get("bjye")){
					bjye=Double.parseDouble(result.get(i).get("bjye").toString());
				} 
				if (null != result.get(i).get("lxye")){
					lxye=Double.parseDouble(result.get(i).get("lxye").toString());
				}  
				if (null != result.get(i).get("state")){ 
					if("1".equals(result.get(i).get("state").toString())){
						state="待签约";
					}else if("2".equals(result.get(i).get("state").toString())){
						state="已签约";
					}else if("3".equals(result.get(i).get("state").toString())){
						state="已撤单";
					}
				} 
				if (null != result.get(i).get("xyhkr")){ 
					if("1".equals(result.get(i).get("state").toString())){
						xyhkr="-";
					}else if("2".equals(result.get(i).get("state").toString())){
						xyhkr=format.format(format.parse(result.get(i).get("xyhkr").toString()));
					}else if("3".equals(result.get(i).get("state").toString())){
						xyhkr="项目撤销";
					}else if(bjye+lxye==0){
						xyhkr="已还清";
					}else{
						xyhkr="-";
					} 
				}else{
					xyhkr="-";
				} 
				
				if("1".equals(result.get(i).get("terminal").toString())){ 
					 xyhkr="已还清"; 
				}
				
				String cbj="0";
				if (null != result.get(i).get("cbj")){ 
					 cbj=result.get(i).get("cbj").toString();
					 
				}  
				String fwf="0";
				if (null != result.get(i).get("fwf")){ 
					fwf=result.get(i).get("fwf").toString();
					
				}  
				String sf="0";
				if (null != result.get(i).get("sf")){ 
					sf=result.get(i).get("sf").toString();
					
				}  
				if (null != result.get(i).get("transferflag")){ 
					if(!"2".equals(result.get(i).get("transferflag").toString())){
						demo="投标额:"+DoubleUtils.doubleCheck2(Double.parseDouble(result.get(i).get("investamount").toString()),2)+"元,服务费:0元";
					}else{
						demo="买入成本价:"+DoubleUtils.doubleCheck2(Double.parseDouble(cbj),2)+"元,费用:"+DoubleUtils.doubleCheck2((Double.parseDouble(fwf)+Double.parseDouble(sf)),2)+"元";
					} 
				}  
				 
				SgVo vo=new SgVo();
				vo.setStr1(zhaiquancode);
				vo.setStr2(fshortname);
				vo.setStr3(rate+"%");
				vo.setStr4(term);
				vo.setStr5(returnpattern);
				vo.setStr6(DoubleUtils.doubleCheck2(investamount,2));
				vo.setStr7(DoubleUtils.doubleCheck2(interest_allah,2));
				vo.setStr8(DoubleUtils.doubleCheck2(bjye,2));
				vo.setStr9(DoubleUtils.doubleCheck2(lxye,2));
				String dl1=DoubleUtils.doubleCheck2(Double.parseDouble(vo.getStr8()),2);
				String dl2=DoubleUtils.doubleCheck2(Double.parseDouble(vo.getStr9()),2);
				Double sum=Double.parseDouble(dl1)+Double.parseDouble(dl2);
				vo.setStr10(sum+"");
				vo.setStr11(state);
				vo.setStr12(xyhkr);
				vo.setStr13(demo);
				vos.add(vo);
				
			}  
			res.setMsg("查询成功");
			res.setSuccess(true);
			//res.setTotalpage(totalpage);
			//res.setTotalrecord(totalrecord);
		   int totalrecord = this.investRecordService.queryForListTotal("ir.id",tablename,sb.toString());
		   int Totalpage=totalrecord%showRecord==0? totalrecord/showRecord : totalrecord/showRecord+1;//总页数
		   res.setTotalpage(Totalpage);
		   res.setTotalrecord(totalrecord);			
		   res.setSgs(vos); 
			
		 } catch (Exception e) {
			 res.setMsg("查询异常");
			 res.setSuccess(false);
			 res.setTotalpage(0);
			 res.setTotalrecord(0); 
			 e.printStackTrace();
		}

		return res;
	}
    /**
     * 我的资金流水
     * @param userName 用户名
     * @param page 第几页
     * @param showRecord 每页显示几条
     * @param startDate  开始时间
     * @param endDate  结束时间
     * @param type 类型{'all':'全部','charge':'充值','cash':'提现','invest_out':'投标','payment_in':'还款','bank_zhengquan':'银商转账','zqzr':'债权'}
     * @return
     */
	public MyAcResult myAcList(String userName,int page,int showRecord, Date startDate, Date endDate,String type) {// 我的现金流水
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		if(null==startDate){
			startDate = today;
		}
		if(null==endDate){
			endDate = today;
		}
		Date endDateNext = DateUtils.getAfter(endDate, 1);
		MyAcResult res=new MyAcResult(); 
		try {
			User u = null;
			if (null != userName && !"".equals(userName)) {
				u = userService.findUser(userName);
				if (null == u) {
					 res.setMsg("用户不存在");
					 res.setSuccess(false);
					 res.setTotalpage(0);
					 res.setTotalrecord(0); 
					 return res;
				}  
			}  
			//PageView<AccountDeal> pageView = new PageView<AccountDeal>(showRecord, page);
			Account account = this.accountService.selectById(u.getUserAccount().getId()); 
			String checkFlag = "";
			if ("all".equals(type) || null == type) {// 全部
				if (null ==type) {
					type = "all";
				}
				checkFlag = "('1','3','4','5','9','11','12','13','14','20','21','22','23','24','25','26','27','31','34','37')";
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
			} else {
				checkFlag = "('1','3','4','5','9','11','12','13','14','20','21','22','23','24','25','26','27')";
			}
			StringBuilder sb = new StringBuilder(" sa.accountid_=o.account_accountid_ ");
			sb.append(" and ");
			sb.append(" o.CHECKFLAG in " + checkFlag);// 投资人只能看：充值审核通过；提现等待审核；提现审核通过；提现审核驳回；还款划入
			sb.append(" and ");
			sb.append(" o.TYPE not in ('" + AccountDeal.TZKHR + "'," + "'" + AccountDeal.RZKJG + "','" + AccountDeal.ZQMCF + "','" + AccountDeal.ZQMRF + "')");
			sb.append(" and ");
			sb.append(" o.ACCOUNT_ACCOUNTID_='" + account.getId() + "' ");
			sb.append(" and ");
			sb.append(" o.CREATEDATE >= to_date('" + format.format(startDate) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.CREATEDATE <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			///System.out.println("myAcList----satartDate="+format.format(startDate)+"------endDateNext="+format.format(endDateNext));
			sb.append(" order by o.CREATEDATE desc ");
			String fields="to_char(o.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate,o.businessflag,o.type,o.checkflag,o.checkflag2,o.zhaiquancode,o.memo,o.bj,o.lx,o.fj,o.money,o.nextmoney,fb.code,sa.accountid_ as id";
			String tablename="sys_account sa,t_accountdeal o left join t_Financing_Base fb on fb.id=o.financing_id ";
			// 来自资金明细(充值和提现)
			//pageView.setQueryResult(accountDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), new ArrayList<String>()));
			List<Map<String, Object>> result=accountDealService.queryForList(fields, tablename, sb.toString(), page, showRecord);

			List<AcVo> vos=new ArrayList<AcVo>();
			for (Map<String, Object> obj : result) {
				if (null != obj.get("id")) { 
					Date createDate=format1.parse(obj.get("createdate").toString());
					String date=format.format(createDate);
					String time=formatTime.format(createDate);
					String businessFlag=obj.get("businessFlag").toString();
					String typeTemp=obj.get("type").toString();    
					String checkflag=obj.get("checkflag").toString();    
					String checkflag2=obj.get("checkflag2").toString();    
					
					String typeStr="";
					if("20".equals(businessFlag)){
						typeStr="融资提现";
					}else if("21".equals(businessFlag)){
						typeStr="还款充值";
					}else if("22".equals(businessFlag)){
						typeStr="履约充值";
					}else{
						typeStr=typeTemp;
					}
					
					
					
					if("提现".equals(typeTemp)){
						if("3".equals(checkflag)){
							typeStr+="(待审核)";
						} 
						if("5".equals(checkflag)){
							typeStr+="(提现驳回)";
						} 
						if("4".equals(checkflag)&&"0".equals(checkflag2)){
							typeStr+="(待划款)";
						} 
						if("4".equals(checkflag)&&"1".equals(checkflag2)){
							typeStr+="(已划款)";
						} 
						if("4".equals(checkflag)&&"2".equals(checkflag2)){
							typeStr+="(转账异常)";
						} 
						if("4".equals(checkflag)&&"3".equals(checkflag2)){
							typeStr+="(提现错误)";
						}   
					}
					if("22".equals(checkflag)||"23".equals(checkflag)){
						if("0".equals(checkflag2)){
							typeStr+="(待审核)";
						} 
						if("3".equals(checkflag2)){
							typeStr+="(已审核)";
						} 
						if("4".equals(checkflag2)){
							typeStr+="(已驳回)";
						} 
						 
					}
					if("25".equals(checkflag)){ 
					   typeStr+="(待审核)";  
					}
					if("26".equals(checkflag)){ 
						typeStr+="(已审核)";  
					}
					if("27".equals(checkflag)){ 
						typeStr+="(已驳回)";  
					}    
					
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
				   String zf=""; 
				   if(typeTemp.equals(AccountDeal.CASH)
						||typeTemp.equals(AccountDeal.HKHC)
						||typeTemp.equals(AccountDeal.RZKJG)
						||typeTemp.equals(AccountDeal.SGHC)
						||typeTemp.equals(AccountDeal.ZQMR)
						||typeTemp.equals(AccountDeal.DDHC)
						||typeTemp.equals(AccountDeal.NBZC)
						||typeTemp.equals(AccountDeal.ZQ2BANK)
						||typeTemp.equals("资金冻结")){
					zf = "-";
				    }else{
							if(typeTemp.equals(AccountDeal.ZHGLF)||typeTemp.equals(AccountDeal.DBF)){
								if(checkflag.equals("15")){//针对融资方展现为负
									zf = "-";
								}else if(checkflag.equals("16")){//针对第三方展现为正
									zf = "";
								}else{
									zf = "";
								}
							}else{
								zf = "";
							}
							if(typeTemp.equals(AccountDeal.JSHC)){
								zf = "-";
							}
					} 
				    AcVo vo=new  AcVo(); 
					vo.setStr1(date);
					vo.setStr2(time);
					vo.setStr3(typeStr);
					vo.setStr4(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("bj").toString()),2));
					vo.setStr5(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("lx").toString()),2));
					vo.setStr6(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("fj").toString()),2)); 
					if(!"-".equals(zf)){
						vo.setStr7(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("money").toString()),2)); 
						vo.setStr8("");
					}else{ 
						vo.setStr7(""); 
						vo.setStr8(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("money").toString()),2));
					} 
					vo.setStr9(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("nextmoney").toString()),2));
					vo.setStr10(memo); 
					vo.setStr11(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("money").toString()),2)); 
					vo.setStr12(DoubleUtils.doubleCheck2(Double.parseDouble(obj.get("nextmoney").toString()),2));
					vo.setStr13(zhaiQuanCode);
					vo.setStr14(financingCode);
					vos.add(vo);	 
				}

			}
			 
			 res.setMsg("查询成功");
			 res.setSuccess(true);
			 int totalrecord = this.financingBaseService.queryForListTotal("sa.accountid_",tablename,sb.toString());
			 int Totalpage=totalrecord%showRecord==0? totalrecord/showRecord : totalrecord/showRecord+1;//总页数
			 res.setTotalpage(Totalpage);
			 res.setTotalrecord(totalrecord);
			 res.setAcs(vos);
		} catch (Exception e) {
			 res.setMsg("查询异常");
			 res.setSuccess(false);
			 res.setTotalpage(0);
			 res.setTotalrecord(0);
			 e.printStackTrace();
		}
		return res;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	/***************************************************************************
	 * 用户登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @return
	 */
	public MessageTip newlogin(String userName, String password, String userType) {
		MessageTip tip = new MessageTip(true, "用户登录成功");
		try {
			/*
			 * User user = userService.findUser(userName); if (null == user) {
			 * tip.setSuccess(false); tip.setMsg("用户名不存在"); return tip; }
			 */
			password = MD5.MD5Encode(password);
			User user = userService.findUser(userName, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("会员编号或密码错误");
				return tip;
			} else {
				if (!user.isEnabled()) {
					
					if (user.getUserState().equals("1")){
					   tip.setMsg("会员待审核,暂不允许登录");
					   tip.setSuccess(false);
					   //return tip;
					}
					if (user.getUserState().equals("4")){
					   tip.setMsg("会员已停用,不允许登录");
					   tip.setSuccess(false);
					   //return tip;
					}
					if (user.getUserState().equals("3")){
					   tip.setMsg("未通过审核,不允许登录");
					   tip.setSuccess(false);
					}
					return tip;
					
				}
				if (!user.getUserType().equals(userType)) {
					tip.setSuccess(false);
					tip.setMsg("会员类型错误");
					return tip;
				}
			}
			tip.setParam1(user.getRealname());// 真实姓名
			MemberBase memberBase = memberBaseService.getMemByUser(user);
			if (null != memberBase) {
				if (memberBase.getState().equals(MemberBase.STATE_STOPPED)) {
					tip.setSuccess(false);
					tip.setMsg("会员已注销");
					return tip;
				}
				tip.setParam2(orgService.findOrg(memberBase.getOrgNo()).getName() + "(" + memberBase.getOrgNo() + ")");
			}// 开户机构名称
			Set<Role> roles = user.getRoles();
			for (Role r : roles) {
				if (Constant.INITROLEID == r.getId())// 初始角色要修改密码才能使用
				{
					tip.setParam3("初始角色");
					return tip;
				}
			}
			if (null != memberBase.getMemberLevel()) {
				tip.setParam4(memberBase.getMemberLevel().getLevelname());
			}
			tip.setDateStr(getServerTime());
			tip.setParam5(memberBase.getBanklib().getCaption());
			tip.setParam6(memberBase.getUser().getFlag());//签约标志:未签约=0；签约中=1；已签约=2；已解约=3
			tip.setParam9(memberBase.getUser().getSignBank()+"");//三方存管签约行:未签约三方存管=0,签约华夏三方存管=1,签约招商三方存管=2
			tip.setParam10(memberBase.getUser().getSignType()+"");//三方存管签约类型:未签约三方存管=0,签约本行=1,签约他行=2
			
			BigDecimal b = new BigDecimal(memberBase.getUser().getUserAccount().getOld_balance()+"");
			double param11 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
			tip.setParam7(param11+ "");
			
			tip.setParam8(memberBase.getUser().isAgreement() + "");//是否签署过补充协议   
			tip.setParam11(userPriorityService.firstAutoInvest(userName) + "");//是否委托自动投标  
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("用户登录出现异常");
			return tip;
		}
	}
	
	/***************************************************************************
	 * 用户登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @return
	 */
	public MessageTip login(String userName, String password, String userType) {
		MessageTip tip = new MessageTip(false, "该版本已经停用，请到www.kmfex.com下载新版客户端!");
	    return tip;
		 
	}
	/**
	 * 用户登录(债权功能是否开放的登陆) 
	 * @param userName 用户名
	 * @param password 密码
	 * @param userType 用户类型
	 * @return
	 **/
	public MessageTip clogin(String userName, String password, String userType) {
		MessageTip tip = new MessageTip(true, "用户登录成功");
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			VersionRestrain vr=versionRestrainService.selectById(userName);
			if (null == vr) {
				tip.setSuccess(false);
				tip.setMsg("该版本未对您开放,不允许登录");
				return tip;
			} else {
				if(0==vr.getUseFlag()){
					tip.setSuccess(false);
					tip.setMsg("该版本用户已停用,不允许登录");
					return tip;
				}
				long now=Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));
				if(now>vr.getEndDate()){
					tip.setSuccess(false);
					tip.setMsg("该版本已过期,不允许登录");
					return tip;
				} 
			}
			
			password = MD5.MD5Encode(password);
			User user = userService.findUser(userName, password);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名或密码错误");
				return tip;
			} else {
				if (!user.isEnabled()) {
					tip.setSuccess(false);
					tip.setMsg("用户待审核,暂不允许登录");
					return tip;
				}
				if (!user.getUserType().equals(userType)) {
					tip.setSuccess(false);
					tip.setMsg("用户类型错误");
					return tip;
				}
			}
			tip.setParam1(user.getRealname());// 真实姓名
			MemberBase memberBase = memberBaseService.getMemByUser(user);
			if (null != memberBase) {
				tip.setParam2(orgService.findOrg(memberBase.getOrgNo()).getName() + "(" + memberBase.getOrgNo() + ")");
			}// 开户机构名称
			Set<Role> roles = user.getRoles();
			for (Role r : roles) {
				if (Constant.INITROLEID == r.getId())// 初始角色要修改密码才能使用
				{
					tip.setParam3("初始角色");
					return tip;
				}
			}
			if (null != memberBase.getMemberLevel()) {
				tip.setParam4(memberBase.getMemberLevel().getLevelname());
			}
			tip.setDateStr(getServerTime());
			tip.setParam5(memberBase.getBanklib().getCaption());
			tip.setParam6(memberBase.getUser().getFlag());
			tip.setParam7(memberBase.getUser().getUserAccount().getOld_balance() + "");
			tip.setParam8(memberBase.getUser().isAgreement() + "");//是否签署过补充协议 
			tip.setParam11(userPriorityService.firstAutoInvest(userName) + "");//是否委托自动投标   
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("用户登录出现异常");
			return tip;
		}
	}

	public MessageTip oldBalance(String userName) {
		MessageTip tip = new MessageTip(true, "获取oldbalance成功");
		try {
			User user = userService.findUser(userName);
			tip.setParam1(user.getUserAccount().getOld_balance() + "");// oldbalance
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("获取oldbalance异常");
			return tip;
		}
	}
	
	/** 
	 * 签署过补充协议
	 * @param userName 用户名
	 * @param flag 签署状态（true设置为同意，flase设置为不同意）
	 * @return
	 */
	public MessageTip agreementUpdate(String userName,String password,boolean flag) {
		
		String flagStr="不同意";
		if(flag){ flagStr="同意"; }
		
		MessageTip tip = new MessageTip(true, flagStr+"签署过补充协议设置成功");
		try {
			password=MD5.MD5Encode(password);
			User user = userService.findUser(userName);
			if(null==user){
				tip.setSuccess(false);
				tip.setMsg("用户不存在");
				return tip;
			}
			if(!user.getPassword().equals(password)){
				tip.setSuccess(false);
				tip.setMsg("密码错误");
				return tip;
			}
			user.setAgreement(flag);
			this.userService.update(user);
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg(flagStr+"签署过补充协议设置操作异常");
			return tip;
		}
	}

	// 获取服务器的时间戳
	public String getServerTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}

	/***************************************************************************
	 * 用户修改密码
	 * 
	 * @param userName
	 *            用户名
	 * @param newPassword
	 *            新密码
	 * @param oldPassword
	 *            原密码
	 * @param userType
	 *            用户类型
	 * @return
	 */
	public MessageTip updatePassword(String userName, String userType, String oldPassword, String newPassword) {
		MessageTip tip = new MessageTip(true, "用户修改密码成功");
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型不能为空");
				return tip;
			}
			if (BaseTool.isNull(oldPassword)) {
				tip.setSuccess(false);
				tip.setMsg("原密码不能为空");
				return tip;
			}
			if (BaseTool.isNull(newPassword)) {
				tip.setSuccess(false);
				tip.setMsg("新密码不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			oldPassword = MD5.MD5Encode(oldPassword);
			user = userService.findUser(userName, oldPassword);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("原密码错误");
				return tip;
			} else {
				if (!user.getUserType().equals(userType)) {
					tip.setSuccess(false);
					tip.setMsg("用户类型错误");
					return tip;
				}
			}
			user.setPassword(MD5.MD5Encode(newPassword));
			if ("R".equals(user.getUserType()))// 还是融资方
			{
				user.getRoles().clear();
				user.getRoles().add(new Role(Constant.RZROLEID));
			}
			if ("T".equals(user.getUserType()))// 判断是投资人
			{
				user.getRoles().clear();
				user.getRoles().add(new Role(Constant.TZROLEID));
			}
			userService.update(user);

			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("用户修改密码出现异常");
			return tip;
		}
	}

	/**
	 * 查询可以投标融资项目列表 按项目编号排序 
	 * @param keyWord  可以根据keyWord模糊查询项目编码和项目简称,为空表示查询全部
	 * @return
	 */
	public FinancingBaseResult investList(String keyWord) {
		FinancingBaseResult result = new FinancingBaseResult();
		
		MessageTip tip = new MessageTip(true, "操作成功"); 
		try {  
			
			//1、先从缓存里取
			try {
				Set<String> financingBaseCodes=financingCacheService.getFinancingBaseCodes(); 
				for(String str:financingBaseCodes) {
					Cache cache=CacheManagerUtils.getCacheInfo("doing_"+str);
					FinancingBaseVo fbvo=(FinancingBaseVo)cache.getValue(); 
					
					if(keyWord.equals(fbvo.getCode())){ 
						List<FinancingBaseVo> financingBaseVoS =  new ArrayList<FinancingBaseVo>();
						financingBaseVoS.add(fbvo);   
						result.setFinancings(financingBaseVoS); 
						result.setMessageTip(tip); 
						return result;//缓存中有此对象直接返回	
					} 
				}
			 } catch (Exception e) { 
				//e.printStackTrace();
			}

			
			//否则查询数据库视图
			if (BaseTool.isNull(keyWord)) {
				tip.setSuccess(false);
				tip.setMsg("错误");
				result.setMessageTip(tip);
				return result;
			}
			String sql = fields+ " from v_finance_investlist o where o.state_ in ('1.5','2','3','4','5','6','7','8')";
			//sql += "and o.curCanInvest >=0 and  o.terminal=0 "; 
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sql += "and (o.code like '%" + keyWord + "%' or o.shortName like '%" + keyWord + "%'  ) ";
			}
			sql += "and o.modifydate is not null order by o.createDate desc "; 
			ArrayList<LinkedHashMap<String, Object>> dataList = this.financingBaseService.selectListWithJDBC(sql);

			List<FinancingBaseVo> financingBaseVoS = swithInvestList(dataList);

			result.setFinancings(financingBaseVoS);
			
			result.setMessageTip(tip);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}
	}
	

	/**
	 * 开启投标优先的新方法
	 * @param keyWord  可以根据keyWord模糊查询项目编码和项目简称,为空表示查询全部
	 * @param username 用户名
	 * @param param1    字符串
	 * @param param1  MD5加密串
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public FinancingBaseResult cInvestList2(String keyWord, String times,String username,String param1,String param2) {
       FinancingBaseResult result = new FinancingBaseResult();
		MessageTip tip = new MessageTip(true, "操作成功"); 
		
		MessageTip checkTip=checkRequest(username,param1,"",param2);
		if(!checkTip.isSuccess()){
			tip.setSuccess(false);
			tip.setMsg(checkTip.getMsg());
			result.setMessageTip(tip);
			return result;  
		} 
		 
		try { 
			List<FinancingBaseVo> res=new ArrayList<FinancingBaseVo>();
			//投标中的融资项目
			Cache  cache=null;
			Set<String> financingBaseCodes=financingCacheService.getFinancingBaseCodes();
			for(String str:financingBaseCodes) { 
				cache=CacheManagerUtils.getCacheInfo("doing_"+str);
				FinancingBaseVo fbvo=(FinancingBaseVo)cache.getValue();
				if(null!=fbvo.getUsers()&&fbvo.getUsers().size()>0){ 
					if(fbvo.getUsers().contains(username)){
						res.add(fbvo); 
					} 
				}else{ 
					res.add(fbvo); 
				}
			}
			
		 
			//3、结束投标的
			cache=CacheManagerUtils.getCacheInfo("stopInvests");
			List<FinancingBaseVo> stopInvests=(List<FinancingBaseVo>)cache.getValue();
			 
			Long timeDian=0L;
			try {
				times=times.trim();
				//System.out.println("times1111--"+times);  
				timeDian = Long.valueOf(times.substring(0,14));//去除毫秒
				//System.out.println("timeDian--"+timeDian);  
			} catch (Exception e1) {/**e1.printStackTrace();**/ }
			

			int day=0;
			if (null != times && !"".equals(times)) { 
				times=times.trim();
				SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
				Date from = format1.parse(times);
				day=DateUtils.getBetween(from, new Date()); 
				//System.out.println("更新相差天数"+day);  
			}
			
			
			
			for(FinancingBaseVo bvo:stopInvests){
				
				Long modifydate=0L;
				try {
					modifydate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(bvo.getModifyDate()));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(""+bvo.getCode()+"----times="+times+",username="+username+",param1="+param1+",param2="+param2+",modifydate="+modifydate+",users="+bvo.getUsers());
                }  
				
				   if(("0").equals(bvo.getPreInvest())){//公开的包，根据时间戳过滤
						if(day>5){//大于5天没登录,
							//if(modifydate>=timeDian&&!("7").equals(bvo.getState())&&!("8").equals(bvo.getState())){//同步除签约以外的融资项目
							if(modifydate>=timeDian&&!("8").equals(bvo.getState())){ // 同步除撤单以外的融资项目(范围扩大) 
								if(null!=bvo.getUsers()&&bvo.getUsers().size()>0){ 
									if(bvo.getUsers().contains(username)){
										res.add(bvo); 
									} 
								}else{ 
									res.add(bvo); 
								}	 
							} 
						}else{
							if(modifydate>=timeDian){
								if(null!=bvo.getUsers()&&bvo.getUsers().size()>0){ 
									if(bvo.getUsers().contains(username)){
										res.add(bvo); 
									} 
								}else{ 
									res.add(bvo); 
								}	 
							}
						}  
				  }else{//定向的包没有时间戳过滤 
					   if(null!=bvo.getUsers()&&bvo.getUsers().size()>0){
							if(bvo.getUsers().contains(username)){
								res.add(bvo); 
							}   
					 }
				  }
				
			}			
			
			result.setFinancings(res);
			res=null;
			
			result.setMessageTip(tip);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}
	}

	/**
	 * 提现申请
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param money
	 *            提现金额
	 */
	public MessageTip toCash(String userName, String password, String userType, double money,String param1,String param2) {
		
		MessageTip tip = new MessageTip(true, "提现申请成功，我们会在一个工作日里转账至您的银行账户中。");
 
		MessageTip checkTip=checkRequest2(userName,param1,param2);
		if(!checkTip.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg("错误"); 
			return tip;  
		}else{
			InvestCheckKey investCheckKey=investCheckKeyService.selectById(param2);
			if(null!=investCheckKey){
				tip.setSuccess(false);
				tip.setMsg("错误");  
				return tip;  
			}else{
				try {
					investCheckKeyService.insert(new InvestCheckKey(param2,"toCash@"+param1,userName));
				} catch (Exception e) {}
			}  
		}
			
		 
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(password)) {
				tip.setSuccess(false);
				tip.setMsg("密码不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			if(null!=user.getFlag()&&!"".equals(user.getFlag())){
				if("1".equals(user.getFlag())){
					tip.setSuccess(false);
					tip.setMsg("第三方支付签约中，不能提现");
					return tip;
				}
				if("2".equals(user.getFlag())){
					tip.setSuccess(false);
					tip.setMsg("第三方支付用户，不能提现，请下载新版客户端!");
					return tip;
				}
			}
			password = MD5.MD5Encode(password);
			if (!password.equals(user.getPassword())) {
				tip.setSuccess(false);
				tip.setMsg("密码不正确");
				return tip;
			}

			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}

			if (money > user.getUserAccount().getBalance()) {
				tip.setSuccess(false);
				tip.setMsg("提现金额大于帐户余额");
				return tip;
			}
			//工行专户，T+1提现
			if (user.getChannel()==2) {
				if(money>user.getUserAccount().getOld_balance()){
					tip.setSuccess(false);
					tip.setMsg("提现金额大于可提金额,当前可提现金额为:"+user.getUserAccount().getOld_balance()+"元");
					return tip;
				}
			}
			HashMap<String, Object> maped = this.accountService.activityWithdrawAllow(user, money);
			if (!Boolean.parseBoolean(maped.get("boolean").toString())) {//iphone5活动不能提现
				tip.setSuccess(false);
				tip.setMsg("你已参与\"耀5扬微\"活动，限制提现" + maped.get("frozen") + "，当前可提现金额为" + maped.get("surplus"));
				return tip;
			}else{//iphone5可以提现money，继续验证投转贷
				//iphone5限制的金额，可用+冻结+债权-投转贷限制-iphone限制 与 提现金额比较大小
				//double iphone_xianzhi = this.accountService.getIphone5(user);
				double iphone_xianzhi=0;
				HashMap<String, Object> invest_lend = this.accountService.investToLendAllow(user, money , iphone_xianzhi);
				if (!Boolean.parseBoolean(invest_lend.get("boolean").toString())){
					tip.setSuccess(false);
					//投转贷限制金额
					double invest_lend_xianzhi = 0;
					if(null!=invest_lend.get("frozen")){
						invest_lend_xianzhi = Double.parseDouble(invest_lend.get("frozen").toString());
					}
					//if(iphone_xianzhi>0){//参加了iphone5活动，也参加了投转贷
					//	tip.setMsg("你已参与\"耀5扬微\"活动，且是投转贷用户，限制提现" + (iphone_xianzhi+invest_lend_xianzhi) + "，当前可提现金额为" + invest_lend.get("surplus"));
					//}else{//没有参加iphone5活动，或到期；参加了投转贷
						tip.setMsg("你是投转贷用户，限制提现" + invest_lend_xianzhi + "，当前可提现金额为" + maped.get("surplus"));
					//}
					return tip;
				}
			}
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (money <= 0d) {
				tip.setSuccess(false);
				tip.setMsg("提现金额不能为0元或低于0元");
				return tip;
			} else {
				boolean b = this.accountDealService.cash(user.getUserAccount(), money);
				if(!b){
					tip.setSuccess(false);
					tip.setMsg("提现申请异常");
				}
			}
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
			return tip;
		}
	}

	/**
	 * 查询用户投标记录列表
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param startDate
	 *            查询起时间
	 * @param endDate
	 *            查询止时间
	 * @return
	 */
	public InvestRecordResult myRecordListSearch(String userName, String userType, Date startDate, Date endDate) {
		String keyWord = "";
		InvestRecordResult result = new InvestRecordResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				result.setMessageTip(tip);
				return result;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				result.setMessageTip(tip);
				return result;
			}
			// MemberBase m = this.memberBaseService.getMemByUser(user);
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dStartDate = null;
			String dEndDate = null;

			// if(!(null==startDate||null==endDate)){
			dStartDate = format.format(startDate);
			dEndDate = format.format(endDate);
			// }
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + keyWord + "%");
				sb.append(" ) ");
			}
			// sb.append(" and ");
			// sb.append(" o.state='1'");
			sb.append(" and ");
			// sb.append(" o.investor.id='"+m.getId()+"'");
			sb.append(" o.id in (" + contractKeyDataService.findContractKeyDatas(user.getUsername()) + ")"); // 根据合同的投资人找投标记录

			// 时间段查询

			// if(!(null==dStartDate||null==dEndDate)){
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + dStartDate + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + dEndDate + "','yyyy-MM-dd')+1");
			// }

			QueryResult<InvestRecord> qr = investRecordService.getScrollData(sb.toString(), params, orderby);
			List<InvestRecord> lis = qr.getResultlist();
			List<InvestRecordVo> tempfbs = new ArrayList<InvestRecordVo>();
			for (InvestRecord f : lis) {
				InvestRecordVo vo = new InvestRecordVo();
				vo.setPreAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getPreAmount(), 3), false));
				vo.setNextAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getNextAmount(), 3), false));
				vo.setInvestAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getInvestAmount(), 3), false));
				vo.setContractkeydatavo(f.getContract());// 合同
				vo.setCostCreateDate(f.getCost().getCreateDate());
				vo.setCostId(f.getCost().getId());
				vo.setCreateDate(f.getCreateDate());
				vo.setFee1(f.getCost().getFee1());
				vo.setFee2(f.getCost().getFee2());
				vo.setFinancingBaseVo(zhuanhuanFinancingBase(f));
				vo.setId(f.getId());
				vo.setInvestAmount(f.getInvestAmount());
				vo.setInvestAmount_daxie(f.getInvestAmount_daxie());
				vo.setBjye(f.getBjye());
				vo.setLxye(f.getLxye());
				vo.setXyhkr(f.getXyhkr());
				vo.setInvestorCode(f.getInvestor().getUser().getUsername());
				vo.setInvestorId(f.getInvestor().getId());
				vo.setRealAmount(f.getCost() != null ? f.getCost().getRealAmount() : 0d);
				if ("1".equals(f.getInvestor().getCategory())) {
					vo.setInvestorName(f.getInvestor().getpName());
				} else {
					vo.setInvestorName(f.getInvestor().geteName());
				}
				vo.setNextAmount(f.getNextAmount());
				vo.setState(f.getState());
				vo.setPreAmount(f.getPreAmount());
				vo.setSxf(f.getCost().getSxf());
				vo.setTzfwf(f.getCost().getTzfwf());
				vo.setYhs(f.getCost().getYhs());

				tempfbs.add(vo);
			}
			result.setMessageTip(tip);
			result.setRecords(tempfbs);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}

	}

	public InvestRecordResult recordListForUser(String userName, String userType, String keyWord) {
		InvestRecordResult result = new InvestRecordResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				result.setMessageTip(tip);
				return result;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				result.setMessageTip(tip);
				return result;
			}
			// MemberBase m = this.memberBaseService.getMemByUser(user);
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + keyWord + "%");
				sb.append(" ) ");
			}
			// sb.append(" and ");
			// sb.append(" o.state='1'");
			sb.append(" and ");
			// sb.append(" o.investor.id='"+m.getId()+"'");
			// System.out.println("----"+contractKeyDataService.findContractKeyDatas(user.getUsername()));
			sb.append(" o.id in (" + contractKeyDataService.findContractKeyDatas(user.getUsername()) + ")"); // 根据合同的投资人找投标记录
			QueryResult<InvestRecord> qr = investRecordService.getScrollData(sb.toString(), params, orderby);
			List<InvestRecord> lis = qr.getResultlist();
			List<InvestRecordVo> tempfbs = new ArrayList<InvestRecordVo>();
			for (InvestRecord f : lis) {
				InvestRecordVo vo = new InvestRecordVo();
				vo.setPreAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getPreAmount(), 3), false));
				vo.setNextAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getNextAmount(), 3), false));
				vo.setInvestAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getInvestAmount(), 3), false));
				vo.setContractkeydatavo(f.getContract());// 合同
				vo.setCostCreateDate(f.getCost().getCreateDate());
				vo.setCostId(f.getCost().getId());
				vo.setCreateDate(f.getCreateDate());
				vo.setFee1(f.getCost().getFee1());
				vo.setFee2(f.getCost().getFee2());
				vo.setFinancingBaseVo(zhuanhuanFinancingBase(f));
				vo.setId(f.getId());
				vo.setInvestAmount(f.getInvestAmount());
				vo.setInvestAmount_daxie(f.getInvestAmount_daxie());
				vo.setRealAmount(f.getCost() != null ? f.getCost().getRealAmount() : 0d);
				vo.setInvestorCode(f.getInvestor().getUser().getUsername());
				vo.setInvestorId(f.getInvestor().getId());
				vo.setBjye(f.getBjye());
				vo.setLxye(f.getLxye());
				vo.setXyhkr(f.getXyhkr());
				if ("1".equals(f.getInvestor().getCategory())) {
					vo.setInvestorName(f.getInvestor().getpName());
				} else {
					vo.setInvestorName(f.getInvestor().geteName());
				}
				vo.setNextAmount(f.getNextAmount());
				vo.setState(f.getState());
				vo.setPreAmount(f.getPreAmount());
				vo.setSxf(f.getCost().getSxf());
				vo.setTzfwf(f.getCost().getTzfwf());
				vo.setYhs(f.getCost().getYhs());

				tempfbs.add(vo);
			}
			result.setMessageTip(tip);
			result.setRecords(tempfbs);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}

	}

	/**
	 * 查看某个人的帐号信息帐号
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @return
	 */
	public AccountResult accountForUser(String userName, String userType) {
		AccountResult result = new AccountResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				result.setMessageTip(tip);
				return result;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				result.setMessageTip(tip);
				return result;
			}
			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				result.setMessageTip(tip);
				return result;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				result.setMessageTip(tip);
				return result;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				result.setMessageTip(tip);
				return result;
			}
			result.setMessageTip(tip);
			BigDecimal b = new BigDecimal(user.getUserAccount().getBalance()+"");
			double param11 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
			AccountVo account = new AccountVo(user.getUserAccount().getId(), param11);
			result.setAccount(account);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}

	}

	/**
	 * 根据项目编号得到担保情况
	 * 
	 * @param financingBaseId
	 *            项目编号ID
	 */
	public MemberGuaranteeReSult guaranteeCondition(String financingBaseId) {
		//System.out.println("financingBaseId= " + financingBaseId);
		MemberGuaranteeReSult result = new MemberGuaranteeReSult();
		MessageTip tip = new MessageTip(true, "查询成功");
		MemberGuaranteeVo guaranteeVo = new MemberGuaranteeVo();
		try {

			if (BaseTool.isNull(financingBaseId)) {
				tip.setSuccess(false);
				tip.setMsg("融资项目ID不能为空");
				result.setMessageTip(tip);
				result.setMemberGuaranteeVo(guaranteeVo);
				return result;
			}

			FinancingBase financingBase = this.financingBaseService.selectById(financingBaseId);
			if (null != financingBase) {
				if (null != financingBase.getGuarantee()) {
					if (null != financingBase.getMemberGuarantee()) {
						guaranteeVo.setId(financingBase.getMemberGuarantee().getId());
/*						guaranteeVo.setBank(financingBase.getMemberGuarantee().getBank());
						guaranteeVo.setCreateDate(financingBase.getMemberGuarantee().getCreateDate());
						guaranteeVo.setCreateMoney(financingBase.getMemberGuarantee().getCreateMoney());
						guaranteeVo.setCreateYear(financingBase.getMemberGuarantee().getCreateYear());
						guaranteeVo.setDaichanRate(financingBase.getMemberGuarantee().getDaichanRate());
						guaranteeVo.setDbedType(financingBase.getMemberGuarantee().getDbedType());
						guaranteeVo.setEmpNumber(financingBase.getMemberGuarantee().getEmpNumber());*/
						if ("1".equals(financingBase.getGuarantee().getCategory())) {
							guaranteeVo.setGuaranteeName(financingBase.getGuarantee().getpName());
						} else {
							guaranteeVo.setGuaranteeName(financingBase.getGuarantee().geteName());
						}
/*						guaranteeVo.setJingyanNumber(financingBase.getMemberGuarantee().getJingyanNumber());
						guaranteeVo.setJzdType(financingBase.getMemberGuarantee().getJzdType());
						
						guaranteeVo.setType(financingBase.getMemberGuarantee().getType());*/
						//guaranteeVo.setNote(financingBase.getMemberGuarantee().getNote());
						guaranteeVo.setTjzs(financingBase.getMemberGuarantee().getTjzs());
						tip.setSuccess(true);
						tip.setMsg("查询成功");
						result.setMessageTip(tip);
						result.setMemberGuaranteeVo(guaranteeVo);
						return result;
					} else {
						tip.setSuccess(false);
						tip.setMsg("担保情况不存在");
						result.setMessageTip(tip);
						result.setMemberGuaranteeVo(guaranteeVo);
						return result;
					}
				} else {
					tip.setSuccess(false);
					tip.setMsg("担保公司不存在");
					result.setMessageTip(tip);
					result.setMemberGuaranteeVo(guaranteeVo);
					return result;
				}
			} else {
				tip.setSuccess(false);
				tip.setMsg("融资项目不存在");
				result.setMessageTip(tip);
				result.setMemberGuaranteeVo(guaranteeVo);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}

	}

	/**
	 * 投标约束(得到投资人对某个融资项目的的投标额相关情况)
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param financingBaseId
	 *            融资项目ID
	 */
	public InvestVO investUI(String userName, String userType, String financingBaseId) {
		InvestVO vo = new InvestVO();

		if (BaseTool.isNull(userName)) {
			vo.setMsg("用户名不能为空");
			return vo;
		}

		if (BaseTool.isNull(financingBaseId)) {
			vo.setMsg("融资项目不能为空");
			return vo;
		}

		try { 
 
			vo = doCheck(userName, financingBaseId,userType);
			
			if ("0".equals(code.toString())) {
				vo.setMsg(message);
				return vo;
			}

			
			vo.setMoney(Double.parseDouble(out_balance.toString()));

			int days = DateUtils.getBetween(out_startdate, new Date());
			if (days < 0) {
				vo.setMsg("融资还没开始!");
				return vo;
			}
			int daye = DateUtils.getBetween(out_enddate, new Date());

			if (daye > 0) {
				vo.setMsg("融资已经过期!");
				return vo;
			}
			if ("1.5".equals(out_state)) {
				vo.setMsg("待挂单, 暂不允许投标!");
				return vo;
			}
			if ("8".equals(out_state)) {
				vo.setMsg("融资已经撤单!");
				return vo;
			}


			vo.setCanInvest(true);
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMsg("查询异常");
			return vo;
		}
	}

	/**
	 * 投标约束带投标金额(得到投资人对某个融资项目的的投标额相关情况)
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param financingBaseId
	 *            融资项目ID
	 * @param money
	 *            投标金额
	 */
	public InvestVO investNewUI(String userName, String userType, String financingBaseId, double money) {
		InvestVO vo = new InvestVO();
		if (BaseTool.isNull(userName)) {
			vo.setMsg("用户名不能为空");
			return vo;
		}

		if (BaseTool.isNull(financingBaseId)) {
			vo.setMsg("融资项目不能为空");
			return vo;
		}

		if (money <= 0) {
			vo.setMsg("投标金额必须大于0");
			return vo;
		}

		try { 
			vo = doCheck(userName, financingBaseId,userType);
			
			if ("0".equals(code.toString())) {
				vo.setMsg(message);
				return vo;
			}
			
			vo.setMoney(Double.parseDouble(out_balance.toString()));
			if (vo.getMoney() < money) {
				vo.setMsg("投标金额大于帐户余额!");
				return vo;
			}

			if (vo.getMinMoney() > money) {
				vo.setMsg("投标金额不能小于您的最小投标额!");
				return vo;
			}
			if (vo.getMaxMoney() < money) {
				vo.setMsg("投标金额不能大于您的最大投标额!");
				return vo;
			}
			int days = DateUtils.getBetween(out_startdate, new Date());
			if (days < 0) {
				vo.setMsg("融资还没开始!");
				return vo;
			}
			int daye = DateUtils.getBetween(out_enddate, new Date());

			if (daye > 0) {
				vo.setMsg("融资已经过期!");
				return vo;
			}
			if ("1.5".equals(out_state)) {
				vo.setMsg("待挂单, 暂不允许投标!");
				return vo;
			}
			if ("8".equals(out_state)) {
				vo.setMsg("融资已经撤单!");
				return vo;
			}
			vo.setCanInvest(true);
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMsg("查询异常");
			return vo;
		}
	}
	/** 
	 * 验证非法请求(加密传值的约定) 
	 * @param username 用户名
	 * @param financingBaseId=融资项目id
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(username+financingBaseId+param1+MD5(密码))
	 *        32位MD5小写
	 * @return
	 */
	private MessageTip checkRequest(String username,String param1,String financingBaseId,String param2){
		MessageTip tip = new MessageTip(true, "验证成功");
		User user = userService.findUser(username);
		if (null == user) {
			tip.setSuccess(false);
			tip.setMsg("用户名不存在"); 
			return tip;
		}else{
			String keyStr=MD5.MD5Encode(username+param1+financingBaseId+user.getPassword());
			if(!keyStr.equals(param2)){
				tip.setSuccess(false);
				tip.setMsg("");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(format.format(new Date())+":"+financingBaseId+"非法请求的用户:"+username); 
				return tip;
			} 
		}
		return tip;
		
	}
 
	/** 
	 * 验证非法请求2(加密传值的约定) 
	 * @param username 用户名 
	 * @param param1=时间戳或随机数
	 * @param param2=MD5(MD5(密码)+param1+username)
	 *        32位MD5小写
	 * @return
	 */
	private MessageTip checkRequest2(String username,String param1,String param2){
		MessageTip tip = new MessageTip(true, "验证成功");
		User user = userService.findUser(username);
		if (null == user) {
			tip.setSuccess(false);
			tip.setMsg("用户名不存在"); 
			return tip;
		}else{
			String keyStr=MD5.MD5Encode(user.getPassword()+param1+username);
			if(!keyStr.equals(param2)){
				tip.setSuccess(false);
				tip.setMsg("错误");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println(format.format(new Date())+":非法请求的用户:"+username); 
				return tip;
			} 
		}
		return tip;
		
	}  

	/**
	 * 投标
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param money
	 *            投标金额
	 * @param financingBaseId
	 *            融资项目ID
	 */
	public InvestRecordCostResult invest(String userName, String password, String userType, double money, String financingBaseId,String param1,String param2) {
		//long  t0 = System.currentTimeMillis(),t1=0l,t2=0l; 
		MessageTip tip = new MessageTip(true, "投标成功");  
		InvestRecordCostResult result = new InvestRecordCostResult();
		MessageTip tip0 =null;
		if(null!=param1&&!"".equals(param1))//记录接口投标来源
		{ 
			if(param1.endsWith("_android")){ 
				tip0 = CloseSoftUtils.closeAndroidInvest(); 
				if(tip0.isSuccess()){  
					tip0.setSuccess(false);
					tip0.setMsg(tip0.getMsg()); 
					result.setMessageTip(tip0);
					return result;  
				}
			}else if(param1.endsWith("_ios")){
				tip0 = CloseSoftUtils.closeIosInvest(); 
				if(tip0.isSuccess()){  
					tip0.setSuccess(false);
					tip0.setMsg(tip0.getMsg()); 
					result.setMessageTip(tip0);
					return result;  
				}
			}  
		}
		
		/*tip0 = CloseSoftUtils.closePcInvest(); 
		if(tip0.isSuccess()){  
			tip0.setSuccess(false);
			tip0.setMsg(tip0.getMsg()); 
			result.setMessageTip(tip0);
			return result;  
		} */
		
 	 	MessageTip checkTip=checkRequest(userName,financingBaseId,param1,param2);
		if(!checkTip.isSuccess()){  
			tip.setSuccess(false);  
			tip.setMsg("投标错误");
			result.setMessageTip(tip);
			return result;  
		}else{
			InvestCheckKey investCheckKey=investCheckKeyService.selectById(param2);
			if(null!=investCheckKey){
				tip.setSuccess(false);
				tip.setMsg("投标错误"); 
				result.setMessageTip(tip);
				return result;  
			}else{
				try {
					investCheckKeyService.insert(new InvestCheckKey(param2,financingBaseId, userName));
				} catch (Exception e) {}
			}  
		}   
		//t1 = System.currentTimeMillis();
		byte state = this.openCloseDealService.checkState();
		//t2 = System.currentTimeMillis();
		//System.out.println("是否开市="+(t2-t1)+"(毫秒)");
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3||state==4) {
			tip.setSuccess(false);
			tip.setMsg("交易系统未开市");
			result.setMessageTip(tip);
			return result;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作");
			result.setMessageTip(tip);
			return result;
		} else if (state == 6){
			tip.setSuccess(false);
			tip.setMsg("交易系统夜市关闭");
			result.setMessageTip(tip);
			return result;
		}
		if (BaseTool.isNull(userName)) {
			tip.setSuccess(false);
			tip.setMsg("用户名不能为空");
			result.setMessageTip(tip);
			return result;
		}
		
		if (BaseTool.isNull(userName)) {
			tip.setSuccess(false);
			tip.setMsg("用户名不能为空");
			result.setMessageTip(tip);
			return result;
		}
		if (money <= 0) {
			tip.setSuccess(false);
			tip.setMsg("投标金额必须大于0");
			result.setMessageTip(tip);
			return result;
		}

		if (BaseTool.isNull(financingBaseId)) {
			tip.setSuccess(false);
			tip.setMsg("融资项目不能为空");
			result.setMessageTip(tip);
			return result;
		}
		InvestVO invest = doCheck(userName, financingBaseId,userType);   
		
		
		if ("0".equals(code.toString())) { 
			tip.setMsg(message);
			tip.setSuccess(false);
			result.setMessageTip(tip);
			return result; 
		}
		//User user = userService.findUser(userName);
		User user =new User(userName);
		user.setId(out_userId.longValue());
		user.setPassword(out_userPassword);
		if(1==out_userIsEnabled.intValue()){
			user.setEnabled(true);
		}else{
			user.setEnabled(false);
		} 
		user.setUserType(out_userUserType);
		Account userAccount=new Account();
		userAccount.setId(out_userAccountId.longValue());
		userAccount.setState(out_userAccountState.intValue());
		userAccount.setBalance(out_balance.doubleValue());
		userAccount.setCredit(out_userAccountCredit.intValue());
		user.setUserAccount(userAccount); 
		
 
		password = MD5.MD5Encode(password);
		if (!password.equals(user.getPassword())) {
			tip.setSuccess(false);
			tip.setMsg("密码错误");
			result.setMessageTip(tip);
			return result;
		} else {
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				result.setMessageTip(tip);
				return result;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				result.setMessageTip(tip);
				return result;
			}
		}
		if (null == user.getUserAccount()) {
			tip.setSuccess(false);
			tip.setMsg("此用户没有帐号");
			result.setMessageTip(tip);
			return result;
		}

		if (Account.STATE_WAIT == user.getUserAccount().getState()) {
			tip.setSuccess(false);
			tip.setMsg("帐号待开通");
			result.setMessageTip(tip);
			return result;
		}

		if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
			tip.setSuccess(false);
			tip.setMsg("帐号已经注销!");
			result.setMessageTip(tip);
			return result;
		}

		if (money > user.getUserAccount().getBalance()) {
			tip.setSuccess(false);
			tip.setMsg("投标金额大于帐户余额");
			result.setMessageTip(tip);
			return result;
		}
		
		MemberBase member = this.memberBaseService.getMemByUser(user);
/* 		MemberBase member =new MemberBase(out_memberId);
		member.setState(out_memberState);
		member.setCategory(out_memberCategory);
		MemberLevel ml=new MemberLevel();
		ml.setId(out_memberLevelId);
		ml.setLevelname(out_memberLevelname);
		member.setMemberLevel(ml); 
		member.setUser(user);*/
		if (null != member && member.getState().equals(MemberBase.STATE_PASSED_AUDIT)) {// 会员审核通过   
			FinancingBase financingBase = this.financingBaseService.selectById(financingBaseId); 
			if (null==financingBase) {
				tip.setSuccess(false);
				tip.setMsg("融资项目不存在!");
				result.setMessageTip(tip);
				return result;
			}
			 
			int days = DateUtils.getBetween(financingBase.getStartDate(), new Date());

			if (financingBase.getCode().startsWith("D") && !(MemberBase.CATEGORY_ORG.equals(member.getCategory()))) {
				tip.setMsg("编号为'D'开头的融资项目只有机构投资人才能投标!");
				tip.setSuccess(false);
				result.setMessageTip(tip);
				return result;
			}

			if (days < 0) {
				tip.setSuccess(false);
				tip.setMsg("融资还没开始!");
				result.setMessageTip(tip);
				return result;
			}

			int daye = DateUtils.getBetween(financingBase.getEndDate(), new Date());

			if (daye > 0) {
				tip.setSuccess(false);
				tip.setMsg("融资已经过期!");
				result.setMessageTip(tip);
				return result;
			}
			if ("0".equals(financingBase.getState())) { 
				tip.setSuccess(false);
				tip.setMsg("暂不允许投标!");
				result.setMessageTip(tip);
				return result;
			}
			if ("1".equals(financingBase.getState())) { 
				tip.setSuccess(false);
				tip.setMsg("暂不允许投标!");
				result.setMessageTip(tip); 
				return result;
			}
			if ("1.5".equals(financingBase.getState())) { 
				tip.setSuccess(false);
				tip.setMsg("待挂单, 暂不允许投标!");
				result.setMessageTip(tip);
				return result;
			}
			
			if ("4".equals(financingBase.getState())) {
				tip.setSuccess(false);
				tip.setMsg("融资已满标 !");
				result.setMessageTip(tip);
				return result;
			}
			if ("5".equals(financingBase.getState())) {
				tip.setSuccess(false);
				tip.setMsg("融资已确认!");
				result.setMessageTip(tip);
				return result;
			}
			if ("6".equals(financingBase.getState())) {
				tip.setSuccess(false);
				tip.setMsg("融资已核算!");
				result.setMessageTip(tip);
				return result;
			}
			if ("7".equals(financingBase.getState())) {
				tip.setSuccess(false);
				tip.setMsg("融资已核算!");
				result.setMessageTip(tip);
				return result;
			}

			if ("8".equals(financingBase.getState())) {
				tip.setSuccess(false);
				tip.setMsg("融资已经撤单!");
				result.setMessageTip(tip);
				return result;
			}
			if (financingBase.getCurCanInvest()<money) {
				tip.setSuccess(false);
				tip.setMsg("该融资项目可投标额为："+financingBase.getCurCanInvest()+"元，请重新投标!");
				result.setMessageTip(tip);
				return result;
			}
			if (invest.getMinMoney() > money) {
				tip.setSuccess(false);
				tip.setMsg("投标额不能低于该融资项目最小投标额!");
				result.setMessageTip(tip);
				return result;
			}
			if (invest.getMaxMoney() < money) {
				tip.setSuccess(false);
				tip.setMsg("投标额不能超出该融资项目最大投标额!");
				result.setMessageTip(tip);
				return result;
			}
			if (financingBase.getHaveInvestNum() > 200 ) {
				tip.setSuccess(false);
				tip.setMsg("投标人数已达上限!");
				result.setMessageTip(tip);
				return result;
			}
			
			//优先控制
			if(1==financingBase.getPreInvest()){
				//1、组验证
				if(!financingRestrainService.inUsergroupCheck(userName,financingBase.getCode())){
					tip.setSuccess(false);
					tip.setMsg("不能投标!");
					result.setMessageTip(tip);
					return result; 
				}  
				 
				//2、规则验证    
				List<Map<String,Object>> list=financingRestrainService.usergrouprestrainCheck(userName,financingBase.getCode());
				if(!list.isEmpty()){ 
					  if(list.get(0).get("username") != null){
							if(Double.parseDouble(list.get(0).get("investmaxmoney").toString())<money){
								tip.setSuccess(false);
								tip.setMsg("新会员体验：不允许超过"+list.get(0).get("investmaxmoney").toString()+"元!");
								result.setMessageTip(tip);  
								return result;
							}
							if(Integer.parseInt(list.get(0).get("investcount").toString())>=Integer.parseInt(list.get(0).get("investmaxcount").toString())){
								tip.setSuccess(false);
								tip.setMsg("新会员体验：申够总次数不允许超过"+list.get(0).get("investmaxcount").toString()+"次!");
								result.setMessageTip(tip);
								return result;
							}  
					}  
			   }   
			 
			}    
			 
			if (invest.getMaxMoney() < money) {
				tip.setSuccess(false);
				tip.setMsg("投标额不能超出该融资项目最大投标额!");
				result.setMessageTip(tip);
				return result;
			}

			// 会员账户开通,且账户余额大于或等于最小融资金额+投资服务费
			String tzfwf = "";
			double p = 0.0;
			if (financingBase.getCode().startsWith("X")) {
				// if (financingBase.getFxbzState().equals("2")) {
				if (member.getMemberLevel().getLevelname().equals("VIP")) {
					tzfwf = "viptzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("高级")) {
					tzfwf = "gjtzrglf-xyd";
				} else if (member.getMemberLevel().getLevelname().equals("普通")) {
					tzfwf = "pttzrglf-xyd";
				} else {
					tzfwf = "viptzrglf-xyd";
				}

				
				// 收取利息中的百分比
				double bj_all = money;// 总本金
				double rate = financingBase.getRate();// 利率
				// 还款期限
				int term = financingBase.getBusinessType().getTerm();
				double lx_all = bj_all * ((rate / 100.00) / 12.00 * term);// 总利息
				
				//优化1
				/**
				CostBase cb = this.costCategoryService.selectByHql("from CostBase cb where cb.code='" + tzfwf + "'");
				CostItem ci = this.chargingStandardService.selectByHql("from CostItem ci where ci.costBase.id='" + cb.getId() + "'");
				p = lx_all * (ci.getPercent().doubleValue() / 100);
				**/
				p = lx_all * (this.investRecordService.getPercentByCostBaseCode(tzfwf)/ 100);
				
			} else {
				tzfwf = "tzfwf";
				//优化2
				/**
				CostBase cb = this.costCategoryService.selectByHql("from CostBase cb where cb.code='" + tzfwf + "'");
				CostItem ci = this.chargingStandardService.selectByHql("from CostItem ci where ci.costBase.id='" + cb.getId() + "'"); 
				p = money * (ci.getPercent().doubleValue() / 100);// 投资服务费
				**/
				p = money * (this.investRecordService.getPercentByCostBaseCode(tzfwf)/ 100);// 投资服务费
				
			}
			
			
			double jiandiao = money + p;// 要扣除会员的金额

			/*
			 * 可抵扣积分
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			//CreditRules creditrules = this.creditRulesService.selectByHql("from CreditRules where enable = true and  to_date('" + now + "','yyyy-MM-dd HH24:mi:ss') between effecttime and expiretime");
			CreditRules creditrules =  getCreditRulesByTime(now); 
			int credit = 0;
			int availablecredit = user.getUserAccount().getCredit();
			if (creditrules != null && availablecredit > 0) {
				if (money >= creditrules.getRelation_value()) {
					credit = (int) (Math.ceil((money * creditrules.getValue()) * 100) / 100);
					if (credit > availablecredit) {
						credit = availablecredit;
					}
				}
			}
			/*
			 * 可抵扣积分
			 */
			if (null != user.getUserAccount() && user.getUserAccount().getState() == Account.STATE_OPEN && user.getUserAccount().getBalance() >= jiandiao) {
				double b = invest.getHasMoney();
				if (invest.getMaxMoney() <= 0) {// 已经投标了融资项目的最大限额,不能再投标了
					tip.setSuccess(false);
					tip.setMsg("您已经投标了" + b + "元,已经达到此融资项目的投标上限.");
				} else {
					String[] ids = null;
					try { 
						
						if(null!=param1&&!"".equals(param1))//记录接口投标来源
						{ 
							if(param1.endsWith("_android")){
								financingBase.setFromApp("android");
							}else if(param1.endsWith("_ios")){
								/*if(financingBase.getCode().startsWith("H")){
									tip.setSuccess(false);
									tip.setMsg("项目编号为‘H’开头的项目属于高风险项目，IOS终端暂未开放投标");
									result.setMessageTip(tip);
									return result;
								}*/
								financingBase.setFromApp("ios");
							}  
						}
						/*
					    String ip=IpAddrUtil.getIpAddr(ServletActionContext.getRequest());
 			          	MessageTip stip=this.investRecordService.doInvest(financingBaseId,money,credit,user.getUsername(),ip);
						if (stip.isSuccess()){
							 ids = this.investRecordService.invest(member, money, financingBase, credit);
						}else{
							tip.setSuccess(false);
							tip.setMsg(stip.getMsg());
							result.setMessageTip(tip);
							financingCacheService.(financingBase.getId());//手机端可能产生投标记录，所有异常也要更新缓存
							return result;
						}	  
						*/ 
						MessageTip stip2=this.investRecordService.investNew3(member, money, financingBase, credit);
						if (stip2.isSuccess()){
							 ids=new String[2];
							 ids[0]=stip2.getParam1();
							 ids[1]=stip2.getParam2(); 
						}else{
							tip.setSuccess(false);
							tip.setMsg(stip2.getMsg());
							result.setMessageTip(tip);
							financingCacheService.updateOneFinancingCache(financingBase.getId());//手机端可能产生投标记录，所有异常也要更新缓存
							return result;
						}
					} catch (Exception e) { 
						e.printStackTrace();
						tip.setSuccess(false);
						tip.setMsg("多人投标中,系统繁忙,本次投标失败。");
						result.setMessageTip(tip);
						financingCacheService.updateOneFinancingCache(financingBase.getId());//手机端可能产生投标记录，所有异常也要更新缓存
						return result;
					}
 				   if (null != ids&&ids.length>0) { 
						 //if(!"android".equals(financingBase.getFromApp())&&!"ios".equals(financingBase.getFromApp())){ 
								result.setInvestRecordCostVo(new InvestRecordCostVo());  
                         /*}else{
								InvestRecordCost cost = this.investRecordCostService.selectById(ids[1]);
								// 给WebService对象赋值
								InvestRecordCostVo vo = new InvestRecordCostVo(cost.getInvestRecord().getId(), cost.getTzfwf(), cost.getSxf(), cost.getInvestRecord().getContract(), cost.getRealAmount());
								vo.setInvestRecordId(cost.getInvestRecord().getId());
								result.setInvestRecordCostVo(vo);  				 
						 }	 
						 */  
						tip.setSuccess(true);             
					} else {
						tip.setSuccess(false);
						tip.setMsg("多人投标中,系统繁忙,本次投标失败。");       
						result.setMessageTip(tip);
						financingCacheService.updateOneFinancingCache(financingBase.getId());//手机端可能产生投标记录，所有异常也要更新缓存
						return result;
					} 
				}
				
				try {
					financingCacheService.updateOneFinancingCache(financingBase.getId());
				} catch (Exception e) { 
					//e.printStackTrace();
				} 
				
				result.setMessageTip(tip);
				return result;
			} else {
				tip.setSuccess(false);
				tip.setMsg("您的账户余额不足,不能进行本次投标操作!");
				result.setMessageTip(tip);
				return result;
			}
		} else {
			tip.setSuccess(false);
			tip.setMsg("您还未通过审核,请联系系统客服!");
			result.setMessageTip(tip);
			return result;
		}
	}
	
	


	
	/**
	 * 合同签约确认
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param heTongId
	 *            合同ID
	 */
	public MessageTip okHeTong(String userName, String userType, String heTongId) {

		MessageTip tip = new MessageTip(true, "签约成功");
		if (BaseTool.isNull(userName)) {
			tip.setSuccess(false);
			tip.setMsg("用户名不能为空");
			return tip;
		}
		User user = userService.findUser(userName);
		if (null == user) {
			tip.setSuccess(false);
			tip.setMsg("用户名不存在");
			return tip;
		} else {
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}
		}

		if (BaseTool.isNull(heTongId)) {
			tip.setSuccess(false);
			tip.setMsg("合同不能为空");
			return tip;
		}

		try {
			ContractKeyData contract = contractKeyDataService.selectById(heTongId);

			if (!userName.equals(contract.getFirst_party_code())) {
				tip.setSuccess(false);
				tip.setMsg("合同与用户不匹配");
				return tip;
			}

			contract.setInvestor_make_sure(new Date());
			contractKeyDataService.update(contract);
			return tip;
		} catch (EngineException e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("签约异常");
			return tip;
		}

	}
 
	/**
	 * 债权行情 
	 * @param times  更新时间点
	 * 1、investRecordId不为空查询某个债权
	 * 2、investRecordId为空，userName不为空,times为空,查询某个用户的可以转让的所有的债权;
	 * 3、investRecordId为空，userName不为空,times不为空,查询某个用户的某个时间点以后的可以转让的债权;
	 * 4、investRecordId为空，userName为空,times为空,查询所有的可以转让的债权;
	 * 5、investRecordId为空，userName为空,times不为空,查询某个时间点以后可以转让的债权;
	 * times格式:yyyyMMddHHmmss 根据时间戳查找我的投标记录
	 */
	public ZhaiQuanRecordResult currentZqs(String investRecordId,String userName,String times)  {
		ZhaiQuanRecordResult result = new ZhaiQuanRecordResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		String sql = "select * from V_ZQ_LIST o  where 1=1  "; 
		if (!BaseTool.isNull(investRecordId)) { 
			sql +=" and o.investrecordid='" + investRecordId + "' ";
		} else {  
			if (!BaseTool.isNull(userName)) { 
				User user = userService.findUser(userName);
				if (null == user) {
					tip.setSuccess(false);
					tip.setMsg("用户名不存在");
					result.setMessageTip(tip);
					return result;
				}else{
					sql +=" and  ( o.investorId='" + memberBaseService.getMemByUser(user).getId() + "' or o.sellingState='1' ) ";//有用户名时，得到别人出让中和自己的   
				} 
			}  
			
			List<ZQBuySellRule> rules=zhaiquanRuleService.getCommonListData("from ZQBuySellRule o where o.enable=true");
			StringBuilder releSql = new StringBuilder();
  
	 	    int i=1;
			for(ZQBuySellRule rule:rules){// 符合转让天数、逾期天数 的
				if(1==rules.size()){   
					 releSql.append("and  ( o.term='"+rule.getTerm()+"' and  trunc(SYSDATE)-trunc(o.zqSuccessDate)>"+rule.getDays()+"    and  trunc(SYSDATE)+"+rule.getOverdue()+"<trunc(o.xyhkr)  )");
    			}else{
					if(1==i){
						 releSql.append(" and ( ");   
						 releSql.append("( o.term='"+rule.getTerm()+"' and  trunc(SYSDATE)-trunc(o.zqSuccessDate)>"+rule.getDays()+"    and   trunc(SYSDATE)+"+rule.getOverdue()+"<trunc(o.xyhkr)   )");

					}else if(rules.size()==i){
						  releSql.append(" or ( o.term='"+rule.getTerm()+"' and  trunc(SYSDATE)-trunc(o.zqSuccessDate)>"+rule.getDays()+"    and   trunc(SYSDATE)+"+rule.getOverdue()+"<trunc(o.xyhkr)   )");
						  releSql.append("    )");  
					}else{
	 					  releSql.append(" or ( o.term='"+rule.getTerm()+"' and  trunc(SYSDATE)-trunc(o.zqSuccessDate)>"+rule.getDays()+"    and   trunc(SYSDATE)+"+rule.getOverdue()+"<trunc(o.xyhkr)   )");  
					}
					
				}
				
			    i++;
			}  
			
			sql +=releSql.toString(); 
			
			
			if (null != times && !"".equals(times.trim())) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss"); 
				try {
					Date from= format1.parse(times); 
					if (null != from) {
						String t = format.format(from);
						sql +=" and  o.modifyDate >= to_date('" + t + "','yyyy-MM-dd HH24:mi:ss')  ";
					}
				} catch (ParseException e) { 
					e.printStackTrace(); 
					tip.setSuccess(false);
					return result;
				}
			} 	
			
		}   
		
		

		sql += "  order by o.modifyDate desc   "; 
	 
		try {
			ArrayList<LinkedHashMap<String, Object>> dataList = this.investRecordService.selectListWithJDBC(sql); 
			List<ZhaiQuanRecordVo> tempfbs=swithZqList(dataList); 
			result.setMessageTip(tip);
			result.setRecords(tempfbs); 
			return result;   
		}catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			return result;
		}
	}
  

	/**
	 * 债权出让下单验证
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param money
	 *            出让价格
	 * @param investRecordId
	 *            投标记录ID
	 */
	public MessageTip sellZqUI(String userName, String userType, double money, String investRecordId) {
		MessageTip tip = new MessageTip(true, "验证成功");
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
		CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
		tip.setParam1("0");
		tip.setParam2("0");
		tip.setParam3("" + sxf.getPercent());
		tip.setParam4("" + sf.getPercent());
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(investRecordId)) {
				tip.setSuccess(false);
				tip.setMsg("出让ID不能为空");
				return tip;
			}
			if (money <= 0d) {
				tip.setSuccess(false);
				tip.setMsg("卖出价格必需大于为0元");
				return tip;
			}

			InvestRecord investRecord = this.investRecordService.selectById(investRecordId);
			if (null == investRecord) {
				tip.setSuccess(false);
				tip.setMsg("此债权不存在");
				return tip;
			}
			if (investRecord.getHaveNum() > 200) {
				tip.setSuccess(false);
				tip.setMsg("此债权转让次数已超最大值");
				return tip;
			}

			if ("2".equals(investRecord.getZqzrState()) || "4".equals(investRecord.getZqzrState())) {
				tip.setSuccess(false);
				tip.setMsg("此债权已经停牌或摘牌,不允许转让");
				return tip;
			}

			if (investRecord.getInvestor().getUser().getId() != user.getId()) {
				tip.setSuccess(false);
				tip.setMsg("已经成功!不允许下单"); // 不能操作别人的债权
				return tip;
			}
			if (this.sellingService.isAlreadySold(user, investRecord)) {
				tip.setSuccess(false);
				tip.setMsg("此债权您已经下单,不允许重复下单");
				return tip;
			}

			// 动态规则调用
			CommonVo vo = zhaiquanRuleService.zhaiQuanCheck(investRecord.getFinancingBase().getId(), investRecordId);
			if ("0".equals(vo.getString1())) {
				tip.setSuccess(false);
				tip.setMsg(vo.getString2());
				return tip;
			}

			/*
			 * if
			 * ("X".equals(investRecord.getFinancingBase().getCode().substring(
			 * 0, 1))) { tip.setSuccess(false);
			 * tip.setMsg("债权代码以'X'开头的暂不允许转让！"); return tip; }
			 */
			return tip;
		} catch (Exception e) {
			tip.setSuccess(false);
			tip.setMsg("操作异常");
		}
		return tip;
	}
    
	/***
	 * 停止债权业务
	 * @return
	 */
	private MessageTip stopZq(){
		MessageTip tip=new MessageTip(true, "债权转让业务测试已暂停！");
		return tip;
	}
	
	/**
	 * 债权出让下单
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param money
	 *            出让价格
	 * @param investRecordId
	 *            投标记录ID
	 */
	public ZqOrderReSult sellZq(String userName, String password, String userType, double money, String investRecordId) {
		
		MessageTip tip0 =stopZq();
		ZqOrderReSult zo = new ZqOrderReSult();
		if(tip0.isSuccess()){
			tip0.setSuccess(false); 
			zo.setMessageTip(tip0);
			return zo;
		}
		
		MessageTip tip = this.sellZqUI(userName, userType, money, investRecordId);
	
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			zo.setMessageTip(tip);
			return zo;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			zo.setMessageTip(tip);
			return zo;
		}
		if (!tip.isSuccess()) {
			zo.setMessageTip(tip);
			return zo;
		}
		if (BaseTool.isNull(password)) {
			tip.setSuccess(false);
			tip.setMsg("密码不能为空");
			zo.setMessageTip(tip);
			return zo;
		}

		User user = userService.findUser(userName);
		password = MD5.MD5Encode(password);
		if (!password.equals(user.getPassword())) {
			tip.setSuccess(false);
			tip.setMsg("密码不正确");
			zo.setMessageTip(tip);
			return zo;
		}
		try {
			tip.setMsg("卖出成功");
			CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
			CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
			InvestRecord ir = investRecordService.selectById(investRecordId);
			double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100) * money, 2);
			double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100) * money, 2);
			// tip.setParam1(String.valueOf(fee));
			// tip.setParam2(String.valueOf(taxes));

			Selling selling = new Selling();
			selling.setSeller(user);
			selling.setSellingPrice(money);
			selling.setZqfwf(fee);
			selling.setZqsf(taxes);
			double realAmount = money - fee - taxes;
			selling.setRealAmount(realAmount);
			selling.setCreateDate(new Date());
			selling.setInvestRecord(ir);
			selling.setContract_numbers(ir.getContract().getContract_numbers());
			selling.setZhaiQuanCode(ir.getZhaiQuanCode());

			sellingService.saveSelling(selling);

			ZqOrderVo zv = new ZqOrderVo();
			zv.setContract_numbers(selling.getContract_numbers());
			zv.setId(selling.getId());
			zv.setFee1(selling.getZqfwf());
			zv.setFee2(selling.getZqsf());
			zv.setSellPrice(selling.getSellingPrice());
			zv.setInvestRecordId(selling.getInvestRecord().getId());
			zv.setState(selling.getState());
			zv.setZhaiQuanCode(selling.getZhaiQuanCode());
			if (null != selling.getContract()) {
				zv.setContractId(selling.getContract().getId());
				zv.setPrice(selling.getContract().getPrice());
				zv.setBuyPrice(selling.getContract().getBuying().getBuyingPrice());
			}
			zv.setCreateDate(selling.getCreateDate());
			zv.setType("sell");
			zo.setOrderVo(zv);
			tip.setMsg(zv.getCnState());
			zo.setMessageTip(tip);
		} catch (Exception e) {
			tip.setSuccess(false);
			tip.setMsg("由于网络繁忙，稍后请重试");
			zo.setMessageTip(tip);
		}
		return zo;
	}

	/**
	 * 出让债权撤单
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param sellId
	 *            出让记录ID
	 */
	public MessageTip sellCancel(String userName, String userType, String sellId) {
		MessageTip tip = new MessageTip(true, "撤单成功");
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}

			if (BaseTool.isNull(sellId)) {
				tip.setSuccess(false);
				tip.setMsg("出让ID不能为空");
				return tip;
			}
			Selling selling = sellingService.selectById(sellId);
			if (null == selling) {
				tip.setSuccess(false);
				tip.setMsg("出让记录不存在");
				return tip;
			}

			if (selling.getSeller().getId() != user.getId()) {
				tip.setSuccess(false);
				tip.setMsg("非法操作"); // 不能操作别人的记录
				return tip;
			}
			sellingService.cancel(sellId);
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
		}
		return tip;
	}

	/**
	 * 我的出让记录
	 * 
	 * @param userName
	 *            用户名
	 * @param state
	 *            出让中:"0";成功 :"1";撤单:"3";系统撤单:"4";
	 * @param sellingId
	 *            出让记录id;
	 */
	public SellingResult mySells(String userName, String state, String sellingId) {
		SellingResult result = new SellingResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		if (BaseTool.isNull(userName)) {
			tip.setSuccess(false);
			tip.setMsg("用户名不能为空");
			result.setMessageTip(tip);
			return result;
		}
		User user = userService.findUser(userName);
		if (null == user) {
			tip.setSuccess(false);
			tip.setMsg("用户名不存在");
			result.setMessageTip(tip);
			return result;
		} 
		StringBuilder sb = new StringBuilder(" t.contract_id=zc.id  and ir.id=t.investrecord_id  and su.id=t.seller_id and zc.selling_id=t.id ");

		// 只显示我的出让记录
		sb.append(" and ");
		sb.append(" su.id ='" + user.getId() + "'");

		if (BaseTool.isNotNull(state)) {
			sb.append(" and ");
			sb.append(" t.state ='" + state + "'");
		}
		if (BaseTool.isNotNull(sellingId)) {
			sb.append(" and ");
			sb.append(" t.id ='" + sellingId + "'");
		}

		sb.append(" order by t.createDate desc " ); 
		
	    String  fields=" t.sellingprice,t.contract_numbers,t.id,t.createdate,t.investrecord_id,t.realamount,t.state,t.zhaiquancode,t.zqfwf,t.zqsf,zc.id as contractId,zc.price";
		String  tables=" Zq_Selling t,sys_user su,t_invest_record ir,ZQ_CONTRACT zc";
	    try {
			SimpleDateFormat format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String,Object>> list  = buyingService.queryForList(fields,tables, sb.toString()); 
			List<SellingVo> tempfbs = new ArrayList<SellingVo>();
	        for(Map<String,Object>  map:list)
				if(map.get("id") != null){   
					SellingVo vo = new SellingVo();
					vo.setContract_numbers(map.get("contract_numbers").toString());
					vo.setId(map.get("id").toString());
					vo.setCreateDate(format_time.parse(map.get("createdate").toString()));
					vo.setInvestRecordId(map.get("investrecord_id").toString());
					vo.setSellingPrice(Double.parseDouble(map.get("sellingprice").toString()));
					vo.setRealAmount(Double.parseDouble(map.get("realamount").toString()));
					vo.setState(map.get("state").toString());
					vo.setZhaiQuanCode(map.get("zhaiquancode").toString());
					vo.setZqfwf(Double.parseDouble(map.get("zqfwf").toString()));
					vo.setZqsf(Double.parseDouble(map.get("zqsf").toString())); 
					vo.setContractId(map.get("contractId").toString()); 
					if (null != map.get("price") &&!"".equals(map.get("price"))) {
						vo.setContractPrice(Double.parseDouble(map.get("price").toString()));
					}else{
						vo.setContractPrice(0d); 
					} 
					tempfbs.add(vo);
					
				}
	        result.setMessageTip(tip);
			result.setSellings(tempfbs);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}
	}


	/**
	 * 债权受让下单前验证
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param money
	 *            受让价格
	 * @param investRecordId
	 *            投标记录ID
	 */
	public MessageTip buyZqUI(String userName, String userType, double money, String investRecordId) {
		MessageTip tip = new MessageTip(true, "验证成功");
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		CostItem sxf = chargingStandardService.findCostItem("zqfwf1", "T");// 债权转让手续费
		CostItem sf = chargingStandardService.findCostItem("zqsf1", "T");// 税费
		tip.setParam1("0");
		tip.setParam2("0");
		tip.setParam3("" + sxf.getPercent());
		tip.setParam4("" + sf.getPercent());
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}
			double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100) * money, 2);
			double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100) * money, 2);

			if ((fee + taxes + money) > user.getUserAccount().getBalance()) {
				tip.setSuccess(false);
				tip.setMsg("买入价格(" + money + ")加平台费用(" + (fee + taxes) + ")超出了您的可用资金");
				return tip;
			}

			// 只能用绑定金额以外的钱来买债权
			HashMap<String, Object> maped = this.accountService.activityWithdrawAllow(user, fee + taxes + money);
			if (!Boolean.parseBoolean(maped.get("boolean").toString())) {
				tip.setSuccess(false);
				tip.setMsg("你已参与\"耀5扬微\"活动，限制金额为" + maped.get("frozen") + "，当前可使用的金额为" + maped.get("surplus") + "，您买入此债权价格(" + money + ")加平台费用(" + (fee + taxes) + ")超出了当前可使用的金额。");
				return tip;
			}

			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}

			if (money <= 0d) {
				tip.setSuccess(false);
				tip.setMsg("买入价格必需大于0元");
				return tip;
			}

			if (BaseTool.isNull(investRecordId)) {
				tip.setSuccess(false);
				tip.setMsg("债权ID不能为空");
				return tip;
			}
			InvestRecord investRecord = this.investRecordService.selectById(investRecordId);
			if (null == investRecord) {
				tip.setSuccess(false);
				tip.setMsg("此债权不存在");
				return tip;
			}

			if (investRecord.getHaveNum() > 200) {
				tip.setSuccess(false);
				tip.setMsg("此债权转让次数已超最大值");
				return tip;
			}

			if ("2".equals(investRecord.getZqzrState()) || "4".equals(investRecord.getZqzrState())) {
				tip.setSuccess(false);
				tip.setMsg("此债权转让已经停牌或摘牌");
				return tip;
			}
			if (0 == investRecord.getBxhj()) {// 还款结束本息合计为0
				tip.setSuccess(false);
				tip.setMsg("此债权转让已经停牌或摘牌");
				return tip;
			}
			if (investRecord.getCrpice() == 0 && "3".equals(investRecord.getSellingState())) {
				tip.setSuccess(false);
				tip.setMsg("此债权已经转让成功");
				return tip;
			}
			if (investRecord.getCrpice() == 0 && "0".equals(investRecord.getSellingState())) {
				tip.setSuccess(false);
				tip.setMsg("此债权已经撤单");
				return tip;
			}

			if (investRecord.getInvestor().getUser().getId() == user.getId()) {
				tip.setSuccess(false);
				tip.setMsg("不允许受让自己的债权"); // 自己的东西不能买
				return tip;
			}

			if (this.buyingService.isAlreadyBought(user, investRecord)) {
				tip.setSuccess(false);
				tip.setMsg("此债权您已经下单,不允许重复下单");
				return tip;
			}
			// 动态规则调用
			CommonVo vo = zhaiquanRuleService.zhaiQuanCheck(investRecord.getFinancingBase().getId(), investRecordId);
			if ("0".equals(vo.getString1())) {
				tip.setSuccess(false);
				tip.setMsg(vo.getString2());
				return tip;
			}
			Selling selling = sellingService.getSellingByUnit(investRecordId, money);
			if (null == selling)// 有符合条件的
			{
				tip.setSuccess(false);
				tip.setMsg("您的报价低于出让价格");
				return tip;
			}

		} catch (Exception e) {
			tip.setSuccess(false);
			tip.setMsg("操作异常");
		}
		return tip;
	}

	/**
	 * 债权受让下单
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param money
	 *            受让价格
	 * @param investRecordId
	 *            投标记录ID
	 */
	public ZqOrderReSult buyZq(String userName, String password, String userType, double money, String investRecordId) {
		MessageTip tip0 =stopZq();
		ZqOrderReSult zo = new ZqOrderReSult();
		if(tip0.isSuccess()){
			tip0.setSuccess(false); 
			zo.setMessageTip(tip0);
			return zo;
		}
		
		MessageTip tip = this.buyZqUI(userName, userType, money, investRecordId); 
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			zo.setMessageTip(tip);
			return zo;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			zo.setMessageTip(tip);
			return zo;
		}
		try {
			if (!tip.isSuccess()) {
				zo.setMessageTip(tip);
				return zo;
			}
			if (BaseTool.isNull(password)) {
				tip.setSuccess(false);
				zo.setMessageTip(tip);
				return zo;
			}

			User user = userService.findUser(userName);
			password = MD5.MD5Encode(password);
			if (!password.equals(user.getPassword())) {
				tip.setSuccess(false);
				tip.setMsg("密码不正确");
				zo.setMessageTip(tip);
				return zo;
			}

			tip.setMsg("买入成功");
			CostItem sxf = chargingStandardService.findCostItem("zqfwf1", "T");// 债权转让手续费
			CostItem sf = chargingStandardService.findCostItem("zqsf1", "T");// 税费
			InvestRecord ir = investRecordService.selectById(investRecordId);
			double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100) * money, 2);
			double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100) * money, 2);
			// tip.setParam1(String.valueOf(fee));
			// tip.setParam2(String.valueOf(taxes));
			Buying buying = new Buying();
			buying.setBuyer(user);
			buying.setBuyingPrice(money);
			buying.setZqfwf(fee);
			buying.setZqsf(taxes);
			double realAmount = money + fee + taxes;
			buying.setRealAmount(realAmount);
			buying.setCreateDate(new Date());

			buying.setInvestRecord(ir);
			buying.setContract_numbers(ir.getContract().getContract_numbers());
			buying.setZhaiQuanCode(ir.getZhaiQuanCode());
			buyingService.saveBuying(buying);

			ZqOrderVo zv = new ZqOrderVo();
			zv.setContract_numbers(buying.getContract_numbers());
			zv.setId(buying.getId());
			zv.setFee1(buying.getZqfwf());
			zv.setFee2(buying.getZqsf());
			zv.setSellPrice(buying.getContract().getSelling().getSellingPrice());
			zv.setInvestRecordId(buying.getInvestRecord().getId());
			zv.setState(buying.getState());
			zv.setZhaiQuanCode(buying.getZhaiQuanCode());
			zv.setCreateDate(buying.getCreateDate());
			if (null != buying.getContract()) {
				zv.setContractId(buying.getContract().getId());
				zv.setPrice(buying.getContract().getPrice());
				zv.setBuyPrice(buying.getContract().getPrice());
			}
			zv.setType("buy");
			zo.setOrderVo(zv);
			tip.setMsg(zv.getCnState());
			zo.setMessageTip(tip);
		} catch (Exception e) {
			tip.setSuccess(false);
			tip.setMsg("由于网络繁忙，稍后请重试");
			zo.setMessageTip(tip);
		}
		return zo;
	}

	/**
	 * 受让债权撤单
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param userType
	 *            用户类型
	 * @param buyId
	 *            受让记录ID
	 */
	public MessageTip buyCancel(String userName, String userType, String buyId) {
		MessageTip tip = new MessageTip(true, "撤单成功");
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}

			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}

			if (BaseTool.isNull(buyId)) {
				tip.setSuccess(false);
				tip.setMsg("受让ID不能为空");
				return tip;
			}
			Buying buying = buyingService.selectById(buyId);
			if (null == buying) {
				tip.setSuccess(false);
				tip.setMsg("受让记录不存在");
				return tip;
			}

			if (buying.getBuyer().getId() != user.getId()) {
				tip.setSuccess(false);
				tip.setMsg("非法操作"); // 不能操作别人的记录
				return tip;
			}
			buyingService.cancel(buyId);
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
		}
		return tip;
	}

	/**
	 * 我的受让记录
	 * 
	 * @param userName
	 *            用户名
	 * @param state
	 *            出让中:"0";成功 :"1";撤单:"3";系统撤单:"4";
	 * @param buyingId
	 *            受让记录id;
	 */
	public BuyingResult myBuys(String userName, String state, String buyingId) {
		BuyingResult result = new BuyingResult();
		MessageTip tip = new MessageTip(true, "查询成功");
		if (BaseTool.isNull(userName)) {
			tip.setSuccess(false);
			tip.setMsg("用户名不能为空");
			result.setMessageTip(tip);
			return result;
		}
		User user = userService.findUser(userName);
		if (null == user) {
			tip.setSuccess(false);
			tip.setMsg("用户名不存在");
			result.setMessageTip(tip);
			return result;
		} 
		StringBuilder sb = new StringBuilder("  t.contract_id=zc.id  and ir.id=t.investrecord_id  and su.id=t.buyer_id  ");

		// 只显示我的出让记录
		sb.append(" and ");
		sb.append(" su.id ='" + user.getId() + "'");

		if (BaseTool.isNotNull(state)) {
			sb.append(" and ");
			sb.append(" t.state ='" + state + "'");
		}

		if (BaseTool.isNotNull(buyingId)) {
			sb.append(" and ");
			sb.append(" t.id ='" + buyingId + "'");
		}
		sb.append(" order by t.createDate desc " );
	 
		try {
			SimpleDateFormat format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String,Object>> list  = buyingService.queryForList("t.buyingprice,t.contract_numbers,t.id,t.createdate,t.investrecord_id,t.realamount,t.state,t.zhaiquancode,t.zqfwf,t.zqsf,zs.sellingprice,zc.id as contractId ", "ZQ_BUYING t,sys_user su,t_invest_record ir,ZQ_CONTRACT zc left join zq_Selling  zs on zs.id=zc.buying_id", sb.toString());//getScrollData(sb.toString(), null, orderby).getResultlist();
			List<BuyingVo> tempfbs = new ArrayList<BuyingVo>();
 
			for(Map<String,Object>  map:list)
				if(map.get("id") != null){ 
					BuyingVo vo = new BuyingVo();
					vo.setBuyingPrice(Double.parseDouble(map.get("buyingprice").toString()));
					vo.setContract_numbers(map.get("contract_numbers").toString());
					vo.setId(map.get("id").toString());
					vo.setCreateDate(format_time.parse(map.get("createdate").toString()));
					vo.setInvestRecordId(map.get("investrecord_id").toString());
					vo.setRealAmount(Double.parseDouble(map.get("realamount").toString()));
					vo.setState(map.get("state").toString());
					vo.setZhaiQuanCode(map.get("zhaiquancode").toString());
					vo.setZqfwf(Double.parseDouble(map.get("zqfwf").toString()));
					vo.setZqsf(Double.parseDouble(map.get("zqsf").toString())); 
					vo.setContractId(map.get("contractId").toString());
					if (null != map.get("sellingprice") &&!"".equals(map.get("sellingprice"))) {
						vo.setSellPrice(Double.parseDouble(map.get("sellingprice").toString()));
					}else{
						vo.setSellPrice(0d); 
					} 
					tempfbs.add(vo);
			 }
			 
			result.setMessageTip(tip);
			result.setBuyings(tempfbs);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("查询异常");
			result.setMessageTip(tip);
			return result;
		}
	}

	/**
	 * 债权下单前验证
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param money
	 *            受让价格
	 * @param investRecordId
	 *            投标记录ID
	 * @param operType
	 *            "sell":出让;"buy":受让
	 */
	public MessageTip validateZqUI(String userName, String userType, double money, String investRecordId, String operType) {
		if ("sell".equals(operType)) {
			return this.sellZqUI(userName, userType, money, investRecordId);
		} else if ("buy".equals(operType)) {
			return this.buyZqUI(userName, userType, money, investRecordId);
		} else {
			return new MessageTip(false, "非法参数");
		}

	}

	/**
	 * 债权下单
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @param money
	 *            受让价格
	 * @param investRecordId
	 *            投标记录ID
	 * @param operType
	 *            "sell":出让;"buy":受让
	 */
	public ZqOrderReSult zqOrder(String userName, String password, String userType, double money, String investRecordId, String operType) {
		if ("sell".equals(operType)) {
			return this.sellZq(userName, password, userType, money, investRecordId);
		} else if ("buy".equals(operType)) {
			return this.buyZq(userName, password, userType, money, investRecordId);
		} else {
			ZqOrderReSult zo = new ZqOrderReSult();
			zo.setMessageTip(new MessageTip(false, "非法下单"));
			return zo;
		}

	}

	/**
	 * 受让债权撤单
	 * 
	 * @param userName
	 *            用户名
	 * @param userType
	 *            用户类型
	 * @operType "sell":出让;"buy":受让
	 * @param Id
	 *            记录ID(sell时传入出让ID,buy传入时受让ID)
	 */
	public MessageTip zqCancel(String userName, String userType, String id, String operType) {
		if ("sell".equals(operType)) {
			return this.sellCancel(userName, userType, id);
		} else if ("buy".equals(operType)) {
			return this.buyCancel(userName, userType, id);
		} else {
			return new MessageTip(false, "非法下单");
		}
	}

	/**
	 * 辅助融资项目赋值转换
	 */
	private FinancingBaseVo zhuanhuanFinancingBase(InvestRecord f) {
		FinancingBaseVo financingBaseVo = new FinancingBaseVo();
		financingBaseVo.setCode(f.getFinancingBase().getCode());
		financingBaseVo.setId(f.getFinancingBase().getId());
		financingBaseVo.setShortName(f.getFinancingBase().getShortName());
		financingBaseVo.setRate(f.getFinancingBase().getRate());
		financingBaseVo.setAddress(f.getFinancingBase().getFinancier().getProvinceName() + f.getFinancingBase().getFinancier().getCityName());
		financingBaseVo.setCompanyProperty(f.getFinancingBase().getFinancier().getCompanyProperty().getName());
		financingBaseVo.setStartDate(f.getFinancingBase().getStartDate());
		financingBaseVo.setEndDate(f.getFinancingBase().getEndDate());

		if ("5".equals(f.getFinancingBase().getState()) || "6".equals(f.getFinancingBase().getState()) || "7".equals(f.getFinancingBase().getState())) {
			financingBaseVo.setProGress("100%");
		} else {
			financingBaseVo.setProGress((DoubleUtils.doubleCheck((f.getFinancingBase().getCurrenyAmount() / f.getFinancingBase().getMaxAmount()) * 100, 2)) + "%");

		}

		financingBaseVo.setFinancierCode(f.getFinancingBase().getFinancier().getUser().getUsername());
		financingBaseVo.setFinancierId(f.getFinancingBase().getFinancier().getId());
		financingBaseVo.setYongtu(f.getFinancingBase().getYongtu());

		if ("1".equals(f.getFinancingBase().getFinancier().getCategory())) {
			financingBaseVo.setFinancierName(f.getFinancingBase().getFinancier().getpName());
		} else {
			financingBaseVo.setFinancierName(f.getFinancingBase().getFinancier().geteName());
		}

		financingBaseVo.setFxbzState(f.getFinancingBase().getFxbzState());

		if ("15".equals(f.getFinancingBase().getFxbzState())) {
			financingBaseVo.setFxbzStateName("无担保");
		}
		if ("10".equals(f.getFinancingBase().getFxbzState())) {
			financingBaseVo.setFxbzStateName("本息担保");
		}
		if ("12".equals(f.getFinancingBase().getFxbzState())) {
			financingBaseVo.setFxbzStateName("本金担保");
		}
		
		// 担保代偿的
		/*if ("10".equals(f.getFinancingBase().getFxbzState()) || "12".equals(f.getFinancingBase().getFxbzState())))
		{
		if (null!=f.getFinancingBase().getGuarantee()) {// 担保代偿的
			financingBaseVo.setGuaranteeCode(f.getFinancingBase().getGuarantee().getUser().getUsername());
			financingBaseVo.setGuaranteeId(f.getFinancingBase().getGuarantee().getId());
			if ("1".equals(f.getFinancingBase().getGuarantee().getCategory())) {
				financingBaseVo.setGuaranteeName(f.getFinancingBase().getGuarantee().getpName());
			} else {
				financingBaseVo.setGuaranteeName(f.getFinancingBase().getGuarantee().geteName());
			}

			if (null != f.getFinancingBase().getGuarantee().getLogoName() && !"".equals(f.getFinancingBase().getGuarantee().getLogoName())) {
				financingBaseVo.setLogoName(f.getFinancingBase().getGuarantee().getLogoName());
			} else {
				financingBaseVo.setLogoName("-");
			}
		}*/
		financingBaseVo.setIndustry(f.getFinancingBase().getHyTypeShow());
		financingBaseVo.setTerm(f.getFinancingBase().getBusinessType().getTerm());
		financingBaseVo.setReturnPattern(f.getFinancingBase().getBusinessType().getReturnPattern());
		financingBaseVo.setMaxAmount(f.getFinancingBase().getMaxAmount());

		// financingBaseVo.setNote(f.getFinancingBase().getNote());
		financingBaseVo.setGuaranteeNote(f.getFinancingBase().getGuaranteeNote());
		financingBaseVo.setState(f.getFinancingBase().getState());

		financingBaseVo.setStateName(f.getFinancingBase().getStateName());// 状态显示名称

		// financingBaseVo.setPurpose(f.getFinancingBase().getPurpose());
		financingBaseVo.setQyzs(f.getFinancingBase().getQyzs());
		financingBaseVo.setQyzsNote(f.getFinancingBase().getQyzsNote());
		financingBaseVo.setFddbzs(f.getFinancingBase().getFddbzs());
		financingBaseVo.setFddbzsNote(f.getFinancingBase().getFddbzsNote());
		financingBaseVo.setCzzs(f.getFinancingBase().getCzzs());
		financingBaseVo.setCzzsNote(f.getFinancingBase().getCzzsNote());
		financingBaseVo.setDbzs(f.getFinancingBase().getDbzs());
		financingBaseVo.setDbzsNote(f.getFinancingBase().getDbzsNote());
		financingBaseVo.setZhzs(f.getFinancingBase().getZhzs());
		financingBaseVo.setZhzsNote(f.getFinancingBase().getZhzsNote());
		financingBaseVo.setHaveInvestNum(f.getFinancingBase().getHaveInvestNum());
		financingBaseVo.setCurCanInvest(f.getFinancingBase().getCurCanInvest());
		financingBaseVo.setCurrenyAmount(f.getFinancingBase().getCurrenyAmount());

		if (f.getFinancingBase().isTerminal()) {
			financingBaseVo.setStateName("还款结束");
		}

		return financingBaseVo;
	}

	/**
	 * 辅助检查投资人约束
	 */
	@SuppressWarnings("unchecked")
	private InvestVO doCheck(String in_userName, String in_financingBaseId,String in_usertype) {

		InvestVO invest = new InvestVO(); 
		Map st=this.investRecordService.callProcedureForDocheck(in_userName, in_financingBaseId, in_usertype); 
		BigDecimal out_frozenamount=new BigDecimal(0);
		BigDecimal out_MAXAMOUNT=new BigDecimal(0);
		BigDecimal out_HIGHPERCENT=new BigDecimal(0);
		BigDecimal out_LOWESTMONEY=new BigDecimal(0);
		BigDecimal out_HaveInvestNum=new BigDecimal(0);
		BigDecimal out_BJYE=new BigDecimal(0);
		BigDecimal out_INVESTAMOUNT=new BigDecimal(0);
		//BigDecimal out_MINSTART=new BigDecimal(0);
		out_balance = (BigDecimal) st.get("3");
		out_frozenamount = (BigDecimal) st.get("4");
		out_CURCANINVEST = (BigDecimal) st.get("5");
		out_CURRENYAMOUNT = (BigDecimal) st.get("6");
		out_MAXAMOUNT = (BigDecimal) st.get("7");
		out_HIGHPERCENT = (BigDecimal) st.get("8");
		out_LOWESTMONEY = (BigDecimal) st.get("9");
		out_HaveInvestNum = (BigDecimal) st.get("10");
		out_BJYE = (BigDecimal) st.get("11");
		out_INVESTAMOUNT = (BigDecimal) st.get("12");
		out_startdate = (Date) st.get("13");
		out_enddate = (Date) st.get("14");
		out_state = (String) st.get("15");
		code = (BigDecimal) st.get("17");
		message = (String) st.get("18");
		
		out_fbCode= (String) st.get("20"); 
		out_fbPreInvest= (BigDecimal) st.get("21");
		out_fbRate= (BigDecimal) st.get("22"); 
		out_fbInterestDay= (BigDecimal) st.get("23");
		
		
		
		out_memberId= (String) st.get("24"); 
		out_memberState= (String) st.get("25"); 
		out_memberCategory= (String) st.get("26"); 
		out_memberLevelname= (String) st.get("27"); 
		out_memberLevelId= (String) st.get("28"); 
		
		out_userPassword= (String) st.get("29"); 
		out_userIsEnabled= (BigDecimal) st.get("30");
		out_userUserType= (String) st.get("31"); 
		out_userAccountState= (BigDecimal) st.get("32");
		out_userAccountId= (BigDecimal) st.get("33"); 
		out_userId= (BigDecimal) st.get("34"); 
		out_userAccountCredit= (BigDecimal) st.get("35");  
		out_minStart=(BigDecimal) st.get("36");  
        
        if(out_CURCANINVEST==null){out_CURCANINVEST=new BigDecimal(0);}
        if(out_HaveInvestNum==null){out_HaveInvestNum=new BigDecimal(0);}
		double can = Double.parseDouble(out_CURCANINVEST.toString());// financingBase.getCurCanInvest();

		/**
		 * 最小融资额=剩余融资额/(200-投标人数)
		 */
		/*double minFinancing = Double.parseDouble(out_CURCANINVEST.toString()) / (200 - Integer.parseInt(out_HaveInvestNum.toString()));// financingBase.getCurCanInvest()
																																		// /(200-financingBase.getHaveInvestNum());
		if(out_LOWESTMONEY==null){out_LOWESTMONEY=new BigDecimal(0);}
		double minCondition = Double.parseDouble(out_LOWESTMONEY.toString());// condition.getLowestMoney();//
																				// 约束的最小融资额

		// (融资额%200)与约束的最小融资额比较取大者；2012-6-11改成取小者 2012-6-21改成取大者
		double min = minFinancing < minCondition ? minCondition : minFinancing;
		if (min < 1000) {
			min = 1000.00;
		}*/
		
        double min= Double.parseDouble(out_minStart.toString());
		double b1 = Double.parseDouble(out_balance.toString()) + Double.parseDouble(out_frozenamount.toString()) + Double.parseDouble(out_BJYE.toString());// account.getTotalAmount();
		double b2 = Double.parseDouble(out_INVESTAMOUNT.toString());// this.investRecordService.investHistory2(m,
																	// financingBase);//
																	// 当前用户对当前融资项目已经投标的金额

		double max = 0d;
		double highPercent = Double.parseDouble(out_HIGHPERCENT.toString());

		if (Constant.MAX_INVEST.equals("F")) {// 根据融资项目可融资额*会员级别的比例
			max = (Double.parseDouble(out_MAXAMOUNT.toString())) * (highPercent / 100);
			max = max - b2;
		} else {
			// 约束的最大融资额（当前用户已经投标的金额+帐号余额）*会员级别的比例
			max = b1 * (highPercent / 100);
		}
		
	/*	if(out_fbCode.startsWith("H")){
			max = 5000;
			max = max - b2;
		}*/
		
		// 保留千位
		//min = DoubleUtils.doubleToQian(min);
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
		invest.setMinMoney(min);
		invest.setMinMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(min, 3), false));
		invest.setMaxMoney(max);
		invest.setMaxMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(max, 3), false));

		return invest;
	}
	
	/**
	 * 辅助检查投资人约束
	 */
	/*private InvestVO doCheckOld(String in_userName, String in_financingBaseId,String in_usertype) {
		
		InvestVO invest = new InvestVO();
		
		Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
		inParamList.put(1, in_userName);
		inParamList.put(2, in_financingBaseId);
		
		inParamList.put(16, in_usertype);
		inParamList.put(19, Constant.MAX_INVEST);
		
		Map<Integer, Integer> outParameter = new HashMap<Integer, Integer>();
		outParameter.put(3, Types.NUMERIC);
		outParameter.put(4, Types.NUMERIC);
		outParameter.put(5, Types.NUMERIC);
		outParameter.put(6, Types.NUMERIC);
		outParameter.put(7, Types.NUMERIC);
		outParameter.put(8, Types.NUMERIC);
		outParameter.put(9, Types.NUMERIC);
		outParameter.put(10, Types.NUMERIC);
		outParameter.put(11, Types.NUMERIC);
		outParameter.put(12, Types.NUMERIC);
		outParameter.put(13, Types.DATE);
		outParameter.put(14, Types.DATE);
		outParameter.put(15, Types.VARCHAR);
		
		outParameter.put(17, Types.NUMERIC); 
		outParameter.put(18, Types.VARCHAR); 
		
		outParameter.put(20, Types.VARCHAR);
		outParameter.put(21, Types.NUMERIC);
		outParameter.put(22, Types.NUMERIC); 
		outParameter.put(23, Types.NUMERIC);
		
		outParameter.put(24, Types.VARCHAR);
		outParameter.put(25, Types.VARCHAR);
		outParameter.put(26, Types.VARCHAR);
		outParameter.put(27, Types.VARCHAR);
		outParameter.put(28, Types.VARCHAR);
		
		outParameter.put(29, Types.VARCHAR);
		outParameter.put(30, Types.NUMERIC);
		outParameter.put(31, Types.VARCHAR);
		outParameter.put(32, Types.NUMERIC);
		outParameter.put(33, Types.NUMERIC);
		outParameter.put(34, Types.NUMERIC);
		outParameter.put(35, Types.NUMERIC);  
		
		
		//HashMap<Integer, Object> st = this.investConditionService.callProcedureForParameters("p_investCheckMaxMin", inParamList, outParameter);
		HashMap<Integer, Object> st = this.investConditionService.callProcedureForParameters("p_investCheck_new", inParamList, outParameter);
		
		out_balance = (BigDecimal) st.get(3);
		BigDecimal out_frozenamount = (BigDecimal) st.get(4);
		out_CURCANINVEST = (BigDecimal) st.get(5);
		out_CURRENYAMOUNT = (BigDecimal) st.get(6);
		BigDecimal out_MAXAMOUNT = (BigDecimal) st.get(7);
		BigDecimal out_HIGHPERCENT = (BigDecimal) st.get(8);
		BigDecimal out_LOWESTMONEY = (BigDecimal) st.get(9);
		BigDecimal out_HaveInvestNum = (BigDecimal) st.get(10);
		BigDecimal out_BJYE = (BigDecimal) st.get(11);
		BigDecimal out_INVESTAMOUNT = (BigDecimal) st.get(12);
		out_startdate = (Date) st.get(13);
		out_enddate = (Date) st.get(14);
		out_state = (String) st.get(15);
		code = (BigDecimal) st.get(17);
		message = (String) st.get(18);
		
		out_fbCode= (String) st.get(20); 
		out_fbPreInvest= (BigDecimal) st.get(21);
		out_fbRate= (BigDecimal) st.get(22); 
		out_fbInterestDay= (BigDecimal) st.get(23);
		
		
		
		out_memberId= (String) st.get(24); 
		out_memberState= (String) st.get(25); 
		out_memberCategory= (String) st.get(26); 
		out_memberLevelname= (String) st.get(27); 
		out_memberLevelId= (String) st.get(28); 
		
		out_userPassword= (String) st.get(29); 
		out_userIsEnabled= (BigDecimal) st.get(30);
		out_userUserType= (String) st.get(31); 
		out_userAccountState= (BigDecimal) st.get(32);
		out_userAccountId= (BigDecimal) st.get(33); 
		out_userId= (BigDecimal) st.get(34); 
		out_userAccountCredit= (BigDecimal) st.get(35);  
		
		if(out_CURCANINVEST==null){out_CURCANINVEST=new BigDecimal(0);}
		if(out_HaveInvestNum==null){out_HaveInvestNum=new BigDecimal(0);}
		double can = Double.parseDouble(out_CURCANINVEST.toString());// financingBase.getCurCanInvest();
		
		*//**
		 * 最小融资额=剩余融资额/(200-投标人数)
		 *//*
		 double minFinancing = Double.parseDouble(out_CURCANINVEST.toString()) / (200 - Integer.parseInt(out_HaveInvestNum.toString()));// financingBase.getCurCanInvest()
		 // /(200-financingBase.getHaveInvestNum());
		 if(out_LOWESTMONEY==null){out_LOWESTMONEY=new BigDecimal(0);}
		 double minCondition = Double.parseDouble(out_LOWESTMONEY.toString());// condition.getLowestMoney();//
		 // 约束的最小融资额
		 
		 // (融资额%200)与约束的最小融资额比较取大者；2012-6-11改成取小者 2012-6-21改成取大者
		 double min = minFinancing < minCondition ? minCondition : minFinancing;
		 if (min < 1000) {
			 min = 1000.00;
		 }
		 
		 double b1 = Double.parseDouble(out_balance.toString()) + Double.parseDouble(out_frozenamount.toString()) + Double.parseDouble(out_BJYE.toString());// account.getTotalAmount();
		 double b2 = Double.parseDouble(out_INVESTAMOUNT.toString());// this.investRecordService.investHistory2(m,
		 // financingBase);//
		 // 当前用户对当前融资项目已经投标的金额
		 
		 double max = 0d;
		 double highPercent = Double.parseDouble(out_HIGHPERCENT.toString());
		 
		 if (Constant.MAX_INVEST.equals("F")) {// 根据融资项目可融资额*会员级别的比例
			 max = (Double.parseDouble(out_MAXAMOUNT.toString())) * (highPercent / 100);
			 max = max - b2;
		 } else {
			 // 约束的最大融资额（当前用户已经投标的金额+帐号余额）*会员级别的比例
			 max = b1 * (highPercent / 100);
		 }
		 
		 if(out_fbCode.startsWith("H")){
			 max = 5000;
			 max = max - b2;
			 min = 1000;
		 }
		 
		 // 保留千位
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
		 invest.setMinMoney(min);
		 invest.setMinMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(min, 3), false));
		 invest.setMaxMoney(max);
		 invest.setMaxMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(max, 3), false));
		 
		 return invest;
	}*/

	/**
	 * 
	 * @param invest_id
	 * @return
	 */
	public MessageTip totalPayMentRecord(String invest_id) {
		// 当前状态 0未还款 1正常还款 2提前还款 3逾期还款
		MessageTip tip = new MessageTip(true, "还款统计成功");
		try {
			tip.setParam1(paymentRecordService.totalPayMentRecord("0", invest_id) + "");// 未还款
			tip.setParam2(paymentRecordService.totalPayMentRecord("1", invest_id) + "");// 正常还款
			tip.setParam3(paymentRecordService.totalPayMentRecord("2", invest_id) + "");// 提前还款
			tip.setParam4(paymentRecordService.totalPayMentRecord("3", invest_id) + "");// 逾期还款
		} catch (RuntimeException e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("统计异常");
		}
		return tip;
	}

	public MessageTip rate(String userName) {
		MessageTip tip = new MessageTip(true, "统计用户收益成功");
		User u = this.userService.findUser(userName);
		if (null != u) {
			Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
			OutParameterModel outParameter = new OutParameterModel(2, OracleTypes.CURSOR);
			inParamList.put(1, userName);
			ArrayList<LinkedHashMap<String, Object>> resultList = this.investRecordService.callProcedureForList("PKG_income.getRate", inParamList, outParameter);
			if (null == resultList || resultList.size() > 1 || resultList.size() == 0) {
				tip.setSuccess(false);
				tip.setMsg("统计用户收益率出错。");
				tip.setParam1("0.00");// 总入金
				tip.setParam2("0.00");// 总投标
				tip.setParam3("0.00");// 已实现收益
				tip.setParam4("0.00");// 未实现收益
				tip.setParam5("0.00");// 总收益
				tip.setParam6("0.00%");// 已实现收益率
				tip.setParam7("0.00%");// 未实现收益率
				tip.setParam8("0.00%");// 总收益率
			}
			for (int i = 0; i < resultList.size(); i++) {
				LinkedHashMap<String, Object> a = resultList.get(i);
				// 现金充值：不含批量导入的奖励金额
				double cash_in = Double.parseDouble(a.get("cash_in").toString());
				// 总投标=invest+zq_buy_cjj,投标划出+债权买入成交价
				double invest_all = Double.parseDouble(a.get("invest_all").toString());

				// 已实现收益
				double yes_all = Double.parseDouble(a.get("yes_all").toString());
				// 未实现收益
				double not_all = Double.parseDouble(a.get("not_all").toString());
				// 所有收益
				double total_all = Double.parseDouble(a.get("total_all").toString());
				// 已实现收益率
				double rate_yes = Double.parseDouble(a.get("rate_yes").toString());
				// 未实现收益率
				double rate_no = Double.parseDouble(a.get("rate_no").toString());
				// 所有收益率
				// double rate_all =
				// Double.parseDouble(a.get("rate_all").toString());
				double rate_all = rate_yes + rate_no;

				tip.setParam1(DoubleUtils.doubleCheck2(cash_in, 2));// 总入金
				tip.setParam2(DoubleUtils.doubleCheck2(invest_all, 2));// 总投标
				tip.setParam3(DoubleUtils.doubleCheck2(yes_all, 2));// 已实现收益
				tip.setParam4(DoubleUtils.doubleCheck2(not_all, 2));// 未实现收益
				tip.setParam5(DoubleUtils.doubleCheck2(total_all, 2));// 总收益
				tip.setParam6(DoubleUtils.doubleCheck2(rate_yes * 100, 2) + "%");// 已实现收益率
				tip.setParam7(DoubleUtils.doubleCheck2(rate_no * 100, 2) + "%");// 未实现收益率
				tip.setParam8(DoubleUtils.doubleCheck2(rate_all * 100, 2) + "%");// 总收益率
				// System.out.println(new GsonBuilder().create().toJson(tip));
			}
		} else {
			tip.setSuccess(false);
			tip.setMsg("用户名:" + userName + "不存在");
		}
		return tip;
	}

	//华夏三方存管出金(本行与他行)，结算部需要审核
	//招商三方存管出金(本行)，结算部需要审核
	public MessageTip outGolden(String userName, String password, String userType, double money,String param1,String param2) {
		MessageTip tip0 = CloseSoftUtils.closeOutInMoney(); 
		if(tip0.isSuccess()){  
			tip0.setSuccess(false);
			tip0.setMsg(tip0.getMsg()); 
			return tip0; 
		}
		
		
		boolean success = false;
		MessageTip tip = new MessageTip(true, "出金申请成功，等待审核。");
 
		
		MessageTip checkTip=checkRequest2(userName,param1,param2);
		if(!checkTip.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg("错误"); 
			return tip;  
		}else{
			InvestCheckKey investCheckKey=investCheckKeyService.selectById(param2);
			if(null!=investCheckKey){
				tip.setSuccess(false);
				tip.setMsg("错误");  
				return tip;  
			}else{
				try {
					investCheckKeyService.insert(new InvestCheckKey(param2,"outGolden@"+param1,userName));
				} catch (Exception e) {}
			}  
		} 
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(password)) {
				tip.setSuccess(false);
				tip.setMsg("密码不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			password = MD5.MD5Encode(password);
			if (!password.equals(user.getPassword())) {
				tip.setSuccess(false);
				tip.setMsg("密码不正确");
				return tip;
			}
			if (null == user.getFlag() || !"2".equals(user.getFlag())) {
				tip.setSuccess(false);
				tip.setMsg("用户未签约三方存管协议");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}
			//华夏才要验证oldbalance，华夏于2013-12-17停用
			/*if (money > user.getUserAccount().getOld_balance()) {
				tip.setSuccess(false);
				tip.setMsg("最大可出金金额为:" + user.getUserAccount().getOld_balance());
				return tip;
			}*/
			if (money > DoubleUtils.doubleCheck(user.getUserAccount().getBalance(), 2)) {
				tip.setSuccess(false);
				tip.setMsg("出金金额大于账户余额");
				return tip;
			}
			if (money > DoubleUtils.doubleCheck(user.getUserAccount().getOld_balance(), 2)) {
				tip.setSuccess(false);
				tip.setMsg("出金金额大于可转金额，当前可转金额为:"+DoubleUtils.doubleCheck(user.getUserAccount().getOld_balance(), 2)+"元");
				return tip;
			}
			HashMap<String, Object> maped = this.accountService.activityWithdrawAllow(user, money);
			if (!Boolean.parseBoolean(maped.get("boolean").toString())) {//iphone5活动不能提现
				tip.setSuccess(false);
				tip.setMsg("你已参与\"耀5扬微\"活动，限制提现" + maped.get("frozen") + "，当前可提现金额为" + maped.get("surplus"));
				return tip;
			}else{//iphone5可以提现money，继续验证投转贷
				//iphone5限制的金额，可用+冻结+债权-投转贷限制-iphone限制 与 提现金额比较大小
				//double iphone_xianzhi = this.accountService.getIphone5(user);
				double iphone_xianzhi=0;
				HashMap<String, Object> invest_lend = this.accountService.investToLendAllow(user, money , iphone_xianzhi);
				if (!Boolean.parseBoolean(invest_lend.get("boolean").toString())){
					tip.setSuccess(false);
					//投转贷限制金额
					double invest_lend_xianzhi = 0;
					if(null!=invest_lend.get("frozen")){
						invest_lend_xianzhi = Double.parseDouble(invest_lend.get("frozen").toString());
					}
					//if(iphone_xianzhi>0){//参加了iphone5活动，也参加了投转贷
					//	tip.setMsg("你已参与\"耀5扬微\"活动，且是投转贷用户，限制提现" + (iphone_xianzhi+invest_lend_xianzhi) + "，当前可提现金额为" + invest_lend.get("surplus"));
					//}else{//没有参加iphone5活动，或到期；参加了投转贷
						tip.setMsg("你是投转贷用户，限制提现" + invest_lend_xianzhi + "，当前可提现金额为" + maped.get("surplus"));
					//}
					return tip;
				}
			}
			if (money <= 0d) {
				tip.setSuccess(false);
				tip.setMsg("出金金额不能为0元或低于0元");
				return tip;
			} else {
				//签约华夏三方存管=1,签约招商三方存管=2
				//签约本行=1,签约他行=2
				if(user.getSignBank()==1){//华夏本行及他行
					success = this.accountDealService.outGolden(user.getUserAccount(), money, AccountDeal.ZQ2BANK, "25", this.memberBaseService.getMemByUser(user).getBanklib());
				}else if(user.getSignBank()==2&&user.getSignType()==1){//招商本行
					success = this.accountDealService.in_out_merchant(user.getUserAccount(), money, AccountDeal.ZQ2BANK, "25",null, DateUtils.generateNo20());
				}else{
					tip.setSuccess(false);
					tip.setMsg("签约错误");
					return tip;
				}
			}
			if (!success) {
				tip.setSuccess(false);
				tip.setMsg("出金发生意外错误，请重试。");
			}
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
			return tip;
		}
	}

	//华夏三方存管入金(本行)，无审核，实时入金 
    //招行三方存管入金(本行)，无审核，实施入金
	public MessageTip inGolden(String userName, String password, String userType, double money, String param1, String param2) {
		MessageTip tip0 = CloseSoftUtils.closeOutInMoney(); 
		if(tip0.isSuccess()){  
			tip0.setSuccess(false);
			tip0.setMsg(tip0.getMsg()); 
			return tip0; 
		}
		
		MessageTip tip = new MessageTip(true, "入金成功");
 
		MessageTip checkTip=checkRequest2(userName,param1,param2);
		if(!checkTip.isSuccess()){  
			tip.setSuccess(false);
			tip.setMsg("错误"); 
			return tip;  
		}else{
			InvestCheckKey investCheckKey=investCheckKeyService.selectById(param2);
			if(null!=investCheckKey){
				tip.setSuccess(false);
				tip.setMsg("错误");  
				return tip;  
			}else{
				try {
					investCheckKeyService.insert(new InvestCheckKey(param2,"inGolden@"+param1,userName));
				} catch (Exception e) {}
			}  
		}
	  
		byte state = this.openCloseDealService.checkState();
		
	
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(password)) {
				tip.setSuccess(false);
				tip.setMsg("密码不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			password = MD5.MD5Encode(password);
			if (!password.equals(user.getPassword())) {
				tip.setSuccess(false);
				tip.setMsg("密码不正确");
				return tip;
			}
			if (null == user.getFlag() || !"2".equals(user.getFlag())) {
				tip.setSuccess(false);
				tip.setMsg("用户未签约");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}

			//签约华夏三方存管=1,签约招商三方存管=2
			//签约本行=1,签约他行=2
			if(user.getSignBank()==1&&user.getSignType()==1){//华夏本行
				HxbankVO vo = new HxbankVO();
				HxbankParam p = new HxbankParam();
				p.setAccountNo(user.getAccountNo());
				p.setMerAccountNo(user.getUsername());
				p.setAmt(money);
				vo = this.hxbankDealService.inGoldRequest(p, user, user);
				tip.setSuccess(vo.isFlag());
				tip.setMsg(vo.getTip());
			}else if(user.getSignBank()==2&&user.getSignType()==1){//招商本行
				CMBVO vo = new CMBVO();
				String txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
				String txTime = DateUtils.formatDate(new Date(), "HHmmss");
				MemberBase mb = this.memberBaseService.getMemByUser(user);
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
				MerChantRequest6200 request6200 = new MerChantRequest6200();
				request6200.setCoSerial(DateUtils.generateNo20());
				request6200.setCurCode("CNY");
				request6200.setCurFlag("1");
				request6200.setCountry("CHN");
				request6200.setBankAcc(mb.getBankAccount());
				request6200.setFundAcc(user.getUsername());
				request6200.setCustName(user.getRealname());
				request6200.setIDNo(idcard);
				request6200.setIDType(idtype);
				String formatAmount = DoubleUtils.formatDouble(money);
				request6200.setAmount(formatAmount);
				vo = this.cmbDealService.request6200(request6200, user.getId(), txDate, txTime);
				if(vo.isSuccess()){
					User u = userService.findUser(request6200.getFundAcc());
					this.acccountDealService.in_out_merchant(u.getUserAccount(), money, AccountDeal.BANK2ZQ, "24",null,request6200.getCoSerial());
				}
				tip.setSuccess(vo.isSuccess());
				tip.setMsg(vo.getMsg());
			}else{
				tip.setSuccess(false);
				tip.setMsg("签约错误");
				return tip;
			}
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
			return tip;
		}
	}
	/**
	//他行签约华夏三方存管后，入金步骤：1 先转账；2 发起
	//华夏三方存管入金登记申请(他行)，无审核
	//inType:入金方式，1 现金汇款，2 转账汇款
	public MessageTip inGolden_request(String userName, String password, String userType, double money,int inType,String hkdate) {
		MessageTip tip = new MessageTip(true, "入金申请成功，等待银行处理。");
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			tip.setSuccess(false);
			tip.setMsg("交易市场未开市");
			return tip;
		} else if (state == 2) {
			tip.setSuccess(false);
			tip.setMsg("现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
			return tip;
		}
		try {
			if (BaseTool.isNull(userName)) {
				tip.setSuccess(false);
				tip.setMsg("用户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(password)) {
				tip.setSuccess(false);
				tip.setMsg("密码不能为空");
				return tip;
			}
			User user = userService.findUser(userName);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("用户名不存在");
				return tip;
			}
			password = MD5.MD5Encode(password);
			if (!password.equals(user.getPassword())) {
				tip.setSuccess(false);
				tip.setMsg("密码不正确");
				return tip;
			}
			if (null == user.getFlag() || !"2".equals(user.getFlag())) {
				tip.setSuccess(false);
				tip.setMsg("用户未签约三方存管协议");
				return tip;
			}
			if (!user.isEnabled()) {
				tip.setSuccess(false);
				tip.setMsg("用户待审核");
				return tip;
			}
			if (!user.getUserType().equals(userType)) {
				tip.setSuccess(false);
				tip.setMsg("用户类型错误");
				return tip;
			}

			if (null == user.getUserAccount()) {
				tip.setSuccess(false);
				tip.setMsg("此用户没有帐号");
				return tip;
			}

			if (Account.STATE_WAIT == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号待开通");
				return tip;
			}

			if (Account.STATE_CANCEL == user.getUserAccount().getState()) {
				tip.setSuccess(false);
				tip.setMsg("帐号已经注销");
				return tip;
			}

			HxbankVO vo = new HxbankVO();
			HxbankParam p = new HxbankParam();
			p.setAccountNo(user.getAccountNo());//子帐号
			p.setMerAccountNo(user.getUsername());//摊位号
			p.setAmt(money);//金额
			p.setIostart(inType+"");//入金方式
			p.setPersonName(user.getRealname());//汇款人姓名
			MemberBase mb = this.memberBaseService.getMemByUser(user);
			p.setBankName(mb.getBanklib().getCaption());//汇款银行
			p.setOutAccount(mb.getBankAccount());//汇款帐号
			Date d = DateUtils.parseDate(hkdate, "yyyyMMdd");
			p.setDate(DateUtils.formatDate(d, "yyyyMMdd"));//汇款日期
			p.setCheckDate(d);
			vo = this.hxbankDealService.inGoldRegistrationRequest(p, user, user);
			tip.setSuccess(vo.isFlag());
			tip.setMsg(vo.getTip());
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("操作异常");
			return tip;
		}
	}
**/

	private List<FinancingBaseVo> swithInvestList(ArrayList<LinkedHashMap<String, Object>> dataList) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<FinancingBaseVo> financingBaseVoS = new ArrayList<FinancingBaseVo>();
		for (int i = 0; i < dataList.size(); i++) {
			FinancingBaseVo vo = new FinancingBaseVo();
			vo.setCode(dataList.get(i).get("code").toString());
			vo.setId(dataList.get(i).get("id").toString());
			vo.setShortName(dataList.get(i).get("shortname").toString());
			vo.setRate(Double.valueOf(dataList.get(i).get("rate").toString()));
			vo.setHaveInvestNum(Integer.parseInt(dataList.get(i).get("haveinvestnum").toString()));
			if(null!=dataList.get(i).get("address"))
			  vo.setAddress(dataList.get(i).get("address").toString());
			
			vo.setFxbzState(dataList.get(i).get("fxbzstate").toString());
			
			if (null == dataList.get(i).get("companyproperty") || "null".equals(dataList.get(i).get("companyproperty")) || "".equals(dataList.get(i).get("companyproperty"))) {
				vo.setCompanyProperty("-");
			} else {
				vo.setCompanyProperty(dataList.get(i).get("companyproperty").toString());
			}
			if (null == dataList.get(i).get("industry") || "null".equals(dataList.get(i).get("industry")) || "".equals(dataList.get(i).get("industry"))) {
				vo.setIndustry("-");   
			} else {
				vo.setIndustry(dataList.get(i).get("industry").toString().substring(0,dataList.get(i).get("industry").toString().indexOf("(")));
			}
			if (null == dataList.get(i).get("logoname") || "null".equals(dataList.get(i).get("logoname")) || "".equals(dataList.get(i).get("logoname"))) {
				vo.setLogoName("-");
			} else {
				if("10".equals(vo.getFxbzState())){//无担保
					vo.setLogoName("-");
				}else{
					vo.setLogoName(dataList.get(i).get("logoname").toString());
				}
				
			}

			if (null == dataList.get(i).get("guaranteenote") || "null".equals(dataList.get(i).get("guaranteenote")) || "".equals(dataList.get(i).get("guaranteenote"))) {
				vo.setGuaranteeNote("-");
			} else {
				vo.setGuaranteeNote(dataList.get(i).get("guaranteenote").toString());
			}

			if (null != dataList.get(i).get("startdate")) vo.setStartDate(format.parse(dataList.get(i).get("startdate").toString()));
			if (null != dataList.get(i).get("enddate")) vo.setEndDate(format.parse(dataList.get(i).get("enddate").toString())); 
			if (null != dataList.get(i).get("modifydate")){
				try {
					vo.setModifyDate(format_time.parse(dataList.get(i).get("modifydate").toString()));
				} catch (RuntimeException e) {
					e.printStackTrace();
					vo.setModifyDate(DateUtils.getAfter(new Date(), 1));
					System.out.println(dataList.get(i).get("modifydate")+"--KmfexService----code="+vo.getCode());  

				}
			} 
			if (null != dataList.get(i).get("preinvest")) vo.setPreInvest(dataList.get(i).get("preinvest").toString());
			
			vo.setState(dataList.get(i).get("state_").toString());
			Double currenyamount = Double.valueOf(dataList.get(i).get("currenyamount").toString());
			Double maxamount = Double.valueOf(dataList.get(i).get("maxamount").toString());
			Double curcaninvest = Double.valueOf(dataList.get(i).get("curcaninvest").toString());
			if ("5".equals(vo.getState()) || "6".equals(vo.getState()) || "7".equals(vo.getState())) {
				vo.setProGress("100%");
			} else {
				vo.setProGress((DoubleUtils.doubleCheck((currenyamount / maxamount) * 100, 2)) + "%");
			}
			if(null!=dataList.get(i).get("financiercode"))
			  vo.setFinancierCode(dataList.get(i).get("financiercode").toString());
			if(null!=dataList.get(i).get("financierid"))
			  vo.setFinancierId(dataList.get(i).get("financierid").toString());
			if(null!=dataList.get(i).get("financiername"))
			  vo.setFinancierName(dataList.get(i).get("financiername").toString());

			if (null != dataList.get(i).get("yongtu")) vo.setYongtu(dataList.get(i).get("yongtu").toString());

			
			vo.setFxbzStateName(dataList.get(i).get("fxbzstatename").toString());
		  		
		/*	*/
			String businessTypeId=dataList.get(i).get("businesstypeid").toString();
            if("day".equals(businessTypeId)){
            	vo.setTerm(1);  
            	vo.setInterestDay(Integer.parseInt(dataList.get(i).get("interestday").toString()));
            	vo.setTermStr(vo.getInterestDay()+"天");
            	vo.setReturnPattern(dataList.get(i).get("returnpattern").toString());
            }else{
            	vo.setTerm(Integer.parseInt(dataList.get(i).get("term").toString()));
            	vo.setTermStr(vo.getTerm()+"个月");
    			vo.setReturnPattern(dataList.get(i).get("returnpattern").toString());
            }
			
			
			vo.setMaxAmount(maxamount);

			if (null != dataList.get(i).get("qyzs")) vo.setQyzs(dataList.get(i).get("qyzs").toString());
			if (null != dataList.get(i).get("qyzsnote")) vo.setQyzsNote(dataList.get(i).get("qyzsnote").toString());
			if (null != dataList.get(i).get("fddbzs")) vo.setFddbzs(dataList.get(i).get("fddbzs").toString());
			if (null != dataList.get(i).get("fddbzsnote")) vo.setFddbzsNote(dataList.get(i).get("fddbzsnote").toString());
			if (null != dataList.get(i).get("czzs")) vo.setCzzs(dataList.get(i).get("czzs").toString());
			if (null != dataList.get(i).get("czzsnote")) vo.setCzzsNote(dataList.get(i).get("czzsnote").toString());
			if (null != dataList.get(i).get("dbzs")) vo.setDbzs(dataList.get(i).get("dbzs").toString());
			if (null != dataList.get(i).get("dbzsnote")) vo.setDbzsNote(dataList.get(i).get("dbzsnote").toString());
			if (null != dataList.get(i).get("zhzs")) vo.setZhzs(dataList.get(i).get("zhzs").toString());
			if (null != dataList.get(i).get("zhzsnote")) vo.setZhzsNote(dataList.get(i).get("zhzsnote").toString());

			if (null != dataList.get(i).get("guaranteecode")) vo.setGuaranteeCode(dataList.get(i).get("guaranteecode").toString());
			if (null != dataList.get(i).get("guaranteeid")) vo.setGuaranteeId(dataList.get(i).get("guaranteeid").toString());
			if (null != dataList.get(i).get("guaranteename")) vo.setGuaranteeName(dataList.get(i).get("guaranteename").toString());

			vo.setState(dataList.get(i).get("state_").toString());
			vo.setStateName(dataList.get(i).get("statename").toString());// 状态显示名称
			vo.setCurCanInvest(curcaninvest);
			vo.setCurrenyAmount(currenyamount);

			vo.setZhzsStar(dataList.get(i).get("zhzsstar").toString());
			
	       	if("1".equals(vo.getPreInvest())){//开启了优先投标的融资项目
	       		vo.setUsers(this.financingCacheService.getPreInvestUsers(vo.getCode()));
	       	}
			financingBaseVoS.add(vo);

		}
		return financingBaseVoS;

	}
	 /**
	  * JDBC
	  * @param fields
	  * @param tables
	  * @param whstr
	  * @return
	  */
	private CreditRules getCreditRulesByTime(String now) { 
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		CreditRules creditRules=null;
		try {  
		    StringBuilder sb = new StringBuilder(); 
		    sb.append(" t.enable=1  ");
		    sb.append(" and to_date('"+now+"','yyyy-MM-dd HH24:mi:ss') between t.effecttime and t.expiretime ");
			list = this.creditRulesService.queryForList(" t.id,t.relation_value,t.value "," t_creditrules t ", sb.toString()); 
			if(!list.isEmpty()){ 
				  if(list.get(0).get("id") != null){
					  creditRules=new CreditRules();
					  creditRules.setId(list.get(0).get("id").toString());
					  creditRules.setRelation_value(Integer.parseInt(list.get(0).get("relation_value").toString()));
					  creditRules.setValue(Float.parseFloat(list.get(0).get("value").toString())); 
				      return creditRules;
				}  
		   }  
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return creditRules;  
	}
		
	
	private List<ZhaiQuanRecordVo> swithZqList(ArrayList<LinkedHashMap<String, Object>> dataList) throws Exception { 
		List<ZhaiQuanRecordVo> tempfbs = new ArrayList<ZhaiQuanRecordVo>();
		double fee = 0d;
		double taxes = 0d; 
		CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
		CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < dataList.size(); i++) {
			ZhaiQuanRecordVo vo = new ZhaiQuanRecordVo();
			vo.setBjye(Double.valueOf(dataList.get(i).get("bjye").toString()));
			String maxsellprice=dataList.get(i).get("maxsellprice").toString();
			if(maxsellprice.contains(".")){
				maxsellprice=maxsellprice.substring(0, maxsellprice.indexOf("."));
			}
			vo.setMaxSellPrice(Double.valueOf(maxsellprice));
			vo.setLxye(Double.valueOf(dataList.get(i).get("lxye").toString()));
			vo.setBjlxye(vo.getBjye()+ vo.getLxye());
			vo.setContractKeyDataCode(dataList.get(i).get("contractkeydatacode").toString());// 合同
			vo.setInvestRecordId(dataList.get(i).get("investrecordid").toString());
			
			if (null == dataList.get(i).get("lastdate") || "null".equals(dataList.get(i).get("lastdate")) || "".equals(dataList.get(i).get("lastdate"))) {
				vo.setLastDate(null);
			}else{
				vo.setLastDate(format.parse(dataList.get(i).get("lastdate").toString()));
			}
			
			
			vo.setCrpice(Double.valueOf(dataList.get(i).get("crpice").toString()));

			String crpiceStr = "";
			if (vo.getCrpice() <= 0.0) {
				crpiceStr = "-";
			} else {
				crpiceStr =vo.getCrpice() + "";
			}
 
			vo.setFxbzStateName(dataList.get(i).get("fxbzstatename").toString());
			 
			vo.setFinancingBasecode(dataList.get(i).get("financingbasecode").toString());
			vo.setUserName(dataList.get(i).get("username").toString());
			vo.setReturnPattern(dataList.get(i).get("returnpattern").toString());
			vo.setCrpiceStr(crpiceStr);
			vo.setZhaiQuanCode(dataList.get(i).get("zhaiquancode").toString());
			vo.setZqzrState(dataList.get(i).get("zqzrstate").toString());
			vo.setSellingState(dataList.get(i).get("sellingstate").toString());
			 
			if (null == dataList.get(i).get("xyhkr") || "null".equals(dataList.get(i).get("xyhkr")) || "".equals(dataList.get(i).get("xyhkr"))) {
				vo.setXyhkr(null);
			}else{
				vo.setXyhkr(format.parse(dataList.get(i).get("xyhkr").toString()));
			}
			
			vo.setParam1(dataList.get(i).get("param1").toString());// 未还款
			vo.setParam2(dataList.get(i).get("param2").toString());// 正常还款
			vo.setParam3(dataList.get(i).get("param3").toString());// 提前还款
			vo.setParam4(dataList.get(i).get("param4").toString());// 逾期还款
			vo.setParam5(dataList.get(i).get("param5").toString());

			// 已经收回的
			Map<Integer, Object> inParamList = new HashMap<Integer, Object>();
			inParamList.put(1, vo.getInvestRecordId());
			inParamList.put(2, dataList.get(i).get("userid").toString()); 
			 
			//System.out.println(vo.getInvestRecordId()+","+dataList.get(i).get("username").toString()+","+dataList.get(i).get("userid").toString()+","+dataList.get(i).get("modifydate").toString());
			

			Map<Integer, Integer> outParameter = new HashMap<Integer, Integer>();
			outParameter.put(3, Types.NUMERIC);
			outParameter.put(4, Types.NUMERIC);
			outParameter.put(5, Types.NUMERIC);
			HashMap<Integer, Object> st = this.investConditionService.callProcedureForParameters("p_OkBjLxFee", inParamList, outParameter);
			BigDecimal out_okbj = (BigDecimal) st.get(3); 
			BigDecimal out_oklx = (BigDecimal) st.get(4); 
			BigDecimal out_okfee = (BigDecimal) st.get(5);  
			vo.setOkbj(out_okbj.doubleValue());// 已经收回的本金
			vo.setOklx(out_oklx.doubleValue());// 已经收回的利息
			vo.setOkfee(out_okfee.doubleValue());// 已经收回的罚金    

			// 参考收益率应该扣除各种手续费、税费
			fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100) * vo.getCrpice(), 2);
			taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100) * vo.getCrpice(), 2);
			if (vo.getCrpice() == 0) {
				vo.setSyl("-");
				vo.setSymonay("-");
			}
			double dqmoney = vo.getBjye() + vo.getLxye() -vo.getCrpice() - fee - taxes;// 收益金额
			double syltemp = 0d;
			if (vo.getCrpice() == 0) {
				syltemp = 0;
			} else {
				syltemp = (dqmoney / vo.getCrpice()) * 100;
			}
			if (syltemp == 0) {
				vo.setSyl("-");
				vo.setSymonay("-");
			} else {
				vo.setSyl(DoubleUtils.doubleCheck2(syltemp, 2) + "%");
				vo.setSymonay(DoubleUtils.doubleCheck2(dqmoney, 2));
			} 
			tempfbs.add(vo);	 
		}
		return tempfbs;

	}
	public BigDecimal getOut_CURRENYAMOUNT() {
		return out_CURRENYAMOUNT;
	}

	public void setOut_CURRENYAMOUNT(BigDecimal out_CURRENYAMOUNT) {
		this.out_CURRENYAMOUNT = out_CURRENYAMOUNT;
	}

	public BigDecimal getOut_balance() {
		return out_balance;
	}

	public void setOut_balance(BigDecimal out_balance) {
		this.out_balance = out_balance;
	}

	public Date getOut_startdate() {
		return out_startdate;
	}

	public void setOut_startdate(Date out_startdate) {
		this.out_startdate = out_startdate;
	}

	public Date getOut_enddate() {
		return out_enddate;
	}

	public void setOut_enddate(Date out_enddate) {
		this.out_enddate = out_enddate;
	}

	public String getOut_state() {
		return out_state;
	}

	public void setOut_state(String out_state) {
		this.out_state = out_state;
	}


	public BigDecimal getCode() {
		return code;
	}


	public void setCode(BigDecimal code) {
		this.code = code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public BigDecimal getOut_CURCANINVEST() {
		return out_CURCANINVEST;
	}


	public void setOut_CURCANINVEST(BigDecimal outCURCANINVEST) {
		out_CURCANINVEST = outCURCANINVEST;
	}


	public String getOut_fbCode() {
		return out_fbCode;
	}


	public void setOut_fbCode(String outFbCode) {
		out_fbCode = outFbCode;
	}


	public BigDecimal getOut_fbPreInvest() {
		return out_fbPreInvest;
	}


	public void setOut_fbPreInvest(BigDecimal outFbPreInvest) {
		out_fbPreInvest = outFbPreInvest;
	}


	public BigDecimal getOut_fbRate() {
		return out_fbRate;
	}


	public void setOut_fbRate(BigDecimal outFbRate) {
		out_fbRate = outFbRate;
	}

 

	public BigDecimal getOut_fbInterestDay() {
		return out_fbInterestDay;
	}


	public void setOut_fbInterestDay(BigDecimal outFbInterestDay) {
		out_fbInterestDay = outFbInterestDay;
	}


	public String getOut_memberId() {
		return out_memberId;
	}


	public void setOut_memberId(String outMemberId) {
		out_memberId = outMemberId;
	}


	public String getOut_memberState() {
		return out_memberState;
	}


	public void setOut_memberState(String outMemberState) {
		out_memberState = outMemberState;
	}


	public String getOut_memberCategory() {
		return out_memberCategory;
	}


	public void setOut_memberCategory(String outMemberCategory) {
		out_memberCategory = outMemberCategory;
	}


	public String getOut_memberLevelname() {
		return out_memberLevelname;
	}


	public void setOut_memberLevelname(String outMemberLevelname) {
		out_memberLevelname = outMemberLevelname;
	}


	public String getOut_memberLevelId() {
		return out_memberLevelId;
	}


	public void setOut_memberLevelId(String outMemberLevelId) {
		out_memberLevelId = outMemberLevelId;
	}


	public String getOut_userPassword() {
		return out_userPassword;
	}


	public void setOut_userPassword(String outUserPassword) {
		out_userPassword = outUserPassword;
	}


	public BigDecimal getOut_userIsEnabled() {
		return out_userIsEnabled;
	}


	public void setOut_userIsEnabled(BigDecimal outUserIsEnabled) {
		out_userIsEnabled = outUserIsEnabled;
	}


	public String getOut_userUserType() {
		return out_userUserType;
	}


	public void setOut_userUserType(String outUserUserType) {
		out_userUserType = outUserUserType;
	}


	public BigDecimal getOut_userAccountState() {
		return out_userAccountState;
	}


	public void setOut_userAccountState(BigDecimal outUserAccountState) {
		out_userAccountState = outUserAccountState;
	}


	public BigDecimal getOut_userAccountId() {
		return out_userAccountId;
	}


	public void setOut_userAccountId(BigDecimal outUserAccountId) {
		out_userAccountId = outUserAccountId;
	}


	public BigDecimal getOut_userId() {
		return out_userId;
	}


	public void setOut_userId(BigDecimal outUserId) {
		out_userId = outUserId;
	}


	public BigDecimal getOut_userAccountCredit() {
		return out_userAccountCredit;
	}


	public void setOut_userAccountCredit(BigDecimal outUserAccountCredit) {
		out_userAccountCredit = outUserAccountCredit;
	}
	
	public BigDecimal getOut_minStart() {
		return out_minStart;
	}


	public void setOut_minStart(BigDecimal outMinStart) {
		out_minStart = outMinStart;
	}

}
