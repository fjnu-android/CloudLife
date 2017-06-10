package com.cloudlife.plan;

import java.util.List;

/**
 * @brief 遗传算法中的个体类（染色体）
 * 		说明； 有多种菜品按规则组合成一天的菜谱
 * 		编码方式； 浮点数编码
 * 		本类只提供各个接口  所有的数据均在工厂类里面生成
 * @author wuyi
 *
 */
public class DietPlanIndividual {
	
	// 个体的适应值
	private double fitness = 0;
	// 个体的编码
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












