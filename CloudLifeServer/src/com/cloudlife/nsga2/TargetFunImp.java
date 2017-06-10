package com.cloudlife.nsga2;

import com.cloudlife.food.DishData;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

/**
 * @brief �����Ŀ�꺯����ʵ��  һ��Ŀ�꺯���̳�һ��ITargetFun�ӿ�
 * 		Ŀ�꺯���Ķ��� fun(indiv)=absoulte(total(indiv.genes(i).power)-standard);
 *       Ŀ�꺯����ֵԽСԽ��
 *     ��Ϊÿ���û���Ӫ����׼����ֵ��һ����ͬ ���Բ��ʺϲ��õ���ģʽ���
 * @author wuyi
 *
 */
public class TargetFunImp {
	
	private ITargetFun[] m_targetFun;
	private DataFactory m_fac = DataFactory.getInstance();
	private int nTargetFun =5;// Ŀ�꺯���ĸ���
	private UserModel m_userModel;
	
	public ITargetFun[] getTargetFun() {
		return m_targetFun;
	}
	public TargetFunImp(UserModel model) {
		m_userModel = model;
		
		m_targetFun = new ITargetFun[nTargetFun];
		m_targetFun[0] = new FunGeneral("����", model.getPower(), model);
		m_targetFun[1] = new FunGeneral("������", model.getProtein(), model);
		m_targetFun[2] = new FunGeneral("��ʳ��ά", model.getDF(), model);
		m_targetFun[3] = new FunGeneral("֬��", model.getFat(), model);
	  	m_targetFun[4] = new FunGeneral("̼ˮ������", (float) (model.getCarbohydrate()
	  			 - model.getRiceWeight()*1.0/100 *25.9), model);
	}
	
	/**
	 * @breif ͨ��Ŀ�꺯��ģ����� ʹ��ʱ ֻ��Ҫ���� Ӫ����ֵ���ƺ�����������
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
					// Ŀ��100g����ֵ* ��������100gΪ��λ��// ����ͨ��ƥ�亯�� ������� 
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















