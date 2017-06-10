package com.cloudlife.food;

import net.sf.json.JSONObject;

/**
 * @brief ��Ʒ�����ݽṹ
 * 
 * @author wuyi 
 * 
 */

public class DishData {

	// ��Ʒ��
	private String name;
	// ��Ʒ����
	private String dishType;
	// ������Ⱥ
	private String man_suit;
	// ������Ⱥ
	private String man_unsuit;
	// Ӫ����ֵ
	private String effect;
	// ��������
	private String bodyType;
	// Ӫ����ֵ�б�
	private float fProtein; // ������
	private float fCarbohydrate; // ̼ˮ������
	private float fDF; // ��ʳ��ά
	private float fCarotene; // ���ܲ���
	private float fVitaminA; // ά����A
	private float fVitaminC; // ά����C
	private float fVitaminE; // ά����E
	private float fNa; // ��
	private float fFe; // ��
	private float fCa; // ��
	private float fPower; // ����
	private float fFat; // ֬��
	
	private String imgUrl; // ͼƬ����
	
	private int nScore; // �÷�
	public void setScore(int n) {
		nScore = n;
	}
	public int getScore( ) {
		return nScore;
	}
	
	public int addScore() {
		nScore += 1;
		return nScore;
	}
	public int reduceScore() {
		nScore -= 1;
		return nScore;
	}
	
	public String getBodyType() {
		return bodyType;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	// �����ֶ�����  ���������ݿ�
	public DishData(String name, float power, float protein, float car) {
		this.name = name;
		this.fPower= power;
		this.fProtein = protein;
		this.fCarbohydrate = car;
	}
	
	public DishData(String name, String type,String effect, String man_suit, String man_unsuit,
			String value, String bodyType, String img) {
		this.name = name;
		this.dishType = type;
		this.man_suit = man_suit;
		this.man_unsuit = man_unsuit;
		this.effect = effect;
		this.bodyType = bodyType==null?"":bodyType;
		this.imgUrl = img;
		// ��ʼ��ʱ ���в�Ʒ����5��
		nScore = 5; 
		// value��Ӫ����ֵ�� Ϊjson���� ��������
		JSONObject json = JSONObject.fromObject(value);
		if (!json.isNullObject()) {
			fProtein = Float.parseFloat(check(json.getString("������(��)")));
			fCarbohydrate = Float.parseFloat(check(json.getString("̼ˮ������(��)")));
			fDF = Float.parseFloat(check(json.getString("��ʳ��ά(��)")));
			fCarotene = Float.parseFloat(check(json.getString("���ܲ���(΢��)")));
			fVitaminA = Float.parseFloat(check(json.getString("ά����A(΢��)")));
			fVitaminC = Float.parseFloat(check(json.getString("ά����C")));
			fVitaminE = Float.parseFloat(check(json.getString("ά����E(����)")));
			fNa = Float.parseFloat(check(json.getString("��(����)")));
			fFe = Float.parseFloat(check(json.getString("��(����)")));
			fCa = Float.parseFloat(check(json.getString("��(����)")));
			fPower = Float.parseFloat(check(json.getString("����(��)")));
			fFat = Float.parseFloat(check(json.getString("֬��")));
		}
		
	}
	
	private String check(String str) {
		if (str==null || str.equals(""))
			return "0";
		return str;
	}
	
	public float getDataByName(String name) {
		if (name.equals("������")) {
			return this.fProtein;
		} else if (name.equals("����")) {
			return (float) (this.fPower*4.184);
		} else if (name.equals("̼ˮ������")) {
			return this.fCarbohydrate;
		} else if (name.equals("��")) {
			return this.fCa;
		} else if (name.equals("��")) {
			return this.fFe;
		} else if (name.equals("��")) {
			return this.fNa;
		} else if (name.equals("֬��")) {
			return this.fFat;
		} else if (name.equals("��ʳ��ά")) {
			return this.fDF;
		} else if (name.equals("ά����A")) {
			return this.fVitaminA;
		} else if (name.equals("ά����E")) {
			return this.fVitaminE; 
		} else if (name.equals("ά����C")) {
			return this.fVitaminC;
		}
		return 0;
	}
	
	public float getVitaminA() {
		return fVitaminA;
	}
	
	public float getVitaminC() {
		return fVitaminC;
	}	
	
	public float getVitaminE() {
		return fVitaminE;
	}
	
	public float getCarotene() {
		return fCarotene;
	}
	
	public float getProtein() {
		return fProtein;
	}
	
	public float getCarbohydrate() {
		return fCarbohydrate;
	}
	
	public float getDF() {
		return fDF;
	}
	
	public float getNa() {
		return fNa;
	}
	
	public float getFe() {
		return fFe;
	}
	
	public float getCa() {
		return fCa;
	}
	
	public float getPower() {
		return fPower;
	}
	
	public float getFat() {
		return fFat;
	}
	
	public String getName() {
		return name;
	}
	
	public String getManSuit() {
		return man_suit;
	}
	
	public String getManUnSuit() {
		return man_unsuit;
	}
	
	public String getDishType() {
		return dishType;
	}
	
	public String getEffect() {
		return effect;
	}
	
}












