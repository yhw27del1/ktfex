package com.kmfex.webservice.vo;
  

/**
 * 某个融资项目的担保公司会员担保情况
 */ 
public class MemberGuaranteeReSult  implements java.io.Serializable{    
 
	private static final long serialVersionUID = -6291932165598858266L;
	private MessageTip messageTip; //信息类
	private MemberGuaranteeVo MemberGuaranteeVo;//担保公司会员担保情况
	public MemberGuaranteeVo getMemberGuaranteeVo() {
		return MemberGuaranteeVo;
	}
	public void setMemberGuaranteeVo(MemberGuaranteeVo memberGuaranteeVo) {
		MemberGuaranteeVo = memberGuaranteeVo;
	}
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
	}  
	
}
