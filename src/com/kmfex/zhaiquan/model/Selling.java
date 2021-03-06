package com.kmfex.zhaiquan.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.kmfex.model.InvestRecord;
import com.wisdoor.core.model.User;
/**
 * 出让记录
 * @author   
 */
@Entity
@Table(name="zq_Selling",schema="KT")
public class Selling  implements Serializable{  
	private static final long serialVersionUID = 1939893027270245534L;
	/**
	 * 出让状态：出让中
	 * */
	public final static String STATE_NOT = "0";
	/**
	 * 出让状态：成功 
	 * */
	public final static String STATE_PASSED = "1"; 
	/**
	 * 出让状态：撤单
	 * */
	public final static String STATE_NOT_BUY = "3";
	/**
	 * 出让状态：系统撤单
	 * */
	public final static String STATE_NOT_GQ = "4";
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
	 * 出让价格
	 */
	private double sellingPrice = 0d; 
	/**
	 * 债权转让手续费
	 */ 
	private double zqfwf = 0d; 
	
	/**
	 * 债权转让税费
	 */
	private double zqsf = 0d;  
	/**
	 * 实际金额（冻结的金额）=出让价格-债权转让服务费-债权转让税费 等费用
	 */
    private double realAmount =0d;   
	/**
	 * 出让人 
	 */ 
	private User seller;
	
	/**
	 * 出让时间
	 */ 
	private Date createDate;  
	
	private String state=Selling.STATE_NOT;//默认待出让
	/**
	 * 投标记录
	 */
	private InvestRecord  investRecord;   
	
	
	private Contract contract;
	
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}  
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@OneToOne
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	@OneToOne
	public InvestRecord getInvestRecord() {
		return investRecord;
	}
	public void setInvestRecord(InvestRecord investRecord) {
		this.investRecord = investRecord;
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
	@OneToOne
	public Contract getContract() {
		return contract;
	}
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	
	
}
