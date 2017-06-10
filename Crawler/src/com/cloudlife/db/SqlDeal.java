package com.cloudlife.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cloudlife.system.SqlInit;

public class SqlDeal {

	/**
	 * 将数据库的初始化 驱动放在此处 方便修改相关参数
	 * 
	 */
	private Connection m_Connection;
	
	private String m_driver;
	private String m_url;
	private String m_user;
	private String m_pass;
	
	public SqlDeal() { 
	
		try {
			SqlInit sql = SqlInit.getInstance();
			m_driver = sql.getDriver();
			m_url = sql.getUrl();
			m_user = sql.getUser();
			m_pass = sql.getPass();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sql init file path has error!");
			return;
		}
		
		try {
			Class.forName(m_driver);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			m_Connection = DriverManager.getConnection(m_url, m_user, m_pass);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 
	public Connection getConnection() {
		return m_Connection;
	}
	
	// 关闭连接
	public void closeSql() {
		try {
			m_Connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}











