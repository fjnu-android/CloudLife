package com.cloudlife.user.db;

/**
 *  @brief �û��ƻ����ݿ�����ӿ�
 * 			ʵ�����½ӿ�:
 * 				1. ��ѯ�Ƿ������мƻ����� ����boolean
 * 				2. ��ȡ�û���ǰ�ļƻ���
 * 				3. �����µļƻ�����
 * 			���������: ��ʳ�Ƽ��� �˶��Ƽ��� ��Ϣ�ȸ��Ի���
 * @author wuyi
 *  
 */

public interface IUserPlanSql {

	/**
	 * @brief ��ѯ�û�����ʳ�ƻ����Ƿ��Ѿ�����
	 * 		��������� �򴴽��µ��Ƽ��ƻ�
	 * @param phone �û����ֻ����� Ψһ��ʶ��
	 * @return ���ؽ��ռƻ�
	 */
	String isTodayDietPlanExist(String phone, int type);
	
	/**
	 * @brief ��ȡ�û��������ʳ�Ƽ���������
	 * @param phone
	 * @return
	 */
	String getTodayDietPlanEvaluate(String phone, int type);
	/**
	 * @brief ��ѯ�û����˶��ƻ����Ƿ��Ѿ�����
	 * @param phone �û����ֻ����� Ψһ��ʶ��
	 * @return �����򷵻�true ���򷵻�false
	 */
	//boolean isExercisePlanExits(String phone);
	
	/**
	 * @brief ��ѯ��ǰ���˶��Ƽ�
	 * @param phone
	 * @return
	 */
	String isTodaySportPlanExits(String phone);

}




