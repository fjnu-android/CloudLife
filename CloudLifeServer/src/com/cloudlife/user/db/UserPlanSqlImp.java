package com.cloudlife.user.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.attribute.standard.PresentationDirection;

import net.sf.json.JSONObject;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.nsga2.DataFactory;
import com.cloudlife.nsga2.Nsga2;
import com.cloudlife.nsga2.Population;
import com.cloudlife.nsga2.TargetFunImp;
import com.cloudlife.nsga2.UserModel;
import com.cloudlife.sport.Ga;

public class UserPlanSqlImp implements IUserPlanSql{

	@Override
	public String isTodayDietPlanExist(String phone, int type) {
		// 先获取时间 然后查询数据库信息
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date()).toString();// new Date()为获取当前系统时间
		System.out.println("date：" + date);
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select *from user_plan_diet where uid=? and time=?");
			stmt.setString(1, phone);
			stmt.setString(2, date);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				String ret = set.getString(type==0?"content_dish":"content_menu");
				JSONObject json = new JSONObject();
				json.put("status", "1");
				json.put("data", ret);
				stmt.close();
				return json.toString();
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("正在获取模型数据=========");
		
		String strRmd = "";
		UserModel model = new UserModel();
		// 从数据库获取用户模型数据
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select model_data from user_info where phone=?;");
			stmt.setString(1, phone);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				String model_data = set.getString("model_data");
				model.resolveJsonData(model_data);
			}
			set.close();
			// 解析用户信息
			stmt = sql.getConnection().prepareStatement("select bodyType, sex, weight, height, work,birthday from user_info where phone=?;");
			stmt.setString(1, phone);
			set = stmt.executeQuery();
			if (set.first()) {
				// 计算年龄
				String birthday = set.getString("birthday");
				int age = Integer.parseInt(date.substring(0,4)) -Integer.parseInt(birthday.substring(0, 4));
				model.resolveUserInfo(set.getString("bodyType"), set.getString("sex"),age, 
						set.getFloat("weight"), set.getFloat("height"), set.getString("work"));
				set.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("数据模型解析完毕======");
		DataFactory fac = DataFactory.getInstance();
		TargetFunImp fun = new TargetFunImp(model);
		Population pop = new Population();
		pop.produceIndividual(20, model);
		Nsga2 nsga2 = new Nsga2();
		pop = nsga2.startEvolution(pop, fun.getTargetFun(), model);
		strRmd = fac.getPlan(pop,2, model);	
		String eva = fac.getEvaluate(pop,model, 2);
		
		model.setType(1);
		Population pop2 = new Population();
		TargetFunImp fun2 = new TargetFunImp(model);
		pop2.produceIndividual(20, model);
		pop2 = nsga2.startEvolution(pop2, fun2.getTargetFun(), model);
		String strRmd2 = fac.getPlan(pop,2, model);	
		String eva2 = fac.getEvaluate(pop2,model, 2);
		
		System.out.println("遗传算法执行完毕============");
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into user_plan_diet(uid, time, content_dish, content_menu,evaluate_dish, evaluate_menu) values(?,?,?,?,?,?);");
			stmt.setString(1, phone);
			stmt.setString(2, date);
			stmt.setString(3, strRmd);
			stmt.setString(4, strRmd2);
			stmt.setString(5, eva);
			stmt.setString(6, eva2);
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		JSONObject json = new JSONObject();
		json.put("status", "1");
		json.put("data", type==0?strRmd:strRmd2);
		return json.toString();
	}

	@Override
	public String getTodayDietPlanEvaluate(String phone, int type) {
		// 先获取时间 然后查询数据库信息
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date()).toString();// new Date()为获取当前系统时间
		System.out.println("date：" + date);
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select  evaluate_dish, evaluate_menu from user_plan_diet where uid=? and time=?");
			stmt.setString(1, phone);
			stmt.setString(2, date);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				String ret = set.getString(type==0?"evaluate_dish":"evaluate_menu");
				JSONObject json = new JSONObject();
				json.put("status", "1");
				json.put("data", ret);
				stmt.close();
				return json.toString();
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String isTodaySportPlanExits(String phone) {
		// 先获取时间 然后查询数据库信息
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date()).toString();// new Date()为获取当前系统时间
		System.out.println("date：" + date);
		SqlDeal sql = new SqlDeal();
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"select *from user_plan_sport where uid=? and time=?");
			stmt.setString(1, phone);
			stmt.setString(2, date);
			ResultSet set = stmt.executeQuery();
			if (set.first()) {
				String ret = set.getString("content");
				JSONObject json = new JSONObject();
				json.put("status", "1");
				json.put("data", ret);
				stmt.close();
				sql.closeSql();
				return json.toString();
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// 如果没有 就生成相关推荐
		com.cloudlife.sport.UserModel model = new com.cloudlife.sport.UserModel();
		com.cloudlife.sport.DataFactory fac = com.cloudlife.sport.DataFactory.getInstance();
		com.cloudlife.sport.Population pop = new com.cloudlife.sport.Population();
		pop.createPop(20,  model);
		Ga ga = new Ga();
		String ret = ga.startRevolution(pop, model);
		try {
			PreparedStatement stmt = sql.getConnection().prepareStatement(
					"insert into user_plan_sport(uid, content, date) values(?,?,?);");
			stmt.setString(1, phone);
			stmt.setString(2, ret);
			stmt.setString(3, date);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql.closeSql();
		}
		return ret;
	}

}
