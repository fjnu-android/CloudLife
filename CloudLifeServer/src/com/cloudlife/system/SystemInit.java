package com.cloudlife.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemInit implements ServletContextListener{

	//����ϵͳ�ر�ʱ����
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	
		
	}

	// ����ϵͳ��ʼ������
	// ������ݿ�ĳ�ʼ�� �� ��س�ʼ������
	/**
	 *  1. ����û����ݱ�Ĵ���
	 *  2. ��ɲ˵���Ĵ���
	 *  
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		SqlInit sql = SqlInit.getInstance();
		
	}

}
