package com.kmfex.webservice.vo;

import java.io.Serializable; 
import java.util.List;
  
/**
 * 投资投标记录返回对象
 * @author     
 */
 
public class InvestRecordResult implements Serializable{
 
	private static final long serialVersionUID = -1461470563165274947L; 
	private MessageTip messageTip; //信息类
	private List<InvestRecordVo>   records;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<InvestRecordVo> getRecords() {
		return records;
	}
	public void setRecords(List<InvestRecordVo> records) {
		this.records = records;
	}
	
 
}
