package com.kmfex.action;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.CostBase;
import com.kmfex.service.CostCategoryService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;

/**
 * 收费项目管理
 * 
 * @author eclipse
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class CostCategoryAction extends BaseAction implements Preparable {
	@Resource
	CostCategoryService costCategoryService;

	private CostBase costbase;
	private String costbase_id;

	/**
	 * 查询关键字
	 * */
	private String keyword;

	/**
	 * 列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		// PageView<CostBase> pageView = new PageView<CostBase>(getShowRecord(),
		// getPage());
		// pageView.setQueryResult(costCategoryService.getScrollData(pageView
		// .getFirstResult(), pageView.getMaxresult()));
		ServletActionContext.getRequest().setAttribute(
				"pageView",
				costCategoryService.queryForConditions(keyword,
						getShowRecord(), getPage()));
		return "list";
	}

	/**
	 * 新增
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception {
		return "ui";
	}

	/**
	 * 保存修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		if (this.costbase_id != null && !"".equals(this.costbase_id)) {
			this.costCategoryService.update(this.costbase);
		} else {
			this.costCategoryService.insert(this.costbase);
		}
		return "save";
	}

	public void setCostbase(CostBase costbase) {
		this.costbase = costbase;
	}

	public CostBase getCostbase() {
		return costbase;
	}

	public void setCostbase_id(String costbase_id) {
		this.costbase_id = costbase_id;
	}

	public String getCostbase_id() {
		return costbase_id;
	}

	@Override
	public void prepare() throws Exception {
		if (costbase_id == null || "".equals(costbase_id)) {
			costbase = new CostBase();
		} else {
			this.costbase = costCategoryService.selectById(costbase_id);
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
