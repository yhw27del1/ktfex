package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;

/**
 * 用户帐户明细表
 */
@Entity
@Table(name = "t_AccountDeal",schema="KT")  
public class AccountDeal   implements Serializable{ 
	private static final long serialVersionUID = -3232212510929965069L;
	//投资人账户、融资方账户
	public final static String CASHCHARGE = "现金充值";
	public final static String CASH = "提现";
	
	public final static String BANK2ZQ = "银转商";//预留给接口使用(转入交易市场，入金)
	public final static String ZQ2BANK = "商转银";//预留给接口使用(转出交易市场，出金)
	
	//融资方账户
	public final static String JGHR = "交割划入";
	public final static String HKHC = "还款划出";
	public final static String BZJTHAW = "保证金解冻";//废弃
	
	//投资人账户
	public final static String SGHC = "投标划出";
	public final static String HKHR = "还款划入";
	public final static String ZQMC = "债权卖出";
	public final static String ZQMR = "债权买入";
	
	//中心账户，废弃
	public final static String TZKHR = "投资款划入";//废弃
	public final static String RZKJG = "融资款交割";//废弃
	
	public final static String ZQMRF = "债权买入费";//废弃
	public final static String ZQMCF = "债权卖出费";//废弃
	
	//融资方账户
	public final static String ZHGLF = "融资服务费";//2012-9-14由"综合管理费"改为"融资服务费”
	public final static String DBF = "担保费";
	
	//第三方账户
	public final static String JSHC = "结算划出";//废弃
	
	//中心账户
	public final static String JSHR = "结算划入";//废弃
	
	//2012-10-25
	public final static String DDHC = "代垫划出";
	public final static String DDHR = "代垫划入";
	
	public final static String NBZC = "内部转出";
	public final static String NBZR = "内部转入";
	
	
	public final static String HQLX = "活期利息";
	
	public final static String ZJBC = "资金拨出";
	public final static String ZJBR = "资金拨入";
	public final static String JYFWF = "交易手续费";
	
	private FinancingBase financing;//投资款划入与融资款交割的融资项目
	
	private User user;//充值操作者；操作者
	/**
	 * 放款确认人
	 */
	private User fkqruser;
	/**
	 * 放款确认时间
	 */
	private Date fkqrdate;
	
	private User checkUser;//审核者
	
	/** 资金账号 */
	public String bankAccount;
	/**
	 * 开户行
	 */
	private String bank;
	/**
	 * 发生额大写
	 */
	private String money_upcase;

	
	private String id;
	private Date createDate = new Date();
	private Account account;//会员交易账号，或者是交易中心的交易账号(投资款划入和融资款交割时)
	
	private double preMoney;//转账前金额
	private double money;//转账金额
	private double nextMoney;//转账后金额
	
	private String type;
	
	//针对提现做更改
	//会员提现申请时，可用减少，冻结增加
	//结算员审核通过的单子：冻结扣除，会员显示，待划款，（checkFlag 4）在出纳的待处理提现申请记录中出现
	//结算员审核驳回的单子：可用增加，冻结减少，会员显示，申请驳回
	//出纳：可进行 已划款和转账异常两个操作
	//已划款操作后，会员显示，已划款；
	//转账异常操作后，新增一条冲正提现的记录(该金额返还给会员账户)，原流水记录显示为提现错误
	//2013-3-10去掉结算部的提现错误的操作，转账异常后，资金流水则为提现错误(但是该记录为成功状态的)
	//checkFlag checkFlag2 组合意义
	//3 0（待审核）, 
	//4 0（已审核，等待出纳处理） , 
	//4 1（已审核，出纳已划款） ,
	//4 2 （已审核，出纳转账异常，转账异常）需要结算再处理一下：提现错误 4 3 （异常变成错误，会员可看错误的信息）
	//在已审核提现/充值明细中对充值的查询有影响，充值审核通过时将checkFlag2=1即可解决影响
	//异常提现中，展现驳回的提现记录，所以在提现驳回时，将checkFlag2=2即可展现
	private String memo;//备注，驳回时输入，转账异常时输入
	private String checkFlag2 = "0";//出纳操作的状态记录:1表示已划款，2表示转账异常(2014-3-10去掉此状态，直接进入状态3)，3表示提现错误，4表示冲正提现
	private Date hkDate;//划款日期或转账异常日期
	private User hkUser;//划款人
	
	private String checkFlag = "0";
	//0充值等待审核，1充值审核通过+，2充值审核驳回
	//2.5表示交易部A岗提交状态，B岗审核变成3进入等待结算部审核状态-->2013-08-30新增
	//2.9提现待放款,风管部
	//3提现等待审核，4提现审核通过+，5提现审核驳回(checkFlag2=2)
	//6还款划出-  
	//9还款划入+
	//10交割划入+
	//11投标划出-
	//12债权买入-
	//13债权卖出+
	
	//14手续费及税费-，废弃
	
	//15综合管理费,担保费；针对融资方展现-		//2012-9-14由"综合管理费"改为"融资服务费”
	//16综合管理费,担保费；针对第三方(中心账户)展现+		//2012-9-14由"综合管理费"改为"融资服务费”
	
	//17三方结算,结算划出；第三方账户-，废弃
	//18三方结算,结算划入；中心账户+，废弃
	
	//19保证金解冻,融资方冻结金额-,可用余额+
	//20代垫划出,21代垫划入
	//22内部转出,23内部转入,checkFlag2=0表示未审核,checkFlag2=3表示已审核,checkFlag2=4表示已驳回
	//24银转商(不用审核)，入金+
	//25商转银(需要审核)，出金-;25待审核，26审核通过，27审核驳回
	
	//30资金冻结(待审核),31(审核通过),32(审核驳回)
	//33资金解冻(待审核),34(审核通过),35(审核驳回)
	
	//36活期利息(待审核),37(审核通过),38(审核驳回)
	
	//40资金拨入(待审核),41(审核通过),42(审核驳回)
	
	//43资金拨出(资金拨入批量审核通过时才产生资金拨出记录)
	
	
	//51 交易手续费  20140424 新增 投资人收益抽成
	
	
	//还款时用到的金额
	private double bj = 0d;//本金;//债权转让:成交价;//投标额本金
	private double lx = 0d;//利息;//债权转让:手续费;//投资服务费
	private AccountDeal accountDeal;
	
	private String zf;
	
	private double fj = 0d;//罚金;//债权转让:税费
	
	private Date checkDate;
	
	private String batchFlag = "0";
	//批量标志，用于现金充值的批量导入，0表示单笔，1表示excel批量;为兼容已审核充值/已成功提现，将提现视为：单笔
	
	private String zhaiQuanCode;//债权转让时记录债权编号
	
	//2013年10月21日此字段废弃
	private BankLibrary bankLibrary;//入金，出金，记录关联的银行。
	
	/**
	 * 业务标志
	 *   1 现金充值		T 可用+
	 *  21 还款充值		R 可用+
	 *  22 履约充值		R 可用+
	 *   2 普通提现		T R 可用-
	 *  20 融资提现		R 可用-
	 * 
	 *   3 交割划入		R 可用+
	 *   4 还款划出		R 可用-
	 *   5 融资服务费	R 	可用-
	 *   6 担保费		R 可用-
	 *   7保证金解冻		R 可用+，冻结-
	 * 
	 *   8 投标划出		T 可用-
	 *   9 还款划入		T 可用+
	 *  10 债权买入		T 可用-
	 *  11 债权卖出		T 可用+
	 *  债权买入与卖出，一条记录包含了3个费用：成交价，服务费/手续费，税费
	 * 
	 *  12 代垫划出		T R 可用-
	 *  13 代垫划入		T R 可用+
	 *  14 内部转出		T R 可用-
	 *  15 内部转入		T R 可用+
	 *  16 资金冻结		T R 可用-，冻结+
	 *  17 资金解冻		T R 可用+，冻结-
	 * 
	 *  18 银转商,入金	T 可用+，暂时未对融资方开放
	 *  19 商转银,出金	T 可用-，暂时未对融资方开放
	 *  
	 *  23 活期利息		T R 可用+
	 *  24 资金拨入		R 可用+
	 *  25 资金拨出		XYD 可用-
	 *  
	 *  51 交易手续费    T -  20140424 新增 投资人收益抽成
	 */
	//2013-05-07重构流水，主要为方便查询
	private int businessFlag = 0;//业务标志
	private boolean successFlag = false;//业务成功与否标志
	private Date successDate;//业务成功的日期，successFlag=true此字段设定实时的系统日期
	//正式使用的初始化工作
	//1 更新老数据的businessFlag
	//2 更新老数据的成功的各项业务的successFlag=true及successDate=creatDate,checkDate,hkDate
	
	
	private String huidan;//充值必须输入的回单号
	
	/*
	 * 一下三个字段主要用于区分三方存管用户的业务流水
	 * **/
	private String coSerial;//市场流水
	private String bkSerial;//银行流水
	private int txOpt = 0;//交易发起方:交易所=0,银行=1
	private int txDir = 1;//交易转账方向:银行转交易所(入金)(可用增加)=1,交易所转银行(出金)(可用减少)=2
	private int signBank = 0;//三方存管签约行:签约华夏三方存管=1,签约招商三方存管=2
	private int signType = 0;//三方存管签约类型:签约本行=1,签约他行=2
	
	private long time = new Date().getTime();
	
	private int channel = 0;//现金充值；提现；手工方式的资金渠道；0:无渠道，1:招商银行，2:工商银行
	
	private int version=0;
	
	private String codes;
	private double bzj = 0;
	
	public String getBkSerial() {
		return bkSerial;
	}
	public void setBkSerial(String bkSerial) {
		this.bkSerial = bkSerial;
	}
	@Column(columnDefinition="number(1) default 0")
	public int getSignBank() {
		return signBank;
	}
	public void setSignBank(int signBank) {
		this.signBank = signBank;
	}
	@Column(columnDefinition="number(1) default 0")
	public int getSignType() {
		return signType;
	}
	public void setSignType(int signType) {
		this.signType = signType;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@ManyToOne
	public Account getAccount() {
		return account;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getMoney() {
		return money;
	}
	public void setPreMoney(double preMoney) {
		this.preMoney = preMoney;
	}
	public double getPreMoney() {
		return preMoney;
	}
	public void setNextMoney(double nextMoney) {
		this.nextMoney = nextMoney;
	}
	public double getNextMoney() {
		return nextMoney;
	}
	public void setFinancing(FinancingBase financing) {
		this.financing = financing;
	}
	@OneToOne
	public FinancingBase getFinancing() {
		return financing;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	@Transient
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public void setBj(double bj) {
		this.bj = bj;
	}
	public double getBj() {
		return bj;
	}
	public void setLx(double lx) {
		this.lx = lx;
	}
	public double getLx() {
		return lx;
	}
	public void setAccountDeal(AccountDeal accountDeal) {
		this.accountDeal = accountDeal;
	}
	@OneToOne
	public AccountDeal getAccountDeal() {
		return accountDeal;
	}
	public void setCheckUser(User checkUser) {
		this.checkUser = checkUser;
	}
	@ManyToOne
	public User getCheckUser() {
		return checkUser;
	}
	public void setZf(String zf) {
		this.zf = zf;
	}
	@Transient
	public String getZf() {
		if(this.type.equals(CASH)
				||this.type.equals(HKHC)
				||this.type.equals(RZKJG)
				||this.type.equals(SGHC)
				||this.type.equals(ZQMR)
				||this.type.equals(DDHC)
				||this.type.equals(NBZC)
				||this.type.equals(ZQ2BANK)
				||this.type.equals(ZJBC)
				||this.type.equals("资金冻结")
				||this.type.equals("交易手续费")
				||this.type.equals("交易服务费")){
			zf = "-";
		}else{
			if(this.type.equals(ZHGLF)||this.type.equals(DBF)){
				if(this.checkFlag.equals("15")){//针对融资方展现为负
					zf = "-";
				}else if(this.checkFlag.equals("16")){//针对第三方展现为正
					zf = "";
				}else{
					zf = "";
				}
			}else{
				zf = "";
			}
			if(this.type.equals(JSHC)){
				zf = "-";
			}
		}
		return zf;
	}
	public void setFj(double fj) {
		this.fj = fj;
	}
	public double getFj() {
		return fj;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckFlag2(String checkFlag2) {
		this.checkFlag2 = checkFlag2;
	}
	public String getCheckFlag2() {
		return checkFlag2;
	}
	public void setHkDate(Date hkDate) {
		this.hkDate = hkDate;
	}
	public Date getHkDate() {
		return hkDate;
	}
	public void setHkUser(User hkUser) {
		this.hkUser = hkUser;
	}
	@ManyToOne
	public User getHkUser() {
		return hkUser;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return memo;
	}
	public void setBatchFlag(String batchFlag) {
		this.batchFlag = batchFlag;
	}
	public String getBatchFlag() {
		return batchFlag;
	}
	@Transient
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	@Transient
	public String getMoney_upcase() {
		return money_upcase;
	}
	public void setMoney_upcase(String money_upcase) {
		this.money_upcase = money_upcase;
	}
	public void setZhaiQuanCode(String zhaiQuanCode) {
		this.zhaiQuanCode = zhaiQuanCode;
	}
	public String getZhaiQuanCode() {
		return zhaiQuanCode;
	}
	public void setBankLibrary(BankLibrary bankLibrary) {
		this.bankLibrary = bankLibrary;
	}
	
	@ManyToOne
	public BankLibrary getBankLibrary() {
		return bankLibrary;
	}
	public void setBusinessFlag(int businessFlag) {
		this.businessFlag = businessFlag;
	}
	@Column(columnDefinition="number(10) default 0")
	public int getBusinessFlag() {
		return businessFlag;
	}
	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}
	@Column(columnDefinition="number(1) default 0")
	public boolean isSuccessFlag() {
		return successFlag;
	}
	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	public Date getSuccessDate() {
		return successDate;
	}
	public void setHuidan(String huidan) {
		this.huidan = huidan;
	}
	public String getHuidan() {
		return huidan;
	}
	@OneToOne
	public User getFkqruser() {
		return fkqruser;
	}
	
	public void setFkqruser(User fkqruser) {
		this.fkqruser = fkqruser;
	}
	
	public Date getFkqrdate() {
		return fkqrdate;
	}
	public void setFkqrdate(Date fkqrdate) {
		this.fkqrdate = fkqrdate;
	}
	public String getCoSerial() {
		return coSerial;
	}
	public void setCoSerial(String coSerial) {
		this.coSerial = coSerial;
	}
	@Column(columnDefinition="number(1) default 0")
	public int getTxOpt() {
		return txOpt;
	}
	public void setTxOpt(int txOpt) {
		this.txOpt = txOpt;
	}
	@Column(columnDefinition="number(1) default 1")
	public int getTxDir() {
		return txDir;
	}
	public void setTxDir(int txDir) {
		this.txDir = txDir;
	}
	
	@Column(name="time_",columnDefinition="number(30) default 0")
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Column(columnDefinition="number(1) default 0")
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	@Version
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public String getCodes() {
		return codes;
	}
	public void setBzj(double bzj) {
		this.bzj = bzj;
	}
	@Column(columnDefinition="float default 0")
	public double getBzj() {
		return bzj;
	}
	
	
/*	@Transient
	public MemberBase getMemberBase() {
		return memberBase;
	}
	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}
	*/
}
