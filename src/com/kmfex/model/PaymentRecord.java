package com.kmfex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 还款记录
 * @author eclipse
 *
 */
@Entity
@Table(name = "t_payment_record",schema="KT")
public class PaymentRecord implements Serializable{
	private static final long serialVersionUID = 7741750255843699551L;
	
	private String id;
	/**
	 * 投标记录
	 */
	private InvestRecord investRecord;
	
	/**
	 * 当前状态 0未还款 1正常还款 2延期还款(改为：提前还款) 3逾期还款 4担保代偿
	 */
	private int state = 0;
	/**
	 * 是否还在生命周期
	 */
	private boolean live = true;
	/**
	 * 应还款日期
	 */
	private Date predict_repayment_date;
	/**
	 * 实际还款日期
	 */
	private Date actually_repayment_date;
	
	/**
	 * 协议本金(应还本金)
	 */
	private double xybj;
	/**
	 * 协议利息(应还利息)
	 */
	private double xylx;
	
	
	/**
	 * 实还本金
	 */
	private double shbj;
	/**
	 * 实还利息 
	 */
	private double shlx;
	
	
	/**
	 * 实际还款额
	 */
	private double paid_debt;
	
	/**
	 * 当前还款次数，如，当前为该次投标的第3次还款，此数值在生成还款记录时自动生成
	 */
	private int succession;
	
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 备注私有
	 */
	private String remark2;
	
	/**
	 * 审核状态 0未处理 1待审 2通过 
	 */
	private int approve = 0;
	
	/**
	 * 还款期限提示  如：已经到期，过期，逾期
	 */
	private String alt;
	
	/**
	 * 延期期限
	 */
	private Date extension_period;
	
	/**
	 * 逾期罚金
	 */
	private double penal = 0;
	
	
	
	/**
	 * 兴易贷费用--融资服务费(总计)：借款初始金额的1%/月，按月计收
	 */
	private double fee_1 = 0;
	/**
	 * 兴易贷费用--担保费(总计)：借款初始金额的0.3%/月，按月计收
	 */
	private double fee_2 = 0;
	/**
	 * 风险管理费 20140124新增，按月
	 */
	private double fee_3 = 0;
	/**
	 * 交易手续费_协议
	 */
	private double xy_fee_4 = 0d;
	
	private double zhglf_fj = 0;//现在的融资服务费
	private double dbf_fj = 0;//担保费罚金
	private double fee_3_fj = 0;//风险管理费罚金
	/**
	 * 
	 * 交易手续费_实收
	 */
	private double ss_fee_4 = 0d;
	
	
	/**
	 * 逾期天数
	 */
	private int overdue_days = 0;
	/**
	 * 此笔款项接受人
	 */
	private long beneficiary_id;
	private String beneficiary_name;
	/**
	 * 20140512增加
	 * 还款时受益人的所在机构
	 * 因有可能受益人的机构有所变动。
	 * 为了统计交易手续费分成
	 * 
	 */
	private long beneficiary_org_id;
	
	/**
	 * 组别(同期还款中的分组)
	 */
	private int group;
	
	/**
	 * 还款操作人ID
	 */
	private long operator_id;
	/**
	 * 还款审核人ID
	 */
	private long auditor_id;
	
	private String batch_no;
	
	private long batch_date;
	
	private int cash_pool;
	
	
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 投标记录
	 * @return
	 */
	@ManyToOne
	public InvestRecord getInvestRecord() {
		return investRecord;
	}
	/**
	 * 投标记录
	 * @param investRecord
	 */
	public void setInvestRecord(InvestRecord investRecord) {
		this.investRecord = investRecord;
	}
	/**
	 * 当前状态 0未还款 1正常还款 2延期还款(改为：提前还款) 3逾期还款 4担保代偿
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * 当前状态 0未处理 1已还 2延期 3逾期
	 * @return
	 */
	public int getState() {
		return state;
	}
	/**
	 * 应还款日期
	 * @return
	 */
	public Date getPredict_repayment_date() {
		return predict_repayment_date;
	}
	/**
	 * 应还款日期
	 * @param predictRepaymentDate
	 */
	public void setPredict_repayment_date(Date predictRepaymentDate) {
		predict_repayment_date = predictRepaymentDate;
	}
	/**
	 * 实际还款日期
	 * @return
	 */
	public Date getActually_repayment_date() {
		return actually_repayment_date;
	}
	/**
	 * 实际还款日期
	 * @param actuallyRepaymentDate
	 */
	public void setActually_repayment_date(Date actuallyRepaymentDate) {
		actually_repayment_date = actuallyRepaymentDate;
	}
	/**
	 * 实际还款额
	 * @return
	 */
	public double getPaid_debt() {
		return paid_debt;
	}
	/**
	 * 实际还款额
	 * @param paidDebt
	 */
	public void setPaid_debt(double paidDebt) {
		paid_debt = paidDebt;
	}
	
	@Column(columnDefinition="number(10,2) default 0")
	public double getXybj() {
		return xybj;
	}
	public void setXybj(double xybj) {
		this.xybj = xybj;
	}
	@Column(columnDefinition="number(10,2) default 0")
	public double getXylx() {
		return xylx;
	}
	public void setXylx(double xylx) {
		this.xylx = xylx;
	}
	
	
	
	
	
	
	@Column(columnDefinition="number(10,2) default 0")
	public double getShbj() {
		return shbj;
	}
	public void setShbj(double shbj) {
		this.shbj = shbj;
	}
	@Column(columnDefinition="number(10,2) default 0")
	public double getShlx() {
		return shlx;
	}
	public void setShlx(double shlx) {
		this.shlx = shlx;
	}
	/**
	 * 是否还在生命周期
	 * @param live
	 */
	
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * 是否还在生命周期
	 * @return
	 */
	public boolean isLive() {
		return live;
	}
	/**
	 * 还款次序
	 * @return
	 */
	@Column(columnDefinition="number(2) default 0",nullable=false)
	public int getSuccession() {
		return succession;
	}
	/**
	 * 还款次序
	 * @param succession
	 */
	public void setSuccession(int succession) {
		this.succession = succession;
	}
	
	/**
	 * 备注
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 备注
	 * @return
	 */
	@Column(name="remark_",nullable=true)   
	public String getRemark() {
		return remark;
	}
	public void setApprove(int approve) {
		this.approve = approve;
	}
	public int getApprove() {
		return approve;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	@Transient
	public String getAlt() {
		return alt;
	}
	/**
	 * 延期期限
	 * @param extension_period
	 */
	public void setExtension_period(Date extension_period) {
		this.extension_period = extension_period;
	}
	/**
	 * 延期期限
	 * @return
	 */
	public Date getExtension_period() {
		return extension_period;
	}
	@Column(columnDefinition="float default 0")
	public double getPenal() {
		return penal;
	}
	public void setPenal(double penal) {
		this.penal = penal;
	}
	public double getFee_1() {
		return fee_1;
	}
	public void setFee_1(double fee_1) {
		this.fee_1 = fee_1;
	}
	public double getFee_2() {
		return fee_2;
	}
	public void setFee_2(double fee_2) {
		this.fee_2 = fee_2;
	}
	public void setZhglf_fj(double zhglf_fj) {
		this.zhglf_fj = zhglf_fj;
	}
	public double getZhglf_fj() {
		return zhglf_fj;
	}
	public void setDbf_fj(double dbf_fj) {
		this.dbf_fj = dbf_fj;
	}
	public double getDbf_fj() {
		return dbf_fj;
	}
	
	
	@Column(columnDefinition="float default 0")
	public double getFee_3() {
		return fee_3;
	}
	public void setFee_3(double fee_3) {
		this.fee_3 = fee_3;
	}
	
	@Column(columnDefinition="float default 0")
	public double getFee_3_fj() {
		return fee_3_fj;
	}
	public void setFee_3_fj(double fee_3_fj) {
		this.fee_3_fj = fee_3_fj;
	}
	@Column(columnDefinition="number(3) default 0")
	public int getOverdue_days() {
		return overdue_days;
	}
	public void setOverdue_days(int overdue_days) {
		this.overdue_days = overdue_days;
	}
	@Column(columnDefinition="number(19) default 0")
	public long getBeneficiary_id() {
		return beneficiary_id;
	}
	public void setBeneficiary_id(long beneficiary_id) {
		this.beneficiary_id = beneficiary_id;
	}
	@Column(name="group_",columnDefinition="number(3) default 0")
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	@Transient
	public PaymentRecord getClone(){
		PaymentRecord result = new PaymentRecord();
		result.setAlt(this.getAlt());
		result.setExtension_period(this.getExtension_period());
		result.setInvestRecord(this.getInvestRecord());
		result.setLive(this.isLive());
		result.setPredict_repayment_date(this.getPredict_repayment_date());
		result.setState(this.getState());
		result.setSuccession(this.getSuccession());
		return result;
	}
	
	@Transient
	public String getBeneficiary_name() {
		return beneficiary_name;
	}
	public void setBeneficiary_name(String beneficiary_name) {
		this.beneficiary_name = beneficiary_name;
	}
	@Column(columnDefinition="int default 0")
	public long getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(long operator_id) {
		this.operator_id = operator_id;
	}
	@Column(columnDefinition="int default 0")
	public long getAuditor_id() {
		return auditor_id;
	}
	public void setAuditor_id(long auditor_id) {
		this.auditor_id = auditor_id;
	}
	@Column(length=20)
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	@Column(columnDefinition="number default 0")
	public long getBatch_date() {
		return batch_date;
	}
	public void setBatch_date(long batch_date) {
		this.batch_date = batch_date;
	}
	
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	
	@Column(columnDefinition="number(7,2) default 0")
	public double getXy_fee_4() {
		return xy_fee_4;
	}
	public void setXy_fee_4(double xy_fee_4) {
		this.xy_fee_4 = xy_fee_4;
	}
	@Column(columnDefinition="number(7,2) default 0")
	public double getSs_fee_4() {
		return ss_fee_4;
	}
	public void setSs_fee_4(double ss_fee_4) {
		this.ss_fee_4 = ss_fee_4;
	}
	
	@Column(columnDefinition="number(19) default 0")
	public long getBeneficiary_org_id() {
		return beneficiary_org_id;
	}
	public void setBeneficiary_org_id(long beneficiary_org_id) {
		this.beneficiary_org_id = beneficiary_org_id;
	}
	
	@Column(columnDefinition="number(2) default -1")
	public int getCash_pool() {
		return cash_pool;
	}
	public void setCash_pool(int cashPool) {
		cash_pool = cashPool;
	}
	
	
	
	
	
}

