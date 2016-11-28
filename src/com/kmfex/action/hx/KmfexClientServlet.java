package com.kmfex.action.hx;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kmfex.hxbank.HxbankParam;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.hx.HxbankDeal;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UserService;

//华夏银行https服务器调用此servlet
public class KmfexClientServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882549789140208902L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String json = req.getParameter("json");
		String msg = "";
		if(null!=json){
			HxbankParam p = new HxbankParam();
			p = p.toParam(json);
			if("DZ001".equals(p.getCode())){//子账户同步，摊位号鉴权
				ApplicationContext cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
				UserService userService = (UserService)cxt.getBean("userImpl");
				User u = userService.findUser(p.getMerAccountNo());
				if(null!=u){
					u.setFlag("2");
					u.setSynDate_bank(new Date());
					u.setSignDate(u.getSynDate_bank());
					u.setAccountNo(p.getAccountNo());
					try {
						userService.update(u);
						msg = "摊位号:"+p.getMerAccountNo()+"同步成功。";
					} catch (EngineException e) {
						msg = "摊位号:"+p.getMerAccountNo()+"，同步时入库异常。";
					}
				}else{
					msg = "摊位号:"+p.getMerAccountNo()+"错误，系统找不到此摊位号。";
				}
			}else if("DZ002".equals(p.getCode())){//入金通知，摊位号及子账号鉴权，生成“银转商”的资金流水
				ApplicationContext cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
				UserService userService = (UserService)cxt.getBean("userImpl");
				AccountService accountService = (AccountService)cxt.getBean("accountImpl");
				AccountDealService accountDealService = (AccountDealService)cxt.getBean("accountDealImpl");
				User u = userService.getUser(p.getMerAccountNo(),p.getAccountNo());
				if(null!=u){
					Account a = accountService.selectById(u.getUserAccount().getId());
					
					AccountDeal ad = new AccountDeal();
					ad.setCheckDate(ad.getCreateDate());// checkDate与createDate同步。需要审核的业务，在审核时再更新checkDate字段
					ad.setAccount(a);
					ad.setType(AccountDeal.BANK2ZQ);//银转商
					ad.setCheckFlag("24");
					ad.setUser(a.getUser());// 操作者
					ad.setMoney(p.getAmt());// 发生额
					ad.setPreMoney(a.getBalance() + a.getFrozenAmount());// 充值前金额
					ad.setNextMoney(ad.getPreMoney() + ad.getMoney());// 充值后金额=充值前金额+发生额
					ad.setCheckUser(ad.getUser());// 审核者为操作者
					ad.setBusinessFlag(18);
					ad.setSuccessFlag(true);
					ad.setSuccessDate(ad.getCreateDate());
					
					a.setBalance(a.getBalance()+p.getAmt());
					a.setOld_balance(a.getOld_balance()+p.getAmt());
					
					try {
						accountService.update(a);
						accountDealService.insert(ad);
						msg = "摊位号:"+p.getMerAccountNo()+"入金:"+p.getAmt()+"元成功。";
					} catch (EngineException e) {
						msg = "摊位号:"+p.getMerAccountNo()+"入金:"+p.getAmt()+"元，入库异常。";
					}
				}else{
					msg = "摊位号:"+p.getMerAccountNo()+"错误，系统找不到此摊位号。";
				}
			}else if("DZ003".equals(p.getCode())){//出金申请，，摊位号及子账号鉴权
				ApplicationContext cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
				UserService userService = (UserService)cxt.getBean("userImpl");
				HxbankDealService hxbankDealService = (HxbankDealService)cxt.getBean("hxbankDealImpl");
				User u = userService.getUser(p.getMerAccountNo(),p.getAccountNo());
				if(null!=u){
					//产生一条出金送审结果发送记录(待审核)，需要交易市场的工作人员审核。
					HxbankDeal d = new HxbankDeal("出金审核结果发送","DZ007");
					d.setOperator(userService.findUser("kmfexadmin"));
					d.setUser(u);
					d.setBankTxSerNo(p.getBankTxSerNo());
					d.setMerchantTrnxNo(p.getMerchantTrnxNo());
					d.setAmt(p.getAmt());
					d.setAccountNo(p.getAccountNo());
					d.setMerAccountNo(p.getMerAccountNo());
					d.setResult("2");
					d.setSuccess(false);
					try {
						hxbankDealService.insert(d);
						msg = "摊位号:"+p.getMerAccountNo()+"出金申请:"+p.getAmt()+"元成功，等待出金审核结果发送。";
					} catch (EngineException e) {
						msg = "摊位号:"+p.getMerAccountNo()+"出金申请:"+p.getAmt()+"元，入库异常。";
					}
				}else{
					msg = "摊位号:"+p.getMerAccountNo()+"错误，系统找不到此摊位号。";
				}
			}else{
				msg = "未知交易码:"+p.getCode();
			}
		}else{
			msg = "参数错误。";
		}
		PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(),"UTF-8"));
            writer.write(msg);
            writer.flush();
            writer.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
	}
	
}
