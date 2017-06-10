package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.food.DishData;
import com.cloudlife.food.FoodSqlImp;
import com.cloudlife.food.IFoodSql;

/**
 * @brief 数据工厂类    与数据库和应用层做中介
 * @author wuyi
 *
 */
public class DataFactory {

	/**
	 *  说明: 为了方便起见  将所有的菜品数据存放在一个列表里面
	 *  	添加标记变量来区分开蔬菜 肉类 水果 谷类 等所在列表的下标范围
	 */
	static private List<List<DishData>> m_Data = new ArrayList<List<DishData>>(); //存放所有的菜品数据
	
	
	static private int[] m_iMeet = new int[2]; // 0-m_iMeet-1为肉类
	static private int[] m_iVeg = new int[2]; // m_iMeet - m_iVeg-1为蔬菜类
	static private int[] m_iFruit = new int[2]; // m_iVeg-m_iFruit-1为水果类
	static private int[] m_iBean = new int[2]; // m_iFruit-m_iBean-1乳面豆类
	static private int[] m_iSeaFood = new int[2]; // 水产/海鲜
	static public enum DataType{Meet, Veg, Fuit, Bean, SeaFood,ERROR};
	static public String[] DataTypeCn = 
			new String[]{"肉类", "蔬菜","水果", "五谷", "海鲜", "错误"};
	
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
	
	// 从数据库获取相关数据 并标记好相关下标变量
	private boolean getDataFromDB() {
		List<DishData> data = new ArrayList<DishData>();
		data = m_Sql.getDishDataByFoodType("肉类");
		
		m_iMeet[0] = data.size();
		System.out.println("meet size:"+ m_iMeet);
		List<DishData> tmp= m_Sql.getDishDataByFoodType("蔬菜");
		data.addAll(tmp);
		m_iVeg[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("水果");
		data.addAll(tmp);
		m_iFruit[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("五谷");
		data.addAll(tmp);
		m_iBean[0] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("海鲜");
		data.addAll(tmp);
		m_iSeaFood[0] = data.size();
		m_Data.add(data);
		return true;
	}

	
	// 从数据库获取相关数据 并标记好相关下标变量
	private boolean getMenuDataFromDB() {
		List<DishData> data = new ArrayList<DishData>();
		data = m_Sql.getMenuDataByFoodType("肉类");

		m_iMeet[1] = data.size();
		List<DishData> tmp= m_Sql.getMenuDataByFoodType("蔬菜");
		data.addAll(tmp);
		m_iVeg[1] = data.size();
		
		tmp = m_Sql.getDishDataByFoodType("水果");
		data.addAll(tmp);
		m_iFruit[1] = data.size();
		
		tmp = m_Sql.getMenuDataByFoodType("五谷");
		data.addAll(tmp);
		m_iBean[1] = data.size();
		
		tmp = m_Sql.getMenuDataByFoodType("海鲜");
		data.addAll(tmp);
		m_iSeaFood[1] = data.size();
		m_Data.add(data);
		return true;
	}
		
	// 根据数据的下标返回菜品的类型
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
		return DataType.ERROR; // 下标越界错误
	}
	
	// 根据用户指定的数据类型  随机产生一种菜品 返回菜品所在的下标
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
	
	// 根据用户指定的数据类型  随机产生一种菜品 返回菜品所在的下标
	// 并且 还根据用户指定的体质类型
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
	 * @brief 获取跟index下标对应的菜品相同种类的菜品
	 * @param index
	 * @return 新的随机产生的菜品下标
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

		// 生成三餐数据
		// 新增修改项：午餐的其中一个 必须是改善体质的菜品（如果存在的话）
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
		// 计算各个营养素的含量
		JSONObject jsonEva = new JSONObject();
		for (int i =0; i< n; ++i) {
			// 存放数据为 菜名+体质+营养价值
			JSONObject jsonEvaDtl = new JSONObject();
			float power =0, protein=0, fat =0, carbohydrate =0;
			float DF = 0;
			// 三餐数据
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
		
		// 将前n个推荐返回
		JSONObject jsonMain = new JSONObject();
		for (int i =0; i< n; ++i) {
			// 存放数据为 菜名+体质+营养价值
			JSONObject jsonDetail = new JSONObject();
			// 三餐数据
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












