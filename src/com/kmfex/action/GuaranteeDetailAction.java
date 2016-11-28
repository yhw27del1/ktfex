package com.kmfex.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.model.ViewPaymentRecord;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PaymentRecordService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.User;
import com.wisdoor.struts.BaseAction;

/**
 * 担保公司、担保费明细查询（已还、未还）
 * @author   
 * http://www.knowsky.com/365589.html 分页  
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class GuaranteeDetailAction extends BaseAction {

	@Resource
	PaymentRecordService paymentRecordService;
	@Resource
	MemberBaseService memberBaseService;  
	@Resource
	JdbcTemplate jdbcTemplate;
	private String keyWord = "";  
	private double sum = 0d; 
	private double dbfSum = 0d;
	private double dbfSumFj = 0d; 
	
	private double dbfSum0 = 0d;
	private double dbfSumFj0 = 0d; 
	
	private double dbfSum1 = 0d;
	private double dbfSumFj1 = 0d; 
	
	private double dbfSum2 = 0d;
	private double dbfSumFj2 = 0d; 
	
	private double dbfSum3 = 0d;
	private double dbfSumFj3 = 0d; 
	
	private String id="0";
	private String state="4";
	/**
	 * 担保费用（已还、未还）明细
	 */
	public String list() throws Exception {

		checkDate();
		
		
		if("0".equals(id)){
			return "list";
		}
		try {
			StringBuilder sb = new StringBuilder("select ");
			
			String groupStr=" qs,financbaseid,financbasecode,fshortname,qianyuedate,yhdate,dbhsname,state ";
			sb.append(groupStr); 
			sb.append(",sum(shfee2) as fee2,sum(fj2) as fj2 ");
			sb.append("  from  v_paymentrecord "); 
			
			sb.append("  where ");
			sb.append("    dbhsname is not null "); 

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			  MemberBase member =null;  
			if (null != u)
				org = u.getOrg();
			    member = this.memberBaseService.getMemByUser(u);
			if (null != org) {
				orgCode = org.getCoding();
			}
			if(null!=member){
			   if(MemberType.CODE_SECURED.equals(member.getMemberType().getCode()))//担保公司登陆
			   { 
				    sb.append(" and dbgsmemberid='"+ member.getId() + "' ");
				   }else {
					sb.append(" and dbgsmemberid='@@@@@@@' ");
				} 
			} 
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" financbasecode like '%" + keyWord + "%'");
				sb.append(" or ");
				sb.append(" fshortname like '%" + keyWord + "%'");
				sb.append(" or ");
				sb.append(" dbhsname like '%" + keyWord + "%'   ");
				sb.append(" )"); 
			} 
			 
			if(!"4".contains(state)){
				sb.append("and state='"+state+"'  "); 	
			} 
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
 
			if(null != this.getStartDate() && !"".equals(this.getStartDate()) ){
				sb.append(" and qianyuedate >= to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')");
			}
			if( null != this.getEndDate() && !"".equals(this.getEndDate())){
				sb.append(" and qianyuedate <= to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')+1");
			}	
			
			sb.append(" group by ").append(groupStr+" order by qs ");
			List<Map<String,Object>> rows = jdbcTemplate.queryForList(sb.toString()); 
			//统计总金额
			dbfSum = 0d;dbfSumFj= 0d; 
			dbfSum0 = 0d;dbfSumFj0= 0d;
			dbfSum1 = 0d;dbfSumFj1= 0d;
			dbfSum2 = 0d;dbfSumFj2= 0d;
			dbfSum3 = 0d;dbfSumFj3= 0d;
			List<ViewPaymentRecord> vrs=new ArrayList<ViewPaymentRecord>();
			for(int i=0;i<rows.size();i++){ 
			   Map<String,Object> userMap=rows.get(i);  
			   String state=userMap.get("state").toString();
			   Double fee2=((BigDecimal)userMap.get("fee2")).doubleValue();
			   Double fj2=((BigDecimal)userMap.get("fj2")).doubleValue();
			   Date   qianyuedate=(Date)userMap.get("qianyuedate");
			   Date   yhdate=(Date)userMap.get("yhdate"); 
			   String succession=userMap.get("qs").toString();
			   String financbaseid=userMap.get("financbaseid").toString();
			   String financbasecode=userMap.get("financbasecode").toString();
			   String fshortname=userMap.get("fshortname").toString(); 
			   String dbhsname=userMap.get("dbhsname").toString();  
			   vrs.add(new ViewPaymentRecord(state, yhdate, succession, fee2, fj2,
						financbaseid, financbasecode, fshortname,
						qianyuedate,dbhsname)); 
				if("0".equals(state)){
					dbfSum0+=fee2;
					dbfSumFj0+=fj2;  
				}else if("1".equals(state)){
					dbfSum1+=fee2;
					dbfSumFj1+=fj2;  
				}else if("2".equals(state)){
					dbfSum2+=fee2;
					dbfSumFj2+=fj2;  
				}else if("3".equals(state)){
					dbfSum3+=fee2;
					dbfSumFj3+=fj2;  
				}  

			}
   
			sum = 0d; 
			dbfSum=dbfSum0+dbfSum1+dbfSum2+dbfSum3;
			dbfSumFj=dbfSumFj0+dbfSumFj1+dbfSumFj2+dbfSumFj3;
			sum=dbfSum+dbfSumFj;
			
			ServletActionContext.getRequest().setAttribute("vrs", vrs);
		} catch (RuntimeException e) { 
			e.printStackTrace();
		}
		
		 
		return "list";
	}

	public double getDbfSum() {
		return dbfSum;
	}

	public void setDbfSum(double dbfSum) {
		this.dbfSum = dbfSum;
	}
 
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	 

	public double getSum() {
		return sum;
	}

	public double getDbfSumFj() {
		return dbfSumFj;
	}

	public void setDbfSumFj(double dbfSumFj) {
		this.dbfSumFj = dbfSumFj;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDbfSum0() {
		return dbfSum0;
	}

	public void setDbfSum0(double dbfSum0) {
		this.dbfSum0 = dbfSum0;
	}

	public double getDbfSumFj0() {
		return dbfSumFj0;
	}

	public void setDbfSumFj0(double dbfSumFj0) {
		this.dbfSumFj0 = dbfSumFj0;
	}

	public double getDbfSum1() {
		return dbfSum1;
	}

	public void setDbfSum1(double dbfSum1) {
		this.dbfSum1 = dbfSum1;
	}

	public double getDbfSumFj1() {
		return dbfSumFj1;
	}

	public void setDbfSumFj1(double dbfSumFj1) {
		this.dbfSumFj1 = dbfSumFj1;
	}

	public double getDbfSum2() {
		return dbfSum2;
	}

	public void setDbfSum2(double dbfSum2) {
		this.dbfSum2 = dbfSum2;
	}

	public double getDbfSumFj2() {
		return dbfSumFj2;
	}

	public void setDbfSumFj2(double dbfSumFj2) {
		this.dbfSumFj2 = dbfSumFj2;
	}

	public double getDbfSum3() {
		return dbfSum3;
	}

	public void setDbfSum3(double dbfSum3) {
		this.dbfSum3 = dbfSum3;
	}

	public double getDbfSumFj3() {
		return dbfSumFj3;
	}

	public void setDbfSumFj3(double dbfSumFj3) {
		this.dbfSumFj3 = dbfSumFj3;
	}
 
}
