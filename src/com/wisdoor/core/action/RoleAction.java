package com.wisdoor.core.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Menu;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.RoleMenu;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.MenuService;
import com.wisdoor.core.service.RoleMenuService;
import com.wisdoor.core.service.RoleService;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.MenuUtils;
import com.wisdoor.struts.BaseAction;
/*** 
 * 角色类
 * @author    
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction implements Preparable {
	@Resource RoleService roleService; 
	@Resource MenuService menuService; 
	@Resource RoleMenuService roleMenuService;
	private long id;

	private Role role;
	
	private String menuIds;
	
	private String keyWord = "";
	
	private List<Menu> roleOwnMenuList;// 角色拥有的菜单列表
	
	public void prepare() throws Exception {
      if(0!=id) {
    	   role=roleService.selectById(id);
       }else{
    	   role=new Role();
       }
	}  
	/**跳转页面**/
	public String ui() throws Exception { 
		return "ui"; 
	} 
	public String list() throws Exception {
		try {
			PageView<Role> pageView = new PageView<Role>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 "); 
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.name like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.desc like ?"); 
				params.add("%"+keyWord+"%");
				sb.append(" )");
			} 
			pageView.setQueryResult(roleService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "list";
	}
	/**新增修改**/
	public String edit() throws Exception {
		try {
			if(0!=id) { 
			      // this.roleMenuService.deleteByRoleId(role,id,menuIds);
				  roleService.update(role);
				  //删除角色的所有资源
				  List<RoleMenu>  roleMenus= this.roleMenuService.findRoleMenuByRoleId(id);
				  for(RoleMenu rm:roleMenus)
				  { 
					  this.roleMenuService.delete(rm.getId());
				  }
				  if(menuIds != null && !menuIds.equals("")){ 
						  for (StringTokenizer tokenizer = new StringTokenizer(menuIds, ","); tokenizer.hasMoreTokens();) {
								String appID = tokenizer.nextToken();
								if (!appID.equals("0")) { 
									if (appID != null&& !appID.equals(""))
										{
										 appID=appID.trim();
										 if(!this.roleMenuService.findRoleMenu(id,new Long(appID))){
											 this.roleMenuService.insert(new RoleMenu(new Long(appID),id));
										 } 
										}
								} 
							  } 
					}
					
			    
			}else{
				roleService.insert(role);
				//重新设置角色的资源
				if(menuIds != null && !menuIds.equals("")){ 
					  for (StringTokenizer tokenizer = new StringTokenizer(menuIds, ","); tokenizer.hasMoreTokens();) {
							String appID = tokenizer.nextToken();
							if (!appID.equals("0")) { 
								if (appID != null&& !appID.equals(""))
									{
									 appID=appID.trim();
									 roleMenuService.insert(new RoleMenu(new Long(appID),role.getId()));
									}
							} 
						  } 
				}
				
		    }
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 
	
	
	/**删除**/
	public String del() throws Exception {
		try {  
		    //删除角色的所有资源
		    this.roleMenuService.deleteByRoleId(id);
		    roleService.delete(id);
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "del";
	}
  
	
	/**
	 * 加载资源,用于界面层tree的显示 
	 */ 
	public String getMenuTree() throws IOException { 
		try {
			this.roleOwnMenuList = this.roleMenuService.findMenus(id); 
			Document document = DocumentHelper.createDocument(); 
			Element root = document.addElement("tree");
			root.addAttribute("id", "0");  
			root.addAttribute("open", "1");  
			List<Menu> menuList =  (new MenuUtils()).getRootAll(); 
			for (Menu menu : menuList) {
				Element item = root.addElement("item");
				item.addAttribute("text", menu.getName()); 
				item.addAttribute("open", "1"); 
				item.addAttribute("id", String.valueOf(menu.getId()));  
				item.addAttribute("im0", "folderClosed.gif");
				item.addAttribute("im1", "folderOpen.gif");
				item.addAttribute("im2", "folderClosed.gif");
				//只有role拥有的并且本身节点不包含子节点的节点才能够被选中 
 	        	if(this.roleMenuService.findRoleMenu(id, menu.getId())&&this.roleMenuService.findNoHasSubRoleMenu(menu.getId())){
					item.addAttribute("checked", "checked");
				} 
				item = getSubElement(item, menu);
			} 
			String content = document.asXML();   
			DoResultUtil.doXMLResult(ServletActionContext.getResponse(), content);
		} catch (Exception e) {    
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 用于递归menu子节点的方法 
	 */
	public Element getSubElement(Element item, Menu menu) { 
		try {
			List<Menu> subList =(new MenuUtils()).getChildMenusById(menu.getId()); 
			if (subList != null && subList.size() > 0) {
				for (Menu subMenu : subList) {
					Element subItem = item.addElement("item");
					subItem.addAttribute("text", subMenu.getName()); 
					subItem.addAttribute("id", String.valueOf(subMenu.getId()));  
					subItem.addAttribute("open", "1"); 
					subItem.addAttribute("im0", "folderClosed.gif");
					subItem.addAttribute("im1", "folderOpen.gif");
					subItem.addAttribute("im2", "folderClosed.gif"); 
					if(this.roleMenuService.findRoleMenu(id, subMenu.getId())&&this.roleMenuService.findNoHasSubRoleMenu(subMenu.getId())){ 
						subItem.addAttribute("checked", "checked");
					} 
					subItem = getSubElement(subItem, subMenu);
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}

		return item;
	}
 
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	public List<Menu> getRoleOwnMenuList() {
		return roleOwnMenuList;
	}
	public void setRoleOwnMenuList(List<Menu> roleOwnMenuList) {
		this.roleOwnMenuList = roleOwnMenuList;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
 
	
}
