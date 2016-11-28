package com.kmfex.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberGuarantee;
import com.kmfex.service.MemberGuaranteeService;
import com.wisdoor.struts.BaseAction;

/**
 * 担保公司附加信息(担保情况)action
 * 
 * @author aora
 * @version 2012-04-26
 * 2013年07月17日11:14   修改save方法，过滤掉页面文本编辑器的特殊字符
 * */
@Controller
@Scope("prototype")
public class MemberGuaranteeAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7033017166894841014L;

	private static String LIST = "list";

	private static String EDIT = "edit";

	/**
	 * 
	 * */
	@Resource
	private MemberGuaranteeService memberGuaranteeService = null;

	private List<MemberGuarantee> memberGuraantees;

	private Map<String, String> dbedTypes = new LinkedHashMap<String, String>();

	private Map<String, String> jzdTypes = new LinkedHashMap<String, String>();

	private MemberGuarantee memberGuarantee = null;

	private String id;

	/**
	 * 查询参数：会员名或用户名
	 * */
	private String keyword;

	/**
	 * 列表页面
	 * */
	public String list() {
		ServletActionContext.getRequest().setAttribute(
				"pageView",
				memberGuaranteeService.listByLatest(keyword, this
						.getShowRecord(), this.getPage()));
		return LIST;
	}

	/**
	 * 修改页面
	 * */
	public String edit() {
		if (isModify()) {
			memberGuarantee = this.memberGuaranteeService.selectById(id);
		} else {
			memberGuarantee = new MemberGuarantee();
		}
		return EDIT;
	}

	/**
	 * 通用担保情况展示页面
	 * */
	public String ajaxDetail() {
		memberGuarantee = this.memberGuaranteeService.selectById(id);
		if (null == memberGuarantee) {
			memberGuarantee = new MemberGuarantee();
		}
		return "ajaxDetail";
	}

	/**
	 * 保存修改或新增的用户
	 * */
	public String save() {
		
		if (null != this.memberGuarantee && null != this.memberGuarantee.getNote() && this.memberGuarantee.getNote().contains("color:black;")) {
			this.memberGuarantee.setNote(this.memberGuarantee.getNote().replaceAll("color:black;", " "));
		}
		
		try {
			if (isModify()) {
				MemberGuarantee oldMemberGuarantee = memberGuaranteeService
						.selectById(id);
				oldMemberGuarantee.setState(MemberGuarantee.STATE_HISTORY);
				memberGuaranteeService.update(oldMemberGuarantee);
			}
			memberGuarantee.setId(null);
			
			//修改历史数据
			List<MemberGuarantee> mbs=memberGuaranteeService.getCommonListData("from MemberGuarantee m where m.memberBase.id='"+memberGuarantee.getMemberBase().getId()+"'");
			for(MemberGuarantee mb:mbs){
				if(null==mb.getNote()||"".equals(mb.getNote())){
					mb.setNote(this.memberGuarantee.getNote());
					memberGuaranteeService.update(mb);  
				}
			}
			memberGuaranteeService.insert(memberGuarantee);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public List<MemberGuarantee> getMemberGuraantees() {
		return memberGuraantees;
	}

	public void setMemberGuraantees(List<MemberGuarantee> memberGuraantees) {
		this.memberGuraantees = memberGuraantees;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberGuarantee getMemberGuarantee() {
		return memberGuarantee;
	}

	public void setMemberGuarantee(MemberGuarantee memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}

	/**
	 * 返回是否为修改
	 * */
	private boolean isModify() {
		return null != id && !"".equals(id);
	}

	public Map<String, String> getDbedTypes() {
		dbedTypes.put("一级", "一级");
		dbedTypes.put("二级", "二级");
		dbedTypes.put("三级", "三级");
		dbedTypes.put("四级", "四级");
		dbedTypes.put("五级", "五级");
		return dbedTypes;
	}

	public Map<String, String> getJzdTypes() {
		jzdTypes.put("一级", "一级");
		jzdTypes.put("二级", "二级");
		jzdTypes.put("三级", "三级");
		jzdTypes.put("四级", "四级");
		jzdTypes.put("五级", "五级");
		return jzdTypes;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
