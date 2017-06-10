package com.cloudlife.sport;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief 运动推荐遗传算法之 种群类
 * @author wuyi
 *
 */
public class Population implements Cloneable{

	private List<Individual> m_indivs = new ArrayList<Individual>();
	
	/**
	 * @brief 创建种群
	 * @param nCnt 种群的大小
	 * @param nSportCnt 需要生成几种运动类型
	 * @return
	 */
	public boolean createPop(int nCnt, UserModel model) {
	
		DataFactory fac  = DataFactory.getInstance();
		for (int i =0; i<nCnt; ) {
			Individual indiv = fac.createInitIndividual(model);
			if (m_indivs.contains(indiv)==false) {
				m_indivs.add(indiv);
				++i;
			}
		}
		return true;
	}
	
	public List<Individual> getIndivAll() {
		return this.m_indivs;
	}
	
	public Individual getIndiv(int index) {
		if (index >this.m_indivs.size()-1 || index<0)
			return null;
		return this.m_indivs.get(index);
	}
	
	public int size() {
		return m_indivs.size();
	}
	
}











