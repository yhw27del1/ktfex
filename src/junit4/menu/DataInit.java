package junit4.menu;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.CostBase;
import com.kmfex.model.CostItem;
import com.kmfex.model.MemberType;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.CostCategoryService;
import com.kmfex.service.MemberTypeService;

/**
 * 初始化会员类型，收费项目和收费标准的相关数据
 * 
 * @author 敖汝安
 * @version 2012-03-27
 */
public class DataInit {
	private static MemberTypeService memberTypeService;
	private static CostCategoryService costCategoryService;
	private static ChargingStandardService costItemService ;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			memberTypeService = (MemberTypeService) cxt
					.getBean("memberTypeImpl");
			costCategoryService = (CostCategoryService) cxt
			.getBean("costCategoryImpl");
			costItemService=(ChargingStandardService) cxt
			.getBean("chargingStandardImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initData() throws Exception {
		Map<String,MemberType> memberTypes=new HashMap<String,MemberType>();
		Map<String,CostBase> costBases=new HashMap<String,CostBase>();
		
		if (null != memberTypeService) {
			MemberType mt = new MemberType();
			mt.setName("投资方");
			mt.setCode("T");
			mt.setCreateDate(new Date());
			memberTypeService.insert(mt);
			memberTypes.put("投资方",mt);
			
			mt = new MemberType();
			mt.setName("融资方");
			mt.setCode("R");
			mt.setCreateDate(new Date());
			memberTypeService.insert(mt);
			memberTypes.put("融资方",mt);

			mt = new MemberType();
			mt.setName("担保方");
			mt.setCode("D");
			mt.setCreateDate(new Date());
			memberTypeService.insert(mt);
			memberTypes.put("担保方",mt);

			mt = new MemberType();
			mt.setName("银行");
			mt.setCode("Y");
			mt.setCreateDate(new Date());
			memberTypeService.insert(mt);
			memberTypes.put("银行",mt);

			mt = new MemberType();
			mt.setName("其它");
			mt.setCode("Q");
			mt.setCreateDate(new Date());
			memberTypeService.insert(mt);
			memberTypes.put("其它",mt);
		}
		
		CostBase cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("开户费");
		cb.setCode("khf");
		costCategoryService.insert(cb);
		costBases.put("开户费", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("席位费(年费)");
		cb.setCode("xwf");
		costCategoryService.insert(cb);
		costBases.put("席位费(年费)", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("担保费");
		cb.setCode("dbf");
		costCategoryService.insert(cb);
		costBases.put("担保费", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("投资服务费");
		cb.setCode("tzfwf");
		costCategoryService.insert(cb);
		costBases.put("投资服务费", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("风险管理费(按月)");
		cb.setCode("fxglf");
		costCategoryService.insert(cb);
		costBases.put("风险管理费(按月)", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("融资服务费");
		cb.setCode("rzfwf");
		costCategoryService.insert(cb);
		costBases.put("融资服务费", cb);
		
		cb = new CostBase();
		cb.setAddtime(new Date());
		cb.setName("保证金");
		cb.setCode("bzj");
		costCategoryService.insert(cb);
		costBases.put("保证金", cb);
		
		CostItem ci=new CostItem();
		ci.setCostBase(costBases.get("开户费"));		
		ci.setMemberTypel(memberTypes.get("融资方"));
		ci.setPercent(0.0D);
		ci.setMoney(500D);
		costItemService.insert(ci);
		
		ci=new CostItem();
		ci.setCostBase(costBases.get("担保费"));		
		ci.setMemberTypel(memberTypes.get("融资方"));
		ci.setPercent(10.0D);
		ci.setMoney(0D);
		costItemService.insert(ci);
		
		ci=new CostItem();
		ci.setCostBase(costBases.get("风险管理费(按月)"));		
		ci.setMemberTypel(memberTypes.get("融资方"));
		ci.setPercent(0.3D);
		ci.setMoney(0D);
		costItemService.insert(ci);
		
		ci=new CostItem();
		ci.setCostBase(costBases.get("投资服务费"));		
		ci.setMemberTypel(memberTypes.get("投资方"));
		ci.setPercent(0.2D);
		ci.setMoney(0D);
		costItemService.insert(ci);
		
		ci=new CostItem();
		ci.setCostBase(costBases.get("保证金"));		
		ci.setMemberTypel(memberTypes.get("融资方"));
		ci.setPercent(0.0D);
		ci.setMoney(10D);
		costItemService.insert(ci);
		
		ci=new CostItem();
		ci.setCostBase(costBases.get("融资服务费"));		
		ci.setMemberTypel(memberTypes.get("融资方"));
		ci.setPercent(0.2D);
		ci.setMoney(0D);
		costItemService.insert(ci);
	}
}
