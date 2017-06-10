package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.cloudlife.nsga2.DataFactory.DataType;

/**
 * @brief 用户数据模型类  存放用户的每天饮食安排
 * @author wuyi
 *
 */
public class UserModel{

	private static final long serialVersionUID = 1L;
	
	// 每餐里面包含的类型
	static class MealData {
		public DataType type; // 菜品类型
		public float weight; // 菜品的重量需求
		public int nCnt;	// 菜品的个数
		public MealData(DataType type, float weight, int nCnt) {
			this.type = type;
			this.nCnt = nCnt;
			this.weight = weight;
		}
	}
	
	private float m_fPower; // 当前用户所需要的能量值 根据身高体重等信息进行计算
	private String m_bodyType; // 当前用户的身体体质类型
	private float m_fProtein; // 蛋白质的需求量
	private float m_fFat; // 脂肪
	private float m_fCarbohydrate; // 碳水化合物
	private float m_fDF; // 膳食纤维
	
	private float m_fVitaminA; // 维生素A的需求量
	private float m_fCa; // 钙的需求量
	private float m_fFe; // 铁的需求量
	private float m_fVitaminC; // 维生素C的含量
	
	private int type =0;
	
	private float m_riceWeight = 0;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public float getRiceWeight() {
		return this.m_riceWeight;
	}
	
	private final String[] works= new String[]{"计算机/互联网/通信",
			"生产/工艺/制造","医疗/护理/制药","金融/银行/投资/保险",
			"商业/服务业/个体经营","文化/广告/传媒","娱乐/艺术/表演",
			"律师/法务","教育/培训","公务员/行政/事业单位","模特","空姐"
			,"学生","其它高劳动力工作","其它低劳动力工作"};
	private final double[] palsBoy = new double[]{1.78,1.78,1.78,1.55,
			1.55, 1.78,2.10, 1.55, 1.55, 1.55, 1.55,1.55,1.78,22.1,1.78};
	private final double[] palsGirl = new double[]{1.64, 1.64, 1.64,1.56,
			1.56, 1.64, 1.82, 1.56, 1.56, 1.56,1.56,1.56,1.64,1.82,1.64};
	
	public String getBodyType() {
		return m_bodyType;
	}
	
	public float getFe() {
		return m_fFe;
	}
	
	public float getCa() {
		return m_fCa;
	}
	
	public float getPower() {
		return m_fPower;
	}
	
	public float getProtein() {
		return m_fProtein;
	}
	
	public float getVitaminA() {
		return m_fVitaminA;
	}
	
	public float getVitaminC() {
		return m_fVitaminC;
	}
	
	public float getFat() {
		return this.m_fFat;
	}
	
	public float getDF() {
		return this.m_fDF;
	}
	
	public float getCarbohydrate() {
		return this.m_fCarbohydrate;
	}
	
	
	List<MealData> m_Breakfast = new ArrayList<MealData>();
	List<MealData> m_Lunch = new ArrayList<MealData>();
	List<MealData> m_Dinner = new ArrayList<MealData>();
	
	public UserModel() {

	}
	
	/**
	 * @brief 解析json数据 获取用户的模型数据
	 * @param data 模型数据
	 * @return 是否解析成功
	 */
	public boolean resolveJsonData(String data) {
		JSONObject json = JSONObject.fromObject(data);
		if (json == null)
			return false;
		JSONObject bf = (JSONObject) json.get("breakfast");
		JSONObject ln = (JSONObject) json.get("lunch");
		JSONObject dn = (JSONObject) json.get("dinner");
		m_riceWeight = (float) json.getDouble("rice");
		
		System.out.println("米饭:"+m_riceWeight);
		
		if (bf==null || ln==null || dn==null)
			return false;
		JSONArray bfArr = null;
		try{
			bfArr = bf.getJSONArray("dish");
		} catch(JSONException e) {
		}
		
		if (bfArr == null) {
			try {
				bfArr = new JSONArray();
				bf = bf.getJSONObject("dish");
				bfArr.add(bf);
			} catch(JSONException e) {
			}
		}
		
		JSONArray lnArr = null;
		try {
			lnArr = ln.getJSONArray("dish");
		} catch(JSONException e) {
		}
		if (lnArr == null) {
			try {
				lnArr = new JSONArray();
				ln = ln.getJSONObject("dish");
				lnArr.add(ln);
			} catch(JSONException e) {
			}
		}
		
		JSONArray dnArr = null;
		try{
			dnArr = dn.getJSONArray("dish");
		} catch(JSONException e) {
		}
		if (dnArr == null) {
			try {
				dnArr = new JSONArray();
				dn = dn.getJSONObject("dish");
				dnArr.add(dn);
			} catch(JSONException e) {
			}
		}

		for (int i =0; i< bfArr.size(); ++i) {
			JSONObject tmp = (JSONObject) bfArr.get(i);
			for (int j=0; j< DataFactory.DataTypeCn.length; ++j) {
				if (DataFactory.DataTypeCn[j].equals(tmp.getString("type"))) {
					MealData meal = new MealData(DataType.values()[j], 
							(float) (tmp.getDouble("weight") /100.0),tmp.getInt("count"));
					m_Breakfast.add(meal);
				}
			}
		}
		
		for (int i =0; i< lnArr.size(); ++i) {
			JSONObject tmp = (JSONObject) lnArr.get(i);
			for (int j=0; j< DataFactory.DataTypeCn.length; ++j) {
				if (DataFactory.DataTypeCn[j].equals(tmp.getString("type"))) {
					MealData meal = new MealData(DataType.values()[j], 
							(float) (tmp.getDouble("weight") /100.0),tmp.getInt("count"));
					m_Lunch.add(meal);
				}
			}
		}
		
		for (int i =0; i< dnArr.size(); ++i) {
			JSONObject tmp = (JSONObject) dnArr.get(i);
			for (int j=0; j< DataFactory.DataTypeCn.length; ++j) {
				if (DataFactory.DataTypeCn[j].equals(tmp.getString("type"))) {
					MealData meal = new MealData(DataType.values()[j], 
							(float) (tmp.getDouble("weight") /100.0),tmp.getInt("count"));
					m_Dinner.add(meal);
				}
			}
		}
		
		return true;
	}


	/**
	 * @brief 解析用户的个人信息 计算各个营养值得目标值
	 * @param bodyType 用户的体质类型
	 * @param sex 用户的性别
	 * @param age 年龄
	 * @param weight 体重
	 * @param height 身高
	 * @param pal 体力活动水平
	 * @return 是否解析成功
	 */
	public boolean resolveUserInfo(String bodyType, String sex, int age, 
			float weight, float height, String work) {
		System.out.println("weight:"+weight+" height:"+height+" age:"+age);
		float bm = 0;
		float pal = 0;
		if (sex.equals("f")) {
			// 女性的BM计算公式
			bm = (float) (655+9.5*weight + 1.8*height-4.7*age);
			for (int i =0; i< works.length; ++i) {
				if (works[i].equals(work)) {
					pal = (float) palsGirl[i];
					break;
				}
			}
		} else {//13.7
			bm = (float) (66+12.7*weight +5.0*height-6.8*age);
			for (int i =0; i< works.length; ++i) {
				if (works[i].equals(work)) {
					pal = (float) palsBoy[i];
					break;
				}
			}
		}

		System.out.println("bm:"+ bm+" pal:"+ pal);
		m_fPower = bm*pal;
		m_bodyType = bodyType;
		
		// 计算蛋白质的值
		if (pal >1.8)
			m_fProtein = (float) (1.2*weight);
		else if (pal>1.7)
			m_fProtein = (float) (1.0*weight);
		else
			m_fProtein = (float) (0.9*weight);
		// 计算脂肪的值
	//	m_fFat = (float) (m_fPower*(Math.random()*10 +20)/100);
		m_fFat = (float) (weight*0.45);
		// 计算碳水化合物的值
		m_fCarbohydrate = (float) (150+Math.random()*100);
		
		// 计算膳食纤维的需求量g
		m_fDF = (float) (25+ Math.random()*10);
		return true;
	}
	
	/**
	 * @brief 获取三餐的数据
	 * @param mealTime 餐数 1为早餐 类推
	 * @return 请求餐的对应的数据
 	 */
	public List<MealData> getMeal(int mealTime) {
		if (mealTime==1)
			return m_Breakfast;
		else if (mealTime ==2)
			return m_Lunch;
		else
			return m_Dinner;
	}
	
	/**
	 * @brief 根据参数返回菜品需求的重量信息
	 * @param mealTime 餐序数 分别为 早餐为1 午餐为2 晚餐为3
	 * @param type
	 * @return
	 */
	public float getWeight(int mealTime, DataFactory.DataType type) {
		if (mealTime == 1) {
			for (int i =0; i< m_Breakfast.size(); ++i) {
				if (m_Breakfast.get(i).type.equals(type))
					return m_Breakfast.get(i).weight;
			}
		} else if (mealTime == 2) {
			for (int i =0; i< m_Lunch.size(); ++i) {
				if (m_Lunch.get(i).type.equals(type))
					return m_Lunch.get(i).weight;
			}
		} else if (mealTime == 3) {
			for (int i =0; i< m_Dinner.size(); ++i) {
				if (m_Dinner.get(i).type.equals(type))
					return m_Dinner.get(i).weight;
			}
		}
		
		return 0;
	}
	
}

















