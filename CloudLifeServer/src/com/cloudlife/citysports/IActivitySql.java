package com.cloudlife.citysports;

/**
 * @brief 活动相关的数据接口
 * @author wuyi
 *
 */
public interface IActivitySql {
	
	// 更新用户的经纬度信息
	boolean updateLocation(String uid, String location_name);
	
	/**
	 * 
	 * @param title 活动的标题
	 * @param lng  活动地点的经度
	 * @param lat  活动地点的纬度
	 * @param info 活动的简介
	 * @param time_begin 活动的开始时间
	 * @param time_build 活动的结束时间
	 * @param location_name 活动地点的名
	 * @param json_man_cnt 活动最多参与的人数
	 * @return 是否创建成功
	 */
	boolean bulidActivity(String uid, String title, float lng, float lat,
			String info, String time_begin,
			String location_name,String city);
	
	/**
	 * @brief 删除活动
	 * @param uid 用户的id
	 * @param aid 活动的id
	 * @return 是否删除成功
	 */
	boolean deleteActivity(String uid, String aid);
	
	/**
	 * @brief 更新活动的介绍
	 * @param uid 用户id
	 * @param aid 活动id
	 * @param info 新的简介
	 * @return 是否更新成功
	 */
	boolean updateActivityInfo(String uid, String aid, String info);
	
	/**
	 * @brief 获取同城市的所有用户的地理位置信息
	 * @param city 城市名
	 * @return json打包的数据信息
	 */
	String getSameCityUser(String city);
	
	/**
	 * @brief 获取某城市的所有活动
	 * @param city 城市名
	 * @return json打包的数据信息
	 */
	String getSameCityActivitys(String city);	
	
	/**
	 * @brief 活动某用户创建的所有活动
	 * @param uid 用户的id
	 * @return json打包的数据信息
	 */
	String getUserActivitys(String uid);
	
	/**
	 * @brief 活动指定活动的详细内容
	 * @param aid 活动的id
	 * @return
	 */
	String getActivityDetailData(String aid);
	
	/**
	 * @brief 用户加入某个活动
	 * @param uid 用户id
	 * @param aid 活动id
	 * @return
	 */
	boolean joinActivity(String uid, String aid);
}













