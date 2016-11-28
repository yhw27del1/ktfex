package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.wisdoor.core.model.User;

/**
 * 担保公司会员担保情况 
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Member_Guarantee", schema = "KT")
public class MemberGuarantee implements Serializable {

	public static final String STATE_HISTORY = "0";
	public static final String STATE_LATEST = "1";

	private String id;
	private MemberBase memberBase;

	private String tjzs="0";// 交易中心推荐指数
	private String type;// 担保范围及方式
	private String createMoney;// 注册资金
	private String createYear;// 成立年限
	private String empNumber;// 员工人数
	private String jingyanNumber;// 有担保行业工作经历职员人数比率
	private String bank;// 合作银行金融机构
	private String dbedType;// 担保额度级别
	private String jzdType;// 担保余额集中度级别
	private String daichanRate;// 累计代偿率
	private User createUser;// 创建人
	private String state = STATE_LATEST;// 状态表示：0，历史记录；1，最新记录
	private Date createDate = new Date();// 创建时间
	
	
	private String note; //详情

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public String getTjzs() {
		return tjzs;
	}

	public void setTjzs(String tjzs) {
		this.tjzs = tjzs;
	}

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

	@ManyToOne
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne
	public MemberBase getMemberBase() {
		return memberBase;
	}

	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
	
}
