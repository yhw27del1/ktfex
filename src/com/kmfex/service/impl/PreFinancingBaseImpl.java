package com.kmfex.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.PreFinancingBase;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.PreFinancingBaseService;
import com.wisdoor.core.model.CodeRule;
import com.wisdoor.core.service.CodeRuleService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.StringUtils;

/**
 * 
 * @author
 * 
 * 修改记录 
 * 2012-08-06   1、buildPreFinancingCode方法针对X的包加入o.state>0
 * 2012-08-17   1、根据融资项目类型（10表示本息保障，12本金保障,15无担保)和 年份 递增生成项目编号
 */
@Service
@Transactional
public class PreFinancingBaseImpl extends BaseServiceImpl<PreFinancingBase> implements PreFinancingBaseService{
	@Resource UserService userService;
	@Resource MemberBaseService memberBaseService;
	@Resource CodeRuleService codeRuleService;
	public String buildPreFinancingCode(String X)  throws Exception{  
		StringBuffer code = new StringBuffer();
 		code.append(X);
 		String year=DateUtils.getTwoYear();
 		code.append(year);   
		CodeRule codeRule=codeRuleService.selectById(X.trim()); 
		if(null!=codeRule)
		{ 
			codeRule.setCurrentMax(codeRule.getCurrentMax()+1);
			if(!year.equals(codeRule.getYear())){
				codeRule.setYear(year);
				codeRule.setCurrentMax(1);
			}
			codeRuleService.update(codeRule); 
	 		code.append(StringUtils.fillZero(6,codeRule.getCurrentMax()+""));
			return code.toString();
		}else{
			return "-1";//没找到融资项目规则
		}
 		
/* 		
 		long count=0;
 		SimpleDateFormat format = new SimpleDateFormat("yyyy");
 		StringBuilder sb = new StringBuilder(" from PreFinancingBase o where  o.state>0 "); 
		sb.append(" and ");
		sb.append(" to_char(o.createDate,'yyyy')= '" + format.format(new Date())+ "'"); 
		
		 
		if("A".equals(X)){//本金担保
 			sb.append(" and o.code like 'A%'  "); 
 			//count=this.getScrollDataCount(" from PreFinancingBase o where o.fxbzState='1'  and o.state>0 ");
 		}else if("B".equals(X)){//本息担保
 			sb.append(" and o.code like 'B%'  "); 
 		}else if("C".equals(X)){
 			sb.append(" and o.code like 'C%'  "); //无担保 
 		}else if("X".equals(X)){ 
 			sb.append(" and o.code like 'X%'  ");  
 		}else if("D".equals(X)){
	      sb.append(" and o.flag='1'  ");  
	    }
 
 		sb.append("    order by o.code desc ");
 		PreFinancingBase preFinancingBase=this.selectByHql(sb.toString());
 		if(null!=preFinancingBase){
 			count=Long.parseLong(preFinancingBase.getCode().substring(3));
 		} 
 		code.append(StringUtils.fillZero(6,(count+1)+""));
		return code.toString();*/
	} 
}
	 
