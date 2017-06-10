package com.cloudlife.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqlInit {
	
	final String m_strCreateUser = "create table if not exists user_info (uid int auto_increment primary key,"
				+ "name nvarchar(20), phone varchar(11), password varchar(13) not null"
				+",age int null, weight float null, height int null, bodyType nvarchar(8) null)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	final String m_strCreateDb = "create database if not exists cloud_life;";
	
	private Statement m_Statement;
	private Connection m_Connection;
	private String m_driver;
	private String m_url;
	private String m_user;
	private String m_pass;
	
	static private SqlInit m_SqlInit;
	
	static public SqlInit getInstance() {
		if (m_SqlInit == null)
			m_SqlInit = new SqlInit();
		return m_SqlInit;
	}
	
	private SqlInit() {
		// 完成各个数据库的初始化
		try {
			initParam("sqlparam.ini");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// 创建相关数据库
		try {
			Class.forName(m_driver);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			m_Connection = DriverManager.getConnection(m_url, m_user, m_pass);
			m_Statement = m_Connection.createStatement();
			System.out.println("m_Statement: "+ m_Statement);
			// 先创建数据库
			//m_Statement.executeUpdate(m_strCreateDb);
			//创建数据表
			int x = m_Statement.executeUpdate(m_strCreateUser);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				m_Statement.close();
				m_Connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void initParam(String paramFile) throws Exception{
	/*	Properties props = new Properties();
		props.load(new FileInputStream(paramFile));
		m_driver = props.getProperty("driver");
		m_url = props.getProperty("url");
		m_user = props.getProperty("user");
		m_pass = props.getProperty("pass");*/
		
		// 注意url的写法  要自己设置编码 否则会按照系统编码 可能出现中文乱码现象
		m_driver= "com.mysql.jdbc.Driver";
		m_url="jdbc:mysql://115.28.182.181:3306/cloud_life?useUnicode=true&characterEncoding=utf8";
		m_user="root";
		m_pass="123456cloudlife";
	}
	
	public String getDriver() {
		return m_driver;
	}
	
	public String getUrl() {
		return m_url;
	}
	
	public String getUser() {
		return m_user;
	}
	
	public String getPass() {
		return m_pass;
	}
	
}
