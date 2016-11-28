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

public class InsertDataParamatersTester {
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
 	
		java.util.List<UserParameter> mbs=userParameterService.getCommonListData("from UserParameter mb  ");
 		for(UserParameter mb:mbs){
 			int [] arr4 = {1,2,3,4}; //产生0-(arr.length-1)的整数值,也是数组的索引
 			int index=(int)(Math.random()*arr4.length); 
 			//int rand = arr4[index];
 		    //System.out.println("index="+index+",rand="+rand); 
 		    String str="";
 		    for(int i=0;i<index+1;i++){
 		    	str+=arr4[i]+",";
 		    } 
 		    System.out.println("str4="+str); 
 		    
 		    int [] arr31 = {1,2,3,4}; //产生0-(arr.length-1)的整数值,也是数组的索引
 		    int index31=(int)(Math.random()*arr31.length); 
 		    //System.out.println("index31="+index31+",rand31="+rand31); 
 		    String str31="";
 		    for(int i=0;i<index31+1;i++){
 		    	str31+=arr31[i]+",";
 		    } 
 		    System.out.println("str31="+str31); 
 		    
 		    int [] arr41 = {1,2,3}; //产生0-(arr.length-1)的整数值,也是数组的索引
 		    int index41=(int)(Math.random()*arr41.length); 
 		    //System.out.println("index41="+index41+",rand31="+rand41); 
 		    String str41="";
 		    for(int i=0;i<index41+1;i++){
 		    	str41+=arr41[i]+",";
 		    } 
 		    System.out.println("str41="+str41); 	
 		   Random r = new Random(); 
 		   mb.setParam1(str);
 		   mb.setParam2(str31);
 		   long dayMin=r.nextInt(4)+1;
 		   long monthMin=r.nextInt(9)+1; 
 		  mb.setDayMin(dayMin);
 		  mb.setDayMax(dayMin+2);
 		  mb.setMonthMin(monthMin);
 		  mb.setMonthMax(monthMin+6);
 		   mb.setParam5(str41);
 		   mb.setParam6(Double.valueOf(r.nextInt(30)+""));
 		   mb.setParam7(Double.valueOf(r.nextInt(10)+""));
 		   userParameterService.update(mb);
        } 
		
		
		
	}

 
}
