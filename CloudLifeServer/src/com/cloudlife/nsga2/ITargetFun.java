package com.cloudlife.nsga2;

/**
 * @brief 目标函数接口设计
 * @author wuyi
 *
 */
public interface ITargetFun {
	float fun(Individual indiv);
	float getUserNeed();
}
