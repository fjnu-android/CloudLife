package com.cloudlife.sport;

/**
 * @brief 运动推荐遗传算法之数据模型类
 * @author wuyi
 *
 */
public class UserModel {

	private int time = 60; // 用户的运动时长
	private float today_diet_power = 350; // 今日饮食获得的能量
	private int item_count = 3;
	private float weight = 60; //体重
	
	public int getItemCount() {
		return item_count;
	}
	
	public int getTotalTime() {
		return this.time;
	}
	
	public float getTotalPower() {
		return this.today_diet_power;
	}
	
	/**
	 * @brief 解析用户的个人信息 计算运动的时长 
	 * @param bodyType 用户的体质类型
	 * @param sex 用户的性别
	 * @param age 年龄
	 * @param weight 体重
	 * @param height 身高
	 * @param pal 体力活动水平
	 * @param place 今日的运动指数 适合室外还是市内运动 out in
	 * @return 是否解析成功
	 */
	public boolean resolveUserInfo(String bodyType, String sex, int age, 
			float weight, float height, String work, String place) {
		
		return true;
	}
	
	public float getWeight() {
		return this.weight;
	}
	
}













