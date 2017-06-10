package com.cloudlife.plan;

import java.util.List;

import com.cloudlife.food.DishData;
import com.cloudlife.food.FoodSqlImp;
import com.cloudlife.food.IFoodSql;

/**
 * @brief ��ʳ�ƻ��Ƽ������㷨
 * 
 * 			�㷨ԭ������
 * ��������: �й��˵�9��������ʳ�Ƽ� + ���Ӫ��ѧ���ݡ�
 * 		������������ ��д�ڲ�Ʒ��Ʒ����
 * 
 * �㷨���ݣ�
 * 
 * 		1. ���� ��ȷ������Ҫ��������������Ӧ�߲˻�����
 * 		2. 
 * 
 * @author wuyi & zhang-han-yuan
 */


public class DietPlanAlgorithm {
	
	// ����Ϊ���Ի���������   ���ݹ��ҷ�������ʳ��׼ �ṩĬ��ֵ
	
	private IFoodSql m_foodSql = new FoodSqlImp();
	// ����߲˵����� ����
	private int m_nLunchVeg =0;
	private float m_fLunchVegKg = 0;
	// ����������  ����
	private int m_nLunchMeet =0;
	private float m_fLunchMeetKg = 0;
	// �����������
	private int m_nfLunchSoup = 0;
	// ���������� ����
	private int m_nDinnerMeet = 0;
	private float m_fDinnerMeetKg = 0;
	// ����߲˵����� ����
	private int m_nDinnerVeg = 0;
	private float m_fDinnerVegKg = 0;
	// �����������
	private int m_nDinnerSoup =0;
	
	// �û���Ϣ 
	private float m_fUserHeight = 0;
	private float m_fUserWeight = 0;
	private int m_nUserAge = 0; 
	private String m_strUserBodyType = null;
	// �û��Ƿ���ɶ��������
	private List<String> m_listUserDiease;
	// �û���Ҫͨ����ʳ���ʲô����  ������� ���ݵȵ� ��������������
	private List<String> m_listUserWish;
	// �û��Ĺ�������    ��Ҫ�û�����ְҵ������
	private String m_strUserWork;
	// �û����ڳ���
	private String m_strUserCity;
	
	// ����������    ÿ�ֲ˶�Ӧһ������  ��ʼ��Ϊ5�� ������Ӧɸѡ���мӼ�
	List<DishData> m_DealData;       // ���ʴ����Ʒ
	List<DishData> m_DealDataAssist; // ������Ʒ
	
	
	/**
	 * @brief ��һ��ɸѡ�㷨   �ȸ����û����ʻ�ȡ��Ӧ��Ʒ
	 * 		ͨ���û��Ƿ����ĳ�ּ���  ɸѡ��(0��)��Щ��Ʒ�Ľ�����Ⱥ�����а����˼�����
	 * @return booloean
	 */
	private boolean firstScreen() {
		m_DealData = m_foodSql.getDishDataToCmpByBodyType(m_strUserBodyType);
	
		for (int i = 0; i< m_DealData.size(); ++i) {
			for (int j =0; j< m_listUserDiease.size(); ++j) {
				String unsuit = m_DealData.get(i).getManUnSuit();
				if (unsuit.contains(m_listUserDiease.get(j))) {
					m_DealData.get(i).setScore(0);
				} 

				String suit = m_DealData.get(i).getManSuit();
				if (suit.contains(m_listUserDiease.get(j))) {
					m_DealData.get(i).addScore();
				}
			}
		}
		return true;
	}
	/**
	 * @brief �ڶ���ɸѡ�㷨 
	 * 		���ݣ� ƥ���û������� ������ �û���Ҫ���� ���� ���� �ȵȣ�
	 * @return boolean
	 */
	private boolean secondScreen() {
		for (int i =0; i< m_DealData.size(); ++i) {
			if (m_DealData.get(i).getScore() == 0)
				continue;
			for (int j=0; j< m_listUserWish.size(); ++j) {
				if (m_DealData.get(i).getEffect().contains(m_listUserWish.get(i)))
					m_DealData.get(i).addScore();
			}
		}
		return true;
	}
}






