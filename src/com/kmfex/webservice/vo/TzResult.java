package com.kmfex.webservice.vo;

import java.util.List;
  
/**
 * 通知返回对象
 * @author
 */
 
public class TzResult {
	private boolean success = true;
	private String msg; 
	private int totalpage;//总页数
	private int totalrecord;//总记录数
	private List<TzVo> tzs;
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
	public List<TzVo> getTzs() {
		return tzs;
	}
	public void setTzs(List<TzVo> tzs) {
		this.tzs = tzs;
	}
	 
}
