package com.cloudlife.food;

import java.sql.ResultSet;
import java.util.List;

/**
 * 食品类数据库交互处理接口类
 * 
 * @author wuyi
 *  
 */
public interface IFoodSql  {
	
	/**
	 * @brief 根据用户的体质查找有利的菜品
	 * @param type user的体质类型
	 * @return 返回查询到的数据内容
	 */
	ResultSet getDishDataByBodyType(String type);
	
	/**
	 * @brief 根据用户的体质查找有利的菜品 
	 * @param type user的体质类型
	 * @return 返回查询到的数据内容 作为算法数据来源
	 */
	List<DishData> getDishDataToCmpByBodyType(String type);
	
	/**
	 * @brief 根据食品的类型查找
	 * @param type 食品类型  如 蔬菜类/水果类 等
	 * @return 返回查询到的数据内容
	 */
	List<DishData> getDishDataByFoodType(String type);
	
	/**
	 * @brief 根据食品的类型查找
	 * @param type 食品类型  如 蔬菜类/水果类 等
	 * @return 返回查询到的数据内容
	 */
	List<DishData> getMenuDataByFoodType(String type);
	/**
	 * @brief 根据菜品名找到相关的菜谱
	 * @param name 菜品名
	 * @return 返回查询到的数据内容
	 */
	ResultSet getMenuDataByDishName(String name);
	
	/**
	 * @brief 查找某菜品的详细信息
	 * @param name 菜品名
	 * @return 返回查询到的数据内容
	 */
	ResultSet getDishDataByName(String name);
	
	
}
