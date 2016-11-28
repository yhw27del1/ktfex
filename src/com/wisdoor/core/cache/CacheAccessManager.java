package com.wisdoor.core.cache;
 
 
import com.wisdoor.core.model.Menu;
import com.wisdoor.core.service.BaseService;
 

/**
 * 对缓存信息进行更新及初始化
 * @author 	 
 */
public interface CacheAccessManager extends BaseService<Menu>{  
	/**
	 * 
	 * 初始化缓存信息 
	 */
	public void initCache() throws Exception;
	  
}



