package com.kmfex.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberBase;
import com.kmfex.model.TreatyBase;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.TreatyService;
import com.wisdoor.core.model.User;
import com.wisdoor.struts.BaseAction;

/**
 * 
 * @author eclipse
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class TreatyBaseAction extends BaseAction {
	
	@Resource TreatyService treatyService;
	@Resource MemberBaseService memberBaseService;
	private List<TreatyBase> treatyBases;
	/**
	 * 个人协议列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		User u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(u==null){return "login";}
		MemberBase memberbase = memberBaseService.selectById(" from MemberBase where user.id = ?", new Object[]{u.getId()});
		if(memberbase==null) return "login"; 
		treatyBases = treatyService.getScrollDataCommon(" from TreatyBase where memberBase.id = ?", new String[]{memberbase.getId()});
		return "list";
	}
	public void setTreatyBases(List<TreatyBase> treatyBases) {
		this.treatyBases = treatyBases;
	}
	public List<TreatyBase> getTreatyBases() {
		return treatyBases;
	}
}
