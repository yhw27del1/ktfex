package com.kmfex.webservice.vo;

import java.io.Serializable;
import java.util.Date;
 
/**
 * 受让记录
 * @author   
 */ 
@SuppressWarnings("serial")
public class BuyingVo  implements Serializable{   
	 

	private String id;
	/**
	 * 债权代码
 	 * 债权代码(11位)：
     * 项目编号(9位)+流水号(3位)，之间用短线分隔 
     * 如：A12000001-001
	 */
	private String zhaiQuanCode;
	/**
	 * 借款合同编码
	 */
	private String contract_numbers; 
	/**
	 * 投标记录
	 */
	private String  investRecordId;   
	/**
	 * 状态中文显示
	 * */
	private String cnState; 
	/**
	 * 受让价格
	 */
	private double buyingPrice = 0d; 
	
	/**
	 * 出让价格--卖者报的价
	 */
	private double sellPrice = 0d; 
	/**
	 * 债权转让手续费
	 */ 
	private double zqfwf = 0d; 
	
	/**
	 * 债权转让税费
	 */
	private double zqsf = 0d;  
	/**
	 * 实际金额（冻结的金额）=受让价格+债权转让服务费+债权转让税费 等费用
	 */
    private double realAmount =0d;   
 
	
	/**
	 * 受让时间
	 */ 
	private Date createDate; 
	 

	
	private String state; 
	
 
	/**
	 * 协议编号
	 */
	private String contractId; 
 
 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
 
	public String getZhaiQuanCode() {
		return zhaiQuanCode;
	}
	public void setZhaiQuanCode(String zhaiQuanCode) {
		this.zhaiQuanCode = zhaiQuanCode;
	}
	public String getContract_numbers() {
		return contract_numbers;
	}
	public void setContract_numbers(String contractNumbers) {
		contract_numbers = contractNumbers;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	 
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	 
 
	public double getBuyingPrice() {
		return buyingPrice;
	}
	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	 
	public double getZqfwf() {
		return zqfwf;
	}
	public void setZqfwf(double zqfwf) {
		this.zqfwf = zqfwf;
	}
	public double getZqsf() {
		return zqsf;
	}
	public void setZqsf(double zqsf) {
		this.zqsf = zqsf;
	}
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}
	public String getInvestRecordId() {
		return investRecordId;
	}
	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}
 
	public String getCnState() {
		if("0".equals(this.state)){ 
			return "下单成功"; 
		}else if("1".equals(this.state)){
			return "已成交"; 
		}else if("3".equals(this.state)){
			return "撤单成功"; 
		}else if("4".equals(this.state)){
			return "系统撤单"; 
		}else{
			return "";
		}
	}
	public void setCnState(String cnState) {
		this.cnState = cnState;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	 
  
}
