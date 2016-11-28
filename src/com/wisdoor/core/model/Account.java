package com.wisdoor.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table; 
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * 用户帐户表
 * @author
 * 新增辅助资产查询优化字段(string5,string6,string7),不参与持久化  
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sys_Account",schema="KT")
public class Account  implements java.io.Serializable{
	public final static String CENTER = "1234567891234567890";//中心账户
	public final static String THIRDPARTY_XYD = "123456789-xyd";//第三方账户兴易贷
	
	public final static int STATE_WAIT = 0;//待开通
	public final static int STATE_OPEN = 1;//开通
	public final static int STATE_CANCEL = 2;//注销
  	private long id;   
	/**账号创建时间 **/
	private Date createDate = new Date(); 
	/**账号余额 **/  
	private double balance = 0d;//可用余额
	private double totalAmount = 0d;//总资产=可用余额+冻结金额+持有债权;此字段为辅助字段
	private double frozenAmount = 0d;//冻结金额
	
	private double old_balance = 0d;//原总余额(入金，出金，对账后更新此字段)
	
	/** 所属用户 **/
	@Column
	private User user;//通过user找到会员
	/** 所属机构和公司 **/
	private Org org;

	
	private Date cancelDate;//销户日期
	private int state = 0;//状态,0待开通(交易中心终审通过后，改成开通)，1开通，2注销
	private String accountId;//系统生成的账户编号，规则:19位数字
	private String daxie;
	private String password;//证银转账时，必须输入的密码
	                    
	private double cyzq = 0d; //**持有债权-- 辅助字段**/ 
	
	private int version=0;
	
	private String accountType;//账户类型，中心账户，兴易贷账户等，辅助字段
	
	/**
	 * 积分帐户
	 */
	private int credit = 0;
	
	private boolean sys_qingsuan = false;//系统清算标志，true表示已清算
	
	@Id   
	@Column(name="accountid_",length=50)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_ACCOUNT_SE")  
    @SequenceGenerator(name="SYS_ACCOUNT_SE",sequenceName="SYS_ACCOUNT_SEQUENCE",allocationSize=1)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Version
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Column(name="createDate_")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="balance_")
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	} 
	@OneToOne(mappedBy = "userAccount") 
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@OneToOne(mappedBy = "orgAccount") 
	public Org getOrg() {
		return org;
	}
	
	
	
	public void setOrg(Org org) {
		this.org = org;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void setDaxie(String daxie) {
		this.daxie = daxie;
	}
	@Transient
	public String getDaxie() {
		return daxie;
	} 
	@Transient
	public double getCyzq() {
		return cyzq;
	}
	public void setCyzq(double cyzq) {
		this.cyzq = cyzq;
	} 
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Transient
	public double getTotalAmount() {
		this.totalAmount = this.frozenAmount+this.balance+this.cyzq;
		return totalAmount;
	}
	public void setFrozenAmount(double frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	public double getFrozenAmount() {
		return frozenAmount;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	@Transient
	public String getAccountType() {
		if(this.accountId.equals(Account.CENTER)){
			accountType = "中心账户";
		}
		/*else if(this.accountId.equals(Account.THIRDPARTY_XYD)){
			accountType = "兴易贷账户";
		}*/
		return accountType;
	}
	@Column(columnDefinition="int default 0")
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public void setOld_balance(double old_balance) {
		this.old_balance = old_balance;
	}
	
	@Column(columnDefinition="float default 0.0") 
	public double getOld_balance() {
		return old_balance;
	}
	
	//辅助资产查询 
	private String string1="0";//借出笔数
	private String string2="0";//借出金额
	private String string3="0";//回收笔试
	private String string4="0";//回收金额 
	private String string5="";//用户名  
	private String string6="";//真实姓名
	private String string7="";//用户ID

	@Transient
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	@Transient
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	@Transient
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	@Transient
	public String getString4() {
		return string4;
	}
	public void setString4(String string4) {
		this.string4 = string4;
	}
	@Transient
	public String getString5() {
		return string5;
	}
	public void setString5(String string5) {
		this.string5 = string5;
	}
	@Transient
	public String getString6() {
		return string6;
	}
	public void setString6(String string6) {
		this.string6 = string6;
	}
	@Transient
	public String getString7() {
		return string7;
	}
	public void setString7(String string7) {
		this.string7 = string7;
	}
	public void setSys_qingsuan(boolean sys_qingsuan) {
		this.sys_qingsuan = sys_qingsuan;
	}
	@Column(columnDefinition="number default 0")
	public boolean isSys_qingsuan() {
		return sys_qingsuan;
	}
	
	
}
