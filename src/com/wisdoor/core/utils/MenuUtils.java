package com.wisdoor.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.wisdoor.core.cache.GlobalParameters;
import com.wisdoor.core.model.Menu; 

/**
 * 菜单工具类
 * @author   
 */
@SuppressWarnings("unchecked")
public class MenuUtils {
	/**
	 * 获得所有菜单
	 */
	public  List<Menu> getAll() {  
		return (List<Menu>)GlobalParameters.business.get("menuCacheGlobal");
	}
	/**
	 * 获得根据菜单
	 */
	public  List<Menu> getRootAll() {  
		return (List<Menu>)GlobalParameters.business.get("menuRootCacheGlobal");
	}
	/**
	 * 从Id该类别下所有菜单 
	 */
	
	public  List<Menu> getChildMenusById(long id) {
		List<Menu> detailCaches = new ArrayList<Menu>();
		List<Menu> details=getAll(); 
		for(Menu dd: details)
		{ 
			if(id==dd.getParent().getId())
			{ 
				detailCaches.add(dd);
			}
		}
		return detailCaches;
	} 
	/**
	 * 根据id进行翻译菜单  
	 */
	public  Menu getMenuDetailById(long id) {  
		List<Menu> details=getAll(); 
		for(Menu dd: details)
		{ 
			if(id==dd.getId())
			{
				return dd;
			}
		}
		return null;
	} 
}
