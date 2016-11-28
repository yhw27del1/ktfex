package com.kmfex.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 从利息表
 * @author linuxp
 *
 */
@Entity
@Table(name = "t_sub_accrual",schema="KT")
public class SubAccrual {
	private String id;
	private Date createDate = new Date();
	private String main_id;//主利息表id
	private String username;
	private long user_id;//user表id
	private double main_rate = 0d;//主期间年利率，间隔为1时此字段有效
	private double main_sum = 0d;//主期间余额总额，间隔为1时等于汇总额；大于1时等于子期间的汇总
	private double main_lx = 0d;//主期间利息总额，间隔为1时等于汇总额；大于1时等于子期间的汇总
	
	private double sub_sum = 0.0;//某人的余额汇总，间隔为1时=汇总额;间隔大于1时=sub_sum_1+sub_sum_2+sub_sum_3
	private double sub_lx = 0.0;//某人的利息汇总，间隔为1时=汇总额;间隔大于1时=sub_lx_1+sub_lx_2+sub_lx_3
	
	//间隔大于1时，以下字段有效
	private double sub_sum_1 = 0d;//子期间1，余额总额
	private double sub_lx_1 = 0d;//子期间1，利息总额
	
	//间隔大于1时，以下字段有效
	private double sub_sum_2 = 0d;//子期间2，余额总额
	private double sub_lx_2 = 0d;//子期间2，利息总额
	
	//间隔大于1时，以下字段有效
	private double sub_sum_3 = 0d;//子期间3，余额总额
	private double sub_lx_3 = 0d;//子期间3，利息总额
	
	private String memo;//备注
	private long operator;//操作者id
	
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
	public double getMain_rate() {
		return main_rate;
	}
	public void setMain_rate(double mainRate) {
		main_rate = mainRate;
	}
	public double getMain_sum() {
		return main_sum;
	}
	public void setMain_sum(double mainSum) {
		main_sum = mainSum;
	}
	public double getMain_lx() {
		return main_lx;
	}
	public void setMain_lx(double mainAccrual) {
		main_lx = mainAccrual;
	}
	public double getSub_sum_1() {
		return sub_sum_1;
	}
	public void setSub_sum_1(double subSum_1) {
		sub_sum_1 = subSum_1;
	}
	public double getSub_lx_1() {
		return sub_lx_1;
	}
	public void setSub_lx_1(double subAccrual_1) {
		sub_lx_1 = subAccrual_1;
	}
	public double getSub_sum_2() {
		return sub_sum_2;
	}
	public void setSub_sum_2(double subSum_2) {
		sub_sum_2 = subSum_2;
	}
	public double getSub_lx_2() {
		return sub_lx_2;
	}
	public void setSub_lx_2(double subAccrual_2) {
		sub_lx_2 = subAccrual_2;
	}
	public double getSub_sum_3() {
		return sub_sum_3;
	}
	public void setSub_sum_3(double subSum_3) {
		sub_sum_3 = subSum_3;
	}
	public double getSub_lx_3() {
		return sub_lx_3;
	}
	public void setSub_lx_3(double subAccrual_3) {
		sub_lx_3 = subAccrual_3;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public long getOperator() {
		return operator;
	}
	public void setOperator(long operator) {
		this.operator = operator;
	}
	public void setMain_id(String main_id) {
		this.main_id = main_id;
	}
	public String getMain_id() {
		return main_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setSub_sum(double sub_sum) {
		this.sub_sum = sub_sum;
	}
	public double getSub_sum() {
		return sub_sum;
	}
	public void setSub_lx(double sub_lx) {
		this.sub_lx = sub_lx;
	}
	public double getSub_lx() {
		return sub_lx;
	}
}
