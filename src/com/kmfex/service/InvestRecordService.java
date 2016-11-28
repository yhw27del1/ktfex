package com.kmfex.service;

import java.util.List;
import java.util.Map;

import com.kmfex.model.ContractKeyData;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberBase;
import com.kmfex.webservice.vo.MessageTip;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.BaseService;

/**
 * 
 * @author linuxp
 */
public interface InvestRecordService extends BaseService<InvestRecord>{
	//自动委托投标
	public String[] investAuto(MemberBase member, double money, FinancingBase f, int credit);
	//投标 1 投标会员，2 投标金额，3 融资项目id
	public String[] invest(MemberBase member,double money,FinancingBase financingBase,int credit); 
	//新版使用JDBC
	public MessageTip investNew(MemberBase member, double money, FinancingBase f, int credit);
	public MessageTip investNew2(MemberBase member, double money, FinancingBase f, int credit);
	public MessageTip investNew3(MemberBase member, double money, FinancingBase f, int credit) ;
	public double investHistory(MemberBase member,FinancingBase financingBase);
	public double investHistory2(MemberBase member, FinancingBase financingBase);
	
	public QueryResult<Object> groupBy(String wherejpql,String by,List<String> queryParams) throws Exception;
    
	public List<InvestRecord> getInvestRecordListByFinancingId(String id); 
	
	public ContractKeyData  createContract(MemberBase member,double money,FinancingBase financingBase) ;
	/**
	 *  债权转让3位流水号
	 */
	public String buildZqZrCode(FinancingBase financingBase)  throws Exception;
	
	/**
	 * 结束融资项目下的所有债权
	 * @param financingBase
	 * @return
	 */
	public boolean terminal(FinancingBase financingBase);
	
	/**
	 *  根据费用编码查费率
	 * @param costBaseCode 费用项目编码
	 * @return
	 */
	public Double getPercentByCostBaseCode(String costBaseCode);
	
	public MessageTip doInvest(String in_financingBaseId,double money,int credit,String username,String ip);	 
	
	@SuppressWarnings("unchecked")
	public Map callProcedureForInvestInsert(
			final String  in_userName, 
			final String  in_financingBaseId,  
			final String  in_IpStr,  
			final double  in_money,  
			final int  in_credit,  
			final String  in_fromapp,  
			final String  in_userGroupRestrain1id,  
			final double  in_tzfwf,  
			final double  in_fee1,  
			final double  in_lx,  
			final int  in_BusinessTypeTerm,  
			final String  in_Orgcode,  
			final String  in_INVESTOR_ID,  
			final String  in_First_id,  
			final String  in_First_party,  
			final String  in_First_party_code,  
			final double  in_INTEREST_ALLAH_M, 
			final double  in_Interest_rate, 
			final double  in_Payment_method, 
			final double  in_PRINCIPAL_ALLAH_M, 
			final double  in_REPAYMENT_AMOUNT_A, 
			final String  in_Second_party, 
			final String  in_Second_party_code, 
			final String  in_Second_party_yyzz, 
			final String  in_Second_party_zzjg); 
	public Map callProcedureForDocheck(final String  in_userName,	final String  in_financingBaseId,final String in_usertype);
}
