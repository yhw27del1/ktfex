package com.kmfex.model.cmb;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author linuxp
 * */
@Entity
@Table(name = "cmb_deal",schema="KT")
public class CmbDeal {
	private String id;
	private Date createDate = new Date();//请求时间
	private Date updateDate;//响应时间
	private String txCode;//交易代码:6600等
	private String name;//交易名称:申请密钥请求等
	private long operator;//操作者id
	private boolean success = false;
	private String memo;
	
	//以下为请求信息，包括部分响应信息
	private String bkSerial = "";//银行流水号C(20)
	private String coSerial = "";//合作方流水号C(20)
	
	private String batchNo = "";//交易批次号C(16)
	
	private String coBrn = "";//合作方机构号C(8)
	private String coTime = "";//交易所服务器时间C(16)
	
	private String keyInfo = "";//密钥信息C(256)
	private String kMCode = "";//Mac校验码C(8)
	private String kPCode = "";//PinBlock校验码C(16)
	
	private String transDate = "";//交易日期C(8)
	private String settRet = "";//清算结果C(8)
	private String remark = "";//结果说明C(200)
	
	private String bankAcc = "";//银行账号C(30)
	private String fundAcc = "";//交易账号C(30)
	private String curCode = "";//货币代码C(3)
	
	private String curFlag= "";//钞汇标志C(4)
	private String amount= "";//转账金额	N(15)
	private double amount_ = 0d;
	private String pinBlock= "";//交易账号密码C(16)
	
	private String iDType= "";//证件类别C(4)
	private String iDNo= "";//证件号码C(36)
	private String custName= "";//客户名称C(80)
	private String country= "";//国别代码C(4)
	//M:男；F:女；O:其他
	private String sex = "";//客户性别C(1)
	
	private String busiType = "";//业务类型C(2)
	//10:一卡通；20:对公账户；30:财富账户
	private String accType = "";//账号类别C(2)
	
	private String phone = "";//电话号码C(30)	
	private String mobile = "";//手机号码	C(30)
	private String fax = "";//传真号码C(30)
	private String email = "";//电子邮件C(30)
	private String postCode = "";//邮政编码C(6)
	private String address = "";//通讯地址C(80)
	
	private String salerId = "";//推荐人编号C(10)
	private String bkBrn = "";//银行分行号C(8)
	
	//以下为响应信息
	private String respCode;//C(4)
	private String errMsg;//C(200)
	
	private String fundBal;//可取余额//N(15)
	private double fundBal_ = 0d;
	private String fundUse;//可用余额//N(15)
	private double fundUse_ = 0d;
	
	public double getAmount_() {
		return amount_;
	}

	public void setAmount_(double amount_) {
		this.amount_ = amount_;
	}

	public double getFundBal_() {
		return fundBal_;
	}

	public void setFundBal_(double fundBal_) {
		this.fundBal_ = fundBal_;
	}

	public double getFundUse_() {
		return fundUse_;
	}

	public void setFundUse_(double fundUse_) {
		this.fundUse_ = fundUse_;
	}

	public CmbDeal(){
		
	}
	
	public CmbDeal(String n,String c){
		this.name = n;
		this.txCode = c;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTxCode() {
		return txCode;
	}
	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getOperator() {
		return operator;
	}
	public void setOperator(long operator) {
		this.operator = operator;
	}
	public String getBkSerial() {
		return bkSerial;
	}
	public void setBkSerial(String bkSerial) {
		this.bkSerial = bkSerial;
	}
	public String getCoSerial() {
		return coSerial;
	}
	public void setCoSerial(String coSerial) {
		this.coSerial = coSerial;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getCoBrn() {
		return coBrn;
	}
	public void setCoBrn(String coBrn) {
		this.coBrn = coBrn;
	}
	
	@Column(length=500)
	public String getKeyInfo() {
		return keyInfo;
	}
	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}
	
	public String getKMCode() {
		return kMCode;
	}
	public void setKMCode(String code) {
		kMCode = code;
	}
	public String getKPCode() {
		return kPCode;
	}
	public void setKPCode(String code) {
		kPCode = code;
	}
	public String getCoTime() {
		return coTime;
	}
	public void setCoTime(String coTime) {
		this.coTime = coTime;
	}
	
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getSettRet() {
		return settRet;
	}
	public void setSettRet(String settRet) {
		this.settRet = settRet;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	
	public String getFundAcc() {
		return fundAcc;
	}
	public void setFundAcc(String fundAcc) {
		this.fundAcc = fundAcc;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	
	public String getCurFlag() {
		return curFlag;
	}
	public void setCurFlag(String curFlag) {
		this.curFlag = curFlag;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPinBlock() {
		return pinBlock;
	}
	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
	}
	public String getIDType() {
		return iDType;
	}
	public void setIDType(String type) {
		iDType = type;
	}
	public String getIDNo() {
		return iDNo;
	}
	public void setIDNo(String no) {
		iDNo = no;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSalerId() {
		return salerId;
	}
	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}
	public String getBkBrn() {
		return bkBrn;
	}
	public void setBkBrn(String bkBrn) {
		this.bkBrn = bkBrn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getFundBal() {
		return fundBal;
	}

	public void setFundBal(String fundBal) {
		this.fundBal = fundBal;
	}

	public String getFundUse() {
		return fundUse;
	}

	public void setFundUse(String fundUse) {
		this.fundUse = fundUse;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}