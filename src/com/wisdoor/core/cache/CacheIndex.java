package com.wisdoor.core.cache;

import java.util.Date;

/**
 * @author  
 * @since 2011-12-8
 * @version 1.0
 */
public class CacheIndex {

	private String cacheName;
	
	private String sort;

	private String cacheType;

	private String sortHql;

	private String selectHql;
	
	private int maxResult;
	
	private String  paramType;

	private Date lastUpdate;

	public CacheIndex() {
	}

	public CacheIndex(String cacheName,String sort,String type, String sortHql,
			String selectHql,String mxaResult,String paramType) {
		this.cacheName = cacheName;
		this.sort=sort;
		this.cacheType = type;
		this.sortHql = sortHql;
		this.selectHql = selectHql;
		this.maxResult=new Integer(mxaResult);
		this.lastUpdate=new Date();
		this.paramType=paramType;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCacheType() {
		return cacheType;
	}

	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public String getSortHql() {
		return sortHql;
	}

	public void setSortHql(String sortHql) {
		this.sortHql = sortHql;
	}

	public String getSelectHql() {
		return selectHql;
	}

	public void setSelectHql(String selectHql) {
		this.selectHql = selectHql;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
}
