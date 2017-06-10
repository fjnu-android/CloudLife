package com.cloudlife.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @brief �����Ŵ��㷨�Ľ���ʳ���Ƽ��㷨
 * @author wuyi
 * 
 */
public class DietPlanGA {

	private static final Comparator<? super DietPlanIndividual> MyCompare = null;
	private double uniformRate = 0.7; // �������
	private double mutationRate = 0.015; // �������
	private int evolutionSize = 300; // ����������
	private boolean elitism = true; // ������Ӣ����
	private int tournamentSize = 5; //��̭����Ĵ�С

	// ������ ��ʼ�����㷨
	public boolean startEvolution(DietPlanPopulation data) {
		int iGen = 0; // ����
		DietPlanPopulation prePop = data;
		int size = data.size();
		System.out.println("��ʼִ���Ŵ��㷨=====");
		DietPlanDataFactory fac = DietPlanDataFactory.getInstance();
		while (iGen++< evolutionSize) {
			DietPlanPopulation newPop = new DietPlanPopulation();
			// ��ȡ��Ӧ��
			fac.getFitnessAll(prePop);
			if (evaluate(prePop) == true && iGen>evolutionSize/2)
				break;
			// ��Ӣѡ��
			int offsetIndex = 0;
			if (elitism) {
				newPop.saveIndividual(prePop.getFittest());
				offsetIndex =1;
			}
			// ���洦��
			System.out.println("���ڽ��洦��");
			for (int i = offsetIndex; i<size; ) {
				DietPlanIndividual indiv1 = tournamentSelection(prePop);
				DietPlanIndividual indiv2 = tournamentSelection(prePop);
				DietPlanIndividual newIndiv = crossover(indiv1, indiv2);
				int flag =0;
				for (int j =0; j< newPop.size(); ++j)
					if (newPop.getIndividual(j).equals(newIndiv)) {
						flag= 1;
						break;
					}
				if (flag == 0) {
					newPop.saveIndividual(newIndiv);
					++i;
				}
			}
			// ���촦��
			System.out.println("���ڱ��촦��");
			for (int i = offsetIndex; i<size; ++i) {
				mutate(newPop.getIndividual(i));
			}
			prePop = newPop;
			System.out.println("���ڴ����"+ iGen+"�������Ե�!");
		}

	//	Collections.sort(prePop.getIndividualAll(), MyCompare);
		sort(prePop);	
		
		// ��ӡ���
		for (int i=0; i< size; ++i) {
			System.out.println("��"+ i+"������������: "+fac.toString(prePop.getIndividual(i))+
					" ����ֵΪ: "+ (float)(fac.getTotalPower(prePop.getIndividual(i))) + " �����Ϊ:"+(float)( fac.getTotalPower(prePop.getIndividual(i))/24)+"%"
					+" ������ֵΪ: " + (float)(fac.getTotalProtein(prePop.getIndividual(i)))+ "�����Ϊ:"+ (float)(fac.getTotalProtein(prePop.getIndividual(i))/0.6)+"%"
					+" ̼ˮ������ֵΪ: " + (float)(fac.getTotalCarbohydrate(prePop.getIndividual(i)))+ "�����Ϊ:"+ (float)(fac.getTotalCarbohydrate(prePop.getIndividual(i))/3)+"%");
		}
		
		return true;
	}
	
	// ��������  �жϽ����Ľ��
	private boolean evaluate(DietPlanPopulation pop) {
		if (pop.getIndividualAll().isEmpty())
			return false;
		double ave = getEverageFitness(pop);
		double dst = ave- pop.getFittest().getFitness();
		dst = dst>0?dst: -1*dst;
		System.out.println("best fitness:"+ pop.getFittest().getFitness()
				+"  ave: "+ ave);
		if (5.0>pop.getFittest().getFitness() && pop.getFittest().getFitness()>0.01
				&& dst<10) 
			return true;
		return false;
	}
	
	// ��ȡƽ����Ӧֵ  ��ֹ��������
	private double getEverageFitness(DietPlanPopulation pop) {
		double ret =0;
		for (int i =0; i< pop.size(); ++i) {
			if (pop.getIndividual(i).getFitness() == 0)
				return 100;
			ret += pop.getIndividual(i).getFitness();
		}
		ret = ret/ pop.size();
		return ret;
	}
	
	// ������������н��洦��
	private DietPlanIndividual crossover(DietPlanIndividual indiv1, DietPlanIndividual indiv2) {
		DietPlanIndividual newIndiv = new DietPlanIndividual();
		List<List<Integer>> tmpData = new ArrayList<List<Integer>>();
		for (int i =0; i< indiv1.getGenes().size(); ++i) {
			List<Integer> tmp = new ArrayList<Integer>();
			for (int j =0; j< indiv1.getGenes().get(i).size(); ++j) {
				if (Math.random()<uniformRate && isIndividualInclude(indiv1.getGenes(), 
							indiv2.getGenes().get(i).get(j))== false) {
					tmp.add(indiv2.getGenes().get(i).get(j));
				} else
					tmp.add(indiv1.getGenes().get(i).get(j));
			}
			tmpData.add(tmp);
		}
		newIndiv.setGenes(tmpData);
		return newIndiv;
	}
	
	// ���촦��
	private void mutate(DietPlanIndividual indiv) {
		
		// ������ǰ�᣺ ��Ҫ�滻�Ļ����ԭ��������  ��಻��
		DietPlanDataFactory fac= DietPlanDataFactory.getInstance();
		for (int i =0; i< indiv.getGenes().size(); ++i) {
			for (int j =0; j< indiv.getGenes().get(i).size(); ++j) {
				if (Math.random()< mutationRate ) {
					int tmp = fac.getSimilar(indiv.getGenes().get(i).get(j));
					if(true==isIndividualInclude(indiv.getGenes(), tmp))
						continue;
					indiv.getGenes().get(i).set(j, tmp);
				}
			}
		}
	}
	
	/*
	// ѡ����
	private void select(DietPlanPopulation savePop, DietPlanPopulation prePop) {
		// �ȴ���Ӣ
	//	Collections.sort(prePop.getIndividualAll(), MyCompare);
		sort(prePop);
		for (int i =0; i< (int)(prePop.getIndividualAll().size() *elitismProp); ++i) {
			savePop.getIndividualAll().add(prePop.getIndividualAll().get(i));
		}
	}*/
	
	// 
	private void sort(DietPlanPopulation pop) {
		DietPlanIndividual indiv;
		List<DietPlanIndividual> data = pop.getIndividualAll();
		for (int i=0; i< pop.size(); ++i) {
			for (int j = i+1; j< pop.size(); ++j) {
				if (data.get(i).getFitness() > data.get(j).getFitness()) {
					indiv = data.get(i);
					pop.getIndividualAll().set(i, data.get(j));
					pop.getIndividualAll().set(j, indiv);
				}
			}
		}
	}
	
	
	// �Ƚ���
	class MyCompare implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			DietPlanIndividual indiv1 = (DietPlanIndividual) o1;
			DietPlanIndividual indiv2 = (DietPlanIndividual) o2;
			return (int)(indiv1.getFitness()-indiv2.getFitness());
		}
	}
	
	private boolean isIndividualInclude(List<List<Integer>> list, int cmp) {
		for (int i =0; i< list.size(); ++i)  {
			for (int j=0; j< list.get(i).size(); ++j) {
				if (list.get(i).get(j) ==cmp)
					return true;
			}
		}
		return false;
	}
	
	private DietPlanIndividual tournamentSelection(DietPlanPopulation pop) {
		DietPlanPopulation tournamentPop = new DietPlanPopulation();
		for (int i =0; i< tournamentSize; ) {
			int index = (int) (Math.random()* pop.size());
			// ��Ӣ������
			if (pop.getIndividual(index).equals(pop.getFittest())) {
				continue;
			}
			tournamentPop.saveIndividual(pop.getIndividual(index));
			++i;
		}
		return tournamentPop.getFittest();
	}
	
}
