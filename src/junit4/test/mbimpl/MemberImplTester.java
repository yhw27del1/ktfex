package junit4.test.mbimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberTypeService;

public class MemberImplTester {
	private static MemberBaseService memberBaseService;
	private static MemberTypeService memberTypeService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			memberBaseService = (MemberBaseService) cxt
					.getBean("memberBaseImpl");
			memberTypeService = (MemberTypeService) cxt
					.getBean("memberTypeImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws Exception {

		System.out.println(memberTypeService.selectByName("投资方"));

	}

	public void testMemberBaseService() throws ParseException {
		String name = "";
		String typeId = "402881fb3652eee3013652eef6c10001";
		String state = "0";
		String orgCode = "1";
		String orgName = "";
		String province = "0";
		String city = "0";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date startDate = sdf.parse("2012-07-01");

		Date endDate = new Date();
		List<MemberBase> members =
		// memberBaseService.listMembersByCondition(
		// name, typeId, state, orgCode, orgName, province, city, 30, 1)
		// .getRecords();
		memberBaseService.listAllMembersByCondition(name, typeId, state,
				orgCode, orgName, province, city, startDate, endDate);
		System.out.println("会员名，用户名，等级，账户余额，所在区域，类型，开户机构，开户人，开户时间，状态");
		for (int i = 0; i < members.size() && null != members; i++) {
			MemberBase m = members.get(i);
			System.out.println((m.getpName() == null ? m.geteName() : m
					.getpName())
					+ ","
					+ m.getUser().getUsername()
					+ ","
					+ m.getMemberLevel().getLevelname()
					+ ","
					+ m.getUser().getUserAccount().getBalance()
					+ ","
					+ m.getProvinceName()
					+ m.getCityName()
					+ ","
					+ m.getCategory()
					+ m.getMemberType().getName()
					+ ","
					+ m.getUser().getOrg().getName()
					+ ","
					+ m.getCreator().getUsername()
					+ ","
					+ m.getCreateDate()
					+ "," + m.getState());
		}
	}
}
