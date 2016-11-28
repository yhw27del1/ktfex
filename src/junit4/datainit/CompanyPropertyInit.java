package junit4.datainit;

 

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
import com.kmfex.model.CompanyProperty; 
import com.kmfex.service.CompanyPropertyService; 

/**
 * 初始化企业性质
 */
public class CompanyPropertyInit {
	private static CompanyPropertyService companyPropertyService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			companyPropertyService = (CompanyPropertyService) cxt.getBean("companyPropertyImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initData() throws Exception {
		
		System.out.println(companyPropertyService
				.selectByHql("from CompanyProperty where name = '个人'"));
		
/*		companyPropertyService.insert(new CompanyProperty("国有"));
		companyPropertyService.insert(new CompanyProperty("集体"));
		companyPropertyService.insert(new CompanyProperty("有限责任公司"));
		companyPropertyService.insert(new CompanyProperty("股份有限公司"));
		companyPropertyService.insert(new CompanyProperty("外商独资"));
		companyPropertyService.insert(new CompanyProperty("中外合资"));
		companyPropertyService.insert(new CompanyProperty("其他"));  */ 
		//新整理 
		//companyPropertyService.insert(new CompanyProperty("100","内资企业",1L,false,null,"1"));
//		companyPropertyService.insert(new CompanyProperty("110","国有企业",110L,true,new CompanyProperty(1L),"1m110"));
//		companyPropertyService.insert(new CompanyProperty("120","集体企业",120L,true,new CompanyProperty(1L),"1m120"));
//		companyPropertyService.insert(new CompanyProperty("130","股份合作企业",130L,true,new CompanyProperty(1L),"1m130"));
//		companyPropertyService.insert(new CompanyProperty("140","联营企业",140L,false,new CompanyProperty(1L),"1m140"));
//		companyPropertyService.insert(new CompanyProperty("141","国有联营企业",141L,true,new CompanyProperty(140L),"1m140m141"));
//		companyPropertyService.insert(new CompanyProperty("142","集体联营企业",142L,true,new CompanyProperty(140L),"1m140m142"));
//		companyPropertyService.insert(new CompanyProperty("143","国有与集体联营企业",143L,true,new CompanyProperty(140L),"1m140m143"));
//		companyPropertyService.insert(new CompanyProperty("149","其他联营企业",149L,true,new CompanyProperty(140L),"1m140m149"));
//		companyPropertyService.insert(new CompanyProperty("150","有限责任公司",150L,false,new CompanyProperty(1L),"1m150"));
//		companyPropertyService.insert(new CompanyProperty("151","国有独资公司",151L,true,new CompanyProperty(150L),"1m150m151"));
//		companyPropertyService.insert(new CompanyProperty("159","其他有限责任公司",159L,true,new CompanyProperty(150L),"1m150m159"));
//		companyPropertyService.insert(new CompanyProperty("160","股份有限公司",160L,true,new CompanyProperty(1L),"1m160"));
//		companyPropertyService.insert(new CompanyProperty("170","私营企业",170L,false,new CompanyProperty(1L),"1m170"));
//		companyPropertyService.insert(new CompanyProperty("171","私营独资企业",171L,true,new CompanyProperty(170L),"1m170m171"));
//		companyPropertyService.insert(new CompanyProperty("172","私营合伙企业",172L,true,new CompanyProperty(170L),"1m170m172"));
//		companyPropertyService.insert(new CompanyProperty("173","私营有限责任公司",173L,true,new CompanyProperty(170L),"1m170m173"));
//		companyPropertyService.insert(new CompanyProperty("174","私营股份有限公司",174L,true,new CompanyProperty(170L),"1m170m174"));
//		companyPropertyService.insert(new CompanyProperty("190","其他企业",190L,true,new CompanyProperty(1L),"1m190"));
//		companyPropertyService.insert(new CompanyProperty("200","港、澳、台商投资企业",2L,false,null,"2"));
//		companyPropertyService.insert(new CompanyProperty("210","合资经营企业（港或澳、台资）",210L,true,new CompanyProperty(2L),"2m210"));
//		companyPropertyService.insert(new CompanyProperty("220","合作经营企业（港或澳、台资）",220L,true,new CompanyProperty(2L),"2m220"));
//		companyPropertyService.insert(new CompanyProperty("230","港、澳、台商独资经营企业",230L,true,new CompanyProperty(2L),"2m230"));
//		companyPropertyService.insert(new CompanyProperty("240","港、澳、台商投资股份有限公司",240L,true,new CompanyProperty(2L),"2m240"));
//		companyPropertyService.insert(new CompanyProperty("290","其他港、澳、台商投资企业",290L,true,new CompanyProperty(2L),"2m290"));
//		companyPropertyService.insert(new CompanyProperty("300","外商投资企业",3L,false,null,"3"));
//		companyPropertyService.insert(new CompanyProperty("310","中外合资经营企业",310L,true,new CompanyProperty(3L),"3m310"));
//		companyPropertyService.insert(new CompanyProperty("320","中外合作经营企业",320L,true,new CompanyProperty(3L),"3m320"));
//		companyPropertyService.insert(new CompanyProperty("330","外资企业",330L,true,new CompanyProperty(3L),"3m330"));

	 

	}
}
