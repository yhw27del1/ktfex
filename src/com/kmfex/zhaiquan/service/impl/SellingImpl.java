package com.kmfex.zhaiquan.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.InvestRecord;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Buying;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.BuyingService;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
@Transactional
public class SellingImpl extends BaseServiceImpl<Selling> implements
		SellingService {
	@Resource AccountService accountService;
	@Resource BuyingService buyingService;
	@Resource MemberBaseService memberBaseService;
	@Resource InvestRecordService investRecordService;
	@Resource ContractService contractService;
    @Resource PaymentRecordService paymentRecordService;
    @Resource AccountDealService accountDealService;
	@Resource ChargingStandardService chargingStandardService;// 收费明细
	@Override
	@Transactional
	public Selling saveSelling(Selling selling)  {
		try {
			// 1、保存出让记录 
			this.insert(selling);
			InvestRecord investRecord = selling.getInvestRecord();
			if (InvestRecord.SELLINFSTATE_BUY.equals(investRecord.getSellingState())) {
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_ALL);
				
			} else if (InvestRecord.SELLINFSTATE_INIT.equals(investRecord
					.getSellingState())) {
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_SELL);
				
			}
			investRecord.setSellingState(InvestRecord.SELLINFSTATE_SELL);//出让中
			investRecord.setCrpice(selling.getSellingPrice());
			investRecord.setModifyDate(selling.getCreateDate());   //更新债权 时间戳
			investRecordService.update(investRecord);
		/*	// 2、自动撮合
			Buying buying = buyingService.getBuyingByUnit(selling.getInvestRecord().getId(), selling.getSellingPrice());
			if (null != buying)// 有符合条件的
			{
				//卖方
				CostItem sxf = chargingStandardService.findCostItem("zqfwf",
						"T");// 债权转让手续费
				CostItem sf = chargingStandardService.findCostItem("zqsf", "T");// 税费
				double fee = DoubleUtils.doubleCheck((sxf.getPercent() / 100)
						* buying.getBuyingPrice(), 2);
				double taxes = DoubleUtils.doubleCheck((sf.getPercent() / 100)
						* buying.getBuyingPrice(), 2);
				selling.setZqfwf(fee);
				selling.setZqsf(taxes);
				double realAmount = buying.getBuyingPrice() - fee - taxes;
				selling.setRealAmount(realAmount);
				//买方
				CostItem sxf1 = chargingStandardService.findCostItem("zqfwf1",
						"T");// 债权转让手续费
				CostItem sf1 = chargingStandardService.findCostItem("zqsf1",
						"T");// 税费
				double fee1 = DoubleUtils.doubleCheck((sxf1.getPercent() / 100)
						* buying.getBuyingPrice(), 2);
				double taxes1 = DoubleUtils.doubleCheck(
						(sf1.getPercent() / 100) * buying.getBuyingPrice(), 2);
				buying.setZqfwf(fee1);
				buying.setZqsf(taxes1);
				double realAmount1 = buying.getBuyingPrice() + fee1 + taxes1;
				buying.setRealAmount(realAmount1);

				double sfAndfwfSell = selling.getZqfwf() + selling.getZqsf();//税费，债权服务费----卖方
				double sfAndfwfbuy = buying.getZqfwf() + buying.getZqsf();//税费，债权服务费----买方

				// 2.1更新出让记录状态
				selling.setState(Buying.STATE_PASSED);// 出让成功 
				// 2.2更新受让记录状态
				buying.setState(Selling.STATE_PASSED);// 受让成功 
				// 2.3更新投标债权记录 
				investRecord.setSellingState(InvestRecord.SELLINFSTATE_SUC);
				investRecord.setCrpice(0d);
				investRecord.setHaveNum(investRecord.getHaveNum() + 1);// 控制不能超过200 
				investRecord.setZqSuccessDate(new Date());// 控制5天内不能在转让
				investRecord.setInvestor(memberBaseService.getMemByUser(buying
						.getBuyer()));// 更新持有人
				investRecord.setTransferFlag("2");
				investRecordService.update(investRecord);
				// 2.4生成协议
				Contract contract = new Contract();
				contract.setBuyer(buying.getBuyer());
				contract.setBuying(buying);
				contract.setBuyerDate(buying.getCreateDate());
				contract.setSeller(selling.getSeller());
				contract.setSellerDate(new Date());
				contract.setSelling(selling);
				contract.setXieyiCode(contractService.getNextContractCode(
						selling.getInvestRecord(), selling.getSeller()));
				contract.setContract_numbers(selling.getContract_numbers());
				contract.setZhaiQuanCode(selling.getZhaiQuanCode());
				contract.setInvestRecord(selling.getInvestRecord());
				contract.setPrice(buying.getBuyingPrice());
				contract.setPrice_dx(MoneyFormat.format(DoubleUtils
						.doubleCheck2(contract.getPrice(), 3), false));
				contract.setFbrq(new Date());
				contract.setSyje(buying.getInvestRecord().getBxhj());
				contract.setSyje_dx(MoneyFormat.format(DoubleUtils
						.doubleCheck2(contract.getSyje(), 3), false));
				contract.setInvestRecord(buying.getInvestRecord());
				contract.setEndDate(paymentRecordService
						.getLastRepayTime(buying.getInvestRecord()));
				contractService.insert(contract);

				//绑定合同
				selling.setContract(contract);
				this.update(selling);
				buying.setContract(contract);
				buyingService.update(buying);

				//2.5解结账户
				//卖出者，产生加钱流水；产生扣税费及手续费流水
				Account selleraccount = this.accountService.selectById(selling
						.getSeller().getUserAccount().getId());
				AccountDeal adse = this.accountDealService.accountDealRecord(
						AccountDeal.CRHR, "13", buying.getBuyingPrice());
				adse.setAccount(selleraccount);
				adse.setUser(selling.getSeller());
				adse.setPreMoney(selleraccount.getBalance()
						+ selleraccount.getFrozenAmount());
				adse.setNextMoney(adse.getPreMoney() + adse.getMoney());
				adse.setCreateDate(DateUtils.getAfterSeccond(new Date(), 1));
				this.accountService.addMoney(selleraccount, adse.getMoney());
				this.accountDealService.insert(adse);
				AccountDeal adse2 = this.accountDealService.accountDealRecord(
						AccountDeal.SFANDFWF, "14", sfAndfwfSell);
				selleraccount = this.accountService.selectById(selling
						.getSeller().getUserAccount().getId());
				adse2.setAccount(selleraccount);
				adse2.setUser(selling.getSeller());
				adse2.setPreMoney(selleraccount.getBalance()
						+ selleraccount.getFrozenAmount());
				adse2.setNextMoney(adse2.getPreMoney() - adse2.getMoney());
				adse2.setCreateDate(DateUtils.getAfterSeccond(new Date(), 2));
				this.accountService.loseMoney(selleraccount, adse2.getMoney());
				this.accountDealService.insert(adse2);

				//中心账户，产生加钱流水：债权出让费
				Account c = this.accountService.centerAccount();
				AccountDeal centerad1 = this.accountDealService
						.accountDealRecord(AccountDeal.ZQCRF, "0", sfAndfwfSell);
				centerad1.setPreMoney(c.getBalance() + c.getFrozenAmount());
				centerad1.setNextMoney(centerad1.getPreMoney()
						+ centerad1.getMoney());
				centerad1.setAccount(c);
				centerad1.setCreateDate(DateUtils
						.getAfterSeccond(new Date(), 3));
				this.accountService.addMoney(c, centerad1.getMoney());
				this.accountDealService.insert(centerad1);

				//买入者，
				//买入者，账户解冻，产生减钱流水
				AccountDeal ad = this.accountDealService.accountDealRecord(
						AccountDeal.SRHC, "12", buying.getBuyingPrice());
				Account buyac = this.accountService.selectById(buying
						.getBuyer().getUserAccount().getId());
				ad.setAccount(buyac);
				ad.setUser(buying.getBuyer());
				ad.setPreMoney(buyac.getBalance() + buyac.getFrozenAmount());
				ad.setNextMoney(ad.getPreMoney() - ad.getMoney());
				ad.setCreateDate(DateUtils.getAfterSeccond(new Date(), 4));
				this.accountService.thawAccount2(buyac, ad.getMoney(), ad);
				AccountDeal ad2 = this.accountDealService.accountDealRecord(
						AccountDeal.SFANDFWF, "14", sfAndfwfbuy);
				buyac = this.accountService.selectById(buying.getBuyer()
						.getUserAccount().getId());
				ad2.setAccount(buyac);
				ad2.setUser(buying.getBuyer());
				ad2.setPreMoney(buyac.getBalance() + buyac.getFrozenAmount());
				ad2.setNextMoney(ad2.getPreMoney() - ad2.getMoney());
				ad2.setCreateDate(new Date());
				this.accountService.thawAccount2(buyac, ad2.getMoney(), ad2);
				this.accountDealService.insert(ad2);  

				//中心账户，产生加钱流水：债权出让费
				c = this.accountService.centerAccount();
				AccountDeal centerad2 = this.accountDealService
						.accountDealRecord(AccountDeal.ZQSRF, "0", sfAndfwfbuy);
				centerad2.setPreMoney(c.getBalance() + c.getFrozenAmount());
				centerad2.setNextMoney(centerad2.getPreMoney()
						+ centerad2.getMoney());
				centerad2.setAccount(c);
				centerad2.setCreateDate(DateUtils
						.getAfterSeccond(new Date(), 5));
				this.accountService.addMoney(c, centerad2.getMoney());
				this.accountDealService.insert(centerad2);

				//2.7解冻此债权除成功以外其他被冻结的账号 
				List<Buying> buys = buyingService
						.getCommonListData("from Buying f where f.investRecord.id='"
								+ buying.getInvestRecord().getId()
								+ "' and f.state='0' and f.id !='"
								+ buying.getId() + "'");
				;
				for (Buying b : buys) {
					this.accountService.thawAccount1(b.getBuyer()
							.getUserAccount(), b.getRealAmount());
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selling;
	}

	@Override
	@Transactional
	public Selling getSellingByUnit(String investRecordId, double money) {
		String hql = "from Selling f where f.investRecord.id='"
				+ investRecordId + "' and f.state='0' and f.sellingPrice<='"
				+ money + "'  order by f.sellingPrice desc,f.createDate asc";
		List<Selling> ls = this.getCommonListData(hql);
		if (null != ls && ls.size() > 0) {
			return ls.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public void cancel(String buyingId) throws EngineException {
		Selling selling = this.selectById(buyingId);
		selling.setState(Selling.STATE_NOT_BUY);
		
		//更新状态
		InvestRecord investRecord = selling.getInvestRecord(); 
		investRecord.setSellingState(InvestRecord.SELLINFSTATE_INIT);
		investRecord.setCrpice(0d);
		if(!Selling.STATE_PASSED.equals(selling.getState())) 
		{
		  this.update(selling);
		  investRecordService.update(investRecord);	
		}
	}
	@Override
	public boolean isAlreadySold(User memberUser, InvestRecord investRecord) {
		if (null != memberUser && null != investRecord) {
			String hql = "from Selling selling where selling.investRecord.id = '"
					+ investRecord.getId()
					+ "' and selling.seller.id = '"
					+ memberUser.getId() + "' and state = '"+Selling.STATE_NOT+"'";
			if(null!=this.selectByHql(hql)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void _cancel(String investrecord_id) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" from Selling s where s.investRecord.id = ? and s.state = ?");
			List<Selling> list = this.getScrollDataCommon(sql.toString(), investrecord_id,Selling.STATE_NOT);
			for(Selling s : list){
				this.cancel(s.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
