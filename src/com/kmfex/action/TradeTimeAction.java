package com.kmfex.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.TradeTime;
import com.kmfex.service.TradeTimeService;
import com.wisdoor.struts.BaseAction;

/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class TradeTimeAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8419521972166829929L;
	@Resource TradeTimeService tradeTimeService;
	private List<TradeTime> times = new ArrayList<TradeTime>();
	private TradeTime trade;
	
	public String tradeTimeList(){
		times = this.tradeTimeService.getCommonListData("from TradeTime");
		return "tradeTimeList";
	}
	
	public String addTradeTime(){
		return "addTradeTime";
	}
	
	public String save(){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		try {
			this.trade.setAm_start_time(format.parse(this.trade.getAm_start()));
			this.trade.setAm_end_time(format.parse(this.trade.getAm_end()));
			this.trade.setPm_start_time(format.parse(this.trade.getPm_start()));
			this.trade.setPm_end_time(format.parse(this.trade.getPm_end()));
		} catch (ParseException e1) {
			this.addActionError("交易时间格式错误，正确的格式为 时间:分钟，比如 09:00");
			return "save_error";
		}
		try {
			this.tradeTimeService.insert(this.trade);
			return tradeTimeList();
		} catch (Exception e) {
			return "save_error";
		}
	}
	
	public String setEnable() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String id = request.getParameter("id");
			boolean value = Boolean.parseBoolean(request.getParameter("value"));
			TradeTime tr = this.tradeTimeService.selectById(id);
			if(tr!=null){
				if(value){//要将某个规则设置为true，则必须将其他所有的设置为false
					List<TradeTime> ls = this.tradeTimeService.getCommonListData("from TradeTime where enabled = true");
					for(TradeTime td:ls){
						TradeTime u = this.tradeTimeService.selectById(td.getId());
						u.setEnabled(false);
						this.tradeTimeService.update(u);
					}
				}
				tr.setEnabled(value);
				this.tradeTimeService.update(tr);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setTimes(List<TradeTime> times) {
		this.times = times;
	}

	public List<TradeTime> getTimes() {
		return times;
	}
	public void setTrade(TradeTime trade) {
		this.trade = trade;
	}

	public TradeTime getTrade() {
		return trade;
	}
}
