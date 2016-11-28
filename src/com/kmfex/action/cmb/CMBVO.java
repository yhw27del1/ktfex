package com.kmfex.action.cmb;

import com.google.gson.GsonBuilder;
/**
 * @author linuxp
 * */
public class CMBVO {
	private String name;
	private String txCode;
	private boolean success = false;
	private String msg;
	public CMBVO(){
		
	}
	public CMBVO(String n,String c){
		this.name = n;
		this.txCode = c;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
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
	public static void main(String[] args) {
		CMBVO vo = new CMBVO();
		vo.setSuccess(true);
		vo.setMsg("交易成功");
		System.out.println(vo.getJSON());
	}
	public String getJSON(){
		return new GsonBuilder().create().toJson(this);
	}
}
