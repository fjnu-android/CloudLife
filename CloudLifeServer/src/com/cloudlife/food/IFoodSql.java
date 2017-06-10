package com.cloudlife.food;

import java.sql.ResultSet;
import java.util.List;

/**
 * ʳƷ�����ݿ⽻������ӿ���
 * 
 * @author wuyi
 *  
 */
public interface IFoodSql  {
	
	/**
	 * @brief �����û������ʲ��������Ĳ�Ʒ
	 * @param type user����������
	 * @return ���ز�ѯ������������
	 */
	ResultSet getDishDataByBodyType(String type);
	
	/**
	 * @brief �����û������ʲ��������Ĳ�Ʒ 
	 * @param type user����������
	 * @return ���ز�ѯ������������ ��Ϊ�㷨������Դ
	 */
	List<DishData> getDishDataToCmpByBodyType(String type);
	
	/**
	 * @brief ����ʳƷ�����Ͳ���
	 * @param type ʳƷ����  �� �߲���/ˮ���� ��
	 * @return ���ز�ѯ������������
	 */
	List<DishData> getDishDataByFoodType(String type);
	
	/**
	 * @brief ����ʳƷ�����Ͳ���
	 * @param type ʳƷ����  �� �߲���/ˮ���� ��
	 * @return ���ز�ѯ������������
	 */
	List<DishData> getMenuDataByFoodType(String type);
	/**
	 * @brief ���ݲ�Ʒ���ҵ���صĲ���
	 * @param name ��Ʒ��
	 * @return ���ز�ѯ������������
	 */
	ResultSet getMenuDataByDishName(String name);
	
	/**
	 * @brief ����ĳ��Ʒ����ϸ��Ϣ
	 * @param name ��Ʒ��
	 * @return ���ز�ѯ������������
	 */
	ResultSet getDishDataByName(String name);
	
	
}
