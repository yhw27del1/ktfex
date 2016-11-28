package com.kmfex.autoinvest.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.InvestVO;
import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.model.UserPriority;
import com.kmfex.autoinvest.service.UserParameterService;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.autoinvest.utils.AutoInvestUtils; 
import com.kmfex.autoinvest.utils.DrawLevelScoreComparator;
import com.kmfex.autoinvest.vo.Draw;
import com.kmfex.autoinvest.vo.PreParams;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberBase;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.MemberBaseService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.DoubleUtils;

/**
 * 
 *  
 */
@Service 
public class UserParameterImpl extends BaseServiceImpl<UserParameter> implements
		UserParameterService {
	@Resource
	transient FinancingBaseService financingBaseService; 
	@Resource
	transient UserPriorityService userPriorityService;
	@Resource
	transient MemberBaseService memberBaseService; 

	public Map<String, Object> drawByFinancingId(String financingid) throws Exception{
		FinancingBase fb=financingBaseService.selectById(financingid);
		PreParams preParams=new PreParams();
		preParams.setParam1(AutoInvestUtils.ConverterParam1(fb.getZhzs()));//风险评级 
		String[] arr=userPriorityService.ConverterParam2(fb.getBusinessType());
		if("day".equals(arr[0])){//按天 
			preParams.setParam2(arr[1]); 
			preParams.setParam7(fb.getRate());
		}else{
			preParams.setParam2(arr[1]); 
			preParams.setParam6(fb.getRate());
		}  
		preParams.setFid(financingid);
		preParams.setParam5(AutoInvestUtils.ConverterParam5(fb.getFxbzState()));
		preParams.setBalance(fb.getMaxAmount());  
		return drawByPreParams(preParams,fb);
	}
	
	//SQL方式
	public Map<String, Object> drawByPreParams(PreParams preParams,FinancingBase fb) throws Exception {
		try {
			StringBuilder sb = new StringBuilder(" 1=1 and pr.userparameter_id=pa.id and pa.member_id=m.id and m.user_id=u.id and u.useraccount_accountid_=a.accountid_"); //后台用户
			
			sb.append(" and u.usertype_='T' and m.state='2' ");  //取正常使用的,停用和冻结的排除
			
			if (BaseTool.isNotNull(preParams.getParam1())){
				sb.append(" and pa.param1 like '%"+preParams.getParam1()+"%'"); 
			}
	 		
			if (BaseTool.isNotNull(preParams.getParam1())){
				sb.append(" and pa.param2 like '%"+preParams.getParam2()+"%'");
			}
	 		
	 		if (BaseTool.isNotNull(preParams.getKeyword())){
		 		sb.append(" and (u.username like '%"+preParams.getKeyword()+"%' or u.realname like '%"+preParams.getKeyword()+"%' ) ");
	 		}
	 		if (BaseTool.isNotNull(preParams.getParam1())){
	 			sb.append(" and pa.param5 like '%"+preParams.getParam5()+"%'");
	 		}
 
	 		if (preParams.getParam9()>0){
	 			sb.append(" and pa.param9_>="+preParams.getParam9());
	 		}
	 		if (preParams.getParam8()>0){
	 			sb.append(" and pa.param8_>="+preParams.getParam8());
	 		}
	 		
			if(preParams.getBalance()>0&&null!=fb){
				double sj = (preParams.getBalance()/200);
				if(sj<=1000){
					sj = 1000;
				}else{
					sj = DoubleUtils.doubleToQian(sj);
				}
				sb.append(" and a.balance_ >= "+sj); 
			}else{
				sb.append(" and a.balance_ >= "+preParams.getBalance());
			}
			
			if(null!=fb){
				if(fb.getInterestDay()>0){//按天
					if(preParams.getParam7()>0){
						sb.append(" and pa.param7 <= "+preParams.getParam7()); 
					}
					sb.append(" and pa.daymin <= "+fb.getInterestDay()+" and pa.daymax>= "+fb.getInterestDay());
				}else{
					if(preParams.getParam6()>0){
						sb.append(" and pa.param6 <= "+preParams.getParam6()); 
					}
					sb.append(" and pa.monthmin<= "+fb.getBusinessType().getTerm()+" and pa.monthmax>= "+fb.getBusinessType().getTerm()); 
				}
			}else{
				
				if("day".equals(preParams.getQxtype())){
					if (preParams.getParam7()>0){
					   sb.append(" and  pa.param7 <= "+preParams.getParam7());
					}
					if (BaseTool.isNotNull(preParams.getDayMax())){ 
						if(BaseTool.isNotNull(preParams.getDayMin())){
							sb.append(" and pa.daymin <= "+preParams.getDayMin()); 
							sb.append(" and pa.daymax >= "+preParams.getDayMax()); 
						}else{
							sb.append(" and pa.daymax >= "+preParams.getDayMax()); 
						} 
					}else if(BaseTool.isNotNull(preParams.getDayMin())){
						sb.append(" and pa.daymin <= "+preParams.getDayMin()); 
					} 
					
				}else{
					if (preParams.getParam6()>0){
					   sb.append(" and  pa.param6 <= "+preParams.getParam6());
				    }
					if (BaseTool.isNotNull(preParams.getMonthMax())){
						if(BaseTool.isNotNull(preParams.getMonthMin())){
							sb.append(" and pa.monthmin <= "+preParams.getMonthMin()); 
							sb.append(" and pa.monthmax >= "+preParams.getMonthMax());
						}else{
							sb.append(" and pa.monthmax >= "+preParams.getMonthMax());
						}  
					}else if(BaseTool.isNotNull(preParams.getMonthMin())){
						sb.append(" and pa.monthmin <= "+preParams.getMonthMin()); 
					} 
				} 
				
	/*			sb.append(" and (");
				sb.append(" (pa.daymin >= "+preParams.getDayMin());
				sb.append(" and pa.daymax <= "+preParams.getDayMax()+") or");
				
				sb.append(" (pa.monthMin >= "+preParams.getMonthMin());
				sb.append(" and pa.monthMax <= "+preParams.getMonthMax()+")");
				
				sb.append(" )");*/
			}

			

			sb.append(" order by pr.levelscore_ asc");
			List<Map<String, Object>> result = this.userPriorityService.queryForList("u.id as userid,u.username as username,m.id as mid,a.accountid_ as aid,a.balance_ as balance,pa.param1 as param1,pa.param2 as param2,pa.daymin,pa.daymax,pa.monthmin,pa.monthmax,pa.param5 as param5,pa.param6 as param6,pa.param7 as param7,pa.param8_ as param8,pa.param9_ as param9,pr.levelScore_ as levelscore,pr.lastTime_ as lasttime","Auto_UserPriority pr,Auto_UserParameter pa,t_member_base m,sys_user u,sys_account a",sb.toString(),null);

			List<Draw> drs=new ArrayList<Draw>();
			Draw draw=null;
			InvestVO investVO=null;
			long n=0;//符合条件的委托人人数
			double sum_balance = 0d;//委托人可用余额总额
			double sum_balance_zq = 0d;//委托人可用余额(整千)总额
			double w = 0d;//符合条件的委托金额
			for(Map<String,Object> up:result){
				long userid = Long.parseLong(up.get("userid").toString());
				String username = up.get("username").toString();
				String mid = up.get("mid").toString();
				long aid = Long.parseLong(up.get("aid").toString());
				double balance = Double.parseDouble(up.get("balance").toString());
				double param8 = Double.parseDouble(up.get("param8").toString());
				double param9 = Double.parseDouble(up.get("param9").toString());
				double balance_zq = AutoInvestUtils.doubleToQian(balance); 
				long daymin = Long.parseLong(up.get("daymin").toString());
				long daymax = Long.parseLong(up.get("daymax").toString());
				long monthmin = Long.parseLong(up.get("monthmin").toString());
				long monthmax = Long.parseLong(up.get("monthmax").toString()); 
				double levelscore = Double.parseDouble(up.get("levelscore").toString());
				long lasttime = Long.parseLong(up.get("lasttime").toString()); 
		 
				draw=new Draw(userid, username, mid, aid,balance, 1000d, balance_zq,0,lasttime,levelscore);
				draw.setBalance_zq(balance_zq);
				//计算最小最大值开始
				if(BaseTool.isNotNull(preParams.getFid())){
					investVO=userPriorityService.getMaxMin(username, preParams.getFid(), "T");
					draw.setMin(investVO.getMinMoney());
					draw.setMax(investVO.getMaxMoney());
				}else{//无融资项目号
					draw.setMin(1000d);
					if(balance_zq>=1000d){
						draw.setMax(balance_zq);
					}else{
						draw.setMax(0);
						draw.setMin(0);
					}
					
				}

				
				draw.setParam1(up.get("param1").toString());
				draw.setParam2(up.get("param2").toString()); 
				draw.setDayMin(daymin);
				draw.setDayMax(daymax);
				draw.setMonthMin(monthmin);
				draw.setMonthMax(monthmax); 
				draw.setParam5(up.get("param5").toString());
				draw.setDayMin(Long.parseLong(up.get("daymin").toString()));
				draw.setDayMax(Long.parseLong(up.get("daymax").toString()));
				draw.setMonthMin(Long.parseLong(up.get("monthmin").toString()));
				draw.setMonthMin(Long.parseLong(up.get("monthmin").toString()));
				draw.setParam6(up.get("param6").toString());
				draw.setParam7(up.get("param7").toString());
				draw.setParam8(Double.parseDouble(up.get("param8").toString()));
				draw.setParam9(Double.parseDouble(up.get("param9").toString()));
				draw.setLastTime(lasttime);
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				draw.setLast(sdf.parse(lasttime+""));
				draw.setPrePrice(draw.getMax());
				if(param9!=0){//单笔的上限 
					if(param9>=draw.getMax()){
						draw.setPrePrice(draw.getMax());
					}else{
						draw.setPrePrice(param9); 
						draw.setMax(param9);
					} 
				}
				if(null!=fb){//根据融资项目筛选
					if(param9!=0){//param9单笔的上限  
						if(draw.getMin()<=param9){
							//剩余金额
							Double chaDouble=balance-param8;
							double chaDouble_zq = 0;
							if(chaDouble<1000){
								chaDouble_zq = 0;
							}else{
								chaDouble_zq = AutoInvestUtils.doubleToQian(chaDouble);
							}
							if(chaDouble_zq>=draw.getMin()&&chaDouble_zq<=draw.getMax()&&draw.getMax()>0){//控制---->param8账户可用余额不低于多少元 
								draw.setPrePrice(chaDouble_zq);
								drs.add(draw); 
								sum_balance += balance;//委托人可用余额总额
								sum_balance_zq += AutoInvestUtils.doubleToQian(balance);//委托人可用余额(整千)总额
								w += draw.getPrePrice();//委托金额
								n++;  				
							}else if(chaDouble_zq>=draw.getMin()&&chaDouble_zq>draw.getMax()&&draw.getMax()>0){//控制---->param8账户可用余额不低于多少元 
								draw.setPrePrice(draw.getMax());
								drs.add(draw); 
								sum_balance += balance;//委托人可用余额总额
								sum_balance_zq += AutoInvestUtils.doubleToQian(balance);//委托人可用余额(整千)总额
								w += draw.getPrePrice();//委托金额
								n++;  				
							}
							
						}
					}else{//param9=0未设置上限 
						Double chaDouble=balance-param8;
						double chaDouble_zq = 0;
						if(chaDouble<1000){
							chaDouble_zq = 0;
						}else{
							chaDouble_zq = AutoInvestUtils.doubleToQian(chaDouble);
						}
						if(chaDouble_zq>=draw.getMin()&&chaDouble_zq<=draw.getMax()&&draw.getMax()>0){//控制---->param8账户可用余额不低于多少元 
							draw.setPrePrice(chaDouble_zq);
							drs.add(draw); 
							sum_balance += balance;//委托人可用余额总额
							sum_balance_zq += AutoInvestUtils.doubleToQian(balance);//委托人可用余额(整千)总额
							w += draw.getPrePrice();//委托金额
							n++;  				
						}else if(chaDouble_zq>=draw.getMin()&&chaDouble_zq>draw.getMax()&&draw.getMax()>0){//控制---->param8账户可用余额不低于多少元 
							draw.setPrePrice(draw.getMax());
							drs.add(draw); 
							sum_balance += balance;//委托人可用余额总额
							sum_balance_zq += AutoInvestUtils.doubleToQian(balance);//委托人可用余额(整千)总额
							w += draw.getPrePrice();//委托金额
							n++;  				
						} 
					} 
		         //System.out.println(username+"--->min="+draw.getMin()+"--->max="+draw.getMax()+"--->当个包上限param9="+param9+"--->可用余额不低于param8="+param8+"--->prePrice="+draw.getPrePrice()+"--->balance="+balance);
				}else{//自动投标委托人查询
					drs.add(draw); 
					sum_balance += balance;//委托人可用余额总额
					sum_balance_zq += AutoInvestUtils.doubleToQian(balance);//委托人可用余额(整千)总额
					w += draw.getPrePrice();//委托金额
					n++;  		
				}
					
	 				
			}
			Map<String,Object> map = new HashMap<String,Object>();
			DecimalFormat df = new DecimalFormat("#0.00");
			map.put("n",n); //符合条件的委托人人数
			map.put("w", df.format(w));//委托金额
			map.put("sum_balance", df.format(sum_balance));//委托人可用余额总额
			map.put("sum_balance_zq", df.format(sum_balance_zq));//委托人可用余额(整千)总额
			Collections.sort(drs, new DrawLevelScoreComparator()); 
			map.put("draws", drs); //符合条件的委托人
			if(null!=fb){
				map.put("hkfs", this.userPriorityService.ConverterParam2(fb.getBusinessType())[1]);//还款方式
				map.put("fxpj", AutoInvestUtils.ConverterParam1(fb.getZhzs()));//风险评级
				map.put("dbfs", AutoInvestUtils.ConverterParam5(fb.getFxbzState()));//担保方式
				
				if(fb.getInterestDay()==0){
					map.put("qx", fb.getBusinessType().getTerm()+"个月");
					map.put("nll", fb.getRate());//年利率
				}else{
					map.put("qx", fb.getInterestDay()+"天");
					map.put("nll", fb.getRate());//年利率
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	public void insertAutoParam(MemberBase member,UserParameter userParameter) throws EngineException{
		String username=member.getUser().getUsername();
		
		List<UserParameter> ups=this.getScrollDataCommon("from UserParameter o where o.member.id=? and nextFlag='1' ", new String[]{member.getId()});
		for(UserParameter up:ups){
			up.setNextFlag("0");
			this.update(up);
		}
		
		UserPriority userPriority=userPriorityService.selectById(username);
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
		userParameter.setMember(member);
		userParameter.setNextFlag("1");
		userParameter.setCreateTime(Long.valueOf(sf.format(new Date())));
		this.insert(userParameter);
		if(null==userPriority){
			userPriority=new UserPriority(username,null,Long.valueOf(sf.format(new Date())), 0l); 
			userPriorityService.insert(userPriority);
		} 
	}
	@Transactional
	public void autoParamOpen(){
		try {
			//以每日0:00为界，下一个交易日生效
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
			long time=Long.valueOf(sf.format(new Date())+"000000");
			//1、处理新设置了参数的用户
			List<UserParameter> ups=this.getScrollDataCommon("from UserParameter o where  o.nextFlag='1' and o.createTime<"+time, new String[]{});//从新设置了参数
			sf=new SimpleDateFormat("yyyyMMddHHmmss");
			for(UserParameter up:ups){
				UserPriority userPriority=userPriorityService.selectById(up.getMember().getUser().getUsername());
				if("1".equals(userPriority.getStopFlag())){//用户 '提交了解除协议申请' 和  '设置参数'
				
					if(up.getCreateTime()<userPriority.getStopTime()) {//先设置参数，后又解除协议，生效的是解除
						//如果用户的协议解除时间大于 待生效参数的生成时间，说明用户在设置参数后，又点了解约。
						//直接解约就好
						userPriority.setUserParameter(null);
						userPriority.setStopFlag("0");  
					} else {
						//先解除协议，后设置参数，生效的是参数
						//如果用户的待生效参数的生成时间，大于解约时间，则表示用户先点了解约，又设置了参数。
						//忽视解约动作。
						userPriority.setUserParameter(up);
						userPriority.setStopFlag("0");
					}			
				}else{//仅仅设置了参数，生效的是参数
					userPriority.setUserParameter(up);
				} 
				userPriority.setLastTime(Long.valueOf(sf.format(new Date())));
				this.userPriorityService.update(userPriority);  
				up.setNextFlag("0");
				up.setOkTime(Long.valueOf(sf.format(new Date())));//生效时间
				up.setUserPriorityId(userPriority.getUsername());//生效的才有值
				this.update(up);
			}
			
      //2、处理仅仅解除了协议的用户
      List<UserPriority> prioritys=this.userPriorityService.getScrollDataCommon("from UserPriority o where  o.stopFlag='1'  and o.stopTime<"+time, new String[]{});//从新设置了参数
      for(UserPriority priority:prioritys){
			    priority.setUserParameter(null);
				priority.setLastTime(Long.valueOf(sf.format(new Date()))); 
				priority.setStopFlag("0");  
				this.userPriorityService.update(priority);  
        }
		} catch (Exception e) { 
			e.printStackTrace();
		}
       
	}
	
	@Transactional
	public void stopAutoParam(String username) throws EngineException{
		UserPriority userPriority=userPriorityService.selectById(username);
		if(null==userPriority){
			 new Exception();
		}
		
		UserParameter up=userPriority.getUserParameter();
		if(null!=up){
			up.setNextFlag("0");
			this.update(up);
		}  
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
		userPriority.setStopTime(Long.valueOf(sf.format(new Date())));
		userPriority.setStopFlag("1");//已经发出停用申请
		userPriorityService.update(userPriority);  
	}
}
