package com.cloudlife.social;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import com.cloudlife.db.SqlDeal;


public class UserDealSqlImp implements IUserDealSql{

	@Override
	public boolean postDynamic(String uid, String content, String img) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select city from user_info where phone=?;");
			stmt.setString(1, uid);
			ResultSet set = stmt.executeQuery();
			String city ="福州";
			if (set.next()) {
				city = set.getString("city");
			}
			city = city.substring(city.indexOf("-")+1);
			set.close();
			stmt.close();
			stmt = sql.getConnection().prepareStatement(
					"insert into user_social(uid, text, image,city, ts, time) values(?,?,?,?,?,null);");
			stmt.setString(1, uid);
			stmt.setString(2, content);
			stmt.setString(3, img);
			stmt.setString(4, city);
			stmt.setLong(5, System.currentTimeMillis());
			return stmt.executeUpdate()==1?true:false;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return false;
	}

	@Override
	public String getCityBynamic(String city, int iBegin, int iEnd, long ts) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from user_social where city=? and ts< ? order by ts desc,sid desc limit ?,?;");
			System.out.println(city);
			stmt.setString(1, city);
			stmt.setLong(2, ts);
			stmt.setInt(3, iBegin);
			stmt.setInt(4, iEnd);
			ResultSet set = stmt.executeQuery();
			JSONObject json = new JSONObject();
			if (set.first()== false) {
				json.put("state", "2"); // 表示空
				set.close();
				return json.toString();
			} else {
				json.put("state", "1"); // 表示已找到
				int nCnt = 0;
				while (true) {
					PreparedStatement stmtTmp = sql.getConnection().prepareStatement(
							"select name, icon_url, uid from user_info where phone=?;");
					stmtTmp.setString(1, set.getString("uid"));
					ResultSet setTmp = stmtTmp.executeQuery();
					String name = "",  icon_url = "", uid="";
					if (setTmp.next()) {
						name = setTmp.getString("name");
						icon_url = setTmp.getString("icon_url");
						uid = setTmp.getString("uid");
						setTmp.close();
					}
					
					JSONObject tmp = new JSONObject();
					tmp.put("name", name);
					tmp.put("icon", icon_url);
					tmp.put("uid", uid);
					tmp.put("city", set.getString("city"));
					tmp.put("text", set.getString("text"));
					tmp.put("img", set.getString("image"));
					tmp.put("time", set.getString("time"));
					json.accumulate("data", tmp);
					++nCnt;
					
					if (set.next() == false)
						break;
				}
				json.put("count", nCnt); // 找到的条数
				set.close();
				return json.toString();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return null;
	}

	@Override
	public String getCityBynamic(int iBegin, int iEnd, long ts) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from user_social where ts< ? order by ts desc,sid desc limit ?,?;");
			stmt.setLong(1, ts);
			stmt.setInt(2, iBegin);
			stmt.setInt(3, iEnd);
			System.out.println(ts+" b:"+iBegin+" e:"+iEnd);
			ResultSet set = stmt.executeQuery();
			JSONObject json = new JSONObject();
			if (set.first() == false) {
				json.put("state", "2"); // 表示空
				set.close();
				return json.toString();
			} else {
				json.put("state", "1"); // 表示已找到
				int nCnt = 0;
				while (true) {
					PreparedStatement stmtTmp = sql.getConnection().prepareStatement(
							"select name, icon_url, uid from user_info where phone=?;");
					stmtTmp.setString(1, set.getString("uid"));
					ResultSet setTmp = stmtTmp.executeQuery();
					String name = "",  icon_url = "", uid="";
					if (setTmp.next()) {
						name = setTmp.getString("name");
						icon_url = setTmp.getString("icon_url");
						uid = setTmp.getString("uid");
						setTmp.close();
					}
					
					JSONObject tmp = new JSONObject();
					tmp.put("name", name);
					tmp.put("icon", icon_url);
					tmp.put("uid", uid);
					tmp.put("city", set.getString("city"));
					tmp.put("text", set.getString("text"));
					tmp.put("img", set.getString("image"));
					tmp.put("time", set.getString("time"));
					json.accumulate("data", tmp);
					++nCnt;
					
					if (set.next() == false)
						break;
				}
				json.put("count", nCnt); // 找到的条数
				set.close();
				return json.toString();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return "{\"status\":\"-2\"}";
	}

	@Override
	public String getMyDynamicData(String uid, int iBegin, int iEnd) {
	
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select sid, time, text, image, city from user_social where uid=? order by ts desc,sid desc limit ?,?;");
			stmt.setString(1, uid);
			stmt.setInt(2, iBegin);
			stmt.setInt(3, iEnd);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			jsonRet.put("status", "1");
			while (set.next()) {
				JSONObject json = new JSONObject();
				json.put("sid", set.getString("sid"));
				json.put("text", set.getString("text"));
				json.put("iamge", set.getString("image"));
				json.put("city", set.getString("city"));
				json.put("time", set.getString("time"));
				jsonRet.accumulate("data", json);
			}
			return jsonRet.toString();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return "{\"status\":\"-2\"}";
	}

	@Override
	public boolean deleteMyDynamicData(String uid, String sid) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"delete from user_social where uid=? and sid=?;");
			stmt.setString(1, uid);
			stmt.setString(2, sid);
			return stmt.executeUpdate()==1?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return false;
	}

	

}







