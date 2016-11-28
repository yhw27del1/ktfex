package com.kmfex;

import java.util.Date;

public class AccrualVO {
	private int interval = 0;
	private double sum_all = 0.0;//余额汇总
	private double sum_lx = 0.0;//利息汇总
	private int count = 0;//人数汇总
	private double rate = 0.0;
	private Date start;
	private Date end;
	private String memo;
	
	private String username;//界面传过来多个值，用","隔开的
	private String sum;//界面传过来多个值，用","隔开的
	private String lx;//界面传过来多个值，用","隔开的
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
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getRate() {
		return rate;
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
}
