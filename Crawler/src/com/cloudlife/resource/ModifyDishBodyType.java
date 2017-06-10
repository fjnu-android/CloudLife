package com.cloudlife.resource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cloudlife.db.SqlDeal;

public class ModifyDishBodyType {

	static private ModifyDishBodyType modify;
	private int isWorking = 0;
	
	
	private String[] data = new String[]{"",
			"ɽҩ �ƶ� ���� ���� �㶹 ��ͷ �㹽 �ⶹ Ŵ�� ���� �׹� �Ϲ� ���Ĳ� ���ܲ� �϶� ��ź ���� ���� ��� ţ�� ���� ���� ��ˮ�� ���� ��Ŀ�� ���� ���� ���� ���Ѹ� ƻ�� ��� ������ ����"
			,"���� ���� ���� �ײ˻� ��� �ٺ� �̶� ��ײ� С�ײ� ݩ�� Ŵ�� ��� ���� ���� �з ţ�� ĵ�� ���� Ѽ�� ���� ��Ƥ ���� ���� ���� ��ľ��",
			"���� �Ϲ� �²� ���� �ƶ�ѿ ���� ���� ��֦ ���� ���� ӣ�� �� ���� ���� ���� ���� ��� ��� ���ܲ� ɽҩ ��� ���� ţ�� ��� ���� ���� ���� Ϻ ���� ���� ����"
			,"�ⶹ �϶� �ඹ ���� ���� ��� �ϲ� ���� ���� ɽҩ ޲�� ���� ���� ��ͷ�� ���� ���� ���� ���� �ܲ� ��� ���� ���� ��� ����",
			"��ź ���� �۲� ���� ���� �ƹ� �ļ��� �϶� �̶� Ѽ�� ���� ݩ�� ���� ���� ���� ���� �ϲ� ���� �߹� ��« ��� �˹� ���� �� �̲� ���� �ײ� ���� ���Ĳ� �ܲ� ���� �̶� ݫ��",
			"�ƻ��� �ܲ� ˿�� ����« ������ �ջ� ���� ���� ���� Ģ�� ���� ���� ���� ��� ���Ĳ� ��� õ�� ���� ���� ���� ɽ� ���� ���� ���Ѹ� ����"
			,"�ڶ� ��ľ�� ���ܲ� ��� ���� �Ͳ� ���� ��� ���� �²� ���� �ƶ� ���� ɽ� ��ľ�� �з ���� �� �ƾ� ���Ѿ� Ŵ�����"};
	private String[] alpha= new String[]{"A", "B", "C","D","E","F","G","H","I"};
	
	static public ModifyDishBodyType getInstance() {
		if (modify==null)
			modify = new ModifyDishBodyType();
		return modify;
	}
	
	private ModifyDishBodyType() {
		
	}
	
	public void begin() {
	
		if (isWorking == 1)
			return ;
		isWorking =1;
		System.out.println("begin");
		SqlDeal sql = new SqlDeal();
		for (int i =1; i<8; ++i) {
			String[] food = data[i].split(" ");
			for (int j=0; j< food.length; ++j) {
				try {
					PreparedStatement stmt = sql.getConnection().prepareStatement(
							"select bodyType from resource_food_menu where name like ?;");
					stmt.setString(1, "%"+food[j]+"%");
					ResultSet set = stmt.executeQuery();
					if (set.first()) {
						String body = set.getString("bodyType");
						body = body+" "+ alpha[i];
						set.close();
						stmt = sql.getConnection().prepareStatement("update resource_food_menu set bodyType=? where name like ?;");
						stmt.setString(1, body);
						stmt.setString(2, "%"+food[j]+"%");
						stmt.execute();
						continue;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("�������");
		isWorking =0;
	}
}
