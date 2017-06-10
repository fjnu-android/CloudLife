package com.cloudlife.sport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.sport.Individual.Sport;

/**
 * @brief �Ŵ��㷨������
 *   �˶����Ƽ���Ҫ�������¼���:
 *   1. ��·�����   2.�˶�ʱ��  3.�û��Ĺ�������   4.���������� �����˶���
 *   5. �¶�  6. �Ƿ��ǳ��� �����籱���������С�����
 *   ���߱����� 1.�˶����� 2. �˶�ʱ��
 *   Լ��������1.�˶����ĵĿ�·��  2. �˶���ʱ��
 * @author wuyi
 *
 */
public class Ga {
	
	private double m_fCrossRate = 0.70; // �������
	private double m_fMutationRate = 0.05;// �������
	private int m_nGen = 300; // �����Ĵ���
	
	/**
	 * @brief ���⿪�ŵĽ�������
	 * @param pop ��Ⱥ
	 * @param model �û�������ģ��
	 */
	@SuppressWarnings("unchecked")
	public String startRevolution(Population pop, UserModel model) {
		int iGen =0;
		MyFitnessCmp myFitnessCmp = new MyFitnessCmp();
		
		while (iGen< m_nGen) {
			System.out.println("����ִ�е�"+iGen+"���Ŵ�����");
			++iGen;
			evaluate(pop, model);
			Collections.sort(pop.getIndivAll(), myFitnessCmp);
			overcross(pop);
			mutate(pop);
		}
		DataFactory fac = DataFactory.getInstance();
		evaluate(pop, model);
		Collections.sort(pop.getIndivAll(), myFitnessCmp);
		
		JSONObject jsonRet = new JSONObject();
		if (true) {
			float calorie_total =0;
			List<SportData> list = fac.getDecode(pop.getIndiv(0));
			for (int j=0; j< list.size(); ++j) {
				JSONObject jsonTmp = new JSONObject();
				jsonTmp.put("name", list.get(j).getName());
				jsonTmp.put("time", list.get(j).getTime());
				jsonTmp.put("img", "");
				float calorie = model.getWeight()*list.get(j).getTime()
						*list.get(j).getK()/60;
				calorie_total += calorie;
				jsonTmp.put("calorie", Float.toString(calorie));
				if (list.get(j).getName().equals("�ܲ�")) {
					jsonTmp.put("speed", "1m/s");
				}
				jsonRet.accumulate("sport", jsonTmp);
			}
			jsonRet.put("des_calorie", model.getTotalPower());
			jsonRet.put("rmd_calorie", Float.toString(calorie_total));
		}
		
		System.out.println("��ӡ�����");
		for (int i =0; i<pop.size(); ++i) {
			
			float totalPower = 0;
			for (int j =0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				Sport sport = pop.getIndiv(i).getGenes().get(j);
				totalPower += model.getWeight()*sport.time*fac.getK(sport.index)/60;
			}
			String tmp = "";
			List<SportData> list = fac.getDecode(pop.getIndiv(i));
			for (int j=0; j< list.size(); ++j) {
				tmp += list.get(j).getName()+" "+list.get(j).getTime()+"��  ";
			}
			System.out.println("����"+i+": "+tmp+"  totalPower:"+ totalPower);
		}
		return jsonRet.toString();
	}
	
	/**
	 * @brief �˶�������������  ������Ӧ��
	 * @param pop ��Ⱥ
	 * @param model �û�������ģ��
	 * @return
	 */
	private boolean evaluate(Population pop, UserModel model) {
		
		// �ȼ�������������Ӧ��
		/**
		 * ��Ӧ�ȼ��㷽ʽ��
		 * 		����� ��׼������ƫ��ֵ
		 */
		DataFactory fac = DataFactory.getInstance();
		for (int i =0; i< pop.size(); ++i) {
			float totalPower = 0;
			for (int j =0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				Sport sport = pop.getIndiv(i).getGenes().get(j);
				totalPower += model.getWeight()*sport.time*fac.getK(sport.index)/60;
			}
			float fitness = totalPower -model.getTotalPower();
			fitness = fitness>=0? fitness:-1*fitness;
			pop.getIndiv(i).setFitness(fitness);
		}
		
		return true;
	}
	
	/**
	 * @brief ��������
	 * @param pop
	 */
	private void overcross(Population pop) {
		for (int i = 0; i< pop.size()-1; i+=2) {
			for (int j=0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				if (Math.random()<m_fCrossRate) {
					int tmp = pop.getIndiv(i).getGenes().get(j).index;
					pop.getIndiv(i).getGenes().get(j).index = pop.getIndiv(i+1).getGenes().get(j).index;
					pop.getIndiv(i+1).getGenes().get(j).index = tmp;
				}
			}
		}
	}
	
	/**
	 * @brief ��������
	 * @param pop
	 */
	private void mutate(Population pop) {
		DataFactory fac = DataFactory.getInstance();
		for (int i = 0; i< pop.size()-1; ++i) {
			for (int j=0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				if (Math.random() <m_fMutationRate) {
					int index = fac.getRandomIndex();
					if (!pop.getIndiv(i).isContain(index))
						pop.getIndiv(i).getGenes().get(j).index = index;	
				}
			}
		}
	}
	
	public class MyFitnessCmp implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			Individual indiv1 = (Individual) o1;
			Individual indiv2 = (Individual) o2;
			int ret = new Float(indiv1.getFitness()).
					compareTo(new Float(indiv2.getFitness()));
			return ret;
		}
	}
	
}
















