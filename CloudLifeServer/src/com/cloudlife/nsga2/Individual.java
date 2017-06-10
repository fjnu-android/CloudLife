package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief nsga2�㷨�еĸ����ࣨ����Ⱦɫ����  ��������
 * @author wuyi
 *
 */

public class Individual implements Cloneable{

	private int m_nRank; // ���ڷֲ�ĵȼ�
	private float m_nCongestion; // ӵ����
	
	private int m_nDomain; // ����Ⱥ��֧�䱾����Ľ� �ĸ��������
	private List<Integer> m_listDomainIndiv = new ArrayList<Integer>(); // ����������֧��Ľ����ļ���
	
	// ��Ȼ���ø���������
	List<List<Integer>> m_Genes; // ������� ÿ�Ͷ�Ӧ�Ĳ�Ʒ�±�
	static public int ore =1;
	public int order =ore;
	
	public Individual() {
		ore++;
	}
	
	public int type = 0;
	
	public void setRank(int rank) {
		this.m_nRank = rank;
	}
	
	public int getRank() {
		return this.m_nRank;
	}
	
	public void setCongestion(float congestion) {
		this.m_nCongestion = congestion;
	}
	
	public float getCongestion() {
		return this.m_nCongestion;
	}
	
	public void setDomainCnt(int nCnt) {
		this.m_nDomain = nCnt;
	}
	
	public int getDomainCnt() {
		return this.m_nDomain;
	}
	
	public void clearDomainColl() {
		this.m_listDomainIndiv.clear();
	}
	
	public void addDomainCollI(int index) {
		this.m_listDomainIndiv.add(index);
	}
	
	public List<Integer> getDomainColl() {
		return this.m_listDomainIndiv;
	}
	
	public List<List<Integer>> getGenes() {
		return this.m_Genes;
	}
	
	public boolean setGenes(List<List<Integer>> genes) {
		this.m_Genes = genes;
		return true;
	}
	
	public int genesSize() {
		return this.m_Genes.size();
	}
	
	public List<Integer> getGenesDetail(int iAspect) {
		if (iAspect >=this.m_Genes.size() || iAspect<0)
			return null;
		return this.m_Genes.get(iAspect);
	}
	
	@Override
	public boolean equals(Object obj) {
		Individual indiv = (Individual) obj;
		int nAlike =0, nCnt =0;
		if (indiv == null)
			return false;
		for (int i =0; i< m_Genes.size(); ++i) {
			for (int j =0; j< m_Genes.get(i).size(); ++j) {
				//int flag =0;
				nCnt++;
				for (int k=0; k< indiv.getGenes().size(); ++k)
					if (indiv.getGenes().get(k).contains(m_Genes.get(i).get(j))) {
					//	flag =1;
						nAlike++;
						break;
					}
			/*	if (flag ==0)
					return false;*/
			}
		}
		if (nCnt*0.7<=nAlike)
		return true;
		return false;
	}
	
	public boolean contain(int index) {
		for (int i =0; i< m_Genes.size(); ++i) {
			if (m_Genes.get(i).contains(index))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		// �򵥴�ӡ����
		String str = "";
		DataFactory fac = DataFactory.getInstance();
		for (int i =0; i< m_Genes.size(); ++i) {
			for (int j =0; j<m_Genes.get(i).size(); ++j) {
				str += fac.getDecodeData(m_Genes.get(i).get(j),type).getName() + " ";
			}
			str +=",";
		}
		return str;
	}
	
	@Override
	protected Object clone() {
		Individual indiv = null;
		try {
			indiv = (Individual) super.clone();
			indiv.m_Genes = new ArrayList<List<Integer>>();
			for (int i =0; i<m_Genes.size(); ++i) {
				List<Integer> tmp = new ArrayList<Integer>();
				for (int j=0;j<m_Genes.get(i).size(); ++j)
					tmp.add(m_Genes.get(i).get(j));
				indiv.m_Genes.add(tmp);
			}
		}catch (CloneNotSupportedException  e) {
			e.printStackTrace();
		}
		return indiv;
	}
}











