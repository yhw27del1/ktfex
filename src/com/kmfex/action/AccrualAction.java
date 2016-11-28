package com.kmfex.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.AccrualVO;
import com.kmfex.AccrualVO2;
import com.kmfex.SumMore;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.MainAccrual;
import com.kmfex.model.SubAccrual;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.DayCutService;
import com.kmfex.service.MainAccrualService;
import com.kmfex.service.SubAccrualService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.struts.BaseAction;

/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class AccrualAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4288690048276875161L;
	private int interval = 1;
	private String id;
	private MainAccrual main;
	private SubAccrual sub;
	@Resource
	private UserService userService;
	@Resource
	private MainAccrualService mainAccrualService;
	@Resource
	private SubAccrualService subAccrualService;
	@Resource
	private DayCutService dayCutService;
	@Resource
	private AccountDealService accountDealService;
	
	private String userType;
	private String signType;
	private String channel;
	public String ui(){
		
		return "ui";
	}
	
	public String calc(){
		int iv = this.main.getInterval();
		if(null!=this.main){
			if(iv>0){
				Date start = this.main.getMain_start();
				Date end = this.main.getMain_end();
				double rate = this.main.getMain_rate();
				if(iv==1){//间隔为1的情形
					ArrayList<LinkedHashMap<String,Object>> result = this.dayCutService.sum_one(start,end,this.userType,this.signType,rate,this.channel);
					ServletActionContext.getRequest().setAttribute("detail",result);
				}else{//间隔大于1的情形，间隔最大为3
					if(iv==2||iv==3){
						List<Date[]> all_date = new ArrayList<Date[]>();
						Date[] d1 = new Date[]{this.main.getSub_start_1(),this.main.getSub_end_1()};
						Date[] d2 = new Date[]{this.main.getSub_start_2(),this.main.getSub_end_2()};
						Date[] d3 = new Date[]{this.main.getSub_start_3(),this.main.getSub_end_3()};
						all_date.add(d1);
						all_date.add(d2);
						all_date.add(d3);
						List<Double> all_rate = new ArrayList<Double>();
						all_rate.add(this.main.getSub_rate_1());
						all_rate.add(this.main.getSub_rate_2());
						all_rate.add(this.main.getSub_rate_3());
						List<Date[]> ds = new ArrayList<Date[]>();
						List<Double> rs = new ArrayList<Double>();
						for(int i=1;i<=iv;i++){//有效的子期间时间段和对应的利率
				        	ds.add(all_date.get(i-1));
				        	rs.add(all_rate.get(i-1));
						}
						ArrayList<LinkedHashMap<String,Object>> result = this.dayCutService.sum_more(ds, userType, signType, rs,this.channel);
						List<SumMore> mores = new ArrayList<SumMore>();
						double sums = 0.0;
						double lxs = 0.0;
						int counts = 0;
						for(LinkedHashMap<String,Object> o:result){
							String[] sub_sum = o.get("sums").toString().split("@");
							String[] sub_lx = o.get("lxs").toString().split("@");
							//这两个数组的长度肯定是一致的
							int size = sub_lx.length;
							SumMore m = new SumMore();
							m.setUsername(o.get("username").toString());
							m.setRealname(o.get("realname").toString());
							m.setSum(Double.parseDouble(o.get("sum").toString()));
							m.setLx(Double.parseDouble(o.get("lx").toString()));
							m.setR1(this.main.getSub_rate_1()+"");
							m.setR2(this.main.getSub_rate_2()+"");
							m.setR3(this.main.getSub_rate_3()+"");
							sums += m.getSum();
							lxs += m.getLx();
							counts += 1;
							if(iv==2){//利率标准数为2,则size的值可能为1或2
								if(size==1){
									m.setSum1(0.0);
									m.setSum2(Double.parseDouble(sub_sum[0]));
									
									m.setLx1(0.0);
									m.setLx2(Double.parseDouble(sub_lx[0]));
								}
								if(size==2){
									m.setSum1(Double.parseDouble(sub_sum[0]));
									m.setSum2(Double.parseDouble(sub_sum[1]));
									
									m.setLx1(Double.parseDouble(sub_lx[0]));
									m.setLx2(Double.parseDouble(sub_lx[1]));
								}
							}
							if(iv==3){//利率标准数为3,则size的值可能为1或2或3
								if(size==1){
									m.setSum1(0.0);
									m.setSum2(0.0);
									m.setSum3(Double.parseDouble(sub_sum[0]));
									
									m.setLx1(0.0);
									m.setLx2(0.0);
									m.setLx3(Double.parseDouble(sub_lx[0]));
								}
								if(size==2){
									m.setSum1(0.0);
									m.setSum2(Double.parseDouble(sub_sum[0]));
									m.setSum3(Double.parseDouble(sub_sum[1]));
									
									m.setLx1(0.0);
									m.setLx2(Double.parseDouble(sub_lx[0]));
									m.setLx3(Double.parseDouble(sub_lx[1]));
								}
								if(size==3){
									m.setSum1(Double.parseDouble(sub_sum[0]));
									m.setSum2(Double.parseDouble(sub_sum[1]));
									m.setSum3(Double.parseDouble(sub_sum[2]));
									
									m.setLx1(Double.parseDouble(sub_lx[0]));
									m.setLx2(Double.parseDouble(sub_lx[1]));
									m.setLx3(Double.parseDouble(sub_lx[2]));
								}
							}
							mores.add(m);
						}
						ServletActionContext.getRequest().setAttribute("detail",mores);
						ServletActionContext.getRequest().setAttribute("sums",DoubleUtils.doubleCheck2(sums, 2));
						ServletActionContext.getRequest().setAttribute("lxs",DoubleUtils.doubleCheck2(lxs, 2));
						ServletActionContext.getRequest().setAttribute("counts",counts);
					}
				}
			}else{
				System.out.println("年利率数不能小于等于0");
			}
		}else{
			System.out.println("参数错误");
		}
		if(iv==2){
			return "calc2";
		}else if(iv==3){
			return "calc3";
		}else{
			return "calc";
		}
	}
	
	public String calc_ex(){
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		ServletActionContext.getRequest().setAttribute("msg", f.format(new Date()));
		int iv = this.main.getInterval();
		if(null!=this.main){
			if(iv>0){
				Date start = this.main.getMain_start();
				Date end = this.main.getMain_end();
				double rate = this.main.getMain_rate();
				if(iv==1){//间隔为1的情形
					ArrayList<LinkedHashMap<String,Object>> result = this.dayCutService.sum_one(start,end,this.userType,this.signType,rate,this.channel);
					ServletActionContext.getRequest().setAttribute("detail",result);
				}else{//间隔大于1的情形，间隔最大为3
					if(iv==2||iv==3){
						List<Date[]> all_date = new ArrayList<Date[]>();
						Date[] d1 = new Date[]{this.main.getSub_start_1(),this.main.getSub_end_1()};
						Date[] d2 = new Date[]{this.main.getSub_start_2(),this.main.getSub_end_2()};
						Date[] d3 = new Date[]{this.main.getSub_start_3(),this.main.getSub_end_3()};
						all_date.add(d1);
						all_date.add(d2);
						all_date.add(d3);
						List<Double> all_rate = new ArrayList<Double>();
						all_rate.add(this.main.getSub_rate_1());
						all_rate.add(this.main.getSub_rate_2());
						all_rate.add(this.main.getSub_rate_3());
						List<Date[]> ds = new ArrayList<Date[]>();
						List<Double> rs = new ArrayList<Double>();
						for(int i=1;i<=iv;i++){//有效的子期间时间段和对应的利率
				        	ds.add(all_date.get(i-1));
				        	rs.add(all_rate.get(i-1));
						}
						ArrayList<LinkedHashMap<String,Object>> result = this.dayCutService.sum_more(ds, userType, signType, rs,this.channel);
						List<SumMore> mores = new ArrayList<SumMore>();
						double sums = 0.0;
						double lxs = 0.0;
						int counts = 0;
						for(LinkedHashMap<String,Object> o:result){
							String[] sub_sum = o.get("sums").toString().split("@");
							String[] sub_lx = o.get("lxs").toString().split("@");
							//这两个数组的长度肯定是一致的
							int size = sub_lx.length;
							SumMore m = new SumMore();
							m.setUsername(o.get("username").toString());
							m.setRealname(o.get("realname").toString());
							m.setSum(Double.parseDouble(o.get("sum").toString()));
							m.setLx(Double.parseDouble(o.get("lx").toString()));
							m.setR1(this.main.getSub_rate_1()+"");
							m.setR2(this.main.getSub_rate_2()+"");
							m.setR3(this.main.getSub_rate_3()+"");
							sums += m.getSum();
							lxs += m.getLx();
							counts += 1;
							if(iv==2){//利率标准数为2,则size的值可能为1或2
								if(size==1){
									m.setSum1(0.0);
									m.setSum2(Double.parseDouble(sub_sum[0]));
									
									m.setLx1(0.0);
									m.setLx2(Double.parseDouble(sub_lx[0]));
								}
								if(size==2){
									m.setSum1(Double.parseDouble(sub_sum[0]));
									m.setSum2(Double.parseDouble(sub_sum[1]));
									
									m.setLx1(Double.parseDouble(sub_lx[0]));
									m.setLx2(Double.parseDouble(sub_lx[1]));
								}
							}
							if(iv==3){//利率标准数为3,则size的值可能为1或2或3
								if(size==1){
									m.setSum1(0.0);
									m.setSum2(0.0);
									m.setSum3(Double.parseDouble(sub_sum[0]));
									
									m.setLx1(0.0);
									m.setLx2(0.0);
									m.setLx3(Double.parseDouble(sub_lx[0]));
								}
								if(size==2){
									m.setSum1(0.0);
									m.setSum2(Double.parseDouble(sub_sum[0]));
									m.setSum3(Double.parseDouble(sub_sum[1]));
									
									m.setLx1(0.0);
									m.setLx2(Double.parseDouble(sub_lx[0]));
									m.setLx3(Double.parseDouble(sub_lx[1]));
								}
								if(size==3){
									m.setSum1(Double.parseDouble(sub_sum[0]));
									m.setSum2(Double.parseDouble(sub_sum[1]));
									m.setSum3(Double.parseDouble(sub_sum[2]));
									
									m.setLx1(Double.parseDouble(sub_lx[0]));
									m.setLx2(Double.parseDouble(sub_lx[1]));
									m.setLx3(Double.parseDouble(sub_lx[2]));
								}
							}
							mores.add(m);
						}
						ServletActionContext.getRequest().setAttribute("detail",mores);
						ServletActionContext.getRequest().setAttribute("sums",DoubleUtils.doubleCheck2(sums, 2));
						ServletActionContext.getRequest().setAttribute("lxs",DoubleUtils.doubleCheck2(lxs, 2));
						ServletActionContext.getRequest().setAttribute("counts",counts);
					}
				}
			}else{
				System.out.println("年利率数不能小于等于0");
			}
		}else{
			System.out.println("参数错误");
		}
		if(iv==2){
			return "calc2_ex";
		}else if(iv==3){
			return "calc3_ex";
		}else{
			return "calc_ex";
		}
	}
	private AccrualVO vo;
	
	private AccrualVO2 vo2;
	
	//年利率数为1时，预发利息
	public String add(){
		String msg = "";
		if(null==this.vo){
			msg = "未查询到余额汇总数据";
		}else{
			String[] username_group = vo.getUsername().split(",");
			String[] sum_group = vo.getSum().split(",");
			String[] lx_group = vo.getLx().split(",");
			//这3个数组的长度一致，一一对应。
			int size = lx_group.length;
			if(size>0){
				MainAccrual m = new MainAccrual();
				m.setMemo(vo.getMemo());
				m.setInterval(vo.getInterval());
				m.setMain_sum(vo.getSum_all());
				m.setMain_lx(vo.getSum_lx());
				m.setMain_count(vo.getCount());
				m.setMain_rate(vo.getRate());
				m.setMain_start(vo.getStart());
				m.setMain_end(vo.getEnd());
				try {
					User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					m.setOperator(u.getId());
					this.mainAccrualService.insert(m);
					for(int i=0;i<size;i++){
						SubAccrual sub = new SubAccrual();
						sub.setMain_id(m.getId());
						sub.setMain_lx(m.getMain_lx());
						sub.setMain_sum(m.getMain_sum());
						sub.setMain_rate(m.getMain_rate());
						sub.setUsername(username_group[i].trim());
						User aim = this.userService.findUser(username_group[i].trim());
						sub.setUser_id(aim.getId());
						sub.setSub_sum(Double.parseDouble(sum_group[i].trim()));
						sub.setSub_lx(Double.parseDouble(lx_group[i].trim()));
						sub.setMemo(m.getMemo());
						sub.setOperator(m.getOperator());
						this.subAccrualService.insert(sub);
						AccountDeal ad = this.accountDealService.accountDealRecord(AccountDeal.HQLX, "36",Double.parseDouble(lx_group[i].trim()));
						ad.setBusinessFlag(23);
						ad.setUser(u);
						ad.setAccount(aim.getUserAccount());
						ad.setPreMoney(aim.getUserAccount().getBalance()+aim.getUserAccount().getFrozenAmount());
						ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
						ad.setMemo(m.getMemo());
						this.accountDealService.insert(ad);
					}
					DecimalFormat df = new DecimalFormat("#.##");
					msg = "预发利息成功，请联系相关部门进行审核，共："+m.getMain_count()+"人次，余额汇总："+df.format(m.getMain_sum())+"元"+"，利息汇总："+df.format(m.getMain_lx())+"元";
				} catch (EngineException e) {
					e.printStackTrace();
					msg = "预发利息入库异常，请联系管理员";
				}
			}else{
				msg = "提交数据错误，请联系管理员";
			}
		}
		ServletActionContext.getRequest().setAttribute("msg",msg);
		return "result";
	}
	
	//年利率数为2或3时，预发利息
	public String add2(){
		String msg = "";
		if(null==this.vo2){
			msg = "未查询到余额汇总数据";
		}else{
			String[] username_group = vo2.getUsername().split(",");
			
			String[] sum_group = vo2.getSum().split(",");
			String[] sum1 = vo2.getSum1().split(",");
			String[] sum2 = vo2.getSum2().split(",");
			String[] sum3 = vo2.getSum3().split(",");
			
			String[] lx_group = vo2.getLx().split(",");
			String[] lx1 = vo2.getLx1().split(",");
			String[] lx2 = vo2.getLx2().split(",");
			String[] lx3 = vo2.getLx3().split(",");
			//这9个数组的长度一致，一一对应。
			
			int size = lx_group.length;
			if(size>0){
				MainAccrual m = new MainAccrual();
				m.setMemo(vo2.getMemo());
				m.setInterval(vo2.getInterval());
				m.setMain_sum(vo2.getSum_all());
				m.setMain_lx(vo2.getSum_lx());
				m.setMain_count(vo2.getCount());
				m.setMain_start(vo2.getStart());
				m.setMain_end(vo2.getEnd());
				
				m.setSub_rate_1(vo2.getRate1());
				m.setSub_rate_2(vo2.getRate2());
				m.setSub_rate_3(vo2.getRate3());
				
				m.setSub_start_1(vo2.getStart1());
				m.setSub_end_1(vo2.getEnd1());
				m.setSub_start_2(vo2.getStart2());
				m.setSub_end_2(vo2.getEnd2());
				m.setSub_start_3(vo2.getStart3());
				m.setSub_end_3(vo2.getEnd3());
				try {
					User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					m.setOperator(u.getId());
					this.mainAccrualService.insert(m);
					for(int i=0;i<size;i++){
						SubAccrual sub = new SubAccrual();
						sub.setMain_id(m.getId());
						sub.setMain_lx(m.getMain_lx());
						sub.setMain_sum(m.getMain_sum());
						sub.setUsername(username_group[i].trim());
						User aim = this.userService.findUser(username_group[i].trim());
						sub.setUser_id(aim.getId());
						sub.setSub_sum(Double.parseDouble(sum_group[i].trim()));
						sub.setSub_lx(Double.parseDouble(lx_group[i].trim()));
						sub.setMemo(m.getMemo());
						sub.setOperator(m.getOperator());
						sub.setSub_sum_1(Double.parseDouble(sum1[i]));
						sub.setSub_sum_2(Double.parseDouble(sum2[i]));
						sub.setSub_sum_3(Double.parseDouble(sum3[i]));
						sub.setSub_lx_1(Double.parseDouble(lx1[i]));
						sub.setSub_lx_2(Double.parseDouble(lx2[i]));
						sub.setSub_lx_3(Double.parseDouble(lx3[i]));
						this.subAccrualService.insert(sub);
						AccountDeal ad = this.accountDealService.accountDealRecord(AccountDeal.HQLX, "36",Double.parseDouble(lx_group[i].trim()));
						ad.setBusinessFlag(23);
						ad.setUser(u);
						ad.setAccount(aim.getUserAccount());
						ad.setPreMoney(aim.getUserAccount().getBalance()+aim.getUserAccount().getFrozenAmount());
						ad.setNextMoney(ad.getPreMoney()+ad.getMoney());
						ad.setMemo(m.getMemo());
						this.accountDealService.insert(ad);
					}
					DecimalFormat df = new DecimalFormat("#.##");
					msg = "预发利息成功，请联系相关部门进行审核，共："+m.getMain_count()+"人次，余额汇总："+df.format(m.getMain_sum())+"元"+"，利息汇总："+df.format(m.getMain_lx())+"元";
				} catch (EngineException e) {
					e.printStackTrace();
					msg = "预发利息入库异常，请联系管理员";
				}
			}else{
				msg = "提交数据错误，请联系管理员";
			}
		}
		ServletActionContext.getRequest().setAttribute("msg",msg);
		return "result";
	}
	
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MainAccrual getMain() {
		return main;
	}
	public void setMain(MainAccrual main) {
		this.main = main;
	}
	public SubAccrual getSub() {
		return sub;
	}
	public void setSub(SubAccrual sub) {
		this.sub = sub;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSignType() {
		return signType;
	}

	public void setVo(AccrualVO vo) {
		this.vo = vo;
	}

	public AccrualVO getVo() {
		return vo;
	}

	public void setVo2(AccrualVO2 vo2) {
		this.vo2 = vo2;
	}

	public AccrualVO2 getVo2() {
		return vo2;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}
}
