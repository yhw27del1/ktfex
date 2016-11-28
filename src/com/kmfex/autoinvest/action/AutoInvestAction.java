package com.kmfex.autoinvest.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.autoinvest.vo.AutoInvestConstants;
import com.kmfex.autoinvest.vo.Draw;
import com.kmfex.autoinvest.vo.PreParams;
import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberBase;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.kmfex.util.SMSNewUtil;
import com.kmfex.util.SMSUtil;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.VelocityUtils;
import com.wisdoor.struts.BaseAction;

@Controller
@Scope("prototype")
public class AutoInvestAction extends BaseAction{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 151649610173051691L;
	private String id;
	private PreParams pre = null;
	private PreParams preParams=new PreParams();
	@Resource
    OpenCloseDealService openCloseDealService;
	@Resource
	FinancingBaseService financingBaseService; 
	@Resource
	UserPriorityService userPriorityService;
	@Resource
	UserParameterService userParameterService; 
	@Resource
	LogsService logsService;	
	@Resource
	FinancingCacheService financingCacheService;
	public String listForFinancingId() {
		return "financingId";
	}
	
	/**
	 * state in ('1','1.5')
	 * 输出JSON
	 */
	public void financing_data(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			StringBuilder subsql = new StringBuilder(" 1=1 ");

 			subsql.append(" and autoinvest='1' "); 
 			
 			subsql.append(" and state in (?,?,?,?)"); 
			args_list.add("1");
			args_list.add("1.5");
			args_list.add("2");
			args_list.add("3");   
			
			if(StringUtils.isNotBlank(this.getKeyWord())) {
				this.setKeyWord(this.getKeyWord().trim());
				subsql.append(" and ");
				subsql.append(" instr(financecode,? ) > 0 ");
				args_list.add(this.getKeyWord());
			}

			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String whos() throws Exception {
		if(null!=this.id && !"".equals(this.id)){
			Map<String, Object> map = this.userParameterService.drawByFinancingId(this.id);
			ServletActionContext.getRequest().setAttribute("n", map.get("n"));
			ServletActionContext.getRequest().setAttribute("w", map.get("w"));
			ServletActionContext.getRequest().setAttribute("sum_balance", map.get("sum_balance"));
			ServletActionContext.getRequest().setAttribute("sum_balance_zq", map.get("sum_balance_zq"));
			ServletActionContext.getRequest().setAttribute("hkfs", map.get("hkfs"));
			ServletActionContext.getRequest().setAttribute("fxpj", map.get("fxpj"));
			ServletActionContext.getRequest().setAttribute("dbfs", map.get("dbfs"));
			ServletActionContext.getRequest().setAttribute("qx", map.get("qx"));
			ServletActionContext.getRequest().setAttribute("nll", map.get("nll"));
			
			ServletActionContext.getRequest().setAttribute("list", map.get("draws"));
		}
		return "whos";
	}
	
	public String draws() throws Exception {
		return "draws";
	}
	
	public void draws_data() throws Exception {
		DecimalFormat df = new DecimalFormat("#0.00");
		ServletActionContext.getRequest().getSession().setAttribute(AutoInvestConstants.AUTOINVESTS_STRING, null);
		if(null!=this.id && !"".equals(this.id)){
			FinancingBase fb = this.financingBaseService.selectById(this.id);
			if(null!=fb){
				Map<String, Object> map = this.userParameterService.drawByFinancingId(this.id);
				List<Draw> result = this.userPriorityService.draw1(map, fb.getCurCanInvest());
				JSONArray object = JSONArray.fromObject(result);
				JSONObject o = new JSONObject();
				o.element("total", result.size());
				o.element("rows", object);
				JSONArray footer = new JSONArray();
				JSONObject _totalDate = new JSONObject();
				double zj = 0d;
				for(Draw d:result){
					zj += d.getPrePrice();
				}
				_totalDate.element("max", "合计");
				_totalDate.element("info", "融资项目:"+fb.getCode()+",融资总额:"+df.format(fb.getMaxAmount())+",已融资额:"+df.format(fb.getCurrenyAmount())+",剩余融资额:"+df.format(fb.getCurCanInvest()));
				_totalDate.element("prePrice", zj);
				footer.add(_totalDate);
				o.element("footer", footer);
				ServletActionContext.getResponse().getWriter().write(o.toString());
				
				ServletActionContext.getRequest().getSession().setAttribute(AutoInvestConstants.AUTOINVESTS_STRING, result);

			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public String autoInvests() throws Exception {
		try {  
			//t1 = System.currentTimeMillis();
			byte state = this.openCloseDealService.checkState(); 
			if (state != 2) {
			       DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"flag\":\"0\",\"message\":\"交易市场未休市!\"}");
					return null;
			}  
			if(null!=this.id && !"".equals(this.id)){
				FinancingBase financingBase = this.financingBaseService.selectById(this.id);
				if(null!=financingBase){
					if ("0".equals(financingBase.getState())) {  
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"暂不允许投标!\"}");
						return null;
					}
					if ("1".equals(financingBase.getState())) {  
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"暂不允许投标!\"}");
						return null;
					}
					if ("1.5".equals(financingBase.getState())) {  
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"暂不允许投标!\"}");
						return null;
					}
					
					if ("4".equals(financingBase.getState())) { 
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"融资已满标 !\"}");
						return null;
					}
					if ("5".equals(financingBase.getState())) { 
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"融资已确认!\"}");
						return null;
					}
					if ("6".equals(financingBase.getState())) { 
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"融资已核算!\"}");
						return null;
					}
					if ("7".equals(financingBase.getState())) { 
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"融资已核算!\"}");
						return null;
					}
					List<Draw> result =(List<Draw>)ServletActionContext.getRequest().getSession().getAttribute(AutoInvestConstants.AUTOINVESTS_STRING); 
					ServletActionContext.getRequest().getSession().setAttribute(AutoInvestConstants.AUTOINVESTS_STRING, null);
					if(null!=result&&result.size()>0){
						
						//成交抽中的投资人
						this.userPriorityService.autoInvest(result, financingBase); 
						
						//从生成的投标记录中取人发自动投标成功短信
						sendsms(financingBase); 
						
						//(账户可用余额-设置的最小金额)<1000元。
						send1000sms(financingBase);
						
					}else{
						DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						"{\"flag\":\"0\",\"message\":\"无中奖人!\"}");
						return null;
					} 
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"flag\":\"1\",\"message\":\"自动投标成功!\"}");
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return null;
	}
	 /**
	  * 投标成功，冻结金额时
	  * @param financingBase
	  */
	 private void sendsms(FinancingBase financingBase){ 
			StringBuilder whereBuilder = new StringBuilder(" 1=1 ");  
			whereBuilder.append(" and fb.id=ir.financingbase_id and bt.id=fb.businesstype_id ");   
			whereBuilder.append(" and ir.fromapp='autoinvest' and ir.investor_id=mb.id ");   
			whereBuilder.append(" and ir.financingbase_id='"+financingBase.getId()+"'");   
			try {
				List<Map<String, Object>> result = this.userPriorityService.queryForList("ir.fromapp,ir.investamount,mb.category,mb.pmobile,mb.emobile,mb.econtactmobile,ir.financingbase_id,fb.code,fb.rate,fb.interestday,bt.term"," T_INVEST_RECORD ir,t_member_base mb,t_financing_base fb,t_business_type bt ",whereBuilder.toString(),null,false);
 				String telNo =""; 
			    for(Map<String,Object> up:result){ 
					String category = up.get("category").toString(); 
					if((MemberBase.CATEGORY_PERSON).equals(category))
						telNo=up.get("pmobile")==null?"":up.get("pmobile").toString();
					else{
						telNo=up.get("emobile")==null?"":up.get("emobile").toString();  
					} 
					String term="";
					if (null != up.get("interestday")){ 
						if("0".equals(up.get("interestday").toString())){
							term=up.get("term").toString()+"月";
						}else{
							term=up.get("interestday").toString()+"天";
						}
					}   
					double investamount = Double.parseDouble(up.get("investamount").toString());
 					VelocityContext context = new VelocityContext();
					SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日"); 
					context.put("endDate", format.format(new Date())); 
					context.put("money", DoubleUtils.doubleCheck(investamount, 2));
					context.put("code", up.get("code").toString());
					context.put("rate", up.get("rate").toString());
					context.put("term", term);
					String content  = VelocityUtils.getVelocityString(context, "tzr_autoinvest.html");
					if(BaseTool.isNotNull(telNo)&&BaseTool.isNotNull(content)){
						SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
						SMSNewUtil.sms(telNo, content, formats.format(new Date()), "","1");
						//老的短信接口不再使用2014-06-30
						//SMSUtil.sms(telNo, content);  
					}
				} 
			} catch (Exception e) { 
				e.printStackTrace();
			} 
	 }	
	 
	 /**
	  * (账户可用余额-设置的最小金额)<1000元
	  * @param financingBase
	  */
	 private void send1000sms(FinancingBase financingBase){ 
		 StringBuilder whereBuilder = new StringBuilder(" 1=1 ");  
		 whereBuilder.append(" and ir.fromapp='autoinvest' and ir.investor_id=mb.id and au.member_id=mb.id ");   
		 whereBuilder.append(" and ap.userparameter_id=au.id and su.id=mb.user_id and sa.accountid_=su.useraccount_accountid_ ");   
		 whereBuilder.append(" and (sa.balance_-au.param8_)<1000 "); //param8账户可用余额不低于
		 whereBuilder.append(" and ir.financingbase_id='"+financingBase.getId()+"'");   
		 try {
	         List<Map<String, Object>> result = this.userPriorityService.queryForList("ir.investamount,mb.category,mb.pmobile,mb.emobile,mb.econtactmobile,sa.balance_,au.param8_,au.param9_"," T_INVEST_RECORD ir,t_member_base mb,sys_user su,sys_account sa,auto_userparameter au,auto_userpriority ap ",whereBuilder.toString(),null,false);
 			 String telNo =""; 
			 for(Map<String,Object> up:result){ 
				 String category = up.get("category").toString(); 
				 if((MemberBase.CATEGORY_PERSON).equals(category))
					 telNo=up.get("pmobile")==null?"":up.get("pmobile").toString();
				 else{
					 telNo=up.get("emobile")==null?"":up.get("emobile").toString();  
				 } 
				 VelocityContext context = new VelocityContext(); 
				 String content  = VelocityUtils.getVelocityString(context, "tzr_autofinish.html");
				 if(BaseTool.isNotNull(telNo)&&BaseTool.isNotNull(content)){
					 SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
					 SMSNewUtil.sms(telNo, content, formats.format(new Date()), "","1");
					 //老的短信接口不再使用2014-06-30
					 //SMSUtil.sms(telNo, content);  
				 }
			 } 
		 } catch (Exception e) { 
			 e.printStackTrace();
		 } 
	 }	
	 
	/**
	 * 委托投标的包对外投标
	 * @return
	 * @throws Exception
	 */
	public String autoInvestOpen() throws Exception {
/*		byte state = this.openCloseDealService.checkState(); 
		if (state != 2) {
		       DoResultUtil.doStringResult(ServletActionContext.getResponse(),
				"{\"flag\":\"0\",\"message\":\"交易市场未休市!\"}");
				return null;
		}  */
		try {
			if(null!=this.id && !"".equals(this.id)){
				FinancingBase financingBase = this.financingBaseService.selectById(this.id);
				if(null!=financingBase){ 
					financingBase.setAutoinvest("0"); 
					financingBase.setModifyDate(new Date()); 
					Logs log0 = logsService.log("委托投标融资项目对外开放");// 日志
					//this.financingBase.getLogs().add(log);// 日志
					log0.setEntityId(financingBase.getId());
					log0.setEntityFrom("FinancingBase");
					this.logsService.insert(log0);  
					financingBaseService.update(financingBase);
					//更新融资项目缓存  
					try { ReadsStaticConstantPropertiesUtil.updateServiceCache(financingBase.getId()); } catch (Exception e) {}
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"flag\":\"1\",\"message\":\"委托投标融资项目对外开放操作处理成功\"}");
					
				}else{
					DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"flag\":\"0\",\"message\":\"融资项目不存在\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					"{\"flag\":\"0\",\"message\":\"自动投标的包对外开放异常\"}");
		}
		return null;
	}
	public String listForPreParamsId() {
		return "preParamsId";
	}
	
	public void pre_data() {
		if(null!=this.pre){
			try {
				Map<String, Object> map = this.userParameterService.drawByPreParams(this.pre,null);
				ServletActionContext.getRequest().setAttribute("n", map.get("n"));
				ServletActionContext.getRequest().setAttribute("w", map.get("w"));
				ServletActionContext.getRequest().setAttribute("sum_balance", map.get("sum_balance"));
				ServletActionContext.getRequest().setAttribute("sum_balance_zq", map.get("sum_balance_zq"));
				JSONObject o = new JSONObject();
				o.element("rows", map.get("draws"));
				o.element("n", map.get("n"));
				o.element("w", map.get("w"));
				o.element("sum_balance", map.get("sum_balance"));
				o.element("sum_balance_zq", map.get("sum_balance_zq"));
				ServletActionContext.getResponse().getWriter().write(o.toString());
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PreParams getPreParams() {
		return preParams;
	}

	public void setPreParams(PreParams preParams) {
		this.preParams = preParams;
	}

	public PreParams getPre() {
		return pre;
	}

	public void setPre(PreParams pre) {
		this.pre = pre;
	}
}
