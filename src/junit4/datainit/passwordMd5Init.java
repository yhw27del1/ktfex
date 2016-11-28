package junit4.datainit;

 

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.MD5;

/**
 *修改密码为MD5
 */
public class passwordMd5Init {
	private static UserService userService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			userService = (UserService) cxt.getBean("userImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdatePassWordMd5() throws Exception { 
		List<User> users=userService.getScrollData().getResultlist();
		for(User u:users){
			u.setPassword(MD5.MD5Encode("123456"));
			userService.update(u);
		}
		  
	}
}
