package com.kmfex.autoinvest.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.autoinvest.utils.AutoInvestUtils;
import com.kmfex.autoinvest.vo.Draw;
import com.kmfex.autoinvest.vo.PreParams;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.service.UserService;

public class DrawByPreParamsTester {
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
	public void testDrawByPreParams() throws Exception {
		PreParams preParams=new PreParams();
		preParams.setParam1(AutoInvestUtils.ConverterParam1("1.5"));//风险评级 
 
		preParams.setBalance(500000d); 
		preParams.setParam5(AutoInvestUtils.ConverterParam5("12"));//担保方式
		
		Map<String, Object> map=userParameterService.drawByPreParams(preParams,null); 
		List<Draw> draws=(ArrayList<Draw>)map.get("draws"); 
		for(Draw dw:draws){
			System.out.println(dw.getUsername()+"--dw-->"+dw.getBalance()+"---->"+dw.getMax());	
		}
	    List<Draw> draws2=userPriorityService.draw1(map, 400000d);
		System.out.println("委托金额---->"+map.get("w"));
		System.out.println("委托人可用余额---->"+map.get("sum_balance"));
		System.out.println("符合条件的委托人---->"+map.get("n"));
		System.out.println("预投标的人数(中签)---->"+draws2.size());
		Double moneyDouble=0d;
		for(Draw dw2:draws2){
			System.out.println(dw2.getUsername()+"--dw2-->"+dw2.getBalance()+"---->"+dw2.getPrePrice());	
			moneyDouble+=dw2.getPrePrice();
		}
		System.out.println("draws2——money---->"+moneyDouble);
        
    }  
 
}
