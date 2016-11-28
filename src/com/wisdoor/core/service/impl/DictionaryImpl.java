package com.wisdoor.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wisdoor.core.model.Dictionary;
import com.wisdoor.core.service.DictionaryService;
import com.wisdoor.core.vo.CommonVo;

@Service
@Transactional
public class DictionaryImpl extends BaseServiceImpl<Dictionary> implements DictionaryService {
 
	public  List<CommonVo> getListByUrlKey(String parent)
	{
		List<CommonVo> vos=new ArrayList<CommonVo>();
		try {    
			List<Dictionary> dts=this.getCommonListData(" from Dictionary c where c.parent='"+ parent+"' order by c.id");
			for(Dictionary oj:dts){    
                vos.add(new CommonVo(oj.getId(), oj.getName()));
            }  
			return vos;
		}catch (RuntimeException e) { e.printStackTrace(); return null;}  
	}
 
	public  List<CommonVo> getListByUrlKey2(String parent)
	{
		List<CommonVo> vos=new ArrayList<CommonVo>();
		try {    
			List<Dictionary> dts=this.getCommonListData(" from Dictionary c where c.parent='"+ parent+"' order by c.id");
			for(Dictionary oj:dts){    
				JSONObject othv = JSONObject.fromObject(oj.getOtherValues());   
				vos.add(new CommonVo(oj.getId(), oj.getName(), othv.getString("yysr"), othv.getString("cyry"), othv.getString("zczh")));
			}     
			return vos;
		}catch (RuntimeException e) { return null;}  
	}
		
}