package com.kmfex.service;
  
import com.kmfex.model.PreFinancingBase;
import com.wisdoor.core.service.BaseService;
/**
 * 融资项目历史记录
 * @author  
 * */
public interface PreFinancingBaseService extends BaseService<PreFinancingBase> {
	/**
	 * XYYNNNNN
	 * X: 融资项目类型标识1位字母，A: 有担保   B：无担保  C：其它
	 * YY：两位年份
	 * NNNNN：5位流水号
	 * 如：A12000001 表示2012年第一笔有担保融资项目 
	 */
	public String buildPreFinancingCode(String X)  throws Exception;
}
