package com.kmfex.autoinvest.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.model.UserPriority;
import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.service.UserService;

public class BusinessTypeTester {
	private static MemberBaseService memberBaseService;
	private static UserParameterService userParameterService;
	private static UserPriorityService userPriorityService;
	private static UserService userService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext-base.xml");
			memberBaseService = (MemberBaseService) cxt
					.getBean("memberBaseImpl");
			userParameterService = (UserParameterService) cxt
					.getBean("userParameterImpl");
			userPriorityService = (UserPriorityService) cxt
			.getBean("userPriorityImpl");
			userService = (UserService) cxt
			.getBean("userImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMemberBase() throws Exception { 
      //String[] arr=userPriorityService.ConverterParam2("ff8080813c816df5013c8410545900d0");
 	  //System.out.println(arr[0]+"--->"+arr[1]); 
	}

 
}
