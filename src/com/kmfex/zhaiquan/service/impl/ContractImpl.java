package com.kmfex.zhaiquan.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.service.ContractService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.vo.CommonVo;

@Service
public class ContractImpl extends BaseServiceImpl<Contract> implements
		ContractService {

	@Resource InvestRecordService investRecordService;
	@Resource PaymentRecordService paymentRecordService;
	
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public PageView<Contract> listByConditions(String sellerId, String buyerId,
			Date sellerDate, Date buyerDate, Date endDate, int pageNo,
			int pageSize) {
		PageView<Contract> pageView = new PageView<Contract>(pageSize, pageNo);

		StringBuilder sb = new StringBuilder("1=1 ");
		List<String> params = new ArrayList<String>();
		if (null != sellerId) {
			sb.append("and seller.id=? ");
			params.add(sellerId);
		}
		if (null != buyerId) {
			sb.append("and buyer.id=? ");
			params.add(buyerId);
		}
		
		// if (null != sellerDate) {
		// sb.append("and to_char(sellerDate,'yyyy-mm-dd hh24:MM:ss')=? ");
		// params.add("to_char(" + dateFormat.format(sellerDate)
		// + ",'yyyy-mm-dd hh24:MM:ss') ");
		// }
		// if (null != buyerDate) {
		// sb.append("and to_char(buyerDate,'yyyy-mm-dd hh24:MM:ss')=? ");
		// params.add("to_char(" + dateFormat.format(buyerDate)
		// + ",'yyyy-mm-dd hh24:MM:ss') ");
		// }
		
		if (null != endDate) {
			sb.append("and to_char(endDate,'YYYY-MM-DD')=? ");
			params.add("to_char('" + dateFormat.format(endDate)
					+ "','YYYY-MM-DD') ");
		}
		try {
			pageView.setQueryResult(getScrollData(pageView.getFirstResult(),
					pageView.getMaxresult(), sb.toString(), params));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pageView;
	}

	
	
	
	
	public static void main(String[] args){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println(dateFormat.format(new Date()));
		
		
		
	}





	@Override
	public String getNextContractCode(InvestRecord ir, User user) throws Exception {
		String result = null;
		String ircode = ir.getZhaiQuanCode();
		String username = user.getUsername();
		long count = this.getScrollDataCount("from Contract where xieyiCode like ?", ircode+"-"+username+"-%");
		count++;
		if( count > 9 ){
			result =  ircode+"-"+username+"-"+count;
		}else{
			result =  ircode+"-"+username+"-"+"0"+count;
		}
		
		return result;
	}
	public Contract getByInvestRecordAndUser(InvestRecord ir,User buyer) throws Exception{
		String hql = "from Contract o where o.investRecord.id='"+ir.getId()+"' and o.buyer.id='"+buyer.getId()+"' ";
		List<Contract> cont = this.getCommonListData(hql);
		if(null!=cont && cont.size()==1){
			return cont.get(0);
		}else{
			return null;
		}
	}
	public Contract getLastByInvestRecord(String investRecordId) throws Exception{
		String hql = "from Contract o where o.investRecord.id='"+investRecordId+"' order by o.buyerDate desc ";
		List<Contract> cont = this.getCommonListData(hql);
		if(null!=cont && cont.size()==1){  
			return cont.get(0);
		}else{
			return null;
		}
	}
 
	public CommonVo selectByDateTime(String investRecordId,String userId) throws Exception { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Contract ct=getLastByInvestRecord(investRecordId);
			String hql = "";
			List<PaymentRecord> prs=null;
			if(null==ct){
				hql = "from PaymentRecord where state!='0' and  investRecord.id = ? ";
				prs=paymentRecordService.getScrollDataCommon(hql, investRecordId); 
			}else{
				hql = "from PaymentRecord where state!='0' and  investRecord.id = ? and to_char(predict_repayment_date,'yyyy-mm-dd hh24:mi:ss') >= ? and beneficiary_id='"+userId+"'";
				prs=paymentRecordService.getScrollDataCommon(hql,new String[]{investRecordId, sdf.format(ct.getBuyerDate())});    
			} 
			
	 
			Double db1=0d;
			Double db2=0d;
			Double db3=0d;
 	        for(PaymentRecord pr:prs){
 	        	db1+=pr.getShbj();//本-已收回
 	        	db2+=pr.getShlx();//息-已收回
 	        	db3+=pr.getPenal();//罚金收入 
	        }
 	       
 	        CommonVo vo=new CommonVo();
	 	    vo.setDb1(db1);
	 	    vo.setDb2(db2);
	 	    vo.setDb3(db3);
			return vo;
	} 
}
