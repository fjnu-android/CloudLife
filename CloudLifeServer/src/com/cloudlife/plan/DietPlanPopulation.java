package com.cloudlife.plan;

import java.util.ArrayList;
import java.util.List;


/**
 * @brief �����Ŵ��㷨����ʳ�Ƽ��㷨------��Ⱥ��
 * 
 * @author wuyi
 *
 */

public class DietPlanPopulation {

	private List<DietPlanIndividual> m_Individual = new ArrayList<DietPlanIndividual>();
	private DietPlanDataFactory m_DataFactory ; // ���ݴ�����
	// ��ʼ����Ⱥ����  ����ΪĬ�ϵ���Ⱥ��ʼ����С
	public DietPlanPopulation(int defalutSize) {
		m_DataFactory = DietPlanDataFactory.getInstance();
		System.out.println("��ʼ����Ⱥ��ʼ��");
		for (int i =0; i< defalutSize;) {
			DietPlanIndividual newIndiv = m_DataFactory.produceIndividual();
			for (int j=0;j< i; ++j) {
				if (m_Individual.get(j).equals(newIndiv))
					continue;
			}
			m_Individual.add(newIndiv);
			++i;
		}
		System.out.println("��ʼ����Ⱥ������");
	}
	
	public DietPlanPopulation( ) {
	
	}
	
	public List<DietPlanIndividual> getIndividualAll() {
		return m_Individual;
	}
	
	public DietPlanIndividual getIndividual(int i) {
		if (i> m_Individual.size()-1)
			return null;
		return m_Individual.get(i);
	}
	
	public DietPlanIndividual getFittest() {
		DietPlanIndividual fittest = m_Individual.get(0);
		for (int i=0; i< m_Individual.size(); ++i) {
			if (fittest.getFitness() > m_Individual.get(i).getFitness() && m_Individual.get(i).getFitness()!=0)
				fittest = m_Individual.get(i);
		}
		return fittest;
	}
	
	public int size() {
		return m_Individual.size();
	}
	
	public boolean changeIndividual(List<DietPlanIndividual> list) {
		m_Individual = list;
		return true;
	}
	
	public boolean saveIndividual(DietPlanIndividual indiv) {
		this.m_Individual.add(indiv);
		return true;
	}
}














