package com.wisdoor.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 时间处理类
 * @author  
 *
 */ 
public class DateUtils {
    /**
	 * 描述：取得一日期后一定天数后的日期 
	 * 参数：Date date 开始日期 
	 *      Integer day 天数
	 * 返回值： Date    
	 */
	public static Date getAfter(Date date, Integer day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date d1 = null;
		String strDate = format.format(date);
		
		if (day==null){
			day = 0;
		}
		String afterDate = addDate(strDate, day);
		try {
			d1 = format.parse(afterDate);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return d1;
	}
	
	public static Date getAfterMonth(Date date, Integer month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date d1 = null;
		String strDate = format.format(date);
		
		if (month==null){
			month = 0;
		}
		String afterDate = addMonth(strDate, month);
		try {
			d1 = format.parse(afterDate);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return d1;
	}
	
	public static Date getAfterSeccond(Date date,int seccond) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		String strDate = format.format(date);
		
		String afterDate = addSeccond(strDate,seccond);
		try {
			d1 = format.parse(afterDate);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return d1;
	}
	
	/**
	 * 描述：取得一日期后一定天数后的日期 
	 * 参数：Date date 开始日期 
	 *      Integer day 天数
	 * 返回值： String    
	 */
	public static String addDate(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, x);
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}
	
	public static String addMonth(String month,int x){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = format.parse(month);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, x);
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}
	
	public static String addSeccond(String day,int seccond) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seccond);
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}
	
	/**
	 * 描述：取得一日期20111101  
	 * 返回值： String    
	 */
	public static String getNowDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date()); 
	}
	
	/**
	 * 描述：取得两位年
	 * 返回值： String    
	 */
	public static String getTwoYear() {
		return getNowDate().substring(2,4); 
	}
	/**
     * 获取两个日期之间的天数<br>
     * @param start
     * @param end
     * @return
     */
    public static int getBetween(Date start, Date end){ 
        long to = getDateNomal(end).getTime(); 
        long from = getDateNomal(start).getTime();
        long result_long = (to - from) / (1000 * 60 * 60 * 24);
        int result = (int)result_long;
        return result;
    }
    
	/**
	 *  得到时分秒为0的日期 
	 */
	public static Date getDateNomal(Date date){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);  
	    SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    String day_end_nextM=df.format(calendar.getTime());   
	    
	    Date theDate = null;
	    try {
	    	theDate = df.parse(day_end_nextM);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return theDate;
	}
	/**
	 *  得到相差的小时
	 */
	public  static long getHours(Date   dt_beg,   Date   dt_end){   
       //int   h=0;   
	   long l=dt_end.getTime()-dt_beg.getTime();
	   long day=l/(24*60*60*1000);
	   long hour=(l/(60*60*1000)-day*24);
	   //long min=((l/(60*1000))-day*24*60-hour*60);
	   //long s=(l/1000-day*24*60*60-hour*60*60-min*60);
       return (day*24)+hour;

    } 
	/**
     * 获取日期年和月份
     */
    public static String getYearMouth(Date dt) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        String str=""; 
        int year=cal.get(Calendar.YEAR);//得到年
        str+=""+year;
        int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
        if(month<=9){
        	str+="0"+month;
        }
        else {
        	str+=""+month;
        }
        	
        return str;
    }
	/**
     * 获取日期的年 
     */
    public static int getYear(Date dt) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt); 
        return cal.get(Calendar.YEAR);//得到年
    }
	/**
     * 获取日期的月份
     */
    public static int getMouth(Date dt) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt); 
        return cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1  
    }
	/**
     * 获取日期的日
     */
    public static int getDay(Date dt) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt); 
        return cal.get(Calendar.DATE);
    }
    
    //判断某个日期是否为周末
    public static boolean isWeekend(Date dt){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dt);
    	int DAY_OF_WEEK = cal.get(Calendar.DAY_OF_WEEK);
    	if(DAY_OF_WEEK==1||DAY_OF_WEEK==7){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**  
     * 验证某一时间是否在某一时间段内  
     * @param dt 某一时间  new Date();
     * @param weekend 为true则要求判断dt是否为周末，如果是周末则返回false，如果不是周末则继续检测时间段
     *             为false则不判断dt是否为周末，只检测时间段
     * @param timeSlot 某一时间段  new String[]{"9:00", "16:00"};
     * @return true/false,true表示给定时间在时间段内
     */  
    public static boolean isShift(Date dt, String[] timeSlot ,boolean weekend) {
    	if(weekend&&isWeekend(dt)){//flag为ture判断是否为周末，是周末则返回false
     		return false;
     	}
    	long currTime = dt.getTime();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(currTime);
        String[] tmpArray = timeSlot[0].split(":");
        long startTime, stopTime;
        tempCalendar.clear(Calendar.HOUR_OF_DAY);
        tempCalendar.clear(Calendar.MINUTE);
        tempCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArray[0]));
        tempCalendar.set(Calendar.MINUTE, Integer.parseInt(tmpArray[1]));
        startTime = tempCalendar.getTimeInMillis();
        tmpArray = timeSlot[1].split(":");
        int stopHour  = Integer.parseInt(tmpArray[0]), stopMinute = Integer.parseInt(tmpArray[1]);
        if (stopHour == 0) {//隔天处理
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        tempCalendar.clear(Calendar.HOUR_OF_DAY);
        tempCalendar.clear(Calendar.MINUTE);
        tempCalendar.set(Calendar.HOUR_OF_DAY, stopHour);
        tempCalendar.set(Calendar.MINUTE, stopMinute);
        stopTime = tempCalendar.getTimeInMillis();
        return ((startTime < currTime && currTime <= stopTime) ? true : false);
    }
    
    public static String formatDate(Date dt,String parten){
    	//yyyy-MM-dd
    	SimpleDateFormat df = new java.text.SimpleDateFormat(parten);
    	return df.format(dt);
    }
    
    public static Date parseDate(String datestr){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	try {
			return dateFormat.parse(datestr);
		} catch (ParseException e) {
			return null;
		}
    }
    
    public static Date parseDate(String datestr,String parten){
    	SimpleDateFormat dateFormat = new SimpleDateFormat(parten);
    	try {
			return dateFormat.parse(datestr);
		} catch (ParseException e) {
			return null;
		}
    }
    
    public static Calendar getCalendar(Date dt){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dt);
    	return cal;
    }
    
    //取月初
    public static Date getMonthStart(Date dt){
    	Calendar cal = getCalendar(dt);
    	//int y = cal.get(Calendar.YEAR);//年
    	//int m = cal.get(Calendar.MONTH)+1;//月
    	//int d = cal.get(Calendar.DAY_OF_MONTH);//日
    	int start = cal.getActualMinimum(Calendar.DAY_OF_MONTH);//第一日
    	cal.set(Calendar.DAY_OF_MONTH, start);
    	return cal.getTime();
    }
    
    //取月末
    public static Date getMonthEnd(Date dt){
    	Calendar cal = getCalendar(dt);
    	//int y = cal.get(Calendar.YEAR);//年
    	//int m = cal.get(Calendar.MONTH)+1;//月
    	//int d = cal.get(Calendar.DAY_OF_MONTH);//日
    	int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);//月份最后一日
    	cal.set(Calendar.DAY_OF_MONTH, end);
    	return cal.getTime();
    }
    
    //取年末
    public static Date getYearEnd(Date dt){
    	Calendar cal = getCalendar(dt);
    	//int y = cal.get(Calendar.YEAR);//年
    	//int m = cal.get(Calendar.MONTH)+1;//月
    	//int d = cal.get(Calendar.DAY_OF_MONTH);//日
    	int end = cal.getActualMaximum(Calendar.DAY_OF_YEAR);//年最后一日
    	cal.set(Calendar.DAY_OF_YEAR, end);
    	return cal.getTime();
    }
    
    //判断两个日期是否同年
    public static boolean commonYear(Date start,Date end){
    	int y_start = getYear(start);//年
    	int y_end = getYear(end);//年
    	if(y_start==y_end){//两个日期同年
    		return true;
    	}else{
    		return false;
    	}
    }
    
	//判断两个日期是否同月，不判断是否同年
    public static boolean commonMonth(Date start,Date end){
    	int m_start = getMouth(start);//月
    	int m_end = getMouth(end);//月
    	if(m_start==m_end){//两个日期同月，不判断是否同年
    		return true;
    	}else{
    		return false;
    	}
    }
    
    //判断两个同年的日期，跨越的月份数
    public static int overMonth(Date start,Date end){
    	if(commonYear(start, end)){//同年则继续
    		int m_start = getMouth(start);//月
        	int m_end = getMouth(end);//月
        	return m_end-m_start+1;
    	}else{//同月返回0
    		return 0;
    	}
    }
    
    //判断两个不同年的日期，跨越的年数
    public static int overYear(Date start,Date end){
    	if(!commonYear(start, end)){//不同年则继续
    		int y_start = getYear(start);//月
        	int y_end = getYear(end);//月
        	return y_end-y_start+1;
    	}else{//同年返回0
    		return 0;
    	}
    }
    
    //判断两个日期是否同年同月
    public static boolean commonYearAndMonth(Date start,Date end){
    	if(commonYear(start,end)&&commonMonth(start,end)){//同年且同月
        	return true;
    	}else{
    		return false;
    	}
    }
    
    
    //计算两个时间差(精确到毫秒) 
    public static String sjc(long start,long end){ 
          long between  = end - start;// 得到两者的毫秒数 
          if(between<0) return "";
          long day = between / (24 * 60 * 60 * 1000);
          long hour = (between / (60 * 60 * 1000) - day * 24);
          long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
          long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
          long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000- min * 60 * 1000 - s * 1000);
          //System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒");
          String str="";
          if(day>0)str=day + "天";
          if(hour>0)str+=hour + "小时";
          if(min>0)str+=min + "分";
          if(s>0)str+=s + "秒";
          if(ms>0)str+=ms + "毫秒";
          return str;
    }
    
	//对不同年的两个日期进行分割，分割成多段同年的日期
    public static Map<Integer,Date[]> splitDate_year(Date start,Date end){
    	Map<Integer,Date[]> main = new HashMap<Integer,Date[]>();
    	if(!commonYear(start, end)){//不同年才继续
			int over = overYear(start, end);//不同年，则over的值至少为2
			for(int i=0;i<over;i++){
				if(i==0){
					main.put(i, new Date[]{start, DateUtils.getYearEnd(start)});
				}else{
					Date[] d = main.get(i-1);
					if(null!=d&&d.length==2){
			        	Date s = DateUtils.getAfter(d[1], 1);
			        	Date e = null;
			        	if(commonYear(s, end)){
			        		e = end;
			        	}else{
			        		e = DateUtils.getYearEnd(s);
			        	}
			        	main.put(i, new Date[]{s,e});
					}
				}
			}
    		return main;
    	}else{//同年，返回null
    		return null;
    	}
    }
    
	//对同年不同月的两个日期进行分割，分割成多段同月的日期
    public static Map<Integer,Date[]> splitDate(Date start,Date end){
    	Map<Integer,Date[]> main = new HashMap<Integer,Date[]>();
    	if(commonYear(start, end)){//同年才继续
    		if(!commonMonth(start,end)){//不同月才继续
    			int over = overMonth(start, end);//不同月，则over的值至少为2
				for(int i=0;i<over;i++){
					if(i==0){
						main.put(i, new Date[]{start, DateUtils.getMonthEnd(start)});
					}else{
						Date[] d = main.get(i-1);
						if(null!=d&&d.length==2){
				        	Date s = DateUtils.getAfter(d[1], 1);
				        	Date e = null;
				        	if(commonMonth(s, end)){
				        		e = end;
				        	}else{
				        		e = DateUtils.getMonthEnd(s);
				        	}
				        	main.put(i, new Date[]{s,e});
						}
					}
				}
    		}else{
    			main.put(0, new Date[]{start, end});
    		}
    		return main;
    	}else{//不同年，返回null
    		return null;
    	}
    }
    
    /**  
     * 产生20位的交易流水
     * @return yyMMddHHmmssSSSxxxxx 
     */  
    public static String generateNo20(){
		//yyMMddHHmmssSSS：年月日时分秒毫秒，共15位
		//2位年，2位月，2位日，2位时，2位分，2位秒，3位毫秒
		SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date today = new Date();
		return (f.format(today)+(String.valueOf(Math.random()).substring(2))).substring(0, 20);
	}
    
    public static String[] timeSubsection = new String[]{"9:00", "16:00"};
    
    public static void main(String[] args) {
    	//SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date today = new Date();
		//System.out.println(df.format(getAfter(today, 1)));
		//System.out.println(df.format(getAfterMonth(today, 3)));
		//System.out.println(df.format(today));
		//System.out.println("seccond:"+df.format(getAfterSeccond(today, 3)));
		//System.out.println(isWeekend(new Date()));
        //System.out.println(isShift(new Date(), timeSubsection,true));
    	//Date d = parseDate("2013-02-08");
    	//System.out.println(formatDate(getMonthStart(d),"yyyy-MM-dd"));
        //System.out.println(formatDate(getMonthEnd(d),"yyyy-MM-dd"));
        
    	/*同年日期分割测试
        Date start = parseDate("2013-11-11");
        Date end = parseDate("2013-11-12");
        Map<Integer,Date[]>  m = splitDate(start, end);
        Iterator<Entry<Integer, Date[]>> it = m.entrySet().iterator();
        while(it.hasNext()){
        	Entry<Integer, Date[]> next = it.next();
        	Date[] one_two = next.getValue();
        	String parten = "yyyy-MM-dd";
        	System.out.println(DateUtils.formatDate(one_two[0], parten)+"至"+DateUtils.formatDate(one_two[1], parten));
        }
		日期分割测试*/
        
        
        /*不同年日期分割测试
        Date start = parseDate("2008-10-11");
        Date end = parseDate("2013-11-12");
        Map<Integer,Date[]>  m = splitDate_year(start, end);
        Iterator<Entry<Integer, Date[]>> it = m.entrySet().iterator();
        while(it.hasNext()){
        	Entry<Integer, Date[]> next = it.next();
        	Date[] one_two = next.getValue();
        	String parten = "yyyy-MM-dd";
        	System.out.println(DateUtils.formatDate(one_two[0], parten)+"至"+DateUtils.formatDate(one_two[1], parten));
        }*/
        String s = ".34";
        //if(s.contains("@")){
        	String[] ss = s.split("@");
        	System.out.println(ss.length);
        //}
	}
}
