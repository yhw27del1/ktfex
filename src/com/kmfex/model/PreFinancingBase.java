package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.wisdoor.core.model.User;


/**
 * 融资项目申请历史表
 * @author
 * * 2013-07-07 15:09    增加interestType   利息方式
 * * 2013-09-16 14:39    新增字段dkType 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_PreFinancing_Base",schema="KT")
public class PreFinancingBase implements Serializable{ 

	private String id;
	
	private String code;       //项目编号
	
	private String shortName;  //项目简称
	
	private MemberBase  financier;    //融资方   memberBase表中memberType的Code为R
	
	
	
	private MemberBase  guarantee;   //担保方     memberType表Code为D
	
	private MemberGuarantee memberGuarantee;//担保方此时间点的担保情况
	
	private BusinessType   businessType;  //业务类型（融资期限）
 
	private Double maxAmount=0d;    //申请的金额   
	
	private Double rate=0d;   //年利率(计算时除100)
	 
	private Date  startDate; //投标开始日期
	private Date  endDate;   //投标截止日期
		
	private User createBy;	//创建人
	private Date createDate; //创建日期
	
	private String purpose; //信用分析
	 
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
	
	private int state=1; //0申请历史记录;1最后一次的申请记录;2已经信用确认,3信用确认驳回,4审核驳回,5废弃
	
	private String guaranteeNote;//担保情况
	
	private String note;         //项目详情
	 
	 
	
	private String fxbzState = "10";////10本息担保 12本金担保   15 无担保   
	
	
	private String opeNote;         //驳回备注    
	
	
	private String flag="0";   //特殊标志   0   普通  1 特殊 (委贷，只运行机构投资人投标)
	
	
 
	
	private String dkType ="1"; //贷款分类(1、抵押贷款，2、信用贷款)
	
	/* 
	 * 利息天数 BusinessType.id='day'按日 其他值按月 
	 */
	private int interestDay = 0;
	
	
	private ContractTemplate contractTemplate;
	
	private String hyType;//行业
	private String qyType;//企业类型(中型企业、小型企业、微型企业)
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}
	 
	public Double getRate() {
		return rate;
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
 
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
 
	public Double getMaxAmount() {
		return maxAmount;
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
	public String getFxbzState() {
		return fxbzState;
	}
	public void setFxbzState(String fxbzState) {
		this.fxbzState = fxbzState;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	} 
	@Lob   
	@Type(type="text")         
	@Column(name="purpose_")
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
 
	public Double getPreMaxAmount() {
		return preMaxAmount;
	}
	public void setPreMaxAmount(Double preMaxAmount) {
		this.preMaxAmount = preMaxAmount;
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
	public String getOpeNote() {
		return opeNote;
	}
	public void setOpeNote(String opeNote) {
		this.opeNote = opeNote;
	}
 
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
    private String fxbzStateName;//本金保障，1表示担保公司担保
 // 不固化,辅助 
	private String dkTypeName;  //
	 
	@Transient 
	public String getDkTypeName() {
		switch (Integer.parseInt(getDkType())) {
		
		   case 1:
			   return "抵押贷"; 
		   case 2:
		       return "信用贷";		    
		   default: 
			   return "";
		}
	}
	
	public void setDkTypeName(String dkTypeName) {
		this.dkTypeName = dkTypeName;
	}
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
	
	@ManyToOne
	@JoinColumn(name="c_t_id")
	public ContractTemplate getContractTemplate() {
		return contractTemplate;
	}
	public void setContractTemplate(ContractTemplate contractTemplate) {
		this.contractTemplate = contractTemplate;
	}
 
	@Column(columnDefinition="number(10) default 0")
	public int getInterestDay() {
		return interestDay;
	}
	public void setInterestDay(int interestDay) {
		this.interestDay = interestDay;
	}
	public String getDkType() {
		return dkType;
	}
	public void setDkType(String dkType) {
		this.dkType = dkType;
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
	
	
}
