package junit4.datainit;

import java.util.List;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.*;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.model.*;
import com.kmfex.model.*;

public class TesterFi {
	@Resource
	private static MemberBaseService memberBaseService;
	@Resource
	private static FinancingBaseService financingBaseService;
	@Resource
	private static MemberGuaranteeService memberGuaranteeService;
	@Resource
	private static InvestRecordService investRecordService;
	@Resource
	private static ContractKeyDataService contractKeyDataService;
	@Resource
	private static FinancingCostService financingCostService;
	@Resource
	private static AccountService accountService;

	private static InvestRecordCostService investRecordCostService;

	private static PaymentRecordService paymentRecordService;

	private static TreatyService treatyService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		memberBaseService = (MemberBaseService) cxt.getBean("memberBaseImpl");

		try {
			financingBaseService = (FinancingBaseService) cxt
					.getBean("financingBaseImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}

		investRecordService = (InvestRecordService) cxt
				.getBean("investRecordImpl");

		contractKeyDataService = (ContractKeyDataService) cxt
				.getBean("contractKeyDataImpl");

		financingCostService = (FinancingCostService) cxt
				.getBean("financingCostImpl");

		investRecordCostService = (InvestRecordCostService) cxt
				.getBean("investRecordCostImpl");

		paymentRecordService = (PaymentRecordService) cxt
				.getBean("paymentRecordImpl");

		treatyService = (TreatyService) cxt.getBean("treatyImpl");

		accountService = (AccountService) cxt.getBean("accountImpl");

	}

	/**
	 * 删除所有融资项目及其相关的信息
	 * */
	@Test
	public void deleteFinancingBase() throws Exception {
		System.out.println("-------------------");

		List<FinancingBase> financingBases = financingBaseService
				.getCommonListData("from FinancingBase ");

		for (int j = 0; j < financingBases.size(); j++) {
			FinancingBase financingBase = financingBases.get(j);
			if (null != financingBase) {
				byte state = Byte.parseByte(financingBase.getState());
				// 此为签约之前的融资项目
				// 删除融资方的融资费用记录
				if (6 == state) {
					FinancingCost financingCost = financingCostService
							.getCostByFinancingBase(financingBase.getId());
					financingCostService.delete(financingCost.getId());
				}

				List<InvestRecord> investRecords = investRecordService
						.getInvestRecordListByFinancingId(financingBase.getId());
				for (int i = 0; i < investRecords.size(); i++) {
					// 此融资项目已有投标记录
					InvestRecord investRecord = investRecords.get(i);
					// 投标人(投资人)
					MemberBase memberBase = investRecord.getInvestor();

					User memberUser = memberBase.getUser();
					Account userAccount = memberUser.getUserAccount();
					// 投标费用明细
					InvestRecordCost investRecordCost = investRecord.getCost();
					if (null != investRecordCost) {
						double amount = investRecordCost.getRealAmount();
						boolean thawed = accountService.thawAccount1(
								userAccount, amount);

						// 删除投标费用明细
						investRecordCostService
								.delete(investRecordCost.getId());
					}

					//删除还款记录
					PaymentRecord paymentRecord = paymentRecordService
							.selectByHql("from PaymentRecord o where o.investRecord.id = '"
									+ investRecord.getId() + "'");
					paymentRecordService.delete(paymentRecord.getId());
					
					//删除协议
					

					// 删除投标记录
					investRecordService.delete(investRecord.getId());

					StringBuilder sqlHql = new StringBuilder(
							"from ContractKeyData o where o.inverstrecord_id = '"
									+ investRecord.getId() + "'");

					// 删除借款合同数据
					ContractKeyData contractKeyData = contractKeyDataService
							.selectByHql(sqlHql.toString());
					contractKeyDataService.delete(contractKeyData.getId());
				}
			}
		}
	}
}
