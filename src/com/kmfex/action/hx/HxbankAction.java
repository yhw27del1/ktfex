package com.kmfex.action.hx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.OpenCloseDeal;
import com.kmfex.model.hx.HxbankDeal;
import com.kmfex.model.hx.HxbankDealSub;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.vo.PageForProcedureVO;
import com.wisdoor.struts.BaseAction;
/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class HxbankAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Resource
	private HxbankDealService hxbankDealService;
	
	@Resource
	private UserService userService;
	
	@Resource
	OpenCloseDealService openCloseDealService;
	
	private List<HxbankDeal> deals;
	
	private HxbankParam param;
	
	private Date checkDate;
	
	private String id;
	private String result;
	private String state = "success";
	private String trade = "yes";
	private HxbankDeal deal;
	private String name;
	//签到与签退页面
	public String sign_in_out(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date latest = this.openCloseDealService.getLatestKaiShi();
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
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" ( o.trnxCode= 'DZ015' or o.trnxCode= 'DZ016' ) ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sign_in_off";
	}
	
	public String sign_in() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = new HxbankParam();
		try {
			vo = this.hxbankDealService.sign_in(p,getLoginUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String sign_off() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = new HxbankParam();
		try {
			vo = this.hxbankDealService.sign_off(p,getLoginUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String subAccountSigned_ui(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ020' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "subAccountSigned_ui";
	}
	
	// 子账户签约(必须是跨行的)，需要摊位号鉴权
	public String subAccountSigned() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()){
			owner = this.userService.findUser(this.param.getMerAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.subAccountSigned(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号不能为空或摊位号不存在");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String subAccountSync_ui(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ010' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "subAccountSync_ui";
	}
	
	//子账户同步，需要摊位号鉴权
	public String subAccountSync() throws Exception {
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()){
			owner = this.userService.findUser(this.param.getMerAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.subAccountSync(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号不能为空或摊位号不存在");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//入金与出金页面
	public String gold_in_out(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" ( o.trnxCode= 'DZ021' or o.trnxCode= 'DZ022' or o.trnxCode= 'DZ017' or o.trnxCode= 'DZ007' ) ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "gold_in_out";
	}
	
	public String gold_in_request() throws Exception{
		if(null==this.checkDate)this.checkDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ022' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "gold_in_request";
	}
	
	public String gold_out_send() throws Exception{
		if(null==this.checkDate)this.checkDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ007' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "gold_out_send";
	}
	
	//入金申请(入金方式1(本行使用),直接入金)，需要摊位号和子账号鉴权
	public String inGoldRequest() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()&&null!=this.param.getAccountNo()){
			owner = this.userService.getUser(this.param.getMerAccountNo(),this.param.getAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.inGoldRequest(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//入金登记申请(入金方式2(跨行使用),交易商先转账,再入金登记申请,银行最后发起入金通知)
	public HxbankVO inGoldRegistrationRequest() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		p.setDate(DateUtils.formatDate(this.checkDate, "yyyyMMdd"));
		p.setCheckDate(this.checkDate);
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()&&null!=this.param.getAccountNo()){
			owner = this.userService.getUser(this.param.getMerAccountNo(),this.param.getAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.inGoldRegistrationRequest(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//出金(出金方式1(本行使用),直接出金)，需要摊位号和子账号鉴权
	public String outGold() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()&&null!=this.param.getAccountNo()){
			owner = this.userService.getUser(this.param.getMerAccountNo(),this.param.getAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.outGold(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//出金审核结果发送(出金方式2(跨行使用),银行先发起出金申请(系统生成待审核的出金审核结果发送记录),交易市场再审核)，需要摊位号和子账号鉴权
	public String outGoldAuditResultSend() throws Exception{
		HxbankVO vo = new HxbankVO();
		if(StringUtils.isNotEmpty(this.id)&&StringUtils.isNotEmpty(this.result)){
			HxbankDeal d = this.hxbankDealService.selectById(this.id);
			if(null!=d){
				if(d.getResult().equals("2")){
					HxbankParam p = new HxbankParam();
					p.setResult(this.result);//审核结果,1位:0拒绝,1通过
					p.setMerAccountNo(d.getMerAccountNo());
					p.setBankTxSerNo(d.getBankTxSerNo());
					p.setAccountNo(d.getAccountNo());
					p.setAmt(d.getAmt());
					p.setId(d.getId());
					User owner = this.userService.getUser(d.getMerAccountNo(),d.getAccountNo());
					if(null!=owner){
						vo = this.hxbankDealService.outGoldAuditResultSend(p,getLoginUser(),owner);
					}else{
						vo.setFlag(false);
						vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
					}
				}else{
					vo.setFlag(false);
					vo.setTip("出金审核结果发送记录已审核过");
				}
			}else{
				vo.setFlag(false);
				vo.setTip("找不到出金审核结果发送记录");
			}
		}else{
			vo.setFlag(false);
			vo.setTip("参数错误");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String tradingMarketBankBalance_ui(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ032' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "tradingMarketBankBalance_ui";
	}
	
	// 银行子帐号余额查询，查询交易市场银行子账户余额，需要摊位号和子账号鉴权
	public String tradingMarketBankBalance() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()&&null!=this.param.getAccountNo()){
			owner = this.userService.getUser(this.param.getMerAccountNo(),this.param.getAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.tradingMarketBankBalance(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public String surrender_ui(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ012' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "surrender_ui";
	}
	
	// 交易商解约，需要摊位号和子账号鉴权
	public String surrender() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if(null!=this.param&&null!=this.param.getMerAccountNo()&&null!=this.param.getAccountNo()){
			owner = this.userService.getUser(this.param.getMerAccountNo(),this.param.getAccountNo());
		}
		if(null!=owner){
			vo = this.hxbankDealService.surrender(p,getLoginUser(),owner);
		}else{
			vo.setFlag(false);
			vo.setTip("摊位号,子账号不能为空或摊位号,子账号不存在或未签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	/**
	 * 明细核对，清算，对账
	 * */
	
	//出入金明细核对页面
	public String inOutGoldDetailedCheck_ui() throws Exception{
		if(null==this.checkDate)this.checkDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if("success".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = true ");
		}else if("failure".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = false ");
		}
		sb.append(" and ");
		sb.append(" o.trnxCode = 'DZ018' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "inOutGoldDetailedCheck_ui";
	}
	
	public String inOutGoldDetail(){
		try {
			if (this.id != null && !"".equals(this.id) ) {
				this.deal = this.hxbankDealService.selectById(this.id);
				List<HxbankDealSub> subs = this.hxbankDealService.getSubs(this.deal);
				this.deal.setSubs(subs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "inOutGoldDetail";
	}
	
	//出入金明细核对
	public String inOutGoldDetailedCheck() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = new HxbankParam();
		p.setDate(DateUtils.formatDate(this.checkDate, "yyyy-MM-dd"));
		p.setCheckDate(this.checkDate);
		vo = this.hxbankDealService.inOutGoldDetailedCheck(p,getLoginUser());
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//银行清算与对账页面
	public String bank_liquidation_reconciliation_ui() throws Exception{
		Date latest = this.openCloseDealService.getLatestKaiShi();
		if(null==latest){
			if(null==this.checkDate)this.checkDate = new Date();
			this.checkDate();
		}else{
			if(null==this.checkDate)this.checkDate = latest;
			if(null==this.getStartDate()){
				this.setStartDate(latest);
			}
			if(null==this.getEndDate()){
				this.setEndDate(new Date());
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if("success".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = true ");
		}else if("failure".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = false ");
		}
		sb.append(" and ");
		sb.append(" ( o.trnxCode= 'DZ008' or o.trnxCode= 'DZ009' ) ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			ServletActionContext.getRequest().setAttribute("user", this.getLoginUser());
			for(HxbankDeal dl:pageView.getRecords()){
				StringBuffer rt = new StringBuffer();
				String jiedai = "";//借贷
				if("DZ008".equals(dl.getTrnxCode())){//清算的子数据统计
					//正常交易，借，贷
					String sub_hql = "select s.flag,sum(s.bishu),sum(s.amt) from HxbankDealSub s where s.type = '01' and s.parent.id = '"+dl.getId()+"' group by s.flag";
					QueryResult<Object> qr = this.hxbankDealService.groupHqlQuery(sub_hql);
					if (null != qr.getResultlist() && qr.getResultlist().size() > 0) {
						for (Object arr : qr.getResultlist()) {
							Object[] o = (Object[]) arr;
							Integer flag = Integer.parseInt(o[0].toString());
							Integer count = Integer.parseInt(o[1].toString());
							Double amt = Double.parseDouble(o[2].toString());
							if(flag==1){
								jiedai = "借:";
							}else if(flag==2){
								jiedai = "贷:";
							}
							rt.append(jiedai+count+"笔,"+amt+"元"+"<br />");
						}
					}else{
						rt.append("无正常交易业务数据");
					}
					dl.setDealerOperNo(rt.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bank_liquidation_reconciliation_ui";
	}
	
	public String bank_liquidation_reconciliation_detail(){
		try {
			if (this.id != null && !"".equals(this.id) ) {
				this.deal = this.hxbankDealService.selectById(this.id);
				List<HxbankDealSub> subs = this.hxbankDealService.getSubs(this.deal);
				this.deal.setSubs(subs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bank_liquidation_reconciliation_detail";
	}
	
	private Date selectDate;//界面选择的日期
	private Date selectDate_pre;//选择日期的前一日
	private boolean show = false;//true,页面显示实时余额,false,页面显示日切余额
	private String type = "T";
	private boolean load = false;
	//系统跨日清算
	public String sys_liquidation_ui(){
		Date current = new Date();
		if(null==this.selectDate)this.selectDate=current;
		this.selectDate_pre = DateUtils.getAfter(this.selectDate, -1); 
		if(DateUtils.getBetween(this.selectDate, current)==0){
			this.show = true;//选择当天日期，则日切还没有数据，故显示实时余额
		}
		System.out.println(this.load);
		if(load){
			Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
			final OutParameterModel outParameter = new OutParameterModel(4, OracleTypes.CURSOR);
			inParamList.put(1, this.getKeyWord());
			inParamList.put(2, new java.sql.Date(this.selectDate.getTime()));
			inParamList.put(3, new java.sql.Date(this.selectDate_pre.getTime()));
			String p = "PKG_sys_qingsuan.sys_qingsuan_kuari_"+this.type;
			ArrayList<LinkedHashMap<String, Object>> result = this.hxbankDealService.callProcedureForList(p, inParamList, outParameter);
			ServletActionContext.getRequest().setAttribute("result", result);
		}
		return "sys_liquidation_ui";
	}
	
	public String sys_liquidation_reconciliation_ui(){
		this.checkDate();
		Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		if("success".equals(this.state)){
			inParamList.put(1, "成功");
		}else if("failure".equals(this.state)){
			inParamList.put(1, "失败");
		}else{
			inParamList.put(1, null);
		}
		if("yes".equals(this.trade)){//有交易
			inParamList.put(2, "yes");
		}else if("no".equals(this.trade)){//无交易
			inParamList.put(2, "no");
		}else{//全部
			inParamList.put(2, null);
		}
		inParamList.put(3, this.getKeyWord());
		inParamList.put(4, this.getPage());
		inParamList.put(5, this.getShowRecord());
		PageForProcedureVO pageView = this.hxbankDealService.callProcedureForPage("PKG_sys_qingsuan.sys_qingsuan", inParamList);
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "sys_liquidation_reconciliation_ui";
	}
	
	public String sys_liquidation_reconciliation_detail() throws Exception{
		if(null==this.checkDate)this.checkDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		if("success".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = true ");
		}else if("failure".equals(this.state)){
			sb.append(" and ");
			sb.append(" o.success = false ");
		}
		sb.append(" and ");
		sb.append(" o.trnxCode= 'SYS001' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sys_liquidation_reconciliation_detail";
	}
	
	@Resource
	AccountService accountService;
	
	//执行系统清算，此功能废弃
	/*public String sys_liquidation_reconciliation_do(){
		HxbankVO vo = new HxbankVO();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		if (state == 1) {
			vo.setTip("正在交易中，不能执行系统清算");
		} else if(state == 0 || state == 2 || state == 3){//未启用规则或者现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)或已清算才能执行系统清算
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			StringBuilder sql_list_failure = new StringBuilder(" select t.accountid from V_QINGSUAN t where 1=1 and t.sys_qingsuan=0 and t.state = '失败' ");
			StringBuilder sql_list_success = new StringBuilder(" select t.accountid from V_QINGSUAN t where 1=1 and t.sys_qingsuan=0 and t.state = '成功' ");
			try {
				ArrayList<LinkedHashMap<String, Object>> result_failure = this.hxbankDealService.selectListWithJDBC(sql_list_failure.toString());
				if(result_failure.size()==0){//无失败的记录
					ArrayList<LinkedHashMap<String, Object>> result_success = this.hxbankDealService.selectListWithJDBC(sql_list_success.toString());
					if(result_success.size()==0){
						vo.setTip(format.format(new Date())+"已清算过，请不要重复进行系统清算。");
					}else{
						long start = System.currentTimeMillis();
						for(LinkedHashMap<String, Object> l:result_success){
							Account a = this.accountService.selectById(Long.parseLong(l.get("accountid").toString()));
							if(!a.isSys_qingsuan()){//系统清算标识为false，则进行清算。防止页面未刷新的重复清算。
								a.setSys_qingsuan(true);
								a.setOld_balance(a.getBalance()+a.getFrozenAmount());
								this.accountService.update(a);
							}
						}
						HxbankDeal d = new HxbankDeal("系统清算","SYS001");
						d.setOperator(this.getLoginUser());//操作者
						d.setCountOut(result_success.size());//笔数
						d.setSuccess(true);
						this.hxbankDealService.insert(d);
						long m = (System.currentTimeMillis()-start)/1000;
						vo.setTip(format.format(new Date())+"，系统清算成功，共耗时："+m+"秒，共："+result_success.size()+"个用户。");
					}
				}else{
					vo.setTip(format.format(new Date())+"存在失败的记录，请处理后再进行系统清算。");
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setTip("系统清算出错");
			}
		}else{
			vo.setTip("未知错误，请联系管理员");
		}
		try {
			getOut().write(vo.getJSON(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	//清算,先清算,清算成功后,发送对账报文给银行
	public String liquidation() throws Exception{
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if(!(state==3)){//开市与休市表最后一条记录不是“清算”，生成一条
			OpenCloseDeal d = new OpenCloseDeal();
			d.setCreateDate(new Date());
			d.setOperator(this.getLoginUser());
			d.setName("清算");
			d.setMemo("华夏");
			d.setSuccess(true);
			this.openCloseDealService.insert(d);
		}
		HxbankVO vo = new HxbankVO();
		HxbankParam p = new HxbankParam();
		p.setDate(DateUtils.formatDate(this.checkDate, "yyyyMMdd"));
		p.setCheckDate(DateUtils.getDateNomal(this.checkDate));
		vo = this.hxbankDealService.liquidation(p,getLoginUser());
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//对账,先清算,清算成功后,发送对账报文给银行
	public String reconciliation() throws Exception{
		HxbankVO vo = new HxbankVO();
		HxbankParam p = new HxbankParam();
		p.setDate(DateUtils.formatDate(this.checkDate, "yyyyMMdd"));
		p.setCheckDate(this.checkDate);
		vo = this.hxbankDealService.reconciliation(p,getLoginUser());
		if(vo.isFlag()){//对账成功,openclosedeal中生成一条"对账"记录
			OpenCloseDeal d = new OpenCloseDeal();
			d.setCreateDate(new Date());
			d.setOperator(this.getLoginUser());
			d.setName("对账");
			d.setMemo("华夏");
			d.setSuccess(true);
			this.openCloseDealService.insert(d);
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	public void setParam(HxbankParam param) {
		this.param = param;
	}

	public HxbankParam getParam() {
		return param;
	}

	public void setDeals(List<HxbankDeal> deals) {
		this.deals = deals;
	}

	public List<HxbankDeal> getDeals() {
		return deals;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setDeal(HxbankDeal deal) {
		this.deal = deal;
	}

	public HxbankDeal getDeal() {
		return deal;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getTrade() {
		return trade;
	}

	public void setSelectDate(Date selectDate) {
		this.selectDate = selectDate;
	}

	public Date getSelectDate() {
		return selectDate;
	}

	public void setSelectDate_pre(Date selectDate_pre) {
		this.selectDate_pre = selectDate_pre;
	}

	public Date getSelectDate_pre() {
		return selectDate_pre;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShow() {
		return show;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}
}
