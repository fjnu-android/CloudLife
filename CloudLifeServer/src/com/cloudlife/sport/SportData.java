package com.cloudlife.sport;

public class SportData {

	private float power; // 记录运动消耗的卡路里信息
	private String name; 
	private int time; // 运动时长
	private float k; // 运动指数k
	private String place; // 运动适合的场所
	
	private int consult_time =0;// 参考时长
	
	public SportData(String name, float k,String place, int time) {
		this.name = name;
		this.place = place;
		this.k = k;
		this.consult_time = time;
	}
	
	public int getConsultTime() {
		return this.consult_time;
	}
	
	public float getPower() {
		return power;
	}
	
	public void setPower(float power) {
		this.power = power;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public String getName() {
		return name;
	}

	// 判断是否适合户外运动
	public boolean isOutdoorSport() {
		return this.place.equals("户外");
	}
	
	// 获取运动指数k
	public float getK() {
		return this.k;
	}
}













