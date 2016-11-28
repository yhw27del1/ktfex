package com.kmfex.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.CostBase;
import com.kmfex.model.CostItem;
import com.kmfex.model.MemberType;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.CostCategoryService;
import com.kmfex.service.MemberTypeService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.User;
import com.wisdoor.struts.BaseAction;

/**
 * 收费标准明细
 * 
 * @author eclipse,aora
 * 
 */
@Controller
@Scope("prototype")
public class ChargingStandardAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7729940483214883207L;

	@Resource
	ChargingStandardService chargingStandardService;
	@Resource
	CostCategoryService costCategoryService;
	@Resource
	MemberTypeService memberTypeService;

	private CostItem costitem;
	private List<CostBase> costbases;
	private List<MemberType> membertypes;
	private String costitem_id;

	/**
	 * 查询关键字：收费项目名称或者编码
	 * */
	private String keyword;

	private String memberTypeId;

	public String list() throws Exception {
		User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setMembertypes(memberTypeService.getList(loginUser));
		try {			
			ServletActionContext.getRequest().setAttribute(
					"pageView",
					chargingStandardService.queryForCondition(keyword,memberTypeId,
							getShowRecord(), getPage()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	/**
	 * 
	 * 取得单条记录信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception {
		// 收费类型
		costbases = costCategoryService.getScrollData().getResultlist();
		// 会员类型
		membertypes = memberTypeService.getScrollData().getResultlist();
		return "ui";
	}

	/**
	 * 
	 * 保存
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		if (this.costitem_id != null && !"".equals(this.costitem_id)) {
			this.chargingStandardService.update(this.costitem);
		} else {
			this.chargingStandardService.insert(this.costitem);
		}
		return "save";
	}

	public void setCostitem(CostItem costitem) {
		this.costitem = costitem;
	}

	public CostItem getCostitem() {
		return costitem;
	}

	@Override
	public void prepare() throws Exception {
		if (this.costitem_id == null) {
			this.costitem = new CostItem();
		} else {
			this.costitem = this.chargingStandardService
					.selectById(costitem_id);
		}
	}

	public void setCostbases(List<CostBase> costbases) {
		this.costbases = costbases;
	}

	public List<CostBase> getCostbases() {
		return costbases;
	}

	public void setMembertypes(List<MemberType> membertypes) {
		this.membertypes = membertypes;
	}

	public List<MemberType> getMembertypes() {
		return membertypes;
	}

	public String getCostitem_id() {
		return costitem_id;
	}

	public void setCostitem_id(String costitemId) {
		costitem_id = costitemId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(String memberTypeId) {
		this.memberTypeId = memberTypeId;
	}

}
