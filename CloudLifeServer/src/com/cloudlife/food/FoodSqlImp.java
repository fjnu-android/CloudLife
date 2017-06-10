package com.cloudlife.food;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloudlife.db.SqlDeal;

/**
 * 食品类数据交互处理接口的具体实现类
 * 
 * @author wuyi
 *
 */
public class FoodSqlImp implements IFoodSql{

	@Override
	public ResultSet getDishDataByBodyType(String type) {
		
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_dish where locate(?, bodyType)>0;");
			stmt.setString(1, type);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<DishData> getDishDataByFoodType(String type) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_dish where dishType=?;");
			stmt.setString(1, type);
			ResultSet res = stmt.executeQuery();
			if (res.first()) {
				List<DishData> data = new ArrayList<DishData>();
				while(res.next()) {
					DishData tmp = new DishData(res.getString("name"),res.getString("dishType"),
							res.getString("effect"), res.getString("man_suit"), res.getString("man_unsuit"),
							res.getString("value"), res.getString("bodyType"), res.getString("image"));
					data.add(tmp);
				}
				res.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ResultSet getMenuDataByDishName(String name) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_menu where locate(?, name)>0;");
			stmt.setString(1, name);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ResultSet getDishDataByName(String name) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_menu where locate(?, name)>0;");
			stmt.setString(1, name);
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<DishData> getDishDataToCmpByBodyType(String type) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_dish where locate(?, bodyType)>0;");
			stmt.setString(1, type);
			ResultSet res = stmt.executeQuery();
			if (res.getRow()!= 0) {
				List<DishData> data = new ArrayList<DishData>();
				while(res.next()) {
					DishData tmp = new DishData(res.getString("name"),res.getString("dishType"),
							res.getString("effect"), res.getString("man_suit"), res.getString("man_unsuit"),
							res.getString("value"),res.getString("bodyType"), res.getString("image"));
					data.add(tmp);
				}
				res.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<DishData> getMenuDataByFoodType(String type) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt= null;
		try {
			stmt = sql.getConnection().prepareStatement("select *from resource_food_menu where menu_type=?;");
			stmt.setString(1, type);
			ResultSet res = stmt.executeQuery();
			if (res.first()) {
				List<DishData> data = new ArrayList<DishData>();
				while(res.next()) {
					DishData tmp = new DishData(res.getString("name"),res.getString("menu_type"),
							res.getString("effect"), "", "",res.getString("value"),
							res.getString("bodyType"), res.getString("image"));
					data.add(tmp);
				}
				res.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}




