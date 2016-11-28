package junit4.datainit;

 

import java.util.List;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.service.PaymentRecordService;
import com.kmfex.zhaiquan.model.Contract;
import com.kmfex.zhaiquan.service.ContractService;
 
public class ContractUpdate {
	@Resource static ContractService contractService;
	@Resource static PaymentRecordService paymentRecordService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			contractService = (ContractService) cxt.getBean("contractImpl"); 
			paymentRecordService = (PaymentRecordService) cxt.getBean("paymentRecordImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateData() throws Exception {
		List<Contract> contracts=contractService.getScrollData().getResultlist();
        for(Contract ir: contracts)
        { 
        	ir.setEndDate(ir.getInvestRecord().getLastDate());
        	contractService.update(ir);
        }
	}
}
