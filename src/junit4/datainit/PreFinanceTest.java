package junit4.datainit;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.PreFinancingBase;
import com.kmfex.service.PreFinancingBaseService;
 
public class PreFinanceTest {
	private static PreFinancingBaseService preFinancingBaseService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			preFinancingBaseService = (PreFinancingBaseService) cxt.getBean("preFinancingBaseImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
	     try {
	 		System.out.println(preFinancingBaseService.buildPreFinancingCode("D")); 
		    } catch (Exception e) { 
			e.printStackTrace();
		} 
	}
 
	 
}
