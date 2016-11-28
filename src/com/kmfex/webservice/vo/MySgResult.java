package com.kmfex.webservice.vo;

import java.util.List;
  
/**
 * 我的投标返回对象
 * @author     
 */
 
public class MySgResult {
	private boolean success = true;//操作是否正常
	private String msg; //操作的提示信息
	private int totalpage;//总页数
	private int totalrecord;//总记录数
	private List<SgVo>   sgs;
	public List<SgVo> getSgs() {
		return sgs;
	}
	public void setSgs(List<SgVo> sgs) {
		this.sgs = sgs;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public int getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}
	 
	
 
}
