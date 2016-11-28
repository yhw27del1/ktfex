package com.kmfex.webservice.vo;

import java.io.Serializable; 
import java.util.List;
/**
 * 融资项目返回对象
 * @author  
 *
 */
public class FinancingBaseResult implements Serializable{  
	private static final long serialVersionUID = 8431777888704203321L;
	private MessageTip messageTip; //信息类
	private List<FinancingBaseVo>   financings;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<FinancingBaseVo> getFinancings() {
		return financings;
	}
	public void setFinancings(List<FinancingBaseVo> financings) {
		this.financings = financings;
	} 
	
}
