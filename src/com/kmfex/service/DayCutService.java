package com.kmfex.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.kmfex.model.DayCut;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.BaseService;
/**
 * @author linuxp
 * */
public interface DayCutService extends BaseService<DayCut> {
	
	//效验某个人的日切数据是否存在，即创建还是更新。true表示已经有记录存在:更新即可。false表示无记录存在:需要创建新记录
	public boolean hasDayCut(User u,int year,int month);
	
	//取得某用户的年月日切记录
	public DayCut getDayCutForUser(User u,int year,int month);
	
	//取个日期的所有日切数据，比如：2013-02-07
	public List<DayCut> getDayCut(int year,int month,int day);
	
	//创建某用户的年月日切记录
	public DayCut createDayCutForUser(User u,int year,int month);
	
	//创建日切数据
	public void createDayCut(List<User> users,int year,int month,int day);
	
	public void updateDayCutForUser(User u,DayCut dc,int day);
	
	public ArrayList<LinkedHashMap<String,Object>> sum_one(Date start,Date end,String userType,String signType,double rate,String channel);
	public ArrayList<LinkedHashMap<String,Object>> sum_more(List<Date[]> ds,String userType,String signType,List<Double> rs,String channel);
}
