package com.kmfex;

public class JYRBVO {
	private String orgCode = "";//开户机构编码
	private String orgName = "";//开户机构名称
	private double sum_balance = 0d;//投资人可用余额汇总
	private double sum_frozen = 0d;//投资人冻结余额汇总
	private double sum_in = 0d;//投资人入金总额
	private int count_in = 0;//投资人入金总笔数
	private double sum_out = 0d;//投资人出金总额
	private int count_out = 0;//投资人出金总笔数
	private double sum_balance_s = 0d;//投资人可用余额汇总,日切中的数据
	private double sum_frozen_s = 0d;//投资人冻结余额汇总,日切中的数据
	public JYRBVO(){
		
	}
	public JYRBVO(String o,String n){
		this.orgCode = o;
		this.orgName = n;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setSum_balance(double sum_balance) {
		this.sum_balance = sum_balance;
	}
	public double getSum_balance() {
		return sum_balance;
	}
	public void setSum_frozen(double sum_frozen) {
		this.sum_frozen = sum_frozen;
	}
	public double getSum_frozen() {
		return sum_frozen;
	}
	public void setSum_in(double sum_in) {
		this.sum_in = sum_in;
	}
	public double getSum_in() {
		return sum_in;
	}
	public void setCount_in(int count_in) {
		this.count_in = count_in;
	}
	public int getCount_in() {
		return count_in;
	}
	public void setSum_out(double sum_out) {
		this.sum_out = sum_out;
	}
	public double getSum_out() {
		return sum_out;
	}
	public void setCount_out(int count_out) {
		this.count_out = count_out;
	}
	public int getCount_out() {
		return count_out;
	}
	public double getSum_balance_s() {
		return sum_balance_s;
	}
	public void setSum_balance_s(double sum_balance_s) {
		this.sum_balance_s = sum_balance_s;
	}
	public double getSum_frozen_s() {
		return sum_frozen_s;
	}
	public void setSum_frozen_s(double sum_frozen_s) {
		this.sum_frozen_s = sum_frozen_s;
	}
	
}
