package junit4.menu;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.RoleMenu;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.RoleMenuService;
import com.wisdoor.core.service.RoleService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.MD5;
/*
 * 先执行菜单和机构，在执行此类
 */
public class quanxianDataInit {
	private static RoleService roleService; 
	private static UserService userService; 
	private static AccountService accountService; 
	private static RoleMenuService roleMenuService; 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			roleService = (RoleService)cxt.getBean("roleImpl"); 
			userService = (UserService)cxt.getBean("userImpl");
			roleMenuService = (RoleMenuService)cxt.getBean("roleMenuImpl");
			accountService = (AccountService)cxt.getBean("accountImpl");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	} 
	@Test//初始化角色
	public void initRoleData() throws Exception {
 		roleService.insert(new Role("系统管理员", "所有权限主要是权限分配，用户管理")); 
 		roleService.insert(new Role("结算统计员", ""));
 		roleService.insert(new Role("交易管理员", "")); 
 		roleService.insert(new Role("服务中心操作员", ""));
 		roleService.insert(new Role("会员管理员", "")); 
 		roleService.insert(new Role("风控管理员", "")); 
 		roleService.insert(new Role("初始角色", ""));
 		roleService.insert(new Role("融资方", "")); 
 		roleService.insert(new Role("投资人", "")); 
	} 
/**/
	@Test//初始化用户
	public void initUserData() throws Exception { 
		User obj=new  User("admin", "admin", "1");
		obj.setUserType("3");
		obj.addRole(new Role(1));
		obj.setOrg(new Org(1));
		Account account=new Account();
		account.setUser(obj);
		accountService.insert(account);
		obj.setUserAccount(account);
		obj.setPassword(MD5.MD5Encode(obj.getPassword()));
		userService.insert(obj);
		
	} 
	/**/
 	@Test//给角色赋资源
	public void initRoleMenuData() throws Exception {  
		roleMenuService.insert(new RoleMenu(4,1)); 
		roleMenuService.insert(new RoleMenu(5,1));
		roleMenuService.insert(new RoleMenu(6,1));  
		roleMenuService.insert(new RoleMenu(9,1));
		roleMenuService.insert(new RoleMenu(10,1));
		
		roleMenuService.insert(new RoleMenu(11,1));
		roleMenuService.insert(new RoleMenu(12,1)); 
		
		roleMenuService.insert(new RoleMenu(14,1));
		roleMenuService.insert(new RoleMenu(15,1));  
	} 
}
