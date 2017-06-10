package com.cloudlife.plan;

import java.util.List;

import com.cloudlife.food.DishData;
import com.cloudlife.food.FoodSqlImp;
import com.cloudlife.food.IFoodSql;

/**
 * @brief 饮食计划推荐生成算法
 * 
 * 			算法原理如下
 * 理论依据: 中国人的9种体质饮食推荐 + 外国营养学内容。
 * 		具体理论依据 书写在产品设计方案里。
 * 
 * 算法内容：
 * 
 * 		1. 首先 先确定今日要用来补的体制相应蔬菜或者肉
 * 		2. 
 * 
 * @author wuyi & zhang-han-yuan
 */


public class DietPlanAlgorithm {
	
	// 以下为个性化变量数据   根据国家发布的饮食标准 提供默认值
	
	private IFoodSql m_foodSql = new FoodSqlImp();
	// 午餐蔬菜的种数 和量
	private int m_nLunchVeg =0;
	private float m_fLunchVegKg = 0;
	// 午餐肉的种数  和量
	private int m_nLunchMeet =0;
	private float m_fLunchMeetKg = 0;
	// 午餐汤的种数
	private int m_nfLunchSoup = 0;
	// 晚餐肉的种数 和量
	private int m_nDinnerMeet = 0;
	private float m_fDinnerMeetKg = 0;
	// 晚餐蔬菜的种数 和量
	private int m_nDinnerVeg = 0;
	private float m_fDinnerVegKg = 0;
	// 晚餐汤的种数
	private int m_nDinnerSoup =0;
	
	// 用户信息 
	private float m_fUserHeight = 0;
	private float m_fUserWeight = 0;
	private int m_nUserAge = 0; 
	private String m_strUserBodyType = null;
	// 用户是否有啥疾病倾向
	private List<String> m_listUserDiease;
	// 用户想要通过饮食获得什么帮助  比如减肥 美容等等 （跟疾病有区别）
	private List<String> m_listUserWish;
	// 用户的工作性质    主要用户处理职业病问题
	private String m_strUserWork;
	// 用户所在城市
	private String m_strUserCity;
	
	// 待处理数据    每种菜对应一个分数  初始分为5分 根据相应筛选进行加减
	List<DishData> m_DealData;       // 体质搭配菜品
	List<DishData> m_DealDataAssist; // 配合类菜品
	
	
	/**
	 * @brief 第一次筛选算法   先根据用户体质获取相应菜品
	 * 		通过用户是否具有某种疾病  筛选掉(0分)那些菜品的禁忌人群参数中包含此疾病的
	 * @return booloean
	 */
	private boolean firstScreen() {
		m_DealData = m_foodSql.getDishDataToCmpByBodyType(m_strUserBodyType);
	
		for (int i = 0; i< m_DealData.size(); ++i) {
			for (int j =0; j< m_listUserDiease.size(); ++j) {
				String unsuit = m_DealData.get(i).getManUnSuit();
				if (unsuit.contains(m_listUserDiease.get(j))) {
					m_DealData.get(i).setScore(0);
				} 

				String suit = m_DealData.get(i).getManSuit();
				if (suit.contains(m_listUserDiease.get(j))) {
					m_DealData.get(i).addScore();
				}
			}
		}
		return true;
	}
	/**
	 * @brief 第二次筛选算法 
	 * 		依据： 匹配用户的需求 （比如 用户想要养颜 美容 瘦身 等等）
	 * @return boolean
	 */
	private boolean secondScreen() {
		for (int i =0; i< m_DealData.size(); ++i) {
			if (m_DealData.get(i).getScore() == 0)
				continue;
			for (int j=0; j< m_listUserWish.size(); ++j) {
				if (m_DealData.get(i).getEffect().contains(m_listUserWish.get(i)))
					m_DealData.get(i).addScore();
			}
		}
		return true;
	}
}






