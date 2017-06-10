package com.cloudlife.sport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import com.cloudlife.sport.Individual.Sport;

/**
 * @brief 遗传算法核心类
 *   运动类推荐主要考虑以下几点:
 *   1. 卡路里计算   2.运动时长  3.用户的工作性质   4.天气（室内 室外运动）
 *   5. 温度  6. 是否考虑城市 （比如北京雾霾城市。。）
 *   决策变量： 1.运动类型 2. 运动时长
 *   约束条件：1.运动消耗的卡路里  2. 运动总时长
 * @author wuyi
 *
 */
public class Ga {
	
	private double m_fCrossRate = 0.70; // 交叉概率
	private double m_fMutationRate = 0.05;// 变异概率
	private int m_nGen = 300; // 进化的代数
	
	/**
	 * @brief 对外开放的进化函数
	 * @param pop 种群
	 * @param model 用户的数据模型
	 */
	@SuppressWarnings("unchecked")
	public String startRevolution(Population pop, UserModel model) {
		int iGen =0;
		MyFitnessCmp myFitnessCmp = new MyFitnessCmp();
		
		while (iGen< m_nGen) {
			System.out.println("正在执行第"+iGen+"代遗传变异");
			++iGen;
			evaluate(pop, model);
			Collections.sort(pop.getIndivAll(), myFitnessCmp);
			overcross(pop);
			mutate(pop);
		}
		DataFactory fac = DataFactory.getInstance();
		evaluate(pop, model);
		Collections.sort(pop.getIndivAll(), myFitnessCmp);
		
		JSONObject jsonRet = new JSONObject();
		if (true) {
			float calorie_total =0;
			List<SportData> list = fac.getDecode(pop.getIndiv(0));
			for (int j=0; j< list.size(); ++j) {
				JSONObject jsonTmp = new JSONObject();
				jsonTmp.put("name", list.get(j).getName());
				jsonTmp.put("time", list.get(j).getTime());
				jsonTmp.put("img", "");
				float calorie = model.getWeight()*list.get(j).getTime()
						*list.get(j).getK()/60;
				calorie_total += calorie;
				jsonTmp.put("calorie", Float.toString(calorie));
				if (list.get(j).getName().equals("跑步")) {
					jsonTmp.put("speed", "1m/s");
				}
				jsonRet.accumulate("sport", jsonTmp);
			}
			jsonRet.put("des_calorie", model.getTotalPower());
			jsonRet.put("rmd_calorie", Float.toString(calorie_total));
		}
		
		System.out.println("打印结果：");
		for (int i =0; i<pop.size(); ++i) {
			
			float totalPower = 0;
			for (int j =0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				Sport sport = pop.getIndiv(i).getGenes().get(j);
				totalPower += model.getWeight()*sport.time*fac.getK(sport.index)/60;
			}
			String tmp = "";
			List<SportData> list = fac.getDecode(pop.getIndiv(i));
			for (int j=0; j< list.size(); ++j) {
				tmp += list.get(j).getName()+" "+list.get(j).getTime()+"分  ";
			}
			System.out.println("方案"+i+": "+tmp+"  totalPower:"+ totalPower);
		}
		return jsonRet.toString();
	}
	
	/**
	 * @brief 运动方案评估函数  计算适应度
	 * @param pop 种群
	 * @param model 用户的数据模型
	 * @return
	 */
	private boolean evaluate(Population pop, UserModel model) {
		
		// 先计算各个个体的适应度
		/**
		 * 适应度计算方式：
		 * 		计算跟 标准热量的偏差值
		 */
		DataFactory fac = DataFactory.getInstance();
		for (int i =0; i< pop.size(); ++i) {
			float totalPower = 0;
			for (int j =0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				Sport sport = pop.getIndiv(i).getGenes().get(j);
				totalPower += model.getWeight()*sport.time*fac.getK(sport.index)/60;
			}
			float fitness = totalPower -model.getTotalPower();
			fitness = fitness>=0? fitness:-1*fitness;
			pop.getIndiv(i).setFitness(fitness);
		}
		
		return true;
	}
	
	/**
	 * @brief 交叉算子
	 * @param pop
	 */
	private void overcross(Population pop) {
		for (int i = 0; i< pop.size()-1; i+=2) {
			for (int j=0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				if (Math.random()<m_fCrossRate) {
					int tmp = pop.getIndiv(i).getGenes().get(j).index;
					pop.getIndiv(i).getGenes().get(j).index = pop.getIndiv(i+1).getGenes().get(j).index;
					pop.getIndiv(i+1).getGenes().get(j).index = tmp;
				}
			}
		}
	}
	
	/**
	 * @brief 变异算子
	 * @param pop
	 */
	private void mutate(Population pop) {
		DataFactory fac = DataFactory.getInstance();
		for (int i = 0; i< pop.size()-1; ++i) {
			for (int j=0; j< pop.getIndiv(i).getGenesSize(); ++j) {
				if (Math.random() <m_fMutationRate) {
					int index = fac.getRandomIndex();
					if (!pop.getIndiv(i).isContain(index))
						pop.getIndiv(i).getGenes().get(j).index = index;	
				}
			}
		}
	}
	
	public class MyFitnessCmp implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			Individual indiv1 = (Individual) o1;
			Individual indiv2 = (Individual) o2;
			int ret = new Float(indiv1.getFitness()).
					compareTo(new Float(indiv2.getFitness()));
			return ret;
		}
	}
	
}
















