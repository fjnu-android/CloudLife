package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.food.DishData;
import com.cloudlife.food.FoodSqlImp;
import com.cloudlife.food.IFoodSql;

/**
 * @brief ���ݹ�����    �����ݿ��Ӧ�ò����н�
 * @author wuyi
 *
 */
public class DataFactory {

	/**
	 *  ˵��: Ϊ�˷������  �����еĲ�Ʒ���ݴ����һ���б�����
	 *  	��ӱ�Ǳ��������ֿ��߲� ���� ˮ�� ���� �������б���±귶Χ
	 */
	static private List<List<DishData>> m_Data = new ArrayList<List<DishData>>(); //������еĲ�Ʒ����
	
	
	static private int[] m_iMeet = new int[2]; // 0-m_iMeet-1Ϊ����
	static private int[] m_iVeg = new int[2]; // m_iMeet - m_iVeg-1Ϊ�߲���
	static private int[] m_iFruit = new int[2]; // m_iVeg-m_iFruit-1Ϊˮ����
	static private int[] m_iBean = new int[2]; // m_iFruit-m_iBean-1���涹��
	static private int[] m_iSeaFood = new int[2]; // ˮ��/����
	static public enum DataType{Meet, Veg, Fuit, Bean, SeaFood,ERROR};
	static public String[] DataTypeCn = 
			new String[]{"����", "�߲�","ˮ��", "���", "����", "����"};
	
	private final String[] m_mealName= new String[]{"breakfast","lunch","dinner"};
	
	private IFoodSql m_Sql = new FoodSqlImp();
	//static private DataFactory m_Fac;
	
	static public DataFactory getInstance() {
		//if (m_Fac== null)
		DataFactory	m_Fac = new DataFactory();
		return m_Fac;
	}
	
	static private int flag =0;
	private DataFactory() {
		
		if (flag !=0)
			return;
		++flag;
		getDataFromDB();
		getMenuDataFromDB();
		System.out.println("data:========="+ m_Data.size());
	}
	
	// �����ݿ��ȡ������� ����Ǻ�����±����
	private boolean getDataFromDB() {
		List<DishData> data = new ArrayList<DishData>();
		data = m_Sql.getDishDataByFoodType("����");
		
		m_iMeet[0] = data.size();
		System.out.println("meet size:"+ m_iMeet);
		List<DishData> tmp= m_Sql.getDishDataByFoodType("�߲�");
		data.addAll(tmp);
		m_iVeg[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("ˮ��");
		data.addAll(tmp);
		m_iFruit[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("���");
		data.addAll(tmp);
		m_iBean[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("����");
		data.addAll(tmp);
		m_iSeaFood[0] = data.size();
		m_Data.add(data);
		return true;
	}

	
	// �����ݿ��ȡ������� ����Ǻ�����±����
	private boolean getMenuDataFromDB() {
		List<DishData> data = new ArrayList<DishData>();
		data = m_Sql.getMenuDataByFoodType("����");

		m_iMeet[1] = data.size();
		List<DishData> tmp= m_Sql.getMenuDataByFoodType("�߲�");
		data.addAll(tmp);
		m_iVeg[1] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("ˮ��");
		data.addAll(tmp);
		m_iFruit[1] = data.size();
		
		tmp = m_Sql.getMenuDataByFoodType("���");
		data.addAll(tmp);
		m_iBean[1] = data.size();
		
		tmp = m_Sql.getMenuDataByFoodType("����");
		data.addAll(tmp);
		m_iSeaFood[1] = data.size();
		m_Data.add(data);
		return true;
	}
		
	// �������ݵ��±귵�ز�Ʒ������
	public DataType getDataIndexRange(int index, int type) {
		if (index>=m_iBean[type]) {
			return DataType.SeaFood;
		} else if (index>=m_iFruit[type]) {
			return DataType.Bean;
		} else if (index>= m_iVeg[type]) {
			return DataType.Fuit;
		} else if (index>= m_iMeet[type]) {
			return DataType.Veg;
		} else if (index >=0) {
			return DataType.Meet;
		}
		return DataType.ERROR; // �±�Խ�����
	}
	
	// �����û�ָ������������  �������һ�ֲ�Ʒ ���ز�Ʒ���ڵ��±�
	private int getDishDataIndexByType(DataType t, int type) {
		int index = 0, iMin=0, iMax=0;
		if (t.equals(DataType.Meet)) {
			iMin = 0; iMax = m_iMeet[type]-1;
		} else if (t.equals(DataType.Veg)) {
			iMin = m_iMeet[type] ; iMax = m_iVeg[type] -1;
		} else if (t.equals(DataType.Fuit)) {
			iMin = m_iVeg[type]; iMax = m_iFruit[type]-1;
		} else if (t.equals(DataType.Bean)) {
			iMin = m_iFruit[type]; iMax = m_iBean[type]-1;
		} else if (t.equals(DataType.SeaFood)) {
			iMin = m_iBean[type]; iMax = m_iSeaFood[type]-1;
		}
		index = (int) (Math.random()*(iMax-iMin) + iMin);
		return index;
	}
	
	// �����û�ָ������������  �������һ�ֲ�Ʒ ���ز�Ʒ���ڵ��±�
	// ���� �������û�ָ������������
		private int getDishDataIndexByType(DataType t, String bodyType, int type) {
			int index = 0, iMin=0, iMax=0;
			if (t.equals(DataType.Meet)) {
				iMin = 0; iMax = m_iMeet[type]-1;
			} else if (t.equals(DataType.Veg)) {
				iMin = m_iMeet[type] ; iMax = m_iVeg[type] -1;
			} else if (t.equals(DataType.Fuit)) {
				iMin = m_iVeg[type]; iMax = m_iFruit[type]-1;
			} else if (t.equals(DataType.Bean)) {
				iMin = m_iFruit[type]; iMax = m_iBean[type]-1;
			} else if (t.equals(DataType.SeaFood)) {
				iMin = m_iBean[type]; iMax = m_iSeaFood[type]-1;
			}
			if (bodyType =="")
				index = (int) (Math.random()*(iMax-iMin) + iMin);
			else {
				List<Integer> iListTmp = new ArrayList<Integer>();
				for (int i =iMin; i<=iMax; ++i) {
					if (m_Data.get(type).get(i).getBodyType().contains(bodyType))
						iListTmp.add(i);
				}
				if(iListTmp.size() == 0)
					index = (int) (Math.random()*(iMax-iMin) + iMin);
				else {
					index = (int) (Math.random()*iListTmp.size());
					if (index >= iListTmp.size())
						index = iListTmp.size()-1;
					index = iListTmp.get(index);
				}
			}

			return index;
		}
	
	/**
	 * @brief ��ȡ��index�±��Ӧ�Ĳ�Ʒ��ͬ����Ĳ�Ʒ
	 * @param index
	 * @return �µ���������Ĳ�Ʒ�±�
	 */
	public int getReplaceDish(int index, int type) {
		return getDishDataIndexByType(getDataIndexRange(index, type), type);
	}
	
	public DishData getDecodeData(int index, int type) {
		if (index>m_Data.get(type).size()-1|| index<0)
			return null;
		return m_Data.get(type).get(index);
	}
	
	public Individual getIndividual2(UserModel model) {
		Individual indiv = new Individual();
		List<List<Integer>> listRet = new ArrayList<List<Integer>>();
		List<Integer> recoder = new ArrayList<Integer>();

		// ������������
		// �����޸����͵�����һ�� �����Ǹ������ʵĲ�Ʒ��������ڵĻ���
		for (int i =0; i< 3; ++i) {
			List<Integer> tmp = new ArrayList<Integer>();
			for (int j =0; j< model.getMeal(i+1).size();++j) {
				int iRnd =0;
				if (i == 1) {
					int nCnt = model.getMeal(i+1).get(j).nCnt;
					iRnd = (int) (Math.random()*(nCnt-1));	
				}
				for (int k=0; k< model.getMeal(i+1).get(j).nCnt;) {
					int index = 0;
					if (i == 1&& iRnd == k) {
						index = getDishDataIndexByType(model.getMeal(i+1).get(j).type, "yi_"+model.getBodyType(), model.getType());
					} else
						index = getDishDataIndexByType(model.getMeal(i+1).get(j).type, model.getType());
					if (!recoder.contains(index)) {
						tmp.add(index);
						recoder.add(index);
						++k;
					}
				}
			}
			listRet.add(tmp);
		}
		indiv.setGenes(listRet);
		return indiv;
	}
	
	public String getEvaluate(Population popPare,UserModel model, int n) {
		// �������Ӫ���صĺ���
		JSONObject jsonEva = new JSONObject();
		for (int i =0; i< n; ++i) {
			// �������Ϊ ����+����+Ӫ����ֵ
			JSONObject jsonEvaDtl = new JSONObject();
			float power =0, protein=0, fat =0, carbohydrate =0;
			float DF = 0;
			// ��������
			Individual indiv = popPare.getIndividual(i);
			for (int j=0; j< indiv.genesSize(); ++j) {
				for (int k = 0; k< indiv.getGenesDetail(j).size(); ++k) {
					DishData dish = getDecodeData(indiv.getGenesDetail(j).get(k), model.getType());
					float  weight= model.getWeight(j+1, getDataIndexRange(indiv.getGenesDetail(j).get(k), model.getType()));
					power+= dish.getPower()*weight;
					protein += dish.getProtein()*weight;
					fat += dish.getFat()*weight;
					carbohydrate += dish.getCarbohydrate()*weight;
					DF += dish.getDF()*weight;
				}
			}
			jsonEvaDtl.put("power", power*4.184);
			jsonEvaDtl.put("protein", protein);
			jsonEvaDtl.put("fat", fat);
			jsonEvaDtl.put("df", DF);
			jsonEvaDtl.put("carbohydrate", carbohydrate+ model.getRiceWeight()*1.0/100 *25.9);
			jsonEva.accumulate("evaluate", jsonEvaDtl);
		}
		JSONObject jsonStd = new JSONObject();
		jsonStd.put("power", model.getPower());
		jsonStd.put("protein", model.getProtein());
		jsonStd.put("fat", model.getFat());
		jsonStd.put("carbohydrate", model.getCarbohydrate());
		jsonStd.put("DF", model.getDF());
		jsonEva.put("std", jsonStd);
		return jsonEva.toString();
	}
	
	public String getPlan(Population popPare, int n, UserModel model) {
		
		// ��ǰn���Ƽ�����
		JSONObject jsonMain = new JSONObject();
		for (int i =0; i< n; ++i) {
			// �������Ϊ ����+����+Ӫ����ֵ
			JSONObject jsonDetail = new JSONObject();
			// ��������
			Individual indiv = popPare.getIndividual(i);
			for (int j=0; j< indiv.genesSize(); ++j) {
				JSONObject tmp = new JSONObject();
				for (int k = 0; k< indiv.getGenesDetail(j).size(); ++k) {
					JSONObject tmpEve = new JSONObject();
					DishData dish = getDecodeData(indiv.getGenesDetail(j).get(k), model.getType());
					tmpEve.put("name", dish.getName());
					tmpEve.put("bodyType", dish.getBodyType());
					tmpEve.put("effect", dish.getEffect()!=null&&dish.getEffect().length()>30?dish.getEffect().substring(0,30):dish.getEffect());
					tmpEve.put("img", dish.getImgUrl());
					tmp.accumulate(model.getType()==0?"dish":"menu", tmpEve);
				}
				jsonDetail.put(m_mealName[j], tmp);
			}
			jsonMain.accumulate("plan", jsonDetail);
		}
		
		return jsonMain.toString();
	}
	
}












