package com.wisdoor.struts;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.wisdoor.core.model.User;
import com.wisdoor.core.vo.CommonVo;

@SuppressWarnings("serial")
public abstract class BaseAction  extends ActionSupport  {
	private Date showToday = new Date();
	private Date startDate;
	private Date endDate;
	private String keyWord;
	private String directUrl;
 
	/** 获取当前页 **/
	private int page=1;
	
	/** 选择的每页显示几个 **/
	private int showRecord=15;
	
	/** 可选择的每页显示几个 **/
	
	private List<CommonVo> showRecordList;  
 
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getShowRecord() {
		return showRecord;
	}

	public void setShowRecord(int showRecord) {
		this.showRecord = showRecord;
	}

	public void setShowRecordList(List<CommonVo> showRecordList) {
		this.showRecordList = showRecordList;
	}
	
	@Deprecated
	public List<CommonVo> getShowRecordList() {
		if (showRecordList != null)
			return showRecordList; 
		showRecordList = new ArrayList<CommonVo>(); 
		showRecordList.add(new CommonVo("10", "10")); 
		showRecordList.add(new CommonVo("20", "20"));
		showRecordList.add(new CommonVo("30", "30"));
		showRecordList.add(new CommonVo("40", "40"));
		showRecordList.add(new CommonVo("50", "50"));
		return showRecordList;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	//按时间查询时，未传入时间，默认取当天时间
	public void checkDate(){
		Date today = new Date();
		if(null==startDate){
			startDate = today;
		}
		if(null==endDate){
			endDate = today;
		}
	}
	
	public User getLoginUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static PrintWriter getOut() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		return out;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public String getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

	public void setShowToday(Date showToday) {
		this.showToday = showToday;
	}

	public Date getShowToday() {
		return showToday;
	}
	
}
