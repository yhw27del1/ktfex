package junit4.datainit;

 

import java.util.List;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.InvestRecord;
import com.kmfex.service.InvestRecordService;

/**
 * 临时补充债权代码
 */
public class investUpdate {
	@Resource static InvestRecordService investRecordService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			investRecordService = (InvestRecordService) cxt.getBean("investRecordImpl");  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateData() throws Exception {
/*		List<InvestRecord> investRecords=investRecordService.getScrollData().getResultlist();
		int count=1;
        for(InvestRecord ir: investRecords)
        {
        	ir.setZhaiQuanCode(ir.getFinancingBase().getCode()+"-"+StringUtils.fillZero(3,(count)+""));
        	investRecordService.update(ir);
        	count++;
        }*/
	}
	
	@Test
	public void query() throws Exception {
 	     List<InvestRecord> investRecords=investRecordService.getScrollData().getResultlist();
        for(InvestRecord ir: investRecords)
        {
         System.out.println("---"+ir.getSyl()+"cbj="+ir.getCbj());
         //System.out.println("---"+DoubleUtils.doubleCheck2(ir.getSyl(), 2)+"%");
        } 
	}
	
}
