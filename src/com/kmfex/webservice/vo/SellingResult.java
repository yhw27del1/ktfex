package com.kmfex.webservice.vo;

import java.io.Serializable; 
import java.util.List;
  
/**
 * 出让记录返回对象
 * @author     
 */
 
public class SellingResult implements Serializable{
 
	private static final long serialVersionUID = -1461470563165274947L; 
	private MessageTip messageTip; //信息类
	private List<SellingVo>   sellings;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<SellingVo> getSellings() {
		return sellings;
	}
	public void setSellings(List<SellingVo> sellings) {
		this.sellings = sellings;
	}
 
	
 
}
