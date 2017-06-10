package com.cloudlife.nsga2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.food.DishData;

/**
 * @brief nsga2算法核心部分
 * @author wuyi
 *
 */
public class Nsga2 {

	private double m_fCrossRate = 0.70; // 交叉概率
	private double m_fMutationRate = 0.15;// 变异概率
	private int m_nGen = 300; // 进化的代数
	private List<Integer> m_listRank = new ArrayList<Integer>();
	private MyCongestionCmp m_myCongestionCmp = new MyCongestionCmp();
	/**
	 * @brief 开始执行进化
	 * @param pop 待处理的种群
	 * @return 代码运行结果  一般返回true
	 */
	@SuppressWarnings("unchecked")
	public Population startEvolution(Population pop, ITargetFun[] targetFun, UserModel model) {
		
		System.out.println("开始执行遗传算法==");
		int iGen =1;
		int nSize = pop.size();
		Population popPare = pop, popChild = null;
		DataFactory fac = DataFactory.getInstance();
		while (iGen<= m_nGen) {
				
		//	System.out.println("正在执行第 "+iGen+"代进化==");
			deleteAlikeIndividual(popPare); // 删除相同个体
			popPare = fastNoDomainSort(popPare, targetFun);
			popChild = new Population();
			popChild.addIndividualAll(popPare);
			// 先判断当前种群的个体数量 <N则进行拥挤度 和精英策略
			if (popPare.size() != nSize) {
				//System.out.println("正在进行精英策略");
				// 循环处理  直到popSize大小为nSize
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
				// 此时 在同一个等级里面进行拥挤度算子比较处理
				if (popChild.size() < nSize) {
					// 按拥挤度排序
					List<Individual> listTmp = popPare.getIndividualAll().subList(nCurCnt, nCurCnt+m_listRank.get(i)-1);
					if (listTmp == null)
						System.out.println("待处理数据出错===拥挤度待计算===");
					else {
						listTmp.get(0).setCongestion(10000000);// 伪无限大
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
				popPare.addIndividualAll(popChild); // 将父代和子代统一化
			} 
			
			// 当前种群个数已经为nSize 可以进行进化处理
			// 交叉 变异处理 产生子代popChild
			getCrossoverData(popChild);
			// 变异处理
			for (int i=0; i<popChild.size(); ++i) {
				mutate(popChild.getIndividual(i), model);
			}
			
			// 将子代和父代合并  下一轮重新排序 
			popPare.addIndividualAll(popChild);
			iGen++;
		}
		popPare = fastNoDomainSort(popPare, targetFun);
		deleteAlikeIndividual(popPare); // 删除相同个体
		
		// 排序
		MyComplianceRateCmp myComplianceRateCmp = new MyComplianceRateCmp(targetFun);
		Collections.sort(popPare.getIndividualAll(), myComplianceRateCmp);
		// 打印结果
		System.out.println("打印结果=====");
		for (int i =0; i< popPare.size(); ++i) {
			
			float power =0, protein=0, fat =0;
			// 三餐数据
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
			System.out.println("方案；"+ indiv.toString()+" 热量:"+power+" "+ power/model.getPower()*100 +"% 蛋白质:"+ protein+" "+protein/model.getProtein()*100+"% 脂肪:"+fat);
			
		}
		
		return popPare;
	}
	
	/**
	 * @brief 将种群按照达标率进行排序
	 *  排序策略  因为每次都会存在所有目标函数的达标率均在80以上的
	 *  	所以采用累加达标率再比较  这种方法比较生硬
	 *  		但是适合本系统的排序
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
	// 交叉处理算法
	private void getCrossoverData(Population pop) {
		// 交叉策略 : 第一个和最后一个 以此类推
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
	 * @brief 计算每个个体的拥挤度
	 * @param indiv1 第i-1个体
	 * @param indiv2第i+1个体
	 * @param targetFun 目标函数数组 这样设计更灵活  方便后期封装此算法
	 * @return 第i个体的拥挤度
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
	 * @brief 快速非支配排序算法  核心算法
	 * @param pop原种群
	 * @param targetFun 目标函数数组
	 * @return 排序后的种群
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
	 * @brief indiv1 和indiv2比较谁支配谁 若indiv1支配indiv2则返回1 
	 * 				反之为0  否则为-1(互不支配)
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
	 * @brief 变异算子
	 * @param indiv 待处理的个体
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










