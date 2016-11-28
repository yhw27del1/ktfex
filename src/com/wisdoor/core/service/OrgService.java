package com.wisdoor.core.service;
 
import java.util.List;

import com.wisdoor.core.model.Org;

/*** 
 * 公司表
 * @author    
 */
public interface OrgService  extends BaseService<Org>{ 
	public Org findOrg(String showCoding);
	
	/**
	 * 注销指定的授权服务中心
	 * @param id 指定的授权服务中心的id号
	 * @return true 注销成功；false 注销失败
	 * */
	public boolean cancel(long id) throws Exception;
	
	//取某机构的所有下属机构
	public List<Org> getOrgChildren(String showCoding);
}
