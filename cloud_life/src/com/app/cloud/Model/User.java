package com.app.cloud.Model;

/**
 * 用户类
 */
import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;

import android.graphics.Bitmap;

public class User {

	private String icon ="";

	private String phone;
	private String password;
	private String name = "无名侠";
	private String sign = "你太懒，什么也没留下";
	private String weight = "0";
	private String height = "0";
	private String sex = "不详"; // f or m
	private String bodytype = "未测试";
	private String birthday = "1995-01-01";
	private String city = "福建-福州";
	private String cookie = "";

	//定位经纬度，城市
	public String location;
	public String locationCity;
	public LatLonPoint lp;
	
	// 天气
	public JSONObject weather;

	// 职业
	private String work = "学生";

	// 模糊后的壁纸
	public Bitmap wallBlurs;

	// 默认未登陆
	private boolean isLogin = false;

	// 唯一登陆实例
	static private User customer = null;

	// 获得实例
	static public User getInstance() {
		if (User.customer == null) {
			User.customer = new User();
		}
		return User.customer;
	}

	public User() {
	}

	//////////////////////////////////////////////////////////////
	/// 参数的获取与设置

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (name.equals(""))
			this.name = "无名侠";
		else
			this.name = name;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		if (sign.equals(""))
			this.sign = "你太懒，什么也没留下。";
		else
			this.sign = sign;
	}

	public String getsex() {
		return this.sex;
	}

	// 返回服务器 f m
	public String getSex() {

		if (sex.equals("男"))
			return "m";
		else if (sex.equals("女"))
			return "f";
		else
			return "";
	}

	public void setsex(String sex) {
		this.sex = sex;
	}

	public void setSex(String sex) {
		if (sex.equals("m"))
			this.sex = "男";
		else if (sex.equals("f"))
			this.sex = "女";
		else
			this.sex = "不详";
	}

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		if (weight.equals(""))
			this.weight = "0";
		else
			this.weight = weight;
	}

	public String getHeight() {
		return this.height;
	}

	public void setHeight(String height) {
		if (height.equals(""))
			this.height = "0";
		else
			this.height = height;
	}

	public String getBirth() {
		return this.birthday;
	}

	public void setBirth(String birth) {
		if (birth.equals(""))
			this.birthday = "1995-1-1";
		else
			this.birthday = birth;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		if (city.equals(""))
			this.city = "福建-福州";
		else
			this.city = city;
	}

	//返回汉字
	public String getBodytype() {
		return this.bodytype;
	}

	// 返回服务器ABCD
	public String getBodyType() {

		switch (this.bodytype) {
		case "平和质":
			return "A";
		case "气虚质":
			return "B";
		case "阳虚质":
			return "C";
		case "阴虚质":
			return "D";
		case "痰湿质":
			return "E";
		case "湿热质":
			return "F";
		case "血瘀质":
			return "G";
		case "气郁质":
			return "H";
		case "特禀质":
			return "I";
		default:
		}
		return "";
	}

	public void setBodytype(String body) {

		switch (body) {
		case "A":
			this.bodytype = "平和质";
			break;
		case "B":
			this.bodytype = "气虚质";
			break;
		case "C":
			this.bodytype = "阳虚质";
			break;
		case "D":
			this.bodytype = "阴虚质";
			break;
		case "E":
			this.bodytype = "痰湿质";
			break;
		case "F":
			this.bodytype = "湿热质";
			break;
		case "G":
			this.bodytype = "血瘀质";
			break;
		case "H":
			this.bodytype = "气郁质";
			break;
		case "I":
			this.bodytype = "特禀质";
			break;
		case "":
			this.bodytype = "未测试";
			break;
		}
	}

	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public Boolean getLogin() {
		return this.isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

}
