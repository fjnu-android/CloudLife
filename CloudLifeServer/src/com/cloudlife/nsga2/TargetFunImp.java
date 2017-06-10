package com.cloudlife.nsga2;

import com.cloudlife.food.DishData;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

/**
 * @brief 具体的目标函数的实现  一个目标函数继承一个ITargetFun接口
 * 		目标函数的定义 fun(indiv)=absoulte(total(indiv.genes(i).power)-standard);
 *       目标函数的值越小越好
 *     因为每个用户的营养标准需求值不一定相同 所以不适合采用单例模式设计
 * @author wuyi
 *
 */
public class TargetFunImp {
	
	private ITargetFun[] m_targetFun;
	private DataFactory m_fac = DataFactory.getInstance();
	private int nTargetFun =5;// 目标函数的个数
	private UserModel m_userModel;
	
	public ITargetFun[] getTargetFun() {
		return m_targetFun;
	}
	public TargetFunImp(UserModel model) {
		m_userModel = model;
		
		m_targetFun = new ITargetFun[nTargetFun];
		m_targetFun[0] = new FunGeneral("热量", model.getPower(), model);
		m_targetFun[1] = new FunGeneral("蛋白质", model.getProtein(), model);
		m_targetFun[2] = new FunGeneral("膳食纤维", model.getDF(), model);
		m_targetFun[3] = new FunGeneral("脂肪", model.getFat(), model);
	  	m_targetFun[4] = new FunGeneral("碳水化合物", (float) (model.getCarbohydrate()
	  			 - model.getRiceWeight()*1.0/100 *25.9), model);
	}
	
	/**
	 * @breif 通用目标函数模板设计 使用时 只需要传入 营养价值名称和需求量即可
	 * @author wuyi
	 *
	 */
	private class FunGeneral implements ITargetFun{

		private String m_name;
		private float user_need;
		private UserModel model;
		public FunGeneral(String name, float nNeed, UserModel model) {
			m_name = name;
			this.model = model;
			user_need = nNeed;
		}
		
		public float getUserNeed() {
			return user_need;
		}
		
		@Override
		public float fun(Individual indiv) {
			float ret = 0;
			for (int i =0; i< indiv.genesSize(); ++i) {
				for (int j =0; j< indiv.getGenesDetail(i).size(); ++j) {
					DishData tmp = m_fac.getDecodeData(indiv.getGenesDetail(i).get(j), model.getType());
					// 目标100g含量值* 需求量（100g为单位）// 采用通用匹配函数 方便计算 
					ret += tmp.getDataByName(m_name) * 
						m_userModel.getWeight(i+1, 
								m_fac.getDataIndexRange(indiv.getGenesDetail(i).get(j), model.getType()));
				}
			}	
			
			ret = 100 - ret /user_need *100;
			ret = ret >0? ret: -1*ret;
			return ret;
		}
	}
}















