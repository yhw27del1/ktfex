package com.kmfex.webservice.vo;


/**
 * 担保公司会员担保情况 
 * @author  
 * 2013年07月17日11:14   增加note字段
 */ 
public class MemberGuaranteeVo   {

	private String id; 
	private String guaranteeName;//担保公司名称
	private String tjzs;// 交易中心推荐指数
	/*		
	private String type;// 担保范围及方式
    private String createMoney;// 注册资金
	private String createYear;// 成立年限
	private String empNumber;// 员工人数
	private String jingyanNumber;// 有担保行业工作经历职员人数比率
	private String bank;// 合作银行金融机构
	private String dbedType;// 担保额度级别
	private String jzdType;// 担保余额集中度级别
	private String daichanRate;// 累计代偿率 
	private Date createDate;// 创建时间 
	private String note;// 详细2013-07-17增加
	*/
	public MemberGuaranteeVo() { 
	}
 
	public String getId() {
		return id;
	}

	public String getTjzs() {
		return tjzs;
	}

	public void setTjzs(String tjzs) {
		this.tjzs = tjzs;
	}
	/*
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateMoney() {
		return createMoney;
	}

	public void setCreateMoney(String createMoney) {
		this.createMoney = createMoney;
	}

	public String getCreateYear() {
		return createYear;
	}

	public void setCreateYear(String createYear) {
		this.createYear = createYear;
	}

	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	public String getJingyanNumber() {
		return jingyanNumber;
	}

	public void setJingyanNumber(String jingyanNumber) {
		this.jingyanNumber = jingyanNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getDbedType() {
		return dbedType;
	}

	public void setDbedType(String dbedType) {
		this.dbedType = dbedType;
	}

	public String getJzdType() {
		return jzdType;
	}

	public void setJzdType(String jzdType) {
		this.jzdType = jzdType;
	}

	public String getDaichanRate() {
		return daichanRate;
	}

	public void setDaichanRate(String daichanRate) {
		this.daichanRate = daichanRate;
	}
 

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	*/

	public void setId(String id) {
		this.id = id;
	}

	public String getGuaranteeName() {
		return guaranteeName;
	}

	public void setGuaranteeName(String guaranteeName) {
		this.guaranteeName = guaranteeName;
	}

 

}
