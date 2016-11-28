package com.kmfex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author linuxp
 * 记录会员签约三方存管的历史信息
 * 记录手工会员，由招行专户变更为工行专户的历史信息
 * */
@Entity
@Table(name = "t_signhistory",schema="KT")
public class SignHistory {
	private String id;
	private Date createDate = new Date();
	private String name;
	private String memo;
	
	private Date signDate;//会员签约三方存管成功的日期；或变更成工行专户成功的日期
	private Date synDate_market;//交易所发起预签的日期(交易所不用预签的为空值)
	private Date surrenderDate;//已签约会员解除三方存管签约的日期
	
	private int signBank = 0;//三方存管签约行:未签约三方存管=0,签约华夏三方存管=1,签约招商三方存管=2
	private int signType = 0;//三方存管签约类型:未签约三方存管=0,签约本行=1,签约他行=2
	
	private long owner;//会员id
	
	private boolean success;
	
	private int channel = 0;//现金充值；提现；手工方式的资金渠道；0:无渠道，1:招商银行，2:工商银行
	
	//签约成功时，记录会员的余额及冻结余额
	private boolean signed;//signed=1,则说明为签约成功时或变更专户为工行成功时，以下两个字段记录签约成功时的余额信息
	private double balance;
	private double frozen;
	
	private long operater;//操作者；变更专户为工行记录操作者

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

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getSynDate_market() {
		return synDate_market;
	}

	public void setSynDate_market(Date synDate_market) {
		this.synDate_market = synDate_market;
	}

	public Date getSurrenderDate() {
		return surrenderDate;
	}

	public void setSurrenderDate(Date surrenderDate) {
		this.surrenderDate = surrenderDate;
	}

	@Column(columnDefinition="number(1) default 0")
	public int getSignBank() {
		return signBank;
	}

	public void setSignBank(int signBank) {
		this.signBank = signBank;
	}

	@Column(columnDefinition="number(1) default 0")
	public int getSignType() {
		return signType;
	}

	public void setSignType(int signType) {
		this.signType = signType;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(columnDefinition="number(1) default 0")
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Column(columnDefinition="float default 0")
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Column(columnDefinition="float default 0")
	public double getFrozen() {
		return frozen;
	}

	public void setFrozen(double frozen) {
		this.frozen = frozen;
	}

	@Column(columnDefinition="number(1) default 0")
	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	@Column(columnDefinition="number(1) default 0")
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public long getOperater() {
		return operater;
	}

	public void setOperater(long operater) {
		this.operater = operater;
	}
}