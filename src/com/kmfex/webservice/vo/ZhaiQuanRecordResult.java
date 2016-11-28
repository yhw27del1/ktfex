package com.kmfex.webservice.vo;

import java.io.Serializable; 
import java.util.List;
  
/**
 * 债权记录返回对象
 * @author     
 */
 
public class ZhaiQuanRecordResult implements Serializable{ 
	private static final long serialVersionUID = -1110810074965041388L;
	private MessageTip messageTip; //信息类
	private List<ZhaiQuanRecordVo>   records;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<ZhaiQuanRecordVo> getRecords() {
		return records;
	}
	public void setRecords(List<ZhaiQuanRecordVo> records) {
		this.records = records;
	}
	
 
}
