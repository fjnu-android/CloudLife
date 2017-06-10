package com.app.cloud.Model;

import java.io.Serializable;

/**
 * 菜谱模型
 *
 */
public class FoodMenu implements Serializable {

	/**
	 * bundle 接口
	 */
	private static final long serialVersionUID = 1L;

	private String image = "";// 图片链接
	private String name;// 菜名
	private String bodyType;// 体质类型
	private String effect;// 食疗和功效
	private String material_main;// 主要食材
	private String material_assist;// 辅佐食材
	private String how_make;// 食材做法
	private String flavor;// 味道

	public FoodMenu() {

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

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getEffect() {
		return effect;
	}

	public void setMaterial_main(String material_main) {
		this.material_main = material_main;
	}

	public String getMaterial_main() {
		return material_main;
	}

	public void setMaterial_assist(String material_assist) {
		this.material_assist = material_assist;
	}

	public String getMaterial_assist() {
		return material_assist;
	}

	public void setHow_make(String how_make) {
		this.how_make = how_make;
	}

	public String getHow_make() {
		return how_make;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public String getFlavor() {
		return flavor;
	}

}
