package com.kmfex.webservice.vo;

import java.io.Serializable;  
  
/**
 * 用户帐号信息
 * @author     
 */
 
public class AccountResult implements Serializable{
 
	private static final long serialVersionUID = -1461470563165274947L; 
	private MessageTip messageTip; //信息类
	private AccountVo   account;
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}
	public AccountVo getAccount() {
		return account;
	}
	public void setAccount(AccountVo account) {
		this.account = account;
	}
 
 
}
