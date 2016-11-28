package com.kmfex.webservice.vo;
 
import java.util.Date;
 
/**
 * 下单记录
 * @author   
 */  
public class ZqOrderVo   {   
	 

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
	 * 债权转让手续费
	 */ 
	private double fee1 = 0d; 
	
	/**
	 * 债权转让税费
	 */
	private double fee2 = 0d;  
	/**
	 * 成交价格
	 */
	private double price = 0d; 
	/**
	 * 出让价格--卖者报的价
	 */
	private double sellPrice = 0d; 
	/**
	 * 受让价格--买者报的价
	 */
	private double buyPrice = 0d; 
	/**
	 *  时间
	 */ 
	private Date createDate;  
	
	private String state;  
	/**
	 * 状态中文显示
	 * */
	private String cnState; 
	
	 /**
	  * "sell":出让;"buy":受让
	  */
	private String type;
	
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
	
	public ZqOrderVo() { 
	}
	public ZqOrderVo(String id, String zhaiQuanCode, String contractNumbers,
			String investRecordId, double price, double fee1, double fee2,
			Date createDate, String state, String type) { 
		this.id = id;
		this.zhaiQuanCode = zhaiQuanCode;
		contract_numbers = contractNumbers;
		this.investRecordId = investRecordId;
		this.price = price;
		this.fee1 = fee1;
		this.fee2 = fee2;
		this.createDate = createDate;
		this.state = state;
		this.type = type;
	}
	public String getInvestRecordId() {
		return investRecordId;
	}
	public void setInvestRecordId(String investRecordId) {
		this.investRecordId = investRecordId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getFee1() {
		return fee1;
	}
	public void setFee1(double fee1) {
		this.fee1 = fee1;
	}
	public double getFee2() {
		return fee2;
	}
	public void setFee2(double fee2) {
		this.fee2 = fee2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
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
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	 
  
	 
}
