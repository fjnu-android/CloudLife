package com.app.cloud.Base;

import android.os.Environment;

/**
 * 接口 地址
 *
 */
public final class C {

	//城市天气接口 返回json数据
	public static final String weather = "http://op.juhe.cn/onebox/weather/query?cityname=";
	//聚合数据的key
	public static final String weather_key = "&key=b4fdfc5741d456503ab95fe2afde0c56";
	////////////////////////////////////////////////////////////////////////
	// core settings (important)

	/**
	 * 文件路径
	 *
	 */
	public static final class dir {
		// 应用根路径
		public static final String base = Environment.getExternalStorageDirectory().getPath() + "/cloudlife";

		// 菜品/菜谱
		public static final String foodImg = base + "/foodImg";
		// 用户拍摄图片保存路径
		public static final String user = base + "/user";
		// 圈子的头像缓存
		public static final String icon = base + "/icon";
		// 圈子的图片缓存
		public static final String imgCache = base + "/imgCache";

		// 账号密码 shared 文件名
		public static final String shared = "user_data";
				
		
	}

	/**
	 * cloud_life服务器接口
	 *
	 */
	public static final class api {
		// "http://hellojava.nat123.net/CloudLife"
		public static String base = "http://hellojava.nat123.net/CloudLife";
		
		//登陆和注册
		public static final String register = base + "/user/register";
		public static final String login = base + "/user/login";
		
		//个人信息上传
		public static final String info = base + "/user/info";
		
		//TODO 密码修改
		public static final String password = base + "/user/password";
		
		//体质上传
		public static final String body = base + "/user/bodyType";

		//用户头像上传
		public static final String userIcon = base + "/user/icon_update";

		//个人数据模型设置和获取
		public static final String setModel = base + "/modifyModelData";
		public static final String getModel = base + "/getModelData";

		// 搜索食材和食谱
		public static final String getDietData = base + "/getDietData";

		// 获取当天运动
		public static final String getaTodaySport = base + "/getTodaySportRmd";

		// 获取推荐食品和食谱
		public static final String getaTodayDiet = base + "/getTodayDietRmd";
		// 当前元素
		public static final String getaTodayDietEva = base + "/getTodayDietRmdEva";

		// 发表动态
		public static final String publish = base + "/user/social";
		// 获取全国，全市动态数据
		public static final String getSocialData = base + "/getSocialData";
		// 个人动态获取和删除
		public static final String getPersonData = base + "/social/my";
		public static final String deletePersonData = base + "/social/delete";
		
		//附近购
		public static final String getStoreMenu = base + "/getStoreMenu";
		
		//同城约
		public static final String activity = base + "/activity";
		/**   type =
		 * bulid 发布新活动
		 * locate 上传用户位置城市
		 * delete 删除活动
		 * update 更新某个活动的简介
		 * city_user 获取附近的人
		 * city_act 获取城市活动
		 * user_act 获取用户发布的活动
		 * detail 活动详细
		 * join 加入活动
		 */
		
	}

}