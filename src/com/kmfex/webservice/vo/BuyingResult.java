package com.kmfex.webservice.vo;

import java.io.Serializable; 
import java.util.List;
  
/**
 * 受让记录返回对象
 * @author     
 */
 
public class BuyingResult implements Serializable{
 
	private static final long serialVersionUID = -1461470563165274947L; 
	private MessageTip messageTip; //信息类
	private List<BuyingVo>   buyings;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<BuyingVo> getBuyings() {
		return buyings;
	}
	public void setBuyings(List<BuyingVo> buyings) {
		this.buyings = buyings;
	}
 
	
 
}
