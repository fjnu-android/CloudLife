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
	 *  ��ָ����ַ����get����
	 *  @param szUrl ���������url
	 *  @param szReferer ���������ͷ����referer����
	 *  @param szCookie ��������ͷ���е�cookie����
	 *  @param szCookieRet ���ص�cookieֵ
	 *  @param iState ����urlʱ���صķ���״̬
	 *  @return ����url��������õ���������
	 */
	public static String getNet(UtilParam param) {
		String szResult = "";
		BufferedReader in = null;
		try {
			URL url = new URL(param.szUrl);
			// ����url������
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// ����ͨ�õ���������
			urlConnect.setRequestProperty("Accept", "*/*");
			urlConnect.setRequestProperty("Connection", "Keep-Alive");
			urlConnect.setRequestProperty("Referer", param.szReferer);
			urlConnect.setRequestProperty("Cookie", param.szCookie);
			urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
			
			// ����ʵ�ʵ�����
			urlConnect.connect();
			
			// ��ȡ������Ӧ��ͷ�ֶ�
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
			
			System.out.println("����Get��������쳣!" + e);
			e.printStackTrace();
		} finally {
			
			// һ��Ҫ�ر�������
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
	 * ��ָ����url����post����������
	 *  @param szUrl ���������url
	 *  @param szReferer ���������ͷ����referer����
	 *  @param szCookie ��������ͷ���е�cookie����
	 *  @param szCookieRet ���ص�cookieֵ
	 *  @param iState ����urlʱ���صķ���״̬
	 *  @param szPostData post���͵�����
	 *  @return ����url��������õ���������
	 */
	public static String postNet(UtilParam param) {		
		String szResult = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL url = new URL(param.szUrl);
			// �򿪺�URL֮�������
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			// ����ͨ�õ���������
			urlConnect.setRequestProperty("Accept", "*/*");
			urlConnect.setRequestProperty("Connection", "Keep-Alive");
			urlConnect.setRequestProperty("Referer", param.szReferer);
			urlConnect.setRequestProperty("Cookie", param.szCookie);
			urlConnect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");

			// ����POST�������������������
			urlConnect.setDoOutput(true);
			urlConnect.setDoInput(true);
			// ��ȡHttpURLConnection�����Ӧ�������
			out = new PrintWriter(urlConnect.getOutputStream());
			// ����POST����
			out.print(param.szPostData);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(urlConnect.getInputStream()));
			
			String szLineTmp;
			while ((szLineTmp = in.readLine()) != null) {
				szResult += "\n" + szLineTmp;
			}


		} catch (Exception e) {

			System.out.println("����POST��������쳣!" +e);
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
				// ÿ�ζ�ȡ����ͼƬ����
				int len = 0;
				
				OutputStream os = new FileOutputStream(dir + imageName + ".jpg");
				while ( (len= is.read(bytes)) >0) {
					os.write(bytes,0, len);
				}
				// �ر������
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










