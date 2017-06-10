package com.cloudlife.citysports;

/**
 * @brief ���ص����ݽӿ�
 * @author wuyi
 *
 */
public interface IActivitySql {
	
	// �����û��ľ�γ����Ϣ
	boolean updateLocation(String uid, String location_name);
	
	/**
	 * 
	 * @param title ��ı���
	 * @param lng  ��ص�ľ���
	 * @param lat  ��ص��γ��
	 * @param info ��ļ��
	 * @param time_begin ��Ŀ�ʼʱ��
	 * @param time_build ��Ľ���ʱ��
	 * @param location_name ��ص����
	 * @param json_man_cnt ������������
	 * @return �Ƿ񴴽��ɹ�
	 */
	boolean bulidActivity(String uid, String title, float lng, float lat,
			String info, String time_begin,
			String location_name,String city);
	
	/**
	 * @brief ɾ���
	 * @param uid �û���id
	 * @param aid ���id
	 * @return �Ƿ�ɾ���ɹ�
	 */
	boolean deleteActivity(String uid, String aid);
	
	/**
	 * @brief ���»�Ľ���
	 * @param uid �û�id
	 * @param aid �id
	 * @param info �µļ��
	 * @return �Ƿ���³ɹ�
	 */
	boolean updateActivityInfo(String uid, String aid, String info);
	
	/**
	 * @brief ��ȡͬ���е������û��ĵ���λ����Ϣ
	 * @param city ������
	 * @return json�����������Ϣ
	 */
	String getSameCityUser(String city);
	
	/**
	 * @brief ��ȡĳ���е����л
	 * @param city ������
	 * @return json�����������Ϣ
	 */
	String getSameCityActivitys(String city);	
	
	/**
	 * @brief �ĳ�û����������л
	 * @param uid �û���id
	 * @return json�����������Ϣ
	 */
	String getUserActivitys(String uid);
	
	/**
	 * @brief �ָ�������ϸ����
	 * @param aid ���id
	 * @return
	 */
	String getActivityDetailData(String aid);
	
	/**
	 * @brief �û�����ĳ���
	 * @param uid �û�id
	 * @param aid �id
	 * @return
	 */
	boolean joinActivity(String uid, String aid);
}













