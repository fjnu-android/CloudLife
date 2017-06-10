package com.cloudlife.sport;

public class SportData {

	private float power; // ��¼�˶����ĵĿ�·����Ϣ
	private String name; 
	private int time; // �˶�ʱ��
	private float k; // �˶�ָ��k
	private String place; // �˶��ʺϵĳ���
	
	private int consult_time =0;// �ο�ʱ��
	
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

	// �ж��Ƿ��ʺϻ����˶�
	public boolean isOutdoorSport() {
		return this.place.equals("����");
	}
	
	// ��ȡ�˶�ָ��k
	public float getK() {
		return this.k;
	}
}













