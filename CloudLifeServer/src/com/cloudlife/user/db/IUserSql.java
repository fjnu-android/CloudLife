package com.cloudlife.user.db;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * @brief �û����ݿ�����ӿ���
 * @author wuyi
 *
 */
public interface IUserSql {
	
	// ������û�
	boolean addUser(String phone, String pass);
	
	// ���/�޸��û�������
	boolean modifyData(String phone, String[] data);
	
	// ��ȡ�û������� ���ؽ��ΪJson����
	String getData(String phone);
	
	// �����û���������Ϣ
	boolean updateBodyType(String phone, String type) ;
	
	// ��ѯ���û��Ƿ���ע�� 
	// ��ע�᷵��true ����Ϊfalse
	boolean isExists(String phone);
	
	boolean modifySign(String phone, String newSign);
	
	// �޸��û�������
	boolean changePass(String phone, String prePass, String newPass);
	
	// �û���¼
	int login(String phone, String pass);
	
	// ����û�
	boolean check(String user, String cookie/*md5���ܵ��û��˺ź�����*/);

	// ��ȡ�û�ģ������
	String getModelData(String phone);
	
	// �޸��û���ģ������
	boolean modifyModelData(String phone, String data);
	
	// ͨ����Ʒ�����ҵ���Ʒ��Ϣ
	String getDietDataByName(String name);
	
	// ֻ��ȡ��Ʒ����
	String getDishDataByName(String type, String name);
	
	// ��Ӻ���
	boolean addNewFriend(String uid, String friend_id);
	
	// ���Һ��� �ɹ��򷵻ر������˵Ļ�����Ϣ �û��� �û�id �Ա� ǩ�� ͷ��
	// ��ʱֻ����ͨ���ֻ�������в���
	String findNewFriend(String type, String user);
	
	// ��ȡ�û��ĺ����б���Ϣ
	String getFriendListData(String uid);
	
	// ɾ������
	boolean deleteFriend(String uid, String friend_id);
	
	boolean uploadUserIcon(String phone, String url);
	
	// ��ȡ�������̵��Ʒ��Ϣ  ���ݲ����û��������ͬ��ֵ
	String getStoreFoodData(String uid);
}







