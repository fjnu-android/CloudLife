package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.food.DishData;

/**
 * @brief nsga2�㷨���Ĳ���
 * @author wuyi
 *
 */
public class Nsga2 {

	private double m_fCrossRate = 0.70; // �������
	private double m_fMutationRate = 0.15;// �������
	private int m_nGen = 300; // �����Ĵ���
	private List<Integer> m_listRank = new ArrayList<Integer>();
	private MyCongestionCmp m_myCongestionCmp = new MyCongestionCmp();
	/**
	 * @brief ��ʼִ�н���
	 * @param pop ���������Ⱥ
	 * @return �������н��  һ�㷵��true
	 */
	@SuppressWarnings("unchecked")
	public Population startEvolution(Population pop, ITargetFun[] targetFun, UserModel model) {
		
		System.out.println("��ʼִ���Ŵ��㷨==");
		int iGen =1;
		int nSize = pop.size();
		Population popPare = pop, popChild = null;
		DataFactory fac = DataFactory.getInstance();
		while (iGen<= m_nGen) {
				
		//	System.out.println("����ִ�е� "+iGen+"������==");
			deleteAlikeIndividual(popPare); // ɾ����ͬ����
			popPare = fastNoDomainSort(popPare, targetFun);
			popChild = new Population();
			popChild.addIndividualAll(popPare);
			// ���жϵ�ǰ��Ⱥ�ĸ������� <N�����ӵ���� �;�Ӣ����
			if (popPare.size() != nSize) {
				//System.out.println("���ڽ��о�Ӣ����");
				// ѭ������  ֱ��popSize��СΪnSize
				int nCurCnt = 0;
				popChild.getIndividualAll().clear();;
				int i =0;
				for ( ; i< m_listRank.size(); ++i) {
					if (m_listRank.get(i)+nCurCnt<=nSize) {
						for (int j=nCurCnt; j< nCurCnt+m_listRank.get(i); ++j) {
							popChild.addIndividual(popPare.getIndividual(j));
						}
						nCurCnt += m_listRank.get(i);
					} else
						break;
				}
				// ��ʱ ��ͬһ���ȼ��������ӵ�������ӱȽϴ���
				if (popChild.size() < nSize) {
					// ��ӵ��������
					List<Individual> listTmp = popPare.getIndividualAll().subList(nCurCnt, nCurCnt+m_listRank.get(i)-1);
					if (listTmp == null)
						System.out.println("���������ݳ���===ӵ���ȴ�����===");
					else {
						listTmp.get(0).setCongestion(10000000);// α���޴�
						listTmp.get(listTmp.size()-1).setCongestion(10000000);
						for (int j=1; j<listTmp.size()-1;++j) {
							listTmp.get(j).setCongestion(
									calculationIndivCongestion(listTmp.get(j-1), listTmp.get(j+1), targetFun));
						}
						Collections.sort(listTmp,m_myCongestionCmp);
						
						for (int j=0; j< nSize- nCurCnt; ++j) {
							popChild.addIndividual(listTmp.get(j));
						}
					}
				}
				popPare = new Population();
				popPare.addIndividualAll(popChild); // ���������Ӵ�ͳһ��
			} 
			
			// ��ǰ��Ⱥ�����Ѿ�ΪnSize ���Խ��н�������
			// ���� ���촦�� �����Ӵ�popChild
			getCrossoverData(popChild);
			// ���촦��
			for (int i=0; i<popChild.size(); ++i) {
				mutate(popChild.getIndividual(i), model);
			}
			
			// ���Ӵ��͸����ϲ�  ��һ���������� 
			popPare.addIndividualAll(popChild);
			iGen++;
		}
		popPare = fastNoDomainSort(popPare, targetFun);
		deleteAlikeIndividual(popPare); // ɾ����ͬ����
		
		// ����
		MyComplianceRateCmp myComplianceRateCmp = new MyComplianceRateCmp(targetFun);
		Collections.sort(popPare.getIndividualAll(), myComplianceRateCmp);
		// ��ӡ���
		System.out.println("��ӡ���=====");
		for (int i =0; i< popPare.size(); ++i) {
			
			float power =0, protein=0, fat =0;
			// ��������
			Individual indiv = popPare.getIndividual(i);
			indiv.type = model.getType();
			for (int j=0; j< indiv.genesSize(); ++j) {
				for (int k = 0; k< indiv.getGenesDetail(j).size(); ++k) {
					DishData dish = fac.getDecodeData(indiv.getGenesDetail(j).get(k), model.getType());
					float  weight= model.getWeight(j+1, fac.getDataIndexRange(indiv.getGenesDetail(j).get(k), model.getType()));
					power+= dish.getPower()*4.184*weight;
					protein += dish.getProtein()*weight;
					fat += dish.getFat()*weight;
				}
			}
			System.out.println("������"+ indiv.toString()+" ����:"+power+" "+ power/model.getPower()*100 +"% ������:"+ protein+" "+protein/model.getProtein()*100+"% ֬��:"+fat);
			
		}
		
		return popPare;
	}
	
	/**
	 * @brief ����Ⱥ���մ���ʽ�������
	 *  �������  ��Ϊÿ�ζ����������Ŀ�꺯���Ĵ���ʾ���80���ϵ�
	 *  	���Բ����ۼӴ�����ٱȽ�  ���ַ����Ƚ���Ӳ
	 *  		�����ʺϱ�ϵͳ������
	 */

	public class MyComplianceRateCmp implements Comparator{
		
		private ITargetFun[] m_fun;
		public MyComplianceRateCmp(ITargetFun[] fun) {
			m_fun = fun;
		}
		
		@Override
		public int compare(Object o1, Object o2) {
			Individual indiv1 = (Individual) o1;
			Individual indiv2 = (Individual) o2;
			float f1= 0, f2=0;
			for (int i=0; i< m_fun.length; ++i) {
				float tmp1 = (100-m_fun[i].fun(indiv1)/m_fun[i].getUserNeed()*100);
				float tmp2 = (100-m_fun[i].fun(indiv2)/m_fun[i].getUserNeed()*100);
				if (tmp1>-50 && tmp1<0)
					tmp1 = 100+tmp1;
				f1 += tmp1;
				if (tmp2>-50 && tmp2 <0)
					tmp2 = 100+tmp2;
				f2 += tmp2;
			}
			return new Double(f2).compareTo(new Double(f1));
		}
		
	}
	// ���洦���㷨
	private void getCrossoverData(Population pop) {
		// ������� : ��һ�������һ�� �Դ�����
		for (int i =0; i< pop.size()/2; ++i) {
			Individual indiv1 = pop.getIndividual(i);
			Individual indiv2 = pop.getIndividual(pop.size()-i-1);
			for (int j=0; j< indiv1.genesSize(); ++j) {
				for (int k =0; k< indiv1.getGenesDetail(j).size(); ++k) {
					if (Math.random()< m_fCrossRate && indiv1.contain(indiv2.getGenesDetail(j).get(k))== false
							&& indiv2.contain(indiv1.getGenesDetail(j).get(k))== false) {
						int data = indiv1.getGenesDetail(j).get(k);
						indiv1.getGenesDetail(j).set(k, indiv2.getGenesDetail(j).get(k));
						indiv2.getGenesDetail(j).set(k, data);
					}
				}
			}
			pop.replaceIndividual(i, indiv1);
			pop.replaceIndividual(pop.size()-i-1, indiv2);
		}
	}
	
	/**
	 * @brief ����ÿ�������ӵ����
	 * @param indiv1 ��i-1����
	 * @param indiv2��i+1����
	 * @param targetFun Ŀ�꺯������ ������Ƹ����  ������ڷ�װ���㷨
	 * @return ��i�����ӵ����
	 */
	public float calculationIndivCongestion(Individual indiv1,Individual indiv2, ITargetFun[] targetFun) {
		float ret = 0;
		for (int i =0; i< targetFun.length; ++i){
			float tmp = targetFun[i].fun(indiv2) - targetFun[i].fun(indiv1);
			tmp = tmp>0 ? tmp: -1* tmp;
			ret += tmp;
		}
		return ret;
	}
	
	/**
	 * @brief ���ٷ�֧�������㷨  �����㷨
	 * @param popԭ��Ⱥ
	 * @param targetFun Ŀ�꺯������
	 * @return ��������Ⱥ
	 */
	private Population fastNoDomainSort(Population pop, ITargetFun[] targetFun) {
		Population newPop = new Population();
		int nSize = pop.size();
		int nRank =0;
		m_listRank.clear();
		while (newPop.size()<nSize) {
			for (int i =0; i< pop.size(); ++i) {
				pop.getIndividual(i).setDomainCnt(0);
				for (int j=0; j<pop.size(); ++j) {
					if (i==j)
						continue;
					int ret = pareto(pop.getIndividual(i), pop.getIndividual(j), targetFun);
					if (ret == 0)
						pop.getIndividual(i).setDomainCnt(pop.getIndividual(i).getDomainCnt()+1);
				}
			}
			
			int nCnt =0;
			for (int i=0; i<pop.size(); ++i) {
				if (pop.getIndividual(i).getDomainCnt()==0) {
					pop.getIndividual(i).setRank(nRank);
					newPop.addIndividual(pop.getIndividual(i));
					pop.getIndividualAll().remove(i);
					--i;
					++nCnt;
				}
			}
			++nRank;
			m_listRank.add(nCnt);
		//	System.out.println("newPop.size():"+newPop.size()+"popSize:"+ pop.size());
		}
		return newPop;
	}
	
	
	/**
	 * @brief indiv1 ��indiv2�Ƚ�˭֧��˭ ��indiv1֧��indiv2�򷵻�1 
	 * 				��֮Ϊ0  ����Ϊ-1(����֧��)
	 * @param indiv1
	 * @param indiv2
	 * @param targetFun
	 * @return
	 */
	private int pareto(Individual indiv1, Individual indiv2, ITargetFun[] targetFun) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i=0; i< targetFun.length; ++i) {
			float f1 = targetFun[i].fun(indiv1);
			float f2 = targetFun[i].fun(indiv2);
			if (f1>f2)
				list.add(2);
			else if (f1<f2)
				list.add(1);
			else
				list.add(0);
		}
		if (list.contains(1) && !list.contains(2))
			return 1;
		if (list.contains(2) && !list.contains(1))
			return 0;
		return -1;
	}
	
	/**
	 * @brief ��������
	 * @param indiv ������ĸ���
	 */
	private void mutate(Individual indiv, UserModel model) {
		DataFactory fac = DataFactory.getInstance();
		for (int i =0; i< indiv.genesSize(); ++i) {
			for (int j=0; j< indiv.getGenesDetail(i).size(); ++j) {
				if (Math.random()<= m_fMutationRate) {
					int ret = fac.getReplaceDish(indiv.getGenesDetail(i).get(j), model.getType());
					if (indiv.contain(ret)== false)
						indiv.getGenesDetail(i).set(j, ret);
				}
			}
		}
	}
	
	public class MyCongestionCmp implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			Individual indiv1 = (Individual) o1;
			Individual indiv2 = (Individual) o2;
			return new Double(indiv2.getCongestion()).
					compareTo(new Double(indiv1.getCongestion()));
		}
		
	}
	
	private void deleteAlikeIndividual(Population pop) {
		
		for (int i =0; i< pop.size();) {
			for (int j=i+1; j< pop.size(); ++j) {
				if (pop.getIndividual(j).equals(pop.getIndividual(i)) ==true && pop.size()>20) {
					pop.getIndividualAll().remove(i);
					--i;
					break;
				}
			}
			++i;
		}
	}
	
	
}










