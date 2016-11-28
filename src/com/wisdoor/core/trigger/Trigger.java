package com.wisdoor.core.trigger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import com.wisdoor.core.model.Transaction;
import com.wisdoor.core.service.TransactionService;

/**
 * 触发器   
 * 
 * @author eclipse
 * 
 */

public class Trigger implements ServletContextAware{
	private static final List<Transaction> transactions = new ArrayList<Transaction>();
	static Logger logger = Logger.getLogger(Trigger.class.getName());
	private ServletContext servletcontext;
	@Resource TransactionService transactionService;
	
	private static boolean init = false;

	public Trigger() throws Exception {
		
	}

	/**
	 * 执行的方法
	 */
	@SuppressWarnings("static-access")
	public void execution() throws Exception {
		try {
			synchronized (transactions) {
				if(!init){
					reload();
					init = true;
				}
				Calendar now = Calendar.getInstance();
				for (Transaction t : transactions) {
					if(t.isEnable()){
						String times = t.getTime();
						if (confirm(times, now)) {
							t.getTransactionbase().excute();
						}
					}else{
						transactions.remove(t);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean confirm(String times, Calendar now) {
		try {
			String[] time = times.split(" ");
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH)+1;
			int day = now.get(Calendar.DATE);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);
//			int second = now.get(Calendar.SECOND);
//			int WeekOfYear = now.get(Calendar.DAY_OF_WEEK);
			if (!"*".equals(time[0])) {// 年
				if (year != Integer.valueOf(time[0])) {
					return false;
				}
			}
			if (!"*".equals(time[1])) {// 月
				if (month != Integer.valueOf(time[1])) {
					return false;
				}
			}
			if (!"*".equals(time[2])) {// 日
				if (day != Integer.valueOf(time[2])) {
					return false;
				}
			}
			if (!"*".equals(time[3])) {// 时
				if (hour != Integer.valueOf(time[3])) {
					return false;
				}
			}
			if (!"*".equals(time[4])) {// 分
				if (minute != Integer.valueOf(time[4])) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void setServletContext(ServletContext servletcontext) {
		this.servletcontext = servletcontext;
		
	}
	
	@SuppressWarnings("static-access")
	public void reload(){
		try {
			synchronized (transactions) {
				// 加载列表，看是否有更新
				List<Transaction> transactions_temp = transactionService.getScrollData().getResultlist();
				for (Transaction t : transactions_temp) {
					String times = t.getTime();
					String entity = t.getEntity();
					if (times == null || "".equals(times) || !times.contains(" ") || entity == null || "".equals(entity)) continue;
					boolean exist = false;
					for (Transaction t_temp : this.transactions) {
						t_temp.setEnable(false);
						if (t_temp.getId().equals(t.getId())) {// 在transactions里，则exist为true
							if (!t_temp.getEntity().equals(t.getEntity())) {
								t_temp.setEntity(t.getEntity());
								Class<?> classtype = Class.forName(entity);
								Constructor<?>[] constructor = classtype.getConstructors();
								TransactionBase tb = (TransactionBase) constructor[0].newInstance();
								t_temp.setTransactionbase(tb);
							}
							if (!t_temp.getTime().equals(t.getTime())) {
								t_temp.setTime(t.getTime());
							}
							t_temp.setEnable(true);
							exist = true;
							
						} else {// bug修正，如果有更新，而且更新不在transactions里，不应该影响原transactions里的事物为false。
							t_temp.setEnable(true);
						}
					}
					if (!exist) {
						try {
							Class<?> classtype = Class.forName(entity);
							Constructor<?>[] constructor = classtype.getConstructors();
							TransactionBase tb = (TransactionBase) constructor[0].newInstance();
							tb.setServletContext(this.servletcontext);
							tb.init();
							t.setTransactionbase(tb);
							t.setEnable(true);
							this.transactions.add(t);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				transactions_temp = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
