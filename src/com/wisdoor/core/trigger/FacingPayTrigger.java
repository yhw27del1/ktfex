package com.wisdoor.core.trigger;
 
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.VelocityContext;

import com.kmfex.model.BusinessType;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingCost;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.util.SMSNewUtil;
import com.kmfex.util.SMSUtil;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.utils.VelocityUtils;

public class FacingPayTrigger extends TransactionBase {
	private static  FinancingBaseService financingBaseService; 
	private static  PaymentRecordService paymentRecordService; 
	private static  FinancingCostService financingCostService;  

	/** 
	 * 修改记录  
	 * 2013年06月04日13:32   修改excute()方法,特殊情况处理(如果保证金按月还款的修正)；
	 */	
	@Override
	public void excute() throws Exception { 
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
	 	    Calendar c = Calendar.getInstance();
	 	    c.add(Calendar.DATE, 5);   
	 	    
			StringBuilder sb = new StringBuilder("select ");
			String sqlGroup = " o.investRecord.financingBase.id";
			sb.append(sqlGroup);
			sb.append("  ,sum(o.xybj) as bj,sum(o.xylx) as lx");
			sb.append("  from PaymentRecord o");
			sb.append("  where   o.state='0'  ");
			sb.append("  and  o.predict_repayment_date= to_date('"+ format.format(c.getTime()) + "','yyyy-MM-dd')");     
			sb.append(" group by ").append(sqlGroup);
			
			QueryResult<Object> qr = paymentRecordService.groupHqlQuery(sb.toString());
			if (null != qr.getResultlist() && qr.getResultlist().size() > 0) {
				for (Object arr : qr.getResultlist()) {
					Object[] o = (Object[]) arr;
					if (null != o[1]) { 
						FinancingBase fb=financingBaseService.selectById(o[0].toString());
						if(fb.getCode().startsWith("X") || fb.getCode().startsWith("G")) continue;
						//发送还款提醒短信  
			 			try {
							VelocityContext context = new VelocityContext();
							format =new SimpleDateFormat("yy年MM月");  
							context.put("createDate", format.format(c.getTime()));   
							Double money=0d;//总的融资额  
							BusinessType businessType=fb.getBusinessType();
							int times=businessType.getReturnTimes();
							
							FinancingCost fc = financingCostService.selectById(" from FinancingCost where financingBase.id = ? ", new Object[]{fb.getId()});
					    
							if(fb.getCode().startsWith("X")){ 
								if(fc.getFee1_tariff()!=0){money += fc.getFee1()/times; }
								if(fc.getFee2_tariff()!=0){money += fc.getFee2()/times; } 
								if(fc.getFee3_tariff()!=0){money += fc.getFee3()/times; }
	 						}else{
	                            if(fc.getDbf_tariff()!=0){
	                            	money+=fc.getDbf()/times;
	                            }
	                            if(fc.getFxglf_tariff()!=0){
	                            	money+=fc.getFxglf()/times;
	                            }
	                            if(fc.getRzfwf_tariff()!=0){
	                            	money+=fc.getRzfwf()/times;
	                            } 							
								
							} 
							//保证金
                            if(fc.getBzj_tariff()!=0){
                            	money+=fc.getBzj()/times;
                            } 	
							context.put("money", String.format("%.2f",Double.parseDouble(o[1].toString())+Double.parseDouble(o[2].toString())+money)+""); 
							
							format1=new SimpleDateFormat("MM月dd日"); 
							context.put("huankuanDate", format1.format(c.getTime()));
							
							String content ="";
							content=VelocityUtils.getVelocityString(context,"rzr_huankuan.html"); 
							SimpleDateFormat format2 =new SimpleDateFormat("yyyyMMddHHmmss");    
							 
							if(isMobileNO(fb.getFinancier().getMobile())){//验证是否是正确的手机号码
								SMSNewUtil.sms(fb.getFinancier().getMobile(), content.trim(), format2.format(new Date()), "","1");
								//老的短信接口不再使用2014-06-30
								//SMSUtil.sms(fb.getFinancier().getMobile(), content.trim(),format2.format(new Date()),"","1");
							}
						
			 			} catch (Exception e) {  
			 				e.printStackTrace();
						}  
					}
				}
	    }
 	  
  
	}
	public static boolean isMobileNO(String mobiles){     
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
	        Matcher m = p.matcher(mobiles);     
	        return m.matches();     
	 } 
	   
	@Override
	public void init() throws Exception {
		financingBaseService = (FinancingBaseService)wac.getBean("financingBaseImpl");  
		paymentRecordService = (PaymentRecordService)wac.getBean("paymentRecordImpl");  
		financingCostService = (FinancingCostService)wac.getBean("financingCostImpl"); 
		
	}
 
	

}
