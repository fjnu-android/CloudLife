package com.cloudlife.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @brief 基于遗传算法改进的食谱推荐算法
 * @author wuyi
 * 
 */
public class DietPlanGA {

	private static final Comparator<? super DietPlanIndividual> MyCompare = null;
	private double uniformRate = 0.7; // 交叉概率
	private double mutationRate = 0.015; // 变异概率
	private int evolutionSize = 300; // 最大进化代数
	private boolean elitism = true; // 开启精英主义
	private int tournamentSize = 5; //淘汰数组的大小

	// 主函数 开始进化算法
	public boolean startEvolution(DietPlanPopulation data) {
		int iGen = 0; // 代数
		DietPlanPopulation prePop = data;
		int size = data.size();
		System.out.println("开始执行遗传算法=====");
		DietPlanDataFactory fac = DietPlanDataFactory.getInstance();
		while (iGen++< evolutionSize) {
			DietPlanPopulation newPop = new DietPlanPopulation();
			// 获取适应度
			fac.getFitnessAll(prePop);
			if (evaluate(prePop) == true && iGen>evolutionSize/2)
				break;
			// 精英选择
			int offsetIndex = 0;
			if (elitism) {
				newPop.saveIndividual(prePop.getFittest());
				offsetIndex =1;
			}
			// 交叉处理
			System.out.println("正在交叉处理");
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
			// 变异处理
			System.out.println("正在变异处理");
			for (int i = offsetIndex; i<size; ++i) {
				mutate(newPop.getIndividual(i));
			}
			prePop = newPop;
			System.out.println("正在处理第"+ iGen+"代，请稍等!");
		}

	//	Collections.sort(prePop.getIndividualAll(), MyCompare);
		sort(prePop);	
		
		// 打印结果
		for (int i=0; i< size; ++i) {
			System.out.println("第"+ i+"个餐饮搭配结果: "+fac.toString(prePop.getIndividual(i))+
					" 能量值为: "+ (float)(fac.getTotalPower(prePop.getIndividual(i))) + " 达标率为:"+(float)( fac.getTotalPower(prePop.getIndividual(i))/24)+"%"
					+" 蛋白质值为: " + (float)(fac.getTotalProtein(prePop.getIndividual(i)))+ "达标率为:"+ (float)(fac.getTotalProtein(prePop.getIndividual(i))/0.6)+"%"
					+" 碳水化合物值为: " + (float)(fac.getTotalCarbohydrate(prePop.getIndividual(i)))+ "达标率为:"+ (float)(fac.getTotalCarbohydrate(prePop.getIndividual(i))/3)+"%");
		}
		
		return true;
	}
	
	// 评估函数  判断进化的结果
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
	
	// 获取平均适应值  防止过早收敛
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
	
	// 将两个个体进行交叉处理
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
	
	// 变异处理
	private void mutate(DietPlanIndividual indiv) {
		
		// 变异结果前提： 需要替换的基因跟原基因类似  差距不大
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
	// 选择处理
	private void select(DietPlanPopulation savePop, DietPlanPopulation prePop) {
		// 先处理精英
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
	
	
	// 比较器
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
			// 精英不参与
			if (pop.getIndividual(index).equals(pop.getFittest())) {
				continue;
			}
			tournamentPop.saveIndividual(pop.getIndividual(index));
			++i;
		}
		return tournamentPop.getFittest();
	}
	
}
