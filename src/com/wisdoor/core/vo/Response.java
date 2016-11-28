package com.wisdoor.core.vo;
/**
 * 操作信息提示类
 * @author   
 */
@SuppressWarnings("serial")
public class Response   implements java.io.Serializable {

	private String status;//操作状态1成功0失败....
	private String message; //消息
	private String redirect; //重定向链接
	public Response() { 
	}

	public Response(String status, String message) { 
		this.status = status;
		this.message = message;
	}
	
	public Response(String redirect, String status, String message) { 
		this.redirect = redirect;
		this.status = status;
		this.message = message;
	}

	public String getRedirect() {
		return redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
