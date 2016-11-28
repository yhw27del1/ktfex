package junit4.menu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
import com.wisdoor.core.model.Org;
import com.wisdoor.core.service.OrgService;

public class OrgDataInit {
	private static OrgService orgService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			orgService = (OrgService)cxt.getBean("orgImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
  	@Test
	public void initData() throws Exception {
  		orgService.insert(new Org("机构根节点", null, "1", false));
  		orgService.insert(new Org("昆明商公司集团", null, "1m2", false));
  		orgService.insert(new Org("昆明商公司", null, "1m2m3", false));
  		//orgService.insert(new Org("公司代理商", null, "1m2m4", false)); 
	 
	}  
}
