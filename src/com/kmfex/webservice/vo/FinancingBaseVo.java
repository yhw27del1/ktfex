package com.kmfex.webservice.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
/**
 * 融资项目返回对象
 * @author 
 * 2013-11-13 14:39    //辅助缓存新增属性 modifyDate、preInvest、users;
 * 2014-04-08 14:39    //基于我公司风险管理部制定的项目等级评定标准;
 */
public class FinancingBaseVo implements Serializable{  
	private static final long serialVersionUID = 8431777888704203321L;
	private String id;         //融资项目ID
	private String code;       //项目编号
	private String shortName;  //项目简称 
	private Double maxAmount=0d; //融资金额
	private Double currenyAmount=0d;  //当前融资额(已融资额) 
	private Double curCanInvest=0d;   //当前可投标额(剩余融资额)
	
	private Double rate=0d;   //年利率(计算时除100)
	private int term;  //融资期限
	
	private String termStr;  //融资期限 20130709新增字段
	/* 
	 * 利息天数
	 *  20130709新增字段
	 */
	private int interestDay = 0;
	
	
	private String returnPattern;  //还款方式
	
	private int haveInvestNum=0;    //当前已投标人数
	
	private String fxbzState = "1";//风险保障，0表示本金保障，1表示担保公司担保
	private String fxbzStateName;//本金保障，1表示担保公司担保
	
	private String proGress= "0";//投标进度
	private Date  startDate; //投标开始
	private Date  endDate;   //投标截止
	
	
	//辅助缓存新增字段
	private Date modifyDate;  //最新修改时间
	private String preInvest;//1让用户优先投标，0所有人可以投标
	private Set<String> users;//优先的投资人用户名
	
	//private  Double minStart;  //最小起投额
	
	/**公司简称 品牌**/
	private String logoName;//担保公司品牌名称 
	
	//private String purpose;      //信用分析 
	
	private String qyzsLevel;       //融资方评级(字母)
	private String czzsLevel;       //产品评级(字母)
	private String dbzsLevel;       //担保机构评级(字母)
	
	private String qyzs;       //企业信用指数-->20140408改为融资方评级(分值)
	private String fddbzs;       //法定代表人指数-->暂时未用
	private String czzs;       //偿债指数--->改为产品评级(分值)
	private String dbzs;       //担保指数-->改为担保机构评级(分值)
	private String zhzs;     //综合指数-->改为项目总评级(分值)
	private String zhzsStar;     //综合指数图片-->(优质项目，中等项目，合格项目，高风险项目）
	
	private String qyzsNote;       //企业信用指数说明-->改为融资方评级说明
	private String fddbzsNote;       //法定代表人指数说明-->暂时未用
	private String czzsNote;       //偿债指数说明-->改为产品评级说明
	private String dbzsNote;       //担保指数说明-->改为担保机构评级说明
	private String zhzsNote;     //综合指数说明-->改为项目评级说明
	
	private String yongtu;      //用途
	
	
	private String address;      //地区
	private String industry;//行业
	private String CompanyProperty;//公司性质  
	
	private String state="0";   //0待审核  1已审核   2投标中  3部分投标   4已满标   5融资确认已完成  6费用核算完成  7签约完成
	private String stateName;   //状态显示名称
	
	private String financierId;//融资方Id
	private String financierCode;//融资方编号
	private String financierName;//融资方名称
	private String guaranteeId;//担保人Id
	private String guaranteeCode;//担保人编号
	private String guaranteeName;//担保人名称
	
	private String guaranteeNote;//担保情况
	//private String note;      //项目详情
	

	
	private MessageTip messageTip; 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Double getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getReturnPattern() {
		return returnPattern;
	}
	public void setReturnPattern(String returnPattern) {
		this.returnPattern = returnPattern;
	}
	public String getFxbzState() {
		return fxbzState;
	}
	public void setFxbzState(String fxbzState) {
		this.fxbzState = fxbzState;
	}
	public String getProGress() {
		return proGress;
	}
	public void setProGress(String proGress) {
		this.proGress = proGress;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/*public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}*/
	public String getFinancierId() {
		return financierId;
	}
	public void setFinancierId(String financierId) {
		this.financierId = financierId;
	}
	public String getFinancierCode() {
		return financierCode;
	}
	public void setFinancierCode(String financierCode) {
		this.financierCode = financierCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCompanyProperty() {
		return CompanyProperty;
	}
	public void setCompanyProperty(String companyProperty) {
		CompanyProperty = companyProperty;
	}
	public String getGuaranteeId() {
		return guaranteeId;
	}
	public void setGuaranteeId(String guaranteeId) {
		this.guaranteeId = guaranteeId;
	}
	public String getFxbzStateName() {
		return fxbzStateName;
	}
	public void setFxbzStateName(String fxbzStateName) {
		this.fxbzStateName = fxbzStateName;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getGuaranteeCode() {
		return guaranteeCode;
	}
	public void setGuaranteeCode(String guaranteeCode) {
		this.guaranteeCode = guaranteeCode;
	}
	public String getGuaranteeName() {
		return guaranteeName;
	}
	public void setGuaranteeName(String guaranteeName) {
		this.guaranteeName = guaranteeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGuaranteeNote() {
		return guaranteeNote;
	}
	public void setGuaranteeNote(String guaranteeNote) {
		this.guaranteeNote = guaranteeNote;
	}
	/*public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}*/
	public Double getCurrenyAmount() {
		return currenyAmount;
	}
	public void setCurrenyAmount(Double currenyAmount) {
		this.currenyAmount = currenyAmount;
	}
	public Double getCurCanInvest() {
		return curCanInvest;
	}
	public void setCurCanInvest(Double curCanInvest) {
		this.curCanInvest = curCanInvest;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getQyzs() {
		return qyzs;
	}
	public void setQyzs(String qyzs) {
		this.qyzs = qyzs;
	}
	public String getFddbzs() {
		return fddbzs;
	}
	public void setFddbzs(String fddbzs) {
		this.fddbzs = fddbzs;
	}
	public String getCzzs() {
		return czzs;
	}
	public void setCzzs(String czzs) {
		this.czzs = czzs;
	}
	public String getDbzs() {
		return dbzs;
	}
	public void setDbzs(String dbzs) {
		this.dbzs = dbzs;
	}
	public String getZhzs() {
		return zhzs;
	}
	public void setZhzs(String zhzs) {
		this.zhzs = zhzs;
	}
	public String getQyzsNote() {
		return qyzsNote;
	}
	public void setQyzsNote(String qyzsNote) {
		this.qyzsNote = qyzsNote;
	}
	public String getFddbzsNote() {
		return fddbzsNote;
	}
	public void setFddbzsNote(String fddbzsNote) {
		this.fddbzsNote = fddbzsNote;
	}
	public String getCzzsNote() {
		return czzsNote;
	}
	public void setCzzsNote(String czzsNote) {
		this.czzsNote = czzsNote;
	}
	public String getDbzsNote() {
		return dbzsNote;
	}
	public void setDbzsNote(String dbzsNote) {
		this.dbzsNote = dbzsNote;
	}
	public String getZhzsNote() {
		return zhzsNote;
	}
	public void setZhzsNote(String zhzsNote) {
		this.zhzsNote = zhzsNote;
	}
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public int getHaveInvestNum() {
		return haveInvestNum;
	}
	public void setHaveInvestNum(int haveInvestNum) {
		this.haveInvestNum = haveInvestNum;
	}
	public String getYongtu() {
		return yongtu;
	}
	public void setYongtu(String yongtu) {
		this.yongtu = yongtu;
	}
	public String getStateName() { 
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getZhzsStar() { 
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			long sl=Long.parseLong(format.format(this.startDate));
			if(sl<=20140420){
				return "-";
			}
			if(Double.parseDouble(this.zhzs)>=8.75d){ 
				return "Ⅰ级";
			}else if(Double.parseDouble(this.zhzs)<8.75d&&Double.parseDouble(this.zhzs)>=6.25d){
				return "Ⅱ级";
			}else if(Double.parseDouble(this.zhzs)<6.25d&&Double.parseDouble(this.zhzs)>=3.75d){
				return "Ⅲ级";
			}else if(Double.parseDouble(this.zhzs)<3.75d){
				return "Ⅳ级";
			}else{
		    	return "Ⅳ级";
		    }  
		} catch (Exception e) { 
			return "-";
		}  
		
	}
	public void setZhzsStar(String zhzsStar) {
		this.zhzsStar = zhzsStar;
	}
	public String getLogoName() {
		return logoName;
	}
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
	public int getInterestDay() {
		return interestDay;
	}
	public void setInterestDay(int interestDay) {
		this.interestDay = interestDay;
	}
	public String getTermStr() {
		return termStr;
	}
	public void setTermStr(String termStr) {
		this.termStr = termStr;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getPreInvest() {
		return preInvest;
	}
	public void setPreInvest(String preInvest) {
		this.preInvest = preInvest;
	}
	public Set<String> getUsers() {
		return users;
	}
	public void setUsers(Set<String> users) {
		this.users = users;
	}
 
	public String getQyzsLevel() { 
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			long sl=Long.parseLong(format.format(this.startDate));
			if(sl<=20140420){
				return "-";
			}
			
			if("2".equals(qyzs)){ 
				return "A";				 
			}else if("1.75".equals(qyzs)){ 
				return "A-";			 
			}else if("1.5".equals(qyzs)){ 
				return "B";			 
			}else if("1.25".equals(qyzs)){ 
				return "B-";			 
			}else if("1".equals(qyzs)){ 
				return "C";			 
			}else if("0.75".equals(qyzs)){ 
				return "C-";			 
			}else if("0.5".equals(qyzs)){ 
				return "D";			 
			}else{
				return ""; 
			} 
			
		} catch (Exception e) { 
			e.printStackTrace();
			return "-";
		}
	}
	 
	public String getCzzsLevel() { 
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			long sl=Long.parseLong(format.format(this.startDate));
			if(sl<=20140420){
				return "-";
			}
			if("4".equals(czzs)){ 
				return "A";				 
			}else if("3.5".equals(czzs)){ 
				return "A-";			 
			}else if("3".equals(czzs)){ 
				return "B";			 
			}else if("2.5".equals(czzs)){ 
				return "B-";			 
			}else if("2".equals(czzs)){ 
				return "C";			 
			}else if("1.5".equals(czzs)){ 
				return "C-";			 
			}else if("1".equals(czzs)){ 
				return "D";			 
			}else{
				return ""; 
			} 
		} catch (Exception e) { 
			return "-";
		}
	} 
	public String getDbzsLevel() { 
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			long sl=Long.parseLong(format.format(this.startDate));
			if(sl<=20140420){
				return "-";
			}
			if("4".equals(dbzs)){ 
				return "A";				 
			}else if("3.5".equals(dbzs)){ 
				return "A-";			 
			}else if("3".equals(dbzs)){ 
				return "B";			 
			}else if("2.5".equals(dbzs)){ 
				return "B-";			 
			}else if("2".equals(dbzs)){ 
				return "C";			 
			}else if("1.5".equals(dbzs)){ 
				return "C-";			 
			}else if("1".equals(dbzs)){ 
				return "D";			 
			}else{
				return ""; 
			} 
		} catch (Exception e) { 
			return "-";  
		}
	}
	/*public Double getMinStart() {
		return minStart;
	}
	public void setMinStart(Double minStart) {
		this.minStart = minStart;
	}*/
}
