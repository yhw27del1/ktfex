package com.kmfex.webservice.vo;
  
import java.util.List;

/**
 * 资金明细
 */ 
public class AccountDealReSult  implements java.io.Serializable{    
 
	private static final long serialVersionUID = -6291932165598858266L;
	private MessageTip messageTip; //信息类
	private List<AccountDealVo>   accountDealVos;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public List<AccountDealVo> getAccountDealVos() {
		return accountDealVos;
	}
	public void setAccountDealVos(List<AccountDealVo> accountDealVos) {
		this.accountDealVos = accountDealVos;
	}
 
}
