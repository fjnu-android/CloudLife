package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

/**
 * nsga2算法中的种群类
 * 
 * @author wuyi
 *
 */
public class Population implements Cloneable{

	private List<Individual> m_listIndividual = new ArrayList<Individual>();
	
	
	public void produceIndividual(int nCnt, UserModel model) {
		DataFactory fac = DataFactory.getInstance();
		try {
		for (int i =0; i< nCnt;) {
			Individual indiv = fac.getIndividual2(model);
			if (isContain(indiv)== false) {
				m_listIndividual.add(indiv);
				indiv.type = model.getType();
				++i;
			}
		}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	public int size() {
		return this.m_listIndividual.size();
	}
	
	public Individual getIndividual(int i ) {
		if (i>= m_listIndividual.size() || i<0)
			return null;
		return this.m_listIndividual.get(i);
	}
	
	public boolean addIndividual(Individual indiv) {
		Individual indivTmp = (Individual) indiv.clone();
		this.m_listIndividual.add(indivTmp);
		return true;
	}
	
	public boolean addIndividualAll(List<Individual> list) {
		return this.m_listIndividual.addAll(list);
	}

	public List<Individual> getIndividualAll() {
		return this.m_listIndividual;
	}
	
	public boolean addIndividualAll(Population pop) {
		Population popTmp = (Population)pop.clone();
		return this.m_listIndividual.addAll(popTmp.getIndividualAll());
	}
	
	public Individual replaceIndividual(int index, Individual indiv) {
		return m_listIndividual.set(index, indiv);
	}
	
	public boolean isContain(Individual indiv) {
		if (m_listIndividual.isEmpty())
			return false;
		for (int i =0; i< m_listIndividual.size(); ++i) {
			if (m_listIndividual.get(i).equals(indiv))
					return true;
		}
		return false;
	}
	
	@Override
	protected Object clone(){
		Population pop = null;
		try {
			pop = (Population) super.clone();
			pop.m_listIndividual =  new ArrayList<Individual>();
			for (int i =0; i< m_listIndividual.size(); ++i) {
				Individual indiv = (Individual) m_listIndividual.get(i).clone();
				pop.m_listIndividual.add(indiv);
			}
			
		} catch (CloneNotSupportedException  e) {
			e.printStackTrace();
		}
		return pop;
	}
	
}









