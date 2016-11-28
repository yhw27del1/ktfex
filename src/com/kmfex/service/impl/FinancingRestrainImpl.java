package com.kmfex.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.FinancingRestrain;
import com.kmfex.model.UserGroup;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.UserGroupService;
import com.wisdoor.core.service.impl.BaseServiceImpl;

@Service
@Transactional
public class FinancingRestrainImpl extends BaseServiceImpl<FinancingRestrain> implements FinancingRestrainService{
	
	@Resource UserGroupService userGroupService;
	public String getFinancingCodes(String username){
		//得到用户的组
		List<UserGroup> groups=userGroupService.getCommonListData("from UserGroup o where o.user.username='"+username+"'");  
		String groupIds="";
		for(UserGroup g:groups) {
			//sql += "and o.preInvest!='1' ";        
			groupIds+= "'"+g.getGroupId()+"',";      
		}   
		if (null != groupIds && !"".equals(groupIds.trim())) {
			groupIds=groupIds.substring(0, groupIds.length()-1);
		}else{
			groupIds+= "''";  
		}
		
		//得到组的融资项目
		List<FinancingRestrain> frs=this.getCommonListData("from FinancingRestrain o where o.groupId in ("+groupIds+")");  
		String codeIds="";
		for(FinancingRestrain f:frs) {       
			codeIds+= "'"+f.getFinancingCode()+"',";           
		}   
		if (null != codeIds && !"".equals(codeIds.trim())) {
			codeIds=codeIds.substring(0, codeIds.length()-1);
		} 
		return codeIds;
	} 
	
	public String getFinancingCodesByGroup(String groupId){
		 
		//得到组的融资项目
		List<FinancingRestrain> frs=this.getCommonListData("from FinancingRestrain o where o.groupId='"+groupId+"' ");  
		String codeIds="";
		for(FinancingRestrain f:frs) {       
			codeIds+= f.getFinancingCode()+",";      
		}   
		if (null != codeIds && !"".equals(codeIds.trim())) {
			codeIds=codeIds.substring(0, codeIds.length()-1);
		} 
		
		return codeIds;
	}
	
    /**
     * 验证分组的约束规则1--新用户体验
     * @param userName
     * @param financingBaseCode
     * @return
     */
	public List<Map<String,Object>> usergrouprestrainCheck(String userName,String financingBaseCode) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		String fields = "ugp.id as ugpid, su.username as username,ugp.investcount as investcount,ugp.investmaxcount as investmaxcount,sum(ugp.investmaxmoney) as investmaxmoney";
		String tables = "t_usergroup ug1 ,sys_user su,t_usergrouprestrain1 ugp";
		 StringBuilder sb = new StringBuilder(); 
		 sb.append(" ug1.user_id=su.id and ugp.id=ug1.id  ")
		.append(" and   su.username='"+userName+"' ")
		.append(" and   ug1.groupid_ in(  select gr.id from t_group gr where gr.id in(  select t.groupid_ from T_FINANCINGRESTRAIN t where t.financingcode='"+financingBaseCode+"')) ")
		.append(" and ugp.investmaxmoney=(select max(ugp.investmaxmoney) as investmaxmoney from t_usergroup ug1 ,sys_user su,t_usergrouprestrain1 ugp ")
		.append(" where  ug1.user_id=su.id and ugp.id=ug1.id ")
		.append(" and   su.username='"+userName+"' ")
		.append("  and   ug1.groupid_ in(  select gr.id from t_group gr where gr.id in(  select t.groupid_ from T_FINANCINGRESTRAIN t where t.financingcode='"+financingBaseCode+"')) ")
		.append(")")  
		.append(" group by ugp.id ,su.username,ugp.investcount,ugp.investmaxcount");
		   
		try {  
			list = this.queryForList(fields, 
					tables, sb.toString()); 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return list;  
	}
	/**
	 * 是否在优先投标组里
	 * @param userName
	 * @param financingBaseCode
	 * @return
	 */ 
	public boolean inUsergroupCheck(String userName,String financingBaseCode) { 
		boolean flag=false;
		StringBuilder sb = new StringBuilder();
		sb.append("  ug1.user_id=su.id  ");
		sb.append("  and   su.username='"+userName+"'   "); 
		sb.append("  and   ug1.groupid_ in(");
		sb.append("  select gr.id from t_group gr where gr.id in(");
		sb.append("  select t.groupid_ from T_FINANCINGRESTRAIN t where t.financingcode='"+financingBaseCode+"')");
		sb.append(")");
		sb.append(" group by su.username"); 
		try {
			List<Map<String,Object>> list = this.queryForList("su.username as username", 
					"t_usergroup ug1 ,sys_user su", sb.toString()); 
			if(list.get(0).get("username") != null){
				flag=true;
			}
		} catch (Exception e) { 
			//e.printStackTrace(); 
		}
		return flag;
	}
}
