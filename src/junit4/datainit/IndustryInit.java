package junit4.datainit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.Industry;
import com.kmfex.service.IndustryService;

/**
 * 初始化行业
 */
public class IndustryInit {
	private static IndustryService industryService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			industryService = (IndustryService) cxt.getBean("industryImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {

		System.out.println(industryService
				.selectByHql("from Industry where name like '%其他%'"));

	}
	//
	// @Test
	// public void initData() throws Exception {
	// industryService.insert(new Industry("计算机（IT）类"));
	// industryService.insert(new Industry("经营/管理类"));
	// industryService.insert(new Industry("销售/客服类"));
	// industryService.insert(new Industry("电子/通讯类"));
	// industryService.insert(new Industry("市场营销/公关/广告类"));
	// industryService.insert(new Industry("行政/文职"));
	// industryService.insert(new Industry("财务/审（统）计类"));
	// industryService.insert(new Industry("房地产/建筑施工类"));
	// industryService.insert(new Industry("金融/保险/证券类"));
	// industryService.insert(new Industry("工程技术"));
	// industryService.insert(new Industry("法律类"));
	// industryService.insert(new Industry("卫生医疗/保健类"));
	// industryService.insert(new Industry("服务类"));
	// industryService.insert(new Industry("咨询/顾问类"));
	// industryService.insert(new Industry("传媒/教育"));
	// industryService.insert(new Industry("其他"));
	//
	//		  
	// }
}
