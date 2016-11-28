package com.kmfex.action.cmb;

import com.google.gson.GsonBuilder;
/**
 * @author linuxp
 * */
public class CMBResult {
	private boolean success = false;//httpClient调用获取数据是否成功
	private String msg;//提示信息
	private String result;//响应结果内容
	public CMBResult(){
		
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public static void main(String[] args) {
		CMBResult vo = new CMBResult();
		vo.setSuccess(true);
		vo.setResult("交易成功");
		System.out.println(vo.getJSON());
	}
	public String getJSON(){
		return new GsonBuilder().create().toJson(this);
	}
}
