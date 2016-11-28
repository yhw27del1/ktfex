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

public class InsertDataTester {
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss"); 
/*		*/
		java.util.List<MemberBase> mbs=memberBaseService.getCommonListData("from MemberBase mb where mb.user.userAccount.balance>5000 and  mb.user.userType='T' ");
 		for(MemberBase mb:mbs){
			UserParameter up=new UserParameter(mb, Long.parseLong(sdf.format(new Date())));
			userParameterService.insert(up);
			
			UserPriority priority=new UserPriority(mb.getUser().getUsername(),up,Long.parseLong(sdf.format(new Date())),Long.parseLong(sdf.format(new Date())));
			userPriorityService.insert(priority);
			
           System.out.println(mb.getUser().getUsername()+"--->"+mb.getUser().getUserAccount().getBalance());
        }
		
		
		
	}

 
}
