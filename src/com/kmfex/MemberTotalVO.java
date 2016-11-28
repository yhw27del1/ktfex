package com.kmfex;

//会员统计VO(已审核通过的，state=2)
public class MemberTotalVO {
	private String orgCode = "";//开户机构编码
	private String orgName = "";//开户机构名称
	private int total = 0;//开户机构编码下总会员数
	private int total_T = 0;//开户机构编码下总投资人数
	private int total_R = 0;//开户机构编码下总融资方数
	private int total_D = 0;//开户机构编码下总担保公司人数
	private int total_Y = 0;//开户机构编码下总银行人数
	private int total_Q= 0;//开户机构编码下总其他人数
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal_T() {
		return total_T;
	}
	public void setTotal_T(int totalT) {
		total_T = totalT;
	}
	public int getTotal_R() {
		return total_R;
	}
	public void setTotal_R(int totalR) {
		total_R = totalR;
	}
	public int getTotal_D() {
		return total_D;
	}
	public void setTotal_D(int totalD) {
		total_D = totalD;
	}
	public int getTotal_Y() {
		return total_Y;
	}
	public void setTotal_Y(int totalY) {
		total_Y = totalY;
	}
	public int getTotal_Q() {
		return total_Q;
	}
	public void setTotal_Q(int totalQ) {
		total_Q = totalQ;
	}
	public MemberTotalVO(){
		
	}
	public MemberTotalVO(String o,String n){
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
}
