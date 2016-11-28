package com.kmfex.zhaiquan.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kmfex.model.InvestRecord;
import com.wisdoor.core.model.User;
/**
 * 债权转让协议
 * @author   
 * 2013-08-23 15:09     增加percentBuy，percentSell   手续费率
 */
@Entity
@Table(name="zq_Contract",schema="KT")
public class Contract  implements Serializable{  
	private static final long serialVersionUID = 1939893027270245534L;
 

	private String id;
	/**
	 * 债权转让协议编号 
	 * 编号规则待定
	 */
	private String xieyiCode;
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
	 * 成交价
	 */
	private double price = 0d;  
	/**
	 * 大写
	 */
	private String price_dx;
	
	/**
	 * 受让人 
	 */ 
	private User buyer;
	/**
	 * 出让人 
	 */ 
	private User seller;
	/**
	 * 出让方签订日期
	 */ 
	private Date sellerDate; 
	
	/**
	 * 受让方签订日期
	 */ 
	private Date buyerDate; 
	/**
	 * 债权到期日
	 */ 
	private Date endDate; 
	  
	/**
	 * 投标记录
	 */
	private InvestRecord  investRecord;  
	/**
	 * 合同来源--受让方
	 */
	private  Buying  buying;
	/**
	 * 合同来源--出让方
	 */
	private  Selling selling;
	
	
	/**发布日期*/
	private Date fbrq;
	/**
	 * 剩余本息
	 */
	private double syje = 0;
	/**
	 * 剩余本息——大写
	 */
	private String syje_dx;
	
	//手续费率
	private Double percentBuy=0.2;         
	private Double percentSell=0.2;        
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
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
	@OneToOne
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	@OneToOne
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	} 
	public Date getSellerDate() {
		return sellerDate;
	}
	public void setSellerDate(Date sellerDate) {
		this.sellerDate = sellerDate;
	}
	public Date getBuyerDate() {
		return buyerDate;
	}
	public void setBuyerDate(Date buyerDate) {
		this.buyerDate = buyerDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@ManyToOne
	public InvestRecord getInvestRecord() {
		return investRecord;
	}
	public void setInvestRecord(InvestRecord investRecord) {
		this.investRecord = investRecord;
	}
	public String getXieyiCode() {
		return xieyiCode;
	}
	public void setXieyiCode(String xieyiCode) {
		this.xieyiCode = xieyiCode;
	}
	@OneToOne
	public Buying getBuying() {
		return buying;
	}
	public void setBuying(Buying buying) {
		this.buying = buying;
	}
	@OneToOne
	public Selling getSelling() {
		return selling;
	}
	public void setSelling(Selling selling) {
		this.selling = selling;
	}
	public Date getFbrq() {
		return fbrq;
	}
	public void setFbrq(Date fbrq) {
		this.fbrq = fbrq;
	}
	public double getSyje() {
		return syje;
	}
	public void setSyje(double syje) {
		this.syje = syje;
	}
	public String getPrice_dx() {
		return price_dx;
	}
	public void setPrice_dx(String price_dx) {
		this.price_dx = price_dx;
	}
	
	public String getSyje_dx() {
		return syje_dx;
	}
	public void setSyje_dx(String syje_dx) {
		this.syje_dx = syje_dx;
	}
	@Column(columnDefinition="float(2) default 0.2")
	public Double getPercentBuy() {
		return percentBuy;
	}
	public void setPercentBuy(Double percentBuy) {
		this.percentBuy = percentBuy;
	}
	@Column(columnDefinition="float(2) default 0.2")
	public Double getPercentSell() {
		return percentSell;
	}
	public void setPercentSell(Double percentSell) {
		this.percentSell = percentSell;
	} 
	
	
	
	
}
