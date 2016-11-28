package com.kmfex.action.activity;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.activity.InvestToLend;
import com.kmfex.service.activity.InvestToLendService;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.struts.BaseAction;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class InvestToLendAction extends BaseAction{
	@Resource UserService userService;
	@Resource InvestToLendService investToLendService;
	private InvestToLend lend;
	private List<InvestToLend> lends;
	
	private int type;
	private String username;
	private String code;
	private String id;
	
	public String invest_lend_list(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			StringBuilder hql = new StringBuilder(" 1=1 ");
			if(StringUtils.isNotBlank(this.getKeyWord())){
				hql.append(" and ( user.username like '"+this.getKeyWord()+"%' ");
				hql.append(" or user.realname like '"+this.getKeyWord()+"%' )");
			}
			if(this.type>=0){
				hql.append(" and startDate between to_date('"+sdf.format(getStartDate())+"','yyyy-MM-dd') and to_date('"+sdf.format(endDateNext)+"','yyyy-MM-dd')");
			}else{
				hql.append(" and endDate between to_date('"+sdf.format(getStartDate())+"','yyyy-MM-dd') and to_date('"+sdf.format(endDateNext)+"','yyyy-MM-dd')");
			}
			PageView<InvestToLend> pageView = new PageView<InvestToLend>(this.getShowRecord(),this.getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			pageView.setQueryResult(this.investToLendService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),hql.toString(),null, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "invest_lend_list";
	}
	
	public String lend_ui(){
		if(!StringUtils.isBlank(this.id)){
			this.lend = this.investToLendService.selectById(this.id);
		}else{
			this.lend = new InvestToLend();
			this.lend.setStartDate(new Date());
		}
		return "lend_ui";
	}
	
	public String saveOrUpdate(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			if(StringUtils.isBlank(this.id)){
				User user = this.userService.findUser(this.username);
				if(user == null ){
					response.getWriter().write("用户不存在");
					return null;
				}else{
					if(this.investToLendService.getInvestToLend(user)){
						response.getWriter().write("用户"+this.username+"已是投转贷会员");
						return null;
					}else{
						this.lend.setUser(user);
						this.investToLendService.insert(this.lend);
					}
				}
			}else{
				InvestToLend ld = this.investToLendService.selectById(this.id);
				if(null!=ld){
					ld.setMoney(this.lend.getMoney());
					ld.setStartDate(this.lend.getStartDate());
					ld.setEndDate(this.lend.getEndDate());
					ld.setMemo(this.lend.getMemo());
					this.investToLendService.update(ld);
				}else{
					response.getWriter().write("参数错误");
					return null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}
	
	public String del(){
		try {
			if(StringUtils.isNotBlank(this.id)){
				this.investToLendService.delete(this.id);
			}
		}catch (ConstraintViolationException cve) {
			cve.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "del";
	}
	
	/**
	 * 自动完成
	 * 用户名提示
	 * @return
	 */
	public String autoc_un(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			HttpServletRequest request = ServletActionContext.getRequest();
			String term = request.getParameter("term");
			if(!StringUtils.isBlank(term)){
				String str = this.userService.autocomplate(term);
				out.write(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLend(InvestToLend lend) {
		this.lend = lend;
	}

	public InvestToLend getLend() {
		return lend;
	}

	public void setLends(List<InvestToLend> lends) {
		this.lends = lends;
	}

	public List<InvestToLend> getLends() {
		return lends;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	
	
	
}
