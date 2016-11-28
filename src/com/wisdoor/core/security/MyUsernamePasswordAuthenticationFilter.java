package com.wisdoor.core.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.MD5;

/**
 * 验证用户名，密码的类
 * 
 * @author  
 * 
 */
public class MyUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public static final String UERNAME = "username";
	public static final String PASSWORD = "password";

	@Resource
	UserService userService;

	@Resource
	MemberBaseService memberBaseService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (!isAllowEmptyValidateCode())
			checkValidateCode(request);
		
		String userName = obtainUsername(request).trim();
		String password = obtainPassword(request).trim();
		//password = MD5.MD5Encode(password);
		// User user = userService.findUser(userName, password);
		try {
			User user = userService.findUser(userName);
			if (user == null) {
				throw new AuthenticationServiceException("用户名或密码错误，请重试");
			}/*else if(user.getTypeFlag().equalsIgnoreCase("1") && user.getUserType()!=null && user.getUserType().equalsIgnoreCase("T")){
				throw new AuthenticationServiceException("投资人不允许从此入口登录！");	
			}*/
			else {
				if (!user.getPassword().equals(password)) {
					user.setLoginCount(user.getLoginCount() + 1);
					if (user.getLoginCount() >= 5) {//edit by sxs 2013.05.24 登录次数限制由3次改为5次
						user.setEnabled(false);
						user.setUserState(User.STATE_LOCKED);//edit by sxs 2013.05.24 添加用户状态为锁定
						user.setLoginCount(0);
						userService.update(user);
						if("1".equals(user.getTypeFlag()))//必须是会员
						 {
							MemberBase mb = memberBaseService.getMemByUser(user);
							mb.setState(MemberBase.STATE_DISABLED);
							memberBaseService.update(mb);
						 }
						throw new AuthenticationServiceException("用户名已经被锁定！");
					} else {
						userService.update(user);
					}
					throw new AuthenticationServiceException("用户名或密码错误！");
				} else {
					if("1".equals(user.getTypeFlag()))//必须是会员
					 {
						MemberBase mb = memberBaseService.getMemByUser(user);
						if(mb.getState().equals(MemberBase.STATE_STOPPED)){

							throw new AuthenticationServiceException("用户已被注销！");
						}
					}
					user.setLoginCount(0);
					userService.update(user);
				}

			}
		} catch (EngineException e) {
			e.printStackTrace();
			throw new AuthenticationServiceException("登录异常！");
		}
		UsernamePasswordAuthenticationToken authenticationRequest = new UsernamePasswordAuthenticationToken(
				userName, password);
		setDetails(request, authenticationRequest);
		return this.getAuthenticationManager().authenticate(
				authenticationRequest);

	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(UERNAME);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		return null == obj ? "" : obj.toString();
	}
	
	
	
	
	
	private boolean allowEmptyValidateCode = false; 
	private String validateCodeParameter = DEFAULT_VALIDATE_CODE_PARAMETER; 
	public static final String DEFAULT_VALIDATE_CODE_PARAMETER = "userCode";	

	/**
	 * 
	 * <li>比较session中的验证码和用户输入的验证码是否相等</li>
	 * 
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (BaseTool.isNull(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException("验证码错误");
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(validateCodeParameter);
	}

	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute("_TXPT_AUTHKEY"); 
		return null == obj ? "" : obj.toString();
	}
 
 
	public boolean isAllowEmptyValidateCode() {
		return allowEmptyValidateCode;
	}

	public void setAllowEmptyValidateCode(boolean allowEmptyValidateCode) {
		this.allowEmptyValidateCode = allowEmptyValidateCode;
	}
}
