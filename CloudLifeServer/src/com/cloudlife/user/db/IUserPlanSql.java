package com.cloudlife.user.db;

/**
 *  @brief 用户计划数据库操作接口
 * 			实现以下接口:
 * 				1. 查询是否有已有计划内容 返回boolean
 * 				2. 获取用户当前的计划表
 * 				3. 导入新的计划内容
 * 			交互表包括: 饮食推荐表 运动推荐表 作息等个性化表
 * @author wuyi
 *  
 */

public interface IUserPlanSql {

	/**
	 * @brief 查询用户的饮食计划表是否已经存在
	 * 		如果不存在 则创建新的推荐计划
	 * @param phone 用户的手机号码 唯一标识符
	 * @return 返回今日计划
	 */
	String isTodayDietPlanExist(String phone, int type);
	
	/**
	 * @brief 获取用户当天的饮食推荐评估数据
	 * @param phone
	 * @return
	 */
	String getTodayDietPlanEvaluate(String phone, int type);
	/**
	 * @brief 查询用户的运动计划表是否已经存在
	 * @param phone 用户的手机号码 唯一标识符
	 * @return 存在则返回true 否则返回false
	 */
	//boolean isExercisePlanExits(String phone);
	
	/**
	 * @brief 查询当前的运动推荐
	 * @param phone
	 * @return
	 */
	String isTodaySportPlanExits(String phone);

}




