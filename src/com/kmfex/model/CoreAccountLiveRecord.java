package com.kmfex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
/**
 * @author linuxp 
 *  2013年09月06日15:50   增加 38 其他费用
 */
@Entity
@Table(name="t_coreaccount_liverecord", schema="KT")
public class CoreAccountLiveRecord {

	
	private String id;
	/**
	 * 动作，指款项用途
	 * 1 融资款交割-划出   				ABX
	 * 2 投资款划入中心账户-划入   		ABX
	 * 3 三方结算-划入				暂无
	 * 4 债权买入-划入				暂无
	 * 5 债权卖出-划入				暂无
	 * 6 融资服务费与罚金-划入		逐月收取    --逐月收取记此状态
	 * 7 担保费-划入					X包  AB暂无	--逐月收取记此状态
	 * 8 风险管理费					暂无		--逐月收取记此状态
	 * 9 交易手续费
	 * 10~20预留			
	 * 
	 * 兴易贷费用（融资服务费和担保费按月计算，逐月收取）
	 * 21 兴易贷费用--风险管理费		X					  --一次性收取方记此状态
	 * 22 兴易贷费用--融资服务费		暂无  应该是使用了状态6  --一次性收取方记此状态
	 * 23 兴易贷费用--担保费			暂无  应该是使用了状态7  --一次性收取方记此状态
	 * 
	 * 
	 * 
	 * 非兴易贷费用（一次性收取）
	 * 31 非兴易贷费用-担保费					A包
	 * 32 非兴易贷费用-风险管理费（按月计算）		A包大多数 X包一个
	 * 33 非兴易贷费用-融资服务费				A
	 * 34 非兴易贷费用-保证金					A
	 * 35 中心账户出账-划出					暂无
	 * 36 席位费								A包3个
	 * 37 信用管理费							暂无
	 * 38 其他费用							暂无
	 * 
	 * 41 内部转帐							暂无
	 * 
	 */
	private int action;
	/**
	 * 发生额，正为进，负为出
	 */
	private double value;
	/**
	 * 发生额，绝对值
	 */
	private double abs_value;
	
	private Date createtime = new Date();
	/**
	 * 对应帐户
	 */
	private Account object_;
	/**
	 * 是否已经处理过
	 */
	private boolean calculated = false;
	
	private Date calculat_time;
	/**
	 * 操作人
	 */
	private User operater;
	/**
	 * 融资项目{
	 * 融资款交割-划出
	 * 投资款划入中心账户-划入
	 * 综合管理费与罚金-划入
	 * 担保费-划入}
	 */
	private FinancingBase fbase;
	
	
	/**
	 * 债权转让 成交价
	 */
	private double zqzr_chengjiaojia;
	/**
	 * 债权转让 服务费
	 */
	private double zqzr_fuwufei;
	/**
	 * 债权转让 税费
	 */
	private double zqzr_shuifei;
	/**
	 * 债权转让 债权编码
	 */
	private String zqzr_zhaiquanbianma;
	
	private String message;
	/**
	 * 收费类型 0：一次性收费；1：逐月收取
	 */
	private int tariff=0;
	
	@Column(columnDefinition="int default 0")
	public int getTariff() {
		return tariff;
	}

	public void setTariff(int tariff) {
		this.tariff = tariff;
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

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@ManyToOne
	public Account getObject_() {
		return object_;
	}

	public void setObject_(Account object_) {
		this.object_ = object_;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}
	/**
	 * 
	 * @return
	 */
	@ManyToOne
	public User getOperater() {
		return operater;
	}

	public void setOperater(User operater) {
		this.operater = operater;
	}

	@ManyToOne
	public FinancingBase getFbase() {
		return fbase;
	}

	public void setFbase(FinancingBase fbase) {
		this.fbase = fbase;
	}

	public double getZqzr_chengjiaojia() {
		return zqzr_chengjiaojia;
	}

	public void setZqzr_chengjiaojia(double zqzr_chengjiaojia) {
		this.zqzr_chengjiaojia = zqzr_chengjiaojia;
	}

	public double getZqzr_fuwufei() {
		return zqzr_fuwufei;
	}

	public void setZqzr_fuwufei(double zqzr_fuwufei) {
		this.zqzr_fuwufei = zqzr_fuwufei;
	}

	public double getZqzr_shuifei() {
		return zqzr_shuifei;
	}

	public void setZqzr_shuifei(double zqzr_shuifei) {
		this.zqzr_shuifei = zqzr_shuifei;
	}

	public String getZqzr_zhaiquanbianma() {
		return zqzr_zhaiquanbianma;
	}

	public void setZqzr_zhaiquanbianma(String zqzr_zhaiquanbianma) {
		this.zqzr_zhaiquanbianma = zqzr_zhaiquanbianma;
	}
	@Column(columnDefinition="float default 0")
	public double getAbs_value() {
		return abs_value;
	}
	
	public void setAbs_value(double abs_value) {
		this.abs_value = abs_value;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Date getCalculat_time() {
		return calculat_time;
	}

	public void setCalculat_time(Date calculat_time) {
		this.calculat_time = calculat_time;
	}

	
	
	
}
