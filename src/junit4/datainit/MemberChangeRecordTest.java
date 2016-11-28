package junit4.datainit;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kmfex.model.DaiLiFeePercent;
import com.kmfex.model.MemberChangeRecord;
import com.kmfex.service.DaiLiFeePercentService;
import com.kmfex.service.MemberChangeRecordService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.utils.DateUtils;
 
public class MemberChangeRecordTest {
	private static MemberChangeRecordService memberChangeRecordService; 
	private static OrgService orgService; 
	private static 	DaiLiFeePercentService daiLiFeePercentService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			orgService = (OrgService)cxt.getBean("orgImpl"); 
			memberChangeRecordService = (MemberChangeRecordService) cxt.getBean("memberChangeRecordImpl");
			daiLiFeePercentService = (DaiLiFeePercentService) cxt.getBean("daiLiFeePercentServiceImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
		List<MemberChangeRecord> lists=memberChangeRecordService.getScrollData().getResultlist();
		for(MemberChangeRecord record:lists){
			Org org=orgService.selectByHql("from Org o where o.name='"+record.getToOrgRealName()+"'");
			if(null!=org){
				//record.setToUserName(org.getShowCoding());
				//memberChangeRecordService.update(record);

				System.out.println(""+org.getShowCoding());
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<DaiLiFeePercent> vps=daiLiFeePercentService.getScrollData().getResultlist();
		for(DaiLiFeePercent o:vps){
			o.setEndDate(DateUtils.getAfter(o.getEndDate(), 1));
			System.out.println(o.getEndDate());
			daiLiFeePercentService.update(o);
		}
		/*daiLiFeePercentService.insert(new DaiLiFeePercent("1","530101","B","0.7","0.003",sdf.parse("2013-1-1"),sdf.parse("2013-12-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("2","530102","B","0.7","0.003",sdf.parse("2013-1-1"),sdf.parse("2013-12-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("3","530105","B","0.7","0.003",sdf.parse("2013-1-1"),sdf.parse("2013-12-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("4","530103","A","","0",sdf.parse("2012-9-1"),sdf.parse("2013-8-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("5","530106","A","","0",sdf.parse("2012-7-11"),sdf.parse("2013-7-10"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("6","530107","A","","0",sdf.parse("2012-7-27"),sdf.parse("2013-7-26"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("7","530108","A","","0",sdf.parse("2012-7-27"),sdf.parse("2013-7-26"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("8","530109","A","","0",sdf.parse("2012-7-27"),sdf.parse("2013-7-26"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("9","530110","A","","0",sdf.parse("2012-8-3"),sdf.parse("2013-8-2"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("10","530112","A","","0",sdf.parse("2012-9-17"),sdf.parse("2013-9-16"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("11","530113","A","","0",sdf.parse("2012-9-25"),sdf.parse("2015-9-24"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("12","530115","B","0.7","0.003",sdf.parse("2012-12-18"),sdf.parse("2013-12-17"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("13","530301","B","0.7","0.003",sdf.parse("2012-12-10"),sdf.parse("2013-12-10"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("14","310400","B","0.7","0.003",sdf.parse("2013-3-1"),sdf.parse("2014-2-28"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("15","530400","B","0.7","0.003",sdf.parse("2013-3-1"),sdf.parse("2014-3-1"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("16","530117","B","0.9","0.003",sdf.parse("2013-3-18"),sdf.parse("2014-3-17"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("17","530118","B","0.7","0.003",sdf.parse("2013-4-1"),sdf.parse("2014-3-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("18","532900","B","0.65","0.003",sdf.parse("2013-4-15"),sdf.parse("2014-4-14"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("19","530121","B","0.7","0.003",sdf.parse("2013-5-2"),sdf.parse("2015-5-1"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("20","530122","B","0.7","0.003",sdf.parse("2013-5-15"),sdf.parse("2015-5-14"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("21","530123","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2015-7-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("22","530124","B","0.7","0.003",sdf.parse("2013-6-2"),sdf.parse("2015-6-1"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("23","530125","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2016-7-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("24","530126","B","0.7","0.003",sdf.parse("2013-6-13"),sdf.parse("2015-6-12"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("25","530127","B","0.7","0.003",sdf.parse("2013-9-1"),sdf.parse("2015-8-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("26","530128","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2015-5-30"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("27","530131","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("28","530133","B","0.7","0.003",sdf.parse("2013-3-16"),sdf.parse("2014-3-15"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("29","530134","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("30","530135","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("32","441900","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("33","530147","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("36","530151","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2015-7-31"),"宋伟","","门店","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("38","532800","B","0.7","0.003",sdf.parse("2013-5-1"),sdf.parse("2014-4-30"),"张歌","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("39","530138","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"李明波","","","徐艺萌"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("40","532300","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"巩彪","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("41","530149","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"李彬","","","龙黎"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("42","530701","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","梁芮骐"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("43","530702","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","梁芮骐"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("44","530302","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","梁芮骐"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("45","532700","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("46","530600","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("47","530139","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","梁芮骐"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("48","530142","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("49","530145","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","","徐艺萌"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("50","530150","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","徐艺萌"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("51","530136","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","","谢威"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("52","530152","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","","徐艺萌"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("53","530154","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","","张峰"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("54","530153","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","","徐亚军"));
		daiLiFeePercentService.insert(new DaiLiFeePercent("55","530144","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("56","532501","B","0.7","0.003",sdf.parse("2013-6-1"),sdf.parse("2014-5-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("57","530148","B","0.7","0",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("58","532503","B","0.7","0.003",sdf.parse("2013-8-1"),sdf.parse("2014-7-31"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("59","530141","B","0.7","0.003",sdf.parse("2013-7-1"),sdf.parse("2014-6-30"),"","","",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("60","530159","B","0.7","0.003",sdf.parse("2013-10-1"),sdf.parse("2014-12-31"),"","","门店",""));
		daiLiFeePercentService.insert(new DaiLiFeePercent("61","532801","B","0.7","0.003",sdf.parse("2013-9-9"),sdf.parse("2013-12-9"),"","","筹备期门店",""));

	  */
		
		/*memberChangeRecordService.insert(new MemberChangeRecord("53011700049","马昆兰","昆明迪超商贸有限公司", format.parse("2013-11-26"),"深圳众畴金融服务有限公司","吴颖","李全","微联活动话费补贴","", format.parse("2013-11-26"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700063","丁富涛","融宜通", format.parse("2013-10-01"),"昆明招财宝经济信息咨询有限公司","周云龙","赵凌","双方机构协商同意","系统实际操作日期11月1号", format.parse("2013-09-24"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700067","周云龙","昆明迪超商贸有限公司", format.parse("2013-10-01"),"深圳众畴金融服务有限公司","周云龙","赵凌","双方机构协商同意","系统实际操作日期11月1号", format.parse("2013-09-24"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100538","张刚","融宜通", format.parse("2013-09-22"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-09-12"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100075","龚梅怡","融宜通", format.parse("2013-08-21"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-21"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100570","柳红贇","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100180","张灿灿","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100337","李玲","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100644","刘维超","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100682","郑箐","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100236","赵昕","融宜通", format.parse("2013-08-06"),"昆明招财宝经济信息咨询有限公司","赵昕","赵凌","更换授权中心","", format.parse("2013-08-06"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100424","刘洋","融宜通", format.parse("2013-07-01"),"保康信达","张忠伟","赵凌","个人原因","", format.parse("2013-06-18"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100067","郑天","融宜通", format.parse("2013-07-01"),"保康信达","张忠伟","赵凌","个人原因","", format.parse("2013-06-18"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700020","王惠君","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700018","马亚丽","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100670","杨镜谕","融宜通", format.parse("2013-05-30"),"昆明迪超商贸有限公司","李全","赵凌","代理人转移","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700004","宋伟","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700036","王艳华","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700021","张立娜","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700022","徐怡","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700026","张之虹","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700028","胡静","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700046","胡秀军","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53011700017","杨春光","昆明迪超商贸有限公司", format.parse("2013-05-30"),"云南恒飞伟业投资管理有限公司","宋伟","李全","路易嘉纳授权中心负责人发展会员","", format.parse("2013-05-30"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100465","黄燕","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100515","夏琳","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100522","李朝红","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100523","任冰","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100531","高燕","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100532","喻斌","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100534","杨瑾","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100145","张杨","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100535","杨琳","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100027","梁芮骐","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","迪超商贸员工","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100548","谢建民","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100452","杨国桃","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100533","张倩","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100431","李牧容","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100430","丁子峪","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100416","王兰","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100415","徐媛","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100410","王博逸","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100409","肖华","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100386","房勤","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100368","曹扬","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100226","张志祥","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100214","谢瑞洁","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100549","彭力","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100089","鲍荣","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100225","李丽媛","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100609","郭丹雷","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100652","蒙华","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100643","熊彦","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100637","曹建祖","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100635","卢胜利","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100543","李艺婷","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100560","刘涛","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100653","吴栩","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100633","曹云建","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100631","杨庆华","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100626","刘雪梅","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100651","邱靖云","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100622","李晓霖","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100634","沈佳","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100606","程惠","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100582","唐佳","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100567","彭振华","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100572","范铁","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100573","刘瑞源","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100574","史婧","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100623","顾藴菲","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100575","瞿法华","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100583","张继美","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100590","龙飞","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100591","翟静一","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100592","周银珠","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100662","陆大东","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100598","戴利青","融宜通", format.parse("2013-05-02"),"昆明迪超商贸有限公司","李全","赵凌","现迪超员工开发会员","", format.parse("2013-04-28"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100599","李建军","融宜通", format.parse("2013-03-01"),"昆明汇诚达商贸","李建军","梁芮骐","个人申请","", format.parse("2013-04-07"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100433","李蕊","融宜通", format.parse("2013-04-01"),"昆明迪超商贸有限公司","李全","梁芮骐","2012年李全发展客户","", format.parse("2013-04-01"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100239","李全","融宜通", format.parse("2013-04-01"),"昆明迪超商贸有限公司","李全","梁芮骐","迪超授权中心负责人","", format.parse("2013-04-01"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100091","卢涛","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100046","胡永华","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100116","杨宏","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100133","赵文瑶","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100134","张琬羚","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100092","李金静","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100219","杨丽波","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100220","曾晶","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100224","李小玲","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100373","张雨华","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100387","喻俊琳","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100388","徐红","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100371","高芳","融宜通", format.parse("2013-03-01"),"丰阜咨询","董俊涛、张锐","梁芮骐","丰阜咨询发展客户","已停用", format.parse("2013-02-22"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100151","马磊","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","2012年杨一舟发展客户","", format.parse("2013-01-14"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100364","程庆年","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","2012年杨一舟发展客户","", format.parse("2013-01-14"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100365","沙平","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","2012年杨一舟发展客户","", format.parse("2013-01-14"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100082","付正龙","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","2012年杨一舟发展客户","", format.parse("2013-01-14"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100081","杨一舟","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","曲靖授权中心负责人","", format.parse("2013-01-14"),"0"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100506","殷蕾","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","2012年杨一舟发展客户","", format.parse("2013-01-14"),"1"));
		memberChangeRecordService.insert(new MemberChangeRecord("53010100510","王江","融宜通", format.parse("2013-02-01"),"曲靖昌融","董学云","梁芮骐","曲靖授权中心负责人","", format.parse("2013-01-14"),"1"));
*/
	  
 }
  
    
} 
