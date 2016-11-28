package junit4.menu;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.Menu;
import com.wisdoor.core.service.MenuService;

public class MenuDataInit {
	private static MenuService menuService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			menuService = (MenuService) cxt.getBean("menuImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void initDataTest() throws Exception {
		/*
		 * menuService.insert(new Menu(1L,"菜单根节点","1","",null,"",false));
		 * menuService.insert(new Menu(2L,"商企业金融交易平台","1m2","",new
		 * Menu(1L),"",false)); menuService.insert(new
		 * Menu(3L,"管理中心","1m3","",new Menu(2L),"",false));
		 * 
		 * //系统管理 menuService.insert(new Menu(4L,"系统管理","1m3m4","","",new
		 * Menu(3L),"class",false));
		 * 
		 * menuService.insert(new
		 * Menu(5L,"新增用户","1m3m4m5","/sys_/userAction!ui","user_add",new
		 * Menu(4L),"link",true)); menuService.insert(new
		 * Menu(6L,"用户管理","1m3m4m6","/sys_/userAction!list","user_list",new
		 * Menu(4L),"link",false)); menuService.insert(new
		 * Menu(7L,"修改用户","1m3m4m6m7","/sys_/userAction!edit","user_edit",new
		 * Menu(6L),"button",true)); //menuService.insert(new
		 * Menu(8L,"删除用户","1m3m4m6m8","/sys_/userAction!del","user_del",new
		 * Menu(6L),"button",true)); menuService.insert(new
		 * Menu(9L,"停用","1m3m4m6m9","/sys_/userAction!stop","user_stop",new
		 * Menu(6L),"button",true)); menuService.insert(new
		 * Menu(10L,"启用","1m3m4m6m10","/sys_/userAction!start","user_start",new
		 * Menu(6L),"button",true));
		 * 
		 * menuService.insert(new
		 * Menu(11L,"新增角色","1m3m4m11","/sys_/roleAction!ui","role_add",new
		 * Menu(4L),"link",true)); menuService.insert(new
		 * Menu(12L,"角色管理","1m3m4m12","/sys_/roleAction!list","role_list",new
		 * Menu(4L),"link",false)); menuService.insert(new
		 * Menu(13L,"修改角色","1m3m4m12m13","/sys_/roleAction!edit","role_edit",new
		 * Menu(12L),"button",true)); menuService.insert(new
		 * Menu(14L,"删除角色","1m3m4m12m14","/sys_/roleAction!del","role_del",new
		 * Menu(12L),"button",true));
		 * 
		 * menuService.insert(new
		 * Menu(15L,"机构管理","1m3m4m15","/sys_/orgAction!list","org_list",new
		 * Menu(4L),"link",false)); menuService.insert(new
		 * Menu(16L,"新增机构","1m3m4m15m16","/sys_/orgAction!ui","org_add",new
		 * Menu(15L),"button",true)); menuService.insert(new
		 * Menu(17L,"修改机构","1m3m4m15m17","/sys_/orgAction!edit","org_edit",new
		 * Menu(15L),"button",true)); menuService.insert(new
		 * Menu(18L,"删除机构","1m3m4m15m18","/sys_/orgAction!del","org_del",new
		 * Menu(15L),"button",true));
		 * 
		 * menuService.insert(new
		 * Menu(19L,"地点管理","1m3m4m19","/back/region/regionAction!list"
		 * ,"region_list",new Menu(4L),"link",true)); menuService.insert(new
		 * Menu
		 * (20L,"事务管理","1m3m4m20","/sys_/transaction/transactionAction!list",
		 * "transaction_list",new Menu(4L),"link",true)); menuService.insert(new
		 * Menu(211L,"修改密码","1m3m4m211","/back/selfmanager/changepassword.jsp",
		 * "transaction_list",new Menu(4L),"link",true));
		 * 
		 * 
		 * //公告管理 menuService.insert(new Menu(21L,"公告管理","1m3m21","","",new
		 * Menu(3L),"class",false)); menuService.insert(new
		 * Menu(22L,"公告列表","1m3m21m22"
		 * ,"/back/announcement/announcementAction!list_normal"
		 * ,"announcement_normal_list",new Menu(21L),"link",false));
		 * menuService.insert(newMenu(23L,"公告审核","1m3m21m23",
		 * "/back/announcement/announcementAction!list_assessor"
		 * ,"announcement_assessor_list",new Menu(21L),"link",false));
		 * 
		 * 
		 * //投融资业务 menuService.insert(new Menu(24L,"投融资业务","1m3m24","","",new
		 * Menu(3L),"class",false)); menuService.insert(new
		 * Menu(25L,"融资项目申请","1m3m24m25"
		 * ,"/back/preFinancingBaseAction!ui","preFinancingBase_ui",new
		 * Menu(24L),"link",false)); menuService.insert(new
		 * Menu(26L,"融资项目信息","1m3m24m26"
		 * ,"/back/financingBaseAction!list","financingBase_list",new
		 * Menu(24L),"link",false)); menuService.insert(new Menu(27L,"审核挂单",
		 * "1m3m24m26m27"
		 * ,"/back/financingBaseAction!check","financingBase_check",new
		 * Menu(26L),"button",true)); menuService.insert(new Menu(28L,"融资确认",
		 * "1m3m24m26m28"
		 * ,"/back/financingBaseAction!finish","financingBase_finish",new
		 * Menu(26L),"button",true)); menuService.insert(new Menu(29L,"融资费用核算",
		 * "1m3m24m29","/back/financingCostAction!list","financingCost_list",new
		 * Menu(24L),"link",true)); menuService.insert(new Menu(30L,"重新发布",
		 * "1m3m24m26m30"
		 * ,"/back/financingBaseAction!edit","financingBase_reAdd",new
		 * Menu(26L),"button",true));
		 * 
		 * 
		 * //会员管理 menuService.insert(new Menu(31L,"会员管理","1m3m31","","",new
		 * Menu(3L),"class",false)); menuService.insert(new
		 * Menu(32L,"会员列表","1m3m31m32"
		 * ,"/back/member/memberBaseAction!list","memberBase_list",new
		 * Menu(31L),"link",true)); menuService.insert(new
		 * Menu(33L,"会员开户","1m3m31m33"
		 * ,"/back/member/memberBaseAction!edit","memberBase_edit",new
		 * Menu(31L),"link",true)); menuService.insert(new
		 * Menu(34L,"停用","1m3m31m34"
		 * ,"/back/member/memberBaseAction!disable","memberBase_disable",new
		 * Menu(32L),"button",true)); menuService.insert(new
		 * Menu(35L,"启用","1m3m31m35"
		 * ,"/back/member/memberBaseAction!enable","memberBase_enable",new
		 * Menu(32L),"button",true)); menuService.insert(new
		 * Menu(36L,"删除","1m3m31m36"
		 * ,"/back/member/memberBaseAction!delete","memberBase_delete",new
		 * Menu(32L),"button",true)); menuService.insert(new
		 * Menu(38L,"审核","1m3m31m37"
		 * ,"/back/member/memberBaseAction!memberAudit",
		 * "memberBase_member_audit",new Menu(37L),"button",true));
		 * menuService.insert(new
		 * Menu(39L,"会员类型列表","1m3m31m39","/back/member/memberTypeAction!list"
		 * ,"memberType_list",new Menu(31L),"link",true));
		 * menuService.insert(new
		 * Menu(40L,"新增会员类型","1m3m31m40","/back/member/memberTypeAction!edit"
		 * ,"memberType_edit",new Menu(31L),"link",true));
		 * menuService.insert(new
		 * Menu(41L,"删除会员类型","1m3m31m41","/back/member/memberTypeAction!del"
		 * ,"memberType_del",new Menu(39L),"button",true));
		 * menuService.insert(new
		 * Menu(42L,"会员级别列表","1m3m31m42","/back/member/memberLevelAction!list"
		 * ,"memberLevel_list",new Menu(31L),"link",true));
		 * menuService.insert(new
		 * Menu(43L,"新增会员级别","1m3m31m43","/back/member/memberLevelAction!ui"
		 * ,"memberLevel_ui",new Menu(31L),"link",true));
		 * 
		 * menuService.insert(newMenu(371L,"待审核融资会员","1m3m31m371",
		 * "/back/member/memberBaseAction!notAuditedMemberlist?membertype=R& "
		 * ,"memberBase_not_audit",new Menu(31L),"link",true));
		 * menuService.insert(newMenu(372L,"待审核投资会员","1m3m31m372",
		 * "/back/member/memberBaseAction!notAuditedMemberlist?membertype=T& "
		 * ,"memberBase_not_audit",new Menu(31L),"link",true));
		 * menuService.insert(newMenu(373L,"待审核其它会员","1m3m31m373",
		 * "/back/member/memberBaseAction!notAuditedMemberlist"
		 * ,"memberBase_not_audit",new Menu(31L),"link",true));
		 * 
		 * 
		 * //投融资业务--续 menuService.insert(new Menu(50L,"融资业务类型维护",
		 * "1m3m24m50","/back/financing/businessTypeAction!list"
		 * ,"businessType_list",new Menu(24L),"link",true));
		 * menuService.insert(new Menu(52L,"投标约束维护",
		 * "1m3m24m52","/back/financing/investConditionAction!list"
		 * ,"investList_ysu",new Menu(24L),"link",true));
		 * 
		 * //结算管理 menuService.insert(new Menu(61L,"结算管理","1m3m61","","",new
		 * Menu(3L),"class",false)); menuService.insert(new Menu(62L,"融资费用核算",
		 * "1m3m61m62"
		 * ,"/back/financingCostAction!finishList","financingCost_finishlist"
		 * ,new Menu(61L),"link",true)); menuService.insert(new
		 * Menu(64L,"收费项目维护", "1m3m61m64","/back/costCategoryAction!list","",new
		 * Menu(61L),"link",true)); menuService.insert(new Menu(65L,"收费标准维护",
		 * "1m3m61m65","/back/chargingStandardAction!list","",new
		 * Menu(61L),"link",true));
		 * 
		 * menuService.insert(new
		 * Menu(66L,"会员充值","1m3m61m44","/back/accountDeal/chargePage2.jsp"
		 * ,"memberCharge_list",new Menu(61L),"link",true));
		 * menuService.insert(new
		 * Menu(67L,"会员账户查询","1m3m61m45","/back/accountDeal/accountDealAction!list"
		 * ,"memberChargeQuery_list",new Menu(61L),"link",true));
		 * menuService.insert(newMenu(68L,"中心账户查看","1m3m61m46",
		 * "/back/accountDeal/accountDealAction!centerAccount"
		 * ,"centerAccount_list",new Menu(61L),"link",true));
		 * menuService.insert(new
		 * Menu(69L,"投标查询","1m3m61m47","/back/investBaseAction!recordListTotal"
		 * ,"centerAccount_list",new Menu(61L),"link",true));
		 * menuService.insert(newMenu(70L,"融资款交割","1m3m61m48",
		 * "/back/accountDeal/accountDealAction!delivery","delivery_list",new
		 * Menu(61L),"link",true)); menuService.insert(new
		 * Menu(71L,"还款记录","1m3m61m49","","huankuan_list",new
		 * Menu(61L),"link",true));
		 * 
		 * //投资人 menuService.insert(new Menu(72L,"投资人","1m3m70","","",new
		 * Menu(3L),"class",false)); menuService.insert(new Menu(73L,"我要投标",
		 * "1m3m70m80"
		 * ,"/back/financingBaseAction!investList","investList_list",new
		 * Menu(72L),"link",true)); menuService.insert(new Menu(74L,"我的投标明细",
		 * "1m3m70m81"
		 * ,"/back/investBaseAction!recordListForPerson","investListForPerson_list"
		 * ,new Menu(72L),"link",true));
		 * 
		 * //融资方 menuService.insert(new Menu(76L,"融资方","1m3m82","","",new
		 * Menu(3L),"class",false)); menuService.insert(new Menu(77L,"我的融资进度",
		 * "1m3m71m83"
		 * ,"/back/financingBaseAction!listForPerson","financingBaseForPerson_list"
		 * ,new Menu(76L),"link",true)); menuService.insert(new
		 * Menu(78L,"我的融资费用核算",
		 * "1m3m71m84","/back/financingCostAction!listFeeForPerson"
		 * ,"listFeeForPerson_list",new Menu(76L),"link",true));
		 * 
		 * //会员管理--续 //menuService.insert(new
		 * Menu(79L,"修改会员等级","1m3m31m79","/back/member/memberBaseAction!tList"
		 * ,"memberBase_tlist",new Menu(31L),"link",true));
		 * 
		 * menuService.insert(new
		 * Menu(212L,"行业管理","1m3m4m212","/back/industry/industryAction!list"
		 * ,"industry_list",new Menu(4L),"link",true)); menuService.insert(new
		 * Menu(213L,"新增","1m3m4m212m213","/back/industry/industryAction!add",
		 * "industry_add",new Menu(212L),"button",true)); menuService.insert(new
		 * Menu(214L,"修改","1m3m4m212m214","/back/industry/industryAction!edit",
		 * "industry_edit",new Menu(212L),"button",true));
		 * menuService.insert(new
		 * Menu(215L,"删除","1m3m4m212m215","/back/industry/industryAction!del"
		 * ,"industry_del",new Menu(212L),"button",true));
		 * 
		 * menuService.insert(newMenu(216L,"公司性质管理","1m3m4m216",
		 * "/back/companyProperty/companyPropertyAction!list"
		 * ,"companyProperty_list",new Menu(4L),"link",true));
		 * menuService.insert(newMenu(217L,"新增","1m3m4m216m217",
		 * "/back/companyProperty/companyPropertyAction!add"
		 * ,"companyProperty_add",new Menu(216L),"button",true));
		 * menuService.insert(newMenu(218L,"修改","1m3m4m216m218",
		 * "/back/companyProperty/companyPropertyAction!edit"
		 * ,"companyProperty_edit",new Menu(216L),"button",true));
		 * menuService.insert(newMenu(219L,"删除","1m3m4m216m219",
		 * "/back/companyProperty/companyPropertyAction!del"
		 * ,"companyProperty_del",new Menu(216L),"button",true));
		 */
		// 投资人，融资方续
		// menuService.insert(new
		// Menu(90L,"我的账户","1m3m70m91","/back/accountDeal/accountDealAction!listForPersonT","myAccount_T_list",new
		// Menu(72L),"link",true));
		// menuService.insert(new
		// Menu(92L,"我的账户","1m3m70m93","/back/accountDeal/accountDealAction!listForPersonR","myAccount_R_list",new
		// Menu(76L),"link",true));

		// menuService.insert(new
		// Menu(98L,"提现申请","1m3m70m100","/back/accountDeal/accountDealAction!toCashForPersonT","toCash_T_list",new
		// Menu(72L),"link",true));
		// menuService.insert(new
		// Menu(99L,"提现申请","1m3m70m101","/back/accountDeal/accountDealAction!toCashForPersonR","toCash_R_list",new
		// Menu(76L),"link",true));

		// menuService.insert(new
		// Menu(95L,"融资项目审核","1m3m24m95","/back/financingBaseAction!checkList","financingBaseCheck_list",new
		// Menu(24L),"link",false));
		// menuService.insert(new
		// Menu(102L,"提现审核","1m3m61m88","/back/accountDeal/accountDealAction!cashList","toCashCheck_list",new
		// Menu(61L),"link",true));

		// menuService.insert(new
		// Menu(106L,"待审核充值列表","1m3m61m107","/back/accountDeal/accountDealAction!chargeCheckList","chargeCheck_list",new
		// Menu(61L),"link",true));
		// menuService.insert(new
		// Menu(108L,"申请中融资项目","1m3m24m108","/back/preFinancingBaseAction!list","preFinancingBase_list",new
		// Menu(24L),"link",false));
		// menuService.insert(new
		// Menu(109L,"修改","1m3m24m108m109","/back/preFinancingBaseAction!edit","preFinancingBase_edit",new
		// Menu(108L),"button",false));
		// menuService.insert(new
		// Menu(110L,"信用确认","1m3m24m108m110","/back/preFinancingBaseAction!xyFinish","preFinancingBase_xyFinish",new
		// Menu(108L),"button",false));
		// menuService.insert(new
		// Menu(111L,"详情","1m3m24m108m111","/back/preFinancingBaseAction!detail","preFinancingBase_detail",new
		// Menu(108L),"button",false));

		// menuService.insert(new
		// Menu(144L,"融资项目查询","1m3m24m144","/back/financingBaseAction!listQuery","financingBase_listQuery",new
		// Menu(24L),"link",false));
		// menuService.insert(new
		// Menu(145L,"融资项目签约","1m3m24m145","/back/financingBaseAction!listQianyue","financingBase_listQianyue",new
		// Menu(24L),"link",false));
		// menuService.insert(new Menu(146L,"我的融资签约",
		// "1m3m71m146","/back/financingBaseAction!myListQianyue","myListQianyue_list",new
		// Menu(76L),"link",true));

		/**
		 * 用于读取数据库中的菜单信息
		 * */
		/*DetachedCriteria criteria=DetachedCriteria.forClass(Menu.class);
		
		criteria.addOrder(Order.asc("id"));
		
		List<Menu> menus = menuService.getListForCriteria(criteria);
		
		for (Menu menu : menus) {

			StringBuilder hql = new StringBuilder();
			hql.append("menuService.insert(");
			hql.append("new Menu(");
			hql.append(menu.getId()+"L");
			hql.append(",");
			hql.append("\"");
			hql.append(menu.getName());

			hql.append("\",\"");

			hql.append(menu.getCoding());

			hql.append("\",\"");
			if(null!=menu.getHref()){
			hql.append(menu.getHref());}
						
			hql.append("\",\"");
			if(null!=menu.getPrivilegeValue()){
			hql.append(menu.getPrivilegeValue());}
			
			hql.append("\",");
			if (null != menu.getParent()) {
				hql.append("new Menu(" + menu.getParent().getId() + ")");
			} else {
				hql.append("\"\"");
			}
			hql.append(",\"");
			if(null!=menu.getType()){
			hql.append(menu.getType());}
			
			hql.append("\",");
			hql.append(menu.isLeaf());
			hql.append("));");
			System.out.println(hql.toString());
		}
		*/
		/*
		menuService.insert(new Menu(32L,"投标用户分组","1m3m24m32","/back/userGroupAction!list","financing_UserGroup",new Menu(24),"link",false));
		menuService.insert(new Menu(46L,"新增/修改组","1m3m24m32m46","/back/userGroupAction!edit","userGroup_edit",new Menu(32),"button",true));	
		menuService.insert(new Menu(47L,"加入成员","1m3m24m32m47","/back/userGroupAction!addUser","userGroup_addUser",new Menu(32),"button",true));	
		menuService.insert(new Menu(48L,"删除成员","1m3m24m32m48","/back/userGroupAction!delUser","userGroup_delUser",new Menu(32),"button",true));	
		menuService.insert(new Menu(49L,"查看成员","1m3m24m32m49","/back/userGroupAction!groupSelectUserList","userGroup_selectUser",new Menu(32),"button",true));	
		*/ 
		//menuService.insert(new Menu(55L,"按日计息","1m3m24m108m55","","preFinancingBase_DayLx",new Menu(108L),"button",false));
		/**
		 * 整理以后的菜单初始化数据
		 */ 
		
/*		menuService.insert(new Menu(153L,"在贷余额统计","1m3m24m153","/back/paymentRecord/paymentRecordAction!list_zdye","list_zdye",new Menu(24),"link",true));
		menuService.insert(new Menu(154L,"融资项目还款统计","1m3m24m154","/back/paymentRecord/paymentRecordAction!list_rzhkqkcx","list_rzhkqkcx",new Menu(24),"link",true));
		menuService.insert(new Menu(155L,"近期还款计划","1m3m24m155","/back/paymentRecord/paymentRecordAction!list_jqhktx","list_jqhktx",new Menu(24),"link",true));
		menuService.insert(new Menu(156L,"逾期记录提醒","1m3m240m156","/back/paymentRecord/paymentRecordAction!list_yqhktx","list_yqhktx",new Menu(24),"link",true));
*/
	/*	
menuService.insert(new Menu(1L,"菜单根节点","1","","","","",false));
menuService.insert(new Menu(2L,"昆明商企业金融交易平台","1m2","","",new Menu(1),"",false));
menuService.insert(new Menu(3L,"管理中心","1m3","","",new Menu(2),"",false));
menuService.insert(new Menu(4L,"系统管理","1m3m4","","",new Menu(3),"class",false));
menuService.insert(new Menu(5L,"新增用户","1m3m4m5","/sys_/userAction!ui","user_add",new Menu(4),"link",true));
menuService.insert(new Menu(6L,"用户管理","1m3m4m6","/sys_/userAction!list","user_list",new Menu(4),"link",false));
menuService.insert(new Menu(7L,"修改用户","1m3m4m6m7","/sys_/userAction!edit","user_edit",new Menu(6),"button",true));
menuService.insert(new Menu(9L,"停用","1m3m4m6m9","/sys_/userAction!stop","user_stop",new Menu(6),"button",true));
menuService.insert(new Menu(10L,"启用","1m3m4m6m10","/sys_/userAction!start","user_start",new Menu(6),"button",true));
menuService.insert(new Menu(11L,"新增角色","1m3m4m11","/sys_/roleAction!ui","role_add",new Menu(4),"link",true));
menuService.insert(new Menu(12L,"角色管理","1m3m4m12","/sys_/roleAction!list","role_list",new Menu(4),"link",false));
menuService.insert(new Menu(13L,"修改角色","1m3m4m12m13","/sys_/roleAction!edit","role_edit",new Menu(12),"button",true));
menuService.insert(new Menu(14L,"删除角色","1m3m4m12m14","/sys_/roleAction!del","role_del",new Menu(12),"button",true));
menuService.insert(new Menu(15L,"机构管理","1m3m4m15","/sys_/orgAction!list","org_list",new Menu(4),"link",false));
menuService.insert(new Menu(16L,"新增机构","1m3m4m15m16","/sys_/orgAction!ui","org_add",new Menu(15),"button",true));
menuService.insert(new Menu(17L,"修改机构","1m3m4m15m17","/sys_/orgAction!edit","org_edit",new Menu(15),"button",true));
menuService.insert(new Menu(18L,"删除机构","1m3m4m15m18","/sys_/orgAction!del","org_del",new Menu(15),"button",true));
menuService.insert(new Menu(19L,"地区管理","1m3m21m19","/back/region/regionAction!list","region_list",new Menu(21),"link",true));
menuService.insert(new Menu(20L,"事务管理","1m3m4m20","/sys_/transaction/transactionAction!list","transaction_list",new Menu(4),"link",true));
menuService.insert(new Menu(21L,"参数管理","1m3m21","","",new Menu(3),"class",false));
menuService.insert(new Menu(22L,"公告列表","1m3m4m22","/back/announcement/announcementAction!list_normal","announcement_normal_list",new Menu(4),"link",false));
menuService.insert(new Menu(23L,"公告审核","1m3m4m23","/back/announcement/announcementAction!list_assessor","announcement_assessor_list",new Menu(4),"link",false));
menuService.insert(new Menu(24L,"投融资服务","1m3m24","","",new Menu(3),"class",false));
menuService.insert(new Menu(25L,"融资项目申请","1m3m24m25","/back/preFinancingBaseAction!ui","preFinancingBase_ui",new Menu(24),"link",true));
menuService.insert(new Menu(26L,"融资项目信息","1m3m24m26","/back/financingBaseAction!list","financingBase_list",new Menu(24),"link",true));
menuService.insert(new Menu(27L,"审核挂单","1m3m24m26m27","/back/financingBaseAction!check","financingBase_check",new Menu(26),"button",true));
menuService.insert(new Menu(28L,"融资确认","1m3m24m26m28","/back/financingBaseAction!finish","financingBase_finish",new Menu(26),"button",true));
menuService.insert(new Menu(29L,"融资费用核算","1m3m24m29","/back/financingCostAction!list","financingCost_list",new Menu(24),"link",true));
menuService.insert(new Menu(30L,"重新发布","1m3m24m26m30","/back/financingBaseAction!edit","financingBase_reAdd",new Menu(26),"button",true));
menuService.insert(new Menu(31L,"会员管理","1m3m31","","",new Menu(3),"class",false));
menuService.insert(new Menu(33L,"会员开户","1m3m31m33","/back/member/memberBaseAction!edit","memberBase_edit",new Menu(31),"link",true));
menuService.insert(new Menu(34L,"停用","1m3m31m34","/back/member/memberBaseAction!disable","memberBase_disable",new Menu(37),"button",true));
menuService.insert(new Menu(35L,"启用","1m3m31m35","/back/member/memberBaseAction!enable","memberBase_enable",new Menu(37),"button",true));
menuService.insert(new Menu(36L,"删除","1m3m31m36","/back/member/memberBaseAction!delete","memberBase_delete",new Menu(37),"button",true));
menuService.insert(new Menu(37L,"会员列表","1m3m31m37","/back/member/memberBaseAction!list","memberBase_list",new Menu(31),"link",true));
menuService.insert(new Menu(38L,"审核","1m3m31m37","/back/member/memberAuditAction!audit","memberBase_member_audit",new Menu(37),"button",true));
menuService.insert(new Menu(39L,"会员类型维护","1m3m31m39","/back/member/memberTypeAction!list","memberType_list",new Menu(31),"link",true));
menuService.insert(new Menu(40L,"新增会员类型","1m3m31m40","/back/member/memberTypeAction!edit","emberType_edit",new Menu(39),"button",true));
menuService.insert(new Menu(41L,"删除会员类型","1m3m31m41","/back/member/memberTypeAction!del","memberType_del",new Menu(39),"button",true));
menuService.insert(new Menu(42L,"会员级别维护","1m3m31m42","/back/member/memberLevelAction!list","memberLevel_list",new Menu(31),"link",true));
menuService.insert(new Menu(43L,"导出会员信息为Excel","1m3m31m43","/back/member/memberBaseAction!list?excel=true","memberBaseList_exportExcel",new Menu(37),"button",false));
menuService.insert(new Menu(44L,"会员银行账号","1m3m31m44","/back/member/memberBaseAction!memberBackAccounts","memberBase_bankAccounts",new Menu(37),"button",true));
menuService.insert(new Menu(45L,"会员详细信息","1m3m31m45","/back/member/memberBaseAction!memberDetails","memberBase_details",new Menu(37),"button",true));
menuService.insert(new Menu(50L,"融资类型维护","1m3m24m50","/back/financing/businessTypeAction!list","businessType_list",new Menu(24),"link",true));
menuService.insert(new Menu(52L,"投标约束维护","1m3m24m52","/back/financing/investConditionAction!list","investList_ysu",new Menu(24),"link",true));
menuService.insert(new Menu(61L,"结算管理","1m3m61","","",new Menu(3),"class",false));
menuService.insert(new Menu(62L,"融资费用查看","1m3m61m62","/back/financingCostAction!finishList","financingCost_finishlist",new Menu(61),"link",true));
menuService.insert(new Menu(64L,"收费项目维护","1m3m61m64","/back/costCategoryAction!list","",new Menu(61),"link",true));
menuService.insert(new Menu(65L,"收费标准维护","1m3m61m65","/back/chargingStandardAction!list","",new Menu(61),"link",true));
menuService.insert(new Menu(66L,"会员充值","1m3m61m44","/back/accountDeal/chargePage2.jsp","memberCharge_list",new Menu(61),"link",true));
menuService.insert(new Menu(67L,"会员账户查询","1m3m61m45","/back/accountDeal/accountDealAction!list","memberChargeQuery_list",new Menu(61),"link",true));
menuService.insert(new Menu(68L,"中心账户查看","1m3m61m46","/back/accountDeal/accountDealAction!centerAccount","centerAccount_list",new Menu(61),"link",true));
menuService.insert(new Menu(69L,"投标查询","1m3m61m47","/back/investBaseAction!recordListTotal","invesSeachww_list",new Menu(61),"link",true));
menuService.insert(new Menu(70L,"融资款交割","1m3m61m48","/back/accountDeal/accountDealAction!delivery","delivery_list",new Menu(61),"link",true));
menuService.insert(new Menu(72L,"投资人","1m3m70","","",new Menu(3),"class",false));
menuService.insert(new Menu(73L,"我要投标","1m3m70m80","/back/financingBaseAction!investList","investList_list",new Menu(72),"link",true));
menuService.insert(new Menu(74L,"我的投标记录","1m3m70m81","/back/investBaseAction!recordListForPerson","investListForPerson_list",new Menu(72),"link",true));
menuService.insert(new Menu(76L,"融资方","1m3m82","","",new Menu(3),"class",false));
menuService.insert(new Menu(77L,"我的融资进度","1m3m71m83","/back/financingBaseAction!listForPerson","financingBaseForPerson_list",new Menu(76),"link",true));
menuService.insert(new Menu(78L,"我的融资费用核算","1m3m71m84","/back/financingCostAction!listFeeForPerson","listFeeForPerson_list",new Menu(76),"link",true));
menuService.insert(new Menu(79L,"修改会员级别","1m3m31m79","/back/member/memberBaseAction!tList","memberBase_tlist",new Menu(31),"link",true));
menuService.insert(new Menu(85L,"我的合同列表","1m3m71m85","/back/financingBase/financingBaseAction!myContracts","",new Menu(76),"link",true));
menuService.insert(new Menu(90L,"我的账户","1m3m70m91","/back/accountDeal/accountDealAction!listForPersonT","myAccount_T_list",new Menu(72),"link",true));
menuService.insert(new Menu(92L,"我的账户","1m3m70m93","/back/accountDeal/accountDealAction!listForPersonR","myAccount_R_list",new Menu(76),"link",true));
menuService.insert(new Menu(95L,"待审核融资项目","1m3m24m95","/back/financingBaseAction!checkList","financingBaseCheck_list",new Menu(24),"link",true));
menuService.insert(new Menu(98L,"提现申请","1m3m70m100","/back/accountDeal/accountDealAction!toCashForPersonT","toCash_T_list",new Menu(72),"link",true));
menuService.insert(new Menu(99L,"提现申请","1m3m70m101","/back/accountDeal/accountDealAction!toCashForPersonR","toCash_R_list",new Menu(76),"link",true));
menuService.insert(new Menu(101L,"债权行情","1m3m26m101","/back/zhaiquan/zhaiQuanInvestAction!flist","",new Menu(482),"link",true));
menuService.insert(new Menu(102L,"待审核提现列表","1m3m61m88","/back/accountDeal/accountDealAction!cashList","toCashCheck_list",new Menu(61),"link",true));
menuService.insert(new Menu(106L,"待审核充值列表","1m3m61m107","/back/accountDeal/accountDealAction!chargeCheckList","chargeCheck_list",new Menu(61),"link",true));
menuService.insert(new Menu(108L,"申请中融资项目","1m3m24m108","/back/preFinancingBaseAction!list","preFinancingBase_list",new Menu(24),"link",true));
menuService.insert(new Menu(109L,"修改","1m3m24m108m109","/back/preFinancingBaseAction!edit","preFinancingBase_edit",new Menu(108),"button",false));
menuService.insert(new Menu(110L,"信用确认","1m3m24m108m110","/back/preFinancingBaseAction!xyFinish","preFinancingBase_xyFinish",new Menu(108),"button",false));
menuService.insert(new Menu(111L,"详情","1m3m24m108m111","/back/preFinancingBaseAction!detail","preFinancingBase_detail",new Menu(108),"button",false));
menuService.insert(new Menu(144L,"融资项目查询","1m3m24m144","/back/financingBaseAction!listQuery","financingBase_listQuery",new Menu(24),"link",true));
menuService.insert(new Menu(145L,"融资项目签约","1m3m24m145","/back/financingBaseAction!listQianyue","financingBase_listQianyue",new Menu(24),"link",true));
menuService.insert(new Menu(146L,"我的融资签约","1m3m71m146","/back/financingBaseAction!myListQianyue","myListQianyue_list",new Menu(76),"link",true));
menuService.insert(new Menu(147L,"担保公司担保信息","1m3m31m43","/back/member/memberGuaranteeAction!list","memberGuaranteeList",new Menu(31),"link",false));
menuService.insert(new Menu(148L,"修改","1m3m31m44","/back/member/memberGuaranteeAction!edit","memberGuarantee_edit",new Menu(147),"button",true));
menuService.insert(new Menu(149L,"列表","1m3m31m45","/back/member/memberGuaranteeAction!list?temp=1","memberGuarantee_list",new Menu(147),"link",false));
menuService.insert(new Menu(150L,"新增","1m3m31m80","/back/member/memberLevelAction!ui","memberLevel_ui",new Menu(42),"button",false));
menuService.insert(new Menu(151L,"查看","1m3m31m45","/back/member/memberBaseAction!list?temp=1","memberBaseList_query",new Menu(37),"button",false));
menuService.insert(new Menu(211L,"修改密码","1m3m4m211","/back/selfmanager/changepassword.jsp","changepasswod_list",new Menu(4),"link",true));
menuService.insert(new Menu(212L,"行业管理","1m3m21m212","/back/industry/industryAction!list","industry_list",new Menu(21),"link",true));
menuService.insert(new Menu(213L,"新增","1m3m4m212m213","/back/industry/industryAction!add","industry_add",new Menu(212),"button",true));
menuService.insert(new Menu(214L,"修改","1m3m4m212m214","/back/industry/industryAction!edit","industry_edit",new Menu(212),"button",true));
menuService.insert(new Menu(215L,"删除","1m3m4m212m215","/back/industry/industryAction!del","industry_del",new Menu(212),"button",true));
menuService.insert(new Menu(216L,"公司性质管理","1m3m21m216","/back/companyProperty/companyPropertyAction!list","companyProperty_list",new Menu(21),"link",true));
menuService.insert(new Menu(217L,"新增","1m3m4m216m217","/back/companyProperty/companyPropertyAction!add","companyProperty_add",new Menu(216),"button",true));
menuService.insert(new Menu(218L,"修改","1m3m4m216m218","/back/companyProperty/companyPropertyAction!edit","companyProperty_edit",new Menu(216),"button",true));
menuService.insert(new Menu(219L,"删除","1m3m4m216m219","/back/companyProperty/companyPropertyAction!del","companyProperty_del",new Menu(216),"button",true));
menuService.insert(new Menu(220L,"会员资料变更","1m3m31m372","/back/member/memberBaseAction!change","memberBase_change",new Menu(31),"link",true));
menuService.insert(new Menu(221L,"变更","1m3m31m221","/back/member/memberBaseAction!edit?time=1","member_change",new Menu(220),"button",false));
menuService.insert(new Menu(371L,"待审核投资会员","1m3m31m371","/back/member/memberBaseAction!notAuditedMemberlist?membertype=T","memberBase_not_audit_T",new Menu(31),"link",true));
menuService.insert(new Menu(372L,"待审核融资会员","1m3m31m372","/back/member/memberBaseAction!notAuditedMemberlist?membertype=R","memberBase_not_audit_R",new Menu(31),"link",true));
menuService.insert(new Menu(373L,"待审核其它会员","1m3m31m373","/back/member/memberBaseAction!notAuditedMemberlist","memberBase_not_audit",new Menu(31),"link",true));
menuService.insert(new Menu(410L,"还款管理","1m3m410","","",new Menu(3),"class",false));
menuService.insert(new Menu(411L,"待还款记录","1m3m410m411","/back/paymentRecord/paymentRecordAction!list_0","huankuan_list_noyet",new Menu(410),"link",true));
menuService.insert(new Menu(412L,"已结束还款记录","1m3m410m412","/back/paymentRecord/paymentRecordAction!list_die","huankuan_list_did",new Menu(410),"link",true));
menuService.insert(new Menu(413L,"延期还款记录","1m3m410m413","/back/paymentRecord/paymentRecordAction!list_2","huankuan_list_wait",new Menu(410),"link",true));
menuService.insert(new Menu(414L,"逾期还款记录","1m3m410m414","/back/paymentRecord/paymentRecordAction!list_3","huankuan_list_timeout",new Menu(410),"link",true));
menuService.insert(new Menu(415L,"待审核还款记录","1m3m410m415","/back/paymentRecord/paymentRecordAction!list_approve","",new Menu(410),"link",true));
menuService.insert(new Menu(416L,"未通过审核会员","1m3m32","/back/member/memberBaseAction!listByNotPassedAudited?memberState=3","memberBase_notPassAuditedList",new Menu(31),"link",true));
menuService.insert(new Menu(417L,"已审核未挂单融资项目","1m3m24m417","/back/financingBaseAction!for_order","",new Menu(24),"link",true));
menuService.insert(new Menu(418L,"投标中融资项目","1m3m24m418","/back/financingBaseAction!for_subscribe","",new Menu(24),"link",true));
menuService.insert(new Menu(419L,"已满标/已到期融资项目","1m3m24m419","/back/financingBaseAction!for_ending","",new Menu(24),"link",true));
menuService.insert(new Menu(444L,"管理我持有的债权","1m3m26m444","/back/zhaiquan/zhaiQuanInvestAction!myzqlist","",new Menu(482),"link",true));
menuService.insert(new Menu(456L,"重置密码","1m3m31m456","/back/member/memberBaseAction!resetPasswrodUi","memberBase_resetpasword",new Menu(31),"link",true));
menuService.insert(new Menu(476L,"今日待挂单融资项目","1m3m24m476","/back/financingBaseAction!today_for_order","",new Menu(24),"link",true));
menuService.insert(new Menu(477L,"债权管理","1m3m25","","claims_management",new Menu(3),"class",false));
menuService.insert(new Menu(478L,"挂牌","1m3m25m1","/back/zhaiquan/zhaiQuanInvestAction!list?zqzrState=1","claims_new",new Menu(486),"button",true));
menuService.insert(new Menu(479L,"停牌","1m3m25m2","/back/zhaiquan/zhaiQuanInvestAction!list?zqzrState=2","claims_stop",new Menu(486),"button",true));
menuService.insert(new Menu(480L,"复牌","1m3m25m3","/back/zhaiquan/zhaiQuanInvestAction!list?zqzrState=3","claims_reset",new Menu(486),"button",true));
menuService.insert(new Menu(481L,"摘牌","1m3m25m3","/back/zhaiquan/zhaiQuanInvestAction!list?zqzrState=4","claims_cancel",new Menu(486),"button",true));
menuService.insert(new Menu(482L,"债权交易","1m3m26","","claims_transform",new Menu(3),"class",false));
menuService.insert(new Menu(483L,"债权出让记录","1m3m26m1","/back/zhaiquan/sellingAction!mlist","claims_transform_out",new Menu(477),"link",true));
menuService.insert(new Menu(484L,"债权受让记录","1m3m26m2","/back/zhaiquan/buyingAction!mlist","claims_transform_in",new Menu(477),"link",true));
menuService.insert(new Menu(485L,"修改","1m3m31m485","/back/member/memberLevelAction!ui","memberLevel_edit",new Menu(42),"button",true));
menuService.insert(new Menu(486L,"债权列表","1m2m26m3","/back/zhaiquan/zhaiQuanInvestAction!list","claims_list",new Menu(477),"link",true));
menuService.insert(new Menu(487L,"已完成的债权转让交易","1m2m26m4","/back/zhaiquan/contractAction","claims_agreement",new Menu(477),"link",true));
menuService.insert(new Menu(555L,"交易时间维护","1m3m24m555","/back/tradeTime/tradeTimeAction!list","",new Menu(24),"link",true));
menuService.insert(new Menu(556L,"我的债权转让记录","1m3m26m556","/back/zhaiquan/sellingAction!myselllist","",new Menu(482),"link",true));
menuService.insert(new Menu(557L,"我申请购买的债权","1m3m26m557","/back/zhaiquan/buyingAction!mybuylist","",new Menu(482),"link",true));
menuService.insert(new Menu(777L,"三方账户查看","1m3m61m777","/back/accountDeal/accountDealAction!thirdAccount","thirdAccount_list",new Menu(61),"link",true));
menuService.insert(new Menu(1111L,"今日成功充值列表","1m3m061m1111","/back/accountDeal/accountDealAction!todayChargeList?abc=1111","todayCharge_listist",new Menu(61),"link",true));
menuService.insert(new Menu(2222L,"今日成功充值列表","1m3m61m2222","/back/accountDeal/accountDealAction!todayChargeList?abc=2222","todayCharge_listist",new Menu(31),"link",true));
menuService.insert(new Menu(4160L,"还款情况","1m3m410m4160","/back/paymentRecord/paymentRecordAction!list_view","",new Menu(410),"link",true));
menuService.insert(new Menu(4161L,"已审核充值明细","1m3m61m4161","/back/accountDeal/accountDealAction!chargeList_checked","chargeList_checked",new Menu(61),"link",true));
menuService.insert(new Menu(4162L,"已审核提现明细","1m3m61m4162","/back/accountDeal/accountDealAction!cashList_checked","cashList_checked",new Menu(61),"link",true));
menuService.insert(new Menu(4163L,"已审核充值/提现明细","1m3m61m4163","/back/accountDeal/accountDealAction!checkAndCashList_checked","checkAndCashList_checked",new Menu(61),"link",true));
menuService.insert(new Menu(4164L,"融资费用收入明细","1m3m61m4164","/back/financingCostAction!financingBaseList_qianyued","financingBaseList_qianyued",new Menu(61),"link",true));

*
*/
/*		menuService.insert(new Menu(222L,"授权服务机构","1m3m31m222","/back/sqorg/sqOrgAction!sqList","org_sqList",new Menu(31),"link",true));
		menuService.insert(new Menu(223L,"新增机构","1m3m31m222m223","/back/sqorg/sqOrgAction!sqAdd","org_sqAdd",new Menu(222),"button",true)); 
		menuService.insert(new Menu(224L,"修改机构","1m3m31m222m224","/back/sqorg/sqOrgAction!sqEdit","org_sqEdit",new Menu(222),"button",true)); 
		menuService.insert(new Menu(225L,"查看","1m3m31m222m225","/back/sqorg/sqOrgAction!sqDetail","org_sqDetail",new Menu(222),"button",true)); 
		*/
		//menuService.insert(new Menu(333L,"授权服务用户管理","1m3m31m333","/back/squser/sqUserAction!sqList","user_sqList",new Menu(31),"link",true));
		//menuService.insert(new Menu(334L,"开户","1m3m31m222m334","/back/squser/sqUserAction!sqAdd","user_sqAdd",new Menu(333),"button",true)); 
		//menuService.insert(new Menu(335L,"修改","1m3m31m222m335","/back/squser/sqUserAction!sqEdit","user_sqEdit",new Menu(333),"button",true));
		//menuService.insert(new Menu(336L,"查看","1m3m31m222m336","/back/squser/sqUserAction!sqDetail","user_sqDetail",new Menu(333),"button",true));
		/*
		menuService.insert(new Menu(226L,"我的还款情况","1m3m70m226","/back/paymentRecord/paymentRecordAction!dieWeb","dieWebList",new Menu(70),"link",true));
		menuService.insert(new Menu(227L,"往期借款情况","1m3m70m227","/back/financingBaseAction!listRzQuery","listRzQuery",new Menu(70),"link",true));
		*/
		
		//menuService.insert(new Menu(8L,"担保公司应收费用查询","1m3m24m8","/back/guaranteeAction!list","guaranteeCost_list",new Menu(24),"link",true));
		
		//menuService.insert(new Menu(51L,"担保公司费用明细","1m3m24m51","/back/guaranteeDetailAction!list","guaranteeCostDetail_list",new Menu(24),"link",true));
		 //menuService.insert(new Menu(54L,"会员资产查询","1m3m61m54","/back/accountDealAction!totalAccount","totalAccount_list",new Menu(61),"link",true));
		//menuService.insert(new Menu(490L,"债权转让费用","1m2m26m490","/back/zhaiquan/contractAction!fee","claims_feelist",new Menu(477),"link",true));
	}
}
