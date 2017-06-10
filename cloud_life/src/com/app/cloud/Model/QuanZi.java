package com.app.cloud.Model;

import java.io.Serializable;

/**
 * 圈子数据模型
 */
public class QuanZi implements Serializable {

	/**
	 * bundle 接口id
	 */
	private static final long serialVersionUID = 1L;

	private String icon = null;
	private String name;
	private String time;
	private String city;
	private String content = null;
	private String image = null;

	// 个人删除用
	private String sid;

	public QuanZi() {

	}

	public void setIcon(String test) {
		this.icon = test;
	}

	public String getIcon() {
		return icon;
	}

	public void setName(String test) {
		this.name = test;
	}

	public String getName() {
		return name;
	}

	public void setTime(String test) {
		this.time = test;
	}

	public String getTime() {
		return time;
	}

	public void setCity(String test) {
		this.city = test;
	}

	public String getCity() {
		return city;
	}

	public void setContent(String test) {
		this.content = test;
	}

	public String getContent() {
		return content;
	}

	public void setImage(String test) {
		this.image = test;
	}

	public String getImage() {
		return image;
	}

	public void setSid(String test) {
		this.sid = test;
	}

	public String getSid() {
		return sid;
	}
}
