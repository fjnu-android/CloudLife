package com.cloudlife.user.db;

import java.sql.ResultSet;
import java.util.HashMap;

/**
 * @brief 用户数据库操作接口类
 * @author wuyi
 *
 */
public interface IUserSql {
	
	// 添加新用户
	boolean addUser(String phone, String pass);
	
	// 添加/修改用户的资料
	boolean modifyData(String phone, String[] data);
	
	// 获取用户的资料 返回结果为Json数据
	String getData(String phone);
	
	// 更新用户的体质信息
	boolean updateBodyType(String phone, String type) ;
	
	// 查询该用户是否已注册 
	// 已注册返回true 否则为false
	boolean isExists(String phone);
	
	boolean modifySign(String phone, String newSign);
	
	// 修改用户的密码
	boolean changePass(String phone, String prePass, String newPass);
	
	// 用户登录
	int login(String phone, String pass);
	
	// 检查用户
	boolean check(String user, String cookie/*md5加密的用户账号和密码*/);

	// 获取用户模型数据
	String getModelData(String phone);
	
	// 修改用户的模型数据
	boolean modifyModelData(String phone, String data);
	
	// 通过菜品名称找到菜品信息
	String getDietDataByName(String name);
	
	// 只获取菜品数据
	String getDishDataByName(String type, String name);
	
	// 添加好友
	boolean addNewFriend(String uid, String friend_id);
	
	// 查找好友 成功则返回被查找人的基本信息 用户名 用户id 性别 签名 头像
	// 暂时只允许通过手机号码进行查找
	String findNewFriend(String type, String user);
	
	// 获取用户的好友列表信息
	String getFriendListData(String uid);
	
	// 删除好友
	boolean deleteFriend(String uid, String friend_id);
	
	boolean uploadUserIcon(String phone, String url);
	
	// 获取附近购商店菜品信息  根据不用用户随机出不同的值
	String getStoreFoodData(String uid);
}







