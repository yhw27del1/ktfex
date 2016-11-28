package junit4.menu;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.RoleMenu;
import com.wisdoor.core.service.MenuService;
import com.wisdoor.core.service.RoleMenuService;
 
public class AdminRoleDataInit { 
	private static RoleMenuService roleMenuService; 
	private static MenuService menuService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
			menuService = (MenuService) cxt.getBean("menuImpl"); 
			roleMenuService = (RoleMenuService)cxt.getBean("roleMenuImpl"); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	} 
	  
	/**/
 	@Test//给Admin赋给所有的权限
	public void initRoleMenuData() throws Exception {  
 	  List<Menu> menus=menuService.getScrollData().getResultlist();
 	  for(Menu m:menus){ 
 			 roleMenuService.insert(new RoleMenu(m.getId(),1));  
 		
 	  }
		 
	} 
}
