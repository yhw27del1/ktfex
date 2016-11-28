package com.wisdoor.core.vo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PageForProcedureVO {
	private int totalpage = 0;
	private int totalrecord = 0;
	private ArrayList<LinkedHashMap<String,Object>> result = new ArrayList<LinkedHashMap<String,Object>>();
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
	public ArrayList<LinkedHashMap<String, Object>> getResult() {
		return result;
	}
	public void setResult(ArrayList<LinkedHashMap<String, Object>> result) {
		this.result = result;
	}
	
	
}
