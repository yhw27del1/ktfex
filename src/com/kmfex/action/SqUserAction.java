package com.kmfex.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.UserService;
import com.wisdoor.struts.BaseAction;
/*** 
 * 授权用户管理员用户类
 * @author    
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class SqUserAction extends BaseAction implements Preparable {
	@Resource UserService userService;  
	private String keyWord = "";  
	public void prepare() throws Exception {
 
	}   
	 
	public String sqList() throws Exception {
		try {
			PageView<User> pageView = new PageView<User>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  "); //后台用户
			User u;
			try {
				u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			} catch (Exception e) { 
				e.printStackTrace();
				return "sqList";
			}
  			if (null == u){
  				return "sqList";
			}
			if (null != u.getOrg().getCoding() && !"".equals(u.getOrg().getCoding())) {
				sb.append(" and o.createBy.org.coding like '" + u.getOrg().getCoding()+ "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			}
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){

				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.username like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.realname like ?"); 
				params.add("%"+keyWord+"%"); 
				sb.append(" or ");
				sb.append(" o.userContact.address like ?"); 
				params.add("%"+keyWord+"%");
				sb.append(" )");
	 		} 
			pageView.setQueryResult(userService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "sqList";
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	 
	
}
