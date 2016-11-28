package com.kmfex.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.DayCut;
import com.kmfex.service.DayCutService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
/**
 * @author linuxp
 * */
@Service
public class DayCutServiceImpl extends BaseServiceImpl<DayCut> implements DayCutService {
	
	@Resource
	AccountService accountService;

	@Override
	@Transactional
	public boolean hasDayCut(User u, int year, int month) {
		String sql = "from DayCut o where o.user.id="+u.getId()+" and o.year="+year+" and o.month="+month;
		DayCut c = this.selectByHql(sql);
		return null==c?false:true;
	}

	@Override
	@Transactional
	public DayCut getDayCutForUser(User u, int year, int month) {
		String sql = "from DayCut o where o.user.id="+u.getId()+" and o.year="+year+" and o.month="+month;
		DayCut c = this.selectByHql(sql);
		return c;
	}

	@Override
	@Transactional
	public List<DayCut> getDayCut(int year, int month, int day) {
		return null;
	}


	@Override
	@Transactional
	public DayCut createDayCutForUser(User u, int year, int month) {
		// TODO Auto-generated method stub
		DayCut dc = new DayCut();
		dc.setYear(year);
		dc.setMonth(month);
		dc.setUser(u);
		try {
			this.insert(dc);
			return dc;
		} catch (EngineException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public void createDayCut(List<User> users,int year, int month, int day) {
		for(User u:users){
			DayCut dc = null;
			if(!this.hasDayCut(u, year, month)){//不存在年月日切记录
				dc = this.createDayCutForUser(u, year, month);
			}else{//已经存在年月日切记录
				dc = this.getDayCutForUser(u, year, month);
			}
			dc = this.selectById(dc.getId());
			this.updateDayCutForUser(u,dc,day);
		}
	}

	@Override
	@Transactional
	public void updateDayCutForUser(User u,DayCut dc, int day) {
		//channel=1为招行资金，channel=2为工行专户，channel=0为招行签约的(按招行专户算)
		int c = u.getChannel();
		if(c==0){
			c = 1;
		}
		Account a = this.accountService.selectById(u.getUserAccount().getId());
		if(null!=a){
			switch (day) {
			case 1:
				dc.setBalance_1(a.getBalance());
				dc.setFrozen_1(a.getFrozenAmount());
				dc.setZ_1(c);
				break;
			case 2:
				dc.setBalance_2(a.getBalance());
				dc.setFrozen_2(a.getFrozenAmount());
				dc.setZ_2(c);
				break;
			case 3:
				dc.setBalance_3(a.getBalance());
				dc.setFrozen_3(a.getFrozenAmount());
				dc.setZ_3(c);
				break;
			case 4:
				dc.setBalance_4(a.getBalance());
				dc.setFrozen_4(a.getFrozenAmount());
				dc.setZ_4(c);
				break;
			case 5:
				dc.setBalance_5(a.getBalance());
				dc.setFrozen_5(a.getFrozenAmount());
				dc.setZ_5(c);
				break;
			case 6:
				dc.setBalance_6(a.getBalance());
				dc.setFrozen_6(a.getFrozenAmount());
				dc.setZ_6(c);
				break;
			case 7:
				dc.setBalance_7(a.getBalance());
				dc.setFrozen_7(a.getFrozenAmount());
				dc.setZ_7(c);
				break;
			case 8:
				dc.setBalance_8(a.getBalance());
				dc.setFrozen_8(a.getFrozenAmount());
				dc.setZ_8(c);
				break;
			case 9:
				dc.setBalance_9(a.getBalance());
				dc.setFrozen_9(a.getFrozenAmount());
				dc.setZ_9(c);
				break;
			case 10:
				dc.setBalance_10(a.getBalance());
				dc.setFrozen_10(a.getFrozenAmount());
				dc.setZ_10(c);
				break;
			case 11:
				dc.setBalance_11(a.getBalance());
				dc.setFrozen_11(a.getFrozenAmount());
				dc.setZ_11(c);
				break;
			case 12:
				dc.setBalance_12(a.getBalance());
				dc.setFrozen_12(a.getFrozenAmount());
				dc.setZ_12(c);
				break;
			case 13:
				dc.setBalance_13(a.getBalance());
				dc.setFrozen_13(a.getFrozenAmount());
				dc.setZ_13(c);
				break;
			case 14:
				dc.setBalance_14(a.getBalance());
				dc.setFrozen_14(a.getFrozenAmount());
				dc.setZ_14(c);
				break;
			case 15:
				dc.setBalance_15(a.getBalance());
				dc.setFrozen_15(a.getFrozenAmount());
				dc.setZ_15(c);
				break;
			case 16:
				dc.setBalance_16(a.getBalance());
				dc.setFrozen_16(a.getFrozenAmount());
				dc.setZ_16(c);
				break;
			case 17:
				dc.setBalance_17(a.getBalance());
				dc.setFrozen_17(a.getFrozenAmount());
				dc.setZ_17(c);
				break;
			case 18:
				dc.setBalance_18(a.getBalance());
				dc.setFrozen_18(a.getFrozenAmount());
				dc.setZ_18(c);
				break;
			case 19:
				dc.setBalance_19(a.getBalance());
				dc.setFrozen_19(a.getFrozenAmount());
				dc.setZ_19(c);
				break;
			case 20:
				dc.setBalance_20(a.getBalance());
				dc.setFrozen_20(a.getFrozenAmount());
				dc.setZ_20(c);
				break;
			case 21:
				dc.setBalance_21(a.getBalance());
				dc.setFrozen_21(a.getFrozenAmount());
				dc.setZ_21(c);
				break;
			case 22:
				dc.setBalance_22(a.getBalance());
				dc.setFrozen_22(a.getFrozenAmount());
				dc.setZ_22(c);
				break;
			case 23:
				dc.setBalance_23(a.getBalance());
				dc.setFrozen_23(a.getFrozenAmount());
				dc.setZ_23(c);
				break;
			case 24:
				dc.setBalance_24(a.getBalance());
				dc.setFrozen_24(a.getFrozenAmount());
				dc.setZ_24(c);
				break;
			case 25:
				dc.setBalance_25(a.getBalance());
				dc.setFrozen_25(a.getFrozenAmount());
				dc.setZ_25(c);
				break;
			case 26:
				dc.setBalance_26(a.getBalance());
				dc.setFrozen_26(a.getFrozenAmount());
				dc.setZ_26(c);
				break;
			case 27:
				dc.setBalance_27(a.getBalance());
				dc.setFrozen_27(a.getFrozenAmount());
				dc.setZ_27(c);
				break;
			case 28:
				dc.setBalance_28(a.getBalance());
				dc.setFrozen_28(a.getFrozenAmount());
				dc.setZ_27(c);
				break;
			case 29:
				dc.setBalance_29(a.getBalance());
				dc.setFrozen_29(a.getFrozenAmount());
				dc.setZ_29(c);
				break;
			case 30:
				dc.setBalance_30(a.getBalance());
				dc.setFrozen_30(a.getFrozenAmount());
				dc.setZ_30(c);
				break;
			case 31:
				dc.setBalance_31(a.getBalance());
				dc.setFrozen_31(a.getFrozenAmount());
				dc.setZ_31(c);
				break;
			default:
				break;
			}
			try {
				dc.setModifyDate(new Date());
				this.update(dc);
				if(a.isSys_qingsuan()){//已经做过清算，在日切时将其置为未清算，一边隔日使用。
					a.setSys_qingsuan(false);//已经做过清算的，则清楚清算标识。
					this.accountService.update(a);
				}
			} catch (EngineException e) {
			}
		}
	}
	
	//余额汇总及利息汇总的SQL拼接
	private String getSQL(Date start,Date end,String userType,String signType,double rate,String channel){
		int z = 0;
		if(null!=channel){
    		if("cmb".equals(channel)){//招行专户
    			z = 1;
    		}
    		if("icbc".equals(channel)){//工行专户
    			z = 2;
    		}
    	}
		String detail = null;
		List<String> finish = new ArrayList<String>();
		Map<Integer,Date[]>  split = new HashMap<Integer, Date[]>();
		if(DateUtils.commonYear(start, end)){//同年
			split = DateUtils.splitDate(start, end);
		}else{
			Map<Integer,Date[]>  year = DateUtils.splitDate_year(start, end);
	        Iterator<Entry<Integer, Date[]>> it = year.entrySet().iterator();
	        while(it.hasNext()){
	        	Entry<Integer, Date[]> next = it.next();
	        	Date[] one_two = next.getValue();
	        	Map<Integer,Date[]>  month = DateUtils.splitDate(one_two[0], one_two[1]);
	        	Iterator<Entry<Integer, Date[]>> it_m = month.entrySet().iterator();
	        	while(it_m.hasNext()){
	        		split.put(split.size(), it_m.next().getValue());
	        	}
	        }
		}
		if(null!=split&&split.size()>0){
			Iterator<Entry<Integer, Date[]>> it = split.entrySet().iterator();
	        while(it.hasNext()){//遍历每一个同年同月的日期段
	        	Entry<Integer, Date[]> next = it.next();
	        	Date[] one_two = next.getValue();
				Date month_start = one_two[0];
	        	Date month_end = one_two[1];
	        	int year = DateUtils.getYear(month_start);
	        	int month = DateUtils.getMouth(month_start);
	        	int day_start = DateUtils.getDay(month_start);
	        	int day_end = DateUtils.getDay(month_end);
	        	List<String> subs = new ArrayList<String>();
	        	String sql_tile = "";
	        	String sql_tile_union = "";
	        	String b = "";
	        	String f = "";
	        	String c = "";
	        	String sql_detail = "";
	        	for(;day_start<=day_end;day_start++){
	        		b = "d.balance_"+day_start;
	        		f = "d.frozen_"+day_start;
	        		c = "d.z_"+day_start;
	        		sql_tile = "(select d.username,d.realname,"+b+" as balance,"+f+" as frozen from v_daycut_user_account d where d.username != '5301' and d.year="+year+" and d.month="+month+" and "+c+"="+z+" and ("+b+">0 or "+f+">0)";
	        		if(null!=userType&&!"".equals(userType)&&!"all".equals(userType)){
	        			sql_tile += " and d.usertype='"+userType+"' ";
		        	}
		        	if(null!=signType&&!"".equals(signType)&&!"all".equals(signType)){
		        		sql_tile += " and d.signflag='"+signType+"' ";
		        	}
		        	sql_tile += " )";
	        		subs.add(sql_tile);
	        	}
	        	for(int i=0;i<subs.size();i++){
	        		if(i==0){
	        			sql_tile_union += "(";
	        		}
	        		sql_tile_union += subs.get(i);
	        		if(i!=(subs.size()-1)){
	        			sql_tile_union += " union all ";
	        		}else{
	        			sql_tile_union += ")";
	        		}
	        	}
	        	sql_detail = "(select tem.username,tem.realname,nvl(sum(tem.balance+tem.frozen),0) as sum from "+sql_tile_union+" tem group by tem.username,tem.realname)";
	        	finish.add(sql_detail);
	        }
	        if(finish.size()>0){
	        	int size = finish.size();
	        	String j = "select j.username,j.realname,nvl(sum(j.sum),0) as sum,round((("+rate+"/36000)*nvl(sum(j.sum),0)),2) as lx from (";
	        	for(int i=0;i<size;i++){
	        		j += finish.get(i);
	        		if(i!=(size-1)){
	        			j += " union all ";
	        		}
	        	}
	        	j += ") j group by j.username,j.realname order by j.username asc";
	        	detail = "select d.username,d.realname,d.sum,d.lx from ("+j+") d where d.lx>0 ";
	        }
		}
		return detail;
	}
	
	@Override
	public ArrayList<LinkedHashMap<String,Object>> sum_one(Date start,Date end,String userType,String signType,double rate,String channel){
		ArrayList<LinkedHashMap<String,Object>> r = null;
		String detail = getSQL(start,end,userType,signType,rate,channel);
		if(null!=detail&&!"".equals(detail)){
			try {
	    		r = this.selectListWithJDBC(detail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	@Override
	public ArrayList<LinkedHashMap<String, Object>> sum_more(List<Date[]> ds,String userType,String signType,List<Double> rs,String channel){
		boolean f = ds.size()==rs.size();//时间与利率的个数一致
		List<String> finish = new ArrayList<String>();
		ArrayList<LinkedHashMap<String,Object>> r = null;
		if(f){
			for(int i=0;i<ds.size();i++){
				Date s = ds.get(i)[0];
				Date e = ds.get(i)[1];
				double rate = rs.get(i);
				String v = getSQL(s,e,userType,signType,rate,channel);
				finish.add(v);
			}
			if(finish.size()>0){
	        	int size = finish.size();
	        	String j = "select j.username,j.realname,REPLACE(WMSYS.WM_CONCAT(j.sum), ',', '@') as sums,REPLACE(WMSYS.WM_CONCAT(j.lx), ',', '@') as lxs,sum(j.sum) as sum,sum(j.lx) as lx from (";
	        	for(int i=0;i<size;i++){
	        		j += "("+finish.get(i)+")";
	        		if(i!=(size-1)){
	        			j += " union all ";
	        		}
	        	}
	        	j += ") j group by j.username,j.realname order by j.username asc";
	        	try {
		    		r = this.selectListWithJDBC(j);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
		}
		return r;
	}
}
