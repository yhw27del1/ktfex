package com.kmfex.model;

import java.io.Serializable; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.kmfex.zhaiquan.model.Contract;
import com.wisdoor.core.utils.DateUtils;

/**
 * 投资投标记录
 * 
 * @author yhw
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_Invest_Record", schema = "KT")
public class InvestRecord implements Serializable {
	/**
	 * 债权挂牌状态--待挂牌
	 */
	public final static String STATE_INIT = "0";
	/**
	 * 债权挂牌状态--挂牌中
	 */
	public final static String STATE_SUCCESS = "1";
	/**
	 * 债权挂牌状态--停牌
	 */
	public final static String STATE_STOP = "2";
	/**
	 * 债权挂牌状态--复牌
	 */
	public final static String STATE_RESTART = "3";
	/**
	 * 债权挂牌状态--摘牌
	 */
	public final static String STATE_ZHAI = "4";
	/**
	 * 债权状态--无意向
	 */
	public final static String SELLINFSTATE_INIT = "0";
	/**
	 * 债权状态--出让中(持有人发起)
	 */
	public final static String SELLINFSTATE_SELL = "1";
	/**
	 * 债权状态--求购中(购买者发起)
	 */
	public final static String SELLINFSTATE_BUY = "2";
	/**
	 * 债权状态--成功
	 */
	public final static String SELLINFSTATE_SUC = "3";
	/**
	 * 债权状态--出让中(既有人买又有人卖)
	 */
	public final static String SELLINFSTATE_ALL = "4";

	private String id;
	private FinancingBase financingBase;
	private MemberBase investor; // 投资人
	private double preAmount;// 投标前会员账户额
	private String preAmount_daxie;
	private double investAmount; // 投标额
	private String investAmount_daxie;
	private double nextAmount;// 投标后会员账户余额
	private String nextAmount_daxie;
	private Date createDate; // 投标时间
	private String state = "1";// 1未签约；2已签约(计算债权)；3取消
	private String months;//剩余还款次数
	private InvestRecordCost cost;

	private String fromApp;// pc,ios,android,autoinvest
	
	private String userParameterId;//委托投标时间点 对应的协议

	/**
	 * 抵扣积分
	 */
	private int credit;

	/**
	 * 合同
	 */
	private ContractKeyData contract;

	/**
	 * 本金余额
	 */
	private double bjye = 0;
	/**
	 * 利息余额
	 */
	private double lxye = 0;

	/**
	 * 成本价 债权的成本价是会员为获得该债权而支付的本金、交易费用等全部金额的合计
	 */
	private double cbj = 0;

	/**
	 * 一笔债权的理论上的最后一个还款日
	 */
	private Date lastDate;

	/**
	 * 出让价格
	 */
	private double crpice = 0;

	/**
	 * 下一还款日
	 */
	private Date xyhkr;

	/**
	 * 债权挂牌状态
	 */
	private String zqzrState = InvestRecord.STATE_INIT;
	/**
	 * 债权状态
	 */
	private String sellingState = InvestRecord.SELLINFSTATE_INIT;

	private Date zqSuccessDate; // 债权协议生成时间(用来判断是否超过5个工作日,超过5个工作日才能转让)

	private int haveNum = 1; // 累计的持有人数（同一债权持有人数累计不超200）
	/**
	 * 投标时系统自动生成 债权代码(11位)： 项目编号(9位)+流水号(3位)，之间用短线分隔
	 */
	private String zhaiQuanCode;

	private List<Contract> contracts = new ArrayList<Contract>();

	private String transferFlag = "1";// 转让标志，为2表示此投标记录被转让过(转让成交)。

	private String memoStr = "";// 辅助标志
	
	private String zqContractID = "";// 债权合同id

	// 在还款，规则变化，成交时更新此字段
	private Date modifyDate = new Date(); // 最新修改时间--客户端更新数据用

	private int version = 0;
	
	/**
	 * 第一持有人，组织机构编码
	 */
	private String orgcode;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne
	public FinancingBase getFinancingBase() {
		return financingBase;
	}

	@ManyToOne
	public MemberBase getInvestor() {
		return investor;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getState() {
		return state;
	}

	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}

	public void setInvestor(MemberBase investor) {
		this.investor = investor;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPreAmount(double preAmount) {
		this.preAmount = preAmount;
	}

	public double getPreAmount() {
		return preAmount;
	}

	public void setNextAmount(double nextAmount) {
		this.nextAmount = nextAmount;
	}

	public double getNextAmount() {
		return nextAmount;
	}

	public void setPreAmount_daxie(String preAmount_daxie) {
		this.preAmount_daxie = preAmount_daxie;
	}

	public String getPreAmount_daxie() {
		return preAmount_daxie;
	}

	public void setInvestAmount_daxie(String investAmount_daxie) {
		this.investAmount_daxie = investAmount_daxie;
	}

	public String getInvestAmount_daxie() {
		return investAmount_daxie;
	}

	public void setNextAmount_daxie(String nextAmount_daxie) {
		this.nextAmount_daxie = nextAmount_daxie;
	}

	public String getNextAmount_daxie() {
		return nextAmount_daxie;
	}

	public void setCost(InvestRecordCost cost) {
		this.cost = cost;
	}

	@OneToOne(mappedBy = "investRecord")
	public InvestRecordCost getCost() {
		return cost;
	}

	/**
	 * 合同
	 * 
	 * @return
	 */
	@OneToOne
	public ContractKeyData getContract() {
		return contract;
	}

	/**
	 * 合同
	 * 
	 * @param contract
	 */
	public void setContract(ContractKeyData contract) {
		this.contract = contract;
	}

	public double getBjye() {
		return bjye;
	}

	public void setBjye(double bjye) {
		this.bjye = bjye;
	}

	public double getLxye() {
		return lxye;
	}

	public void setLxye(double lxye) {
		this.lxye = lxye;
	}

	public Date getXyhkr() {
		return xyhkr;
	}

	public void setXyhkr(Date xyhkr) {
		this.xyhkr = xyhkr;
	}

	/**
	 * 本息合计
	 */
	@Transient
	public double getBxhj() {
		return this.bjye + this.lxye;
	}

	public String getZhaiQuanCode() {
		return zhaiQuanCode;
	}

	public void setZhaiQuanCode(String zhaiQuanCode) {
		this.zhaiQuanCode = zhaiQuanCode;
	}

	public int getHaveNum() {
		return haveNum;
	}

	public void setHaveNum(int haveNum) {
		this.haveNum = haveNum;
	}

	public String getZqzrState() {
		return zqzrState;
	}

	public void setZqzrState(String zqzrState) {
		this.zqzrState = zqzrState;
	}

	public String getSellingState() {
		return sellingState;
	}

	public void setSellingState(String sellingState) {
		this.sellingState = sellingState;
	}

	public Date getZqSuccessDate() {
		return zqSuccessDate;
	}

	public void setZqSuccessDate(Date zqSuccessDate) {
		this.zqSuccessDate = zqSuccessDate;
	}

	public double getCbj() {
		return cbj;
	}

	public void setCbj(double cbj) {
		this.cbj = cbj;
	}

	/*
	 * 债权的期末值为该债权当前交易日尚未收回的本息合计
	 */
	@Transient
	// 期末值
	public double getQmz() {
		return this.bjye + this.lxye;
	}

	/*
	 * 当前参考收益率=（当前期末值-当前成本价）÷当前成本价×100% 当前成本价小于等于0时，当前参考收益率标记为“-”。
	 */
	@Transient
	// 当前参考收益率
	public double getSyl() {
		try {
			if (this.cbj == 0)
				return 0;
			double syltemp = (((this.bjye + this.lxye) - this.cbj) / this.cbj) * 100;
			if (syltemp == 0)
				return 0;
			return syltemp;
		} catch (Exception e) {
			e.printStackTrace();
			return 0d;
		}
	}

	@Transient
	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	@Transient
	public int getDay() {
		if (null != this.zqSuccessDate) {
			return DateUtils.getBetween(this.zqSuccessDate, new Date());
		}
		return 0;
	}

	public double getCrpice() {
		return crpice;
	}

	public void setCrpice(double crpice) {
		this.crpice = crpice;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getMonths() {  
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public void setTransferFlag(String transferFlag) {
		this.transferFlag = transferFlag;
	}

	public String getTransferFlag() {
		return transferFlag;
	}

	public void setMemoStr(String memoStr) {
		this.memoStr = memoStr;
	}

	@Transient
	public String getMemoStr() {
		return memoStr;
	}

	@Column(columnDefinition = "int default 0")
	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(length = 10)
	public String getFromApp() {
		return fromApp;
	}

	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getZqContractID() {
		return zqContractID;
	}

	public void setZqContractID(String zqContractID) {
		this.zqContractID = zqContractID;
	}
	@Column(length = 6)
	public String getOrgcode() {
		return orgcode;
	}
	
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getUserParameterId() {
		return userParameterId;
	}

	public void setUserParameterId(String userParameterId) {
		this.userParameterId = userParameterId;
	}

	
}
