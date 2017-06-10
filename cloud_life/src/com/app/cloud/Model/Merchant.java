package com.app.cloud.Model;

/**
 * 商家商品模型
 *
 */
public class Merchant {

	public String flag;
	public String name;
	public int num = 0 ;
	
	public Merchant() {

	}
	
	public Merchant(String n,String f) {
		name = n;
		flag = f;
	}
	
	public Merchant(String n,int u) {
		name = n;
		num = u;
	}
}
