package com.kmfex.service;

import com.kmfex.model.Region;
import com.wisdoor.core.service.BaseService;
/**
 * 
 * @author eclipse
 *
 */
public interface RegionService extends BaseService<Region>{
public Region selectByAreacode(String areacode);
}
