package com.kmfex.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kmfex.model.MemberLevel;
import com.kmfex.service.MemberLevelService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.StringUtils;

@Service
@Transactional
public class MemberLevelImpl extends BaseServiceImpl<MemberLevel> implements
		MemberLevelService {

	@Override
	public List<MemberLevel> listAll() {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(MemberLevel.class);
		return (List<MemberLevel>) this.getListForCriteria(criteria);
	}

	@Override
	public List<MemberLevel> listTAll() {
		try {
			return this.getCommonListData("from MemberLevel where memberType.code = 'T' ");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public MemberLevel getCommonLevel() {
		return this.selectByHql("from MemberLevel where levelname = '普通' ");
	}

	/**
	 * @return 
	 * -1 要删除的ID为空<br/>
	 * -2 要删除的实体不存在<br/>
	 * -3 要删除的ID与移动到的ID重复<br/>
	 * -4 移动到的实体不存在<br/>
	 * -5 要删除的实体下还有关联<br/>
	 */
	@Override
	@Transactional
	public int remove(String remove_id,String target_id) {
		try {
			ArrayList<Object> args_list = new ArrayList<Object>();
			if(StringUtils.isBlank(remove_id)){
				return -1;
			}
			args_list.add(remove_id);
			int total = this.queryForListTotal("l.id", "t_member_level l", "l.id = ?", args_list.toArray());
			if(total <= 0 ){
				return -2;
			}
			
			if(StringUtils.isNotBlank(target_id)){
				if(remove_id.equals(target_id)){
					return -3;
				}
				args_list.clear();
				args_list.add(target_id);
				total = this.queryForListTotal("l.id", "t_member_level l", "l.id = ?", args_list.toArray());
				if(total <= 0 ){
					return -4;
				}
				args_list.clear();
				args_list.add(target_id);
				args_list.add(remove_id);
				this.update("update t_member_base b set b.memberlevel_id = ? where b.memberlevel_id = ?",args_list.toArray(),false);
			}
			
			args_list.clear();
			args_list.add(remove_id);
			total = this.queryForListTotal("b.id", "t_member_base b", "b.id = ?", args_list.toArray());
			if(total > 0){
				return -5;
			}
			
			
			args_list.clear();
			args_list.add(remove_id);
			this.update("delete from t_invest_condition c where c.memberlevel_id = ? ", args_list.toArray(), false);
			int code = this.update("delete from t_member_level l where l.id = ? ", args_list.toArray(), false);
			return code;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return 0;
	}
	
	
	
}
