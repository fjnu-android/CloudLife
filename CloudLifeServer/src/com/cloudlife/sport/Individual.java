package com.cloudlife.sport;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief 运动类推荐遗传算法 之个体类
 * 			基因编码方式： 实数编码
 * @author wuyi
 */
public class Individual implements Cloneable{

	static public class Sport{
		public Sport(int index, int time) {
			this.index = index;
			this.time = time;
		}
		public int index;
		public int time;
	}
	
	private List<Sport> m_genes = new ArrayList<Sport>(); // 实数编码
	private float m_fitness;// 适应度
	
	public Individual() {

	}
	
	public boolean modifyTime(int index, int time) {
		if (index >this.m_genes.size()-1 || index<0)
			return false;
		this.m_genes.get(index).time = time;
		return true;
	}
	
	public boolean add(int index, int time) {
		return this.m_genes.add(new Sport(index, time));
	}
	
	public List<Sport> getGenes() {
		return this.m_genes;
	}
	
	public boolean setGen(int index, int value,int time) {
		if (index> m_genes.size()-1)
			return false;
		 this.m_genes.set(index, new Sport(value, time));
		 return true;
	}
	
	public float getFitness() {
		return this.m_fitness;
	}
	
	public int getGenesSize() {
		return this.m_genes.size();
	}
	
	public void setFitness(float fitness) {
		this.m_fitness = fitness;
	}
	
	public boolean isContain(int index) {
		for (int i =0; i< m_genes.size(); ++i) {
			if (m_genes.get(i).index == index)
				return true;
		}
		return false;
	}

}
















