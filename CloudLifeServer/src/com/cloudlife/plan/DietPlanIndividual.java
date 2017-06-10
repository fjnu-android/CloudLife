package com.cloudlife.plan;

import java.util.List;

/**
 * @brief �Ŵ��㷨�еĸ����ࣨȾɫ�壩
 * 		˵���� �ж��ֲ�Ʒ��������ϳ�һ��Ĳ���
 * 		���뷽ʽ�� ����������
 * 		����ֻ�ṩ�����ӿ�  ���е����ݾ��ڹ�������������
 * @author wuyi
 *
 */
public class DietPlanIndividual {
	
	// �������Ӧֵ
	private double fitness = 0;
	// ����ı���
	List<List<Integer>> genes = null;
	
	public double getFitness() {
		return fitness;
	}
	
	@Override
	public boolean equals(Object obj) {
		DietPlanIndividual indiv = (DietPlanIndividual)obj;
		for (int i =0; i< genes.size(); ++i) {
			for (int j =0; j< genes.get(i).size(); ++j) {
				if (genes.get(i).get(j) != indiv.genes.get(i).get(j))
					return false;
			}
		}
		return true;
	}
	
	public void setFitness(double fit) {
		this.fitness = fit;
	}
	
	public List<List<Integer>> getGenes() {
		return this.genes;
	}
	
	public void setGenes(List<List<Integer>> genes) {
		this.genes = genes;
	}
	
	@Override
	public String toString() {
		String tmp = "";
		for (int i = 0; i< genes.size(); ++i) {
			for (int j=0; j< genes.get(i).size(); ++j) {
				tmp += genes.get(i).get(j).toString()+"  ";
			}
			tmp += " , ";
		}
		return tmp;
	}
	
}












