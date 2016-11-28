package com.kmfex.webservice.vo;

import java.io.Serializable;
import java.util.Date;

import com.kmfex.model.InvestRecord;
import com.wisdoor.core.utils.DoubleUtils;
  
/**
 * 投资投标记录
 * @author     
 */
 
public class InvestRecordVo implements Serializable{ 
	private static final long serialVersionUID = -2335271092677136910L; 
	
	
	private MessageTip messageTip; 
	private String id; 
	private FinancingBaseVo financingBaseVo;//融资项目 
	private String investorId;//投资人Id
	private String investorCode;//投资人编号
	private String investorName;//投资人名称 
	private double preAmount;//投标前会员账户额
	private String preAmount_daxie;
	private double investAmount; //投标额
	private String investAmount_daxie;
	private double nextAmount;//投标后会员账户余额
	private String nextAmount_daxie;
	private Date     createDate;   //投标时间
	private String   state;  //0 未确认   1 已确认   2已取消  
	private Date investor_make_sure;//投资人确认合同时间 
	private Date financier_make_sure;//融资方确认合同时间 
	private ContractKeyDataVO contractkeydatavo;//合同 
	
	private String userName; 
	
    //费用明细
	private String costId; 
	private double tzfwf =0d;//投资服务费
	private String tzfwf_daxie; 
    private double   sxf =0d;    //手续费
    private double   yhs =0d;    //印花税
    private double   fee1 =0d;   //其它费1
    private double   fee2 =0d;   //其它费2
    private double   realAmount =0d;   //实际费用    投资记录的投标额+手续费+印花税等费用
    private Date costCreateDate; 
    
    /**
	 * 本金余额
	 */
	private double bjye = 0;
	/**
	 * 利息余额
	 */
	private double lxye = 0;
	
	/**
	 * 成本价
	 * 债权的成本价是会员为获得该债权而支付的本金、交易费用等全部金额的合计
	 */
	private double cbj = 0;
	
	private double qmz = 0;//期末值
	
	private String syl;//当前参考收益率
	
	private Date lastDate; //最后一个还款日(还款时同步更新)
	
	private double crpice = 0;//出让价格 
	private String crpiceStr = "";
 
	private Date xyhkr;//下一还款日
 
	private String zqzrState;//债权挂牌状态;   
 
	private String sellingState;//债权状态
	
	private Date   zqSuccessDate;   //债权协议生成时间(用来判断是否超过5个工作日,超过5个工作日才能转让) 
	
	private int haveNum=1; //累计的持有人数（同一债权持有人数累计不超200）
 
	private String zhaiQuanCode;//债权代码(11位) 
	/**
	 * 债权状态中文显示
	 * */
	private String zqCnState; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
 
	public void setInvestor_make_sure(Date investorMakeSure) {
		investor_make_sure = investorMakeSure;
	}
	public Date getFinancier_make_sure() {
		return financier_make_sure;
	}
	public void setFinancier_make_sure(Date financierMakeSure) {
		financier_make_sure = financierMakeSure;
	}
	
	public ContractKeyDataVO getContractkeydatavo() {
		return contractkeydatavo;
	}
	public void setContractkeydatavo(ContractKeyDataVO contractkeydatavo) {
		this.contractkeydatavo = contractkeydatavo;
	}
	public FinancingBaseVo getFinancingBaseVo() {
		return financingBaseVo;
	}
	public void setFinancingBaseVo(FinancingBaseVo financingBaseVo) {
		this.financingBaseVo = financingBaseVo;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
 
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public Date getInvestor_make_sure() {
		return investor_make_sure;
	}
	public String getInvestorCode() {
		return investorCode;
	}
	public void setInvestorCode(String investorCode) {
		this.investorCode = investorCode;
	}
	public String getCostId() {
		return costId;
	}
	public void setCostId(String costId) {
		this.costId = costId;
	}
	public double getTzfwf() {
		return tzfwf;
	}
	public void setTzfwf(double tzfwf) {
		this.tzfwf = tzfwf;
	}
	public String getTzfwf_daxie() {
		return tzfwf_daxie;
	}
	public void setTzfwf_daxie(String tzfwfDaxie) {
		tzfwf_daxie = tzfwfDaxie;
	}
	public double getSxf() {
		return sxf;
	}
	public void setSxf(double sxf) {
		this.sxf = sxf;
	}
	public double getYhs() {
		return yhs;
	}
	public void setYhs(double yhs) {
		this.yhs = yhs;
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
	public double getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}
	public Date getCostCreateDate() {
		return costCreateDate;
	}
	public void setCostCreateDate(Date costCreateDate) {
		this.costCreateDate = costCreateDate;
	}
	public String getInvestorName() {
		return investorName;
	}
	public MessageTip getMessageTip() {
		return messageTip;
	}
	public void setMessageTip(MessageTip messageTip) {
		this.messageTip = messageTip;
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
	public double getQmz() {
		return this.bjye+this.lxye;
	} 
	public String getSyl() {
		try {
			if(this.cbj==0)
				return "-";
			double syltemp=(((this.bjye+this.lxye)-this.cbj)/this.cbj)*100;
			if(syltemp==0)
				return "-";
			return DoubleUtils.doubleCheck2(syltemp, 2)+"%";
		} catch (Exception e) { 
			e.printStackTrace();
			return "-";
		}
	}
	public double getCbj() {
		return cbj;
	}
	public void setCbj(double cbj) {
		this.cbj = cbj;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public double getCrpice() {
		return crpice;
	}
	public void setCrpice(double crpice) {
		this.crpice = crpice;
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
	public int getHaveNum() {
		return haveNum;
	}
	public void setHaveNum(int haveNum) {
		this.haveNum = haveNum;
	}
	public String getZhaiQuanCode() {
		return zhaiQuanCode;
	}
	public void setZhaiQuanCode(String zhaiQuanCode) {
		this.zhaiQuanCode = zhaiQuanCode;
	}
	public void setQmz(double qmz) {
		this.qmz = qmz;
	}
 
	  
	public String getZqCnState() {
		if(InvestRecord.STATE_SUCCESS.equals(this.zqzrState)){
			if(InvestRecord.SELLINFSTATE_ALL.equals(this.sellingState)){
				return "出让中";
			}else if(InvestRecord.SELLINFSTATE_SUC.equals(this.sellingState)){
				return "成功";
			}else if(InvestRecord.SELLINFSTATE_BUY.equals(this.sellingState)){
				return "求购中";
			}else if(InvestRecord.SELLINFSTATE_SELL.equals(this.sellingState)){
				return "出让中";
			}else{
				return "持有";
			}
			
		}else if(InvestRecord.STATE_RESTART.equals(this.zqzrState)){
			if(InvestRecord.SELLINFSTATE_ALL.equals(this.sellingState)){
				return "出让中";
			}else if(InvestRecord.SELLINFSTATE_SUC.equals(this.sellingState)){
				return "成功";
			}else if(InvestRecord.SELLINFSTATE_BUY.equals(this.sellingState)){
				return "求购中";
			}else if(InvestRecord.SELLINFSTATE_SELL.equals(this.sellingState)){
				return "出让中";
			}else{
				return "持有";
			}
		}else if(InvestRecord.STATE_STOP.equals(this.zqzrState)){
			return "停牌";
		}else if(InvestRecord.STATE_ZHAI.equals(this.zqzrState)){
			return "摘牌";
		}else{
			return "";
		}  
	}
	public void setZqCnState(String zqCnState) {
		this.zqCnState = zqCnState;
	}
	public void setSyl(String syl) {
		this.syl = syl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setCrpiceStr(String crpiceStr) {
		this.crpiceStr = crpiceStr;
	}
	public String getCrpiceStr() {
		return crpiceStr;
	}
	
}
