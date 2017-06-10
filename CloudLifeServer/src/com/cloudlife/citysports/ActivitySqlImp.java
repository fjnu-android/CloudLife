package com.cloudlife.citysports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import com.cloudlife.db.SqlDeal;

/**
 * @brief IActivitySql接口的实现类
 * @author wuyi
 *
 */
public class ActivitySqlImp implements IActivitySql {

	@Override
	public boolean updateLocation(String uid, String location_name) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"update user_info set location_city=? where phone=?;");
			stmt.setString(1, location_name);
			stmt.setString(2, uid);
			int rst = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (rst==1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean bulidActivity(String uid, String title, float lng,
			float lat, String info, String time_begin,
			String location_name,String city) {
		SqlDeal sql = new SqlDeal();
		System.out.println("地点名："+location_name);
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date()).toString();// new Date()为获取当前系统时间
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into user_sport_city(title,lng,lat,location_name,time_begin,who_build,bulid_time, info,city)"
					+ " values(?,?,?,?,?,?,?,?,?);");
			stmt.setString(1, title);
			stmt.setFloat(2, lng);
			stmt.setFloat(3, lat);
			stmt.setString(4, location_name);
			stmt.setString(5, time_begin);
			stmt.setString(6, uid);
			stmt.setString(7, date);
			stmt.setString(8, info);
			stmt.setString(9, city);
			int rst = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (rst == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteActivity(String uid, String aid) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"delete from user_sport_city where sid=? and who_build=?;");
			stmt.setString(1, aid);
			stmt.setString(2, uid);
			int rst = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (rst ==1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateActivityInfo(String uid, String aid, String info) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"update user_sport_city set info=? where sid=? and who_build=?;");
			stmt.setString(1, info);
			stmt.setString(2, aid);
			stmt.setString(3, uid);
			int rst = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (rst == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getSameCityUser(String city) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select name, sign, location_city,icon_url from user_info where location_city=?;");
			stmt.setString(1, city);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			boolean has = false;
			while (set.next()) {
				has = true;
				JSONObject tmp = new JSONObject();
				tmp.put("name", set.getString("name"));
				tmp.put("loc_city", set.getString("location_city"));
				tmp.put("sign", set.getString("sign"));
				tmp.put("icon_url", set.getString("icon_url"));
				jsonRet.accumulate("user", tmp);
			}
			if (has)
				jsonRet.put("state", "1");
			else
				jsonRet.put("state", "0");
			
			set.close();
			stmt.close();
			sql.closeSql();
			return jsonRet.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{\"status\":\"-2\",\"msg\":\"服务器出现异常\"}";
	}

	@Override
	public String getSameCityActivitys(String city) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select *from user_sport_city where city=?;");
			stmt.setString(1, city);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			boolean has = false;
			while (set.next()) {
				has = true;
				JSONObject tmp = new JSONObject();
				tmp.put("aid", set.getInt("sid"));
				tmp.put("title", set.getString("title"));
				tmp.put("time", set.getString("time_begin"));
				tmp.put("locate", set.getString("location_name"));
				tmp.put("is_over", set.getBoolean("is_over"));
				tmp.put("join_num", set.getInt("join_cut_num"));
				jsonRet.accumulate("activity", tmp);
			}
			if (has)
				jsonRet.put("state", "1");
			else
				jsonRet.put("state", "0");
			set.close();
			stmt.close();
			sql.closeSql();
			
			return jsonRet.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{\"status\":\"-2\",\"msg\":\"服务器出现异常\"}";
	}

	@Override
	public String getUserActivitys(String uid) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select *from user_sport_city where who_build=?;");
			stmt.setString(1, uid);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			boolean has = false;
			while (set.next()) {
				has = true;
				JSONObject tmp = new JSONObject();
				tmp.put("aid", set.getInt("sid"));
				tmp.put("title", set.getString("title"));
				tmp.put("time", set.getString("time_begin"));
				tmp.put("locate", set.getString("location_name"));
				tmp.put("is_over", set.getBoolean("is_over"));
				tmp.put("join_num", set.getInt("join_cut_num"));
				jsonRet.accumulate("activity", tmp);
			}
			if (has)
				jsonRet.put("state", "1");
			else
				jsonRet.put("state", "0");
			set.close();
			stmt.close();
			sql.closeSql();
			
			return jsonRet.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{\"status\":\"-2\",\"msg\":\"服务器出现异常\"}";
	}

	@Override
	public String getActivityDetailData(String aid) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select *from user_sport_city where sid=?;");
			stmt.setString(1, aid);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			if (set.next()) {
				jsonRet.put("state", "1");
				jsonRet.put("aid", set.getInt("sid"));
				jsonRet.put("title", set.getString("title"));
				jsonRet.put("is_over", set.getBoolean("is_over"));
				jsonRet.put("join_num", set.getInt("join_cut_num"));
				jsonRet.put("lng", set.getFloat("lng"));
				jsonRet.put("lat", set.getFloat("lat"));
				jsonRet.put("city", set.getString("city"));
				jsonRet.put("info", set.getString("info"));
				jsonRet.put("time_begin", set.getString("time_begin"));
				jsonRet.put("time_build", set.getString("bulid_time"));
				jsonRet.put("loc_name", set.getString("location_name"));
				jsonRet.put("who_build", set.getString("who_build"));
				// 接下来是添加参加活动的用户的列表
				String tmpStr = set.getString("who_join");
				set.close();
				stmt.close();
				
				if (tmpStr != null) {
					String[] list = tmpStr.split(" ");
					
					for (int i =0; i< list.length; ++i) {
						if(list[i] == null || list[i]=="")
							continue;
						JSONObject tmp = new JSONObject();
						stmt = sql.getConnection().prepareStatement(
								"select name, icon_url,sign from user_info where phone=?;");
						stmt.setString(1,list[i]);
						set = stmt.executeQuery();
						if (set.first()) {
							tmp.put("name", set.getString("name"));
							tmp.put("sign", set.getString("sign"));
							tmp.put("icon_url", set.getString("icon_url"));
							jsonRet.accumulate("user", tmp);
						}
						set.close();
						stmt.close();
					}
				}
			} else
				jsonRet.put("state", "0");
			set.close();
			stmt.close();
			sql.closeSql();
			return jsonRet.toString();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "{\"status\":\"-2\",\"msg\":\"服务器出现异常\"}";
	}

	@Override
	public boolean joinActivity(String uid, String aid) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt  = null;
		ResultSet set = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select is_over, who_join from user_sport_city where sid=?;");
			stmt.setString(1, aid);
			set = stmt.executeQuery();
			if (set.next()) {
				if (set.getBoolean("is_over"))
					return false;
				String users = set.getString("who_join");
				if (users != null && users !="") {
					String[] list = users.split(" ");
					for (int i =0; i< list.length; ++i) {
						System.out.println(list[i]);
						if (list[i].equals(uid))
							return false;
					}
				} else 
					users = "";
				users += uid +" ";
				System.out.println(users);
				stmt = sql.getConnection().prepareStatement(
						"update user_sport_city set who_join=? where sid=?;");
				stmt.setString(1, users);
				stmt.setString(2, aid);
				stmt.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				set.close();
				stmt.close();
				sql.closeSql();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}




