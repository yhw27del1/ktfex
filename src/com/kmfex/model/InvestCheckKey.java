package com.kmfex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投资投标验证
 * @author   
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Invest_CheckKey", schema = "KT")
public class InvestCheckKey implements Serializable {
	private String keyStr;
	private String financingCode;  
	private String username;  

	
	
	public InvestCheckKey() { 
	}

	public InvestCheckKey(String keyStr, String financingCode, String username) { 
		this.keyStr = keyStr;
		this.financingCode = financingCode;
		this.username = username;
	}

	@Id
	public String getKeyStr() {
		return keyStr;
	}
	
	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}
	@Column(length=50)
	public String getFinancingCode() {
		return financingCode;
	}
	public void setFinancingCode(String financingCode) {
		this.financingCode = financingCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	} 
	
}
