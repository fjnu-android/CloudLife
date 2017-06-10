package com.cloudlife.user.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.utils.EncodeAndDecode;

/**
 * @breif 用户数据库操作接口类的具体实现类
 * @author dell
 *
 */
public class UserSqlImp implements IUserSql{

	// 数据库操作类 集合了数据库的初始化 方便操作
	//private SqlDeal m_SqlDeal;
	final String m_tableName = "user_info";
	
	@Override
	public boolean addUser(String phone, String pass) {
		
		SqlDeal sql = new SqlDeal();
		// 执行添加新用户的sql语句
		int ret = 0;
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into user_info(phone, password) values(?,?);");
			stmt.setString(1, phone);
			stmt.setString(2, pass);
			ret = stmt.executeUpdate();
			
			System.out.println("注册结果："+ret);
			stmt.close();
			sql.closeSql();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return ret == 1?true:false;
	}

	@Override
	public boolean modifyData(String phone, String[] data) {
		
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"update user_info set name=?, birthday=?, height=?, weight=?,city=?,sex=?,sign=?,work=? where phone=?;");
			stmt.setString(1, data[0]);
			stmt.setString(2, data[1]);
			stmt.setString(3, data[2]);
			stmt.setString(4, data[3]);
			stmt.setString(5, data[4]);
			stmt.setString(6, data[5]);
			stmt.setString(7, data[6]);
			stmt.setString(8, data[7]);
			stmt.setString(9,  phone);
			int ret = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (ret == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isExists(String phone) {

		SqlDeal sql = new SqlDeal();
		// 查询用户 
		int count = 0;
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement("select uid from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet res = stmt.executeQuery();
			count = res.first()==true?1:0;
			stmt.close();
			sql.closeSql();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count==1?true:false;
	}

	@Override
	public boolean changePass(String phone, String prePass, String newPass) {
		SqlDeal sql = new SqlDeal();
		String passwd = "";
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement("select password from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet res = stmt.executeQuery();
			if (res.first() == false)
				return false;
			passwd = res.getString("password");
			if (prePass.equals(passwd) == false)
				return false;
			// 修改密码
			stmt = sql.getConnection().prepareStatement("update user_info set password=? where phone=?");
			stmt.setString(1, newPass);
			stmt.setString(2, phone);
			int n = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			return n == 1? true:false;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getData(String phone) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement("select name,sign, birthday, height, weight,bodyType,sex,city,work,icon_url from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet res = stmt.executeQuery();
			if (!res.first())
				return "";
			JSONObject json = new JSONObject();
			json.put("status", "1");
			json.put("name", res.getString("name")==null?"":res.getString("name"));
			json.put("sign", res.getString("sign")==null?"":res.getString("sign"));
			json.put("birthday", res.getString("birthday")==null?"":res.getString("birthday"));
			json.put("height", res.getFloat("height"));
			json.put("weight", res.getFloat("weight"));
			json.put("sex", res.getString("sex")==null?"":res.getString("sex"));
			json.put("bodyType", res.getString("bodyType")==null?"":res.getString("bodyType"));
			json.put("city", res.getString("city")==null?"":res.getString("city"));
			json.put("work", res.getString("work")==null?"":res.getString("work"));
			json.put("img", res.getString("icon_url") == null?"":res.getString("icon_url"));
			return json.toString();
	
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return "";
	}

	@Override
	public int login(String phone, String pass) {
		
		SqlDeal sql = new SqlDeal();
		// 验证用户账号密码是否正确
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement("select password from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet res = stmt.executeQuery();
			if (res.first() == false)
				return -1; // 用户名不存在
			String passwd = res.getString("password");
			if (passwd.equals(pass))
				return 1;
			else
				return 0;
		} catch(SQLException e) {
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
		return -3;
	}

	@Override
	public boolean check(String user, String cookie) {
		if (isExists(user) == false)
			return false;
		// 查询数据库 获取用户的密码 然后比较md5加密后的数据是否一致
		SqlDeal sql = new SqlDeal();
		String passwd = "";
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement("select password from user_info where phone=?;");
			stmt.setString(1, user);
			ResultSet res = stmt.executeQuery();
			if (res.first() == false)
				return false;
			passwd = res.getString("password");
			res.close();
			stmt.close();
			sql.closeSql();
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		String ret = EncodeAndDecode.md5(user + passwd);
		return ret.equals(cookie) == true? true: false;
	}

	@Override
	public boolean modifySign(String phone, String newSign) {
		// 修改用户的个性签名
		SqlDeal sql = new SqlDeal();
		
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement("update user_info set sign=? where phone=?");
			stmt.setString(1, newSign);
			stmt.setString(2, phone);
			stmt.close();
			int n = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			if (n == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateBodyType(String phone, String type) {
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement("update user_info set bodyType=? where phone=?;");
			stmt.setString(1, type);
			stmt.setString(2, phone);
			int n = stmt.executeUpdate();
			stmt.close();
			sql.closeSql();
			return  n==1? true:false;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String getModelData(String phone) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt= sql.getConnection().prepareStatement(
					"select model_data from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				String ret = set.getString("model_data");
				set.close();
				return ret;
			}
		} catch (SQLException e) {
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
	public boolean modifyModelData(String phone, String data) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt= sql.getConnection().prepareStatement(
					"update user_info set model_data=? where phone=?;");
			stmt.setString(1, data);
			stmt.setString(2, phone);
		    stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return true;
	}

	private String getMenuDataByName(String name) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from resource_food_menu where locate(?, name)>0;");
			stmt.setString(1, name);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			while (set.next()) {
				JSONObject json = new JSONObject();
				json.put("name", set.getString("name"));
				json.put("bodyType", set.getString("bodyType"));
				json.put("image", set.getString("image"));
				String effect = set.getString("effect");
				if (effect == null)
					effect="";
				json.put("effect", effect.length()>30? effect.substring(0,30):effect);
				jsonRet.accumulate("menu", json);
			} 
			set.close();
			return jsonRet.toString();
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
	
	public String getDishDataByName(String name) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {// select *from resource_food_dish where locate(?, bodyType)>0;
			stmt = sql.getConnection().prepareStatement(
					"select *from resource_food_dish where locate(?, name)>0;");
			stmt.setString(1, name);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			while(set.next()) {
				JSONObject json = new JSONObject();
				json.put("name", set.getString("name"));
				json.put("bodyType", set.getString("bodyType"));
				json.put("image", set.getString("image"));
				String effect = set.getString("effect");
				if (effect == null)
					effect="";
				json.put("effect", effect.length()>30? effect.substring(0,30):effect);
				jsonRet.accumulate("dish", json);
			} 
			set.close();
			return jsonRet.toString();
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
	public boolean addNewFriend(String uid, String friend_id) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement("insert into user_friend(uid, friend_id) values(?,?);");
			stmt.setString(1, uid);
			stmt.setString(2, friend_id);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return true;
	}

	@Override
	public String findNewFriend(String type, String user) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select uid, name, sex, sign from user_info where phone=?;");
			stmt.setString(1, user);
			ResultSet set = stmt.executeQuery();
			JSONObject ret = new JSONObject();
			if (set.first()) {
				ret.put("status", "1");
				ret.put("uid", set.getString("uid"));
				ret.put("name", set.getString("name"));
				ret.put("sex", set.getString("sex"));
				ret.put("sign", set.getString("sign"));
				
			} else {
				ret.put("status", "0");
			}
			return ret.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return "{\"status\":\"-1\"}"; // 服务器异常
	}

	@Override
	public String getFriendListData(String uid) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from user_friend where uid=? or friend_id=?;");
			stmt.setString(1, uid);
			stmt.setString(2, uid);
			ResultSet set = stmt.executeQuery();
			JSONObject jsonRet = new JSONObject();
			jsonRet.put("status", "1");
			while (set.next()) {
				JSONObject jsonTmp = new JSONObject();
				String user1 = set.getString("uid");
				String user2 = set.getString("friend_id");
				if (user1.equals(uid)) {
					stmt = sql.getConnection().prepareStatement(
							"select uid,name, sign,icon_url from user_info where uid=?");
					stmt.setString(1, user2);
				} else {
					stmt = sql.getConnection().prepareStatement(
							"select uid,name, sign,icon_url from user_info where uid=?");
					stmt.setString(1, user1);
				}
				ResultSet setTmp = stmt.executeQuery();
				jsonTmp.put("name", setTmp.getString("name"));
				jsonTmp.put("uid", setTmp.getString("uid"));
				jsonTmp.put("sign", setTmp.getString("sign"));
				jsonTmp.put("icon_url", setTmp.getString("icon_url"));
				jsonRet.accumulate("friend", jsonTmp);
				setTmp.close();
			}
			set.close();
			return jsonRet.toString();
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
	public boolean deleteFriend(String uid, String friend_id) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"delete from user_friend where ((uid='?' && friend_id='?') or (uid='?' && friend_id='?'));");
			stmt.setString(1, uid);
			stmt.setString(2, friend_id);
			stmt.setString(3, friend_id);
			stmt.setString(4, uid);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		return true;
	}

	@Override
	public String getDietDataByName(String name) {
		JSONObject json = new JSONObject();
		json.put("status", "1");
		json.put("dish", getDishDataByName(name));
		json.put("menu", getMenuDataByName(name));
		return json.toString();
	}

	private String getMenuDataByName(String type,String name) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from resource_food_menu where locate(?, name)>0;");
			stmt.setString(1, name);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				JSONObject json = new JSONObject();
				json.put("status", "1");
				json.put("name", set.getString("name"));
				json.put("bodyType", set.getString("bodyType"));
				json.put("image", set.getString("image"));
				json.put("material_main", set.getString("material_main"));
				json.put("material_assist", set.getString("material_assist"));
				json.put("how_make", set.getString("how_make"));
				json.put("effect", set.getString("effect"));
				json.put("flavor", set.getString("flavor"));
				set.close();
				return json.toString();
			} else 
				return null;
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
	public String getDishDataByName(String type, String name) {
		if (type.equals("menu")) {
			return getMenuDataByName("menu", name);
		}
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {// select *from resource_food_dish where locate(?, bodyType)>0;
			stmt = sql.getConnection().prepareStatement(
					"select *from resource_food_dish where name=?;");
			stmt.setString(1, name);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				JSONObject json = new JSONObject();
				json.put("status", "1");
				json.put("name", set.getString("name"));
				json.put("bodyType", set.getString("bodyType"));
				json.put("man_suit", set.getString("man_suit"));
				json.put("man_unsuit", set.getString("man_unsuit"));
				json.put("image", set.getString("image"));
				json.put("how_eat", set.getString("how_eat"));
				json.put("save", set.getString("save"));
				json.put("effect", set.getString("effect"));
				json.put("select", set.getString("how_select"));
				json.put("introduction", set.getString("introduction"));
				
				set.close();
				return json.toString();
			} else 
				return null;
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
	public boolean uploadUserIcon(String phone,String url) {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"update user_info set icon_url=? where phone=?;");
			stmt.setString(1, url);
			stmt.setString(2, phone);
			return stmt.executeUpdate() ==1?true:false;
		} catch (SQLException e) {
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
	public String getStoreFoodData(String uid) {
		/**
		 * 总共生成5个店的信息 其中3个店包含了用户三餐推荐的数据
		 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date()).toString();// new Date()为获取当前系统时间
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(""
					+ "select content_menu from user_plan_diet where uid=? and time=?;");
			stmt.setString(1, uid);
			stmt.setString(2, date);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				JSONObject json = JSONObject.fromObject(set.getString("content_menu"));
				
				if (json == null)
					return "";
				JSONArray jsonArr = (JSONArray)json.get("plan");
				JSONObject bf = (JSONObject) jsonArr.getJSONObject(0).get("breakfast");
				JSONObject ln = (JSONObject) jsonArr.getJSONObject(0).get("lunch");
				JSONObject dn = (JSONObject) jsonArr.getJSONObject(0).get("dinner");
				
				JSONObject jsonRet = new JSONObject(); // 返回结果的json数据
				
				if (bf==null || ln==null || dn==null)
					return "";
				JSONArray bfArr = null;
				try{
					bfArr = bf.getJSONArray("menu");
				} catch(JSONException e) {
				}
				
				if (bfArr == null) {
					try {
						bfArr = new JSONArray();
						bf = bf.getJSONObject("menu");
						bfArr.add(bf);
					} catch(JSONException e) {
					}
				}
				
				JSONArray lnArr = null;
				try {
					lnArr = ln.getJSONArray("menu");
				} catch(JSONException e) {
				}
				if (lnArr == null) {
					try {
						lnArr = new JSONArray();
						ln = ln.getJSONObject("menu");
						lnArr.add(ln);
					} catch(JSONException e) {
					}
				}
				
				JSONArray dnArr = null;
				try{
					dnArr = dn.getJSONArray("menu");
				} catch(JSONException e) {
				}
				if (dnArr == null) {
					try {
						dnArr = new JSONArray();
						dn = dn.getJSONObject("menu");
						dnArr.add(dn);
					} catch(JSONException e) {
					}
				}
				
				//组装早餐数据
				if (true) {
					JSONObject tmp = new JSONObject();
					List list = new ArrayList<String>();
					for (int i =0; i< bfArr.size(); ++i) {
						JSONObject tmp2 = new JSONObject();
						tmp2.put("name", bfArr.getJSONObject(i).get("name"));
						tmp2.put("flag", "1");
						list.add(bfArr.getJSONObject(i).get("name"));
						tmp.accumulate("menu", tmp2);
					}
					// 添加早餐其他菜谱
					set.close();
					stmt = sql.getConnection().prepareStatement("select name from resource_food_menu;");
					set = stmt.executeQuery();
					if (set.first()) {
						for (int i=0; i< 10;) {
							JSONObject tmp2 = new JSONObject();
							if (!list.contains(set.getString("name"))) {
								tmp2.put("name", set.getString("name"));
								tmp2.put("flag", "0");
								tmp.accumulate("menu", tmp2);
								++i;
							} 
							set.next();
						}

					}
					jsonRet.accumulate("store", tmp);
				}
				//组装午餐数据
				if (true) {
					JSONObject tmp = new JSONObject();
					List list = new ArrayList<String>();
					for (int i =0; i< lnArr.size(); ++i) {
						JSONObject tmp2 = new JSONObject();
						tmp2.put("name", lnArr.getJSONObject(i).get("name"));
						tmp2.put("flag", "1");
						list.add(lnArr.getJSONObject(i).get("name"));
						tmp.accumulate("menu", tmp2);
					}
					// 添加午餐其他菜谱
					set.close();
					stmt = sql.getConnection().prepareStatement("select name from resource_food_menu;");
					set = stmt.executeQuery();
					if (set.first()) {
						for (int i=0; i< 10;) {
							JSONObject tmp2 = new JSONObject();
							if (!list.contains(set.getString("name"))) {
								tmp2.put("name", set.getString("name"));
								tmp2.put("flag", "0");
								tmp.accumulate("menu", tmp2);
								++i;
							} 
							set.next();
						}

					}
					jsonRet.accumulate("store", tmp);
				}
				//组装晚餐数据
				if (true) {
					JSONObject tmp = new JSONObject();
					List list = new ArrayList<String>();
					for (int i =0; i< dnArr.size(); ++i) {
						JSONObject tmp2 = new JSONObject();
						tmp2.put("name", dnArr.getJSONObject(i).get("name"));
						tmp2.put("flag", "1");
						list.add(dnArr.getJSONObject(i).get("name"));
						tmp.accumulate("menu", tmp2);
					}
					// 添加晚餐其他菜谱
					set.close();
					stmt = sql.getConnection().prepareStatement("select name from resource_food_menu;");
					set = stmt.executeQuery();
					if (set.first()) {
						for (int i=0; i< 10;) {
							JSONObject tmp2 = new JSONObject();
							if (!list.contains(set.getString("name"))) {
								tmp2.put("name", set.getString("name"));
								tmp2.put("flag", "0");
								tmp.accumulate("menu", tmp2);
								++i;
							} 
							set.next();
						}

					}
					jsonRet.accumulate("store", tmp);
					set.close();
					stmt = sql.getConnection().prepareStatement("select name from resource_food_menu;");
					set = stmt.executeQuery();
					List lt = new ArrayList<String>();
					while (set.next()) {
						lt.add(set.getString("name"));
					}
					for (int i =0; i< 3; ++i) {

						for (int j=0; j< 10;++j) {
							JSONObject tmp2 = new JSONObject();
							int index = (int) (Math.random()*lt.size());
							tmp2.put("name", lt.get(index));
							lt.remove(index);
							tmp2.put("flag", "0");
							tmp.accumulate("menu", tmp2);
						}
						jsonRet.accumulate("store", tmp);
					}
				}

				System.out.println(jsonRet.toString());
				return jsonRet.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}









