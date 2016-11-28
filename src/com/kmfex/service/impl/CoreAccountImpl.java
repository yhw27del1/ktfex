package com.kmfex.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.model.CoreAccountLiveRecord;
import com.kmfex.service.CoreAccountService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.StringUtils;
@Service
public class CoreAccountImpl extends BaseServiceImpl<CoreAccountLiveRecord> implements CoreAccountService {
	@Resource AccountService accountService;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public QueryResult<Object> groupByCreatDate(final String wherejpql) throws Exception {
		QueryResult qr = new QueryResult<Object>();
		List<Object> resultlist=this.getHibernateTemplate().executeFind(new HibernateCallback()
		{
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException
			{
				String entityname = getGenericType(0).getSimpleName();
				String hql="select to_date(to_char(o.createtime,'yyyy-MM-dd'),'yyyy-MM-dd') as createDate,sum(o.abs_value) as money from "+entityname + " o "+(wherejpql==null || "".equals(wherejpql.trim())? "": "where "+ wherejpql);
				Query query = session.createQuery(hql);
				return (List<Object>) query.list();  
			}  
		});
		qr.setResultlist(resultlist);
		return qr;
	}

	@Override
	@Transactional
	public double clear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		double sum = 0d;
		try {  
			Map<String,Object> summap = this.queryForObject("sum(value) sum ", "t_coreaccount_liverecord", "calculated = 0");
			if(summap != null && summap.size() > 0 && null != summap.get("sum") && StringUtils.isNotBlank(summap.get("sum").toString())){
				sum = Double.parseDouble(summap.get("sum").toString());
				this.executeHql("update CoreAccountLiveRecord set calculated = true,calculat_time = to_date('"+sdf.format(now)+"','yyyy-mm-dd hh24:mi:ss') where calculated = false");
				
				Account ca = this.accountService.centerAccount();
				this.accountService.addMoney(ca, sum);
			}
		} catch (StaleObjectStateException e) {
			e.printStackTrace();
		} catch (EngineException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum;
	}
	
	
}
