package com.ssd.admin.util;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
public class FileDownloadUtil {

	public final static String APPLICATION = "application/octet-stream";
	
	/**
	 * 下载文件
	 * @param filePath  文件路径
	 * @param fileName  文件名称
	 * @param request   请求对象
	 * @param response  响应对象
	 * @throws Exception  抛出异常对象
	 * @modify by 吕佳诚 2015-8-17
	 */
	public static boolean downloadFile(String filePath, String fileName,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		try {
			//得到系统时间的字符串形式：20150813193344
			String datetimeString =  new SimpleDateFormat("yyyyMMddHHmmss").format( new Date());
			//将fileName修改为fileName系统时间字串.扩展名
			fileName = fileName.replaceAll("\\.", datetimeString+".");
			//转码
			fileName = URLDecoder.decode(fileName, "utf-8");
			filePath = filePath.replace("%20", " ");
			
			File file = new File(filePath);
		
			// 以流的形式下载文件
			InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			
			response.setContentType(APPLICATION);
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("gbk"), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(buffer);
			outputStream.flush();
			outputStream.close();
			return true;
		} catch (Exception e) {
			log.error("下载文件出现异常,文件不存在" + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 
	 * @param inputStream 读取字节流
	 * @param fileName    文件名称
	 * @param extendName  文件后缀名
	 * @param request     请求对象
	 * @param response    响应对象
	 * @throws Exception  抛出异常对象
	 */
	public static void downloadFile(InputStream inputStream, String fileName,
			String extendName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType(APPLICATION);
		//创建一个日期对象
//		Date date = new Date();
		//格式化日期对象
//		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		//获取系统当前的时间的格式化结果集 如：20150713174023
//		String dateStr = sf.format(date);
		//对文件名称进行urldecoder加密
		fileName = URLDecoder.decode(fileName, "utf-8");
		//追加上文件的后缀名
//		fileName += extendName;
		// 清空response
		response.reset();

		// 设置response的Header
		response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("gbk"), "iso8859-1"));
		
		response.addHeader("Content-Length", "" +inputStream.available());

		try {
			// 以流的形式下载文件
			InputStream fis = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			OutputStream toClient = new BufferedOutputStream(
			response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
