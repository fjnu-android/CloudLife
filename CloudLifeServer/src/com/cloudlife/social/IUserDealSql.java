package com.cloudlife.social;

public interface IUserDealSql {

	// 用户发表动态
	boolean postDynamic(String uid, String content,String img);
	
	// 获取同个城市的帖子内容
	String getCityBynamic(String city, int iBegin, int iEnd,long ts);
	
	// 获取全国的帖子内容
	String getCityBynamic(int iBegin, int iEnd, long ts);
	
	// 获取自己发表的所有动态内容
	String getMyDynamicData(String uid, int iBegin, int iEnd);
	
	// 删除自己指定的圈子内容
	boolean deleteMyDynamicData(String uid, String sid);
}




