package com.ssd.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author Li Banggui
 */
@Slf4j
public class DateUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间戳
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * 获取当前日期字符串
     * @return
     */
    public static String getDateStr() {
        return getDateStr(DEFAULT_FORMAT);
    }

    /**
     * 取得当前时间字符串
     * @return
     */
    public static String getDateStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期
     * @param date 日期实例
     * @param pattern 格式
     * @return
     */
    public static String getDateStr(Date date,String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 格式化日期
     * @param date 日期实例
     * @return
     */
    public static String getDateStr(Date date) {
        return DateFormatUtils.format(date, DEFAULT_FORMAT);
    }

    /**
     * 获取当前日期的年月
     * @return
     */
    public static String getDateYYYYMM() {
        return getDateStr("yyyyMM");
    }


    /**
     * 日期字符串转换成Date
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return
     * @throws Exception
     */
    public static Date parse(String dateStr,String pattern){
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return Date.from(LocalDateTime.parse(dateStr,format).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 取得下一天
     * @param dateStr 日期字符串
     * @param sourcePattern 传入的日期格式
     * @param resultPattern 返回之后的日期格式
     * @return
     */
    public static String getNextDay(String dateStr,String sourcePattern, String resultPattern){
        return getAfterDay(dateStr, 1, sourcePattern, resultPattern);
    }

    /**
     * 取得下一天
     * @param dateStr 日期字符串
     * @return
     */
    public static String getNextDay(String dateStr) {
        return getAfterDay(dateStr,1,DEFAULT_FORMAT,DEFAULT_FORMAT);
    }

    /**
     * 取得下一天
     * @param dateStr 日期字符串
     * @param days 天数
     * @param sourcePattern 传入的日期格式
     * @param resultPattern 返回之后的日期格式
     * @return
     */
    public static String getAfterDay(String dateStr,int days,String sourcePattern, String resultPattern) {
        LocalDateTime dateTime = parseLocalDateTime(dateStr, sourcePattern);
        return formatLocalDateTime(dateTime.plusDays(days),resultPattern);
    }


    /**
     * 取得前一天
     * @param dateStr 日期字符串
     * @return
     */
    public static String getBeforeDay(String dateStr) {
        return getBeforeDays(dateStr,1, DEFAULT_FORMAT, DEFAULT_FORMAT);
    }

    /**
     * 取得前一天
     * @param dateStr 日期字符串
     * @param days 天数
     * @return
     */
    public static String getBeforeDays(String dateStr,int days) {
        return getBeforeDays(dateStr,days,DEFAULT_FORMAT,DEFAULT_FORMAT);
    }

    /**
     * 取得前一天
     * @param dateStr 日期字符串
     * @param days 天数
     * @param sourcePattern 传入的日期格式
     * @param resultPattern 返回之后的日期格式
     * @return
     */
    public static String getBeforeDays(String dateStr,int days,String sourcePattern, String resultPattern) {
        return getAfterDay(dateStr,-days,sourcePattern,resultPattern);
    }


    /**
     *
     * @param dateStr 日期字符串
     * @param sourcePattern 传入的日期格式
     * @param resultPattern 返回之后的日期格式
     * @return
     */
    public static String lastDayOfMonth(String dateStr,String sourcePattern,String resultPattern) {
        return formatLocalDateTime(parseLocalDateTime(dateStr, sourcePattern).with(TemporalAdjusters.lastDayOfMonth()), resultPattern);
    }

    private static LocalDateTime parseLocalDateTime(String dateStr, String sourcePattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(sourcePattern);
        return LocalDateTime.parse(dateStr,format);
    }

    private static String formatLocalDateTime(LocalDateTime dateTime, String resultPattern){
        if(dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(resultPattern));
    }

    public static String firstDayOfMonth(String dateStr){
        return formatLocalDateTime(parseLocalDateTime(dateStr, DEFAULT_FORMAT).with(TemporalAdjusters.firstDayOfMonth()), DEFAULT_FORMAT);
    }
    /**
     * 获得指定月
     * @param dateStr 日期字符串
     * @param sourcePattern 传入的日期格式
     * @param resultPattern 返回之后的日期格式
     * @param month　往前或往后几个月
     * @return
     */
    public static String getMonth(String dateStr,String sourcePattern,String resultPattern,int month){
        LocalDateTime dateTime = parseLocalDateTime(dateStr, sourcePattern);
        return formatLocalDateTime(dateTime.plusMonths(month),resultPattern);
    }
    
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }
    
    /**
     * 获取当年的第一天
     * @return date
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    
    /**
     * 获取当年的最后一天
     * @return date
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
    
    /**
     * 获取当年的第一天
     * @return string
     */
    public static String getCurrYearFirstStr(){
    	SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
    	Date date = getCurrYearFirst();
        String sDate = f.format(date);
    	return sDate;
    }
    
    /**
     * 获取当年的最后一天
     * @return string
     */
    public static String getCurrYearLastStr(){
    	SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
    	Date date = getCurrYearLast();
        String sDate = f.format(date);
    	return sDate;
    }

    public static void main(String[] args) {
        /*String dateStr = DateUtils.getDateStr();
        System.out.println(dateStr);
        System.out.println(DateUtils.getDateYYYYMM());
        System.out.println(DateUtils.getNextDay(dateStr, DEFAULT_FORMAT, DEFAULT_FORMAT));
        System.out.println(DateUtils.lastDayOfMonth(dateStr, DEFAULT_FORMAT, DEFAULT_FORMAT));
        System.out.println(DateUtils.getBeforeDay(dateStr));
        
        Calendar curr = Calendar.getInstance();
    	curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
    	Date date=curr.getTime();
    	System.out.println(date);*/
    	
    	log.debug(DateUtils.getCurrYearFirstStr());
    	log.debug(DateUtils.getCurrYearLastStr());

    	System.out.println(DateUtils.parse("2017-11-20 12:35:27", "yyyy-MM-dd HH:mm:ss"));
    }

}
