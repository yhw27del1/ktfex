package com.kmfex.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberType;
import com.kmfex.service.MemberTypeService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;

/**
 * 会员类型Action
 * */
@Controller
@Scope("prototype")
public class MemberTypeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3300741683131513060L;

	/**
	 * 列出会员类型
	 * */
	private static final String LIST = "list";

	private static final String EDIT = "edit";

	// private static final String

	@Resource
	private MemberTypeService memberTypeService;

	private String id;
	private String name;
	private String code;
	private String keyword;
	/**
	 * 修改标记，false为新增；true为修改
	 * */
	private boolean modify = false;

	public String addPage() {
		return SUCCESS;
	}

	/**
	 * 修改或者新增会员类型
	 * */
	public String save() {
		MemberType mt = null;
		if (modify) {
			mt = memberTypeService.selectById(id);
			mt.setName(name);
			mt.setCode(code);
			mt.setUpdateDate(new Date());
			try {
				memberTypeService.update(mt);
			} catch (EngineException e) {
				e.printStackTrace();
				return EDIT;
			}
		} else {
			mt = new MemberType();
			mt.setName(name);
			mt.setCode(code);
			try {
				memberTypeService.insert(mt);
			} catch (EngineException e) {
				e.printStackTrace();
				return EDIT;
			}
		}
		return list();
	}

	public String del() {
		if (null != id && !"".equals(id)) {
			try {
				memberTypeService.delete(id);
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		return list();
	}

	/**
	 * 修改或新增页面
	 * */
	public String edit() {
		if (modify) {
			MemberType mt = memberTypeService.selectById(id);
			this.name = mt.getName();
			this.code = mt.getCode();
		}
		return EDIT;
	}

	public String list() {
		PageView<MemberType> pageView = new PageView<MemberType>(
				getShowRecord(), getPage());
		StringBuilder sb = new StringBuilder("");
		List<String> params = new ArrayList<String>();
		if (null != keyword && !"".equals(keyword)) {
			sb.append("name like ? or code like ? ");
			params.add(keyword);
			params.add(keyword);
		}
		try {
			pageView.setQueryResult(memberTypeService.getScrollData(pageView
					.getFirstResult(), pageView.getMaxresult(), sb.toString(),
					params));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return LIST;
	}

	public MemberTypeService getMemberTypeService() {
		return memberTypeService;
	}

	public void setMemberTypeService(MemberTypeService memberTypeService) {
		this.memberTypeService = memberTypeService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

}
