package com.kmfex.model.hx;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author linuxp
 * */
@Entity
@Table(name = "hx_bank_deal_sub",schema="KT")
public class HxbankDealSub {
	private String id;
	private Date createDate = new Date();
	private String accountNo;//子账号
	private String merAccountNo;//摊位号
	private String accountName;//子账户名称
	//出入金明细核对
	private String bankTxSerNoHis;//银行交易流水号
	private String merTxSerNoHis;//交易市场交易流水号
	private String trnxCodeHis;//历史交易请求代码
	private String trnxType;//出入金类型:0出金；1入金
	private String trnxType_show;//辅助字段
	//清算
	private String type;//交易类型:01为正常交易(仅对子账号进行借贷)，02为冻结资金
	private String flag;//借贷标识:1为‘借’,2位‘贷’；type为02时必须输入，但不验证字段值
	private String type_show;//辅助字段
	private String flag_show;//辅助字段
	private String remark;//备注
	//对账
	private double amt = 0.0;//发生额,余额,出金金额,入金金额等
	private double amtUse = 0.0;//可用余额
	private double bankAmt = 0.0;//银行余额
	private double bankAmtUse = 0.0;//银行可用余额
	//子账户签约
	private String dealerOperNo;//登录银行系统操作员代码
	private String errorInfo;//错误信息
	
	private HxbankDeal parent;
	
	private String direction;//request表示请求数据，response表示响应数据
	private String direction_show;
	
	private Date checkDate;
	private long user_id;
	private int bishu = 0;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setParent(HxbankDeal parent) {
		this.parent = parent;
	}
	@ManyToOne
	public HxbankDeal getParent() {
		return parent;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getMerAccountNo() {
		return merAccountNo;
	}
	public void setMerAccountNo(String merAccountNo) {
		this.merAccountNo = merAccountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankTxSerNoHis() {
		return bankTxSerNoHis;
	}
	public void setBankTxSerNoHis(String bankTxSerNoHis) {
		this.bankTxSerNoHis = bankTxSerNoHis;
	}
	public String getMerTxSerNoHis() {
		return merTxSerNoHis;
	}
	public void setMerTxSerNoHis(String merTxSerNoHis) {
		this.merTxSerNoHis = merTxSerNoHis;
	}
	public String getTrnxCodeHis() {
		return trnxCodeHis;
	}
	public void setTrnxCodeHis(String trnxCodeHis) {
		this.trnxCodeHis = trnxCodeHis;
	}
	public String getTrnxType() {
		return trnxType;
	}
	public void setTrnxType(String trnxType) {
		this.trnxType = trnxType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public double getAmtUse() {
		return amtUse;
	}
	public void setAmtUse(double amtUse) {
		this.amtUse = amtUse;
	}
	public double getBankAmt() {
		return bankAmt;
	}
	public void setBankAmt(double bankAmt) {
		this.bankAmt = bankAmt;
	}
	public double getBankAmtUse() {
		return bankAmtUse;
	}
	public void setBankAmtUse(double bankAmtUse) {
		this.bankAmtUse = bankAmtUse;
	}
	public String getDealerOperNo() {
		return dealerOperNo;
	}
	public void setDealerOperNo(String dealerOperNo) {
		this.dealerOperNo = dealerOperNo;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	@Column(columnDefinition="varchar2(10) default 'response'")
	public String getDirection() {
		return direction;
	}
	public void setType_show(String type_show) {
		this.type_show = type_show;
	}
	@Transient
	public String getType_show() {
		if("01".equals(type.trim())){
			type_show = "正常交易";
		}
		if("02".equals(type.trim())){
			type_show = "冻结资金";
		}
		return type_show;
	}
	public void setFlag_show(String flag_show) {
		this.flag_show = flag_show;
	}
	@Transient
	public String getFlag_show() {
		if("1".equals(flag.trim())){
			flag_show = "借";
		}
		if("2".equals(flag.trim())){
			flag_show = "贷";
		}
		return flag_show;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setTrnxType_show(String trnxType_show) {
		this.trnxType_show = trnxType_show;
	}
	@Transient
	public String getTrnxType_show() {
		if("0".equals(this.trnxType)){
			trnxType_show = "出金";
		}
		if("1".equals(this.trnxType)){
			trnxType_show = "入金";
		}
		return trnxType_show;
	}
	public void setDirection_show(String direction_show) {
		this.direction_show = direction_show;
	}
	
	@Transient
	public String getDirection_show() {
		if("request".equals(this.direction)){
			direction_show = "市场请求";
		}
		if("response".equals(this.direction)){
			direction_show = "银行响应";
		}
		return direction_show;
	}
	public void setBishu(int bishu) {
		this.bishu = bishu;
	}
	@Column(columnDefinition="number(10) default 0")
	public int getBishu() {
		return bishu;
	}
	
}
