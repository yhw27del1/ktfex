package com.kmfex.webservice.vo;

import java.io.Serializable;


/**
 * 投资投标费用明细 
 */
 
public class InvestRecordCostResult implements Serializable{
 
	private static final long serialVersionUID = -8651934125920219134L;
	private MessageTip messageTip; //信息类
	private InvestRecordCostVo investRecordCostVo; //投资费用
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public InvestRecordCostVo getInvestRecordCostVo() {
		return investRecordCostVo;
	}
	public void setInvestRecordCostVo(InvestRecordCostVo investRecordCostVo) {
		this.investRecordCostVo = investRecordCostVo;
	}
   
    

}
