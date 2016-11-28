package com.kmfex.zhaiquan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.MoneyFormat;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.CoreAccountLiveRecord;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.PaymentRecord;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.CoreAccountService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Buying;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;
/**
 * 2013-08-23 10:09     修改saveBuying(),记录买卖双方的手续费率 
 */
@Service
@Transactional
public class BuyingImpl extends BaseServiceImpl<Buying> implements
		BuyingService {
	@Resource AccountService accountService;   
	@Resource SellingService sellingService; 
	@Resource InvestRecordService investRecordService;
	@Resource MemberBaseService memberBaseService;
	@Resource ContractService contractService;
	@Resource PaymentRecordService paymentRecordService;
	@Resource AccountDealService accountDealService;
	@Resource
	ChargingStandardService chargingStandardService;// 收费明细
	@Resource CoreAccountService coreAccountService;
	
	
	@Transactional
	public Buying saveBuying(Buying buy)  {
		 
		try {
			InvestRecord investRecord = buy.getInvestRecord();
			FinancingBase fb = investRecord.getFinancingBase();
			if(InvestRecord.SELLINFSTATE_SELL.equals(investRecord.getSellingState())){
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_ALL); 
			}else if(InvestRecord.SELLINFSTATE_INIT.equals(investRecord.getSellingState())){
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_BUY); 
			} 
			investRecordService.update(investRecord);
			//2、冻结账户
			this.accountService.frozenAccount(buy.getBuyer().getUserAccount(), buy.getRealAmount());
			//3、自动撮合
			Selling selling=sellingService.getSellingByUnit(buy.getInvestRecord().getId(), buy.getBuyingPrice());
			if(null!=selling)//有符合条件的
			{ 
				//卖方--收取费用
				CostItem sxf = chargingStandardService.findCostItem("zqfwf", "T");// 债权转让手续费
				CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
				double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100)
						* buy.getBuyingPrice(), 2);
				double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100)
						* buy.getBuyingPrice(), 2);
				selling.setZqfwf(fee);
				selling.setZqsf(taxes);
				double realAmount = buy.getBuyingPrice() - fee - taxes; 
				selling.setRealAmount(realAmount);
					
				
				
				//买方--收取费用
				CostItem sxf1 = chargingStandardService.findCostItem("zqfwf1", "T");// 债权转让手续费
				CostItem sf1 = chargingStandardService.findCostItem("zqsf1", "T");// 税费
				double fee1 = DoubleUtils.doubleCheck((sxf1.getPercent() / 100)
						* buy.getBuyingPrice(), 2);
				double taxes1 = DoubleUtils.doubleCheck((sf1.getPercent() / 100)
						* buy.getBuyingPrice(), 2);	
				buy.setZqfwf(fee1);
				buy.setZqsf(taxes1);
				double realAmount1 = buy.getBuyingPrice() + fee1 + taxes1; 
				buy.setRealAmount(realAmount1);
				
				
				
				double sfAndfwfSell = selling.getZqfwf()+selling.getZqsf();//税费，债权服务费----卖方
				double sfAndfwfbuy = buy.getZqfwf()+buy.getZqsf();//税费，债权服务费----买方 
				
				
				//3.1更新受让记录状态
				buy.setState(Buying.STATE_PASSED);//受让成功 
				
				//3.2更新出让记录状态
				selling.setState(Selling.STATE_PASSED);//出让成功 
				
				//3.3更新投标债权记录 
				investRecord.setHaveNum(investRecord.getHaveNum()+1);//控制不能超过200
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_SUC);
				investRecord.setCrpice(0d);
				investRecord.setZqSuccessDate(new Date());//控制5天内不能在转让
				investRecord.setInvestor(memberBaseService.getMemByUser(buy.getBuyer()));//更新持有人
				investRecord.setTransferFlag("2");
				investRecord.setModifyDate(new Date());
				investRecordService.update(investRecord);
				
				//更新还款记录的款项接受人
				List<PaymentRecord> prs=paymentRecordService.getScrollDataCommon("from PaymentRecord where state='0' and  investRecord.id = ? ", investRecord.getId()); 
				for(PaymentRecord p:prs){
					p.setBeneficiary_id(buy.getBuyer().getId());
					paymentRecordService.update(p);
				}
				
				//3.4生成协议
				Contract contract=new Contract();
				contract.setPercentBuy(sxf1.getPercent());//记录比例
				contract.setPercentSell(sxf.getPercent());
				contract.setBuyer(buy.getBuyer());
				contract.setBuying(buy);
				contract.setBuyerDate(new Date());
				contract.setSeller(selling.getSeller());
				contract.setSellerDate(selling.getCreateDate());
				contract.setSelling(selling);
			    contract.setContract_numbers(selling.getContract_numbers());
				contract.setXieyiCode(contractService.getNextContractCode(buy.getInvestRecord(), buy.getBuyer()));
				contract.setZhaiQuanCode(buy.getZhaiQuanCode());
				contract.setInvestRecord(buy.getInvestRecord());
				contract.setPrice(buy.getBuyingPrice());
				contract.setPrice_dx(MoneyFormat.format(DoubleUtils.doubleCheck2(contract.getPrice(),3),false));
				contract.setFbrq(new Date());
				contract.setSyje(buy.getInvestRecord().getBxhj());
				contract.setSyje_dx(MoneyFormat.format(DoubleUtils.doubleCheck2(contract.getSyje(),3),false));
				contract.setInvestRecord(buy.getInvestRecord());
				contract.setEndDate(buy.getInvestRecord().getLastDate());
				contractService.insert(contract);
				
				investRecord.setZqContractID(contract.getId());//最后一次债权合同ID 
				investRecordService.update(investRecord);
				
				//绑定合同
				buy.setContract(contract);
				//1、保存受让记录
				this.insert(buy);
				selling.setContract(contract); 
				sellingService.update(selling);

				
				//3.5解结交易账户，资金划转
				//12债权买入-
				//13债权卖出+
				//14手续费及税费-
				//买入者，账户解冻，产生减钱流水；
				AccountDeal adbuy = this.accountDealService.accountDealRecord(AccountDeal.ZQMR, "12", buy.getRealAmount());
				Account buyac = this.accountService.selectById(buy.getBuyer().getUserAccount().getId());
				adbuy.setAccount(buyac);
				adbuy.setUser(buy.getBuyer());
				adbuy.setPreMoney(buyac.getBalance()+buyac.getFrozenAmount());
				adbuy.setNextMoney(adbuy.getPreMoney()-adbuy.getMoney());
				adbuy.setCreateDate(DateUtils.getAfterSeccond(new Date(),1));
				adbuy.setBj(buy.getBuyingPrice());//成交价,以买方的出价为准
				adbuy.setLx(buy.getZqfwf());//服务费/手续费,以买方的标准收费
				adbuy.setFj(buy.getZqsf());//税费,以买方的标准收费
				adbuy.setZhaiQuanCode(investRecord.getZhaiQuanCode());
				adbuy.setBusinessFlag(10);
				adbuy.setSuccessFlag(true);
				adbuy.setSuccessDate(adbuy.getCreateDate());
				adbuy.setSignBank(buyac.getUser().getSignBank());//签约行
				adbuy.setSignType(buyac.getUser().getSignType());//签约类型
				adbuy.setTxDir(2);//交易转账方向
				adbuy.setChannel(buyac.getUser().getChannel());//手工专户
				this.accountService.thawAccount2(buyac, adbuy.getMoney(), adbuy);
				
 
				//卖出者，产生加钱流水；
				Account selleraccount = this.accountService.selectById(selling.getSeller().getUserAccount().getId());
				AccountDeal adsell = this.accountDealService.accountDealRecord(AccountDeal.ZQMC, "13", selling.getRealAmount());
				adsell.setAccount(selleraccount);
				adsell.setUser(selling.getSeller());
				adsell.setPreMoney(selleraccount.getBalance()+selleraccount.getFrozenAmount());
				adsell.setNextMoney(adsell.getPreMoney()+adsell.getMoney());
				adsell.setCreateDate(DateUtils.getAfterSeccond(new Date(),4));
				adsell.setBj(buy.getBuyingPrice());//成交价,以买方的出价为准
				adsell.setLx(selling.getZqfwf());//服务费/手续费,以卖方的标准收费
				adsell.setFj(selling.getZqsf());//税费,以卖方的标准收费
				adsell.setZhaiQuanCode(investRecord.getZhaiQuanCode());
				adsell.setBusinessFlag(11);
				adsell.setSuccessFlag(true);
				adsell.setSuccessDate(adsell.getCreateDate());
				adsell.setSignBank(adsell.getUser().getSignBank());//签约行
				adsell.setSignType(adsell.getUser().getSignType());//签约类型
				adsell.setTxDir(1);//交易转账方向
				adsell.setChannel(adsell.getUser().getChannel());//手工专户
				this.accountService.addMoney(selleraccount, adsell.getMoney());
				this.accountDealService.insert(adsell);
				
				//中心账户，产生加钱流水：债权买入费
				CoreAccountLiveRecord calr = new CoreAccountLiveRecord();
				calr.setAction(4);
				calr.setValue(sfAndfwfbuy);
				calr.setFbase(fb);
				calr.setAbs_value(Math.abs(calr.getValue()));
				calr.setObject_(buyac);
				calr.setOperater(buy.getBuyer());
				calr.setCreatetime(DateUtils.getAfterSeccond(new Date(),3));
				calr.setZqzr_chengjiaojia(buy.getBuyingPrice());
				calr.setZqzr_fuwufei(buy.getZqfwf());
				calr.setZqzr_shuifei(buy.getZqsf());
				this.coreAccountService.insert(calr);
				
				//中心账户，产生加钱流水：债权卖出费
				calr = new CoreAccountLiveRecord();
				calr.setAction(5);
				calr.setObject_(selleraccount);
				calr.setFbase(fb);
				calr.setValue(sfAndfwfSell);
				calr.setAbs_value(Math.abs(calr.getValue()));
				calr.setCreatetime(DateUtils.getAfterSeccond(new Date(),6));
				calr.setZqzr_chengjiaojia(buy.getBuyingPrice());//成交价,以买方的出价为准
				calr.setZqzr_fuwufei(selling.getZqfwf());//服务费/手续费,以卖方的标准收费
				calr.setZqzr_shuifei(selling.getZqsf());//税费,以卖方的标准收费
				this.coreAccountService.insert(calr);
 
				
				
				
				//3.6解冻此债权除成功以外其他被冻结的账号 
				List<Buying> buys=this.getCommonListData("from Buying f where f.investRecord.id='"+buy.getInvestRecord().getId()+"' and f.state='0' and f.id !='"+buy.getId()+"'");
				for(Buying b:buys){ 
					this.accountService.thawAccount1(b.getBuyer().getUserAccount(), b.getRealAmount());
				} 
				
				 
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return buy;
	}
	@Override
	@Transactional
	public Buying getBuyingByUnit(String investRecordId,double money) {
		String hql = "from Buying f where f.investRecord.id='"+investRecordId+"' and f.state='0' and f.buyingPrice>='"+money+"' order by f.buyingPrice desc,f.createDate asc";
		List<Buying> ls = this.getCommonListData(hql);
		if(null!=ls&&ls.size()>0){
			return ls.get(0);
		}else{
			return null;
		}
	}
	@Override
	@Transactional
	public void cancel(String buyingId) throws EngineException {
		Buying buying=this.selectById(buyingId); 
		buying.setState(Buying.STATE_NOT_BUY);
			//更新状态
		InvestRecord investRecord = buying.getInvestRecord(); 
		if(InvestRecord.SELLINFSTATE_ALL.equals(investRecord.getSellingState())){
			investRecord.setSellingState(InvestRecord.SELLINFSTATE_SELL); 
		}else if(InvestRecord.SELLINFSTATE_BUY.equals(investRecord.getSellingState())){ 
			    String hql = "from Buying buying where buying.investRecord.id = '"
					+ investRecord.getId() + "' and buying.state = '"+Buying.STATE_NOT+"'";
				if (this.getScrollDataCount(hql)==0) {//说明您是最后一个撤销的人 （改变债权的状态）
					investRecord.setSellingState(InvestRecord.SELLINFSTATE_INIT); 
				}
			} 
		//investRecord.setCrpice(0d);
		if(!Buying.STATE_PASSED.equals(buying.getState())) 
		{
			this.update(buying);
			
		    investRecordService.update(investRecord); 
			//解冻帐号 
			this.accountService.thawAccount1(buying.getBuyer().getUserAccount(), buying.getRealAmount());
 		}
		
		
	}
	@Override
	@Transactional
	public void upZqzrState(String zqzrState,String investRecordId) throws EngineException{
   		InvestRecord investRecord=this.investRecordService.selectById(investRecordId);
		investRecord.setZqzrState(zqzrState);
		investRecordService.update(investRecord); 
		if(InvestRecord.STATE_ZHAI.equals(zqzrState))//如果是摘牌
		{
		    //根据情况解冻帐号 
			List<Buying> buys=this.getCommonListData("from Buying f where f.investRecord.id='"+investRecordId+"' and f.state='0' ");
			for(Buying b:buys){ 
				this.accountService.thawAccount1(b.getBuyer().getUserAccount(), b.getRealAmount());
			} 
		}
		
	}  
	@Override
	public boolean isAlreadyBought(User memberUser, InvestRecord investRecord) {
		if (null != memberUser && null != investRecord) {
			String hql = "from Buying buying where buying.investRecord.id = '"
					+ investRecord.getId() + "' and buying.buyer.id = '"
					+ memberUser.getId() + "' and state = '"+Buying.STATE_NOT+"')";
			if (null != this.selectByHql(hql)) {
				return true;
			}
		}
		return false;
	}
	
}
