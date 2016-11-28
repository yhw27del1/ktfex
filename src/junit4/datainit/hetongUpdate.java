package junit4.datainit;

 

import java.util.List;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.ContractKeyData;
import com.kmfex.model.InvestRecord;
import com.kmfex.service.ContractKeyDataService;
import com.kmfex.service.InvestRecordService;

/**
 * 修改历史数据(把合同的新增字段inverstrecord_id赋值)
 */
public class hetongUpdate {
	@Resource static InvestRecordService investRecordService;
	@Resource static ContractKeyDataService contractKeyDataService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			investRecordService = (InvestRecordService) cxt.getBean("investRecordImpl"); 
			contractKeyDataService = (ContractKeyDataService) cxt.getBean("ContractKeyDataImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateData() throws Exception {
		List<InvestRecord> investRecords=investRecordService.getScrollData().getResultlist();
        for(InvestRecord ir: investRecords)
        {
        	ContractKeyData contract=ir.getContract();
        	contract.setInverstrecord_id(ir.getId());
        	contractKeyDataService.update(contract);
        }
	}
}
