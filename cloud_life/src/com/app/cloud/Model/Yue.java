package com.app.cloud.Model;

import java.io.Serializable;

/**
 * 同城约
 */
public class Yue implements Serializable {
	
	/**
	 * bundle 接口
	 */
	private static final long serialVersionUID = 2L;

	public String aid;
	
	public String time;

	public String name;

	public String address;
	
	public String info;

	public double lat;
	public double lng;
	
	public boolean state;

	public String publish_man;
	
	public String join_man = "" ; 

	public Yue() {

	}
}
