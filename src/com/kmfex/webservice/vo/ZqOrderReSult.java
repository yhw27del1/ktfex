package com.kmfex.webservice.vo;
  

/**
 * 出让受让下单结果
 */ 
public class ZqOrderReSult  implements java.io.Serializable{   
	private static final long serialVersionUID = -8064617542266377957L;
	private MessageTip messageTip; //信息类
	private ZqOrderVo   orderVo;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public ZqOrderVo getOrderVo() {
		return orderVo;
	}
	public void setOrderVo(ZqOrderVo orderVo) {
		this.orderVo = orderVo;
	}
 
}
