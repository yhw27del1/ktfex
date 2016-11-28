package com.kmfex.zhaiquan.action;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.service.InvestRecordService;
import com.kmfex.zhaiquan.model.ZQBuySellRule;
import com.kmfex.zhaiquan.service.ZQBuySellRuleService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;


////

@Controller
@SuppressWarnings("serial")
@Scope("prototype")
public class BuySellRuleAction extends BaseAction implements Preparable {
     @Resource ZQBuySellRuleService zqbuysellruleService;
     private ZQBuySellRule zqbuysellrule;
     @Resource InvestRecordService investRecordService;
     private String id;
    //
     
     /**
 	 * 新增
 	 * 
 	 * @return
 	 * @throws Exception
 	 */
 	public String ui() throws Exception {
 		return "ui";
 	}
         
     public String save() throws Exception{ 
		 if(id!=null&&!"".equals(id)){
			  zqbuysellruleService.update(this.zqbuysellrule);
		  }else {
			  zqbuysellruleService.insert(this.zqbuysellrule); 			
		 }
 
		 //更新债权行情时间点  
	     Map<Integer, Object> inParamList=new HashMap<Integer, Object>();  
	     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     inParamList.put(1, format.format(new Date())); 
	     zqbuysellruleService.callProcedure("p_modifyDateForZqRuleChange", inParamList) ;
    	 return "save";
 	}
     
     public String list() throws Exception{
 		PageView<ZQBuySellRule> pageView = new PageView<ZQBuySellRule>(getShowRecord(),getPage());
 		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
 		orderby.put("createDate", "desc");
 		pageView.setQueryResult(zqbuysellruleService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),  orderby));
 		ServletActionContext.getRequest().setAttribute("pageView", pageView);
 		return "list";
 	}
     
     
     public String setEnable() {
 		try {
 			HttpServletRequest request = ServletActionContext.getRequest();
 			String id = request.getParameter("id");
 			boolean value = Boolean.parseBoolean(request.getParameter("value"));
 			ZQBuySellRule zq = this.zqbuysellruleService.selectById(id);
 			if(zq!=null){				
 				zq.setEnable(value);
 				if (value){
 					zq.setEndDate(null);
 				}else{
 					zq.setEndDate(new Date());
 				} 
 				  zqbuysellruleService.update(zq);
 				 //更新债权行情时间点  
 			     Map<Integer, Object> inParamList=new HashMap<Integer, Object>();  
 			     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			     inParamList.put(1, format.format(new Date())); 
 			     zqbuysellruleService.callProcedure("p_modifyDateForZqRuleChange", inParamList) ;
 			}
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
 	}
  
     @Override
 	public void prepare() throws Exception {
 		if (this.id == null) {
 			this.zqbuysellrule = new ZQBuySellRule();
 		} else {
 			this.zqbuysellrule = this.zqbuysellruleService.selectById(id);
 		}
 	}

	public ZQBuySellRule getZqbuysellrule() {
		return zqbuysellrule;
	}

	public void setZqbuysellrule(ZQBuySellRule zqbuysellrule) {
		this.zqbuysellrule = zqbuysellrule;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	} 
}
