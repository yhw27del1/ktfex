package com.kmfex.cxfrs.vo;

import javax.xml.bind.annotation.XmlRootElement;
/*
 * 操作提示信息类
 * @author 
 */

@XmlRootElement(name="MessageTip")
public class MessageTip {
	private boolean success = true;//操作是否正常
	private String msg; //操作的提示信息
	private String param1; //预留信息1
	private String param2; //预留信息2
	private String param3; //预留信息3
	private String dateStr;//记录服务器当前时间
	
	private String param4; //预留信息4
	private String param5; //预留信息5
	private String param6; //预留信息6
	private String param7; //预留信息7
	private String param8; //预留信息8
	private String param9; //预留信息9
	private String param10; //预留信息10	
	private String param11; //预留信息11
	
	public MessageTip(){}
	public MessageTip(boolean success, String msg) { 
		this.success = success;
		this.msg = msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam6(String param6) {
		this.param6 = param6;
	}
	public String getParam6() {
		return param6;
	}
	public void setParam7(String param7) {
		this.param7 = param7;
	}
	public String getParam7() {
		return param7;
	}
	public void setParam8(String param8) {
		this.param8 = param8;
	}
	public String getParam8() {
		return param8;
	}
	public void setParam9(String param9) {
		this.param9 = param9;
	}
	public String getParam9() {
		return param9;
	}
	public void setParam10(String param10) {
		this.param10 = param10;
	}
	public String getParam10() {
		return param10;
	}
	public String getParam11() {
		return param11;
	}
	public void setParam11(String param11) {
		this.param11 = param11;
	} 
	
	
	@Override
	public String toString() {
		return "MessageTip [success=" + success + ", msg=" + msg + "]";
	}
	
}
