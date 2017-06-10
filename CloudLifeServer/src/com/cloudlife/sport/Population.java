package com.cloudlife.sport;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief �˶��Ƽ��Ŵ��㷨֮ ��Ⱥ��
 * @author wuyi
 *
 */
public class Population implements Cloneable{

	private List<Individual> m_indivs = new ArrayList<Individual>();
	
	/**
	 * @brief ������Ⱥ
	 * @param nCnt ��Ⱥ�Ĵ�С
	 * @param nSportCnt ��Ҫ���ɼ����˶�����
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











