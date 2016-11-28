package com.kmfex.zhaiquan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
import com.opensymphony.xwork2.ActionContext;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;

/**
 * 债权信息
 * 
 * @author         
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class ZhaiQuanInvestAction extends BaseAction {
	private String id;
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource
	ContractKeyDataService contractKeyDataService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	BuyingService buyingService;
	@Resource
	UserService userService;
	@Resource PaymentRecordService paymentRecordService;
	@Resource ContractService contractService;
	private String zqzrState;
	private String querykeyWord = "";
	private double money = 0d;
	private String opeState;
	private String investRecordId;
	private String contract_id;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	FinancingBaseService financingBaseService;
	
	@Resource
	SellingService sellingService;
	/**
	 * 债权的id号
	 */
	private String zhaiQuanId;

	/**
	 * 当前用户(User)id号
	 */
	private String loginUserId;
	
	private String userName;
	
	public String list_bao(){
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//this.checkDate();
		//Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		if(null==this.querykeyWord||"".equals(this.querykeyWord)){
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			return "list_bao";
		}
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" state = '7' ");
			//sql.append(" and ");
			//sql.append(" o.qianyueDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			//sql.append(" and ");
			//sql.append(" o.qianyueDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			// 过滤项目编号
			sql.append(" and o.code like '%" + this.querykeyWord.toUpperCase().trim() + "%' ");
			sql.append(" order by o.qianyueDate desc");
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			QueryResult<FinancingBase> fbs = this.financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sql.toString(), new ArrayList<String>());
			pageView.setQueryResult(fbs);
			for(FinancingBase f:pageView.getRecords()){
				List<InvestRecord> ir = this.investRecordService.getInvestRecordListByFinancingId(f.getId());
				f.setInvestrecords(ir);
			}
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_bao";
	}
	
	//允许转让的包设置为禁止转让，并检测该包下是否有“出让中”的债权，如果有则进行撤单操作。
	public String disableZq(){
		if(null!=this.id){
			FinancingBase b = this.financingBaseService.selectById(this.id);
			if(null!=b&&"7".equals(b.getState())&&b.isEnableZq()){
				List<InvestRecord> ir = this.investRecordService.getInvestRecordListByFinancingId(b.getId());
				for(InvestRecord r:ir){
					InvestRecord temp = this.investRecordService.selectById(r.getId());
					//债权已签约且为出售中状态，则进行撤单操作。
					if("2".equals(temp.getState())&&InvestRecord.SELLINFSTATE_SELL.equals(temp.getSellingState())){//当前债权
						String h = "from Selling o where o.state='"+Selling.STATE_NOT+"' and o.investRecord.id='"+temp.getId()+"'";
						Selling s = this.sellingService.selectByHql(h);
						try {
							//出让中时进行撤单操作(撤单更新了：卖出单状态及投标记录状态)
							if(null!=s&&Selling.STATE_NOT.equals(s.getState())){
								this.sellingService.cancel(s.getId());
							}
						} catch (EngineException e) {
							e.printStackTrace();
						}
					}
				    ////更新修改时间
					try {
						temp.setModifyDate(new Date());
						this.investRecordService.update(temp);
					} catch (EngineException e) { 
						e.printStackTrace();
					}
				}
				b.setEnableZq(false);
				try {
					this.financingBaseService.update(b);
					
				} catch (EngineException e) {
				}
			}
		}
		return "list_bao_return";
	}
	
	//禁止转让的包设置为允许转让
	public String enableZq(){
		if(null!=this.id){
			FinancingBase b = this.financingBaseService.selectById(this.id);
			if(null!=b&&"7".equals(b.getState())&&!b.isEnableZq()){ 
				List<InvestRecord> ir = this.investRecordService.getInvestRecordListByFinancingId(b.getId());
				for(InvestRecord r:ir){ 
					try {
						//更新修改时间
						r.setModifyDate(new Date());
						this.investRecordService.update(r);
					} catch (EngineException e) { 
						e.printStackTrace();
					}
				} 
				
				b.setEnableZq(true);
				try { 
					this.financingBaseService.update(b);
					
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}
		}
		return "list_bao_return";
	}

	// 债权信息查询
	public String list() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.getQuerykeyWord().trim();
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.zhaiQuanCode like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" ) ");
			}
			if (null != this.zqzrState && !"".equals(zqzrState)) {
				sb.append(" and ");
				sb.append(" o.zqzrState='" + zqzrState + "'");
			} else {
				sb.append(" and ");
				sb.append(" o.zqzrState in ('1','2','3','4')");
			}

			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");
			pageView.setQueryResult(investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	// 债权信息前台(行情)
	public String flist() {
		try {
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.setLoginUserId(String.valueOf(loginUser.getId()));

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "asc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.getQuerykeyWord().trim();
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.zhaiQuanCode like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" ) ");
			}

			if (null != this.zqzrState && !"".equals(zqzrState)) {
				sb.append(" and ");
				sb.append(" o.zqzrState='" + zqzrState + "' and o.zqzrState!='4'");
			} else {
				sb.append(" and ");
				sb.append(" o.zqzrState in ('1','2','3')");
			}
			
			//转让成功的不显示
			sb.append(" and ");
			sb.append(" o.sellingState!='"+InvestRecord.SELLINFSTATE_SUC+"'");
			
			
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			pageView.setQueryResult(investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "flist";
	}

	// 我持有的债权
	public String myzqlist() {
		try {
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.setLoginUserId(String.valueOf(loginUser.getId()));

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<InvestRecord> pageView = new PageView<InvestRecord>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.getQuerykeyWord().trim();
				sb.append(" and ( o.financingBase.shortName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.pName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.investor.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.eName like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.financingBase.financier.user.username like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" or o.zhaiQuanCode like ?");
				params.add("%" + querykeyWord + "%");
				sb.append(" ) ");
			}

			if (null != this.zqzrState && !"".equals(zqzrState)) {
				sb.append(" and ");
				sb.append(" o.zqzrState='" + zqzrState + "'");
			} else {
				sb.append(" and ");
				sb.append(" o.zqzrState in ('1','2','3','4')");
			}

			// 只显示我持有的债权
			sb.append(" and ");
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			sb.append(" o.investor.id='" + this.memberBaseService.getMemByUser(user).getId() + "'");
			// sb.append(" o.id in
			// ("+contractKeyDataService.findContractKeyDatas(user.getUsername())+")");
			// //根据合同的投资人找投标记录

			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			pageView.setQueryResult(investRecordService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "myzqlist";
	}

	/**
	 * 根据报价计算各种费用和收益
	 * 
	 * @return
	 * @throws Exception
	 */
	public String jsmoney() throws Exception {
		try {

			HashMap<String, Double> fy = js();

			CommonVo feiyong = new CommonVo();

			feiyong.setDb1(fy.get("fee"));
			feiyong.setDb2(fy.get("taxes"));
			feiyong.setDb3(fy.get("qmz"));

			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), feiyong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, Double> js() {
		HashMap<String, Double> result = new HashMap<String, Double>();
		try {
			CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
			CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
			InvestRecord ir = investRecordService.selectById(zhaiQuanId);
			double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100) * money, 2);
			double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100) * money, 2);
			double qmz = DoubleUtils.doubleCheck(ir.getQmz() - (fee + taxes + money), 2);
			result.put("fee", fee);
			result.put("taxes", taxes);
			result.put("qmz", qmz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 牌操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upZqzrState() throws Exception {
		String ope = "";
		if ("1".equals(opeState)) {
			ope = "挂牌";
		} else if ("2".equals(opeState)) {
			ope = "停牌";
		} else if ("3".equals(opeState)) {
			ope = "复牌";
		} else if ("4".equals(opeState)) {
			ope = "摘牌";
		} else {
		}
		try {
			buyingService.upZqzrState(opeState, this.investRecordId);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, ope + "成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, ope + "失败");
		}
		return "upZqzrState";
	}

	/**
	 * 查看债权详细信息,管理我持有的债权<br/> 取得指定债权的所有还款记录。<br/> 如果债权下的转让合同数为0<br/>
	 * 则该债权为当前用户的原始债权<br/>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		try {

			if (this.zhaiQuanId == null || "".equals(this.zhaiQuanId)) {
				return null;
			}
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			InvestRecord ir = this.investRecordService.selectById(this.zhaiQuanId);
			if (ir == null) {
				return null;
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("isfrombuy", false);

			List<PaymentRecord> prs = this.paymentRecordService.getScrollDataCommon(" from PaymentRecord where investRecord.id = ? order by succession asc", ir.getId());
			Contract contract = null;
			if(contract_id == null || "".equals(contract_id)){
				contract = this.contractService.selectById(" from Contract where buyer.id = ? and investRecord.id = ? order by fbrq desc", new Object[]{loginUser.getId(),ir.getId()});
			}else{
				contract = this.contractService.selectById(" from Contract where id = ? and investRecord.id = ? order by fbrq desc", new Object[]{contract_id,ir.getId()});
			}
			if (contract != null) {
				request.setAttribute("isfrombuy", true);
				request.setAttribute("con", contract);
			}
			

			request.setAttribute("ir", ir);
			request.setAttribute("prs", prs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "detail";
	}

	/**
	 * 供webservice客户端调用的
	 * 查看债权详细信息,管理我持有的债权<br/> 取得指定债权的所有还款记录。<br/> 如果债权下的转让合同数为0<br/>
	 * 则该债权为当前用户的原始债权<br/>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detailWs() throws Exception {
		try {

			if (this.zhaiQuanId == null || "".equals(this.zhaiQuanId)) {
				return null;
			}
			//User loginUser = this.userService.findUser(userName);

			InvestRecord ir = this.investRecordService.selectById(this.zhaiQuanId);
			if (ir == null) {
				return null;
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("isfrombuy", false);

			List<PaymentRecord> prs = this.paymentRecordService.getScrollDataCommon(" from PaymentRecord where investRecord.id = ? order by succession asc", ir.getId());
			Contract contract = null;
			if(contract_id == null || "".equals(contract_id)){
				contract = this.contractService.selectById(" from Contract where  investRecord.id = ? order by fbrq desc", new Object[]{ir.getId()});
			}else{
				contract = this.contractService.selectById(" from Contract where id = ? and investRecord.id = ? order by fbrq desc", new Object[]{contract_id,ir.getId()});
			}
			if (contract != null) {
				request.setAttribute("isfrombuy", true);
				request.setAttribute("con", contract);
			}
			

			request.setAttribute("ir", ir);
			request.setAttribute("prs", prs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "detailWs";
	}
	public String getZqzrState() {
		return zqzrState;
	}

	public void setZqzrState(String zqzrState) {
		this.zqzrState = zqzrState;
	}

	public String getQuerykeyWord() {
		return querykeyWord;
	}

	public void setQuerykeyWord(String querykeyWord) {
		this.querykeyWord = querykeyWord;
	}

	public List<CommonVo> getPaiList() {
		List<CommonVo> tList = new ArrayList<CommonVo>();
		tList.add(new CommonVo("1", "挂牌"));
		tList.add(new CommonVo("2", "停牌"));
		tList.add(new CommonVo("3", "复牌"));
		tList.add(new CommonVo("4", "摘牌"));
		return tList;
	} 
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getZhaiQuanId() {
		return zhaiQuanId;
	}

	public void setZhaiQuanId(String zhaiQuanId) {
		this.zhaiQuanId = zhaiQuanId;
	}

	public String getInvestRecordId() {
		return investRecordId;
	}

	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}

	public String getOpeState() {
		return opeState;
	}

	public void setOpeState(String opeState) {
		this.opeState = opeState;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getContract_id() {
		return contract_id;
	}

	public void setContract_id(String contract_id) {
		this.contract_id = contract_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
