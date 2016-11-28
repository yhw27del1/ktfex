package com.kmfex.autoinvest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kmfex.model.MemberBase;

/**
 * 委托投标----用户参数设置
 * @author    
 * 时间格式：yyyyMMddHHmmss 
 */ 
@Entity
@Table(name = "Auto_UserParameter",schema="KT")
public class UserParameter implements Serializable{
 
	private static final long serialVersionUID = 7350917437622762931L;

	private String id; 
 
	private MemberBase member;//规则制定人 
	
	@Column(name="param1_",length=200)
	private String param1;//风险评级，多个逗号相隔
	
 
 	@Column(name="param2_",length=100)
	private String param2;//还款方式，多个逗号相隔
 	
	@Column(name="dayMin_")
	private long dayMin=0;//按天最小值
	
	@Column(name="dayMax_")
	private long dayMax=0;//按天最大值
	
	@Column(name="monthMin_")
	private long monthMin=0;//按月最小值
	
	@Column(name="monthMax_")
	private long monthMax=0;//按月最大值
	
	@Column(name="param5_",length=50)
	private String param5;//担保方式多个逗号相隔
	
	@Column(name="param6_", nullable = true,precision=12, scale=2) 
	private Double param6=0.00;//按月年利率(不低于)
	
	@Column(name="param7_", nullable = true,precision=12, scale=2)
	private Double param7=0.00;//按天年利率(不低于)  
	
	
	private Double param8=0.00;//账户可用余额不低于
	 
	private Double param9=0.00;//单笔投标的最大金额  
	
	
	@Column(name="createTime_")
	private long createTime=0;//设置时间
 
	@Column(name="nextFlag_",length=20)
	private String nextFlag="1";//1下一个生效的参数0历史参数
	
	
	private long okTime=0;//生效时间
	
	@Column(name="userPriorityId_")
    private String userPriorityId;//主表,非空表示已经生效过，生效过的才有值
	
	
	public UserParameter() { 
	}
	
	public UserParameter(MemberBase member, long createTime) { 
		this.member = member;
		this.createTime = createTime;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne 
	public MemberBase getMember() {
		return member;
	}
	public void setMember(MemberBase member) {
		this.member = member;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
 
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public Double getParam6() {
		return param6;
	}

	public void setParam6(Double param6) {
		this.param6 = param6;
	}

	public Double getParam7() {
		return param7;
	}

	public void setParam7(Double param7) {
		this.param7 = param7;
	}

	public String getNextFlag() {
		return nextFlag;
	}

	public void setNextFlag(String nextFlag) {
		this.nextFlag = nextFlag;
	}

	public long getDayMin() {
		return dayMin;
	}

	public void setDayMin(long dayMin) {
		this.dayMin = dayMin;
	}

	public long getDayMax() {
		return dayMax;
	}

	public void setDayMax(long dayMax) {
		this.dayMax = dayMax;
	}

	public long getMonthMin() {
		return monthMin;
	}

	public void setMonthMin(long monthMin) {
		this.monthMin = monthMin;
	}

	public long getMonthMax() {
		return monthMax;
	}

	public void setMonthMax(long monthMax) {
		this.monthMax = monthMax;
	}
	@Column(name="okTime_",columnDefinition="number default 0")
	public long getOkTime() {
		return okTime;
	}

	public void setOkTime(long okTime) {
		this.okTime = okTime;
	}

	public String getUserPriorityId() {
		return userPriorityId;
	}

	public void setUserPriorityId(String userPriorityId) {
		this.userPriorityId = userPriorityId;
	}
	
	@Column(name="param8_", nullable = true,precision=12, scale=2,columnDefinition="float default 0")
	public Double getParam8() {
		return param8;
	}

	public void setParam8(Double param8) {
		this.param8 = param8;
	}
	
	@Column(name="param9_", nullable = true,precision=12, scale=2,columnDefinition="float default 0")
	public Double getParam9() {
		return param9;
	}

	public void setParam9(Double param9) {
		this.param9 = param9;
	}
 
	
}
