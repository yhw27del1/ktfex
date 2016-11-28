package com.kmfex.autoinvest.test;

import java.text.SimpleDateFormat;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.service.UserService;

public class InsertDataParamaters2Tester {
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
		  
		java.util.List<UserParameter> mbs=userParameterService.getCommonListData("from UserParameter mb  ");
 		for(UserParameter mb:mbs){ 
 		   Random r = new Random();  
 		   mb.setParam8(Double.valueOf(r.nextInt(30)+"000"));
 		   mb.setParam9(Double.valueOf(r.nextInt(10)+"000"));
 		   userParameterService.update(mb);
        } 
		
		
		
	}

 
}
