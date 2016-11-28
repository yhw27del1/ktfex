package com.kmfex.service.impl;

import org.springframework.stereotype.Service;

import com.kmfex.model.Region;
import com.kmfex.service.RegionService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
/**
 * 地点
 * @author eclipse
 *
 */
@Service
public class RegionImpl extends BaseServiceImpl<Region> implements RegionService{

	@Override
	public Region selectByAreacode(String areacode) {
		String hql="from Region where areacode = '"+areacode+"' order by areacode";
		return this.selectByHql(hql);
	}

}
