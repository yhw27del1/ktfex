package com.kmfex.cxfrs.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import com.kmfex.Constant;
import com.kmfex.cxfrs.SystemService;
import com.kmfex.cxfrs.vo.MessageTip;
import com.kmfex.model.MemberBase;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.MD5;

@SuppressWarnings("serial")
public class SystemServiceImpl implements SystemService {

	public static final String  fields="select  id, code, shortname, maxamount, currenyamount, curcaninvest, rate, term, returnpattern, haveinvestnum, fxbzstate, fxbzstatename, startdate, enddate, qyzs, fddbzs, czzs, dbzs, zhzs, zhzsstar, qyzsnote, fddbzsnote, czzsnote, dbzsnote, zhzsnote, yongtu, address, industry, companyproperty, state_, statename, financierid, financiercode, financiername, guaranteeid, guaranteecode, guaranteename, logoname, guaranteenote, terminal, to_char(modifydate,'yyyy-MM-dd HH24:mi:ss') as modifydate, createdate, preinvest, interestday, businesstypeid ";
	@Resource
	transient UserService userService;
	@Resource
	transient MemberBaseService memberBaseService;
	@Resource
	transient OrgService orgService;
	
	@Override
	public String getServerTime() {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}

	@Override
	public MessageTip login(String un, String up, String ut) {
		// TODO Auto-generated method stub
		MessageTip tip = new MessageTip(true, "success!");
		try {		
			up = MD5.MD5Encode(up);
			User user = userService.findUser(un, up);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("户名或密码错误");
				return tip;
			} else {
				if (!user.isEnabled()) {
					
					if (user.getUserState().equals("1")){
					   tip.setMsg("待审核,暂不允许登录");
					   tip.setSuccess(false);
					   
					}
					if (user.getUserState().equals("4")){
					   tip.setMsg("已停用,不允许登录");
					   tip.setSuccess(false);					 
					}
					if (user.getUserState().equals("3")){
					   tip.setMsg("未通过审核,不允许登录");
					   tip.setSuccess(false);
					}
					return tip;
					
				}
				if (!user.getUserType().equals(ut)) {
					tip.setSuccess(false);
					tip.setMsg("类型错误");
					return tip;
				}
			}
			tip.setParam1(user.getRealname());// 真实姓名
			MemberBase memberBase = memberBaseService.getMemByUser(user);
			if (null != memberBase) {
				if (memberBase.getState().equals(MemberBase.STATE_STOPPED)) {
					tip.setSuccess(false);
					tip.setMsg("会员已注销");
					return tip;
				}
				tip.setParam2(orgService.findOrg(memberBase.getOrgNo()).getName() + "(" + memberBase.getOrgNo() + ")");
			}// 开户机构名称
			Set<Role> roles = user.getRoles();
			for (Role r : roles) {
				if (Constant.INITROLEID == r.getId())// 初始角色要修改密码才能使用
				{
					tip.setParam3("初始角色");
					return tip;
				}
			}
			if (null != memberBase.getMemberLevel()) {
				tip.setParam4(memberBase.getMemberLevel().getLevelname());
			}
			tip.setDateStr(getServerTime());
			tip.setParam5(memberBase.getBanklib().getCaption());
		//	tip.setParam6(memberBase.getUser().getFlag());//签约标志:未签约=0；签约中=1；已签约=2；已解约=3
		//	tip.setParam9(memberBase.getUser().getSignBank()+"");//三方存管签约行:未签约三方存管=0,签约华夏三方存管=1,签约招商三方存管=2
		//	tip.setParam10(memberBase.getUser().getSignType()+"");//三方存管签约类型:未签约三方存管=0,签约本行=1,签约他行=2
			
			BigDecimal b = new BigDecimal(memberBase.getUser().getUserAccount().getOld_balance()+"");
			double param11 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
			tip.setParam7(param11+ "");			
			//tip.setParam8(memberBase.getUser().isAgreement() + "");//是否签署过补充协议   
			//tip.setParam11(userPriorityService.firstAutoInvest(userName) + "");//是否委托自动投标  
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("用户登录出现异常");
			return tip;
		}
	}

	@Override
	public MessageTip updatePassword(String un, String ut, String op, String np) {
		// TODO Auto-generated method stub
		MessageTip tip = new MessageTip(true, "修改密码成功");
		try {
			if (BaseTool.isNull(un)) {
				tip.setSuccess(false);
				tip.setMsg("户名不能为空");
				return tip;
			}
			if (BaseTool.isNull(ut)) {
				tip.setSuccess(false);
				tip.setMsg("类型不能为空");
				return tip;
			}
			if (BaseTool.isNull(op)) {
				tip.setSuccess(false);
				tip.setMsg("原密码不能为空");
				return tip;
			}
			if (BaseTool.isNull(np)) {
				tip.setSuccess(false);
				tip.setMsg("新密码不能为空");
				return tip;
			}
			User user = userService.findUser(un);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("户名不存在");
				return tip;
			}
			op = MD5.MD5Encode(op);
			user = userService.findUser(un, op);
			if (null == user) {
				tip.setSuccess(false);
				tip.setMsg("原密码错误");
				return tip;
			} else {
				if (!user.getUserType().equals(ut)) {
					tip.setSuccess(false);
					tip.setMsg("类型错误");
					return tip;
				}
			}
			user.setPassword(MD5.MD5Encode(np));
			if ("R".equals(user.getUserType()))// 还是融资方
			{
				user.getRoles().clear();
				user.getRoles().add(new Role(Constant.RZROLEID));
			}
			if ("T".equals(user.getUserType()))// 判断是投资人
			{
				user.getRoles().clear();
				user.getRoles().add(new Role(Constant.TZROLEID));
			}
			userService.update(user);

			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			tip.setSuccess(false);
			tip.setMsg("修改密码出现异常");
			return tip;
		}
	}
	
	


}
