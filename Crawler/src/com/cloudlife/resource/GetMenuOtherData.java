package com.cloudlife.resource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloudlife.db.SqlDeal;

public class GetMenuOtherData {

	class Data{
		
		String name;
		String bodyType;
		String dishType;
	}
	
	private static GetMenuOtherData g_data;
	
	static public GetMenuOtherData getInstance() { 
		
		if (g_data == null)
			g_data = new GetMenuOtherData();
		return g_data;
	}
	
	
	private GetMenuOtherData() {
	
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				fun();
				
			}
		}).start();
		
	}
	
	void fun() {
		
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select name, bodyType, dishType from resource_food_dish where dishType='∫£œ ' or dishType='ŒÂπ»' or dishType='”„¿‡';");
			ResultSet set = stmt.executeQuery();
			List<Data> list = new ArrayList<Data>();
			while (set.next()) {
				Data data = new Data();
				data.name = set.getString("name");
				data.bodyType = set.getString("bodyType");
				data.dishType = set.getString("dishType");
				list.add(data);
			}
			System.out.println(list.size());
			for (int i =0; i< list.size(); ++i) {
				PreparedStatement stmtTmp = sql.getConnection().prepareStatement(
						"update resource_food_menu2 set menu_type=? , bodyType=? where locate(?, name)>0;");
				stmtTmp.setString(1, list.get(i).dishType);
				stmtTmp.setString(2, list.get(i).bodyType);
				stmtTmp.setString(3, list.get(i).name);
				stmtTmp.executeUpdate();
				System.out.println("i:"+ i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}












