package com.kmfex.statistics.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.statistics.service.StatisticsAuthorityService;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
 
@Service
@Transactional
public class StatisticsAuthorityImpl extends BaseServiceImpl<Org> implements StatisticsAuthorityService {
	
	@Transactional
	public List<Map<String,Object>> getMapList_Jyl(int year,String queryByOrgCode)  {  
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
	    try {
			StringBuilder fields = new StringBuilder(); 
			fields.append("investor_orgno,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-01', investamount, 0)) as m1,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-02', investamount, 0)) as m2,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-03', investamount, 0)) as m3,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-04', investamount, 0)) as m4,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-05', investamount, 0)) as m5,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-06', investamount, 0)) as m6,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-07', investamount, 0)) as m7,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-08', investamount, 0)) as m8,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-09', investamount, 0)) as m9,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-10', investamount, 0)) as m10,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-11', investamount, 0)) as m11,");
			fields.append("SUM(DECODE(TO_CHAR(qianyuedate,'YYYY-MM'), '"+year+"-12', investamount, 0)) as m12  ");
			 
			 StringBuilder sb = new StringBuilder();  
			 sb.append(" investor_orgno='"+queryByOrgCode+"' ");
			 sb.append(" and TO_CHAR(qianyuedate,'YYYY')='"+year+"' ");
			 sb.append(" and state='2' ");  
			 sb.append(" GROUP BY investor_orgno"); 
		      list = this.queryForList(fields.toString(), "V_INVESTRECORD", sb.toString());
	        } catch (Exception e) {
		     e.printStackTrace();
		   }
	   return list;
	}

	@Transactional
	public List<Map<String, Object>> getMapList_forPie(Date startDate,
			Date endDate, String queryByOrgCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(); 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
//		System.out.println(startDate);
//		System.out.println(endDate);
		
		if(startDate == null){
			startDate = new Date();
		}
		if(endDate == null){
			endDate = new Date();
		}
		
		try {
				StringBuilder fields = new StringBuilder(); 
				fields.append(" v.cyr,count(*) as cyr_by_times ");
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				 StringBuilder sb = new StringBuilder(" 1=1 "); 
				 if(!"1".equals(queryByOrgCode) && !"530100".equals(queryByOrgCode))
				 sb.append(" and v.investor_orgno='"+queryByOrgCode+"' ");
				 
				 sb.append(" and  v.investdate between to_date('"+format.format(startDate)+"','YYYY-MM-DD') " +
							"and to_date('"+format.format(DateUtils.getAfter(endDate,1))+"','YYYY-MM-DD') ");
					
				 sb.append(" GROUP BY v.cyr"); 
			    list = this.queryForList(fields.toString(), "V_INVESTRECORD v", sb.toString());
		        } catch (Exception e) {
			     e.printStackTrace();
			   }
		return list;
	}

}
