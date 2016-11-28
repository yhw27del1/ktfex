package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 还款视图 
 */ 
@SuppressWarnings("serial")
public class ViewPaymentRecord implements Serializable{
 
	private String id;  
	private String state; 
	private Date yhdate;  
	private String succession;   
	private double fee2 = 0; 
	private double fj2 = 0;
	private String financbaseid;
	private String financbasecode;
	private String fshortname;
	private Date qianyuedate;
	private String dbgsmemberid;
	private String dbhsname; 

	public ViewPaymentRecord() {  
	}


	public ViewPaymentRecord(String  state, Date yhdate,  String succession, double fee2, double fj2,
			String financbaseid, String financbasecode, String fshortname,
			Date qianyuedate, String dbhsname) {  
		this.state = state;
		this.yhdate = yhdate; 
		this.succession = succession; 
		this.fee2 = fee2;
		this.fj2 = fj2;
		this.financbaseid = financbaseid;
		this.financbasecode = financbasecode;
		this.fshortname = fshortname;
		this.qianyuedate = qianyuedate; 
		this.dbhsname = dbhsname;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public Date getYhdate() {
		return yhdate;
	}


	public void setYhdate(Date yhdate) {
		this.yhdate = yhdate;
	}


	public String getSuccession() {
		return succession;
	}


	public void setSuccession(String succession) {
		this.succession = succession;
	}


	 

	public double getFee2() {
		return fee2;
	}


	public void setFee2(double fee2) {
		this.fee2 = fee2;
	}


	public double getFj2() {
		return fj2;
	}


	public void setFj2(double fj2) {
		this.fj2 = fj2;
	}


	public String getFinancbaseid() {
		return financbaseid;
	}


	public void setFinancbaseid(String financbaseid) {
		this.financbaseid = financbaseid;
	}


	public String getFinancbasecode() {
		return financbasecode;
	}


	public void setFinancbasecode(String financbasecode) {
		this.financbasecode = financbasecode;
	}


	public String getFshortname() {
		return fshortname;
	}


	public void setFshortname(String fshortname) {
		this.fshortname = fshortname;
	}


	public Date getQianyuedate() {
		return qianyuedate;
	}


	public void setQianyuedate(Date qianyuedate) {
		this.qianyuedate = qianyuedate;
	}


	public String getDbgsmemberid() {
		return dbgsmemberid;
	}


	public void setDbgsmemberid(String dbgsmemberid) {
		this.dbgsmemberid = dbgsmemberid;
	}


	public String getDbhsname() {
		return dbhsname;
	}


	public void setDbhsname(String dbhsname) {
		this.dbhsname = dbhsname;
	}


}

