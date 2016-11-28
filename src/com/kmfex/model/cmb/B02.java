package com.kmfex.model.cmb;

public class B02 {
	public String BANKID;//银行编号
	public String DEALID;//合作方编号
	public String COBRN;//合作方机构号
	public String TXDATE;//交易日期
	public String BANKACC;//银行账号
	public String FUNDACC;//交易所交易账号
	public String CUSTNAME;//客户姓名
	public String CURCODE;//货币代码
	public String STATUS;//协议状态
	
	public String getBANKID() {
		return BANKID;
	}
	public void setBANKID(String bankid) {
		BANKID = bankid;
	}
	public String getDEALID() {
		return DEALID;
	}
	public void setDEALID(String dealid) {
		DEALID = dealid;
	}
	public String getCOBRN() {
		return COBRN;
	}
	public void setCOBRN(String cobrn) {
		COBRN = cobrn;
	}
	public String getTXDATE() {
		return TXDATE;
	}
	public void setTXDATE(String txdate) {
		TXDATE = txdate;
	}
	public String getBANKACC() {
		return BANKACC;
	}
	public void setBANKACC(String bankacc) {
		BANKACC = bankacc;
	}
	public String getFUNDACC() {
		return FUNDACC;
	}
	public void setFUNDACC(String fundacc) {
		FUNDACC = fundacc;
	}
	public String getCUSTNAME() {
		return CUSTNAME;
	}
	public void setCUSTNAME(String custname) {
		CUSTNAME = custname;
	}
	public String getCURCODE() {
		return CURCODE;
	}
	public void setCURCODE(String curcode) {
		CURCODE = curcode;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String status) {
		STATUS = status;
	}
}