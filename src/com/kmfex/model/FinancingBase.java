package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.wisdoor.core.model.User;


/**
 * 确认后的融资项目基本信息
 * @author    
 * 
 * <pre>
 * 2012-08-15  修改状态为8已取消的注释说明
 * 2012-8-31    增加modifyDate   挂单、确认、撤单、签约操作时更新该字段
 * 2013-06-07 15:09    增加preInvest   开启优先投标
 * 2013-07-05 11:09    增加interestDay   利息天数
 * 2013-07-25 13:09    发布融资信息 新增状态，state=1.5 代表待挂单 
 * 2013-09-13 14:29    jysc 不固化,交易时长
 * 2013-09-16 14:39    新增字段dkType,qyType,hyType
 * 2013-10-23 14:39    不固化,优先投标的组信息groupStr
 * 2013-11-13 14:39    logs增加fetch=FetchType.EAGER 
 * 2014-04-03 09:18    删除日志关联 
 * 20140423  增加字段ii_fee_bl 记录交易手续费比例
 * 
 * 20150508  增加最小投标额
 * </pre>
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Financing_Base",schema="KT")

public class FinancingBase implements Serializable{ 

	private String id;
	private int version=0;
	
	private String code;       //项目编号  
	private String shortName;  //项目简称
	
	private MemberBase  financier;    //融资方   memberBase表中memberType的Code为R
	
	private MemberBase  guarantee;   //担保方     memberType表Code为D
	private MemberGuarantee memberGuarantee;//担保方此时间点的担保情况
	
	private BusinessType   businessType;  //业务类型（融资期限）
	
	private String purpose;      //信用分析--
	
	private String yongtu;      //用途
	
	private String qyzs="0";       //企业信用指数-->20140408改为融资方标准
	private String fddbzs="0";       //法定代表人指数 
	private String czzs="0";       //偿债指数--->20140408改为产品标准
	private String dbzs="0";       //担保指数-->20140408改为担保机构标准
	private String zhzs="0";     //综合指数-->20140408改为项目总评分
	
	
	private String qyzsNote;       //企业信用指数说明-->20140408改为融资方评分说明
	private String fddbzsNote;       //法定代表人指数说明
	private String czzsNote;       //偿债指数说明-->20140408改为产品评分说明
	private String dbzsNote;       //担保指数说明-->20140408改为担保机构评分说明
	private String zhzsNote;     //综合指数说明-->20140408改为项目评分说明
	
	
	private Double preMaxAmount=0d;    //申请的金额
	private Double maxAmount=0d;    //可融资额(总融资额)--信用分析确认的金额
	private String maxAmount_daxie;
	private Double currenyAmount=0d;  //当前融资额(已融资额)     初始为0
	private String currenyAmount_daxie;
	
	private Double rate=0d;   //年利率(计算时除100)
	
	private Double curCanInvest=0d;   //当前可投标额(剩余融资额)
	private String curCanInvest_daxie;
	private int haveInvestNum=0;    //当前已投标人数
	private Date  startDate; //投标开始日期
	private Date  endDate;   //投标截止日期
		
	private User createBy;	//创建人
	private Date createDate; //创建日期
	private User auditBy;   //审核人
	private Date auditDate;  //审核日期
	 
	private String state="0";   //0待审核  1已审核  1.5已发布信息     2投标中  3投标中(部分投标)   4已满标   5融资确认已完成  6费用核算完成  7签约完成 8已撤单
	
	private boolean terminal = false;//是否还完全部的款
	private Date terminal_date;//完成最后一次还款时间
	
	private Date finishtime;//核算完成时间
	private String guaranteeNote;//担保情况
	private String note;      //项目详情
	
	private boolean showOk=false;//是否显示融资确认按钮
	
	private String dilivery = "0";//交割标志，0表示待交割，1表示已经交割
 
	//最新 ：10本息担保12本金担保 15无担保
	private String fxbzState = "10";//>风险保障
	/**
	 * 还款记录 不固化
	 */
	private LinkedHashMap<PayMGroup,List<PaymentRecord>> paymentrecords;
	/**
	 * 不固化,融资项目费用
	 */
	private FinancingCost fc;
	/**
	 * 不固化,辅助投标记录记录投标来源--web手机端投标辅助用
	 */	
	private String fromApp;//m表示来自手机客户端 其他来自pc
	
	/**交易时长**/
	private long jysc;
	
	/**
	 * 投标记录 不固化
	 */
	private List<InvestRecord> investrecords;
 
	
	private Date qianyueDate;//融资方签约时间
	private Date daoqiDate;//到期日期
	
	private Date modifyDate;  //最新修改时间
	
	private ContractTemplate contractTemplate;
	
	
	//是否允许债权转让  
	private boolean enableZq = true;
	
	/**
	 * 是否已经融资提现
	 * 
	 */
	private boolean txed = false;  
	
	/*
	 *针对某些用户开启优先投标
	 *优先的用户维护在表(FinancingRestrain)
	 */
	private int preInvest = 0;//1让用户优先投标，0所有人可以投标
 
	/* 
	 * 利息天数
	 */
	private int interestDay = 0;
	

	private String hyType;//行业
	
	
	private String qyType;//企业类型(中型企业、小型企业、微型企业)
	
	
	private String dkType;//贷款分类(1、抵押贷款，2、信用贷款)
	
	private Double minStart=100d;  
	
	
	
	@Column(length=10)
	private String autoinvest="0";//是否参与自动委托投标(1参与委托自动投标，0正常投标)
	

	/**不固化,优先投标的组信息**/
	private String groupStr;
	
	/**
	 * 交易手续费比例
	 */
	private double ii_fee_bl = 0.00;
	private List<Map<String, Object>> logs;
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Version
	//@OptimisticLock(excluded=true)
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getCode() {
		return code;
	}
	public String getShortName() {
		return shortName;
	}
	
	@ManyToOne
	public MemberBase getFinancier() {
		return financier;
	}
	@ManyToOne
	public MemberBase getGuarantee() {
		return guarantee;
	} 
	
	@ManyToOne
	public MemberGuarantee getMemberGuarantee() {
		return memberGuarantee;
	}
	public void setMemberGuarantee(MemberGuarantee memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}
	@Lob   
	@Type(type="text")         
	@Column(name="purpose_")
	public String getPurpose() {
		return purpose;
	}
	public Double getMaxAmount() {
		return maxAmount;
	}
	public Double getCurrenyAmount() {
		return currenyAmount;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setFinancier(MemberBase financier) {
		this.financier = financier;
	}
	public void setGuarantee(MemberBase guarantee) {
		this.guarantee = guarantee;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public void setCurrenyAmount(Double currenyAmount) {
		this.currenyAmount = currenyAmount;
	}
	public Double getRate() {
		return rate;
	}
	public Double getCurCanInvest() {
		return curCanInvest;
	}
	public int getHaveInvestNum() {
		return haveInvestNum;
	}
	@ManyToOne 
	public User getCreateBy() {
		return createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public void setCurCanInvest(Double curCanInvest) {
		this.curCanInvest = curCanInvest;
	}
	public void setHaveInvestNum(int haveInvestNum) {
		this.haveInvestNum = haveInvestNum;
	}
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	@ManyToOne
	public User getAuditBy() {
		return auditBy;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public String getState() {
		return state;
	}
	// 不固化,辅助
	private String hyTypeShow;
	private String qyTypeShow;
	private String dkTypeShow; 
	
	@Transient 
	public String getHyTypeShow() {
		if (null != this.getHyType() && !"".equals(this.getHyType())) {
			return this.getHyType().substring(0,this.getHyType().indexOf("("));
		}else{
			return "";
		}
		
	}
	@Transient 
	public String getQyTypeShow() {
		if (null != this.getQyType() && !"".equals(this.getQyType())) {
			return this.getQyType().substring(0,this.getQyType().indexOf("("));
		}else{
			return "";
		}
	}
	@Transient 
	public String getDkTypeShow() {
		if (null != this.getDkType() && !"".equals(this.getDkType())) {
			return this.getDkType().substring(0,this.getDkType().indexOf("("));
		}else{
			return "";
		}
	}
	

	@Transient 
	public String getStateName() { 
		 try {
			 if("0".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "待审核";				 
			 }else if("1".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已审核 ";			 
			 }else if("1.5".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已发布信息";			 
			 }else if("2".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "投标中";		 
			 }else if("3".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "部分投标";	  		 
			 }else if("4".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已满标"; 
			 }else if("5".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已确认";	 	 
			 }else if("6".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已确认"; 		 
			 }else if("7".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已签约";	 
			 }else if("8".equals(state)){
				   if(terminal){
					   return "还款结束";
				   }
			       return "已撤单"; 
			 }else{
				   return ""; 
			 }
			 
			/*switch (Integer.parseInt(state)) {
			   case 0:
				   if(terminal){
					   return "还款结束";
				   }
			       return "待审核";
			   case 1:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已审核 ";	
			   case 2:
				   if(terminal){
					   return "还款结束";
				   }
			       return "投标中";
			   case 3:
				   if(terminal){
					   return "还款结束";
				   }
			       return "部分投标";	  
			   case 4:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已满标";
			   case 5:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已确认";	  
			   case 6:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已确认"; 
			   case 7:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已签约";
			   case 8:
				   if(terminal){
					   return "还款结束";
				   }
			       return "已撤单";
			   default:  
				   return "待挂单"; 
				   
			  }*/
		} catch (Exception e) { 
			 e.printStackTrace();
			 return "";
		}
	}
 
	@Transient 
	public String getQyzsShow() { 
		try {
			if("2".equals(qyzs)){ 
				return "A(2分)";				 
			}else if("1.75".equals(qyzs)){ 
				return "A-(1.75分)";			 
			}else if("1.5".equals(qyzs)){ 
				return "B(1.5分)";			 
			}else if("1.25".equals(qyzs)){ 
				return "B-(1.25分)";			 
			}else if("1".equals(qyzs)){ 
				return "C(1分)";			 
			}else if("0.75".equals(qyzs)){ 
				return "C-(0.75分)";			 
			}else if("0.5".equals(qyzs)){ 
				return "D(0.5分)";			 
			}else{
				return ""; 
			} 
			
		} catch (Exception e) { 
			e.printStackTrace();
			return "";
		}
	}
	 
	@Transient 
	public String getCzzsShow() { 
		try {
			if("4".equals(czzs)){ 
				return "A(4分)";				 
			}else if("3.5".equals(czzs)){ 
				return "A-(3.5分)";			 
			}else if("3".equals(czzs)){ 
				return "B(3分)";			 
			}else if("2.5".equals(czzs)){ 
				return "B-(2.5分)";			 
			}else if("2".equals(czzs)){ 
				return "C(2分)";			 
			}else if("1.5".equals(czzs)){ 
				return "C-(1.5分)";			 
			}else if("1".equals(czzs)){ 
				return "D(1分)";			 
			}else{
				return ""; 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
			return "";
		}
	}
	@Transient 
	public String getDbzsShow() { 
		try {
			if("4".equals(dbzs)){ 
				return "A(4分)";				 
			}else if("3.5".equals(dbzs)){ 
				return "A-(3.5分)";			 
			}else if("3".equals(dbzs)){ 
				return "B(3分)";			 
			}else if("2.5".equals(dbzs)){ 
				return "B-(2.5分)";			 
			}else if("2".equals(dbzs)){ 
				return "C(2分)";			 
			}else if("1.5".equals(dbzs)){ 
				return "C-(1.5分)";			 
			}else if("1".equals(dbzs)){ 
				return "D(1分)";			 
			}else{
				return ""; 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
			return "";
		}
	}
	
	public void setState(String state) {
		this.state = state;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setAuditBy(User auditBy) {
		this.auditBy = auditBy;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@ManyToOne
	public BusinessType getBusinessType() {
		return businessType;
	}
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}
	@Lob   
	@Type(type="text")         
	@Column(name="guaranteeNote_")
	public String getGuaranteeNote() {
		return guaranteeNote;
	}
	public void setGuaranteeNote(String guaranteeNote) {
		this.guaranteeNote = guaranteeNote;
	}
    @Lob   
	@Type(type="text")         
	@Column(name="note_")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	} 
	@Column(columnDefinition="number default 0")
	public long getJysc() {
		return jysc;
	}
	public void setJysc(long jysc) {
		this.jysc = jysc;
	}
	@Transient
	public boolean isShowOk() {
		return showOk;
	}
	public void setShowOk(boolean showOk) {
		this.showOk = showOk;
	}
	public void setMaxAmount_daxie(String maxAmount_daxie) {
		this.maxAmount_daxie = maxAmount_daxie;
	}
	@Transient
	public String getMaxAmount_daxie() {
		return maxAmount_daxie;
	}
	public void setCurrenyAmount_daxie(String currenyAmount_daxie) {
		this.currenyAmount_daxie = currenyAmount_daxie;
	}
	@Transient
	public String getCurrenyAmount_daxie() {
		return currenyAmount_daxie;
	}
	public void setCurCanInvest_daxie(String curCanInvest_daxie) {
		this.curCanInvest_daxie = curCanInvest_daxie;
	}
	@Transient
	public String getCurCanInvest_daxie() {
		return curCanInvest_daxie;
	}
	public void setDilivery(String dilivery) {
		this.dilivery = dilivery;
	}
	public String getDilivery() {
		return dilivery;
	}
 
	public String getFxbzState() {
		return fxbzState;
	} 
	
	public void setFxbzState(String fxbzState) {
		this.fxbzState = fxbzState;
	}
	public Double getPreMaxAmount() {
		return preMaxAmount;
	}
	public void setPreMaxAmount(Double preMaxAmount) {
		this.preMaxAmount = preMaxAmount;
	}
	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}
	public Date getFinishtime() {
		return finishtime;
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
	public String getYongtu() {
		return yongtu;
	}
	public void setYongtu(String yongtu) {
		this.yongtu = yongtu;
	}
	@Transient
	public LinkedHashMap<PayMGroup, List<PaymentRecord>> getPaymentrecords() {
		return paymentrecords;
	}
	public void setPaymentrecords(
			LinkedHashMap<PayMGroup, List<PaymentRecord>> paymentrecords) {
		this.paymentrecords = paymentrecords;
	}

	public boolean isTerminal() {
		return terminal;
	}
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	@Transient
	public FinancingCost getFc() {
		return fc;
	}
	public void setFc(FinancingCost fc) {
		this.fc = fc;
	}
	public void setQianyueDate(Date qianyueDate) {
		this.qianyueDate = qianyueDate;
	}
	public Date getQianyueDate() {
		return qianyueDate;
	}
	public void setDaoqiDate(Date daoqiDate) {
		this.daoqiDate = daoqiDate;
	}
	public Date getDaoqiDate() {
		return daoqiDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Date getModifyDate() {
		return modifyDate;
		
	}
	private String fxbzStateName;//本金保障，1表示担保公司担保
	
	@Transient
	public String getFxbzStateName() {
		switch (Integer.parseInt(getFxbzState())) {
		  
		   case 10:
		       return "本息担保";  
		   case 12:
			   return "本金担保";
		   case 15:
		       return "无担保";
		   default: 
			   return "";
		}
	} 
	public void setFxbzStateName(String fxbzStateName) {
		this.fxbzStateName = fxbzStateName;
	}
	public Date getTerminal_date() {
		return terminal_date;
	}
	public void setTerminal_date(Date terminal_date) {
		this.terminal_date = terminal_date;
	}
	@ManyToOne
	@JoinColumn(name="c_t_id")
	public ContractTemplate getContractTemplate() {
		return contractTemplate;
	}
	public void setContractTemplate(ContractTemplate contractTemplate) {
		this.contractTemplate = contractTemplate;
	}
	public boolean isEnableZq() {
		return enableZq;
	}
	public void setEnableZq(boolean enableZq) {
		this.enableZq = enableZq;
	}
	@Column(columnDefinition="number(1) default 0")
	public boolean isTxed() {
		return txed;
	}
	public void setTxed(boolean txed) {
		this.txed = txed;
	}
	public void setInvestrecords(List<InvestRecord> investrecords) {
		this.investrecords = investrecords;
	}
	@Transient
	public List<InvestRecord> getInvestrecords() {
		return investrecords;
	}
	
	@Transient
	public String getFromApp() {
		return fromApp;
	}
	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}
	@Column(columnDefinition="number(2) default 0")
	public int getPreInvest() {
		return preInvest;
	}
	public void setPreInvest(int preInvest) {
		this.preInvest = preInvest;
	}
 
	@Column(columnDefinition="number(10) default 0")
	public int getInterestDay() {
		return interestDay;
	}
	public void setInterestDay(int interestDay) {
		this.interestDay = interestDay;
	}
	public String getHyType() {
		return hyType;
	}
	public void setHyType(String hyType) {
		this.hyType = hyType;
	}
	public String getQyType() {
		return qyType;
	}
	public void setQyType(String qyType) {
		this.qyType = qyType;
	}
	public String getDkType() {
		return dkType;
	}
	public void setDkType(String dkType) {
		this.dkType = dkType;
	}
	public String getGroupStr() {
		return groupStr;
	}
	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}
	@Transient
	public List<Map<String, Object>> getLogs() {
		return logs;
	}
	public void setLogs(List<Map<String, Object>> logs) {
		this.logs = logs;
	}
	@Transient
	public String getZhzsLevel() { 
		if(Double.parseDouble(this.zhzs)>=8.75d){ 
			return "优质";
		}else if(Double.parseDouble(this.zhzs)<8.75d&&Double.parseDouble(this.zhzs)>=6.25d){
			return "中等";
		}else if(Double.parseDouble(this.zhzs)<6.25d&&Double.parseDouble(this.zhzs)>=3.75d){
			return "合格";
		}else if(Double.parseDouble(this.zhzs)<3.75d){
			return "高风险";
		}else{
	    	return "高风险";
	    }  
	}
	@Column(columnDefinition="number(5,4) default 0")
	public double getIi_fee_bl() {
		return ii_fee_bl;
	}
	public void setIi_fee_bl(double ii_fee_bl) {
		this.ii_fee_bl = ii_fee_bl;
	}
	
	public String getAutoinvest() {
		if(null==autoinvest){ autoinvest="0";}
		return autoinvest;
	}
	public void setAutoinvest(String autoinvest) {
		this.autoinvest = autoinvest;
	}
	
	public Double getMinStart() {
		return minStart;
	}
	public void setMinStart(Double minStart) {
		this.minStart = minStart;
	}
	
}
