package com.kmfex.action;

import java.util.List;
import javax.annotation.Resource;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.kmfex.model.InvestCondition;
import com.kmfex.model.MemberLevel;
import com.kmfex.service.InvestConditionService;
import com.kmfex.service.MemberLevelService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.struts.BaseAction;

/**
 * 投标投资约束
 * 
 * @author eclipse
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class InvestConditionAction extends BaseAction implements Preparable {

	@Resource
	InvestConditionService investConditionService;

	@Resource
	private MemberLevelService memberLevelService;

	private String investcondition_id;

	private String memberLeveId;
	
	private List<MemberLevel> memberLevels = null;

	private InvestCondition investcondition;

	/**
	 * 取列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		PageView<InvestCondition> pageView = new PageView<InvestCondition>(
				getShowRecord(), getPage());
		pageView.setQueryResult(investConditionService.getScrollData(pageView
				.getFirstResult(), pageView.getMaxresult()));
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "list";
		
	}

	/**
	 * 取列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception {
		return "ui";
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	public String edit() {
		try {
			investcondition.setMemberLevel(this.memberLevelService.selectById(memberLeveId));
			if (this.investcondition_id == null
					|| "".equals(this.investcondition_id)) {				
				this.investConditionService.insert(this.investcondition);
			} else {
				this.investConditionService.update(this.investcondition);
			    
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "edit";
	}

	@Override
	public void prepare() throws Exception {
		this.setMemberLevels(memberLevelService.listAll());
		if (this.investcondition_id == null
				|| "".equals(this.investcondition_id)) {
			this.investcondition = new InvestCondition();
		} else {
			this.investcondition = this.investConditionService
					.selectById(this.investcondition_id);
			this.memberLeveId=investcondition.getMemberLevel().getId();
		}

	}

	public void setInvestcondition_id(String investcondition_id) {
		this.investcondition_id = investcondition_id;
	}

	public String getInvestcondition_id() {
		return investcondition_id;
	}

	public void setInvestcondition(InvestCondition investcondition) {
		this.investcondition = investcondition;
	}

	public InvestCondition getInvestcondition() {
		return investcondition;
	}

	/**
	 * 判断是否为修改记录
	 * */
	public boolean isModify() {
		return null != investcondition_id && !"".equals(investcondition_id);
	}

	public String getMemberLeveId() {
		return memberLeveId;
	}

	public void setMemberLeveId(String memberLeveId) {
		this.memberLeveId = memberLeveId;
	}

	public List<MemberLevel> getMemberLevels() {
		return memberLevels;
	}

	public void setMemberLevels(List<MemberLevel> memberLevels) {
		this.memberLevels = memberLevels;
	}


}
