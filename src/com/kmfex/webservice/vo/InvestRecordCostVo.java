package com.kmfex.webservice.vo;

import java.io.Serializable;


/**
 * 投资投标费用明细 
 */
 
public class InvestRecordCostVo implements Serializable{
	
 
	private static final long serialVersionUID = -8793977266971096427L;
	private String id;  
	private String investRecordId;  
	private double tzfwf =0d;//投资服务费 
	private double sxf =0d;    //手续费  
	private ContractKeyDataVO contractvo; //合同编号
 
    private double   realAmount =0d;   //实际费用             投资记录的投标额+手续费+印花税等费用
    
    
    
	public InvestRecordCostVo() { 
	}
	
	
	public InvestRecordCostVo(String id, double tzfwf, double sxf,
			ContractKeyDataVO contractvo, double realAmount) { 
		this.id = id;
		this.tzfwf = tzfwf;
		this.sxf = sxf;
		this.contractvo = contractvo;
		this.realAmount = realAmount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getTzfwf() {
		return tzfwf;
	}
	public void setTzfwf(double tzfwf) {
		this.tzfwf = tzfwf;
	}
	public double getSxf() {
		return sxf;
	}
	public void setSxf(double sxf) {
		this.sxf = sxf;
	}
	
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}


	public ContractKeyDataVO getContractvo() {
		return contractvo;
	}


	public void setContractvo(ContractKeyDataVO contractvo) {
		this.contractvo = contractvo;
	}


	public String getInvestRecordId() {
		return investRecordId;
	}


	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}


 

    

}
