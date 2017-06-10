package com.cloudlife.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemInit implements ServletContextListener{

	//重载系统关闭时函数
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	
		
	}

	// 重载系统初始化函数
	// 完成数据库的初始化 和 相关初始化操作
	/**
	 *  1. 完成用户数据表的创建
	 *  2. 完成菜单表的创建
	 *  
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		SqlInit sql = SqlInit.getInstance();
		
	}

}
