package com.cloudlife.sport;

/**
 * @brief �˶��Ƽ��Ŵ��㷨֮����ģ����
 * @author wuyi
 *
 */
public class UserModel {

	private int time = 60; // �û����˶�ʱ��
	private float today_diet_power = 350; // ������ʳ��õ�����
	private int item_count = 3;
	private float weight = 60; //����
	
	public int getItemCount() {
		return item_count;
	}
	
	public int getTotalTime() {
		return this.time;
	}
	
	public float getTotalPower() {
		return this.today_diet_power;
	}
	
	/**
	 * @brief �����û��ĸ�����Ϣ �����˶���ʱ�� 
	 * @param bodyType �û�����������
	 * @param sex �û����Ա�
	 * @param age ����
	 * @param weight ����
	 * @param height ���
	 * @param pal �����ˮƽ
	 * @param place ���յ��˶�ָ�� �ʺ����⻹�������˶� out in
	 * @return �Ƿ�����ɹ�
	 */
	public boolean resolveUserInfo(String bodyType, String sex, int age, 
			float weight, float height, String work, String place) {
		
		return true;
	}
	
	public float getWeight() {
		return this.weight;
	}
	
}













