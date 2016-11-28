package com.kmfex;

import java.util.Date;

public class AccrualVO2 {
	private int interval = 0;
	private double sum_all = 0.0;//余额汇总
	private double sum_lx = 0.0;//利息汇总
	private int count = 0;//人数汇总
	private Date start;
	private Date end;
	private String memo;
	
	private Date start1;
	private Date end1;
	
	private Date start2;
	private Date end2;
	
	private Date start3;
	private Date end3;
	
	private double rate1 = 0.0;
	private double rate2 = 0.0;
	private double rate3 = 0.0;
	
	private String username;//界面传过来多个值，用","隔开的
	
	private String sum;//界面传过来多个值，用","隔开的
	private String sum1;//界面传过来多个值，用","隔开的
	private String sum2;//界面传过来多个值，用","隔开的
	private String sum3;//界面传过来多个值，用","隔开的
	
	private String lx;//界面传过来多个值，用","隔开的
	private String lx1;//界面传过来多个值，用","隔开的
	private String lx2;//界面传过来多个值，用","隔开的
	private String lx3;//界面传过来多个值，用","隔开的
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getSum() {
		return sum;
	}
	public void setLx(String lx) {
		this.lx = lx;
	}
	public String getLx() {
		return lx;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getInterval() {
		return interval;
	}
	public void setSum_all(double sum_all) {
		this.sum_all = sum_all;
	}
	public double getSum_all() {
		return sum_all;
	}
	public void setSum_lx(double sum_lx) {
		this.sum_lx = sum_lx;
	}
	public double getSum_lx() {
		return sum_lx;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getStart() {
		return start;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getEnd() {
		return end;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return memo;
	}
	public Date getStart1() {
		return start1;
	}
	public void setStart1(Date start1) {
		this.start1 = start1;
	}
	public Date getEnd1() {
		return end1;
	}
	public void setEnd1(Date end1) {
		this.end1 = end1;
	}
	public Date getStart2() {
		return start2;
	}
	public void setStart2(Date start2) {
		this.start2 = start2;
	}
	public Date getEnd2() {
		return end2;
	}
	public void setEnd2(Date end2) {
		this.end2 = end2;
	}
	public Date getStart3() {
		return start3;
	}
	public void setStart3(Date start3) {
		this.start3 = start3;
	}
	public Date getEnd3() {
		return end3;
	}
	public void setEnd3(Date end3) {
		this.end3 = end3;
	}
	public double getRate1() {
		return rate1;
	}
	public void setRate1(double rate1) {
		this.rate1 = rate1;
	}
	public double getRate2() {
		return rate2;
	}
	public void setRate2(double rate2) {
		this.rate2 = rate2;
	}
	public double getRate3() {
		return rate3;
	}
	public void setRate3(double rate3) {
		this.rate3 = rate3;
	}
	public String getSum1() {
		return sum1;
	}
	public void setSum1(String sum1) {
		this.sum1 = sum1;
	}
	public String getSum2() {
		return sum2;
	}
	public void setSum2(String sum2) {
		this.sum2 = sum2;
	}
	public String getSum3() {
		return sum3;
	}
	public void setSum3(String sum3) {
		this.sum3 = sum3;
	}
	public String getLx1() {
		return lx1;
	}
	public void setLx1(String lx1) {
		this.lx1 = lx1;
	}
	public String getLx2() {
		return lx2;
	}
	public void setLx2(String lx2) {
		this.lx2 = lx2;
	}
	public String getLx3() {
		return lx3;
	}
	public void setLx3(String lx3) {
		this.lx3 = lx3;
	}
	
}
