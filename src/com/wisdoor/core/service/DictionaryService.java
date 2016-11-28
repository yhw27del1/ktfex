package com.wisdoor.core.service;
 
import java.util.List;

import com.wisdoor.core.model.Dictionary;
import com.wisdoor.core.vo.CommonVo;

 
public interface DictionaryService  extends BaseService<Dictionary>{ 
 
	public  List<CommonVo> getListByUrlKey(String parent);
	public  List<CommonVo> getListByUrlKey2(String parent);
}
