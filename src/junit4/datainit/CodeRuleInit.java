package junit4.datainit;

 

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.CodeRule;
import com.wisdoor.core.service.CodeRuleService;

/**
 * 初始化规则
 */
public class CodeRuleInit {
	private static CodeRuleService codeRuleService; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			codeRuleService = (CodeRuleService) cxt.getBean("codeRuleImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initData() throws Exception {  
       codeRuleService.insert(new CodeRule("A", 7L,"13","本金担保"));
       codeRuleService.insert(new CodeRule("B", 30L,"12","本息担保"));
       codeRuleService.insert(new CodeRule("C", 0L,"13","无担保"));
       codeRuleService.insert(new CodeRule("D", 0L,"13","特殊融资项目只能机构能投标"));
       codeRuleService.insert(new CodeRule("X", 176L,"13","兴易贷融资项目"));  
	}
}
