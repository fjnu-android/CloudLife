package com.cloudlife.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetPostUtil {

	public class UtilParam{
		
		public String szUrl = "";
		public String szReferer = "";
		public String szCookie = "";
		public String szCookieRet = "";
		public int iState = 0;
		public String szPostData = "";
	}
	
	/**
	 *  向指定网址发送get请求
	 *  @param szUrl 发送请求的url
	 *  @param szReferer 发送请求的头包的referer参数
	 *  @param szCookie 发送请求头包中的cookie参数
	 *  @param szCookieRet 返回的cookie值
	 *  @param iState 访问url时返回的访问状态
	 *  @return 返回url请求所获得的数据内容
	 */
	public static String getNet(UtilParam param) {
		String szResult = "";
		BufferedReader in = null;
		try {
			URL url = new URL(param.szUrl);
			// 打开与url的连接
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			urlConnect.setRequestProperty("Accept", "*/*");
			urlConnect.setRequestProperty("Connection", "Keep-Alive");
			urlConnect.setRequestProperty("Referer", param.szReferer);
			urlConnect.setRequestProperty("Cookie", param.szCookie);
			urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
			
			// 建立实际的连接
			urlConnect.connect();
			
			// 获取所有响应的头字段
			param.szCookieRet = urlConnect.getHeaderField("Set-Cookie");
			param.iState = urlConnect.getResponseCode();
			if (param.iState != 200) 
				szResult = "error";
			else {
				
				in = new BufferedReader(
						new InputStreamReader(urlConnect.getInputStream(), "utf-8"));
				String szLineTmp;
				while ( (szLineTmp = in.readLine()) != null) {
					
					szResult += "\n" + szLineTmp;
				}
			}

		} catch (Exception e) {
			
			System.out.println("发送Get请求出现异常!" + e);
			e.printStackTrace();
		} finally {
			
			// 一定要关闭输入流
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
				
				ex.printStackTrace();
			}
		}
		return szResult;
	}
	
	/**
	 * 向指定的url发送post方法的请求
	 *  @param szUrl 发送请求的url
	 *  @param szReferer 发送请求的头包的referer参数
	 *  @param szCookie 发送请求头包中的cookie参数
	 *  @param szCookieRet 返回的cookie值
	 *  @param iState 访问url时返回的访问状态
	 *  @param szPostData post发送的内容
	 *  @return 返回url请求所获得的数据内容
	 */
	public static String postNet(UtilParam param) {		
		String szResult = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL url = new URL(param.szUrl);
			// 打开和URL之间的连接
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			urlConnect.setRequestProperty("Accept", "*/*");
			urlConnect.setRequestProperty("Connection", "Keep-Alive");
			urlConnect.setRequestProperty("Referer", param.szReferer);
			urlConnect.setRequestProperty("Cookie", param.szCookie);
			urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");

			// 发送POST请求必须设置如下两行
			urlConnect.setDoOutput(true);
			urlConnect.setDoInput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(urlConnect.getOutputStream());
			// 发送POST内容
			out.print(param.szPostData);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(urlConnect.getInputStream()));
			
			String szLineTmp;
			while ((szLineTmp = in.readLine()) != null) {
				szResult += "\n" + szLineTmp;
			}


		} catch (Exception e) {

			System.out.println("发送POST请求出现异常!" +e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return szResult;
		
	}
	
	static public String downloadImage(String strUrl , String imageName, String dir) {
		
		try {
			URL url = new URL(strUrl);
			try {
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(5*1000);
				InputStream is = conn.getInputStream();
				byte[] bytes = new byte[1024];
				// 每次读取到的图片长度
				int len = 0;
				
				OutputStream os = new FileOutputStream(dir + imageName + ".jpg");
				while ( (len= is.read(bytes)) >0) {
					os.write(bytes,0, len);
				}
				// 关闭相关流
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return dir + imageName + ".jpg";
	}
}










