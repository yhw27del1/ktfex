package junit4.menu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberBaseService;

public class MemberDataInit {
	private static MemberBaseService memberBaseService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			memberBaseService = (MemberBaseService)cxt.getBean("memberBaseImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
  	@Test
	public void initData() throws Exception { 
  		for(int i=1;i<100;i++)
  		{
  			memberBaseService.insert(new MemberBase("wzg"+i, "1", "pName"+i, "pPhone"+i, " pMobile"+i, "eName"+i, "ePhone"+i, "eMobile"+i,new MemberType("402881fb363e73a101363e7705ff0002")));
  		}
	}  
}
