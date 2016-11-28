package test;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.service.TradeTimeService;
import com.kmfex.zhaiquan.model.Selling;
import com.kmfex.zhaiquan.service.ContractService;
import com.kmfex.zhaiquan.service.SellingService;

public class Tester {
	private static ContractService contractService;
	private static TradeTimeService tradeTimeService;
	private static SellingService sellingService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			contractService = (ContractService) cxt.getBean("contractImpl");
			tradeTimeService = (TradeTimeService) cxt.getBean("tradeTimeServiceImpl");
			sellingService = (SellingService)cxt.getBean("sellingImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test(){
		/*Contract contract=new Contract();
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2012);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DATE, 31);
//		contract.setEndDate(calendar.getTime());
//		contract.setPrice(20000.00D);
//		contract.setXieyiCode("201200000000100204");
//		contract.setZhaiQuanCode("201200000000100204005");
		try {
			//contractService.insert(contract);
			PageView<Contract> pv=contractService.listByConditions("976", "4039", null, null, calendar.getTime(), 1, 15);
			
			System.out.println(pv.getRecords().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<Integer, Object> inParamList = new LinkedHashMap<Integer, Object>();
		final OutParameterModel outParameter = new OutParameterModel(2, OracleTypes.CURSOR);
		inParamList.put(1, new java.sql.Date(new Date().getTime()));
		System.out.println("贷:账户金额增加，银行子账户金额增加");
		ArrayList<LinkedHashMap<String,Object>> list_dai = this.contractService.callProcedureForList("PKG_AccountDeal.get_dai", inParamList, outParameter);
		for(int i=0;i<list_dai.size();i++){
			LinkedHashMap<String,Object> a0 = list_dai.get(i);
			System.out.println(a0.get("name"));
			System.out.println(a0.get("money"));
			System.out.println(a0.get("accountno"));
		}
		System.out.println("============================");
		System.out.println("借:账户金额减少，银行子账户金额减少");
		ArrayList<LinkedHashMap<String,Object>> list_jie = this.contractService.callProcedureForList("PKG_AccountDeal.get_jie", inParamList, outParameter);
		for(int i=0;i<list_jie.size();i++){
			LinkedHashMap<String,Object> a0 = list_jie.get(i);
			System.out.println(a0.get("name"));
			System.out.println(a0.get("money"));
			System.out.println(a0.get("accountno"));
		}
		*/
		this.tradeTimeService.checkTradeTime();
		Selling s = this.sellingService.selectByHql("from Selling o where o.investRecord.id='ff80808139f5cc240139f70a16c70114'");
		Assert.assertNotNull(s);
	}
	
}
