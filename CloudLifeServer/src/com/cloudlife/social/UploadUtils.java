package com.cloudlife.social;

import javax.servlet.http.Part;

/**
 * @brief 文件上传工具类
 * @author wuyi
 *
 */
public class UploadUtils {
	
	public static String getFileType(Part p){  
		String name = p.getHeader("content-disposition");  
		String fileNameTmp = name.substring(name.indexOf("filename=")+10);  
		String type = fileNameTmp.substring(fileNameTmp.indexOf(".")+1,fileNameTmp.indexOf("\""));  
		return type;  
	}  
	public static String getFileName(Part p){  
		String name = p.getHeader("content-disposition");  
		String fileNameTmp = name.substring(name.indexOf("filename=")+10);  
		String fileName = fileNameTmp.substring(0,fileNameTmp.indexOf("\""));  
		return fileName;  
	}  
}
