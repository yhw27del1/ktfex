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
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
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
 * 出让信息
 * 
 * @author  ,aora
 * 修改记录  
 * 2013年5月30日16:32   修改mlist()方法,根据关键字模糊查询修正；
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class SellingAction extends BaseAction {
	@Resource
	SellingService sellingService;
	@Resource ContractService contractService;
	public String queryState;
	private String querykeyWord = "";
	private String id;
	/**
	 * 标记：债权转让结果
	 */
	private final static String SELL_RESULT = "sellResult";
	/**
	 * 标记：债权转让出错消息
	 */
	private final static String SELL_ERROR_MSG = "sellErrorMsg";

	/**
	 * 债权转让结果
	 */
	private Map<String, String> sellResult = new HashMap<String, String>();

	/**
	 * 债权id号
	 */
	private String zhaiQuanId;
	/**
	 * 债权转让服务费
	 */
	private double zqfwf;

	/**
	 * 代扣税费
	 */
	private double zqsf;

	/**
	 * 债权人叫价
	 */
	private double price;

	@Resource
	InvestRecordService investRecordService;

	/*
	 * 后台管理列表
	 */
	public String mlist() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Selling> pageView = new PageView<Selling>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.querykeyWord.trim(); 
				sb.append(" and ( o.seller.realname like ? ");
				params.add("%" + this.querykeyWord + "%");
				sb.append(" or o.seller.username like ? ");
				params.add("%" + this.querykeyWord + "%");
				sb.append(" or o.zhaiQuanCode like ? ");
				params.add("%" + this.querykeyWord + "%"); 
				sb.append(" )"); 
			}
			if (null != this.queryState && !"".equals(queryState)) {
				sb.append(" and ");
				sb.append(" o.state='" + queryState + "'");
			}
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			pageView.setQueryResult(sellingService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "mlist";
	}

	/*
	 * 我的出让记录
	 */
	public String myselllist() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<Selling> pageView = new PageView<Selling>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			if (null != this.querykeyWord && !"".equals(querykeyWord)) {
				querykeyWord = this.querykeyWord.trim();

			}
			if (null != this.queryState && !"".equals(queryState)) {
				sb.append(" and ");
				sb.append(" o.state='" + queryState + "'");
			}
			sb.append(" and ");
			sb.append(" o.createDate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.createDate <= to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			// 只显示我的出让记录
			sb.append(" and ");
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			sb.append(" o.seller.id ='" + user.getId() + "'");
			QueryResult<Selling> sells = sellingService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			
			for(Selling s : sells.getResultlist()){
				Contract contract = this.contractService.selectById(" from Contract where selling.id = ? ", s.getId());
				if(contract!=null){
					s.setContract(contract);
				}
			}
			pageView.setQueryResult(sells);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "myselllist";
	}

	/**
	 * 转让自己的债权
	 */
	public String sell() {
		sellResult.put(SELL_RESULT, "false");
		sellResult.put(SELL_ERROR_MSG, "");
		InvestRecord investRecord = investRecordService.selectById(zhaiQuanId);
		User seller = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (sellingService.isAlreadySold(seller, investRecord)) {
			sellResult.put(SELL_ERROR_MSG, "您已对此债权下达过出让指令！");
			try {
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(), sellResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		Selling selling = new Selling();

		selling.setSeller(seller);
		selling.setSellingPrice(price);
		selling.setZqfwf(zqfwf);
		selling.setZqsf(zqsf);

		double realAmount = price - zqfwf -zqsf;

		selling.setRealAmount(realAmount);
		selling.setCreateDate(new Date());

		selling.setInvestRecord(investRecord);
		selling.setContract_numbers(investRecord.getContract().getContract_numbers());
		selling.setZhaiQuanCode(investRecord.getZhaiQuanCode());
		try {
			sellingService.saveSelling(selling);
			sellResult.put(SELL_RESULT, "true");
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), sellResult);
		} catch (Exception ex) {
			sellResult.put(SELL_RESULT, "false");
			sellResult.put(SELL_ERROR_MSG, "在试图下达出让指令时发生了错误！");
			ex.printStackTrace();
			try {
				DoResultUtil.doObjectResult(ServletActionContext.getResponse(), sellResult);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		return null;
	}

	/**
	 * 撤销操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upState() throws Exception {

		try {
			sellingService.cancel(id);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "出让撤销操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "出让撤销操作失败");
		}
		return "upState";
	}

	public List<CommonVo> getStateList() {
		List<CommonVo> tList = new ArrayList<CommonVo>();
		tList.add(new CommonVo("0", "出让中"));
		tList.add(new CommonVo("1", "成功"));
		tList.add(new CommonVo("3", "撤单"));
		tList.add(new CommonVo("4", "系统撤单"));
		return tList;
	}

	public String getQueryState() {
		return queryState;
	}

	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}

	public String getQuerykeyWord() {
		return querykeyWord;
	}

	public void setQuerykeyWord(String querykeyWord) {
		this.querykeyWord = querykeyWord;
	}

	public Map<String, String> getSellResult() {
		return sellResult;
	}

	public void setSellResult(Map<String, String> sellResult) {
		this.sellResult = sellResult;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
