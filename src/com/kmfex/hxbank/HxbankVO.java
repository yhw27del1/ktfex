package com.kmfex.hxbank;

import com.google.gson.GsonBuilder;
/**
 * @author linuxp
 * */
public class HxbankVO {
	private boolean flag = false;
	private String tip = "";
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getTip() {
		return tip;
	}
	public static void main(String[] args) {
		HxbankVO vo = new HxbankVO();
		vo.setFlag(true);
		vo.setTip("罚款多少积分离开技术的法律框架");
		System.out.println(vo.getJSON(vo));
	}
	public String getJSON(HxbankVO vo){
		return new GsonBuilder().create().toJson(vo);
	}
}
