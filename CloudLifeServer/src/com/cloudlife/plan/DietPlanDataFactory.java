package com.cloudlife.plan;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.IndirectionException;

import com.cloudlife.food.DishData;
import com.cloudlife.food.FoodSqlImp;
import com.cloudlife.food.IFoodSql;

/**
 * @brief ��ʳ�Ƽ��㷨�е��������ɹ�����
 * 
 * @author wuyi
 *
 */
public class DietPlanDataFactory {

	
	// �򵥲������  ��ʱ�ȹ̶���������=======
	private IFoodSql m_foodSql = new FoodSqlImp();
	// ����߲˵����� ����
	private int m_nBreakfastVeg =1;
	private float m_fBreakfastVegKg = 100;
	// ����������  ����
	private int m_nBreakfasthMeet =1;
	private float m_fBreakfastMeetKg = 100;
	// ����߲˵����� ����
	private int m_nLunchVeg =2;
	private float m_fLunchVegKg = 100;
	// ����������  ����
	private int m_nLunchMeet =1;
	private float m_fLunchMeetKg = 100;
	// ���������� ����
	private int m_nDinnerMeet = 0;
	private float m_fDinnerMeetKg = 0;
	// ����߲˵����� ����
	private int m_nDinnerVeg = 2;
	private float m_fDinnerVegKg = 100;
	
	private int m_nFruit = 2; // ˮ�����Ƽ�
	private float m_fFruitKg = 2;
	
	private float[] m_fVegKg={1,1,1};
	private float[] m_fMeetKg={1,1,0};
	
	// ����������������  ��Ϊ����߲�  ��ʱ������
	private List<DishData> m_DataMeet ;
	private List<DishData> m_DataVeg ;
	private List<DishData> m_DataFruit;
	
	static private DietPlanDataFactory m_Factory;
	
	private IFoodSql m_Sql = new FoodSqlImp();
	
	static public DietPlanDataFactory getInstance() {
		if (m_Factory == null)
			m_Factory = new DietPlanDataFactory();
		return m_Factory;
	}
	
	private DietPlanDataFactory() {
		getDataFromDB();
	}
	
	private boolean getDataFromDB() {
		// �����ݿ��ȡ�������
		m_DataMeet = m_Sql.getDishDataByFoodType("����/����");
		m_DataVeg = m_Sql.getDishDataByFoodType("�߲���");
		m_DataFruit = m_Sql.getDishDataByFoodType("ˮ��/�ɹ�");
		if (m_DataMeet == null || m_DataVeg == null)
			return false;
		return true;
	}
	
	/**
	 * @brief ���׸������ɺ���
	 * @return ���ɵĽ��
	 */
	public DietPlanIndividual produceIndividual() {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		
		// ������������������������
		// Ϊ����������߲�   Ĭ��  �߲˵���� Ϊ �������+0 1 2 3 4.����
		// Ϊ��ֹ�ظ�ʹ��һ���� ��һ����¼
		List<Integer> recoder = new ArrayList<Integer>(); 
		// ���
		List<Integer> tmpBf = new ArrayList<Integer>();
		for (int i =0; i< m_nBreakfasthMeet; ++i) {
			int index = (int)(Math.random()*m_DataMeet.size());
			tmpBf.add(index);
			recoder.add(index);
		}
		for (int i =0; i< m_nBreakfastVeg; ++i) {
			int index = (int)(Math.random()*m_DataVeg.size());
			tmpBf.add(index + m_DataMeet.size());
			recoder.add(index + m_DataMeet.size());
		}
		ret.add(tmpBf);
		
		List<Integer> tmpLn = new ArrayList<Integer>();
		for (int i =0; i< m_nLunchMeet; ) {
			int index = (int)(Math.random()*m_DataMeet.size());
			if (!recoder.contains(index)) {
				tmpLn.add(index);
				recoder.add(index);
				++i;
			}
		}
		
		for (int i =0; i< m_nLunchVeg; ) {
			int index = (int)(Math.random()*m_DataVeg.size());
			if (!recoder.contains(index + m_DataMeet.size())) {
				tmpLn.add(index + m_DataMeet.size());
				recoder.add(index + m_DataMeet.size());
				++i;
			}
		}
		ret.add(tmpLn);
		
		List<Integer> tmpDn = new ArrayList<Integer>();
		for (int i =0; i< m_nDinnerMeet; ) {
			int index = (int)(Math.random()*m_DataMeet.size());
			if (!recoder.contains(index)) {
				tmpDn.add(index);
				recoder.add(index);
				++i;
			}
		}
		for (int i =0; i< m_nDinnerVeg; ) {
			int index = (int)(Math.random()*m_DataVeg.size());
			if (!recoder.contains(index + m_DataMeet.size())) {
				tmpDn.add(index + m_DataMeet.size());
				recoder.add(index + m_DataMeet.size());
				++i;
			}
		}
		ret.add(tmpDn);
		
		List<Integer> tmpFi = new ArrayList<Integer>();
		for (int i =0; i< m_nFruit; ++i) {
			int index = (int)(Math.random()*m_DataFruit.size());
			tmpFi.add(index + m_DataMeet.size()+ m_DataVeg.size());
		}
		ret.add(tmpFi);
		
		DietPlanIndividual tmp = new DietPlanIndividual();
		tmp.setGenes(ret);
		return tmp;
	}
	
	// ��ȡ�������Ӧ��
	public boolean getFitness(DietPlanIndividual indiv) {
		// ���� �����͵�������Ϊ��Ӧֵ
		double fitPower = getTotalPower(indiv);
		fitPower = getAbsoluteValue((fitPower-2400)/2400*100);
		
		double fitCarbohydrate= getTotalCarbohydrate(indiv);
		fitCarbohydrate = getAbsoluteValue((fitCarbohydrate-300)/300*100);
		
		double fitProtein = getTotalProtein(indiv);
		fitProtein = getAbsoluteValue((fitProtein-60)/60*100);
		indiv.setFitness(fitPower*0.6 + fitProtein*0.3 + fitCarbohydrate*0.1);
		return true;
	}
	
	// �ҳ����Ʋ�Ʒ  ��ʱ�򵥴���
	public int getSimilar(int iSrc) {
		
		if(iSrc>= m_DataMeet.size()+ m_DataVeg.size()){
			int index = (int) (Math.random()*m_DataFruit.size());
			return index + m_DataMeet.size()+m_DataVeg.size();
		}else if (iSrc>= m_DataMeet.size()) {
			int index = (int) (Math.random()*m_DataVeg.size());
			return index + m_DataMeet.size();
		} else {
			int index = (int) (Math.random()*m_DataMeet.size());
			return index;
		}
	}
	
	// ��ȡ�������ݵ���Ӧֵ
	public boolean getFitnessAll(DietPlanPopulation pop) {
		List<DietPlanIndividual> list = pop.getIndividualAll();
		for (int i =0; i<list.size(); ++i)
			getFitness(list.get(i));
		return true;
	}
	
	public double getMaxFitness(List<DietPlanIndividual> list) {
		double ret = list.get(0).getFitness();
		for (int i =0; i<list.size(); ++i)
			if (ret > list.get(i).getFitness())
				ret = list.get(i).getFitness();
		return ret;
	}
	
	// ����ĳ����Ʒ�������ܺ�
	public double getTotalPower(DietPlanIndividual indiv) {
		double ret =0;
		for (int i =0; i<indiv.getGenes().size(); ++i) {
			for (int j=0; j< indiv.getGenes().get(i).size(); ++j) {
				int in = indiv.getGenes().get(i).get(j);
				if(in>= m_DataMeet.size()+ m_DataVeg.size()){
					ret += m_DataFruit.get(in-m_DataMeet.size()-m_DataVeg.size()).getPower()*m_fFruitKg*4.184;
				} else if (in>= m_DataMeet.size()) {
					ret  += m_DataVeg.get(in-m_DataMeet.size()).getPower()* m_fVegKg[i]*4.184;
				} else 
					ret += m_DataMeet.get(in).getPower()* m_fMeetKg[i]*4.184;
			}	
		}
		return ret;
	}
	
	// ����ĳ����Ʒ�������ܺ�
		public double getTotalCarbohydrate(DietPlanIndividual indiv) {
			double ret =0;
			for (int i =0; i<indiv.getGenes().size(); ++i) {
				for (int j=0; j< indiv.getGenes().get(i).size(); ++j) {
					int in = indiv.getGenes().get(i).get(j);
					if(in>= m_DataMeet.size()+ m_DataVeg.size()){
						ret += m_DataFruit.get(in-m_DataMeet.size()-m_DataVeg.size()).getCarbohydrate()*m_fFruitKg;
					} else if (in>= m_DataMeet.size()) {
						ret  += m_DataVeg.get(in-m_DataMeet.size()).getCarbohydrate()* m_fVegKg[i];
					} else 
						ret += m_DataMeet.get(in).getCarbohydrate()* m_fMeetKg[i];
				}	
			}
			return ret;
		}
	
	// ������׵ĵ����ʺ���
	public double getTotalProtein(DietPlanIndividual indiv) {
		double ret =0;
		for (int i =0; i<indiv.getGenes().size(); ++i) {
			for (int j=0; j< indiv.getGenes().get(i).size(); ++j) {
				int in = indiv.getGenes().get(i).get(j);
				if(in>= m_DataMeet.size()+ m_DataVeg.size()){
					ret += m_DataFruit.get(in-m_DataMeet.size()-m_DataVeg.size()).getProtein()*m_fFruitKg;
				} else if (in>= m_DataMeet.size()) {
					ret  += m_DataVeg.get(in-m_DataMeet.size()).getProtein()* m_fVegKg[i];
				} else 
					ret += m_DataMeet.get(in).getProtein()* m_fMeetKg[i];
			}	
		}
		return ret;
	}
	
	public String toString(DietPlanIndividual indiv) {
		String ret = "";
		for (int i =0; i< indiv.getGenes().size(); ++i) {
			for (int j =0; j< indiv.getGenes().get(i).size(); ++j) {
				int index = indiv.getGenes().get(i).get(j);
				if(index>= m_DataMeet.size()+ m_DataVeg.size()){
					ret += m_DataFruit.get(index-m_DataMeet.size()-m_DataVeg.size()).getName();
				}else if (index >= m_DataMeet.size()) {
					ret += m_DataVeg.get(index- m_DataMeet.size()).getName(); 
				}else 
					ret += m_DataMeet.get(index).getName();
				ret += " ";
			}
			ret +=" , ";
		}
		return ret;
	}
	
	// �ж�����Ⱦɫ���Ƿ������  ��ֹ���׽���
	public boolean isIndividualSimilar(DietPlanIndividual indiv1, DietPlanIndividual indiv2) {
		// �ж��Ƿ��г���һ��Ļ�������
		int smlSize =0, totalSize=0; // ���ƴ�С ���ܴ�С
		for (int i =0; i< indiv1.getGenes().size(); ++i) {
			for (int j=0; j< indiv1.getGenes().get(i).size(); ++j) {
				if (indiv1.getGenes().get(i).get(j) == indiv2.getGenes().get(i).get(j)) {
					smlSize++;
				}
				totalSize++;
			}
		}
		if (totalSize/2< smlSize)
			return true;
		return false;
	}
	
	private double getAbsoluteValue(double pre) {
		return pre>0? pre: -1*pre;
	}
	
}
