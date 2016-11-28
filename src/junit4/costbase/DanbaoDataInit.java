package junit4.costbase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.model.Contact;
import com.wisdoor.core.model.User;

public class DanbaoDataInit {

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
		User memberUser = new User("1", "123456", "1");
		Contact ct = new Contact();
		MemberBase memberBase=new MemberBase();
		memberUser.setUserContact(ct); 
		memberBase.setMemberType(new MemberType("4028819f3657313e01365731455d0003"));
		memberBase.setCategory("0");
		memberBase.seteName("小薇担保公司"); 
		ct.setAddress("");
		ct.setMobile("");
		ct.setPhone("");
		ct.setPostalcode(memberBase.getePostcode());  
		memberBaseService.insert(memberBase); 
	}  
}
