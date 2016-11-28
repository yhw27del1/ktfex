package com.kmfex.webservice.vo;

import java.io.Serializable;
import java.util.Date;

import com.kmfex.model.InvestRecord;
  
/**
 * 债权记录
 * @author     
 */
 
public class ZhaiQuanRecordVo implements Serializable{  
  
	private static final long serialVersionUID = -5445816225723548237L;

	private String investRecordId;  //投标ID 
	
	private double bjlxye = 0; //本息余额 
	
	
	private double maxSellPrice = 0; //最高报价
	
	private String syl;//当前参考收益率
	
	private String symonay;//收益金额(参考收益)
	
	private Date lastDate; //到期日
	
	private String crpiceStr = "";//卖出价
 
	private Date xyhkr;//下一还款日
 
	private String zhaiQuanCode;//债权代码(11位)
	
	private String fxbzStateName;//担保方式
	
	private String returnPattern;  //还款方式 
	
	
	private String financingBasecode;//项目编号
	
	private String userName;  //用户名 
 
	private double bjye = 0;//本金余额
 
	private double lxye = 0;//利息余额
	
	private String contractKeyDataCode; //合同编号
	
	private double crpice = 0;//卖出价
	private String zqzrState;//债权挂牌状态;   
	private String sellingState;//债权状态 
	
	private String param1;//未还款 
	private String param2;//正常还款 
	private String param3;//提前还款
	private String param4;//逾期还款  
	private String param5; //总期数
	
	private double okbj = 0;//最后一次持有人已经收回的本金
	 
	private double oklx = 0;//最后一次持有人已经收回的利息
	
	private double okfee = 0;//最后一次持有人已经收回的罚金 
 
	public String getInvestRecordId() {
		return investRecordId;
	}

	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}
	
	

/*	public String getSyl() {
		try {
			if(this.crpice==0)
				return "-";
			double syltemp=((bjlxye-this.crpice)/this.crpice)*100;
			if(syltemp==0)
				return "-";
			return DoubleUtils.doubleCheck2(syltemp, 2)+"%";
		} catch (Exception e) { 
			e.printStackTrace();
			return "-";
		}
	}*/
	 
	public String getSymonay() {
		return symonay;
	}

	public void setSymonay(String symonay) {
		this.symonay = symonay;
	}

	public String getSyl() {
		return syl;
	}

	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	 
 
	public String getZhaiQuanCode() {
		return zhaiQuanCode;
	}
	public void setZhaiQuanCode(String zhaiQuanCode) {
		this.zhaiQuanCode = zhaiQuanCode;
	}
	 
	  
	public String getZqCnState() {
		if(InvestRecord.STATE_SUCCESS.equals(this.zqzrState)){
			if(InvestRecord.SELLINFSTATE_ALL.equals(this.sellingState)){
				return "出让中";
			}else if(InvestRecord.SELLINFSTATE_SUC.equals(this.sellingState)){
				return "成功";
			}else if(InvestRecord.SELLINFSTATE_BUY.equals(this.sellingState)){
				return "求购中";
			}else if(InvestRecord.SELLINFSTATE_SELL.equals(this.sellingState)){
				return "出让中";
			}else{
				return "持有";
			}
			
		}else if(InvestRecord.STATE_RESTART.equals(this.zqzrState)){
			if(InvestRecord.SELLINFSTATE_ALL.equals(this.sellingState)){
				return "出让中";
			}else if(InvestRecord.SELLINFSTATE_SUC.equals(this.sellingState)){
				return "成功";
			}else if(InvestRecord.SELLINFSTATE_BUY.equals(this.sellingState)){
				return "求购中";
			}else if(InvestRecord.SELLINFSTATE_SELL.equals(this.sellingState)){
				return "出让中";
			}else{
				return "持有";
			}
		}else if(InvestRecord.STATE_STOP.equals(this.zqzrState)){
			return "停牌";
		}else if(InvestRecord.STATE_ZHAI.equals(this.zqzrState)){
			return "摘牌";
		}else{
			return "";
		}  
	}
 
	public void setSyl(String syl) {
		this.syl = syl;
	}
 
	public void setCrpiceStr(String crpiceStr) {
		this.crpiceStr = crpiceStr;
	}
	public String getCrpiceStr() {
		return crpiceStr;
	}
	public double getBjlxye() {
		return bjlxye;
	}
	public void setBjlxye(double bjlxye) {
		this.bjlxye = bjlxye;
	}
	public Date getXyhkr() {
		return xyhkr;
	}
	public void setXyhkr(Date xyhkr) {
		this.xyhkr = xyhkr;
	}
	public String getFxbzStateName() {
		return fxbzStateName;
	}
	public void setFxbzStateName(String fxbzStateName) {
		this.fxbzStateName = fxbzStateName;
	}
	public String getReturnPattern() {
		return returnPattern;
	}
	public void setReturnPattern(String returnPattern) {
		this.returnPattern = returnPattern;
	}
	public String getContractKeyDataCode() {
		return contractKeyDataCode;
	}
	public void setContractKeyDataCode(String contractKeyDataCode) {
		this.contractKeyDataCode = contractKeyDataCode;
	}
	public double getCrpice() {
		return crpice;
	}
	public void setCrpice(double crpice) {
		this.crpice = crpice;
	}
	public String getZqzrState() {
		return zqzrState;
	}
	public void setZqzrState(String zqzrState) {
		this.zqzrState = zqzrState;
	}
	public String getSellingState() {
		return sellingState;
	}
	public void setSellingState(String sellingState) {
		this.sellingState = sellingState;
	}

	public String getFinancingBasecode() {
		return financingBasecode;
	}

	public void setFinancingBasecode(String financingBasecode) {
		this.financingBasecode = financingBasecode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getBjye() {
		return bjye;
	}

	public void setBjye(double bjye) {
		this.bjye = bjye;
	}

	public double getLxye() {
		return lxye;
	}

	public void setLxye(double lxye) {
		this.lxye = lxye;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}
 

	public double getOkbj() {
		return okbj;
	}

	public void setOkbj(double okbj) {
		this.okbj = okbj;
	}

	public double getOklx() {
		return oklx;
	}

	public void setOklx(double oklx) {
		this.oklx = oklx;
	}

	public double getOkfee() {
		return okfee;
	}

	public void setOkfee(double okfee) {
		this.okfee = okfee;
	}
 
	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public double getMaxSellPrice() {
		return maxSellPrice;
	}

	public void setMaxSellPrice(double maxSellPrice) {
		this.maxSellPrice = maxSellPrice;
	}
 
	
}
