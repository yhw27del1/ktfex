package com.kmfex.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 主利息表
 * @author linuxp
 *
 */
@Entity
@Table(name = "t_main_accrual",schema="KT")
public class MainAccrual {
	private String id;
	private Date createDate = new Date();
	private Date main_start;//主期间，起始日期
	private Date main_end;//主期间，截止日期
	private int interval = 1;//间隔，默认为1
	private double main_rate = 0d;//主期间年利率，间隔为1时此字段有效
	private double main_sum = 0d;//主期间余额总额，间隔为1时等于汇总额；大于1时等于子期间的汇总
	private int main_count = 0;
	private double main_lx = 0d;//主期间利息总额，间隔为1时等于汇总额；大于1时等于子期间的汇总
	
	//间隔大于1时，以下字段有效
	private Date sub_start_1;//子期间1，起始日期
	private Date sub_end_1;//子期间1，截止日期
	private double sub_rate_1 = 0d;//子期间1，年利率
	
	//间隔大于1时，以下字段有效
	private Date sub_start_2;//子期间2，起始日期
	private Date sub_end_2;//子期间2，截止日期
	private double sub_rate_2 = 0d;//子期间2，年利率
	
	//间隔大于1时，以下字段有效
	private Date sub_start_3;//子期间3，起始日期
	private Date sub_end_3;//子期间3，截止日期
	private double sub_rate_3 = 0d;//子期间3，年利率
	
	private String memo;//备注
	private boolean pay;//是否发放
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
	public Date getMain_start() {
		return main_start;
	}
	public void setMain_start(Date mainStart) {
		main_start = mainStart;
	}
	public Date getMain_end() {
		return main_end;
	}
	public void setMain_end(Date mainEnd) {
		main_end = mainEnd;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
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
	public Date getSub_start_1() {
		return sub_start_1;
	}
	public void setSub_start_1(Date subStart_1) {
		sub_start_1 = subStart_1;
	}
	public Date getSub_end_1() {
		return sub_end_1;
	}
	public void setSub_end_1(Date subEnd_1) {
		sub_end_1 = subEnd_1;
	}
	public double getSub_rate_1() {
		return sub_rate_1;
	}
	public void setSub_rate_1(double subRate_1) {
		sub_rate_1 = subRate_1;
	}
	public Date getSub_start_2() {
		return sub_start_2;
	}
	public void setSub_start_2(Date subStart_2) {
		sub_start_2 = subStart_2;
	}
	public Date getSub_end_2() {
		return sub_end_2;
	}
	public void setSub_end_2(Date subEnd_2) {
		sub_end_2 = subEnd_2;
	}
	public double getSub_rate_2() {
		return sub_rate_2;
	}
	public void setSub_rate_2(double subRate_2) {
		sub_rate_2 = subRate_2;
	}
	public Date getSub_start_3() {
		return sub_start_3;
	}
	public void setSub_start_3(Date subStart_3) {
		sub_start_3 = subStart_3;
	}
	public Date getSub_end_3() {
		return sub_end_3;
	}
	public void setSub_end_3(Date subEnd_3) {
		sub_end_3 = subEnd_3;
	}
	public double getSub_rate_3() {
		return sub_rate_3;
	}
	public void setSub_rate_3(double subRate_3) {
		sub_rate_3 = subRate_3;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public boolean isPay() {
		return pay;
	}
	public void setPay(boolean pay) {
		this.pay = pay;
	}
	public long getOperator() {
		return operator;
	}
	public void setOperator(long operator) {
		this.operator = operator;
	}
	public void setMain_count(int main_count) {
		this.main_count = main_count;
	}
	public int getMain_count() {
		return main_count;
	}
}
