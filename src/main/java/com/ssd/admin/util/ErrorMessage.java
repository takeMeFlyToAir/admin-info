package com.ssd.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 日期工具类
 */
@Slf4j
public class ErrorMessage implements java.io.Serializable{
	
	 /**
     * 获取当前字段名称
     * @return
     */
    private  String title;
    private String errorMessage;
    
    public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTitle() {
		return title;
	}
	public static String getAllErrorMessage(Exception e) {
        return getErrorMessage(e, Integer.MAX_VALUE);
    }
	public void setTitle(String title) {
		this.title = title;
	}
//    public static String ToString(List<ErrorMessage> errorList) {
//    	StringBuffer buffer=new StringBuffer();
//    	for(ErrorMessage e:errorList){
//    		buffer.append(e.title + e.errorMessage+",");
//    	}	
//        buffer=buffer.append("\n");
//    	return buffer.toString();
//    }
//    /**
//     * 获取当前日期字符串
//     * @return
//     */
    public  static String ToString(List<Map> errorList) {
//    	Set<Long> list=errorList.keySet();
    	StringBuffer buffer=new StringBuffer();
    	for(Map l:errorList){
    		buffer=buffer.append("第"+l.get("row")+"行:");
    		List<ErrorMessage> error=(List<ErrorMessage>)l.get("error");
        	for(ErrorMessage e:error){
        		buffer.append(e.title + e.errorMessage+",");
        	}
        	buffer=buffer.append("\n");
    	}
    	return buffer.toString();
    }
//   
    /**
     * 取得当前时间字符串
     * @return
     */

    /**
     * 获取当年的最后一天
     * @return string
     */
   

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
    }
    /**
     * 公共的获取错误信息的方法
     * @author : fangbing-wu
     * @date :  2015年11月9日 下午6:15:40
     * @Version 1.0
     * @param e
     * @param rows	捕获异常信息行数
     * @return
     * @throws IOException 
     */
    public static String getErrorMessage(Exception e,int rows) {
        StringBuffer sbuffer= new StringBuffer();
        if(e.getCause()!=null){
		   sbuffer.append(e.getCause().getMessage()+"<br/>");
        }
        if(e.getMessage()!=null){
    		sbuffer.append(e.getMessage()+"<br/>");
        }
		StackTraceElement[] errors = e.getStackTrace();
		if(errors.length>0){
			for(int i =0;i<errors.length;i++){
				if(i<rows){
				   sbuffer.append("【"+errors[i].toString()+"】;<br/>");
				}else{
				   break;
				}
			}
		}
		return sbuffer.toString();
    }
}
