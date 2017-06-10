package com.app.cloud.Model;

/**
 * 数据模型里的模型
 *
 */
public class Data {

	// 食物类型
	private String type;
	// 多少种
	private String kind;
	// 数量多少克
	private String num;

	public Data() {
	}

	public Data(String type, String kind, String num) {
		this.type = type;
		this.kind = kind;
		this.num = num;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNum() {
		return num;
	}

}
