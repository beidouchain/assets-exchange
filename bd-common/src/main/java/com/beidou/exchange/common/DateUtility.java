/**
 * <p>Title:Toten</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  </p>
 * @author 
 * @version 1.0
 */
package com.beidou.exchange.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;


/**
 * 2005-7-26 14:41:31
 */
public class DateUtility {

    private static final long ONE_DAY = 86400000L; //一天的毫秒数

    /**
     * 获取当前日期字符串 格式为YYYY-MM-DD
     * 
     * @return java.lang.String
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(new Date());
        return s;
    }

    /**
     * 取得当天（yyyy-MM-dd）time
     * @return
     */
    public static long getCuttentDateTime() {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String s = df.format(new Date());
         Date date = strToDate(s);
         return date.getTime();
    }

    public static String dateToStrWithoutYear(Long date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }
    public static String dateToStrWithoutHour(Long date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }
    public static int dateToStrWithoutDay(Long date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("dd");
        if (date != null) {
        	str = df.format(date);
        }
        return Integer.parseInt(str);
    }
    public static String getCurrentYear() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        String s = df.format(new Date());
        return s;
    }

    public static String getCurrentMonth() {
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String s = df.format(new Date());
        return s;
    }

    public static String getDayInWeek(String sDate) {
        Date date = strToDate(sDate);
        SimpleDateFormat df = new SimpleDateFormat("EEE");
        String s = df.format(date);
        return s;
    }

    public static Date strToDate(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date convertToDate(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date convertToDateTime(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date strToDateTime(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date strToDateTime(String timeStr, String pattern) {
        Date date = null;
        if (timeStr != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                date = df.parse(timeStr);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static String dateTimeToStr(Date date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }
    public static String dateTimeToStrWithOutSec(Date date) {
    	String str = null;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	if (date != null) {
    		str = df.format(date);
    	}
    	return str;
    }

    public static String dateTimeToStr(Date date, String pattern) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    public static String dateToStr(Date date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    /**
     * 转化成中文类型的日期
     * 
     * @param date
     * @return
     */
    public static String dateToStrCh(Date date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }
    
    public static String dateToStrMDW(Date date){
    	String ymd = DateUtility.dateToStrCh(date);
    	ymd = ymd.substring(5,ymd.length());
    	String week = DateUtility.dateToWeekNameCN(DateUtility.dateToStr(date));
		return ymd+week;
    }

    /**
     * 在原有的日期上面加i天
     * 
     * @param date
     * @param i
     * @return
     */
    public static Date add(Date date, int i) {
        date = new Date(date.getTime() + i * ONE_DAY);
        return date;
    }

    /**
     * 加1天
     * 
     * @param date
     * @return
     */
    public static Date add(Date date) {
        return add(date, 1);
    }

    /**
     * 在原有基础上增加year年
     */
    public static Date addYear(Date date, int year) {
    	Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
    }

    /**
     * 减1天
     * 
     * @param date
     * @return
     */
    public static Date sub(Date date) {
        return add(date, -1);
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = df.format(new Date());
        return s;
    }

    public static String getCurrentDateTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String s = df.format(new Date());
        return s;
    }

    public static String getCurrentDateWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd EEE");
        String s = df.format(new Date());
        return s;

    }

    public static int getDateWeekNum(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 返回月份之间的差。
     * 
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    public static int compareMonth(String startYear, String startMonth, String endYear,
            String endMonth) {
        return (Integer.parseInt(endYear) - Integer.parseInt(startYear)) * 12
                + (Integer.parseInt(endMonth) - Integer.parseInt(startMonth));

    }

    /**
     * @param sDate
     * @return
     */
    public static String getYearMonth(String sDate) {
        Date date1 = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = null;
        try {
            date1 = df.parse(sDate);
            df.applyPattern("yyMM");
            s = df.format(date1);
        } catch (ParseException e) {
            return s;
        }
        return s;
    }

    /**
     * @param date
     * @return
     */
    public static String getYearMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyMM");
        String s = null;

        s = df.format(date);

        return s;

    }

    public static String getYearMonthDay(String sDate) {
        Date date1 = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = null;
        try {
            date1 = df.parse(sDate);
            df.applyPattern("yyMMdd");
            s = df.format(date1);
        } catch (ParseException e) {
            return s;
        }
        return s;
    }

    /**
     * @param sDate
     * @return
     */
    public static String getMonth(String sDate) {
        Date date1 = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String s = null;
        try {
            date1 = df.parse(sDate);
            df.applyPattern("MM");
            s = df.format(date1);
        } catch (ParseException e) {
            return s;
        }
        return s;

    }

    /**
     * @param sDate1
     * @param sDate2
     * @return
     */
    public static int compareDate(String sDate1, String sDate2) {

        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date1 = dateFormat.parse(sDate1);
            date2 = dateFormat.parse(sDate2);
        } catch (ParseException e) {

        }

        long dif = 0;
        if (date2.after(date1)) dif = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24;
        else dif = (date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24;

        return (int) dif;
    }

    /**
     * 比较日期大小
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(Date date1, Date date2) {
    	if (date1 == null && date2 == null) {
    		return 0;
    	} else if (date1 != null && date2 == null) {
    		return 1;
    	} else if (date1 == null && date2 != null) {
    		return -1;
    	} else {
    		return date1.compareTo(date2);
    	}
    }

    public static int getDate(String sDate, String sTag) {
        int iSecondMinusPos = sDate.lastIndexOf('-');
        if (sTag.equalsIgnoreCase("y")) {
            return Integer.parseInt(sDate.substring(0, 4));
        } else if (sTag.equalsIgnoreCase("m")) {
            return Integer.parseInt(sDate.substring(5, iSecondMinusPos));
        } else return Integer.parseInt(sDate.substring(iSecondMinusPos + 1));
    }

    /**
     * 获取指定时间的上一个月份</br></br>
     * 输入的日期格式为: 'yyyy-MM-dd'</br>
     * 返回的日期格式为: 'yyyy-MM'</br>
     * @return
     */
    public static String getLastMonthOfDate(String currentDate){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
        Calendar calendar = Calendar.getInstance();  
        if(StringUtils.isEmpty(currentDate)){
        	throw new RuntimeException("getLastMonthOfDate currentDate is blank, failure.");
        }
        Date currDate = java.sql.Date.valueOf(currentDate);  
        calendar.setTime(currDate);
        calendar.add(Calendar.MONTH, -1);
        return sdf.format(calendar.getTime());
    }
    
    /**
     * 得到当前时间的时分秒。格式：hh:mm:ss。
     * 
     * @return
     */
    public static String getCurrentTime() {
        Calendar CD = Calendar.getInstance();
        int HH = CD.get(Calendar.HOUR);
        int NN = CD.get(Calendar.MINUTE);
        int SS = CD.get(Calendar.SECOND);
        String time = String.valueOf(HH) + ":" + String.valueOf(NN) + ":" + String.valueOf(SS);
        return time;
    }

    /**
     * 返回相差天数
     * 
     * @param endDate
     * @param startDate
     * @return
     */
    public static int betweenDay(Date endDate, Date startDate) {
        if (endDate == null || startDate == null) return 0;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(endDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        long l = c1.getTimeInMillis() - c2.getTimeInMillis();

        return (int) (l / (1000*3600*24));
    }

    /**
     * 返回相差天数
     * 
     * @param endDate
     * @param startDate
     * @return
     */
    public static int getIntervalDays(Date endDate, Date startDate) {
    	SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		endDate = simple.parse(simple.format(endDate));
    		startDate = simple.parse(simple.format(startDate));
    		long time1 = endDate.getTime();
    		long time2 = startDate.getTime();
    		long between_days = (time1 - time2)/(1000*3600*24);
    		return (int)between_days;
    	} catch(Exception ex) {
    		
    	}

    	return 0;
    }

    /**
     * 返回相差天数
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static int getIntervalDaysWithFloorDiv(Long endDate, Long startDate) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long between_days = Math.floorDiv((endDate - startDate), (1000*3600*24L));
            return (int)between_days;
        } catch(Exception ex) {

        }

        return 0;
    }

    /**
     * 返回相差分钟数
     * 
     * @param endDate
     * @param startDate
     * @return
     */
    public static int betweenMinute(Date endDate, Date startDate) {
        if (endDate == null || startDate == null) return 0;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(endDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        long l = c1.getTimeInMillis() - c2.getTimeInMillis();

        return (int) (l / (1000 * 60));
    }

    /**
     * 返回相差秒数数
     * 
     * @param endDate
     * @param startDate
     * @return
     */
    public static int betweenSecond(Date endDate, Date startDate) {
        if (endDate == null || startDate == null) return 0;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(endDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        long l = c1.getTimeInMillis() - c2.getTimeInMillis();

        return (int) (l / (1000));
    }

    public static String dateToStr(Long date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    public static String dateToDay(Long date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

  public static long stringToLong(String strTime, String formatType) {
    Date date = strToDateTime(strTime, formatType); // String类型转成date类型
    if (date == null) {
      return 0;
    }
    else {
        return date.getTime();
    }
  }

  
  public static long dateToLong(Date date) {
    return date.getTime();
  }

    //获取当前天0点时间
    public static Long getCurrentDayMillisecond() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
  //获取某天0点时间
    public static Long getCurrentDayMillisecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
    /**
     * 年月 的第一天时间
     * @param year
     * @param month
     * @return
     */
    public static Long getStartDayMillisecond(String year,String month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
    /**
     * 年月 的最后时间
     * @param year
     * @param month
     * @return
     */
    public static Long getLastDayMillisecond(String year,String month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.DATE, getThisMonthForDayNum(year + month));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime();
    }
    //获取当前天23点59秒时间
    public static Long getCurrentDayEndMillisecond() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime();
    }
  //获取某天23点59秒时间
    public static Long getCurrentDayEndMillisecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime();
    }
    /**
     * 获取当月第一天0点时间
     */
    public static Long getFirstDayOfMonthStartTime() {
    	Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        gcLast.set(Calendar.HOUR_OF_DAY, 0);
        gcLast.set(Calendar.SECOND, 0);
        gcLast.set(Calendar.MINUTE, 0);
        gcLast.set(Calendar.MILLISECOND, 0);
        return gcLast.getTime().getTime();
    }
    /**
     * 得到当月某一天开始时间
     * @param day
     * @return
     */
    public static Long getSomeDayOfMonthStartTime(int day) {
    	Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, day);
        gcLast.set(Calendar.HOUR_OF_DAY, 0);
        gcLast.set(Calendar.SECOND, 0);
        gcLast.set(Calendar.MINUTE, 0);
        gcLast.set(Calendar.MILLISECOND, 0);
        return gcLast.getTime().getTime();
    }
    /**
     * 得到当月某一天结束时间
     * @param day
     * @return
     */
    public static Long getSomeDayOfMonthEndTime(int day) {
    	Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, day);
        gcLast.set(Calendar.HOUR_OF_DAY, 23);
        gcLast.set(Calendar.SECOND, 59);
        gcLast.set(Calendar.MINUTE, 59);
        gcLast.set(Calendar.MILLISECOND, 999);
        return gcLast.getTime().getTime();
    }
    /**
     * 得到从周一开始两周时间
     * @return
     */
    public static long getTwoWeeks() {
        Calendar now = Calendar.getInstance();
        now.setFirstDayOfWeek(Calendar.MONDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        //long monday = now.getTimeInMillis();
        now.add(Calendar.DAY_OF_MONTH, 13);
        return now.getTimeInMillis();
    }
    
    public static long getWeeks(int number) {
        Calendar now = Calendar.getInstance();
        now.setFirstDayOfWeek(Calendar.MONDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        //long monday = now.getTimeInMillis();
        now.add(Calendar.DAY_OF_MONTH, number);
        return now.getTimeInMillis();
    }
    
    public static long getPreviousDays(int number) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        //long monday = now.getTimeInMillis();
        now.add(Calendar.DAY_OF_MONTH, -number);
        return now.getTimeInMillis();
    }
    
    public static long getPreviousDaysEndTime(int number) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        //long monday = now.getTimeInMillis();
        now.add(Calendar.DAY_OF_MONTH, -number);
        return now.getTimeInMillis();
    }
    /**
     * 得到周一方法
     * @return
     */
    public static long getMonday() {
        Calendar now = Calendar.getInstance();
        now.setFirstDayOfWeek(Calendar.MONDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }
    /**
     * 得到周二开始时间
     * @return
     */
    public static long getTuesdayStartTime() {
        Calendar now = Calendar.getInstance();
        now.setFirstDayOfWeek(Calendar.TUESDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }
    /**
     * 得到周二开始时间
     * @return
     */
    public static long getTuesdayEndTime() {
        Calendar now = Calendar.getInstance();
        now.setFirstDayOfWeek(Calendar.TUESDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        return now.getTimeInMillis();
    }



    public static int getYear(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getWeek(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDay(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DATE);
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getDayOfMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date parseDateIgnoreException(DateFormat df, String dateStr){
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 得到月份的天数
     * @return
     */
    public static int getThisMonthForDayNum(String date){
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat oSdf = new SimpleDateFormat ("",Locale.ENGLISH);
    	oSdf.applyPattern("yyyyMM");
    	try {  
            cal.setTime(oSdf.parse(date));
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
    	return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 当前时间到当天23:59:59还有多少秒
     * @param date 日期
     * @return
     */
    public static int betweenSeconds(Date date) {
        if (date == null) {
            return 0;
        }
        long beginTime = date.getTime();
        long endTime = DateUtility.getEndTimeOfDay(date);
        return (int)(Math.abs(endTime - beginTime) / 1000);
    }

    /**
     * 取得几点
     * @param time
     * @return
     */
    public static int getTimeHour(long time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(time);

    	int hour = cal.get(Calendar.HOUR_OF_DAY);
    	return hour;
    }

    /**
     * 取得几分
     * @param time
     * @return
     */
    public static int getTimeMinute(long time) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(time);

    	int minute = cal.get(Calendar.MINUTE);
    	return minute;
    }

    /**
     * 得到日期所在周的周一
     * @return
     */
    public static long getTimeMonday(long time) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time);
        now.setFirstDayOfWeek(Calendar.MONDAY);
        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }

    /**
     * 得到日期为几号
     * @return
     */
    public static int getTimeDay(long time) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time);
        int date = now.get(Calendar.DATE);
        return date;
    }

    /**
     * 取得周几
     *  周一为第一天
     * @param time
     * @return
     */
    public static int getTimeWeek(long time) {
	  Calendar cal = Calendar.getInstance();
	  cal.setFirstDayOfWeek(Calendar.MONDAY); 
	  cal.setTimeInMillis(time);
	  int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
      if (0 == weekDay) {
    	  weekDay = 7;
      }

	  return weekDay;
	}

    /** 
     * 取得当月天数 
     * */  
    public static int getCurrentMonthLastDay(long time) {
        Calendar cal = Calendar.getInstance();  
        cal.setTimeInMillis(time);
        cal.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        cal.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = cal.get(Calendar.DATE);  
        return maxDate;  
    }  

    /**
     * 获取给定时间当月第一天0点时间
     */
    public static long getMonthStartTime(long time) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public static long getDayNumber() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return now.getTimeInMillis();
	}

    /**
     * 获取date所在月的1号 00:00:00
     * @param date
     * @return
     */
    public static Long getStartTimeOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取date所在月的上个月1号 00:00:00
     * @param date
     * @return
     */
    public static Long getStartTimeOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 上个月的最后一天 23:59:59.999
     * @return
     */
    public static long getEndTimeOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //月份最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取date所在月最后一天的 23:59:59
     * @param date
     * @return
     */
    public static Long getEndTimeOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //月的最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取date所在月的上个月1号 00:00:00
     * @param date
     * @return
     */
    public static Long getLastMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 日期的开始时间 00:00:00.000
     * @param date
     * @return
     */
    public static Long getStartTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 日期的结束时间 23:59:59.999
     *
     * @param date  指定的日期
     * @return
     */
    public static Long getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 日期明日的开始时间 00:00:00.000
     * @param date
     * @return
     */
    public static Long getStartTimeOfTomorrow(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1); //明日
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 日期明日的结束时间 23:59:59.999
     *
     * @param date  指定的日期
     * @return
     */
    public static Long getEndTimeOfTomorrow(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1); //明日
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 指定日期的前一天（昨日）
     * @param date  指定的日期
     * @return
     */
    public static Long getStartTimeOfYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1); //向前一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 日期的结束时间 日期的开始时间 23:59:59.000
     *
     * @param date
     * @return
     */
    public static Long getEndTimeOfDayEndSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 000);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本周日 00:00:00
     */
    public static long getWeekOfSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //周日
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取中国本周周一 00:00:00
     * 注意：欧美，第一天是周日，周日～周六是一周，如果在周日计算周一，则会变成中国的下周一
     * 而中国，周一是一周的第一天
     */
    public static long getStartTimeOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); //将每周第一天设为星期一，默认是星期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //周一
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取中国本周周日 00:00:00
     * 注意：欧美，第一天是周日，周日～周六是一周，如果在周日计算周一，则会变成中国的下周一
     * 而中国，周一是一周的第一天
     */
    public static long getEndTimeOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); //将每周第一天设为星期一，默认是星期
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //周日
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取上周周一 00:00:00
     */
    public static long getStartTimeOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); //将每周第一天设为星期一，默认是星期天
        calendar.add(Calendar.WEEK_OF_MONTH, -1); //周数减一，即上周
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //日子设为周一

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取上周周日 23:59:59
     */
    public static long getEndTimeOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); //将每周第一天设为星期一，默认是星期天
        calendar.add(Calendar.WEEK_OF_MONTH, -1); //周数减一，即上周
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //日子设为周日

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取相对于date的下个月的第一天的 00:00:00
     * @param date 日期
     * @return
     */
    public static long getNextMonthStartDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); //下个月
        calendar.set(Calendar.DAY_OF_MONTH, 1); //月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 日期的昨天 23:59:59.999
     * @param date
     * @return
     */
    public static Long getEndTimeOfYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取date所在月份总天数
     * @param date 日期
     * @return
     */
    public static int getMonthDayCount(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private static final int REPORT_CATCH_CLEAR_TIME = 3;
    /**
     * 返回缓存时间：如果传入的日期大于3点的，缓存时间为到第二天0时，否则到当天的三点
     * 
     * @param startDate
     * @return
     */
    public static int betweenMinuteForReport(Date startDate) {
        if (startDate == null) {
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);
        
        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        int hour = c1.get(Calendar.HOUR_OF_DAY);
        if(hour < REPORT_CATCH_CLEAR_TIME){
        	c2.set(Calendar.HOUR_OF_DAY, REPORT_CATCH_CLEAR_TIME - 1);
        }else{
        	c2.set(Calendar.HOUR_OF_DAY, 23);
        }
        
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        c2.set(Calendar.MILLISECOND, 999);
        long l = c2.getTimeInMillis() - c1.getTimeInMillis();
        return (int) (l / (1000 * 60));
    }

    /**
     * 获取日期是周几
     * @param date 日期
     * @return     周日~周六：1-7，中国按照：周一~周日来算一周
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //周日~周六：1-7，中国按照：周一~周日来算一周
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 判断两个日期是不是同一天
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
        String day1 = fmtYMD.format(date1);
        String day2 = fmtYMD.format(date2);
        return day1.equals(day2);
    }

    /**
     * 设置日期为具体某一年
     * @param date
     * @param year
     * @return
     */
    public static Date setYear(Date date, int year) {
    	Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
    }

    /**
     * 设置日期为具体某一天
     * @param date
     * @param month
     * @return
     */
    public static Date setMonth(Date date, int month) {
    	Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, month - 1);
		return calendar.getTime();
    }

    /**
     * 设置日期为具体某一天
     */
    public static Date setDay(Date date, int day) {
    	Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.DATE, day);
		return calendar.getTime();
    }

    /**
     * 设置date1的时分秒为date2的时分秒
     * @param date1
     * @param date2
     * @return
     */
    public static Date setDayTime(Date date1, Date date2) {
    	Calendar calendar2 = Calendar.getInstance();
    	calendar2.setTime(date2);

    	Calendar calendar1 = Calendar.getInstance(); 
		calendar1.setTime(date1);
		calendar1.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
		calendar1.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
		calendar1.set(Calendar.SECOND, calendar2.get(Calendar.SECOND));
		calendar1.set(Calendar.MILLISECOND, calendar2.get(Calendar.MILLISECOND));

		return calendar1.getTime();
    }

    /**
     * 时间转日期
     * @param day  日期
     * @param hhmmss HH:mm:ss格式的时间
     * @return
     */
    public static Date hhmmssToDate(Date day, String hhmmss) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
            String dayStr = fmtYMD.format(day);
            Date target = fmt.parse(dayStr + " " + hhmmss);
            return target;
        }
        catch (Exception e) {
        }
        return null;
    }

    /**
     * 时间转日期
     * @param day  日期
     * @param hhmmss HH:mm:ss格式的时间
     * @return
     */
    public static Long hhmmssToDateTime(Date day, String hhmmss) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
            String dayStr = fmtYMD.format(day);
            Date target = fmt.parse(dayStr + " " + hhmmss);
            return target.getTime();
        }
        catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据workDays字符串判断日期是否是工作日
     * @param day       日期
     * @param workDays  工作日标记，用1-7表示周一~周日，逗号隔开，如 1,2,3,4,5
     * @return          true:是工作日，false:不是工作日
     */
    public static boolean isWorkDay(Date day, String workDays) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(day);
            //获取星期几，1-7对应 周日~周一
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            //转换成workDays一样的表示方法
            int week = (dayOfWeek - 1) == 0 ? 7 : (dayOfWeek - 1);
            return workDays.contains(Integer.toString(week));
        }
        catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取beginTime ~ endTime之间的日期列表，日期（字符串）格式为：yyyy-MM-dd
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return
     */
    public static List<String> getDayListBetween(long beginTime, long endTime) {
        List<String> dayList = new ArrayList<String>();
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
        //按照日期填充数据
        beginTime = DateUtility.getStartTimeOfDay(new Date(beginTime)); // 00:00:00
        endTime = DateUtility.getStartTimeOfDay(new Date(endTime));     // 00:00:00
        while (beginTime <= endTime) {
            String dayStr = formatYMD.format(new Date(beginTime));
            dayList.add(dayStr);

            beginTime += 24*60*60*1000;
        }

        return dayList;
    }

    /**
     * 获取beginTime ~ endTime之间的日期列表
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return
     */
    public static List<Date> getDateListBetween(long beginTime, long endTime) {
        List<Date> dateList = new ArrayList<Date>();
        //按照日期填充数据
        beginTime = DateUtility.getStartTimeOfDay(new Date(beginTime)); // 00:00:00
        endTime = DateUtility.getStartTimeOfDay(new Date(endTime));     // 00:00:00
        while (beginTime <= endTime) {
            dateList.add(new Date(beginTime));

            beginTime += 24*60*60*1000;
        }

        return dateList;
    }

    /**
     * 获取beginTime ~ endTime之间的日期个数
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return
     */
    public static int getDateCountBetween(long beginTime, long endTime) {
        int count = 0;
        //按照日期填充数据
        beginTime = DateUtility.getStartTimeOfDay(new Date(beginTime)); // 00:00:00
        endTime = DateUtility.getStartTimeOfDay(new Date(endTime));     // 00:00:00
        while (beginTime <= endTime) {
            count ++;
            beginTime += 24*60*60*1000;
        }

        return count;
    }
    
    /**
     *  往前推指定天数
     * @param lastDay
     * @return
     */
    public static Date queryStartDayTime(Date lastDay,int amount){
    		Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDay);
        calendar.add(Calendar.DAY_OF_MONTH, 0-amount); 
        Date firstDay = calendar.getTime();
        return new Date(getStartTimeOfDay(firstDay));
    }

    /**
     * 从lastDay往前推30天
     * @param lastDay 日期
     * @return
     */
    public static Map<String, Date> query30DaySearchTime(Date lastDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDay);
        calendar.add(Calendar.DAY_OF_MONTH, -30); //往前30天
        Date firstDay = calendar.getTime();

        Map<String, Date> map = new HashMap<String, Date>();
        map.put("beginTime", new Date(getEndTimeOfDay(lastDay)));
        map.put("endTime", new Date(getStartTimeOfDay(firstDay)));
        return map;
    }



    /**
     * 判断两个日期是否是同一个月
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        return month1 == month2;
    }

    /**
     * date1所在的月份是否比date2所在的月份小
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static boolean monthLessThan(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        return month1 < month2;
    }



    /**
     * 获取某个季度第一天 00:00:00
     * @param year     年
     * @param quarter  季度，1=一季度，2=二季度，3=三季度，4=四季度
     * @return
     */
    public static long getStartTimeOfQuarter(int year, int quarter) {
        if(quarter < 1 || quarter > 4) {
            throw new RuntimeException("quarter参数错误，只能为1-4的数字！");
        }
        int startMonth = 0, endMonth = 0; //季度的开始月份、结束月份
        switch (quarter) {
            case 1: //第一季度：1月－3月
                startMonth = 1;
                endMonth = 3;
                break;
            case 2: //第二季度：4月－6月
                startMonth = 4;
                endMonth = 6;
                break;
            case 3: //第三季度：7月－9月
                startMonth = 7;
                endMonth = 9;
                break;
            case 4: //第四季度：10月－12月
                startMonth = 10;
                endMonth = 12;
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year); //年
        calendar.set(Calendar.MONTH, startMonth - 1); //月
        calendar.set(Calendar.DAY_OF_MONTH, 1); //月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某个季度最后一天 23:59:59
     * @param year     年
     * @param quarter  季度，1=一季度，2=二季度，3=三季度，4=四季度
     * @return
     */
    public static long getEndTimeOfQuarter(int year, int quarter) {
        if(quarter < 1 || quarter > 4) {
            throw new RuntimeException("quarter参数错误，只能为1-4的数字！");
        }
        int startMonth = 0, endMonth = 0; //季度的开始月份、结束月份
        switch (quarter) {
            case 1: //第一季度：1月－3月
                startMonth = 1;
                endMonth = 3;
                break;
            case 2: //第二季度：4月－6月
                startMonth = 4;
                endMonth = 6;
                break;
            case 3: //第三季度：7月－9月
                startMonth = 7;
                endMonth = 9;
                break;
            case 4: //第四季度：10月－12月
                startMonth = 10;
                endMonth = 12;
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year); //年
        calendar.set(Calendar.MONTH, endMonth - 1); //月
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //月份最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某一年第一天 00:00:00
     * @param year 年
     * @return
     */
    public static long getStartTimeOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year); //年
        calendar.set(Calendar.MONTH, 1 - 1); //月
        calendar.set(Calendar.DAY_OF_MONTH, 1); //月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某一年最后一天 23:59:59
     * @param year 年
     * @return
     */
    public static long getEndTimeOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year); //年
        calendar.set(Calendar.MONTH, 12 - 1); //月
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //月份最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 查询日期属于哪个季度
     * 第一季度：1月-3月， 第二季度：4月-6月，第三季度：7月-9月，第四季度：10月-12月
     * @param date 日期
     * @return     1-4代表四个季度
     */
    public static int getQuarterOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        if (month >= 1 && month <= 3) {
            return 1;
        }
        else if (month >= 4 && month <= 6) {
            return 2;
        }
        else if (month >= 7 && month <= 9) {
            return 3;
        }
        else {
            return 4;
        }
    }

    /**
     * 将日期转换成com.sosgps.framework.i18n.MessageUtils.getMessage("mobile.biz.DateUtility_6")这样的名字
     * @param dateYMD yyyy-MM-dd格式的日期字符串
     * @return
     * @deprecated 此方法有问题，取出的为实际多一天，慎用
     * @see DateUtility#dateToWeekNameCN(String) 
     */
    public static String dateToWeekName(String dateYMD) {
        String weekName = "-";
        try {
            SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmtYMD.parse(dateYMD);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case 1: weekName = "星期一"; break;
                case 2: weekName = "星期二"; break;
                case 3: weekName = "星期三"; break;
                case 4: weekName = "星期四"; break;
                case 5: weekName = "星期五"; break;
                case 6: weekName = "星期六"; break;
                case 7: weekName = "星期日"; break;
            }
        }
        catch (Exception e) {}
        return weekName;
    }
    public static String dateToWeekNameCN(String dateYMD) {
    	String weekName = "-";
    	try {
    		SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = fmtYMD.parse(dateYMD);
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(date);
    		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
    		switch (dayOfWeek) {
            case 1: weekName = "星期一"; break;
            case 2: weekName = "星期二"; break;
            case 3: weekName = "星期三"; break;
            case 4: weekName = "星期四"; break;
            case 5: weekName = "星期五"; break;
            case 6: weekName = "星期六"; break;
            case 0: weekName = "星期日"; break;
        }
    	}
    	catch (Exception e) {}
    	return weekName;
    }
    
    /**
     * 将星期的数字转化成文字
     * @param weekNum
     * @return
     */
    public static String numToWeekName(Integer weekNum) {
    	String weekName = "-";
		switch (weekNum) {
		case 1: weekName = "星期一"; break;
		case 2: weekName = "星期二"; break;
		case 3: weekName = "星期三"; break;
		case 4: weekName = "星期四"; break;
		case 5: weekName = "星期五"; break;
		case 6: weekName = "星期六"; break;
		case 7: weekName = "星期日"; break;
		}
    	return weekName;
    }

    /**
     * 毫秒数转换成"xx小时xx分钟xx秒xxx毫秒"这样的描述信息
     * @param totalMillis 毫秒数
     * @return
     */
    public static String costTimeDescByTime(long totalMillis) {
        long millis = totalMillis % 1000;
        long seconds = (totalMillis / 1000) % 60;
        long minutes = (totalMillis / 1000 / 60) % 60;
        long hour = totalMillis / 1000 / 60 / 60;

        StringBuilder builder = new StringBuilder();
        if(hour > 0) {
            builder.append(hour).append("小时");
        }
        if(minutes > 0) {
            builder.append(minutes).append("分钟");
        }
        if(seconds > 0) {
            builder.append(seconds).append("秒");
        }
        if(millis > 0) {
            builder.append(millis).append("毫秒");
        }

        return builder.toString();
    }
 
    /**
     * 将当前时间与startTime之间的时间差，转换成"xx小时xx分钟xx秒xxx毫秒"这样的描述信息
     * @param startTime 开始时间
     * @return
     */
    public static String costTimeDesc(long startTime) {
        return costTimeDesc(startTime, System.currentTimeMillis());
    }

    /**
     * 将endTime与startTime之间的时间差，转换成"xx小时xx分钟xx秒xxx毫秒"这样的描述信息
     * @param startTime 开始时间
     * @return
     */
    public static String costTimeDesc(long startTime, long endTime) {
        long totalMillis = endTime - startTime;
        return costTimeDescByTime(totalMillis);
    }

    public static String getDistanceDate(Long s, Long e) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff =e-s;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        if(day==0&&hour!=0){
            return  hour + "小时" + min + "分钟";
        }
        if(day==0&&hour==0&&min!=0){
        	return  min + "分钟";
        }
        return day + "天" + hour + "小时" + min + "分钟";
    }


    public static void main(String[] args) {
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 0))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 1))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 2))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 3))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 4))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 5))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 6))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 7))));
    	System.out.println(dateToWeekNameCN(DateUtility.dateTimeToStr(DateUtility.add(new Date(), 8))));
        System.out.println(betweenMinuteForReport(new Date(1449599400000l)));
        System.out.println(betweenMinuteForReport(new Date()));
        System.out.println(getMonthListBetween(1515081600000L,1515340800000L));
    }

    /**
     * 从lastDay往前推固定的天数
     * @param lastDay 日期
     * @return
     */
    public static Long queryDayBeforeSearchTime(Date lastDay,int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDay);
        calendar.add(Calendar.DAY_OF_MONTH, -days); //往前30天
        Date firstDay = calendar.getTime();

        return firstDay.getTime();
    }

    public static int getMonthCountBetweenDays(Long startTime,Long endTime){
        int startMonth = getMonth(startTime);
        int endMonth = getMonth(endTime);
        return  endMonth - startMonth;
    }

    /**
     * 获取相对于date的下个月的最后一天的 23:59:59
     * @param date 日期
     * @return
     */
    public static long getNextMonthEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); //下个月
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //月的最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取相对当前前n个月的第一天的00：00：00
     * @return
     */
    public static long getPreviousMonthStartTime(Date date, int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-months);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); //月的第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        return  calendar.getTimeInMillis();
    }

    public static Long getYesterdayStartByLongTime(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime().getTime();
    }

    public static List<String> getMonthListBetween(Long startTime,Long endTime){
        String startStr = new SimpleDateFormat("yyyy-MM").format(new Date(startTime));//定义起始日期
        String endStr = new SimpleDateFormat("yyyy-MM").format(new Date(endTime));//定义结束日期
        List<String> monthList = new ArrayList<>();

        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(startStr);
            Date endDate = new SimpleDateFormat("yyyy-MM").parse(endStr);

            Calendar dd = Calendar.getInstance();//定义日期实例

            dd.setTime(startDate);//设置日期起始时间

            while(dd.getTime().before(endDate) || dd.getTime().equals(endDate)){//判断是否到结束日期

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String str = sdf.format(dd.getTime());
                monthList.add(str);

                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return monthList;
    }
}