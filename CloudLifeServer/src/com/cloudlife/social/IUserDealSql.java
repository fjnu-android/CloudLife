package com.cloudlife.social;

public interface IUserDealSql {

	// �û�����̬
	boolean postDynamic(String uid, String content,String img);
	
	// ��ȡͬ�����е���������
	String getCityBynamic(String city, int iBegin, int iEnd,long ts);
	
	// ��ȡȫ������������
	String getCityBynamic(int iBegin, int iEnd, long ts);
	
	// ��ȡ�Լ���������ж�̬����
	String getMyDynamicData(String uid, int iBegin, int iEnd);
	
	// ɾ���Լ�ָ����Ȧ������
	boolean deleteMyDynamicData(String uid, String sid);
}




