package com.kmfex.zhaiquan.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.InvestRecord;
import com.kmfex.service.InvestRecordService;
import com.kmfex.zhaiquan.model.Buying;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.ContractService;
import com.opensymphony.xwork2.ActionContext;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;

/**
 * 受让信息
 * 
 * @author  ,aora
 * 修改记录  
 * 2013年5月30日16:32   修改mlist()方法,根据关键字模糊查询修正；
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class BuyingAction extends BaseAction {
	private static String BUY_RESULT = "buyResult";
	private static String BUY_ERROR_MSG = "buyErrorMsg";

	private Map<String, String> buyResult = new HashMap<String, String>();

	@Resource BuyingService buyingService;
	@Resource private InvestRecordService investRecordService;
	@Resource ContractService contractService;
	public String queryState;
	private String id;
	private int recordSum=5;
	private String querykeyWord = "";
	/**
	 * 债权id号
	 * */
	private String zhaiQuanId;
	/**
	 * 债权转让服务费
	 * */
	private double zqfwf;

	/**
	 * 代扣税费
	 * */
	private double zqsf;

	/**
	 * 投资人出价
	 * */
	private double buyingPrice;

	/*
	 * 后台管理列表
	 */
	public String mlist() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Buying> pageView = new PageView<Buying>(getShowRecord(),
					getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();

			sb.append(" and ");
			sb.append(" o.createDate >= to_date('"
					+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext)
					+ "','yyyy-MM-dd')");
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.querykeyWord.trim();
				sb.append(" and ( o.buyer.realname like ? ");
				params.add("%" + this.querykeyWord + "%");
				sb.append(" or o.buyer.username like ? ");
				params.add("%" + this.querykeyWord + "%"); 
				sb.append(" or o.zhaiQuanCode like ? ");
				params.add("%" + this.querykeyWord + "%"); 
				sb.append(" )"); 
			}
			if (null != this.queryState && !"".equals(queryState)) {
				sb.append(" and ");
				sb.append(" o.state='" + queryState + "'");
			}
			pageView.setQueryResult(buyingService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby));
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "mlist";
	}

	/*
	 * 我的受让记录
	 */
	public String mybuylist() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Buying> pageView = new PageView<Buying>(getShowRecord(),
					getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();

			sb.append(" and ");
			sb.append(" o.createDate >= to_date('"
					+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext)
					+ "','yyyy-MM-dd')");

			if (null != this.queryState && !"".equals(queryState)) {
				sb.append(" and ");
				sb.append(" o.state='" + queryState + "'");
			}

			// 只显示我的出让记录
			sb.append(" and ");
			User user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			sb.append(" o.buyer.id ='" + user.getId() + "'");
			QueryResult<Buying> buys = buyingService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby);
			
			for(Buying b : buys.getResultlist()){
				Contract contract = this.contractService.selectById(" from Contract where buying.id = ? ", b.getId());
				if(contract!=null){
					b.setContract(contract);
				}
			}
			
			
			pageView.setQueryResult(buys);
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "mybuylist";
	}
	/*
	 * 最新的10条求购记录
	 */
	public String newBuyList() {
		try { 
			PageView<Buying> pageView = new PageView<Buying>(recordSum,1);
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("buyingPrice", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();

			sb.append(" and ");
			sb.append(" o.state='0' ");
			sb.append(" and ");
			sb.append(" o.investRecord.id='"+id+"'"); 
			 
			List<Buying> buys = buyingService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params, orderby).getResultlist();
			
			for(Buying b : buys){
				 b.setBuyer(null);
				 b.setContract(null);
				 b.setInvestRecord(null);
			}
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(),buys);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	public List<CommonVo> getStateList() {
		List<CommonVo> tList = new ArrayList<CommonVo>();
		tList.add(new CommonVo("0", "受让中"));
		tList.add(new CommonVo("1", "成功"));
		tList.add(new CommonVo("3", "撤单"));
		tList.add(new CommonVo("4", "系统撤单"));
		return tList;
	}

	/**
	 * 撤销操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upState() throws Exception { 

		try {
			buyingService.cancel(id);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP,
					"受让撤销操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP,
					"受让撤销操作失败");
		}
		return "upState";
	}

	/**
	 * 买入某个的债权
	 * */
	public String buy() {
		buyResult.put(BUY_RESULT, "false");
		buyResult.put(BUY_ERROR_MSG, "");
		InvestRecord investRecord = investRecordService.selectById(zhaiQuanId);
		User buyer = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (buyingService.isAlreadyBought(buyer, investRecord)) {
			buyResult.put(BUY_ERROR_MSG, "你已对此债权下达过受让指令！");
			try {
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(),
						buyResult);
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
			return null;
		}

		Buying buying = new Buying();
		buying.setBuyer(buyer);
		buying.setBuyingPrice(buyingPrice);
		buying.setZqfwf(zqfwf);
		buying.setZqsf(zqsf);

		double realAmount = buyingPrice + zqfwf + zqsf;

		buying.setRealAmount(realAmount);
		buying.setCreateDate(new Date());

		buying.setInvestRecord(investRecord);
		buying.setContract_numbers(investRecord.getContract()
				.getContract_numbers());
		buying.setZhaiQuanCode(investRecord.getZhaiQuanCode());

		try {
			buyingService.saveBuying(buying);
		} catch (Exception e) {

			buyResult.put(BUY_ERROR_MSG, "在试图对此债权下达受让指令时发生了错误！");
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(),
						buyResult);
			} catch (Exception e1) {

				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		buyResult.put(BUY_RESULT, "true");
		try {
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(),
					buyResult);
		} catch (Exception e1) {

			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	public String getQueryState() {
		return queryState;
	}

	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}

	public BuyingService getBuyingService() {
		return buyingService;
	}

	public void setBuyingService(BuyingService buyingService) {
		this.buyingService = buyingService;
	}

	public InvestRecordService getInvestRecordService() {
		return investRecordService;
	}

	public void setInvestRecordService(InvestRecordService investRecordService) {
		this.investRecordService = investRecordService;
	}

	public String getZhaiQuanId() {
		return zhaiQuanId;
	}

	public void setZhaiQuanId(String zhaiQuanId) {
		this.zhaiQuanId = zhaiQuanId;
	}

	public double getZqfwf() {
		return zqfwf;
	}

	public void setZqfwf(double zqfwf) {
		this.zqfwf = zqfwf;
	}

	public double getZqsf() {
		return zqsf;
	}

	public void setZqsf(double zqsf) {
		this.zqsf = zqsf;
	}

	public double getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRecordSum() {
		return recordSum;
	}

	public void setRecordSum(int recordSum) {
		this.recordSum = recordSum;
	}

	public String getQuerykeyWord() {
		return querykeyWord;
	}

	public void setQuerykeyWord(String querykeyWord) {
		this.querykeyWord = querykeyWord;
	}

}
