package com.app.cloud.Model;

import java.io.Serializable;

/**
 * 菜品模型
 *
 */
public class FoodDish implements Serializable {

	/**
	 * bundle 接口id
	 */
	private static final long serialVersionUID = 1L;

	private String image;// 图片链接
	private String name;// 菜名
	private String bodyType;// 体质类型
	private String introduction;// 菜品介绍
	private String nutrition;// 主要营养
	private String effect;// 食疗和功效
	private String man_suit = "";// 适宜人群
	private String man_unsuit = "";// 禁忌人群
	private String fun = "";// 功效和作用
	private String how_eat = "";// 使用方法
	private String save = "";// 如何保存
	private String how_select = "";// 如何挑选
	private String value;// 营养价值表
	private String collocation_good;// 食物相利表 json数据
	private String collocation_bad;// 食物相克表 json数据

	public FoodDish() {

	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setBodyType(String body) {
		this.bodyType = body;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}

	public String getNutrition() {
		return nutrition;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getEffect() {
		return effect;
	}

	public void setMan_suit(String man_suit) {
		this.man_suit = man_suit;
	}

	public String getMan_suit() {
		return man_suit;
	}

	public void setMan_unsuit(String man_unsuit) {
		this.man_unsuit = man_unsuit;
	}

	public String getMan_unsuit() {
		return man_unsuit;
	}

	public void setHow_eat(String eat) {
		this.how_eat = eat;
	}

	public String getHow_eat() {
		return how_eat;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public String getSave() {
		return save;
	}

	public void setHow_select(String how_select) {
		this.how_select = how_select;
	}

	public String getHow_select() {
		return how_select;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setCollocation_good(String collocation_good) {
		this.collocation_good = collocation_good;
	}

	public String getCollocation_good() {
		return collocation_good;
	}

	public void setCollocation_bad(String collocation_bad) {
		this.collocation_bad = collocation_bad;
	}

	public String getCollocation_bad() {
		return collocation_bad;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public String getFun() {
		return fun;
	}

}
