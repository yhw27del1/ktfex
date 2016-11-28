package com.kmfex.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Resource;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.UserService;
import com.wisdoor.struts.BaseAction;
/*** 
 * @author linuxp  
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class UserManagerAction  extends BaseAction  {
	private String id;
	private String userType = "3";
	private String keyWord = "";
	@Resource UserService userService;
	private Date startTime;
	private Date endTime;
	
	public String list() throws Exception {
		try {
			PageView<User> pageView = new PageView<User>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("regTime", "desc");
			if(null!=keyWord&&!"".equals(keyWord.trim())){
				StringBuilder sb = new StringBuilder(); 
				List<String> params = new ArrayList<String>(); 
				keyWord = keyWord.trim();
				sb.append(" o.userType="+userType);
				sb.append(" and (");
				sb.append(" o.username like ?");
				params.add("%"+keyWord+"%");
				sb.append(" or ");
				sb.append(" o.realname like ?"); 
				params.add("%"+keyWord+"%");
				sb.append(" )");
				pageView.setQueryResult(userService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			}else{
				StringBuilder sb = new StringBuilder(); 
				List<String> params = new ArrayList<String>(); 
				sb.append(" o.userType='"+userType+"' ");
				pageView.setQueryResult(userService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
			}
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		String str = "list";
		if("1".equals(userType)){
			str = "listPerson";
		}else if("2".equals(userType)){
			str = "listCompany";
		}
        return str;
	}
	
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}


	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
