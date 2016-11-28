package com.kmfex.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.Announcement;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.AnnouncementService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberTypeService;
import com.kmfex.service.OpenCloseDealService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Notice;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.NoticeService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.struts.BaseAction;
/**
 * 公告action
 * @author eclipse
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class AnnouncementAction extends BaseAction implements Preparable{
	@Resource AnnouncementService announcementService;
	@Resource NoticeService noticeService;
	@Resource MemberTypeService memberTypeService;
	@Resource UserService userService;
	@Resource MemberBaseService memberBaseService;
	private String announcement_id;
	private String appFlag;
	private Announcement announcement;
	private List<MemberType> membertypes;
	private List<Announcement> announcements;
	private List<Notice> notices;
	/**
	 * 管理列表
	 * @return
	 * @throws Exception
	 */
	public String list_normal() throws Exception{
		try {
			PageView<Announcement> pageView = new PageView<Announcement>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("addtime", "desc");
			pageView.setQueryResult(announcementService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			setMembertypes(memberTypeService.getScrollData().getResultlist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_normal";
	}
	/**
	 * 审核列表
	 * @return
	 * @throws Exception
	 */
	public String list_assessor() throws Exception{
		try {
			
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("addtime", "desc");
			List<String> param = new ArrayList<String>();
			PageView<Announcement> pageView = new PageView<Announcement>(getShowRecord(), getPage());
			pageView.setQueryResult(announcementService.getScrollData(pageView.getFirstResult(),pageView.getMaxresult(),"audit_state = 1",param,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			setMembertypes(memberTypeService.getScrollData().getResultlist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list_assessor";
	}
	/**
	 * 编辑，新增界面
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception{
		setMembertypes(memberTypeService.getScrollData().getResultlist());
		return "ui";
	}
	
	
	public String show() throws Exception{
		try {
			Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = null;
			MemberBase member;
			if("anonymousUser".equals(o)){
				HttpServletRequest request = ServletActionContext.getRequest();
				String username = request.getParameter("username");
				if(username !=null && !"".equals(username)){
					user=userService.findUser(username);
				}
			}else{
				user = (User)o;
			}
			if(user!=null){
				member = memberBaseService.getMemByUser(user);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
				String now = sdf.format(new Date());
				if(member!=null){
					String usertype = member.getMemberType().getCode();
					this.announcements= this.announcementService.getScrollDataCommon("from Announcement where (target = ? or target = 'A') and audit_state = 2 and endtime >= to_date(substr(?,1,10),'yyyy-MM-dd')", new String[]{usertype,now});
				}else{
					this.announcements= this.announcementService.getScrollDataCommon("from Announcement where audit_state = 2 and endtime >= to_date(substr(?,1,10),'yyyy-MM-dd')", new String[]{now});
				}
				
				//发送给指定用户的通知
				this.notices=this.noticeService.getScrollDataCommon("from Notice where STATE = 2 and targetUser like ? and endtime >= to_date(substr(?,1,10),'yyyy-MM-dd') order by addtime desc", new String[]{"%"+user.getRealname()+"("+user.getUsername()+")%",now});
			  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "show";
		
	}
	
	
	
	public String detail() throws Exception{
		try {
			this.announcement = this.announcementService.selectById(this.announcement_id);
			if(null!=appFlag&&"1".equals(appFlag)){
				return "appdetail";  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "detail";
	}
	
	/**
	 * 新增，编辑，保存
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		try {
			if(this.announcement_id==null||"".equals(this.announcement_id)){
				this.announcementService.insert(this.announcement);
			}else{
				this.announcementService.update(this.announcement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "edit";
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception{
		try {
			if(this.announcement_id!=null&&!"".equals(this.announcement_id)){
				this.announcementService.delete(this.announcement_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "del";
	}
	/**
	 * 更改状态_审核
	 * @return
	 * @throws Exception
	 */
	public String state_assessor() throws Exception{
		try {
			if(this.announcement.getId()!=null&&!"".equals(this.announcement.getId())){
				this.announcementService.update(this.announcement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "state_assessor";
	}
	/**
	 * 更改状态_发布
	 * @return
	 * @throws Exception
	 */
	public String state_normal() throws Exception{
		try {
			if(this.announcement.getId()!=null&&!"".equals(this.announcement.getId())){
				this.announcementService.update(this.announcement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "state_normal";
	}
	@Override
	public void prepare() throws Exception {
		if(this.announcement_id==null || "".equals(this.announcement_id)){
			this.announcement = new Announcement();
		}else{
			this.announcement = this.announcementService.selectById(this.announcement_id);
		}
		
	}
	
	@Resource
	OpenCloseDealService openCloseDealService;
	public String getSysTime(){
		HttpServletResponse response = ServletActionContext.getResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String s = "";
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 0) {
			s = "规则未启用";
		} else if (state == 1) {
			s = "已开市";
		} else if (state == 2) {
			s = "已休市";
		} else if (state == 3) {
			s = "已清算";
		} else if (state == 4) {
			s = "已对账";
		} else if (state == 5) {
			s = "已开夜市";
		} else if (state == 6) {
			s = "已休夜市";
		}
		try {
			response.getWriter().print("{\"time\":\""+sdf.format(new Date())+"\",\"state\":\""+s+"\"}");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	public String getAnnouncement_id() {
		return announcement_id;
	}
	public void setAnnouncement_id(String announcementId) {
		announcement_id = announcementId;
	}
	public Announcement getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}
	public void setMembertypes(List<MemberType> membertypes) {
		this.membertypes = membertypes;
	}
	public List<MemberType> getMembertypes() {
		return membertypes;
	}
	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}
	public List<Announcement> getAnnouncements() {
		return announcements;
	}
	public String getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}
	public List<Notice> getNotices() {
		return notices;
	}
	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	} 
}
