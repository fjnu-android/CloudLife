package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.cloudlife.nsga2.DataFactory.DataType;

/**
 * @brief �û�����ģ����  ����û���ÿ����ʳ����
 * @author wuyi
 *
 */
public class UserModel{

	private static final long serialVersionUID = 1L;
	
	// ÿ���������������
	static class MealData {
		public DataType type; // ��Ʒ����
		public float weight; // ��Ʒ����������
		public int nCnt;	// ��Ʒ�ĸ���
		public MealData(DataType type, float weight, int nCnt) {
			this.type = type;
			this.nCnt = nCnt;
			this.weight = weight;
		}
	}
	
	private float m_fPower; // ��ǰ�û�����Ҫ������ֵ ����������ص���Ϣ���м���
	private String m_bodyType; // ��ǰ�û���������������
	private float m_fProtein; // �����ʵ�������
	private float m_fFat; // ֬��
	private float m_fCarbohydrate; // ̼ˮ������
	private float m_fDF; // ��ʳ��ά
	
	private float m_fVitaminA; // ά����A��������
	private float m_fCa; // �Ƶ�������
	private float m_fFe; // ����������
	private float m_fVitaminC; // ά����C�ĺ���
	
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
	
	private final String[] works= new String[]{"�����/������/ͨ��",
			"����/����/����","ҽ��/����/��ҩ","����/����/Ͷ��/����",
			"��ҵ/����ҵ/���徭Ӫ","�Ļ�/���/��ý","����/����/����",
			"��ʦ/����","����/��ѵ","����Ա/����/��ҵ��λ","ģ��","�ս�"
			,"ѧ��","�������Ͷ�������","�������Ͷ�������"};
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
	 * @brief ����json���� ��ȡ�û���ģ������
	 * @param data ģ������
	 * @return �Ƿ�����ɹ�
	 */
	public boolean resolveJsonData(String data) {
		JSONObject json = JSONObject.fromObject(data);
		if (json == null)
			return false;
		JSONObject bf = (JSONObject) json.get("breakfast");
		JSONObject ln = (JSONObject) json.get("lunch");
		JSONObject dn = (JSONObject) json.get("dinner");
		m_riceWeight = (float) json.getDouble("rice");
		
		System.out.println("�׷�:"+m_riceWeight);
		
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
	 * @brief �����û��ĸ�����Ϣ �������Ӫ��ֵ��Ŀ��ֵ
	 * @param bodyType �û�����������
	 * @param sex �û����Ա�
	 * @param age ����
	 * @param weight ����
	 * @param height ���
	 * @param pal �����ˮƽ
	 * @return �Ƿ�����ɹ�
	 */
	public boolean resolveUserInfo(String bodyType, String sex, int age, 
			float weight, float height, String work) {
		System.out.println("weight:"+weight+" height:"+height+" age:"+age);
		float bm = 0;
		float pal = 0;
		if (sex.equals("f")) {
			// Ů�Ե�BM���㹫ʽ
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
		
		// ���㵰���ʵ�ֵ
		if (pal >1.8)
			m_fProtein = (float) (1.2*weight);
		else if (pal>1.7)
			m_fProtein = (float) (1.0*weight);
		else
			m_fProtein = (float) (0.9*weight);
		// ����֬����ֵ
	//	m_fFat = (float) (m_fPower*(Math.random()*10 +20)/100);
		m_fFat = (float) (weight*0.45);
		// ����̼ˮ�������ֵ
		m_fCarbohydrate = (float) (150+Math.random()*100);
		
		// ������ʳ��ά��������g
		m_fDF = (float) (25+ Math.random()*10);
		return true;
	}
	
	/**
	 * @brief ��ȡ���͵�����
	 * @param mealTime ���� 1Ϊ��� ����
	 * @return ����͵Ķ�Ӧ������
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
	 * @brief ���ݲ������ز�Ʒ�����������Ϣ
	 * @param mealTime ������ �ֱ�Ϊ ���Ϊ1 ���Ϊ2 ���Ϊ3
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

















