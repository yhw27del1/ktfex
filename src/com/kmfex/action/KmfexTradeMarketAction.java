package com.kmfex.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.KmfexTradeMarket;
import com.kmfex.model.OpenCloseDeal;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.KmfexTradeMarketService;
import com.kmfex.service.OpenCloseDealService;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.struts.BaseAction;

/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class KmfexTradeMarketAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8419521972166829929L;
	@Resource KmfexTradeMarketService kmfexTradeMarketService;
	@Resource OpenCloseDealService openCloseDealService;
	@Resource CoreAccountService coreAccountService;
	@Resource UserParameterService userParameterService;
	private List<KmfexTradeMarket> rules = new ArrayList<KmfexTradeMarket>();
	private KmfexTradeMarket market;
	
	public String rules(){
		rules = this.kmfexTradeMarketService.getCommonListData("from KmfexTradeMarket");
		return "kmfexTradeMarketList";
	}
	
	public String addRules(){
		return "addKmfexTradeMarket";
	}
	
	public String save(){
		try {
			this.kmfexTradeMarketService.insert(this.market);
			return rules();
		} catch (Exception e) {
			return "save_error";
		}
	}
	
	public String setEnable() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			boolean value = Boolean.parseBoolean(request.getParameter("value"));
			KmfexTradeMarket tr = this.kmfexTradeMarketService.selectById(id);
			if(tr!=null){
				if(value){//要将某个规则设置为true，则必须将其他所有的设置为false
					List<KmfexTradeMarket> ls = this.kmfexTradeMarketService.getCommonListData("from KmfexTradeMarket where enabled = true");
					for(KmfexTradeMarket td:ls){
						KmfexTradeMarket u = this.kmfexTradeMarketService.selectById(td.getId());
						u.setEnabled(false);
						u.setEndDate(new Date());
						this.kmfexTradeMarketService.update(u);
					}
				}
				tr.setEnabled(value);
				tr.setStartDate(new Date());
				this.kmfexTradeMarketService.update(tr);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String name = "";

	public String openOrCloseList(){
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
		PageView<OpenCloseDeal> pageView = new PageView<OpenCloseDeal>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		sb.append(" and ");
		sb.append(" o.name in ('开市','休市','清算','对账','开夜市','休夜市') ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('"+ format.format(this.getStartDate()) + "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.openCloseDealService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(),params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "opan_close";
	}
	
	//开市与休市，会调用华夏银行接口
	public String openOrClose() throws Exception{
		HxbankVO vo = new HxbankVO();
		OpenCloseDeal d = new OpenCloseDeal();
		d.setMemo(vo.getTip());
		if("开市".equals(this.name)){
			d = this.openCloseDealService.open_close("开市", this.getLoginUser());
			userParameterService.autoParamOpen();//更新自动投标委托协议的参数和解除协议
		}
		if("休市".equals(this.name)){
			d = this.openCloseDealService.open_close("休市", this.getLoginUser());
		}
		if(null==d){
			vo.setFlag(false);
			vo.setTip("操作失败，请联系管理员。");
		}else{
			vo.setFlag(d.isSuccess());
			if(d.isSuccess()){
				vo.setTip("操作成功");
				if("休市".equals(d.getName())){
					this.coreAccountService.clear();
				}
			}else{
				vo.setTip(d.getMemo());
			}
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//开夜市与休夜市，不会调用华夏银行接口
	public String openOrClose_night() throws Exception{
		HxbankVO vo = new HxbankVO();
		OpenCloseDeal d = new OpenCloseDeal();
		d.setMemo(vo.getTip());
		if("开夜市".equals(this.name)){
			if(this.openCloseDealService.isSuccessDuiZhang()){//最后一条成功记录为“对账”才能开夜市
				d = this.openCloseDealService.open_close_night("开夜市", this.getLoginUser());
			}else{
				d.setSuccess(false);
				d.setMemo("对帐未成功，不能开夜市。");
			}
		}
		if("休夜市".equals(this.name)){
			if(this.openCloseDealService.isSuccessOpenNight()){//最后一条成功记录为“开夜市”才能休夜市
				d = this.openCloseDealService.open_close_night("休夜市", this.getLoginUser());
			}else{
				d.setSuccess(false);
				d.setMemo("未开夜市，不能休夜市。");
			}
		}
		if(null==d){
			vo.setFlag(false);
			vo.setTip("操作失败，请联系管理员。");
		}else{
			vo.setFlag(d.isSuccess());
			if(d.isSuccess()){
				vo.setTip("操作成功");
			}else{
				vo.setTip(d.getMemo());
			}
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}

	public void setRules(List<KmfexTradeMarket> times) {
		this.rules = times;
	}

	public List<KmfexTradeMarket> getRules() {
		return rules;
	}
	public void setMarket(KmfexTradeMarket trade) {
		this.market = trade;
	}

	public KmfexTradeMarket getMarket() {
		return market;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
