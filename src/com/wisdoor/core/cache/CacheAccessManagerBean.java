package com.wisdoor.core.cache;
 
import java.util.List;

import javax.annotation.Resource;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.model.Menu;
import com.wisdoor.core.service.impl.BaseServiceImpl;
 


/**
 * 对缓存信息进行更新及初始化
 * @author 	 
 */
@Service 
public class CacheAccessManagerBean extends BaseServiceImpl<Menu> implements CacheAccessManager {
	
	
	private BusinessCache businessCache;
	
	private boolean isRun=false; 
	
	/**
	 * 定时器开始执行 
	 */
	public void cacheUpdateJob() throws Exception {
		updateElementsBySort(null);
	}
	
	/**
	 * 定时器开始执行
	 */
	public void dotUpdateJob() {
 
	}
	
	/**
	 * 刷新所有的缓存信息
	 * 
	 * @throws Exception
	 */
	public void initCacheConfig() throws Exception {
		List<CacheIndex> indexs=businessCache.getIndex();
		if (indexs == null || indexs.size() == 0)
			return;
		for (CacheIndex index : indexs) {
			updateElementByName(index);
		}
	}
	
	/**
	 * 根据缓存名称更新缓存
	 * 
	 * @param cacheName
	 * @throws Exception
	 */
	public void refreshCacheByName(String cacheName) throws Exception {
		List<CacheIndex> indexs = businessCache.getIndex();
		if (indexs == null || indexs.size() == 0 || cacheName == null || cacheName.equals(""))
			return;
		for (CacheIndex index : indexs) {
			if (index.getCacheName().equals(cacheName)) {
				updateElementByName(index);
				break;
			}
		}
	}

	/**
	 * 根据缓存名称更新缓存
	 * 
	 * @param index
	 */
	public void updateElementByName(CacheIndex element) throws Exception {
		String type = element.getCacheType();
		// 如果为空，则跳过
		if (type == null)
			return;
		if (type.equals("select")) {
			setSelectCache(element);
		} /*else if (type.equals("tree")) {
			setTreeCache(element);
		} */
	}
	
	/**
	 * 根据缓存名称更新缓存
	 * 
	 * @param index
	 */
	public void updateElementsBySort(String sort) throws Exception{
		if (sort == null)
			sort = "day";
		for(CacheIndex index : businessCache.getIndex())
			if(index.getSort().equals(sort))
				updateElementByName(index);
	}
	
	/**
	 * 一般的缓存信息
	 * 
	 * @param cacheName
	 * @param selectHql
	 */
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	private void setSelectCache(CacheIndex element) { 
			businessCache.put(element.getCacheName(), this.getCommonListData(element.getSelectHql()));
	} 

	/**
	 * 
	 * 初始化缓存信息 
	 */
	public void initCache() throws Exception {
			if(isRun);
			  businessCache.setIndex((new CacheConfigUtils()).getCacheConfig());  
			initCacheConfig();
			GlobalParameters.business=businessCache; 
			isRun=true;
		}
	
	@Resource(name="businessCache") 
	public void setBusinessCache(BusinessCache businessCache) {
		this.businessCache = businessCache;
	}
 
	

	
}



