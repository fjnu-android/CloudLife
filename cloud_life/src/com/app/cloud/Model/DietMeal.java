package com.app.cloud.Model;

import java.util.ArrayList;

/**
 * 饮食界面expandable列表 group+child
 *
 */
public class DietMeal {

	public String group; // 组名
	ArrayList<FoodMenu> child; // 子数据

	public DietMeal() {
		group = "";
		child = null;
	}

	public DietMeal(String groupName) {
		this.group = groupName;
		this.child = new ArrayList<FoodMenu>();
	}

	public DietMeal(String groupName, ArrayList<FoodMenu> groupChild) {
		this.group = groupName;
		this.child = new ArrayList<FoodMenu>();
		for (int i = 0; i < groupChild.size(); i++)
			child.add(groupChild.get(i));
	}

	// 往小组中添加子数据
	public void add(FoodMenu u) {
		child.add(u);
	}

	// 根据用户对象移除子数据
	public void remove(FoodMenu u) {
		child.remove(u);
	}

	// 根据下标移除子数据
	public void remove(int index) {
		child.remove(index);
	}

	// 子数据的大小
	public int getChildSize() {
		return child.size();
	}

	// 清楚所有子数据
	public void clearAllChild() {
		for (int i = getChildSize() - 1; i >= 0; i--)
			remove(i);
	}

	// 根据下标得到子用户
	public FoodMenu getChild(int index) {
		return child.get(index);
	}

	// get...set...
	public String getGroup() {
		return group;
	}

	public void setGroup(String m_group) {
		this.group = m_group;
	}

	public ArrayList<FoodMenu> getGroupChild() {
		return child;
	}

	public void setGroupChild(ArrayList<FoodMenu> groupChild) {
		this.child = groupChild;
	}

}
