package com.cloudlife.resource;

import java.io.Serializable;

public class PublicData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	static public int gHasDeal = 0;
	static public int gNeedDeal = 0;
	static public String gLogError = "";
	
	private int needDeal = 0;
	private int hasDeal = 0;
	
	private String logError;
	
	public PublicData () {
		
	}

	public int getHasDeal() {
		return gHasDeal;
	}
	
	public void setHasDeal(int n) {
		hasDeal = n;
	}
	
	public void setLogError(String s) {
		logError = s;
	}
	
	public String getLogError( ) {
		return gLogError;
	}
	
    public void setNeedDeal(int n) {
		needDeal = n;
	}
	
    public int getNeedDeal() {
		return gNeedDeal;
	}

}
