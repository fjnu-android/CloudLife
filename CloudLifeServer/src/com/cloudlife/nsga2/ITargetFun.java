package com.cloudlife.nsga2;

/**
 * @brief Ŀ�꺯���ӿ����
 * @author wuyi
 *
 */
public interface ITargetFun {
	float fun(Individual indiv);
	float getUserNeed();
}
