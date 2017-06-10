package com.app.cloud.Base;

import android.os.Environment;

/**
 * �ӿ� ��ַ
 *
 */
public final class C {

	//���������ӿ� ����json����
	public static final String weather = "http://op.juhe.cn/onebox/weather/query?cityname=";
	//�ۺ����ݵ�key
	public static final String weather_key = "&key=b4fdfc5741d456503ab95fe2afde0c56";
	////////////////////////////////////////////////////////////////////////
	// core settings (important)

	/**
	 * �ļ�·��
	 *
	 */
	public static final class dir {
		// Ӧ�ø�·��
		public static final String base = Environment.getExternalStorageDirectory().getPath() + "/cloudlife";

		// ��Ʒ/����
		public static final String foodImg = base + "/foodImg";
		// �û�����ͼƬ����·��
		public static final String user = base + "/user";
		// Ȧ�ӵ�ͷ�񻺴�
		public static final String icon = base + "/icon";
		// Ȧ�ӵ�ͼƬ����
		public static final String imgCache = base + "/imgCache";

		// �˺����� shared �ļ���
		public static final String shared = "user_data";
				
		
	}

	/**
	 * cloud_life�������ӿ�
	 *
	 */
	public static final class api {
		// "http://hellojava.nat123.net/CloudLife"
		public static String base = "http://hellojava.nat123.net/CloudLife";
		
		//��½��ע��
		public static final String register = base + "/user/register";
		public static final String login = base + "/user/login";
		
		//������Ϣ�ϴ�
		public static final String info = base + "/user/info";
		
		//TODO �����޸�
		public static final String password = base + "/user/password";
		
		//�����ϴ�
		public static final String body = base + "/user/bodyType";

		//�û�ͷ���ϴ�
		public static final String userIcon = base + "/user/icon_update";

		//��������ģ�����úͻ�ȡ
		public static final String setModel = base + "/modifyModelData";
		public static final String getModel = base + "/getModelData";

		// ����ʳ�ĺ�ʳ��
		public static final String getDietData = base + "/getDietData";

		// ��ȡ�����˶�
		public static final String getaTodaySport = base + "/getTodaySportRmd";

		// ��ȡ�Ƽ�ʳƷ��ʳ��
		public static final String getaTodayDiet = base + "/getTodayDietRmd";
		// ��ǰԪ��
		public static final String getaTodayDietEva = base + "/getTodayDietRmdEva";

		// ����̬
		public static final String publish = base + "/user/social";
		// ��ȡȫ����ȫ�ж�̬����
		public static final String getSocialData = base + "/getSocialData";
		// ���˶�̬��ȡ��ɾ��
		public static final String getPersonData = base + "/social/my";
		public static final String deletePersonData = base + "/social/delete";
		
		//������
		public static final String getStoreMenu = base + "/getStoreMenu";
		
		//ͬ��Լ
		public static final String activity = base + "/activity";
		/**   type =
		 * bulid �����»
		 * locate �ϴ��û�λ�ó���
		 * delete ɾ���
		 * update ����ĳ����ļ��
		 * city_user ��ȡ��������
		 * city_act ��ȡ���л
		 * user_act ��ȡ�û������Ļ
		 * detail ���ϸ
		 * join ����
		 */
		
	}

}