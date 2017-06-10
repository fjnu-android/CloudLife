package com.cloudlife.food;

import net.sf.json.JSONObject;

/**
 * @brief 菜品的数据结构
 * 
 * @author wuyi 
 * 
 */

public class DishData {

	// 菜品名
	private String name;
	// 菜品种类
	private String dishType;
	// 适宜人群
	private String man_suit;
	// 禁忌人群
	private String man_unsuit;
	// 营养价值
	private String effect;
	// 体质类型
	private String bodyType;
	// 营养价值列表
	private float fProtein; // 蛋白质
	private float fCarbohydrate; // 碳水化合物
	private float fDF; // 膳食纤维
	private float fCarotene; // 胡萝卜素
	private float fVitaminA; // 维生素A
	private float fVitaminC; // 维生素C
	private float fVitaminE; // 维生素E
	private float fNa; // 钠
	private float fFe; // 铁
	private float fCa; // 钙
	private float fPower; // 热量
	private float fFat; // 脂肪
	
	private String imgUrl; // 图片链接
	
	private int nScore; // 得分
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
	
	// 用来手动测试  不连接数据库
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
		// 初始化时 所有菜品都是5分
		nScore = 5; 
		// value是营养价值表 为json数据 单独解析
		JSONObject json = JSONObject.fromObject(value);
		if (!json.isNullObject()) {
			fProtein = Float.parseFloat(check(json.getString("蛋白质(克)")));
			fCarbohydrate = Float.parseFloat(check(json.getString("碳水化合物(克)")));
			fDF = Float.parseFloat(check(json.getString("膳食纤维(克)")));
			fCarotene = Float.parseFloat(check(json.getString("胡萝卜素(微克)")));
			fVitaminA = Float.parseFloat(check(json.getString("维生素A(微克)")));
			fVitaminC = Float.parseFloat(check(json.getString("维生素C")));
			fVitaminE = Float.parseFloat(check(json.getString("维生素E(毫克)")));
			fNa = Float.parseFloat(check(json.getString("钠(毫克)")));
			fFe = Float.parseFloat(check(json.getString("铁(毫克)")));
			fCa = Float.parseFloat(check(json.getString("钙(毫克)")));
			fPower = Float.parseFloat(check(json.getString("热量(大卡)")));
			fFat = Float.parseFloat(check(json.getString("脂肪")));
		}
		
	}
	
	private String check(String str) {
		if (str==null || str.equals(""))
			return "0";
		return str;
	}
	
	public float getDataByName(String name) {
		if (name.equals("蛋白质")) {
			return this.fProtein;
		} else if (name.equals("热量")) {
			return (float) (this.fPower*4.184);
		} else if (name.equals("碳水化合物")) {
			return this.fCarbohydrate;
		} else if (name.equals("钙")) {
			return this.fCa;
		} else if (name.equals("铁")) {
			return this.fFe;
		} else if (name.equals("钠")) {
			return this.fNa;
		} else if (name.equals("脂肪")) {
			return this.fFat;
		} else if (name.equals("膳食纤维")) {
			return this.fDF;
		} else if (name.equals("维生素A")) {
			return this.fVitaminA;
		} else if (name.equals("维生素E")) {
			return this.fVitaminE; 
		} else if (name.equals("维生素C")) {
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












