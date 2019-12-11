package mark.moduledatasource.util;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarkDateUtils extends DateUtils {

    public final static String  YYYY_MM_DD = "yyyy-MM-dd";
    public final static String  YYYY_MM = "yyyy-MM";
    public final static String  YYYY = "yyyy";
    public final static String  YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String  HH_MM="HH:mm";
    public final static String  HH_MM_SS="HH:mm:ss";

    private static Logger logger = LoggerFactory.getLogger(MarkDateUtils.class);
    /**
     * 将时间转换为字符
     * @param date 时间
     * @param pattern 格式（如: yyyy-MM-dd HH:mm:ss）
     * @return
     */
    public static String toString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将时间转换为字符
     * @param date 时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toString(Date date) {
        if(date==null){
            return null;
        }
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
    }

    /**
     * 获取当前时间字符
     * @return yyyy-MM-dd
     */
    public static String nowDate(){
        return	toString(new Date(), YYYY_MM_DD);
    }


    /**
     * 获取当前时间字符
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String nowTime(){
        return	toString(new Date(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取当前日期  年月日
     * @return
     */
    public static Date nowDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        try {
            return dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            logger.error("DateUtils.nowDay.exception:{}",e );
        }
        return null;
    }

    /**
     * 取得指定日期所在周的第一天
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 取得指定日期所在周的最后一天
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
    /**
     * 添加日期
     * @param date  ：指定时间
     * @param field ：添加类型
     * @param amount ：数量，为负则是减少
     * @return
     */
    public static Date addDay(Date date,int field, int amount) {   
        Calendar cd = Calendar.getInstance();  
        cd.setTime(date);   
        cd.add(field, amount);//增加一天   
        return cd.getTime();   
    }    
    /**
     * 获取指定年指定周的第一天
     * 		一周第一天为周一
     * 		一年中第一个星期必须7天
     * @param years
     * @param amount
     * @return
     */
    public static Date getFirstDateOfWeekAndYear(int years,int amount) {
    	Calendar cd = Calendar.getInstance();
    	cd.set(Calendar.YEAR, years);
    	cd.set(Calendar.WEEK_OF_YEAR, amount);
    	cd.setFirstDayOfWeek(Calendar.MONDAY);
    	cd.setMinimalDaysInFirstWeek(7);
    	return cd.getTime();
    }
    
    /**
     * 取得指定日期所在月的第一天
     */
    public static Date getFirstDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
    /**
     * 获取指定年指定月第一天
     * @return
     */
    public static Date getFirstDateByYearAndMonth(int year,int month) {
    	 Calendar c = Calendar.getInstance();
    	 c.set(Calendar.YEAR, year);
    	 c.set(Calendar.MONTH, month);
    	 c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
         c.set(Calendar.HOUR_OF_DAY, 0);
         c.set(Calendar.MINUTE, 0);
         c.set(Calendar.SECOND, 0);
         return c.getTime();
    }

    /**
     * 取得指定日期所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
    
    /**
	 * @Description:取得指定日期所在季度的第一天
	 * @Title: getCurrentQuarterStartTime
	 * @return Date  
	 * @author: huafu.su
	 */
	public static Date getFirstDayOfQuarter(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		SimpleDateFormat longSdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		SimpleDateFormat shortSdf = new SimpleDateFormat(YYYY_MM_DD);
		Date now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 3)
				c.set(Calendar.MONTH, 0);
			else if (currentMonth >= 4 && currentMonth <= 6)
				c.set(Calendar.MONTH, 3);
			else if (currentMonth >= 7 && currentMonth <= 9)
				c.set(Calendar.MONTH, 4);
			else if (currentMonth >= 10 && currentMonth <= 12)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
            logger.error("DateUtils.getFirstDayOfQuarter.exception:{}",e );
		}
		return now;
	}
 
	/**
	 * @Description: 取得指定日期所在季度的最后一天      
	 * @Title: getCurrentQuarterEndTime
	 * @return Date  
	 * @author: huafu.su
	 */
	public static Date getLastDayOfQuarter(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(getFirstDayOfQuarter(date));
		cal.add(Calendar.MONTH, 3);
		return cal.getTime();
	}

    /**
     * 获取某年第一天日期
     * @return Date
     */
    public static Date getFirstDayOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @return Date
     */
    public static Date getLastDayOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1); 
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    /**
     * @Description: 日期转换      
     * @Title: dateToString
     * @param date
     * @param pattern
     * @return String  
     * @author: huafu.su
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 将字符转日期
     * @param dateStr
     * @return
     */
    public static Date parseDateYMD(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        return dateFormat.parse(dateStr);
    }

    public static Date stringToDate(String dateStr,String pattern){
        SimpleDateFormat sf =new SimpleDateFormat(pattern);
        Date parseDate = null;
        try {
            parseDate = sf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("DateUtils.parseDateYMD.exception:{}， dateStr:{}",e , dateStr);
        }
        return parseDate;
    }
    /**
     * 返回年月数字
     * 例如：201712格式的
     * @param timeLong
     * @return
     */
    public static  long getDateYMLong(long timeLong){
        if(timeLong>0) {
            Date date = new Date(timeLong);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            String monthStr = "";
            if (month < 10) {
                monthStr = "0" + String.valueOf(month);
            } else {
                monthStr = String.valueOf(month);
            }
            String ymdStr = String.valueOf(year) + monthStr;
            return Long.parseLong(ymdStr);
        }
        return 0;
    }
    /**
     * 返回年月日数字
     * 例如：20171211
     * @param timeLong
     * @return
     */
    public static long getDateYMDLong(long timeLong){
        if(timeLong>0) {
            Date date = new Date(timeLong);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DATE);
            String monthStr = "", dayStr = "";
            if (month < 10) {
                monthStr = "0" + String.valueOf(month);
            } else {
                monthStr = String.valueOf(month);
            }
            if (day < 10) {
                dayStr = "0" + String.valueOf(day);
            } else {
                dayStr = String.valueOf(day);
            }
            String ymdStr = String.valueOf(year) + monthStr + dayStr;
            return Long.parseLong(ymdStr);
        }
        return 0;
    }

    public static List<Date> getDateRangeList(Date start, Date end){
        List<Date> list = new ArrayList<>();
        list.add(start);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Date nowDay = MarkDateUtils.nowDay();
            if(nowDay !=null && nowDay.getTime()>calBegin.getTime().getTime()) {
                list.add(calBegin.getTime());
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
    	String timeStr = "2018-01-09";
    	// 获取周数
    	Calendar cd = Calendar.getInstance();
    	cd.setTime(MarkDateUtils.stringToDate(timeStr, MarkDateUtils.YYYY_MM_DD));
    	int weeks = cd.get(Calendar.WEEK_OF_YEAR);
    	Calendar cd2 = Calendar.getInstance();
    	cd2.set(Calendar.YEAR, 2018);
    	cd2.set(Calendar.WEEK_OF_YEAR, weeks);
    }
}
