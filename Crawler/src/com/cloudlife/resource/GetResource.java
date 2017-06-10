package com.cloudlife.resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.system.EncodeAndDecode;
import com.cloudlife.util.GetPostUtil;

/**
 *  实现爬取指定网页的数据 并入库
 * 
 * @author wuyi
 *
 */

public class GetResource {

	private class Data {
		String url;
		String type;

		public Data(String url, String type) {
			this.url = url;
			this.type = type;
		}
	
		@Override
		public boolean equals(Object obj) {
			Data cmp = (Data) obj;
			if (cmp.url.equals(url)== true &&
					cmp.type.equals(type) == true)
				return true;
			return false;
		}
	}

	static private GetResource m_GetResource;
	// 用队列存放当前需要爬虫的网址
	private ArrayDeque<Data> m_DataQueue = new ArrayDeque<Data>();

	private ArrayList<Data> m_ExistData = new ArrayList<Data>();

	private int m_nHasDeal = 0;
	
	private String[] bodyTypes = new String[]{
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qixuzhi.html\">气虚质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qixuzhi.html\">气虚质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yangxuzhi.html\">阳虚质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yangxuzhi.html\">阳虚质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yinxuzhi.html\">阴虚质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/yinxuzhi.html\">阴虚质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/pinghezhi.html\">平和质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/pinghezhi.html\">平和质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/shirezhi.html\">湿热质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/shirezhi.html\">湿热质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tanshizhi.html\">痰湿质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tanshizhi.html\">痰湿质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tebingzhi.html\">特秉质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/tebingzhi.html\">特秉质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qiyuzhi.html\">气郁质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/qiyuzhi.html\">气郁质</a>",
			"class=\"yi\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/xueyuzhi.html\">血瘀质</a>",
			"class=\"ji\"><a target=\"_blank\" href=\"http://tizhi.meishi.cc/xueyuzhi.html\">血瘀质</a>"};

	private String[]  bodyTypeAsw = new String[]{"yi_B", "ji_B","yi_D","ji_D","yi_C","ji_C","yi_A","ji_A",
							"yi_F", "ji_F","yi_E", "ji_E","yi_I", "ji_I","yi_G", "ji_G","yi_H", "ji_H"};
	
	static public GetResource getInstance() {

		if (m_GetResource == null)
			m_GetResource = new GetResource();
		return m_GetResource;
	}

	// 从构造函数里面启动独立线程进行爬虫处理
	// 因为GetResouce类采用单例模式 可以保障线程的安全性
	private GetResource() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (m_DataQueue.isEmpty() == false) {
						
						// 访问网络 获取数据   出队列
						System.out.println("count:" + m_DataQueue.size());
						Data data = m_DataQueue.pollFirst();
						
						try {
							getMenuData(data.url, data.type);
						} catch (Exception e) {
							e.printStackTrace();
							addLog(e.getMessage());
						}
						++m_nHasDeal;
						PublicData.gHasDeal = m_nHasDeal;
						System.out.println("count:" + m_DataQueue.size());
					}
					
				}

			}
		}).start();
	}
	
	public boolean addResource(String url, String type, int page) {

		// 这个函数用来获取批量菜品链接 一次性获取一个类目的菜品
		for (int i =0; i<= page; ++i) {
			// 访问网络 下载数据
			GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
			int pageCnt = i+1;
			utilParam.szUrl = url+ pageCnt;
			String strRet = GetPostUtil.getNet(utilParam);//cla_ing_i_list
			Pattern pat = Pattern.compile("<a target=\"_blank\" class=\"gray\" href='/food/[^']{1,50}'");
			Matcher mat = pat.matcher(strRet);//java.net.URLEncoder.encode(param.szUrl,   "utf-8");  

			while (mat.find()) {
				String curl = mat.group().replace("<a target=\"_blank\" class=\"gray\" href='", "").replace("'", "");

				Data data = new Data("http://www.ilinkee.com"+curl, type);
				
				m_ExistData.add(data);
				m_DataQueue.offerLast(data);
				PublicData.gNeedDeal = m_DataQueue.size();
			}
		}
		return true;
	}

	
	public boolean addResource3(String url, String type, int page) {

		// 这个函数用来获取批量菜品链接 一次性获取一个类目的菜品
		for (int i =2; i<= page; ++i) {
			// 访问网络 下载数据
			GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
			int pageCnt = i+1;
			utilParam.szUrl = url+ pageCnt;
			String strRet = GetPostUtil.getNet(utilParam);//cla_ing_i_list
			Pattern pat = Pattern.compile("(?=cla_ing_i_list)([\\s\\S]*?)(?=pagebar)");
			Matcher mat = pat.matcher(strRet);//java.net.URLEncoder.encode(param.szUrl,   "utf-8");  
			if (mat.find()) {
				strRet = mat.group();
				pat = Pattern.compile("<a title=\"[^\"]{1,10}\"");
				mat = pat.matcher(strRet);
			} else {
				return false;
			}
			while (mat.find()) {
				String curl = mat.group().replace("<a title=\"", "").replace("\"", "");
				if (curl.equals("鲳鱼")|| curl.equals("鳗鱼"))
					continue;
				try {
					curl = java.net.URLEncoder.encode(curl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}  
				Data data = new Data("http://www.xiangha.com/shicai/"+curl, type);
				
				m_ExistData.add(data);
				m_DataQueue.offerLast(data);
				PublicData.gNeedDeal = m_DataQueue.size();
			}
		}
		return true;
	}

	public boolean addResource(String url) {

		// 这个函数用来获取批量菜品链接 一次性获取一个类目的菜品
		
		// 访问网络 下载数据
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);
		// http://www.cnys.com/shicai/33275.html
		Pattern pat = Pattern.compile("http://www.cnys.com/shicai/[^.]{1,10}.html");
		Matcher mat = pat.matcher(strRet);
		while (mat.find()) {
			String curl = mat.group().replace(" ", "");
			Data data = new Data(curl, "A");
			m_ExistData.add(data);
			m_DataQueue.offerLast(data);
			PublicData.gNeedDeal = m_DataQueue.size();
		}
		return true;
	}

	public boolean addResouce(String url, String type) {
		SqlDeal sql = new SqlDeal();
		try{
			PreparedStatement stmt = sql.getConnection().prepareStatement("select bodyType from resource_food_dish where url=?;");
			stmt.setString(1, url);
			ResultSet res = stmt.executeQuery();
			String tmp = res.getString("bodyType");
			if (tmp != null) {
				if (tmp.contains(type))
					return false;
				else {
					tmp = tmp + " " + type;
					stmt = sql.getConnection().prepareStatement("update resource_food_dish set bodyType=? where url=?;");
					stmt.setString(1, tmp);
					stmt.setString(2, url);
					stmt.executeUpdate();
					++m_nHasDeal;
					PublicData.gHasDeal = m_nHasDeal;

					if (true) {
						stmt = sql.getConnection().prepareStatement("select bodyType from resource_food_menu where url=?;");
						stmt.setString(1, url);
						res = stmt.executeQuery();
						tmp = res.getString("bodyType");
						if (tmp != null) {
							if (tmp.contains(type))
								return false;
							else {
								tmp = tmp + " " + type;
								stmt = sql.getConnection().prepareStatement("update resource_food_menu set bodyType=? where url=?;");
								stmt.setString(1, tmp);
								stmt.setString(2, url);
								stmt.executeUpdate();
							}
						}
					}
					return true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		Data data = new Data(url, type);
		m_ExistData.add(data);
		PublicData.gNeedDeal = m_DataQueue.size();
		return m_DataQueue.offerLast(data);
	}

	// 待处理数据大小
	public int dealCount() {
		return m_DataQueue.size();
	}

	// 已处理数据的大小
	public int hasDealCount() {
		return m_nHasDeal;
	}

	 /** 
	    * 汉字转换位汉语拼音，英文字符不变 
	    * @param chines 汉字 
	    * @return 拼音 
	    */  
	    public static String converterToSpell(String chines){          
	        String pinyinName = "";  
	        char[] nameChar = chines.toCharArray();  
	        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
	        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
	        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
	        for (int i = 0; i < nameChar.length; i++) {  
	            if (nameChar[i] > 128) {  
	                try {  
	                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];  
	                } catch (BadHanyuPinyinOutputFormatCombination e) {  
	                    e.printStackTrace();  
	                }  
	            }else{  
	                pinyinName += nameChar[i];  
	            }  
	        }  
	        return pinyinName;  
	    }  
	
	private void getNetData2(String url, String type) {
		System.out.println("url："+ url);
		// 访问网络 下载数据
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// 菜品名 功能介绍 营养价值
		String food_name = null, food_info = null, food_nutrition = null;
		// 功效 适宜人群 禁忌人群
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;
		
		// 如果该食材不存在营养价值表 则放弃添加
		if (!strRet.contains("热量表"))
			return;
		
		// 正则匹配"
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		} else 
			return;
		String dishType = type;
		pat = Pattern.compile("(?=基本介绍)([\\s\\S]*?)(?=</p>)"); // 添加问号 换成懒惰模式
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = Html2Text(food_info).replace("基本介绍", "");
		}
		
		pat = Pattern.compile("(?=营养价值</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = Html2Text(food_nutrition).replace("营养价值", "");;
		}
		
		pat = Pattern.compile("(?=功效与作用</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = Html2Text(food_effect).replace("功效与作用", "");
		}	

		pat = Pattern.compile("适宜人群</strong>：[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("适宜人群</strong>：", "").replace("<", "");
		}		

		pat = Pattern.compile("禁忌人群</strong>：[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("禁忌人群</strong>：", "").replace("<", "");
		}	
		
		// 如何挑选物品
		String food_select = null;
		pat = Pattern.compile("(?=选购</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_select= mat.group();
			food_select = Html2Text(food_select).replace("选购", "");
		}
		
		////////////////////
		String food_tip = "";
		pat = Pattern.compile("(?=食用方法</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_tip = mat.group();
			food_tip = Html2Text(food_tip).replace("食用方法", "");;
		}
		
		String food_save = "";
		pat = Pattern.compile("(?=存储</h2>)([\\s\\S]*?)(?=</p>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_save= mat.group();
			food_save = Html2Text(food_save).replace("存储", "");
			
		}
	
		// 在美食杰获取食材的体质类型
		String urlTmp = "http://www.meishij.net/";
		try {
			urlTmp = "http://www.meishij.net/"+  java.net.URLEncoder.encode(food_name,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 
		System.out.println("urlTmp:"+ urlTmp);
		GetPostUtil.UtilParam utilParam2 = new GetPostUtil().new UtilParam();
		utilParam2.szUrl = urlTmp;
		String strRet2 = GetPostUtil.getNet(utilParam2);
		// 获取体质类型 写法：yi_  ji_
		// 采用循环匹配法
		String bodyTypeTmp = "";
		pat = Pattern.compile("(?=sc_header_con2)([\\s\\S]*?)(?=</ul>)");
		mat = pat.matcher(strRet2);
		String bodyType = "";
		if (mat.find()) {
			bodyTypeTmp = mat.group();
			for (int i =0; i< bodyTypes.length; ++i) {
				if (strRet2.contains(bodyTypes[i])) {
					bodyType +=" "+bodyTypeAsw[i];
				}
			}
		}
		
		System.out.println("介绍:"+food_info);
		System.out.println("名字:"+ food_name);
		System.out.println("存储:" + food_save);
		System.out.println("体质:" + bodyType);
		System.out.println("挑选:"+ food_select);
		System.out.println("提示:"+ food_tip);
		System.out.println("营养价值:" + food_nutrition);
		System.out.println("存储:"+ food_save);
		System.out.println("效果:" + food_effect);
		System.out.println("禁忌人群；"+ food_man_unsuit);
		System.out.println("适宜人群:"+food_man_suit);
		
		
		// 下载图片 http://images.meishij.net/p
		// 找到食物图片链接 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://static.xiangha.com/shicai/[^\"]{1,100}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "").replace("150x150", "280x220");      

			// 开始下载图片 保存到本地 不保存到数据库
			// 图片名为: md5(食物名)
			String dirPath = IndexServlet.getRealPath() + "/dish";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();		
			String imgName = EncodeAndDecode.md5(food_name);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServlet.getRealPath(), "");
		}
		
		
		System.out.println("开始解析营养值====");
	
		// 匹配出食物营养价值表 使用Json来存储数据
		JSONObject jsonValue = new JSONObject();
		// 热量
		pat = Pattern.compile("(?=热量</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("热量</a>（大卡）</span><em>", "").replace("</em>", "");
			jsonValue.put("热量(大卡)", tmp);
		} else {
			jsonValue.put("热量(大卡)", "0");
		}
		
		// 蛋白质
		pat = Pattern.compile("(?=蛋白质</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("蛋白质</a>（克）</span><em>", "").replace("</em>", "");
			jsonValue.put("蛋白质(克)", tmp);
		} else {
			jsonValue.put("蛋白质(克)", "0");
		}
		
		pat = Pattern.compile("(?=碳水化合物</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("碳水化合物</a>（克）</span><em>", "").replace("</em>", "");
			jsonValue.put("碳水化合物(克)", tmp);
		} else {
			jsonValue.put("碳水化合物(克)", "0");
		}
		
		pat = Pattern.compile("(?=膳食纤维</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("膳食纤维</a>（克）</span><em>", "").replace("</em>", "");
			jsonValue.put("膳食纤维(克)", tmp);
		} else {
			jsonValue.put("膳食纤维(克)", "0");
		}	
		
		pat = Pattern.compile("(?=胡萝卜素</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("胡萝卜素</a>（微克）</span><em>", "").replace("</em>", "");
			jsonValue.put("胡萝卜素(微克)", tmp);
		} else {
			jsonValue.put("胡萝卜素(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素A</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素A</a>（微克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素A(微克)", tmp);
		} else {
			jsonValue.put("维生素A(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素C</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素C</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素C", tmp);
		} else {
			jsonValue.put("维生素C", "0");
		}	
		
		pat = Pattern.compile("(?=钠</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("钠</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("钠(毫克)", tmp);
		} else {
			jsonValue.put("钠(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=铁</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("铁</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("铁(毫克)", tmp);
		} else {
			jsonValue.put("铁(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=钙</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("钙</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("钙(毫克)", tmp);
		} else {
			jsonValue.put("钙(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=叶酸</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("叶酸</a>（微克）</span><em>", "").replace("</em>", "");
			jsonValue.put("叶酸(微克)", tmp);
		} else {
			jsonValue.put("叶酸(微克)", "0");
		}

		pat = Pattern.compile("(?=维生素E</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素E</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素E(毫克)", tmp);
		} else {
			jsonValue.put("维生素E(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=脂肪</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("脂肪</a>（克）</span><em>", "").replace("</em>", "");
			jsonValue.put("脂肪", tmp);
		} else {
			jsonValue.put("脂肪", "0");
		}	
		
		pat = Pattern.compile("(?=钾</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("钾</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("钾(毫克)", tmp);
		} else {
			jsonValue.put("钾(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=镁</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("镁</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("镁(毫克)", tmp);
		} else {
			jsonValue.put("镁(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=烟酸</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("烟酸</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("烟酸(毫克)", tmp);
		} else {
			jsonValue.put("烟酸(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素B1</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素B1</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素B1(毫克)", tmp);
		} else {
			jsonValue.put("维生素B1(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素B12</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素B12</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素B12(毫克)", tmp);
		} else {
			jsonValue.put("维生素B12(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素B2</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素B2</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素B2(毫克)", tmp);
		} else {
			jsonValue.put("维生素B2(毫克)", "0");
		}	
			
		pat = Pattern.compile("(?=锌</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("锌</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("锌(毫克)", tmp);
		} else {
			jsonValue.put("锌(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=磷</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("磷</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("磷(毫克)", tmp);
		} else {
			jsonValue.put("磷(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=碘</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("碘</a>（微克）</span><em>", "").replace("</em>", "");
			jsonValue.put("碘(微克)", tmp);
		} else {
			jsonValue.put("碘(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=铜</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("铜</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("铜(毫克)", tmp);
		} else {
			jsonValue.put("铜(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=硒</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("硒</a>（微克）</span><em>", "").replace("</em>", "");
			jsonValue.put("硒(微克)", tmp);
		} else {
			jsonValue.put("硒(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=锰</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("锰</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("锰(毫克)", tmp);
		} else {
			jsonValue.put("锰(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素B6</a>)(.*?)(?=</em>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素B6</a>（毫克）</span><em>", "").replace("</em>", "");
			jsonValue.put("维生素B6(毫克)", tmp);
		} else {
			jsonValue.put("维生素B6(毫克)", "0");
		}	
		
		System.out.println("营养价值表:"+jsonValue.toString());
		
		System.out.println("开始解析食物搭配表====");
		pat = Pattern.compile("http://www.xiangha.com/xiangke/[^\"]{1,7}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			urlTmp = mat.group().replace("\"", "");
			utilParam2.szUrl = urlTmp;
			strRet = GetPostUtil.getNet(utilParam2);
		}

		// 解析食物相利相克表 搭配宜忌 <strong
		String dapei = "";
		JSONObject jsonBadCollo = new JSONObject();
		JSONObject jsonGoodCollo = new JSONObject();
		if (strRet.contains("相克</span>") && strRet.contains("宜搭")) {

			pat = Pattern.compile("(?=相克</span>)([\\s\\S]*?)(?=<th)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("<th", "").replace("相克", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("："));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("：")+1);
					jsonBadCollo.put(name, bad_fun);
				}
			}
			
			pat = Pattern.compile("(?=宜搭)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("宜搭", "").replace("查看菜谱&gt;&gt;", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("："));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("：")+1);
					jsonGoodCollo.put(name, bad_fun);
				}
			}
		} else if (strRet.contains("相克</span>") && !strRet.contains("宜搭")) {
			pat = Pattern.compile("(?=相克</span>)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("相克", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("："));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("：")+1);
					jsonBadCollo.put(name, bad_fun);
				}
			}
		} else if (!strRet.contains("相克</span>") && strRet.contains("宜搭")) {
			pat = Pattern.compile("(?=宜搭)([\\s\\S]*?)(?=</table>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				String tmp = mat.group().replace("宜搭", "").replace("查看菜谱&gt;&gt;", "").replace("</tr><tr>", "--");
				tmp = Html2Text(tmp);
				String[] tmpList = tmp.split("--");
				for (int i =0; i< tmpList.length; ++i) {
					String name = tmpList[i];
					name = name.substring(name.indexOf("+")+1,
							name.indexOf("："));
					String bad_fun = tmpList[i].substring(tmpList[i].indexOf("：")+1);
					jsonGoodCollo.put(name, bad_fun);
				}
			}
		}
		
		System.out.println("菜品类型:"+ dishType);
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// 将菜品基本数据进行入库
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_dish2(name,bodyType,introduction,nutrition,effect,man_suit"+
					",man_unsuit,save,how_select,image,value,collocation_bad, collocation_good,dishType)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, bodyType);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, food_save);
			stmt.setString(9, food_select);
			stmt.setString(10, imageUrl);
			stmt.setString(11, jsonValue.toString());
			stmt.setString(12, jsonBadCollo.toString());
			stmt.setString(13, jsonGoodCollo.toString());
			stmt.setString(14, dishType);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}

	private void getNetData(String url, String type) {

		// 访问网络 下载数据
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);
		
		// 菜品名 功能介绍 营养价值
		String food_name = null, food_info = null, food_nutrition = null;
		// 功效 适宜人群 禁忌人群
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;
	

		// 正则匹配
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		} else 
			return;
				
		String dishType = null;
		pat = Pattern.compile(">[^<]{1,10}</a> &gt;  ");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			dishType = mat.group().replace("</a> &gt;  ", "").replace(">", "");
		}

		pat = Pattern.compile("<dd>[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = food_info.replace("<dd>", "").replace("<", "");
		}

		pat = Pattern.compile("主要营养：</b><p>[^<]{1,100}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = food_nutrition.replace("主要营养：</b><p>", "").replace("<", "");
		}

		pat = Pattern.compile("食疗功效：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = food_effect.replace("食疗功效：</b><p>", "").replace("<", "");
		}	

		pat = Pattern.compile("适宜人群：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("适宜人群：</b><p>", "").replace("<", "");
		}		

		pat = Pattern.compile("禁忌人群：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("禁忌人群：</b><p>", "").replace("<", "");
		}	

		// 营养和功效 食用注意 如何挑选 如何保存
		String food_fun = null, food_notice=null, food_select=null;
		String food_save = null;
		
		// 匹配出营养和功效的内容
		int n = strRet.indexOf("<p class=\"icoli1\">");
		int m = strRet.indexOf("bgc4");
		if (n != -1 && m!= -1) {
			food_fun = strRet.substring(n, m);
			food_fun = food_fun.replace("<h2 class=", "");
			food_fun = Html2Text(food_fun);
		}

		// 匹配出注意事项
		n = strRet.indexOf("<p class=\"icoli2\">");
		m = strRet.indexOf("bgc6");
		if (n != -1 && m!= -1) {
		
			food_notice = strRet.substring(n, m);
			food_notice = food_notice.replace("<h2 class=", "");
			food_notice = Html2Text(food_notice);
		}
		// 如何挑选物品
		pat = Pattern.compile("</h2></div><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_select= mat.group();
			food_select = food_select.replace("</h2></div><p>", "").replace("<", "");
			
		}
	
		// 如何保存物品
		pat = Pattern.compile("</h2></div><p>[^<]{1,200}<");
		mat = pat.matcher(strRet.substring(strRet.indexOf("</h2></div><p>")+ 10));
		if (mat.find()) {
			food_save= mat.group();
			food_save = food_save.replace("</h2></div><p>", "").replace("<", "");
			
		}
		
		// 找到食物图片链接 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.cnys.com/upload/thumb/[^\"]{1,50}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");
			
			// 开始下载图片 保存到本地 不保存到数据库
			// 图片名为: md5(食物名)
			String dirPath = IndexServletGroup.getRealPath() + "/dish";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();
			String imgName = photo_url.substring(photo_url.lastIndexOf("/")+1, photo_url.lastIndexOf(".")-1);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServletGroup.getRealPath(), "");
		}
		
		// 匹配出食物营养价值表 使用Json来存储数据
		JSONObject json = new JSONObject();
		// 蛋白质
		pat = Pattern.compile("蛋白质</a> <i>([^克]{1,10}克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("蛋白质</a> <i>(", "").replace("克)", "");
			json.put("蛋白质", tmp);
		} else {
			json.put("蛋白质", "0");
		}
		
		pat = Pattern.compile("碳水化合物</a> <i>([^克]{1,10}克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("碳水化合物</a> <i>(", "").replace("克)", "");
			json.put("碳水化合物", tmp);
		} else {
			json.put("碳水化合物", "0");
		}
		
		pat = Pattern.compile("膳食纤维</a> <i>([^克]{1,10}克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("膳食纤维</a> <i>(", "").replace("克)", "");
			json.put("膳食纤维", tmp);
		} else {
			json.put("膳食纤维", "0");
		}	
		
		pat = Pattern.compile("胡萝卜素</a> <i>([^微]{1,10}微克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("胡萝卜素</a> <i>(", "").replace("微克)", "");
			json.put("胡萝卜素", tmp);
		} else {
			json.put("胡萝卜素", "0");
		}	
		
		pat = Pattern.compile("维生素A</a> <i>([^微]{1,10}微克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素A</a> <i>(", "").replace("微克)", "");
			json.put("维生素A", tmp);
		} else {
			json.put("维生素A", "0");
		}	
		
		pat = Pattern.compile("维生素C</a> <i>([^毫]{1,10}毫克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素C</a> <i>(", "").replace("毫克)", "");
			json.put("维生素C", tmp);
		} else {
			json.put("维生素C", "0");
		}	
		
		pat = Pattern.compile("钠</a> <i>([^毫]{1,10}毫克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("钠</a> <i>(", "").replace("毫克)", "");
			json.put("钠", tmp);
		} else {
			json.put("钠", "0");
		}	
		
		pat = Pattern.compile("铁</a> <i>([^毫]{1,10}毫克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("铁</a> <i>(", "").replace("毫克)", "");
			json.put("铁", tmp);
		} else {
			json.put("铁", "0");
		}	
		
		pat = Pattern.compile("钙</a> <i>([^毫]{1,10}毫克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("钙</a> <i>(", "").replace("毫克)", "");
			json.put("钙", tmp);
		} else {
			json.put("钙", "0");
		}	
		
		pat = Pattern.compile("热量</a> <i>([^千]{1,10}千卡)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("热量</a> <i>(", "").replace("千卡)", "");
			json.put("热量", tmp);
		} else {
			json.put("热量", "0");
		}
		// 脂肪
		pat = Pattern.compile("维生素E</a> <i>([^毫]{1,10}毫克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("维生素E</a> <i>(", "").replace("毫克)", "");
			json.put("维生素E", tmp);
		} else {
			json.put("维生素E", "0");
		}	
		
		pat = Pattern.compile("脂肪</a> <i>([^克]{1,10}克)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = tmp.replace("脂肪</a> <i>(", "").replace("克)", "");
			json.put("脂肪", tmp);
		} else {
			json.put("脂肪", "0");
		}	
		
		// 获取食物相克表 json数据保存 保存格式 food---reson&imageUrl;
		JSONObject jsonColloGood = new JSONObject();
		n = strRet.indexOf("icoyes");
		m = strRet.indexOf("icono");
		if (m == -1)
			m = strRet.lastIndexOf("clear20");
		String strTmp = null;
		if (m != -1 && n!= -1)
			strTmp = strRet.substring(n, m);
		if (strTmp != null) {

			pat = Pattern.compile("/upload/baike/[^\"]{1,50}\"");
			mat = pat.matcher(strTmp);
			String tmp2 = strTmp, tmp3 = strTmp;
			Pattern pat2 = Pattern.compile("和[^一]{1,20}一起吃");
			Matcher mat2 = pat2.matcher(tmp2);
			Pattern pat3 = Pattern.compile("一起吃：[^<]{1,100}</p>");
			Matcher mat3 = pat3.matcher(tmp3);
			
			String dirPath = IndexServletGroup.getRealPath() + "/minidish";
			File file = new File (dirPath);
			if (file.exists() == false)
				file.mkdir();
			
			while (mat.find() && mat2.find() && mat3.find()) {

				String imgUrl = "http://www.cnys.com"+mat.group().replace("\"", "");
				String foodNam = mat2.group().replace("和", "").replace("一起吃", "");
				// 下载网络图片 baike
				String imgName = imgUrl.substring(imgUrl.lastIndexOf("/")+1, imgUrl.lastIndexOf(".")-1);
				imgUrl =  GetPostUtil.downloadImage(imgUrl, imgName, dirPath + "/");
				imgUrl = imgUrl.replace(IndexServletGroup.getRealPath(), "");
	
				String fun = mat3.group().replace("一起吃：", "").replace("</p>", "");
				fun = Html2Text(fun);
				jsonColloGood.put(foodNam,new String[]{imgUrl, fun});
			}
		}

		JSONObject jsonColloBad = new JSONObject();
		n = strRet.indexOf("icono");
		m = strRet.lastIndexOf("clear20");
		strTmp = null;
		if (n != -1 && m!= -1)
			strTmp = strRet.substring(n, m);
		if (strTmp != null) {

			pat = Pattern.compile("/upload/baike/[^\"]{1,50}\"");
			mat = pat.matcher(strTmp);
			String tmp2 = strTmp, tmp3 = strTmp;
			Pattern pat2 = Pattern.compile("和[^一]{1,20}一起吃");
			Matcher mat2 = pat2.matcher(tmp2);
			Pattern pat3 = Pattern.compile("一起吃：[^<]{1,500}</p>");
			Matcher mat3 = pat3.matcher(tmp3);
			String dirPath = IndexServletGroup.getRealPath() + "/minidish";
			
			while (mat.find() && mat2.find() && mat3.find()) {
			
				String imgUrl = "http://www.cnys.com"+mat.group().replace("\"", "");
				String foodNam = mat2.group().replace("和", "").replace("一起吃", "");
				// 下载网络图片 baike
				String imgName = imgUrl.substring(imgUrl.lastIndexOf("/")+1, imgUrl.lastIndexOf(".")-1);
				imgUrl =  GetPostUtil.downloadImage(imgUrl, imgName, dirPath + "/");
				imgUrl = imgUrl.replace(IndexServletGroup.getRealPath(), "");
	
				String fun = mat3.group().replace("一起吃：", "").replace("", "");
				fun = Html2Text(fun);
				jsonColloBad.put(foodNam,new String[]{imgUrl, fun});
			}
		}
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// 将菜品基本数据进行入库
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_dish(name,bodyType,introduction,nutrition,effect,man_suit"+
					",man_unsuit,fun,notice,save,how_select,image,value,collocation_good,collocation_bad,dishType,url)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, type);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, food_fun);
			stmt.setString(9, food_notice);
			stmt.setString(10, food_save);
			stmt.setString(11, food_select);
			stmt.setString(12, imageUrl);
			stmt.setString(13, json.toString());
			stmt.setString(14, jsonColloGood.toString());
			stmt.setString(15, jsonColloBad.toString());
			stmt.setString(16, dishType);
			stmt.setString(17, url);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
		/*
		// 匹配出菜谱链接 并进入下载
		n = strRet.indexOf("<ul class=\"cai700\">");
		m = strRet.indexOf("<div class=\"ban700 b700_09\"");
		if (n != -1 && m!= -1) {
			String tmp = strRet.substring(n, m);

			pat = Pattern.compile("http://www.cnys.com/caipu/[^\"]{1,100}\"");
			mat = pat.matcher(tmp);
			while (mat.find()) {
				String urlDes = mat.group().replace("\"", "");
				downloadMenu(urlDes, type, url);
			}
		}*/
	}

	public static void downloadMenu(String url, String type, String preUrl) {
	
		// 下载菜谱
		// 访问网络 下载数据
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// 菜品名 功能介绍 营养价值
		String food_name = null, food_info = null, food_nutrition = null;
		// 功效 适宜人群 禁忌人群
		String food_effect = null, food_man_suit = null, food_man_unsuit = null;

		// 正则匹配
		Pattern pat = Pattern.compile("<h1>[^<]{1,20}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("<h1>", "").replace("<", "");
		}

		pat = Pattern.compile("<p>[^<]{1,300}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_info = mat.group();
			food_info = food_info.replace("<p>", "").replace("<", "");
		}

		pat = Pattern.compile("营养价值：</b><p>[^<]{1,100}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_nutrition = mat.group();
			food_nutrition = food_nutrition.replace("营养价值：</b><p>", "").replace("<", "");
		}

		pat = Pattern.compile("食疗功效：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = food_effect.replace("食疗功效：</b><p>", "").replace("<", "");
		}	

		pat = Pattern.compile("适宜人群：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_suit = mat.group();
			food_man_suit = food_man_suit.replace("适宜人群：</b><p>", "").replace("<", "");
		}		

		pat = Pattern.compile("禁忌人群：</b><p>[^<]{1,200}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_man_unsuit = mat.group();
			food_man_unsuit = food_man_unsuit.replace("禁忌人群：</b><p>", "").replace("<", "");
		}	

		// 功效和作用
		int n = strRet.indexOf("<p class=\"icoli1\">");
		int m = strRet.indexOf("bgc2");
		String food_fun  = null;
		if (n != -1 && m!= -1) {
			food_fun = strRet.substring(n, m);
			food_fun = food_fun.replace("<h2 class=\"", "");
			food_fun = Html2Text(food_fun);
		}
		// 做法
		n = strRet.indexOf("<p class=\"icoli2\">");
		m = strRet.indexOf("bgc3");
		String how_make  = null;
		if (n != -1 && m!= -1) {
			how_make = strRet.substring(n, m);
			how_make = how_make.replace("<h2 class=\"", "");
			how_make = Html2Text(how_make);
		}
		// 所需要的材料 主要食材 辅助佐料
		String material_main = null, material_assist = null;
		n = strRet.indexOf("主要食材");
		m = strRet.indexOf("辅助佐料"); // bgc4
		if ( m== -1)
			m = strRet.indexOf("bgc4");
		material_main = strRet.substring(n, m).replace("<h2 class=\"", "");
		material_main = Html2Text(material_main).replace("主要食材、", "").replace(")", ")  ");
		
		if (-1 != strRet.indexOf("辅助佐料")) {
			
			n = m;
			m = strRet.indexOf("bgc4");
			material_assist = strRet.substring(n, m).replace("<h2 class=\"", "");;
			material_assist = Html2Text(material_assist).replace("辅助佐料、", "").replace(")", ")  ");
		}
		
		// 菜谱图片
		// 找到食物图片链接 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.cnys.com/upload/thumb/[^\"]{1,50}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");
			
			// 开始下载图片 保存到本地 不保存到数据库
			// 图片名为: 原链接名
			String dirPath = IndexServletGroup.getRealPath() + "/menu";
			File file = new File(dirPath);
			if (file.exists() == false)
				file.mkdir();
			
			String imgName = photo_url.substring(photo_url.lastIndexOf("/"), photo_url.lastIndexOf(".")-1);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServletGroup.getRealPath(), "");
		}
		// 存入数据库
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into resource_food_menu(name,bodyType,introduction,nutrition,"
					+"effect,man_suit,man_unsuit,material_main,material_assist,how_make,"
					+"fun,image) values(?,?,?,?,?,?,?,?,?,?,?,?);");
			stmt.setString(1, food_name);
			stmt.setString(2, type);
			stmt.setString(3, food_info);
			stmt.setString(4, food_nutrition);
			stmt.setString(5, food_effect);
			stmt.setString(6, food_man_suit);
			stmt.setString(7, food_man_unsuit);
			stmt.setString(8, material_main);
			stmt.setString(9, material_assist);
			stmt.setString(10, how_make);
			stmt.setString(11, food_fun);
			stmt.setString(12, imageUrl);
			stmt.executeUpdate();
			sql.closeSql();
		} catch(SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}

	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		htmlStr = htmlStr.replace("<br/>", "\n");
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			// 优先匹配 <strong> </span>
			p_script = Pattern.compile("</strong>");
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("");

			p_script = Pattern.compile("</span>");
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll("、");

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr.replace("\t", "").replace("\n", "").replace("&nbsp;", " ");
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}
	
	static void addLog(String log) {
		
		PublicData.gLogError += log +"<br>";
	}
	
	
	private void getMenuData(String url, String type) {
		System.out.println("url："+ url);
		// 访问网络 下载数据
		GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
		utilParam.szUrl = url;
		String strRet = GetPostUtil.getNet(utilParam);

		// 菜品名 功能介绍 营养价值
		String food_name = null;
		// 功效 适宜人群 禁忌人群
		String food_effect = null;
	
		// 正则匹配"
		Pattern pat = Pattern.compile("foodName\">[^<]{1,30}<");
		Matcher mat = pat.matcher(strRet);
		if (mat.find()) {
			food_name = mat.group();
			food_name = food_name.replace("foodName\">", "").replace("<", "");
		} else 
			return;
		String dishType = type;

		pat = Pattern.compile("(?=功效</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			food_effect = mat.group();
			food_effect = Html2Text(food_effect).replace("功效", "");
			if(food_effect.contains("暂无"))
				food_effect= "";
		}	

		String main_food = "", assist_food="";
		pat = Pattern.compile("(?=主料</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			main_food = mat.group();
			main_food = Html2Text(main_food).replace("主料", "");
		}	

		pat = Pattern.compile("(?=调料</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			assist_food = mat.group();
			assist_food = Html2Text(assist_food).replace("调料", "");
		}	
		
		if (assist_food==""&&main_food == "") {
			pat = Pattern.compile("(?=原料</h3>)([\\s\\S]*?)(?=</div>)");
			mat = pat.matcher(strRet);
			if (mat.find()) {
				main_food = mat.group();
				main_food = Html2Text(main_food).replace("原料", "");
			}	
			
		}
		
		String how_make = "";
		pat = Pattern.compile("(?=做法</h3>)([\\s\\S]*?)(?=</div>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			how_make = mat.group();
			how_make = Html2Text(how_make).replace("做法", "");
		}	
		
		String flavor =""; // 口味
		pat = Pattern.compile("口味：[^<]{1,20}<");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			flavor = mat.group();
			flavor = flavor.replace("口味：", "").replace("<", "");
		}	
		
		System.out.println("名字:"+ food_name);
		System.out.println("效果:" + food_effect);
		System.out.println("主料:"+ main_food);
		System.out.println("调料:" + assist_food);
		System.out.println("做法:" + how_make);	
		System.out.println("口味:"+ flavor);
		
		// 找到食物图片链接 
		String imageUrl = null;
		String photo_url = null;
		pat = Pattern.compile("http://img.ilinkee.com/img/images/iLinkeeImage_mid/[^\"]{1,100}\"");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			photo_url = mat.group();
			photo_url = photo_url.replace("\"", "");      

			// 开始下载图片 保存到本地 不保存到数据库
			// 图片名为: md5(食物名)
			String dirPath = IndexServlet.getRealPath() + "/menu";
			File  file = new File(dirPath);
			if (file.exists() == false) 
				file.mkdir();		
			String imgName = EncodeAndDecode.md5(food_name);
			imageUrl = GetPostUtil.downloadImage(photo_url, imgName, dirPath+ "/");
			imageUrl = imageUrl.replace(IndexServlet.getRealPath(), "");
		}
		
		
		System.out.println("开始解析营养值====");
	
		// 匹配出食物营养价值表 使用Json来存储数据
		JSONObject jsonValue = new JSONObject();
		// 热量
		pat = Pattern.compile("(?=热量</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("热量</a>(大卡)", "")).replace(" ", "");
			jsonValue.put("热量(大卡)", tmp);
		} else {
			jsonValue.put("热量(大卡)", "0");
		}
		
		// 蛋白质
		pat = Pattern.compile("(?=蛋白质)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("蛋白质</a>(克)", "")).replace(" ", "");
			jsonValue.put("蛋白质(克)", tmp);
		} else {
			jsonValue.put("蛋白质(克)", "0");
		}
		
		pat = Pattern.compile("(?=碳水化合物</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("碳水化合物</a>(克)", "")).replace(" ", "");
			jsonValue.put("碳水化合物(克)", tmp);
		} else {
			jsonValue.put("碳水化合物(克)", "0");
		}
		
		pat = Pattern.compile("(?=膳食纤维</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("膳食纤维</a>(克)", "")).replace(" ", "");
			jsonValue.put("膳食纤维(克)", tmp);
		} else {
			jsonValue.put("膳食纤维(克)", "0");
		}	
		
		pat = Pattern.compile("(?=胡萝卜素</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("胡萝卜素</a>(微克)", "")).replace(" ", "");
			jsonValue.put("胡萝卜素(微克)", tmp);
		} else {
			jsonValue.put("胡萝卜素(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素A</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("维生素A</a>(微克)", "")).replace(" ", "");
			jsonValue.put("维生素A(微克)", tmp);
		} else {
			jsonValue.put("维生素A(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=维生素C</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("维生素C</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("维生素C", tmp);
		} else {
			jsonValue.put("维生素C", "0");
		}	
		
		pat = Pattern.compile("(?=钠</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("钠</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("钠(毫克)", tmp);
		} else {
			jsonValue.put("钠(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=铁</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("铁</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("铁(毫克)", tmp);
		} else {
			jsonValue.put("铁(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=钙</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("钙</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("钙(毫克)", tmp);
		} else {
			jsonValue.put("钙(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=叶酸</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("叶酸</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("叶酸(微克)", tmp);
		} else {
			jsonValue.put("叶酸(微克)", "0");
		}

		pat = Pattern.compile("(?=维生素E</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("维生素E</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("维生素E(毫克)", tmp);
		} else {
			jsonValue.put("维生素E(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=脂肪</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("脂肪</a>(克)", "")).replace(" ", "");
			jsonValue.put("脂肪", tmp);
		} else {
			jsonValue.put("脂肪", "0");
		}	
		
		pat = Pattern.compile("(?=钾</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("钾</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("钾(毫克)", tmp);
		} else {
			jsonValue.put("钾(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=镁</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("镁</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("镁(毫克)", tmp);
		} else {
			jsonValue.put("镁(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=烟酸</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("烟酸</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("烟酸(毫克)", tmp);
		} else {
			jsonValue.put("烟酸(毫克)", "0");
		}			
			
		pat = Pattern.compile("(?=锌</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("锌</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("锌(毫克)", tmp);
		} else {
			jsonValue.put("锌(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=磷</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("磷</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("磷(毫克)", tmp);
		} else {
			jsonValue.put("磷(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=碘</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("碘</a>(微克)", "")).replace(" ", "");
			jsonValue.put("碘(微克)", tmp);
		} else {
			jsonValue.put("碘(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=铜</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("铜</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("铜(毫克)", tmp);
		} else {
			jsonValue.put("铜(毫克)", "0");
		}	
		
		pat = Pattern.compile("(?=硒</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("硒</a>(微克)", "")).replace(" ", "");
			jsonValue.put("硒(微克)", tmp);
		} else {
			jsonValue.put("硒(微克)", "0");
		}	
		
		pat = Pattern.compile("(?=锰</a>)([\\s\\S]*?)(?=</tr>)");
		mat = pat.matcher(strRet);
		if (mat.find()) {
			String tmp = mat.group();
			tmp = Html2Text(tmp.replace("锰</a>(毫克)", "")).replace(" ", "");
			jsonValue.put("锰(毫克)", tmp);
		} else {
			jsonValue.put("锰(毫克)", "0");
		}	
		
		System.out.println("营养价值表:"+jsonValue.toString());
		System.out.println("菜品类型:"+ dishType);
		
		SqlDeal m_SqlDeal = new SqlDeal();
		// 将菜品基本数据进行入库
		try {
			
			PreparedStatement stm = m_SqlDeal.getConnection().prepareStatement("select id from resource_food_dish where name=?;");
			stm.setString(1, food_name);
			ResultSet res = stm.executeQuery();
			if (res.getRow() != 0)
				return;
			
			PreparedStatement stmt = m_SqlDeal.getConnection().prepareStatement(
					"insert into resource_food_menu2(name, material_main,material_assist, effect, value, how_make,image, flavor)"
					+ " values(?,?,?,?,?,?,?,?)");
			stmt.setString(1, food_name);
			stmt.setString(2, main_food);
			stmt.setString(3, assist_food);
			stmt.setString(4, food_effect);
			stmt.setString(5, jsonValue.toString());
			stmt.setString(6, how_make);
			stmt.setString(7, imageUrl);
			stmt.setString(8, flavor);
			stmt.executeUpdate();
			m_SqlDeal.closeSql();
		}catch (SQLException e) {
			e.printStackTrace();
			addLog(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
}








