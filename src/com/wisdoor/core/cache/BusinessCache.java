package com.wisdoor.core.cache;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  
 * @since 2012-12-8
 * @version 1.0
 */ 
public class BusinessCache  {

	private static final Logger log = LoggerFactory.getLogger(BusinessCache.class);

	// 注入的缓存信息
	//private static Cache cache;
	
	private  Date weekDotUpdate;
	
	private  Date monthDotUpdate;
	
	private  Cache cache;

	// 缓存中索引信息
	private  List<CacheIndex> index = new ArrayList<CacheIndex>();
	
	public  List<CacheIndex> getIndex() {
		return index;
	}
    
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	

	/**
	 * Gets a value of an element which matches the given key.
	 * 
	 * @param key
	 *            the key of the element to return.
	 * @return The value placed into the cache with an earlier put, or null if
	 *         not found or expired
	 * @throws CacheException
	 */
	public  Object get(Object key) throws CacheException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("key: " + key);
			}
			if (key == null) {
				return null;
			} else {
				Element element = cache.get(key);
				if (element == null) {
					if (log.isDebugEnabled()) {
						log.debug("Element for " + key + " is null");
					}
					return null;
				} else {
					return element.getObjectValue();
				}
			}
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException();  
		}
	}

	public  Object read(Object key) throws CacheException {
		return get(key);
	}

	/**
	 * Puts an object into the cache.
	 * 
	 * @param key
	 *            a key
	 * @param value
	 *            a value
	 * @throws CacheException
	 *             if the {@link CacheManager} is shutdown or another
	 *             {@link Exception} occurs.
	 */
	public  synchronized void update(Object key, Object value)
			throws CacheException {
		put(key, value);
	}

	/**
	 * Puts an object into the cache. 需要对最后放入缓存的时间戳进行记录
	 * 
	 * @param key
	 *            a key
	 * @param value
	 *            a value
	 * @throws CacheException
	 *             if the {@link CacheManager} is shutdown or another
	 *             {@link Exception} occurs.
	 */
	public  synchronized void put(Object key, Object value)
			throws CacheException {
		try {
			Element element = new Element(key, value);
			cache.put(element);
		} catch (IllegalArgumentException e) {
			throw new CacheException();
		} catch (IllegalStateException e) {
			throw new CacheException();
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException();  
		}

	}

	/**
	 * Removes the element which matches the key. <p/> If no element matches,
	 * nothing is removed and no Exception is thrown.
	 * 
	 * @param key
	 *            the key of the element to remove
	 * @throws CacheException
	 */
	public  synchronized void remove(Object key) throws CacheException {
		try {
			cache.remove(key);
		} catch (ClassCastException e) {
			throw new CacheException();
		} catch (IllegalStateException e) {
			throw new CacheException();
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException();
		}
	}


	/**
	 * Remove all elements in the cache, but leave the cache in a useable state.
	 * 
	 * @throws CacheException
	 */
	public  synchronized void clear() throws CacheException {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}


	/**
	 * Remove the cache and make it unuseable.
	 * 
	 * @throws CacheException
	 */
	public  synchronized void destroy() throws CacheException {
		try {
			cache.getCacheManager().removeCache(cache.getName());
		} catch (IllegalStateException e) {
			throw new CacheException();
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException();
		}
	}
	
	public  Date getWeekDotUpdate() {
		return weekDotUpdate;
	}

	public  void setWeekDotUpdate(Date weekUpdate) {
		weekDotUpdate = weekUpdate;
	}

	public  Date  getMonthDotUpdate() {
		return monthDotUpdate;
	}

	public  void setMonthDotUpdate(Date monthUpdate) {
		monthDotUpdate = monthUpdate;
	}

	public void setIndex(List<CacheIndex> index) {
		this.index = index;
	}

}
