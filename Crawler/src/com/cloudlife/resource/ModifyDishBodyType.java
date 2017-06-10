package com.cloudlife.resource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloudlife.db.SqlDeal;

public class ModifyDishBodyType {

	static private ModifyDishBodyType modify;
	private int isWorking = 0;
	
	
	private String[] data = new String[]{"",
			"山药 黄豆 红薯 土豆 豌豆 芋头 香菇 扁豆 糯米 莲子 白果 南瓜 包心菜 胡萝卜 蚕豆 莲藕 豆腐 鸡肉 猪肚 牛肉 兔肉 羊肉 淡水鱼 黄鱼 比目鱼 刀鱼 泥鳅 黄鳝 葡萄干 苹果 菱角 龙眼肉 橙子"
			,"银耳 番茄 菠菜 白菜花 苦瓜 百合 绿豆 大白菜 小白菜 荸荠 糯米 龟鳖 海参 鲍鱼 螃蟹 牛奶 牡蛎 海蜇 鸭肉 猪肉 猪皮 兔肉 豆腐 甘蔗 黑木耳",
			"刀豆 南瓜 韭菜 生姜 黄豆芽 椒类 茼蒿 荔枝 龙眼 榴莲 樱桃 杏 核桃 栗子 腰果 松子 洋葱 香菜 胡萝卜 山药 红茶 羊肉 牛肉 猪肚 鸡肉 带鱼 黄鳝 虾 海参 鲍鱼 淡菜"
			,"扁豆 蚕豆 赤豆 冬瓜 竹笋 橄榄 紫菜 大蒜 芥蓝 山药 薏米 花生 海蜇 胖头鱼 鲫鱼 鲤鱼 鲈鱼 羊肉 萝卜 洋葱 豆角 辣椒 咖喱 生姜",
			"莲藕 莲子 芹菜 茭白 生菜 黄瓜 四季豆 蚕豆 绿豆 鸭肉 兔肉 荸荠 鲫鱼 鲤鱼 田螺 海带 紫菜 冬瓜 线瓜 葫芦 苦瓜 菜瓜 西瓜 梨 绿茶 花茶 白菜 竹笋 空心菜 萝卜 豆角 绿豆 莴笋",
			"黄花菜 萝卜 丝瓜 西葫芦 乌塌菜 菊花 荞麦 高粱 刀豆 蘑菇 豆鼓 柑橘 柚子 香菜 包心菜 苦瓜 玫瑰 茉莉花 海带 海藻 山楂 龙眼 红枣 葡萄干 蛋黄"
			,"黑豆 黑木耳 胡萝卜 香菜 茴香 油菜 茄子 洋葱 大蒜 韭菜 生姜 黄豆 竹笋 山楂 番木瓜 螃蟹 海参 醋 黄酒 葡萄酒 糯米甜酒"};
	private String[] alpha= new String[]{"A", "B", "C","D","E","F","G","H","I"};
	
	static public ModifyDishBodyType getInstance() {
		if (modify==null)
			modify = new ModifyDishBodyType();
		return modify;
	}
	
	private ModifyDishBodyType() {
		
	}
	
	public void begin() {
	
		if (isWorking == 1)
			return ;
		isWorking =1;
		System.out.println("begin");
		SqlDeal sql = new SqlDeal();
		for (int i =1; i<8; ++i) {
			String[] food = data[i].split(" ");
			for (int j=0; j< food.length; ++j) {
				try {
					PreparedStatement stmt = sql.getConnection().prepareStatement(
							"select bodyType from resource_food_menu where name like ?;");
					stmt.setString(1, "%"+food[j]+"%");
					ResultSet set = stmt.executeQuery();
					if (set.first()) {
						String body = set.getString("bodyType");
						body = body+" "+ alpha[i];
						set.close();
						stmt = sql.getConnection().prepareStatement("update resource_food_menu set bodyType=? where name like ?;");
						stmt.setString(1, body);
						stmt.setString(2, "%"+food[j]+"%");
						stmt.execute();
						continue;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("任务结束");
		isWorking =0;
	}
}
