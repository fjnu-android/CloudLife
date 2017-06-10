package com.cloudlife.resource;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.util.GetPostUtil;

public class GetDishBodyType {

	private static GetDishBodyType get;
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
	
	
	static public GetDishBodyType getInstance() {
		if (get == null)
			get = new GetDishBodyType();
		return get;
	}
	
	private GetDishBodyType() {
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

					SqlDeal sql = new SqlDeal();
					try {
						PreparedStatement stmt = sql.getConnection().prepareStatement(
								"select name from resource_food_dish where bodyType='' and dishType='鱼类'");
						ResultSet set = stmt.executeQuery();
						List<String> names = new ArrayList<String>();
						while (set.next()) {
							names.add(set.getString("name"));
							System.out.println("name:"+set.getString("name"));
						}
						set.close();
						for (int i =0; i< names.size(); ++i) {
							
							String urlTmp = "http://www.meishij.net/";
							try {
								urlTmp = "http://www.meishij.net/"+  java.net.URLEncoder.encode(names.get(i),"utf-8");
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							} 
							GetPostUtil.UtilParam utilParam2 = new GetPostUtil().new UtilParam();
							utilParam2.szUrl = urlTmp;
							String strRet2 = GetPostUtil.getNet(utilParam2);
							// 获取体质类型 写法：yi_  ji_
							// 采用循环匹配法
							String bodyTypeTmp = "";
							Pattern pat = Pattern.compile("(?=sc_header_con2)([\\s\\S]*?)(?=</ul>)");
							Matcher mat = pat.matcher(strRet2);
							String bodyType = "";
							if (mat.find()) {
								bodyTypeTmp = mat.group();
								for (int j =0; j< bodyTypes.length; ++j) {
									if (strRet2.contains(bodyTypes[j])) {
										bodyType +=" "+bodyTypeAsw[j];
									}
								}
								System.out.println("name:"+ names.get(i)+" bodyType:"+ bodyType);
								stmt = sql.getConnection().prepareStatement(
										"update resource_food_dish set bodyType=? where name=?");
								stmt.setString(1, bodyType);
								stmt.setString(2, names.get(i));
								stmt.execute();
							}
						}
						break;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	
	void getHowEatData() {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select name from resource_food_dish where how_eat='' and dishType='蔬菜'");
			ResultSet set = stmt.executeQuery();
			List<String> names = new ArrayList<String>();
			System.out.println("正在判断====");
			while (set.next()) {
				names.add(set.getString("name"));
			}
			for (int i =0; i<names.size(); ++i) {
				
				String name = names.get(i);
				String name2 = name;
				System.out.println("name:"+ name);
				try {
					name = java.net.URLEncoder.encode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}  
				GetPostUtil.UtilParam utilParam = new GetPostUtil().new UtilParam();
				utilParam.szUrl = "http://www.xiangha.com/shicai/"+name;
				String strRet = GetPostUtil.getNet(utilParam);
				////////////////////
				String food_tip = "";
				Pattern pat = Pattern.compile("(?=食用方法</h2>)([\\s\\S]*?)(?=</p>)");
				Matcher mat = pat.matcher(strRet);
				if (mat.find()) {
					food_tip = mat.group();
					food_tip = GetResource.Html2Text(food_tip).replace("食用方法", "");;
				}
				stmt = sql.getConnection().prepareStatement(
						"update resource_food_dish set how_eat=? where name=?");
				stmt.setString(1, food_tip);
				stmt.setString(2, name2);
				System.out.println(stmt.execute());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}










