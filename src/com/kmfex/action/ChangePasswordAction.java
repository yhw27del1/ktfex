package com.kmfex.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.Constant;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.MD5;
import com.wisdoor.struts.BaseAction;

/**
 * 
 * @author eclipse
 * 
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class ChangePasswordAction extends BaseAction {
	private String password_current;
	private String password_target;
	private String password_confirm;
	@Resource UserService userService;
	public String save() throws Exception {
		try{
			User u  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(u==null){addActionError("登录超时，请重新登录");return "login";}
			if(password_current.trim() ==null || "".equals(password_current.trim())) {addActionError("当前密码不能为空");return "save";}
			if(password_target.trim() == null || "".equals(password_target.trim())) {addActionError("新密码不能为空");return "save";}
			if(password_confirm.trim() ==null || "".equals(password_confirm.trim())) {addActionError("确认密码不能为空");return "save";}
			if(!password_confirm.trim().equals(password_target.trim())) {addActionError("新密码与确认密码不一致");return "save";}
			if(password_target.trim().length()<6) {addActionError("新密码长度不能小于六位");return "save";}
			if(u.getPassword().equals(MD5.MD5Encode(password_current.trim()))){
				User current = userService.selectById(u.getId());
				current.setPassword(MD5.MD5Encode(password_target.trim()));//密码MD5加密
				if("R".equals(current.getUserType()))//还是融资方
				{
				   current.getRoles().clear();  
				   current.getRoles().add(new Role(Constant.RZROLEID));  
				}
				if("T".equals(current.getUserType()))//判断是投资人
				{
				   current.getRoles().clear();  
				   current.getRoles().add(new Role(Constant.TZROLEID));  
				}
				//if("D".equals(current.getUserType()))//判断是担保公司
				//{
				//   current.getRoles().clear();  
				//   current.getRoles().add(new Role(Constant.DBROLEID));  
				//}
				userService.update(current);
				addActionMessage("密码修改成功，新密码将在下次登录时生效"); 
			}else{
				addActionError("当前密码错误");
				return "save";
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "save";
	}
	public String getPassword_current() {
		return password_current;
	}
	public void setPassword_current(String passwordCurrent) {
		password_current = passwordCurrent;
	}
	public String getPassword_target() {
		return password_target;
	}
	public void setPassword_target(String passwordTarget) {
		password_target = passwordTarget;
	}
	public String getPassword_confirm() {
		return password_confirm;
	}
	public void setPassword_confirm(String passwordConfirm) {
		password_confirm = passwordConfirm;
	}
	
}
